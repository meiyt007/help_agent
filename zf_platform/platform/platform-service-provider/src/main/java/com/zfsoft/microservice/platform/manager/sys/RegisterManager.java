package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.reflect.TypeToken;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.microservice.platform.data.vo.RegisterLicense;
import com.zfsoft.microservice.platform.data.vo.RegisterPermission;
import com.zfsoft.microservice.platform.data.vo.ZhuoFanRegister;
import com.zfsoft.microservice.platform.feign.settings.SysConfigFeignService;
import com.zfsoft.microservice.platform.util.IpUtils;
import com.zfsoft.microservice.platform.util.SecurityUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

/**
 * @description:  注册服务
 * @author: wuxx
 * @Date: 2020/11/3 11:40
 **/
@Service
public class RegisterManager {

    @Autowired
    private ZhuoFanRegister zhuoFanRegister;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private SysConfigFeignService sysConfigFeignService;

    @Autowired
    private SysAttaManager sysAttaManager;

    public String checkRegisterLicense(RegisterLicense registerLicense, String key) throws Exception {

        String promptMessage = zhuoFanRegister.getMessage();
        String zfsoftRegister = zhuoFanRegister.getRegister();
        String zfSoftAuthorization = zhuoFanRegister.getAuthorization();
        String code = zhuoFanRegister.getCode();
        if (StrUtil.isEmpty(registerLicense.getAttaOid())) {
            return "授权文件有误！";
        }
        SysAtta atta = sysAttaManager.getSysAttaByAttaOid(registerLicense.getAttaOid());
        if (atta == null) {
            return "授权文件有误！";
        }
        InputStream inputStream = null;
        if(null!=atta && StrUtil.isNotEmpty(atta.getFastdfsNginxUrl())){
            //解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HttpUtil.download(atta.getFastdfsNginxUrl(), outputStream, true);
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        }else{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            inputStream = sysAttaManager.getFileInputStream(request, atta.getAttaOid());
        }
        if (null == inputStream) {
            return promptMessage + "代码：Q001";
        }
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        // 获取操作系统名称
        String osName = System.getProperty("os.name");
        if (osName != null) {
            osName = osName.toLowerCase();
        }
        // 读取注册文件
        // 考虑到编码格式
        sb.append(IOUtils.toString(inputStream, "GB2312"));
        sb2.append(sb.toString());
        /** 读取上传的文件结束 */
        /** 创建授权文件开始 */
        String path = zhuoFanRegister.getPath();
        //path = path.replace("file:/", "");
        //path = path.replace('/', '\\');
        //path = path.replace("classes\\", "");
        // 去掉第一个\,如 \D:\JavaWeb...
        String string = "//";
        if (path.substring(0, 1).equals(string)) {
            path = path.substring(1);
        }
        // 解密
        sb = new StringBuffer(SecurityUtil.detrypt(sb.toString(), key));
        if (sb.length() == 0) {
            return promptMessage + "代码：A017";
        }
        // 判断上传文件的正确性，判断授权码中的版本，序列号，IP地址，注册文件毫秒数四项值是否与平台所在服务器获取的四项值一致
        String[] sbArray = sb.toString().split(",Permission:");
        String[] sbArray1 = sbArray[0].split(",allowCodes:");
        // 授权文件异常
        if (StrUtil.isEmpty(sbArray1[0])) {
            return promptMessage + "代码：A017";
        }
        String pathVersion = "";
        String windowsStr = "windows";
        // 读取版本文件
        Resource resource = resourceLoader.getResource("classpath:" + zhuoFanRegister.getVersion());
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        StringBuffer sbAll = new StringBuffer();
        String version = "";
        String serialNumber = "";
        try (BufferedReader br = new BufferedReader(isr);) {
            version = br.readLine();
            serialNumber = br.readLine();
        }
        // 版本号为空
        if (StrUtil.isEmpty(version)) {
            return promptMessage + "代码：B028";
        }
        BufferedWriter out = null;
        // 如果版本文件中的序列号为空，则获取序列号并追加
        if (StringUtils.isEmpty(serialNumber)) {
            String[] strArray = sbArray1[0].split(",");
            int count = 0;
            for (String str : strArray) {
                if (!StringUtils.isEmpty(str) && str.contains("serialNumber")) {
                    if (str.split(":").length > 1) {
                        serialNumber = str.split(":")[1];
                        out = new BufferedWriter(
                                new OutputStreamWriter(new FileOutputStream(new File(pathVersion), false)));
                        out.write(version + System.getProperty("line.separator") + serialNumber);
                        out.close();
                        count++;
                    }
                }
            }
            // 授权文件有误
            if (count == 0) {
                return promptMessage + "代码：C036";
            }
        }
        String regdir = "";
        // 指定文件所在路径
        if (osName.contains(windowsStr.trim())) {
            regdir = path + File.separator + zfsoftRegister;
        } else {
            regdir = path.replace('\\', '/');
            regdir = regdir + File.separator + zfsoftRegister;
        }
        // 读取注册文件，检查是否有IP TIME等信息
        BufferedReader br1 = new BufferedReader(new FileReader(new File(regdir)));
        String ip = br1.readLine();
        br1.close();
        if (StrUtil.isEmpty(ip)) {
            return promptMessage + "代码：D045";
        }
        // creationTime 要求JDK1.7及以上
//			FileTime t = Files.readAttributes(Paths.get(regdir), BasicFileAttributes.class).creationTime();
        FileTime t = Files.readAttributes(Paths.get(regdir, new String[0]), BasicFileAttributes.class, new LinkOption[0]).creationTime();
        String time = t.toMillis() + "".trim();
//			1515046460381
//			1512815539834
        String[] strChekArray = sbArray1[0].split(",");
        for (String strCheck : strChekArray) {
            if (strCheck.indexOf("ExpireDate") != -1) {
                BaseStaticParameter.DUEDATE_ = DateUtil.parse(strCheck.split(":")[1], DatePattern.NORM_DATE_PATTERN);
                String configWarn = sysConfigFeignService.getSysConfigByCode("REGISTER_WARN").getData().getValue();
                BaseStaticParameter.DUEDATE_WARN = DateUtil.offsetDay(DateUtil.parse(strCheck.split(":")[1], DatePattern.NORM_DATE_PATTERN), -Integer.parseInt(configWarn));
                if (BaseStaticParameter.DUEDATE_.before(new Date())) {
                    return "授权文件已到期";
                }
            }
            // 授权码中的版本
            if (strCheck.contains("version")) {
                if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(version))) {
                    return promptMessage + "代码：E051";
                }
            }

            // 序列号
            if (strCheck.contains("serialNumber")) {
                if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(serialNumber))) {
                    return promptMessage + "代码：F062";
                }
            }

            // 区划代码
            if (strCheck.contains("code")) {
                if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(code))) {
                    return promptMessage + "代码：I095";
                }
            }

            // IP地址
            if (strCheck.contains("ip")) {
                if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(ip))) {
                    return promptMessage + "代码：G073";
                }
            }

            // 注册文件毫秒数
//            if (strCheck.contains("register")) {
//                if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(time))) {
//                    return promptMessage + "代码：H084";
//                }
//            }

            if (strCheck.indexOf("RegistDate") != -1) {
                BaseStaticParameter.STARTDATE_ = DateUtil.parse(strCheck.split(":")[1], DatePattern.NORM_DATE_PATTERN);
            }
        }
        String dirAuth = "";
        if (osName.contains(windowsStr.trim())) {
            dirAuth = path + "\\Authorization\\";
        } else {
            dirAuth = path.replace('\\', '/');
            dirAuth = File.separator + dirAuth + "Authorization/";
        }
        File dir = new File(dirAuth);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 创建授权文件
        // 目录为\WEB-INF\Authorization\zfSoftAuthorization.txt
        File authfile = new File(dir, zfSoftAuthorization);
        if (!authfile.exists()) {
            try {
                authfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte bytes[] = new byte[512];
        // 新加的
        bytes = sb2.toString().getBytes();
        int b = sb2.toString().length();
        FileOutputStream fos = new FileOutputStream(authfile);
        fos.write(bytes, 0, b);
        fos.flush();
        fos.close();
        /** 创建授权文件结束 */
        List<RegisterPermission> retList = new ArrayList<>();
        if (sbArray.length > 1) {
            String regPer = sbArray[1];
            //Gson gson = new Gson();
            retList = JSON.parseObject(regPer, new TypeToken<List<RegisterPermission>>() {
            }.getType());
        }
        // 读取到的权限转换成MAP
        Map<String, String> perCode = new HashMap<String, String>(6);
        for (RegisterPermission registerPermission : retList) {
            perCode.put(registerPermission.getCode(), registerPermission.getIsAuth());
        }
        if (!initRoleInfo()) {
            return promptMessage + "代码：H095";
        }

        return null;
    }

    public String initRegister(String key) throws Exception {
        if (StrUtil.isEmpty(zhuoFanRegister.getPath())) {
            return "文件路径不存在！";
        }
        String zfsoftRegister = zhuoFanRegister.getRegister();
        String code = zhuoFanRegister.getCode();
        // 获取操作系统名称
        String osName = System.getProperty("os.name");
        if (osName != null) {
            osName = osName.toLowerCase();
        } else {
            osName = "";
        }
        String writeAdd = "";
        // 创建开始获取IP
        String sIP = IpUtils.getLocalIp();
        /* 注册时间等信息 */
        // 指定文件名
        writeAdd = zhuoFanRegister.getPath() + "/" + zfsoftRegister;
        File file = new File(writeAdd);
        // 文件不存在则创建文件
        if (!file.exists()) {
            file.createNewFile();
        }
        // 读取注册文件，检查是否有IP TIME等信息
        BufferedReader br1 = new BufferedReader(new FileReader(file));
        String ip = br1.readLine();
        br1.close();
        // 如果追加方式用true
        StringBuffer sb = new StringBuffer();
        // 注意需要转换对应的字符集
        sb.append(sIP + System.getProperty("line.separator"));
        if (StringUtils.isEmpty(ip)) {
            IOUtils.write(sb.toString().getBytes("utf-8"), new FileOutputStream(file, true));
        } else if (!ip.equals(sIP)) {
            IOUtils.write(sb.toString().getBytes("utf-8"), new FileOutputStream(file, false));
        }
        // 防止文件被冒用，必须用JDK1.7
        FileTime ft = Files.readAttributes(Paths.get(writeAdd), BasicFileAttributes.class).creationTime();
        String time = ft.toMillis() + "".trim();
        // 读取版本文件
        Resource resource = resourceLoader.getResource("classpath:" + zhuoFanRegister.getVersion());
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        StringBuffer sbAll = new StringBuffer();

        try (BufferedReader br = new BufferedReader(isr);) {
            String version = br.readLine();
            String serialNumber = br.readLine();
            if (StringUtils.isEmpty(serialNumber)) {
                serialNumber = "";
            }
            String config = sysConfigFeignService.getSysConfigByCode("CUSTOMER_NAME").getData().getValue();
            if (null == config) {
                return "系统名称不存在";
            }
            sbAll.append("customer:" + config);
            sbAll.append(",version:" + version);
            sbAll.append(",serialNumber:" + serialNumber);
            sbAll.append(",ip:" + sIP);
            sbAll.append(",register:" + time);
            sbAll.append(",code:" + code);
        }
        return SecurityUtil.encrypt(sbAll.toString(), key).replaceAll(System.getProperty("line.separator"), "");

    }

    private boolean initRoleInfo() {
        try {
            List<RegisterPermission> retList = new ArrayList<RegisterPermission>();
            // List<SysRole> roleList = sysRoleService.queryAllRole();
            // 读取Authorization/zfSoftAuthorization.txt授权文件
            String path = zhuoFanRegister.getPath();
            // 将/换成\
//            path = path.replace('/', '\\');
            // 去掉file:
            path = path.replace("file:", "");
            // 去掉class\
            path = path.replace("classes\\", "");
            // 去掉第一个\,如 \D:\JavaWeb...
            String str = "//";
            if (path.substring(0, 1).equals(str)) {
                path = path.substring(1);
            }
            // 获取操作系统名称
            String osName = System.getProperty("os.name");
            if (osName != null) {
                osName = osName.toLowerCase();
            } else {
                osName = "";
            }
            String windowsStr = "windows";
            if (osName.contains(windowsStr.trim())) {
                path = path + "\\Authorization\\zfSoftAuthorization.txt";
            } else {
                path = path.replace('\\', '/');
                path = File.separator + path + "Authorization/zfSoftAuthorization.txt";
            }
            File authfile = new File(path);
            String regPer = "";
            StringBuffer sb = new StringBuffer();
            if (authfile.exists()) {
                // 如果文件存在，此处认为该系统已经被授权过
                BaseStaticParameter.AUTH_STATE = "Y";
                // 考虑到编码格式
                sb.append(IOUtils.toString(new FileInputStream(authfile), "UTF-8"));
                sb = new StringBuffer(SecurityUtil.detrypt(sb.toString(), "zfsoft888"));
                String[] sbArray = sb.toString().split(",Permission:");
                String[] sbArray1 = sbArray[0].split(",allowCodes:");
                if (sbArray.length > 1) {
                    regPer = sbArray[1];
                }
                String[] strChekArray = sbArray1[0].split(",");

                if (BaseStaticParameter.REGISTER_CODE.size() > 0) {
                    BaseStaticParameter.REGISTER_CODE.clear();
                }
                String comma = ",";
                String[] codes = sbArray1[1].split(comma);
                for (String code : codes) {
                    if (!StringUtils.isEmpty(code)) {
                        BaseStaticParameter.REGISTER_CODE.add(code);
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

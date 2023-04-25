package com.zfsoft.microservice.platform.init;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.vo.ZhuoFanRegister;
import com.zfsoft.microservice.platform.feign.settings.SysConfigFeignService;
import com.zfsoft.microservice.platform.util.IpUtils;
import com.zfsoft.microservice.platform.util.SecurityUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Enumeration;

/**
 * @Description: 初始化参数
 * @Author: wuxx
 * @Date: 2020/9/14 15:06
 **/
@Component
public class DataSourceInitListener implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitListener.class);

    @Autowired
    private SysConfigFeignService sysConfigFeignService;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    private ZhuoFanRegister zhuoFanRegister;

    @Value("${zhuofan.register.isAble:#{null}}")
    private String registerIsAble;

    @Override
    public void run(ApplicationArguments args){
        LOGGER.info("初始化数据开始..........");
        try {
            if(null!=registerIsAble && "true".equals(registerIsAble)){
                initAuthorization();//初始化注册授权
            }
            LOGGER.info("系统初始化完成...........");
        }catch (Exception e){
            LOGGER.error("系统初始化失败！settings-service-provider程序访问异常！");
        }
    }

    /**
     * @description: 初始化注册授权
     * @author: wuxx
     * @Date: 2020/11/4 10:24
     **/
    private void initAuthorization() {
        try {
            String path = zhuoFanRegister.getPath();
            // 将/换成\
            // 去掉file:
            path = path.replace("file:", "");
            // 去掉class\
            path = path.replace("classes\\", "");
            // 去掉第一个\,如 \D:\JavaWeb...
            String str = "//";
            if (path.substring(0, 1).equals(str)) {
                path = path.substring(1);
            }
            String dirAuth = "";
            String windows = "windows";
            String property = "os.name";
            // 获取操作系统名称
            String osName = System.getProperty(property);
            if (osName != null) {
                osName = osName.toLowerCase();
            } else {
                osName = "";
            }
            String writeAdd = path;
            // 创建开始获取IP
            String sIP = IpUtils.getLocalIp();
            // 指定文件所在路径
            String windowsStr = "windows";
            writeAdd += "/" + zhuoFanRegister.getRegister();
            File file = new File(writeAdd);
            // 文件不存在则创建文件
            if (!file.exists()) {
                file.createNewFile();
            }
            IOUtils.write(sIP.getBytes("utf-8"), new FileOutputStream(file, false));
            if (osName.contains(windows)) {
                //dirAuth = path + "\\Authorization\\";
                dirAuth = path + "\\Authorization\\";
            } else {
                dirAuth = path.replace('\\', '/');
                //dirAuth = dirAuth + File.separator + "Authorization/";
                dirAuth = File.separator + path + "Authorization/";
            }

            File dir = new File(dirAuth);
            // 注册文件丢失
            if (!dir.exists()) {
                LOGGER.error(BaseStaticParameter.AUTHORIZATION_ERROR_MESSAGE + "代码：IK359");
            }
            // 创建授权文件
            String fileName = "zfSoftAuthorization.txt";
            File authfile = new File(dir, fileName);
            //System.out.println(authfile.getPath());
            String regPer = "";
            StringBuffer sb = new StringBuffer();
            if (authfile.exists()) {
                // 考虑到编码格式
                sb.append(IOUtils.toString(new FileInputStream(authfile), "UTF-8"));
                sb = new StringBuffer(SecurityUtil.detrypt(sb.toString(), "zfsoft888"));

                // 判断上传文件的正确性，判断授权码中的版本，序列号，IP地址，注册文件毫秒数四项值是否与平台所在服务器获取的四项值一致
                String[] sbArray = sb.toString().split(",Permission:");

                String allowCodes = sbArray[0].split("allowCodes:")[1];
                BaseStaticParameter.REGISTER_CODE.clear();
                String comma = ",";
                for (String code : allowCodes.split(comma)) {
                    BaseStaticParameter.REGISTER_CODE.add(code.trim());
                }
                // 授权文件异常受到破坏
                if (StrUtil.isEmpty(sbArray[0])) {
                    LOGGER.error(BaseStaticParameter.AUTHORIZATION_ERROR_MESSAGE + "代码：IA017");
                }
                // 如果文件存在，此处认为该系统已经被授权过
                if (checkCode(path, sbArray[0])) {
                    BaseStaticParameter.AUTH_STATE = BaseStaticParameter.YES;
                } else {
                    LOGGER.error(BaseStaticParameter.AUTHORIZATION_ERROR_MESSAGE + "代码：IA365");
                }

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 获取linux的ip
     * @author: wuxx
     * @Date: 2020/11/4 10:22
     **/
    private String getLinuxLocalIp(){
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
                                    && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        return ip;
    }

    private boolean checkCode(String path, String sbArray) {
        String zfsoftRegister = zhuoFanRegister.getRegister();
        // 读取版本文件
        String version = "";
        String serialNumber = "";
        String ip = "";
        String time = "";
        String property = "os.name";
        String windows = "windows";
        try {
            String pathVersion = "";
            // 读取版本文件
            // 获取操作系统名称
            String osName = System.getProperty(property);
            if (osName != null) {
                osName = osName.toLowerCase();
            } else {
                osName = "";
            }
            // 读取版本文件
            Resource resource = resourceLoader.getResource("classpath:" + zhuoFanRegister.getVersion());
            InputStream is = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            StringBuffer sbAll = new StringBuffer();
            try (BufferedReader br = new BufferedReader(isr);) {
                version = br.readLine();
                serialNumber = br.readLine();
            }
            BufferedWriter out = null;
            // 如果版本文件中的序列号为空，则获取序列号并追加
            if (StrUtil.isEmpty(serialNumber)) {
                String[] strArray = sbArray.split(",");
                for (String str : strArray) {
                    if (str.contains("serialNumber")) {
                        if (str.split(":").length > 1) {
                            serialNumber = str.split(":")[1];
                            out = new BufferedWriter(
                                    new OutputStreamWriter(new FileOutputStream(new File(pathVersion), false)));
                            out.write(version + System.getProperty("line.separator") + serialNumber);
                            out.close();
                        }
                    }
                }
            }
            // 读取注册文件
            String regdir = "";
            // 指定文件所在路径
            if (osName.contains(windows)) {
                regdir = path + zfsoftRegister;
            } else {
                regdir = path.replace('\\', '/');
                regdir = regdir + File.separator + zfsoftRegister;
            }
            // 读取注册文件，检查是否有IP TIME等信息
            BufferedReader br1 = new BufferedReader(new FileReader(new File(regdir)));
            ip = br1.readLine();
            // TIME = br1.readLine();
            br1.close();
            // creationTime 要求JDK1.7及以上
            FileTime t = Files.readAttributes(Paths.get(regdir), BasicFileAttributes.class).creationTime();
            time = t.toMillis() + "".trim();
            String[] strChekArray = sbArray.split(",");
            for (String strCheck : strChekArray) {
                if (strCheck.indexOf("ExpireDate") != -1) {
                    BaseStaticParameter.DUEDATE_ = DateUtil.parse(strCheck.split(":")[1], DatePattern.NORM_DATE_PATTERN);
                    String configWarn = sysConfigFeignService.getSysConfigByCode("REGISTER_WARN").getData().getValue();
                    BaseStaticParameter.DUEDATE_WARN = DateUtil.offsetDay(DateUtil.parse(strCheck.split(":")[1], DatePattern.NORM_DATE_PATTERN), -Integer.parseInt(configWarn));
                    if (BaseStaticParameter.DUEDATE_.before(new Date())) {
                        return false;
                    }
                }

                // 授权码中的版本
                if (strCheck.contains("version")) {
                    if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(version))) {
                        return false;
                    }
                }

                // 序列号
                if (strCheck.contains("serialNumber")) {
                    if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(serialNumber))) {
                        return false;
                    }
                }

                // IP地址
                if (strCheck.contains("ip")) {
                    if (!(strCheck.split(":").length > 1 && strCheck.split(":")[1].equals(ip))) {
                        return false;
                    }
                }

                if (strCheck.indexOf("RegistDate") != -1) {
                    BaseStaticParameter.STARTDATE_ = DateUtil.parse(strCheck.split(":")[1], DatePattern.NORM_DATE_PATTERN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

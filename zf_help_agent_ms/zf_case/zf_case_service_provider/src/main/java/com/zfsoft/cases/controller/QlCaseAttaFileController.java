package com.zfsoft.cases.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.data.PhoneUploadDto;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.QlCaseMaterialManager;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.service.QlCaseAttaFileService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.feign.ZzqFastDfsFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @（#）: QlCaseMaterialAttaController
 * @description: 材料附件信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
@RequestMapping("/qlcaseAtta")
public class QlCaseAttaFileController implements QlCaseAttaFileService {

    public static final String PHONE_UPLOAD = "PHONE_UPLOAD:";

    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private QlCaseMaterialManager qlCaseMaterialManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ZzqFastDfsFeignService zzqFastDfsFeignService;

    @Value("${zfsoft.zzq.fastDFS.isAble}")
    private String fastDFSIsAble;


    /**
     * 上传办件材料附件
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/uploadCaseMaterialFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, MultipartFile[] files) {
        List<QlSysAtta> attaList=new ArrayList<QlSysAtta>();
        //登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        // 上传文件路径
  /*      String filePath = CaseServiceConfig.getUploadPath();
        File path = new File(filePath);
        if(!path.exists()) {
            path.mkdirs();
        }*/
        //判断file数组不能为空并且长度大于0
        if(files != null && files.length > 0) {
            //循环获取file数组中得文件
          /*  for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                //保存文件
                if (!file.isEmpty()) {
                    try {
                        // 上传并返回新文件名称
                        String fileName = FileUploadUtils.upload(filePath, file);
                        SysAtta sysAtta=new SysAtta();
                        sysAtta.setExtensionName(file.getContentType());
                        sysAtta.setOriginName(file.getOriginalFilename());
                        sysAtta.setUserOid(loginUser.getUserOid());
                        sysAtta.setName(fileName.substring(fileName.lastIndexOf("/")+1));
                        sysAtta.setFilePath(fileName.substring(8,fileName.lastIndexOf("/")));
                        sysAtta.setUploadDate(new Date());
                        SysAtta atta = sysAttaManager.saveSysAtta(sysAtta);
                        attaList.add(atta);
                        System.out.println(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            MultipartFile file = null;
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            //循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                //保存文件
                if (!file.isEmpty()) {
                    try {
                        // 上传并返回新文件名称
                        if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                            file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                        }
                        filePath = uploadUtil.uploadFile(file);
                        String userOid ="";
                        if(loginUser==null){
                            userOid = "c5ccbbaec7c14c319b3ce030d392cbad";
                        }else{
                            userOid =  loginUser.getUserOid();
                        }
                        SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath,userOid);
                        QlSysAtta sysAtta = new QlSysAtta();
                        BeanUtils.copyProperties(sysAttaFile,sysAtta);

                        if("true".equals(fastDFSIsAble)){
                            /** 对接自治区统一文件管理系统 add by WangKe 2022-06-10 */
                            ApiResultSet<String> qlSysAttaApiResultSet = zzqFastDfsFeignService.uploadAtta(file);
                            sysAtta.setZzqAttaOid(qlSysAttaApiResultSet.getData());
                        }

                       /* String fastdfsNginxUrl="http://hf.zhuofansoft.com:8888/"+sysAttaFile.getFastdfsUploadUrl();
                        sysAtta.setFastdfsNginxUrl(fastdfsNginxUrl);*/
                        //保存附件信息
                        QlSysAtta atta = sysAttaManager.saveSysAtta(sysAtta);
                        attaList.add(atta);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ApiResultSet<List<QlSysAtta>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(attaList);
        return apiResultSet;
    }

    /**
     * 获取二维码uuid
     * @return
     */
    @GetMapping(value = "/getPhoneQrScanRandom")
    @Override
    public ApiResultSet<String> getPhoneQrScanUuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(uuid);
        return apiResultSet;
    }

    /**
     * 获取扫码上传状态
     * @param uuid
     * @return
     */
    @GetMapping(value = "/getPhoneUploadStatus")
    @Override
    public ApiResultSet<String> getPhoneUploadStatus(String uuid) {
        PhoneUploadDto dto = (PhoneUploadDto) redisTemplate.opsForValue().get(PHONE_UPLOAD + uuid);
        String status = "0";
        if (dto != null) {
            status = dto.getScanStatus();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(status);
        return apiResultSet;
    }

    /**
     * 初始化上传信息
     * @param uuid
     * @param caseMaterialOid
     * @return
     */
    @PostMapping(value = "/initPhoneUpload")
    @Override
    public ApiResultSet<String> initPhoneUpload(String uuid, String caseMaterialOid) {
        PhoneUploadDto dto = (PhoneUploadDto) redisTemplate.opsForValue().get(PHONE_UPLOAD + uuid);
        if (dto == null) {
            dto = new PhoneUploadDto();
            dto.setExpireDate(System.currentTimeMillis() + 2 * 60 * 1000);
            dto.setScanStatus("1");
            QlCaseMaterial qlCaseMaterial = qlCaseMaterialManager.queryMaterialByCaseMaterialOid(caseMaterialOid);
            if (qlCaseMaterial == null) {
                throw new ResultInfoException("程序异常，请联系工作人员");
            }
            dto.setMaterialName(qlCaseMaterial.getMaterialName());
            redisTemplate.opsForValue().set(PHONE_UPLOAD + uuid, dto, 600, TimeUnit.SECONDS);
        } else {
            if ("1".equals(dto.getScanStatus())) {
                throw new ResultInfoException("二维码已被扫描，二维码失效！");
            }
            if ("2".equals(dto.getScanStatus())) {
                throw new ResultInfoException("材料已提交，二维码失效！");
            }
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dto.getMaterialName());
        return apiResultSet;
    }

    /**
     * 检测上传状态
     * @param uuid
     * @return
     */
    @GetMapping(value = "/checkPhoneUploadStatus")
    @Override
    public ApiResultSet<Boolean> checkPhoneUploadStatus(String uuid) {
        PhoneUploadDto dto = (PhoneUploadDto) redisTemplate.opsForValue().get(PHONE_UPLOAD + uuid);
        if (dto == null) {
            throw new ResultInfoException("操作超时，请关闭页面！");
        } else {
            if (System.currentTimeMillis() > dto.getExpireDate()) {
                throw new ResultInfoException("操作超时，请关闭页面！");
            }
            if ("2".equals(dto.getScanStatus())) {
                throw new ResultInfoException("材料已提交，请关闭页面！");
            }
        }
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(Boolean.TRUE);
        return apiResultSet;
    }

    /**
     * 保存上传信息
     * @param uuid
     * @param attaOids
     * @return
     */
    @PostMapping(value = "/saveTempPhoneUploadAtta")
    @Override
    public ApiResultSet saveTempPhoneUploadAtta(String uuid, String attaOids) {
        PhoneUploadDto dto = (PhoneUploadDto) redisTemplate.opsForValue().get(PHONE_UPLOAD + uuid);
        if (dto == null) {
            throw new ResultInfoException("操作超时，请关闭页面！");
        } else {
            if (System.currentTimeMillis() > dto.getExpireDate()) {
                throw new ResultInfoException("操作超时，请关闭页面！");
            }
            if ("2".equals(dto.getScanStatus())) {
                throw new ResultInfoException("材料已提交，请关闭页面！");
            }
            dto.setAttaOids(attaOids);
            dto.setScanStatus("2");
            redisTemplate.opsForValue().set(PHONE_UPLOAD + uuid, dto, 600, TimeUnit.SECONDS);
        }
        return ApiResultSet.ok();
    }

    /**
     * 获取上传信息
     * @param uuid
     * @return
     */
    @GetMapping(value = "/getTempPhoneUploadAtta")
    @Override
    public ApiResultSet<List<QlSysAtta>> getTempPhoneUploadAtta(String uuid) {
        PhoneUploadDto dto = (PhoneUploadDto) redisTemplate.opsForValue().get(PHONE_UPLOAD + uuid);
        if (dto == null) {
            throw new ResultInfoException("操作超时，请重新使用功能！");
        } else {
            if (!"2".equals(dto.getScanStatus())) {
                throw new ResultInfoException("材料尚未提交，请先提交！");
            }
        }
        ApiResultSet<List<QlSysAtta>> apiResultSet = new ApiResultSet<>();
        List<QlSysAtta> qlSysAttas = new ArrayList<>();
        if(StrUtil.isNotEmpty(dto.getAttaOids())) {
            String[] attaArr = dto.getAttaOids().split(",");
            for(String oid : attaArr) {
                QlSysAtta qlSysAtta = sysAttaManager.querySysAttaByOid(oid);
                if(null != qlSysAtta) {
                    qlSysAttas.add(qlSysAtta);
                }
            }
        }
        apiResultSet.setData(qlSysAttas);
        return apiResultSet;
    }

}

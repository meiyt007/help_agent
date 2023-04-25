package com.zfsoft.ha.front.controller;

import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.dbaccess.data.DbQlSysAtta;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.requestData.HaUserResourceRequestData;
import com.zfsoft.ha.data.responseData.HaUserResourceResponseData;
import com.zfsoft.ha.data.vo.HaUserResourceVo;
import com.zfsoft.ha.front.RecourceService;
import com.zfsoft.ha.managers.HaUserResourceManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.shareResult;

/**
 * 资源管理controller
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/15
 */
@RestController
@Slf4j
public class ResourceController implements RecourceService {
    @Resource
    private HaUserResourceManager haUserResourceManager;
    @Resource
    private SysAttaManager sysAttaManager;

    @Value("${fdfs.fastDFSNginxUrl}")
    private String fastDFSNginxUrl;
    /**
     * 模糊查询，获取资源列表
     * @param ha parentId 父级id 当父级为空时，只获取顶级的资源，包含：文件和文件夹
     * @param ha name 资源名称
     * @return
     */
    @Override
    public ApiResultSet<List<HaUserResourceResponseData>> listResource(HaUserResourceRequestData ha){

        log.info("资源管理：进入获取资源列表,入参:"+ha);
        List<HaUserResourceResponseData> haUserResourceRespList;
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        try {
            List<HaUserResource> haUserResources = haUserResourceManager.queryHaUserResourceByNameAndParentId(ha.getName(), String.valueOf(ha.getParentId()), String.valueOf(currentHaLoginUserInfo.getId()));
            haUserResourceRespList = haUserResources.stream().map(haUserResource -> {
                HaUserResourceResponseData haUserResourceResp = new HaUserResourceResponseData();
                BeanUtils.copyProperties(haUserResource, haUserResourceResp);
                if(haUserResource.getResourceInfo()!=null&&!haUserResource.getResourceInfo().isEmpty()){
                    DbQlSysAtta dbQlSysAtta  = new DbQlSysAtta();
                    dbQlSysAtta.setFastdfsUploadUrl(haUserResource.getResourceInfo());
                    dbQlSysAtta.setDelFlag(0);
                    List<QlSysAtta> qlSysAttas = sysAttaManager.queryAll(dbQlSysAtta);
                    if(qlSysAttas.size()>0){
                        QlSysAtta qlSysAtta = qlSysAttas.get(0);
                        haUserResourceResp.setFileName(qlSysAtta.getOriginName());
                    }
                }
                return haUserResourceResp;
            }).collect(Collectors.toList());

            log.info("资源管理：获取资源列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("资源管理：获取资源列表失败："+e.getMessage());
            return  new ApiResultSet<>(500,"资源管理：获取资源列表失败",e.getMessage());
        }

        return ApiResultSet.ok("资源管理：获取资源列表成功",haUserResourceRespList);
    }

    /**
     * 根据id获取资源信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet<HaUserResourceResponseData> getResourceInfo(String id) {
        HaUserResourceResponseData haUserResourceList = new HaUserResourceResponseData();
        log.info("资源管理：进入根据id获取资源信息,入参:"+id);
        if(StringUtil.isEmpty(id)){
            return  new ApiResultSet<>(501,"资源管理：根据id获取资源信息失败,id为空",null);
        }
        try {
            HaUserResourceVo haUserResource = haUserResourceManager.getHaUserResourceById(id);
            if("2".equals(haUserResource.getType())){
                haUserResource.setFastdfsUploadUrl(fastDFSNginxUrl+"/"+haUserResource.getResourceInfo());
            }
            BeanUtils.copyProperties(haUserResource, haUserResourceList);
            log.info("资源管理：根据id获取资源信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("资源管理：根据id获取资源信息失败："+e.getMessage());
            return  new ApiResultSet<>(500,"资源管理：根据id获取资源信息失败",e.getMessage());
        }
        return ApiResultSet.ok("资源管理：根据id获取资源信息成功",haUserResourceList);

    }
//    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ApiResultSet<Map<String,String>> uploadFile(HttpServletRequest request,MultipartFile file) {
        log.info("资源管理：进入上传文件信息");
        Map<String,String> map = new HashMap<>();
        UploadUtil uploadUtil = new UploadUtil(request);
        String filePath = "";
        HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        if (!file.isEmpty()) {
            try {
                // 上传并返回新文件名称
                if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                    file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                }
                filePath = uploadUtil.uploadFile(file);
                if(filePath==null) {
                    return  new ApiResultSet<>(500,"资源管理：上传文件错误",null);
                }
                String userOid;
                if(loginUser==null){
                    return  new ApiResultSet<>(500,"资源管理：上传文件错误","没有获取到登录信息");
                }else{
                    userOid = String.valueOf(loginUser.getId());
                }
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath,userOid);
                QlSysAtta sysAtta = new QlSysAtta();
                BeanUtils.copyProperties(sysAttaFile,sysAtta);

//                if("true".equals(fastDFSIsAble)){
//                    /** 对接自治区统一文件管理系统 add by WangKe 2022-06-10 */
//                    ApiResultSet<String> qlSysAttaApiResultSet = zzqFastDfsFeignService.uploadAtta(file);
//                    sysAtta.setZzqAttaOid(qlSysAttaApiResultSet.getData());
//                }

                       /* String fastdfsNginxUrl="http://hf.zhuofansoft.com:8888/"+sysAttaFile.getFastdfsUploadUrl();
                        sysAtta.setFastdfsNginxUrl(fastdfsNginxUrl);*/
                //保存附件信息
                QlSysAtta atta = sysAttaManager.saveSysAtta(sysAtta);
                log.info("资源管理：上传文件信息成功");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("资源管理：上传文件信息失败："+e.getMessage());
                return  new ApiResultSet<>(500,"资源管理：上传文件信息失败",e.getMessage());

            }
        }
        map.put("filePath",filePath);
        return ApiResultSet.ok("资源管理：进入上传文件信息成功",map);
    }

//    @Autowired
//    private FastFileStorageClient storageClient;
//    public final static String server_host = "http://139.9.123.180:22122/";
//    @ResponseBody
//    @ApiOperation(value = "上传文件",httpMethod = "POST")
//    @PostMapping("/upload")
//    public String uploadFile(@ApiParam("文件") MultipartFile file) throws IOException {
//        StorePath storePath = storageClient.uploadFile(file.getInputStream(),
//                file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
//
//        String filepath = server_host + storePath.getFullPath();
//        return "文件上传成功 地址为："+filepath;
//    }

    @Override
    public ApiResultSet<HaUserResource> saveResourceInfo(HaUserResourceVo haUserResourceVo) {
        log.info("资源管理：进入保存资源信息");
        String message = null;
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        try {
            if(haUserResourceVo.getId()!=null){
                haUserResourceVo.setUpdateBy(currentHaLoginUserInfo.getName());
                haUserResourceVo.setUpdateDate(new Date());
                haUserResourceVo.setDeleteStatus("1");
            }else{
                haUserResourceVo.setCreateDate(new Date());
                haUserResourceVo.setUpdateDate(new Date());
                haUserResourceVo.setUpdateBy(currentHaLoginUserInfo.getName());
                haUserResourceVo.setCreateBy(currentHaLoginUserInfo.getName());
                haUserResourceVo.setDeleteStatus("1");
            }
            haUserResourceVo.setWorkUserId(currentHaLoginUserInfo.getId());
            HaUserResource haUserResource = new HaUserResource();
            BeanUtils.copyProperties(haUserResourceVo,haUserResource);
            if(haUserResourceVo.getFileName()!=null&&!haUserResourceVo.getFileName().isEmpty()){
                DbQlSysAtta dbQlSysAtta  = new DbQlSysAtta();
                dbQlSysAtta.setFastdfsUploadUrl(haUserResourceVo.getResourceInfo());
                dbQlSysAtta.setDelFlag(0);
                List<QlSysAtta> qlSysAttas = sysAttaManager.queryAll(dbQlSysAtta);
                if(qlSysAttas.size()>0){
                    QlSysAtta qlSysAtta = qlSysAttas.get(0);
                    qlSysAtta.setOriginName(haUserResourceVo.getFileName());
                    sysAttaManager.saveSysAtta(qlSysAtta);
                }
                else{
                    return  new ApiResultSet<>(500,"资源管理：保存资源信息失败，根据fastdfsUploadUrl找不到文件",null);
                }
            }
            int index = haUserResourceManager.saveOrUpdateHaUserResource(haUserResource);
            if(index != 0){
                if(haUserResourceVo.getId()!=null){
                    //说明有新增或修改
                    message="修改成功";
                }else{
                    message="新增成功";
                }

            }else{
                message="未更新表数据";
            }
            log.info("资源管理：保存资源信息操作"+message);
        }catch (Exception e){
            e.printStackTrace();
            log.info("资源管理：上传文件信息失败："+e.getMessage());
            return  new ApiResultSet<>(500,"资源管理：上传文件信息失败",e.getMessage());
        }
        return ApiResultSet.ok("资源管理：保存资源信息成功"+message,haUserResourceVo);
    }

    @Override
    public ApiResultSet deleteResource(String ids) {
        log.info("资源管理：进入删除资源信息");
        try {
            String[] idc = ids.split(",");
            for (String id : idc) {
                HaUserResource haUserResource = haUserResourceManager.deleteUserid(id);
            }
            log.info("资源管理：删除资源信息成功");

        } catch (Exception e) {
            e.printStackTrace();
            log.info("资源管理：删除资源信息是失败"+e.getMessage());
            return new ApiResultSet<>(500,"资源管理：删除资源信息失败",null);
        }
        return ApiResultSet.ok("资源管理：删除资源信息成功",null);
    }

    @Override
    public ApiResultSet shareResource(String id, String workUserIds) {
        int index = haUserResourceManager.shareResource(id,workUserIds);
        if(index <0){
            return  new ApiResultSet<>(500,"资源管理：分享资源失败"+shareResult.get(index+""),null);
        }
        return ApiResultSet.ok("资源管理：分享资源成功",index);
    }
}

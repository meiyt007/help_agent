package com.zfsoft.ha.manager.controller;

import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.controller.QlCaseController;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.responseData.HaUserResourceResponseData;
import com.zfsoft.ha.data.vo.HaUserResourceVo;
import com.zfsoft.ha.manager.HaUserResourceService;
import com.zfsoft.ha.managers.HaUserResourceManager;
import com.zfsoft.ha.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.DownloadUtil;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.web.TreeSelect;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description //用户资源
 * @Author: zhaobf
 * @Date: 2022/7/15 14:08
 */
@RestController
@Slf4j
public class HaUserResourceController implements HaUserResourceService {

    @Resource
    private HaUserResourceManager haUserResourceManager;
    @Resource
    private QlCaseController qlCaseController;
    @Resource
    private SysAttaManager sysAttaManager;

    @Override
    public ApiResultSet<HaUserResource> saveHaUserResource(HaUserResource HaUserResource){
        log.info("用户资源管理：进入保存资源信息,入参:"+HaUserResource);
        com.zfsoft.ha.data.HaUserResource haUserResource = null;
        try {
            haUserResource = haUserResourceManager.saveHaUserResource(HaUserResource);
            ApiResultSet<com.zfsoft.ha.data.HaUserResource> apiResultSet = new ApiResultSet<>();
            apiResultSet.setData(haUserResource);
            log.info("用户资源管理：保存资源信息成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：保存资源信息失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：保存资源信息失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：保存资源信息成功",haUserResource);
    }

    @Override
    public ApiResultSet<PageResult<HaUserResource>> queryHaUserResourceByName(String name, Integer pageNumber, Integer pageSize){
        log.info("用户资源管理：进入根据资源名称获取用户资源信息：入参："+name);
        PageHelper.startPage(pageNumber, pageSize);
        PageResult<HaUserResource> resultSet = null;
        try {
            resultSet = haUserResourceManager.queryHaUserResourceByName(name);
            log.info("用户资源管理：进入根据资源名称获取用户资源信息成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：进入根据资源名称获取用户资源信息失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：进入根据资源名称获取用户资源信息失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：进入根据资源名称获取用户资源信息成功",resultSet);
    }

    @Override
    public ApiResultSet<PageResult<HaUserResourceVo>> queryHaUserResourceByNameAndWorkUserName(String name, String workName, Integer pageNumber, Integer pageSize){
        log.info("用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息：入参：name:{},workName:{}",name,workName);
        PageHelper.startPage(pageNumber, pageSize);
        PageResult<HaUserResourceVo> resultSet = null;
        try {
            resultSet = haUserResourceManager.queryHaUserResourceByNameAndWorkUserName(name,workName);
            log.info("用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息成功",resultSet);
    }

    @Override
    public ApiResultSet<HaUserResource> save(@RequestBody HaUserResource haUserResource){
        log.info("用户资源管理：进入修改或保存用户资源信息：入参："+haUserResource);
        ApiResultSet<Map<String, String>> loginUser = qlCaseController.getLoginUser();
        String message;
        try {
            if(haUserResource.getId()!=null){
                haUserResource.setUpdateBy(loginUser.getData().get("userName"));
                haUserResource.setUpdateDate(new Date());
                haUserResource.setDeleteStatus("1");
            }else{
                haUserResource.setCreateDate(new Date());
                haUserResource.setUpdateDate(new Date());
                haUserResource.setUpdateBy(loginUser.getData().get("userName"));
                haUserResource.setCreateBy(loginUser.getData().get("userName"));
                haUserResource.setDeleteStatus("1");
            }
            int index = haUserResourceManager.saveOrUpdateHaUserResource(haUserResource);
            if(index != 0){
                if(haUserResource.getId()!=null){
                    //说明有新增或修改
                    message="修改成功";
                }else{
                    message="新增成功";
                }
            }else{
                message="已存在，没有更改";
            }
            log.info("用户资源管理：修改或保存用户资源信息成功"+message);
        }catch (ServiceException e){
            log.info("用户资源管理：修改或保存用户资源信息失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：修改或保存用户资源信息失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息成功",haUserResource);
    }

    @Override
    public ApiResultSet<HaUserResourceVo> getHaUserResourceById(String id){
        log.info("用户资源管理：进入根据资源id获取用户资源信息：入参："+id);
        HaUserResourceVo haUserResource = null;
        try {
            haUserResource = haUserResourceManager.getHaUserResourceById(id);
            log.info("用户资源管理：根据资源id获取用户资源信息成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：根据资源id获取用户资源信息失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：根据资源id获取用户资源信息失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：根据资源id获取用户资源信息成功",haUserResource);
    }

    @Override
    public ApiResultSet<HaUserResource> deleteById(String id) {
        log.info("用户资源管理：进入根据id删除用户资源：入参：id{}"+id);
        HaUserResource haUserResource = null;
        try {
            haUserResource = haUserResourceManager.deleteUserid(id);
            log.info("用户资源管理：根据id删除用户资源成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：根据id删除用户资源败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：根据id删除用户资源失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：根据id删除用户资源成功",haUserResource);
    }
    @Override
    public ApiResultSet<List<HaUserResourceResponseData>> queryHaUserResourceList(){
        log.info("用户资源管理：进入获取文件夹类型资源");
        List<HaUserResourceResponseData> haUserResourceRespList = null;
        try {
            List<HaUserResource> haUserResources = haUserResourceManager.queryHaUserResourceByType("1");
            haUserResourceRespList = haUserResources.stream().map(haUserResource -> {
                HaUserResourceResponseData haUserResourceResp = new HaUserResourceResponseData();
                BeanUtils.copyProperties(haUserResource, haUserResourceResp);
                return haUserResourceResp;
            }).collect(Collectors.toList());
            log.info("用户资源管理：获取文件夹类型资源成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：获取文件夹类型资源失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：获取文件夹类型资源失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：获取文件夹类型资源成功",haUserResourceRespList);
    }


    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ApiResultSet<QlSysAtta> uploadFile(HttpServletRequest request, MultipartFile file) {
        log.info("资源管理：进入上传文件信息");
        QlSysAtta atta = null;
        UploadUtil uploadUtil = new UploadUtil(request);
        String filePath = "";
        ApiResultSet<Map<String, String>> loginUser = qlCaseController.getLoginUser();
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
                    userOid = String.valueOf(loginUser.getData().get("userOid"));
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
                atta = sysAttaManager.saveSysAtta(sysAtta);
                log.info("资源管理：上传文件信息成功");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("资源管理：上传文件信息失败："+e.getMessage());
                return  new ApiResultSet<>(500,"资源管理：上传文件信息失败",e.getMessage());

            }
        }
        return ApiResultSet.ok("资源管理：获取资源列表成功",atta);
    }
    @Override
    public ApiResultSet<Map<String,Object>> queryHaUserResourceDataByIdAndType(String id, String type){
        log.info("用户资源管理：进入根据资源文件父id获取所有下级资源数据:入参Id:{},type:{}",id,type);
        List<Map<String,Object>> map = new ArrayList<>();
        try {
            //根据类型判断是获取材料文件信息还是文件夹下级材料信息
            List<HaUserResource> haUserResources = haUserResourceManager.queryHaUserResourceByIdAndType(id, type);
            for (HaUserResource haUserResource : haUserResources) {
                Map<String,Object> mapOne = new HashMap<>();
                mapOne.put("id",haUserResource.getId());
                mapOne.put("workUserId",haUserResource.getWorkUserId());
                mapOne.put("type",haUserResource.getType());
                mapOne.put("name",haUserResource.getName());
                mapOne.put("resourceInfo",haUserResource.getResourceInfo());
                if (StringUtil.isNotEmpty(haUserResource.getResourceInfo())){
                    QlSysAtta qlSysAtta = sysAttaManager.querySysAttaByOid(haUserResource.getResourceInfo());
                    mapOne.put("attaOid",qlSysAtta==null?"":qlSysAtta.getAttaOid());
                    mapOne.put("attaName",qlSysAtta==null?"":qlSysAtta.getName());
                    mapOne.put("attaOriginName",qlSysAtta==null?"":qlSysAtta.getOriginName());
                    mapOne.put("attaId",qlSysAtta==null?"":qlSysAtta.getId());
                }
                map.add(mapOne);
            }
            log.info("用户资源管理：根据资源文件父id获取所有下级资源数据成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("用户资源管理：根据资源文件父id获取所有下级资源数据失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"用户资源管理：根据资源文件父id获取所有下级资源数据失败",e.getMessage());
        }
        return ApiResultSet.ok("用户资源管理：根据资源文件父id获取所有下级资源数据成功",map);
    }
    @Override
    public void downloadAtta(String attaOid){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //获取文件信息
        QlSysAtta atta = sysAttaManager.querySysAttaByOid(attaOid);
        try {
            DownloadUtil.downloadUploadUrlFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), atta.getFastdfsNginxUrl(), request, response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public ApiResultSet<List<TreeSelect>> queryAllResourceSimpleTree() {
        log.info("用户资源管理：进入获取资源树形结构");
        List<HaUserResource> haUserResources = null;
        try {
            haUserResources = haUserResourceManager.queryHaUserResourceByType("1");
            List<TreeSelect> treeSelects = GenDataTreeUtil.buildHaUserResourceTreeSelect(haUserResources);
            return new ApiResultSet(treeSelects);
        } catch (ServiceException e) {
            log.error("用户资源管理：进入获取资源树形结构错误:{}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "帮办人员接待下一位异常！" + e.getMessage());
        }
    }

}

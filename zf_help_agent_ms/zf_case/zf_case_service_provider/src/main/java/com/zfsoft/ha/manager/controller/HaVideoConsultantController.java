package com.zfsoft.ha.manager.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.vo.VideoConsultant;
import com.zfsoft.ha.manager.HaVideoConsultantService;
import com.zfsoft.ha.managers.HaVideoConsultantManager;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.service.browser.service.BrowserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description //视频咨询用户控制层
 * @Author: Wangyh
 * @Date: 2023/1/6 14:12
 */
@RestController
@Slf4j
public class HaVideoConsultantController implements HaVideoConsultantService {

    /**
     * 上传附件信息实现类
     */
    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private HaVideoConsultantManager haVideoConsultantManager;
    @Resource
    private SysOrganFeginService sysOrganFeignService;

    /**
     * @description:  重置密码
     * @param id 用户主键
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Override
    public ApiResultSet resetPassword(Long id) throws Exception{
        log.info("重置密码，id:{}",id);
        VideoConsultant videoConsultant = haVideoConsultantManager.ResetPassword(id);
        log.debug("haWorkUser结果集： haWorkUser:{}",videoConsultant);
        ApiResultSet<VideoConsultant> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(videoConsultant);
        apiResultSet.setMessage("重置密码为:123456");
        apiResultSet.setTime(String.valueOf(new Date()));
        return apiResultSet;
    }
    /**
     * @param file        文件
     * @description: 上传图片
     * @author: wangyh
     * @Date: 2022/7/15 14:08
     **/
    @Override
    public ApiResultSet uploadImage(HttpServletRequest request, MultipartFile file) {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        JSONObject jsonObject = null;
        if (!file.isEmpty()) {
            try {
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = uploadUtil.uploadFile(file);
                if(filePath==null) {
                    return  new ApiResultSet<>(500,"头像上传失败",null);
                }
                String userOid ="";
                if(loginUser==null){
                    return  new ApiResultSet<>(500,"头像上传失败","没有获取到登录信息");
                }else{
                    userOid = String.valueOf(loginUser.getUserOid());
                }
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath,userOid);
                QlSysAtta sysAtta = new QlSysAtta();
                BeanUtils.copyProperties(sysAttaFile,sysAtta);
                QlSysAtta atta = sysAttaManager.saveSysAtta(sysAtta);
                jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName());
            }catch (Exception e){
                log.error("头像上传失败：",e);
                return  new ApiResultSet<>(500,"头像上传失败",e.getMessage());
            }
        }
        return new ApiResultSet<>(jsonObject);
    }


    /**
     * @description:  查询视频用户分页信息列表
     * @param videoConsultant 视频咨询人员表请求实体类
     * @param pageNumber 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2023/1/6
     **/
    @Override
    public ApiResultSet<PageResult<VideoConsultant>> queryVideoPage(VideoConsultant videoConsultant, Integer pageNumber, Integer pageSize) throws Exception {
        log.info("查询视频用户分页信息列表，videoConsultant:{},pageNumber:{},pageSize:{}",videoConsultant,pageNumber,pageSize);
        PageResult<VideoConsultant> pageResult = haVideoConsultantManager.queryVideoPage(videoConsultant,pageNumber,pageSize);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<VideoConsultant>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }


    /**
     * @description:  删除视频咨询用户信息
     * @param id 账号
     * @author: wangyh
     * @Date: 2023/1/6
     **/
    @Override
    public ApiResultSet deleteByOid(Long id) throws Exception {
        log.info("删除视频咨询用户信息，id:{}",id);
        VideoConsultant videoConsultant = haVideoConsultantManager.deleteVideoByid(id);
        log.debug("videoConsultant结果集：videoConsultant:{}",videoConsultant);
        ApiResultSet<VideoConsultant> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(videoConsultant);
        apiResultSet.setMessage("用户删除成功");
        apiResultSet.setTime(String.valueOf(new Date()));
        return apiResultSet;
    }


    /**
     * @description:  参数配置的新增或者修改
     * @param videoConsultant 参数配置实体类
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Override
    public ApiResultSet saveVideoConsultant(VideoConsultant videoConsultant) throws Exception {
        log.info("参数配置的新增或者修改，videoConsultant:{}",videoConsultant);
        ApiResultSet<VideoConsultant> apiResultSet = new ApiResultSet<>();
        Map<String,Object> result= haVideoConsultantManager.saveOrUpdateVideoConsultant(videoConsultant);
        log.debug("result结果集：result:{}",result);
        if(result.size()>0){
            int index = (int) result.get("index");
            if(index != 0){
                if(result.get("type").equals("1")){
                    //说明有新增或修改
                    apiResultSet.setCode(200);
                    apiResultSet.setMessage("修改成功");
                }else{
                    apiResultSet.setCode(200);
                    apiResultSet.setMessage("新增成功");
                }

            }else{
                apiResultSet.setMessage("用户或房间号已存在");
                apiResultSet.setCode(201);
            }
        }else{
            apiResultSet.setMessage("用户或房间号已存在");
            apiResultSet.setCode(201);
        }

        return apiResultSet;
    }


    /**
     * @description:  根据id查询员工表信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet getById(Long id) throws Exception {
        log.info("根据id查询videoConsultant表信息，id:{}", id);
        VideoConsultant videoConsultant = haVideoConsultantManager.selectByid(id);
        if(videoConsultant==null){
            return new ApiResultSet<>(500, "根据id查询videoConsultant表信息失败");
        }
        return ApiResultSet.ok("接口调用成功", videoConsultant);
    }
}

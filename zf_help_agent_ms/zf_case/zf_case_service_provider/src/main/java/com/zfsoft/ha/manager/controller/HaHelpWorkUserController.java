package com.zfsoft.ha.manager.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.ha.data.HaBanner;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.requestData.HaWorkUserRequestData;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.data.vo.HaWorkTurnRecordVo;
import com.zfsoft.ha.manager.HaWorkUserService;
import com.zfsoft.ha.managers.HaWorkQueueManager;
import com.zfsoft.ha.managers.HaWorkTurnRecordManager;
import com.zfsoft.ha.managers.HaWorkUserServiceManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description //用户表控制层
 * @Author: Wangyh
 * @Date: 2022/7/15 14:08
 */
@RestController
@Slf4j
public class HaHelpWorkUserController implements HaWorkUserService {
    /**
     * 用户登录service层
     */
    @Resource
    private HaWorkUserServiceManager haWorkUserServiceManager;

    /**
     * 上传附件信息实现类
     */
    @Resource
    private SysAttaManager sysAttaManager;
    /**
     * 办事队列manager
     */
    @Resource
    private HaWorkQueueManager haWorkQueueManager;
    /**
     * 办事转派记录manager
     */
    @Resource
    private HaWorkTurnRecordManager haWorkTurnRecordManager;



    /**
     * @param file        文件
     * @description: 上传图片
     * @author: wangyh
     * @Date: 2022/7/15 14:08
     **/
    public ApiResultSet uploadImage(HttpServletRequest request,MultipartFile file){
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
     * @description:  查询用户分页信息列表
     * @param haWorkUserRequestData 帮代办工作人员表请求实体类
     * @param pageNumber 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Override
    public ApiResultSet<PageResult<HaWorkUser>> queryUserServiceWithPage(HaWorkUserRequestData haWorkUserRequestData,
                                                                         Integer pageNumber, Integer pageSize) throws Exception {
        log.info("查询用户分页信息列表方法，haWorkUserRequestData:{},pageNumber:{},pageSize:{}",haWorkUserRequestData,pageNumber,pageSize);
        PageResult<HaWorkUser> pageResult = haWorkUserServiceManager.queryUserServiceWithPage(haWorkUserRequestData,pageNumber,pageSize);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<HaWorkUser>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  删除用户信息
     * @param id 账号
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Override
    public ApiResultSet deleteUserOid(Long id) throws Exception{
        log.info("删除用户信息，id:{}",id);
        HaWorkUser haWorkUser = haWorkUserServiceManager.deleteUserid(id);
        log.debug("haWorkUser结果集：haWorkUser:{}",haWorkUser);
        ApiResultSet<HaWorkUser> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(haWorkUser);
        apiResultSet.setMessage("用户删除成功");
        apiResultSet.setTime(String.valueOf(new Date()));
        return apiResultSet;
    }

    /**
     * @description:  批量删除用户信息
     * @param ids 主键
     * @return
     */
    @Override
    public ApiResultSet deleteUsertids(List<Long> ids) throws Exception {
        log.info("批量删除用户信息，ids:{}",ids);
        ApiResultSet<HaWorkUser> apiResultSet = new ApiResultSet<>();
            haWorkUserServiceManager.batchUserid( ids);
            apiResultSet.setMessage("批量删除成功");
            return apiResultSet;
        }

    /**
     * @description:  参数配置的新增或者修改
     * @param haWorkUser 参数配置实体类
     * @return
     */
    @Override
    public ApiResultSet saveThaWorkUser(HaWorkUser haWorkUser) throws Exception{
        log.info("参数配置的新增或者修改，haWorkUser:{}",haWorkUser);
        ApiResultSet<HaWorkUser> apiResultSet = new ApiResultSet<>();
            Map<String,Object> result= haWorkUserServiceManager.saveOrUpdateThaWorkUser(haWorkUser);
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
                    apiResultSet.setMessage("用户已存在");
                    apiResultSet.setCode(201);
                }
            }else{
                apiResultSet.setMessage("用户已存在");
                apiResultSet.setCode(201);
            }

        return apiResultSet;
    }

    /**
     * @description:  重置密码
     * @param id 用户主键
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Override
    public ApiResultSet resetPassword(Long id) throws Exception{
        log.info("重置密码，id:{}",id);
        HaWorkUser haWorkUser = haWorkUserServiceManager.ResetPassword(id);
        log.debug("haWorkUser结果集： haWorkUser:{}",haWorkUser);
        ApiResultSet<HaWorkUser> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(haWorkUser);
        apiResultSet.setMessage("重置密码为:123456");
        apiResultSet.setTime(String.valueOf(new Date()));
        return apiResultSet;
    }

    /**
     * @param id
     * @return
     * @description: 根据id查询User表信息
     */
    @Override
    public ApiResultSet getHaUserById(Long id) throws Exception {
        log.info("根据id查询User表信息，id:{}", id);
        HaWorkUser haWorkUser = haWorkUserServiceManager.selectByid(id);
        if(haWorkUser==null){
            return new ApiResultSet<>(500, "根据id查询User表信息失败");
        }
        return ApiResultSet.ok("接口调用成功", haWorkUser);
    }

    @Override
    public ApiResultSet<PageResult> queryAllWorkQueueListByWorkUserId(HaWorkQueueVo haWorkQueueVo, String beginTime,
                                                                      String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取当前勾选的帮办人服务的全部办事队列的数据，参数haQueueRequestData:  {}", haWorkQueueVo);
        //分页参数
        PageResult<HaWorkQueueVo> resultSet = haWorkQueueManager.queryAllWorkQueueListWithPage(haWorkQueueVo, beginTime, endTime, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    @Override
    public ApiResultSet<PageResult> queryAllTurnServiceListByWorkUserId(HaWorkTurnRecordVo haWorkTurnRecordVo,
                                                                        String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入查询所选帮代办人员的所有的转派服务记录");
        PageResult<HaWorkTurnRecordVo> resultSet = haWorkTurnRecordManager.queryWorkTrunServiceListWithPage(haWorkTurnRecordVo,beginTime,endTime,pageNum,pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }
}

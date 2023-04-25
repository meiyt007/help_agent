package com.zfsoft.ha.manager.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.ha.data.HaBanner;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.manager.HaBannerService;
import com.zfsoft.ha.managers.HaBannerServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description //Banner控制层
 * @Author: Wangyh
 * @Date: 2022/7/26 14:47
 */
@RestController
@Slf4j
public class HaBannerController implements HaBannerService {
    /**
     * 上传附件信息实现类
     */
    @Resource
    private SysAttaManager sysAttaManager;

    /**
     * banner实现业务接口层
     */
    @Resource
    private HaBannerServiceManager haBannerServiceManager;
    /**
     * @param file        文件
     * @description: 上传图片
     * @author: wangyh
     * @Date: 2022/7/15 14:08
     **/
    public ApiResultSet uploadImage(HttpServletRequest request, MultipartFile file){
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
     * @param title        标题
     * @param content      内容
     * @param ableStatus   启禁用状态
     * @param deleteStatus 删除状态
     * @description: 查询banner分页信息列表
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     **/
    @Override
    public ApiResultSet<PageResult<HaBanner>> queryBannerServicePage(String title, String content, String ableStatus, String deleteStatus,
                                                                     Integer pageNumber, Integer pageSize) {
        log.info("查询banner分页信息列表，title:{},content:{},ableStatus:{},deleteStatus:{},pageNumber:{},pageSize:{}", title, content, ableStatus, deleteStatus, pageNumber, pageSize);
        HaBanner haBanner = new HaBanner();
        ApiResultSet<PageResult<HaBanner>> apiResultSet = null;
        haBanner.setContent(content);
        haBanner.setTitle(title);
        haBanner.setAbleStatus(ableStatus);
        haBanner.setDeleteStatus(deleteStatus);
        try {
            PageResult<HaBanner> pageResult = haBannerServiceManager.queryBannerServicePage(haBanner, pageNumber, pageSize);
            log.debug("pageResult结果集,pageResult:{}", pageResult);
            apiResultSet = new ApiResultSet<>();
            apiResultSet.setData(pageResult);
        } catch (Exception e) {
            log.error("查询banner分页信息列表方法错误：", e);
            return new ApiResultSet(500, "查询banner分页信息列表方法错误", e.getMessage());
        }
        return apiResultSet;
    }

    /**
     * @param id 主键
     * @description: 删除banner信息
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Override
    public ApiResultSet<HaBanner> deleteBannerid(Long id) {
        log.info("删除banner信息，id:{}", id);
        try {
            Integer index = haBannerServiceManager.deleteBannerid(id);
            if (index == 1) {
                return ApiResultSet.ok("用户删除成功");
            } else {
                return new ApiResultSet(500, "用户删除失败");
            }
        } catch (Exception e) {
            log.error("删除banner信息方法错误：", e);
            return new ApiResultSet(500, "删除banner信息方法错误", e.getMessage());
        }

    }

    /**
     * @param ids 主键
     * @description: 批量删除banner信息
     * @author: wangyh
     * @Date: 2022年7月26日14:43:36
     **/
    @Override
    public ApiResultSet deleteBannerids(List<Long> ids) throws Exception {
        log.info("批量删除banner信息，ids:{}", ids);
        ApiResultSet<HaWorkUser> apiResultSet = new ApiResultSet<>();
        haBannerServiceManager.batchBannerid(ids);
        apiResultSet.setMessage("批量删除成功");
        return apiResultSet;
    }

    /**
     * @param haBanner 参数配置实体类
     * @description: 参数配置的新增或者修改
     * @author: wangyh
     * @Date: 2022年7月26日14:43:41
     **/
    @Override
    public ApiResultSet saveThaBannerorUpdata(HaBanner haBanner) throws Exception {
        log.info("参数配置的新增或者修改，thaBanner:{}", haBanner);
        Map<String, Object> map = haBannerServiceManager.saveOrUpdateThaWorkUser(haBanner);
        int index = (int) map.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @param id
     * @return
     * @description: 根据id查询banner表信息
     */
    @Override
    public ApiResultSet getHaUserResourceById(Long id) throws Exception {
        log.info("根据id查询banner表信息，id:{}", id);
        HaBanner haBanner = haBannerServiceManager.selectByid(id);
        return ApiResultSet.ok("接口调用成功", haBanner);
    }
}

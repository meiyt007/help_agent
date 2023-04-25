package com.zfsoft.superwindow.controller.yxpz;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.PbpjUser;
import com.zfsoft.superwindow.dbaccess.dao.DbPbpjUserMapper;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjUser;
import com.zfsoft.superwindow.manager.yxpz.PbpjUserManager;
import com.zfsoft.superwindow.service.yxpz.PbpjUserService;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PbpjManageController
 * @Description 平板人员管理管理的实现类
 * @Author liangxm
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class PbpjUserController implements PbpjUserService {
    @Resource
    private PbpjUserManager pbpjUserManager;
    @Autowired
    private SysUserFeginService sysUserFeginService;
    @Resource
    private DbPbpjUserMapper dbPbpjUserMapper;
    @Autowired
    private SysOrganFeginService sysOrganFeginService;
    @Autowired
    private SysDistrictFeignService sysDistrictFeginService;
    @Override
    public ApiResultSet queryPbpjManageWithPage(SysUser user,  Integer pageNum, Integer pageSize) {
        if(null == user) {
            user = new SysUser();
        }
        if(StringUtils.isEmpty(user.getDistrictOid())){
            user.setDistrictOid(CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid());
        }
        if(StringUtils.isEmpty(user.getOrganOid())){
            user.setOrganOid(CurrentLoginUserHolder.getCurrentLoginUser().getOrganOid());
        }
        ApiResultSet<PageResult<SysUser>> pageResult = sysUserFeginService.queryWithPage(user.getDistrictOid(),user.getOrganOid(),user.getName(),1,pageNum,pageSize);
        if(null != pageResult.getData()) {
            List<SysUser> userList = JSONUtil.parseArray(JSONUtil.toJsonStr(JSONUtil.parseObj(pageResult.getData()).get("data"))).toList(SysUser.class);
            if(userList.size()>0){
                for(SysUser usernew : userList){
                    DbPbpjUser dbPbpjUser = dbPbpjUserMapper.selectByUserOid(usernew.getUserOid());
                    if(dbPbpjUser!=null){
                        if(dbPbpjUser.getAppraiseFlag()!=null){
                            usernew.setIsAble(dbPbpjUser.getAppraiseFlag());
                        }
                        if(dbPbpjUser.getConfirmFlag()!=null){
                            usernew.setIsDelete(dbPbpjUser.getConfirmFlag());
                        }
                    }else{
                        usernew.setIsAble(0);
                        usernew.setIsDelete(0);
                    }
                }
                JSONObject jsonObject = JSONUtil.parseObj(pageResult.getData());
                PageResult result = JSONUtil.toBean(jsonObject, PageResult.class);
                PageResult<SysUser> pageResult1 = new PageResult<SysUser>(result.getPageNum(), result.getPageSize(), result.getTotal());
                pageResult1.setData(userList);
                pageResult.setData(pageResult1);
            }
        }
        return pageResult;
    }

    /**
     * @description:  平板人员管理的新增或者修改
     * @param id 平板人员管理实体类
     * @author: liangxm
     * @Date: 2020/10/25 10:14
     **/
    @Override
    public ApiResultSet<SysUser> getOne(String id) {
        ApiResultSet<SysUser> user = sysUserFeginService.getSysUserByUserOid(id);
        SysUser sysUser=user.getData();
        sysUser.setDistrictName(sysDistrictFeginService.getSysDistrictByDistrictOid(sysUser.getDistrictOid()).getData().getName());
        sysUser.setOrganName(sysOrganFeginService.getSysOrganByOrganOid(sysUser.getOrganOid()).getData().getName());
        sysUser.getDistrictOid();
        DbPbpjUser dbPbpjUser = dbPbpjUserMapper.selectByUserOid(sysUser.getUserOid());
        if(dbPbpjUser!=null){
            if(dbPbpjUser.getAppraiseFlag()!=null){
                sysUser.setIsAble(dbPbpjUser.getAppraiseFlag());
            }
            if(dbPbpjUser.getConfirmFlag()!=null){
                sysUser.setIsDelete(dbPbpjUser.getConfirmFlag());
            }
        }else{
            sysUser.setIsAble(0);
            sysUser.setIsDelete(0);
        }
        ApiResultSet<SysUser> userSet = new ApiResultSet<SysUser>();
        userSet.setData(sysUser);
        return userSet;
    }
    @Override
    public int confirmPbpjUser(String id) {
        int index = pbpjUserManager.confirmById(id);
        return index;
    }

    @Override
    public int appraisePbpjUser(String id) {
        int index = pbpjUserManager.appraiseById(id);
        return index;
    }

    @Override
    public ApiResultSet<PbpjUser> getPbpjUserByUserOid(String userOid) {
        PbpjUser pbpjUser = pbpjUserManager.getPbpjUserByUserOid(userOid);
        ApiResultSet<PbpjUser> apiResultSet = new ApiResultSet<PbpjUser>();
        apiResultSet.setData(pbpjUser);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Boolean> ifPbpjUser() {
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<Boolean>();
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        if(null != userOid){
            PbpjUser pbpjUser = pbpjUserManager.getPbpjUserByUserOid(userOid);
            if(null != pbpjUser){
                if(null != pbpjUser.getConfirmFlag() && pbpjUser.getConfirmFlag()==1){
                    apiResultSet.setData(true);
                }
            }
        }else{
            apiResultSet.setData(false);
        }
        return apiResultSet;
    }
}

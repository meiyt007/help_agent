package com.zfsoft.single.controller.yxpz;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.single.data.yxpz.SxServiceRegistrar;
import com.zfsoft.single.manager.yxpz.SxServiceRegistrarManager;
import com.zfsoft.single.service.yxpz.SxServiceRegistrarService;
import com.zfsoft.single.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * dongxl
 * 授权登记人
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SxServiceRegistrarController implements SxServiceRegistrarService {

    private final SxServiceRegistrarManager sxServiceRegistrarManager;

    /**
     * 机构外部接口
     */
    private final SysOrganFeginService sysOrganFeginService;

    private final SysDistrictFeignService sysDistrictFeginService;


    @PostMapping(value = "/pageList")
    public ApiResultSet queryPageList(String districtOid,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return null;
    }




    @Override
    public ApiResultSet saveOrUpdate( SxServiceRegistrar sxServiceRegistrar) {
        String serviceName = sxServiceRegistrar.getServiceName();
        if(StringUtils.isNotEmpty(serviceName)&&serviceName.length()>200){
            return new ApiResultSet(50001,"事项名称长度超过最大值限制,请重新输入!","事项名称长度超过最大值限制,请重新输入!");
        }

        this.sxServiceRegistrarManager.saveOrUpdate(sxServiceRegistrar);
        log.info("登记授权信息新增/更新成功：{}", JSON.toJSONString(sxServiceRegistrar));
        return new ApiResultSet(sxServiceRegistrar);
    }

    /*
     * @Description:取消授权
     * @Author: wangxl
     * @Date: 2020/12/30 11:29
     * @param id:
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.Integer>
     **/
    @Override
    public ApiResultSet<Integer> deleteServiceReg(String serviceOid) {
        Integer  rows = sxServiceRegistrarManager.delSxServiceRegistrarByServiceOid(serviceOid);
        log.info("取消授权成功：{}", serviceOid);
        return new ApiResultSet<Integer>(rows);
    }

    /*
     * @Description:取消人员授权
     * @Author: wangxl
     * @Date: 2020/12/30 14:41
     * @param serviceOid:
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.Integer>
     **/
    @Override
    public ApiResultSet<Integer> deleteServiceUserAuth(String userOid) {
        Integer  rows = sxServiceRegistrarManager.delSxServiceRegistrarByUserOid(userOid);

        log.info("取消授权成功：{}", userOid);
        return new ApiResultSet<Integer>(rows);
    }


    @Override
    public ApiResultSet getOne(String id) {
        SxServiceRegistrar sxServiceRegistrar = this.sxServiceRegistrarManager.getOneByServiceOid(id);
        log.info("详情获取成功：{}", JSON.toJSONString(sxServiceRegistrar));
        return new ApiResultSet(sxServiceRegistrar);
    }


    @Override
    public ApiResultSet getSysUserRegistrarList(String id) {
        List<SysUser> users= this.sxServiceRegistrarManager.getSysUserRegistrarList(id);


        List<String> strList=new ArrayList<>();
        List<String> strDistrictList=new ArrayList<>();
        for(SysUser user:users){
            strList.add("USER-"+user.getUserOid());
            strList.add("ORGAN-"+user.getOrganOid());
            //获取是否有上级机构
            SysOrgan organParent= sysOrganFeginService.getSysOrganByOrganOid(user.getOrganOid()).getData();
            if(null!=organParent&& StringUtils.isNotEmpty(organParent.getOrganOid())){
                strList.add("ORGAN-"+organParent.getOrganOid());
                if( StringUtils.isNotEmpty(organParent.getParentOid())){
                    strList.add("ORGAN-"+organParent.getParentOid());
                    organParent= sysOrganFeginService.getSysOrganByOrganOid(organParent.getParentOid()).getData();
                    if(null!=organParent&& StringUtils.isNotEmpty(organParent.getOrganOid())){
                        strList.add("ORGAN-"+organParent.getOrganOid());
                        if( StringUtils.isNotEmpty(organParent.getParentOid())){
                            strList.add("ORGAN-"+organParent.getParentOid());
                            organParent= sysOrganFeginService.getSysOrganByOrganOid(organParent.getParentOid()).getData();
                            if(null!=organParent&& StringUtils.isNotEmpty(organParent.getOrganOid())){
                                strList.add("ORGAN-"+organParent.getOrganOid());
                                if( StringUtils.isNotEmpty(organParent.getParentOid())){
                                    strList.add("ORGAN-"+organParent.getParentOid());
                                    organParent= sysOrganFeginService.getSysOrganByOrganOid(organParent.getParentOid()).getData();
                                }else{
                                    strList.add("DISTRICT-"+organParent.getDistrictOid());
                                    if(!strDistrictList.contains(organParent.getDistrictOid())){
                                        strDistrictList.add(organParent.getDistrictOid());
                                    }

                                }
                            }
                        }else{
                            strList.add("DISTRICT-"+organParent.getDistrictOid());
                            if(!strDistrictList.contains(organParent.getDistrictOid())){
                                strDistrictList.add(organParent.getDistrictOid());
                            }
                        }
                    }
                }else{
                    strList.add("DISTRICT-"+organParent.getDistrictOid());
                    if(!strDistrictList.contains(organParent.getDistrictOid())){
                        strDistrictList.add(organParent.getDistrictOid());
                    }
                }
            }
        }
        for(String str:strDistrictList){
            SysDistrict parentDistrict= sysDistrictFeginService.getSysDistrictByDistrictOid(str.replace("DISTRICT-","")).getData();
            if(null!=parentDistrict&&StringUtils.isNotEmpty(parentDistrict.getParentOid())){
                strList.add("DISTRICT-"+parentDistrict.getParentOid());
                parentDistrict= sysDistrictFeginService.getSysDistrictByDistrictOid(parentDistrict.getParentOid()).getData();
                if(null!=parentDistrict&&StringUtils.isNotEmpty(parentDistrict.getParentOid())){
                    strList.add("DISTRICT-"+parentDistrict.getParentOid());
                    parentDistrict= sysDistrictFeginService.getSysDistrictByDistrictOid(parentDistrict.getParentOid()).getData();
                    if(null!=parentDistrict&&StringUtils.isNotEmpty(parentDistrict.getParentOid())){
                        strList.add("DISTRICT-"+parentDistrict.getParentOid());
                        parentDistrict= sysDistrictFeginService.getSysDistrictByDistrictOid(parentDistrict.getParentOid()).getData();
                        if(null!=parentDistrict&&StringUtils.isNotEmpty(parentDistrict.getParentOid())){
                            strList.add("DISTRICT-"+parentDistrict.getParentOid());
                        }
                    }
                }
            }
        }
        ApiResultSet<List<String>> resultSet = new ApiResultSet<>();
        resultSet.setData(strList);
        return resultSet;
    }


    @Override
    public ApiResultSet saveOrUpdatePersonReg(SxServiceRegistrar sxServiceRegistrar) {
        this.sxServiceRegistrarManager.saveOrUpdatePersonReg(sxServiceRegistrar);
        log.info("事项人员授权新增/更新成功：{}", JSON.toJSONString(sxServiceRegistrar));
        return new ApiResultSet(sxServiceRegistrar);
    }


    @Override
    public ApiResultSet getOneByUserOid(String id) {
        SxServiceRegistrar sxServiceRegistrar = this.sxServiceRegistrarManager.getOneByUserOid(id);
        log.info("详情获取成功：{}", JSON.toJSONString(sxServiceRegistrar));
        return new ApiResultSet(sxServiceRegistrar);
    }


    @Override
    public ApiResultSet queryServiceTree() {
        List<TreeSelect> serviceTree=this.sxServiceRegistrarManager.queryServiceTree();
        return new ApiResultSet(serviceTree);
    }

    @Override
    public ApiResultSet queryUserTree() {
        List<TreeSelect> userTree = sxServiceRegistrarManager.queryServiceTreeForUser();
        return new ApiResultSet(userTree);
    }
    @Override
    public ApiResultSet sxServiceOidsListByUserOid() {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        List serviceOids=this.sxServiceRegistrarManager.sxServiceOidsListByUserOid(currentLoginUser.getUserOid());
        return new ApiResultSet(serviceOids);
    }

    @Override
    public ApiResultSet getServiceRegistrarByServiceOid(String serviceOid) {
        boolean result = this.sxServiceRegistrarManager.getServiceRegistrarByServiceOid(serviceOid);
        return new ApiResultSet(result);
    }

}

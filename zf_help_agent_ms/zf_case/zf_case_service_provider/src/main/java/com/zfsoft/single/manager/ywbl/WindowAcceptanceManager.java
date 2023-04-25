package com.zfsoft.single.manager.ywbl;

import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.util.GenDataTreeUtil;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.vo.SysUserVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.single.data.yxpz.SxServiceRegistrar;
import com.zfsoft.single.dbaccess.dao.DbSxServiceRegistrarMapper;
import com.zfsoft.single.dbaccess.data.DbSxServiceRegistrar;
import com.zfsoft.service.sxService.data.SxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @（#）: WindowAcceptanceManager
 * @description: 窗口办理接口实现类
 * @author: wangwg
 * @date: 2020/10/27
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "dzcpt:windowAcceptance")
public class WindowAcceptanceManager {

    @Resource
    private DbSxServiceRegistrarMapper dbSxServiceRegistrarMapper;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private SysUserFeginService sysUserFeginService;


    public List<SxService> listWindowAcceptancePage(String serviceName, String organOid, String serviceType, String serviceOids, String userOid, String districtOid, Integer pageNum, Integer pageSize) {
        List<String> oidList = null;
        if(serviceOids !=null){
            oidList = Arrays.asList(serviceOids.split(","));
        }
        //查询事项人员授权信息
        SxServiceRegistrar registrar = new SxServiceRegistrar();
        registrar.setUserOid(userOid);
        registrar.setServiceName(serviceName);
        registrar.setServiceType(serviceType);
        registrar.setServiceOidList(oidList);
        DbSxServiceRegistrar dbSxServiceRegistrar = new DbSxServiceRegistrar();
        BeanUtils.copyProperties(registrar, dbSxServiceRegistrar);
        Map<String, SxService> sxServiceMap = new LinkedHashMap<>();
        List<DbSxServiceRegistrar> resultRegistrars = dbSxServiceRegistrarMapper.selectSxServiceRegistrarList(dbSxServiceRegistrar);
        if(resultRegistrars !=null){
            for (DbSxServiceRegistrar dbSxServiceRegistrar1:resultRegistrars) {
                ApiResultSet<SxService> serviceRegistrar = sxServiceFeginService.getSxServiceByOid(dbSxServiceRegistrar1.getServiceOid());
                if(serviceRegistrar.getData() != null){
                    Short delFlag = serviceRegistrar.getData().getDelFlag();
                    if (delFlag ==0) {
                        sxServiceMap.put(serviceRegistrar.getData().getServiceOid(),serviceRegistrar.getData());
                    }
                }

            }
        }
        //查询事项辖区授权信息
        /*ServiceSiteAuthorize serviceSiteAuthorize = new ServiceSiteAuthorize();
        serviceSiteAuthorize.setServiceName(serviceName);
        serviceSiteAuthorize.setServiceType(serviceType);
        serviceSiteAuthorize.setServiceOidList(oidList);
        DbServiceSiteAuthorize dbServiceSiteAuthorize = new DbServiceSiteAuthorize();
        BeanUtils.copyProperties(serviceSiteAuthorize, dbServiceSiteAuthorize);

        //查询用户所在的区划的辖区
        ApiResultSet<List<SysSiteDistrictRelation>> list=sysSiteDistrictRelationFeginService.siteDistrictRelListByDistOid("DISTRICT-"+CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid());
        List<SysSiteDistrictRelation> dbSysSiteDistrictRelations=null;
        if(list!=null&&list.getData()!=null&&list.getData().size()>0){
            dbSysSiteDistrictRelations=list.getData();
        }
        if(null != dbSysSiteDistrictRelations && dbSysSiteDistrictRelations.size()>0){
            //查询辖区下面所有的授权事项
            for(SysSiteDistrictRelation relate:dbSysSiteDistrictRelations){
                dbServiceSiteAuthorize.setSiteOid(relate.getSysAreaSiteOid());
                List<DbServiceSiteAuthorize> dbServiceSiteAuthorizes =dbServiceSiteAuthorizeMapper.selectDbServiceSiteAuthorizList(dbServiceSiteAuthorize);
                if(dbServiceSiteAuthorizes!=null&&dbServiceSiteAuthorizes.size()>0){
                    for (DbServiceSiteAuthorize authors:dbServiceSiteAuthorizes){
                        ApiResultSet<SxService> resultSiteAuthorize = sxServiceFeginService.getSxServiceByOid(authors.getServiceOid());
                        if(resultSiteAuthorize.getData() != null){
                            sxServiceMap.put(authors.getServiceOid(),resultSiteAuthorize.getData());
                        }
                    }
                }
            }
        }*/

        List<SxService> sxServiceList = new LinkedList<SxService>();
        if(null != sxServiceMap && sxServiceMap.size()>0){
            SxService sxService =null;
            for(Map.Entry<String, SxService> sxServiceEntry : sxServiceMap.entrySet()){
                sxService = new SxService();
                BeanUtils.copyProperties(sxServiceEntry.getValue(),sxService);
                sxServiceList.add(sxService);
            }
        }
        return sxServiceList;
    }

    public List<TreeSelect> queryUserSimpleTree() {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if (null != currentLoginUser) {
            ApiResultSet resultSet = this.sysDistrictFeginService.querySysDistrictListByParentOid(null);
            List<SysDistrict> sysDistrictList= (List<SysDistrict>) resultSet.getData();
            ApiResultSet<List<SysOrgan>> sysOrganResultSet=this.sysOrganFeginService.querySysOrganListByParentOid(null);
            List<SysOrgan> sysOrganList=sysOrganResultSet.getData();
            ApiResultSet<List<SysUserVo>> allUserVoList = this.sysUserFeginService.getAllUserVoList();
            List<SysUserVo> usrList=null;
            if(allUserVoList!=null&&allUserVoList.getCode()==200){
                usrList=  allUserVoList.getData();
            }
            if(usrList!=null){
                List<TreeSelect> treeSelects = GenDataTreeUtil.buildUserTreeSelect(sysDistrictList, sysOrganList, usrList);
                return treeSelects;
            }
        }
        return null;
    }
}

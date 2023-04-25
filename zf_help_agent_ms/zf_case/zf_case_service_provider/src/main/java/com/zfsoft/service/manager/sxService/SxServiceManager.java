package com.zfsoft.service.manager.sxService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.service.dbaccess.dao.sxService.DbRefinedMaterialMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceExtendMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMaterialMapper;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbServiceMaterialMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.dbaccess.data.sxSituation.DbServiceMaterial;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.manager.sxDirectory.SxDirectoryManager;
import com.zfsoft.service.manager.sxDirectory.SxServiceTypeManager;
import com.zfsoft.service.manager.sxSituation.*;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.manager.sxSys.SxSysDictManager;
import com.zfsoft.service.sxDirectory.data.SxDirectory;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxSituation.data.*;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceServiceImpl
 * @Description: 实施清单实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
public class SxServiceManager {

    @Resource
    private DbSxServiceMapper dbSxServiceMapper;
    @Resource
    private SxServiceTypeManager sxServiceTypeManager;
    @Resource
    private SxSysDictManager sxSysDictManager;
    @Resource
    private SxDirectoryManager sxDirectoryManager;
    @Resource
    private SysDistrictFeignService sysDistrictFeignService;
    @Resource
    private SysOrganFeginService sysOrganFeignService;
    @Resource
    private SxServiceSituationManager sxServiceSituationManager;
    @Resource
    private SxServiceSituationOptionManager sxServiceSituationOptionManager;
    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;
    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;
    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;
    @Resource
    private ServiceMaterialManager serviceMaterialManager;
    @Resource
    private SxSysAttaManager sxSysAttaManager;
    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;
    @Resource
    private ServiceOptionRelManager serviceOptionRelManager;

    @Resource
    private DbSxServiceMaterialMapper dbSxServiceMaterialMapper;
    @Resource
    private DbServiceMaterialMapper dbServiceMaterialMapper;

    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;
    @Resource
    private DbRefinedMaterialMapper dbRefinedMaterialMapper;

    public SxService getSxServiceByOidEs(String oid) {
        SxService sxService = new SxService();
        DbSxServiceWithBLOBs dbSxServiceWithBLOBs = dbSxServiceMapper.getSxServiceByOid(oid);
        if(dbSxServiceWithBLOBs == null){
            return sxService;
        }
        BeanUtils.copyProperties(dbSxServiceWithBLOBs,sxService);
        if(StrUtil.isNotEmpty(sxService.getDistrictOid())){
            SysDistrict district = sysDistrictFeignService.getSysDistrictByDistrictOid(sxService.getDistrictOid()).getData();
            if(null != district ){
                sxService.setDistrictName(district.getName());
            }
        }
        return sxService;
    }
    //@Cacheable(key = "'getSxServiceByOid:oid=' + #oid", unless = "#result == null")
    public SxService getSxServiceByOid(String oid) {
        DbSxServiceWithBLOBs dbSxServiceWithBLOBs = dbSxServiceMapper.getSxServiceByOid(oid);
        if(dbSxServiceWithBLOBs == null){
            throw new ResultInfoException("实施清单信息为空！");
        }
        SxService sxService = new SxService();
        BeanUtils.copyProperties(dbSxServiceWithBLOBs,sxService);
        if(StrUtil.isNotEmpty(sxService.getDistrictOid())){
            SysDistrict district = sysDistrictFeignService.getSysDistrictByDistrictOid(sxService.getDistrictOid()).getData();
            if(null != district ){
                sxService.setDistrictName(district.getName());
            }
        }
        //事项类型名称
        if(StrUtil.isNotEmpty(sxService.getServiceTypeOid())){
            SxServiceType sxServiceType = sxServiceTypeManager.getSxServiceTypeByOid(sxService.getServiceTypeOid());
            if(null != sxServiceType){
                sxService.setServiceTypeName(sxServiceType.getServiceTypeName());
            }
        }
        //实施部门名称
        if(StrUtil.isNotEmpty(sxService.getOrganOid())){
            SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(sxService.getOrganOid()).getData();
            if(null != sysOrgan){
                sxService.setOrganName(sysOrgan.getName());
            }
        }

        return sxService;
    }

    //@Cacheable(key = "'viewSxServiceByOid:serviceOid=' + #serviceOid", unless = "#result == null")
    public SxService viewSxServiceByOid(String serviceOid){
        if(StrUtil.isNotEmpty(serviceOid)){
            SxService sxService = this.getSxServiceByOid(serviceOid);
            if(null == sxService){
                throw  new ResultInfoException("实施清单信息为空");
            }
            //事项类型名称
            if(StrUtil.isNotEmpty(sxService.getServiceTypeOid())){
                SxServiceType sxServiceType = sxServiceTypeManager.getSxServiceTypeByOid(sxService.getServiceTypeOid());
                if(null != sxServiceType){
                    sxService.setServiceTypeName(sxServiceType.getServiceTypeName());
                }
            }
            //区划名称
            if(StrUtil.isNotEmpty(sxService.getDistrictOid())){
                SysDistrict district = sysDistrictFeignService.getSysDistrictByDistrictOid(sxService.getDistrictOid()).getData();
                if(null != district){
                    sxService.setDistrictName(district.getName());
                }
            }
            //实施部门名称
            if(StrUtil.isNotEmpty(sxService.getOrganOid())){
                SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(sxService.getOrganOid()).getData();
                if(null != sysOrgan){
                    sxService.setOrganName(sysOrgan.getName());
                }
            }
            //实施主体性质
            if(StrUtil.isNotEmpty(sxService.getImplementOrganProperty())){
                sxService.setImplementOrganPropertyName(SxptBaseStaticParameter.organPropertyMaps.get(sxService.getImplementOrganProperty()).getName());
            }
            //办理类型
            if(StrUtil.isNotEmpty(sxService.getHandleType())){
                sxService.setHandleTypeName(SxptBaseStaticParameter.HANDLE_TYPE_MAP.get(sxService.getHandleType()));
            }
            //办件类型
            if(null != sxService.getCaseType()){
                sxService.setCaseTypeName(SxptBaseStaticParameter.CASE_TYPE_MAP.get(sxService.getCaseType()));
            }
            //办件状态
            if(null != sxService.getServiceStatus()){
                sxService.setServiceStatusName(SxptBaseStaticParameter.SERVICE_STATUS.get(sxService.getServiceStatus()));
            }
            //是否收费
            if(null != sxService.getChargeFlag()){
                sxService.setChargeFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getChargeFlag()));
            }
            //服务对象
            if(null != sxService.getServiceObject()){
                String[] serviceObjs = sxService.getServiceObject().split(",");
                StringBuffer bf = new StringBuffer();
                if(serviceObjs.length>0){
                    for(int j=0;j<serviceObjs.length;j++) {
                        bf.append(SxptBaseStaticParameter.SERVICE_OBJECT_MAP.get(serviceObjs[j])+" ");
                    }
                }else {
                    bf.append(SxptBaseStaticParameter.SERVICE_OBJECT_MAP.get(sxService.getServiceObject()));
                }
                sxService.setServiceObjectName(bf.toString());
            }
            //办理形式(老版本是单选的字典值,3.0才是多选的静态值)
            if(StrUtil.isNotEmpty(sxService.getHandleForm())){
                String[] forms = sxService.getHandleForm().split(",");
                StringBuffer formNames = new StringBuffer();
                if(forms.length>0){
                    for(int i=0;i<forms.length;i++){
                        formNames.append(SxptBaseStaticParameter.HANDLE_FORM_MAP.get(forms[i])+" ");
                    }
                }else{
                    formNames.append(SxptBaseStaticParameter.HANDLE_FORM_MAP.get(sxService.getHandleForm()));
                }
                sxService.setHandleFormName(formNames.toString());
            }
            //是否网上支付
            if(null != sxService.getOnlinePayFlag()){
                sxService.setOnlinePayFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getOnlinePayFlag()));
            }
            //是否支持在线预约
            if(null != sxService.getAppointmentFlag()){
                sxService.setAppointmentFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getAppointmentFlag()));
            }
            //是否支持物流快递
            if(null != sxService.getExpressFlag()){
                sxService.setExpressFlagName(SxptBaseStaticParameter.EXPRESS_NO_YES_MAP.get(sxService.getExpressFlag()));
            }
            //是否联合办理
            if(null != sxService.getUnionOrganFlag()){
                sxService.setUnionOrganFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getUnionOrganFlag()));
            }
            //行政管辖地
            if(StrUtil.isNotEmpty(sxService.getAdminJurisdiction())){
                sxService.setAdminJurisdictionName(SxptBaseStaticParameter.ADMIN_JURISDICTION_MAP.get(sxService.getAdminJurisdiction()));
            }
            //办理深度(老版本是单选的字典值,3.0才是多选的静态值)
            if(StrUtil.isNotEmpty(sxService.getHandleDepth())){
                sxService.setHandleDepthName(sxSysDictManager.getSxSysDictNameByDictOids(sxService.getHandleDepth()));
            }
            //事项性质
            if(StrUtil.isNotEmpty(sxService.getServiceCharacter())){
                sxService.setServiceCharacterName(SxptBaseStaticParameter.SERVICE_CHARACTER.get(sxService.getServiceCharacter()));
            }
            //主题分类
            if(StrUtil.isNotEmpty(sxService.getSubjectClassification())){
                sxService.setSubjectClassificationName(sxSysDictManager.getSxSysDictNameByDictOids(sxService.getSubjectClassification()));
            }
            //生命周期分类
            if(StrUtil.isNotEmpty(sxService.getLifeCycleClassification())){
                sxService.setLifeCycleClassificationName(sxSysDictManager.getSxSysDictNameByDictOids(sxService.getLifeCycleClassification()));
            }
            //办事群体分类
            if(StrUtil.isNotEmpty(sxService.getServiceGroupClassification())){
                sxService.setServiceGroupClassificationName(sxSysDictManager.getSxSysDictNameByDictOids(sxService.getServiceGroupClassification()));
            }
            //公开方式
            if(null != sxService.getOpenWay()){
                sxService.setOpenWayName(SxptBaseStaticParameter.OPEN_WAY_MAP.get(sxService.getOpenWay()));
            }
            //公开渠道
            if(null != sxService.getOpenChannel()){
                sxService.setOpenChannelName(SxptBaseStaticParameter.OPEN_CHANNEL_MAP.get(sxService.getOpenChannel()));
            }
            //办理结果送达方式
            if(null != sxService.getResultDeliveryWay()){
                String[] ways = sxService.getResultDeliveryWay().split(",");
                StringBuffer wayNames = new StringBuffer();
                if(ways.length>0){
                    for(int k=0;k<ways.length;k++){
                        wayNames.append(SxptBaseStaticParameter.RESULT_DELIVERY_WAY_MAP.get(ways[k])+" ");
                    }
                }else{
                    wayNames.append(SxptBaseStaticParameter.RESULT_DELIVERY_WAY_MAP.get(sxService.getResultDeliveryWay()));
                }
                sxService.setResultDeliveryWayName(wayNames.toString());
            }
            //收费环节
            if(StrUtil.isNotEmpty(sxService.getChargeLinkOid())){
                sxService.setChargeLinkName(sxSysDictManager.getSxSysDictByOid(sxService.getChargeLinkOid()).getName());
            }
            //所属目录
            if(StrUtil.isNotEmpty(sxService.getDirectoryOid())){
                SxDirectory sxDirectory= sxDirectoryManager.getSxDirectoryByOid(sxService.getDirectoryOid());
                if(null!=sxDirectory){
                    sxService.setDirectoryName(sxDirectory.getDirectoryName());
                }
            }
            //权力来源
            if(StrUtil.isNotEmpty(sxService.getOrigin())){
                sxService.setOriginName(SxptBaseStaticParameter.originMaps.get(sxService.getOrigin()).getName());
            }
            //所属是否有中介服务事项(0否、1是)
            if(null != sxService.getIsZjfw()){
                sxService.setIsZjfwName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getIsZjfw()));
            }
            //是否特殊程序(0否、1是)
            if(null != sxService.getIsSpecial()){
                sxService.setIsSpecialName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getIsSpecial()));
            }
            //是否支持人证核身标识
            if(null != sxService.getShowFlag()){
                sxService.setShowFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getShowFlag()));
            }
            //移动端是否对接单点登录(0否、1是)
            if(null != sxService.getAppIssingleLogin()){
                sxService.setAppIssingleLoginName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getAppIssingleLogin()));
            }
            //计算机端是否对接单点登录(0否、1是)
            if(StrUtil.isNotEmpty(sxService.getIsSingleLogin())){
                sxService.setIsSingleLoginName(SxptBaseStaticParameter.NO_YES_MAP.get(Integer.valueOf(sxService.getIsSingleLogin()).shortValue()));
            }
            //是否配置填报系统(0否、1是)
            if(StrUtil.isNotEmpty(sxService.getFormFlag())){
                sxService.setFormFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(Integer.valueOf(sxService.getFormFlag()).shortValue()));
            }
            //是否存在子项(0否、1是)
            if(StrUtil.isNotEmpty(sxService.getExistChildItem())){
                sxService.setExistChildItemName(SxptBaseStaticParameter.NO_YES_MAP.get(Integer.valueOf(sxService.getExistChildItem()).shortValue()));
            }
            //父级主键（若为办理项，该字段不能为空）
            if(StrUtil.isNotEmpty(sxService.getServiceParentOid())){
                sxService.setServiceParentName(getSxServiceByOid(sxService.getServiceParentOid()).getServiceName());
            }
            //投诉方式
            if(StrUtil.isNotEmpty(sxService.getTsType())){
                String[] tyTypes = sxService.getTsType().split(",");
                StringBuffer tyTypeNams = new StringBuffer();
                if(tyTypes.length>0){
                    for(int ii=0;ii<tyTypes.length;ii++){
                        tyTypeNams.append(SxptBaseStaticParameter.SX_ZXTS_TYPE_MAP.get(tyTypes[ii])+"  ");
                    }
                    sxService.setTsTypeName(tyTypeNams.toString());
                }
            }
            //咨询方式
            if(StrUtil.isNotEmpty(sxService.getZxType())) {
                String[] zyTypes = sxService.getZxType().split(",");
                StringBuffer zyTypeNams = new StringBuffer();
                if (zyTypes.length > 0) {
                    for (int ii = 0; ii < zyTypes.length; ii++) {
                        zyTypeNams.append(SxptBaseStaticParameter.SX_ZXTS_TYPE_MAP.get(zyTypes[ii]) + "  ");
                    }
                    sxService.setZxTypeName(zyTypeNams.toString());
                }
            }
            //是否支持终端办理(0否、1是)
            if(null != sxService.getTerminalProcessing()){
                sxService.setTerminalProcessingName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getTerminalProcessing()));
            }
            //是否支持人证核身标识
            if(null != sxService.getShowRzhs()){
                sxService.setShowRzhsName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getShowRzhs()));
            }
            //创建人(基础平台改成根据业务主健获取)
            if(StrUtil.isNotEmpty(sxService.getCreateUser())){
                //sxService.setCreateUserName(sysUserFeignService.getSysUserById());
            }
            //是否删除
//                if(null != sxService.getDelFlag()){
//                    sxService.setDelFlagName(SxptBaseStaticParameter.NO_YES_MAP.get(sxService.getDelFlag()));
//                }
            return sxService;
        }
        return null;
    }

    //@Cacheable(key = "'querySxServiceWithPage:sxService=' + #sxService+',pageNumber=' + #pageNumber+',pageSize=' + #pageSize", unless = "#result == null")
    public PageResult<SxService>  querySxServiceWithPage(SxService sxService , Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        if(null!=sxService){
            if(StrUtil.isNotEmpty(sxService.getServiceName())){
                criteria.andServiceNameLike("%"+sxService.getServiceName()+"%");
            }
            if(StrUtil.isNotEmpty(sxService.getBasicCode())){
                criteria.andBasicCodeLike(sxService.getBasicCode());
            }
            if(StrUtil.isNotEmpty(sxService.getServiceTypeOid())){
                criteria.andServiceTypeOidEqualTo(sxService.getServiceTypeOid());
            }
            if(StrUtil.isNotEmpty(sxService.getImplementCode())){
                criteria.andImplementCodeEqualTo(sxService.getImplementCode());
            }
            if(null != sxService.getServiceStatus()){
                criteria.andServiceStatusEqualTo(sxService.getServiceStatus());
            }
            if(StrUtil.isNotEmpty(sxService.getDistrictOid())){
                criteria.andDistrictOidEqualTo(sxService.getDistrictOid());
            }
            if(StrUtil.isNotEmpty(sxService.getOrganOid())){
                criteria.andOrganOidEqualTo(sxService.getOrganOid());
            }
            if(StrUtil.isNotEmpty(sxService.getOrganOids())){
                String[] oids = sxService.getOrganOids().split(",");
                List<String> oidList = new ArrayList<>(Arrays.asList(oids));
                criteria.andOrganOidIn(oidList);
            }
            if(StrUtil.isNotEmpty(sxService.getExistChildItem())){
                //0不存在子项 只查询办理项，如果存在子项，则不展示父项
                criteria.andExistChildItemEqualTo(sxService.getExistChildItem().trim());
            }
            if(sxService.getCaseType() !=null){
                //0不存在子项 只查询办理项，如果存在子项，则不展示父项
                criteria.andCaseTypeEqualTo(sxService.getCaseType());
            }
            // 好办快办参数
            if(sxService.getHandleType() !=null){
                criteria.andHandleTypeEqualTo(sxService.getHandleType());
            }
            // 是否屏蔽
            if(sxService.getIsShield() !=null){
                criteria.andIsShieldEqualTo(sxService.getIsShield());
            }
        }
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        //criteria.andExistChildItemEqualTo("0");
        dbSxServiceExample.setOrderByClause("CREATE_DATE DESC");
        Page<DbSxService> dbSxServices = (Page<DbSxService>)dbSxServiceMapper.selectByExample(dbSxServiceExample);
        PageResult<SxService> pageResult = new PageResult<>(dbSxServices.getPageNum(),dbSxServices.getPageSize(),dbSxServices.getTotal());
        List<SxService> sxServiceList = dbSxServices.stream().map(dbSxService -> {
            SxService service = new SxService();
            //事项类型名称
            if(StrUtil.isNotEmpty(dbSxService.getServiceTypeOid())){
                SxServiceType sxServiceType = sxServiceTypeManager.getSxServiceTypeByOid(dbSxService.getServiceTypeOid());
                if(null != sxServiceType){
                    service.setServiceTypeName(sxServiceType.getServiceTypeName());
                }
            }
            BeanUtils.copyProperties(dbSxService,service);
            //区划名称
            if(StrUtil.isNotEmpty(service.getDistrictOid())){
                SysDistrict district = sysDistrictFeignService.getSysDistrictByDistrictOid(service.getDistrictOid()).getData();
                if(null != district){
                    service.setDistrictName(district.getName());
                }
            }
            //实施部门名称
            if(StrUtil.isNotEmpty(service.getOrganOid())){
                SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(service.getOrganOid()).getData();
                if(null != sysOrgan){
                    service.setOrganName(sysOrgan.getName());
                }
            }
            return service;
        }).collect(Collectors.toList());
        pageResult.setData(sxServiceList);
        return pageResult;
    }

    public PageResult<SxService>  querySxServiceSortWithPage(SxService sxService , Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        if(null!=sxService){
            if(StrUtil.isNotEmpty(sxService.getServiceName())){
                criteria.andServiceNameLike("%"+sxService.getServiceName()+"%");
            }
            if(StrUtil.isNotEmpty(sxService.getBasicCode())){
                criteria.andBasicCodeLike(sxService.getBasicCode());
            }
            if(StrUtil.isNotEmpty(sxService.getServiceTypeOid())){
                criteria.andServiceTypeOidEqualTo(sxService.getServiceTypeOid());
            }
            if(StrUtil.isNotEmpty(sxService.getImplementCode())){
                criteria.andImplementCodeEqualTo(sxService.getImplementCode());
            }
            if(null != sxService.getServiceStatus()){
                criteria.andServiceStatusEqualTo(sxService.getServiceStatus());
            }
            if(StrUtil.isNotEmpty(sxService.getDistrictOid())){
                criteria.andDistrictOidEqualTo(sxService.getDistrictOid());
            }
            if(StrUtil.isNotEmpty(sxService.getOrganOid())){
                criteria.andOrganOidEqualTo(sxService.getOrganOid());
            }
            if(StrUtil.isNotEmpty(sxService.getOrganOids())){
                String[] oids = sxService.getOrganOids().split(",");
                List<String> oidList = new ArrayList<>(Arrays.asList(oids));
                criteria.andOrganOidIn(oidList);
            }
            if(StrUtil.isNotEmpty(sxService.getExistChildItem())){
                //0不存在子项 只查询办理项，如果存在子项，则不展示父项
                criteria.andExistChildItemEqualTo(sxService.getExistChildItem().trim());
            }
            if(sxService.getCaseType() !=null){
                //0不存在子项 只查询办理项，如果存在子项，则不展示父项
                criteria.andCaseTypeEqualTo(sxService.getCaseType());
            }
            // 好办快办参数
            if(sxService.getHandleType() !=null){
                criteria.andHandleTypeEqualTo(sxService.getHandleType());
            }
        }
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        //criteria.andExistChildItemEqualTo("0");
        dbSxServiceExample.setOrderByClause("CREATE_DATE DESC");
        Page<DbSxService> dbSxServices = (Page<DbSxService>)dbSxServiceMapper.selectByExampleSort(dbSxServiceExample);
        PageResult<SxService> pageResult = new PageResult<>(dbSxServices.getPageNum(),dbSxServices.getPageSize(),dbSxServices.getTotal());
        List<SxService> sxServiceList = dbSxServices.stream().map(dbSxService -> {
            SxService service = new SxService();
            //事项类型名称
            if(StrUtil.isNotEmpty(dbSxService.getServiceTypeOid())){
                SxServiceType sxServiceType = sxServiceTypeManager.getSxServiceTypeByOid(dbSxService.getServiceTypeOid());
                if(null != sxServiceType){
                    service.setServiceTypeName(sxServiceType.getServiceTypeName());
                }
            }
            BeanUtils.copyProperties(dbSxService,service);
            //区划名称
            if(StrUtil.isNotEmpty(service.getDistrictOid())){
                SysDistrict district = sysDistrictFeignService.getSysDistrictByDistrictOid(service.getDistrictOid()).getData();
                if(null != district){
                    service.setDistrictName(district.getName());
                }
            }
            //实施部门名称
            if(StrUtil.isNotEmpty(service.getOrganOid())){
                SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(service.getOrganOid()).getData();
                if(null != sysOrgan){
                    service.setOrganName(sysOrgan.getName());
                }
            }
            return service;
        }).collect(Collectors.toList());
        pageResult.setData(sxServiceList);
        return pageResult;
    }

   // @Cacheable(key = "'getSxServicList:ser=' + #ser", unless = "#result == null")
    public List<SxService> getSxServicList(SxService ser){
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        if(null != ser && StrUtil.isNotEmpty(ser.getOrganOid())){
            criteria.andOrganOidEqualTo(ser.getOrganOid());
        }
        if(null != ser && StrUtil.isNotEmpty(ser.getDistrictOid())){
            criteria.andDistrictOidEqualTo(ser.getDistrictOid());
        }
        if(null != ser && StrUtil.isNotEmpty(ser.getServiceTypeOid())){
            criteria.andServiceTypeOidEqualTo(ser.getServiceTypeOid());
        }
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        criteria.andExistChildItemEqualTo("0");
        criteria.andServiceStatusEqualTo((short) 3);
        List<DbSxService> dbSxServices = dbSxServiceMapper.selectByExample(dbSxServiceExample);
        // 原接口改造 modifyBy 20220625 wangyg
        List<SxServiceType> sxServiceTypeList = sxServiceTypeManager.getSxServiceTypeList();
        Map<String, String> sxServiceTypeMap = sxServiceTypeList.stream().collect(Collectors.toMap(SxServiceType::getServiceTypeOid, SxServiceType::getServiceTypeName));
        List<SxService> sxServiceList = dbSxServices.stream().map(dbSxService -> {
            SxService service = new SxService();
            //事项类型名称
            if(StrUtil.isNotEmpty(dbSxService.getServiceTypeOid())){
                String serviceTypeName = sxServiceTypeMap.get(dbSxService.getServiceTypeOid());
                service.setServiceTypeName(serviceTypeName);
            }
            BeanUtils.copyProperties(dbSxService,service);
            return service;
        }).collect(Collectors.toList());
        return sxServiceList;
    }

    /**
     * 根据serviceOid 获取该事项下的情形 选项 颗粒化材料 事项材料 数据
     * @param serviceOid
     * @return
     */
    public Map<String, Object> getStringObjectMap(String serviceOid) {
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isNotEmpty(serviceOid)) {
            //事项信息
            SxService sxService = this.getSxServiceByOid(serviceOid);
            returnMap.put("sxService", sxService);
            //事项情形
            List<SxServiceSituation> sxServiceSituations = this.sxServiceSituationManager.getSxServiceSituationsByServiceOid(serviceOid);
            if(!CollectionUtils.isEmpty(sxServiceSituations)){
                for (SxServiceSituation sxServiceSituation: sxServiceSituations) {
                    //根据事项情形oid查询情形、选项关系
                    SxServiceSituationOption sxServiceSituationOption = this.sxServiceSituationOptionManager.getSxServiceSituationOptionBySituationOid(sxServiceSituation.getOid());
                    if(!ObjectUtils.isEmpty(sxServiceSituationOption)){
                        //根据sxServiceSituationOption的 optionOid 事项选项主键，查询对应的选项值
                        SxServiceOptionVal sxServiceOptionVal = this.sxServiceOptionValManager.getSxServiceOptionValByOid(sxServiceSituationOption.getOptionOid());
                        if(!ObjectUtils.isEmpty(sxServiceOptionVal)){
                            //查询对应的选项标题
                            SxServiceOptionTitle sxServiceOptionTitle = this.sxServiceOptionTitleManager.getServiceOptionTitleByOid(sxServiceOptionVal.getTitleOid());
                            //查询该选项标题对应的所有选项值
                            List<SxServiceOptionVal> sxServiceOptionVals = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionVal.getTitleOid());
                            if(!CollectionUtils.isEmpty(sxServiceOptionVals)){
                                for(SxServiceOptionVal serviceOptionVal:sxServiceOptionVals){
                                    //判断是否被选中
                                    if (sxServiceOptionVal.getOid().equals(serviceOptionVal.getOid())) {
                                        serviceOptionVal.setIsSelected("1");
                                    }
                                    //查询细化材料选项值关系表
                                    List<SxServiceMateOptRel> sxServiceMateOptRels = this.sxServiceMateOptRelManager.getSxServiceMateOptRelsByOids(serviceOptionVal.getOid(),null,null);
                                    if(!CollectionUtils.isEmpty(sxServiceMateOptRels)){
                                        this.getSxServiceMateOptRels(sxServiceMateOptRels);
                                        serviceOptionVal.setSxServiceMateOptRels(sxServiceMateOptRels);
                                    }
                                }
                                sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
                            }
                        }
                        sxServiceSituation.setSxServiceSituationOption(sxServiceSituationOption);
                    }
                }
                returnMap.put("sxServiceSituations",sxServiceSituations);
            }
        }
        //多此一举是用来去除returnMap里面的 重复节点
        JSONObject jsonObject = JSONObject.fromObject(returnMap);
        returnMap = JSON.parseObject(jsonObject.toString(), HashMap.class);
        return returnMap;
    }

    /**
     * 查询选项值对应的材料数据，
     *  SxServiceSituationOptionManager 此类有用到
     * @param sxServiceMateOptRels
     */
    public void getSxServiceMateOptRels(List<SxServiceMateOptRel> sxServiceMateOptRels) {
        for (SxServiceMateOptRel sxServiceMateOptRel: sxServiceMateOptRels) {
            //判断颗粒化材料oid是否有值
            if (StringUtils.isNotEmpty(sxServiceMateOptRel.getMaterialOid())) {
                ServiceMaterial serviceMaterial = this.serviceMaterialManager.getServiceMaterialByOid(sxServiceMateOptRel.getMaterialOid());
                sxServiceMateOptRel.setServiceMaterial(serviceMaterial);
                if(!ObjectUtils.isEmpty(serviceMaterial)){
                    //查询事项材料，获取材料形式
                    SxServiceMaterial sxServiceMaterial = this.sxServiceMaterialManager.getSxServiceMaterialByOid(serviceMaterial.getRawMaterialOid());
                    if(!ObjectUtils.isEmpty(sxServiceMaterial)){
                        serviceMaterial.setMaterialFormat(sxServiceMaterial.getMaterialFormat());
                    }
                    //查询颗粒化材料附件
                    SxSysAtta sxSysAtta = this.sxSysAttaManager.getSxSysAttaByOid(serviceMaterial.getMaterialSampleAddr());
                    serviceMaterial.setSxSysAtta(sxSysAtta);
                }

            }else{//若事项颗粒材料信息为空，则去查询事项材料信息
                SxServiceMaterial sxServiceMaterial = this.sxServiceMaterialManager.getSxServiceMaterialByOid(sxServiceMateOptRel.getSxMaterialOid());
                sxServiceMateOptRel.setSxServiceMaterial(sxServiceMaterial);
            }
        }
    }

    /**
     * 所有情形，所有标题与标题对应的选项值
     * @param serviceOid
     * @return
     */
    public Map<String, Object> getSxServiceSituationAndOptionMap(String serviceOid) {
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isNotEmpty(serviceOid)) {
            //所有有关联的选项标题
            List<String> allTitleOids = Lists.newArrayList();
            //事项情形
            List<SxServiceSituation> sxServiceSituations = this.sxServiceSituationManager.getSxServiceSituationsByServiceOid(serviceOid);
            //所有的情形
            returnMap.put("sxServiceSituations", sxServiceSituations);
            //事项选项标题信息
            List<SxServiceOptionTitle> sxServiceOptionTitles = this.sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
            if (!CollectionUtils.isEmpty(sxServiceOptionTitles)) {
                for (SxServiceOptionTitle sxServiceOptionTitle : sxServiceOptionTitles) {
                    List<SxServiceOptionVal> sxServiceOptionVals = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
                    if (!CollectionUtils.isEmpty(sxServiceOptionVals)) {
                        for (SxServiceOptionVal sxServiceOptionVal : sxServiceOptionVals) {
                            //与该选项值有关联的选项标题
                            List<String> titleOids = serviceOptionRelManager.getTitleOidListByServiceOidAndOptionOid(serviceOid, sxServiceOptionVal.getOid());
                            if (!CollectionUtils.isEmpty(titleOids)) {
                                allTitleOids.addAll(titleOids);
                            }
                        }
                    }
                    sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
                }
                //判断选项标题是否有相关联的选项值，若有关联则置为 1
                out: for (SxServiceOptionTitle sxServiceOptionTitle : sxServiceOptionTitles) {
                    for (String titleOid : allTitleOids) {
                        if (sxServiceOptionTitle.getOid().equals(titleOid)) {
                            sxServiceOptionTitle.setIsHavingCorrelation("1");
                            continue out;
                        }
                    }
                }
            }
            //所有的标题
            returnMap.put("sxServiceOptionTitles", sxServiceOptionTitles);
            //所有的事项材料
            //申请材料
     /*       List<SxServiceMaterial> sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
            if(null != sxServiceMaterials && sxServiceMaterials.size()>0){
                returnMap.put("sxServiceMaterials",sxServiceMaterials);
            }*/
        }
        //多此一举是用来去除returnMap里面的 重复节点
        JSONObject jsonObject = JSONObject.fromObject(returnMap);
        returnMap = JSON.parseObject(jsonObject.toString(), HashMap.class);
        return returnMap;
    }

    public   Map<String, Object>  saveOrUpdateSxServiceMaterialClassifier(SxService sxService){
        Map<String, Object> returnMap = new HashMap<>();
        if(null!=sxService) {
            DbSxService dbSxService = dbSxServiceMapper.getDbSxServiceByOid(sxService.getServiceOid());
            String autoClassificationStatus=sxService.getAutoClassificationStatus();
            String classifierId=sxService.getClassifierId();

            dbSxService.setAutoClassificationStatus(autoClassificationStatus);
            dbSxService.setClassifierId(classifierId);
            if(StrUtil.isNotEmpty(autoClassificationStatus)){
                if(autoClassificationStatus.equals("Y")){

                    //事项材料
                    List<SxServiceMaterial> serviceMaterList=sxService.getServiceMaterList();
                    for (SxServiceMaterial sxServiceMaterial:serviceMaterList) {
                        String materialOid = sxServiceMaterial.getMaterialOid();
                        String baiduTemplateIds = sxServiceMaterial.getBaiduTemplateIds();
                        if(null==baiduTemplateIds){
                            baiduTemplateIds="";
                        }
                        DbSxServiceMaterial dbSxServiceMaterial = new DbSxServiceMaterial();
                        dbSxServiceMaterial.setBaiduTemplateIds(baiduTemplateIds);
                        dbSxServiceMaterial.setMaterialOid(materialOid);
                        dbSxServiceMaterialMapper.updateByMaterialOid(dbSxServiceMaterial);

                    }

                    //颗粒化材料
                    List<ServiceMaterial> lkhserviceMaterList=sxService.getServiceMaterialList();
                    for (ServiceMaterial serviceMaterial:lkhserviceMaterList) {
                        String materialOid = serviceMaterial.getOid();
                        String baiduTemplateIds = serviceMaterial.getBaiduTemplateIds();
                        if(null==baiduTemplateIds){
                            baiduTemplateIds="";
                        }
                        // DbServiceMaterial dbServiceMaterial = dbServiceMaterialMapper.selectByOid(materialOid);
                        DbServiceMaterial dbServiceMaterial=new DbServiceMaterial();
                        dbServiceMaterial.setOid(materialOid);
                        dbServiceMaterial.setBaiduTemplateIds(baiduTemplateIds);
                        this.dbServiceMaterialMapper.updateByPrimaryKeySelectiveNew(dbServiceMaterial);

                    }


                }

            }

            dbSxService.setModifyDate(new Date());
            dbSxServiceMapper.updateByPrimaryKey(dbSxService);

            returnMap.put("message","保存成功");
        }
        return returnMap;
    }

    public   Map<String, Object>  saveOrUpdateSxServiceMaterial(SxService sxService){
        Map<String, Object> returnMap = new HashMap<>();
        if(null!=sxService) {
              // DbSxService dbSxService = dbSxServiceMapper.getDbSxServiceByOid(sxService.getServiceOid());

            //事项材料
            List<SxServiceMaterial> serviceMaterList=sxService.getServiceMaterList();
            for (SxServiceMaterial sxServiceMaterial:serviceMaterList) {
                String materialOid = sxServiceMaterial.getMaterialOid();
                String materialCatalogOid=sxServiceMaterial.getMaterialCatalogOid();

                if(null==materialCatalogOid){
                    materialCatalogOid="";
                }
                DbSxServiceMaterial dbSxServiceMaterial = new DbSxServiceMaterial();
                dbSxServiceMaterial.setMaterialCatalogOid(materialCatalogOid);
                dbSxServiceMaterial.setMaterialOid(materialOid);
                dbSxServiceMaterialMapper.updateByMaterialOid(dbSxServiceMaterial);

            }

           //颗粒化材料
            List<ServiceMaterial> lkhserviceMaterList=sxService.getServiceMaterialList();
            for (ServiceMaterial serviceMaterial:lkhserviceMaterList) {
                String materialOid = serviceMaterial.getOid();
                String materialCatalogOid=serviceMaterial.getMaterialCatalogOid();
                if(null==materialCatalogOid){
                    materialCatalogOid="";
                }
                // DbServiceMaterial dbServiceMaterial = dbServiceMaterialMapper.selectByOid(materialOid);
                DbServiceMaterial dbServiceMaterial=new DbServiceMaterial();
                dbServiceMaterial.setOid(materialOid);
                dbServiceMaterial.setMaterialCatalogOid(materialCatalogOid);
                this.dbServiceMaterialMapper.updateByPrimaryKeySelectiveNew(dbServiceMaterial);

            }
            //dbSxServiceMapper.updateByPrimaryKey(dbSxService);
            returnMap.put("message","保存成功");
        }
        return returnMap;
    }

    public   Map<String, Object>  saveOrUpdateServiceMaterial(SxService sxService){
        Map<String, Object> returnMap = new HashMap<>();
        if(null!=sxService) {
            // DbSxService dbSxService = dbSxServiceMapper.getDbSxServiceByOid(sxService.getServiceOid());
            List<ServiceMaterial> serviceMaterList=sxService.getServiceMaterialList();
            for (ServiceMaterial serviceMaterial:serviceMaterList) {
                String materialOid = serviceMaterial.getOid();
                String materialCatalogOid=serviceMaterial.getMaterialCatalogOid();
                if(null==materialCatalogOid){
                    materialCatalogOid="";
                }
               // DbServiceMaterial dbServiceMaterial = dbServiceMaterialMapper.selectByOid(materialOid);
                DbServiceMaterial dbServiceMaterial=new DbServiceMaterial();
                dbServiceMaterial.setOid(materialOid);
                dbServiceMaterial.setMaterialCatalogOid(materialCatalogOid);
                this.dbServiceMaterialMapper.updateByPrimaryKeySelectiveNew(dbServiceMaterial);

            }
            //dbSxServiceMapper.updateByPrimaryKey(dbSxService);
            returnMap.put("message","保存成功");
        }
        return returnMap;
    }

    /**
     * 获取事项表中最大时间节点
     * @return
     */
    public Date getMaxServiceTime(){
      Date time=  dbSxServiceMapper.getMaxServiceTime();
      return time;
    }

    public SxService getDbSxServiceByServiceOid(String serviceOid){
       DbSxService service= dbSxServiceMapper.getDbSxServiceByServiceOid(serviceOid);
       if(service!=null){
           SxService sxService=new SxService();
           BeanUtil.copyProperties(service,sxService);
           return  sxService;
       }
        return  null;
    }
    public List<SxService> getSxRecommendServiceListByOid(String[] serviceOid){
        List<DbSxService> serviceList= dbSxServiceMapper.getSxRecommendServiceListByOid(serviceOid);
        List<SxService>  sxServiceLists =  new ArrayList<>();
        for(DbSxService  dbSxService : serviceList){
            SxService service = new SxService();
            BeanUtils.copyProperties(dbSxService,service);
            sxServiceLists.add(service);
        }
        return  sxServiceLists;
    }

    public Map<String, Object> getSxServiceHotSituations(String serviceOid) {

        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isNotEmpty(serviceOid)) {
            //所有有关联的选项标题
            List<String> allTitleOids = Lists.newArrayList();
            //事项情形
            List<SxServiceSituation> sxServiceSituations = this.sxServiceSituationManager.getSxServiceSituationsByServiceOid(serviceOid);
            //所有的情形
            returnMap.put("sxServiceSituations", sxServiceSituations);
            //事项选项标题信息
            List<SxServiceOptionTitle> sxServiceOptionTitles = this.sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
            if (!CollectionUtils.isEmpty(sxServiceOptionTitles)) {
                for (SxServiceOptionTitle sxServiceOptionTitle : sxServiceOptionTitles) {
                    List<SxServiceOptionVal> sxServiceOptionVals = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
                    if (!CollectionUtils.isEmpty(sxServiceOptionVals)) {
                        for (SxServiceOptionVal sxServiceOptionVal : sxServiceOptionVals) {
                            //与该选项值有关联的选项标题
                            List<String> titleOids = serviceOptionRelManager.getTitleOidListByServiceOidAndOptionOid(serviceOid, sxServiceOptionVal.getOid());
                            if (!CollectionUtils.isEmpty(titleOids)) {
                                allTitleOids.addAll(titleOids);
                            }
                        }
                    }
                    sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
                }
                //判断选项标题是否有相关联的选项值，若有关联则置为 1
                out: for (SxServiceOptionTitle sxServiceOptionTitle : sxServiceOptionTitles) {
                    for (String titleOid : allTitleOids) {
                        if (sxServiceOptionTitle.getOid().equals(titleOid)) {
                            sxServiceOptionTitle.setIsHavingCorrelation("1");
                            continue out;
                        }
                    }
                }
            }
            //所有的标题
            returnMap.put("sxServiceOptionTitles", sxServiceOptionTitles);
            //所有的事项材料
            //申请材料
     /*       List<SxServiceMaterial> sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
            if(null != sxServiceMaterials && sxServiceMaterials.size()>0){
                returnMap.put("sxServiceMaterials",sxServiceMaterials);
            }*/
        }
        //多此一举是用来去除returnMap里面的 重复节点
        JSONObject jsonObject = JSONObject.fromObject(returnMap);
        returnMap = JSON.parseObject(jsonObject.toString(), HashMap.class);
        return returnMap;
    }

    public List<SxServiceOptionTitle> getSxServiceTitlesNoRelation(String serviceOid) {
        //事项下没有与其他选项值有关联的选项标题
        List<SxServiceOptionTitle> titleList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(serviceOid)) {
            //事项选项标题信息
            List<SxServiceOptionTitle> sxServiceOptionTitles = this.sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
            //与事项有关联关系的选项值和选项标题
            List<ServiceOptionRel> optionRels = serviceOptionRelManager.getServiceOptionRelByServiceOid(serviceOid);
            if (!CollectionUtils.isEmpty(sxServiceOptionTitles)) {
                for (SxServiceOptionTitle sxServiceOptionTitle : sxServiceOptionTitles) {
                    List<SxServiceOptionVal> sxServiceOptionVals = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
                    sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
                }
                if (!CollectionUtils.isEmpty(optionRels)) {
                    String titleOids = "";
                    for (ServiceOptionRel serviceOptionRel : optionRels) {
                        titleOids += serviceOptionRel.getTitleOids()+",";

                    }
                    for (SxServiceOptionTitle optionTitle : sxServiceOptionTitles) {
                        if(!titleOids.contains(optionTitle.getOid())){
                            titleList.add(optionTitle);
                        }
                    }
                }else {
                    titleList.addAll(sxServiceOptionTitles);
                }
            }
        }
        return  titleList;
    }

    //添加默认选中逻辑。
    public List<SxServiceOptionTitle> getSxServiceTitlesNoRelationDefault(String serviceOid) {
        //事项下没有与其他选项值有关联的选s项标题
        List<SxServiceOptionTitle> titleList = getSxServiceTitlesNoRelation(serviceOid);
        List<SxServiceOptionTitle> result =  new ArrayList<>();
        for(SxServiceOptionTitle sxServiceOptionTitle : titleList){
            List<SxServiceOptionTitle>  lists = getSxServiceOptionTitlesBySxServiceTitlesNoRelation(sxServiceOptionTitle,serviceOid);
            result.addAll(lists);
            //titleList.addAll(lists);
        }
        result.addAll(titleList);
        //
        List<SxServiceOptionTitle>  returnLists  = new ArrayList<>();
        List<String> oids  =  new ArrayList<>();
        for(SxServiceOptionTitle re : result){
            if(!oids.contains(re.getOid())){
                oids.add(re.getOid());
                returnLists.add(re);
            }
        }
        //排序
        Collections.sort(returnLists, (o1, o2) -> {
            int diff =  Integer.parseInt(String.valueOf(o2.getSort())) -  Integer.parseInt(String.valueOf(o1.getSort()));
            if(diff > 0){
                return  -1;
            }else if(diff < 0){
                return  1;
            }
            return 0;
        });
        return returnLists;
    }


    //根据选项值（默认）关联
    public List<SxServiceOptionTitle> getSxServiceOptionTitlesBySxServiceTitlesNoRelation(SxServiceOptionTitle sxServiceOptionTitle,String serviceOid) {
        List<SxServiceOptionTitle>  lists  = new ArrayList<>();
        //lists.add(sxServiceOptionTitle);
        Queue<SxServiceOptionTitle> sxServiceOptionTitleStacks = new LinkedList<>();
        //sxServiceOptionTitle  肯定没被选项值关联，因为是接口查询出来的
        sxServiceOptionTitleStacks.offer(sxServiceOptionTitle);
        while (!sxServiceOptionTitleStacks.isEmpty()){
            SxServiceOptionTitle sxs = sxServiceOptionTitleStacks.poll();
            for(SxServiceOptionVal  sxServiceOptionVal : sxs.getSxServiceOptionVals()){
                if(sxServiceOptionVal.getDefaultFlag()!=null&&sxServiceOptionVal.getDefaultFlag() == 1){
                    sxServiceOptionVal.setIsSelected("1");
                    //默认选中
                    List<ServiceOptionRel>  serviceOptionRelList  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, sxServiceOptionVal.getOid());
                    for(ServiceOptionRel serviceOptionRel : serviceOptionRelList) {
                        String[] titleIds = serviceOptionRel.getTitleOids().split(",");
                        for (String titleId : titleIds) {
                            SxServiceOptionTitle t = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleId);
                            if(t!=null){
                                List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(t.getOid());
                                t.setSxServiceOptionVals(vals);
                                sxServiceOptionTitleStacks.offer(t);
                                lists.add(t);
                            }
                        }
                    }
                }
            }
        }
        return  lists;
    };


    public List<SxServiceOptionTitle> querySxServiceSituationRelation2(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid) {
        //返回信息
        List<SxServiceOptionTitle> list = null ;
        //查询的所有返回选中的情形关系
        List<SxServiceOptionTitle> titleList = Lists.newArrayList();
        //查询的返回当前选中的情形关系
        List<SxServiceOptionTitle> currentList = Lists.newArrayList();
        //获取包括当前选项之前的oid
        List<String>  firstStrList = Lists.newArrayList();
        //获取当前全部选中的选项值oid
        List<String> oidList = null;
        String[] oids = null;
        //查询所有没有关联关系的标题
        List<SxServiceOptionTitle> optionTitleList = this.getSxServiceTitlesNoRelation(serviceOid);
        //有选中值的处理
        if (valOids != null) {
            oids = valOids.split(",");
            oidList = Arrays.stream(oids).collect(Collectors.toList());
            //查询当前选项值选中的标题
            SxServiceOptionTitle currentTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(currentTitleOid);
            if (currentTitle != null) {
                List<SxServiceOptionVal> currentVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(currentTitle.getOid());
                if (currentVals != null) {
                    for (SxServiceOptionVal val : currentVals) {
                        if (val.getOid().equals(currentOid)) {
                            val.setIsSelected("1");
                        }
                    }
                    currentTitle.setSxServiceOptionVals(currentVals);
                }
                currentList.add(currentTitle);
                //获取当前选项值关联选项
                //List<ServiceOptionRel> currentRelList = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, currentOid);
                List<ServiceOptionRel> currentRelList = serviceOptionRelManager.getServiceOptionRelListByOidsAndValueOidNull(serviceOid, currentOid);
                if (currentRelList != null) {
                    for (ServiceOptionRel optionRel: currentRelList) {
                        String[] currentOids = optionRel.getTitleOids().split(",");
                        for (String titleOid: currentOids) {
                            SxServiceOptionTitle optionTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleOid);
                            if (optionTitle != null) {
                                List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(optionTitle.getOid());
                                if (vals != null) {
                                    optionTitle.setSxServiceOptionVals(vals);
                                }
                            }
                            currentList.add(optionTitle);
                        }
                    }
                }
            }
            //处理获取包括当前选项之前的oid
            if (currentList.size() == 1){
                //取得当前选中的后面的oid
                boolean flag = true;
                for (String oid : oidList) {
                    if (flag) {
                        firstStrList.add(oid);
                    }
                    if(oid.equals(currentOid)){
                        flag = false;
                    }
                }
                oidList =  firstStrList;
            }
            //如果选中的选项值是最顶层，则只返回当前数据
            if(currentTitleOid.equals(rootTitleOid)){
                titleList.addAll(currentList);
            }else{
                //获取所有选项值关联的信息
                oidList.forEach(oid -> {
                    //查询选项值
                    SxServiceOptionVal optionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(oid);
                    //查询选项值选中的标题
                    SxServiceOptionTitle optionTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(optionVal.getTitleOid());
                    if (optionTitle != null) {
                        //查询标题所属选项值
                        List<SxServiceOptionVal> optionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(optionTitle.getOid());
                        if (optionVals != null) {
                            optionTitle.setSxServiceOptionVals(optionVals);
                        }
                        titleList.add(optionTitle);
                    }
                    //获取选项值关联选项
                    List<ServiceOptionRel> optionRels = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, oid);
                    if (optionRels != null) {
                        for (ServiceOptionRel optionRel: optionRels) {
                            String[] titleOids = optionRel.getTitleOids().split(",");
                            for (String titleOid: titleOids) {
                                SxServiceOptionTitle title = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleOid);
                                if (title != null) {
                                    List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(title.getOid());
                                    if (vals != null) {
                                        title.setSxServiceOptionVals(vals);
                                    }
                                }
                                titleList.add(title);
                            }
                        }
                    }
                });
            }
            titleList.addAll(optionTitleList);
            list = new ArrayList<SxServiceOptionTitle>(new LinkedHashSet(titleList));
            //选中的选项值设置值回显到页面
            for (SxServiceOptionTitle title: list) {
                List<SxServiceOptionVal> serviceOptionVals = title.getSxServiceOptionVals();
                for (SxServiceOptionVal optionVal: serviceOptionVals) {
                    for (String oid: oidList) {
                        if(optionVal.getOid().equals(oid)){
                            optionVal.setIsSelected("1");
                        }
                    }
                }
            };
            //排序
            Collections.sort(list, (o1, o2) -> {
                int diff =  Integer.parseInt(String.valueOf(o2.getSort())) -  Integer.parseInt(String.valueOf(o1.getSort()));
                if(diff > 0){
                    return  -1;
                }else if(diff < 0){
                    return  1;
                }
                return 0;
            });
        }
        return list;
    }


    public List<SxServiceOptionTitle> querySxServiceSituationRelation(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid) {
        //返回信息
        List<SxServiceOptionTitle> list = null ;
        //查询的所有返回选中的情形关系
        List<SxServiceOptionTitle> titleList = Lists.newArrayList();
        //查询的返回当前选中的情形关系
        List<SxServiceOptionTitle> currentList = Lists.newArrayList();
        //获取包括当前选项之前的oid
        List<String>  firstStrList = Lists.newArrayList();
        //获取当前全部选中的选项值oid
        List<String> oidList = null;
        String[] oids = null;
        //计算根据截取后的 valOids
        List<String>  computeValOids =  new ArrayList<>();
        //查询所有没有关联关系的标题
        List<SxServiceOptionTitle> optionTitleList = this.getSxServiceTitlesNoRelation(serviceOid);
        //有选中值的处理
        for(SxServiceOptionTitle  sxServiceOptionTitle : optionTitleList){
            List<SxServiceOptionTitle>  lists = querySxServiceOptionTitleListOne(sxServiceOptionTitle,serviceOid,valOids,currentOid,currentTitleOid,rootTitleOid,computeValOids);
            if(lists!=null&&lists.size()>0){
                titleList.addAll(lists);
            }
        }
        //把没关联的放进去
        titleList.addAll(optionTitleList);
        //计算根据截取后的 valOids
        //去重，可能要修改
        list = new ArrayList<SxServiceOptionTitle>(new LinkedHashSet(titleList));
        //选中的选项值设置值回显到页面
        for (SxServiceOptionTitle title: list) {
            List<SxServiceOptionVal> serviceOptionVals = title.getSxServiceOptionVals();
            for (SxServiceOptionVal optionVal: serviceOptionVals) {
                if(computeValOids.contains(optionVal.getOid())){
                    optionVal.setIsSelected("1");
                }
            }
        };
        //排序
        Collections.sort(list, (o1, o2) -> {
            int diff =  Integer.parseInt(String.valueOf(o2.getSort())) -  Integer.parseInt(String.valueOf(o1.getSort()));
            if(diff > 0){
                return  -1;
            }else if(diff < 0){
                return  1;
            }
            return 0;
        });


        return list;
    }

    private  List<SxServiceOptionTitle>   querySxServiceOptionTitleListOne(SxServiceOptionTitle  sxServiceOptionTitle,String serviceOid,
                                                                           String valOids, String currentOid, String currentTitleOid, String rootTitleOid
    ,List<String>  computeValOids){
        //1.2.1	找这条标题，对应的选项值 .
        //List<SxServiceOptionVal> sxServiceOptionVals = sxServiceOptionTitle.getSxServiceOptionVals();

        Queue<SxServiceOptionTitle> sxServiceOptionTitleStacks = new LinkedList<>();
        //sxServiceOptionTitle  肯定没被选项值关联，因为是接口查询出来的
        sxServiceOptionTitleStacks.offer(sxServiceOptionTitle);
        //compute
        List<SxServiceOptionTitle> lists = compute(sxServiceOptionTitleStacks,valOids,currentOid,serviceOid,computeValOids,currentTitleOid);
        //log.info(sxServiceOptionTitle.getName() + "计算结束");
        return lists;
    }


    private  List<SxServiceOptionTitle> compute(Queue<SxServiceOptionTitle> sxServiceOptionTitleStacks,String valOids,String
            currentOid,String serviceOid,List<String>  computeValOids,String currentTitleOid){
        List<SxServiceOptionTitle>  lists  = new ArrayList<>();
        boolean  isFirst =  true;//是否是第一个
        while(!sxServiceOptionTitleStacks.isEmpty()){
            SxServiceOptionTitle sxServiceOptionTitle = sxServiceOptionTitleStacks.poll();
            //boolean  isAdd = judge(sxServiceOptionTitle.getSxServiceOptionVals(),valOids,currentOid);
            //判断此选项标题是否命中,根据  事项业务主键， 选项值 业务主键 查询

            //List<ServiceOptionRel>  srLists  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid,currentOid);
            for(SxServiceOptionVal sxServiceOptionVal : sxServiceOptionTitle.getSxServiceOptionVals()){
                //现在，需要关联的有二种情况，一是 选中了，二是 默认选中。
                if(valOids.contains(sxServiceOptionVal.getOid())||sxServiceOptionVal.getDefaultFlag()==1){
                    //计算根据截取后的 valOids
                    //computeValOids.add(sxServiceOptionVal.getOid())
                    //addStringAndNoDuplicate(computeValOids,sxServiceOptionVal.getOid());
                    //是最后一个，即是当前，点击的选项值。
                    if(currentOid.equals(sxServiceOptionVal.getOid())){
                        //到点击的选项值了。
                        //addStringAndNoDuplicate(computeValOids, sxServiceOptionVal.getOid());
                        //复选，单选 区别处理
                        //moreStatus  0  不是多选， 1是
                        //单选
                        if(sxServiceOptionTitle.getMoreStatus()==0){
                            addStringAndNoDuplicate(computeValOids, sxServiceOptionVal.getOid());
                            //一样，说明 到最后一个选中的了。
                            //List<SxServiceOptionTitle>  child  =  new ArrayList<>();
                            List<ServiceOptionRel>  serviceOptionRelList  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, sxServiceOptionVal.getOid());
                            for(ServiceOptionRel serviceOptionRel : serviceOptionRelList){
                                String[] titleIds = serviceOptionRel.getTitleOids().split(",");
                                //关联关系，选中的。
                                for(String titleId : titleIds){
                                    SxServiceOptionTitle t = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleId);
                                    if (t != null) {
                                        List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(t.getOid());
                                        if (vals != null) {
                                            //关联出来的，再去添加，命中的id
                                            for(SxServiceOptionVal val : vals){
/*                                                if(valOids.contains(val.getOid())) {
                                                    addStringAndNoDuplicate(computeValOids, val.getOid());
                                                }*/
                                               //关联关系，关联出来的。 分二种情况，一是  选中的（正常情况下，不需要，因为，currentOid  后面的 选项值 id都不需要了），二是  默认选中。
                                                //修改前  valOids.contains(val.getOid())||val.getDefaultFlag()==1
                                               if(val.getDefaultFlag()==1){
                                                   addStringAndNoDuplicate(computeValOids, val.getOid());
                                                   List<ServiceOptionRel>  list  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, val.getOid());
                                                   //去查询关联，看是否有关联得选项
                                                   for(ServiceOptionRel sor : list){
                                                       String[] ts = sor.getTitleOids().split(",");
                                                       for(String td : ts){
                                                           SxServiceOptionTitle ssot = sxServiceOptionTitleManager.getServiceOptionTitleByOid(td);
                                                           if(ssot!=null){
                                                               List<SxServiceOptionVal> vs = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(ssot.getOid());
                                                               if(vs!=null){
                                                                   ssot.setSxServiceOptionVals(vs);
                                                               }
                                                               addAndRemoveDuplicate(lists,ssot);
                                                               //有关联的，放入队列。 继续遍历
                                                               sxServiceOptionTitleStacks.offer(ssot);
                                                           }
                                                       }
                                                   }
                                               }
                                            }
                                        }
                                        t.setSxServiceOptionVals(vals);
                                    }
                                    //child.add(t);
                                    //放入计算的stack
                                    //sxServiceOptionTitleStacks.push(t);
                                    //放入返回值的list
                                    //lists.add(t);
                                    addAndRemoveDuplicate(lists,t);
                                    //有默认的  可能会，继续 遍历。 不return
                                    //return lists;
                                }
                            }
                        }else if(sxServiceOptionTitle.getMoreStatus()==1){
                            //是多选， 集合到一起，去重。
                            List<SxServiceOptionVal> sxServiceOptionVals = sxServiceOptionTitle.getSxServiceOptionVals();
                            for(SxServiceOptionVal sso : sxServiceOptionVals){
                                //计算根据截取后的 valOids
                                //computeValOids.add(sso.getOid());
                                //addStringAndNoDuplicate(computeValOids,sso.getOid());
                                if(valOids.contains(sso.getOid())){
                                    addStringAndNoDuplicate(computeValOids, sso.getOid());
                                    //addStringAndNoDuplicate(computeValOids,sso.getOid());
                                    List<ServiceOptionRel>  serviceOptionRelList  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, sso.getOid());
                                    for(ServiceOptionRel serviceOptionRel : serviceOptionRelList){
                                        String[] titleIds = serviceOptionRel.getTitleOids().split(",");
                                        for(String titleId : titleIds){
                                            SxServiceOptionTitle t = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleId);
                                            //child.add(t);
                                            //放入计算的stack
                                            //sxServiceOptionTitleStacks.push(t);
                                            //放入返回值的list
                                            //lists.add(t);
                                            if (t != null) {
                                                List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(t.getOid());
                                                if (vals != null) {
                                                    //关联出来的，再去添加，命中的id
                                                    //offer  到队列里的  选项，对应的选项值  都不处理选中。
 /*                                                   for(SxServiceOptionVal val : vals){
                                                        if(valOids.contains(val.getOid())){
                                                            addStringAndNoDuplicate(computeValOids,val.getOid());
                                                        }
                                                    }*/
                                                    t.setSxServiceOptionVals(vals);
                                                }
                                                sxServiceOptionTitleStacks.offer(t);
                                                addAndRemoveDuplicate(lists,t);
                                            }
                                            //return lists;
                                        }
                                    }
                                }
/*                                else if(sso.getDefaultFlag()==1&&(!sso.getOid().equals(currentOid))){
                                    addStringAndNoDuplicate(computeValOids, sso.getOid());
                                    //选项值，未选中，但是默认的。  必须不是，当前点击的选项值。 因为当前点击，是要去掉。
                                    //未选中，且默认
                                        //默认选中
                                        //addStringAndNoDuplicate(computeValOids,sso.getOid());
                                        List<ServiceOptionRel>  ls  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, sso.getOid());
                                        //去查询关联，看是否有关联得选项
                                        for(ServiceOptionRel so : ls){
                                            String[] t = so.getTitleOids().split(",");
                                            for(String d : t){
                                                SxServiceOptionTitle ss = sxServiceOptionTitleManager.getServiceOptionTitleByOid(d);
                                                if(ss!=null){
                                                    List<SxServiceOptionVal> vs = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(ss.getOid());
                                                    if(vs!=null){
                                                        ss.setSxServiceOptionVals(vs);
                                                    }
                                                    addAndRemoveDuplicate(lists,ss);
                                                    //有关联的，放入队列。 继续遍历
                                                    sxServiceOptionTitleStacks.offer(ss);
                                                }
                                            }
                                        }
                                }*/
                            }
                            //有默认的  可能会，继续 遍历。 不return
                            //return lists;
                        }
                        //遍历选项值，到了选中的，没必要继续，遍历了。 不return 是因为，有可能有 默认值。 会带出来新的，选项，需要继续循环。
                        continue;
                    }else {
                        //遍历中
                        boolean isNowTitle = false;
                        if(currentTitleOid.equalsIgnoreCase(sxServiceOptionTitle.getOid())){
                            isNowTitle = true;
                        }
                        //单选，需要跳过。 默认的，点击 id 之前的。
                        //boolean  isCan = true;
                        boolean  isCan = false;
/*                        if(isNowTitle&&sxServiceOptionVal.getDefaultFlag()==1){
                            isCan =false;
                        }
                        //单选，不是当前标题，必须是选中的。
                        if(!isNowTitle&&!valOids.contains(sxServiceOptionVal.getOid())){
                            isCan =false;
                        }*/
                        //(有一个，是点击的id集合里面有，则认为  是点击过的， 取id集合。  不然，则认为是关联出来的，取默认值)
                        List<SxServiceOptionVal> l = sxServiceOptionTitle.getSxServiceOptionVals();
                        boolean i = false;
                        for(SxServiceOptionVal s: l){
                            if(valOids.contains(s.getOid())){
                                i = true;
                            }
                        }
/*                        //一个都没选中，都不取默认的
                        if(num==l.size()){

                        }*/
                        if(isNowTitle){
                            //同一级的，默认，还要看是否选中。 所以，必须选中，才可以，认为是  被选中
                            if(valOids.contains(sxServiceOptionVal.getOid())){
                                isCan = true;
                            }
                        }else {
                            //不是同一级的，默认，选中 都可以
/*                            if(valOids.contains(sxServiceOptionVal.getOid())||sxServiceOptionVal.getDefaultFlag()==1){
                                isCan = true;
                            }*/
                            if(i){
                                if(valOids.contains(sxServiceOptionVal.getOid())){
                                    isCan = true;
                                }
                            }else {
                                if(sxServiceOptionVal.getDefaultFlag()==1){
                                    isCan = true;
                                }
                            }
                        }
                        //多选， 如果是默认值，需要判断，是否选中。
                        boolean isCanMore = true;
                        if(sxServiceOptionTitle.getMoreStatus()==1){
                            //是否与当前点击，是同一级？
                            if(isNowTitle){
                                // 是同一级别的，需要看默认选中，是否被 选中。
                                if(sxServiceOptionVal.getDefaultFlag()==1){
                                        if(!valOids.contains(sxServiceOptionVal.getOid())){
                                            isCanMore = false;
                                        }
                                }
                            }else {
                                //不是同一个选项，默认选中，可以勾选。  (有一个，是点击的id集合里面有，则认为  是点击过的， 取id集合。  不然，则认为是关联出来的，取默认值)
                                List<SxServiceOptionVal> ls = sxServiceOptionTitle.getSxServiceOptionVals();
                                boolean is = false;
                                int  num =0;
                                for(SxServiceOptionVal s: ls){
                                    if(valOids.contains(s.getOid())){
                                        is = true;
                                    }
                                }
                                SxServiceOptionTitle currentTitle  = sxServiceOptionTitleManager.getServiceOptionTitleByOid(currentTitleOid);
                                //点击的选项，在 sxServiceOptionTitle 这个的前面，还是后面。
                                boolean  isFor =  (sxServiceOptionTitle.getSort() - currentTitle.getSort()) < 0;
                                if(is){
                                    if(!valOids.contains(sxServiceOptionVal.getOid())){
                                        isCanMore = false;
                                    }
                                }else {
                                    if(isFor){
                                        isCanMore = false;
                                    }else {
                                        if(sxServiceOptionVal.getDefaultFlag()!=1){
                                            isCanMore = false;
                                        }
                                    }
                                }
                                //都没选择，都不取默认的。

/*                                if(sxServiceOptionVal.getDefaultFlag()!=1){
                                    isCanMore = false;
                                }*/
                            }
                        }
                        boolean  trans = false;
                        if(sxServiceOptionTitle.getMoreStatus()==1){
                            trans = isCanMore;
                        }else {
                            trans = isCan;
                        }
                        //如果是多选，没关系。  单选，不是默认的需要跳过
                        //(sxServiceOptionTitle.getMoreStatus()==1  )|| isCan
                        if(trans){
                            //遍历中
                            addStringAndNoDuplicate(computeValOids, sxServiceOptionVal.getOid());
                            List<ServiceOptionRel>  serviceOptionRelList  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, sxServiceOptionVal.getOid());
                            for(ServiceOptionRel serviceOptionRel : serviceOptionRelList){
                                String[] titleIds = serviceOptionRel.getTitleOids().split(",");
                                for(String titleId : titleIds){
                                    SxServiceOptionTitle t = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleId);
                                    //child.add(t);
                                    //放入计算的stack
                                    if (t != null) {
                                        List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(t.getOid());
                                        if (vals != null) {
                                            //关联出来的，再去添加，命中的id
                                            for(SxServiceOptionVal val : vals){
                                                //现在，需要关联的有二种情况，一是 选中了，二是 默认选中。
/*                                            if(valOids.contains(val.getOid())||sxServiceOptionVal.getDefaultFlag()==1){
                                                //选中的有二种情况。 一是 选中了，二是 默认选中。
                                                addStringAndNoDuplicate(computeValOids,val.getOid());
                                            }*/
                                                //前端，控制。
                                                if(valOids.contains(val.getOid())){
                                                    //选中的有二种情况。 一是 选中了，二是 默认选中。
                                                    addStringAndNoDuplicate(computeValOids,val.getOid());
                                                }else  if(val.getDefaultFlag()==1){

                                                }
                                            }
                                            t.setSxServiceOptionVals(vals);
                                        }
                                        //不是最后一个，即不是当前，点击的选项值
                                        // 关联的有二种情况，一是 选中了，二是 默认选中。 都放到  队列里面
                                        sxServiceOptionTitleStacks.offer(t);
                                        //放入返回值的list
                                        //lists.add(t);
                                        addAndRemoveDuplicate(lists,t);
                                    }
                                }
                            }
                        }
                        //不是最后一个，即不是当前，点击的选项值。

                        //根据选项值，去查关联得 选项
                        //先把子节点加入
                        //List<SxServiceOptionTitle>  child  =  new ArrayList<>();
                        //sxServiceOptionTitleStacks.push();
                    }
                }
            }
        }
        return  lists;
    }

    //添加id，去重
    private void addStringAndNoDuplicate(List<String> oids,String oid){
        if(!oids.contains(oid)){
            oids.add(oid);
        }
    }

    //添加到集合，并且去重
    private void  addAndRemoveDuplicate(List<SxServiceOptionTitle>  lists,SxServiceOptionTitle sxServiceOptionTitle){
        List<String>  oids = new ArrayList<>();
        if(lists.size() > 0){
            for(SxServiceOptionTitle sso : lists){
                oids.add(sso.getOid());
            }
        }
        if(sxServiceOptionTitle !=null){
            if(!oids.contains(sxServiceOptionTitle.getOid())){
                lists.add(sxServiceOptionTitle);
            }
        }

    }

    //
    private boolean  judge(List<SxServiceOptionVal> sxServiceOptionVals,String valOids,String currentOid){
        List<SxServiceOptionTitle>  lists = new ArrayList<>();
        for(SxServiceOptionVal sxServiceOptionVal : sxServiceOptionVals){
            if(valOids.contains(sxServiceOptionVal.getOid())){

                if(currentOid.equals(sxServiceOptionVal.getOid())){
                    //一样，说明 到最后一个选中的了。
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 根据事项名称，模糊查询所有的事项
     *
     * @param
     * @return
     */
    public ApiResultSet<List<SxService>> getSxServiceListBySxServiceName(String serviceName) {
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        criteria.andDelFlagEqualTo((short)0);
        if(StringUtils.isNotEmpty(serviceName)){
            String value  =  "%"+serviceName+"%";
            criteria.andServiceNameLike(value);
        }
        // 只查好办易办事项
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        criteria.andHandleTypeIn(list);
        List<DbSxService>  dbSxServiceLists = dbSxServiceMapper.selectByExample(dbSxServiceExample);

        // 原接口改造 modifyBy 20220627 wangyg
        // 获取所有的事项细化材料
        DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
        DbRefinedMaterialExample.Criteria criteriaRefinedMaterial=dbRefinedMaterialExample.createCriteria();
        criteriaRefinedMaterial.andDeleteStatusEqualTo((short) 0);
        List<DbRefinedMaterial> dbRefinedMaterials=dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);

        Map<String, List<DbRefinedMaterial>> refinedMaterialMap = dbRefinedMaterials.stream().collect(Collectors.groupingBy(DbRefinedMaterial::getServiceOid));

        List<SxService>  sxServiceLists =  new ArrayList<>();

        for(DbSxService  dbSxService : dbSxServiceLists){
            SxService service = new SxService();
            BeanUtils.copyProperties(dbSxService,service);
            List<DbRefinedMaterial> lists = refinedMaterialMap.get(service.getServiceOid());
            int refinedMaterialNum = 0;
            if(lists!=null&&lists.size()>0){
                refinedMaterialNum = lists.size();
            }
            service.setRefinedMaterialNum(refinedMaterialNum);
            sxServiceLists.add(service);
        }

        return new ApiResultSet(sxServiceLists);
    }

    public SxService  saveSxService(SxService sxService){
        if(sxService!=null){
            DbSxServiceWithBLOBs record=new DbSxServiceWithBLOBs();
            //基础数据塞入
            sxService.setExistChildItem("0");
            sxService.setServiceStatus((short) 3);
            sxService.setDelFlag((short) 0);
            //判断更新保存
            if(StringUtil.isNotEmpty(sxService.getServiceOid())){
              DbSxService service=dbSxServiceMapper.getDbSxServiceByServiceOid(sxService.getServiceOid());
              if(service!=null){
                  sxService.setCreateDate(service.getCreateDate());
                  sxService.setCreateUser(service.getCreateUser());
                  sxService.setDistrictOid("4028545d665734290166b02711c20073");
//                  sxService.setDistrictOid(service.getDistrictOid());
//                  sxService.setOrganOid(service.getOrganOid()); //不给默认值
                  BeanUtil.copyProperties(sxService,record);

                  record.setModifyDate(new Date());
                  dbSxServiceMapper.updateByPrimaryKeyWithBLOBs(record);
              }
            }else{
                CurrentLoginUser sysUser=CurrentLoginUserHolder.getCurrentLoginUser();
                sxService.setServiceOid(UUID.randomUUID().toString().replace("-", ""));
                sxService.setCreateDate(new Date());
                sxService.setCreateUser(sysUser.getUserOid());
                sxService.setDistrictOid("4028545d665734290166b02711c20073");
//                sxService.setDistrictOid(sysUser.getDistrictOid());
//                sxService.setOrganOid(sysUser.getOrganOid()); //不给默认值
                BeanUtil.copyProperties(sxService,record);
                dbSxServiceMapper.insertSelective(record);
            }
            if(sxService.getSxServiceExtend()!=null){
                //根据事项主键查询扩展信息
             DbSxServiceExtendWithBLOBs extend= dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(sxService.getServiceOid());
             //获取扩展信息
             SxServiceExtend sxExtend= sxService.getSxServiceExtend();
             if(extend!=null){
                 sxExtend.setModifyDate(new Date());
                 sxExtend.setExtendOid(extend.getExtendOid());
                 sxExtend.setServiceOid(extend.getServiceOid());
                 BeanUtil.copyProperties(sxExtend,extend);
                 dbSxServiceExtendMapper.updateByPrimaryKeyWithBLOBs(extend);
             }else{
                 extend=new DbSxServiceExtendWithBLOBs();
                 sxExtend.setExtendOid(UUID.randomUUID().toString().replace("-", ""));
                 sxExtend.setServiceOid(sxService.getServiceOid());
                 sxExtend.setModifyDate(new Date());
                 BeanUtil.copyProperties(sxExtend,extend);
                 dbSxServiceExtendMapper.insert(extend);
             }
            }
        }
        return sxService;
    }
    //删除事项
    public Boolean delSxServiceByOid(String serviceOid){
        if(StringUtil.isNotEmpty(serviceOid)){
          DbSxService sxService=  dbSxServiceMapper.getDbSxServiceByServiceOid(serviceOid);
          if(sxService!=null){
              sxService.setDelFlag(Short.valueOf("1"));
              sxService.setModifyDate(new Date());
              dbSxServiceMapper.updateByPrimaryKey(sxService);
              return true;
          }
        }
        return false;
    }

  public Map<String,Object>  getDistInfo(){
      Map<String,Object> map=new HashMap<>();
      //权力来源
      map.put("qlly", SxptBaseStaticParameter.originList);
      //主题分类
      map.put("ztfl",SxptBaseStaticParameter.SUB_CLASS);
      //生命周期分类
      map.put("smzq",SxptBaseStaticParameter.LIFE_CYCLE);
      //办事群体
      map.put("bsqt",SxptBaseStaticParameter.SERVICE_GROUP);
      //办理深度
      map.put("blsd",SxptBaseStaticParameter.handleDepths);
      //实施主体性质
      map.put("ssztxz",SxptBaseStaticParameter.organPropertys);
      //通办范围
      map.put("tbfw",SxptBaseStaticParameter.handleScopes);
      //收费环节
      map.put("XZSP",SxptBaseStaticParameter.chargeLink);
      //行使层级
      map.put("XSCJ",SxptBaseStaticParameter.levelList);

      return map;

    }

    public List<SxServiceOptionTitle> querySxServiceSituationRelationBySituationAndTitle(String serviceOid, String titleOid, String situationId) {
        SxServiceOptionTitle currentTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleOid);
        List<SxServiceOptionVal> currentVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(currentTitle.getOid());
        //当前选项下的选项值id集合
        List<String>  valoIds  = new ArrayList<>();
        for(SxServiceOptionVal sxServiceOptionVal : currentVals ){
            valoIds.add(sxServiceOptionVal.getOid());
        }
        //情形关联的所有的选项值的id集合
        List<String>  valIds = new ArrayList<>();
        Map<String, Object> sxServiceSituationOption =  sxServiceSituationOptionManager.getOptionTitleAndValsOfSituation(situationId);
        if(sxServiceSituationOption.containsKey("sxServiceSituationOption")){
            com.alibaba.fastjson.JSONObject job  =  (com.alibaba.fastjson.JSONObject)sxServiceSituationOption.get("sxServiceSituationOption");
            //SxServiceSituationOption situationOption =  (SxServiceSituationOption)sxServiceSituationOption.get("sxServiceSituationOption");
            SxServiceSituationOption situationOption =  (SxServiceSituationOption)com.alibaba.fastjson.JSONObject.toJavaObject(job,SxServiceSituationOption.class);
            for(SxServiceOptionTitle ssot : situationOption.getSxServiceOptionTitles()){
/*                if(titleOid.equalsIgnoreCase(ssot.getOid())){
                    for(SxServiceOptionVal ssov : ssot.getSxServiceOptionVals()){
                        if("1".equalsIgnoreCase(ssov.getIsSelected())){
                            valIds.add(ssov.getOid());
                        }
                    }
                    break;
                }*/
                    for(SxServiceOptionVal ssov : ssot.getSxServiceOptionVals()){
                        if("1".equalsIgnoreCase(ssov.getIsSelected())){
                            valIds.add(ssov.getOid());
                        }
                    }
            }
        }
        List<SxServiceOptionTitle>  sxServiceOptionTitleLists = new ArrayList<>();
        //将自己加入
        query(currentTitle,valIds);
        sxServiceOptionTitleLists.add(currentTitle);
        for(String  id: valIds){
            SxServiceOptionVal sxServiceOptionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(id);
            SxServiceOptionTitle title = sxServiceOptionTitleManager.getServiceOptionTitleByOid(sxServiceOptionVal.getTitleOid());
            //SxServiceOptionTitle ct = sxServiceOptionTitleManager.getServiceOptionTitleByOid(title.getOid());
            //选中的选项值
            query(title,valIds);
            sxServiceOptionTitleLists.add(title);

            List<ServiceOptionRel>  serviceOptionRelList  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, id);
            for(ServiceOptionRel serviceOptionRel : serviceOptionRelList){
                String[] titleIds = serviceOptionRel.getTitleOids().split(",");
                for(String titleId : titleIds){
                    SxServiceOptionTitle t = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleId);
                    query(t,valIds);
                    sxServiceOptionTitleLists.add(t);
                }
            }

        }
        //去重
        List<SxServiceOptionTitle>  returnTitles = new ArrayList<>();
        List<String> reductIds = new ArrayList<>();
        for(SxServiceOptionTitle  title   : sxServiceOptionTitleLists){
            if(!reductIds.contains(title.getOid())){
                returnTitles.add(title);
                reductIds.add(title.getOid());
            }
        }

        //排序
        Collections.sort(returnTitles, (o1, o2) -> {
            int diff =  Integer.parseInt(String.valueOf(o2.getSort())) -  Integer.parseInt(String.valueOf(o1.getSort()));
            if(diff > 0){
                return  -1;
            }else if(diff < 0){
                return  1;
            }
            return 0;
        });
        return returnTitles;
    }


    public List<SxServiceOptionTitle> querySxServiceSituationRelationBySituationAndTitle2(String serviceOid, String titleOid, String situationId) {
        SxServiceOptionTitle currentTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleOid);
        List<SxServiceOptionVal> currentVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(currentTitle.getOid());
        //当前选项下的选项值id集合
        List<String>  valoIds  = new ArrayList<>();
        for(SxServiceOptionVal sxServiceOptionVal : currentVals ){
            valoIds.add(sxServiceOptionVal.getOid());
        }
        //情形关联的所有的选项值的id集合
        List<String>  valIds = new ArrayList<>();
        Map<String, Object> sxServiceSituationOption =  sxServiceSituationOptionManager.getOptionTitleAndValsOfSituation(situationId);
        if(sxServiceSituationOption.containsKey("sxServiceSituationOption")){
            com.alibaba.fastjson.JSONObject job  =  (com.alibaba.fastjson.JSONObject)sxServiceSituationOption.get("sxServiceSituationOption");
            //SxServiceSituationOption situationOption =  (SxServiceSituationOption)sxServiceSituationOption.get("sxServiceSituationOption");
            SxServiceSituationOption situationOption =  (SxServiceSituationOption)com.alibaba.fastjson.JSONObject.toJavaObject(job,SxServiceSituationOption.class);
            for(SxServiceOptionTitle ssot : situationOption.getSxServiceOptionTitles()){
                if(titleOid.equalsIgnoreCase(ssot.getOid())){
                    for(SxServiceOptionVal ssov : ssot.getSxServiceOptionVals()){
                        valIds.add(ssov.getOid());
                    }
                    break;
                }
            }
        }
        List<SxServiceOptionTitle>  sxServiceOptionTitleLists = new ArrayList<>();
        //将自己加入
        query(currentTitle,valIds);
        sxServiceOptionTitleLists.add(currentTitle);
        Stack<String>  stack = new Stack<>();
        pushVaIds(stack,valoIds,valIds);
        //遍历过的选项值id
        List<String>  hasRead = new ArrayList<>();
        while (!stack.isEmpty()&&(hasRead.size()!=valIds.size())){
            //选项值id
            String id = stack.pop();
            //被情形关联
            if(valIds.contains(id)){
                //找到  对应的 SxServiceOptionTitle 放入集合，返回页面，遍历
                SxServiceOptionVal sxServiceOptionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(id);
                SxServiceOptionTitle title = sxServiceOptionTitleManager.getServiceOptionTitleByOid(sxServiceOptionVal.getTitleOid());
                //SxServiceOptionTitle ct = sxServiceOptionTitleManager.getServiceOptionTitleByOid(title.getOid());
                //选中的选项值
                query(title,valIds);
                sxServiceOptionTitleLists.add(title);
                //先把子节点加入
                List<String>  child  =  new ArrayList<>();
                List<ServiceOptionRel>  serviceOptionRelList  = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid, id);
                for(ServiceOptionRel serviceOptionRel : serviceOptionRelList){
                    String[] titleIds = serviceOptionRel.getTitleOids().split(",");
                    for(String titleId : titleIds){
                        SxServiceOptionTitle t = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleId);
                        for(SxServiceOptionVal sso: t.getSxServiceOptionVals()){
                            child.add(sso.getOid());
                        }
                    }
                }
                pushVaIds(stack,child,valIds);
                //pushVaIds
                List<SxServiceOptionVal>  sxServiceOptionValLists  = title.getSxServiceOptionVals();
                List<String>  vs  =  new ArrayList<>();
                for(SxServiceOptionVal  sv : sxServiceOptionValLists){
                    vs.add(sv.getOid());
                }
                //移除关联得
                vs.remove(id);
                pushVaIds(stack,vs,valIds);
                if(!hasRead.contains(id)){
                    hasRead.add(id);
                }
            }
        }
        //去重
        List<SxServiceOptionTitle>  returnTitles = new ArrayList<>();
        List<String> reductIds = new ArrayList<>();
        for(SxServiceOptionTitle  title   : sxServiceOptionTitleLists){
            if(!reductIds.contains(title.getOid())){
                returnTitles.add(title);
                reductIds.add(title.getOid());
            }
        }

        //排序
        Collections.sort(returnTitles, (o1, o2) -> {
            int diff =  Integer.parseInt(String.valueOf(o2.getSort())) -  Integer.parseInt(String.valueOf(o1.getSort()));
            if(diff > 0){
                return  -1;
            }else if(diff < 0){
                return  1;
            }
            return 0;
        });
        return returnTitles;
    }


    private void pushVaIds(Stack<String>  stack,List<String>  valIds,List<String>  has){
        for(String valId : valIds){
            if(!stack.contains(valId)&&has.contains(valId)){
                stack.push(valId);
            }
            //stack.push(valId);
        }
    }

    //情形关联的所有的选项值的id集合
    private void  query(SxServiceOptionTitle currentTitle,List<String>  oids) {
        if (currentTitle != null) {
            List<SxServiceOptionVal> currentVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(currentTitle.getOid());
            if (currentVals != null) {
                for (SxServiceOptionVal val : currentVals) {
                    if (oids.contains(val.getOid())) {
                        val.setIsSelected("1");
                    }
                }
                currentTitle.setSxServiceOptionVals(currentVals);
            }
        }
    }

    public List<Map<String, Object>> listOrganByDistrictAndService(String districtOid, String handleType) {
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        if(StrUtil.isNotEmpty(districtOid)){
            criteria.andDistrictOidEqualTo(districtOid);
        }
        // 好办快办参数
        if(StrUtil.isNotEmpty(handleType)){
            criteria.andHandleTypeEqualTo(handleType);
        }
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        criteria.andExistChildItemEqualTo("0");
        criteria.andServiceStatusEqualTo((short) 3);
        List<DbSxService> dbSxServices = dbSxServiceMapper.selectByExample(dbSxServiceExample);

        List<Map<String, Object>> organList = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        dbSxServices.forEach(dbSxService -> {
            if(StrUtil.isNotEmpty(dbSxService.getOrganOid())){
                SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(dbSxService.getOrganOid()).getData();
                if(null != sysOrgan){
                    if(null == result.get(sysOrgan.getOrganOid())) {
                        result.put(sysOrgan.getOrganOid(), sysOrgan.getOrganOid());
                        Map<String, Object> map = new HashMap<>(2);
                        map.put("organOid", sysOrgan.getOrganOid());
                        map.put("name", sysOrgan.getName());
                        organList.add(map);
                    }
                }
            }
        });
        return organList;
    }

    public PageResult<SxService>  querySxServiceList(Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        //criteria.andExistChildItemEqualTo("0");
        Page<DbSxService> dbSxServices = (Page<DbSxService>)dbSxServiceMapper.selectOidAndShieldList(dbSxServiceExample);
        PageResult<SxService> pageResult = new PageResult<>(dbSxServices.getPageNum(),dbSxServices.getPageSize(),dbSxServices.getTotal());
        List<SxService> sxServiceList = dbSxServices.stream().map(dbSxService -> {
            SxService service = new SxService();
            BeanUtils.copyProperties(dbSxService,service);
            return service;
        }).collect(Collectors.toList());
        pageResult.setData(sxServiceList);
        return pageResult;
    }

}

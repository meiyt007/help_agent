package com.zfsoft.single.controller.insideapi;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zfsoft.cases.config.IpConfiguration;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseLinkResult;
import com.zfsoft.cases.feign.*;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseLinkResultService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.data.sys.*;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.sxDirectory.service.SxServiceTypeService;
import com.zfsoft.service.sxService.service.*;
import com.zfsoft.service.sxSys.service.SxSysAttaService;
import com.zfsoft.single.data.yxpz.ServiceSiteAuthorize;
import com.zfsoft.single.data.yxpz.SxServiceRegistrar;
import com.zfsoft.single.feign.settings.*;
import com.zfsoft.single.manager.yxpz.ServiceSiteAuthorizeManager;
import com.zfsoft.single.manager.yxpz.SxServiceRegistrarManager;
import com.zfsoft.single.service.insideapi.pbpj.IPbpjService;
import com.zfsoft.single.util.CommonUtil;
import com.zfsoft.single.util.HttpRequestUtil;
import com.zfsoft.single.util.SM2Utils.SM2Util;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.ZFExclusionStrategy;
import com.zfsoft.superwindow.data.pbpj.*;
import com.zfsoft.superwindow.data.yxpz.PbpjHistoryManage;
import com.zfsoft.superwindow.data.yxpz.PbpjManage;
import com.zfsoft.superwindow.data.yxpz.ShowInformation;
import com.zfsoft.superwindow.data.yxpz.SysSiteDistrictRelation;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.feign.settings.SysLoginFeginService;
import com.zfsoft.superwindow.service.pbpj.PbpjHistoryManageService;
import com.zfsoft.superwindow.service.yxpz.PbpjManageService;
import com.zfsoft.superwindow.service.yxpz.ShowInformationService;
import com.zfsoft.superwindow.service.yxpz.SysSiteDistrictRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.zfsoft.sxService.data.*;

/**
 * @（#）: PbpjController
 * @description: 平板评价接口实现类
 * @author: wangwg
 * @date: 2020/12/02
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class PbpjController implements IPbpjService {

    @Value("${zfsoft.pbpj.key}")
    private String pbpjKey;

    @Value("${zfsoft.pbpj.secret}")
    private String pbpjSecret;

    @Value("${zfsoft.pbpj.privateKey}")
    private String pbpjPrivateKey;

    @Value("${zfsoft.pbpj.publicKey}")
    private String pbpjPublicKey;

    @Value("${zfsoft.pbpj.url}")
    private String pbpjUrl;

    @Value("${zfsoft.pbpj.pbpjPage}")
    private String pbpjPage;

    @Value("${zfsoft.pbpj.saveEvaluateCase}")
    private String pbpjSaveCase;

    @Value("${zfsoft.pbpj.saveEvaluateInfo}")
    private String saveEvaluateInfo;

    @Value("${zfsoft.pbpj.pbpjCallBackUrl}")
    private String pbpjCallBack;

    @Resource
    private IpConfiguration ipConfiguration;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Resource
    private SysLoginFeginService sysLoginFeginService;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private SysAttaFeginService sysAttaFeginService;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private SxServiceMaterialService sxServiceMaterialFeginService;

    @Resource
    private SxServiceTypeService sxServiceTypeFeginService;

    @Resource
    private SxServiceExtendService sxServiceExtendFeginService;

    @Resource
    private SxSysAttaService sxServiceAttaFeignService;

    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    @Resource
    private SxServiceQuestionService sxServiceQuestionFeginService;

    @Resource
    private SxServiceLinkService sxServiceLinkFeginService;

    @Resource
    private SxServiceLocationService sxServiceLocationFeginService;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;

    @Resource
    private PbpjManageService pbpjManageFeginService;

    @Resource
    private PbpjHistoryManageService pbpjHistoryManageFeginService;

    @Resource
    private ShowInformationService showInformationFeginService;

    @Resource
    private SxServiceRegistrarManager sxServiceRegistrarManager;
    @Resource
    private QlCaseLinkResultService qlCaseLinkResultServiceFeginService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SysSiteDistrictRelationService sysSiteDistrictRelationFeginService;
    @Resource
    private ServiceSiteAuthorizeManager serviceSiteAuthorizeManager;

    @Override
    public String login(String code, String pass, String runCode, String name, String iP) {
        ResultObject ro = new ResultObject();
        if (StringUtils.isEmpty(code)) {
            ro.setResultCode("0");
            ro.setResuleMessage("用户名不能为空");
        } else if (StringUtils.isEmpty(pass)) {
            ro.setResultCode("0");
            ro.setResuleMessage("密码不能为空");
        } else if (StringUtils.isEmpty(runCode)) {
            ro.setResultCode("0");
            ro.setResuleMessage("设备编号不能为空");
        } else if (StringUtils.isEmpty(name)) {
            ro.setResultCode("0");
            ro.setResuleMessage("设备名称不能为空");
        } else {
            SysUser sysUser = null;
            ApiResultSet<SysLogin> sysLoginResult = sysLoginFeginService.getSysLoginByAccount(code);
            ApiResultSet<PbpjManage> result=pbpjManageFeginService.getPbpjManageByUserCode(code);
            PbpjManage usrPb=null;
            if(result.getData()!=null){
                usrPb = result.getData();
            }
            //根据设备编号查找设备信息
            ApiResultSet<PbpjManage> manage=pbpjManageFeginService.getPbpjManageByRunCode(runCode);
            PbpjManage pr=null;
            if(manage.getData()!=null){
                pr=manage.getData();
            }
            if (sysLoginResult.getData() == null) {
                ro.setResultCode("0");
                ro.setResuleMessage("用户名不存在");
            } else if (!CommonUtil.md5(pass).equals(sysLoginResult.getData().getPassword())) {
                ro.setResultCode("0");
                ro.setResuleMessage("密码不正确");
            } else if (usrPb != null) {
                if ((!StringUtils.isEmpty(usrPb.getRunCode()) && usrPb.getRunCode().equals(runCode))
                        && (!StringUtils.isEmpty(usrPb.getUserCode()) && usrPb.getUserCode().equals(code))) {
                    pr.setDateTime(new Date());
                    pbpjManageFeginService.updatePbpjManage(pr);
                    //插入到设备历史表
                    PbpjHistoryManage history = new PbpjHistoryManage();
                    history.setRunCode(pr.getRunCode());
                    history.setName(pr.getName());
                    history.setIp(pr.getIp());
                    history.setRemark(pr.getRemark());
                    history.setUserCode(pr.getUserCode());
                    history.setUserName(pr.getUserName());
                    history.setDateTime(pr.getDateTime());
                    history.setPbpjType(0);
                    pbpjHistoryManageFeginService.savePbpjHistoryManage(history);
                    ro.setResultCode("1");
                    //格式化数据，返回app
                    UserObject user = new UserObject();
                    OrganObject organ = new OrganObject();
                    if (StringUtils.isNotEmpty(sysLoginResult.getData().getUserOid())) {
                        ApiResultSet<SysUser> userResult = sysUserFeginService.getSysUserByUserOid(sysLoginResult.getData().getUserOid());
                        sysUser = userResult.getData();
                    }
                    user.setOid(sysUser.getUserOid());
                    user.setCode(sysUser.getAccount());
                    user.setName(sysUser.getName());
                    user.setAreaId(sysUser.getDistrictOid());
                    user.setAreaName(sysUser.getDistrictName());
                    if (StringUtils.isEmpty(user.getPjqFlag()) || user.getPjqFlag().equals("1")) {
                        user.setPjqFlag("0");
                    } else {
                        user.setPjqFlag("1");
                    }
                    if (StringUtils.isEmpty(user.getPosition())) {
                        user.setPosition("");
                    } else {
                       /* ApiResultSet<SysDict> dictResult = sysDictFeignService.getSysDictByDictOid(user.getPosition());
                        if (dictResult.getData() == null) {
                            user.setPosition("");
                        } else {
                            user.setPosition(dictResult.getData().getName());
                        }*/
                        user.setPosition(sysUser.getPosition());
                    }
                    user.setMobile(sysUser.getMobile());
                    if (StrUtil.isNotEmpty(sysUser.getHeadImageAttaOid())) {
                        ApiResultSet<SysAtta> sysAttaResult = sysAttaFeginService.getSysAttaByAttaOid(sysUser.getHeadImageAttaOid());
                        if (sysAttaResult.getData() == null) {
                            user.setUserImg("");
                        } else {
                            String imgUrl = sysAttaResult.getData().getFastdfsNginxUrl();
                            user.setUserImg(imgUrl);
                        }
                    }
             /*       Integer avg = qlCaseEvaluateService.getAvgQlCaseEvaluate(sysLogin.getUserOid());
                    if (avg == null) {
                        user.setXjFlag("3");
                        user.setHistoryPj("3");
                    } else {
                        user.setXjFlag(String.valueOf(avg));
                        user.setHistoryPj(String.valueOf(avg));
                    }*/
                    organ.setOid(sysUser.getOrganOid());
                    ApiResultSet<SysOrgan> sysOrganResult = sysOrganFeginService.getSysOrganByOrganOid(sysUser.getOrganOid());
                    if (sysOrganResult.getData() != null) {
                        organ.setOrgname(sysOrganResult.getData().getName());
                    }
                    user.setOrgan(organ);
                    ro.setResuleMessage(user);

                } else {
                    ro.setResultCode("0");
                    ro.setResuleMessage("该用户已在其他平板上登录，无法同时登录两个平板");
                }
            } else {
                if (pr == null) {
                    pr = new PbpjManage();
                    pr.setRunCode(runCode);
                    pr.setName(name);
                    pr.setIp(iP);
                    pr.setStatus(0);
                    pr.setUserCode(sysLoginResult.getData().getAccount());
                    pr.setUserName(sysLoginResult.getData().getName());
                    pr.setDateTime(new Date());
                    pr.setPbpjType(0);
                    pbpjManageFeginService.savePbpjManage(pr);
                } else {
                    pr.setStatus(0);
                    pr.setUserCode(sysLoginResult.getData().getAccount());
                    pr.setUserName(sysLoginResult.getData().getName());
                    pr.setDateTime(new Date());
                    pr.setPbpjType(0);
                    pbpjManageFeginService.savePbpjManage(pr);
                }
                //插入到设备历史表
                PbpjHistoryManage history = new PbpjHistoryManage();
                history.setRunCode(pr.getRunCode());
                history.setName(pr.getName());
                history.setIp(pr.getIp());
                history.setRemark(pr.getRemark());
                history.setUserCode(pr.getUserCode());
                history.setUserName(pr.getUserName());
                history.setDateTime(pr.getDateTime());
                pbpjHistoryManageFeginService.savePbpjHistoryManage(history);
                ro.setResultCode("1");
                // 格式化数据，返回app
                UserObject user = new UserObject();
                OrganObject organ = new OrganObject();
                user.setOid(sysLoginResult.getData().getUserOid());
                user.setCode(sysLoginResult.getData().getAccount());
                ApiResultSet<SysUser> userResult = sysUserFeginService.getSysUserByUserOid(sysLoginResult.getData().getUserOid());
                sysUser = userResult.getData();
                user.setMobile(sysUser.getMobile());
                user.setName(sysUser.getName());
                user.setAreaId(sysUser.getDistrictOid());
                ApiResultSet<SysDistrict> districtResult = sysDistrictFeginService.getSysDistrictByDistrictOid(sysUser.getDistrictOid());
                if (districtResult.getData() != null) {
                    user.setAreaName(districtResult.getData().getName());
                }
                if (StringUtils.isEmpty(sysUser.getPosition()) && sysUser.getPosition() != "") {
                    user.setPosition("");
                } else {
                    /*ApiResultSet<SysDict> dictResult = sysDictFeignService.getSysDictByDictOid(user.getPosition());
                    if (dictResult.getData() == null) {
                        user.setPosition("");
                    } else {
                        user.setPosition(dictResult.getData().getName());
                    }*/
                    user.setPosition(sysUser.getPosition());
                }
                if (StringUtils.isEmpty(user.getPjqFlag()) || user.getPjqFlag().equals("1")) {
                    user.setPjqFlag("0");
                } else {
                    user.setPjqFlag("1");
                }
                if (StrUtil.isNotEmpty(sysUser.getHeadImageAttaOid())) {
                    ApiResultSet<SysAtta> sysAttaResult = sysAttaFeginService.getSysAttaByAttaOid(sysUser.getHeadImageAttaOid());
                    if (sysAttaResult.getData() == null) {
                        user.setUserImg("");
                    } else {
                        String imgUrl = sysAttaResult.getData().getFastdfsNginxUrl();
                        user.setUserImg(imgUrl);
                    }
                }
                organ.setOid(sysUser.getOrganOid());
                ApiResultSet<SysOrgan> sysOrganResult = sysOrganFeginService.getSysOrganByOrganOid(sysUser.getOrganOid());
                if (sysOrganResult.getData() != null) {
                    organ.setOrgname(sysOrganResult.getData().getName());
                }
                user.setOrgan(organ);
                ro.setResuleMessage(user);
            }
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getPbSysList() {
        ResultObject ro = new ResultObject();
        try {
            ApiResultSet<List<SysConfig>> resultSet = sysConfigFeignService.querySysConfigListByParentCode("PBYY");
            List<PbpjVoiceManage> yuList = new ArrayList<PbpjVoiceManage>();
            for (SysConfig sys : resultSet.getData()) {
                PbpjVoiceManage pbpj = new PbpjVoiceManage();
                pbpj.setOid(sys.getConfigOid());
                pbpj.setSysNum(sys.getValue());
                pbpj.setSysContent(sys.getAttaOid());
                pbpj.setStatus(sys.getIsAble().toString());
                pbpj.setSysRemark(sys.getName());
                yuList.add(pbpj);
            }
            if (yuList == null || yuList.size() == 0) {
                ro.setResultCode("0");
                ro.setResuleMessage("语音集合不存在");
            } else {
                ro.setResultCode("1");

                for (int i = 0; i < yuList.size(); i++) {
                    if (yuList.get(i).getStatus().equals("Y")) {
                        yuList.get(i).setStatus("1");
                    } else if (yuList.get(i).getStatus().equals("N")) {
                        yuList.get(i).setStatus("0");
                    } else {
                        yuList.get(i).setStatus("1");
                    }
                }
                ro.setResuleMessage(yuList);
            }
        } catch (Exception e) {
            ro.setResultCode("0");
            ro.setResuleMessage("对不起！该用户没有启用平板评价");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getPjType() {
        ResultObject ro = new ResultObject();
        try {
            ApiResultSet<List<SysConfig>> configList = sysConfigFeignService.querySysConfigListByParentCode("PJXGL");
            ApiResultSet<List<SysConfig>> reasonList = sysConfigFeignService.querySysConfigListByParentCode("PBPJ_REASON");
            List<PbpjType> typeList = new ArrayList<PbpjType>();
            for (SysConfig sys : configList.getData()) {
                PbpjType pbpj = new PbpjType();
                pbpj.setPjTypeId(sys.getValue());
                pbpj.setMydFlag(sys.getIsAble().toString());
                pbpj.setImgUrl(sys.getAttaOid());
                pbpj.setPjNumValue(sys.getValue());
                pbpj.setPjTypeName(sys.getName());
                pbpj.setPjTypeValue(sys.getValue());
                if ("1".equals(sys.getValue()) || "2".equals(sys.getValue())) {
                    if (reasonList.getData() != null && reasonList.getData().size() > 0) {
                        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        for (SysConfig reason : reasonList.getData()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("oid", reason.getConfigOid());
                            map.put("reason", reason.getName());
                            list.add(map);
                        }
                        pbpj.setReasonList(list);
                    }
                } else {
                    pbpj.setReasonList(null);
                }
                typeList.add(pbpj);
            }
            if (typeList == null || typeList.size() == 0) {
                ro.setResultCode("0");
                ro.setResuleMessage("评价项不存在");
            } else {
                ro.setResultCode("1");
                ro.setResuleMessage(typeList);
            }
        } catch (Exception e) {
            ro.setResultCode("0");
            ro.setResuleMessage("对不起！该用户没有启用平板评价");
        }
        ZFExclusionStrategy zfes = new ZFExclusionStrategy(new Class[]{PbpjType.class, ResultObject.class});
        zfes.setReverse(true);
        zfes.setFields(ResultObject.class, new String[]{"resultCode", "resuleMessage"});
        zfes.setFields(PbpjType.class, new String[]{"pjTypeId", "pjTypeName", "pjNumValue", "pjTypeValue", "imgUrl", "delFlag", "mydFlag", "reasonList"});
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getPtUserById(String userOid) {
        ResultObject ro = new ResultObject();
        try {
            if (StringUtils.isEmpty(userOid)) {
                ro.setResultCode("0");
                ro.setResuleMessage("用户主键不能为空");
            } else {
                ApiResultSet<SysLogin> loginResult = sysLoginFeginService.getSysLoginByUserOid(userOid);
                if (loginResult.getData() == null) {
                    ro.setResultCode("0");
                    ro.setResuleMessage("用户对象不存在");
                } else {
                    ApiResultSet<SysUser> userResult = sysUserFeginService.getSysUserByUserOid(userOid);
                    ro.setResultCode("1");
                    UserObject userObject = new UserObject();
                    OrganObject organ = new OrganObject();
                    userObject.setOid(loginResult.getData().getLoginOid());
                    if (StringUtils.isEmpty(userResult.getData().getPosition())) {
                        userObject.setPosition("");
                    } else {
                        ApiResultSet<SysDict> dictResult = sysDictFeignService.getSysDictByDictOid(userResult.getData().getPosition());
                        if (dictResult.getData() == null) {
                            userResult.getData().setPosition("");
                        } else {
                            userResult.getData().setPosition(dictResult.getData().getName());
                        }
                    }
                    userObject.setCode(userResult.getData().getAccount());
                    userObject.setName(userResult.getData().getName());
                    userObject.setPass(userResult.getData().getPassword());
                    userObject.setPjqFlag("1");
                    userObject.setAreaId(userResult.getData().getDistrictOid());
                    ApiResultSet<SysDistrict> districtResult = sysDistrictFeginService.getSysDistrictByDistrictOid(userResult.getData().getDistrictOid());
                    if (districtResult.getData() != null) {
                        userObject.setAreaName(districtResult.getData().getName());
                    }
                    userObject.setMobile(userResult.getData().getMobile());
                    userObject.setUserImg(userResult.getData().getHeadImageAttaOid());
                 /*   Integer avg = qlCaseEvaluateService.getAvgQlCaseEvaluate(user.getOid());
                    if (avg == null) {
                        userObject.setXjFlag("3");
                        userObject.setHistoryPj("3");
                    } else {
                        userObject.setXjFlag(String.valueOf(avg));
                        userObject.setHistoryPj(String.valueOf(avg));
                    }*/
                    organ.setOid(userResult.getData().getOrganOid());
                    ApiResultSet<SysOrgan> sysOrganResult = sysOrganFeginService.getSysOrganByOrganOid(userResult.getData().getOrganOid());
                    if (sysOrganResult.getData() != null) {
                        organ.setOrgname(sysOrganResult.getData().getName());
                    }
                    userObject.setOrgan(organ);
                    ro.setResuleMessage(userObject);
                }
            }

        } catch (Exception e) {
            ro.setResultCode("0");
            ro.setResuleMessage("对不起！该用户没有启用平板评价");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getPtService(String serviceoid, String code) {
        ResultObject ro = new ResultObject();
        if (StringUtils.isEmpty(serviceoid)) {
            ro.setResultCode("0");
            ro.setResuleMessage("事项主键不能为空");
        } else {
            try {
                ApiResultSet<SxService> serviceResult = sxServiceFeginService.getSxServiceByOid(serviceoid);
                if (serviceResult.getData() == null) {
                    ro.setResultCode("0");
                    ro.setResuleMessage("事项不存在");
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("itemName", serviceResult.getData().getServiceName()); //事项名称
                    if (serviceResult.getData().getOrganOid() != null) {
                        ApiResultSet<SysOrgan> organResult = sysOrganFeginService.getSysOrganByOrganOid(serviceResult.getData().getOrganOid());
                        if (organResult.getData() == null) {
                            map.put("s_orgname", organResult.getData().getName());  //事项办理机构名称
                        }
                    }
                    ApiResultSet<SxServiceExtend> extendResult = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(serviceResult.getData().getServiceOid());
                    if (extendResult.getData() != null) {
                        map.put("workdays", extendResult.getData().getPromiseLimit());  //承诺办理时限
                        if (extendResult.getData().getLegalLimit() != null) {
                            map.put("limitlastdate", extendResult.getData().getLegalLimit());  //预计办结时间日期
                        } else {
                            map.put("limitlastdate", "");
                        }
                    }
                    map.put("freeflag", serviceResult.getData().getChargeFlag());  //是否收费
                    map.put("bl_lxdh", serviceResult.getData().getZxDhText());  //咨询电话
                    map.put("s_jddh", serviceResult.getData().getTsDhText());  //监督电话
                    ro.setResultCode("1");
                    ro.setResuleMessage(map);
                }
            } catch (Exception e) {
                ro.setResultCode("0");
                ro.setResuleMessage("对不起！该用户没有启用平板评价");
            }
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getCaseByCaseNumber(String number) {
        ResultObject ro = new ResultObject();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ApiResultSet<QlCase> qlCaseResult = qlCaseServiceFeginService.queryQlCaseByCaseNumber(number);
            if (qlCaseResult.getData() != null) {
                ApiResultSet<SxService> serviceResult = sxServiceFeginService.getSxServiceByOid(qlCaseResult.getData().getServiceOid());
                ApiResultSet<QlCaseApplay> applayResult = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(qlCaseResult.getData().getCaseOid());
                map.put("serviceOid", qlCaseResult.getData().getServiceOid());//事项id
                map.put("serviceName", serviceResult.getData().getServiceName());//事项名称
                map.put("applyProjectName", qlCaseResult.getData().getProjectName());//申报项目
                map.put("applyUserName", applayResult.getData().getApplyUserName());//申报人姓名
                if (qlCaseResult.getData().getCaseStatus() == 0) {
                    map.put("caseStatus", "暂存");
                } else if (qlCaseResult.getData().getCaseStatus() == 1) {
                    map.put("caseStatus", "待预审");
                } else if (qlCaseResult.getData().getCaseStatus() == 2) {
                    map.put("caseStatus", "办理中");
                } else if (qlCaseResult.getData().getCaseStatus() == 3) {
                    //查询办理环节判断是否是不予受理办结
                   ApiResultSet<List<QlCaseLinkResult>> link= qlCaseLinkResultServiceFeginService.queryQlCaseLinkResultListByCaseOid(qlCaseResult.getData().getCaseOid());
                   if(link!=null &&link.getData()!=null&&link.getData().size()>0){
                       for(QlCaseLinkResult result: link.getData()){
                           if(result.getFinalStatus()==2 ||result.getFinalStatus()==0){
                               map.put("caseStatus", "不予受理");
                           }else if(result.getFinalStatus()==47){
                               map.put("caseStatus", "预审不通过");
                           }else{
                               map.put("caseStatus", "办结");
                           }
                       }
                   }else{
                       map.put("caseStatus", "办结");
                   }
                } else if (qlCaseResult.getData().getCaseStatus() == 5) {
                    map.put("caseStatus", "异常办结");
                } else if (qlCaseResult.getData().getCaseStatus() == -1) {
                    map.put("caseStatus", "作废");
                }else {
                    map.put("caseStatus", "暂无");
                }
                if (null != qlCaseResult.getData().getAcceptanceDate()) {
                    map.put("acceptanceDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(qlCaseResult.getData().getAcceptanceDate()));//登记日期
                } else {
                    map.put("acceptanceDate", "");
                }
                if (null != qlCaseResult.getData().getConcludeDate()) {
                    map.put("concludeDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(qlCaseResult.getData().getConcludeDate()));//办结时间
                } else {
                    map.put("concludeDate", "");
                }
                ro.setResultCode("1");
                ro.setResuleMessage(map);
            } else {
                ro.setResultCode("0");
                ro.setResuleMessage("未查询到信息!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ro.setResultCode("-1");
            ro.setResuleMessage("未查询到信息!");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getSVByCode(Integer code) {
        ResultObject ro = new ResultObject();
        try {
            if (code == null) {
                ro.setResultCode("0");
                ro.setResuleMessage("版本号为空");
            }
            ApiResultSet<List<SysConfig>> resultSet = sysConfigFeignService.querySysConfigListByParentCode("PBGL_INFO");
            List<SysConfig> list = new ArrayList<SysConfig>();
            for (SysConfig conf : resultSet.getData()) {
                if ("Y".equals(conf.getIsAble())) {
                    list.add(conf);
                }
            }
            if (list == null || list.size() < 1) {
                ro.setResultCode("0");
                ro.setResuleMessage("已是最新版本");
            } else {
                SysConfig fig = list.get(0);
                if (fig == null || code >= Integer.valueOf(fig.getValue())) {
                    ro.setResultCode("0");
                    ro.setResuleMessage("已是最新版本");
                } else {
                    if (!StringUtils.isEmpty(fig.getAttaOid())) {
                        VersionObject res = new VersionObject();
                        res.setCode(fig.getValue());
                        res.setMemo(fig.getMemo());
                        res.setUrl(fig.getAttaOid());
                        ro.setResultCode("1");
                        ro.setResuleMessage(res);
                    } else {
                        ro.setResultCode("0");
                        ro.setResuleMessage("没有上传apk");
                    }
                }
            }
        } catch (Exception e) {
            ro.setResultCode("0");
            ro.setResuleMessage("出现未知异常");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String loginOut(String userOid, String pass, String runCode) {
        ResultObject ro = new ResultObject();
        try {
            if (StringUtils.isEmpty(userOid)) {
                ro.setResultCode("0");
                ro.setResuleMessage("用户主键不能为空");
            } else if (StringUtils.isEmpty(pass)) {
                ro.setResultCode("0");
                ro.setResuleMessage("密码不能为空");
            } else if (StringUtils.isEmpty(pass)) {
                ro.setResultCode("0");
                ro.setResuleMessage("设备编号不能为空");
            } else {
                ApiResultSet<SysLogin> loginResult = sysLoginFeginService.getSysLoginByUserOid(userOid);
                //根据设备编号查找设备信息
                ApiResultSet<PbpjManage> rest=pbpjManageFeginService.getPbpjManageByRunCode(runCode);
                PbpjManage pr=null;
                if(rest.getData()!=null){
                    pr=rest.getData();
                }
                if (loginResult.getData() == null) {
                    ro.setResultCode("0");
                    ro.setResuleMessage("用户对象不存在");
                } else if (!CommonUtil.md5(pass).equals(loginResult.getData().getPassword())) {
                    ro.setResultCode("0");
                    ro.setResuleMessage("密码不正确");
                } else if (pr == null) {
                    ro.setResultCode("0");
                    ro.setResuleMessage("对不起！该设备不存在");
                } else {
                    pr.setStatus(0);//登录状态改为未登录0
                    pr.setUserCode("");
                    pr.setUserName("");
                    pr.setDateTime(null);
                    pbpjManageFeginService.updatePbpjManage(pr);
                    ro.setResultCode("1");
                    ro.setResuleMessage("注销成功");
                }
            }
        } catch (Exception e) {
            ro.setResultCode("0");
            ro.setResuleMessage("出现未知异常");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getServiceList(String userOid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(10);
        List<SxServiceRegistrar> listSxService = null;
        try {
            if (StringUtils.isEmpty(userOid)) {
                resultMap.put("state", "0");
                resultMap.put("msg", "请求参数不能为空");
                return JSON.toJSONString(resultMap);
            }
            //根据主键查询用户信息
            ApiResultSet<SysUser> loginResult = sysUserFeginService.getSysUserByUserOid(userOid);
            Map<String, SxService> allServiceMap = new HashMap<String, SxService>(10);
            if (loginResult.getData() != null) {
                listSxService = sxServiceRegistrarManager.listSxServiceByUserOid(userOid);
            }
            if(listSxService.size() > 0){
                for (SxServiceRegistrar sxServiceRegistrar : listSxService) {
                    SxService sx=new SxService();
                    sx.setServiceOid(sxServiceRegistrar.getServiceOid());
                    sx.setServiceName(sxServiceRegistrar.getServiceName());
                    allServiceMap.put(sxServiceRegistrar.getServiceOid(), sx);
                }
            }

            //查询用户所在的区划的辖区
            ApiResultSet<List<SysSiteDistrictRelation>> list=sysSiteDistrictRelationFeginService.siteDistrictRelListByDistOid(loginResult.getData().getDistrictOid());
            List<SysSiteDistrictRelation> dbSysSiteDistrictRelations=null;
            if(list!=null&&list.getData()!=null&&list.getData().size()>0){
                dbSysSiteDistrictRelations=list.getData();
            }

            ServiceSiteAuthorize serviceSiteAuthorize = new ServiceSiteAuthorize();
            if(null != dbSysSiteDistrictRelations && dbSysSiteDistrictRelations.size()>0){
                //查询辖区下面所有的授权事项
                for(SysSiteDistrictRelation relate:dbSysSiteDistrictRelations){
                    serviceSiteAuthorize.setSiteOid(relate.getSysAreaSiteOid());
                    List<ServiceSiteAuthorize> dbServiceSiteAuthorizes =serviceSiteAuthorizeManager.selectDbServiceSiteAuthorizList(serviceSiteAuthorize);
                    if(dbServiceSiteAuthorizes!=null&&dbServiceSiteAuthorizes.size()>0){
                        for (ServiceSiteAuthorize authors:dbServiceSiteAuthorizes){
                            SxService sx=new SxService();
                            sx.setServiceOid(authors.getServiceOid());
                            sx.setServiceName(authors.getServiceName());
                            allServiceMap.put(authors.getServiceOid(),sx);
                        }
                    }
                }
            }
            List<Map<String, Object>> sxServiceMapList = new ArrayList<Map<String, Object>>();
            if(allServiceMap!=null){
                for(SxService ss:allServiceMap.values()){
                    Map<String, Object> sxServiceMap = new HashMap<String, Object>(10);
                    sxServiceMap.put("serviceOid", ss.getServiceOid());
                    sxServiceMap.put("serviceName", ss.getServiceName());
                    sxServiceMapList.add(sxServiceMap);
                }
            }
            if (sxServiceMapList.size() > 0) {
                resultMap.put("listSxServiceAll", sxServiceMapList);
                resultMap.put("state", 1);
            } else {
                resultMap.put("state", "0");
                resultMap.put("msg", "没有获取到目录信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.clear();
            resultMap.put("state", "-1");
            resultMap.put("msg", "获取实施清单集合信息异常：" + e.getMessage());
            return JSON.toJSONString(resultMap);
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(resultMap);
    }

    @Override
    public String getPbInformationList(String distOid) {
        ResultObject ro = new ResultObject();
        try {
            if (StringUtils.isEmpty(distOid)) {
                ro.setResultCode("0");
                ro.setResuleMessage("区划id不能为空");
            } else {
               ApiResultSet<ShowInformation> reInfo=showInformationFeginService.getShowInformationByDistOid(distOid);
                ShowInformation showInformation=null;
               if(reInfo.getData()!=null){
                   showInformation=reInfo.getData();
                   if (StrUtil.isNotEmpty(showInformation.getTheme())) {
                       if(showInformation.getType()==2){//附件
                         ApiResultSet<SysAtta> sysAttaResult = sysAttaFeginService.getSysAttaByAttaOid(showInformation.getAttaOid());
                         if(sysAttaResult!=null){
                             showInformation.setAttaOid(sysAttaResult.getData().getFastdfsNginxUrl());
                         }
                       }
                       ro.setResultCode("1");
                       ro.setResuleMessage(showInformation);
                   } else {
                       ro.setResultCode("0");
                       ro.setResuleMessage("展示信息集合不存在");
                   }
               }

            }
        } catch (Exception e) {
            ro.setResultCode("0");
            ro.setResuleMessage("对不起！该用户没有启用平板评价");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }

    @Override
    public String getApkVersion(String apkType) {
        String json = "";
        try {
            String androidType = "1";
            String iosType = "2";
            String apkPath = "";
            //移动审批接口
            String rootFilePath = "";
            String realPath = rootFilePath + "/app/pbpj";
            if (androidType.equals(apkType)) {
                apkPath = realPath + "/android/";
            } else if (iosType.equals(apkType)) {
                apkPath = realPath + "/ios/";
            } else {
                json = "{\"state\":0,\"msg\":\"版本类型值不正确\"}";
                return json;
            }

            File apkFile = new File(apkPath + "version_android.xml");
            if (apkFile.exists()) {
                json = "{\"state\":1,\"msg\":\"\", \"apkVersionPath\":\"" + "/app/pbpj/android/version_android.xml" + "\"}";
            } else {
                if (androidType.equals(apkType)) {
                    json = "{\"state\":0,\"msg\":\"不存在Android 版本信息\"}";
                } else {
                    json = "{\"state\":0,\"msg\":\"不存在IOS 版本信息\"}";
                }
            }
        } catch (Exception e) {
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(json);
    }

    @Override
    public String getServiceBaseInfo(String serviceOid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(12);
        try {
            ApiResultSet<SxService> serviceResult = sxServiceFeginService.getSxServiceByOid(serviceOid);
            if (null != serviceResult && serviceResult.getData() != null) {
                //填充收费信息内容
                Map<Object, Object> sfMap = new HashMap<Object, Object>(6);
                // 填充基本信息内容
                Map<Object, Object> baseInfo = new HashMap<Object, Object>(70);
                //是否收费
                sfMap.put("freeflag", serviceResult.getData().getChargeFlag());
                //区划名称
                if (StringUtils.isNotEmpty(serviceResult.getData().getDistrictOid())) {
                    ApiResultSet<SysDistrict> districtResult = sysDistrictFeginService.getSysDistrictByDistrictOid(serviceResult.getData().getDistrictOid());
                    if (null != districtResult && districtResult.getData() != null) {
                        baseInfo.put("areaName", districtResult.getData().getName());
                    }
                }
                //机构名称
                if (StringUtils.isNotEmpty(serviceResult.getData().getOrganOid())) {
                    ApiResultSet<SysOrgan> organResult = sysOrganFeginService.getSysOrganByOrganOid(serviceResult.getData().getOrganOid());
                    if (null != organResult && organResult.getData() != null) {
                        baseInfo.put("orgName", organResult.getData().getName());
                    }
                }
                //事项类型
                if (StringUtils.isNotEmpty(serviceResult.getData().getServiceTypeOid())) {
                    ApiResultSet<SxServiceType> sxServiceTypeResult = sxServiceTypeFeginService.getSxServiceTypeByOid(serviceResult.getData().getServiceTypeOid());
                    if (null != sxServiceTypeResult && sxServiceTypeResult.getData() != null) {
                        baseInfo.put("serviceType", sxServiceTypeResult.getData().getServiceTypeName());
                    }
                }
                //事项基本编码
                baseInfo.put("serviceCode", serviceResult.getData().getBasicCode());
                //事项名称
                baseInfo.put("serviceName", serviceResult.getData().getServiceName());
                //行使层次
                baseInfo.put("blcc", serviceResult.getData().getLevelName());
                //实施主体性质
                if (StringUtils.isNotEmpty(serviceResult.getData().getImplementOrganProperty())) {
                    ApiResultSet<SysDict> dictResult = sysDictFeignService.getSysDictByDictOid(serviceResult.getData().getImplementOrganProperty());
                    if (null != dictResult && dictResult.getData() != null) {
                        baseInfo.put("ssztxz", dictResult.getData().getName());
                    }
                }
                //实施编码
                baseInfo.put("ssbm", serviceResult.getData().getImplementCode());
                //遍历循环获取服务对象信息
                String mulObjectValue = "";
                if (StringUtils.isNotEmpty(serviceResult.getData().getServiceObject())) {
                    String[] objs = serviceResult.getData().getServiceObject().split(",");
                    for (String obj : objs) {
                        obj = "FWDX_" + obj;
                        ApiResultSet<SysDict> codeResult = sysDictFeignService.getSysDictByCode(obj);
                        if (null != codeResult && codeResult.getData() != null) {
                            mulObjectValue += codeResult.getData().getName() + " ";
                        }
                    }
                    //服务对象
                    baseInfo.put("fwdx", mulObjectValue);
                }
                //业务项名称
                baseInfo.put("ywxmc", serviceResult.getData().getServiceName());
                //办件类型
                if (serviceResult.getData().getCaseType() != null) {
                    String caseType = "BJLX_" + serviceResult.getData().getCaseType();
                    ApiResultSet<SysDict> typeResult = sysDictFeignService.getSysDictByCode(caseType);
                    if (null != typeResult && typeResult.getData() != null) {
                        baseInfo.put("caseType", typeResult.getData().getName());
                    }
                }
                //主题分类
                String subjectNameValue = "";
                if (StringUtils.isNotEmpty(serviceResult.getData().getSubjectClassification())) {
                    String[] oids = serviceResult.getData().getSubjectClassification().split(",");
                    for (String oid : oids) {
                        ApiResultSet<SysDict> codeResult = sysDictFeignService.getSysDictByDictOid(oid);
                        if (null != codeResult && codeResult.getData() != null) {
                            subjectNameValue += codeResult.getData().getName() + ",";
                        }
                    }
                    baseInfo.put("ztfl", subjectNameValue);
                }
                // 生命周期分类
                String lifeCycleNameValue = "";
                if (StringUtils.isNotEmpty(serviceResult.getData().getLifeCycleClassification())) {
                    String[] oids = serviceResult.getData().getLifeCycleClassification().split(",");
                    for (String oid : oids) {
                        ApiResultSet<SysDict> codeResult = sysDictFeignService.getSysDictByDictOid(oid);
                        if (null != codeResult && codeResult.getData() != null) {
                            lifeCycleNameValue += codeResult.getData().getName() + ",";
                        }
                    }
                    baseInfo.put("smzqfl", lifeCycleNameValue);
                }
                // 办事群体分类
                String serviceGroupNameValue = "";
                if (StringUtils.isNotEmpty(serviceResult.getData().getServiceGroupClassification())) {
                    String[] oids = serviceResult.getData().getServiceGroupClassification().split(",");
                    for (String oid : oids) {
                        ApiResultSet<SysDict> codeResult = sysDictFeignService.getSysDictByDictOid(oid);
                        if (null != codeResult && codeResult.getData() != null) {
                            serviceGroupNameValue += codeResult.getData().getName() + ",";
                        }
                    }
                    baseInfo.put("bsqtfl", serviceGroupNameValue);
                }
                //是否联合办理
                if (serviceResult.getData().getUnionOrganFlag() != null) {
                    String unionFlag = "LHBL_" + serviceResult.getData().getUnionOrganFlag();
                    ApiResultSet<SysDict> unionResult = sysDictFeignService.getSysDictByCode(unionFlag);
                    if (null != unionResult && unionResult.getData() != null) {
                        baseInfo.put("sflhbl", unionResult.getData().getName());
                    }
                }
                // 联合办理机构
                baseInfo.put("unionOrgan", serviceResult.getData().getUnionOrgan());
                //办理形式
                if (serviceResult.getData().getHandleForm() != null) {
                    String handleForm = "BLXS_" + serviceResult.getData().getHandleForm();
                    ApiResultSet<SysDict> handleFormResult = sysDictFeignService.getSysDictByCode(handleForm);
                    if (null != handleFormResult && handleFormResult.getData() != null) {
                        baseInfo.put("handleForm", handleFormResult.getData().getName());
                    }
                }
                //办理深度
                String handleDepthValue = "";
                if (StringUtils.isNotEmpty(serviceResult.getData().getHandleDepth())) {
                    String[] oids = serviceResult.getData().getHandleDepth().split(",");
                    for (String oid : oids) {
                        ApiResultSet<SysDict> depthResult = sysDictFeignService.getSysDictByDictOid(oid);
                        if (null != depthResult && depthResult.getData() != null) {
                            handleDepthValue += depthResult.getData().getName() + ",";
                        }
                    }
                    baseInfo.put("handleDepth", handleDepthValue);
                }
                //公开内容
                baseInfo.put("openContent", serviceResult.getData().getOpenContent());
                //公开文书内容
                baseInfo.put("openDocumentContent", serviceResult.getData().getOpenDocumentContent());
                //办理结果送达方式
                if (!StringUtils.isEmpty(serviceResult.getData().getResultDeliveryWay())) {
                    String way = "SDFS_" + serviceResult.getData().getResultDeliveryWay();
                    ApiResultSet<SysDict> wayResult = sysDictFeignService.getSysDictByCode(way);
                    if (null != wayResult && wayResult.getData() != null) {
                        baseInfo.put("resultDeliveryWay", wayResult.getData());
                    }
                }
                //公开方式
                if (serviceResult.getData().getOpenWay() != null) {
                    String openWay = "GKFS_" + serviceResult.getData().getOpenWay();
                    ApiResultSet<SysDict> openWayResult = sysDictFeignService.getSysDictByCode(openWay);
                    if (openWayResult.getData() != null) {
                        baseInfo.put("openWay", openWayResult.getData().getName());
                    }
                }
                //公开渠道
                if (serviceResult.getData().getOpenChannel() != null) {
                    String openChannel = "GKQD_" + serviceResult.getData().getOpenChannel();
                    ApiResultSet<SysDict> openChannelResult = sysDictFeignService.getSysDictByCode(openChannel);
                    if (null != openChannelResult && openChannelResult.getData() != null) {
                        baseInfo.put("openChannel", openChannelResult.getData().getName());
                    }
                }
                //是否支持网上支付
                if (serviceResult.getData().getOnlinePayFlag() != null) {
                    String onlinePayFlag = "WSZF_" + serviceResult.getData().getOnlinePayFlag();
                    ApiResultSet<SysDict> onlinePayFlagResult = sysDictFeignService.getSysDictByCode(onlinePayFlag);
                    if (null != onlinePayFlagResult && onlinePayFlagResult.getData() != null) {
                        baseInfo.put("onlinePayFlag", onlinePayFlagResult.getData().getName());
                    }
                }
                //行政管辖地
                if (serviceResult.getData().getAdminJurisdiction() != null) {
                    String adminJurisdiction = "XZGXD_" + serviceResult.getData().getAdminJurisdiction();
                    ApiResultSet<SysDict> adminJurisdictionResult = sysDictFeignService.getSysDictByCode(adminJurisdiction);
                    if (null != adminJurisdictionResult && adminJurisdictionResult.getData() != null) {
                        baseInfo.put("adminJurisdiction", adminJurisdictionResult.getData().getName());
                    }
                }
                //是否支持电话预约
                if (serviceResult.getData().getAppointmentFlag() != null) {
                    String appointmentFlag = "WLKD_" + serviceResult.getData().getAppointmentFlag();
                    ApiResultSet<SysDict> appointmentFlagResult = sysDictFeignService.getSysDictByCode(appointmentFlag);
                    if (null != appointmentFlagResult && appointmentFlagResult.getData() != null) {
                        baseInfo.put("appointmentFlag", appointmentFlagResult.getData().getName());
                    }
                }
                //是否支持物流快递
                if (serviceResult.getData().getExpressFlag() != null) {
                    String expressFlag = "WLKD_" + serviceResult.getData().getExpressFlag();
                    ApiResultSet<SysDict> expressFlagResult = sysDictFeignService.getSysDictByCode(expressFlag);
                    if (null != expressFlagResult && expressFlagResult.getData() != null) {
                        baseInfo.put("expressFlag", expressFlagResult.getData().getName());
                    }
                }
                //监督电话
                baseInfo.put("superviseTel", serviceResult.getData().getTsDhText());
                //咨询电话
                baseInfo.put("consultTel", serviceResult.getData().getZxDhText());
                ApiResultSet<SxServiceExtend> extendResult = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(serviceResult.getData().getServiceOid());
                if (null != extendResult && extendResult.getData() != null) {
                    //收费标准
                    sfMap.put("feelevel", extendResult.getData().getChargeStandard());
                    //收费依据
                    sfMap.put("feeaccord", extendResult.getData().getChargeAccord());
                    //设定依据
                    baseInfo.put("lawaccord", extendResult.getData().getSetAccord());
                    //收件凭证送达渠道
                    baseInfo.put("recipientVoucherChannel", extendResult.getData().getRecipientVoucherChannel());
                    //收件凭证送达相关要求
                    baseInfo.put("recipientVoucherRequirement", extendResult.getData().getRecipientVoucherRequirement());
                    //运行系统
                    if (extendResult.getData().getRunSystem() != null) {
                        String runSystem = "YXXT_" + serviceResult.getData().getExpressFlag();
                        ApiResultSet<SysDict> runSystemResult = sysDictFeignService.getSysDictByCode(runSystem);
                        if (null != runSystemResult && runSystemResult.getData() != null) {
                            baseInfo.put("runSystem", runSystemResult.getData().getName());
                        }
                    }
                    //是否有数量限制
                    if (extendResult.getData().getNumberLimitType() != null) {
                        String numberLimitType = "SLXZ_" + extendResult.getData().getNumberLimitType();
                        ApiResultSet<SysDict> numberLimitTypeResult = sysDictFeignService.getSysDictByCode(numberLimitType);
                        if (null != numberLimitTypeResult && numberLimitTypeResult.getData() != null) {
                            baseInfo.put("numberLimitType", numberLimitTypeResult.getData().getName());
                        }
                    }
                    //限制数量
                    baseInfo.put("numberLimit", extendResult.getData().getNumberLimit());
                    if (StringUtils.isNotEmpty(extendResult.getData().getHandleScope())) {
                        ApiResultSet<SysDict> sysDictResult = sysDictFeignService.getSysDictByDictOid(extendResult.getData().getHandleScope());
                        if (null != sysDictResult && sysDictResult.getData() != null) {
                            String tbfw = sysDictResult.getData().getName();
                            //通办范围
                            baseInfo.put("tbfw", tbfw);
                        }
                    } else {
                        //通办范围
                        baseInfo.put("tbfw", "");
                    }
                    //结果名称
                    baseInfo.put("resultName", extendResult.getData().getResultName());
                    //结果样本类型
                    if (extendResult.getData().getResultSampleType() != null) {
                        String resultSampleType = "JGYB_" + extendResult.getData().getResultSampleType();
                        ApiResultSet<SysDict> resultSampleTypeResult = sysDictFeignService.getSysDictByCode(resultSampleType);
                        if (null != resultSampleTypeResult && resultSampleTypeResult.getData() != null) {
                            baseInfo.put("resultSampleType", resultSampleTypeResult.getData().getName());
                        }
                    }
                    //证照目录类型
                    if (extendResult.getData().getDirectoryType() != null) {
                        String directoryType = "SLXZ_" + extendResult.getData().getDirectoryType();
                        ApiResultSet<SysDict> directoryTypeResult = sysDictFeignService.getSysDictByCode(directoryType);
                        if (null != directoryTypeResult && directoryTypeResult.getData() != null) {
                            baseInfo.put("directoryType", directoryTypeResult.getData().getName());
                        }
                    }
                    if (StringUtils.isNotEmpty(extendResult.getData().getResultSampleAddr())) {
                        ApiResultSet<SxSysAtta> attaResult = sxServiceAttaFeignService.getSxSysAttaByOId(extendResult.getData().getResultSampleAddr());
                        if(null != attaResult && null != attaResult.getData()) {
                            //结果样本
                            baseInfo.put("jgyb", attaResult.getData().getOriginName());
                        }
                    } else {
                        //结果样本
                        baseInfo.put("jgyb", "");
                    }
                    //结果样本附件ID
                    baseInfo.put("jgybAttaId", extendResult.getData().getResultSampleAddr());
                    //权限划分
                    baseInfo.put("powerModular", extendResult.getData().getPowerModular());
                    //行使内容
                    baseInfo.put("exerciseContent", extendResult.getData().getExerciseContent());
                    //承诺期限
                    baseInfo.put("promiseLimit", extendResult.getData().getPromiseLimit());
                    //承诺时限类型
                    if (extendResult.getData().getPromiseLimitType() != null) {
                        String promiseLimitType = extendResult.getData().getPromiseLimitType();
                        ApiResultSet<SysDict> promiseLimitTypeResult = sysDictFeignService.getSysDictByCode(promiseLimitType);
                        if (null != promiseLimitTypeResult && promiseLimitTypeResult.getData() != null) {
                            baseInfo.put("promiseLimitType", promiseLimitTypeResult.getData().getName());
                        }
                    }
                    //法定期限
                    baseInfo.put("legalLimit", extendResult.getData().getLegalLimit());
                    //法定时限类型
                    if (extendResult.getData().getLegalLimitType() != null) {
                        String legalLimitType = extendResult.getData().getLegalLimitType();
                        ApiResultSet<SysDict> legalLimitTypeResult = sysDictFeignService.getSysDictByCode(legalLimitType);
                        if (null != legalLimitTypeResult && legalLimitTypeResult.getData() != null) {
                            baseInfo.put("legalLimitType", legalLimitTypeResult.getData().getName());
                        }
                    }
                    //审查标准
                    baseInfo.put("censorStandard", extendResult.getData().getCensorStandard());
                    //处理办理地点
                    List<Map<String, String>> locationList = new ArrayList<Map<String, String>>();
                    ApiResultSet<List<SxServiceLocation>> locationResult = sxServiceLocationFeginService.getSxServicLocationByExtendOid(extendResult.getData().getExtendOid());
                    if (null != locationResult && locationResult.getData() != null && locationResult.getData().size() > 0) {
                        for (SxServiceLocation serviceLocation : locationResult.getData()) {
                            Map<String, String> sxServiceLocationMap = new HashMap<String, String>(5);
                            //办理地点
                            sxServiceLocationMap.put("locationName", serviceLocation.getLocationName());
                            //办理地址
                            sxServiceLocationMap.put("locationAddr", serviceLocation.getLocationAddr());
                            locationList.add(sxServiceLocationMap);
                        }
                    }
                    //办理地点
                    baseInfo.put("locationList", locationList);
                }
                //基本信息内容 map
                resultMap.put("baseInfo", baseInfo);
                //收费信息内容 map
                resultMap.put("sfMap", sfMap);
                //材料清单列表
                List<Map<String, String>> materialList = new ArrayList<Map<String, String>>();
                ApiResultSet<List<SxServiceMaterial>> listResult = sxServiceMaterialFeginService.getSxServiceMaterialListByServiceOid(serviceResult.getData().getServiceOid());
                if (null != listResult && listResult.getData() != null && listResult.getData().size() > 0) {
                    for (SxServiceMaterial material : listResult.getData()) {
                        Map<String, String> materialMap = new HashMap<String, String>(10);
                        //材料名称
                        materialMap.put("materialName", material.getMaterialName());
                        //材料类型
                        if (material.getMaterialType() != null) {
                            String materialType = "CLLX_" + material.getMaterialType();
                            ApiResultSet<SysDict> materialTypeResult = sysDictFeignService.getSysDictByCode(materialType);
                            if (null != materialTypeResult && materialTypeResult.getData() != null) {
                                baseInfo.put("materialType", materialTypeResult.getData().getName());
                            }
                        }
                        //材料样本名称
                        materialMap.put("materialSampleName", material.getMaterialSampleName());
                        //电子表单名称
                        materialMap.put("electronicFormName", material.getElectronicFormName());
                        //来源渠道
                        if (material.getMaterialSource() != null) {
                            String materialSource = "LYQD_" + material.getMaterialSource();
                            ApiResultSet<SysDict> materialSourceResult = sysDictFeignService.getSysDictByCode(materialSource);
                            if (null != materialSourceResult && materialSourceResult.getData() != null) {
                                baseInfo.put("materialSource", materialSourceResult.getData().getName());
                            }
                        }
                        //纸质材料份数
                        materialMap.put("paperNumber", material.getPaperNumber().toString());
                        materialList.add(materialMap);
                    }
                }
                //材料信息
                resultMap.put("materialList", materialList);
                // 常见问题列表
                List<Map<String, String>> questionList = new ArrayList<Map<String, String>>();
                ApiResultSet<List<SxServiceQuestion>> listQuestionResult = sxServiceQuestionFeginService.getSxServiceQuestionListByServiceOid(serviceResult.getData().getServiceOid());
                if (null != listQuestionResult && listQuestionResult.getData() != null && listQuestionResult.getData().size() > 0) {
                    for (SxServiceQuestion question : listQuestionResult.getData()) {
                        Map<String, String> questionMap = new HashMap<String, String>(7);
                        //主题词
                        questionMap.put("keyWord", question.getKeyWord());
                        //问题标题
                        questionMap.put("title", question.getTitle());
                        //问题描述
                        questionMap.put("description", question.getDescription());
                        //问题解答
                        questionMap.put("answer", question.getAnswer());
                        questionList.add(questionMap);
                    }
                }
                //常见问题信息
                resultMap.put("questionList", questionList);
                //办理环节列表
                List<Map<String, String>> serviceLinkList = new ArrayList<Map<String, String>>();
                ApiResultSet<List<SxServiceLink>> serviceLinkResult = sxServiceLinkFeginService.getSxServiceLinkListByServiceOid(serviceResult.getData().getServiceOid());
                if (null != serviceLinkResult && serviceLinkResult.getData() != null && serviceLinkResult.getData().size() > 0) {
                    for (SxServiceLink link : serviceLinkResult.getData()) {
                        if(null == link) {
                            continue;
                        }
                        Map<String, String> sxServiceLinkMap = new HashMap<String, String>(6);
                        //环节名称
                        sxServiceLinkMap.put("linkName", link.getLinkName());
                        //办理时限
                        sxServiceLinkMap.put("handleTime", link.getHandleTime().toString());
                        //办理时限类型
                        if (extendResult.getData().getNumberLimitType() != null) {
                            String numberLimitType = "SLXZ_" + extendResult.getData().getNumberLimitType();
                            ApiResultSet<SysDict> numberLimitTypeResult = sysDictFeignService.getSysDictByCode(numberLimitType);
                            if (null !=numberLimitTypeResult && numberLimitTypeResult.getData() != null) {
                                baseInfo.put("expressFlag", numberLimitTypeResult.getData().getName());
                            }
                        }
                        if (link.getTimeUnit() != null) {
                            ApiResultSet<SysDict> timeUnitResult = sysDictFeignService.getSysDictByCode(link.getTimeUnit().toString());
                            if (null != timeUnitResult && timeUnitResult.getData() != null) {
                                sxServiceLinkMap.put("timeUnit", timeUnitResult.getData().getName());
                            }
                        }
                        serviceLinkList.add(sxServiceLinkMap);
                    }
                }
                //办理环节信息
                resultMap.put("serviceLinkList", serviceLinkList);
                //受理条件
                List<Map<String, String>> conditionList = new ArrayList<Map<String, String>>();
               /* ApiResultSet<List<SxAcceptCondition>> conditionResult = sxAcceptConditionFeginService.getSxAcceptConditionListByServiceOid(serviceResult.getData().getServiceOid());
                if (conditionResult.getData() != null && conditionResult.getData().size() > 0) {
                    for (SxAcceptCondition sxAcceptCondition : conditionResult.getData()) {
                        Map<String, String> sxServiceConditionMap = new HashMap<String, String>(5);
                        //受理条件内容
                        sxServiceConditionMap.put("condition", sxAcceptCondition.getConditionText());
                        conditionList.add(sxServiceConditionMap);
                    }
                }*/
                //受理条件
                resultMap.put("conditionList", conditionList);

                resultMap.put("state", "1");
                resultMap.put("msg", "查询成功");
            } else {
                resultMap.put("state", "0");
                resultMap.put("msg", "未查询到事项基本信息！");
            }

        } catch (Exception e) {
            resultMap.clear();
            resultMap.put("state", "-1");
            resultMap.put("msg", "获取实施清单基本信息异常：" + e.getMessage());
            return JSON.toJSONString(resultMap);
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(resultMap);
    }

    @Override
    public ApiResultSet<Boolean> checkUserLogin() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", "");
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("PBPJ_URL");
        String uriString = config.getData().getValue() + "/api/user/checkUserLogin.do?userOid=" + CurrentLoginUserHolder.getCurrentLoginUser().getUserOid() + "&message=" + JSON.toJSONString(map);
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uriString)
                .build()
                //  .expand("dodo")
                .encode();
        URI uri = uriComponents.toUri();
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<Boolean>();
        Boolean responseEntity = restTemplate.getForEntity(uri, Boolean.class).getBody();
        apiResultSet.setData(responseEntity);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Boolean> pushPbpjConfirmInfo(Object object) {
        //解析办件信息
        String jsonObject = JSON.toJSONString(object);
        JSONObject obj = JSONObject.parseObject(jsonObject);
        String userOid = obj.get("userOid") == null ? "" : obj.get("userOid").toString();
        String type = obj.get("type") == null ? "" : obj.get("type").toString();// 3确认
        String pushUrl = obj.get("pushUrl") == null ? "" : obj.get("pushUrl").toString();
        String caseInfo = obj.get("content") == null ? "" : obj.get("content").toString();
        //平板评价推送服务地址
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("PBPJ_URL");
        if ("3".equals(type)) {
            pushUrl = pushUrl + "?jsonObject=" + caseInfo;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("url", pushUrl);
        String message = JSON.toJSONString(map);
        String uriString = config.getData().getValue() + "/api/user/sendToUser.do?userOid=" + userOid + "&message=" + message;
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uriString)
                .build()
                //  .expand("dodo")
                .encode();
        URI uri = uriComponents.toUri();
        Boolean responseEntity = restTemplate.getForEntity(uri, Boolean.class).getBody();
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<Boolean>();
        apiResultSet.setData(responseEntity);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Boolean> pushCallBackInfo(String userOid, String returnFlag) {
        boolean flag = false;
        String userId = (String) redisTemplate.opsForValue().get(userOid);
        if (userId != null) {
            redisTemplate.delete(userOid);
        }
        //1 无误
        if ("1".equals(returnFlag)) {
            //1信息确认成功 2 信息评价成功
            redisTemplate.opsForValue().set(userOid, "1");
            flag = true;
        }
        // 0有误
        if ("0".equals(returnFlag)) {
            //0确认信息有误
            redisTemplate.opsForValue().set(userOid, "0");
        }
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<Boolean>();
        apiResultSet.setData(flag);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getCallBackInfo(String userOid) {
        String userId = (String) redisTemplate.opsForValue().get(userOid);
        if (userId != null) {
            redisTemplate.delete(userOid);
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(userId);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Boolean> pushPbpjInfo(String caseNumber) {
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<Boolean>();
        try {
            //解析办件信息
            String type = "10";//10 评价
            String requestData = "{\"caseNum\":\"" + caseNumber + "\",\"backUrl\":\"" + ipConfiguration.getUrl() + pbpjCallBack + "\"}";
            //平板评价推送服务地址
            ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("PBPJ_URL");
            Map<String, Object> map = new HashMap<String, Object>();
            String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
            String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
            String parma = "apiKey=" + pbpjKey + "&systemPwd=" + pbpjSecret + "&requestData=" + encData + "&dataSign=" + dataSign;
            String url = pbpjUrl + pbpjPage + "?" + URLEncoder.encode(parma, "UTF-8");
            map.put("type", type);
            map.put("url", url);
            String message = JSON.toJSONString(map);
            String uriString = config.getData().getValue() + "/api/user/sendToUser.do?userOid=" + userOid + "&message=" + message;
            RestTemplate restTemplate = new RestTemplate();
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(uriString)
                    .build()
                    .encode();
            URI uri = uriComponents.toUri();
            Boolean responseEntity = restTemplate.getForEntity(uri, Boolean.class).getBody();
            apiResultSet.setData(responseEntity);
        }catch (Exception e){
            e.printStackTrace();
        }

        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> pbpjSaveQlCase(String caseOid){
        String body = null;
        try {
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
            if (resultSet.getData() != null) {
                ApiResultSet<QlCaseApplay> resultApplay = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(resultSet.getData().getCaseOid());
                ApiResultSet<SysOrgan> resultOrgan = sysOrganFeginService.getSysOrganByOrganOid(resultSet.getData().getOrganOid());
                ApiResultSet<SxService> resultService = sxServiceFeginService.getSxServiceByOid(resultSet.getData().getServiceOid());
                //解析办件信息
                Map<String, Object> requestDataMap = new HashMap<String, Object>();
                requestDataMap.put("caseNum", resultSet.getData().getCaseNumber());
                requestDataMap.put("serviceName", resultSet.getData().getServiceName());
                if (resultApplay.getData() != null) {
                    ApiResultSet<SysDict> resultDict = sysDictFeignService.getSysDictByDictOid(resultApplay.getData().getCredentialType());
                    requestDataMap.put("applyName", resultApplay.getData().getApplyUserName());
                    requestDataMap.put("applyTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultApplay.getData().getCreateDate()));
                    requestDataMap.put("cardNo", resultApplay.getData().getCredentialNumber());
                    requestDataMap.put("lxrPhone", resultApplay.getData().getApplyUserPhone());
                    if ("0".equals(resultApplay.getData().getApplyUserType())) {//申请对象类型
                        requestDataMap.put("objectType", "1");
                    } else {
                        requestDataMap.put("objectType", "0");
                    }
                    if (resultDict.getData() != null) {//证件名称
                        requestDataMap.put("cardName", resultDict.getData().getName());
                    }
                }
                requestDataMap.put("blStatus", "2");
                if (resultService.getData() != null) {
                    if ("2c287bb66859a90001685a5612f20007".equals(resultSet.getData().getServiceType())) {//公共服务
                        requestDataMap.put("serviceType", "0");
                    } else {
                        requestDataMap.put("serviceType", "1");
                    }
                    requestDataMap.put("serviceCode", resultService.getData().getImplementCode());
                }
                if (resultOrgan.getData() != null) {
                    requestDataMap.put("organCode", resultOrgan.getData().getUniteCode());
                }
                requestDataMap.put("projectName", resultSet.getData().getProjectName());
                String requestData = JSON.toJSONString(requestDataMap);
                String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
                String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
                Map<String, Object> map = new HashMap<String, Object>();
                String uriString = pbpjUrl + pbpjSaveCase;
                map.put("apiKey", pbpjKey);
                map.put("systemPwd", pbpjSecret);
                map.put("requestData", encData);
                map.put("dataSign", dataSign);
                body = HttpRequestUtil.sendPost(uriString, map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> qlCasePbpjCallBack(){
        String userOid = null;
        if(null != CurrentLoginUserHolder.getCurrentLoginUser()) {
            userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        }
        //平板评价推送服务地址
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("PBPJ_URL");
        if(null == config || null == config.getData() || null == config.getData().getValue()) {
            return new ApiResultSet<>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "11");
        String message = JSON.toJSONString(map);
        String uriString = config.getData().getValue() + "/api/user/sendToUser.do?userOid=" + userOid + "&message=" + message;
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uriString)
                .build()
                //  .expand("dodo")
                .encode();
        URI uri = uriComponents.toUri();
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        if(null != restTemplate.getForEntity(uri, String.class)) {
            String responseEntity = restTemplate.getForEntity(uri, String.class).getBody();
            apiResultSet.setData(responseEntity);
        }
        return apiResultSet;
    }


    @Override
    public String getLoginUserImgUrl(String userOid) {
        ResultObject ro = new ResultObject();
        ApiResultSet<SysAtta> sysAttaResult = null;
        String imgUrl = null;
        if (StringUtils.isEmpty(userOid)) {
            ro.setResultCode("0");
            ro.setResuleMessage("用户编号为空！");
        } else {
            sysAttaResult = sysAttaFeginService.getSysAttaByAttaOid(userOid);
            if (sysAttaResult.getData() == null) {
                ro.setResultCode("0");
                ro.setResuleMessage("未查询到用户头像！");
            } else {
                imgUrl = sysAttaResult.getData().getFastdfsNginxUrl();
                ro.setResultCode("1");
                ro.setResuleMessage(imgUrl);
            }
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(ro);
    }
}

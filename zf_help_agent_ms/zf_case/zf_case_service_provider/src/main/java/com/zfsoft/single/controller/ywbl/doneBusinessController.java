package com.zfsoft.single.controller.ywbl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zfsoft.cases.data.*;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.service.*;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.ywbl.QlCaseCorrection;
import com.zfsoft.single.feign.settings.*;
import com.zfsoft.single.manager.ywbl.QlCaseCorrectionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dongxl
 * @create: 2020-11-9
 * @description: 已办业务
 */
@Slf4j
@RestController
@RequestMapping(value = "/doneBusiness")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class doneBusinessController  {

    private final QlCaseMaterialService qlCaseMaterialServiceFeginService;

    private final QlCaseService qlCaseServiceFeginService;

    private final QlCaseApplayService qlCaseApplayServiceFeginService;

    private final QlCaseExtService qlCaseExtServiceFeginService;

    private final QlCaseLinkResultService qlCaseLinkResultServiceFeginService;

    private final SysUserFeginService userFeginService;

    private final SysOrganFeginService organFeginService;

    private final QlCaseCorrectionManager qlCaseCorrectionManager;



    /**
     * 获取办件打印信息
     * dongxl
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/getOneByCaseOid")
    public ApiResultSet getOneByCaseOid(String caseOid) {
        //查询办件信息
        ApiResultSet<QlCase> qlcaseResult=qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        Map<String,Object> map=new HashMap<String,Object>();
        //受理人
        ApiResultSet<SysUser> user = userFeginService.getSysUserByUserOid(qlcaseResult.getData().getCreateUserOid());
        if (user != null) {
            map.put("slPerson", user.getData().getName());
        }
        if(qlcaseResult.getData()!=null){
           QlCase qlcase= qlcaseResult.getData();
            map.put("qlCase",qlcase);
            map.put("caseNumber",qlcase.getCaseNumber());
            //申请人信息
            ApiResultSet<QlCaseApplay> apply=qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(caseOid);
            if(apply!=null&&apply.getData()!=null){
                map.put("applyUserName",apply.getData().getApplyUserName());
                map.put("applyUserPhone",apply.getData().getApplyUserPhone()==null?apply.getData().getApplyUserTel():apply.getData().getApplyUserPhone());
                map.put("specificLocation",apply.getData().getSpecificLocation());

            }
            //送达信息
            ApiResultSet<QlCaseExt> caseExt=qlCaseExtServiceFeginService.queryQlCaseExtByCaseOid(caseOid);
            if(caseExt!=null&&caseExt.getData()!=null){
                map.put("deliveryWay",caseExt.getData().getResultDeliveryWay());
            }
            //材料信息
           ApiResultSet<List<QlCaseMaterial>> materialResutl= qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
           if(materialResutl!=null&&materialResutl.getData()!=null){
               List<QlCaseMaterial> materialList=materialResutl.getData();
               String str= Optional.ofNullable(materialList).orElseGet(Lists::newArrayList).stream().map(QlCaseMaterial::getMaterialName).collect(Collectors.joining(";"));
               map.put("materialNames",str);
           }
        }
        return new ApiResultSet(map);
    }

    /**
     * 获取办件打印信息--已办业务
     * chenjm
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/getOneByCaseOidForYbyw")
    public ApiResultSet getOneByCaseOidForYbyw(String caseOid) {
        //查询办件信息
        ApiResultSet<QlCase> qlcaseResult=qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        Map<String,Object> map=new HashMap<String,Object>();
        //受理人
        ApiResultSet<SysUser> user = userFeginService.getSysUserByUserOid(qlcaseResult.getData().getCreateUserOid());
        if (user != null) {
            map.put("slPerson", user.getData().getName());
            map.put("slInternet",user.getData().getMobile());
        }
        if(qlcaseResult.getData()!=null){
            QlCase qlcase= qlcaseResult.getData();
            qlcase.setOrganOid(organFeginService.getSysOrganByOrganOid(qlcase.getOrganOid()).getData().getName());
            map.put("qlCase",qlcase);
            map.put("caseNumber",qlcase.getCaseNumber());
            //申请人信息
            ApiResultSet<QlCaseApplay> apply=qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(caseOid);
            if(apply!=null&&apply.getData()!=null){
                map.put("applyUserName",apply.getData().getApplyUserName());
                map.put("applyUserPhone",apply.getData().getApplyUserPhone()==null?apply.getData().getApplyUserTel():apply.getData().getApplyUserPhone());
                map.put("specificLocation",apply.getData().getSpecificLocation());
                map.put("applyUserType",apply.getData().getApplyUserType());

            }
            //送达信息
            ApiResultSet<QlCaseExt> caseExt=qlCaseExtServiceFeginService.queryQlCaseExtByCaseOid(caseOid);
            if(caseExt!=null&&caseExt.getData()!=null){
                map.put("deliveryWay",caseExt.getData().getResultDeliveryWay());
            }
            //材料信息
            ApiResultSet<List<QlCaseMaterial>> materialResutl= qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            if(materialResutl!=null&&materialResutl.getData()!=null){
                List<QlCaseMaterial> materialList=materialResutl.getData();
                String str= Optional.ofNullable(materialList).orElseGet(Lists::newArrayList).stream().map(QlCaseMaterial::getMaterialName).collect(Collectors.joining(";"));
                map.put("materialNames",str);
            }
        }
        return new ApiResultSet(map);
    }



    /**
     * 获取当前登录人所在机构
     * @return
     */
    @PostMapping(value = "/getOrganCurr")
    public ApiResultSet getOrganCurr() {
        return new ApiResultSet(CurrentLoginUserHolder.getCurrentLoginUser().getOrganOid());
    }

    /**
     * 保存办件手动办结信息
     * dongxl
     * @param qlCaseLinkResult
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    public ApiResultSet saveOrUpdate(@RequestBody @Validated QlCaseLinkResult qlCaseLinkResult) {
        log.info("保存办件手动办结信息：{}", JSON.toJSONString(qlCaseLinkResult));
        qlCaseLinkResult.setPersonOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        qlCaseLinkResult.setLinkCode("bj");//暂时先塞
        qlCaseLinkResult.setLinkName("办结");
        //查询当前登录人名称，因为直接取会出现乱码问题
        ApiResultSet<SysUser> user = userFeginService.getSysUserByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        if (user != null) {
            qlCaseLinkResult.setPersonName(user.getData().getName());
        }
        qlCaseLinkResult.setFinalDate(new Date());
        ApiResultSet<Map<String, String>> result=qlCaseLinkResultServiceFeginService.saveQlCaseLinkResult(qlCaseLinkResult);
        if(result.getCode()==200){
            if(qlCaseLinkResult.getFinalStatus()==44){
                this.qlCaseServiceFeginService.yuShenQlCase(qlCaseLinkResult.getCaseOid(),3);
                //补齐补正终止
                this.qlCaseCorrectionManager.saveBjCorrection(qlCaseLinkResult.getCaseOid());
            }else{
                this.qlCaseServiceFeginService.yuShenQlCase(qlCaseLinkResult.getCaseOid(),3);
            }

            //更新办件状态
            qlCaseServiceFeginService.updateQlCase(qlCaseLinkResult.getCaseOid(),3);
        }
        return new ApiResultSet(qlCaseLinkResult);
    }

    /**
     * 验证办件是否存在容缺后补材料或者补齐不正材料没有补全
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/checkedCase")
    public ApiResultSet checkedCase(String caseOid) {
        //验证是否存在容缺后补材料没补
        ApiResultSet<List<QlCaseMaterial>> rqhbList=qlCaseMaterialServiceFeginService.queryRqhbMaterialByCaseOid(caseOid);
        String str="";
        if(rqhbList.getData()!=null&&rqhbList.getData().size()>0){
            str="存在容缺后补材料未提交，无法办结！";
        }
        //判断是否存在补齐补正材料没有进行补正
        QlCaseCorrection qlCorrection=qlCaseCorrectionManager.getQlCorrectByCaseOid(caseOid);
        if(qlCorrection!=null){
            str="存在补齐补正材料未提交，无法办结！";
        }
        return new ApiResultSet(str);
    }
}

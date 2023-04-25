package com.zfsoft.cases.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.data.CaseReturn;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.manager.CaseReturnManager;
import com.zfsoft.cases.manager.QlCaseManager;
import com.zfsoft.cases.service.CaseReturnService;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.SendSmsUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @（#）: CaseReturnController
 * @description: 办件退件实现类
 * @author: liangss
 * @date: 2021/01/15
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class CaseReturnController implements CaseReturnService {

    @Resource
    private CaseReturnManager caseReturnManager;
    @Resource
    private QlCaseManager qlCaseManager;
    @Resource
    private SysUserFeginService sysUserFeginService;

    @Override
    public ApiResultSet<PageResult<QlCase>> queryCaseReturnList(String caseNumber, String organOid, String applyUserName,

                                                                String returnStatus, String informStatus,
                                                                Integer caseStatus, String startDate, String endDate,
                                                                Integer sourceApp, String serviceOids, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        QlCaseVo doneCaseVo = new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setOrganOid(organOid);
        doneCaseVo.setApplyUserName(applyUserName);
        doneCaseVo.setReturnStatus(returnStatus);
        doneCaseVo.setInformStatus(informStatus);
        doneCaseVo.setCaseStatus(caseStatus);
        if(startDate !=null){
            doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
        }
        if(endDate !=null){
            doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
        }
        doneCaseVo.setSourceApp(sourceApp);
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(StringUtil.isNotEmpty(serviceOids)){
                doneCaseVo.setServiceOids(Arrays.asList(serviceOids.split(",").clone()));
            }else{
                return apiResultSet;
            }
        }

        PageResult<QlCase> pageResult = qlCaseManager.querycltjQlCaseList(doneCaseVo, pageNum, pageSize);
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> saveCaseReturn(CaseReturn caseReturn) {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        ApiResultSet<SysUser> resultUser = sysUserFeginService.getSysUserByUserOid(loginUser.getUserOid());
        caseReturn.setCreateUser(resultUser.getData().getName());
        Map<String, String> map = caseReturnManager.saveCaseReturn(caseReturn);
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }



    @Override
    public ApiResultSet<Map<String, String>> outReturnFile(CaseReturn caseReturn) {
        Map<String, String> map = null;
        if(StrUtil.isNotEmpty(caseReturn.getCaseOid())){
            CaseReturn caseReturnOld= caseReturnManager.queryCaseReturnByCaseOid(caseReturn.getCaseOid());
            if(caseReturnOld!=null){
                caseReturn.setId(caseReturnOld.getId());
                caseReturn.setReturnOid(caseReturn.getReturnOid());
                caseReturn.setInformStatus(caseReturnOld.getInformStatus());
                caseReturn.setInformCms(caseReturnOld.getInformCms());
                caseReturn.setInformRemark(caseReturnOld.getInformRemark());
                map= caseReturnManager.saveCaseReturnInform(caseReturn);
            }else{
                caseReturn.setReturnStatus("3");
                caseReturn.setCreateDate(new Date());
                CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
                ApiResultSet<SysUser> resultUser = sysUserFeginService.getSysUserByUserOid(loginUser.getUserOid());
                caseReturn.setCreateUser(resultUser.getData().getName());
                map= caseReturnManager.saveCaseReturnInform(caseReturn);
            }
            if(caseReturn.getIsCms().equals("Y")){
                if(StringUtil.isNotEmpty(caseReturn.getApplyPhone()) &&StringUtil.isNotEmpty(caseReturn.getReceivePhone())){
                    if(caseReturn.getApplyPhone().equals(caseReturn.getReceivePhone())){
                        SendSmsUtil.sendSms(caseReturn.getApplyPhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");

                    }else{
                        SendSmsUtil.sendSms(caseReturn.getApplyPhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
                        SendSmsUtil.sendSms(caseReturn.getReceivePhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
                    }
                }else{
                    if(StringUtil.isNotEmpty(caseReturn.getApplyPhone())){
                        SendSmsUtil.sendSms(caseReturn.getApplyPhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
                    }
                    if(StringUtil.isNotEmpty(caseReturn.getReceivePhone())){
                        SendSmsUtil.sendSms(caseReturn.getReceivePhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
                    }

                }
            }
        }
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> saveCaseReturnInform(CaseReturn caseReturn) {
        Map<String, String> map = null;
        if(StrUtil.isNotEmpty(caseReturn.getCaseOid())){
           CaseReturn caseReturnOld= caseReturnManager.queryCaseReturnByCaseOid(caseReturn.getCaseOid());
            if(caseReturnOld!=null){
                caseReturnOld.setInformStatus(caseReturn.getInformStatus());
                caseReturnOld.setReceiveCardCode(caseReturn.getReceiveCardCode());
                caseReturnOld.setReceiveName(caseReturn.getReceiveName());
                caseReturnOld.setReceivePhone(caseReturn.getReceivePhone());
                caseReturnOld.setInformCms(caseReturn.getInformCms());
                caseReturnOld.setInformRemark(caseReturn.getInformRemark());
                map= caseReturnManager.saveCaseReturnInform(caseReturn);
            }else{
                caseReturn.setInformStatus("3");
                caseReturn.setReturnStep("0");
                caseReturn.setCreateDate(new Date());
                CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
                ApiResultSet<SysUser> resultUser = sysUserFeginService.getSysUserByUserOid(loginUser.getUserOid());
                caseReturn.setCreateUser(resultUser.getData().getName());
                map= caseReturnManager.saveCaseReturnInform(caseReturn);
            }

        }
        if(caseReturn.getInformCms().equals("Y")){//发送短信
            SendSmsUtil.sendSms(caseReturn.getReceivePhone(), caseReturn.getInformRemark());
        }

        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<CaseReturn> queryCaseReturnByCaseOid(String caseOid) {
        CaseReturn caseReturn = caseReturnManager.queryCaseReturnByCaseOid(caseOid);
        ApiResultSet<CaseReturn> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(caseReturn);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCase> getOneRetuenCaseByCaseNumber(String caseNumber) {
        QlCaseVo doneCaseVo=new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        QlCase qlCase= qlCaseManager.getOneRetuenCaseByCaseNumber(doneCaseVo);
        ApiResultSet<QlCase> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCase);
        return apiResultSet;
    }
}

package com.zfsoft.single.controller.ywbl;

import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.data.QlCaseLinkResult;
import com.zfsoft.cases.service.QlCaseLinkResultService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.ywbl.InternetAuditRecord;
import com.zfsoft.single.manager.ywbl.InternetAuditRecordManager;
import com.zfsoft.single.service.ywbl.InternetAuditRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: dongxl
 * @create: 2020-11-9
 * @description: 办件预审
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InternetAuditRecordController implements InternetAuditRecordService {

    private final InternetAuditRecordManager internetAuditRecordManager;

    private final QlCaseLinkResultService qlCaseLinkResultServiceFeginService;


    /**
     * 保存或者更新信息
     * dongxl
     * @param internetAuditRecord
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdate( InternetAuditRecord internetAuditRecord) {
        log.info("办件预审信息新增/更新成功：{}", JSON.toJSONString(internetAuditRecord));
        this.internetAuditRecordManager.saveOrUpdate(internetAuditRecord);
        //保存到办件审批环节表中
        if(internetAuditRecord.getAuditResult().equals("3")){//不予通过
            QlCaseLinkResult qlCaseLinkResult=new QlCaseLinkResult();
            qlCaseLinkResult.setPersonOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
            qlCaseLinkResult.setLinkCode("bj");//暂时先塞
            qlCaseLinkResult.setLinkName("办结");
            qlCaseLinkResult.setPersonName(CurrentLoginUserHolder.getCurrentLoginUser().getUserName());
            qlCaseLinkResult.setFinalDate(new Date());
            qlCaseLinkResult.setFinalStatus(47);//预审不通过
            qlCaseLinkResult.setCaseOid(internetAuditRecord.getCaseOid());
            qlCaseLinkResult.setFinalOpinion(internetAuditRecord.getResultDesc());
            qlCaseLinkResult.setFinalOpinionDesc("预审不通过！！！！！");
            qlCaseLinkResultServiceFeginService.saveQlCaseLinkResult(qlCaseLinkResult);
        }
        return new ApiResultSet(internetAuditRecord);
    }

    @Override
    public ApiResultSet getOneByCaseOid(String caseOid) {
        return null;
    }


}

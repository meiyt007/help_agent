package com.zfsoft.single.controller.ywbl;

import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.ywbl.CaseForm;
import com.zfsoft.single.manager.ywbl.CaseFormManager;
import com.zfsoft.single.service.ywbl.CaseFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: dongxl
 * @create: 2020-12-1
 * @description: 办件表单
 */
@Slf4j
@RestController
public class CaseFormController implements CaseFormService {
    @Resource
    private CaseFormManager caseFormManager;

    /**
     * 保存或者更新
     * dongxl
     * @param caseForm
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdate(CaseForm caseForm) {
        log.info("办件表单信息新增/更新成功：{}", JSON.toJSONString(caseForm));
        this.caseFormManager.saveOrUpdate(caseForm);
        return new ApiResultSet(caseForm);
    }

    /**
     * 根据办件编号和事项表单配置编号查询信息
     * @param caseOid
     * @param sxSerFormOid
     * @return
     */

    @Override
    public ApiResultSet caseFormByCaseOid(String caseOid, String sxSerFormOid) {
        CaseForm caseForm = this.caseFormManager.caseFormByCaseOid(caseOid,sxSerFormOid);
        log.info("详情获取成功：{}", caseForm);
        return new ApiResultSet(caseForm);
    }

    @Override
    public ApiResultSet caseFormByCase(CaseForm caseForm) {
        List<CaseForm> caseList = this.caseFormManager.caseFormByCase(caseForm);
        log.info("详情获取成功：{}", caseList);
        return new ApiResultSet(caseList);
    }

    @Override
    public ApiResultSet saveOrUpdateComboCaseForm(CaseForm caseForm) {
        List<CaseForm> list=new ArrayList<>();
        if(caseForm!=null){
            if(caseForm.getComboCaseFormList()!=null &&caseForm.getComboCaseFormList().size()>0){
                for(CaseForm cf: caseForm.getComboCaseFormList()){
                    this.caseFormManager.saveOrUpdate(cf);
                    list.add(cf);
                }
            }
        }
        return new ApiResultSet(list);
    }
}

package com.zfsoft.single.controller.api;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.api.SignatureFlowRecord;
import com.zfsoft.single.data.formTemplate.TermlateDataDto;
import com.zfsoft.single.data.ywbl.QlCaseCorrection;
import com.zfsoft.single.manager.api.ApiManager;
import com.zfsoft.single.manager.formTemplate.MaterialTemplateManager;
import com.zfsoft.single.manager.ywbl.QlCaseCorrectionManager;
import com.zfsoft.single.service.api.ApiService;
import com.zfsoft.superwindow.data.sxry.DishonestPerson;
import com.zfsoft.superwindow.data.sxry.DishonestRecord;
import com.zfsoft.superwindow.service.sxry.DishonestPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 接口提供类
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController implements ApiService {

    private final QlCaseService qlCaseServiceFeginService;

    private final QlCaseApplayService qlCaseApplayServiceFeginService;

    private final DishonestPersonService dishonestPersonFeginService;

    private final QlCaseCorrectionManager qlCaseCorrectionManager;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Resource
    private MaterialTemplateManager materialTemplateManager;

    @Resource
    private ApiManager apiManager;

    @Override
    public ApiResultSet batchSaveDishonestPerson() {
        //查询容缺补正所有已经超期的信息
        ApiResultSet<List<QlCase>> list = qlCaseServiceFeginService.getOverDueAllCase();
        if (list != null && list.getData() != null && list.getData().size() > 0) {
            for (QlCase qlCase : list.getData()) {
                ApiResultSet<QlCaseApplay> qlApply = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(qlCase.getCaseOid());
                DishonestPerson person = new DishonestPerson();
                person.setCardNumber(qlApply.getData().getCredentialNumber());
                person.setDelFlag(0);
                person.setPhone(qlApply.getData().getApplyUserPhone());
                person.setCardType(qlApply.getData().getCredentialType());
                person.setName(qlApply.getData().getApplyUserName());
                person.setStatus(0);
                person.setCreateDate(new Date());
                //保存失信记录
                DishonestRecord record = new DishonestRecord();
                record.setDishonestRecordTitle("容缺后补受理过期!");
                record.setDishonestRecord("容缺后补受理过期!");
                record.setCaseNumber(qlCase.getCaseNumber());
                person.setDishonestRecord(record);
                dishonestPersonFeginService.saveOrUpdate(person);

                qlCaseServiceFeginService.updateQlCase(qlCase.getCaseOid(), 5);
            }
        }
        //查询补齐补正已经超期的信息
        List<QlCaseCorrection> listCorrection = qlCaseCorrectionManager.getDueTimeCorrection();
        if (listCorrection != null && listCorrection.size() > 0) {
            for (QlCaseCorrection correction : listCorrection) {
                ApiResultSet<QlCaseApplay> qlApply = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(correction.getCaseOid());
                DishonestPerson person = new DishonestPerson();
                person.setCardNumber(qlApply.getData().getCredentialNumber());
                person.setDelFlag(0);
                person.setPhone(qlApply.getData().getApplyUserPhone());
                person.setCardType(qlApply.getData().getCredentialType());
                person.setName(qlApply.getData().getApplyUserName());
                person.setStatus(0);
                person.setCreateDate(new Date());
                //保存失信记录
                DishonestRecord record = new DishonestRecord();
                record.setDishonestRecordTitle("补正受理过期!");
                record.setDishonestRecord("补正受理过期!");
                record.setCaseNumber(correction.getCaseNumber());
                person.setDishonestRecord(record);
                dishonestPersonFeginService.saveOrUpdate(person);
                correction.setCorrectionStatus("2");//补证过期未受理
                qlCaseCorrectionManager.updateCorrection(correction);

                qlCaseServiceFeginService.updateQlCase(correction.getCaseOid(), 5);
            }
        }
        return null;
    }

    @Override
    public ApiResultSet saveOrUpdateSignatureFlow(SignatureFlowRecord signatureFlowRecord) {
        ApiResultSet apiResultSet = new ApiResultSet();
        String oid = apiManager.saveOrUpdateSignatureFlow(signatureFlowRecord);
        apiResultSet.setMessage("保存成功");
        apiResultSet.setData(oid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet electronicSignatureCallBack(HttpServletRequest request) {
        apiManager.electronicSignatureCallBack(request);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<List<SignatureFlowRecord>> getSignatureFlowRecordByCaseOid(String caseOid) {
        ApiResultSet<List<SignatureFlowRecord>> apiResultSet = new ApiResultSet<>();
        List<SignatureFlowRecord> signatureFlowRecords = apiManager.getSignatureFlowRecordByCaseOid(caseOid);
        apiResultSet.setData(signatureFlowRecords);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SignatureFlowRecord> getSignatureFlowRecord(String caseOid, String materialOid) {
        SignatureFlowRecord signatureFlowRecord = apiManager.getSignatureFlowRecord(caseOid, materialOid);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        if (null != signatureFlowRecord) {
//            info.put("flowId", signatureFlowRecord.getFlowId());
//            info.put("status", signatureFlowRecord.getStatus());
//            List<Map<String, Object>> taskList = new ArrayList<>(1);
//            Map<String, Object> task = new HashMap<>();
//            task.put("signUrl", signatureFlowRecord.getFileKey());
//            taskList.add(task);
//            info.put("signTasks", taskList);
//            resultMap.put("data", info);
            return new ApiResultSet<>(signatureFlowRecord);
        } else {
            return new ApiResultSet<>();
        }
    }

    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(String caseOid) {
        // 查询办件所有材料
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialListByCaseOid(caseOid);
        if (null != apiResultSet && null != apiResultSet.getData()) {
            // 所有材料-已分类（智能生成、免交、自备）
            Map<String, List<QlCaseMaterial>> mapResult = apiResultSet.getData();
            // 实际返回的数据（如果材料属性是智能制作，但是没有配置表单模板，则将此材料从智能制作分类移到自备分类）
            Map<String, List<QlCaseMaterial>> realMap = new HashMap<>();
            List<QlCaseMaterial> autoList = new ArrayList<>();
            List<QlCaseMaterial> noSubmitList = new ArrayList<>();
            List<QlCaseMaterial> uploadList = new ArrayList<>();
            // 办件信息
            ApiResultSet<QlCase> qlCaseApiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
            if (null != qlCaseApiResultSet && null != qlCaseApiResultSet.getData()) {
                QlCase qlCase = qlCaseApiResultSet.getData();
                ApiResultSet<QlCaseApplay> qlApply = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(qlCase.getCaseOid());
                if ("19921510723".equals(qlApply.getData().getApplyUserPhone())) {
                    String testMaterial = "[\n" +
                            "\t\t\t{\n" +
                            "\t\t\t\t\"id\": \"323165965992034304\",\n" +
                            "\t\t\t\t\"caseMaterialOid\": \"36afda7e63f2406cbb85ea07a89c0a62\",\n" +
                            "\t\t\t\t\"materialOid\": \"815c9ca13faa4a978316c5a55f3ef240\",\n" +
                            "\t\t\t\t\"materialName\": \"《公司登记（备案）申请书》\",\n" +
                            "\t\t\t\t\"caseOid\": \"f8a87e2f811049f496fd4dbac2823bfe\",\n" +
                            "\t\t\t\t\"collectionFlag\": 0,\n" +
                            "\t\t\t\t\"collectionType\": null,\n" +
                            "\t\t\t\t\"collectionNumber\": 0,\n" +
                            "\t\t\t\t\"collectionDate\": null,\n" +
                            "\t\t\t\t\"delFlag\": 0,\n" +
                            "\t\t\t\t\"createDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"createUserOid\": \"233892098962853888\",\n" +
                            "\t\t\t\t\"elemLicenseOid\": null,\n" +
                            "\t\t\t\t\"elemNumber\": null,\n" +
                            "\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\"baiduTemplateIds\": null,\n" +
                            "\t\t\t\t\"attaList\": [],\n" +
                            "\t\t\t\t\"materialCatalogList\": [],\n" +
                            "\t\t\t\t\"classifyRecList\": [],\n" +
                            "\t\t\t\t\"attaListwgl\": [],\n" +
                            "\t\t\t\t\"refinedMaterialList\": [],\n" +
                            "\t\t\t\t\"materialSampleAddr\": \"fe6b0d80d8234cdba76c12920e4f8830\",\n" +
                            "\t\t\t\t\"materialSampleAddrYl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/00/2E/iwl7tGMzxEGAENW3AAIQAOX8Jmc300.doc\",\n" +
                            "\t\t\t\t\"qlCaseMaterialAttaList\": [\n" +
                            "\t\t\t\t\t{\n" +
                            "\t\t\t\t\t\t\"id\": null,\n" +
                            "\t\t\t\t\t\t\"materialAttaOid\": \"592d3588c1d649e3a823ac2fcaf34dd5\",\n" +
                            "\t\t\t\t\t\t\"caseMaterialOid\": \"36afda7e63f2406cbb85ea07a89c0a62\",\n" +
                            "\t\t\t\t\t\t\"attaOid\": \"bad14826d7d44197a0fd891beeaa8bd1\",\n" +
                            "\t\t\t\t\t\t\"createDate\": null,\n" +
                            "\t\t\t\t\t\t\"modifyDate\": null,\n" +
                            "\t\t\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\t\t\"src\": null,\n" +
                            "\t\t\t\t\t\t\"qlSysAtta\": null,\n" +
                            "\t\t\t\t\t\t\"refinedMaterialOid\": null,\n" +
                            "\t\t\t\t\t\t\"modifyBeforeAttaOid\": null,\n" +
                            "\t\t\t\t\t\t\"serialNumber\": null,\n" +
                            "\t\t\t\t\t\t\"templatePdfUrl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E8/rBWyJmQ1P--AKU7qAAI5bBkF2E8246.pdf\",\n" +
                            "\t\t\t\t\t\t\"signaturePdfUrl\": \"\",\n" +
                            "\t\t\t\t\t\t\"autoType\": 0,\n" +
                            "\t\t\t\t\t\t\"attaOidWord\": \"cd4b66ee7d434fe4873d3b0efccf5d9a\",\n" +
                            "\t\t\t\t\t\t\"materialAttaOidWord\": \"20f20bbf64e14e0e96bdfd2655e3b250\",\n" +
                            "\t\t\t\t\t\t\"templatePdfUrlWord\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E7/rBWyJmQ1P-2AbS_RAACAWV2SOsc59.docx\"\n" +
                            "\t\t\t\t\t}\n" +
                            "\t\t\t\t],\n" +
                            "\t\t\t\t\"preTrialResult\": null,\n" +
                            "\t\t\t\t\"materialAttaOid\": null,\n" +
                            "\t\t\t\t\"auditType\": null,\n" +
                            "\t\t\t\t\"resultStatus\": null,\n" +
                            "\t\t\t\t\"confirmStatus\": null,\n" +
                            "\t\t\t\t\"rqbzFlag\": null,\n" +
                            "\t\t\t\t\"elecLicenName\": null,\n" +
                            "\t\t\t\t\"elecLicenNumber\": null,\n" +
                            "\t\t\t\t\"modifyDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"mustFlag\": 0,\n" +
                            "\t\t\t\t\"directoryObj\": null,\n" +
                            "\t\t\t\t\"elecBillOid\": null,\n" +
                            "\t\t\t\t\"electronicResult\": null,\n" +
                            "\t\t\t\t\"roleType\": null,\n" +
                            "\t\t\t\t\"memo\": null,\n" +
                            "\t\t\t\t\"materialType\": 0,\n" +
                            "\t\t\t\t\"materialFormat\": 1,\n" +
                            "\t\t\t\t\"paperNumber\": \"1\",\n" +
                            "\t\t\t\t\"materialSort\": \"1\",\n" +
                            "\t\t\t\t\"materialEmptyAttoid\": \"678372b4d62b4c099aeda80be0a4352a\",\n" +
                            "\t\t\t\t\"materialEmptyAddrUrl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/00/07/rBWyJmNpyc-AS-J8AAH4AGKu2tc650.doc\",\n" +
                            "\t\t\t\t\"emptyOriginName\": \"公司登记（备案）申请书.doc\",\n" +
                            "\t\t\t\t\"simpleOriginName\": \"【样表】公司登记（备案）申请书.doc\"\n" +
                            "\t\t\t},\n" +
                            "\t\t\t{\n" +
                            "\t\t\t\t\"id\": \"323165966000422912\",\n" +
                            "\t\t\t\t\"caseMaterialOid\": \"bd72077689264c13a926acaf2d70e68b\",\n" +
                            "\t\t\t\t\"materialOid\": \"165e0d750bf546d2a141ea57d76e50c1\",\n" +
                            "\t\t\t\t\"materialName\": \"关于修改公司章程的决议、决定(股东决定)\",\n" +
                            "\t\t\t\t\"caseOid\": \"f8a87e2f811049f496fd4dbac2823bfe\",\n" +
                            "\t\t\t\t\"collectionFlag\": 0,\n" +
                            "\t\t\t\t\"collectionType\": null,\n" +
                            "\t\t\t\t\"collectionNumber\": 0,\n" +
                            "\t\t\t\t\"collectionDate\": null,\n" +
                            "\t\t\t\t\"delFlag\": 0,\n" +
                            "\t\t\t\t\"createDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"createUserOid\": \"233892098962853888\",\n" +
                            "\t\t\t\t\"elemLicenseOid\": null,\n" +
                            "\t\t\t\t\"elemNumber\": null,\n" +
                            "\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\"baiduTemplateIds\": null,\n" +
                            "\t\t\t\t\"attaList\": [],\n" +
                            "\t\t\t\t\"materialCatalogList\": [],\n" +
                            "\t\t\t\t\"classifyRecList\": [],\n" +
                            "\t\t\t\t\"attaListwgl\": [],\n" +
                            "\t\t\t\t\"refinedMaterialList\": [],\n" +
                            "\t\t\t\t\"materialSampleAddr\": null,\n" +
                            "\t\t\t\t\"materialSampleAddrYl\": null,\n" +
                            "\t\t\t\t\"qlCaseMaterialAttaList\": [\n" +
                            "\t\t\t\t\t{\n" +
                            "\t\t\t\t\t\t\"id\": null,\n" +
                            "\t\t\t\t\t\t\"materialAttaOid\": \"dd4489aebc9a4108bb5773aaf070e37f\",\n" +
                            "\t\t\t\t\t\t\"caseMaterialOid\": \"bd72077689264c13a926acaf2d70e68b\",\n" +
                            "\t\t\t\t\t\t\"attaOid\": \"fe7a28dac076436abfcfca1fe96cfe8e\",\n" +
                            "\t\t\t\t\t\t\"createDate\": null,\n" +
                            "\t\t\t\t\t\t\"modifyDate\": null,\n" +
                            "\t\t\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\t\t\"src\": null,\n" +
                            "\t\t\t\t\t\t\"qlSysAtta\": null,\n" +
                            "\t\t\t\t\t\t\"refinedMaterialOid\": null,\n" +
                            "\t\t\t\t\t\t\"modifyBeforeAttaOid\": null,\n" +
                            "\t\t\t\t\t\t\"serialNumber\": null,\n" +
                            "\t\t\t\t\t\t\"templatePdfUrl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E8/rBWyJmQ1P_OAXZzxAABTlTeBzVA944.pdf\",\n" +
                            "\t\t\t\t\t\t\"signaturePdfUrl\": \"\",\n" +
                            "\t\t\t\t\t\t\"autoType\": 0,\n" +
                            "\t\t\t\t\t\t\"attaOidWord\": \"7bcdb3b8e45f459d9bd1ee10e7f0caab\",\n" +
                            "\t\t\t\t\t\t\"materialAttaOidWord\": \"21076dc9ba764cb2bcae4bc2c29dda97\",\n" +
                            "\t\t\t\t\t\t\"templatePdfUrlWord\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E8/rBWyJmQ1P_GAW6f_AAA0UwTM19E00.docx\"\n" +
                            "\t\t\t\t\t}\n" +
                            "\t\t\t\t],\n" +
                            "\t\t\t\t\"preTrialResult\": null,\n" +
                            "\t\t\t\t\"materialAttaOid\": null,\n" +
                            "\t\t\t\t\"auditType\": null,\n" +
                            "\t\t\t\t\"resultStatus\": null,\n" +
                            "\t\t\t\t\"confirmStatus\": null,\n" +
                            "\t\t\t\t\"rqbzFlag\": null,\n" +
                            "\t\t\t\t\"elecLicenName\": null,\n" +
                            "\t\t\t\t\"elecLicenNumber\": null,\n" +
                            "\t\t\t\t\"modifyDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"mustFlag\": 0,\n" +
                            "\t\t\t\t\"directoryObj\": null,\n" +
                            "\t\t\t\t\"elecBillOid\": null,\n" +
                            "\t\t\t\t\"electronicResult\": null,\n" +
                            "\t\t\t\t\"roleType\": null,\n" +
                            "\t\t\t\t\"memo\": null,\n" +
                            "\t\t\t\t\"materialType\": 0,\n" +
                            "\t\t\t\t\"materialFormat\": 1,\n" +
                            "\t\t\t\t\"paperNumber\": \"1\",\n" +
                            "\t\t\t\t\"materialSort\": \"2\",\n" +
                            "\t\t\t\t\"materialEmptyAttoid\": null,\n" +
                            "\t\t\t\t\"materialEmptyAddrUrl\": null,\n" +
                            "\t\t\t\t\"emptyOriginName\": null,\n" +
                            "\t\t\t\t\"simpleOriginName\": null\n" +
                            "\t\t\t},\n" +
                            "\t\t\t{\n" +
                            "\t\t\t\t\"id\": \"323165966008811520\",\n" +
                            "\t\t\t\t\"caseMaterialOid\": \"fcb87c601af44714b002266eaf96cddf\",\n" +
                            "\t\t\t\t\"materialOid\": \"9bb5efbb733f4322b2100c90dd28f242\",\n" +
                            "\t\t\t\t\"materialName\": \"修改后的公司章程或者公司章程修正案（章修-1个股东）\",\n" +
                            "\t\t\t\t\"caseOid\": \"f8a87e2f811049f496fd4dbac2823bfe\",\n" +
                            "\t\t\t\t\"collectionFlag\": 0,\n" +
                            "\t\t\t\t\"collectionType\": null,\n" +
                            "\t\t\t\t\"collectionNumber\": 0,\n" +
                            "\t\t\t\t\"collectionDate\": null,\n" +
                            "\t\t\t\t\"delFlag\": 0,\n" +
                            "\t\t\t\t\"createDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"createUserOid\": \"233892098962853888\",\n" +
                            "\t\t\t\t\"elemLicenseOid\": null,\n" +
                            "\t\t\t\t\"elemNumber\": null,\n" +
                            "\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\"baiduTemplateIds\": null,\n" +
                            "\t\t\t\t\"attaList\": [],\n" +
                            "\t\t\t\t\"materialCatalogList\": [],\n" +
                            "\t\t\t\t\"classifyRecList\": [],\n" +
                            "\t\t\t\t\"attaListwgl\": [],\n" +
                            "\t\t\t\t\"refinedMaterialList\": [],\n" +
                            "\t\t\t\t\"materialSampleAddr\": null,\n" +
                            "\t\t\t\t\"materialSampleAddrYl\": null,\n" +
                            "\t\t\t\t\"qlCaseMaterialAttaList\": [\n" +
                            "\t\t\t\t\t{\n" +
                            "\t\t\t\t\t\t\"id\": null,\n" +
                            "\t\t\t\t\t\t\"materialAttaOid\": \"20a414e4caa54cb88a4292061f5b2b9b\",\n" +
                            "\t\t\t\t\t\t\"caseMaterialOid\": \"fcb87c601af44714b002266eaf96cddf\",\n" +
                            "\t\t\t\t\t\t\"attaOid\": \"9f5513f2e19140b2a534f8c90581abfd\",\n" +
                            "\t\t\t\t\t\t\"createDate\": null,\n" +
                            "\t\t\t\t\t\t\"modifyDate\": null,\n" +
                            "\t\t\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\t\t\"src\": null,\n" +
                            "\t\t\t\t\t\t\"qlSysAtta\": null,\n" +
                            "\t\t\t\t\t\t\"refinedMaterialOid\": null,\n" +
                            "\t\t\t\t\t\t\"modifyBeforeAttaOid\": null,\n" +
                            "\t\t\t\t\t\t\"serialNumber\": null,\n" +
                            "\t\t\t\t\t\t\"templatePdfUrl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E8/rBWyJmQ1P_aAdrWZAACy0kg8kfk739.pdf\",\n" +
                            "\t\t\t\t\t\t\"signaturePdfUrl\": \"\",\n" +
                            "\t\t\t\t\t\t\"autoType\": 0,\n" +
                            "\t\t\t\t\t\t\"attaOidWord\": \"89c054e3ce374358aa2ede8d2f94cedd\",\n" +
                            "\t\t\t\t\t\t\"materialAttaOidWord\": \"698db02c1fe84e1886e6130f5f37d577\",\n" +
                            "\t\t\t\t\t\t\"templatePdfUrlWord\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E8/rBWyJmQ1P_WAVka3AAAu_4gXe-483.docx\"\n" +
                            "\t\t\t\t\t}\n" +
                            "\t\t\t\t],\n" +
                            "\t\t\t\t\"preTrialResult\": null,\n" +
                            "\t\t\t\t\"materialAttaOid\": null,\n" +
                            "\t\t\t\t\"auditType\": null,\n" +
                            "\t\t\t\t\"resultStatus\": null,\n" +
                            "\t\t\t\t\"confirmStatus\": null,\n" +
                            "\t\t\t\t\"rqbzFlag\": null,\n" +
                            "\t\t\t\t\"elecLicenName\": null,\n" +
                            "\t\t\t\t\"elecLicenNumber\": null,\n" +
                            "\t\t\t\t\"modifyDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"mustFlag\": 0,\n" +
                            "\t\t\t\t\"directoryObj\": null,\n" +
                            "\t\t\t\t\"elecBillOid\": null,\n" +
                            "\t\t\t\t\"electronicResult\": null,\n" +
                            "\t\t\t\t\"roleType\": null,\n" +
                            "\t\t\t\t\"memo\": null,\n" +
                            "\t\t\t\t\"materialType\": 0,\n" +
                            "\t\t\t\t\"materialFormat\": 1,\n" +
                            "\t\t\t\t\"paperNumber\": \"1\",\n" +
                            "\t\t\t\t\"materialSort\": \"3\",\n" +
                            "\t\t\t\t\"materialEmptyAttoid\": null,\n" +
                            "\t\t\t\t\"materialEmptyAddrUrl\": null,\n" +
                            "\t\t\t\t\"emptyOriginName\": null,\n" +
                            "\t\t\t\t\"simpleOriginName\": null\n" +
                            "\t\t\t},\n" +
                            "\t\t\t{\n" +
                            "\t\t\t\t\"id\": \"323165966021394432\",\n" +
                            "\t\t\t\t\"caseMaterialOid\": \"6c00188013884c6c88f6876f5afe2447\",\n" +
                            "\t\t\t\t\"materialOid\": \"cff4b8b9d5b8494bb149d114de4db99f\",\n" +
                            "\t\t\t\t\"materialName\": \"股权转让相关材料\",\n" +
                            "\t\t\t\t\"caseOid\": \"f8a87e2f811049f496fd4dbac2823bfe\",\n" +
                            "\t\t\t\t\"collectionFlag\": 0,\n" +
                            "\t\t\t\t\"collectionType\": null,\n" +
                            "\t\t\t\t\"collectionNumber\": 0,\n" +
                            "\t\t\t\t\"collectionDate\": null,\n" +
                            "\t\t\t\t\"delFlag\": 0,\n" +
                            "\t\t\t\t\"createDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"createUserOid\": \"233892098962853888\",\n" +
                            "\t\t\t\t\"elemLicenseOid\": null,\n" +
                            "\t\t\t\t\"elemNumber\": null,\n" +
                            "\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\"baiduTemplateIds\": null,\n" +
                            "\t\t\t\t\"attaList\": [],\n" +
                            "\t\t\t\t\"materialCatalogList\": [],\n" +
                            "\t\t\t\t\"classifyRecList\": [],\n" +
                            "\t\t\t\t\"attaListwgl\": [],\n" +
                            "\t\t\t\t\"refinedMaterialList\": [],\n" +
                            "\t\t\t\t\"materialSampleAddr\": \"6f541b485ff7421bbb5fb89216c644c2\",\n" +
                            "\t\t\t\t\"materialSampleAddrYl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/01/47/rBWyJmOQK1iAY6YQAABAbD_UbAA56.docx\",\n" +
                            "\t\t\t\t\"qlCaseMaterialAttaList\": [\n" +
                            "\t\t\t\t\t{\n" +
                            "\t\t\t\t\t\t\"id\": null,\n" +
                            "\t\t\t\t\t\t\"materialAttaOid\": \"79bb78734a6442c89471dccedb106049\",\n" +
                            "\t\t\t\t\t\t\"caseMaterialOid\": \"6c00188013884c6c88f6876f5afe2447\",\n" +
                            "\t\t\t\t\t\t\"attaOid\": \"eb055970a202496c97ca09dcc99e7def\",\n" +
                            "\t\t\t\t\t\t\"createDate\": null,\n" +
                            "\t\t\t\t\t\t\"modifyDate\": null,\n" +
                            "\t\t\t\t\t\t\"materialCatalogOid\": null,\n" +
                            "\t\t\t\t\t\t\"src\": null,\n" +
                            "\t\t\t\t\t\t\"qlSysAtta\": null,\n" +
                            "\t\t\t\t\t\t\"refinedMaterialOid\": null,\n" +
                            "\t\t\t\t\t\t\"modifyBeforeAttaOid\": null,\n" +
                            "\t\t\t\t\t\t\"serialNumber\": null,\n" +
                            "\t\t\t\t\t\t\"templatePdfUrl\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E9/rBWyJmQ1P_uAMNizAACnR3n85WU662.pdf\",\n" +
                            "\t\t\t\t\t\t\"signaturePdfUrl\": \"\",\n" +
                            "\t\t\t\t\t\t\"autoType\": 0,\n" +
                            "\t\t\t\t\t\t\"attaOidWord\": \"e161fb007f7746e68a0d88199e494626\",\n" +
                            "\t\t\t\t\t\t\"materialAttaOidWord\": \"094d4e91f00f47a9b2e3497af2e20511\",\n" +
                            "\t\t\t\t\t\t\"templatePdfUrlWord\": \"https://onlineserve.shhuangpu.gov.cn/fastdf/group1/M00/02/E9/rBWyJmQ1P_mAO7O9AAAqZq5pBxo46.docx\"\n" +
                            "\t\t\t\t\t}\n" +
                            "\t\t\t\t],\n" +
                            "\t\t\t\t\"preTrialResult\": null,\n" +
                            "\t\t\t\t\"materialAttaOid\": null,\n" +
                            "\t\t\t\t\"auditType\": null,\n" +
                            "\t\t\t\t\"resultStatus\": null,\n" +
                            "\t\t\t\t\"confirmStatus\": null,\n" +
                            "\t\t\t\t\"rqbzFlag\": null,\n" +
                            "\t\t\t\t\"elecLicenName\": null,\n" +
                            "\t\t\t\t\"elecLicenNumber\": null,\n" +
                            "\t\t\t\t\"modifyDate\": \"2023-04-11 18:26:10\",\n" +
                            "\t\t\t\t\"mustFlag\": 1,\n" +
                            "\t\t\t\t\"directoryObj\": null,\n" +
                            "\t\t\t\t\"elecBillOid\": null,\n" +
                            "\t\t\t\t\"electronicResult\": null,\n" +
                            "\t\t\t\t\"roleType\": null,\n" +
                            "\t\t\t\t\"memo\": null,\n" +
                            "\t\t\t\t\"materialType\": 0,\n" +
                            "\t\t\t\t\"materialFormat\": 1,\n" +
                            "\t\t\t\t\"paperNumber\": \"1\",\n" +
                            "\t\t\t\t\"materialSort\": \"9\",\n" +
                            "\t\t\t\t\"materialEmptyAttoid\": null,\n" +
                            "\t\t\t\t\"materialEmptyAddrUrl\": null,\n" +
                            "\t\t\t\t\"emptyOriginName\": null,\n" +
                            "\t\t\t\t\"simpleOriginName\": \"股权转让协议.docx\"\n" +
                            "\t\t\t}\n" +
                            "\t\t]";

//                                    QlCaseMaterial testQlCaseMaterial = new QlCaseMaterial();
                    List<QlCaseMaterial> testQlCaseMaterial = JSONObject.parseArray(testMaterial, QlCaseMaterial.class);
                    autoList.addAll(testQlCaseMaterial);
                }
                for (String key : mapResult.keySet()) {
                    List<QlCaseMaterial> qlCaseMaterials = mapResult.get(key);
                    if (null == qlCaseMaterials || qlCaseMaterials.size() == 0) {
                        continue;
                    }

                    // 遍历每个分类中的所有材料
                    for (QlCaseMaterial qlCaseMaterial : qlCaseMaterials) {
                        if ("autoProduceMaterialList".equals(key)) {
                            if ("19921510723".equals(qlApply.getData().getApplyUserPhone())) {

                            } else {
                            // 根据办件信息查询所有表单模板
                            List<TermlateDataDto> termlateDataDtos = materialTemplateManager.getTemplateList(caseOid, qlCase.getServiceOid());
                            Map<String, List<TermlateDataDto>> templateMap = termlateDataDtos.stream().collect(Collectors.groupingBy(TermlateDataDto::getMaterialOid));
                            System.out.println("表单模板信息====>" + JSON.toJSONString(templateMap));
                            // 判断智能制作的材料是否具有模板信息，若没有配置模板，则将该材料移到自备分类
                            if (null != templateMap.get(qlCaseMaterial.getMaterialOid())) {
                                List<QlCaseMaterialAtta> qlCaseMaterialAttas = qlCaseMaterial.getQlCaseMaterialAttaList();
                                SignatureFlowRecord signatureFlowRecord = apiManager.getSignatureFlowRecord(caseOid, qlCaseMaterial.getMaterialOid());
                                List<TermlateDataDto> termlateDataDtoList = templateMap.get(qlCaseMaterial.getMaterialOid());
                                if (null == qlCaseMaterialAttas || qlCaseMaterialAttas.size() == 0) {
                                    if (termlateDataDtoList.size() > 1) {//判断当前材料是否是两份，同时需要pdf和word类型的场景
                                        QlCaseMaterialAtta qlCaseMaterialAtta = new QlCaseMaterialAtta();
                                        for (int i = 0; i < termlateDataDtoList.size(); i++) {
                                            qlCaseMaterialAttas = new ArrayList<>();
                                            TermlateDataDto termlateDataDto = termlateDataDtoList.get(i);
                                            //                                            TermlateDataDto dto = termlateDataDtoList.get(0);
                                            if (termlateDataDto.getAttaUrl().contains(".pdf")) {
                                                //是pdf 这条信息
                                                qlCaseMaterialAtta.setAttaOid(termlateDataDto.getAttaOid());
                                                qlCaseMaterialAtta.setMaterialAttaOid(IdUtil.simpleUUID());
                                                qlCaseMaterialAtta.setCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                                                qlCaseMaterialAtta.setTemplatePdfUrl(termlateDataDto.getAttaUrl());
                                                qlCaseMaterialAtta.setAutoType(0);// 0-签章方式提交；1-普通上传
                                                qlCaseMaterialAtta.setSignaturePdfUrl(signatureFlowRecord == null ? "" : signatureFlowRecord.getDownloadUrl());
                                            }
                                            if (termlateDataDto.getAttaUrl().contains(".docx")) {
                                                //是word
                                                qlCaseMaterialAtta.setAttaOidWord(termlateDataDto.getAttaOid());
                                                qlCaseMaterialAtta.setMaterialAttaOidWord(IdUtil.simpleUUID());
                                                qlCaseMaterialAtta.setTemplatePdfUrlWord(termlateDataDto.getAttaUrl());
                                            }
                                            qlCaseMaterialAttas.add(qlCaseMaterialAtta);
                                            qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
                                        }
                                    } else {
                                        //改条材料就一条信息，不需要同事展示pdf和word类型的场景
                                        qlCaseMaterialAttas = new ArrayList<>();
                                        TermlateDataDto dto = termlateDataDtoList.get(0);
                                        QlCaseMaterialAtta qlCaseMaterialAtta = new QlCaseMaterialAtta();
                                        qlCaseMaterialAtta.setAttaOid(dto.getAttaOid());
                                        qlCaseMaterialAtta.setMaterialAttaOid(IdUtil.simpleUUID());
                                        qlCaseMaterialAtta.setCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                                        qlCaseMaterialAtta.setTemplatePdfUrl(dto.getAttaUrl());
                                        qlCaseMaterialAtta.setAutoType(0);// 0-签章方式提交；1-普通上传
                                        qlCaseMaterialAtta.setSignaturePdfUrl(signatureFlowRecord == null ? "" : signatureFlowRecord.getDownloadUrl());
                                        qlCaseMaterialAttas.add(qlCaseMaterialAtta);
                                        qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
                                    }

                                } else {
                                    // 材料附件信息
                                    for (QlCaseMaterialAtta atta : qlCaseMaterialAttas) {
                                        for (TermlateDataDto dto : termlateDataDtoList) {
                                            if (dto.getMaterialOid().equals(qlCaseMaterial.getMaterialOid())) {
                                                atta.setTemplatePdfUrl(dto.getAttaUrl());// 签章前模板附件地址
                                                atta.setSignaturePdfUrl(signatureFlowRecord == null ? "" : signatureFlowRecord.getDownloadUrl());// 签章后的文件附件地址
                                            }
                                        }
                                    }
                                    qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
                                }
                                //qlCaseMaterialAttaServiceFeginService.updateCaseMaterialAttaList(qlCaseMaterialAttas);
                                autoList.add(qlCaseMaterial);
                            } else {
                                // 没有模板信息，加入自备分类
                                uploadList.add(qlCaseMaterial);
                            }
                        }
                    } else if ("noSubmissionMaterialList".equals(key)) {
                        noSubmitList.add(qlCaseMaterial);
                    } else if ("needUploadMaterialList".equals(key)) {
                        uploadList.add(qlCaseMaterial);
                    }
                }
            }
        }
        realMap.put("autoProduceMaterialList", autoList);// 智能制作
        realMap.put("noSubmissionMaterialList", noSubmitList);// 免交
        realMap.put("needUploadMaterialList", uploadList);// 自备
        apiResultSet.setData(realMap);
    }
        return apiResultSet;
}

    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOidForZC(String caseOid) {
        // 查询办件所有材料
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialListByCaseOidForZC(caseOid);
        if (null != apiResultSet && null != apiResultSet.getData()) {
            // 所有材料分类（智能生成、本地上传）
            Map<String, List<QlCaseMaterial>> mapResult = apiResultSet.getData();
            // 实际返回的数据（如果材料属性是智能制作，但是没有配置表单模板，则将此材料从智能制作分类移到本地上传分类）
            Map<String, List<QlCaseMaterial>> realMap = new HashMap<>();
            List<QlCaseMaterial> autoList = new ArrayList<>();
            List<QlCaseMaterial> uploadList = new ArrayList<>();
            // 办件信息
            ApiResultSet<QlCase> qlCaseApiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
            if (null != qlCaseApiResultSet && null != qlCaseApiResultSet.getData()) {
                QlCase qlCase = qlCaseApiResultSet.getData();
                // 根据办件信息查询所有表单模板
                List<TermlateDataDto> termlateDataDtos = materialTemplateManager.getTemplateList(caseOid, qlCase.getServiceOid());
                Map<String, List<TermlateDataDto>> templateMap = termlateDataDtos.stream().collect(Collectors.groupingBy(TermlateDataDto::getMaterialOid));
                for (String key : mapResult.keySet()) {
                    List<QlCaseMaterial> qlCaseMaterials = mapResult.get(key);
                    if (null == qlCaseMaterials || qlCaseMaterials.size() == 0) {
                        continue;
                    }
                    // 遍历每个分类中的所有材料
                    for (QlCaseMaterial qlCaseMaterial : qlCaseMaterials) {
                        if ("autoProduceMaterialList".equals(key)) {
                            // 判断智能制作的材料是否具有模板信息，若没有配置模板，则将该材料移到自备分类
                            if (null != templateMap.get(qlCaseMaterial.getMaterialOid())) {
                                List<QlCaseMaterialAtta> qlCaseMaterialAttas = qlCaseMaterial.getQlCaseMaterialAttaList();
                                SignatureFlowRecord signatureFlowRecord = apiManager.getSignatureFlowRecord(caseOid, qlCaseMaterial.getMaterialOid());
                                List<TermlateDataDto> termlateDataDtoList = templateMap.get(qlCaseMaterial.getMaterialOid());
                                if (null == qlCaseMaterialAttas || qlCaseMaterialAttas.size() == 0) {
                                    qlCaseMaterialAttas = new ArrayList<>();
                                    TermlateDataDto dto = termlateDataDtoList.get(0);
                                    QlCaseMaterialAtta qlCaseMaterialAtta = new QlCaseMaterialAtta();
                                    qlCaseMaterialAtta.setMaterialAttaOid(IdUtil.simpleUUID());
                                    qlCaseMaterialAtta.setCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                                    qlCaseMaterialAtta.setTemplatePdfUrl(dto.getAttaUrl());
                                    qlCaseMaterialAtta.setSignaturePdfUrl(signatureFlowRecord == null ? "" : signatureFlowRecord.getDownloadUrl());
                                    qlCaseMaterialAttas.add(qlCaseMaterialAtta);
                                    qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
                                } else {
                                    // 材料附件信息
                                    for (QlCaseMaterialAtta atta : qlCaseMaterialAttas) {
                                        for (TermlateDataDto dto : termlateDataDtoList) {
                                            if (dto.getMaterialOid().equals(qlCaseMaterial.getMaterialOid())) {
                                                atta.setTemplatePdfUrl(dto.getAttaUrl());// 签章前模板附件地址
                                                atta.setSignaturePdfUrl(signatureFlowRecord == null ? "" : signatureFlowRecord.getDownloadUrl());// 签章后的文件附件地址
                                            }
                                        }
                                    }
                                    qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
                                }
                                //qlCaseMaterialAttaServiceFeginService.updateCaseMaterialAttaList(qlCaseMaterialAttas);
                                autoList.add(qlCaseMaterial);
                            } else {
                                // 没有模板信息，加入自备分类
                                uploadList.add(qlCaseMaterial);
                            }
                        } else {
                            uploadList.add(qlCaseMaterial);
                        }
                    }
                }
            }
            realMap.put("autoProduceMaterialList", autoList);// 智能制作列表
            realMap.put("needUploadMaterialList", uploadList);// 需要上传列表
            apiResultSet.setData(realMap);
        }
        return apiResultSet;
    }
}

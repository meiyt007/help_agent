package com.zfsoft.superwindow.manager.sign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.manager.EvictSettingManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.single.data.api.SignatureFlowRecord;
import com.zfsoft.single.service.api.ApiService;
import com.zfsoft.superwindow.dbaccess.dao.DbMaterialSignPersonMapper;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialSignPerson;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialSignPersonExample;
import com.zfsoft.superwindow.feign.settings.IdsLoginFeignService;
import com.zfsoft.superwindow.service.easyquickcase.data.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ChangSheng
 * @Date 14:55 2022/6/20
 * @Description 材料多角色签章配置
 **/
@Service
@Slf4j
public class MaterialSignManager {

    @Resource
    private EvictSettingManager evictSettingManager;

    @Resource
    private ApiService apiFeignService;

    @Resource
    private IdsLoginFeignService idsLoginFeignService;

    @Resource
    private DbMaterialSignPersonMapper dbMaterialSignPersonMapper;

    /**
     * 新增多角色签章配置
     * 第一保存子表，第二获取电子签章地址
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public boolean saveSignaturePerson(MaterialSignPersonDto materialSignPersonDto) {
        DbMaterialSignPerson dbMaterialSignPerson;
        boolean flag = false;
        try {
            /**
             * 1、先调用签章获取接口，拿到签章地址
             */
            JSONObject flowJson = getFlow(materialSignPersonDto);
            List<MaterialSignPerson> materialSignPersonList = materialSignPersonDto.getSignList();
            for (MaterialSignPerson tmp : materialSignPersonList) {
                /**
                 * 2、再存表
                 */
                dbMaterialSignPerson = new DbMaterialSignPerson();
                BeanUtils.copyProperties(tmp, dbMaterialSignPerson);
                int index = 0;
                if (null == tmp.getId()) {
                    dbMaterialSignPerson.setSignOid(flowJson.getString("oid"));
                    dbMaterialSignPerson.setFlowId(flowJson.getString("flowId"));
                    dbMaterialSignPerson.setCreateDate(new Date());
                    dbMaterialSignPerson.setDeleteStatus("N");
                    dbMaterialSignPerson.setMailStatus("N");
                    dbMaterialSignPerson.setSignatureUrl(flowJson.getString(dbMaterialSignPerson.getName()));
                    index = dbMaterialSignPersonMapper.insert(dbMaterialSignPerson);
                } else {
                    //修改这边还未开放，后续者记得考虑是否要重新修改签章地址
                    index = dbMaterialSignPersonMapper.updateByPrimaryKeySelective(dbMaterialSignPerson);
                }
                if (index > 0) {
                    flag = true;
                }
                //清除缓存
                evictSettingManager.evictpbpjManage(dbMaterialSignPerson.getId());
            }

        } catch (Exception e) {
            log.warn("新增多角色签章配置报错：{}", e.getMessage());
        }
        return flag;
    }

    /**
     * 根据材料oid查询多角色签章配置
     */
    public List<MaterialSignPerson> getSignaturePerson(String caseOid, String materialOid) {
        DbMaterialSignPersonExample dbMaterialSignPersonExample = new DbMaterialSignPersonExample();
        dbMaterialSignPersonExample.setOrderByClause(" CREATE_DATE DESC ");
        DbMaterialSignPersonExample.Criteria criteria = dbMaterialSignPersonExample.createCriteria();
        criteria.andCaseOidEqualTo(caseOid);//办件oid caseOid
        criteria.andMaterialOidEqualTo(materialOid);//材料oid
        criteria.andDeleteStatusEqualTo(BaseStaticParameter.NO);
        List<DbMaterialSignPerson> dbMaterialSignPersonList = dbMaterialSignPersonMapper.selectByExample(dbMaterialSignPersonExample);
        List<MaterialSignPerson> materialSignPersonList = dbMaterialSignPersonList.stream().map(temp -> {
            MaterialSignPerson materialSignPerson = new MaterialSignPerson();
            BeanUtils.copyProperties(temp, materialSignPerson);
            return materialSignPerson;
        }).collect(Collectors.toList());
        return materialSignPersonList;
    }

    /**
     * 获取电子签章地址
     *
     * @return
     */
    private JSONObject getFlow(MaterialSignPersonDto materialSignPersonDto) throws Exception {
        //拼接参数
        ElectronicSignatureDto electronicSignatureDto = new ElectronicSignatureDto();
        List<SignerDto> list = new ArrayList<>();
        SignerDto signerDto;
        List<MaterialSignPerson> personList = materialSignPersonDto.getSignList();
        for (MaterialSignPerson person : personList) {
            signerDto = new SignerDto();
            //签署人姓名
            if (Strings.isNotBlank(person.getName())) {
                signerDto.setName(person.getName());
            } else {
                throw new Exception("缺少签署人姓名");
            }
            //签署人手机号
            if (Strings.isNotBlank(person.getPhone())) {
                signerDto.setMobile(person.getPhone());
            } else {
                throw new Exception("缺少签署人手机号");
            }
            //企业名称
            if (Strings.isNotBlank(person.getCompanyName())) {
                signerDto.setOrgName(person.getCompanyName());
            }
            //统一社会信用代码
            if (Strings.isNotBlank(person.getSocialCode())) {
                signerDto.setOrgUscc(person.getSocialCode());
            }
            list.add(signerDto);
        }
        electronicSignatureDto.setMaterialOid(personList.get(0).getMaterialOid());
        electronicSignatureDto.setCaseOid(personList.get(0).getCaseOid());
        electronicSignatureDto.setSignerDTOList(list);
        electronicSignatureDto.setFileList(materialSignPersonDto.getFileList());
        electronicSignatureDto.setCallType("1");
        electronicSignatureDto.setInterId("");
        electronicSignatureDto.setRedirectUrl(materialSignPersonDto.getRedirectUrl());
        //创建签章流程
        ResponseData<String> data = idsLoginFeignService.createFlow(electronicSignatureDto);
        //解析数据，返回签章地址
        JSONObject dataJson = new JSONObject();
        if (data.getSuccess()) {
            if (Strings.isNotBlank(data.getData())) {
                JSONObject json = JSONObject.parseObject(data.getData());
                if(!json.getBoolean("success")){
                    throw new Exception(json.getString("message"));
                }
                //创建主表数据，拿到oid
                SignatureFlowRecord signatureFlowRecord = new SignatureFlowRecord();
                if (json.getJSONObject("data").containsKey("flowId")) {
                    signatureFlowRecord.setFlowId(json.getJSONObject("data").getString("flowId"));
                    dataJson.put("flowId", json.getJSONObject("data").getString("flowId"));
                }
                signatureFlowRecord.setStatus(2);
                signatureFlowRecord.setMaterialOid(personList.get(0).getMaterialOid());
                signatureFlowRecord.setCaseOid(personList.get(0).getCaseOid());
                ApiResultSet signatureFlowData = apiFeignService.saveOrUpdateSignatureFlow(signatureFlowRecord);
                dataJson.put("oid", signatureFlowData.getData());
                JSONArray array = json.getJSONObject("data").getJSONArray("signTasks");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject temp = array.getJSONObject(i);
                    dataJson.put(temp.getString("name"), temp.getString("signUrl"));
                }
            } else {
                log.warn("签章返回数据为空：{}", data.getMessage());
                throw new Exception("签章返回数据为空");
            }
        } else {
            log.warn("创建签章失败：{}", data.getMessage());
            throw new Exception("创建签章失败");
        }
        return dataJson;
    }
}

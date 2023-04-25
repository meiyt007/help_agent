package com.zfsoft.service.manager.sxService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFormInfoMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFormInfo;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFormInfoExample;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxMaterialFormTempMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxOptionFieldMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxMaterialFormTemp;
import com.zfsoft.service.dbaccess.data.sxService.DbSxMaterialFormTempExample;
import com.zfsoft.service.dbaccess.data.sxService.DbSxOptionField;
import com.zfsoft.service.dbaccess.data.sxService.DbSxOptionFieldExample;
import com.zfsoft.service.dto.FormTableDto;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.util.BusinessUtil;
import com.zfsoft.service.util.DataSourceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SxFormInfoManager
 * @Author xiayj
 * @Date 2021/7/21 16:17
 **/
@Service
public class SxFormInfoManager {

    @Resource
    private DbSxFormInfoMapper dbSxFormInfoMapper;

    @Resource
    private SxFieldTypeManager sxFieldTypeManager;

    @Resource
    private SxFillFieldManager sxFillFieldManager;

    @Resource
    private DbSxOptionFieldMapper dbSxOptionFieldMapper;

    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private SxOptionFieldValManager sxOptionFieldValManager;

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private DbSxMaterialFormTempMapper dbSxMaterialFormTempMapper;

    /***
     * @param serviceOid
     * @description: 获取字段事项关联列表
     * @return: java.util.List<com.zfsoft.formConfig.data.SxFormInfo>
     * @author: xiayj
     * @date: 2021/7/21
     */
    public List<SxFormInfo> getSxFormInfoList(String serviceOid, String typeOid){
        DbSxFormInfoExample example = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(typeOid)){
            criteria.andFieldTypeOidEqualTo(typeOid);
        }
        List<DbSxFormInfo> dbSxFormInfos = dbSxFormInfoMapper.selectByExample(example);
        List<SxFormInfo> result = new ArrayList<>();
        for(DbSxFormInfo dbSxFormInfo : dbSxFormInfos){
            SxFormInfo sxFormInfo = new SxFormInfo();
            BeanUtils.copyProperties(dbSxFormInfo, sxFormInfo);
            if (StringUtils.isNotEmpty(sxFormInfo.getFieldTypeOid())) {
                sxFormInfo.setFieldTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(sxFormInfo.getFieldTypeOid()).getFieldTypeName());
            } else {
                sxFormInfo.setFieldTypeName(sxFormInfo.getLogicFormName());
            }
            sxFormInfo.setServiceName(sxServiceManager.getSxServiceByOid(sxFormInfo.getServiceOid()).getServiceName());
            result.add(sxFormInfo);
        }
        return result;
    }

    /***
     * @param connectOid
     * @description: 查询SxFormInfo详情
     * @return: com.zfsoft.formConfig.data.SxFormInfo
     * @author: xiayj
     * @date: 2021/7/22
     */
    public SxFormInfo getSxFormInfoByOid(String connectOid){
        List<DbSxFormInfo> dbSxFormInfos = this.getDbSxFormInfo(connectOid, null, null);
        if(dbSxFormInfos.isEmpty()){
            return null;
        }
        DbSxFormInfo dbSxFormInfo = dbSxFormInfos.get(0);
        SxFormInfo connect = new SxFormInfo();
        BeanUtils.copyProperties(dbSxFormInfo,connect);
        if (StringUtils.isNotEmpty(connect.getFieldTypeOid())) {
            connect.setFieldTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(connect.getFieldTypeOid()).getFieldTypeName());
        } else {
            connect.setFieldTypeName(connect.getLogicFormName());
        }
        connect.setServiceName(sxServiceManager.getSxServiceByOid(connect.getServiceOid()).getServiceName());
        return connect;
    }


    /***
     * @param sxFormInfo
     * @description: 保存或更新sxFieldConnect
     * @return: void
     * @author: xiayj
     * @date: 2021/7/22
     */
    public void saveOrUpdateSxFormInfo(SxFormInfo sxFormInfo){
        if(StringUtils.isEmpty(sxFormInfo.getFormOid())){
//            List<DbSxFormInfo> dbSxFormInfos = this.queryDbSxFormInfo(null, sxFormInfo.getServiceOid(), sxFormInfo.getFieldTypeOid());
//            if(!dbSxFormInfos.isEmpty()){
//                throw new ResultInfoException("对应分类数据已存在");
//            }
            DbSxFormInfo dbSxFormInfo = new DbSxFormInfo();
            BeanUtils.copyProperties(sxFormInfo,dbSxFormInfo);
            dbSxFormInfo.setFormOid(IdUtil.simpleUUID());
            dbSxFormInfo.setDelFlag(0);
            dbSxFormInfo.setIsAble(1);
            //逻辑主表为空
            if (StringUtils.isNotEmpty(dbSxFormInfo.getFieldTypeOid())) {
                SxFieldType sxFieldType = sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(dbSxFormInfo.getFieldTypeOid());
                dbSxFormInfo.setSort(sxFieldType.getSort());
            } else {
                dbSxFormInfo.setSort(1);
            }
            dbSxFormInfo.setCreateDate(new Date());
            dbSxFormInfo.setModifyDate(new Date());
            dbSxFormInfoMapper.insert(dbSxFormInfo);

        }else {
            DbSxFormInfo dbSxFormInfos = dbSxFormInfoMapper.getSxFormInfoByFormOid(sxFormInfo.getFormOid());
            if(dbSxFormInfos==null){
                throw new ResultInfoException("找不到对应数据");
            }
            BeanUtils.copyProperties(sxFormInfo,dbSxFormInfos);
            dbSxFormInfos.setModifyDate(new Date());
            dbSxFormInfoMapper.updateByPrimaryKeySelective(dbSxFormInfos);
        }
    }

    /***
     * @param connectOid
     * @description: 删除SxFormInfo
     * @return: void
     * @author: xiayj
     * @date: 2021/7/22
     */
    public void deleteSxFormInfo(String connectOid){
        List<DbSxFormInfo> dbSxFormInfos = this.getDbSxFormInfo(connectOid, null, null);
        for(DbSxFormInfo dbSxFormInfo : dbSxFormInfos){
            dbSxFormInfo.setDelFlag(1);
            dbSxFormInfoMapper.updateByPrimaryKeySelective(dbSxFormInfo);
        }
    }


    public String ableSxFormInfo(Long id) {
        DbSxFormInfo dbSxFormInfo = dbSxFormInfoMapper.selectByPrimaryKey(id);
        if(dbSxFormInfo==null){
            throw new ResultInfoException("找不到表单数据");
        }else{
            if(dbSxFormInfo.getIsAble()==0){
                SxFormInfo info=new SxFormInfo();
                info.setServiceOid(dbSxFormInfo.getServiceOid());
                info.setFieldTypeOid(dbSxFormInfo.getFieldTypeOid());
                info.setIsAble(1);
                info.setId(dbSxFormInfo.getId());
                //判断相同分类下面是否有启用的表单
                List<SxFormInfo> list=getSxFormInfoList(info);
                if(list!=null&& list.size()>0 ){
                    return "同分类下不能同时启用多个表单!";
                }
            }
            if(dbSxFormInfo.getIsAble()==1){
                dbSxFormInfo.setIsAble(0);
            }else{
                dbSxFormInfo.setIsAble(1);
            }
            dbSxFormInfoMapper.updateByPrimaryKeySelective(dbSxFormInfo);
        }
        return null;
    }

    public void updateDesign(String oid, String designOid,String formMainOid,String serviceOid){
        List<DbSxFormInfo> dbSxFormInfos = this.getDbSxFormInfo(oid, null, null);
        if(dbSxFormInfos.isEmpty()){
            throw new ResultInfoException("找不到表单数据");
        }
        DbSxFormInfo dbSxFormInfo = dbSxFormInfos.get(0);
        if(!designOid.equals(dbSxFormInfo.getDesignOid())){
            dbSxFormInfo.setDesignOid(designOid);
            dbSxFormInfo.setFormMainOid(formMainOid);
            dbSxFormInfo.setStatus(5);
            dbSxFormInfoMapper.updateByPrimaryKeySelective(dbSxFormInfo);
        }
        // 表单designOid修改，同步更新事项材料表单关联配置
        if(StrUtil.isNotEmpty(serviceOid)) {
            DbSxMaterialFormTempExample formTempExample = new DbSxMaterialFormTempExample();
            DbSxMaterialFormTempExample.Criteria criteria = formTempExample.createCriteria();
            criteria.andServiceOidEqualTo(serviceOid);
            criteria.andDelFlagEqualTo(0);
            List<DbSxMaterialFormTemp> list = dbSxMaterialFormTempMapper.selectByExample(formTempExample);
            if(CollUtil.isNotEmpty(list)){
                for (DbSxMaterialFormTemp dbSxMaterialFormTemp : list) {
                    dbSxMaterialFormTemp.setDesignOid(designOid);
                    dbSxMaterialFormTempMapper.updateByPrimaryKeySelective(dbSxMaterialFormTemp);
                }
            }
        }
    }


    /***
     * @param connectOid
     * @param serviceOid
     * @param fieldTypeOid
     * @description: 查询DbSxFormInfo
     * @return: java.util.List<com.zfsoft.dbaccess.data.formConfig.DbSxFormInfo>
     * @author: xiayj
     * @date: 2021/7/22
     */
    private List<DbSxFormInfo> queryDbSxFormInfo(String connectOid,String serviceOid,String fieldTypeOid){
        DbSxFormInfoExample example = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andIsAbleEqualTo(1);
        if(StringUtils.isNotEmpty(connectOid)){
            criteria.andFormOidEqualTo(connectOid);
        }
        if(StringUtils.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        return dbSxFormInfoMapper.selectByExample(example);
    }

    private List<DbSxFormInfo> getDbSxFormInfo(String connectOid,String serviceOid,String fieldTypeOid){
        DbSxFormInfoExample example = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        if(StringUtils.isNotEmpty(connectOid)){
            criteria.andFormOidEqualTo(connectOid);
        }
        if(StringUtils.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        return dbSxFormInfoMapper.selectByExample(example);
    }

    /***
     * 创建表存储
     *
     * @return: void
     * @author: yuy
     * @date: 2021/8/3
     */
    public void saveOrUpdateCreateTable(String url, String userName, String password, List<String> list) throws Exception {
        url = "jdbc:mysql://172.168.250.1:3307/a";
        userName = "cpyz";
        password = "cpyz";
        SqlSessionFactory sqlSessionFactory = DataSourceUtil.getSqlSessionFactory(url, userName, password);
        SqlSession sqlsession = sqlSessionFactory.openSession();
        DbSxFormInfoMapper mapper = sqlsession.getMapper(DbSxFormInfoMapper.class);
        list.add("col_1 varchar(20)");
        list.add("col_2 TIMESTAMP");
        list.add("col_3 INTEGER");
        int num = mapper.isTableExist("t_table2");
        if (num > 0) {
            mapper.dropTable("t_table2");
        }
        mapper.createTableResult("t_table2", list);
        sqlsession.close();
    }

    /***
     * 创建表存储
     *
     * @return: void
     * @author: yuy
     * @date: 2021/8/3
     */
    public String createTableSqlByParams(FormTableDto formTableDto) {
        if (formTableDto == null || StringUtil.isEmpty(formTableDto.getTableName())
             || formTableDto.getColumnList().size() <= 0) {
            return null;
        }
        String sql = "CREATE TABLE " + formTableDto.getTableName() + "("
                + "ID BIGINT NOT NULL COMMENT '主键', "
                + "MODIFY_DATE DATETIME COMMENT '修改时间', ";
        for (FormTableDto dto : formTableDto.getColumnList()) {
            if ("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())) {
                continue;
            }
            if ("VARCHAR".equals(dto.getColumnType())) {
                sql += dto.getColumnName() + " " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") COMMENT '" + dto.getDemo() + "', ";
            } else {
                sql += dto.getColumnName() + " " + dto.getColumnType() + " COMMENT '" + dto.getDemo() + "', ";
            }
        }
        sql += " PRIMARY KEY (ID));";
        return sql;
    }

    public List<SxFormInfo> getDesignFormfoList(String serviceOid) {
        DbSxFormInfoExample example = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andIsAbleEqualTo(1);
        criteria.andStatusEqualTo(5);
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andDesignOidIsNotNull();
        List<DbSxFormInfo> dbSxFormInfos = dbSxFormInfoMapper.selectByExample(example);
        List<SxFormInfo> sxFormInfos = dbSxFormInfos.stream().map(dbSxFormInfo -> {
            SxFormInfo sxFormInfo = new SxFormInfo();
            BeanUtils.copyProperties(dbSxFormInfo,sxFormInfo);
            if(StrUtil.isNotEmpty(sxFormInfo.getFieldTypeOid())) {
                SxFieldType sxFieldType = sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(sxFormInfo.getFieldTypeOid());
                sxFormInfo.setFormName(null == sxFieldType ? sxFormInfo.getFormName() : sxFieldType.getFieldTypeName());
            }
            return sxFormInfo;
        }).collect(Collectors.toList());
        return sxFormInfos;
    }

    public List<SxFormInfo> queryFormInfoExist(String serviceOid, String fieldTypeOid) {
        List<DbSxFormInfo> dbSxFormInfos = this.queryDbSxFormInfo(null, serviceOid, fieldTypeOid);
        List<SxFormInfo> sxFormInfoList = dbSxFormInfos.stream().map(dbSxFormInfo -> {
           SxFormInfo sxFormInfo = new SxFormInfo();
           BeanUtils.copyProperties(dbSxFormInfo,sxFormInfo);
           return  sxFormInfo;
        }).collect(Collectors.toList());
        return sxFormInfoList;
    }

    public void updateDesignChildName(String oid, String childFormName) {
        List<DbSxFormInfo> dbSxFormInfos = this.getDbSxFormInfo(oid, null, null);
        if(dbSxFormInfos.isEmpty()){
            throw new ResultInfoException("找不到表单数据");
        }
        DbSxFormInfo dbSxFormInfo = dbSxFormInfos.get(0);
        if(!childFormName.isEmpty()){
            dbSxFormInfo.setChildFormName(childFormName);
        }
        dbSxFormInfoMapper.updateByPrimaryKeySelective(dbSxFormInfo);
    }

    public SxFormInfo getSxFormInfoByDesignOid(String designOid) {
        DbSxFormInfo dbSxFormInfo =  dbSxFormInfoMapper.getSxFormInfoByDesignOid(designOid);
        SxFormInfo sxFormInfo = null;
        if(dbSxFormInfo !=null){
            sxFormInfo =  new SxFormInfo();
            BeanUtils.copyProperties(dbSxFormInfo,sxFormInfo);
        }
        return sxFormInfo;
    }

    public List<SxFormInfo> getSxFormInfoList(SxFormInfo sxFormInfo) {
        DbSxFormInfoExample example = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        if(sxFormInfo!=null){
            if(StrUtil.isNotEmpty(sxFormInfo.getServiceOid())){
                criteria.andServiceOidEqualTo(sxFormInfo.getServiceOid());
            }
            if(StrUtil.isNotEmpty(sxFormInfo.getFieldTypeOid())){
                criteria.andFieldTypeOidEqualTo(sxFormInfo.getFieldTypeOid());
            }
            if(sxFormInfo.getIsAble()!=0){
                criteria.andIsAbleEqualTo(sxFormInfo.getIsAble());
            }
            if(StrUtil.isNotEmpty(sxFormInfo.getFormOid())){
                criteria.andFormOidEqualTo(sxFormInfo.getFormOid());
            }
            //此处id用于启禁用处的查询使用，勿动
            if(sxFormInfo.getId()!=null){
                criteria.andIdNotEqualTo(sxFormInfo.getId());
            }
        }
        List<DbSxFormInfo> dbSxFormInfos = dbSxFormInfoMapper.selectByExample(example);
        List<SxFormInfo> sxFormInfos = dbSxFormInfos.stream().map(dbSxFormInfo -> {
            SxFormInfo sxFormInfoNew = new SxFormInfo();
            BeanUtils.copyProperties(dbSxFormInfo,sxFormInfoNew);
            return sxFormInfoNew;
        }).collect(Collectors.toList());
        return sxFormInfos;
    }

    public List<SxFormInfo> getDesignFormListByServiceOidAndTypeOid(String serviceOid,String typeOid) {
        DbSxFormInfoExample example = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andIsAbleEqualTo(1);
        criteria.andStatusEqualTo(5);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StrUtil.isNotEmpty(typeOid)){
            criteria.andFieldTypeOidEqualTo(typeOid);
        }
        criteria.andDesignOidIsNotNull();
        List<DbSxFormInfo> dbSxFormInfos = dbSxFormInfoMapper.selectByExample(example);
        List<SxFormInfo> sxFormInfos = dbSxFormInfos.stream().map(dbSxFormInfo -> {
            SxFormInfo sxFormInfo = new SxFormInfo();
            BeanUtils.copyProperties(dbSxFormInfo,sxFormInfo);
            return sxFormInfo;
        }).collect(Collectors.toList());
        return sxFormInfos;
    }

    public List<ZcFormInfo> getZcFormInfoList(String serviceOid, String caseOid, String valOids) {
        List<SxFormInfo> sxFormInfos = getDesignFormfoList(serviceOid);
        List<ZcFormInfo> zcFormInfos = new ArrayList<>();
        for(SxFormInfo sxFormInfo : sxFormInfos) {
            ZcFormInfo zcFormInfo = new ZcFormInfo();
            BeanUtils.copyProperties(sxFormInfo, zcFormInfo);
            zcFormInfo.setCaseOid(caseOid);
            // 需要展示的字段
            zcFormInfo.setFormNames(getFormNames(serviceOid, sxFormInfo.getFieldTypeOid(), valOids, sxFormInfo.getFormOid()));
            // 字段预填值
            zcFormInfo.setFormVals(getFormVals(serviceOid, zcFormInfo.getFieldTypeOid(), valOids, zcFormInfo.getFormOid()));
            zcFormInfos.add(zcFormInfo);
        }
        return zcFormInfos;
    }

    private Map<String, Object> getFormVals(String serviceOid, String fieldTypeOid, String valOids, String formOid) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<SxFillField> sxFillFields = sxFillFieldManager.queryBasicFieldList(serviceOid, fieldTypeOid, formOid);
        //
        List<SxOptionFieldVal> sxOptionFieldVals = sxOptionFieldValManager.getFieldValList(serviceOid);
        Map<String, String> relMap = new HashMap<>();
        for(SxOptionFieldVal fieldVal : sxOptionFieldVals) {
            relMap.put(fieldVal.getOid(), fieldVal.getValOids());
        }
        List<String> fieldOidList = new ArrayList<>();
        for(SxFillField sxFillField : sxFillFields) {
            fieldOidList.add(sxFillField.getFieldOid());
        }
        List<SxOptionFieldVal> fieldValsList = sxOptionFieldValManager.queryFieldFillList(serviceOid, fieldOidList);

        Map<String, List<SxOptionFieldVal>> fieldValMap = fieldValsList.stream().collect(Collectors.groupingBy(SxOptionFieldVal::getFieldOid));

        for(SxFillField sxFillField : sxFillFields) {
            List<SxOptionFieldVal> fieldValList = fieldValMap.get(sxFillField.getFieldOid());
            if(null == fieldValList || fieldValList.size() == 0) {
                continue;
            }
            String join = changeToJavaFiled(sxFillField.getFieldCode());
            for(SxOptionFieldVal valRel : fieldValList) {
                boolean flag = BusinessUtil.checkSelectValAndRel(valOids, relMap.get(valRel.getOid()));
                if (flag) {
                    //多选
                    if (valRel.getValInfo().contains(",")) {
                        String[] strings = valRel.getValInfo().split(",");
                        List arrList = new ArrayList();
                        for (String str : strings) {
                            String[] val = str.split(":");
                            if(val[2].equals("boolean")){
                                arrList.add(Boolean.valueOf(val[1]));
                            }else if(val[2].equals("number")){
                                arrList.add(Integer.valueOf(val[1]));
                            }else{
                                arrList.add(val[1]);
                            }
                        }
                        map.put(join, arrList);
                    }else{//单选
                        String[] valu= valRel.getValInfo().split(":");
                        List arrList=new ArrayList();
                        //判断是否为多选
                        if(valRel.getControlType().equals("checkbox")){
                            if(valu[2].equals("boolean")){
                                arrList.add(Boolean.valueOf(valu[1]));
                            }else if(valu[2].equals("number")){
                                arrList.add(Integer.valueOf(valu[1]));
                            }else{
                                arrList.add(valu[1]);
                            }
                            /**
                             * 多个单选对应同一个填充字段，解决覆盖问题
                             */
                            if(map.containsKey(join)){
                                ArrayList arrayList2= (ArrayList) map.get(join);
                                for (Object o : arrayList2) {
                                    arrList.add(o);
                                }
                            }
                            map.put(join, arrList);
                        }else{
                            if(valu[2].equals("boolean")){
                                map.put(join, Boolean.valueOf(valu[1]));
                            }else if(valu[2].equals("number")){
                                map.put(join, Integer.valueOf(valu[1]));
                            }else{
                                map.put(join, valu[1]);
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    private String getFormNames(String serviceOid, String fieldTypeOid, String valOids, String formOid) {
        StringBuffer result = new StringBuffer();
        // 查询情形对应的表单字段
        DbSxOptionFieldExample optionFieldExample = new DbSxOptionFieldExample();
        DbSxOptionFieldExample.Criteria criteria = optionFieldExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        criteria.andDelFlagEqualTo(0);
        List<DbSxOptionField> dbSxOptionFields = dbSxOptionFieldMapper.selectByExample(optionFieldExample);
        // 表单所有字段
        List<SxFillField> sxFillFields = sxFillFieldManager.queryFieldListNew(serviceOid, fieldTypeOid, formOid);
        // 未配置情形关联关系，展示所有字段
        if(null == dbSxOptionFields || dbSxOptionFields.size() == 0) {
            for(SxFillField sxFillField : sxFillFields) {
                String join = changeToJavaFiled(sxFillField.getFieldCode());
                result.append(join).append(",");
            }
        } else {
            // 配置了情形，未选择情形时，先查询情形字段配置，如果有配置，此时去掉配置中包含的字段
            if(StrUtil.isEmpty(valOids)) {
                // 待去掉的字段
                Map<String, List<DbSxOptionField>> groupMap = dbSxOptionFields.stream().collect(Collectors.groupingBy(DbSxOptionField :: getFieldOid));
                // 剩下需要展示的
                List<SxFillField> sxFillFieldList = new ArrayList<>();
                for(SxFillField fillField : sxFillFields) {
                    for(String str : groupMap.keySet()) {
                        if(!fillField.getFieldOid().equals(str)) {
                            sxFillFieldList.add(fillField);
                        }
                    }
                }
                Map<String, String> relMap = new HashMap<>();
                for(DbSxOptionField dbSxOptionField : dbSxOptionFields) {
                    relMap.put(dbSxOptionField.getFieldOid(), dbSxOptionField.getValOid());
                }
                for(SxFillField sxFillField : sxFillFieldList) {
                    if(null == relMap.get(sxFillField.getFieldOid())) {
                        continue;
                    }
                    boolean flag = BusinessUtil.checkSelectValAndRel(valOids, relMap.get(sxFillField.getFieldOid()));
                    if(flag){
                        String join = changeToJavaFiled(sxFillField.getFieldCode());
                        result.append(join).append(",");
                    }
                }
            } else {
                /**
                 * 2022-11-11  赵冰峰修改
                 * 原先逻辑是根据已经选择的标题，删除掉选择的标题的所有情形中不是当前选择情形绑定的表单字段
                 * 例如    1-是否有法定代表人   是   否
                 *        2-是否已经办理酒类   是   否
                 *        3-选择更改的事项     姓名   法定代表人   股权
                 * 这样的逻辑在实际业务中会出现以下两个问题
                 * 1） 选择多选框  3-姓名和法定代表人时，这两个情形所关联的表单字段会取交集，（同一标题下结果两个循环会删除掉不同部分）
                 * 2） 只选择 1.2 选项时，3关联的字段也会展示（根据提交的情形的标题去做的删除半段）

                // 选择了情形，展示情形对应的字段
                String[] arr = valOids.split(",");
                // 将要去掉的字段
                List<DbSxOptionField> allFieldList = new ArrayList<>();
                for(String oid : arr) {
                    SxServiceOptionVal optionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(oid);
                    if(null == optionVal) {
                        continue;
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("serviceOid", serviceOid);
                    params.put("titleOid", optionVal.getTitleOid());
                    List<DbSxOptionField> sxOptionFields = dbSxOptionFieldMapper.getOptionFieldList(params);
                    for(DbSxOptionField optionField : sxOptionFields) {
                        //多选框时会出现问题
                        if(!optionField.getValOid().equals(oid)) {
                            allFieldList.add(optionField);
                        }
                    }
                }
                Map<String, List<DbSxOptionField>> map = allFieldList.stream().collect(Collectors.groupingBy(DbSxOptionField :: getFieldOid));
                // 剩下的
                List<SxFillField> sxFillFieldList = new ArrayList<>();
                if(allFieldList.size() == 0) {
                    sxFillFieldList = sxFillFields;
                } else {
                    for(SxFillField fillField : sxFillFields) {
                        if(null == map.get(fillField.getFieldOid())) {
                            sxFillFieldList.add(fillField);
                        }
                    }
                }
                for(SxFillField sxFillField : sxFillFieldList) {
                    String join = changeToJavaFiled(sxFillField.getFieldCode());
                    result.append(join).append(",");
                }
                 */
                /**
                 * 新逻辑是选出所有的公共字段+选择情形的配置字段
                 */
                // 待去掉的字段 所有的配置的情形字段
                Map<String, List<DbSxOptionField>> groupMap = dbSxOptionFields.stream().collect(Collectors.groupingBy(DbSxOptionField :: getFieldOid));
                // 剩下需要展示的（所有公共字段）
                Set<SxFillField> sxFillFieldList = new HashSet<>();
                for(SxFillField fillField : sxFillFields) {
                    if(!groupMap.containsKey(fillField.getFieldOid())) {
                        sxFillFieldList.add(fillField);
                    }

                }
                // 选择了情形，展示情形对应的字段
                String[] arr = valOids.split(",");
                for(String oid : arr) {
                    Map<String, String> params = new HashMap<>();
                    params.put("serviceOid", serviceOid);
                    params.put("valOid", oid);
                    List<DbSxOptionField> optionFieldList = dbSxOptionFieldMapper.getOptionFieldList(params);
                    for(DbSxOptionField optionField : optionFieldList) {
                        sxFillFieldList.add(sxFillFieldManager.getSxFillFieldByOid(
                                optionField.getFieldOid()))  ;
                    }
                }
                for(SxFillField sxFillField : sxFillFieldList) {
                    String join = changeToJavaFiled(sxFillField.getFieldCode());
                    result.append(join).append(",");
                }

            }
        }
        if(result.length() > 0){
            return result.substring(0, result.length() - 1);
        }
        return null;
    }

    private String changeToJavaFiled(String fieldCode) {
        if (!fieldCode.contains("_")){
            return fieldCode.toLowerCase();
        }
        String[] fields = fieldCode.toLowerCase().split("_");
        StringBuilder sbuilder = new StringBuilder(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            char[] cs = fields[i].toCharArray();
            cs[0] -= 32;
            sbuilder.append(String.valueOf(cs));
        }
        return sbuilder.toString();
    }

}

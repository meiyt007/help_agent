package com.zfsoft.single.manager.yxpz;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.sxService.service.RefinedMaterialService;
import com.zfsoft.service.sxService.service.SxFillFieldService;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.single.data.yxpz.FormFieldRelConfig;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.data.yxpz.vo.CascaderTreeVo;
import com.zfsoft.single.dbaccess.dao.DbFormFieldRelConfigMapper;
import com.zfsoft.single.dbaccess.data.DbFormFieldRelConfig;
import com.zfsoft.single.dbaccess.data.DbFormFieldRelConfigExample;
import com.zfsoft.single.manager.sxpz.SxMaterialElmsConfigManager;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.data.clzs.*;
import com.zfsoft.superwindow.service.clzs.*;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dongxl
 * @create: 2020-10-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FormFieldRelConfigManager {

    private final DbFormFieldRelConfigMapper dbFormFieldRelConfigMapper;

    private final SxFillFieldService sxFillFieldFeginService;

    private final BasicFormFieldService basicFormFieldFeginService;

    private final SxServiceMaterialService sxServiceMaterialFeginService;

    private final SxMaterialElmsConfigManager sxMaterialElmsConfigManager;

    private final ElectronicLicenseService electronicLicenseFeginService;

    private final RefinedMaterialService refinedMaterialFeginService;

    private final MaterialCatalogElementService materialCatalogElementFeginService;

    private final MaterialCatalogService materialCatalogFeginService;

    private final InterApiService interApiFeginService;


    public PageResult<FormFieldRelConfig> queryPageList(FormFieldRelConfig formFieldRelConfig, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        DbFormFieldRelConfigExample example=new DbFormFieldRelConfigExample();
        DbFormFieldRelConfigExample.Criteria criteria=example.createCriteria();
        if(formFieldRelConfig!=null){
            if(StrUtil.isNotEmpty(formFieldRelConfig.getServiceOid())){
                criteria.andServiceOidEqualTo(formFieldRelConfig.getServiceOid());
            }
            if (formFieldRelConfig.getFillType() !=null) {
                criteria.andFillTypeEqualTo(formFieldRelConfig.getFillType());
            }
        }
        criteria.andDelFlagEqualTo(0);
        example.setOrderByClause(" CREATE_DATE desc");
        PageHelper.startPage(pageNum,pageSize);
        Page<DbFormFieldRelConfig> page= (Page<DbFormFieldRelConfig>)dbFormFieldRelConfigMapper.selectByExample(example);
        PageResult<FormFieldRelConfig> pageResult=new PageResult<>(page.getPageNum(),page.getPageSize(),page.getTotal());
        List<FormFieldRelConfig> list=page.stream().map(field->{
            FormFieldRelConfig relNew=new FormFieldRelConfig();
            BeanUtils.copyProperties(field,relNew);

            if(StrUtil.isNotEmpty(relNew.getInterApiId())){
                String interApiText;
                ApiResultSet<InterApi> apiResultSet = interApiFeginService.getInterApi(Long.valueOf(relNew.getInterApiId()));
                if(apiResultSet.getData() != null){
                    interApiText = apiResultSet.getData().getName();
                    relNew.setInterApiText(interApiText);
                }
            }

            if(StrUtil.isNotEmpty(relNew.getInterApiValId())){
                String interApiValText;
                ApiResultSet<InterApiResponse> interApiReo = interApiFeginService.getInterApiReo(Long.valueOf(relNew.getInterApiValId()));
                if(interApiReo.getData() != null){
                    interApiValText = interApiReo.getData().getResponseName();
                    relNew.setInterApiValText(interApiValText);
                }
            }

            if (field.getFillType() !=null && field.getFillType() ==1) {  //电子表单字段
                ApiResultSet<SxFillField> sxfill=sxFillFieldFeginService.getDbSxFillFieldByOid(field.getFillFieldOid());
                if(sxfill!=null && sxfill.getData()!=null){
                    relNew.setFillFieldName(sxfill.getData().getFieldName()+"【"+sxfill.getData().getFieldCode()+"】");
                }
            } else if (field.getFillType() !=null && field.getFillType() ==0){   //基础表单字段
                ApiResultSet<BaseFormField> basicForm= basicFormFieldFeginService.getOneByOid(field.getFillFieldOid());
                if(basicForm!=null && basicForm.getData()!=null){
                    relNew.setFillFieldName(basicForm.getData().getFieldName()+"【"+basicForm.getData().getFieldKey()+"】");
                }
            }

            if(StrUtil.isNotEmpty(relNew.getBasicFormFieldOid())){
                relNew.setFillSource(0);
               ApiResultSet<BaseFormField> basicForm= basicFormFieldFeginService.getOneByOid(relNew.getBasicFormFieldOid());
               if(basicForm!=null && basicForm.getData()!=null){
                   relNew.setTemFieldName(basicForm.getData().getFieldName()+"【"+basicForm.getData().getFieldKey()+"】");
               }
            }else if(StrUtil.isNotEmpty(relNew.getInterApiId())){
                relNew.setFillSource(1);
            }else if(StrUtil.isNotEmpty(relNew.getOcrFieldOid())){
                String[] fieldArray = relNew.getOcrFieldOid().split(",");
                if (fieldArray !=null && fieldArray.length >2) {
                    String refindOid = fieldArray[1].split(";")[0];  //细化材料id
                    String refindMaterialName = "";
                    ApiResultSet<RefinedMaterial> refinedMaterialApiResultSet = refinedMaterialFeginService.getRefinedMaterialByOid(refindOid);
                    if (refinedMaterialApiResultSet.getData() !=null) {
                        refindMaterialName = refinedMaterialApiResultSet.getData().getRefinedMaterialName();
                    }
                    String cataElement= fieldArray[2];  //字段元素信息
                    if (StringUtils.isNotEmpty(cataElement)) {
                        String elementCode = cataElement.split(";")[1];
                        String elementName = cataElement.split(";")[2];
                        if (StringUtils.isNotEmpty(refindMaterialName)) {
                            relNew.setTemFieldName(refindMaterialName + "->" + elementName+"【"+elementCode+"】");
                        } else {
                            relNew.setTemFieldName(elementName+"【"+elementCode+"】");
                        }
                    }
                }
                relNew.setFillSource(2);
            }
            return relNew;
        }).collect(Collectors.toList());
        pageResult.setData(list);
        return pageResult;
    }


    /**
     * 保存或者更新信息
     * @param formFieldRelConfig
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdate(FormFieldRelConfig formFieldRelConfig) {
        if(formFieldRelConfig!=null){
            //基础表单字段
            if(formFieldRelConfig.getFillSource()==0){
                formFieldRelConfig.setBasicFormFieldOid(formFieldRelConfig.getTempFieldOid());
                formFieldRelConfig.setElecLiecenseFieldOid(null);
                formFieldRelConfig.setInterApiId(null);
                formFieldRelConfig.setInterApiValId(null);
                formFieldRelConfig.setOcrFieldOid(null);
            }else if(formFieldRelConfig.getFillSource()==1){
                formFieldRelConfig.setElecLiecenseFieldOid(formFieldRelConfig.getLicenceType());
                formFieldRelConfig.setOcrFieldOid(null);
                formFieldRelConfig.setBasicFormFieldOid(null);
            }
            if(StrUtil.isNotEmpty(formFieldRelConfig.getOid())){
               DbFormFieldRelConfig fieldRel= dbFormFieldRelConfigMapper.selectOneByOid(formFieldRelConfig.getOid());
               if(fieldRel!=null){
                   fieldRel.setElecLiecenseFieldOid(formFieldRelConfig.getElecLiecenseFieldOid());
                   fieldRel.setBasicFormFieldOid(formFieldRelConfig.getBasicFormFieldOid());
                   fieldRel.setFillFieldOid(formFieldRelConfig.getFillFieldOid());
                   fieldRel.setInterApiValId(formFieldRelConfig.getInterApiValId());
                   fieldRel.setInterApiId(formFieldRelConfig.getInterApiId());
                   fieldRel.setOcrFieldOid(formFieldRelConfig.getOcrFieldOid());
                   fieldRel.setFillFieldOid(formFieldRelConfig.getFillFieldOid());
                   fieldRel.setModifyDate(new Date());
                   dbFormFieldRelConfigMapper.updateByPrimaryKey(fieldRel);
               }
            }else{
                formFieldRelConfig.setOid(UUIDUtil.randomUUID().replaceAll("-",""));
                formFieldRelConfig.setDelFlag(0);
                formFieldRelConfig.setModifyDate(new Date());
                formFieldRelConfig.setCreateDate(new Date());
                DbFormFieldRelConfig rel=new DbFormFieldRelConfig();
                BeanUtils.copyProperties(formFieldRelConfig,rel);
                dbFormFieldRelConfigMapper.insert(rel);
            }
        }
        return null;
    }

    /**
     * 根据主键获取单个查询信息
     * @param id
     * @return
     */
    public FormFieldRelConfig getOneRelConfig(Long id) {
        if(id!=null){
           DbFormFieldRelConfig fieldRel= dbFormFieldRelConfigMapper.selectByPrimaryKey(id);
           if(fieldRel!=null){
               FormFieldRelConfig rel=new FormFieldRelConfig();
               BeanUtils.copyProperties(fieldRel,rel);
               if(StrUtil.isNotEmpty(rel.getBasicFormFieldOid())){
                   rel.setFillSource(0);
                   rel.setTempFieldOid(rel.getBasicFormFieldOid());
               }else if(StrUtil.isNotEmpty(rel.getInterApiId())){
                   rel.setFillSource(1);
                   rel.setLicenceType(rel.getElecLiecenseFieldOid());
               }
               return rel;
           }
        }
        return null;
    }

    /**
     * 根据业务主键查询配置信息
     * @param oid
     * @return
     */
    public FormFieldRelConfig selectOneByOid(String oid) {
        if(StrUtil.isNotEmpty(oid)){
            DbFormFieldRelConfig fieldRel= dbFormFieldRelConfigMapper.selectOneByOid(oid);
            if(fieldRel!=null){
                FormFieldRelConfig rel=new FormFieldRelConfig();
                BeanUtils.copyProperties(fieldRel,rel);
                return rel;
            }
        }
        return null;
    }

    /**
     * 根据主键删除信息
     * @param id
     */
    public void delRelConfig(Long id){
      DbFormFieldRelConfig relConfig=  dbFormFieldRelConfigMapper.selectByPrimaryKey(id);
      if(relConfig!=null){
          relConfig.setDelFlag(1);
          relConfig.setModifyDate(new Date());
          dbFormFieldRelConfigMapper.updateByPrimaryKey(relConfig);
      }
    }

    /**
     * 证照目录和照面元素树
     * @param serviceOid
     * @return
     */
    public List<CascaderTreeVo> queryElecAndElementTree(String serviceOid) {
        //查询所有的事项材料
       ApiResultSet<List<SxServiceMaterial>> list= sxServiceMaterialFeginService.getSxServiceMaterialListByServiceOid(serviceOid);
       if(list!=null && list.getData()!=null && list.getData().size()>0){
          List<String> materialOids= list.getData().stream().map(sxServiceMaterial -> sxServiceMaterial.getMaterialOid()).collect(Collectors.toList());
          //查询所有目录配置信息
          List<SxMaterialElmsConfig> configList= sxMaterialElmsConfigManager.getElecConfigByMaterialOids(materialOids);
          if(configList!=null && configList.size()>0){
              List<String> billOids= configList.stream().map(eleConfig->eleConfig.getBillOid()).distinct().collect(Collectors.toList());
              //根据目录查询目录配置和目录元素
             ApiResultSet<List<ElectronicLicense>> billList= electronicLicenseFeginService.queryElectronicLicenseListByBillOids(billOids);
             if(billList!=null &&billList.getData()!=null){
                 List<String>  listEleCOid= billList.getData().stream().map(license->license.getOid()).distinct().collect(Collectors.toList());
              //根据主键信息查询所有的元素信息
                ApiResultSet<List<ElectronicLicenseElement>> elementList= electronicLicenseFeginService.queryElectronicElementListByBillOids(listEleCOid);
                if(elementList!=null && elementList.getData()!=null){
                  List<CascaderTreeVo> listVo= elementList.getData().stream().map(elec->{
                        CascaderTreeVo vo=new CascaderTreeVo();
                        vo.setValue(elec.getOid());
                        vo.setParentOid(elec.getElectronicLicenseInterfaceOid());
                        vo.setLabel(elec.getElectronicLicenseElementName()+"【"+elec.getElectronicLicenseElementCode()+"】");
                        return vo;
                    }).collect(Collectors.toList());
                    Map<String, List<CascaderTreeVo>> collectMap = listVo.stream().collect(Collectors.groupingBy(CascaderTreeVo::getParentOid));
                    Iterator<ElectronicLicense> iterator = billList.getData().iterator();
                    List<CascaderTreeVo> result = new ArrayList<>();
                    while (iterator.hasNext()){
                        ElectronicLicense license = iterator.next();
                        List<CascaderTreeVo> fieldTrees = collectMap.get(license.getOid());
                        if(fieldTrees==null){
                            iterator.remove();
                            continue;
                        }
                        CascaderTreeVo fieldTree = new CascaderTreeVo();
                        fieldTree.setLabel(license.getElectronicLicenseName());
                        fieldTree.setValue(license.getOid());
                        fieldTree.setChildren(fieldTrees);
                        result.add(fieldTree);
                    }
                    return result;
                }
             }
          }
       }
        return null;
    }

    /**
     * 查询事项下面关联的细化材料配置的目录和目录元素
     * @param serviceOid
     * @return
     */
    public List<CascaderTreeVo> queryCatalogAndCataLogElementTree(String serviceOid) {
        //查询所有的事项材料
        ApiResultSet<List<RefinedMaterial>> list= refinedMaterialFeginService.getOnlyRefinedMaterialByServiceOid(serviceOid);
        if(list!=null && list.getData()!=null && list.getData().size()>0){
           List<String> catalogOids= list.getData().stream().filter(refinedMaterial -> StrUtil.isNotEmpty(refinedMaterial.getMaterialCatalogOid()))
                    .collect(Collectors.toList()).stream().map(refined->refined.getMaterialCatalogOid()).distinct().collect(Collectors.toList());
            //查询所有目录配置的元素信息
            if(catalogOids!=null && catalogOids.size()>0){
                //查询所有的智审目录信息
               ApiResultSet<List<MaterialCatalog>> billList= materialCatalogFeginService.queryAllCatalogByCatalogOidsList(catalogOids);
               if(billList!=null && billList.getData()!=null){
                   //查询所有的目录元素信息
                   ApiResultSet<List<MaterialCatalogElement>> elementList= materialCatalogElementFeginService.queryMaterialCatalogElementByCatalogOids(catalogOids);
                   if(elementList!=null && elementList.getData()!=null){
                       List<CascaderTreeVo> listVo= elementList.getData().stream().map(elec->{
                           CascaderTreeVo vo=new CascaderTreeVo();
                           vo.setValue(elec.getMaterialCatalogElementOid());
                           vo.setParentOid(elec.getMaterialCatalogOid());
                           vo.setLabel(elec.getElementName()+"【"+elec.getElementCode()+"】");
                           return vo;
                       }).collect(Collectors.toList());
                       Map<String, List<CascaderTreeVo>> collectMap = listVo.stream().collect(Collectors.groupingBy(CascaderTreeVo::getParentOid));
                       Iterator<MaterialCatalog> iterator = billList.getData().iterator();
                       List<CascaderTreeVo> result = new ArrayList<>();
                       while (iterator.hasNext()){
                           MaterialCatalog license = iterator.next();
                           List<CascaderTreeVo> fieldTrees = collectMap.get(license.getMaterialCatalogOid());
                           if(fieldTrees==null){
                               iterator.remove();
                               continue;
                           }
                           CascaderTreeVo fieldTree = new CascaderTreeVo();
                           fieldTree.setLabel(license.getCatalogName());
                           fieldTree.setValue(license.getMaterialCatalogOid());
                           fieldTree.setChildren(fieldTrees);
                           result.add(fieldTree);
                       }
                       return result;
                   }
               }
            }
        }
        return null;
    }

    /**
     *  查看有没有重复添加被填充的字段
     * @param serviceOid
     * @param fillType
     * @param fillFieldOid
     * @param oid
     * @return
     */
    public String checkHasRepeat(String serviceOid, Integer fillType, String fillFieldOid, String oid) {
        String result = "false";
        DbFormFieldRelConfigExample dbFormFieldRelConfigExample = new DbFormFieldRelConfigExample();
        DbFormFieldRelConfigExample.Criteria criteria = dbFormFieldRelConfigExample.createCriteria();
        if (StringUtils.isNotEmpty(serviceOid)) {
            criteria.andServiceOidEqualTo(serviceOid);
        }
        if (fillType !=null) {
            criteria.andFillTypeEqualTo(fillType);
        }
        if (StringUtils.isNotEmpty(fillFieldOid)) {
            criteria.andFillFieldOidEqualTo(fillFieldOid);
        }
        criteria.andDelFlagEqualTo(0);
        List<DbFormFieldRelConfig> dbFormFieldRelConfigs = dbFormFieldRelConfigMapper.selectByExample(dbFormFieldRelConfigExample);

        if (StringUtils.isEmpty(oid)) {
            if (dbFormFieldRelConfigs !=null && dbFormFieldRelConfigs.size()>0) {
                result = "true";
            }
        } else {
            if (dbFormFieldRelConfigs !=null && dbFormFieldRelConfigs.size()>0) {
                DbFormFieldRelConfig dbFormFieldRelConfig = dbFormFieldRelConfigs.get(0);
                if (!dbFormFieldRelConfig.getOid().equals(oid)) {
                    result = "true";
                }
            }
        }
        return result;
    }

    public List<DbFormFieldRelConfig> getFormFieldRelConfigByType(String serviceOid, Integer fillType) {
        DbFormFieldRelConfigExample dbFormFieldRelConfigExample = new DbFormFieldRelConfigExample();
        DbFormFieldRelConfigExample.Criteria criteria = dbFormFieldRelConfigExample.createCriteria();
        if (StringUtils.isNotEmpty(serviceOid)) {
            criteria.andServiceOidEqualTo(serviceOid);
        }
        if (fillType !=null) {
            criteria.andFillTypeEqualTo(fillType);
        }
        criteria.andDelFlagEqualTo(0);
        List<DbFormFieldRelConfig> dbFormFieldRelConfigs = dbFormFieldRelConfigMapper.selectByExample(dbFormFieldRelConfigExample);
        return dbFormFieldRelConfigs;
    }

    /**
     *  查询基础表单字段是否被关联
     * @param basicFieldOid
     * @return
     */
    public List<DbFormFieldRelConfig> getFormFieldRelConfigByBasicFieldOid(String basicFieldOid) {
        DbFormFieldRelConfigExample dbFormFieldRelConfigExample = new DbFormFieldRelConfigExample();
        DbFormFieldRelConfigExample.Criteria criteria = dbFormFieldRelConfigExample.createCriteria();
        if (StringUtils.isNotEmpty(basicFieldOid)) {
            criteria.andBasicFormFieldOidEqualTo(basicFieldOid);
        }
        criteria.andDelFlagEqualTo(0);
        List<DbFormFieldRelConfig> dbFormFieldRelConfigs = dbFormFieldRelConfigMapper.selectByExample(dbFormFieldRelConfigExample);
        if(dbFormFieldRelConfigs ==null || dbFormFieldRelConfigs.size()==0) {
            dbFormFieldRelConfigExample = new DbFormFieldRelConfigExample();
            criteria = dbFormFieldRelConfigExample.createCriteria();
            if (StringUtils.isNotEmpty(basicFieldOid)) {
                criteria.andFillFieldOidEqualTo(basicFieldOid);
                criteria.andDelFlagEqualTo(0);
                dbFormFieldRelConfigs = dbFormFieldRelConfigMapper.selectByExample(dbFormFieldRelConfigExample);
            }
        }
        return dbFormFieldRelConfigs;
    }

    public void copyFormFieldRelConfig(Map map) {
        //map.get(0)被复制的serviceOid
        //map.get(1)新的serviceOid
        DbFormFieldRelConfig formFieldRelConfig = new DbFormFieldRelConfig();
        String serviceOidOld = map.get("serviceOidOld").toString();
        formFieldRelConfig.setServiceOid(serviceOidOld);
        formFieldRelConfig.setDelFlag(0);
        List<DbFormFieldRelConfig> list = dbFormFieldRelConfigMapper.selectByServiceOid(formFieldRelConfig);
        list.forEach(dbFormFieldRelConfig -> {
            if(dbFormFieldRelConfig.getServiceOid().equals(serviceOidOld)) {
                DbFormFieldRelConfig dbFormFieldRelConfigCopy = new DbFormFieldRelConfig();
                BeanUtils.copyProperties(dbFormFieldRelConfig, dbFormFieldRelConfigCopy);
                dbFormFieldRelConfigCopy.setServiceOid(map.get("serviceOidNew").toString());
                dbFormFieldRelConfigCopy.setId(null);
                if(map.get(dbFormFieldRelConfig.getFillFieldOid())!=null){
                    dbFormFieldRelConfigCopy.setFillFieldOid(map.get(dbFormFieldRelConfig.getFillFieldOid()).toString());
                    dbFormFieldRelConfigCopy.setOid(IdUtil.simpleUUID());
                    dbFormFieldRelConfigCopy.setModifyDate(new Date());
                    dbFormFieldRelConfigCopy.setCreateDate(new Date());
                    dbFormFieldRelConfigCopy.setDelFlag(0);
                    dbFormFieldRelConfigMapper.insertSelective(dbFormFieldRelConfigCopy);
                }
            }
        });

    }

}

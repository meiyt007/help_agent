package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMaterialMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxMaterialFormTempMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxMaterialFormTemp;
import com.zfsoft.service.dbaccess.data.sxService.DbSxMaterialFormTempExample;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceMaterialServiceImpl
 * @Description: 实施清单-申请材料 实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:sxServiceMaterial")
public class SxServiceMaterialManager {

    @Resource
    private DbSxServiceMaterialMapper dbSxServiceMaterialMapper;

    @Resource
    private SxSysAttaManager sxSysAttaManager;

    @Resource
    private DbSxMaterialFormTempMapper dbSxMaterialFormTempMapper;

   // @Cacheable(key = "'getSxServiceMaterialByServiceOid:serviceOid=' + #serviceOid", unless = "#result == null")
    public List<SxServiceMaterial> getSxServiceMaterialByServiceOid(String serviceOid) {
        DbSxServiceMaterialExample dbSxServiceMaterialExample = new DbSxServiceMaterialExample();
        DbSxServiceMaterialExample.Criteria criteria = dbSxServiceMaterialExample.createCriteria();
        if (StrUtil.isNotEmpty(serviceOid)) {
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDelFlagEqualTo((short) 0);
        List<DbSxServiceMaterial> dbSxServiceMaterials = dbSxServiceMaterialMapper.selectByExample(dbSxServiceMaterialExample);
        List<SxServiceMaterial> sxServiceMaterialList = dbSxServiceMaterials.stream().map(sxServiceMaterial -> {
            SxServiceMaterial serviceMaterial = new SxServiceMaterial();
            BeanUtils.copyProperties(sxServiceMaterial, serviceMaterial);
            return serviceMaterial;
        }).collect(Collectors.toList());
        return sxServiceMaterialList;
    }

    //@Cacheable(key = "'getSxServiceMaterialByOid:oid=' + #oid", unless = "#result == null")
    public SxServiceMaterial getSxServiceMaterialByOid(String oid) {
        Assert.hasLength(oid, "材料主键不能为空！");
        DbSxServiceMaterial dbSxServiceMaterial = dbSxServiceMaterialMapper.getSxServiceMaterialByOid(oid);
        if (null == dbSxServiceMaterial) {
            return null;
        }
        SxServiceMaterial sxServiceMaterial = new SxServiceMaterial();
        BeanUtils.copyProperties(dbSxServiceMaterial, sxServiceMaterial);
        return sxServiceMaterial;
    }

    public SxServiceMaterial getSxServiceMaterialHasFileByOid(String oid) {
        Assert.hasLength(oid, "材料主键不能为空！");
        DbSxServiceMaterial dbSxServiceMaterial = dbSxServiceMaterialMapper.getSxServiceMaterialByOid(oid);
        if (null == dbSxServiceMaterial) {
            return null;
        }
        SxServiceMaterial sxServiceMaterial = new SxServiceMaterial();
        BeanUtils.copyProperties(dbSxServiceMaterial, sxServiceMaterial);
        if(StringUtils.isNotEmpty(sxServiceMaterial.getMaterialSampleAddr())){
            List<SxServiceMaterialSampleAtta> sampleAttaList = new ArrayList<>();
            if(sxServiceMaterial.getMaterialSampleAddr().length() > 32){
                String[] atta = sxServiceMaterial.getMaterialSampleAddr().split(",");
                for(String a : atta){
                    if(StringUtils.isNotEmpty(a)){
                        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(a);
                        SxServiceMaterialSampleAtta sampleAtta = new SxServiceMaterialSampleAtta();
                        sampleAtta.setMaterialSampleOid(sxSysAtta.getOid());
                        sampleAtta.setMaterialSampleAddr(sxSysAtta.getFilePath());
                        sampleAtta.setMaterialSampleName(sxSysAtta.getOriginName());
                        sampleAttaList.add(sampleAtta);
                    }
                }
            }else{
                SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getMaterialSampleAddr());
                SxServiceMaterialSampleAtta sampleAtta = new SxServiceMaterialSampleAtta();
                sampleAtta.setMaterialSampleOid(sxSysAtta.getOid());
                sampleAtta.setMaterialSampleAddr(sxSysAtta.getFilePath());
                sampleAtta.setMaterialSampleName(sxSysAtta.getOriginName());
                sampleAttaList.add(sampleAtta);
            }
            sxServiceMaterial.setSxServiceMaterialSampleAttaList(sampleAttaList);
        }

        if(StringUtils.isNotEmpty(sxServiceMaterial.getElectronicFormAddr())){
            List<SxServiceMaterialElectronicAtta> electronicAttaList = new ArrayList<>();
            if(sxServiceMaterial.getElectronicFormAddr().length() > 32){
                String[] atta = sxServiceMaterial.getElectronicFormAddr().split(",");
                for(String a : atta){
                    if(StringUtils.isNotEmpty(a)){
                        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(a);
                        SxServiceMaterialElectronicAtta electronicAtta = new SxServiceMaterialElectronicAtta();
                        electronicAtta.setElectronicFormOid(sxSysAtta.getOid());
                        electronicAtta.setElectronicFormAddr(sxSysAtta.getFilePath());
                        electronicAtta.setElectronicFormName(sxSysAtta.getOriginName());
                        electronicAttaList.add(electronicAtta);
                    }
                }
            }else{
                SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getElectronicFormAddr());
                SxServiceMaterialElectronicAtta electronicAtta = new SxServiceMaterialElectronicAtta();
                electronicAtta.setElectronicFormOid(sxSysAtta.getOid());
                electronicAtta.setElectronicFormAddr(sxSysAtta.getFilePath());
                electronicAtta.setElectronicFormName(sxSysAtta.getOriginName());
                electronicAttaList.add(electronicAtta);
            }
            sxServiceMaterial.setSxServiceMaterialElectronicAttaList(electronicAttaList);
        }

        if(StringUtils.isNotEmpty(sxServiceMaterial.getMaterialEmptyAddr())){
            List<SxServiceMaterialEmptyAtta> emptyAttaList = new ArrayList<>();
            if(sxServiceMaterial.getMaterialEmptyAddr().length() > 32){
                String[] atta = sxServiceMaterial.getMaterialEmptyAddr().split(",");
                for(String a : atta){
                    if(StringUtils.isNotEmpty(a)){
                        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(a);
                        SxServiceMaterialEmptyAtta emptyAtta = new SxServiceMaterialEmptyAtta();
                        emptyAtta.setMaterialEmptyOid(sxSysAtta.getOid());
                        emptyAtta.setMaterialEmptyAddr(sxSysAtta.getFilePath());
                        emptyAtta.setMaterialEmptyName(sxSysAtta.getOriginName());
                        emptyAttaList.add(emptyAtta);
                    }
                }
            }else{
                SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                SxServiceMaterialEmptyAtta emptyAtta = new SxServiceMaterialEmptyAtta();
                emptyAtta.setMaterialEmptyOid(sxSysAtta.getOid());
                emptyAtta.setMaterialEmptyAddr(sxSysAtta.getFilePath());
                emptyAtta.setMaterialEmptyName(sxSysAtta.getOriginName());
                emptyAttaList.add(emptyAtta);
            }
            sxServiceMaterial.setSxServiceMaterialEmptyAttaList(emptyAttaList);
        }
        return sxServiceMaterial;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateByMaterialOid(String param) {
        if (StringUtils.isEmpty(param)) {
            log.info("据所属事项主健更新实施清单材料信息失败：{}，请求参数为空！", param);
            return;
        }
        JSONObject paramJson = JSONObject.fromObject(param);
        //材料主键集合
        JSONArray materialOids = paramJson.getJSONArray("materialOids");
        //材料目录集合 - 待更新 字段
        JSONArray materialCatalogOids = paramJson.getJSONArray("materialCatalogOids");

        if (!(ObjectUtils.isEmpty(materialOids) && ObjectUtils.isEmpty(materialCatalogOids))) {
            for (int i = 0; i < materialCatalogOids.size(); i++) {
                //按顺序更新 materialOids，materialCatalogOids 俩集合数据按顺序对应
                String materialOid = materialOids.get(i).toString();
                String materialCatalogOid = materialCatalogOids.get(i).toString();
                DbSxServiceMaterial dbSxServiceMaterial = new DbSxServiceMaterial();
                dbSxServiceMaterial.setMaterialCatalogOid(materialCatalogOid);
                dbSxServiceMaterial.setMaterialOid(materialOid);

                dbSxServiceMaterialMapper.updateByMaterialOid(dbSxServiceMaterial);


            }
        }

    }

    //@Cacheable(key = "'getSxServiceMaterialListByMaterialCatalogOid:materialCatalogOid=' + #materialCatalogOid", unless = "#result == null")
    public List<SxServiceMaterial> getSxServiceMaterialListByMaterialCatalogOid(String materialCatalogOid) {
        List<DbSxServiceMaterial> dbSxServiceMaterials = dbSxServiceMaterialMapper.getSxServiceMaterialListByMaterialCatalogOid(materialCatalogOid);
        List<SxServiceMaterial> sxServiceMaterialList = dbSxServiceMaterials.stream().map(sxServiceMaterial -> {
            SxServiceMaterial serviceMaterial = new SxServiceMaterial();
            BeanUtils.copyProperties(sxServiceMaterial, serviceMaterial);
            return serviceMaterial;
        }).collect(Collectors.toList());
        return sxServiceMaterialList;
    }


    public void updateByMaterial(SxServiceMaterial sxServiceMaterial) {
        if (null!=sxServiceMaterial) {
            log.info("据所属事项主健更新实施清单材料信息失败：{}，请求参数为空！", sxServiceMaterial);
            return;
        }
        String materialOid = sxServiceMaterial.getMaterialOid();
        String baiduTemplateIds = sxServiceMaterial.getBaiduTemplateIds();
        DbSxServiceMaterial dbSxServiceMaterial = new DbSxServiceMaterial();
        dbSxServiceMaterial.setBaiduTemplateIds(baiduTemplateIds);
        dbSxServiceMaterial.setMaterialOid(materialOid);
        dbSxServiceMaterialMapper.updateByMaterialOid(dbSxServiceMaterial);
    }

    public PageResult<SxServiceMaterial>  getSxServiceMaterialPageListByServiceOid(Integer pageNumber, Integer pageSize, String searchName,String serviceOid) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        DbSxServiceMaterialExample dbSxServiceMaterialExample = new DbSxServiceMaterialExample();
        DbSxServiceMaterialExample.Criteria criteria = dbSxServiceMaterialExample.createCriteria();
        dbSxServiceMaterialExample.setOrderByClause(" MATERIAL_SORT  ASC ");
        criteria.andDelFlagEqualTo((short) 0);
        if(StringUtils.isNotEmpty(searchName)){
            criteria.andMaterialNameLike(searchName);
        }
        if(StringUtils.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        PageHelper.startPage(pageNumber,pageSize);
        Page<DbSxServiceMaterial> dbSxServiceMaterials = (Page<DbSxServiceMaterial>) dbSxServiceMaterialMapper.selectByExample(dbSxServiceMaterialExample);
        PageResult<SxServiceMaterial> pageResult = new PageResult<>(dbSxServiceMaterials.getPageNum(),dbSxServiceMaterials.getPageSize(),dbSxServiceMaterials.getTotal());
        List<SxServiceMaterial> sxServiceMaterialList = dbSxServiceMaterials.stream().map(sxServiceMaterial -> {
            SxServiceMaterial serviceMaterial = new SxServiceMaterial();
            BeanUtils.copyProperties(sxServiceMaterial, serviceMaterial);
            if(StringUtils.isNotEmpty(serviceOid)) {
                Map<String, String> params = new HashMap<>(2);
                params.put("serviceOid", serviceOid);
                params.put("materialOid", serviceMaterial.getMaterialOid());
                DbSxMaterialFormTemp materialFormTemp = dbSxMaterialFormTempMapper.getOneMaterialFormTemplate(params);
                if(null != materialFormTemp) {
                    serviceMaterial.setTemplateName(materialFormTemp.getTemplateName());
                }
            }
            return serviceMaterial;
        }).collect(Collectors.toList());
        pageResult.setData(sxServiceMaterialList);
        return pageResult;
    }

    public int  insertSxServiceMaterialService(SxServiceMaterial sxServiceMaterial) {
        DbSxServiceMaterialWithBLOBs record  = new DbSxServiceMaterialWithBLOBs();
        BeanUtils.copyProperties(sxServiceMaterial, record);
        //MATERIAL_OID
        record.setMaterialOid(UUID.randomUUID().toString().replaceAll("-", ""));
        record.setDelFlag((short)0);
        record.setCreateDate(new Date());
        String  userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        record.setCreateUser(userOid);
        record.setModifyDate(new Date());
        dbSxServiceMaterialMapper.insertSelective(record);
        log.info("material-------"+ record.getMaterialOid());
        log.info("id-------"+ record.getId());
        return 1;
    }

    public int  updateSxServiceMaterialService(SxServiceMaterial sxServiceMaterial) {
        //DbSxServiceMaterial dbSxServiceMaterial = dbSxServiceMaterialMapper.getSxServiceMaterialByOid(sxServiceMaterial.getMaterialOid());
        DbSxServiceMaterialWithBLOBs record  = new DbSxServiceMaterialWithBLOBs();
        BeanUtils.copyProperties(sxServiceMaterial, record);
        record.setModifyDate(new Date());
        return dbSxServiceMaterialMapper.updateByPrimaryKeySelective(record);
    }

    public int delSxServiceMaterialByOid(String materialOid) {
        DbSxServiceMaterialWithBLOBs dbSxServiceMaterialWithBLOBs = dbSxServiceMaterialMapper.getSxServiceMaterialByOid(materialOid);
        dbSxServiceMaterialWithBLOBs.setDelFlag((short)1);
        return dbSxServiceMaterialMapper.updateByPrimaryKeySelective(dbSxServiceMaterialWithBLOBs);
    }

    public int saveMaterialFormTemplate(SxMaterialFormTemp sxMaterialFormTemp) {
        if(null == sxMaterialFormTemp) {
            throw new ResultInfoException("保存对象不能为空");
        }
        if(StrUtil.isEmpty(sxMaterialFormTemp.getServiceOid())) {
            throw new ResultInfoException("事项主键不能为空");
        }
        if(StrUtil.isEmpty(sxMaterialFormTemp.getMaterialOid())) {
            throw new ResultInfoException("材料主键不能为空");
        }
        DbSxMaterialFormTempExample formTempExample = new DbSxMaterialFormTempExample();
        DbSxMaterialFormTempExample.Criteria criteria = formTempExample.createCriteria();
        criteria.andServiceOidEqualTo(sxMaterialFormTemp.getServiceOid());
        criteria.andMaterialOidEqualTo(sxMaterialFormTemp.getMaterialOid());
        criteria.andDelFlagEqualTo(0);
        List<DbSxMaterialFormTemp> list = dbSxMaterialFormTempMapper.selectByExample(formTempExample);
        if(null != list && list.size() != 0) {
            for(DbSxMaterialFormTemp dbSxMaterialFormTemp : list) {
                dbSxMaterialFormTemp.setDelFlag(1);
                dbSxMaterialFormTemp.setModifyDate(new Date());
                dbSxMaterialFormTempMapper.updateByPrimaryKeySelective(dbSxMaterialFormTemp);
            }
        }
        DbSxMaterialFormTemp dbSxMaterialFormTemp = new DbSxMaterialFormTemp();
        BeanUtils.copyProperties(sxMaterialFormTemp, dbSxMaterialFormTemp);
        dbSxMaterialFormTemp.setOid(IdUtil.simpleUUID());
        dbSxMaterialFormTemp.setDelFlag(0);
        dbSxMaterialFormTemp.setCreateDate(new Date());
        dbSxMaterialFormTempMapper.insert(dbSxMaterialFormTemp);
        return 1;
    }

    public SxMaterialFormTemp getMaterialFormTemplate(String sxServiceOid, String materialOid) {
        DbSxMaterialFormTempExample formTempExample = new DbSxMaterialFormTempExample();
        DbSxMaterialFormTempExample.Criteria criteria = formTempExample.createCriteria();
        if(StrUtil.isNotEmpty(sxServiceOid)) {
            criteria.andServiceOidEqualTo(sxServiceOid);
        }
        if(StrUtil.isNotEmpty(materialOid)) {
            criteria.andMaterialOidEqualTo(materialOid);
        }
        criteria.andDelFlagEqualTo(0);
        List<DbSxMaterialFormTemp> list = dbSxMaterialFormTempMapper.selectByExample(formTempExample);
        if(null != list && list.size() != 0) {
            DbSxMaterialFormTemp dbSxMaterialFormTemp = list.get(0);
            if(StringUtil.isNotEmpty(dbSxMaterialFormTemp.getFormCode()) || StringUtil.isNotEmpty(dbSxMaterialFormTemp.getTemplateOid())){
                SxMaterialFormTemp sxMaterialFormTemp = new SxMaterialFormTemp();
                BeanUtils.copyProperties(dbSxMaterialFormTemp, sxMaterialFormTemp);
                return sxMaterialFormTemp;
            }

        }
        return null;
    }

    public List<SxMaterialFormTemp> getServiceFormTemplateList(String sxServiceOid) {
        if(StrUtil.isEmpty(sxServiceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        DbSxMaterialFormTempExample formTempExample = new DbSxMaterialFormTempExample();
        DbSxMaterialFormTempExample.Criteria criteria = formTempExample.createCriteria();
        criteria.andServiceOidEqualTo(sxServiceOid);
        criteria.andDelFlagEqualTo(0);
        List<DbSxMaterialFormTemp> list = dbSxMaterialFormTempMapper.selectByExample(formTempExample);
        List<SxMaterialFormTemp> sxMaterialFormTemps = list.stream().map(dbSxMaterialFormTemp -> {
            SxMaterialFormTemp sxMaterialFormTemp = new SxMaterialFormTemp();
            BeanUtils.copyProperties(dbSxMaterialFormTemp, sxMaterialFormTemp);
            return sxMaterialFormTemp;
        }).collect(Collectors.toList());
        return sxMaterialFormTemps;
    }
}

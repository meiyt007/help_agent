package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbServiceMaterialMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbServiceMaterial;
import com.zfsoft.service.dbaccess.data.sxSituation.DbServiceMaterialExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 事项颗粒材料 实现类
 * @date 2020/11/3 10:04
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class ServiceMaterialManager {

    @Resource
    private DbServiceMaterialMapper dbServiceMaterialMapper;

    public ServiceMaterial getServiceMaterialByOid(String oid){
        DbServiceMaterial dbServiceMaterial = dbServiceMaterialMapper.selectByOid(oid);
        ServiceMaterial serviceMaterial = null;
        if (null == dbServiceMaterial) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询事项颗粒材料表单为空！");
        }else{
            serviceMaterial = new ServiceMaterial();
            BeanUtils.copyProperties(dbServiceMaterial,serviceMaterial);
        }
        return serviceMaterial;
    }

    public List<ServiceMaterial> getServiceMaterialsByServiceOid(String serviceOid) {
        DbServiceMaterialExample dbServiceMaterialExample = new DbServiceMaterialExample();
        DbServiceMaterialExample.Criteria criteria = dbServiceMaterialExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short)0);
        List<DbServiceMaterial> dbServiceMaterial = dbServiceMaterialMapper.selectByExample(dbServiceMaterialExample);
        List<ServiceMaterial> serviceMaterials = dbServiceMaterial.stream().map(serviceMaterial -> {
            ServiceMaterial sMaterial = new ServiceMaterial();
            BeanUtils.copyProperties(serviceMaterial,sMaterial);
            return sMaterial;
        }).collect(Collectors.toList());
        return serviceMaterials;
    }


    /**
     * 分页查询颗粒材料列表
     * @param serviceMaterial
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<ServiceMaterial> queryServiceMaterialWithPage(ServiceMaterial serviceMaterial , Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbServiceMaterialExample dbServiceMaterialExample = new DbServiceMaterialExample();
        DbServiceMaterialExample.Criteria criteria = dbServiceMaterialExample.createCriteria();
        if(null!=serviceMaterial){
            if(StrUtil.isNotEmpty(serviceMaterial.getMaterialName())){
                criteria.andMaterialNameLike("%"+serviceMaterial.getMaterialName()+"%");
            }
            if(StrUtil.isNotEmpty(serviceMaterial.getServiceOid())){
                criteria.andServiceOidEqualTo(serviceMaterial.getServiceOid());
            }

        }
        criteria.andDeleteStatusEqualTo((short)0);
        Page<DbServiceMaterial> dbServiceMaterials = (Page<DbServiceMaterial>)dbServiceMaterialMapper.selectByExample(dbServiceMaterialExample);
        PageResult<ServiceMaterial> pageResult = new PageResult<>(dbServiceMaterials.getPageNum(),dbServiceMaterials.getPageSize(),dbServiceMaterials.getTotal());
        List<ServiceMaterial> serviceMaterialList = dbServiceMaterials.stream().map(dbServiceMaterial -> {
            ServiceMaterial material=new ServiceMaterial();
            BeanUtils.copyProperties(dbServiceMaterial,material);
            return material;
        }).collect(Collectors.toList());
        pageResult.setData(serviceMaterialList);
        return pageResult;
    }



/*
    public void updateByServiceMaterial(ServiceMaterial serviceMaterial) {
        if (null!=serviceMaterial) {
            log.info("据所属事项主健更新实施清单材料信息失败：{}，请求参数为空！", serviceMaterial);
            return;
        }
        String oid = serviceMaterial.getOid();
        String baiduTemplateIds = serviceMaterial.getBaiduTemplateIds();
        DbServiceMaterial dbServiceMaterial=new DbServiceMaterial();
        dbServiceMaterial.setBaiduTemplateIds(baiduTemplateIds);
        dbServiceMaterial.setOid(oid);
        dbServiceMaterialMapper.updateByPrimaryKey(dbServiceMaterial);

    }
*/


    public void updateServiceMaterial(ServiceMaterial serviceMaterial){
        DbServiceMaterial dbServiceMateria;
        if (null != serviceMaterial.getId()) {
            dbServiceMateria=this.dbServiceMaterialMapper.selectByPrimaryKey(serviceMaterial.getId());
            Assert.notNull(dbServiceMateria, MessageFormat.format("更新对象不存在！对象id为{0}", dbServiceMateria.getId()));
            BeanUtils.copyProperties(serviceMaterial, dbServiceMateria);
            dbServiceMateria.setModifyDate(new Date());
            this.dbServiceMaterialMapper.updateByPrimaryKeySelectiveNew(dbServiceMateria);
        }

    }

}

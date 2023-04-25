package com.zfsoft.superwindow.manager.clzs;

import com.zfsoft.superwindow.data.clzs.MaterialCatalogRelation;
import com.zfsoft.superwindow.dbaccess.dao.DbMaterialCatalogRelationMapper;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalogRelation;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: 目录关联记录实现类
 * @Author: liangss
 * @Date: 2021/1/25 14:24
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialCatalogRelationManager {

    @Resource
    private DbMaterialCatalogRelationMapper dbMaterialCatalogRelationMapper;

    public String saveOrUpdate(MaterialCatalogRelation materialCatalogRelation) throws Exception {
        if (null!=materialCatalogRelation.getId()) {
            DbMaterialCatalogRelation dbMaterialCatalogRelation=this.dbMaterialCatalogRelationMapper.selectByPrimaryKey(materialCatalogRelation.getId());
            Assert.notNull(materialCatalogRelation, MessageFormat.format("更新对象不存在！对象id为{0}", materialCatalogRelation.getId()));
            BeanUtils.copyProperties(materialCatalogRelation, dbMaterialCatalogRelation);
            this.dbMaterialCatalogRelationMapper.updateByPrimaryKeySelective(dbMaterialCatalogRelation);
        } else {
            DbMaterialCatalogRelation dbMaterialCatalogRelation=new DbMaterialCatalogRelation();
            BeanUtils.copyProperties(materialCatalogRelation, dbMaterialCatalogRelation);
            dbMaterialCatalogRelation.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbMaterialCatalogRelation.setCreateDate(new Date());
            dbMaterialCatalogRelation.setModifyDate(new Date());
            dbMaterialCatalogRelation.setMaterialCatalogRelationOid(UUIDUtil.randomUUID());
            this.dbMaterialCatalogRelationMapper.insert(dbMaterialCatalogRelation);
        }
        return null;
    }


    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCatalogRelation dbMaterialCatalogRelation=this.dbMaterialCatalogRelationMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbMaterialCatalogRelation, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbMaterialCatalogRelation.setDelFlag(dbMaterialCatalogRelation.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbMaterialCatalogRelationMapper.updateByPrimaryKeySelective(dbMaterialCatalogRelation);
    }

    public MaterialCatalogRelation getMaterialCatalogRelationByOid(String materialCatalogRelationOid) {
        Assert.hasLength(materialCatalogRelationOid, "业务主键不能为空！");
        DbMaterialCatalogRelation dbMaterialCatalogRelation=this.dbMaterialCatalogRelationMapper.getMaterialCatalogRelationByOid(materialCatalogRelationOid);
        MaterialCatalogRelation materialCatalogRelation = new MaterialCatalogRelation();
        BeanUtils.copyProperties(dbMaterialCatalogRelation,materialCatalogRelation);
        return materialCatalogRelation;
    }


    public List<MaterialCatalogRelation> queryList(String materialCatalogOid) {
        List<DbMaterialCatalogRelation> dbMaterialCatalogRelationList=this.dbMaterialCatalogRelationMapper.queryListByMaterialCatalogOid(materialCatalogOid);
        return BeanUtils.copyListProperties(dbMaterialCatalogRelationList, MaterialCatalogRelation::new);
    }


}

package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.MaterialCategory;
import com.zfsoft.superwindow.dbaccess.dao.DbMaterialCategoryMapper;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCategory;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCategoryExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName MaterialCategoryManager
 * @Description: 材料智审实现类
 * @Author liangss
 * @Date 2020-11-03 10:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialCategoryManager {
    //材料类别
    @Resource
    private DbMaterialCategoryMapper dbMaterialCategoryMapper;

    /**
     * 分页查询列表
     * @param materialCategory
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<MaterialCategory> queryMaterialCategoryWithPage
            (MaterialCategory materialCategory, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbMaterialCategoryExample dbMaterialCategoryExample=new DbMaterialCategoryExample();
        dbMaterialCategoryExample.setOrderByClause(" CREATE_DATE DESC ");
        DbMaterialCategoryExample.Criteria criteria=dbMaterialCategoryExample.createCriteria();
        if(null!=materialCategory){
            if(null!=materialCategory.getCategoryName()){
                criteria.andCategoryNameLike("%"+materialCategory.getCategoryName().trim()+"%");
            }
            if(null!=materialCategory.getCategoryCode()){
                criteria.andCategoryCodeEqualTo(materialCategory.getCategoryCode());
            }

        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbMaterialCategory> dbMaterialCategories = (Page<DbMaterialCategory>)dbMaterialCategoryMapper.selectByExample(dbMaterialCategoryExample);
        PageResult<MaterialCategory> pageResult = new PageResult<>(dbMaterialCategories.getPageNum(),dbMaterialCategories.getPageSize(),dbMaterialCategories.getTotal());
        List<MaterialCategory> materialCategoryList = dbMaterialCategories.stream().map(dbMaterialCategory -> {
            MaterialCategory materialCategory1 = new MaterialCategory();
            BeanUtils.copyProperties(dbMaterialCategory,materialCategory1);
            return materialCategory1;
        }).collect(Collectors.toList());
        pageResult.setData(materialCategoryList);
        return pageResult;
    }

    /**
     * 查询列表
     * @param materialCategory
     * @return
     */
    public List<MaterialCategory> queryList(MaterialCategory materialCategory) {
        DbMaterialCategoryExample dbMaterialCategoryExample=new DbMaterialCategoryExample();
        DbMaterialCategoryExample.Criteria criteria=dbMaterialCategoryExample.createCriteria();
        if(null!=materialCategory){
            if(null!=materialCategory.getCategoryName()){
                criteria.andCategoryNameLike("%"+materialCategory.getCategoryName().trim()+"%");
            }
            if(null!=materialCategory.getCategoryCode()){
                criteria.andCategoryCodeEqualTo(materialCategory.getCategoryCode());
            }

        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCategory>  dbMaterialCategoryList=this.dbMaterialCategoryMapper.selectByExample(dbMaterialCategoryExample);
        return BeanUtils.copyListProperties(dbMaterialCategoryList, MaterialCategory::new);
    }

    public void saveOrUpdate(MaterialCategory materialCategory) {
        if (null!=materialCategory.getId()) {
            DbMaterialCategory dbMaterialCategory=this.dbMaterialCategoryMapper.selectByPrimaryKey(materialCategory.getId());
            Assert.notNull(dbMaterialCategory, MessageFormat.format("更新对象不存在！对象id为{0}", dbMaterialCategory.getId()));
            BeanUtils.copyProperties(materialCategory, dbMaterialCategory);
            dbMaterialCategory.setModifyDate(new Date());
            this.dbMaterialCategoryMapper.updateByPrimaryKeySelective(dbMaterialCategory);
        } else {
            DbMaterialCategory dbMaterialCategory=new DbMaterialCategory();
            BeanUtils.copyProperties(materialCategory, dbMaterialCategory);
            dbMaterialCategory.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbMaterialCategory.setCreateDate(new Date());
            dbMaterialCategory.setModifyDate(new Date());
            dbMaterialCategory.setMaterialCategoryOid(UUIDUtil.randomUUID());
            this.dbMaterialCategoryMapper.insert(dbMaterialCategory);

        }

    }

    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public MaterialCategory getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCategory dbMaterialCategory=this.dbMaterialCategoryMapper.selectByPrimaryKey(Long.valueOf(id));
        MaterialCategory materialCategory=new MaterialCategory();
        BeanUtils.copyProperties(dbMaterialCategory,materialCategory);
        return materialCategory;
    }


    /**
     * 根据业务主键查询材料类型
     * @param materialCategoryOid
     * @return
     */
    public DbMaterialCategory getMaterialCategoryOid(String materialCategoryOid) {
        Assert.hasLength(materialCategoryOid, "业务主键不能为空！");
        DbMaterialCategoryExample dbMaterialCategoryExample=new DbMaterialCategoryExample();
        DbMaterialCategoryExample.Criteria criteria=dbMaterialCategoryExample.createCriteria();
        criteria.andMaterialCategoryOidEqualTo(materialCategoryOid);
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCategory>  dbMaterialCategoryList=this.dbMaterialCategoryMapper.selectByExample(dbMaterialCategoryExample);
        return CollectionUtils.isEmpty(dbMaterialCategoryList) ? null : dbMaterialCategoryList.get(0);
    }

    /**
     * 根据主键删除登记信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCategory dbMaterialCategory=this.dbMaterialCategoryMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbMaterialCategory, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbMaterialCategory.setDelFlag(dbMaterialCategory.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbMaterialCategoryMapper.updateByPrimaryKeySelective(dbMaterialCategory);


    }


    /**
     * 查询数据
     * @param materialCategory
     * @return
     */
    public Long queryMaterialCategoryCount(MaterialCategory materialCategory) {
        DbMaterialCategoryExample dbMaterialCategoryExample=new DbMaterialCategoryExample();
        DbMaterialCategoryExample.Criteria criteria=dbMaterialCategoryExample.createCriteria();
        if(null!=materialCategory){
            if(null!=materialCategory.getCategoryName()){
                criteria.andCategoryNameLike("%"+materialCategory.getCategoryName().trim()+"%");
            }
            if(null!=materialCategory.getCategoryCode()){
                criteria.andCategoryCodeEqualTo(materialCategory.getCategoryCode());
            }

        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);

        return Long.valueOf(this.dbMaterialCategoryMapper.countByExample(dbMaterialCategoryExample));
    }

    /**
     * @description 判断重复数据
     * @param materialCategory
     * @return java.util.List<com.zfsoft.data.clzs.MaterialCategory>
     * @author chenjm
     * @date 2021/4/7 13:44
     **/
    public List<MaterialCategory> getMaterialCategoryList(MaterialCategory materialCategory){
        DbMaterialCategoryExample dbMaterialCategoryExample=new DbMaterialCategoryExample();
        if(null!=materialCategory){
            if(null!=materialCategory.getCategoryName()){
                DbMaterialCategoryExample.Criteria criteria1=dbMaterialCategoryExample.createCriteria();
                criteria1.andCategoryNameEqualTo(materialCategory.getCategoryName().trim());
                criteria1.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
                dbMaterialCategoryExample.or(criteria1);
            }
            if(null!=materialCategory.getCategoryCode()){
                DbMaterialCategoryExample.Criteria criteria2=dbMaterialCategoryExample.createCriteria();
                criteria2.andCategoryCodeEqualTo(materialCategory.getCategoryCode());
                criteria2.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
                dbMaterialCategoryExample.or(criteria2);
            }
        }
        List<DbMaterialCategory>  dbMaterialCategoryList=this.dbMaterialCategoryMapper.selectByExample(dbMaterialCategoryExample);
        return BeanUtils.copyListProperties(dbMaterialCategoryList, MaterialCategory::new);
    }


}

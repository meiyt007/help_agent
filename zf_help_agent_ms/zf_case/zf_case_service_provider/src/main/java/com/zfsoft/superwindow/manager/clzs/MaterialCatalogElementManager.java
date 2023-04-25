package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.superwindow.dbaccess.dao.DbCardCatalogueElementMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbMaterialCatalogElementMapper;
import com.zfsoft.superwindow.dbaccess.data.DbCardCatalogueElement;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalog;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalogElement;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalogElementExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
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
 * @ClassName MaterialCatalogElementManager
 * @Description: 材料目录元素实现类
 * @Author liangss
 * @Date 2020-11-03 16:10:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialCatalogElementManager {
    @Resource
    private DbMaterialCatalogElementMapper dbMaterialCatalogElementMapper;
    @Resource
    private MaterialCatalogManager materialCatalogManager;

    @Resource
    private DbCardCatalogueElementMapper dbCardCatalogueElementMapper;

    /**
     * 分页查询列表
     * @param materialCatalogElement
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<MaterialCatalogElement> queryMaterialCatalogElementWithPage
            (MaterialCatalogElement materialCatalogElement, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);

        DbMaterialCatalogElementExample dbMaterialCatalogElementExample=new DbMaterialCatalogElementExample();
        DbMaterialCatalogElementExample.Criteria criteria=dbMaterialCatalogElementExample.createCriteria();
        if(null!=materialCatalogElement){
            if(StringUtils.isNotEmpty(materialCatalogElement.getMaterialCatalogOid())){
                criteria.andMaterialCatalogOidEqualTo(materialCatalogElement.getMaterialCatalogOid());
            }
            if(StringUtils.isNotEmpty(materialCatalogElement.getMaterialCatalogParentOid())){
                criteria.andMaterialCatalogParentOidEqualTo(materialCatalogElement.getMaterialCatalogParentOid());
            }
            if(StringUtils.isNotEmpty(materialCatalogElement.getElementName())){
                criteria.andElementNameLike("%"+materialCatalogElement.getElementName().trim()+"%");
            }
            if(StringUtils.isNotEmpty(materialCatalogElement.getElementCode())){
                criteria.andElementCodeEqualTo(materialCatalogElement.getElementCode());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbMaterialCatalogElement> dbMaterialCatalogElements = (Page<DbMaterialCatalogElement>)dbMaterialCatalogElementMapper.selectByExample(dbMaterialCatalogElementExample);
        PageResult<MaterialCatalogElement> pageResult = new PageResult<>(dbMaterialCatalogElements.getPageNum(),dbMaterialCatalogElements.getPageSize(),dbMaterialCatalogElements.getTotal());
        List<MaterialCatalogElement> materialCatalogElementList = dbMaterialCatalogElements.stream().map(dbMaterialCatalogElement -> {
            MaterialCatalogElement materialCatalogElement1 = new MaterialCatalogElement();
            BeanUtils.copyProperties(dbMaterialCatalogElement,materialCatalogElement1);
            return materialCatalogElement1;
        }).collect(Collectors.toList());
        pageResult.setData(materialCatalogElementList);
        return pageResult;
    }

    /**
     * 查询列表
     * @param materialCatalogElement
     * @return
     */
    public List<MaterialCatalogElement> queryList(MaterialCatalogElement materialCatalogElement) {
        DbMaterialCatalogElementExample dbMaterialCatalogElementExample=new DbMaterialCatalogElementExample();
        DbMaterialCatalogElementExample.Criteria criteria=dbMaterialCatalogElementExample.createCriteria();
        if(null!=materialCatalogElement){
            if(null!=materialCatalogElement.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(materialCatalogElement.getMaterialCatalogOid());
            }
            if(null!=materialCatalogElement.getMaterialCatalogParentOid()){
                criteria.andMaterialCatalogParentOidEqualTo(materialCatalogElement.getMaterialCatalogParentOid());
            }
            if(null!=materialCatalogElement.getElementName()){
                criteria.andElementNameLike("%"+materialCatalogElement.getElementName().trim()+"%");
            }
            if(null!=materialCatalogElement.getElementCode()){
                criteria.andElementCodeEqualTo(materialCatalogElement.getElementCode());
            }
            if (StringUtils.isNotEmpty(materialCatalogElement.getMaterialCatalogElementOid())) {
                criteria.andMaterialCatalogElementOidEqualTo(materialCatalogElement.getMaterialCatalogElementOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalogElement>  dbMaterialCatalogElementList=this.dbMaterialCatalogElementMapper.selectByExample(dbMaterialCatalogElementExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogElementList, MaterialCatalogElement::new);
    }

    /**
     * 保存更新信息
     * @param materialCatalogElement
     */
    public void saveOrUpdate(MaterialCatalogElement materialCatalogElement) {
        if (null!=materialCatalogElement.getId()) {
            DbMaterialCatalogElement dbMaterialCatalogElement=this.dbMaterialCatalogElementMapper.selectByPrimaryKey(materialCatalogElement.getId());
            Assert.notNull(dbMaterialCatalogElement, MessageFormat.format("更新对象不存在！对象id为{0}", dbMaterialCatalogElement.getId()));
            BeanUtils.copyProperties(materialCatalogElement, dbMaterialCatalogElement);
            dbMaterialCatalogElement.setModifyDate(new Date());
            String cardCatalogueElementOid=dbMaterialCatalogElement.getCardCatalogueElementOid();
            if(StringUtils.isNotEmpty(cardCatalogueElementOid)){
                DbCardCatalogueElement dbCardCatalogueElement=dbCardCatalogueElementMapper.getCardCatalogueElementByOid(cardCatalogueElementOid);
                if(null!=dbCardCatalogueElement){
                    String cardCatalogueElementCode=dbCardCatalogueElement.getCardCatalogueElementCode();
                    String cardCatalogueElementName=dbCardCatalogueElement.getCardCatalogueElementName();
                    dbMaterialCatalogElement.setCardCatalogueElementCode(cardCatalogueElementCode);
                    dbMaterialCatalogElement.setCardCatalogueElementName(cardCatalogueElementName);
                }
            }
            this.dbMaterialCatalogElementMapper.updateByPrimaryKeySelective(dbMaterialCatalogElement);
        } else {
            DbMaterialCatalogElement dbMaterialCatalogElement=new DbMaterialCatalogElement();
            BeanUtils.copyProperties(materialCatalogElement, dbMaterialCatalogElement);
            createMetadataCode(dbMaterialCatalogElement);
            dbMaterialCatalogElement.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbMaterialCatalogElement.setCreateDate(new Date());
            dbMaterialCatalogElement.setModifyDate(new Date());
            dbMaterialCatalogElement.setMaterialCatalogElementOid(UUIDUtil.randomUUID());
            String cardCatalogueElementOid=dbMaterialCatalogElement.getCardCatalogueElementOid();
            if(StringUtils.isNotEmpty(cardCatalogueElementOid)){
                DbCardCatalogueElement dbCardCatalogueElement=dbCardCatalogueElementMapper.getCardCatalogueElementByOid(cardCatalogueElementOid);
                if(null!=dbCardCatalogueElement){
                    String cardCatalogueElementCode=dbCardCatalogueElement.getCardCatalogueElementCode();
                    String cardCatalogueElementName=dbCardCatalogueElement.getCardCatalogueElementName();
                    dbMaterialCatalogElement.setCardCatalogueElementCode(cardCatalogueElementCode);
                    dbMaterialCatalogElement.setCardCatalogueElementName(cardCatalogueElementName);
                }
            }
            this.dbMaterialCatalogElementMapper.insert(dbMaterialCatalogElement);
        }





    }

    //创建目录元素编码
    private void createMetadataCode(DbMaterialCatalogElement dbMaterialCatalogElement) {
        MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
        BeanUtils.copyProperties(dbMaterialCatalogElement,materialCatalogElement);
        String materialCatalogOid = materialCatalogElement.getMaterialCatalogOid();
        // 获取目录对象信息
        DbMaterialCatalog fmc=this.materialCatalogManager.getMaterialCatalogOid(materialCatalogOid);
        if (fmc != null) {
            // 获取目录编码
            String metadataCode = fmc.getCatalogCode();
            // 获取元素数量
            Long num = queryMaterialCatalogElementCount(materialCatalogElement);
            num += 1;
            String ex = "";
            if(String.valueOf(num).length()==1){
                ex = "00";
            }else if(String.valueOf(num).length()==2){
                ex = "0";
            }
            metadataCode += ex + num;
            dbMaterialCatalogElement.setElementCode(metadataCode);
        }
    }

    /**
     * 查询元素数量根据材料目录
     * @param materialCatalogElement
     * @return
     */
    public Long queryMaterialCatalogElementCount(MaterialCatalogElement materialCatalogElement) {
        DbMaterialCatalogElementExample dbMaterialCatalogElementExample=new DbMaterialCatalogElementExample();
        DbMaterialCatalogElementExample.Criteria criteria=dbMaterialCatalogElementExample.createCriteria();
        if(null!=materialCatalogElement){
            if(null!=materialCatalogElement.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(materialCatalogElement.getMaterialCatalogOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        return Long.valueOf(this.dbMaterialCatalogElementMapper.countByExample(dbMaterialCatalogElementExample));
    }


    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public MaterialCatalogElement getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCatalogElement dbMaterialCatalogElement=this.dbMaterialCatalogElementMapper.selectByPrimaryKey(Long.valueOf(id));
        MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
        BeanUtils.copyProperties(dbMaterialCatalogElement,materialCatalogElement);
        return materialCatalogElement;
    }

    /**
     * 根据主键删除登记信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCatalogElement dbMaterialCatalogElement=this.dbMaterialCatalogElementMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbMaterialCatalogElement, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbMaterialCatalogElement.setDelFlag(dbMaterialCatalogElement.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbMaterialCatalogElementMapper.updateByPrimaryKeySelective(dbMaterialCatalogElement);
    }


    /**
     * 根据条件查询材料目录元素
     * @param materialCatalogElement
     * @return
     */
    public MaterialCatalogElement getOneMaterialCatalogElement(MaterialCatalogElement materialCatalogElement) {
        DbMaterialCatalogElementExample dbMaterialCatalogElementExample=new DbMaterialCatalogElementExample();
        DbMaterialCatalogElementExample.Criteria criteria=dbMaterialCatalogElementExample.createCriteria();
        if(null!=materialCatalogElement){
            if(null!=materialCatalogElement.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(materialCatalogElement.getMaterialCatalogOid());
            }
            if(null!=materialCatalogElement.getMaterialCatalogParentOid()){
                criteria.andMaterialCatalogParentOidEqualTo(materialCatalogElement.getMaterialCatalogParentOid());
            }
            if(null!=materialCatalogElement.getElementName()){
                criteria.andElementNameLike("%"+materialCatalogElement.getElementName().trim()+"%");
            }
            if(null!=materialCatalogElement.getElementCode()){
                criteria.andElementCodeEqualTo(materialCatalogElement.getElementCode());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalogElement>  dbMaterialCatalogElementList=this.dbMaterialCatalogElementMapper.selectByExample(dbMaterialCatalogElementExample);
        DbMaterialCatalogElement dbMaterialCatalogElement= CollectionUtils.isEmpty(dbMaterialCatalogElementList) ? null : dbMaterialCatalogElementList.get(0);
        BeanUtils.copyProperties(dbMaterialCatalogElement,materialCatalogElement);
        return materialCatalogElement;
    }


    /**
     * @description 根据名称详细查找
     * @param materialCatalogElement
     * @return java.util.List<com.zfsoft.data.clzs.MaterialCatalogElement>
     * @author chenjm
     * @date 2021/4/7 15:35
     **/
    public List<MaterialCatalogElement> queryListByName(MaterialCatalogElement materialCatalogElement) {
        DbMaterialCatalogElementExample dbMaterialCatalogElementExample=new DbMaterialCatalogElementExample();
        DbMaterialCatalogElementExample.Criteria criteria=dbMaterialCatalogElementExample.createCriteria();
        if(null!=materialCatalogElement){
            if(null!=materialCatalogElement.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(materialCatalogElement.getMaterialCatalogOid());
            }
            if(null!=materialCatalogElement.getElementName()){
                criteria.andElementNameEqualTo(materialCatalogElement.getElementName().trim());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalogElement>  dbMaterialCatalogElementList=this.dbMaterialCatalogElementMapper.selectByExample(dbMaterialCatalogElementExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogElementList, MaterialCatalogElement::new);
    }

    public List<MaterialCatalogElement> queryMaterialCatalogElementByCatalogOids(List<String> materialCatalogOids) {
        DbMaterialCatalogElementExample dbMaterialCatalogElementExample=new DbMaterialCatalogElementExample();
        DbMaterialCatalogElementExample.Criteria criteria=dbMaterialCatalogElementExample.createCriteria();
        if(materialCatalogOids!=null && materialCatalogOids.size()>0){
            criteria.andMaterialCatalogOidIn(materialCatalogOids);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalogElement>  dbMaterialCatalogElementList=this.dbMaterialCatalogElementMapper.selectByExample(dbMaterialCatalogElementExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogElementList, MaterialCatalogElement::new);
    }
}

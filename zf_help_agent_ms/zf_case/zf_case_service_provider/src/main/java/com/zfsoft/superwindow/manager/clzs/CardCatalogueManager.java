package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.CardCatalogue;
import com.zfsoft.superwindow.data.clzs.CardCatalogueElement;
import com.zfsoft.superwindow.dbaccess.dao.DbCardCatalogueMapper;
import com.zfsoft.superwindow.dbaccess.data.DbCardCatalogue;
import com.zfsoft.superwindow.dbaccess.data.DbCardCatalogueExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 卡证目录
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardCatalogueManager {

    @Resource
    private DbCardCatalogueMapper dbCardCatalogueMapper;
    @Resource
    private CardCatalogueElementManager cardCatalogueElementManager;

    /**
     * 分页查询列表
     * @param catalogName
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<CardCatalogue> queryCardCatalogueWithPage(String catalogName, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbCardCatalogueExample dbCardCatalogueExample=new DbCardCatalogueExample();
        DbCardCatalogueExample.Criteria criteria=dbCardCatalogueExample.createCriteria();
        if(StringUtils.isNotEmpty(catalogName)){
            criteria.andCardCatalogueNameEqualTo(catalogName);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbCardCatalogue> dbCardCatalogues= (Page<DbCardCatalogue>)dbCardCatalogueMapper.selectByExample(dbCardCatalogueExample);
        PageResult<CardCatalogue> pageResult = new PageResult<>(dbCardCatalogues.getPageNum(),dbCardCatalogues.getPageSize(),dbCardCatalogues.getTotal());
        List<CardCatalogue> cardCatalogueList= dbCardCatalogues.stream().map(dbCardCatalogue -> {
            CardCatalogue cardCatalogue1 = new CardCatalogue();
            BeanUtils.copyProperties(dbCardCatalogue,cardCatalogue1);
            return cardCatalogue1;
        }).collect(Collectors.toList());
        pageResult.setData(cardCatalogueList);
        return pageResult;
    }

    /***
    * @Description:  更新保存方法
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [cardCatalogue]
    */
    public void saveOrUpdate(CardCatalogue cardCatalogue) throws Exception {
        DbCardCatalogue dbCardCatalogue;
        if (null != cardCatalogue.getId()) {
            dbCardCatalogue=this.dbCardCatalogueMapper.selectByPrimaryKey(cardCatalogue.getId());
            Assert.notNull(dbCardCatalogue, MessageFormat.format("更新对象不存在！对象id为{0}", dbCardCatalogue.getId()));
            org.springframework.beans.BeanUtils.copyProperties(cardCatalogue, dbCardCatalogue);
            dbCardCatalogue.setModifyDate(new Date());
            this.dbCardCatalogueMapper.updateByPrimaryKeySelective(dbCardCatalogue);
        } else {
            dbCardCatalogue = new DbCardCatalogue();
            org.springframework.beans.BeanUtils.copyProperties(cardCatalogue, dbCardCatalogue);
            dbCardCatalogue.setDeleteFlag(SysCode.DELETE_STATUS.NO);
            dbCardCatalogue.setCreateDate(new Date());
            dbCardCatalogue.setModifyDate(new Date());
            dbCardCatalogue.setOid(UUIDUtil.randomUUID());
            this.dbCardCatalogueMapper.insert(dbCardCatalogue);
        }
        String  cardCatalogueOid=dbCardCatalogue.getOid();
        List<CardCatalogueElement> subList=cardCatalogue.getSubList();
        if(subList.size()>0){
            for(CardCatalogueElement cardCatalogueElement:subList){
                if(StringUtils.isNotEmpty(cardCatalogueElement.getCardCatalogueElementCode())&&StringUtils.isNotEmpty(cardCatalogueElement.getCardCatalogueElementName())){
                    cardCatalogueElement.setCardCatalogueOid(cardCatalogueOid);
                    cardCatalogueElementManager.saveOrUpdate(cardCatalogueElement);

                }

            }
        }
    }



    /**
     * 保存数据
     * @param cardCatalogueName 卡证目录名称
     */
   /* public void saveOrUpdateCardCatalogue(String cardCatalogueName, String oid) {
        Map<String, Object> map = new HashMap<>(5);
        if (StringUtils.isNotEmpty(oid)) {
            //修改
            map.put("oid", oid);
            map.put("cardCatalogueName", cardCatalogueName);
            dbCardCatalogueMapper.updateCardCatalogue(map);
        } else {
            //新增
            map.put("oid", UUID.randomUUID().toString().replace("-", ""));
            map.put("cardCatalogueName", cardCatalogueName);
            map.put("deleteFlag", "0");
            map.put("createDate", new Date());
            map.put("modifyDate", new Date());
            dbCardCatalogueMapper.saveCardCatalogue(map);
        }
    }*/

    /**
     *  oid查询
     * @param oid 主键oid
     * @return DbCardCatalogue
     */
    public DbCardCatalogue getCardCatalogueByOid(String oid) {
       return dbCardCatalogueMapper.getCardCatalogueByOid(oid);

    }

    public void deleteByOid(String oid) {
       dbCardCatalogueMapper.deleteByOid(oid);
    }


    /***
    * @Description: 查询卡证目录列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: []
    */
    public List<CardCatalogue> getAllCardCatalogueList() {
        List<DbCardCatalogue> dbCardCatalogues=dbCardCatalogueMapper.getAllCardCatalogueList();
        List<CardCatalogue> cardCatalogueList= dbCardCatalogues.stream().map(dbCardCatalogue -> {
            CardCatalogue cardCatalogue = new CardCatalogue();
            BeanUtils.copyProperties(dbCardCatalogue,cardCatalogue);
            return cardCatalogue;
        }).collect(Collectors.toList());
        return cardCatalogueList;
    }

}

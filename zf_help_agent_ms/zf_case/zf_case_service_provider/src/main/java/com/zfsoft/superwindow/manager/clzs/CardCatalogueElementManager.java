package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.CardCatalogueElement;
import com.zfsoft.superwindow.dbaccess.dao.DbCardCatalogueElementMapper;
import com.zfsoft.superwindow.dbaccess.data.DbCardCatalogueElement;
import com.zfsoft.superwindow.dbaccess.data.DbCardCatalogueElementExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
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
import java.util.stream.Collectors;


/***
* @Description:卡证目录元素
* @Author:liangss
* @Date:2021/11/4
* @Param:
*/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardCatalogueElementManager {

    @Resource
    private DbCardCatalogueElementMapper dbCardCatalogueElementMapper;

    /**
     * 分页查询列表
     * @param
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<CardCatalogueElement> queryCardCatalogueElementWithPage(CardCatalogueElement cardCatalogueElement, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbCardCatalogueElementExample dbCardCatalogueElementExample=new DbCardCatalogueElementExample();
        DbCardCatalogueElementExample.Criteria criteria=dbCardCatalogueElementExample.createCriteria();
        if(null!=cardCatalogueElement){
            if(StringUtils.isNotEmpty(cardCatalogueElement.getCardCatalogueOid())){
                criteria.andCardCatalogueOidEqualTo(cardCatalogueElement.getCardCatalogueOid());
            }
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbCardCatalogueElement> dbCardCatalogueElements= (Page<DbCardCatalogueElement>)dbCardCatalogueElementMapper.selectByExample(dbCardCatalogueElementExample);
        PageResult<CardCatalogueElement> pageResult = new PageResult<>(dbCardCatalogueElements.getPageNum(),dbCardCatalogueElements.getPageSize(),dbCardCatalogueElements.getTotal());
        List<CardCatalogueElement> cardCatalogueElementList= dbCardCatalogueElements.stream().map(dbCardCatalogueElement -> {
            CardCatalogueElement cardCatalogueElement1 = new CardCatalogueElement();
            BeanUtils.copyProperties(dbCardCatalogueElement,cardCatalogueElement1);
            return cardCatalogueElement1;
        }).collect(Collectors.toList());
        pageResult.setData(cardCatalogueElementList);
        return pageResult;
    }

   /***
   * @Description:更新目录元素
   * @Author:liangss
   * @Date:2021/11/4
   * @Param: [cardCatalogueElement]
   */
    public void saveOrUpdate(CardCatalogueElement cardCatalogueElement) throws Exception {
        DbCardCatalogueElement dbCardCatalogueElement = null;
        if (null != cardCatalogueElement.getId()) {
            dbCardCatalogueElement=this.dbCardCatalogueElementMapper.selectByPrimaryKey(cardCatalogueElement.getId());
            Assert.notNull(dbCardCatalogueElement, MessageFormat.format("更新对象不存在！对象id为{0}", dbCardCatalogueElement.getId()));
            org.springframework.beans.BeanUtils.copyProperties(cardCatalogueElement, dbCardCatalogueElement);
            dbCardCatalogueElement.setModifyDate(new Date());
            this.dbCardCatalogueElementMapper.updateByPrimaryKeySelective(dbCardCatalogueElement);
        } else {
            dbCardCatalogueElement = new DbCardCatalogueElement();
            org.springframework.beans.BeanUtils.copyProperties(cardCatalogueElement, dbCardCatalogueElement);
            dbCardCatalogueElement.setDeleteFlag(SysCode.DELETE_STATUS.NO);
            dbCardCatalogueElement.setCreateDate(new Date());
            dbCardCatalogueElement.setModifyDate(new Date());
            dbCardCatalogueElement.setOid(UUIDUtil.randomUUID());
            this.dbCardCatalogueElementMapper.insert(dbCardCatalogueElement);
        }
    }


    /***
    * @Description:  根据oid查询
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [oid]
    */
    public DbCardCatalogueElement getCardCatalogueElementByOid(String oid) {
       return dbCardCatalogueElementMapper.getCardCatalogueElementByOid(oid);

    }

    public void deleteByOid(String oid) {
        dbCardCatalogueElementMapper.deleteByOid(oid);
    }

    /***
    * @Description:根据目录oid查询目录元素编码列表
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [cardCatalogueOid]
    */
    public List<CardCatalogueElement> getCardCatalogueElementList(String cardCatalogueOid) {
        List<DbCardCatalogueElement> dbCardCatalogueElements=dbCardCatalogueElementMapper.getCardCatalogueElementList(cardCatalogueOid);
        List<CardCatalogueElement> cardCatalogueElementList= dbCardCatalogueElements.stream().map(dbCardCatalogueElement -> {
            CardCatalogueElement cardCatalogueElement1 = new CardCatalogueElement();
            BeanUtils.copyProperties(dbCardCatalogueElement,cardCatalogueElement1);
            return cardCatalogueElement1;
        }).collect(Collectors.toList());
        return cardCatalogueElementList;
    }

}

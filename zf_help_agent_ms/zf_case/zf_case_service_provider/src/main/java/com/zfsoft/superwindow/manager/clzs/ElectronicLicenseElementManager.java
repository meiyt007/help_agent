package com.zfsoft.superwindow.manager.clzs;

import com.zfsoft.superwindow.data.clzs.ElectronicLicenseElement;
import com.zfsoft.superwindow.dbaccess.dao.itfr.DbEntityElectronicLicenseElementMapper;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicenseElement;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicenseElementExample;
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
public class ElectronicLicenseElementManager {

    @Resource
    private DbEntityElectronicLicenseElementMapper dbEntityElectronicLicenseElementMapper;

    /**
     * 分页查询列表
     * @param
     * @param pageNumber
     * @param pageSize
     * @return
     */
    /*public PageResult<CardCatalogueElement> queryCardCatalogueElementWithPage(CardCatalogueElement cardCatalogueElement, Integer pageNumber, Integer pageSize) {
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
    }*/

   /***
   * @Description:更新目录元素
   * @Author:liangss
   * @Date:2021/11/4
   * @Param: [cardCatalogueElement]
   */
    public void saveOrUpdate(ElectronicLicenseElement electronicLicenseElement) throws Exception {
        DbEntityElectronicLicenseElement dbEntityElectronicLicenseElement = null;
        if (null != electronicLicenseElement.getId()) {
            dbEntityElectronicLicenseElement=this.dbEntityElectronicLicenseElementMapper.selectByPrimaryKey(electronicLicenseElement.getId());
            Assert.notNull(dbEntityElectronicLicenseElement, MessageFormat.format("更新对象不存在！对象id为{0}", dbEntityElectronicLicenseElement.getId()));
            org.springframework.beans.BeanUtils.copyProperties(electronicLicenseElement, dbEntityElectronicLicenseElement);
            dbEntityElectronicLicenseElement.setModifyDate(new Date());
            this.dbEntityElectronicLicenseElementMapper.updateByPrimaryKeySelective(dbEntityElectronicLicenseElement);
        } else {
            if(electronicLicenseElement.getDeleteFlag().equals(SysCode.DELETE_STATUS.YES)){
                dbEntityElectronicLicenseElement = new DbEntityElectronicLicenseElement();
                org.springframework.beans.BeanUtils.copyProperties(electronicLicenseElement, dbEntityElectronicLicenseElement);
                dbEntityElectronicLicenseElement.setDeleteFlag(SysCode.DELETE_STATUS.NO);
                dbEntityElectronicLicenseElement.setCreateDate(new Date());
                dbEntityElectronicLicenseElement.setModifyDate(new Date());
                dbEntityElectronicLicenseElement.setOid(UUIDUtil.randomUUID());
                this.dbEntityElectronicLicenseElementMapper.insert(dbEntityElectronicLicenseElement);
            }

        }
    }


    /***
    * @Description:  根据oid查询
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [oid]
    */
    /*public DbCardCatalogueElement getCardCatalogueElementByOid(String oid) {
       return dbCardCatalogueElementMapper.getCardCatalogueElementByOid(oid);

    }*/

    /*public void deleteByOid(String oid) {
        dbCardCatalogueElementMapper.deleteByOid(oid);
    }*/

    /***
    * @Description:根据目录oid查询目录元素编码列表
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [cardCatalogueOid]
    */
    public List<ElectronicLicenseElement> getElectronicLicenseElementList(String electronicLicenseInterfaceOid) {
        DbEntityElectronicLicenseElementExample dbEntityElectronicLicenseElementExample = new DbEntityElectronicLicenseElementExample();
        DbEntityElectronicLicenseElementExample.Criteria criteria = dbEntityElectronicLicenseElementExample.createCriteria();
        if (StringUtils.isNotEmpty(electronicLicenseInterfaceOid)) {
            criteria.andElectronicLicenseInterfaceOidEqualTo(electronicLicenseInterfaceOid);
        }
        criteria.andDeleteFlagEqualTo(0);
        List<DbEntityElectronicLicenseElement> dbEntityElectronicLicenseElements = dbEntityElectronicLicenseElementMapper.selectByExample(dbEntityElectronicLicenseElementExample);
        List<ElectronicLicenseElement> electronicLicenseElements= dbEntityElectronicLicenseElements.stream().map(dbEntityElectronicLicenseElement -> {
            ElectronicLicenseElement electronicLicenseElement = new ElectronicLicenseElement();
            BeanUtils.copyProperties(dbEntityElectronicLicenseElement,electronicLicenseElement);
            return electronicLicenseElement;
        }).collect(Collectors.toList());
        return electronicLicenseElements;
    }

    /**
     * 根据目录主键查询所有的元素信息
     * @param billOids
     * @return
     */
    public List<ElectronicLicenseElement> getElectronicElementListByBillOids(List<String> billOids){
        DbEntityElectronicLicenseElementExample example=new DbEntityElectronicLicenseElementExample();
        DbEntityElectronicLicenseElementExample.Criteria criteria=example.createCriteria();
        if(billOids!=null && billOids.size()>0){
            criteria.andElectronicLicenseInterfaceOidIn(billOids);
        }
        criteria.andDeleteFlagEqualTo(0);
      List<DbEntityElectronicLicenseElement> elecList=  dbEntityElectronicLicenseElementMapper.selectByExample(example);
      if(elecList!=null && elecList.size()>0){
         return elecList.stream().map(element->{
              ElectronicLicenseElement elementElec=new ElectronicLicenseElement();
              BeanUtils.copyProperties(element,elementElec);
              return elementElec;
          }).collect(Collectors.toList());
      }
      return null;
    }

    public ElectronicLicenseElement queryElectronicElementListByOid(String oid){
       DbEntityElectronicLicenseElement element= dbEntityElectronicLicenseElementMapper.selectOneByOid(oid);
        if(element!=null){
            ElectronicLicenseElement elementElec=new ElectronicLicenseElement();
            BeanUtils.copyProperties(element,elementElec);
            return elementElec;
        }
        return null;
    }

}

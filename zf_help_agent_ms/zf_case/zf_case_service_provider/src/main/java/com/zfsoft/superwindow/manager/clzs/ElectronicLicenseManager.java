package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.ElectronicLicense;
import com.zfsoft.superwindow.data.clzs.ElectronicLicenseElement;
import com.zfsoft.superwindow.dbaccess.dao.itfr.DbEntityElectronicLicenseMapper;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicense;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicenseExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.ElectronicLicenseTree;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 卡证目录
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronicLicenseManager {

    @Resource
    private DbEntityElectronicLicenseMapper dbEntityElectronicLicenseMapper;

    @Resource
    ElectronicLicenseElementManager electronicLicenseElementManager;

    /**
     * 分页查询列表
     * @param electronicLicenseName
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<ElectronicLicense> queryInfoWithPage(String electronicLicenseName, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        //拼装参数
        DbEntityElectronicLicenseExample dbEntityElectronicLicenseExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria=dbEntityElectronicLicenseExample.createCriteria();
        if(StringUtils.isNotEmpty(electronicLicenseName)){
            criteria.andElectronicLicenseNameLike("%"+ electronicLicenseName.trim() + "%");
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbEntityElectronicLicenseExample.setOrderByClause(" CREATE_DATE DESC ");
        Page<DbEntityElectronicLicense> dbEntityElectronicLicenses= (Page<DbEntityElectronicLicense>)dbEntityElectronicLicenseMapper.selectByExample(dbEntityElectronicLicenseExample);
        PageResult<ElectronicLicense> pageResult = new PageResult<>(dbEntityElectronicLicenses.getPageNum(),dbEntityElectronicLicenses.getPageSize(),dbEntityElectronicLicenses.getTotal());

        List<ElectronicLicense> electronicLicenseList= dbEntityElectronicLicenses.stream().map(dbEntityElectronicLicense -> {
            ElectronicLicense electronicLicense = new ElectronicLicense();
            BeanUtils.copyProperties(dbEntityElectronicLicense,electronicLicense);
            return electronicLicense;
        }).collect(Collectors.toList());
        pageResult.setData(electronicLicenseList);
        return pageResult;
    }

    /***
    * @Description:  更新保存方法
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [cardCatalogue]
    */
    public void saveOrUpdate(ElectronicLicense electronicLicense) throws Exception {
        DbEntityElectronicLicense dbEntityElectronicLicense = null;
        if (null != electronicLicense.getId()) {
            dbEntityElectronicLicense=this.dbEntityElectronicLicenseMapper.selectByPrimaryKey(electronicLicense.getId());
            Assert.notNull(dbEntityElectronicLicense, MessageFormat.format("更新对象不存在！对象id为{0}", dbEntityElectronicLicense.getId()));
            org.springframework.beans.BeanUtils.copyProperties(electronicLicense, dbEntityElectronicLicense);
            dbEntityElectronicLicense.setModifyDate(new Date());
            this.dbEntityElectronicLicenseMapper.updateByPrimaryKeySelective(dbEntityElectronicLicense);
        } else {
            dbEntityElectronicLicense = new DbEntityElectronicLicense();
            org.springframework.beans.BeanUtils.copyProperties(electronicLicense, dbEntityElectronicLicense);
            dbEntityElectronicLicense.setDeleteFlag(SysCode.DELETE_STATUS.NO);
            dbEntityElectronicLicense.setCreateDate(new Date());
            dbEntityElectronicLicense.setModifyDate(new Date());
            dbEntityElectronicLicense.setOid(UUIDUtil.randomUUID());
            this.dbEntityElectronicLicenseMapper.insert(dbEntityElectronicLicense);
        }
        String  electronicLicenseInterfaceOid=dbEntityElectronicLicense.getOid();
        List<ElectronicLicenseElement> subList=electronicLicense.getSubList();
        if(subList !=null && subList.size()>0){
            for(ElectronicLicenseElement electronicLicenseElement:subList){
                if(StringUtils.isNotEmpty(electronicLicenseElement.getElectronicLicenseElementCode())&&StringUtils.isNotEmpty(electronicLicenseElement.getElectronicLicenseElementName())){
                    electronicLicenseElement.setElectronicLicenseInterfaceOid(electronicLicenseInterfaceOid);
                    electronicLicenseElementManager.saveOrUpdate(electronicLicenseElement);
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
    public DbEntityElectronicLicense getElectronicLicenseByOid(String oid) {
        DbEntityElectronicLicenseExample dbCardCatalogueExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria=dbCardCatalogueExample.createCriteria();
        if(StringUtils.isNotEmpty(oid)){
            criteria.andOidEqualTo(oid);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbEntityElectronicLicense> dbEntityElectronicLicenses = dbEntityElectronicLicenseMapper.selectByExample(dbCardCatalogueExample);
        if (dbEntityElectronicLicenses !=null && dbEntityElectronicLicenses.size()>0) {
            return dbEntityElectronicLicenses.get(0);
        } else {
            return null;
        }
    }

    /**
     *  刪除信息
     * @param oid
     */
    public void deleteByOid(String oid) {
        DbEntityElectronicLicenseExample dbCardCatalogueExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria=dbCardCatalogueExample.createCriteria();
        if(StringUtils.isNotEmpty(oid)){
            criteria.andOidEqualTo(oid);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbEntityElectronicLicense> dbEntityElectronicLicenses = dbEntityElectronicLicenseMapper.selectByExample(dbCardCatalogueExample);
        DbEntityElectronicLicense dbEntityElectronicLicense = dbEntityElectronicLicenses.get(0);
        dbEntityElectronicLicense.setDeleteFlag(SysCode.DELETE_STATUS.YES);
        this.dbEntityElectronicLicenseMapper.updateByPrimaryKeySelective(dbEntityElectronicLicense);
    }


    /***
    * @Description: 查询卡证目录列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: []
    */
    /*public List<CardCatalogue> getAllCardCatalogueList() {
        List<DbCardCatalogue> dbCardCatalogues=dbCardCatalogueMapper.getAllCardCatalogueList();
        List<CardCatalogue> cardCatalogueList= dbCardCatalogues.stream().map(dbCardCatalogue -> {
            CardCatalogue cardCatalogue = new CardCatalogue();
            BeanUtils.copyProperties(dbCardCatalogue,cardCatalogue);
            return cardCatalogue;
        }).collect(Collectors.toList());
        return cardCatalogueList;
    }*/

    /**
     *  检测是否已添加证照元素
     * @param electronicLicense
     * @return
     */
    public String checkElectronicLicenseIsExist(ElectronicLicense electronicLicense) {
        DbEntityElectronicLicenseExample dbEntityElectronicLicenseExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria = dbEntityElectronicLicenseExample.createCriteria();
        if (electronicLicense !=null) {
            if (StringUtils.isNotEmpty(electronicLicense.getElectronicLicenseCode())) {
                criteria.andElectronicLicenseCodeEqualTo(electronicLicense.getElectronicLicenseCode());
            }
            if (StringUtils.isNotEmpty(electronicLicense.getElectronicLicenseName())) {
                criteria.andElectronicLicenseNameEqualTo(electronicLicense.getElectronicLicenseName());
            }
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbEntityElectronicLicense> dbEntityElectronicLicenses = dbEntityElectronicLicenseMapper.selectByExample(dbEntityElectronicLicenseExample);
        if (dbEntityElectronicLicenses !=null && dbEntityElectronicLicenses.size()>0) {
            return "ElectronicLicense";
        }
        return null;
    }


    public List<ElectronicLicenseTree> queryElectronicLicenseList(ElectronicLicense electronicLicense) {
        DbEntityElectronicLicenseExample dbEntityElectronicLicenseExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria = dbEntityElectronicLicenseExample.createCriteria();

        if (electronicLicense != null) {
            if (StringUtils.isNotEmpty(electronicLicense.getElectronicLicenseCode())) {
                criteria.andElectronicLicenseCodeEqualTo(electronicLicense.getElectronicLicenseCode());
            }
            if (StringUtils.isNotEmpty(electronicLicense.getElectronicLicenseName())) {
                criteria.andElectronicLicenseNameEqualTo(electronicLicense.getElectronicLicenseName());
            }
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<ElectronicLicenseTree> electronicLicenseTrees = new ArrayList<>();
        List<DbEntityElectronicLicense> dbEntityElectronicLicenses = dbEntityElectronicLicenseMapper.selectByExample(dbEntityElectronicLicenseExample);
        for (DbEntityElectronicLicense dbEntityElectronicLicense : dbEntityElectronicLicenses) {
            ElectronicLicenseTree electronicLicenseTree = new ElectronicLicenseTree();
            electronicLicenseTree.setId(dbEntityElectronicLicense.getOid());
            electronicLicenseTree.setValue(dbEntityElectronicLicense.getOid() + ";"  + dbEntityElectronicLicense.getElectronicLicenseName()+";"+dbEntityElectronicLicense.getElectronicLicenseOid()+";"+dbEntityElectronicLicense.getElectronicLicenseCode());
            // electronicLicenseTree.setValue(dbEntityElectronicLicense.getOid() + ";" + dbEntityElectronicLicense.getElectronicLicenseCode() + ";" + dbEntityElectronicLicense.getElectronicLicenseName());
            electronicLicenseTree.setLabel(dbEntityElectronicLicense.getElectronicLicenseName());
            List<ElectronicLicenseTree> electronicLicenseTreesChild = new ArrayList<>();
            ElectronicLicense electronicLicense1 = new ElectronicLicense();
            List<ElectronicLicenseElement> subList = electronicLicenseElementManager.getElectronicLicenseElementList(dbEntityElectronicLicense.getOid());
            for (ElectronicLicenseElement electronicLicenseElement : subList) {
                ElectronicLicenseTree child = new ElectronicLicenseTree();
                child.setId(electronicLicenseElement.getOid());
                child.setLabel(electronicLicenseElement.getElectronicLicenseElementName());
                child.setValue(electronicLicenseElement.getOid() + ";" + electronicLicenseElement.getElectronicLicenseElementCode() + ";" + electronicLicenseElement.getElectronicLicenseElementName());
                child.setParentId(dbEntityElectronicLicense.getOid());
                electronicLicenseTreesChild.add(child);
            }
            BeanUtils.copyProperties(dbEntityElectronicLicense, electronicLicense1);
            electronicLicenseTree.setChildren(electronicLicenseTreesChild);
            electronicLicenseTrees.add(electronicLicenseTree);
        }
        return  electronicLicenseTrees;
    }

    /**
     *  保存查看证照是否存在
     * @param oid
     * @param code
     * @return
     */
    public String checkHasRepeat(String oid ,String code) {
        DbEntityElectronicLicenseExample dbEntityElectronicLicenseExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria = dbEntityElectronicLicenseExample.createCriteria();
        if (StringUtils.isNotEmpty(code)) {
            criteria.andElectronicLicenseCodeEqualTo(code);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbEntityElectronicLicense> dbEntityElectronicLicenses = dbEntityElectronicLicenseMapper.selectByExample(dbEntityElectronicLicenseExample);

        //重复判断
        String flag = "false";
        if (StringUtils.isNotEmpty(oid)) {
            if (dbEntityElectronicLicenses !=null && dbEntityElectronicLicenses.size()> 0) {
                DbEntityElectronicLicense dbEntityElectronicLicense = dbEntityElectronicLicenses.get(0);
                if (dbEntityElectronicLicense !=null) {
                    if (!oid.equals(dbEntityElectronicLicense.getOid())) {
                        flag = "true";
                    }
                }
            }
        } else {
            if (dbEntityElectronicLicenses !=null && dbEntityElectronicLicenses.size()>0) {
                flag = "true";
            }
        }
        return flag;
    }

    /**
     * 查询所有的配置的信息
     * @param billOids
     * @return
     * dongxl
     */
    public List<ElectronicLicense> queryElectronicLicenseListByBillOids(List<String> billOids) {
        DbEntityElectronicLicenseExample dbEntityElectronicLicenseExample = new DbEntityElectronicLicenseExample();
        DbEntityElectronicLicenseExample.Criteria criteria = dbEntityElectronicLicenseExample.createCriteria();
        if (billOids != null && billOids.size()>0) {
            criteria.andElectronicLicenseOidIn(billOids);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbEntityElectronicLicense> dbEntityElectronicLicenses = dbEntityElectronicLicenseMapper.selectByExample(dbEntityElectronicLicenseExample);
        if(dbEntityElectronicLicenses!=null && dbEntityElectronicLicenses.size()>0){
           return dbEntityElectronicLicenses.stream().map(license->{
                ElectronicLicense eleLince=new ElectronicLicense();
                BeanUtils.copyProperties(license,eleLince);
                return eleLince;
            }).collect(Collectors.toList());
        }
        return null;
    }

}

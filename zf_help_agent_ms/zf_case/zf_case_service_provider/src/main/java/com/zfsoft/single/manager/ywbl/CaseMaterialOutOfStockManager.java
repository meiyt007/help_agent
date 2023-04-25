package com.zfsoft.single.manager.ywbl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStock;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStockRecord;
import com.zfsoft.single.dbaccess.dao.ywbl.DbCaseMaterialOutOfStockMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseMaterialOutOfStock;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseMaterialOutOfStockExample;
import com.zfsoft.single.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: dongxl
 * @create: 2020-11-6
 * @description: 办件材料出库
 */
@Slf4j
@Service
public class CaseMaterialOutOfStockManager {
    @Resource
    private DbCaseMaterialOutOfStockMapper dbCaseMaterialOutOfStockMapper;
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;
    @Resource
    private SysUserFeginService sysUserFeginService;
    @Resource
    private QlCaseService qlCaseServiceFeginService;
    @Resource
    private SysOrganFeginService sysOrganFeginService;
    @Resource
    private CaseMaterialOutOfStockRecordManager caseMaterialOutOfStockRecordManager;

    /**
     * 材料出库保存更新信息
     * dongxl
     * @param caseMaterialOutOfStock
     */
    @Transactional(rollbackFor = Exception.class)
    public CaseMaterialOutOfStock saveOrUpdate(CaseMaterialOutOfStock caseMaterialOutOfStock) {

        DbCaseMaterialOutOfStock outOfStock=new DbCaseMaterialOutOfStock();
        if(caseMaterialOutOfStock!=null){
            if(caseMaterialOutOfStock.getId()!=null){
                 outOfStock=dbCaseMaterialOutOfStockMapper.selectByPrimaryKey(caseMaterialOutOfStock.getId());
                Assert.notNull(outOfStock, MessageFormat.format("更新对象不存在！对象id为{0}", caseMaterialOutOfStock.getId()));
                outOfStock.setOutStatus(String.valueOf(SysCode.STATUS.YES));
                outOfStock.setIdCard(caseMaterialOutOfStock.getIdCard());
                outOfStock.setOutType(caseMaterialOutOfStock.getOutType());
                outOfStock.setKdCode(caseMaterialOutOfStock.getKdCode());
                outOfStock.setKdCompany(caseMaterialOutOfStock.getKdCompany());
                outOfStock.setReceiverName(caseMaterialOutOfStock.getReceiverName());
                outOfStock.setReceiverPhone(caseMaterialOutOfStock.getReceiverPhone());
                outOfStock.setSenderUserName(caseMaterialOutOfStock.getSenderUserName());
                outOfStock.setSenderUserPhone(caseMaterialOutOfStock.getSenderUserPhone());
                outOfStock.setSelectMaterialOids(caseMaterialOutOfStock.getSelectMaterialOids());
                outOfStock.setOutStockDate(new Date());
                outOfStock.setModifyDate(new Date());
                outOfStock.setOutStockUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                //由于获取当前登录人名称乱码，所以查询
                ApiResultSet<SysUser> sysuser=sysUserFeginService.getSysUserByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                outOfStock.setOutUserName(sysuser.getData().getName());
                this.dbCaseMaterialOutOfStockMapper.updateByPrimaryKeySelective(outOfStock);
            }else{
                outOfStock=new DbCaseMaterialOutOfStock();
                BeanUtils.copyProperties(caseMaterialOutOfStock,outOfStock);
                outOfStock.setOutOid(UUIDUtil.randomUUID());
                outOfStock.setCaseCreateDate(new Date());
                outOfStock.setOutStatus(String.valueOf(SysCode.STATUS.NO));
                outOfStock.setModifyDate(new Date());
                //办件创建人
                ApiResultSet<QlCase> qlcase=qlCaseServiceFeginService.queryQlCaseByCaseOid(outOfStock.getRegOid());
                if(null!=qlcase && qlcase.getData()!=null){
                    outOfStock.setCaseCreateUserOid(qlcase.getData().getCreateUserOid());
                }
                this.dbCaseMaterialOutOfStockMapper.insertSelective(outOfStock);
            }
        }

        BeanUtils.copyProperties(outOfStock,caseMaterialOutOfStock);
        return  caseMaterialOutOfStock;
    }



    @Transactional(rollbackFor = Exception.class)
    public void updateCaseMaterialOutOfStock(CaseMaterialOutOfStock caseMaterialOutOfStock) {
        if(caseMaterialOutOfStock!=null){
            if(caseMaterialOutOfStock.getId()!=null){
                DbCaseMaterialOutOfStock caseMaterial=dbCaseMaterialOutOfStockMapper.selectByPrimaryKey(caseMaterialOutOfStock.getId());
                Assert.notNull(caseMaterial, MessageFormat.format("更新对象不存在！对象id为{0}", caseMaterialOutOfStock.getId()));
                if(StringUtils.isNotEmpty(caseMaterialOutOfStock.getOutStatus()) && !caseMaterialOutOfStock.getOutStatus().equals("2")){
                    caseMaterial.setIdCard(caseMaterialOutOfStock.getIdCard());
                    caseMaterial.setOutType(caseMaterialOutOfStock.getOutType());
                    caseMaterial.setKdCode(caseMaterialOutOfStock.getKdCode());
                    caseMaterial.setKdCompany(caseMaterialOutOfStock.getKdCompany());
                    caseMaterial.setReceiverName(caseMaterialOutOfStock.getReceiverName());
                    caseMaterial.setReceiverPhone(caseMaterialOutOfStock.getReceiverPhone());
                    caseMaterial.setSenderUserName(caseMaterialOutOfStock.getSenderUserName());
                    caseMaterial.setSenderUserPhone(caseMaterialOutOfStock.getSenderUserPhone());
                    caseMaterial.setOutStockDate(new Date());
                    if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                        caseMaterial.setOutStockUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                        //由于获取当前登录人名称乱码，所以查询
                        ApiResultSet<SysUser> sysuser=sysUserFeginService.getSysUserByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                        caseMaterial.setOutUserName(sysuser.getData().getName());
                    }

                }
                if(StringUtils.isNotEmpty(caseMaterialOutOfStock.getSelectMaterialOids())){
                    caseMaterial.setSelectMaterialOids(caseMaterialOutOfStock.getSelectMaterialOids());
                }
                caseMaterial.setModifyDate(new Date());
                if(StringUtils.isNotEmpty(caseMaterialOutOfStock.getOutStatus())){
                    caseMaterial.setOutStatus(caseMaterialOutOfStock.getOutStatus());
                }
                this.dbCaseMaterialOutOfStockMapper.updateByPrimaryKeySelective(caseMaterial);
            }
        }
    }

    /**
     * 材料出库列表查询
     * dongxl
     * @param caseMaterialOutOfStock
     * @return
     */
    public List<CaseMaterialOutOfStock> queryPage(CaseMaterialOutOfStock caseMaterialOutOfStock) {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        Map<String,Object> map =null;
        if(null!=caseMaterialOutOfStock){
            map= new HashMap<String,Object>();
            if(StrUtil.isNotEmpty(caseMaterialOutOfStock.getRegOid())){
                map.put("regOid",caseMaterialOutOfStock.getRegOid());
            }else {
                if(caseMaterialOutOfStock.getStartDate() !=null){
                    map.put("startDate",DateUtil.startDateFormat(caseMaterialOutOfStock.getStartDate()));
                }
                if(caseMaterialOutOfStock.getEndDate() !=null){
                    map.put("endDate",DateUtil.endDateFormat(caseMaterialOutOfStock.getEndDate()));
                }
            }
            if(StrUtil.isNotEmpty(caseMaterialOutOfStock.getOutStatus())){
                map.put("outStatus",caseMaterialOutOfStock.getOutStatus());
            }
            if(StrUtil.isNotEmpty(caseMaterialOutOfStock.getApplyUserName())){
                map.put("applyUserName",caseMaterialOutOfStock.getApplyUserName());
            }
            if(StrUtil.isNotEmpty(caseMaterialOutOfStock.getRegOid())){
                map.put("caseOid",caseMaterialOutOfStock.getRegOid());
            }
            if(StrUtil.isNotEmpty(caseMaterialOutOfStock.getCaseNumber())){
                map.put("caseNumber",caseMaterialOutOfStock.getCaseNumber());
            }
            //map.put("userOid",loginUser.getUserOid());

        }
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(caseMaterialOutOfStock.getServiceOids()!=null && caseMaterialOutOfStock.getServiceOids().size()>0){
                map.put("serviceOids",caseMaterialOutOfStock.getServiceOids());
            }else{
                return new ArrayList<CaseMaterialOutOfStock>();
            }
        }
        List<DbCaseMaterialOutOfStock> dbCaseMaterial=dbCaseMaterialOutOfStockMapper.queryCaseMaterialOutOfStockList(map);
        return BeanUtils.copyListProperties(dbCaseMaterial, CaseMaterialOutOfStock::new);
    }

    public List<CaseMaterialOutOfStock> getOneByCaseNumber(String caseNumber) {
        Assert.hasLength(caseNumber, "办件编号不能为空！");
        DbCaseMaterialOutOfStockExample dbCaseMaterialOutOfStockExample = new DbCaseMaterialOutOfStockExample();
        DbCaseMaterialOutOfStockExample.Criteria criteria = dbCaseMaterialOutOfStockExample.createCriteria();
        if (StrUtil.isNotEmpty(caseNumber)) {
            criteria.andCaseNumberEqualTo(caseNumber.trim());
        }
        criteria.andOutStatusEqualTo(String.valueOf(SysCode.STATUS.NO));
        List<DbCaseMaterialOutOfStock> dbCaseMaterial = dbCaseMaterialOutOfStockMapper.selectByExample(dbCaseMaterialOutOfStockExample);
        if (!CollectionUtils.isEmpty(dbCaseMaterial)) {
            return BeanUtils.copyListProperties(dbCaseMaterial,CaseMaterialOutOfStock::new);
        }
        return Collections.emptyList();
    }
    public Map<String, Object> getReplaceMap(Long id, CurrentLoginUser user){
        DbCaseMaterialOutOfStock caseMaterial=dbCaseMaterialOutOfStockMapper.selectByPrimaryKey(id);

        //获取流转记录
        String outOid=caseMaterial.getOutOid();
        String regOid=caseMaterial.getRegOid();
        CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord=new CaseMaterialOutOfStockRecord();
        caseMaterialOutOfStockRecord.setOutOid(outOid);
        caseMaterialOutOfStockRecord.setRegOid(regOid);
        List<CaseMaterialOutOfStockRecord> caseMaterialOutOfStockRecordList=caseMaterialOutOfStockRecordManager.queryCaseMaterialOutOfStockRecordList(caseMaterialOutOfStockRecord);

        String selectMaterialOids=caseMaterial.getSelectMaterialOids();
        String checkMaterlOids="";
        String[] materialArray= caseMaterial.getMaterialOids().split(";");
        Set<String> sets = new HashSet(Arrays.asList(materialArray));
        List<QlCaseMaterial> caseRegMaterialList=new ArrayList<>();
        for (String materialOid:sets){
            if(StrUtil.isNotEmpty(materialOid)){
                ApiResultSet<QlCaseMaterial> material= qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(materialOid);
                if(material!=null){
                    QlCaseMaterial qlCaseMaterial=material.getData();
                    qlCaseMaterial.setAuditType("N");
                    if(StringUtils.isNotEmpty(selectMaterialOids)){
                          if(selectMaterialOids.contains(qlCaseMaterial.getCaseMaterialOid())){
                              qlCaseMaterial.setAuditType("Y");
                          }
                    }else{
                        if(qlCaseMaterial.getCollectionType().equals("1")){
                            qlCaseMaterial.setAuditType("Y");
                        }
                    }
                    caseRegMaterialList.add(material.getData());
                }
            }
        }
        //办件材料信息
        String materialsInfo = "";
       /*
            if (caseRegMaterialList!=null&&caseRegMaterialList.size() > 0) {
                for (int i = 0; i < caseRegMaterialList.size(); i++) {
                    materialsInfo += String.valueOf(i + 1);
                    if (StrUtil.isNotEmpty(caseRegMaterialList.get(i).getMaterialOid())) {
                        materialsInfo += " - 材料名称：" + caseRegMaterialList.get(i).getMaterialName();
                        materialsInfo += "，收取数量：" + caseRegMaterialList.get(i).getCollectionNumber() + ";";
                    } else {
                        materialsInfo += " - 材料名称：" + caseRegMaterialList.get(i).getMaterialName()+ ";";
                    }
                }
            } else {
                materialsInfo += "无";
            }*/

        Map<String, Object> map = new HashMap<String, Object>();
        if (StrUtil.isNotEmpty(caseMaterial.getApplyUserName())) {
            map.put("PO_applyUserName",caseMaterial.getApplyUserName());
        }else{
            map.put("PO_applyUserName", "");
        }
        //查询办件所属部门
        if(StrUtil.isNotEmpty(caseMaterial.getRegOid())){

          ApiResultSet<QlCase> qlcase=  qlCaseServiceFeginService.queryQlCaseByCaseOid(caseMaterial.getRegOid());
          if(qlcase!=null && qlcase.getData()!=null){
             //查询办件所属机构
             ApiResultSet<SysOrgan> sysOrgan= sysOrganFeginService.getSysOrganByOrganOid(qlcase.getData().getOrganOid());
             if(sysOrgan!=null && sysOrgan.getData()!=null){
                 map.put("PO_organName", sysOrgan.getData().getName());
             }else{
                 map.put("PO_organName", "");
             }
          }
        }
        map.put("PO_regNumber", caseMaterial.getCaseNumber());
        map.put("PO_createDate", DateUtil.date2Str(caseMaterial.getCaseCreateDate(), DateUtil.date_sdf));
        map.put("PO_noticeNum", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        map.put("PO_circulationType", "打印材料流转单");
        ApiResultSet<SysUser> sysUser=sysUserFeginService.getSysUserByUserOid(user.getUserOid());
        if(sysUser.getData()!=null){
            map.put("PO_signature", sysUser.getData().getName());
        }else{
            map.put("PO_signature", "");
        }
        map.put("PO_circulationDate", DateUtil.date2Str(new Date(), DateUtil.date_sdf));
        map.put("PO_materialsInfo", materialsInfo);
        map.put("PO_outType",caseMaterial.getOutType()+"");
        map.put("PO_receiverName",caseMaterial.getReceiverName());
        map.put("PO_receiverPhone",caseMaterial.getReceiverPhone());
        map.put("PO_kdCompany",caseMaterial.getKdCompany());
        map.put("PO_kdCode",caseMaterial.getKdCode());
        map.put("PO_idCard",caseMaterial.getIdCard());
        map.put("PO_senderUserName",caseMaterial.getSenderUserName());
        map.put("PO_senderUserPhone",caseMaterial.getSenderUserPhone());
        map.put("caseRegMaterialList",caseRegMaterialList);
        map.put("selectMaterialOids",caseMaterial.getSelectMaterialOids());
        map.put("id",caseMaterial.getId());
        map.put("caseMaterialOutOfStockRecordList",caseMaterialOutOfStockRecordList);
        return map;
    }

    public CaseMaterialOutOfStock getOutMaterialById(Long id){
        DbCaseMaterialOutOfStock caseMaterial=dbCaseMaterialOutOfStockMapper.selectByPrimaryKey(id);
        if(caseMaterial!=null){
            CaseMaterialOutOfStock cm=new CaseMaterialOutOfStock();
            BeanUtils.copyProperties(caseMaterial,cm);
            return cm;
        }
        return null;
    }

    public List<CaseMaterialOutOfStock> getCaseMaterialOutByCaseNumber(String caseNumber) {
        Assert.hasLength(caseNumber, "办件编号不能为空！");
        DbCaseMaterialOutOfStockExample dbCaseMaterialOutOfStockExample = new DbCaseMaterialOutOfStockExample();
        DbCaseMaterialOutOfStockExample.Criteria criteria = dbCaseMaterialOutOfStockExample.createCriteria();
        if (StrUtil.isNotEmpty(caseNumber)) {
            criteria.andCaseNumberEqualTo(caseNumber.trim());
        }
        List<DbCaseMaterialOutOfStock> dbCaseMaterial = dbCaseMaterialOutOfStockMapper.selectByExample(dbCaseMaterialOutOfStockExample);
        List<CaseMaterialOutOfStock> list=new ArrayList<>();
        if (!CollectionUtils.isEmpty(dbCaseMaterial)) {
            for(DbCaseMaterialOutOfStock cm:dbCaseMaterial){


                //获取流转记录
                String outOid=cm.getOutOid();
                String regOid=cm.getRegOid();
                CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord=new CaseMaterialOutOfStockRecord();
                caseMaterialOutOfStockRecord.setOutOid(outOid);
                caseMaterialOutOfStockRecord.setRegOid(regOid);
                List<CaseMaterialOutOfStockRecord> caseMaterialOutOfStockRecordList=caseMaterialOutOfStockRecordManager.queryCaseMaterialOutOfStockRecordList(caseMaterialOutOfStockRecord);
                String materialname="";
                if(cm!=null){
                    if(StrUtil.isNotEmpty(cm.getSelectMaterialOids())){
                        //String[] materiArray=cm.getMaterialOids().split(";");
                        String[] materiArray=cm.getSelectMaterialOids().split(";");
                        for(int i=0;i<materiArray.length;i++){
                            ApiResultSet<QlCaseMaterial> material= qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(materiArray[i]);
                            if(material.getData()!=null){
                                materialname += (i + 1)+"、"+material.getData().getMaterialName()+";";
                            }
                        }
                    }
                }
                CaseMaterialOutOfStock cs=new CaseMaterialOutOfStock();
                if(cm!=null){
                    BeanUtils.copyProperties(cm,cs);
                }
                cs.setMaterialNameOut(materialname);
                cs.setCaseMaterialOutOfStockRecordList(caseMaterialOutOfStockRecordList);
                list.add(cs);
            }
        }
        return list;
    }

    public List<CaseMaterialOutOfStock> queryListOutOfStock(List<String> listService){
        DbCaseMaterialOutOfStockExample dbCaseMaterialOutOfStockExample = new DbCaseMaterialOutOfStockExample();
        DbCaseMaterialOutOfStockExample.Criteria criteria = dbCaseMaterialOutOfStockExample.createCriteria();
        criteria.andOutStatusEqualTo("0");
        if(listService!=null && listService.size()>0){
            criteria.andServiceOidIn(listService);
        }
        List<DbCaseMaterialOutOfStock> dbCaseMaterial = dbCaseMaterialOutOfStockMapper.selectByExample(dbCaseMaterialOutOfStockExample);
        List<CaseMaterialOutOfStock> list=dbCaseMaterial.stream().map(caseMaterial->{
            CaseMaterialOutOfStock stock=new CaseMaterialOutOfStock();
            BeanUtils.copyProperties(caseMaterial,stock);
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseMaterial.getRegOid());
            QlCase qlCase = resultSet.getData();
            stock.setServiceName(qlCase.getProjectName());
            return stock;
        }).collect(Collectors.toList());
         return list;
    }

    /**
     * 批量出库
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchOutMaterial(String ids) {
        if(StrUtil.isNotEmpty(ids)){
            String[] caseIds=ids.split(";");
            for(String id:caseIds){
                DbCaseMaterialOutOfStock caseMaterial=dbCaseMaterialOutOfStockMapper.selectByPrimaryKey(Long.parseLong(id));
                Assert.notNull(caseMaterial, MessageFormat.format("更新对象不存在！对象id为{0}", id));
                //查询办件材料
               ApiResultSet<List<QlCaseMaterial>> casematerial= qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseMaterial.getRegOid());
               if(casematerial!=null && casematerial.getData()!=null && casematerial.getData().size()>0){
                 List<String> materialOids= casematerial.getData().stream().map(material->material.getCaseMaterialOid()).collect(Collectors.toList());
                 String strOids="";
                 for(String oid:materialOids){
                     strOids=strOids+oid+";";
                 }
                   caseMaterial.setSelectMaterialOids(strOids);
               }
                caseMaterial.setOutStatus(String.valueOf(SysCode.STATUS.YES));
                caseMaterial.setOutStockDate(new Date());
                caseMaterial.setModifyDate(new Date());
                caseMaterial.setOutStockUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                //由于获取当前登录人名称乱码，所以查询
                ApiResultSet<SysUser> sysuser=sysUserFeginService.getSysUserByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                caseMaterial.setOutUserName(sysuser.getData().getName());
                this.dbCaseMaterialOutOfStockMapper.updateByPrimaryKeySelective(caseMaterial);

            }
        }
    }

    public PageResult<CaseMaterialOutOfStock> queryListOutOfStockPage(List<String> listService, Integer pageNum, Integer pageSize){
        DbCaseMaterialOutOfStockExample dbCaseMaterialOutOfStockExample = new DbCaseMaterialOutOfStockExample();
        DbCaseMaterialOutOfStockExample.Criteria criteria = dbCaseMaterialOutOfStockExample.createCriteria();
        criteria.andOutStatusEqualTo("0");
        if(listService!=null && listService.size()>0){
            criteria.andServiceOidIn(listService);
        }
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Page<DbCaseMaterialOutOfStock> dbDbQlCases = (Page<DbCaseMaterialOutOfStock>)  dbCaseMaterialOutOfStockMapper.selectByExample(dbCaseMaterialOutOfStockExample);
        PageResult<CaseMaterialOutOfStock> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<CaseMaterialOutOfStock> qlCaseList = dbDbQlCases.stream().map(caseMaterial -> {
            CaseMaterialOutOfStock stock=new CaseMaterialOutOfStock();
            BeanUtils.copyProperties(caseMaterial,stock);
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseMaterial.getRegOid());
            QlCase qlCase = resultSet.getData();
            if (qlCase!= null) {
                stock.setServiceName(qlCase.getProjectName());
            }
            return stock;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

}

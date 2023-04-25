package com.zfsoft.single.manager.fzgl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.fzgl.CaseLicenseManage;
import com.zfsoft.single.data.fzgl.EmsParamManage;
import com.zfsoft.single.data.fzgl.ResponseOrderVo;
import com.zfsoft.single.dbaccess.dao.DbCaseLicenseDeliverRecordMapper;
import com.zfsoft.single.dbaccess.dao.fzgl.DbCaseLicenseManageMapper;
import com.zfsoft.single.dbaccess.data.DbCaseLicenseDeliverRecord;
import com.zfsoft.single.dbaccess.data.fzgl.DbCaseLicenseManage;
import com.zfsoft.single.dbaccess.data.fzgl.DbCaseLicenseManageExample;
import com.zfsoft.single.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 办件证照签发
 * dongxl
 */
@Service
@Slf4j
public class CaseLincenseManageManager {

    @Resource
    private DbCaseLicenseManageMapper dbCaseLicenseManageMapper;

    @Resource
    private DbCaseLicenseDeliverRecordMapper dbCaseLicenseDeliverRecordMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    /**
     * 查询证照签收列表
     * @param caseLicenseManage
     * @return
     */
    public List<CaseLicenseManage> queryPage(CaseLicenseManage caseLicenseManage) {

        DbCaseLicenseManageExample dbCaseLicenseManageExample = new DbCaseLicenseManageExample();
        DbCaseLicenseManageExample.Criteria criteria = dbCaseLicenseManageExample.createCriteria();
        if(null!=caseLicenseManage){
            if(StrUtil.isNotEmpty(caseLicenseManage.getCaseNumber().trim())){
                criteria.andCaseNumberLike("%"+caseLicenseManage.getCaseNumber().trim()+"%");
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getIdCard().trim())){
                criteria.andIdCardEqualTo(caseLicenseManage.getIdCard().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getRegOid().trim())){
                criteria.andRegOidEqualTo(caseLicenseManage.getRegOid().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getLicenseInStorage())&&caseLicenseManage.getLicenseInStorage().equals("1")){//签收
                criteria.andLicenseInStorageEqualTo(caseLicenseManage.getLicenseInStorage());
            }else{//查询未签收的
                criteria.andLicenseInStorageNotEqualTo(String.valueOf(SysCode.STATUS.YES));
            }
            if(!CurrentLoginUserHolder.getIsAdminUser()){
                if(caseLicenseManage.getServiceOids()!=null){
                    criteria.andServiceOidIn(Arrays.asList(caseLicenseManage.getServiceOids().split(",").clone()));

                }else{
                    return new ArrayList<CaseLicenseManage>();
                }
            }

            criteria.andDelFlagEqualTo(String.valueOf(SysCode.STATUS.NO));
            criteria.andLicenseStatusEqualTo("4");//签发状态

        }
        dbCaseLicenseManageExample.setOrderByClause(" CASE_REGISTER_DATE desc");
        List<DbCaseLicenseManage> dbCaseLicense=dbCaseLicenseManageMapper.selectByExample(dbCaseLicenseManageExample);
        return BeanUtils.copyListProperties(dbCaseLicense, CaseLicenseManage::new);
    }

    public CaseLicenseManage getOneByCaseOid(String caseOid) {
        Assert.hasLength(caseOid, "办件主键不能为空！");
        DbCaseLicenseManage dbLicense=this.dbCaseLicenseManageMapper.selectByCaseOid(caseOid);
        if(dbLicense!=null){
            CaseLicenseManage manage=new CaseLicenseManage();
            BeanUtils.copyProperties(dbLicense,manage);
            return manage;
        }
        return null;
    }

    /**
     * 材料出库保存更新信息
     * dongxl
     */
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateSign(CaseLicenseManage caseLicenseManage) {
        if(caseLicenseManage!=null){
            if(caseLicenseManage.getRegOid()!=null){
                DbCaseLicenseManage dbCaseLicense=this.dbCaseLicenseManageMapper.selectByCaseOid(caseLicenseManage.getRegOid());
                Assert.notNull(dbCaseLicense, MessageFormat.format("更新对象不存在！对象id为{0}", dbCaseLicense.getRegOid()));
                dbCaseLicense.setIsCms(caseLicenseManage.getIsCms());
                dbCaseLicense.setLicenseInStorage(String.valueOf(SysCode.STATUS.YES));
                dbCaseLicense.setDateOfIssue(new Date());//发证时间
                dbCaseLicense.setTypeOfIssue(caseLicenseManage.getDeliverWay());
                dbCaseLicense.setModifyDate(new Date());
                dbCaseLicense.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                dbCaseLicense.setSendWay(caseLicenseManage.getSendWay());
                dbCaseLicense.setCarType(caseLicenseManage.getCarType());
                if ("1".equals(caseLicenseManage.getSendWay())){
                    dbCaseLicense.setDeliverTarget(caseLicenseManage.getDeliverTarget());
                    dbCaseLicense.setDeliverCompany(caseLicenseManage.getDeliverCompany());
                    dbCaseLicense.setDeliverNumber(caseLicenseManage.getDeliverNumber());
                }
                dbCaseLicense.setSendPerson(caseLicenseManage.getSendPerson());
                dbCaseLicense.setSendPhone(caseLicenseManage.getSendPhone());
                dbCaseLicense.setOrganName(caseLicenseManage.getOrganName());
                dbCaseLicense.setSendTime(caseLicenseManage.getSendTime());
                this.dbCaseLicenseManageMapper.updateByPrimaryKeySelective(dbCaseLicense);
            }else{
                return "相关数据不能为空！";
            }
        }
        return null;
    }

    /**
     * 查询证照签发列表
     * @param caseLicenseManage
     * @return
     */
    public PageResult<CaseLicenseManage> queryPageListIssued(CaseLicenseManage caseLicenseManage, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum,pageSize);
        DbCaseLicenseManageExample dbCaseLicenseManageExample = new DbCaseLicenseManageExample();
        DbCaseLicenseManageExample.Criteria criteria = dbCaseLicenseManageExample.createCriteria();
        if(null!=caseLicenseManage){
            if(StrUtil.isNotEmpty(caseLicenseManage.getRegOid())){
                criteria.andRegOidEqualTo(caseLicenseManage.getRegOid().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getCaseNumber())){
                criteria.andCaseNumberLike("%"+caseLicenseManage.getCaseNumber().trim()+"%");
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getLicenseInStorage())){
                if(caseLicenseManage.getLicenseInStorage().equals(String.valueOf(SysCode.STATUS.NO))){//入库,代签发
                    //入库状态
                    criteria.andLicenseInStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                    //出库状态
                    criteria.andLicenseOutStorageNotEqualTo(String.valueOf(SysCode.STATUS.YES));
                }else {//出库
                    criteria.andLicenseOutStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                }
            }else{
                //入库状态
                criteria.andLicenseInStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                //出库状态
                criteria.andLicenseOutStorageIsNull();
            }
            criteria.andDelFlagEqualTo(String.valueOf(SysCode.STATUS.NO));
            if(!CurrentLoginUserHolder.getIsAdminUser()){
                if(caseLicenseManage.getServiceOids()!=null){
                    criteria.andServiceOidIn(Arrays.asList(caseLicenseManage.getServiceOids().split(",").clone()));
                }
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getIdCard())){
                criteria.andIdCardEqualTo(caseLicenseManage.getIdCard().trim());
            }
            //criteria.andLicenseStatusEqualTo("4");//签发状态
            /*if(CurrentLoginUserHolder.getLoginUserDataAuthority()==2){
                criteria.andCreateUserEqualTo(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
            }*/
        }
        dbCaseLicenseManageExample.setOrderByClause(" CASE_REGISTER_DATE desc");
        Page<DbCaseLicenseManage> dbCaseLicensePage = (Page<DbCaseLicenseManage>) dbCaseLicenseManageMapper.selectByExample(dbCaseLicenseManageExample);
        PageResult<CaseLicenseManage> pageResult = new PageResult<>(dbCaseLicensePage.getPageNum(), dbCaseLicensePage.getPageSize(), dbCaseLicensePage.getTotal());
        List<CaseLicenseManage> list =  dbCaseLicensePage.stream().map(dbCaseLicenseManage -> {
           CaseLicenseManage caseLicense =new CaseLicenseManage();
           BeanUtils.copyProperties(dbCaseLicenseManage,caseLicense);
           ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(dbCaseLicenseManage.getRegOid());
           QlCase qlCase = resultSet.getData();
           caseLicense.setServiceName(qlCase.getProjectName());
           return  caseLicense;
       }).collect(Collectors.toList());
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 保存快递信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateKd(String oid,String kdCode,String addresseeName,String addresseePhone) {
        if(StrUtil.isNotEmpty(oid)){
           DbCaseLicenseManage caseLicense= this.dbCaseLicenseManageMapper.selectByOid(oid);
           if(caseLicense!=null){
               caseLicense.setKdCode(kdCode);
               caseLicense.setLicenseOutStorage(String.valueOf(SysCode.STATUS.YES));
               caseLicense.setOutStorageDate(new Date());
               this.dbCaseLicenseManageMapper.updateByPrimaryKeySelective(caseLicense);
               //发送信息给申请人
           }else{
               return "未查询到相关信息！";
           }
        }else{
            return "主键不能为空！";
        }
        return null;
    }

    public CaseLicenseManage selectByCaseNumber(String caseNumber) {
        Assert.hasLength(caseNumber, "办件编号不能为空！");
        DbCaseLicenseManage dbLicense=this.dbCaseLicenseManageMapper.selectByCaseNumber(caseNumber);
        if(dbLicense!=null){
            CaseLicenseManage manage=new CaseLicenseManage();
            BeanUtils.copyProperties(dbLicense,manage);
            return manage;
        }
        return null;
    }

    public void saveOrUpdateCaseLicenseManage(CaseLicenseManage caseLicenseManage){
        caseLicenseManage.setOid(UUIDUtil.randomUUID());
        caseLicenseManage.setLicenseInStorage(String.valueOf(SysCode.STATUS.NO));
        caseLicenseManage.setModifyDate(new Date());
        caseLicenseManage.setCreateDate(new Date());
        caseLicenseManage.setDelFlag(String.valueOf(SysCode.STATUS.NO));
        caseLicenseManage.setLicenseStatus("4");
        caseLicenseManage.setLicenseOutStorage("0");
        caseLicenseManage.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        DbCaseLicenseManage dbCaseLicenseManage =new DbCaseLicenseManage();
        BeanUtils.copyProperties(caseLicenseManage,dbCaseLicenseManage);
        this.dbCaseLicenseManageMapper.insertSelective(dbCaseLicenseManage);
    }

    /**
     * 处理ems信息
     * @param result
     * @param emsParamManage
     * @return
     */
    public DbCaseLicenseDeliverRecord handleEmsInfo(String result, EmsParamManage emsParamManage) {
        log.info("result: {}", result);
        DbCaseLicenseDeliverRecord dbCaseLicenseDeliverRecord  = setDbCaseLicenseDeliverRecord(emsParamManage);
        String suc = JsonUtil.jsonToString(result, "success");
        if ("T".equals(suc)) {
            // 接口连接成功
            String responseOrders = JsonUtil.jsonToString(result, "responseOrders");
            @SuppressWarnings("unchecked")
            List<ResponseOrderVo> listResponseOrder = JsonUtil.jsonToCollection(responseOrders, ResponseOrderVo.class);
            // 此处做单个节点处理
            ResponseOrderVo responseOrder = listResponseOrder.get(0);
            dbCaseLicenseDeliverRecord.setWayBillNo(responseOrder.getTxLogisticID());
            if ("T".equals(responseOrder.getSuccess())) {
                dbCaseLicenseDeliverRecord.setIsSuccess("T");
                dbCaseLicenseDeliverRecord.setMailNo(responseOrder.getMailNo());
            } else {
                dbCaseLicenseDeliverRecord.setIsSuccess("F");
                dbCaseLicenseDeliverRecord.setErrorMsg(responseOrder.getErrorMsg());
                dbCaseLicenseDeliverRecord.setErrorCode(responseOrder.getErrorCode());
            }
        } else {
            // 接口连接失败
            dbCaseLicenseDeliverRecord.setIsSuccess("T");
            String errorMsg = JsonUtil.jsonToString(result, "errorMsg");
            // 失败信息
            dbCaseLicenseDeliverRecord.setErrorMsg(errorMsg);
            // 失败代码
            String errorCode = JsonUtil.jsonToString(result, "errorCode");
            dbCaseLicenseDeliverRecord.setErrorCode(errorCode);
        }

        // 保存接口调用信息
        if (StringUtils.isEmpty(dbCaseLicenseDeliverRecord.getOid())) {
            dbCaseLicenseDeliverRecord.setOid(UUID.randomUUID().toString().trim().replaceAll("-", ""));
            dbCaseLicenseDeliverRecordMapper.insert(dbCaseLicenseDeliverRecord);
        } else {
            dbCaseLicenseDeliverRecordMapper.update(dbCaseLicenseDeliverRecord);
        }
        return dbCaseLicenseDeliverRecord;
    }

    /**
     * 设置值
     * @param emsParamManage
     * @return
     */
    private DbCaseLicenseDeliverRecord setDbCaseLicenseDeliverRecord(EmsParamManage emsParamManage) {
        DbCaseLicenseDeliverRecord dbCaseLicenseDeliverRecord =null;
        DbCaseLicenseManage caseLicense= this.dbCaseLicenseManageMapper.selectByOid(emsParamManage.getOid());
        //办件信息
        if (caseLicense !=null) {
            dbCaseLicenseDeliverRecord = dbCaseLicenseDeliverRecordMapper.queryByCaseOid(caseLicense.getRegOid());
            if (dbCaseLicenseDeliverRecord ==null) {
                dbCaseLicenseDeliverRecord = new DbCaseLicenseDeliverRecord();
                dbCaseLicenseDeliverRecord.setCaseOid(caseLicense.getRegOid());
                dbCaseLicenseDeliverRecord.setCreateUser(caseLicense.getCreateUser());
            }
        }
        //收件人信息
        dbCaseLicenseDeliverRecord.setReceiveName(emsParamManage.getAddresseeName());
        dbCaseLicenseDeliverRecord.setReceivePhone(emsParamManage.getAddresseePhone());
        dbCaseLicenseDeliverRecord.setReceiveDetailAddress(emsParamManage.getAddresseeDetailAddress());
        dbCaseLicenseDeliverRecord.setReceiveAddress(emsParamManage.getAddresseeAddress());
        dbCaseLicenseDeliverRecord.setReceivePostCode(emsParamManage.getAddresseePostCode());
        dbCaseLicenseDeliverRecord.setReceiveTel(emsParamManage.getAddresseeTel());
        //寄件人信息
        dbCaseLicenseDeliverRecord.setSendPerson(emsParamManage.getSendePerson());
        dbCaseLicenseDeliverRecord.setSendCall(emsParamManage.getSenderCall());
        dbCaseLicenseDeliverRecord.setSendPhone(emsParamManage.getSenderPhone());
        dbCaseLicenseDeliverRecord.setSendAddress(emsParamManage.getSenderAddress());
        dbCaseLicenseDeliverRecord.setSendDetailAddress(emsParamManage.getSenderDetailAddress());
        dbCaseLicenseDeliverRecord.setSendMailCode(emsParamManage.getSenderMailCode());
        return dbCaseLicenseDeliverRecord;
    }

    /**
     *  ems请求
     * @param emsParamManage ems参数
     * @return
     */
    public String submitInfoToExpress(EmsParamManage emsParamManage) {
        // 获取授权码
        String result = "";
        DbCaseLicenseManage caseLicense= this.dbCaseLicenseManageMapper.selectByOid(emsParamManage.getOid());
        log.info("get caseLicense: {}", caseLicense);
        if (caseLicense !=null) {
            //测试
            //String authorization = EMSUtil.authorization_map.get("8a5107085fbaea01015fbaecc01b0005");
            ApiResultSet<SysUser> sysUserApiResultSet = sysUserFeginService.getSysUserByUserOid(caseLicense.getCreateUser());
            SysUser sysUser = sysUserApiResultSet.getData();
            String authorization = EMSUtil.authorization_map.get(sysUser.getDistrictOid());
            if (!StringUtils.isEmpty(authorization)) {
                // 处理数据信息
                Map<String, String> map = new HashMap<String, String>();
                // 获取收件人所在省市
                String[] receiveCityArr = emsParamManage.getAddresseeAddress().split("/");
                String receiveProv = receiveCityArr[0];
                String receiveCity = receiveCityArr[1];
                // 获取寄件人所在省市
                String[] sendCityArr = emsParamManage.getSenderAddress().split("/");
                String sendProv = sendCityArr[0];
                String sendCity = sendCityArr[1];
                // 生成物流订单号
                String txLogisticID = UUID.randomUUID().toString().trim().replaceAll("-", "");
                // 拼接运单详细信息
                String orderNormals = encapsulationInfo(emsParamManage, sendProv, sendCity, receiveProv, receiveCity, txLogisticID);
                log.info("orderNormals:  {}", orderNormals);
                map.put("authorization", authorization);
                map.put("orderNormal", orderNormals);
                result = EMSUtil.doGet(map);
            }
        }
        return result;
    }

    /**
     *  拼接运单详细信息（如果想要测试，数据后面加测试字样区分）
     * @param emsParamManage
     * @param sendProv
     * @param sendCity
     * @param receiveProv
     * @param receiveCity
     * @param txLogisticID
     * @return
     */
    private String encapsulationInfo(EmsParamManage emsParamManage, String sendProv, String sendCity, String receiveProv,
                                     String receiveCity, String txLogisticID) {
        StringBuffer orderNormals = new StringBuffer();
        orderNormals.append("{\"orderNormals\":[{\"sender\":{\"name\":\"");
        orderNormals.append(emsParamManage.getSendePerson());
        orderNormals.append("\",\"postCode\":\"");
        orderNormals.append(emsParamManage.getSenderMailCode());
        orderNormals.append("\",\"phone\":\"");
        orderNormals.append(emsParamManage.getSenderCall());
        orderNormals.append("\",\"mobile\":\"");
        orderNormals.append(emsParamManage.getSenderPhone());
        orderNormals.append("\",\"prov\":\"");
        orderNormals.append(sendProv);
        orderNormals.append("\",\"city\":\"");
        orderNormals.append(sendCity);
        orderNormals.append("\",\"address\":\"");
        orderNormals.append(emsParamManage.getSenderDetailAddress());
        orderNormals.append("\",\"remark\":\"");
        orderNormals.append("service");
        orderNormals.append("\"},\"receiver\":{\"name\":\"");
        orderNormals.append(emsParamManage.getAddresseeName());
        orderNormals.append("\",\"postCode\":\"");
        orderNormals.append(emsParamManage.getAddresseePostCode());
        orderNormals.append("\",\"phone\":\"");
        orderNormals.append(emsParamManage.getAddresseeTel());
        orderNormals.append("\",\"mobile\":\"");
        orderNormals.append(emsParamManage.getAddresseePhone());
        orderNormals.append("\",\"prov\":\"");
        orderNormals.append(receiveProv);
        orderNormals.append("\",\"city\":\"");
        orderNormals.append(receiveCity);
        orderNormals.append("\",\"address\":\"");
        orderNormals.append(emsParamManage.getAddresseeDetailAddress());
        orderNormals.append("\"},\"serviceType\":1,\"txLogisticID\":\"");
        orderNormals.append(txLogisticID);
        orderNormals.append("\"}]}");
        return orderNormals.toString();
    }

    public DbCaseLicenseDeliverRecord getDeliverRecordByCaseOid(String caseOid) {
        DbCaseLicenseDeliverRecord dbCaseLicenseDeliverRecord = dbCaseLicenseDeliverRecordMapper.queryByCaseOid(caseOid);
        return dbCaseLicenseDeliverRecord;
    }

    public List<CaseLicenseManage> queryIssuedList(CaseLicenseManage caseLicenseManage) {
        DbCaseLicenseManageExample dbCaseLicenseManageExample = new DbCaseLicenseManageExample();
        DbCaseLicenseManageExample.Criteria criteria = dbCaseLicenseManageExample.createCriteria();
        if(null!=caseLicenseManage){
            if(StrUtil.isNotEmpty(caseLicenseManage.getRegOid())){
                criteria.andRegOidEqualTo(caseLicenseManage.getRegOid().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getCaseNumber())){
                criteria.andCaseNumberEqualTo(caseLicenseManage.getCaseNumber().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getLicenseInStorage())){
                if(caseLicenseManage.getLicenseInStorage().equals(String.valueOf(SysCode.STATUS.NO))){//入库,代签发
                    //入库状态
                    criteria.andLicenseInStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                    //出库状态
                    criteria.andLicenseOutStorageNotEqualTo(String.valueOf(SysCode.STATUS.YES));
                }else {//出库
                    criteria.andLicenseOutStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                }
            }else{
                //入库状态
                criteria.andLicenseInStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                //出库状态
                criteria.andLicenseOutStorageIsNull();
            }
            criteria.andDelFlagEqualTo(String.valueOf(SysCode.STATUS.NO));
            if(!CurrentLoginUserHolder.getIsAdminUser()){
                if(caseLicenseManage.getServiceOids()!=null){
                    criteria.andServiceOidIn(Arrays.asList(caseLicenseManage.getServiceOids().split(",").clone()));
                }
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getIdCard())){
                criteria.andIdCardEqualTo(caseLicenseManage.getIdCard().trim());
            }
        }
        dbCaseLicenseManageExample.setOrderByClause(" CASE_REGISTER_DATE desc");
        List<DbCaseLicenseManage> dbCaseLicenseManages = dbCaseLicenseManageMapper.selectByExample(dbCaseLicenseManageExample);
        List<CaseLicenseManage> list =  dbCaseLicenseManages.stream().map(dbCaseLicenseManage -> {
            CaseLicenseManage caseLicense =new CaseLicenseManage();
            BeanUtils.copyProperties(dbCaseLicenseManage,caseLicense);
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(dbCaseLicenseManage.getRegOid());
            QlCase qlCase = resultSet.getData();
            caseLicense.setServiceName(qlCase.getProjectName());
            return  caseLicense;
        }).collect(Collectors.toList());
        return list;
    }

    public PageResult<CaseLicenseManage> queryIssuedPage(CaseLicenseManage caseLicenseManage, Integer pageNum, Integer pageSize) {
        DbCaseLicenseManageExample dbCaseLicenseManageExample = new DbCaseLicenseManageExample();
        DbCaseLicenseManageExample.Criteria criteria = dbCaseLicenseManageExample.createCriteria();
        if(null!=caseLicenseManage){
            if(StrUtil.isNotEmpty(caseLicenseManage.getRegOid())){
                criteria.andRegOidEqualTo(caseLicenseManage.getRegOid().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getCaseNumber())){
                criteria.andCaseNumberEqualTo(caseLicenseManage.getCaseNumber().trim());
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getLicenseInStorage())){
                if(caseLicenseManage.getLicenseInStorage().equals(String.valueOf(SysCode.STATUS.NO))){//入库,代签发
                    //入库状态
                    criteria.andLicenseInStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                    //出库状态
                    criteria.andLicenseOutStorageNotEqualTo(String.valueOf(SysCode.STATUS.YES));
                }else {//出库
                    criteria.andLicenseOutStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                }
            }else{
                //入库状态
                criteria.andLicenseInStorageEqualTo(String.valueOf(SysCode.STATUS.YES));
                //出库状态
                criteria.andLicenseOutStorageIsNull();
            }
            criteria.andDelFlagEqualTo(String.valueOf(SysCode.STATUS.NO));
            if(!CurrentLoginUserHolder.getIsAdminUser()){
                if(caseLicenseManage.getServiceOids()!=null){
                    criteria.andServiceOidIn(Arrays.asList(caseLicenseManage.getServiceOids().split(",").clone()));
                }
            }
            if(StrUtil.isNotEmpty(caseLicenseManage.getIdCard())){
                criteria.andIdCardEqualTo(caseLicenseManage.getIdCard().trim());
            }
        }
        dbCaseLicenseManageExample.setOrderByClause(" CASE_REGISTER_DATE desc");
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Page<DbCaseLicenseManage> dbDbQlCases = (Page<DbCaseLicenseManage>)  dbCaseLicenseManageMapper.selectByExample(dbCaseLicenseManageExample);
        PageResult<CaseLicenseManage> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<CaseLicenseManage> qlCaseList = dbDbQlCases.stream().map(dbCaseLicenseManage -> {
            CaseLicenseManage caseLicense =new CaseLicenseManage();
            BeanUtils.copyProperties(dbCaseLicenseManage,caseLicense);
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(dbCaseLicenseManage.getRegOid());
            QlCase qlCase = resultSet.getData();
            caseLicense.setServiceName(qlCase.getProjectName());
            return  caseLicense;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;

    }

}



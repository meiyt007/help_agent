package com.zfsoft.single.manager.fzgl;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.fzgl.LicenseIssued;
import com.zfsoft.single.dbaccess.dao.fzgl.DbCaseLicenseManageMapper;
import com.zfsoft.single.dbaccess.dao.fzgl.DbLicenseIssuedMapper;
import com.zfsoft.single.dbaccess.data.fzgl.DbCaseLicenseManage;
import com.zfsoft.single.dbaccess.data.fzgl.DbLicenseIssued;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 办件证照签发
 * dongxl
 */
@Service
@Slf4j
public class LicenseIssuedManager {

    @Resource
    private DbLicenseIssuedMapper dbLicenseIssuedMapper;
    @Resource
    private DbCaseLicenseManageMapper dbCaseLicenseManageMapper;
    @Resource
    private QlCaseService qlCaseServiceFeginService;

    /**
     * 办件业务主键查询信息
     * @param caseOid
     * @return
     */
    public LicenseIssued getLicenseIssuedByCaseOid(String caseOid) {
        Assert.hasLength(caseOid, "办件主键不能为空！");
        DbLicenseIssued dbLicense=this.dbLicenseIssuedMapper.selectByCaseOid(caseOid);
        if(dbLicense!=null){
            LicenseIssued manage=new LicenseIssued();
            BeanUtils.copyProperties(dbLicense,manage);
            return manage;
        }
        return null;
    }

    /**
     * 证照签发
     * dongxl
     */
    @Transactional(rollbackFor = Exception.class)
    public String saveLicenseIssued(LicenseIssued licenseIssued) {
        if(licenseIssued!=null){
            if(StrUtil.isEmpty(licenseIssued.getOid())){//新增
                licenseIssued.setCreateDate(new Date());
                licenseIssued.setOid(UUIDUtil.randomUUID());
                licenseIssued.setModifyDate(new Date());
                licenseIssued.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                licenseIssued.setCurrStep(String.valueOf(SysCode.STATUS.YES));
                DbLicenseIssued dbLicenseIssued=new DbLicenseIssued();
                BeanUtils.copyProperties(licenseIssued,dbLicenseIssued);
                dbLicenseIssuedMapper.insertSelective(dbLicenseIssued);
            }else{//修改
                DbLicenseIssued licenseRecord=dbLicenseIssuedMapper.selectByOid(licenseIssued.getOid());
                if(licenseRecord!=null){
                    licenseIssued.setCreateUser(licenseRecord.getCreateUser());
                    licenseIssued.setCreateDate(licenseRecord.getCreateDate());
                    licenseIssued.setCurrStep(String.valueOf(SysCode.STATUS.YES));
                    licenseIssued.setModifyDate(new Date());
                    BeanUtils.copyProperties(licenseIssued,licenseRecord);
                    dbLicenseIssuedMapper.updateByPrimaryKeySelective(licenseRecord);
                }
            }
        }
        return null;
    }

    //根据业务主键查询信息
    public LicenseIssued getLicenseIssuedByOid(String oid) {
        Assert.hasLength(oid, "业务主键不能为空！");
        DbLicenseIssued dbLicense=this.dbLicenseIssuedMapper.selectByOid(oid);
        if(dbLicense!=null){
            LicenseIssued manage=new LicenseIssued();
            BeanUtils.copyProperties(dbLicense,manage);
            return manage;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResultSet saveOrUpOutOfStock(String  oid,String barCode) {
        Map<String ,Object> map= new HashMap<>();
        if (StrUtil.isNotEmpty(oid)&&StrUtil.isNotEmpty(barCode)) {
            DbCaseLicenseManage licenseManage = this.dbCaseLicenseManageMapper.selectByOid(oid);
            if (licenseManage != null) {
                DbLicenseIssued licenseIssued = this.dbLicenseIssuedMapper.selectByCaseOid(licenseManage.getRegOid());
                    ApiResultSet<QlCase> qlcaseSet=qlCaseServiceFeginService.queryQlCaseByCaseOid(licenseManage.getRegOid());
                    if(qlcaseSet!=null&&qlcaseSet.getData()!=null){
                        if(barCode.trim().equals(qlcaseSet.getData().getCaseNumber())){
                            licenseManage.setLicenseOutStorage(String.valueOf(SysCode.STATUS.YES));
                            licenseManage.setOutStorageDate(new Date());
                            dbCaseLicenseManageMapper.updateByPrimaryKeySelective(licenseManage);
                            licenseIssued.setCurrStep("4");
                            this.dbLicenseIssuedMapper.updateByPrimaryKeySelective(licenseIssued);
                            map.put("outDate",licenseManage.getOutStorageDate());
                        }else {
                            map.put("result","未查询到相关信息!");
                        }
                    }else{
                        map.put("result","未查询到相关信息!");
                    }

            } else{
                map.put("result","未查询到相关信息!");
            }
        } else {
            map.put("result","未查询到相关信息!");
        }

        return new ApiResultSet(map);
    }


}



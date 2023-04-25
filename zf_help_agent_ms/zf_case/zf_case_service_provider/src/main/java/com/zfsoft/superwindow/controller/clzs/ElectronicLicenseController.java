package com.zfsoft.superwindow.controller.clzs;


import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.ElectronicLicense;
import com.zfsoft.superwindow.data.clzs.ElectronicLicenseElement;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicense;
import com.zfsoft.superwindow.manager.clzs.ElectronicLicenseElementManager;
import com.zfsoft.superwindow.manager.clzs.ElectronicLicenseManager;
import com.zfsoft.superwindow.service.clzs.ElectronicLicenseService;
import com.zfsoft.superwindow.util.ElectronicLicenseTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-04 10:49:29
 * @description: 材料目录控制层
 */
@Slf4j
@RestController
public class ElectronicLicenseController implements ElectronicLicenseService {

    @Resource
    private ElectronicLicenseManager electronicLicenseManager;

    @Resource
    private ElectronicLicenseElementManager electronicLicenseElementManager;

    @Override
    public ApiResultSet<PageResult> queryInfoWithPage(ElectronicLicense electronicLicense) {
        PageResult<ElectronicLicense> electronicLicensePageResult = electronicLicenseManager.queryInfoWithPage(electronicLicense.getElectronicLicenseName(), electronicLicense.getPageNum(), electronicLicense.getPageSize());
        ApiResultSet apiResultSet  = new ApiResultSet();
        apiResultSet.setData(electronicLicensePageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdate(ElectronicLicense electronicLicense) throws Exception {
        ApiResultSet apiResultSet = new ApiResultSet();
        electronicLicenseManager.saveOrUpdate(electronicLicense);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getInfoByOid(String oid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        ElectronicLicense electronicLicense = new ElectronicLicense();
        DbEntityElectronicLicense dbEntityElectronicLicense = electronicLicenseManager.getElectronicLicenseByOid(oid);
        List<ElectronicLicenseElement> subList=electronicLicenseElementManager.getElectronicLicenseElementList(oid);
        org.springframework.beans.BeanUtils.copyProperties(dbEntityElectronicLicense,electronicLicense);
        electronicLicense.setSubList(subList);
        apiResultSet.setData(electronicLicense);
        return apiResultSet;
    }

    @Override
    public ApiResultSet deleteByOid(String oid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        electronicLicenseManager.deleteByOid(oid);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getAllElectronicLicenseList() {
        ApiResultSet apiResultSet = new ApiResultSet();
        /*List<CardCatalogue>   cardCatalogueList= cardCatalogueManager.getAllCardCatalogueList();
        apiResultSet.setData(cardCatalogueList);*/
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryElectronicLicenseTree() {
        List<ElectronicLicenseTree>  electronicLicenseTrees=electronicLicenseManager.queryElectronicLicenseList(null);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(electronicLicenseTrees);
        return apiResultSet;
    }

    @Override
    public ApiResultSet checkHasRepeat(String oid, String code) {
        ApiResultSet apiResultSet = new ApiResultSet();
        String result = electronicLicenseManager.checkHasRepeat(oid, code);
        apiResultSet.setCode(200);
        apiResultSet.setData(result);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<ElectronicLicense>> queryElectronicLicenseListByBillOids(List<String> billOids) {
       List<ElectronicLicense> list= electronicLicenseManager.queryElectronicLicenseListByBillOids(billOids);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<ElectronicLicenseElement>> queryElectronicElementListByBillOids(List<String> billOids) {
       List<ElectronicLicenseElement> res= electronicLicenseElementManager.getElectronicElementListByBillOids(billOids);
        return new ApiResultSet<>(res);
    }

    @Override
    public ApiResultSet<ElectronicLicenseElement> queryElectronicElementListByOid(String oid) {
       ElectronicLicenseElement licenseElec= electronicLicenseElementManager.queryElectronicElementListByOid(oid);
        return new ApiResultSet<>(licenseElec);
    }

}




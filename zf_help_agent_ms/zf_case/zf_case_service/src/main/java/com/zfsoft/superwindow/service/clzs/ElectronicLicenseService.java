package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.ElectronicLicense;
import com.zfsoft.superwindow.data.clzs.ElectronicLicenseElement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 电子证照接口定义
 */
@RequestMapping("/electronicLicense")
public interface ElectronicLicenseService {
    /***
    * @Description:  获取证照目录分页列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [electronicLicenseName, pageNum, pageSize]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryInfoWithPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryInfoWithPage(@RequestBody ElectronicLicense electronicLicense);
   /***
   * @Description:  更新或保存证照目录
   * @Author:liangss
   * @Date:2021/11/5
   * @Param: [electronicLicense]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
   ApiResultSet saveOrUpdate(@RequestBody ElectronicLicense electronicLicense) throws Exception;

    /***
    * @Description:根据oid查询证照目录信息
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [oid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getInfoByOid", method = {RequestMethod.GET})
    ApiResultSet getInfoByOid(@RequestParam(value = "oid", required = false) String oid);

    /***
    * @Description: 根据oid删除卡证目录信息
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [oid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteByOid", method = {RequestMethod.GET})
    ApiResultSet deleteByOid(@RequestParam(value = "oid", required = false) String oid);

    /***
    * @Description: 查询所有卡证列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: []
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getAllElectronicLicenseList", method = {RequestMethod.GET})
    ApiResultSet  getAllElectronicLicenseList();


    /***
     * @Description: 证照目录树
     * @Author:liangss
     * @Date:2021/11/26
     * @Param: []
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryElectronicLicenseTree",method = {RequestMethod.GET})
    ApiResultSet queryElectronicLicenseTree();

    /***
     * @Description: 根据oid删除卡证目录信息
     * @Author:liangss
     * @Date:2021/11/5
     * @Param: [oid]
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkHasRepeat", method = {RequestMethod.GET})
    ApiResultSet checkHasRepeat(@RequestParam(value = "oid", required = false) String oid, @RequestParam(value = "code", required = false) String code);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryElectronicLicenseListByBillOids",method = {RequestMethod.GET})
    ApiResultSet<List<ElectronicLicense>> queryElectronicLicenseListByBillOids(@RequestParam(value = "billOids") List<String> billOids);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryElectronicElementListByBillOids",method = {RequestMethod.GET})
    ApiResultSet<List<ElectronicLicenseElement>> queryElectronicElementListByBillOids(@RequestParam(value = "billOids") List<String> billOids);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryElectronicElementListByOid",method = {RequestMethod.GET})
    ApiResultSet<ElectronicLicenseElement> queryElectronicElementListByOid(@RequestParam(value = "oid") String oid);


}


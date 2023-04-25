package com.zfsoft.single.service.insideapi.charge;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: IChargeService
 * @description: 办件收费接口
 * @author: wangwg
 * @date: 2021/06/11
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/chargeService")
public interface IChargeService {

    /**
     * 打开扫描枪
     *
     * @author wangwg
     * @date 2021-06-11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/openQRCodeScanningGun")
    ApiResultSet<String> openQRCodeScanningGun();


    /**
     * 关闭扫描枪
     *
     * @author wangwg
     * @date 2021-06-11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/closeQRCodeScanningGun")
    ApiResultSet<String> closeQRCodeScanningGun();

    /**
     * 激活扫描枪
     *
     * @author wangwg
     * @date 2021-06-11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/activeQRCodeScanningGun")
    ApiResultSet<String> activeQRCodeScanningGun();

    /**
     * 取消扫描枪
     *
     * @author wangwg
     * @date 2021-06-11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/cancleQRCodeScanningGun")
    ApiResultSet<String> cancleQRCodeScanningGun();


    /**
     * 推送扫描枪激活返回数据到redis
     *
     * @author wangwg
     * @date 2021-06-11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pushActiveScanningMessage")
    ApiResultSet<String> pushActiveScanningMessage(@RequestParam(value = "resEntity") String resEntity);

    /**
     * 获取扫描枪激活返回数据
     *
     * @author wangwg
     * @date 2021-06-11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getActiveScanningMessage")
    ApiResultSet<String> getActiveScanningMessage();
}

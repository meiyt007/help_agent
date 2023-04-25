
package com.zfsoft.single.service.ywbl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStock;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: dongxl
 * @create: 2020-11-06
 * @description: 办件材料出库
 */
@RequestMapping(value = "/caseMaterialOut")
public interface CaseMaterialOutOfStockService {

    /**
     * 查询办件材料出库列表
     *dongxl
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/pageList")
    ApiResultSet queryPageList(CaseMaterialOutOfStock caseMaterialOutOfStock, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 更新材料出库信息
     * dongxl
     * @param caseMaterialOutOfStock
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/updateCaseMaterialOutOfStock")
    ApiResultSet updateCaseMaterialOutOfStock(@RequestBody @Validated CaseMaterialOutOfStock caseMaterialOutOfStock);


    /**
     * 保存或者更新材料出库信息
     * dongxl
     * @param caseMaterialOutOfStock
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody @Validated CaseMaterialOutOfStock caseMaterialOutOfStock);

    /**
     * 根据办件编号查询出库信息
     * dongxl
     * @param CaseNumber
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/getOneByCaseNumber")
    ApiResultSet getOneByCaseNumber(String caseNumber);

    @ProcessFeignCalledResult
    @PostMapping(value = "/batchOutMaterial")
    ApiResultSet batchOutMaterial(String ids);

    @ProcessFeignCalledResult
    @PostMapping(value = "/printOutMaterial")
    ApiResultSet printOutMaterial(String ids);

    @ProcessFeignCalledResult
    @PostMapping(value = "/getOneMaterialInfo")
    ApiResultSet getOneMaterialInfo(Long id);


    @ProcessFeignCalledResult
    @PostMapping(value = "/getPrintOneMaterialInfo")
    ApiResultSet getPrintOneMaterialInfo(Long id);

    /**
     * 获取材料出库信息包括材料名称
     * @param caseNumber
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/getCaseMaterialOutByCaseNumber")
    ApiResultSet getCaseMaterialOutByCaseNumber(@RequestParam(value = "caseNumber") String caseNumber);



    /***
     * @Description:  保存材料流转记录用于材料流程打印
     * @Author:liangss
     * @Date:2022/1/13
     * @Param: [id]
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveCaseMaterialOutOfStockRecordByPrint")
    ApiResultSet saveCaseMaterialOutOfStockRecordByPrint(Long id);

}

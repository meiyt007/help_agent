package com.zfsoft.single.service.sxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.SxMaterialBill;
import com.zfsoft.single.data.yxpz.vo.SxMaterialBillVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @ClassName sxMaterialBillService
 * @Description 证照目录接口
 * @Author liangxm
 * @Date 2020-11-07
 * @Version V1.0
 **/
@RequestMapping("/combo/sxMaterialBill")
public interface SxMaterialBillService {

    /**
     * 证照目录查询List
  * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySxMaterialBill",method = {RequestMethod.GET})
    ApiResultSet<List<SxMaterialBillVo>> querySxMaterialBill();

    /**
     * 根据主键查询证照目录信息
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxMaterialBill>  getSxMaterialElmsConfigByoId(@PathVariable(value = "oid", required = false) String oid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxMaterialBillByMaterOid",method = {RequestMethod.GET})
    ApiResultSet<SxMaterialBill>  getSxMaterialBillByMaterOid(@PathVariable(value = "billOid", required = false) String billOid);

    }

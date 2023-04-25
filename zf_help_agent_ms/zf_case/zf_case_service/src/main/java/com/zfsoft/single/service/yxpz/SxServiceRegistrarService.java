
package com.zfsoft.single.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.SxServiceRegistrar;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxl
 * @create: 2020-10-26
 * @description: 授权登记人表
 */
@RequestMapping(value = "/sxServiceRegistrar")
public interface SxServiceRegistrarService{

    /**
     * 保存/更新
     * @param sxServiceRegistrar
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody @Validated SxServiceRegistrar sxServiceRegistrar);

    /**
     * 获取详情
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne", method = {RequestMethod.GET})
    ApiResultSet getOne(String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteServiceReg", method = {RequestMethod.DELETE})
    ApiResultSet<Integer> deleteServiceReg(String serviceOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteServiceUserAuth", method = {RequestMethod.DELETE})
    ApiResultSet<Integer> deleteServiceUserAuth(String userOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSysUserRegistrarList", method = {RequestMethod.GET})
    ApiResultSet getSysUserRegistrarList(String id);

    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdatePersonReg")
    ApiResultSet saveOrUpdatePersonReg(@RequestBody @Validated SxServiceRegistrar sxServiceRegistrar);

    @ProcessFeignCalledResult
    @PostMapping(value = "/getOneByUserOid")
    ApiResultSet getOneByUserOid(String id);

    @ProcessFeignCalledResult
    @PostMapping(value = "/queryServiceTree")
    ApiResultSet queryServiceTree();

    @ProcessFeignCalledResult
    @PostMapping(value = "/queryUserTree")
    ApiResultSet queryUserTree() ;


    @ProcessFeignCalledResult
    @GetMapping(value = "/sxServiceOidsListByUserOid")
    ApiResultSet sxServiceOidsListByUserOid();

    @ProcessFeignCalledResult
    @PostMapping(value = "/getServiceRegistrarByServiceOid")
    ApiResultSet getServiceRegistrarByServiceOid(String serviceOid);

}

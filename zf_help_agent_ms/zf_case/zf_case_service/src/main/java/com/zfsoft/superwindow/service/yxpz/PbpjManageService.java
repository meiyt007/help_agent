package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.PbpjManage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName PbpjManageService
 * @Description 平板设备组件服务定义接口
 * @Author liangxm
 * @Date 2020-10-24 11:33
 * @Version V1.0
 **/
@RequestMapping("/manage/pbpj")
public interface PbpjManageService {


    /**
     * @param pbpjManage 平板设备实体类
     * @description: 平板设备的新增或者修改
     * @author: liangxm
     * @Date: 2020/10/24 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<PbpjManage> savePbpjManage(@RequestBody PbpjManage pbpjManage);


    /**
     * @param name    平板设备名称
     * @param runCode 平板设备编码
     * @description: 查询平板设备的信息列表
     * @author: liangxm
     * @Date: 2020/10/24 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet<PageResult<PbpjManage>> queryPbpjManageWithPage(@RequestParam(value = "name") String name,
                                                                 @RequestParam(value = "runCode") String runCode,
                                                                 @RequestParam(value = "pageNum") Integer pageNum,
                                                                 @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * @description: 初始化平板设备的信息
     * @param id 平板设备实体类主键
     * @author: liangxm
     * @Date: 2020/10/24 10:14
     **/
    /*@ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initPbpjManage(@RequestParam(value="id",required=false)Long id);*/

    /**
     * 删除指定Id的平板设备信息
     *
     * @param id 平板设备id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deletePbpjManageById(@RequestParam("id") String id);


    /**
     * @param id 平板设备实体类主键
     * @description: 获取平板设备的信息
     * @author: liangxm
     * @Date: 2020/10/24 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne/{id}", method = {RequestMethod.GET})
    ApiResultSet<PbpjManage> getPbpjManageById(@RequestParam("id") String id);

    /**
     * 启禁用
     *
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/able/{id}", method = {RequestMethod.POST})
    int ablePbpjManage(@RequestParam("id") String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPbpjManageByUserCode", method = {RequestMethod.GET})
    ApiResultSet<PbpjManage> getPbpjManageByUserCode(@RequestParam("userCode") String userCode);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPbpjManageByRunCode", method = {RequestMethod.POST})
    ApiResultSet<PbpjManage> getPbpjManageByRunCode(@RequestParam("runCode") String runCode);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/updatePbpjManage", method = {RequestMethod.POST})
    int updatePbpjManage(@RequestBody PbpjManage pbpjManage);

}

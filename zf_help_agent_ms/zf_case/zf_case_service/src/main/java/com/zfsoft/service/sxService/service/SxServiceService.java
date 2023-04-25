package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SxServiceService
 * @Description 实施清单服务定义接口
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RequestMapping("/affair/sxService")
public interface SxServiceService {

    /**
     * @param serviceOid 业务主键
     * @description: 初始化实施清单的信息
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/viewSxService"}, method = {RequestMethod.GET})
    ApiResultSet viewSxService(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * @param serviceOid 业务主键
     * @description: 获取实施清单的信息
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceByOid/{serviceOid}", method = {RequestMethod.GET})
    ApiResultSet<SxService> getSxServiceByOid(@PathVariable("serviceOid") String serviceOid);
    /**
     * @param serviceOid 业务主键
     * @description: 获取事项关联推荐事项列表
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxRecommendServiceListByOid", method = {RequestMethod.POST})
    ApiResultSet<List<SxService>> getSxRecommendServiceListByOid(
            @RequestParam("serviceOid") String serviceOid
    );


    /**
     * 实施清单分页列表
     *
     * @param serviceName    实施清单名称
     * @param basicCode      基本编码
     * @param serviceTypeOid 事项类型OID
     * @param serviceStatus  状态
     * @param implementCode  实施编码
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sxServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxService>> querySxServiceWithPage(
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "basicCode", required = false) String basicCode,
            @RequestParam(value = "serviceTypeOid", required = false) String serviceTypeOid,
            @RequestParam(value = "serviceStatus", required = false) Integer serviceStatus,
            @RequestParam(value = "implementCode", required = false) String implementCode,
            @RequestParam(value = "districtOid", required = false) String districtOid,
            @RequestParam(value = "organOid", required = false) String organOid,
            @RequestParam(value = "existChildItem", required = false) String existChildItem,
            @RequestParam(value = "caseType", required = false) String caseType,
            @RequestParam(value = "handleType", required = false) String handleType,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 根据部门OID获取事项列表
     *
     * @param organOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServicList", method = {RequestMethod.GET})
    ApiResultSet<List<SxService>> getSxServicList(
            @RequestParam(value = "organOid", required = false) String organOid);

    /**
     * 根据部门OID获取事项列表
     *
     * @param
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServicListByDistrictOid", method = {RequestMethod.GET})
    ApiResultSet<List<SxService>> getSxServicListByDistrictOid(
            @RequestParam(value = "districtOid", required = false) String districtOid);


    /**
     * 根据serviceOid获取事项情形列表
     *
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceSituationList", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> getSxServiceSituationList(
            @RequestParam(value = "serviceOid", required = false) String serviceOid);


    /**
     * 根据serviceOid获取事项的 所有情形和所有选项
     *
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceSituationAndOptions", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> getSxServiceSituationAndOptions(
            @RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * 实施清单分页列表
     *
     * @param sxService
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxService>> querySxServicePage(@RequestBody SxService sxService,
                                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateSxServiceMaterialClassifier", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxServiceMaterialClassifier(@RequestBody SxService sxService);


    /**
     * 更新事项材料列表
     *
     * @param sxService
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateSxServiceMateria", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxServiceMaterial(@RequestBody SxService sxService);

    /**
     * 更新颗粒化材料列表
     *
     * @param sxService
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateServiceMaterial", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateServiceMaterial(@RequestBody SxService sxService);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/geSxServiceAndMaterial", method = {RequestMethod.GET})
    ApiResultSet geSxServiceAndMaterial(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    /**
     * 根据serviceOid获取所属事项的所有没有任何关系的选项标题(智能登记)
     *
     * @param serviceOid
     * @return
     * @author: wangwg
     * @Date: 2021/07/13
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceTitlesNoRelation", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> getSxServiceTitlesNoRelation(@RequestParam(value = "serviceOid") String serviceOid);


    /**
     * 根据serviceOid和选项值获取所属事项的所有有关系的选项标题(智能登记)
     *
     * @param serviceOid valOid
     * @return
     * @author: wangwg
     * @Date: 2021/07/23
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxServiceSituationRelation", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(@RequestParam(value = "serviceOid") String serviceOid,
                                                                             @RequestParam(value = "valOids") String valOids,
                                                                             @RequestParam(value = "currentOid") String currentOid,
                                                                             @RequestParam(value = "currentTitleOid") String currentTitleOid,
                                                                             @RequestParam(value = "rootTitleOid") String rootTitleOid);

    /**
     * 根据事项名称，模糊查询所有的事项
     *
     * @param
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceListBySxServiceName", method = {RequestMethod.GET})
    ApiResultSet<List<SxService>> getSxServiceListBySxServiceName(@RequestParam(value = "serviceName", required = false) String serviceName);

    /**
     * 保存事项信息
     * @param sxService
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveSxService", method = {RequestMethod.POST})
    ApiResultSet<SxService> saveSxService(@RequestBody SxService sxService);

    /**
     * 删除事项
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delSxServiceByOid/{serviceOid}", method = {RequestMethod.GET})
    ApiResultSet<Boolean> delSxServiceByOid(@PathVariable("serviceOid") String serviceOid);

    /**
     * 获取事项所需的字典值
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getDistInfo", method = {RequestMethod.GET})
    ApiResultSet<Map<String,Object>> getDistInfo();

    /**
     * 获取事项信息和扩展信息
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceAndExtend/{serviceOid}", method = {RequestMethod.GET})
    ApiResultSet<SxService> getSxServiceAndExtend(@PathVariable("serviceOid") String serviceOid);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxServiceSituationRelationBySituationAndTitle", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelationBySituationAndTitle(@RequestParam(value = "serviceOid") String serviceOid,
                                                                                                @RequestParam(value = "titleOid") String titleOid,

                                                                                                @RequestParam(value = "situationId") String situationId);
    /**
     * 获取当前事项的咨询人信息
     * @author chenyq
     * @date 20220323
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryConsultUser", method = {RequestMethod.GET})
    ApiResultSet queryConsultUser(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * 获取机构分组列表
     * @author wangyh
     * @Date: 2020/9/09 14:14
     */
    @RequestMapping(value = "/queryOrganSelectOptions", method = {RequestMethod.GET})
    ApiResultSet queryOrganSelectOptions();

    /**
     * 实施清单分页列表
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sxServiceList", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxService>> querySxServiceList(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);
}

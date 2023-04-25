package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangns
 * @description 选项标题表
 * @date 2020/11/3 11:09
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceOptionTitle")
public interface SxServiceOptionTitleService {

    /**
     * @description:  根据选项标题业务主健获取选项标题消息
     * @param oid 业务主键
     * @author: wangns
     * @Date: 2020/11/3 11:09
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceOptionTitleByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceOptionTitle> getSxServiceOptionTitleByOid(@PathVariable("oid") String oid);


    /**
     * @description:  根据选项标题业务主健该标题下的所有选项对应的有关联的标题
     * @param titleOid 标题主键
     * @author: wangwg
     * @Date: 2020/11/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceOptionAllTitleValRelation",method = {RequestMethod.GET})
    ApiResultSet<List<Map<String,Object>>> getSxServiceOptionAllTitleValRelation(@RequestParam("serviceOid") String serviceOid, @RequestParam("titleOid") String titleOid);

    /**
     * @description:  分页查询选项标题列表信息
     * @author: shimh
     * @Date: 2021/08/04
     * @param name 标题名称
     * @param serviceOid 事项oid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxServiceOptionTitlePag", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxServiceOptionTitle>> querySxServiceOptionTitlePag(@RequestParam(value = "name", required = false) String name,
                                                                                @RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                                                @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 查询事项下所有的标题和选项
     * @param serviceOid
     * * @author: xldong
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getListTitleAndOption",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> getListTitleAndOption(@RequestParam("serviceOid") String serviceOid);

    /**
     * 查询标题、选项、材料
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getListTitleAndOptionMaterial",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> getListTitleAndOptionMaterial(@RequestParam("serviceOid") String serviceOid);

    /**
     * 通过事项id获取选项关系数据 json格式
     * @param serviceOid
     * @param titleOid
     * @param valOid
     * @author xld
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getRelationJsonForPicture",method = {RequestMethod.GET})
    ApiResultSet<Map<String,Object>> getRelationJsonForPicture(@RequestParam("serviceOid") String serviceOid, @RequestParam("titleOid") String titleOid, @RequestParam("valOid") String valOid);

    /**
     * 初始化新增修改选项标题页面
     * @author shimh
     * @date 2021-08-05
     * @param titleOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/initSxServiceOptionTitle/{titleOid}",method = {RequestMethod.GET})
    ApiResultSet initSxServiceOptionTitle(@PathVariable("titleOid") String titleOid);

    /**
     * 保存选项标题信息
     * @author shimh
     * @date 2021-08-05
     * @param sxServiceOptionTitle
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSxServiceOptionTitle",method = {RequestMethod.POST})
    ApiResultSet saveSxServiceOptionTitle(@RequestBody SxServiceOptionTitle sxServiceOptionTitle);

    /**
     * 删除选项标题信息
     * @author shimh
     * @date 2021-08-05
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delSxServiceOptionTitleByOid/{oid}", method = {RequestMethod.GET})
    ApiResultSet delSxServiceOptionTitleByOid(@PathVariable("oid") String oid);


}

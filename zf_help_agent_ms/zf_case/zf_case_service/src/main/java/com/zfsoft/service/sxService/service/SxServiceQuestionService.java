package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxServiceQuestion;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SxServiceQuestionService
 * @Description 实施清单常见问题服务定义接口
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RequestMapping("/affair/sxServiceQuestion")
public interface SxServiceQuestionService {
    /**
     * @description:  根据业务主健获取实施清单常见问题
     * @param questionOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceQuestionByOid/{questionOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceQuestion>  getSxServiceQuestionByOid(@PathVariable("questionOid") String questionOid);

    /**
     * @description:  根据实施清单业务主健获取实施清单常见问题
     * @param serviceOid 业务主键
     * @author: wanwgwg
     * @Date: 2020/12/04
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceQuestionListByServiceOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceQuestion>>  getSxServiceQuestionListByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * 分页查询常见问题信息
     * @param sxServiceQuestion
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getQuestionList",method = {RequestMethod.POST})
    ApiResultSet pageList(SxServiceQuestion sxServiceQuestion,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 新增或修改实施清单常见问题信息
     * @param sxServiceQuestion
     * @return
     */
    @RequestMapping( value = "/saveOrUpdateSxServiceQuestion",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxServiceQuestion(@RequestBody SxServiceQuestion sxServiceQuestion);

    /**
     * 删除操作
     * @param id
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.GET})
    ApiResultSet delete(@RequestParam(value = "id", required = false) String id);

    /**
     * 查询详细信息
     * @param id
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/detail",method = {RequestMethod.GET})
    ApiResultSet getSxServiceQuestionById(@RequestParam(value = "id", required = false) String id);

    /**
     * 是否启用
     * @param questionOid
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able",method = {RequestMethod.GET})
    ApiResultSet enableQuestion(@RequestParam(value = "questionOid", required = false) String questionOid);

}

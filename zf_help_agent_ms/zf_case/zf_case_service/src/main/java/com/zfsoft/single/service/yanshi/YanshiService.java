package com.zfsoft.single.service.yanshi;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 演示所保存表单信息
 @dongxl
 */
@RequestMapping("/yanshi")
public interface YanshiService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> saveOrUpdateInfo(@RequestBody Object object);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCaseFormInfo", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> getCaseFormInfo(@RequestParam("caseOid") String caseOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/openRabot"}, method = {RequestMethod.GET})
    ApiResultSet openRabot();

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/colseRabot"}, method = {RequestMethod.GET})
    ApiResultSet colseRabot();

    @RequestMapping(value="/acceptedPrint", method = {RequestMethod.POST})
    ApiResultSet acceptedPrint(@RequestBody Object object);

    @GetMapping(value="/acceptedPrintDown")
    void acceptedPrintDown(@RequestParam(value = "filePath", required = false) String filePath);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCaseTjInfo", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Integer>> getCaseTjInfo();

    /**
     * 获取工作台代办
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getWorkTaskCase", method = {RequestMethod.GET})
    ApiResultSet getWorkTaskCase(@RequestParam(value = "type", required = false) Integer type,
                                 @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @GetMapping(value = "uploadDocuTemplate")
    ApiResultSet<SysAtta> uploadDocuTemplate(@RequestParam(value = "oid", required = false) String oid);

    /**
     * 打印告知承诺书
     * @return
     */
    @RequestMapping(value = "/printGzWord",method ={RequestMethod.POST} ,produces = "application/json;charset=UTF-8")
    ApiResultSet<String> printGzWord(@RequestBody Object object);



}

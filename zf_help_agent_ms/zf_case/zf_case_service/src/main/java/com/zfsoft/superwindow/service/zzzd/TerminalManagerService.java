package com.zfsoft.superwindow.service.zzzd;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.zzzd.TerminalInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ChangSheng
 * @Date 17:40 2022/5/24
 * @Description 自助终端操作
 **/
@RequestMapping("/terminal")
public interface TerminalManagerService {

    /**
     * 查看列表分页
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.POST})
    ApiResultSet<PageResult<TerminalInfo>> queryTerminalInfoPage(TerminalInfo terminalInfo,
                                                                 @RequestParam(value = "pageNum") Integer pageNum,
                                                                 @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 查看单个详细
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne", method = {RequestMethod.POST})
    ApiResultSet<TerminalInfo> getTerminalInfoById(@RequestParam("id") String id);

    /**
     * 新增
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<TerminalInfo> saveTerminalInfo(@RequestBody TerminalInfo terminalInfo);


    /**
     * 逻辑删除
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteTerminalInfoById(@RequestParam("id") String id);
}

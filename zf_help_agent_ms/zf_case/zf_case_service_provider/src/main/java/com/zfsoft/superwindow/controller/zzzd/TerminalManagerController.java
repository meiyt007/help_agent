package com.zfsoft.superwindow.controller.zzzd;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zzzd.TerminalInfo;
import com.zfsoft.superwindow.manager.zzzd.TerminalManager;
import com.zfsoft.superwindow.service.zzzd.TerminalManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author ChangSheng
 * @Date 17:39 2022/5/24
 * @Description 终端设备操作
 **/
@RestController
@Slf4j
public class TerminalManagerController implements TerminalManagerService {

    @Resource
    private TerminalManager terminalManager;

    /**
     * 获取自助终端分页数据
     * @param terminalInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<TerminalInfo>> queryTerminalInfoPage(TerminalInfo terminalInfo, Integer pageNum, Integer pageSize) {
        log.info("查询自助终端分页数据，参数：{}",terminalInfo);
        PageResult<TerminalInfo> pageResult = terminalManager.queryTerminalInfoWithPage(terminalInfo,pageNum,pageSize);
        ApiResultSet<PageResult<TerminalInfo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * 根据id获取终数据
     * @param id
     * @return
     */
    @Override
    public ApiResultSet<TerminalInfo> getTerminalInfoById(String id) {
        TerminalInfo terminalInfo = terminalManager.getTerminalInfoById(id);
        return new ApiResultSet<TerminalInfo>(terminalInfo);
    }

    /**
     * 保存自助终端数据
     * @param terminalInfo
     * @return
     */
    @Override
    public ApiResultSet<TerminalInfo> saveTerminalInfo(@RequestBody TerminalInfo terminalInfo) {
        log.info("修改或保存终端信息");
        ApiResultSet<TerminalInfo> apiResultSet = new ApiResultSet<>();
        int index = terminalManager.saveTerminalInfo(terminalInfo);
        if(index != 0){
            //说明有新增或修改
            apiResultSet.setCode(200);
        }else{
            apiResultSet.setCode(201);
        }
        return apiResultSet;
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @Override
    public ApiResultSet<Integer> deleteTerminalInfoById(String id) {
        log.info("删除终端信息：{}",id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        int index = terminalManager.deleteTerminalInfoById(id);
        if(index != 0){
            //说明有新增或修改
            apiResultSet.setCode(200);
        }else{
            apiResultSet.setCode(201);
        }
        return apiResultSet;
    }


}

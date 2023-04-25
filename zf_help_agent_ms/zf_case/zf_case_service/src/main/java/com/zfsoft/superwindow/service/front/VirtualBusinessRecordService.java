package com.zfsoft.superwindow.service.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.front.VirtualBusinessRecord;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/virtualBusinessRecord")
public interface VirtualBusinessRecordService {

    /**
     * 获取所有的荣誉
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/satisfactionList"}, method = {RequestMethod.GET})
    List getSatisfactionList();

    /**
     * 分页查询列表
     * @param virtualBusinessRecord
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/virtualBusinessRecordList"}, method = {RequestMethod.POST})
    ApiResultSet queryPageList(VirtualBusinessRecord virtualBusinessRecord,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 新增虚拟业务
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/saveVirtualBusinessRecord"}, method = {RequestMethod.POST})
    ApiResultSet saveVirtualBusinessRecord(@RequestBody VirtualBusinessRecord virtualBusinessRecord);

    /**
     * 查询虚拟业务
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getVirtualBusinessRecordByOid"}, method = {RequestMethod.GET})
    ApiResultSet getVirtualBusinessRecord(@RequestParam(value = "virtualBusinessNum", required = false) String virtualBusinessNum);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/uploadAudio"}, method = {RequestMethod.POST} , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResultSet uploadAudio(@RequestPart MultipartFile file,
                             @RequestParam(value = "virtualBusinessNum", required = false) String virtualBusinessNum);
}

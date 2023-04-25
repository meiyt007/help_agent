package com.zfsoft.superwindow.service.wgpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.wgpj.SaveCallRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/callRecordService")
public interface CallRecordService {
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveCallRecord", method = {RequestMethod.POST})
    ApiResultSet<String> SaveCallRecord(@RequestBody SaveCallRecord saveCallRecord);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/selectCallNums"}, method = {RequestMethod.GET})
    ApiResultSet selectCallNums();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySaveCallRecordByOid", method = {RequestMethod.GET})
    ApiResultSet querySaveCallRecordByOid(@RequestParam(value = "oid", required = true) String oid);

}

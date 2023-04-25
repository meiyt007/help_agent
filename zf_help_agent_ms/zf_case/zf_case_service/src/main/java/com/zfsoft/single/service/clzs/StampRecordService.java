package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.clzs.StampRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: StampRecordService
 * @description: 样表信息接口
 * @author: wangwg
 * @date: 2021-01-11
 */
@RequestMapping(value = "/stampRecord")
public interface StampRecordService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody StampRecord stampRecord);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getStampRecordByCataOidAndAttaOid", method = {RequestMethod.GET})
    ApiResultSet getStampRecordByCataOidAndAttaOid(@RequestParam(value = "cataOid", required = false) String cataOid,
                                                   @RequestParam(value = "attaOid", required = false) String attaOid);

}

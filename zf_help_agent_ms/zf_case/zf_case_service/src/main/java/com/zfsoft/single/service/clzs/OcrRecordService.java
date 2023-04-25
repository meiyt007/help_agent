package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.clzs.OcrRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @（#）: OcrRecordService
 * @description: 材材料识别记录表接口
 * @author: wangwg
 * @date: 2021-01-11
 */
@RequestMapping(value = "/ocrRecord")
public interface OcrRecordService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody OcrRecord ocrRecord) ;
}

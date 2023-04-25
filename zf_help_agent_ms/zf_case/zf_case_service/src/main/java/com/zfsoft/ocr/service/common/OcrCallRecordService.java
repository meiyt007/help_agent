package com.zfsoft.ocr.service.common;

import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
* @Description:  ocr接口调用记录
* @Author:liangss
* @Date:2021/12/31
* @Param:
*/
@RequestMapping("/ocrCallRecord")
public interface OcrCallRecordService {


   @ProcessFeignCalledResult
   @RequestMapping(value = "/saveOrUpdateOcrCallRecord", method = RequestMethod.POST)
   String saveOrUpdateOcrCallRecord(@RequestBody OcrCallRecord ocrCallRecord);


   @ProcessFeignCalledResult
   @RequestMapping(value = "/queryDbOcrCallRecordByMd5", method = {RequestMethod.GET})
   ApiResultSet queryDbOcrCallRecordByMd5(@RequestParam(value = "md5", required = false) String md5);



   @ProcessFeignCalledResult
   @RequestMapping(value = "/queryDbOcrCallRecordByMd5AndInterUrl", method = {RequestMethod.GET})
   ApiResultSet queryDbOcrCallRecordByMd5AndInterUrl(@RequestParam(value = "md5", required = false) String md5
           ,@RequestParam(value = "interUrl", required = false) String interUrl);


}



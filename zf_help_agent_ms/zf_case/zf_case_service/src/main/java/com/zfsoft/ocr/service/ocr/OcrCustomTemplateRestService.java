package com.zfsoft.ocr.service.ocr;

import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateResponse;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
* @Description:  ocr自定义模板识别
* @Author:liangss
* @Date:2021/10/27 
* @Param: 
*/
@RequestMapping("/ocr")
public interface OcrCustomTemplateRestService {

   /* @RequestLine("POST /ocrCustomTemplateRestService/customTemplate")*/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/ocrCustomTemplateRestService/customTemplate", method = RequestMethod.POST)
    OcrCustomTemplateResponse customTemplate(@RequestBody OcrCustomTemplateRequest ocrCustomTemplateRequest);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/getcs",method = {RequestMethod.GET})
    String getcs(@RequestParam(value="comboDireOid",required = false) String comboDireOid) throws Exception;

}

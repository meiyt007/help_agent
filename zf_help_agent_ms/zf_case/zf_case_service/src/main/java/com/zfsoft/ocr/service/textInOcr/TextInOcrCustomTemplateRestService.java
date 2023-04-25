package com.zfsoft.ocr.service.textInOcr;

import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/***
 * @Description: ocr自定义模板识别
 * @Author:liangss
 * @Date:2021/10/27
 * @Param:
 */
@RequestMapping("/textINocr")
public interface TextInOcrCustomTemplateRestService {

    /* @RequestLine("POST /ocrCustomTemplateRestService/customTemplate")*/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/textInOcrCustomTemplateRestService/customTextInOcr", method = RequestMethod.POST)
    TextInOcrResponse customTextInOcr(@RequestBody TextInOcrRequest textInOcrRequest);


}

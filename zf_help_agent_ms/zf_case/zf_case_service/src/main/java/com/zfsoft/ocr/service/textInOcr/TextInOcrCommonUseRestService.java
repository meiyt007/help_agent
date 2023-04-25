package com.zfsoft.ocr.service.textInOcr;

import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/***
* @Description:  ocr通用文字识别识别
* @Author:liangss
* @Date:2021/10/27 
* @Param: 
*/
@RequestMapping("/commonUseOcr")
public interface TextInOcrCommonUseRestService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/textInOcrCommonUseRestService/customCommonUseOcr", method = RequestMethod.POST)
    String customCommonUseOcr(@RequestBody TextInOcrRequest textInOcrRequest);



}

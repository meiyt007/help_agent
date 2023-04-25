package com.zfsoft.superwindow.feign.settings;

import com.zfsoft.superwindow.feign.settings.data.TextInOcrFormResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "${zfsoft.feign.ocr}", contextId = "textInOcrForm")
public interface TextInOcrFormFeignService{

    @RequestMapping(value = "/textInOcrForm/textInOcrFormService/textInOcrBusinessLicense", method = RequestMethod.POST)
    TextInOcrFormResponse textInOcrBusinessLicense(@RequestParam("base64ImgStr") List<String> base64ImgStr);

}

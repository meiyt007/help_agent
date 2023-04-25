package com.zfsoft.ocr.controller.textInOcr;


import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.ocr.manager.textInOcr.TextInOcrCustomTemplateRestManager;
import com.zfsoft.ocr.service.textInOcr.TextInOcrCustomTemplateRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/***
* @Description:  ocr自定义模板识别
* @Author:liangss
* @Date:2021/10/27 
* @Param: 
*/
@RestController
@Slf4j
public class TextInOcrCustomTemplateRestController implements TextInOcrCustomTemplateRestService {
    @Resource
    private TextInOcrCustomTemplateRestManager textInOcrCustomTemplateRestManager;
    @Override
    public TextInOcrResponse customTextInOcr(TextInOcrRequest textInOcrRequest) {
        TextInOcrResponse textInOcrResponse=textInOcrCustomTemplateRestManager.customTextInOcr(textInOcrRequest);
        return textInOcrResponse;
    }
  /*  @Resource
    private OcrCustomTemplateRestManager ocrCustomTemplateRestManager;

    
    @Override
    public OcrCustomTemplateResponse customTemplate(OcrCustomTemplateRequest ocrCustomTemplateRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCustomTemplateRestManager.customTemplate(ocrCustomTemplateRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public String getcs(String comboDireOid) {
        return "cs测试";
    }*/
}

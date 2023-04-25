package com.zfsoft.ocr.controller.textInOcr;


import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.ocr.manager.textInOcr.TextInOcrCertificateRestManager;
import com.zfsoft.ocr.service.textInOcr.TextInOcrCertificateRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/***
* @Description: TextInOcr卡证证照识别
* @Author:liangss
* @Date:2021/11/2
* @Param:
*/
@RestController
@Slf4j
public class TextInOcrCertificateRestController implements TextInOcrCertificateRestService {
    @Resource
    private TextInOcrCertificateRestManager textInOcrCertificateRestManager;

    @Override
    public TextInOcrResponse textInOcrCertificateRestOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        TextInOcrResponse textInOcrResponse= textInOcrCertificateRestManager.textInOcrCertificateRestOcr(textInOcrRequest);
        return textInOcrResponse;
    }

    @Override
    public TextInOcrResponse businessLicenseOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        TextInOcrResponse textInOcrResponse= textInOcrCertificateRestManager.businessLicenseOcr(textInOcrRequest);
        return textInOcrResponse;
    }

    @Override
    public TextInOcrResponse idCardOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        TextInOcrResponse textInOcrResponse= textInOcrCertificateRestManager.idCardOcr(textInOcrRequest);
        return textInOcrResponse;
    }
}

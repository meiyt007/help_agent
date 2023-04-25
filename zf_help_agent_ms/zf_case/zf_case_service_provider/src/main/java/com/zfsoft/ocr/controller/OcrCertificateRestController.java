package com.zfsoft.ocr.controller;


import com.zfsoft.ocr.data.pojo.ocr.HouseholdRegisterRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrBusinessLicenseRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateResponse;
import com.zfsoft.ocr.data.pojo.ocr.OcrIdcardRequest;
import com.zfsoft.ocr.manager.ocr.OcrCertificateRestManager;
import com.zfsoft.ocr.service.ocr.OcrCertificateRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/***
* @Description: ocr卡证证照识别
* @Author:liangss
* @Date:2021/10/27 
* @Param: 
*/
@RestController
@Slf4j
public class OcrCertificateRestController implements OcrCertificateRestService {
    @Resource
    private OcrCertificateRestManager ocrCertificateRestManager;

    @Override
    public OcrCustomTemplateResponse businessLicense(OcrBusinessLicenseRequest ocrBusinessLicenseRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCertificateRestManager.businessLicense(ocrBusinessLicenseRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public OcrCustomTemplateResponse idcardFront(OcrIdcardRequest ocrIdcardRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCertificateRestManager.idcardFront(ocrIdcardRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public OcrCustomTemplateResponse idcardBack(OcrIdcardRequest ocrIdcardRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCertificateRestManager.idcardBack(ocrIdcardRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public OcrCustomTemplateResponse tempIdcard(OcrIdcardRequest ocrIdcardRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCertificateRestManager.tempIdcard(ocrIdcardRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public OcrCustomTemplateResponse householdRegister(HouseholdRegisterRequest householdRegisterRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCertificateRestManager.householdRegister(householdRegisterRequest);
        return ocrCustomTemplateResponse;
    }

    
   /* @Override
    public OcrCustomTemplateResponse customTemplate(OcrCustomTemplateRequest ocrCustomTemplateRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCustomTemplateRestManager.customTemplate(ocrCustomTemplateRequest);
        return ocrCustomTemplateResponse;
    }*/


}

package com.zfsoft.ocr.service.ocr;

import com.zfsoft.ocr.data.pojo.ocr.HouseholdRegisterRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrBusinessLicenseRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateResponse;
import com.zfsoft.ocr.data.pojo.ocr.OcrIdcardRequest;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/***
* @Description: ocr卡证证照识别实现类
* @Author:liangss
* @Date:2021/10/28
* @Param:
*/
@RequestMapping("/ocrCertificateRest")
public interface OcrCertificateRestService {


   /* @ProcessFeignCalledResult
    @RequestMapping( value = "/getcs",method = {RequestMethod.GET})
    String getcs(@RequestParam(value="comboDireOid",required = false) String comboDireOid);*/


   /***
   * @Description:营业执照识别
   * @Author:liangss
   * @Date:2021/10/28
   * @Param: [ocrBusinessLicenseRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/ocrCertificateRestService/businessLicense", method = RequestMethod.POST)
   OcrCustomTemplateResponse businessLicense(@RequestBody OcrBusinessLicenseRequest ocrBusinessLicenseRequest);

   /***
   * @Description: 身份证头像面识别
   * @Author:liangss
   * @Date:2021/10/28
   * @Param: [ocrIdcardRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/ocrCertificateRestService/idcardFront", method = RequestMethod.POST)
   OcrCustomTemplateResponse idcardFront(@RequestBody OcrIdcardRequest ocrIdcardRequest);

   /***
   * @Description: 身份证国徽面识别
   * @Author:liangss
   * @Date:2021/10/28
   * @Param: [ocrIdcardRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/ocrCertificateRestService/idcardBack", method = RequestMethod.POST)
   OcrCustomTemplateResponse idcardBack(@RequestBody OcrIdcardRequest ocrIdcardRequest);

   /***
   * @Description:临时身份证、复印件识别
   * @Author:liangss
   * @Date:2021/10/28
   * @Param: [ocrIdcardRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/ocrCertificateRestService/tempIdcard", method = RequestMethod.POST)
   OcrCustomTemplateResponse tempIdcard(@RequestBody OcrIdcardRequest ocrIdcardRequest);

   /***
   * @Description:户口本识别
   * @Author:liangss
   * @Date:2021/10/28
   * @Param: [householdRegisterRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/ocrCertificateRestService/householdRegister", method = RequestMethod.POST)
   OcrCustomTemplateResponse householdRegister(@RequestBody HouseholdRegisterRequest householdRegisterRequest);
}



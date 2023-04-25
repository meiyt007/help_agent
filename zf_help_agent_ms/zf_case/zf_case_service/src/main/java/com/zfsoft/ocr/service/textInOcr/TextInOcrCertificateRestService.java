package com.zfsoft.ocr.service.textInOcr;

import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/***
* @Description:textInOcr卡证证照识别实现类
* @Author:liangss
* @Date:2021/11/2
* @Param:
*/
@RequestMapping("/textInOcrCertificateRest")
public interface TextInOcrCertificateRestService {

   /***
   * @Description:根据卡证类别进行ocr识别
   * @Author:liangss
   * @Date:2021/11/3
   * @Param: [textInOcrRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/textInOcrCertificateRestService/textInOcrCertificateRestOcr", method = RequestMethod.POST)
   TextInOcrResponse textInOcrCertificateRestOcr(@RequestBody TextInOcrRequest textInOcrRequest) throws Exception;

   /***
   * @Description: 营业执照ocr识别
   * @Author:liangss
   * @Date:2021/11/3
   * @Param: [textInOcrRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/textInOcrCertificateRestService/businessLicenseOcr", method = RequestMethod.POST)
   TextInOcrResponse businessLicenseOcr(@RequestBody TextInOcrRequest textInOcrRequest) throws Exception;

   /***
   * @Description:身份证ocr识别
   * @Author:liangss
   * @Date:2021/11/3
   * @Param: [textInOcrRequest]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/textInOcrCertificateRestService/idCardOcr", method = RequestMethod.POST)
   TextInOcrResponse idCardOcr(@RequestBody TextInOcrRequest textInOcrRequest) throws Exception;
}



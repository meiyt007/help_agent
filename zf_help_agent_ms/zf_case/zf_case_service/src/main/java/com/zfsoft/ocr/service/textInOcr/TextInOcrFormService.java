package com.zfsoft.ocr.service.textInOcr;


import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrFormResponse;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/***
 * @Description:textInOcr卡证证照识别实现类对接表单服务
 * @Author:wangyg
 * @Date:2022/6/1
 */
@RequestMapping("/textInOcrForm")
public interface TextInOcrFormService {

    /***
     * @Description:textInOcr身份证
     * @Author:wangyg
     * @Date:2022/6/1
     * @param base64ImgStr 身份证图片base64编码数据集合
     * @param side face:正面，back:反面
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/textInOcrFormService/textInOcrIdCard", method = RequestMethod.POST)
    @CrossOrigin
    TextInOcrFormResponse textInOcrIdCard(@RequestParam("base64ImgStr") List<String> base64ImgStr,
                                          @RequestParam(value = "side", required = false) String side);

    /***
     * @Description:textInOcr身份证
     * @Author:wangyg
     * @Date:2022/6/1
     * @param base64ImgStr 营业执照base64编码数据集合
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/textInOcrFormService/textInOcrBusinessLicense", method = RequestMethod.POST)
    @CrossOrigin
    TextInOcrFormResponse textInOcrBusinessLicense(@RequestParam("base64ImgStr") List<String> base64ImgStr);

    /***
     * @Description:textInOcr身份证
     * @Author:wangyg
     * @Date:2022/6/1
     * @param base64ImgStr 户口本base64编码数据集合
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/textInOcrFormService/textInOcrHouseholdRegister", method = RequestMethod.POST)
    @CrossOrigin
    TextInOcrFormResponse textInOcrHouseholdRegister(@RequestParam("base64ImgStr") List<String> base64ImgStr);
}

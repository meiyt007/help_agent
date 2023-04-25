package com.zfsoft.ocr.data.pojo.ocr;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/***
* @Description: ocr卡证识别请求信息
* @Author:liangss
* @Date:2021/10/29
* @Param:
*/
@ApiModel(description = "ocr卡证识别请求信息")
public class OcrCardRequest extends BaseRequest {

    @ApiModelProperty(value = "图片base64字符串", required = true)
    private String imgBase64;

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

}

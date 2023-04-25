package com.zfsoft.ocr.data.pojo.textInOcr;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/***
* @Description: TextInOcr卡证识别请求信息
* @Author:liangss
* @Date:2021/11/1
* @Param:
*/
@ApiModel(description = "TextInOcrocr卡证识别请求信息")
@Data
public class TextInOcrRequest extends BaseRequest {

    @ApiModelProperty(value = "图片base64字符串", required = true)
    private String imgBase64;
    @ApiModelProperty(value = "图片地址", required = true)
    private String imgUrl;
    @ApiModelProperty(value = "模板id", required = true)
    private String id;
    @ApiModelProperty(value = "卡证类型", required = true)
    private String type;
    @ApiModelProperty(value = "图片md5", required = true)
    private String imgMd5;

}

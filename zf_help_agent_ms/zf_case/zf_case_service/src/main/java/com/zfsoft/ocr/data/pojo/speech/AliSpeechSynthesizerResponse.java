package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author CF
 * @Date 2020/2/12 13:08
 */
@Data
@ApiModel(description = "阿里云智能语音交互服务 - 语音合成接口响应信息")
public class AliSpeechSynthesizerResponse extends BaseResponse {

    @ApiModelProperty(value = "合成语音base64")
    private String speechBase64;
}

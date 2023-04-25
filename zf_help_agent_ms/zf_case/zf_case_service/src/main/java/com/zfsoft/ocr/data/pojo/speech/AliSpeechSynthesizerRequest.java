package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author CF
 * @Date 2020/2/12 11:31
 */
@Data
@ApiModel(description =  "阿里云智能语音交互服务 - 语音合成接口请求参数")
public class AliSpeechSynthesizerRequest extends BaseRequest {

    @ApiModelProperty(value = "语音合成的文本", required = true,example = "欢迎使用智能政务服务工作台")
    private String speechText;

    @ApiModelProperty(value = "合成语音类型：1 PCM；2 WAV；3 MP3。默认为MP3", required = false)
    private String speechFormat;

    @ApiModelProperty(value = "发音人", required = false)
    private String voicePerson;

    @ApiModelProperty(value = "合成语音语调。范围是-500~500，默认是0", required = false)
    private Integer pitchRate;

    @ApiModelProperty(value = "合成语音语速。范围是-500~500，默认是0", required = false)
    private Integer speechRate;

    @ApiModelProperty(value = "合成语音音量，范围是0~100，默认50", required = false)
    private Integer volume;

}

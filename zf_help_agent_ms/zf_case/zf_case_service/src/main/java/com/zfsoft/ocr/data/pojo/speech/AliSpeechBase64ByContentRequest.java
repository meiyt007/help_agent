package com.zfsoft.ocr.data.pojo.speech;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 阿里云-文字转语音base64请求信息
 * @Auther chenbw
 * @Date 2020/3/4 15:33
 * @Copyright 上海卓繁信息技术股份有限公司版权所有 .
 */
@ApiModel(description = "")
public class AliSpeechBase64ByContentRequest extends BaseRequest {

    /**
     * 待合成的文本，需要为UTF-8编码。使用GET方法，需要再采用RFC 3986规范进行urlencode编码，比如加号 + 编码为 %2B；使用POST方法不需要urlencode编码。
     */
    @ApiModelProperty(value = "待合成的文本", required = true)
    private String text;


    /**
     * 音频编码格式，支持的格式：pcm、wav、mp3，默认是pcm
     */
    @ApiModelProperty(value = "音频编码格式，支持的格式：pcm、wav、mp3")
    private String format;

    /**
     * 音频采样率，支持16000Hz、8000Hz，默认是16000Hz
     */
    @ApiModelProperty(value = "音频采样率，支持16000、8000")
    private Integer sampleRate;

    /**
     * 发音人，默认是xiaoyun，其他发音人名称请在简介中选择
     */
    @ApiModelProperty(value = "发音人")
    private String voice;

    /**
     * 音量，范围是0~100，默认50
     */
    @ApiModelProperty(value = "音量")
    private Integer volume;

    /**
     * 语速，范围是-500~500，默认是0
     */
    @ApiModelProperty(value = "语速，范围是-500~500，默认是0")
    private Integer speechRate;

    /**
     * 语调，范围是-500~500，可选，默认是0
     */
    @ApiModelProperty(value = "语调，范围是-500~500，可选，默认是0")
    private Integer pitchRate;

    /**
     * Gets the value of text.
     *
     * @return the value of text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     *
     * <p>You can use getText() to get the value of text</p>
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the value of format.
     *
     * @return the value of format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format.
     *
     * <p>You can use getFormat() to get the value of format</p>
     *
     * @param format format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets the value of sampleRate.
     *
     * @return the value of sampleRate
     */
    public Integer getSampleRate() {
        return sampleRate;
    }

    /**
     * Sets the sampleRate.
     *
     * <p>You can use getSampleRate() to get the value of sampleRate</p>
     *
     * @param sampleRate sampleRate
     */
    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * Gets the value of voice.
     *
     * @return the value of voice
     */
    public String getVoice() {
        return voice;
    }

    /**
     * Sets the voice.
     *
     * <p>You can use getVoice() to get the value of voice</p>
     *
     * @param voice voice
     */
    public void setVoice(String voice) {
        this.voice = voice;
    }

    /**
     * Gets the value of volume.
     *
     * @return the value of volume
     */
    public Integer getVolume() {
        return volume;
    }

    /**
     * Sets the volume.
     *
     * <p>You can use getVolume() to get the value of volume</p>
     *
     * @param volume volume
     */
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    /**
     * Gets the value of speechRate.
     *
     * @return the value of speechRate
     */
    public Integer getSpeechRate() {
        return speechRate;
    }

    /**
     * Sets the speechRate.
     *
     * <p>You can use getSpeechRate() to get the value of speechRate</p>
     *
     * @param speechRate speechRate
     */
    public void setSpeechRate(Integer speechRate) {
        this.speechRate = speechRate;
    }

    /**
     * Gets the value of pitchRate.
     *
     * @return the value of pitchRate
     */
    public Integer getPitchRate() {
        return pitchRate;
    }

    /**
     * Sets the pitchRate.
     *
     * <p>You can use getPitchRate() to get the value of pitchRate</p>
     *
     * @param pitchRate pitchRate
     */
    public void setPitchRate(Integer pitchRate) {
        this.pitchRate = pitchRate;
    }
}

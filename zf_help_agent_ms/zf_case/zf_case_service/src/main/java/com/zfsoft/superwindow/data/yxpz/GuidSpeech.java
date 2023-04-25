package com.zfsoft.superwindow.data.yxpz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 引导语音管理
 */
@Data
public class GuidSpeech {

    public interface INSERT_GROUP{};

    public interface UPDATE_GROUP{};
    @NotNull(message = "主键不能为空",groups = {UPDATE_GROUP.class})
    private String oid;

    @NotNull(message = "引导语名称不能为空")
    private String guideSpeechName;

    /**
     引导语编码
     */
    @NotNull(message = "引导语编码不能为空")
    private String guideSpeechCode;

    /**
     语音生成方式  0:接口生成  1:上传文件
     */
    @NotNull(message = "语音生成方式不能为空")
    private String buildType;
    /**
     * 引导语内容
     */
    private String guideSpeechContent;
    /**
     * 引导语音转换状态  Y:完成转换  N:未完成转换
     */
    private String convertStatus;

    /**
     备注
     */
    private String guideSpeechMemo;

    /**
     附件主键
     */
    private String attaOid;


    private Integer sort;

    private String deleteStatus;

    private Date modifyDate;

    private Date createDate;
    //语音附件地址
    private String speechUrl;


}

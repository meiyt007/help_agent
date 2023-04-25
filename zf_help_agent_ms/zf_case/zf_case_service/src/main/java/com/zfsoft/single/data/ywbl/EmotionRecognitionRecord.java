package com.zfsoft.single.data.ywbl;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 情绪分析记录实体类
 *
 * @author wangwg
 * @since 2021-06-17
 */
@Data
@ToString
public class EmotionRecognitionRecord {

    private Long id;
    /**
     * 办件人编码
     */
    private String code;
    /**
     * 第几张图片
     */
    private Integer photoNum;
    /**
     * 情绪（angre后悔 disgust厌恶 fear害怕 happy高兴 neutral中立 sad悲伤 surprise惊讶）
     */
    private String emotion;
    /**
     * 置信程度
     */
    private String emotionDegree;

    private String emotionTop;

    private String emotionRight;

    private String emotionBottom;

    private String emotionLeft;

    private Date createDate;

    private String createUser;
}

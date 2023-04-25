package com.zfsoft.superwindow.data.wgpj;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/13 9:20
 */

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

    //add
    private String  score;//情绪分数

    private String  virtualBusinessOid;//虚拟业务记录表主键

    private String  location;//坐标

    private Integer types;//0 上传到服务器，1 不上传

    private String   picAddress;//图片地址

    private String   caseOid;//办件表主键

}


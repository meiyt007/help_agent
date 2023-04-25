package com.zfsoft.superwindow.util.fa;

import lombok.Data;
import org.opencv.core.Mat;

/**
 * @author: liangss
 * @create: 2020-11-03 15:40:29
 * @description: 图片切割规则
 */
@Data
public class PictureCutRuleNew {
    private static final long serialVersionUID = -1908983316054320584L;

    /**
     * 规则编号
     */
    private String oid;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 规则编码
     */
    private String code;
    /**
     * 起点X坐标
     */
    private int startPosX;
    /**
     * 起点Y坐标
     */
    private int startPosY;
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 切割规则(1-印刷、2-手写、3-图像、4-选择框（印刷）、5-选择框（手写）)
     */
    private String type;
    /**
     * 选框值
     */
    private String marqueeValue;
    /**
     * 选框处理的json值
     */
    private String marqueeJsonValue;
    /**
     * 结果文字
     */
    private String resultText;
    /**
     * 结果图片
     */
    private String resultPic;
    /**
     * 结果图片对象
     */
    private Mat resultMat;


    /**
     * 默认构造函数
     */
    public PictureCutRuleNew() {
        super();
    }


    /**
     * 创建图片分割规则
     *
     * @param type 切割规则
     */
    public PictureCutRuleNew(String type) {
        super();
        this.type = type;
    }

    /**
     * 创建图片分割规则
     *
     * @param oid       规则编号
     * @param name      规则名称
     * @param startPosX 顶点X坐标
     * @param startPosY 顶点Y坐标
     * @param width     宽
     * @param height    高
     * @param type      切割规则
     */
    public PictureCutRuleNew(String oid, String code, String name, int startPosX, int startPosY, int width, int height,
                          String type) {
        super();
        this.oid = oid;
        this.code = code;
        this.name = name;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /**
     * 创建图片分割规则
     *
     * @param oid              规则编号
     * @param name             规则名称
     * @param startPosX        顶点X坐标
     * @param startPosY        顶点Y坐标
     * @param width            宽
     * @param height           高
     * @param type             切割规则
     * @param marqueeValue     选框值
     * @param marqueeJsonValue 选框处理的json值
     */
    public PictureCutRuleNew(String oid, String code, String name, int startPosX, int startPosY, int width, int height,
                          String type, String marqueeValue, String marqueeJsonValue) {
        super();
        this.oid = oid;
        this.code = code;
        this.name = name;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.width = width;
        this.height = height;
        this.type = type;
        this.marqueeValue = marqueeValue;
        this.marqueeJsonValue = marqueeJsonValue;
    }


    /**
     * 创建图片分割规则
     *
     * @param name      规则名称
     * @param startPosX 顶点X坐标
     * @param startPosY 顶点Y坐标
     * @param width     宽
     * @param height    高
     */
    public PictureCutRuleNew(String name, int startPosX, int startPosY, int width, int height) {
        super();
        this.name = name;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.width = width;
        this.height = height;
    }

    /**
     * 顶点以及宽高的构造函数
     *
     * @param startPosX 顶点X坐标
     * @param startPosY 顶点Y坐标
     * @param width     宽
     * @param height    高
     */
    public PictureCutRuleNew(int startPosX, int startPosY, int width, int height) {
        super();
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.width = width;
        this.height = height;
    }

    /**
     * 创建图片分割规则 （注意！只适用于目录识别为证照类型、百度自定义模板）
     *
     * @param oid       规则编号
     * @param code      规则编码
     * @param name      规则名称
     */
    public PictureCutRuleNew(String oid, String code, String name) {
        super();
        this.oid = oid;
        this.code = code;
        this.name = name;

    }

}

package com.zfsoft.service.sxSituation.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 选项关系图
 * xldong
 */
@Data
@ToString
public class ServiceOptionLineDto {
    /**
     * 起点
     */
    private String from;
    /**
     * 终点
     */
    private String to;
    /**
     * 起点x 坐标
     */
    private double fromX;
    /**
     * 起点y 坐标
     */
    private double fromY;
    /**
     * 终点x 坐标
     */
    private double toX;
    /**
     * 终点y 坐标
     */
    private double toY;

    /**
     * 线条颜色
     */
    private String color;


}

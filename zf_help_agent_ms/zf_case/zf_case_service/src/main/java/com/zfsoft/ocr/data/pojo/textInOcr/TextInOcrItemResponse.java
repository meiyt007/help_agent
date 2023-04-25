package com.zfsoft.ocr.data.pojo.textInOcr;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/***
* @Description:  合合识别信息
* @Author:liangss
* @Date:2021/11/1
* @Param:
*/
@ApiModel(description = "合合ocr识别，识别区块信息")
@Data
public class TextInOcrItemResponse {

    /**
     * 识别区块名称
     */
    @ApiModelProperty(value = "识别区块key")
    private String key;


    /**
     * 识别区块名称
     */
    @ApiModelProperty(value = "识别区块description")
    private String name;
    /**
     * 识别区块名称
     */
    @ApiModelProperty(value = "识别区块value")
    private String word;

    /**
     * 识别区块位置   [0,1,2,3,4,5,6,7]
     * (0, 1) 左上角坐标
     * (2, 3) 右上角坐标
     * (4, 5) 左下角坐标
     * (6, 7) 右下角坐标
     */
    @ApiModelProperty(value = "识别区块位置")
    private String position;


    @ApiModelProperty(value = "坐标0")
    private Integer zero;
    @ApiModelProperty(value = "坐标1")
    private Integer one;
    @ApiModelProperty(value = "坐标2")
    private Integer two;
    @ApiModelProperty(value = "坐标3")
    private Integer three;
    @ApiModelProperty(value = "坐标4")
    private Integer four;
    @ApiModelProperty(value = "坐标5")
    private Integer five;
    @ApiModelProperty(value = "坐标6")
    private Integer six;
    @ApiModelProperty(value = "坐标7")
    private Integer seven;

    /**
     * 识别区块宽度（2-0；6-4）
     */
    @ApiModelProperty(value = "识别区块宽度")
    private Integer width;

    /**
     * 识别区块高度（5-1；7-3）
     */
    @ApiModelProperty(value = "识别区块高度")
    private Integer height;

    /**
     * 识别区块，相对于左上顶点为原点的垂直坐标 (取1或3)
     */
    @ApiModelProperty(value = "识别区块，相对于左上顶点为原点的垂直坐标")
    private Integer top;

    /**
     * 识别区块，相对于左上顶点为原点的水平坐标(取0或6)
     */
    @ApiModelProperty(value = "识别区块，相对于左上顶点为原点的水平坐标")
    private Integer left;



}

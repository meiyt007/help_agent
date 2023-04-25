package com.zfsoft.ocr.data.pojo.ocr;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/***
* @Description: ocr卡证识别信息
* @Author:liangss
* @Date:2021/10/29
* @Param:
*/
@ApiModel(description = "ocr卡证识别，识别区块信息")
public class OcrCardItemResponse {

    /**
     * 识别区块名称
     */
    @ApiModelProperty(value = "识别区块名称")
    private String name;

    /**
     * 识别区块文字
     */
    @ApiModelProperty(value = "识别区块文字")
    private String word;

    /**
     * 识别区块宽度
     */
    @ApiModelProperty(value = "识别区块宽度")
    private String width;

    /**
     * 识别区块高度
     */
    @ApiModelProperty(value = "识别区块高度")
    private String height;

    /**
     * 识别区块，相对于左上顶点为原点的垂直坐标
     */
    @ApiModelProperty(value = "识别区块，相对于左上顶点为原点的垂直坐标")
    private String top;

    /**
     * 识别区块，相对于左上顶点为原点的水平坐标
     */
    @ApiModelProperty(value = "识别区块，相对于左上顶点为原点的水平坐标")
    private String left;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    /**
     * 获取 识别区块宽度
     *
     * @return width 识别区块宽度
     */
    public String getWidth() {
        return this.width;
    }

    /**
     * 设置 识别区块宽度
     *
     * @param width 识别区块宽度
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * 获取 识别区块高度
     *
     * @return height 识别区块高度
     */
    public String getHeight() {
        return this.height;
    }

    /**
     * 设置 识别区块高度
     *
     * @param height 识别区块高度
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * 获取 识别区块，相对于左上顶点为原点的垂直坐标
     *
     * @return top 识别区块，相对于左上顶点为原点的垂直坐标
     */
    public String getTop() {
        return this.top;
    }

    /**
     * 设置 识别区块，相对于左上顶点为原点的垂直坐标
     *
     * @param top 识别区块，相对于左上顶点为原点的垂直坐标
     */
    public void setTop(String top) {
        this.top = top;
    }

    /**
     * 获取 识别区块，相对于左上顶点为原点的水平坐标
     *
     * @return left 识别区块，相对于左上顶点为原点的水平坐标
     */
    public String getLeft() {
        return this.left;
    }

    /**
     * 设置 识别区块，相对于左上顶点为原点的水平坐标
     *
     * @param left 识别区块，相对于左上顶点为原点的水平坐标
     */
    public void setLeft(String left) {
        this.left = left;
    }
}

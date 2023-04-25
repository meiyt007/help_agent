package com.zfsoft.ocr.data.pojo.textInOcr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/***
* @Description:  ocr卡证识别，响应信息
* @Author:liangss
* @Date:2021/10/29
* @Param:
*/
@ApiModel(description = "ocr卡证识别，响应信息")
@Data
public class TextInOcrResponse  {
    
    /**
     * 识别区块内容列表
     */
    @ApiModelProperty(value = "识别区块内容列表")
    private List<TextInOcrItemResponse> textInOcrItemResponseList;


    /**
     * 识别图的宽
     */
    @ApiModelProperty(value = "识别图的宽")
    private  Integer rotated_image_width;
    /**
     * 识别图的高
     */
    @ApiModelProperty(value = "识别图的高")
    private  Integer rotated_image_height;

    /**
     * 图片的旋转角度，指原图需要经过顺时针旋转多少度，才能得到正方向的图片
     */
    @ApiModelProperty(value = "图片的旋转角度，指原图需要经过顺时针旋转多少度，才能得到正方向的图片")
    private  String image_angle;

    /**
     * 当前识别证件类型
     */
    @ApiModelProperty(value = "当前识别证件类型")
    private  String type;



    /***
    * @Description:
     * 40101    x-ti-app-id 或 x-ti-secret-code 为空
     * 40102	x-ti-app-id 或 x-ti-secret-code 无效，验证失败
     * 40003	余额不足，请充值后再使用
     * 40004	参数错误，请查看技术文档，检查传参
     * 40007	机器人不存在或未发布
     * 40008	机器人未开通，请自行开通后再使用
     * 40303	文件类型不支持
     * 40302	上传文件大小不符
     * 40301	图片类型不支持
     * 40304	图片尺寸不符，图像宽高须介于 20 * 20 和 10000 * 100000 （像素）之间
     * 30203	基础服务故障，请稍后重试
     * 500	服务器内部错误
    * @Author:liangss
    * @Date:2021/11/1
    * @Param:
    */
    @ApiModelProperty(value = "响应代码",position = -1000)
    private Integer code;
    @ApiModelProperty(value = "响应结果说明",position = -999)
    private String message;

}

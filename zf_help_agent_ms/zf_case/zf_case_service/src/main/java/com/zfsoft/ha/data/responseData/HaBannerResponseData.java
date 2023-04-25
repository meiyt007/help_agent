package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //Banner响应实体类
 * @Author: Wangyh
 * @Date: 2022/7/29 10:49
 */
@Data
@ToString
public class HaBannerResponseData {

    /**
     *主键
     */
    private Long id;

    /**
     *标题
     * @mbg.generated
     */
    private String title;

    /**
     *图片
     * @mbg.generated
     */
    private String images;

    /**
     *内容
     * @mbg.generated
     */
    private String content;

    /**
     *跳转地址
     * @mbg.generated
     */
    private String url;
}

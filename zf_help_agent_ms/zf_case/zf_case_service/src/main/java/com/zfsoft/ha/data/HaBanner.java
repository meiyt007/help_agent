package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description //Banner
 * @Author: Wangyh
 * @Date: 2022/7/26 14:40
 */

@Data
@ToString
public class HaBanner {
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

    /**
     *启禁用状态;1-启用，2-禁用
     * @mbg.generated
     */
    private String ableStatus;

    /**
     *删除状态;1-未删除，2-已删除
     * @mbg.generated
     */
    private String deleteStatus;

    /**
     *创建人
     * @mbg.generated
     */
    private String createBy;

    /**
     *创建时间
     * @mbg.generated
     */
    private Date createDate;

    /**
     *更新人
     * @mbg.generated
     */
    private String updateBy;

    /**
     *更新时间
     * @mbg.generated
     */
    private Date updateDate;
}

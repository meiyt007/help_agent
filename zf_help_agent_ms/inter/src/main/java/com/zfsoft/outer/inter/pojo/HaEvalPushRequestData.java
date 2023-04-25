package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 评价结果请求参数
 * @author: kangax
 * @date: 2022-08-12 17:32
 **/
@Data
public class HaEvalPushRequestData {
    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;
    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;
    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    private String cardNo;
    /**
     * 统一-社会信用代码
     */
    private String companyCode;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 评价结果主键
     */
    private Long Id;

    /**
     * 队列编号
     */
    private Long queueId;

    /**
     * 评价内容
     */
    private String evalContent;

    /**
     * 评价项结果
     */
    private List<Result> result;

    @Data
    @ToString
    public static class Result {
        /**
         * 评价结果明细主键
         */
        private Long Id;
        /**
         * 评价项编号
         */
        private Long evalItemId;

        /**
         * 评价项得分
         */
        private String evalItemScore;

        /**
         * 评价项内容
         */
        private String evalContent;
    }
}

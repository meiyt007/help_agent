package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @description: 评价结果请求参数
 * @author: kangax
 * @date: 2022-08-12 17:32
 **/
@Data
@ToString
public class HaEvalResultRequestData {
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

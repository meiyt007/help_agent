package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description //提供万行的，事项常见问题响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 18:56
 */
@Data
@ToString
public class ServiceQuestionsRespVo {
    /**
     * 主健
     */
    private  Long id;

    /**
     * questionOid
     */
    private String questionOid;

    /**
     * 所属事项
     */
    private String serviceOid;

    /**
     * 主题词
     */
    private String keyWord;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题解答
     */
    private String answer;

    /**
     * 排序号
     */
    private Long sort;

}

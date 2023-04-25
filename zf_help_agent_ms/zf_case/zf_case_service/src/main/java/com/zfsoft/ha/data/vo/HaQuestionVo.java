package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 用户常见问题实体前端返回参数
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/19 下午1:28
 */
@Data
@ToString
public class HaQuestionVo {
    private Long id;
    private Long workUserId;
    private String question;
    private String answer;
    private Date createDate;


}

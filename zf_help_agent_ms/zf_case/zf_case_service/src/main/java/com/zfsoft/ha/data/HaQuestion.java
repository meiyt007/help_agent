package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author kangax
 * @version 1.0
 * @date 2022/7/15 上午1:06
 */
@Data
@ToString
public class HaQuestion {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;

    /**
     * 工作人员编号
     */
    private Long workUserId;

    private String question;

    private String answer;

    /*备注*/
    private String note;

    private String deleteStatus;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
    /*工作人员信息*/
    private String workUserName;



}

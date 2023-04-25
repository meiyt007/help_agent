package com.zfsoft.superwindow.data.yxpz;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @description:  平板人员信息表
 * @author: liangxm
 * @Date: 2020/10/24 11:49
 **/
@Data
@ToString
public class PbpjUser {
    /**
     * 新增用户，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键  */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 用户主键  */
    private String userOid;

    /* 是否启用  */
    private Integer appraiseFlag;

    /* 是否确认评价  */
    private Integer confirmFlag;


    private Date modifyDate;

    }
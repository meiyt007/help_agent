package com.zfsoft.microservice.settings.data;


import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description:  系统配置信息表
 * @author: wuxx
 * @Date: 2020/9/12 11:49
 **/
@Data
@ToString
public class SysConfig{

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

    @NotNull(message = "configid不能为空",groups = {UPDATE_GROUP.class})
    private String configOid;

    /* 配置项代码  */
    @NotNull(message = "配置项代码不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "配置项代码长度为1-100",groups = {INSERT_GROUP.class})
    private String code;

    /* 配置项名称  */
    @NotNull(message = "配置项名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "配置项名称长度为1-100",groups = {INSERT_GROUP.class})
    private String name;

    /* 配置项描述  */
    private String memo;

    /* 配置项值  */
    @NotNull(message = "配置项值不能为空",groups = {INSERT_GROUP.class})
    private String value;

    /* 启禁用状态  */
    private Integer isAble;

    /* 附件id  */
    private String attaOid;

    /* 附件名称  */
    private String attaName;

    /* 删除状态  */
    private Integer isDelete;

    /* 配置项类型  */
    private Integer type;

    /* 上级主键  */
    private String parentOid;

    /* 上级名称  */
    private String parentName;
    /* 字典主键路径  */
    private String path;


}

package com.zfsoft.microservice.workflow.data;


import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @description: 工作流流程类型操作实体类
 * @author: wuxx
 * @Date: 2021/1/22 14:42
 **/
@Data
@ToString
public class WorkflowLink{

    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = {UPDATE_GROUP.class})
    private String linkOid;


    /** 流程类型主键 */
    @NotNull(message = "流程类型不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String typeOid;

    /** 流程类型名称 */
    private String typeName;

    /** 编码 */
    @NotNull(message = "编码不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    @Length(min = 1,max = 20,message = "编码长度为1-20",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String code;

    /** 名称 */
    @NotNull(message = "名称不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    @Length(min = 1,max = 100,message = "名称长度为1-100",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String name;

    /** 备注 */
    @Length(min = 0,max = 1000,message = "备注长度不能超过1000",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String memo;

    /** 环节操作 */
    @NotNull(message = "环节操作名称不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    @Length(min = 1,max = 250,message = "环节操作长度为1-250",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String actionName;

    /** 创建时间 */
    private Date createDate;

    /** 启禁用状态 */
    private Integer isAble;

    /** 删除状态 */
    private Integer isDelete;

}

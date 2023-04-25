package com.zfsoft.platform.utils.data;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据字典信息表
 *
 * @author wuxx
 * @date 2020/9/12
 */
@Data
@ToString
public class SysDict {
    /**
     * 新增用户，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    @NotNull(message = "dictid不能为空",groups = {UPDATE_GROUP.class})
    private String dictOid;

    /* 字典代码 */
    @NotNull(message = "字典代码不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 25,message = "字典代码长度为1-25",groups = {INSERT_GROUP.class})
    private String code;

    /* 字典名称 */
    @NotNull(message = "字典名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "字典名称长度为1-100",groups = {INSERT_GROUP.class})
    private String name;

    /* 排序号 */
    @NotNull(message = "排序号不能为空",groups = {INSERT_GROUP.class})
    private Integer sort;

    /* 上级字典主键 */
    private String parentOid;

    /* 上级字典名称 */
    private String parentName;

    /* 说明 */
    private String memo;

    /* 字典主键路径 */
    private String path;

    /* 创建用户id */
    private String createUserOid;

    /* 创建用户 */
    private String createUserName;

    /* 创建日期  */
    private Date createDate;

    /* 启用状态 */
    private Integer isAble;

    /* 删除状态 */
    private Integer isDelete;

}

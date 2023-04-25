package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description: 岗位信息表
 * @author: wuxx
 * @Date: 2021/01/12 16:35
 **/
@Data
@ToString
public class SysPost {

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    @NotNull(message = "岗位业务主键不能为空",groups = {UPDATE_GROUP.class})
    private String postOid;

    /* 所属区划OID */
    @NotNull(message = "所属区划不能为空",groups = {INSERT_GROUP.class})
    private String districtOid;

    /* 所属区划名称 */
    private String districtName;

    /* 所属组织机构OID */
    @NotNull(message = "所属组织机构不能为空",groups = {INSERT_GROUP.class})
    private String organOid;

    /* 所属组织机构名称 */
    private String organName;

    /* 岗位名称 */
    @NotNull(message = "岗位名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 20,message = "岗位名称长度为1-20",groups = {INSERT_GROUP.class})
    private String name;

    /* 启禁用状态 */
    private Integer isAble;

    /* 删除状态 */
    private Integer isDelete;

    /* 岗位职责 */
    private String duty;

    /* 排序号 */
    private Integer sort;
    /* 备注 */
    private String note;
}

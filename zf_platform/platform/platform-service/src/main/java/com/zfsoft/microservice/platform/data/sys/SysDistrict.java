package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description:  区划信息实体类
 * @author: wuxx
 * @Date: 2020/8/28 13:04
 **/
@Data
@ToString
public class SysDistrict {
    /**
     * 新增区划，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新区划，验证规则组
     */
    public interface UPDATE_GROUP{};

    /*
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;
    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String districtOid;
    /*
     *区划代码
     */
    @NotNull(message = "区划代码不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 25,message = "区划代码长度为1-25",groups = {INSERT_GROUP.class})
    private String code;
    /*
     * 区划名称
     */
    @NotNull(message = "区划名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "区划名称长度为1-100",groups = {INSERT_GROUP.class})
    private String name;
    /*
     * 上级区划代码
     */
    private String parentOid;

    /*
     * 上级区划名称
     */
    private String parentName;

    /*
     * 区划级别
     */
    private String levelDictOid;

    /*
     * 区划级别名称
     */
    private String levelDictName;

    /*
     * 区划路径
     */
    private String path;
    /*
     * 启禁用状态 1启用  0禁用
     */
    private Integer isAble;
    /*
     * 删除状态  1删除  0正常
     */
    private Integer isDelete;
    /*
     * 排序号
     */
    @NotNull(message = "排序号不能为空",groups = {INSERT_GROUP.class})
    private Integer sort;
    /*
     * 展示区划名称
     */
    private String disNameStr;

    /*
     * 区划图片得oid
     */
    private String imageAttaOid;
    /*
     * 附件名称
     */
    private String attaName;

    /** 禁用  用于区划树是否可被选中 */
    private Boolean disabled;

}
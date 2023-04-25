package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 字段分类信息
 **/
@Data
@ToString
public class SxFieldType {

    /**
     * 新增信息验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新信息验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * @COLUMN_EXPLAIN : 主键
     * @TABLE_COLUMN_TYPE : VARCHAR (64)
     */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "业务主键不能为空",groups = {UPDATE_GROUP.class})
    private String fieldTypeOid;

    /**
     * @COLUMN_EXPLAIN : 类别名称
     * @TABLE_COLUMN_TYPE : VARCHAR (500)
     */
    //@NotNull(message = "类别名称不能为空",groups = {INSERT_GROUP.class})
    //@Length(min = 1,max = 20,message = "类别名称长度为1-20",groups = {INSERT_GROUP.class})
    private String fieldTypeName;

    /**
     * @COLUMN_EXPLAIN : 父类oid
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String parentOid;

    /**
     * @COLUMN_EXPLAIN : 变更类别 0-否：正常展示 1-是：变更事项
     * @TABLE_COLUMN_TYPE : int (1)
     */
    private Integer changeCategory;

    /**
     * @COLUMN_EXPLAIN : 是否表格展示1是表格
     * @TABLE_COLUMN_TYPE : int (1)
     */
    private Integer tableStatus;

    /**
     * @COLUMN_EXPLAIN : 排序号
     * @TABLE_COLUMN_TYPE : int (1)
     */
    private Integer sort;

    /**
     * @COLUMN_EXPLAIN : 级别
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    private String level;

    /**
     * @COLUMN_EXPLAIN : 备注
     * @TABLE_COLUMN_TYPE : VARCHAR (2000)
     */
    private String remark;


    /**
     * @COLUMN_EXPLAIN : 启禁用状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private Integer isAble;

    /**
     * @COLUMN_EXPLAIN : 删除状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private Integer isDelete;

    /**
     * @COLUMN_EXPLAIN : 创建时间
     * @TABLE_COLUMN_TYPE : DATETIME
     */
    private java.util.Date createDate;

    /**
     * @COLUMN_EXPLAIN : 创建人oid
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String createUserOid;

    /**
     * @description: 子类标签列表
     **/
    private List<SxFillLabel> childrenLableList;





}

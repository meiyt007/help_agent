package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description: 表单模板表表
 * @author: wuxx
 * @Date: 2021/4/19 10:26
 **/
@Data
@ToString
public class FormTemplate {

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
    @NotNull(message = "id不能为空", groups = {FormTemplate.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空", groups = {FormTemplate.UPDATE_GROUP.class})
    private String templateOid;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "模板名称不能为空", groups = {FormTemplate.UPDATE_GROUP.class})
    @Length(min = 1, max = 30, message = "模板名称长度为1-30", groups = {FormTemplate.INSERT_GROUP.class, FormTemplate.UPDATE_GROUP.class})
    private String templateName;

    /**
     * 授权KEY
     */
    private String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 设计表业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String designOid;

    /**
     * @COLUMN_EXPLAIN : 模板附件的主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String templateAtta;

    /**
     * 附件名称
     */
    private String attaName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模板的配置数据json
     */
    private String templateConfig;

    /**
     * 是否公开（1公开 0不公开） 默认0
     */
    private Integer isPublic;

    /**
     * 是否固化（1固化 其他不固化）
     */
    private Integer isFixed;
    
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
     * 模板图片url
     */
    private String fastdfsUrl ;

}

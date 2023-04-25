package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 电子表单成员管理表
 * @author: wuxx
 * @Date: 2021/4/8 10:26
 **/
@Data
@ToString
public class FormMember {

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
    @NotNull(message = "id不能为空",groups = {FormMember.UPDATE_GROUP.class})
    private Long id;
    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormMember.UPDATE_GROUP.class})
    private String memberOid;

    /**
     * @COLUMN_EXPLAIN : 授权authorizeKey
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormMember.INSERT_GROUP.class,FormMember.UPDATE_GROUP.class})
    private String authorizeKey;

    /**
     *  成员集合
     */
    private List<String> userOids;

    /**
     * @COLUMN_EXPLAIN : 成员oid
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String userOid;

    /**
     * @COLUMN_EXPLAIN : 成员名称
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    private String userName;

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


}

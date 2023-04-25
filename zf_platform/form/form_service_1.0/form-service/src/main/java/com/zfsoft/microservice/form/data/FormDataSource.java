package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @description: 电子表单表
 * @author: wuxx
 * @Date: 2021/3/10 15:26
 **/
@Data
@ToString
public class FormDataSource {

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
    private java.lang.Long id;

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = {FormDataSource.UPDATE_GROUP.class})
    private String datasourceOid;

    /**
     * 所属模块
     */
    @NotNull(message = "所属模块不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    private String moduleOid;

    /**
     * 所属模块名称
     */
    private String moduleName;
    /**
     * 连接名
     */
    @NotNull(message = "连接名不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    @Length(min = 1,max = 50,message = "连接名长度为1-50",groups = {FormDataSource.INSERT_GROUP.class})
    private String connectionName;

    /**
     * 数据库名
     */
    private String datasourceName;

    /**
     *  授权KEY
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormDataSource.INSERT_GROUP.class, FormDataSource.UPDATE_GROUP.class})
    private String authorizeKey;

    /**
     *  类型
     */
    @NotNull(message = "类型不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    private String type;

    /**
     *  驱动名称
     */
    private String driverClassName;

    /**
     *  主机
     */
    @NotNull(message = "主机不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    @Length(min = 1,max = 50,message = "主机长度为1-50",groups = {FormDataSource.INSERT_GROUP.class})
    private String host;

    /**
     *  端口
     */
    @NotNull(message = "端口不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    @Pattern(message = "端口只能为正整数",regexp = "^[\\+\\-]?[1-9]\\d*$",groups = {FormDataSource.INSERT_GROUP.class})
    @Length(min = 1,max = 5,message = "端口长度为1-5",groups = {FormDataSource.INSERT_GROUP.class})
    private String port;

    /**
     *  用户名
     */
    @NotNull(message = "用户名不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    @Length(min = 1,max = 40,message = "用户名长度为1-40",groups = {FormDataSource.INSERT_GROUP.class})
    private String username;

    /**
     *  密码
     */
    @NotNull(message = "密码不能为空",groups = {FormDataSource.INSERT_GROUP.class})
    @Length(min = 1,max = 40,message = "密码长度为1-40",groups = {FormDataSource.INSERT_GROUP.class})
    private String password;

    /**
     *  服务名
     */
    private String serviceName;

    /**
     *  加密的密码
     */
    private String encryptPassword;

    /**
     * @COLUMN_EXPLAIN : 启禁用状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private java.lang.Integer isAble;

    /**
     * @COLUMN_EXPLAIN : 删除状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private java.lang.Integer isDelete;

}

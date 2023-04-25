package com.zfsoft.microservice.form.data;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zfsoft.platform.utils.DESAuthorizeUtils;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 电子表单访问授权
 * @author: wuxx
 * @Date: 2021/3/10 15:26
 **/
@Data
@ToString
public class FormAuthorize{

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
    @NotNull(message = "id不能为空",groups = {FormAuthorize.UPDATE_GROUP.class})
    private java.lang.Long id;

    /**
     * @COLUMN_EXPLAIN : 授权authorizeKey
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormAuthorize.UPDATE_GROUP.class})
    private java.lang.String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 系统名称
     * @TABLE_COLUMN_TYPE : VARCHAR (500)
     */
    @NotNull(message = "系统名称不能为空",groups = {FormAuthorize.INSERT_GROUP.class})
    @Length(min = 1,max = 20,message = "系统名称长度为1-20",groups = {INSERT_GROUP.class})
    private java.lang.String systemName;


    /**
     * @COLUMN_EXPLAIN : 授权期限类型（0永久 1临时）
     * @TABLE_COLUMN_TYPE : int (1)
     */
    @NotNull(message = "授权期限类型不能为空",groups = {FormAuthorize.INSERT_GROUP.class})
    private java.lang.Integer timeType;

    /**
     * @COLUMN_EXPLAIN : 授权开始时间
     * @TABLE_COLUMN_TYPE : datetime
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startTime;

    /**
     * @COLUMN_EXPLAIN : 授权结束时间
     * @TABLE_COLUMN_TYPE : datetime
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime;

    /**
     * @COLUMN_EXPLAIN : 备注
     * @TABLE_COLUMN_TYPE : VARCHAR (1000)
     */
    private java.lang.String remark;

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

    /**
     * @COLUMN_EXPLAIN : 创建时间
     * @TABLE_COLUMN_TYPE : DATETIME
     */
    private java.util.Date createDate;

    /**
     * @description:  授权key的集合
     * @author: wuxx
     * @Date: 2021/4/9 13:33
     **/
    private List<String> authorizeKeyList;
}

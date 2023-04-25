package com.zfsoft.superwindow.data.yxpz;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @description:  平板设备信息表
 * @author: liangxm
 * @Date: 2020/10/24 11:49
 **/
@Data
@ToString
public class PbpjManage {

    /**
     * 新增用户，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.ID
     *
     * @mbggenerated
     */
    /* 主键  */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.RUN_CODE
     *
     * @mbggenerated
     */
    @NotNull(message = "设备编号不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 25,message = "设备编号代码长度为1-25",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String runCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.NAME
     *
     * @mbggenerated
     */
    @NotNull(message = "设备名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "设备名称长度为1-100",groups = {INSERT_GROUP.class})
    private String name;


    /* 设备ip值  */
    @NotNull(message = "配置项值不能为空",groups = {INSERT_GROUP.class})
    private String ip;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.STATUS
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.USER_CODE
     *
     * @mbggenerated
     */
    @NotNull(message = "用户编号不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "用户编号长度为1-100",groups = {INSERT_GROUP.class})
    private String userCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.USER_NAME
     *
     * @mbggenerated
     */
    @NotNull(message = "用户名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "用户名称长度为1-100",groups = {INSERT_GROUP.class})
    private String userName;


    private Date dateTime;


    private String remark;


    private Integer isDelete;

    /**
     * 评价类型  0 平板评价 1 智能评价
     */
    private Integer pbpjType;


}
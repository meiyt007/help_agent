package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysLogin
 * @Description
 * @Author
 * @Date2020-09-11 17:14
 * @Version V1.0
 **/
@Data
public class SysLogin {

    /**
     * 新增验证组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证组
     */
    public interface UPDATE_GROUP{};

    private Long id;

    @NotNull(message = "用户信息不能为空",groups = {INSERT_GROUP.class})
    private String userOid;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String loginOid;

    @NotBlank(message = "账号不能为空",groups = {INSERT_GROUP.class})
    private String account;

    @NotBlank(message = "密码不能为空",groups = {INSERT_GROUP.class})
    @Pattern(message = "密码格式不正确",regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@#*_]{8,16}$",groups = {INSERT_GROUP.class})
    private String password;

    @NotBlank(message = "用户名称不能为空",groups = {INSERT_GROUP.class})
    private String name;

    private Integer failNum;

    private Integer lockStatus;

    private Integer isAble;

    private Integer isDelete;

    private Date lockDate;

    private Date modifyDate;

    private Date passwordDate;

    /**
     * 登录角色
     */
    private List<SysLoginRole> sysLoginRoleList;
}

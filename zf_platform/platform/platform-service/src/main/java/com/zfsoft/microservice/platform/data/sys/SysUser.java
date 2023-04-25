package com.zfsoft.microservice.platform.data.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户信息
 * @author zxx
 * @date 2020/9/14 10:34 上午
 */
@Data
@ToString
public class SysUser {

    /**
     * 新增验证组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String userOid;

    /**
     * 名称
     */
    @NotBlank(message = "姓名不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 50,message = "姓名长度为1-50",groups = {INSERT_GROUP.class})
    private String name;

    /**
     * 区划
     */
    @NotNull(message = "所属区划不能为空",groups = {INSERT_GROUP.class})
    private String districtOid;

    /**
     * 机构
     */
    @NotNull(message = "所属区划不能为空",groups = {INSERT_GROUP.class})
    private String organOid;

    /**
     * 类型
     */
    private String type;

    /**
     * 性别
     */
    @Pattern(message = "性别只能是男-M，女-W",regexp = "[MW]",groups = {INSERT_GROUP.class})
    private String sex;

    /**
     * 电话号码
     */
    private String telphone;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号码不能为空",groups = {INSERT_GROUP.class})
    @Pattern(message = "手机号码格式不正确",regexp = "^((0\\d{2,3}-\\d{7,8})|(1[34578]\\d{9}))$",groups = {INSERT_GROUP.class})
    private String mobile;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空",groups = {INSERT_GROUP.class})
    @Pattern(message = "邮箱格式不正确",regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",groups = {INSERT_GROUP.class})
    private String email;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 出生年月
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date birthdate;

    /**
     * 头像附件
     */
    private String headImageAttaOid;

    /**
     * 启禁用
     */
    private Integer isAble;

    /**
     * 删除
     */
    private Integer isDelete;

    /**
     * 证件号
     */
    @NotBlank(message = "身份证号不能为空",groups = {INSERT_GROUP.class})
    @Pattern(message = "身份证号格式不正确",regexp = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",groups = {INSERT_GROUP.class})
    private String cardNo;

    /**
     * 职位
     */
    @NotBlank(message = "职位不能为空",groups = {INSERT_GROUP.class})
    private String position;

    /**
     * 数据权限（1全部 2本人 3本部门 4本区划）
     */
    private Integer dataAuthority;

    /**
     * 皮肤
     */
    private String skinClassname;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 所属岗位oids
     */
    private String postOids;

    /**
     * 登录信息
     */
    private SysLogin sysLogin;

    /**
     * 区划名称
     */
    private String districtName;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 类型名称
     */
    private String TypeName;

    private List<String> roleOids;

    /**
     * 后缓咨询状态
     */
    private Integer adviStatus;

    /**
     * 统计类型 多个使用逗号隔开
     */
    private String statisticTypes;

}

package com.zfsoft.platform.auth.data;

import com.zfsoft.microservice.platform.data.sys.SysLogin;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName SysUser
 * @Description 用户信息实体
 * @Author lijun
 * @Date2020-08-25 11:33
 * @Version V1.0
 **/
@Data
public class SysUser {
    /**
     * 主键
     */
    private String userOid;

    /**
     * 名称
     */
    private String name;

    /**
     * 区划
     */
    private String districtOid;

    /**
     * 机构
     */
    private String organOid;

    /**
     * 类型
     */
    private String type;

    /**
     * 性别
     */
    private String sex;

    /**
     * 电话号码
     */
    private String telphone;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 出生年月
     */
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
    private String cardNo;

    /**
     * 职位
     */
    private String position;

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
    /**
     * 后缓咨询状态
     */
    private Integer adviStatus;

    /**
     * 数据权限（1全部 2本人 3本部门 4本区划）
     */
    private Integer dataAuthority;
}

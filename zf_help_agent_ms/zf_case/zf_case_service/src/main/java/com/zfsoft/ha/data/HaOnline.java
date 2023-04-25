package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description //用户在线时长表接收实体类
 * @Author: Wangyh
 * @Date: 2022/8/12 11:12
 */
@Data
@ToString
public class HaOnline {
    /** 主键 */
    private Long id ;
    /** 用户主键 */
    private Long workUserId ;
    /** 登录类型;1-快捷帮办，2-圆桌帮办 */
    private String loginType ;
    /** 登录时间 */
    private Date loginTime ;
    /** 退出时间 */
    private Date logoutTime ;
    /** 在线时长;单位：秒 */
    private String onlineTime ;
    /** 退出类型;1-手动退出，2-异常退出，3-登录 */
    private String logoutType ;
    /** 登录token值 */
    private String loginToken ;
    /** 登录IP地址 */
    private String loginIp ;
    /** 创建人 */
    private String createBy ;
    /** 创建时间 */
    private Date createDate ;
    /** 更新人 */
    private String updateBy ;
    /** 更新时间 */
    private Date updateDate ;


}

package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //帮代办工作人员表响应实体类
 * @Author: Wangyh
 * @Date: 2022/7/27 13:21
 */
@Data
@ToString
public class HaWorkUserResponseData {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /** 工号 */
    private String workNumber ;

    /**
     * 账号;唯一
     */
    private String account;

    /**
     * 用户类型;1-导服人员，2-帮代办人员，3-委办局
     */
    private String userType;

    /**
     * 帮代办人员类型;1-快捷帮办，2-圆桌帮办
     */
    private String haType;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 状态;1-离线、2-忙碌、3-空闲、4-服务中
     */
    private String status;


    /**
     * 所属区划
     */
    private String districtOid;

    /**
     * 头像
     */
    private String image;

    /**
     * 备注
     */
    private String memo;

    /**
     * 唯一标识
     */
    private String token;

    /** 服务位置;提供帮代办服务时，办公地点 */
    private String servicePostion ;

    /** 分组编号 */
    private Long groupId ;

    /** 分组名称 */
    private String groupName ;

    /** 分组职务;1-组长，2-副组长，3-组员 */
    private String groupPost ;

    /** 预约状态 1-可以预约，2-不可以预约 */
    private String apponStatus ;

}

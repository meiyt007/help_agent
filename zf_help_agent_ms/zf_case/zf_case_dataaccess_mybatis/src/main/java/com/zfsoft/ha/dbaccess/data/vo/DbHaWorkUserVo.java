package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description //员工表Vo
 * @Author: Wangyh
 * @Date: 2022/8/2 15:44
 */
@Data
@ToString
public class DbHaWorkUserVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

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
     * 盐值，用于密码加密
     */
    private String salt;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 备注
     */
    private String memo;

    /**
     * 删除状态;1-未删除，2-已删除
     */
    private String deleteStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 当前服务人数
     */
    private Integer currentServiceNum;
    /**
     * 最大服务人数
     */
    private Integer maxServiceNum;

    /*
     * 每个人平均服务时长
     * */
    private Integer avgServiceTime;

    /**
     * 对接用户编号;对接第三方系统时，第三方系统的用户唯一
     */
    private String connectUserId;

    /**
     * 工号
     */
    private String workNumber;

    /**
     * 服务位置;提供帮代办服务时，办公地点
     */
    private String servicePostion;

    /**
     * 分组编号
     */
    private Long groupId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 分组职务;1-组长，2-副组长，3-组员
     */
    private String groupPost;

    /**
     * 转派配置;1-手动接收，2-自动接收
     */
    private String turnConfig;

    /** 预约状态 1-可预约 2-不可逾越 */
    private String apponStatus;

    /** 取号状态 1-可取号  2-不可取号*/
    private String takeNumStatus;

    /** 绩效配置 1-有绩效 2-无绩效 */
    private String evalStatus;

    /** 资料库配置 1-有资料库 2-无资料库 */
    private String resourceStatus;

    /** 问题配置 1-有问题  2-没问题 */
    private String questionStatus;

    /** 智能问答配置 1-可智能问答 2-不可智能问答 */
    private String qaStatus;

    private Integer inServiceNum;
    private Integer waitingNum;
    private Integer esuimateTime;
    private Integer todayServiceNum;

    /**组内分组**/
    private String groupSplitId;

    private String groupSplitName;

    /** 登录地名称**/
    private String loginLocationName;

    /** 云客服工号（电话工号）     */
    private String cloudServiceNumber;

    /** 视频呼叫超时配置，单位秒 */
    private Integer callTimeout;

}

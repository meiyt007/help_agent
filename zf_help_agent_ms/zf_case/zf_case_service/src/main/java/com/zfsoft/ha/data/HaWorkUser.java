package com.zfsoft.ha.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description //用户表
 * @Author: Wangyh
 * @Date: 2022/7/15 13:11
 */
@Data
@ToString
@ApiModel(value = "帮代办工作人员表",description = "")
public class HaWorkUser {
    /**
     * 新增用户，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};
    /** 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    @ApiModelProperty(name = "主键",notes = "")
    private Long id;

    /** 姓名 */
    @ApiModelProperty(name = "姓名",notes = "")
    private String name;

    /** 账号;唯一 */
    @ApiModelProperty(name = "账号",notes = "唯一")
    @NotNull(message = "账号不能为空",groups = {UPDATE_GROUP.class})
    private String account;

    /** 用户类型;1-导服人员，2-帮代办人员 ，3-委办局 */
    @ApiModelProperty(name = "用户类型",notes = "1-导服人员，2-帮代办人员，3-委办局")
    private String userType;

    /** 帮代办人员类型;1-快捷帮办，2-圆桌帮办 */
    @ApiModelProperty(name = "帮代办人员类型",notes = "1-快捷帮办，2-圆桌帮办")
    private String haType;

    /** 手机号 */
    @ApiModelProperty(name = "手机号",notes = "")
    private String phone;

    /** 电子邮箱 */
    @ApiModelProperty(name = "电子邮箱",notes = "")
    private String email;

    /** 状态;1-离线、2-忙碌、3-空闲、4-服务中 */
    @ApiModelProperty(name = "状态",notes = "1-离线、2-忙碌、3-空闲、4-服务中")
    private String status;

    /** 所属区划 */
    @ApiModelProperty(name = "所属区划",notes = "")
    private String districtOid;

    /** 头像 */
    @ApiModelProperty(name = "头像",notes = "")
    private String image;

    /** 盐值，用于密码加密 */
    @ApiModelProperty(name = "盐值，用于密码加密",notes = "")
    private String salt;

    /** 登录密码 */
    @ApiModelProperty(name = "登录密码",notes = "")
    @NotNull(message = "登录密码不能为空",groups = {UPDATE_GROUP.class})
    private String password;

    /** 备注 */
    @ApiModelProperty(name = "备注",notes = "")
    private String memo;

    /** 每个人平均服务时长;单位：分钟 */
    @ApiModelProperty(name = "每个人平均服务时长",notes = "单位：分钟")
    private Integer avgServiceTime ;
    /** 最大服务人数;当达到最大服务人数时，状态为忙碌 */
    @ApiModelProperty(name = "最大服务人数",notes = "当达到最大服务人数时，状态为忙碌")
    private Integer maxServiceNum ;
    /** 当前服务人数;完成服务减1，接待一位加1，当达到最大服务人数时，状态为忙碌 */
    @ApiModelProperty(name = "当前服务人数",notes = "完成服务减1，接待一位加1，当达到最大服务人数时，状态为忙碌")
    private Integer currentServiceNum ;

    /** 删除状态;1-未删除，2-已删除 */
    @ApiModelProperty(name = "删除状态",notes = "1-未删除，2-已删除")
    private String deleteStatus ;
    /** 创建人 */
    @ApiModelProperty(name = "创建人",notes = "")
    private String createBy ;
    /** 创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createDate ;
    /** 更新人 */
    @ApiModelProperty(name = "更新人",notes = "")
    private String updateBy ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateDate ;

    /** 对接用户编号;对接第三方系统时，第三方系统的用户唯一 */
    private String connectUserId ;

    /** 工号 */
    private String workNumber ;

    /** 服务位置;提供帮代办服务时，办公地点 */
    private String servicePostion ;

    /** 分组编号 */
    private Long groupId ;

    /** 分组职务;1-组长，2-副组长，3-组员 */
    private String groupPost ;

    /** 转派配置;1-手动接收，2-自动接收 */
    private String turnConfig ;


    /** 预约状态 1-可预约 2-不可逾越 */
    private String apponStatus;

    /** 取号状态 1-可取号  2-不可取号*/
    private String takeNumStatus;

    /** 分组编号(组内分组） */
    private String groupSplitId ;

    /** 登录地名称**/
    private String loginLocationName;


    /** 绩效配置 1-有绩效 2-无绩效 */
    private String evalStatus;

    /** 资料库配置 1-有资料库 2-无资料库 */
    private String resourceStatus;

    /** 问题配置 1-有问题  2-没问题 */
    private String questionStatus;

    /** 智能问答配置 1-可智能问答 2-不可智能问答 */
    private String qaStatus;

    /** 云客服工号（电话工号）     */
    private String cloudServiceNumber;

    /** 视频呼叫超时配置，单位秒 */
    private Integer callTimeout;

    /**9个政务窗口是否屏蔽*/
    private String nineZwWindowShieldStatus;

    /**10+N个营商服务点是否屏蔽*/
    private String tenYsFwShieldStatus;
}

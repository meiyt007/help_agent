package com.zfsoft.ha.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @Description //返回用户信息
 * @Author: Wangyh
 * @Date: 2022/7/26 18:46
 */
@Data
@ToString
public class HaLoginUserInfo {
        /** 主键 */
        @ApiModelProperty(name = "主键",notes = "")
        private  Long id;

        /** 姓名 */
        @ApiModelProperty(name = "姓名",notes = "")
        private String name;

        /** 工号 */
        private String workNumber ;

        /** 账号;唯一 */
        @ApiModelProperty(name = "账号",notes = "唯一")
        private String account;

        /** 用户类型;1-导服人员，2-帮代办人员，3-委办局 */
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

        /** 备注 */
        @ApiModelProperty(name = "备注",notes = "")
        private String memo;

        /** 服务位置;提供帮代办服务时，办公地点 */
        private String servicePostion ;

        /** 分组编号 */
        private Long groupId ;

        /** 分组名称 */
        private String groupName ;


        /** 分组编号 */
        private String groupSplitId ;

        /** 分组名称 */
        private String groupSplitName ;

        /** 分组职务;1-组长，2-副组长，3-组员 */
        private String groupPost ;

        /**
         * 转派配置;1-手动接收，2-自动接收
         */
        private String turnConfig;

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

        /** token 唯一标识 */
        private String token ;
}

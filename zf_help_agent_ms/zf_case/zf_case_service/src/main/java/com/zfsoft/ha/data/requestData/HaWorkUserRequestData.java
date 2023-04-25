package com.zfsoft.ha.data.requestData;

import com.zfsoft.ha.data.HaWorkUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Description //帮代办工作人员表请求实体类
 * @Author: Wangyh
 * @Date: 2022/7/27 10:45
 */
@Data
@ToString
@ApiModel(value = "帮代办工作人员表请求实体类",description = "")
public class HaWorkUserRequestData {
    /** 姓名 */
    @ApiModelProperty(name = "姓名",notes = "")
    private String name;
    /** 账号;唯一 */
    @ApiModelProperty(name = "账号",notes = "唯一")
    @NotNull(message = "账号不能为空",groups = {HaWorkUser.UPDATE_GROUP.class})
    private String account;
    /** 手机号 */
    @ApiModelProperty(name = "手机号",notes = "")
    private String phone;
    /** 所属区划 */
    @ApiModelProperty(name = "所属区划",notes = "")
    private String districtOid;
    /** 用户类型;1-导服人员，2-帮代办人员 ，3-委办局*/
    @ApiModelProperty(name = "用户类型",notes = "1-导服人员，2-帮代办人员，3-委办局")
    private String userType;
    /** 删除状态;1-未删除，2-已删除 */
    @ApiModelProperty(name = "删除状态",notes = "1-未删除，2-已删除")
    private String deleteStatus ;
}

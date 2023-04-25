package com.zfsoft.ha.data.requestData;

import com.zfsoft.ha.data.HaWorkUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description //在线时长表请求封装实体类
 * @Author: Wangyh
 * @Date: 2022/8/12 11:03
 */
@Data
@ToString
public class HaOnlineRequestData {
    /** 登录时间 */
    private String loginTime ;
    /** 退出时间 */
    private String logoutTime ;
    /** 退出类型;1-手动退出，2-异常退出，3-登录 */
    private String logoutType ;
    /** 用户主键 */
    private Long workUserId ;

}

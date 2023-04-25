package com.zfsoft.ha.data.responseData;

import com.zfsoft.ha.data.HaVideoAccess;
import com.zfsoft.ha.data.vo.CreateRoomVo;
import com.zfsoft.ha.data.vo.HaWorkUserVo;
import lombok.Data;
import lombok.ToString;

/**
 * @description: 扫码填写帮代办信息接口响应类
 * @author: kangax
 * @date: 2022-08-10 11:28
 **/
@Data
@ToString
public class HaTakeNumHelpResponseData {

    /**
     * 队列编号
     */
    private Long id;

    /**
     * 姓名
     */

    private String name;

    /**
     * 身份证类型
     *
     */
    private String cardType;
    /**
     * 身份证号码
     *
     */
    private String cardNo;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 统一社会信用代码
     */
    private String companyCode;

    /**
     * 排队状态;1-扫码排队中，2-导服已分配
     */
    private String queueStatus;

    /**
     * 排队号
     */
    private String windowsNumber;

    /**
     * 取号类型
     */
    private String takeNumberType;

    private HaWorkUserVo haWorkUser;

    private HaVideoAccess haVideoAccess;

    private CreateRoomVo createRoomVo;

}

package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //队列接口请求实体类
 * @Author: Wangyh
 * @Date: 2022/7/27 15:01
 */
@Data
@ToString
public class HaQueueRequestData {
    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String cardNo;
    /**
     * 手机号
     */
    private String phone;
    /**
     *企业名称
     */
    private String companyName;


    /**
     * 统一社会信用代码
     */
    private String companyCode;
    /**
     *排队状态;1-扫码排队中，2-导服已分配
     */
    private String queueStatus;
    /**
     *服务状态;1-等待服务，2-服务中，3-服务完成
     */
    private String serviceStatus;


    /**
     * 1-快捷帮办，2-圆桌帮办，3-窗口取号
     */
    private String serviceType;

    /**
     * 窗口排队号码
     */
    private String windowsNumber;
}

package com.zfsoft.superwindow.data.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * create by duanx 2022/5/30
 *
 * @description： 好差评办件数据推送
 * @classname: reviewDto
 * @author: duanx
 * @date: 2022/5/30 11:19
 **/
@Data
public class ReviewDto implements Serializable {


    private static final long serialVersionUID = 7304536250372297831L;

    /**
     * 实施编码
     */
    @NotBlank(message = "实施编码不可为空")
    private String taskId;
    /**
     * 事项名称
     */@NotNull(message = "事项名称不为空")
    private String taskName;
    /**
     * 办件编号
     */
    private String projectId;
    /**
     * 办理状态 (传对应的数字1，2，3)
     * 待受理=1，已受理=2，已办结=3
     */
    private String proStatus;
    /**
     * 受理部门编号
     */
    private String proDepartId;
    /**
     * 受理部门
     */
    private String proDepart;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 办理证件号码（DES加密，参照2.2.2）
     */
    private String userCert;
    /**
     * 证件类型（具体参照附录3.1）默认为（111）
     */
    private String certType;
    /**
     * 自然人=1，企业法人=2，事业法人=3
     * 社会组织法人=4，非法人企业=5，行政机关=6，其他组织=9。
     * 例如：如果申请人类型为 “自然人”时，传参数：1
     */
    private String subMatter;
    /**
     * 可为空
     * 业务办理项编码（如果该事项为业务办理项则必须上传）
     */
    private String taskHandleItem;
    /**
     * 即办件=1，承诺办件=2
     */
    private String taskType;
    /**
     * 可空
     *（当申请人为非法定代表人时，此项必填）
     * 代理人姓名（DES加密）
     */
    private String handleUserName;
    /**
     * 可空
     * 代理人证件类型
     * （具体参照附录3.1）默认为（111）
     */
    private String handleUserPageType;
    /**
     * 可空
     * 代理人证件号码（DES加密）
     */
    private String handleUserPageCode;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务次数首次服务=1，第二次服务=2……N次服务=N，
     * N<=99
     */
    private String serviceNumber;
    /**
     * 服务时间
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    private String serviceTime;
    /**
     * 办件受理时间，格式如下： yyyy-MM-dd HH:mm:ss
     * （当办理状态=2或3时，此项必填）
     */
    private String acceptDate;
    /**
     * 办结时间
     * 格式：yyyy-MM-dd HH:mm:ss
     * （当办理状态= 3时，此项必填）
     */
    private String resultDate;
    /**
     * 办件类型（互联网办件传1，政务外网办件 2）
     */
    private String eventType;
    /**
     * 行政区划代码
     */
    private String areaId;
    /**
     * 行政区划名称
     */
    private String areaName;
    /**
     * 可空
     * 事项主题参照附录3.5
     */
    private String itemMatter;
    /**
     * 电话号码（DES加密，参照2.2.2）
     */
    private String phoneNumber;
    /**
     * 受理部门统一社会信用代码
     */
    private String deptCode;
    /**
     * 可空
     * 经办人
     */
    private String proManager;
    /**
     * 可空
     * 经办人编号
     */
    private String proManagerNo;
    /**
     * 可空
     * 科室名称
     */
    private String departmentName;
    /**
     * 线上评价且证件类型为身份证时必传）当申请人证件类型为身份证时，certKey为申请人的身份证号码和姓名通过国家政务服务平台统一身份认证系统提供的身份证散列函数形成的散列值
     */
    private String certKey;
    /**
     * 应用id（获取评价地址时必传）
     */
    private String appId;
    /**
     * 需要评价的渠道（获取评价地址时必传）
     */
    private String pf;
    /**
     * 可空
     * 科室编码
     */
    private String officeCode;
    /**
     * 可空
     * 科室名称
     */
    private String officeName;
    /**
     *传空值即可
     */
    private String ISZJ;
    /**
     *调用类型
     */
    private String callType;
    /**
     *接口id
     */
    private String interId;
}

package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description //申请人确认对象
 * @Author: Wangyh
 * @Date: 2022/9/14 15:26
 */
@Data
@ToString
public class WorkApplyInfoVo {
    /**
     * 是否是主题事项，0：否；1：是
     */
    private String isScene;
    /**
     * 事项id
     */
    private String itemId;
    /**
     * 事项编码
     */
    private String itemNo;
    /**
     * 申请人姓名
     */
    private String username;
    /**
     * 申请人证照类型，数值：1-身份证
     */
    private String licenseType;
    /**
     * 申请人证照编码
     */
    private String licenseNo;
    /**
     * 申请人手机号
     */
    private String mobile;

    private List<Status> status;

    /**
     *  办理对象编号，如果类型是个人，则为身份证号等；如果类型是企业，则为统一社会信用代码
     */
    private String targetNo;
    /**
     * 办理对象类型，取值有个人和企业
     */
    private String targetType;
    /**
     * 办理对象名称，企业名称、个人姓名
     */
    private String targetName;
    /**
     * 办理对象证件类型，取值：身份证、护照、军官证、营业执照
     */
    private String targetLicenseType;
    /**
     * 号票主键，综窗办件必传
     */
    private String callId;
    /**
     * 部门code，综窗办件必传
     */
    private String orgCode;
//    @Data
//    @ToString
//    class Status{
//        /**
//         * 非必填，情形ID
//         */
//        private String statusId;
//        /**
//         * 情形编码33位编码
//         */
//        private String statusNo;
//        /**
//         * 必填，情形名称
//         */
//        private String statusName;
//        /**
//         * 子事项编码，33位编码倒数第三位开始，往前12位
//         */
//        private String statusItemNo;
//        /**
//         * 子事项ID
//         */
//        private String statusItemId;
//    }
}

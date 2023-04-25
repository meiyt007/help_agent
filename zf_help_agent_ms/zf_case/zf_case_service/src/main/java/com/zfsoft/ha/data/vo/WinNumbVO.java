package com.zfsoft.ha.data.vo;

import lombok.Data;

/**
 * 窗口取号推送实体类
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/25 15:19
 */
@Data
public class WinNumbVO {
    /**
     * 身份证
     */
    private String cardNo;
    /**
     * 企业统一社会信用代码
     */
    private String companyCode;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 事项目录分类名称
     */
    private String stCategoryName;
    /**
     * 描述
     */
    private String stDesc;
    /**
     * 大厅地址
     */
    private String stHallAddress;
    /**
     * 大厅全称
     */
    private String stHallFullName;
    /**
     * 号票号码
     */
    private String stNumber;
    /**
     * 时间
     */
    private String stTakeDate;
    /**
     * 等待人数
     */
    private long waitCount;

}

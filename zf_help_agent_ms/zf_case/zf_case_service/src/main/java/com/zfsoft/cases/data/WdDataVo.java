package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //过渡VO
 * @Author: Wangyh
 * @Date: 2023/1/12 10:50
 */
@Data
@ToString
public class WdDataVo {
    /**
     * 所属办件
     */
    private String caseOid;
    /**
     * 姓名
     */
    private String name;
    /**
     * 证件号码
     */
    private String cardNo;
    /**
     * 手机号码
     */
    private String phone;


}

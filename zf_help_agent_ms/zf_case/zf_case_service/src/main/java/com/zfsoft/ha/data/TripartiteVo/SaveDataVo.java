package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //领取方式请求实体类
 * @Author: Wangyh
 * @Date: 2022/12/14 13:13
 */
@Data
public class SaveDataVo {
    /**
     * 办件id
     */
    private String applyId;
    /**
     * 发证方式：自取，物流，无结果物
     */
    private String certWay;
    /**
     * 收货人
     */
    private String receiver;
    /**
     * 收货人手机号
     */
    private String revPhone;
    /**
     * 收货人邮政编号
     */
    private String revZipcode;
    /**
     * 收货人省市
     */
    private String revProvince;
    /**
     * 收货人城市
     */
    private String revCity;
    /**
     * 收货人区县
     */
    private String revArea;
    /**
     * 收货人详细地址
     */
    private String revAddress;
    /**
     * 发货人
     */
    private String sender;
    /**
     * 发货人手机号
     */
    private String sndPhone;
    /**
     * 发货人邮政编号
     */
    private String sndZipcode;
    /**
     * 发货人省市
     */
    private String sndProvince;
    /**
     * 发货人城市
     */
    private String sndCity;
    /**
     * 发货人区县
     */
    private String sndArea;
    /**
     * 发货人详细地址
     */
    private String sndAddress;
    /**
     * certWay=物流时，该字段必传，配送公司，取值为EMS、中国邮政、顺丰速递、其
     * 他
     */
    private String expressCompany;

}

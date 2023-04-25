package com.zfsoft.single.data.ywbl.vo;

import lombok.Data;

/**
 * @（#）: SignImageVo
 * @description: 签名信息临时表
 * @author: wangwg
 * @date: 2021/8/17
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Data
public class SignImageVo {
    private String base64Img;
    private String caseOid;
    private String cardNo;
}

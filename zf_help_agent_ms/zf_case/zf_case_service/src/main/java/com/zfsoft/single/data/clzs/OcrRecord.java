package com.zfsoft.single.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2020-12-14 19:08:29
 * @description: 材料识别记录表
 */
@Data
public class OcrRecord {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;
    private String ocrRecordOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ahs_ocr_record.ATTA_OID
     *
     * @mbggenerated
     */
    private String attaOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ahs_ocr_record.CASE_OID
     *
     * @mbggenerated
     */
    private String caseOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ahs_ocr_record.BUSINESS_LICENSE_STATUS
     *
     * @mbggenerated
     */
    private String businessLicenseStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ahs_ocr_record.OCR_RESULT
     *
     * @mbggenerated
     */
    private String ocrResult;


    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;



}

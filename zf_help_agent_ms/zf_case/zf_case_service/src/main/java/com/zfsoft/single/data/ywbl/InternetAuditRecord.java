package com.zfsoft.single.data.ywbl;

import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InternetAuditRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.RECORD_OID
     *
     * @mbggenerated
     */
    private String recordOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.CASE_OID
     *
     * @mbggenerated
     */
    private String caseOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.AUDIT_RESULT
     *
     * @mbggenerated
     */
    private String auditResult;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.RESULT_DESC
     *
     * @mbggenerated
     */
    private String resultDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.CREATE_USER
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_internet_audit_record.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

}
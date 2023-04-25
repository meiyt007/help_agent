package com.zfsoft.single.data.yxpz;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class SxMaterialBill {
    /**
     * 新增模板，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新模板，验证规则组
     */
    public interface UPDATE_GROUP{};
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.BILL_OID
     *
     * @mbggenerated
     */
    private String billOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_NAME
     *
     * @mbggenerated
     */
    private String directoryName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_GENRE
     *
     * @mbggenerated
     */
    private String directoryGenre;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_CODE
     *
     * @mbggenerated
     */
    private String directoryCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_OBJ
     *
     * @mbggenerated
     */
    private String directoryObj;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_TYPE
     *
     * @mbggenerated
     */
    private Integer directoryType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_CODE_PRE
     *
     * @mbggenerated
     */
    private String directoryCodePre;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DIRECTORY_CODE_POS
     *
     * @mbggenerated
     */
    private Integer directoryCodePos;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.DEPARTMENT_TYPE
     *
     * @mbggenerated
     */
    private String departmentType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_bill.service_oid
     *
     * @mbggenerated
     */
    private String serviceOid;

    private String directoryStatus;
     }
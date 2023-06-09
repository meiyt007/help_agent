package com.zfsoft.single.data.yxpz;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class SxMaterialElmsConfig {
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
     * This field corresponds to the database column t_sx_material_elms_config.CONFIG_OID
     *
     * @mbggenerated
     */
    private String configOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_elms_config.MATERIAL_OID
     *
     * @mbggenerated
     */
    private String materialOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_elms_config.BILL_OID
     *
     * @mbggenerated
     */
    private String billOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_elms_config.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_elms_config.CREATE_USER
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_material_elms_config.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;


    }
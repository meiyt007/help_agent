package com.zfsoft.superwindow.data.zsgl;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ZskDict {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.ZSK_DICT_OID
     *
     * @mbggenerated
     */
    private String zskDictOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.CODE
     *
     * @mbggenerated
     */
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.NAME
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.PARENT_OID
     *
     * @mbggenerated
     */
    private String parentOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.REMARK
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.PATH
     *
     * @mbggenerated
     */
    private String path;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.SORT
     *
     * @mbggenerated
     */
    private Integer sort;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.CREAT_USER_OID
     *
     * @mbggenerated
     */
    private String creatUserOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_zsk_dict.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    private List<ZskDict> childrenList;

}
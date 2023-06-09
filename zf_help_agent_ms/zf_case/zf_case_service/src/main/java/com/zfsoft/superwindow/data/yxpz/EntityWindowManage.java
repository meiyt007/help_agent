package com.zfsoft.superwindow.data.yxpz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityWindowManage {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.WINDOW_OID
     *
     * @mbggenerated
     */
    private String windowOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.DISTRICT_OID
     *
     * @mbggenerated
     */
    private String districtOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.DISTRICT_NAME
     *
     * @mbggenerated
     */
    private String districtName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.ORGAN_OID
     *
     * @mbggenerated
     */
    private String organOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.ORGAN_NAME
     *
     * @mbggenerated
     */
    private String organName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.WINDOW_NAME
     *
     * @mbggenerated
     */
    private String windowName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.WINDOW_NUM
     *
     * @mbggenerated
     */
    private String windowNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.WINDOW_SEAT
     *
     * @mbggenerated
     */
    private String windowSeat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.ATTA_OID
     *
     * @mbggenerated
     */
    private String attaOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.PC_LOCATION
     *
     * @mbggenerated
     */
    private String pcLocation;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.IP_ADDRESS
     *
     * @mbggenerated
     */
    private String ipAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.MAC_ADDRESS
     *
     * @mbggenerated
     */
    private String macAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_window_manage.IN_FLOOR
     *
     * @mbggenerated
     */
    private String inFloor;

    /**
     * 窗口授权用户
     */
    private String userOids;
    /**
     * 窗口授权用户名称
     */
    private String userNames;
}
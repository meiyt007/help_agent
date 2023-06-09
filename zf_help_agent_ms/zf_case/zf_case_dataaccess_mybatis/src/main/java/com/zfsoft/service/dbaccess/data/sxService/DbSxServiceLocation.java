package com.zfsoft.service.dbaccess.data.sxService;

import java.util.Date;

public class DbSxServiceLocation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.SERVICE_LOCATION_OID
     *
     * @mbggenerated
     */
    private String serviceLocationOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.EXTEND_OID
     *
     * @mbggenerated
     */
    private String extendOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.HANDLE_LOCATION_OID
     *
     * @mbggenerated
     */
    private String handleLocationOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.LOCATION_NAME
     *
     * @mbggenerated
     */
    private String locationName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.LOCATION_ADDR
     *
     * @mbggenerated
     */
    private String locationAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.ACCEPT_DATE
     *
     * @mbggenerated
     */
    private String acceptDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.DEL_FLAG
     *
     * @mbggenerated
     */
    private Short delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_location.BUS_ROUTE
     *
     * @mbggenerated
     */
    private String busRoute;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.ID
     *
     * @return the value of t_sx_service_location.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.ID
     *
     * @param id the value for t_sx_service_location.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.SERVICE_LOCATION_OID
     *
     * @return the value of t_sx_service_location.SERVICE_LOCATION_OID
     *
     * @mbggenerated
     */
    public String getServiceLocationOid() {
        return serviceLocationOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.SERVICE_LOCATION_OID
     *
     * @param serviceLocationOid the value for t_sx_service_location.SERVICE_LOCATION_OID
     *
     * @mbggenerated
     */
    public void setServiceLocationOid(String serviceLocationOid) {
        this.serviceLocationOid = serviceLocationOid == null ? null : serviceLocationOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.EXTEND_OID
     *
     * @return the value of t_sx_service_location.EXTEND_OID
     *
     * @mbggenerated
     */
    public String getExtendOid() {
        return extendOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.EXTEND_OID
     *
     * @param extendOid the value for t_sx_service_location.EXTEND_OID
     *
     * @mbggenerated
     */
    public void setExtendOid(String extendOid) {
        this.extendOid = extendOid == null ? null : extendOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.HANDLE_LOCATION_OID
     *
     * @return the value of t_sx_service_location.HANDLE_LOCATION_OID
     *
     * @mbggenerated
     */
    public String getHandleLocationOid() {
        return handleLocationOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.HANDLE_LOCATION_OID
     *
     * @param handleLocationOid the value for t_sx_service_location.HANDLE_LOCATION_OID
     *
     * @mbggenerated
     */
    public void setHandleLocationOid(String handleLocationOid) {
        this.handleLocationOid = handleLocationOid == null ? null : handleLocationOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.LOCATION_NAME
     *
     * @return the value of t_sx_service_location.LOCATION_NAME
     *
     * @mbggenerated
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.LOCATION_NAME
     *
     * @param locationName the value for t_sx_service_location.LOCATION_NAME
     *
     * @mbggenerated
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName == null ? null : locationName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.LOCATION_ADDR
     *
     * @return the value of t_sx_service_location.LOCATION_ADDR
     *
     * @mbggenerated
     */
    public String getLocationAddr() {
        return locationAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.LOCATION_ADDR
     *
     * @param locationAddr the value for t_sx_service_location.LOCATION_ADDR
     *
     * @mbggenerated
     */
    public void setLocationAddr(String locationAddr) {
        this.locationAddr = locationAddr == null ? null : locationAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.ACCEPT_DATE
     *
     * @return the value of t_sx_service_location.ACCEPT_DATE
     *
     * @mbggenerated
     */
    public String getAcceptDate() {
        return acceptDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.ACCEPT_DATE
     *
     * @param acceptDate the value for t_sx_service_location.ACCEPT_DATE
     *
     * @mbggenerated
     */
    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate == null ? null : acceptDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.DEL_FLAG
     *
     * @return the value of t_sx_service_location.DEL_FLAG
     *
     * @mbggenerated
     */
    public Short getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.DEL_FLAG
     *
     * @param delFlag the value for t_sx_service_location.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.MODIFY_DATE
     *
     * @return the value of t_sx_service_location.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.MODIFY_DATE
     *
     * @param modifyDate the value for t_sx_service_location.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_location.BUS_ROUTE
     *
     * @return the value of t_sx_service_location.BUS_ROUTE
     *
     * @mbggenerated
     */
    public String getBusRoute() {
        return busRoute;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_location.BUS_ROUTE
     *
     * @param busRoute the value for t_sx_service_location.BUS_ROUTE
     *
     * @mbggenerated
     */
    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute == null ? null : busRoute.trim();
    }
}
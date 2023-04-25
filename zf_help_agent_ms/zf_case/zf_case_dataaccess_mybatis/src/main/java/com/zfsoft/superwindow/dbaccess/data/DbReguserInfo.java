package com.zfsoft.superwindow.dbaccess.data;

public class DbReguserInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_reguser_info.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_reguser_info.USER_OID
     *
     * @mbggenerated
     */
    private String userOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_reguser_info.BUSS_VENUE_DISTRICT_OID
     *
     * @mbggenerated
     */
    private String bussVenueDistrictOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_reguser_info.SPECIFIC_LOCATION
     *
     * @mbggenerated
     */
    private String specificLocation;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_reguser_info.POST_CODE
     *
     * @mbggenerated
     */
    private String postCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_reguser_info.ID
     *
     * @return the value of t_reguser_info.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_reguser_info.ID
     *
     * @param id the value for t_reguser_info.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_reguser_info.USER_OID
     *
     * @return the value of t_reguser_info.USER_OID
     *
     * @mbggenerated
     */
    public String getUserOid() {
        return userOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_reguser_info.USER_OID
     *
     * @param userOid the value for t_reguser_info.USER_OID
     *
     * @mbggenerated
     */
    public void setUserOid(String userOid) {
        this.userOid = userOid == null ? null : userOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_reguser_info.BUSS_VENUE_DISTRICT_OID
     *
     * @return the value of t_reguser_info.BUSS_VENUE_DISTRICT_OID
     *
     * @mbggenerated
     */
    public String getBussVenueDistrictOid() {
        return bussVenueDistrictOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_reguser_info.BUSS_VENUE_DISTRICT_OID
     *
     * @param bussVenueDistrictOid the value for t_reguser_info.BUSS_VENUE_DISTRICT_OID
     *
     * @mbggenerated
     */
    public void setBussVenueDistrictOid(String bussVenueDistrictOid) {
        this.bussVenueDistrictOid = bussVenueDistrictOid == null ? null : bussVenueDistrictOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_reguser_info.SPECIFIC_LOCATION
     *
     * @return the value of t_reguser_info.SPECIFIC_LOCATION
     *
     * @mbggenerated
     */
    public String getSpecificLocation() {
        return specificLocation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_reguser_info.SPECIFIC_LOCATION
     *
     * @param specificLocation the value for t_reguser_info.SPECIFIC_LOCATION
     *
     * @mbggenerated
     */
    public void setSpecificLocation(String specificLocation) {
        this.specificLocation = specificLocation == null ? null : specificLocation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_reguser_info.POST_CODE
     *
     * @return the value of t_reguser_info.POST_CODE
     *
     * @mbggenerated
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_reguser_info.POST_CODE
     *
     * @param postCode the value for t_reguser_info.POST_CODE
     *
     * @mbggenerated
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode == null ? null : postCode.trim();
    }
}
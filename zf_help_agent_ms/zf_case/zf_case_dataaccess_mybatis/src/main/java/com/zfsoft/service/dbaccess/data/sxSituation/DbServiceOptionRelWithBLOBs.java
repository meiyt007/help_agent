package com.zfsoft.service.dbaccess.data.sxSituation;

public class DbServiceOptionRelWithBLOBs extends DbServiceOptionRel {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.VALUE_OIDS
     *
     * @mbggenerated
     */
    private String valueOids;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.TITLE_OIDS
     *
     * @mbggenerated
     */
    private String titleOids;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.VALUE_OIDS
     *
     * @return the value of t_service_option_rel.VALUE_OIDS
     *
     * @mbggenerated
     */
    @Override
    public String getValueOids() {
        return valueOids;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.VALUE_OIDS
     *
     * @param valueOids the value for t_service_option_rel.VALUE_OIDS
     *
     * @mbggenerated
     */
    @Override
    public void setValueOids(String valueOids) {
        this.valueOids = valueOids == null ? null : valueOids.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.TITLE_OIDS
     *
     * @return the value of t_service_option_rel.TITLE_OIDS
     *
     * @mbggenerated
     */
    @Override
    public String getTitleOids() {
        return titleOids;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.TITLE_OIDS
     *
     * @param titleOids the value for t_service_option_rel.TITLE_OIDS
     *
     * @mbggenerated
     */
    @Override
    public void setTitleOids(String titleOids) {
        this.titleOids = titleOids == null ? null : titleOids.trim();
    }
}
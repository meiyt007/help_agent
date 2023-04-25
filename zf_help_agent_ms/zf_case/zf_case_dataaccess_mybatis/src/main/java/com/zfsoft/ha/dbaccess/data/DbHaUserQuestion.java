package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaUserQuestion {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.WORK_USER_ID
     *
     * @mbg.generated
     */
    private Long workUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.QUESTION
     *
     * @mbg.generated
     */
    private String question;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.ANSWER
     *
     * @mbg.generated
     */
    private String answer;
    /*
    * 备注
    * */
    private String note;


    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.DELETE_STATUS
     *
     * @mbg.generated
     */
    private String deleteStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_question.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;


    private DbHaWorkUser workUser;

    public DbHaWorkUser getDbThaWorkUser() {
        return workUser;
    }

    public void setDbThaWorkUser(DbHaWorkUser dbThaWorkUser) {
        this.workUser = dbThaWorkUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.ID
     *
     * @return the value of t_ha_user_question.ID
     *
     * @mbg.generated
     */


    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.ID
     *
     * @param id the value for t_ha_user_question.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.WORK_USER_ID
     *
     * @return the value of t_ha_user_question.WORK_USER_ID
     *
     * @mbg.generated
     */
    public Long getWorkUserId() {
        return workUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.WORK_USER_ID
     *
     * @param workUserId the value for t_ha_user_question.WORK_USER_ID
     *
     * @mbg.generated
     */
    public void setWorkUserId(Long workUserId) {
        this.workUserId = workUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.QUESTION
     *
     * @return the value of t_ha_user_question.QUESTION
     *
     * @mbg.generated
     */
    public String getQuestion() {
        return question;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.QUESTION
     *
     * @param question the value for t_ha_user_question.QUESTION
     *
     * @mbg.generated
     */
    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.ANSWER
     *
     * @return the value of t_ha_user_question.ANSWER
     *
     * @mbg.generated
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.ANSWER
     *
     * @param answer the value for t_ha_user_question.ANSWER
     *
     * @mbg.generated
     */
    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.ANSWER
     *
     * @return the value of t_ha_user_question.ANSWER
     *
     * @mbg.generated
     */
    public String getNote() {
        return note;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.ANSWER
     *
     * @param note the value for t_ha_user_question.ANSWER
     *
     * @mbg.generated
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }


    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.DELETE_STATUS
     *
     * @return the value of t_ha_user_question.DELETE_STATUS
     *
     * @mbg.generated
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.DELETE_STATUS
     *
     * @param deleteStatus the value for t_ha_user_question.DELETE_STATUS
     *
     * @mbg.generated
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.CREATE_BY
     *
     * @return the value of t_ha_user_question.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.CREATE_BY
     *
     * @param createBy the value for t_ha_user_question.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.CREATE_DATE
     *
     * @return the value of t_ha_user_question.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.CREATE_DATE
     *
     * @param createDate the value for t_ha_user_question.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.UPDATE_BY
     *
     * @return the value of t_ha_user_question.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.UPDATE_BY
     *
     * @param updateBy the value for t_ha_user_question.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_question.UPDATE_DATE
     *
     * @return the value of t_ha_user_question.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_question.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_user_question.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

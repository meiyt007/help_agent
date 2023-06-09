package com.zfsoft.service.dbaccess.data.sxService;

import java.util.Date;

public class DbSxServiceQuestion {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.QUESTION_OID
     *
     * @mbggenerated
     */
    private String questionOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.SERVICE_OID
     *
     * @mbggenerated
     */
    private String serviceOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.KEY_WORD
     *
     * @mbggenerated
     */
    private String keyWord;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.TITLE
     *
     * @mbggenerated
     */
    private String title;

    /**
     * 问题解答
     */
   // private String answer;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.DEL_FLAG
     *
     * @mbggenerated
     */
    private Short delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.CREATE_USER
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.SORT
     *
     * @mbggenerated
     */
    private Long sort;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_question.ENABLED_FLAG
     *
     * @mbggenerated
     */
    private Short enabledFlag;

    private Date modifyDate;

    private String description;
    private String answer;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }


    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.ID
     *
     * @return the value of t_sx_service_question.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.ID
     *
     * @param id the value for t_sx_service_question.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.QUESTION_OID
     *
     * @return the value of t_sx_service_question.QUESTION_OID
     *
     * @mbggenerated
     */
    public String getQuestionOid() {
        return questionOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.QUESTION_OID
     *
     * @param questionOid the value for t_sx_service_question.QUESTION_OID
     *
     * @mbggenerated
     */
    public void setQuestionOid(String questionOid) {
        this.questionOid = questionOid == null ? null : questionOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.SERVICE_OID
     *
     * @return the value of t_sx_service_question.SERVICE_OID
     *
     * @mbggenerated
     */
    public String getServiceOid() {
        return serviceOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.SERVICE_OID
     *
     * @param serviceOid the value for t_sx_service_question.SERVICE_OID
     *
     * @mbggenerated
     */
    public void setServiceOid(String serviceOid) {
        this.serviceOid = serviceOid == null ? null : serviceOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.KEY_WORD
     *
     * @return the value of t_sx_service_question.KEY_WORD
     *
     * @mbggenerated
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.KEY_WORD
     *
     * @param keyWord the value for t_sx_service_question.KEY_WORD
     *
     * @mbggenerated
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.TITLE
     *
     * @return the value of t_sx_service_question.TITLE
     *
     * @mbggenerated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.TITLE
     *
     * @param title the value for t_sx_service_question.TITLE
     *
     * @mbggenerated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.DEL_FLAG
     *
     * @return the value of t_sx_service_question.DEL_FLAG
     *
     * @mbggenerated
     */
    public Short getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.DEL_FLAG
     *
     * @param delFlag the value for t_sx_service_question.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.CREATE_DATE
     *
     * @return the value of t_sx_service_question.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.CREATE_DATE
     *
     * @param createDate the value for t_sx_service_question.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.CREATE_USER
     *
     * @return the value of t_sx_service_question.CREATE_USER
     *
     * @mbggenerated
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.CREATE_USER
     *
     * @param createUser the value for t_sx_service_question.CREATE_USER
     *
     * @mbggenerated
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.SORT
     *
     * @return the value of t_sx_service_question.SORT
     *
     * @mbggenerated
     */
    public Long getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.SORT
     *
     * @param sort the value for t_sx_service_question.SORT
     *
     * @mbggenerated
     */
    public void setSort(Long sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_question.ENABLED_FLAG
     *
     * @return the value of t_sx_service_question.ENABLED_FLAG
     *
     * @mbggenerated
     */
    public Short getEnabledFlag() {
        return enabledFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_question.ENABLED_FLAG
     *
     * @param enabledFlag the value for t_sx_service_question.ENABLED_FLAG
     *
     * @mbggenerated
     */
    public void setEnabledFlag(Short enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /*public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }*/
}

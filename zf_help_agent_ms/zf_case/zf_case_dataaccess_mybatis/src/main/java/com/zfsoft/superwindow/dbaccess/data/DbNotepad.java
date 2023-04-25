package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

public class DbNotepad {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.NOTEPAD_OID
     *
     * @mbggenerated
     */
    private String notepadOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.ZSLB_DICT_OID
     *
     * @mbggenerated
     */
    private String zslbDictOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.ZSLB_DICT_NAME
     *
     * @mbggenerated
     */
    private String zslbDictName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.KNOWLEDGE_TITLE
     *
     * @mbggenerated
     */
    private String knowledgeTitle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.KNOWLEDGE_CONTENT
     *
     * @mbggenerated
     */
    private String knowledgeContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.NOTE
     *
     * @mbggenerated
     */
    private String note;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.CREATE_USER_OID
     *
     * @mbggenerated
     */
    private String createUserOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.CREATE_USER_NAME
     *
     * @mbggenerated
     */
    private String createUserName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.SHARE_USER_OID
     *
     * @mbggenerated
     */
    private String shareUserOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.SHARE_USER_Name
     *
     * @mbggenerated
     */
    private String shareUserName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.SHARE_DATE
     *
     * @mbggenerated
     */
    private Date shareDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.DEL_FLAG
     *
     * @mbggenerated
     */
    private Integer delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_notepad.SHARE_FLAG
     *
     * @mbggenerated
     */
    private Integer shareFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.ID
     *
     * @return the value of t_notepad.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.ID
     *
     * @param id the value for t_notepad.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.NOTEPAD_OID
     *
     * @return the value of t_notepad.NOTEPAD_OID
     *
     * @mbggenerated
     */
    public String getNotepadOid() {
        return notepadOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.NOTEPAD_OID
     *
     * @param notepadOid the value for t_notepad.NOTEPAD_OID
     *
     * @mbggenerated
     */
    public void setNotepadOid(String notepadOid) {
        this.notepadOid = notepadOid == null ? null : notepadOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.ZSLB_DICT_OID
     *
     * @return the value of t_notepad.ZSLB_DICT_OID
     *
     * @mbggenerated
     */
    public String getZslbDictOid() {
        return zslbDictOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.ZSLB_DICT_OID
     *
     * @param zslbDictOid the value for t_notepad.ZSLB_DICT_OID
     *
     * @mbggenerated
     */
    public void setZslbDictOid(String zslbDictOid) {
        this.zslbDictOid = zslbDictOid == null ? null : zslbDictOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.ZSLB_DICT_NAME
     *
     * @return the value of t_notepad.ZSLB_DICT_NAME
     *
     * @mbggenerated
     */
    public String getZslbDictName() {
        return zslbDictName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.ZSLB_DICT_NAME
     *
     * @param zslbDictName the value for t_notepad.ZSLB_DICT_NAME
     *
     * @mbggenerated
     */
    public void setZslbDictName(String zslbDictName) {
        this.zslbDictName = zslbDictName == null ? null : zslbDictName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.KNOWLEDGE_TITLE
     *
     * @return the value of t_notepad.KNOWLEDGE_TITLE
     *
     * @mbggenerated
     */
    public String getKnowledgeTitle() {
        return knowledgeTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.KNOWLEDGE_TITLE
     *
     * @param knowledgeTitle the value for t_notepad.KNOWLEDGE_TITLE
     *
     * @mbggenerated
     */
    public void setKnowledgeTitle(String knowledgeTitle) {
        this.knowledgeTitle = knowledgeTitle == null ? null : knowledgeTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.KNOWLEDGE_CONTENT
     *
     * @return the value of t_notepad.KNOWLEDGE_CONTENT
     *
     * @mbggenerated
     */
    public String getKnowledgeContent() {
        return knowledgeContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.KNOWLEDGE_CONTENT
     *
     * @param knowledgeContent the value for t_notepad.KNOWLEDGE_CONTENT
     *
     * @mbggenerated
     */
    public void setKnowledgeContent(String knowledgeContent) {
        this.knowledgeContent = knowledgeContent == null ? null : knowledgeContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.NOTE
     *
     * @return the value of t_notepad.NOTE
     *
     * @mbggenerated
     */
    public String getNote() {
        return note;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.NOTE
     *
     * @param note the value for t_notepad.NOTE
     *
     * @mbggenerated
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.CREATE_USER_OID
     *
     * @return the value of t_notepad.CREATE_USER_OID
     *
     * @mbggenerated
     */
    public String getCreateUserOid() {
        return createUserOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.CREATE_USER_OID
     *
     * @param createUserOid the value for t_notepad.CREATE_USER_OID
     *
     * @mbggenerated
     */
    public void setCreateUserOid(String createUserOid) {
        this.createUserOid = createUserOid == null ? null : createUserOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.CREATE_USER_NAME
     *
     * @return the value of t_notepad.CREATE_USER_NAME
     *
     * @mbggenerated
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.CREATE_USER_NAME
     *
     * @param createUserName the value for t_notepad.CREATE_USER_NAME
     *
     * @mbggenerated
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.CREATE_DATE
     *
     * @return the value of t_notepad.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.CREATE_DATE
     *
     * @param createDate the value for t_notepad.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.MODIFY_DATE
     *
     * @return the value of t_notepad.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.MODIFY_DATE
     *
     * @param modifyDate the value for t_notepad.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.SHARE_USER_OID
     *
     * @return the value of t_notepad.SHARE_USER_OID
     *
     * @mbggenerated
     */
    public String getShareUserOid() {
        return shareUserOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.SHARE_USER_OID
     *
     * @param shareUserOid the value for t_notepad.SHARE_USER_OID
     *
     * @mbggenerated
     */
    public void setShareUserOid(String shareUserOid) {
        this.shareUserOid = shareUserOid == null ? null : shareUserOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.SHARE_USER_Name
     *
     * @return the value of t_notepad.SHARE_USER_Name
     *
     * @mbggenerated
     */
    public String getShareUserName() {
        return shareUserName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.SHARE_USER_Name
     *
     * @param shareUserName the value for t_notepad.SHARE_USER_Name
     *
     * @mbggenerated
     */
    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName == null ? null : shareUserName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.SHARE_DATE
     *
     * @return the value of t_notepad.SHARE_DATE
     *
     * @mbggenerated
     */
    public Date getShareDate() {
        return shareDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.SHARE_DATE
     *
     * @param shareDate the value for t_notepad.SHARE_DATE
     *
     * @mbggenerated
     */
    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.DEL_FLAG
     *
     * @return the value of t_notepad.DEL_FLAG
     *
     * @mbggenerated
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.DEL_FLAG
     *
     * @param delFlag the value for t_notepad.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_notepad.SHARE_FLAG
     *
     * @return the value of t_notepad.SHARE_FLAG
     *
     * @mbggenerated
     */
    public Integer getShareFlag() {
        return shareFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_notepad.SHARE_FLAG
     *
     * @param shareFlag the value for t_notepad.SHARE_FLAG
     *
     * @mbggenerated
     */
    public void setShareFlag(Integer shareFlag) {
        this.shareFlag = shareFlag;
    }
}
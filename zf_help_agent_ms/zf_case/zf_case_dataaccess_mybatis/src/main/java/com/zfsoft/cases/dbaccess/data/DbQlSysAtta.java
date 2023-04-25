package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件表(SysAtta)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbQlSysAtta implements Serializable {
    private static final long serialVersionUID = 975230626736651347L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String attaOid;
    /**
     * 业务主键
     */
    private String zzqAttaOid;
    /**
     * 附件名称
     */
    private String name;
    /**
     * 附件原始名称
     */
    private String originName;
    /**
     * 附件路径
     */
    private String filePath;
    /**
     * 附件扩展名
     */
    private String extensionName;
    /**
     * 上传时间
     */
    private Date uploadDate;
    /**
     * 所属用户
     */
    private String userOid;
    /**
     * 删除状态（否0、是1）
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date createDate;

    private Date modifyDate;

    public String getZzqAttaOid() {
        return zzqAttaOid;
    }

    public void setZzqAttaOid(String zzqAttaOid) {
        this.zzqAttaOid = zzqAttaOid;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    private String fastdfsUploadUrl;

    private String fastdfsNginxUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttaOid() {
        return attaOid;
    }

    public void setAttaOid(String attaOid) {
        this.attaOid = attaOid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFastdfsUploadUrl() {
        return fastdfsUploadUrl;
    }

    public void setFastdfsUploadUrl(String fastdfsUploadUrl) {
        this.fastdfsUploadUrl = fastdfsUploadUrl;
    }

    public String getFastdfsNginxUrl() {
        return fastdfsNginxUrl;
    }

    public void setFastdfsNginxUrl(String fastdfsNginxUrl) {
        this.fastdfsNginxUrl = fastdfsNginxUrl;
    }
}

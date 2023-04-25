package com.zfsoft.superwindow.data.front;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class WorkerHonor {
    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 工作人员ID
     */
    private String userOid;
    /**
     * 荣誉ID
     */
    private String honorOid;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 逻辑删除状态 0 未删除 1 删除
     */
    private Integer delFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getHonorOid() {
        return honorOid;
    }

    public void setHonorOid(String honorOid) {
        this.honorOid = honorOid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}

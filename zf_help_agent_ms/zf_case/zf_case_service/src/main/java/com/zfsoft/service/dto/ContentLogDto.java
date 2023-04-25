package com.zfsoft.service.dto;

/**
 * Description:内容日志dto
 *
 * @author : yuy
 * @date :  2021/1/25
 */
public class ContentLogDto {

    private String topConOid;//内容顶级主键
    private String conOid; //内容主键
    private Integer type;  //日志类型  ：1：新建；2：修改
    private Integer version;    //事项版本
    private String originalJson; //修改前数据
    private String updateJson;  //修改后数据、
    private String userId;
    private String userName;
    private String handleOid;


    public ContentLogDto(){}

    public ContentLogDto(String topConOid, String conOid, String userId, String userName, Integer version, Integer type){
        this.topConOid = topConOid;
        this.conOid = conOid;
        this.userId = userId;
        this.userName = userName;
        this.version = version;
        this.type = type;
    }

    public ContentLogDto(String topConOid, String conOid, String userId, String userName, Integer version, Integer type,
                         String originalJson, String updateJson, String handleOid){
        this.topConOid = topConOid;
        this.conOid = conOid;
        this.userId = userId;
        this.userName = userName;
        this.version = version;
        this.type = type;
        this.originalJson = originalJson;
        this.updateJson = updateJson;
        this.handleOid = handleOid;
    }



    public String getHandleOid() {
        return handleOid;
    }

    public void setHandleOid(String handleOid) {
        this.handleOid = handleOid;
    }


    public String getConOid() {
        return conOid;
    }

    public void setConOid(String conOid) {
        this.conOid = conOid;
    }

    public String getTopConOid() {
        return topConOid;
    }

    public void setTopConOid(String topConOid) {
        this.topConOid = topConOid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOriginalJson() {
        return originalJson;
    }

    public void setOriginalJson(String originalJson) {
        this.originalJson = originalJson;
    }

    public String getUpdateJson() {
        return updateJson;
    }

    public void setUpdateJson(String updateJson) {
        this.updateJson = updateJson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

package com.zfsoft.ha.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2022/7/19 13:45
 */
@ApiModel(description= "内层数据响应封装")
public class HaMessageVo {
    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "id", required=false)
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "title", required=false)
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "content", required=false)
    private String content;
    /**
     * 链接地址
     */
    @ApiModelProperty(value = "url", required=false)
    private String url;
    /**
     * 读取状态;1-未读取，2-已读取
     */
    @ApiModelProperty(value = "readStatus", required=false)
    private String readStatus;

    public HaMessageVo(Long id, String title, String content, String url, String readStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
        this.readStatus = readStatus;
    }

    public HaMessageVo(Long id, String title, String content, String readStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.readStatus = readStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HaMessageVo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}

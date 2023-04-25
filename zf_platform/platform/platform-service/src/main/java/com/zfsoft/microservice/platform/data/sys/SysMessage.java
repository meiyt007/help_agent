package com.zfsoft.microservice.platform.data.sys;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 消息信息表
 * @author: wuxx
 * @Date: 2020/10/23 13:02
 **/
@Data
public class SysMessage{

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};


    /*主键*/
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /*发信人*/
    private String userName;

    /*标题*/
    @NotNull(message = "标题不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "标题长度为1-100",groups = {INSERT_GROUP.class})
    private String title;

    /*内容*/
    @NotNull(message = "内容不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 1000,message = "内容长度为1-1000",groups = {INSERT_GROUP.class})
    private String content;

    /*链接地址*/
    private String url;

    /*消息时间*/
    private Date createDate;

    /*接收人编号*/
    @NotNull(message = "接收人不能为空",groups = {INSERT_GROUP.class})
    private String userOid;

    /*附件主键的集合*/
    private String attaOids;

    /*
    * 读取状态 0未读 1已读
    */
    private Integer readStatus;

    /*读取时间*/
    private Date readDate;

    /*
     * 删除状态 0未删除 1已删除
     */
    private Integer isDelete;

    //临时字段
    /*接受人用户主键的集合*/
    private List<String> userOids;


}

package com.zfsoft.superwindow.data.zsgl;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:  记事本表
 * @author: liangss
 * @Date: 2020/10/26 18:21
 **/

@Data
@ToString
public class Notepad {
    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    /* 业务主键  */
    private String notepadOid;
    /* 知识分类字典oid */
    private String zslbDictOid;

    /* 知识分类字典名称 */
    private String zslbDictName;

    /*  知识标题 */
    private String knowledgeTitle;

    /*  知识内容 */
    private String knowledgeContent;

    /*  备注 */
    private String note;

    /*  创建人oid */
    private String createUserOid;
    /*  创建人名称 */
    private String createUserName;
    /*  分享人oid */
    private String shareUserOid;
    /*  分享人名称 */
    private String shareUserName;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /*  分享时间 */
    private Date shareDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;

    /* 共享状态 0 未共享 1 共享  */
    private Integer shareFlag;



}

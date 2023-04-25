package com.zfsoft.ha.data.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description //知识库文件管理表实体类
 * @Author: Wangyh
 * @Date: 2022/12/2 16:34
 */
@Data
public class HaFileKnowledgeBase {
    /**
     *主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String attaOid;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 所属事项主键
     */
    private String serviceOid;

    /**
     * 附件原始名称
     */
    private String originName;

    /**
     * 附件地址
     */
    private String filePath;

    /**
     * fastdfs代理的nginx地址
     */
    private String fastdfsNginxUrl;

    /**
     * 附件扩展名
     */
    private String extensionName;

    /**
     * 删除状态(0否、1是)
     */
    private Short isDelete;

    /**
     * 上传时间
     */
    private Date uploadDate;
    /**
     * "类型 1-文件 2-文字";
     */
    private String kbType;
    /**
     * 答案 纯文本
     */
    private String answerPureText;
    /**
     * 答案 富文本
     */
    private String answeRrichText;
}

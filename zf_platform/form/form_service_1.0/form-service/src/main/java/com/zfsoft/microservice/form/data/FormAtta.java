package com.zfsoft.microservice.form.data;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:  表单附件信息表
 * @author: wuxx
 * @Date: 2021/5/26 11:51
 **/
@Data
@ToString
public class FormAtta {

    /**
     * 新增区划，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新区划，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 附件业务主键 */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String attaOid;

    /* 附件名称 */
    @NotNull(message = "附件名称不能为空",groups = {INSERT_GROUP.class})
    private String name;

    /* 附件原始名称 */
    private String originName;

    /* 附件地址 */
    @NotNull(message = "附件地址不能为空",groups = {INSERT_GROUP.class})
    private String filePath;

    /* 附件存储路径 */
    private String savePath;

    /* 附件扩展名 */
    private String extensionName;

    /* 上传时间 */
    private Date uploadDate;

    /* 接收人编号 */
    private String userOid;

    /* 删除状态 */
    private Integer isDelete;

    /**
     * 文件大小  （因网站前台用户中心我的自制材料上传需要控制文件大小才添加）
     */
    private String fileSize;

    /**
     * @COLUMN_EXPLAIN :  FASTDFS上传URL
     * @TABLE_COLUMN_TYPE : VARCHAR(2)
     */
    private String fastdfsUploadUrl;

    /**
     * 修改日期
     */
    private Date modifyDate;

}

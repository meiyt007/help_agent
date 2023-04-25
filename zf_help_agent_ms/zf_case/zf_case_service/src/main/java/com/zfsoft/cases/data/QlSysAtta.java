package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 附件表(SysAtta)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlSysAtta  {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;
    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = {INSERT_GROUP.class})
    private String attaOid;

    /**
     * 业务主键
     */
    @NotNull(message = "自治区附件主键不能为空",groups = {INSERT_GROUP.class})
    private String zzqAttaOid;

    /**
     * 附件名称
     */
    @NotNull(message = "附件名称不能为空",groups = {INSERT_GROUP.class})
    private String name;
    /**
     * 附件原始名称
     */
    private String originName;
    /**
     * 附件路径
     */
    @NotNull(message = "附件路径不能为空",groups = {INSERT_GROUP.class})
    private String filePath;
    /**
     * 附件扩展名
     */
    @NotNull(message = "附件扩展名不能为空",groups = {INSERT_GROUP.class})
    private String extensionName;
    /**
     * 上传时间
     */
    @NotNull(message = "上传时间不能为空",groups = {INSERT_GROUP.class})
    private Date uploadDate;
    /**
     * 所属用户
     */
    @NotNull(message = "所属用户不能为空",groups = {INSERT_GROUP.class})
    private String userOid;
    /**
     * 删除状态（否0、是1）
     */
    @NotNull(message = "删除状态不能为空",groups = {INSERT_GROUP.class})
    private Integer delFlag;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空",groups = {INSERT_GROUP.class})
    private Date createDate;

    private Date modifyDate;

    private String fastdfsUploadUrl;

    private String fastdfsNginxUrl;

    private String catalogOid;


}

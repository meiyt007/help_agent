package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 材料附件表(QlCaseMaterialAtta)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlCaseMaterialAtta {
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
    private String materialAttaOid;
    /**
     * 所属办件材料
     */
    @NotNull(message = "所属办件材料不能为空",groups = {INSERT_GROUP.class})
    private String caseMaterialOid;
    /**
     * 存储位置
     */
    @NotNull(message = "存储位置不能为空",groups = {INSERT_GROUP.class})
    private String attaOid;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空",groups = {INSERT_GROUP.class})
    private Date createDate;

    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空",groups = {QlCase.INSERT_GROUP.class})
    private Date modifyDate;

    private String materialCatalogOid;

    private String src;

    private QlSysAtta qlSysAtta;


    /**
     * 精细化材料oid
     */
    private  String refinedMaterialOid;

    /**
     * 材料去黑边修改附件地址
     */
    private  String modifyBeforeAttaOid;
    //序号
    private Integer serialNumber;

    // 表单模板地址
    private String templatePdfUrl;

    // 签章文件下载地址
    private String signaturePdfUrl;

    // 智能生成材料类型：1-签章，2-上传
    private Integer autoType;

    private String attaOidWord;
    private String materialAttaOidWord;
    // 表单模板地址
    private String templatePdfUrlWord;
}

package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 办件材料表(QlCaseMaterial)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlCaseMaterial {

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
    private String caseMaterialOid;
    /**
     * 所属事项材料
     */
    @NotNull(message = "所属事项材料不能为空",groups = {INSERT_GROUP.class})
    private String materialOid;

    @NotNull(message = "所属事项材料名称不能为空",groups = {INSERT_GROUP.class})
    private String materialName;
    /**
     * 所属办件
     */
    @NotNull(message = "所属办件不能为空",groups = {INSERT_GROUP.class})
    private String caseOid;
    /**
     * 材料是否已收取（否0、是1）
     */
    @NotNull(message = "材料是否已收取不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private Integer collectionFlag;
    /**
     * 收取方式 1、纸质收取 2、附件 3、扫描 4、容缺 5、证照 7、告知承诺
     */
    @NotNull(message = "收取方式不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String collectionType;
    /**
     * 收取数量
     */
    @NotNull(message = "收取数量不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private Integer collectionNumber;
    /**
     * 收取时间
     */
    @NotNull(message = "收取时间不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private Date collectionDate;
    /**
     * 删除状态（否0、是1）
     */
    @NotNull(message = "删除状态不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private Integer delFlag;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空",groups = {INSERT_GROUP.class})
    private Date createDate;
    /**
     * 创建人
     */
    @NotNull(message = "创建人不能为空",groups = {INSERT_GROUP.class})
    private String createUserOid;

    /**
     * 证照主键
     */
    private String elemLicenseOid;
    /**
     * 证照编码
     */
    private String elemNumber;

    /**
     * 材料目录主键
     */
    private String materialCatalogOid;

    /**
     * 百度自定义识别模板id，多个以英文半角逗号隔开
     */
    private String baiduTemplateIds;


    private List attaList = new ArrayList();

    private List materialCatalogList = new ArrayList<>();


    private List classifyRecList=new ArrayList<>();
    private List attaListwgl = new ArrayList<>();

    //精细化材料列表
    private List refinedMaterialList = new ArrayList<>();

    private String materialSampleAddr;
    /**
     * 预览样表地址
     */
    private String materialSampleAddrYl;

    //材料办件关联表
    private List<QlCaseMaterialAtta> qlCaseMaterialAttaList;

    //用于智审查看
    private Map<String, Object> preTrialResult;

    //用于查看
    private String materialAttaOid;



    /**
     * 审核类型
     */
    private String  auditType;


    /**
     *审核结果状态
     */
    private String  resultStatus;
    /**
     *确认状态
     */
    private String  confirmStatus;

    /**
     *是否可以容缺补正
     */
    private String  rqbzFlag;
    /**
     *证照名称
     */
    private String elecLicenName;
    /**
     *证照编号
     */
    private String elecLicenNumber;


    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空",groups = {QlCase.INSERT_GROUP.class})
    private Date modifyDate;

    /**
     * 是否必须(0必要、1非必要 2容缺)
     */
    private Short mustFlag;

    //证照类型
    private String directoryObj;


    //配置证照目录主键
    private String elecBillOid;

    private String electronicResult;

    // 单人或者多角色
    private Short roleType;

    //签名须知
    private String memo;

    /**
     * 材料类型
     */
    private Short materialType;

    /**
     * 材料形式
     */
    private Short materialFormat;

    //材料份数
    private Long paperNumber;
    /**
     * 材料顺序
     */
    private Long materialSort;
    /**
     * 材料空表路径
     */
    private String materialEmptyAttoid;
    /**
     * 材料空表路径
     */
    private String materialEmptyAddrUrl;
}

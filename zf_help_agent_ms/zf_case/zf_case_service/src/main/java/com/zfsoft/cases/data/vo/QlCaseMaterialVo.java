package com.zfsoft.cases.data.vo;

import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
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
public class QlCaseMaterialVo {

    /**
     * 主键
     */

    private Long id;
    /**
     * 业务主键
     */
    private String caseMaterialOid;
    /**
     * 所属事项材料
     */
    private String materialOid;

    private String materialName;
    /**
     * 所属办件
     */
    private String caseOid;
    /**
     * 材料是否已收取（否0、是1）
     */
    private Integer collectionFlag;
    /**
     * 收取方式 1、纸质收取 2、附件 3、扫描 4、容缺 5、证照 7、告知承诺
     */
    private String collectionType;
    /**
     * 收取数量
     */
    private Integer collectionNumber;
    /**
     * 收取时间
     */
    private Date collectionDate;
    /**
     * 删除状态（否0、是1）
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
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


    /**
     * 空表附件名称
     */
    private String emptyOriginName;
    /**
     * 样表
     */
    private String simpleOriginName;
}

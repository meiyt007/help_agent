package com.zfsoft.single.data.clzs;

import com.zfsoft.service.sxService.data.ReviewPoints;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 预审结果vo
 *
 * @author chenbw
 * @date 2019年3月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Data
public class PreDetailTrialResultVo {

    /**
     * 预审结果标识 0:通过 1:不通过 2:无需审核
     */
    private String resultStatus;

    /**
     * 事项材料oid
     */
    private String materialOid;

    /**
     * 审核结果oid
     */
    private String auditResultOid;

    /**
     * 细化材料oid
     */
    private String refinedMaterialOid;

    /**
     * 细化材料名称
     */
    private String refinedMaterialName;

    /**
     * 细化材料序号
     */
    private Long serialNumber;


    private String  uploadURL;

    private   List<ReviewPoints> reviewPointsList;

    //不通过结果列表
    List<Map<String, Object>> notPassList = new ArrayList<Map<String, Object>>();
    //通过结果列表
    List<Map<String, Object>> passList = new ArrayList<Map<String, Object>>();
    //人工审核列表
    List<Map<String, Object>> manualAuditList = new ArrayList<Map<String, Object>>();

    private String localImageUrl;

    private String localAuditImageUrl;

    private String attaOid;

    //样本图地址
    private String materialSampleAddr;



}

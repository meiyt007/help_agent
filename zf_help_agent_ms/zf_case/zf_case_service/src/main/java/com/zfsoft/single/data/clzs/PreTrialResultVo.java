package com.zfsoft.single.data.clzs;

import com.zfsoft.cases.data.QlCaseMaterial;
import lombok.Data;

/**
 * 预审结果vo
 *
 * @author chenbw
 * @date 2019年3月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Data
public class PreTrialResultVo {

    /**
     * 预审结果标识 0:通过 1:不通过 2:无需审核
     */
    private String resultStatus;

    /**
     * 展示名称
     */
    private String name;

    /**
     * 目录id
     */
    private String catalogOid;


    /**
     * 材料主键
     */
    private String materialOid;


    /**
     * 提示信息
     */
    private String memoJson;

    /**
     * 办件材料记录表
     */
    /*private CaseFileRec caseFileRec;*/

    /**
     * 父级标识，为Y 则不展示查看按钮
     */
    private String parentStatus;

    /**
     * 子级标识，为Y 则不展示序号，缩进字符
     */
    private String childStatus;

    /**
     * 办件材料表
     */
    private QlCaseMaterial qlCaseMaterial;


    /**
     * 套餐办件材料
     */
    //private ComboCaseMaterial comboCaseMaterial;


    private int xuhao;



}

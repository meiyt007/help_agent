package com.zfsoft.cases.data.vo;

import lombok.Data;

import java.util.List;

/**
 * 办件材料信息
 * @ClassName CaseMaterialVo
 * @Author WangKe
 * @Date 2022/06/10
 **/
@Data
public class CaseMaterialVo {

    /**
     * 事项材料主键
     */
    private String materialOid;

    /**
     * 材料是否已收取，0-已收取，1-未收取
     */
    private String collectionFlag;

    /**
     * 收取方式，0-纸质收取，1-附件上传，2-证照库，3-扫描，6-材料库
     */
    private String collectionType;

    /**
     * 收取数量
     */
    private String collectionNumber;

    /**
     * 收取时间，格式yyyy-MM-dd HH:mm:ss
     */
    private String collectionDate;

    /**
     * 收取人主键，统一受理平台人员编码
     */
    private String createUserOid;

    /**
     * 办件材料附件集合
     */
    private List<CaseMaterialAttaInfoVo> attaInfoList;
}

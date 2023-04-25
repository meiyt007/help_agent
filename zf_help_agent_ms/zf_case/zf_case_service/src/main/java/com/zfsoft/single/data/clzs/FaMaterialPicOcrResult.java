package com.zfsoft.single.data.clzs;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @author: liangss
 * @create: 2020-11-07 16:40:29
 * @description: 材料目录图片识别结果信息
 */
@Data
public class FaMaterialPicOcrResult {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    /* 材料目录图片识别结果信息业务主键  */
    private String faMaterialPicOcrResultOid;

    /* 办件唯一编号  */
    private String uuid;

    /* 材料编号  */
    private String materialOid;
    /* 附件材料关联id*/
    private String materialAttaOid;

    /*  目标编号 */
    private String cataCode;

    /* 图片Base64的MD5值 */
    private String picBase64Md5;

    /* 识别结果json */
    private String resultJson;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;


    //附件oid
    private String attaOid;
    //印章数据
    private String sealResultJson;
    //签字数据
    private String signResultJson;

    //细化材料oid
    private String refinedMaterialOid;

    //办件材料oid
    private String caseMaterialOid;

    //验证失败选择区域
    private String selectionMarkOid;



    private String imageWidth;


    private String imageHeight;

    private String localImageUrl;

    private String localAuditImageUrl;

    private org.json.JSONObject resultJsonObject;

    private SysAtta sysAtta;

}

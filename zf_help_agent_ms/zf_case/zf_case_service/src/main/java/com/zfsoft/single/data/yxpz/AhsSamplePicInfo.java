package com.zfsoft.single.data.yxpz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author: liangss
 * @create: 2020-11-16 14:40:29
 * @description: 样表信息
 */
@Data
public class AhsSamplePicInfo {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    private String ahsSamplePicInfoOid;
    /**
     * 事项关联主键
     */
    private String sampleInfoOid;

    //套餐oid
    private String comboDirectoryOid;
    //材料关联主键
    private String materiaOid;

    private String attaOid;

    private String attaName;

    private String emptyPicFile;

    private String comparePicFile;

    private String sort;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;

    //显示图片列表
    private String attaUrl;

    private List<ExaminePointCarding> examinePointCardingList;

}

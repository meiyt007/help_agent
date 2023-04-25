package com.zfsoft.single.data.yxpz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2020-11-16 14:40:29
 * @description:  审查要点信息
 */
@Data
public class ExaminePointCarding {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;
    //业务主键
    private String examinePointCardingOid;
    //样表关联主键
    private String ahsSamplePicInfoOid;
    //附件关联主键
    private String attaOid;
    //用户id
    private String userOid;
    //用户名称
    private String userName;

    private String x;
    private String y;
    private String ex;
    private String ey;
    //区域名称
    private String name;
    //审查要点详细说明
    private String examineInfo;
    /*  创建时间 */
    private Date createDate;
    /*  修改时间  */
    private Date modifyDate;
    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;

}

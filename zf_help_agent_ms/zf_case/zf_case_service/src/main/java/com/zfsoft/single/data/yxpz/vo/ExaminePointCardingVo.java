package com.zfsoft.single.data.yxpz.vo;

import com.zfsoft.single.data.yxpz.ExaminePointCarding;
import lombok.Data;

import java.util.List;

/**
 * @author: liangss
 * @create: 2020-11-24
 * @description:  审查要点信息vo
 */
@Data
public class ExaminePointCardingVo {

    private List<ExaminePointCarding> examinePointCardingList;
    private String materiaOid;
    private String serviceOid;
    private String ahsSamplePicInfoOid;
    private String attaOid;

}

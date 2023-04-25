package com.zfsoft.single.controller.yxpz;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.yxpz.AhsSamplePicInfo;
import com.zfsoft.single.data.yxpz.ExaminePointCarding;
import com.zfsoft.single.manager.yxpz.AhsSamplePicInfoManager;
import com.zfsoft.single.manager.yxpz.ExaminePointCardingManager;
import com.zfsoft.single.service.yxpz.ExaminePointCardingByComboService;
import com.zfsoft.single.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: liangss
 * @create: 2020-11-16 15:01:29
 * @description: 审查要点控制层
 */
@Slf4j
@RestController
public class ExaminePointCardingByComboController implements ExaminePointCardingByComboService {
    //审查要点
    @Resource
    private ExaminePointCardingManager examinePointCardingManager;

    //样表
    @Resource
    private AhsSamplePicInfoManager ahsSamplePicInfoManager;

    @Override
    public ApiResultSet queryAhsSamplePicInfoByComboList(String materiaOid, String sampleInfoOid, String comboDirectoryOid) {
        Map<String, Object> resultMap = new HashMap<>();
        AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
        ahsSamplePicInfo1.setMateriaOid(materiaOid);
        log.info("参数数据：{}", JSON.toJSONString(materiaOid));
        if (StringUtils.isNotEmpty(sampleInfoOid)) {
            ahsSamplePicInfo1.setSampleInfoOid(sampleInfoOid);
        }
        if (StringUtils.isNotEmpty(comboDirectoryOid)) {
            ahsSamplePicInfo1.setComboDirectoryOid(comboDirectoryOid);
        }
        List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
        for(AhsSamplePicInfo ahsSamplePicInfo2:ahsSamplePicInfoList){
            ExaminePointCarding examinePointCarding= new ExaminePointCarding();
            examinePointCarding.setAhsSamplePicInfoOid(ahsSamplePicInfo2.getAhsSamplePicInfoOid());
            examinePointCarding.setAttaOid(ahsSamplePicInfo2.getAttaOid());
            List<ExaminePointCarding> examinePointCardingList=this.examinePointCardingManager.queryExaminePointCardingList(examinePointCarding);
            ahsSamplePicInfo2.setExaminePointCardingList(examinePointCardingList);
        }
        resultMap.put("ahsSamplePicInfoList", ahsSamplePicInfoList);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }






}

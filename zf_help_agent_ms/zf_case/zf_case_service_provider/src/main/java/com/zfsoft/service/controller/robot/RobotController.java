package com.zfsoft.service.controller.robot;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.common.RobotPage;
import com.zfsoft.service.document.RobotDocument;
import com.zfsoft.service.manager.robot.RobotManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author hutao
 * @date 2022/5/17
 * 问答机器人控制器
 */
@RestController
@RequestMapping("/robot")
@Slf4j
public class RobotController {
    @Resource
    private RobotManager robotManager;

    @RequestMapping( value = "/deleteData",method = {RequestMethod.GET})
    public ApiResultSet deleteData() {
        robotManager.deleteData();
        return  ApiResultSet.ok();
    }

    @RequestMapping( value = "/generateData",method = {RequestMethod.GET})
    public ApiResultSet generateData() {
        robotManager.generateData();
        return  ApiResultSet.ok();
    }

    @RequestMapping( value = "/associationSearch",method = {RequestMethod.GET})
    public ApiResultSet<RobotPage<RobotDocument>> associationSearch(String kws, String curSiteId) {
        RobotPage<RobotDocument> robotPage = robotManager.associationSearch(kws, curSiteId);
        ApiResultSet<RobotPage<RobotDocument>> apiResultSet = new ApiResultSet<RobotPage<RobotDocument>>();
        apiResultSet.setData(robotPage);
        return  apiResultSet;
    }

    @RequestMapping( value = "/analysisSearch",method = {RequestMethod.GET})
    public ApiResultSet<RobotPage<RobotDocument>> analysisSearch(String kws, String curSiteId,
                                                                 String modelType, String talkId,
                                                                 String pageNumber) {
        pageNumber = StrUtil.isNotEmpty(pageNumber) ? pageNumber : "1";
        RobotPage<RobotDocument> robotPage = robotManager.analysisSearch(kws, curSiteId, modelType, Integer.parseInt(pageNumber));
        if (robotPage != null) {
            robotPage.setKws(kws);
            robotPage.getExtraData().put("modelType", modelType);
            robotPage.setTalkId(StrUtil.isNotEmpty(talkId) ? talkId : UUID.randomUUID().toString());
        }
        ApiResultSet<RobotPage<RobotDocument>> apiResultSet = new ApiResultSet<RobotPage<RobotDocument>>();
        apiResultSet.setData(robotPage);
        return  apiResultSet;
    }

    @RequestMapping( value = "/searchDataById",method = {RequestMethod.GET})
    public ApiResultSet<RobotDocument> searchDataById(String docId, String modelType) {
        RobotDocument robotDocument = robotManager.searchDataById(docId, modelType);
        ApiResultSet<RobotDocument> apiResultSet = new ApiResultSet<RobotDocument>();
        apiResultSet.setData(robotDocument);
        return  apiResultSet;
    }

    @RequestMapping( value = "/commonQuestion",method = {RequestMethod.GET})
    public ApiResultSet<List<RobotDocument>> commonQuestion() {
        List<RobotDocument> robotDocumentList = robotManager.commonQuestion();
        ApiResultSet<List<RobotDocument>> apiResultSet = new ApiResultSet<List<RobotDocument>>();
        apiResultSet.setData(robotDocumentList);
        return  apiResultSet;
    }

}

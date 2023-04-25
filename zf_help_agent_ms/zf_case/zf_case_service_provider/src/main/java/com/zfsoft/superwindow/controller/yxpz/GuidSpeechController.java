package com.zfsoft.superwindow.controller.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.GuidSpeech;
import com.zfsoft.superwindow.manager.yxpz.GuidSpeechManager;
import com.zfsoft.superwindow.service.yxpz.GuidSpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 引导语管理功能
 @Dongxl
 **/
@RestController
@Slf4j
public class GuidSpeechController implements GuidSpeechService {

    @Resource
    private GuidSpeechManager guidSpeechManager;
    @Override
    public ApiResultSet queryGuidSpeechPage(GuidSpeech guidSpeech, Integer pageNum, Integer pageSize) {
       PageResult page= guidSpeechManager.queryGuidSpeechPage(guidSpeech,pageNum,pageSize);
        return new ApiResultSet(page);
    }

    @Override
    public ApiResultSet<GuidSpeech> getOneGuidSpeech(String oid) {
       GuidSpeech guid= guidSpeechManager.getOneGuidSpeech(oid);
        return new ApiResultSet<>(guid);
    }

    @Override
    public ApiResultSet<String> deleteOneGuidSpeech(String oid) {
       String str= guidSpeechManager.deleteOneGuidSpeech(oid);
        return new ApiResultSet<>(str);
    }

    @Override
    public ApiResultSet<String> saveOrUpdate(GuidSpeech guidSpeech) {
        String str=guidSpeechManager.saveOrUpdate(guidSpeech);
        return new ApiResultSet<>(str);
    }

    @Override
    public ApiResultSet<GuidSpeech> selectByGuidSpeechCode(String guidSpeechCode) {
        GuidSpeech guid= guidSpeechManager.selectByGuidSpeechCode(guidSpeechCode);
        return new ApiResultSet<>(guid);
    }
}

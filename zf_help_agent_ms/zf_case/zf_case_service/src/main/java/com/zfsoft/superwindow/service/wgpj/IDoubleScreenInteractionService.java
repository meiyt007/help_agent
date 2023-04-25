package com.zfsoft.superwindow.service.wgpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.wgpj.EmotionRecognitionRecord;
import com.zfsoft.superwindow.data.wgpj.ImageVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/interactionService")
public interface IDoubleScreenInteractionService {
    /**
     * 获取评价设备服务系统
     *
     * @return
     * @author wangwg
     * @date 2021-06-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPjServiceSystem", method = {RequestMethod.GET})
    ApiResultSet<String> getPjServiceSystem();

    /**
     * 超级综窗柜台情绪识别
     *
     * @return
     * @author wangwg
     * @date 2021-06-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getEmotionJudgment", method = {RequestMethod.POST})
    ApiResultSet<String> getEmotionJudgment(@RequestBody ImageVo imageVo);

    /**
     * 超级综窗柜台情绪列表
     *
     * @return
     * @author
     * @date 2021-06-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getEmotionList", method = {RequestMethod.POST})
    ApiResultSet getEmotionList(EmotionRecognitionRecord emotionRecognitionRecord,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 根据主键查询情绪信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getEmotionRecognitionRecordById", method = {RequestMethod.GET})
    ApiResultSet<EmotionRecognitionRecord> getEmotionRecognitionRecordById(@RequestParam(value = "id", required = false) Long id);

    /**
     * 根据主键查询情绪信息
     * @param virtualBusinessOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getEmotionListById", method = {RequestMethod.GET})
    ApiResultSet getEmotionListById(@RequestParam(value = "virtualBusinessOid", required = false) String virtualBusinessOid, @RequestParam(value = "caseOid", required = false) String caseOid);
}

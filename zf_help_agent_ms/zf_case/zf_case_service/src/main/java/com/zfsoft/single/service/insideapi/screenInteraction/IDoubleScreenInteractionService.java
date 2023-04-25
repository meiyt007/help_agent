package com.zfsoft.single.service.insideapi.screenInteraction;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.wgpj.ImageVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @（#）: IDoubleScreenInteraction
 * @description: 超级综窗柜台双屏互动评价
 * @author: wangwg
 * @date: 2021/06/15
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
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


}

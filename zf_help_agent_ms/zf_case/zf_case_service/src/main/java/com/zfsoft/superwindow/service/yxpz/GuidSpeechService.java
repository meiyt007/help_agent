package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.GuidSpeech;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 引导语管理功能
 @Dongxl
 **/
@RequestMapping("/guidSpeech")
public interface GuidSpeechService {

    /**
     * 分页查询引导语列表
     * @param guidSpeech
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryGuidSpeechPage(GuidSpeech guidSpeech, Integer pageNum, Integer pageSize);

    /**
     * 根据主键获取详细信息
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOneGuidSpeech",method = {RequestMethod.GET})
    ApiResultSet<GuidSpeech> getOneGuidSpeech(@RequestParam(value = "oid") String oid);

    /**
     * 根据主键删除信息
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.GET})
    ApiResultSet<String> deleteOneGuidSpeech(@RequestParam(value = "oid") String oid);

    /**
     * 保存或修改信息
     * @param guidSpeech
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdate",method = {RequestMethod.POST})
    ApiResultSet<String> saveOrUpdate(@RequestBody GuidSpeech guidSpeech);

    /**
     * 根据code获取详细信息
     * @param guidSpeechCode
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/selectByGuidSpeechCode",method = {RequestMethod.GET})
    ApiResultSet<GuidSpeech> selectByGuidSpeechCode(@RequestParam(value = "guidSpeechCode") String guidSpeechCode);




}

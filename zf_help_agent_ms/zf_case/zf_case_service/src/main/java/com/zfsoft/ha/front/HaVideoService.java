package com.zfsoft.ha.front;

import com.zfsoft.ha.data.vo.VideoConsultant;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description //视频咨询类
 * @Author: Wangyh
 * @Date: 2023/1/9 14:08
 */
@RequestMapping(value = "/ha/video")
public interface HaVideoService {
    /**
     * @description:  根据id查询员工表信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getById",method = {RequestMethod.GET})
    ApiResultSet getById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  查询视频用户分页信息列表
     * @param videoConsultant 视频咨询人员表请求实体类
     * @param pageNum 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2023/1/6
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryVideoPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<VideoConsultant>> queryVideoPage(
            VideoConsultant videoConsultant,
            @RequestParam(value = "pageNumber", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;
}

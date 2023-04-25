package com.zfsoft.ha.front.controller;


import com.zfsoft.ha.data.vo.VideoConsultant;
import com.zfsoft.ha.front.HaVideoService;
import com.zfsoft.ha.managers.HaVideoConsultantManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * @Description //视频咨询用户控制层
 * @Author: Wangyh
 * @Date: 2023/1/6 14:12
 */
@RestController
@Slf4j
public class HaVideoController implements HaVideoService {

    @Resource
    private HaVideoConsultantManager haVideoConsultantManager;


    /**
     * @description:  根据id查询员工表信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet getById(Long id) throws Exception {
        log.info("根据id查询videoConsultant表信息，id:{}", id);
        VideoConsultant videoConsultant = haVideoConsultantManager.selectByid(id);
        if(videoConsultant==null){
            return new ApiResultSet<>(500, "根据id查询videoConsultant表信息失败");
        }
        return ApiResultSet.ok("接口调用成功", videoConsultant);
    }
    /**
     * @description:  查询视频用户分页信息列表
     * @param videoConsultant 视频咨询人员表请求实体类
     * @param pageNumber 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2023/1/6
     **/
    @Override
    public ApiResultSet<PageResult<VideoConsultant>> queryVideoPage(VideoConsultant videoConsultant, Integer pageNumber, Integer pageSize) throws Exception {
        log.info("查询视频用户分页信息列表，videoConsultant:{},pageNumber:{},pageSize:{}",videoConsultant,pageNumber,pageSize);
        PageResult<VideoConsultant> pageResult = haVideoConsultantManager.queryVideoPage(videoConsultant,pageNumber,pageSize);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<VideoConsultant>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
}

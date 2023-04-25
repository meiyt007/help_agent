package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.vo.VideoConsultant;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description //视频咨询接口
 * @Author: Wangyh
 * @Date: 2023/1/6 14:05
 */
@RequestMapping("/work/video")
public interface HaVideoConsultantService {
    /**
     * @description:  重置密码
     * @param id 用户主键
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/ResetPassword",method = {RequestMethod.GET})
    ApiResultSet resetPassword(@RequestParam("id") Long id) throws Exception;
    /**
     * @param file        文件
     * @description: 上传图片
     * @author: wangyh
     * @Date: 2022/7/15 14:08
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/uploadImage",  method = {RequestMethod.POST},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet uploadImage(HttpServletRequest request,
                             @RequestPart(value = "file") MultipartFile file) ;

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

    /**
     * @description:  删除用户信息
     * @param id 账号
     * @author: wangyh
     * @Date: 2023/1/6
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteByOid", method = {RequestMethod.GET})
    ApiResultSet deleteByOid(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  参数配置的新增或者修改
     * @param videoConsultant 参数配置实体类
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet saveVideoConsultant(@RequestBody VideoConsultant videoConsultant) throws Exception;

    /**
     * @description:  根据id查询员工表信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getById",method = {RequestMethod.GET})
    ApiResultSet getById(@RequestParam("id") Long id) throws Exception;
}

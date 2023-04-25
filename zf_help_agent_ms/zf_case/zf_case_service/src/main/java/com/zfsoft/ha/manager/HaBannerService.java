package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaBanner;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description //banner接口层
 * @Author: Wangyh
 * @Date: 2022/7/26 13:56
 */
@RequestMapping("/work/banner")
public interface HaBannerService {
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
     * @description:  查询banner分页信息列表
     * @param title 标题
     * @param content 内容
     * @param ableStatus 启禁用状态
     * @param deleteStatus 删除状态
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/BannerServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaBanner>> queryBannerServicePage(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "ableStatus", required = false) String ableStatus,
            @RequestParam(value = "deleteStatus", required = false) String deleteStatus,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * @description:  删除banner信息
     * @param id 主键
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteBannerid", method = {RequestMethod.GET})
    ApiResultSet<HaBanner> deleteBannerid(@RequestParam("id") Long id);

    /**
     * @description:  批量删除banner信息
     * @param ids 主键
     * @author: wangyh
     * @Date: 2022年7月26日14:43:36
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteBannerids", method = {RequestMethod.GET})
    ApiResultSet deleteBannerids(@RequestParam(value = "ids") List<Long> ids) throws Exception;

    /**
     * @description:  参数配置的新增或者修改
     * @param haBanner 参数配置实体类
     * @author: wangyh
     * @Date: 2022年7月26日14:43:41
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdata",method = {RequestMethod.POST})
    ApiResultSet saveThaBannerorUpdata(@RequestBody HaBanner haBanner) throws Exception;

    /**
     * @description:  根据id查询banner表信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getThaBannerById",method = {RequestMethod.GET})
    ApiResultSet getHaUserResourceById(@RequestParam("id") Long id) throws Exception;
}

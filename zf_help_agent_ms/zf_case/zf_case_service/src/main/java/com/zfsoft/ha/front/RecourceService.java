package com.zfsoft.ha.front;

import com.zfsoft.ha.data.requestData.HaUserResourceRequestData;
import com.zfsoft.ha.data.responseData.HaUserResourceResponseData;
import com.zfsoft.ha.data.vo.HaUserResourceVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 资源管理
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/15
 */
@RequestMapping("/ha/resource")
public interface RecourceService {

    /**
     * 模糊查询，获取资源列表
     * @param ha 用户资源信息
     * @param ha parentId 父级id 当父级为空时，只获取顶级的资源，包含：文件和文件夹
     * @param ha name 资源名称
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/listResource",method = {RequestMethod.GET})
    ApiResultSet<List<HaUserResourceResponseData>> listResource(HaUserResourceRequestData ha)  ;

    /**
     * 根据id获取资源信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getResourceInfo",method = {RequestMethod.GET})
    ApiResultSet<HaUserResourceResponseData> getResourceInfo(
            @RequestParam(value = "id", required = true) String id)  ;

    /**
     * 上传文件
     * @param request
     * @param file 文件
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/uploadFile",method = {RequestMethod.POST}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers = "content-type=multipart/form-data")
    ApiResultSet uploadFile(HttpServletRequest request,@RequestParam(value = "file", required = true) MultipartFile file)  ;

    /**
     * 保存资源文件
     * @param haUserResourceVo 用户资源信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveResourceInfo",method = {RequestMethod.POST})
    ApiResultSet saveResourceInfo(HaUserResourceVo haUserResourceVo);

    /**
     * 删除资源文件
     * @param ids 已逗号隔开
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteResource", method = {RequestMethod.GET})
    ApiResultSet deleteResource(@RequestParam("ids") String ids);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/shareResource", method = {RequestMethod.GET})
    ApiResultSet shareResource(String id,String workUserIds);
}

package com.zfsoft.ha.front;

import cn.hutool.json.JSONObject;
import com.zfsoft.ha.data.vo.HaFileKnowledgeBase;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description //文件知识库管理
 * @Author: Wangyh
 * @Date: 2022/12/2 16:26
 */
@RequestMapping("/ha/knowledge")
public interface FHaKnowledgeBaseService {

    /**
     * @description:  查询文件知识库分页信息集合
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryKnowledgeServiceList", method = {RequestMethod.GET})
    ApiResultSet<List<HaFileKnowledgeBase>> queryKnowledgeServiceList();
    /**
     * @description:  查询文件知识库分页信息列表
     * @param fileName 文件名称
     * @param serviceOid 所属事项
     * @param isDelete 删除状态
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/KnowledgeServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaFileKnowledgeBase>> queryKnowledgeServicePage(
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "serviceOid", required = false) String serviceOid,
            @RequestParam(value = "isDelete", required = false) String isDelete,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * @description:  参数配置的新增或者修改
     * @param haFileKnowledgeBase 参数配置实体类
     * @author: wangyh
     * @Date: 2022年7月26日14:43:41
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdata",method = {RequestMethod.POST})
    ApiResultSet saveknowledgeorUpdata(@RequestBody HaFileKnowledgeBase haFileKnowledgeBase) throws Exception;

    /**
     * @description:  删除haFileKnowledgeBase信息
     * @param id 主键
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteKnowledgeId", method = {RequestMethod.GET})
    ApiResultSet<HaFileKnowledgeBase> deleteKnowledgeId(@RequestParam("id") Long id);

    /**
     * @description:  根据id查询haFileKnowledgeBase表信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getZskBaseById",method = {RequestMethod.GET})
    ApiResultSet getZskBaseById(@RequestParam("id") Long id) throws Exception;
}

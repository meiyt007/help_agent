package com.zfsoft.ha.manager;

import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.responseData.HaUserResourceResponseData;
import com.zfsoft.ha.data.vo.HaUserResourceVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.platform.utils.web.TreeSelect;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/help/userManager/userResource")
public interface HaUserResourceService {

    /**
     * 保存用户资源
     *
     * @author zhaobf
     * @date 20220715
     * @param HaUserResource
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveHaUserResource",method = {RequestMethod.POST})
    ApiResultSet<HaUserResource> saveHaUserResource(@RequestBody HaUserResource HaUserResource);

    /**
     *用户资源列表
     *
     * @author zhaobf
     * @date 20220715
     * @param name
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHaUserResourceByName", method = {RequestMethod.GET})
    ApiResultSet<PageResult<HaUserResource>> queryHaUserResourceByName(@RequestParam("name") String name, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10")  Integer pageSize);

    /**
     * 根据资源名，工作人员名称获取用户资源管理，分页
     * @param name 资源名
     * @param workName 工作人员名称
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHaUserResourceByNameAndWorkUserName", method = {RequestMethod.GET})
    ApiResultSet<PageResult<HaUserResourceVo>> queryHaUserResourceByNameAndWorkUserName(@RequestParam("name") String name, @RequestParam("workName") String workName, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10")  Integer pageSize);

    /**
     * 保存用户资源
     * @param haUserResource 用户资源信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<HaUserResource> save(@RequestBody HaUserResource haUserResource);

    /**
     * 删除用户资源
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET})
    ApiResultSet<HaUserResource> deleteById(@RequestParam("id") String id);

    /**
     * 根据用户资源id获取资源信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getHaUserResourceById",method = {RequestMethod.GET})
    ApiResultSet<HaUserResourceVo> getHaUserResourceById(@RequestParam("id") String id);

    /**
     * 获取资源信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryHaUserResourceList",method = {RequestMethod.GET})
    ApiResultSet<List<HaUserResourceResponseData>> queryHaUserResourceList();

    /**
     * 上传文件
     * @author zhaobf
     * @param request
     * @param file 文件
     * @date 2022/08/03 13:30
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet<QlSysAtta> uploadFile(HttpServletRequest request, MultipartFile file);
    /**
     * 根据主键和类型获取文件夹下级文件列表。文件等数据
     * @author zhaobf
     * @param id 主键
     * @param type 类型
     * @date 2022/08/03 13:30
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryHaUserResourceDataByIdAndType",method = {RequestMethod.GET})
    ApiResultSet<Map<String,Object>> queryHaUserResourceDataByIdAndType(@RequestParam("id") String id,@RequestParam("type") String type);
    /**
     *
     * 下载
     * @author zhaobf
     * @param attaOid 文件oid
     * @date 2022/08/03 13:30
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/downloadAtta", method = {RequestMethod.GET})
    void downloadAtta(@RequestParam("attaOid") String attaOid);
    /**
     *
     * 获取资源树形结构
     * @author zhaobf
     * @date 2022/08/03 13:30
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllResourceSimpleTree", method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryAllResourceSimpleTree() ;

}

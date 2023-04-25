package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.InterApi;
import com.zfsoft.superwindow.data.clzs.InterApiResponse;
import com.zfsoft.superwindow.data.feign.Server;
import com.zfsoft.superwindow.data.feign.ServerInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 接口定义
 */
@RequestMapping("/interApi")
public interface InterApiService {
    /***
    * @Description:  获取分页列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [interApi]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryInfoWithPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryInfoWithPage(@RequestBody InterApi interApi);

    /**
     * 获取所有的内部接口实体
     * @Author:WangKe
     * @Date:2022/05/19
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllInterApi", method = {RequestMethod.POST})
    ApiResultSet<List<InterApi>> queryAllInterApi();

    /**
     * 获取所有的内部接口实体的返回值
     * @Author:WangKe
     * @Date:2022/05/19
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryInterApiValById", method = {RequestMethod.POST})
    ApiResultSet<List<InterApiResponse>> queryInterApiValById(Long id);

    /**
     * 获取所有的内部接口实体的返回值
     * @Author:WangKe
     * @Date:2022/05/19
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getInterApiReo", method = {RequestMethod.POST})
    ApiResultSet<InterApiResponse> getInterApiReo(@RequestBody Long id);

    /**
     * 获取所有的内部接口实体的返回值
     * @Author:WangKe
     * @Date:2022/05/19
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getInterApiReponse", method = {RequestMethod.POST})
    ApiResultSet<InterApiResponse> getInterApiReponse(Long id);


    /**
     * 通过id获取interApi接口
     * @Author:WangKe
     * @Date:2022/05/19
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getInterApi", method = {RequestMethod.POST})
    ApiResultSet<InterApi> getInterApi(@RequestBody Long id);

   /***
   * @Description:  更新或保存证照目录
   * @Author:liangss
   * @Date:2021/11/5
   * @Param: [interApi]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
   ApiResultSet saveOrUpdate(@RequestBody InterApi interApi) throws Exception;

    /***
    * @Description:根据oid查询证照目录信息
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [oid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getInfoByOid", method = {RequestMethod.GET})
    ApiResultSet<InterApi> getInfoByOid(@RequestParam(value = "id", required = false) Long id);

    /***
    * @Description: 根据oid删除卡证目录信息
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [oid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteByOid", method = {RequestMethod.GET})
    ApiResultSet deleteByOid(@RequestParam(value = "id", required = false) Long id);

    /***
     * @Description: 根据oid删除卡证目录信息
     * @Author:liangss
     * @Date:2021/11/5
     * @Param: [oid]
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkHasRepeat", method = {RequestMethod.GET})
    ApiResultSet checkHasRepeat(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "code", required = false) String code);

    /**
     *  查询数据协同平台服务
     * @return
     */
    @GetMapping("/queryServerList")
    ApiResultSet<List<Server>>  queryServerList();

    /**
     * 查询数据协同平台服务接口
     * @param serverOid
     * @return
     */
    @GetMapping("/queryServerInterfaceList")
    public ApiResultSet<List<ServerInterface>> queryForeignServerInterfaceList(@RequestParam(required = true, value = "serverOid") String serverOid);

}


package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.requestData.HaWorkUserRequestData;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.data.vo.HaWorkTurnRecordVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description //用户务定义接口
 * @Author: Wangyh
 * @Date: 2022/7/15 13:06
 */
@RequestMapping("/work/user")
public interface HaWorkUserService {
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
     * @description:  查询用户分页信息列表
     * @param haWorkUserRequestData 帮代办工作人员表请求实体类
     * @param pageNum 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/UserServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaWorkUser>> queryUserServiceWithPage(
            HaWorkUserRequestData haWorkUserRequestData,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;


    /**
     * @description:  删除用户信息
     * @param id 账号
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteUserOid", method = {RequestMethod.GET})
    ApiResultSet deleteUserOid(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  批量删除用户信息
     * @param ids 主键
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteUserListid", method = {RequestMethod.GET})
    ApiResultSet deleteUsertids(@RequestParam(value = "ids")List<Long> ids) throws Exception;


    /**
     * @description:  参数配置的新增或者修改
     * @param haWorkUser 参数配置实体类
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet saveThaWorkUser(@RequestBody HaWorkUser haWorkUser) throws Exception;

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
     * @description:  根据id查询员工表信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getHaUserById",method = {RequestMethod.GET})
    ApiResultSet getHaUserById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description: 查询所选帮代办人员的所有的办事队列数据
     * @param haWorkQueueVo 办事队列请求参数
     * @author: dingsn
     * @date: 2023-01-10 11:11
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllWorkQueueListByWorkUserId", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryAllWorkQueueListByWorkUserId(HaWorkQueueVo haWorkQueueVo,
                                                          @RequestParam(value = "beginTime", required = false) String beginTime,
                                                          @RequestParam(value = "endTime", required = false) String endTime,
                                                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;
    /**
     * 查询所选帮代办人员的所有的转派服务记录
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllTurnServiceListByWorkUserId", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryAllTurnServiceListByWorkUserId(HaWorkTurnRecordVo haWorkTurnRecordVo,
                                            @RequestParam(value = "beginTime", required = false) String beginTime,
                                            @RequestParam(value = "endTime", required = false) String endTime,
                                            Integer pageNum,
                                            Integer pageSize) throws Exception;

}

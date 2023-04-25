package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.PbpjUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName PbpjUserService
 * @Description 平板人员组件服务定义接口
 * @Author liangxm
 * @Date 2020-10-24 11:33
 * @Version V1.0
 **/
@RequestMapping("/pbpj/user")
public interface PbpjUserService {

    /**
     * @description:  平板人员的查看列表
     * @param  user 人员
     * @author: liangxm
     * @Date: 2020/10/24 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryPbpjManageWithPage(SysUser user, Integer pageNum, Integer pageSize);
    /**
     * @description:  平板人员的新增或者修改
     * @param id 平板人员实体类
     * @author: liangxm
     * @Date: 2020/10/24 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<SysUser> getOne(String id);

    /**
     * @description:  平板人员的是否确认
     * @param id 平板人员实体类
     * @author: liangxm
     * @Date: 2020/10/28 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/confirm/{id}",method = {RequestMethod.POST})
    int confirmPbpjUser(String id);

    /**
     * @description:  平板人员的是否启用
     * @param id 平板人员实体类
     * @author: liangxm
     * @Date: 2020/10/28 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/appraise/{id}",method = {RequestMethod.POST})
    int appraisePbpjUser(String id);


    /**
     * 平板评价授权用户信息
     * @author  wangwg
     * @date  2020-12-06
     * @param userOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getPbpjUserByUserOid",method = {RequestMethod.GET})
    ApiResultSet<PbpjUser> getPbpjUserByUserOid(@RequestParam(value = "userOid", required = false) String userOid);


    /*判断当前登录用户是否启用评价
     * @Description:
     * @Author: wangxl
     * @Date: 2020/12/9 11:48

     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.yxpz.PbpjUser>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/ifPbpjUser",method = {RequestMethod.GET})
    ApiResultSet<Boolean> ifPbpjUser();
}

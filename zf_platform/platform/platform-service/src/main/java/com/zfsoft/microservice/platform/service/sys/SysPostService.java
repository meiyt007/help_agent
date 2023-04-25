package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysPost;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName SysPostService
 * @Description 岗位组件服务定义接口
 * @Author wuxx
 * @Date 2021-01-21 16:33
 * @Version V1.0
 **/
@RequestMapping("/security/post")
public interface SysPostService {
    /**
     * 增加一个新岗位
     *
     * @param sysPost 新岗位
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<SysPost> saveSysPost(@RequestBody SysPost sysPost);

    /**
     * 删除指定Id的岗位信息
     *
     * @param oid 岗位id
     * @return
     */

    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteSysPostById(@PathVariable("oid") Long oid);

    /**
     * 启禁用指定Id的岗位信息
     *
     * @param oid 岗位id
     * @return
     */
    @RequestMapping(value = "/able/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> ableSysPostById(@PathVariable("oid") Long oid);

    /**
     * 根据岗位Id获取岗位信息
     *
     * @param oid 岗位Id
     * @return
     */
    @RequestMapping(value = "/getOne/{oid}", method = {RequestMethod.GET})
    ApiResultSet<SysPost> getSysPostById(@PathVariable("oid") Long oid);

    /**
     * 查询岗位信息
     *
     * @param name     岗位
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet querySysPostWithPage(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "districtOid", required = false) String districtOid,
                                      @RequestParam(value = "organOid", required = false) String organOid,
                                     @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize);

   /**
    * @description: 根据区划oid和组织机构oid查询岗位列表
    * @param districtOid 区划oid
    * @param organOid 组织机构oid
    * @author: wuxx
    * @Date: 2021/1/21 16:20
    **/
    @GetMapping("/list")
    ApiResultSet<List<SysPost>> querySysPostList(@RequestParam(value = "districtOid", required = false) String districtOid,
                                        @RequestParam(value = "organOid", required = false) String organOid);
}

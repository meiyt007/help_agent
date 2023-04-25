package com.zfsoft.service.sxSys.service;

import cn.hutool.json.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName SxSysAttaService
 * @Description 事项附件接口
 * @Author wangxl
 * @Date 2020-10-22
 * @Version V1.0
 **/
@RequestMapping("/sxSys/atta")
public interface SxSysAttaService {

    /**
     * @description:  初始化事项附件信息
     * @param oid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/22
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSxSysAtta(@RequestParam(value = "oid", required = false) String oid);

    /**
     * @description:  获取事项附件的信息
     * @param oid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/22
     **/
    @ProcessFeignCalledResult
    @RequestMapping(
            value = {"/getOne/{oid}"},
            method = {RequestMethod.GET}
    )
    ApiResultSet<SxSysAtta> getSxSysAttaByOId(@PathVariable("oid") String oid);

    /**
     * 附件下载
     * @param attaOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/download",method = {RequestMethod.GET})
    void download(@RequestParam(value = "attaOid", required = false) String attaOid);

    @ProcessFeignCalledResult
    @PostMapping(value = "/uploadFile")
    ApiResultSet<JSONObject> uploadFile(HttpServletRequest request, @RequestParam(value = "name", required = false) String name
            , @RequestParam("file") MultipartFile file);

    /**
     * fastds的下载
     * @param attaOid
     */
    @RequestMapping(value = "/downloadFile",method = {RequestMethod.GET})
    @ResponseBody
    void downloadFileAtta(@RequestParam(value = "attaOid", required = false) String attaOid);

    /***
    * @Description: 保存附件信息
    * @Author:liangss
    * @Date:2021/8/26
    * @Param: [sysAtta]
    */
    @RequestMapping(
            value = {"/save"},
            method = {RequestMethod.POST}
    )
    ApiResultSet<SxSysAtta> saveSysAtta(@RequestBody SxSysAtta sysAtta);

}

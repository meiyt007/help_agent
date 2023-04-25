package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName SysAttaService
 * @Description 附件组件服务定义接口
 * @Author wuxx
 * @Date 2020-09-12 11:33
 * @Version V1.0
 **/
@RequestMapping("/security/atta")
public interface SysAttaService {
    /**
     * 增加一个新附件
     *
     * @param sysAtta 新附件
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<SysAtta> saveSysAtta(@RequestBody SysAtta sysAtta);

    /**
     * @param attaOid 附件实体类业务主键
     * @description: 获取附件的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = "/getSysAttaByAttaOid/{attaOid}", method = {RequestMethod.GET})
    ApiResultSet<SysAtta> getSysAttaByAttaOid(@PathVariable("attaOid") String attaOid);

    /**
     * @param userOid 用户oid
     * @description: 根据用户oid获取附件的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = "/getSysAttaListByUserOid/{userOid}", method = {RequestMethod.GET})
    ApiResultSet<List<SysAtta>> getSysAttaListByUserOid(@PathVariable("userOid") String userOid);

    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOidList id主键集合
     * @author: wuxx
     * @Date: 2020/10/28 17:28
     **/
    @RequestMapping( value = "/getAttaListByOidList",method = {RequestMethod.POST})
    ApiResultSet getAttaListByOidList(@RequestBody List<String> attaOidList);

    /**
     * @description:  上传文件-通过feign的方式
     * @param name 上传文件名称
     * @param file 文件
     * @author: wuxx
     * @Date: 2020/12/7 18:33
     **/
    @PostMapping(value = "/uploadFeignFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet uploadFeignFile(@RequestParam(value="file",required=false) MultipartFile file, @RequestParam(value="name",required=false) String name);

   /**
    * @description:  根据业务主键获取base64
    * @param attaOid 业务主键
    * @author: wuxx
    * @Date: 2020/12/15 9:37
    **/
    @RequestMapping(value = "/getSysAttaBase64ByAttaOid/{attaOid}", method = {RequestMethod.GET})
    ApiResultSet<String> getSysAttaBase64ByAttaOid(@PathVariable("attaOid") String attaOid);

}

package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @（#）: SysAttaService
 * @description: 上传附件信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/sysAttaService")
public interface SysAttaService {

    /**
     * 保存上传附件信息
     *
     * @author wangwg
     * @date 2020/10/24
     * @param sysAtta
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.SysAtta>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSysAtta",method = {RequestMethod.POST})
    ApiResultSet<QlSysAtta> saveSysAtta(@RequestBody QlSysAtta sysAtta);


    /**
     * 附件业务主键查看附件信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param attaOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.SysAtta>
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySysAttaByOid", method = {RequestMethod.GET})
    ApiResultSet<QlSysAtta> querySysAttaByOid(@RequestParam("attaOid") String attaOid);


    /**
     *
     * 根据材料业务主键获取上传的附件信息
     * @author wangwg
     * @date 2020/12/01
     * @param materialOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySysAttaListByMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String,Object>> querySysAttaListByMaterialOid(@RequestParam("materialOid") String materialOid, @RequestParam("caseOid") String caseOid);


    /**
     *
     * 预览
     * @author wangwg
     * @date 2020/12/03
     * @param attaOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/preview", method = {RequestMethod.GET})
    ApiResultSet<String> preview(@RequestParam("attaOid") String attaOid) throws IOException;

    /**
     *
     * 下载
     * @author wangwg
     * @date 2020/12/03
     * @param attaOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    ApiResultSet download(@RequestParam("attaOid") String attaOid) throws IOException;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/downloadAtta", method = {RequestMethod.GET})
    void downloadAtta(@RequestParam("attaOid") String attaOid);

    }

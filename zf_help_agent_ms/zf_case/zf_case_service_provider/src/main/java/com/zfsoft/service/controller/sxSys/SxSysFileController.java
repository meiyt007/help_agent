package com.zfsoft.service.controller.sxSys;


import com.zfsoft.platform.utils.fileUtil.DownloadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author: wangxl
 * @create: 2020-11-17
 * @description: 事项附件控制层
 */
@RestController
@Slf4j
@RequestMapping("/sxSys/file")
public class SxSysFileController {
    @Resource
    private SxSysAttaManager sxSysAttaManager;

    /**
     * @description:  文件下载
     * @param attaOid 文件的oid
     * @author: wangxl
     * @Date: 2020/11/20
     **/
    @RequestMapping(value = {"/downloadFile/{attaOid}"},method = {RequestMethod.GET})
    @ResponseBody
    public void downloadFile(HttpServletResponse response, HttpServletRequest request, @PathVariable("attaOid") String attaOid) {
        try {
            SxSysAtta atta = sxSysAttaManager.getSxSysAttaByOid(attaOid);
            if(null!=atta){
                DownloadUtil.downloadFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), null, request, response);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("文件下载失败！",e);
        }
    }
}

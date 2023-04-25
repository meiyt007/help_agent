package com.zfsoft.cases.controller;


import com.zfsoft.cases.config.CaseServiceConfig;
import com.zfsoft.cases.config.IpConfiguration;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.QlCaseMaterialAttaManager;
import com.zfsoft.cases.manager.QlCaseMaterialManager;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.fileUtil.DownloadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @（#）: SysAttaController
 * @description: 上传附件信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SysAttaController implements SysAttaService {

    @Resource
    private IpConfiguration ipConfiguration;

    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private QlCaseMaterialManager qlCaseMaterialManager;

    @Resource
    private QlCaseMaterialAttaManager qlCaseMaterialAttaManager;

    @Override
    public ApiResultSet<QlSysAtta> saveSysAtta(QlSysAtta sysAtta) {
        if(StringUtil.isEmpty(sysAtta.getExtensionName())){
            sysAtta.setExtensionName(sysAtta.getFilePath().substring(sysAtta.getFilePath().lastIndexOf(".")+1,sysAtta.getFilePath().length()));
        }
        if(StringUtil.isEmpty(sysAtta.getUserOid())){
            if (CurrentLoginUserHolder.getCurrentLoginUser() !=null && StringUtils.isNotEmpty(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid())) {
                sysAtta.setUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
            }
        }
        QlSysAtta atta=sysAttaManager.saveSysAtta(sysAtta);
        ApiResultSet<QlSysAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(atta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlSysAtta> querySysAttaByOid(String attaOid) {
        QlSysAtta atta =  sysAttaManager.querySysAttaByOid(attaOid);
        ApiResultSet<QlSysAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(atta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String,Object>> querySysAttaListByMaterialOid(String materialOid,String caseOid) {
        Map<String,Object> map = new HashMap<String,Object>();
        List<QlSysAtta> list =new ArrayList<QlSysAtta>();
        List<QlCaseMaterial> qlCaseMaterialList = qlCaseMaterialManager.queryMaterialListByMaterialOid(materialOid,caseOid);
        if(qlCaseMaterialList !=null){
            for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
                List<QlCaseMaterialAtta> materialAttas = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                if(materialAttas !=null){
                    for (QlCaseMaterialAtta materialAtta : materialAttas) {
                        QlSysAtta atta =sysAttaManager.querySysAttaByOid(materialAtta.getAttaOid());
                         list.add(atta);
                    }
                }
            }
        }
        map.put("attaList",list);
        ApiResultSet<Map<String,Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }


    @Override
    public ApiResultSet<String> preview(String attaOid) throws IOException {
        QlSysAtta atta = sysAttaManager.querySysAttaByOid(attaOid);
        String realpath = atta.getFastdfsNginxUrl();
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(realpath);
        return apiResultSet;
    }

    @Override
    public ApiResultSet download(String attaOid) throws IOException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        // 上传文件路径
        String filePath = CaseServiceConfig.getDowloadPath();
        //获取文件信息
        QlSysAtta atta = sysAttaManager.querySysAttaByOid(attaOid);
        response.reset();
        String realpath = atta.getFastdfsNginxUrl();
        File file = new File(realpath);
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
        response.setHeader("content-disposition",  "attachment;fileName=" + URLEncoder.encode(atta.getOriginName(), "UTF-8"));
        response.setContentType("application/octet-stream;charset=UTF-8");
        outputStream.flush();
        return null;
    }

    @Override
    public void downloadAtta(String attaOid){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //获取文件信息
        QlSysAtta atta = sysAttaManager.querySysAttaByOid(attaOid);
        try {
            DownloadUtil.downloadFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), atta.getFastdfsNginxUrl(), request, response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

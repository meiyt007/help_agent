package com.zfsoft.service.controller.sxSys;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.feign.ZzqFastDfsFeignService;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.util.DownloadUtil;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.service.sxSys.service.SxSysAttaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 事项附件控制类
 * @ClassName SxSysAttaController
 * @Description 参数配置管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxSysAttaController implements SxSysAttaService{
    @Resource
    private SxSysAttaManager sxSysAttaManager;

    @Resource
    private ZzqFastDfsFeignService zzqFastDfsFeignService;

    /**
     * @description:  初始化事项附件的信息
     * @param oid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/22
     **/
    @Override
    public ApiResultSet initSxSysAtta(String oid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=oid){
            SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(oid);
            resultMap.put("sxSysAtta",sxSysAtta);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    /**
     * @description:  获取事项附件的信息
     * @param oid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/22
     **/
    @Override
    public ApiResultSet<SxSysAtta> getSxSysAttaByOId(String oid) {
        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(oid);
        ApiResultSet<SxSysAtta> apiResultSet = new ApiResultSet<SxSysAtta>();
        apiResultSet.setData(sxSysAtta);
        return apiResultSet;
    }

    @Override
    public void download(String attaOid) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(attaOid);
        if(null == sxSysAtta){
            throw new ResultInfoException("附件不存在");
        }
        try {
            DownloadUtil.downloadFile(sxSysAtta.getOriginName(), sxSysAtta.getFilePath(),
                    sxSysAtta.getName(), request, response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public ApiResultSet<JSONObject> uploadFile(HttpServletRequest request, String name, MultipartFile file) {
        try {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            if(StrUtil.isNotEmpty(name)){
                file = new MockMultipartFile("File",name,"text/plain", file.getInputStream());
            }
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, userOid);
            SxSysAtta atta=new SxSysAtta();
            atta.setOriginName(sysAttaFile.getOriginName());
            atta.setExtensionName(sysAttaFile.getExtensionName());
            atta.setName(sysAttaFile.getName());
            atta.setUserOid(sysAttaFile.getUserOid());
            //atta.setFilePath(sysAttaFile.getFastdfsUploadUrl());
            atta.setFilePath(sysAttaFile.getFastdfsNginxUrl());
            atta.setOid(sysAttaFile.getAttaOid());
            //BeanUtils.copyProperties(sysAttaFile,atta);
            //保存附件信息
            SxSysAtta attaRes=sxSysAttaManager.saveAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", attaRes.getOid()).set("name",attaRes.getOriginName()).set("attaUrl",attaRes.getFilePath());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }

    @Override
    public void downloadFileAtta(String attaOid){
        try {
            if(StringUtils.isEmpty(attaOid)){
                //
                throw new ResultInfoException("附件主键不能为空！");
            }
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            if(attaOid.contains(",")){
                String[] attaList = attaOid.split(",");
                for(String newAttaOid : attaList){
                    if(StringUtil.isNotEmpty(newAttaOid)){
                        getFileIsDown(newAttaOid, response);
                    }
                }
            }else{
                getFileIsDown(attaOid, response);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("文件下载失败！",e);
        }
    }

    private void getFileIsDown(String attaOid, HttpServletResponse response) throws IOException {
        OutputStream os;
        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(attaOid);
        if(null != sxSysAtta && !StringUtils.isEmpty(sxSysAtta.getOriginName())){
            byte[] fileIs = zzqFastDfsFeignService.getFileIs(sxSysAtta.getOid(), sxSysAtta.getOriginName());
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(sxSysAtta.getOriginName(),"UTF-8"));
            response.setContentType("application/force-download");
            os = response.getOutputStream();
            os.write(fileIs);
            os.flush();
            os.close();
        }
    }

    @Override
    public ApiResultSet<SxSysAtta> saveSysAtta(SxSysAtta sysAtta) {
        SxSysAtta attaRes=sxSysAttaManager.saveAtta(sysAtta);
        return new ApiResultSet<>(attaRes);
    }


    /* *//**
     * 文件下载
     *//*
    @RequestMapping("/downloadFile.do")
    public String download(String attaOid, HttpServletRequest request,
                           HttpServletResponse response) {
        try {
            SysAtta atta = sysAttaService.getSysAttaByOid(attaOid);
            if (atta == null) {
                return null;
            }

            DownloadUtil.downloadFile(atta.getOriginName(), atta.getFilePath(),
                    atta.getName(), request, response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回值要注意，要不然就出现下面这句错误！
        // java+getOutputStream() has already been called for this response
        return null;
    }*/
}

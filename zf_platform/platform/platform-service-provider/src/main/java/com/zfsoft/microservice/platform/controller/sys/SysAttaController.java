package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.microservice.platform.manager.sys.SysAttaManager;
import com.zfsoft.microservice.platform.service.sys.SysAttaService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName SysAttaController
 * @Description 附件管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysAttaController implements SysAttaService{

    @Resource
    private SysAttaManager sysAttaManager;

    /**
     * @param sysAtta 附件实体类
     * @description: 附件的新增或者修改
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    public ApiResultSet<SysAtta> saveSysAtta(SysAtta sysAtta) {
        sysAttaManager.saveSysAtta(sysAtta);
        ApiResultSet<SysAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysAtta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SysAtta> getSysAttaByAttaOid(String attaOid) {
        if(StrUtil.isEmpty(attaOid)){
            return  new ApiResultSet<>();
        }
        SysAtta atta = sysAttaManager.getSysAttaByAttaOid(attaOid);
        return new ApiResultSet<>(atta);
    }

    @Override
    public ApiResultSet<List<SysAtta>> getSysAttaListByUserOid(String userOid) {
        List<SysAtta> sysAttaList = sysAttaManager.getSysAttaListByUserOid(userOid);
        return new ApiResultSet<>(sysAttaList);
    }

    /**
     * @param oid 附件实体类主键
     * @description: 附件的信息的删除-只删除数据，不删除文件
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.DELETE})
    public ApiResultSet<Integer> deleteSysAttaById(@PathVariable("oid") Long oid) {
        int rows = sysAttaManager.deleteSysAttaById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @param oid 附件实体类主键
     * @description: 获取附件的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = "/getOne/{oid}", method = {RequestMethod.GET})
    public ApiResultSet<SysAtta> getSysAttaById(@PathVariable("oid") Long oid) {
        SysAtta sysAtta = sysAttaManager.getSysAttaById(oid);
        ApiResultSet<SysAtta> apiResultSet = new ApiResultSet<SysAtta>();
        apiResultSet.setData(sysAtta);
        return apiResultSet;
    }
    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOidList id主键集合
     * @author: wuxx
     * @Date: 2020/10/28 17:28
     **/
    @Override
    public ApiResultSet getAttaListByOidList(List<String> attaOidList) {
        List<SysAtta> sysAttaList = sysAttaManager.getAttaListByOidList(attaOidList);
        return new ApiResultSet(sysAttaList);
    }

    /**
     * @param sysAtta 附件实体类
     * @description: 查询附件的信息列表
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    public ApiResultSet<PageResult<SysAtta>> querySysAttaWithPage(SysAtta sysAtta, Integer pageNum,
                                                                 Integer pageSize) {
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        sysAtta.setUserOid(userOid);
        PageResult<SysAtta> pageResult = sysAttaManager.querySysAttaWithPage(sysAtta, pageNum, pageSize);
        ApiResultSet<PageResult<SysAtta>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  上传文件-通过feign的方式
     * @param name 上传文件名称
     * @param file 文件
     * @author: wuxx
     * @Date: 2020/12/7 18:33
     **/
    @PostMapping(value = "/uploadFeignFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadFeignFile(MultipartFile file, String name){
        try {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            if(StrUtil.isNotEmpty(name)){
                file = new MockMultipartFile("File",name,"text/plain", file.getInputStream());
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            sysAttaManager.saveSysAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }

    @Override
    public ApiResultSet<String> getSysAttaBase64ByAttaOid(String attaOid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String base64ByAttaOid = sysAttaManager.getSysAttaBase64ByAttaOid(attaOid, request);
        if(StrUtil.isEmpty(base64ByAttaOid)){
            throw new ResultInfoException("文件的Base64为空！");
        }
        return new ApiResultSet<>(base64ByAttaOid);
    }
}

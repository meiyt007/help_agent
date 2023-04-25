package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormAtta;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormAttaMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormAtta;
import com.zfsoft.microservice.form.dbaccess.data.DbFormAttaExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.DownloadUtil;
import com.zfsoft.platform.utils.fileUtil.FileUtil;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FormAttaManager
 * @Description: 附件接口实现类
 * @Author wuxx
 * @Date 2021/5/26
 **/
@Service
@Slf4j
@RefreshScope
@CacheConfig(cacheNames = "form:formAtta")
public class FormAttaManager {
    @Resource
    private DbFormAttaMapper dbFormAttaMapper;

    @Value("${fdfs.fastDFSNginxUrl:''}")
    private String fastDFSNginxUrl;

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public FormAtta saveFormAtta(@ValidGroups(groups = {FormAtta.INSERT_GROUP.class}) FormAtta formAtta) {
        if (formAtta == null) {
            throw new ResultInfoException("附件信息不正确!");
        }
        if (null == formAtta.getId()) {
            formAtta.setId(null);
            formAtta.setAttaOid(IdUtil.simpleUUID());
        } else {
            // 附件oid不为空
            DbFormAtta atta = dbFormAttaMapper.selectByPrimaryKey(formAtta.getId());
            if (atta == null) {
                throw new ResultInfoException("附件编号未查询到相应的附件信息!");
            }
        }
        if (null==formAtta.getUploadDate()){
            formAtta.setUploadDate(new Date());
        }

        // 设置附件信息的状态
        if (null!=formAtta.getIsDelete()) {
            formAtta.setIsDelete(BaseStaticParameter.N);
        }
        if(StrUtil.isNotEmpty(formAtta.getFileSize())){
            try {
                formAtta.setFileSize(FileUtil.formatFileSize(Long.valueOf(formAtta.getFileSize())));
            }catch (Exception e){
               //异常了不做处理
            }
        }
        formAtta.setModifyDate(new Date());
        DbFormAtta dbFormAtta = new DbFormAtta();
        BeanUtils.copyProperties(formAtta,dbFormAtta);
        if (null == formAtta.getId()) {
            int i = dbFormAttaMapper.insertSelective(dbFormAtta);
            formAtta.setId(dbFormAtta.getId());
        }else {
             dbFormAttaMapper.updateByPrimaryKeySelective(dbFormAtta);
        }
        return formAtta;
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormAttaById(Long oid) {
        return dbFormAttaMapper.deleteByOid(oid);
    }

    @Cacheable(key = "'getFormAttaById:'+#oid", unless = "#result == null")
    public FormAtta getFormAttaById(Long oid) {
        DbFormAtta dbFormAtta = dbFormAttaMapper.selectByPrimaryKey(oid);
        if(dbFormAtta == null)
            return null;
        FormAtta formAtta = new FormAtta();
        BeanUtils.copyProperties(dbFormAtta,formAtta);
        return formAtta;
    }

    /**
     * @description:  根据业务主键获取区划
     * @param attaOid
     * @author: wuxx
     * @Date: 2020/10/29 11:01
     **/
    @Cacheable(key = "'getFormAttaByAttaOid:'+#attaOid", unless = "#result == null")
    public FormAtta getFormAttaByAttaOid(String attaOid) {
        DbFormAtta dbFormAtta = dbFormAttaMapper.selectByAttaOid(attaOid);
        if(dbFormAtta == null)
            return null;
        FormAtta formAtta = new FormAtta();
        BeanUtils.copyProperties(dbFormAtta,formAtta);
        return formAtta;
    }

    public PageResult<FormAtta> queryFormAttaWithPage(FormAtta formAtta, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormAttaExample dbFormAttaExample = new DbFormAttaExample();
        DbFormAttaExample.Criteria criteria = dbFormAttaExample.createCriteria();
        dbFormAttaExample.setOrderByClause("UPLOAD_DATE desc");
        if(null!=formAtta){
            if(StrUtil.isNotEmpty(formAtta.getName())){
                criteria.andNameLike("%"+formAtta.getName()+"%");
            }
            if(null!= formAtta.getUserOid()){
                criteria.andUserOidEqualTo(formAtta.getUserOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormAtta> dbFormAttas = (Page<DbFormAtta>)dbFormAttaMapper.selectByExample(dbFormAttaExample);
        PageResult<FormAtta> pageResult = new PageResult<>(dbFormAttas.getPageNum(),dbFormAttas.getPageSize(),dbFormAttas.getTotal());
        List<FormAtta> formAttaList = dbFormAttas.stream().map(dbFormAtta -> {
            FormAtta atta = new FormAtta();
            BeanUtils.copyProperties(dbFormAtta,atta);
            return atta;
        }).collect(Collectors.toList());
        pageResult.setData(formAttaList);
        return pageResult;
    }


    public void downloadFile(HttpServletResponse response, HttpServletRequest request, String attaOid) {
        try {
            DbFormAtta atta = dbFormAttaMapper.selectByAttaOid(attaOid);
            if(null!=atta){
                DownloadUtil.downloadUploadUrlFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), atta.getFastdfsUploadUrl(), request, response);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("文件下载失败！",e);
        }
    }


    public InputStream getFileInputStream(HttpServletRequest request, String attaOid) {
        try {
            DbFormAtta atta = dbFormAttaMapper.selectByAttaOid(attaOid);
            if(null!=atta){
               return DownloadUtil.downloadFile(atta.getName(), atta.getFilePath(), atta.getFastdfsUploadUrl(), request);
            }
        }catch (Exception e){
            throw new ResultInfoException("文件下载失败！");
        }
        return null;
    }


    public JSONArray getAttaListByOids(List<String> attaOidList) {
        JSONArray array = JSONUtil.createArray();
        DbFormAttaExample dbFormAttaExample = new DbFormAttaExample();
        DbFormAttaExample.Criteria criteria = dbFormAttaExample.createCriteria();
        criteria.andAttaOidIn(attaOidList);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormAtta> dbFormAttaList = dbFormAttaMapper.selectByExample(dbFormAttaExample);
        dbFormAttaList.forEach(atta -> {
            array.add(new HashMap<String, String>() {{
                put("oid", atta.getAttaOid()+"");
                put("name", atta.getOriginName());
                put("size", atta.getFileSize()+"");
                put("url", StrUtil.isEmpty(atta.getFastdfsUploadUrl()) ? "" : fastDFSNginxUrl +"/" + atta.getFastdfsUploadUrl());
            }});
        });
        return array;
    }

    /**
     * @description: 删除附件包括文件
     * @author: wuxx
     * @Date: 2021/5/26 13:57
     **/
    @CacheEvict(allEntries = true)
    public int deleteFileById(Long oid) {
        DbFormAtta formAtta = dbFormAttaMapper.selectByPrimaryKey(oid);
        if(null==formAtta || BaseStaticParameter.YES.equals(formAtta.getIsDelete())){
            throw new ResultInfoException("文件不存在或已被删除！");
        }
        FileUtil.deleteFileByUploadType(formAtta.getFilePath(), formAtta.getName(), formAtta.getFastdfsUploadUrl());
        return dbFormAttaMapper.deleteByOid(oid);
    }

    /**
     * @description:  根据业务主键删除文件
     * @param attaOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/29 11:16
     **/
    @CacheEvict(allEntries = true)
    public int deleteFileByAttaOid(String attaOid) {
        DbFormAtta formAtta = dbFormAttaMapper.selectByAttaOid(attaOid);
        if(null==formAtta || BaseStaticParameter.YES.equals(formAtta.getIsDelete())){
            throw new ResultInfoException("文件不存在或已被删除！");
        }
        FileUtil.deleteFileByUploadType(formAtta.getFilePath(), formAtta.getName(), formAtta.getFastdfsUploadUrl());
        return dbFormAttaMapper.deleteByOid(formAtta.getId());
    }

    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOidList id主键集合
     * @author: wuxx
     * @Date: 2020/10/28 17:28
     **/
    public List<FormAtta> getAttaListByOidList(List<String> attaOidList) {
        DbFormAttaExample dbFormAttaExample = new DbFormAttaExample();
        DbFormAttaExample.Criteria criteria = dbFormAttaExample.createCriteria();
        dbFormAttaExample.setOrderByClause("UPLOAD_DATE desc");
        criteria.andAttaOidIn(attaOidList);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormAtta> dbFormAttas= dbFormAttaMapper.selectByExample(dbFormAttaExample);
        List<FormAtta> formAttaList = dbFormAttas.stream().map(dbFormAtta -> {
            FormAtta atta = new FormAtta();
            BeanUtils.copyProperties(dbFormAtta,atta);
            return atta;
        }).collect(Collectors.toList());
        return formAttaList;
    }

    /**
     * @param userOid 用户oid
     * @description: 根据用户oid获取附件的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    public List<FormAtta> getFormAttaListByUserOid(String userOid) {
        DbFormAttaExample dbFormAttaExample = new DbFormAttaExample();
        DbFormAttaExample.Criteria criteria = dbFormAttaExample.createCriteria();
        dbFormAttaExample.setOrderByClause("UPLOAD_DATE desc");
        criteria.andUserOidEqualTo(userOid);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormAtta> dbFormAttas= dbFormAttaMapper.selectByExample(dbFormAttaExample);
        List<FormAtta> formAttaList = dbFormAttas.stream().map(dbFormAtta -> {
            FormAtta atta = new FormAtta();
            BeanUtils.copyProperties(dbFormAtta,atta);
            return atta;
        }).collect(Collectors.toList());
        return formAttaList;
    }

    /**
     * @description:  根据业务主键获取base64
     * @param attaOid 业务主键
     * @author: wuxx
     * @Date: 2020/12/15 9:37
     **/
    public String getFormAttaBase64ByAttaOid(String attaOid,HttpServletRequest request){
        DbFormAtta atta = dbFormAttaMapper.selectByAttaOid(attaOid);
        if(null!=atta){
            try {
                InputStream inputStream = DownloadUtil.downloadFile(atta.getName(), atta.getFilePath(), atta.getFastdfsUploadUrl(), request);
                byte[] buffer = this.toByteArray(inputStream);
                return  Base64.encodeBase64String(buffer);
            }catch (Exception e){
                e.printStackTrace();
                return  null;
            }
        }
        return null;
    }

    /**
     * InputStream转化为byte[]数组
     * @param input
     * @return
     * @throws IOException
     */
    private static byte[] toByteArray(InputStream input) throws IOException {
        if(null==input){
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public JSONObject uploadImage(HttpServletRequest request,MultipartFile file, String userOid, String name) {
        try {
            if (StrUtil.isNotEmpty(name)) {
                String formatName = "png";
                if (StrUtil.isNotEmpty(name)) {
                    formatName = name.substring(name.lastIndexOf(".") + 1, name.length());
                }
                if (!BaseStaticParameter.ATTA_IMAGE_EXT_SET.contains(formatName.toLowerCase())) {
                    throw new ResultInfoException("请上传gif、jpg、jpeg、png或bmp格式的文件！");
                }
                file = new MockMultipartFile("File", name, "text/plain", file.getInputStream());
            }
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            //保存附件信息
            FormAtta atta = new FormAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            FormAtta sysAtta = this.saveFormAtta(atta);
            return JSONUtil.createObj().set("attaOid", sysAtta.getAttaOid()).set("url", fastDFSNginxUrl +"/" + sysAtta.getFastdfsUploadUrl()).set("name", sysAtta.getOriginName()).set("size",sysAtta.getFileSize());
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }
}

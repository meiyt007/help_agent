package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysAttaMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysAtta;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysAttaExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.fileUtil.DownloadUtil;
import com.zfsoft.platform.utils.fileUtil.FileUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * @ClassName SysAttaServiceImpl
 * @Description: 附件接口实现类
 * @Author wuxx
 * @Date 2020/9/7
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:atta")
public class SysAttaManager{
    @Resource
    private DbSysAttaMapper dbSysAttaMapper;


    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public SysAtta saveSysAtta(@ValidGroups(groups = {SysAtta.INSERT_GROUP.class})SysAtta sysAtta) {
        if (sysAtta == null) {
            throw new ResultInfoException("附件信息不正确!");
        }
        if (null == sysAtta.getId()) {
            sysAtta.setId(null);
            sysAtta.setAttaOid(IdUtil.simpleUUID());
        } else {
            // 附件oid不为空
            DbSysAtta atta = dbSysAttaMapper.selectByPrimaryKey(sysAtta.getId());
            if (atta == null) {
                throw new ResultInfoException("附件编号未查询到相应的附件信息!");
            }
        }
        if (null==sysAtta.getUploadDate()){
            sysAtta.setUploadDate(new Date());
        }

        // 设置附件信息的状态
        if (null!=sysAtta.getIsDelete()) {
            sysAtta.setIsDelete(BaseStaticParameter.N);
        }
        if(StrUtil.isNotEmpty(sysAtta.getFileSize())){
            try {
                sysAtta.setFileSize(FileUtil.formatFileSize(Long.valueOf(sysAtta.getFileSize())));
            }catch (Exception e){
               //异常了不做处理
            }
        }
        sysAtta.setModifyDate(new Date());
        DbSysAtta dbSysAtta = new DbSysAtta();
        BeanUtils.copyProperties(sysAtta,dbSysAtta);
        if (null == sysAtta.getId()) {
            int i = dbSysAttaMapper.insertSelective(dbSysAtta);
            sysAtta.setId(dbSysAtta.getId());
        }else {
             dbSysAttaMapper.updateByPrimaryKeySelective(dbSysAtta);
        }
        return sysAtta;
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysAttaById(Long oid) {
        return dbSysAttaMapper.deleteByOid(oid);
    }

    @Cacheable(key = "'getSysAttaById:'+#oid", unless = "#result == null")
    public SysAtta getSysAttaById(Long oid) {
        DbSysAtta dbSysAtta = dbSysAttaMapper.selectByPrimaryKey(oid);
        if(dbSysAtta == null)
            throw new ResultInfoException("附件信息为空！");
        SysAtta sysAtta = new SysAtta();
        BeanUtils.copyProperties(dbSysAtta,sysAtta);
        return sysAtta;
    }

    /**
     * @description:  根据业务主键获取区划
     * @param attaOid
     * @author: wuxx
     * @Date: 2020/10/29 11:01
     **/
    @Cacheable(key = "'getSysAttaByAttaOid:'+#attaOid", unless = "#result == null")
    public SysAtta getSysAttaByAttaOid(String attaOid) {
        DbSysAtta dbSysAtta = dbSysAttaMapper.selectByAttaOid(attaOid);
        if(dbSysAtta == null)
            throw new ResultInfoException("附件信息为空！");
        SysAtta sysAtta = new SysAtta();
        BeanUtils.copyProperties(dbSysAtta,sysAtta);
        return sysAtta;
    }

    public PageResult<SysAtta> querySysAttaWithPage(SysAtta sysAtta, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysAttaExample dbSysAttaExample = new DbSysAttaExample();
        DbSysAttaExample.Criteria criteria = dbSysAttaExample.createCriteria();
        dbSysAttaExample.setOrderByClause("UPLOAD_DATE desc");
        if(null!=sysAtta){
            if(StrUtil.isNotEmpty(sysAtta.getName())){
                criteria.andNameLike("%"+sysAtta.getName()+"%");
            }
            if(null!= sysAtta.getUserOid()){
                criteria.andUserOidEqualTo(sysAtta.getUserOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbSysAtta> dbSysAttas = (Page<DbSysAtta>)dbSysAttaMapper.selectByExample(dbSysAttaExample);
        PageResult<SysAtta> pageResult = new PageResult<>(dbSysAttas.getPageNum(),dbSysAttas.getPageSize(),dbSysAttas.getTotal());
        List<SysAtta> sysAttaList = dbSysAttas.stream().map(dbSysAtta -> {
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(dbSysAtta,atta);
            return atta;
        }).collect(Collectors.toList());
        pageResult.setData(sysAttaList);
        return pageResult;
    }


    public void downloadFile(HttpServletResponse response, HttpServletRequest request, String attaOid) {
        try {
            DbSysAtta atta = dbSysAttaMapper.selectByAttaOid(attaOid);
            if(null!=atta){
                //DownloadUtil.downloadFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), atta.getFastdfsNginxUrl(), request, response);
                DownloadUtil.downloadUploadUrlFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), atta.getFastdfsUploadUrl(), request, response);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("文件下载失败！",e);
        }
    }


    public InputStream getFileInputStream(HttpServletRequest request, String attaOid) {
        try {
            DbSysAtta atta = dbSysAttaMapper.selectByAttaOid(attaOid);
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
        DbSysAttaExample dbSysAttaExample = new DbSysAttaExample();
        DbSysAttaExample.Criteria criteria = dbSysAttaExample.createCriteria();
        criteria.andAttaOidIn(attaOidList);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysAtta> dbSysAttaList = dbSysAttaMapper.selectByExample(dbSysAttaExample);
        dbSysAttaList.forEach(atta -> {
            array.add(new HashMap<String, String>() {{
                put("oid", atta.getAttaOid()+"");
                put("name", atta.getOriginName());
                put("size", atta.getFileSize()+"");
                put("url", StrUtil.isEmpty(atta.getFastdfsNginxUrl()) ? "" : atta.getFastdfsNginxUrl());
            }});
        });
        return array;
    }

    @CacheEvict(allEntries = true)
    public int deleteFileById(Long oid) {
        DbSysAtta sysAtta = dbSysAttaMapper.selectByPrimaryKey(oid);
        if(null==sysAtta || BaseStaticParameter.YES.equals(sysAtta.getIsDelete())){
            throw new ResultInfoException("文件不存在或已被删除！");
        }
        FileUtil.deleteFileByUploadType(sysAtta.getFilePath(), sysAtta.getName(), sysAtta.getFastdfsUploadUrl());
        return dbSysAttaMapper.deleteByOid(oid);
    }

    /**
     * @description:  根据业务主键删除文件
     * @param attaOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/29 11:16
     **/
    @CacheEvict(allEntries = true)
    public int deleteFileByAttaOid(String attaOid) {
        DbSysAtta sysAtta = dbSysAttaMapper.selectByAttaOid(attaOid);
        if(null==sysAtta || BaseStaticParameter.YES.equals(sysAtta.getIsDelete())){
            throw new ResultInfoException("文件不存在或已被删除！");
        }
        FileUtil.deleteFileByUploadType(sysAtta.getFilePath(), sysAtta.getName(), sysAtta.getFastdfsUploadUrl());
        return dbSysAttaMapper.deleteByOid(sysAtta.getId());
    }

    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOidList id主键集合
     * @author: wuxx
     * @Date: 2020/10/28 17:28
     **/
    public List<SysAtta> getAttaListByOidList(List<String> attaOidList) {
        DbSysAttaExample dbSysAttaExample = new DbSysAttaExample();
        DbSysAttaExample.Criteria criteria = dbSysAttaExample.createCriteria();
        dbSysAttaExample.setOrderByClause("UPLOAD_DATE desc");
        criteria.andAttaOidIn(attaOidList);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysAtta> dbSysAttas= dbSysAttaMapper.selectByExample(dbSysAttaExample);
        List<SysAtta> sysAttaList = dbSysAttas.stream().map(dbSysAtta -> {
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(dbSysAtta,atta);
            return atta;
        }).collect(Collectors.toList());
        return sysAttaList;
    }

    /**
     * @param userOid 用户oid
     * @description: 根据用户oid获取附件的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    public List<SysAtta> getSysAttaListByUserOid(String userOid) {
        DbSysAttaExample dbSysAttaExample = new DbSysAttaExample();
        DbSysAttaExample.Criteria criteria = dbSysAttaExample.createCriteria();
        dbSysAttaExample.setOrderByClause("UPLOAD_DATE desc");
        criteria.andUserOidEqualTo(userOid);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysAtta> dbSysAttas= dbSysAttaMapper.selectByExample(dbSysAttaExample);
        List<SysAtta> sysAttaList = dbSysAttas.stream().map(dbSysAtta -> {
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(dbSysAtta,atta);
            return atta;
        }).collect(Collectors.toList());
        return sysAttaList;
    }

    /**
     * @description:  根据业务主键获取base64
     * @param attaOid 业务主键
     * @author: wuxx
     * @Date: 2020/12/15 9:37
     **/
    public String getSysAttaBase64ByAttaOid(String attaOid,HttpServletRequest request){
        DbSysAtta atta = dbSysAttaMapper.selectByAttaOid(attaOid);
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
}

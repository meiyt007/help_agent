package com.zfsoft.service.manager.sxDirectory;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxDirectory.DbSxDirectoryMapper;
import com.zfsoft.service.dbaccess.data.sxDirectory.DbSxDirectory;
import com.zfsoft.service.dbaccess.data.sxDirectory.DbSxDirectoryExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.util.JsonUtil;
import com.zfsoft.service.sxDirectory.data.SxDirectory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SxDirectoryServiceImpl
 * @Description: 目录清单实现类
 * @Author wangxl
 * @Date 2020/10/22
 **/
@Service
@Slf4j
public class SxDirectoryManager {

        @Resource
        private DbSxDirectoryMapper dbSxDirectoryMapper;

        public SxDirectory getSxDirectoryByOid(String oid) {
            DbSxDirectory dbSxDirectory = dbSxDirectoryMapper.getDbSxDirectoryByOid(oid);
            SxDirectory sxDirectory = new SxDirectory();
            if(dbSxDirectory != null){
                BeanUtils.copyProperties(dbSxDirectory,sxDirectory);
            }
            return sxDirectory;
        }

        public String getSxDirectoryJsonByOid(String oid) {
            DbSxDirectory dbSxDirectory = dbSxDirectoryMapper.getDbSxDirectoryByOid(oid);
            if(dbSxDirectory == null){
                throw new ResultInfoException("目录清单信息为空！");
            }
            SxDirectory sxDirectory = new SxDirectory();
            BeanUtils.copyProperties(dbSxDirectory,sxDirectory);
            return JsonUtil.toJSONString(sxDirectory);
        }

    public PageResult<SxDirectory>  querySxDirectoryWithPage(SxDirectory sxDirectory , Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxDirectoryExample dbSxDirectoryExample = new DbSxDirectoryExample();
        DbSxDirectoryExample.Criteria criteria = dbSxDirectoryExample.createCriteria();
        if(null!=sxDirectory){
            if(StrUtil.isNotEmpty(sxDirectory.getDirectoryName())){
                criteria.andDirectoryNameLike("%"+sxDirectory.getDirectoryName()+"%");
            }
            if(StrUtil.isNotEmpty(sxDirectory.getBasicCode())){
                criteria.andBasicCodeLike(sxDirectory.getBasicCode());
            }
            if(StrUtil.isNotEmpty(sxDirectory.getServiceTypeOid())){
                criteria.andServiceTypeOidEqualTo(sxDirectory.getServiceTypeOid());
            }
            if(null != sxDirectory.getDirectoryStatus()){
                criteria.andDirectoryStatusEqualTo(sxDirectory.getDirectoryStatus());
            }
            if(StrUtil.isNotEmpty(sxDirectory.getLevelDicts())){
                criteria.andDirectoryNameLike("%,"+sxDirectory.getDirectoryName()+",%");
            }
        }
        criteria.andDelFlagEqualTo((short)0);
        Page<DbSxDirectory> dbSxDirectorys = (Page<DbSxDirectory>)dbSxDirectoryMapper.selectByExample(dbSxDirectoryExample);
        PageResult<SxDirectory> pageResult = new PageResult<>(dbSxDirectorys.getPageNum(),dbSxDirectorys.getPageSize(),dbSxDirectorys.getTotal());
        List<SxDirectory> sxDirectoryList = dbSxDirectorys.stream().map(dbSxDirectory -> {
            SxDirectory directory = new SxDirectory();
            BeanUtils.copyProperties(dbSxDirectory,directory);
            return directory;
        }).collect(Collectors.toList());
        pageResult.setData(sxDirectoryList);
        return pageResult;
    }

    public List<SxDirectory> querySxDirectory() {
        List<DbSxDirectory> dbSxDirectories =  dbSxDirectoryMapper.selectAllDirectoryList();
        List<SxDirectory> sxDirectoryList =  dbSxDirectories.stream().map(dbSxDirectory -> {
            SxDirectory sxDirectory = new SxDirectory();
            BeanUtils.copyProperties(dbSxDirectory,sxDirectory);
            return sxDirectory;
        }).collect(Collectors.toList());
        return sxDirectoryList;
    }
}

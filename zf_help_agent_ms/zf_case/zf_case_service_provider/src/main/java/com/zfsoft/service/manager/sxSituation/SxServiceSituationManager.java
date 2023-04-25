package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceSituationMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceSituation;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceSituationExample;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxSituation.data.SxServiceSituation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 事项情形表 实现类
 * @date 2020/11/3 11:56
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceSituationManager {

    @Resource
    private DbSxServiceSituationMapper dbSxServiceSituationMapper;

    public SxServiceSituation getSxServiceSituationByOid(String oid){
        DbSxServiceSituation dbSxServiceSituation = dbSxServiceSituationMapper.selectByOid(oid);
        SxServiceSituation sxServiceSituation = null ;
        if (null == dbSxServiceSituation) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询事项情形表单为空！");
        }else{
            sxServiceSituation = new SxServiceSituation();
            BeanUtils.copyProperties(dbSxServiceSituation, sxServiceSituation);
        }
        return sxServiceSituation;
    }


    public List<SxServiceSituation> getSxServiceSituationsByServiceOid(String serviceOid) {
        DbSxServiceSituationExample dbSxServiceSituationExample = new DbSxServiceSituationExample();
        DbSxServiceSituationExample.Criteria criteria = dbSxServiceSituationExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short)0);
        List<DbSxServiceSituation> dbSxServiceSituation = dbSxServiceSituationMapper.selectByExample(dbSxServiceSituationExample);
        List<SxServiceSituation> sxServiceSituations = dbSxServiceSituation.stream().map(sxServiceSituation -> {
            SxServiceSituation serviceSituation = new SxServiceSituation();
            BeanUtils.copyProperties(sxServiceSituation,serviceSituation);
            return serviceSituation;
        }).collect(Collectors.toList());
        return sxServiceSituations;
    }

    public ApiResultSet<PageResult<SxServiceSituation>> querySituations(String situtationName, String serviceOid, Integer pageNum, Integer pageSize){
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum,pageSize);
        DbSxServiceSituationExample dbSxServiceSituationExample = new DbSxServiceSituationExample();
        DbSxServiceSituationExample.Criteria criteria = dbSxServiceSituationExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        if(StrUtil.isNotEmpty(situtationName)){
            criteria.andSituationNameLike("%"+situtationName+"%");
        }
        criteria.andDeleteStatusEqualTo((short)0);
        dbSxServiceSituationExample.setOrderByClause(" sort asc");
        Page<DbSxServiceSituation> dbSxServiceSituation = (Page<DbSxServiceSituation>) dbSxServiceSituationMapper.selectByExample(dbSxServiceSituationExample);
        PageResult pageRestu=new PageResult(dbSxServiceSituation.getPageNum(),dbSxServiceSituation.getPageSize(),dbSxServiceSituation.getTotal());
        List<SxServiceSituation> sxServiceSituations = dbSxServiceSituation.stream().map(sxServiceSituation -> {
            SxServiceSituation serviceSituation = new SxServiceSituation();
            BeanUtils.copyProperties(sxServiceSituation,serviceSituation);
            return serviceSituation;
        }).collect(Collectors.toList());
        pageRestu.setData(sxServiceSituations);
        return new ApiResultSet<>(pageRestu);
    }

    public boolean saveSxServiceSituation(SxServiceSituation serviceMaterial) {
        if(!StrUtil.isNotEmpty(serviceMaterial.getOid())){
            String situationName = serviceMaterial.getSituationName();
            DbSxServiceSituationExample dbSxServiceSituationExample = new DbSxServiceSituationExample();
            DbSxServiceSituationExample.Criteria criteria = dbSxServiceSituationExample.createCriteria();
            criteria.andSituationNameEqualTo(situationName.trim());
            criteria.andDeleteStatusEqualTo((short)0);
            criteria.andServiceOidEqualTo(serviceMaterial.getServiceOid().trim());
            List<DbSxServiceSituation> dbSxServiceSituations = dbSxServiceSituationMapper.selectByExample(dbSxServiceSituationExample);
            if(CollectionUtils.isNotEmpty(dbSxServiceSituations)){
                return Boolean.FALSE;
            }
            DbSxServiceSituation record = new  DbSxServiceSituation();
            BeanUtils.copyProperties(serviceMaterial,record);
            record.setOid(UUID.randomUUID().toString().replaceAll("-",""));
            record.setDeleteStatus((short)0);
            record.setCreateDate(new Date());
            dbSxServiceSituationMapper.insertSelective(record);
            return Boolean.TRUE;
        }else{
            DbSxServiceSituation record = new  DbSxServiceSituation();
            BeanUtils.copyProperties(serviceMaterial,record);
            record.setModifyDate(new Date());
            dbSxServiceSituationMapper.updateByPrimaryKeySelective(record);
            return Boolean.TRUE;
        }
    }

    public int delSxServiceSituationByOid(String oid) {
        DbSxServiceSituation dbSxServiceSituation = dbSxServiceSituationMapper.selectByOid(oid);
        dbSxServiceSituation.setDeleteStatus((short)1);
        return dbSxServiceSituationMapper.updateByPrimaryKeySelective(dbSxServiceSituation);
    }

}

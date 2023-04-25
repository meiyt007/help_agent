package com.zfsoft.microservice.form.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormDesign;
import com.zfsoft.microservice.form.data.FormReportLog;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormReportLogMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormReportLog;
import com.zfsoft.microservice.form.dbaccess.data.DbFormReportLogExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FormReportLogManager
 * @Description: 表单填报数据日志日志接口实现类
 * @Author wuxx
 * @Date 2021/4/19 10:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:reportLog")
public class FormReportLogManager {

    @Resource
    private DbFormReportLogMapper dbFormReportLogMapper;

    @Resource
    private FormDesignManager formDesignManager;

    /**
     * @description: 分页查询填报数据日志的列表
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    public PageResult<FormReportLog> queryFormReportLogWithPage(FormReportLog formReportLog, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormReportLogExample dbFormReportLogExample = new DbFormReportLogExample();
        DbFormReportLogExample.Criteria criteria = dbFormReportLogExample.createCriteria();
        dbFormReportLogExample.setOrderByClause(" CREATE_DATE DESC ");
        if(null!=formReportLog){
            if(StrUtil.isNotEmpty(formReportLog.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formReportLog.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formReportLog.getFormMainOid())){
                criteria.andFormMainOidEqualTo(formReportLog.getFormMainOid());
            }
            if(StrUtil.isNotEmpty(formReportLog.getDesignOid())){
                criteria.andDesignOidEqualTo(formReportLog.getDesignOid());
            }
            if(StrUtil.isNotEmpty(formReportLog.getStartTime())){
                String startTime = formReportLog.getStartTime();
                Date date = DateUtil.parse(startTime);
                //一天的开始，结果：2017-03-01 00:00:00
                Date beginOfDay = DateUtil.beginOfDay(date);
                criteria.andCreateDateGreaterThanOrEqualTo(beginOfDay);
            }
            if(StrUtil.isNotEmpty(formReportLog.getEndTime())){
                String endTime = formReportLog.getEndTime();
                Date date = DateUtil.parse(endTime);
                //一天的结束，结果：2017-03-01 23:59:59
                Date endOfDay = DateUtil.endOfDay(date);
                criteria.andCreateDateLessThanOrEqualTo(endOfDay);
            }
        }
        Page<DbFormReportLog> dbFormReportLogs = (Page<DbFormReportLog>)dbFormReportLogMapper.selectByExampleWithBLOBs(dbFormReportLogExample);
        PageResult<FormReportLog> pageResult = new PageResult<>(dbFormReportLogs.getPageNum(),dbFormReportLogs.getPageSize(),dbFormReportLogs.getTotal());
        List<FormReportLog> formReportLogList = dbFormReportLogs.stream().map(dbFormReportLog -> {
            FormReportLog object = new FormReportLog();
            BeanUtils.copyProperties(dbFormReportLog,object);
            if(StrUtil.isNotBlank(object.getFormMainOid())){
                FormDesign design = formDesignManager.getFormDesignByDesignOid(object.getDesignOid());
                if(null==design){
                    design = formDesignManager.getFormDesignByDesignOid(object.getFormMainOid());
                }
                if(null!=design){
                    object.setFormName(design.getDesignName());
                    object.setVersion(design.getVersion());
                }
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formReportLogList);
        return pageResult;
    }

    /**
     * @description:  保存填报数据日志信息
     * @param formReportLog 填报数据日志对象
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormReportLog(@ValidGroups(groups = {FormReportLog.INSERT_GROUP.class}) FormReportLog formReportLog) {
        if(null == formReportLog){
            throw new ResultInfoException("填报数据日志对象不能为空！");
        }
        if (null == formReportLog.getId()) {
            formReportLog.setId(null);
            formReportLog.setReportLogOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormReportLog curDict = this.getFormReportLogById(formReportLog.getId());
            if (curDict == null) {
                throw new ResultInfoException("填报数据日志对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formReportLog.getCreateDate()) {
            formReportLog.setCreateDate(new Date());
        }
        if(StrUtil.isNotBlank(formReportLog.getFormMainOid())){
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(formReportLog.getFormMainOid());
            formReportLog.setDesignOid(null!=design?design.getDesignOid():null);
        }
        DbFormReportLog dbFormReportLog = new DbFormReportLog();
        BeanUtils.copyProperties(formReportLog,dbFormReportLog);
        int index=0;
        if (null == formReportLog.getId()) {
            index = dbFormReportLogMapper.insert(dbFormReportLog);
        }else {
            index = dbFormReportLogMapper.updateByPrimaryKeySelective(dbFormReportLog);
        }
        formReportLog.setId(dbFormReportLog.getId());
        return index;
    }

    /**
     * @description: 填报数据日志的更新
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateFormReportLog(@ValidGroups(groups = {FormReportLog.UPDATE_GROUP.class}) FormReportLog formReportLog) {
        DbFormReportLog dbFormReportLog = new DbFormReportLog();
        BeanUtils.copyProperties(formReportLog,dbFormReportLog);
        int index = dbFormReportLogMapper.updateByPrimaryKeySelective(dbFormReportLog);
        return index ;
    }

    /**
     * @description:  删除填报数据日志信息
     * @param id 填报数据日志对象主键
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormReportLogById(Long id) {
        int index =  dbFormReportLogMapper.deleteByPrimaryKey(id);
        if(index==0){
            throw new ResultInfoException("填报数据日志对象删除失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Cacheable(key = "'getFormReportLogById:'+#id", unless = "#result == null")
    public FormReportLog getFormReportLogById(Long id) {
        DbFormReportLog dbFormReportLog = dbFormReportLogMapper.selectByPrimaryKey(id);
        if(dbFormReportLog == null)
            return null;
        FormReportLog formReportLog = new FormReportLog();
        BeanUtils.copyProperties(dbFormReportLog,formReportLog);
        return formReportLog;
    }

    /**
     * @description: 根据主键获取最新的一条对象信息
     * @param reportOid 填报主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Cacheable(key = "'getFormReportLogByReportOid:'+#reportOid", unless = "#result == null")
    public FormReportLog getFormReportLogByReportOid(String reportOid) {
        DbFormReportLogExample dbFormReportLogExample = new DbFormReportLogExample();
        dbFormReportLogExample.setOrderByClause(" CREATE_DATE DESC ");
        DbFormReportLogExample.Criteria criteria = dbFormReportLogExample.createCriteria();
        criteria.andReportOidEqualTo(reportOid);
        List<DbFormReportLog> dbFormReportLogs = dbFormReportLogMapper.selectByExample(dbFormReportLogExample);
        if(dbFormReportLogs.size() == 0)
            return null;
        DbFormReportLog dbFormReportLog = dbFormReportLogs.get(0);
        FormReportLog formReportLog = new FormReportLog();
        BeanUtils.copyProperties(dbFormReportLog,formReportLog);
        return formReportLog;
    }

    /**
     * @description:  获取表单设计对象的配置信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @Cacheable(key = "'getFormConfigAndData:'+#id", unless = "#result == null")
    public FormReportLog getFormConfigAndData(Long id) {
        DbFormReportLog dbFormReportLog = dbFormReportLogMapper.selectByPrimaryKey(id);
        if(dbFormReportLog == null)
            return null;
        FormReportLog formReportLog = new FormReportLog();
        BeanUtils.copyProperties(dbFormReportLog,formReportLog);
        FormDesign design = formDesignManager.getFormDesignByDesignOid(formReportLog.getDesignOid());
        if(null==design){
           return null;
        }
        //获取配置信息
        formReportLog.setFormConfig(design.getFormConfig());
        return formReportLog;
    }

    /**
     * @description: 根据填报数据日志对象查询集合
     * @param formReportLog 填报数据日志对象
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    public List<FormReportLog> queryFormReportLogList(FormReportLog formReportLog) {
        DbFormReportLogExample dbFormReportLogExample = new DbFormReportLogExample();
        dbFormReportLogExample.setOrderByClause(" ID DESC ");
        DbFormReportLogExample.Criteria criteria = dbFormReportLogExample.createCriteria();
        if(null!=formReportLog){
            if(StrUtil.isNotEmpty(formReportLog.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formReportLog.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formReportLog.getFormMainOid())){
                criteria.andFormMainOidEqualTo(formReportLog.getFormMainOid());
            }
            if(StrUtil.isNotEmpty(formReportLog.getDesignOid())){
                criteria.andDesignOidEqualTo(formReportLog.getDesignOid());
            }
        }
        List<DbFormReportLog> dbFormReportLogList = dbFormReportLogMapper.selectByExampleWithBLOBs(dbFormReportLogExample);
        List<FormReportLog> formReportLogList = dbFormReportLogList.stream().map(dbFormReportLog -> {
            FormReportLog object = new FormReportLog();
            BeanUtils.copyProperties(dbFormReportLog,object);
            return object;
        }).collect(Collectors.toList());
        return formReportLogList;
    }

}

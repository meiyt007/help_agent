package com.zfsoft.microservice.form.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormDesign;
import com.zfsoft.microservice.form.data.FormReport;
import com.zfsoft.microservice.form.data.vo.FormDataVo;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormReportMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormReport;
import com.zfsoft.microservice.form.dbaccess.data.DbFormReportExample;
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
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName FormReportManager
 * @Description: 表单填报数据接口实现类
 * @Author wuxx
 * @Date 2021/4/19 10:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:report")
public class FormReportManager {

    @Resource
    private DbFormReportMapper dbFormReportMapper;

    @Resource
    private FormDesignManager formDesignManager;

    /**
     * @description: 分页查询填报数据的列表
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    public PageResult<FormReport> queryFormReportWithPage(FormReport formReport, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormReportExample dbFormReportExample = new DbFormReportExample();
        DbFormReportExample.Criteria criteria = dbFormReportExample.createCriteria();
        dbFormReportExample.setOrderByClause(" ID DESC ");
        if(null!=formReport){
            if(StrUtil.isNotEmpty(formReport.getBusinessKey())){
                String value = formReport.getBusinessKey().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andBusinessKeyLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formReport.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formReport.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formReport.getFormMainOid())){
                criteria.andFormMainOidEqualTo(formReport.getFormMainOid());
            }
            if(StrUtil.isNotEmpty(formReport.getDesignOid())){
                criteria.andDesignOidEqualTo(formReport.getDesignOid());
            }
            if(StrUtil.isNotEmpty(formReport.getStartTime())){
                String startTime = formReport.getStartTime();
                Date date = DateUtil.parse(startTime);
                //一天的开始，结果：2017-03-01 00:00:00
                Date beginOfDay = DateUtil.beginOfDay(date);
                criteria.andCreateDateGreaterThanOrEqualTo(beginOfDay);
            }
            if(StrUtil.isNotEmpty(formReport.getEndTime())){
                String endTime = formReport.getEndTime();
                Date date = DateUtil.parse(endTime);
                //一天的结束，结果：2017-03-01 23:59:59
                Date endOfDay = DateUtil.endOfDay(date);
                criteria.andCreateDateLessThanOrEqualTo(endOfDay);
            }
        }
        Page<DbFormReport> dbFormReports = (Page<DbFormReport>)dbFormReportMapper.selectByExampleWithBLOBs(dbFormReportExample);
        PageResult<FormReport> pageResult = new PageResult<>(dbFormReports.getPageNum(),dbFormReports.getPageSize(),dbFormReports.getTotal());
        List<FormReport> formReportList = dbFormReports.stream().map(dbFormReport -> {
            FormReport object = new FormReport();
            BeanUtils.copyProperties(dbFormReport,object);
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
        pageResult.setData(formReportList);
        return pageResult;
    }

    /**
     * @description:  保存填报数据信息
     * @param formReport 填报数据对象
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormReport(@ValidGroups(groups = {FormReport.INSERT_GROUP.class}) FormReport formReport) {
        if(null == formReport){
            throw new ResultInfoException("填报数据对象不能为空！");
        }
        if (null == formReport.getId()) {
            formReport.setId(null);
            formReport.setReportOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormReport curDict = this.getFormReportById(formReport.getId());
            if (curDict == null) {
                throw new ResultInfoException("填报数据对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formReport.getCreateDate()) {
            formReport.setCreateDate(new Date());
        }
        if(StrUtil.isNotBlank(formReport.getFormMainOid())){
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(formReport.getFormMainOid());
            formReport.setDesignOid(null!=design?design.getDesignOid():null);
        }
        DbFormReport dbFormReport = new DbFormReport();
        BeanUtils.copyProperties(formReport,dbFormReport);
        int index=0;
        if (null == formReport.getId()) {
            index = dbFormReportMapper.insert(dbFormReport);
        }else {
            index = dbFormReportMapper.updateByPrimaryKeySelective(dbFormReport);
        }
        formReport.setId(dbFormReport.getId());
        return index;
    }

    /**
     * @description: 填报数据的更新
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateFormReport(@ValidGroups(groups = {FormReport.UPDATE_GROUP.class}) FormReport formReport) {
        DbFormReport dbFormReport = new DbFormReport();
        BeanUtils.copyProperties(formReport,dbFormReport);
        int index = dbFormReportMapper.updateByPrimaryKeySelective(dbFormReport);
        return index ;
    }

    /**
     * @description:  删除填报数据信息
     * @param id 填报数据对象主键
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormReportById(Long id) {
        int index =  dbFormReportMapper.deleteByPrimaryKey(id);
        if(index==0){
            throw new ResultInfoException("填报数据对象删除失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Cacheable(key = "'getFormReportById:'+#id", unless = "#result == null")
    public FormReport getFormReportById(Long id) {
        DbFormReport dbFormReport = dbFormReportMapper.selectByPrimaryKey(id);
        if(dbFormReport == null)
            return null;
        FormReport formReport = new FormReport();
        BeanUtils.copyProperties(dbFormReport,formReport);
        /*if(StrUtil.isNotBlank(formReport.getFormData())){
            FormDesign design = formDesignManager.getFormDesignByDesignOid(dbFormReport.getDesignOid());
            if(null!=design){
                String formData = FormUtil.getFormDataByConfig(design.getFormConfig(), formReport.getFormData());
                formReport.setFormData(formData);
            }
        }*/
        return formReport;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param reportOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Cacheable(key = "'getFormReportByFormReportOid:'+#reportOid", unless = "#result == null")
    public FormReport getFormReportByFormReportOid(String reportOid) {
        DbFormReport dbFormReport = dbFormReportMapper.selectByForeignKey(reportOid);
        if(dbFormReport == null)
            return null;
        FormReport formReport = new FormReport();
        BeanUtils.copyProperties(dbFormReport,formReport);
        return formReport;
    }

    /**
     * @description: 根据填报数据对象查询集合
     * @param formReport 填报数据对象
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    public List<FormReport> queryFormReportList(FormReport formReport) {
        DbFormReportExample dbFormReportExample = new DbFormReportExample();
        dbFormReportExample.setOrderByClause(" ID DESC ");
        DbFormReportExample.Criteria criteria = dbFormReportExample.createCriteria();
        if(null!=formReport){
            if(StrUtil.isNotEmpty(formReport.getBusinessKey())){
                String value = formReport.getBusinessKey().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andBusinessKeyLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formReport.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formReport.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formReport.getFormMainOid())){
                criteria.andFormMainOidEqualTo(formReport.getFormMainOid());
            }
            if(StrUtil.isNotEmpty(formReport.getDesignOid())){
                criteria.andDesignOidEqualTo(formReport.getDesignOid());
            }
        }
        List<DbFormReport> dbFormReportList = dbFormReportMapper.selectByExampleWithBLOBs(dbFormReportExample);
        List<FormReport> formReportList = dbFormReportList.stream().map(dbFormReport -> {
            FormReport object = new FormReport();
            BeanUtils.copyProperties(dbFormReport,object);
            return object;
        }).collect(Collectors.toList());
        return formReportList;
    }

}

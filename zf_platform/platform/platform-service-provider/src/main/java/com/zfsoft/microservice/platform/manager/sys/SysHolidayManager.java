package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysHoliday;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysHolidayMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysHoliday;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysHolidayExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName SysHolidayServiceImpl
 * @Description: 节假日接口实现类
 * @Author wuxx
 * @Date 2020/10/20
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:holiday")
public class SysHolidayManager{

    @Resource
    private DbSysHolidayMapper dbSysHolidayMapper;

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysHoliday(@ValidGroups(groups = {SysHoliday.INSERT_GROUP.class})SysHoliday sysHoliday) {
        if (sysHoliday == null) {
            throw new ResultInfoException("节假日信息不正确!");
        }
        if (null == sysHoliday.getId()) {
            sysHoliday.setId(null);
        } else {
            // 节假日oid不为空
            SysHoliday curDict = getSysHolidayById(sysHoliday.getId());
            if (curDict == null) {
                throw new ResultInfoException("节假日编号未查询到相应的节假日信息!");
            }
        }
        // 获取日期的星期几，判断是否为工作日或节假日
        // 当星期一二三四五时，只能为节假日
        // 当星期六日时，只能为工作日
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.parse(sysHoliday.getHolidayDate(), "yyyy-MM-dd"));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        // 周一二三四五
        int dayLenth=5;
        if (w <= dayLenth && w >= 1) {
            if (!BaseStaticParameter.HOLIDAY_DATE_1
                    .equals(sysHoliday.getHolidayType())) {
                throw new ResultInfoException( "周一二三四五只能为节假日！");
            }
        } else { // 周六日
            if (!BaseStaticParameter.HOLIDAY_DATE_2
                    .equals(sysHoliday.getHolidayType())) {
                throw new ResultInfoException( "周六日只能为工作日！");
            }
        }
        DbSysHoliday fristDbSysHoliday = dbSysHolidayMapper.selectFristByHolidayDate(sysHoliday.getHolidayDate());
        if (fristDbSysHoliday != null
                && !fristDbSysHoliday.getId().equals(sysHoliday.getId())) {
            throw new ResultInfoException( "节假日期已经存在，不能重复添加！");
        }

        DbSysHoliday dbSysHoliday = new DbSysHoliday();
        BeanUtils.copyProperties(sysHoliday,dbSysHoliday);
        if (null == sysHoliday.getId()) {
            return dbSysHolidayMapper.insert(dbSysHoliday);
            //return dbSysHolidayMapper.insertSelective(dbSysHoliday);
        }else {
            return dbSysHolidayMapper.updateByPrimaryKeySelective(dbSysHoliday);
        }
    }

    @Cacheable(key = "'getSysHolidayById:'+#oid", unless = "#result == null")
    public SysHoliday getSysHolidayById(Long oid) {
        DbSysHoliday dbSysHoliday = dbSysHolidayMapper.selectByPrimaryKey(oid);
        if(dbSysHoliday == null)
            throw new ResultInfoException("节假日信息为空！");
        SysHoliday sysHoliday = new SysHoliday();
        BeanUtils.copyProperties(dbSysHoliday,sysHoliday);
        return sysHoliday;
    }

    @CacheEvict(allEntries = true)
    public int deleteSysHolidayById(Long oid) {
        return dbSysHolidayMapper.deleteByPrimaryKey(oid);
    }

    public PageResult<SysHoliday> querySysHolidayWithPage(SysHoliday sysHoliday, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysHolidayExample dbSysHolidayExample = new DbSysHolidayExample();
        dbSysHolidayExample.setOrderByClause("ID desc");
        DbSysHolidayExample.Criteria criteria = dbSysHolidayExample.createCriteria();
        if(null!=sysHoliday){
            if(StrUtil.isNotEmpty(sysHoliday.getHolidayDate())){
                criteria.andHolidayDateEqualTo(sysHoliday.getHolidayDate());
            }
            if(StrUtil.isNotEmpty(sysHoliday.getHolidayType())){
                criteria.andHolidayTypeEqualTo(sysHoliday.getHolidayType().trim());
            }
        }
        Page<DbSysHoliday> dbSysHolidays = (Page<DbSysHoliday>)dbSysHolidayMapper.selectByExample(dbSysHolidayExample);
        PageResult<SysHoliday> pageResult = new PageResult<>(dbSysHolidays.getPageNum(),dbSysHolidays.getPageSize(),dbSysHolidays.getTotal());
        List<SysHoliday> sysHolidayList = dbSysHolidays.stream().map(dbSysHoliday -> {
            SysHoliday app = new SysHoliday();
            BeanUtils.copyProperties(dbSysHoliday,app);
            return app;
        }).collect(Collectors.toList());
        pageResult.setData(sysHolidayList);
        return pageResult;
    }

    /**
     * @description: 根据节假日类型获取节假日集合
     * @author: wuxx
     * @Date: 2021/2/1 15:16
     **/
    @Cacheable(key = "'querySysHolidaySetByHolidayType:'+#holidayType", unless = "#result == null")
    public Set<String> querySysHolidaySetByHolidayType(String holidayType) {
        return dbSysHolidayMapper.querySysHolidaySetByHolidayType(holidayType);
    }


    public List<SysHoliday> querySysHolidayListByYear(String year, String holidayType) {
        int yearNow = DateUtil.year(new Date());
        if(StrUtil.isNotBlank(year) && StrUtil.isNotEmpty(year)){
           try {
               yearNow =  Integer.parseInt(year);
           }catch (Exception e){
               throw new ResultInfoException("参数year不正确！");
           }
        }
        DbSysHolidayExample dbSysHolidayExample = new DbSysHolidayExample();
        dbSysHolidayExample.setOrderByClause("ID desc");
        DbSysHolidayExample.Criteria criteria = dbSysHolidayExample.createCriteria();
        criteria.andHolidayDateLike(yearNow+"%");
        if(StrUtil.isNotEmpty(holidayType)){
            criteria.andHolidayTypeEqualTo(holidayType.trim());
        }
        List<DbSysHoliday> dbSysHolidays = dbSysHolidayMapper.selectByExample(dbSysHolidayExample);
        List<SysHoliday> sysHolidayList = dbSysHolidays.stream().map(dbSysHoliday -> {
            SysHoliday app = new SysHoliday();
            BeanUtils.copyProperties(dbSysHoliday,app);
            return app;
        }).collect(Collectors.toList());
        return sysHolidayList;
    }

}

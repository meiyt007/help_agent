package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysMessageSended;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysMessageSendedMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageSended;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageSendedExample;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysMessageSendedManager
 * @Description: 已发送消息接口实现类
 * @Author wuxx
 * @Date 2020/10/23
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:message")
public class SysMessageSendedManager {

    @Resource
    private DbSysMessageSendedMapper dbSysMessageSendedMapper;

    /**
     * 增加一个新消息
     * @param sysMessageSended 新消息
     * @return
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysMessageSended(@ValidGroups(groups = {SysMessageSended.INSERT_GROUP.class})SysMessageSended sysMessageSended) {
        if (sysMessageSended == null) {
            throw new ResultInfoException("消息信息不正确!");
        }
        if (null == sysMessageSended.getId()) {
            sysMessageSended.setId(null);
        } else {
            // 消息oid不为空
            SysMessageSended cur = getSysMessageSendedById(sysMessageSended.getId());
            if (cur == null) {
                throw new ResultInfoException("消息编号未查询到相应的消息信息!");
            }
        }
        if (null==sysMessageSended.getCreateDate()){
            sysMessageSended.setCreateDate(new Date());
        }
        // 设置消息信息的状态
        if (null==sysMessageSended.getIsDelete()) {
            sysMessageSended.setIsDelete(BaseStaticParameter.N);
        }
        DbSysMessageSended dbSysMessageSended = new DbSysMessageSended();
        BeanUtils.copyProperties(sysMessageSended,dbSysMessageSended);
        if (null == sysMessageSended.getId()) {
            return dbSysMessageSendedMapper.insert(dbSysMessageSended);
            //return dbSysMessageMapper.insertSelective(dbSysMessage);
        }else {
            return dbSysMessageSendedMapper.updateByPrimaryKeySelective(dbSysMessageSended);
        }
    }

    /**
     * @description:  逻辑删除信息
     * @author: wuxx
     * @Date: 2020/10/23 14:23
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysMessageSendedById(Long oid) {
        return dbSysMessageSendedMapper.deleteByPrimaryKey(oid);
    }

    /**
     * @description:  根据消息Id获取消息信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/23 13:38
     **/
    @Cacheable(key = "'getSysMessageSendedById:'+#id", unless = "#result == null")
    public SysMessageSended getSysMessageSendedById(Long id) {
        DbSysMessageSended dbSysMessage = dbSysMessageSendedMapper.selectByPrimaryKey(id);
        if(dbSysMessage == null)
            throw new ResultInfoException("消息信息为空！");
        SysMessageSended sysMessageSended = new SysMessageSended();
        BeanUtils.copyProperties(dbSysMessage,sysMessageSended);
        return sysMessageSended;
    }

    /**
     * 分页查询已发送消息信息列表
     * @param title     标题
     * @param userName     接受人
     * @param sendUserOid     发送人oid
     * @param beginDateStr  开始时间
     * @param endDateStr 接受时间
     * @param pageNumber 开始页
     * @param pageSize   每页大小
     * @return
     */
    public PageResult<SysMessageSended> querySysMessageSendedPage(String title,String userName,String sendUserOid, String beginDateStr, String endDateStr, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysMessageSendedExample dbSysMessageExample = new DbSysMessageSendedExample();
        DbSysMessageSendedExample.Criteria criteria = dbSysMessageExample.createCriteria();
        criteria.andSendUserOidEqualTo(sendUserOid);
        if(StrUtil.isNotEmpty(title)){
            criteria.andTitleLike("%"+title.trim()+"%");
        }
        if(StrUtil.isNotEmpty(userName)){
            criteria.andUserNameLike("%"+userName.trim()+"%");
        }
        try {
            if(StrUtil.isNotEmpty(beginDateStr)){
                Date beginDate = DateUtil.parse(beginDateStr);
                Date beginOfDay = DateUtil.beginOfDay(beginDate);
                criteria.andCreateDateGreaterThanOrEqualTo(beginOfDay);
            }
            if(StrUtil.isNotEmpty(endDateStr)){
                Date endDate = DateUtil.parse(endDateStr);
                Date endOfDay = DateUtil.endOfDay(endDate);
                criteria.andCreateDateLessThanOrEqualTo(endOfDay);
            }
        }catch (Exception e){

        }
        dbSysMessageExample.setOrderByClause("CREATE_DATE DESC ");
        Page<DbSysMessageSended> dbSysMessages = (Page<DbSysMessageSended>)dbSysMessageSendedMapper.selectByExample(dbSysMessageExample);
        PageResult<SysMessageSended> pageResult = new PageResult<>(dbSysMessages.getPageNum(),dbSysMessages.getPageSize(),dbSysMessages.getTotal());
        List<SysMessageSended> sysMessageList = dbSysMessages.stream().map(dbSysMessage -> {
            SysMessageSended app = new SysMessageSended();
            BeanUtils.copyProperties(dbSysMessage,app);
            return app;
        }).collect(Collectors.toList());
        pageResult.setData(sysMessageList);
        return pageResult;
    }

}

package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysMessage;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysMessageMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysMessageSendedMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessage;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageExample;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageSended;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @ClassName SysMessageManager
 * @Description: 消息接口实现类
 * @Author wuxx
 * @Date 2020/10/23
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:message")
public class SysMessageManager {

    @Resource
    private DbSysMessageMapper dbSysMessageMapper;
    //@Resource
    //private DbSysUserMapper dbSysUserMapper;
    @Resource
    private DbSysMessageSendedMapper dbSysMessageSendedMapper;
    @Autowired
    private SysUserManager sysUserManager;
    /**
     * 增加一个新消息
     * @param sysMessage 新消息
     * @return
     */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int sendSysMessage(SysMessage sysMessage,String loginUserOid) {
        if (sysMessage == null) {
            throw new ResultInfoException("消息信息不正确!");
        }
        if (null == sysMessage.getId()) {
            sysMessage.setId(null);
        } else {
            // 消息oid不为空
            SysMessage cur = getSysMessageById(sysMessage.getId());
            if (cur == null) {
                throw new ResultInfoException("消息编号未查询到相应的消息信息!");
            }
        }
        if (null==sysMessage.getCreateDate()){
            sysMessage.setCreateDate(new Date());
        }
        if (null==sysMessage.getReadStatus()){
            sysMessage.setReadStatus(BaseStaticParameter.N);
        }
        // 设置消息信息的状态
        if (null==sysMessage.getIsDelete()) {
            sysMessage.setIsDelete(BaseStaticParameter.N);
        }
        // 接收人Oid
        List<String> userOidList = sysMessage.getUserOids();
        for(String userOid:userOidList){
            sysMessage.setUserOid(userOid);
            DbSysMessage dbSysMessage = new DbSysMessage();
            BeanUtils.copyProperties(sysMessage,dbSysMessage);
            dbSysMessageMapper.insert(dbSysMessage);

            DbSysMessageSended dbSysMessageSended = new DbSysMessageSended();
            BeanUtils.copyProperties(sysMessage,dbSysMessageSended);
            dbSysMessageSended.setSendUserOid(loginUserOid);
            //DbSysUser sysUser = dbSysUserMapper.selectByUserOid(userOid);
            SysUser sysUser = sysUserManager.getSysUserByUserOid(userOid);
            dbSysMessageSended.setUserName(null!=sysUser?sysUser.getName():null);
            dbSysMessageSendedMapper.insert(dbSysMessageSended);
        }
        return 1;
    }

    /**
     * @description:  查看消息信息更新对应的查看状态
     * @author: wuxx
     * @Date: 2020/10/23 14:23
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int readSysMessage(Long id) {
        DbSysMessage dbSysMessage = dbSysMessageMapper.selectByPrimaryKey(id);
        if(dbSysMessage == null)
            throw new ResultInfoException("消息信息为空！");
        if(BaseStaticParameter.Y==dbSysMessage.getReadStatus()){
            return 0;
        }
        dbSysMessage.setReadDate(new Date());
        dbSysMessage.setReadStatus(BaseStaticParameter.Y);
        return dbSysMessageMapper.updateByPrimaryKeySelective(dbSysMessage);
    }

    /**
     * @description:  逻辑删除信息
     * @author: wuxx
     * @Date: 2020/10/23 14:23
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysMessageById(Long oid) {
        return dbSysMessageMapper.deleteByOid(oid);
    }

    /**
     * @description:  根据消息Id获取消息信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/23 13:38
     **/
    @Cacheable(key = "'getSysMessageById:'+#id", unless = "#result == null")
    public SysMessage getSysMessageById(Long id) {
        DbSysMessage dbSysMessage = dbSysMessageMapper.selectByPrimaryKey(id);
        if(dbSysMessage == null)
            throw new ResultInfoException("消息信息为空！");
        SysMessage sysMessage = new SysMessage();
        BeanUtils.copyProperties(dbSysMessage,sysMessage);
        return sysMessage;
    }

    /**
     * @description:  分页查询消息信息列表
     * @author: wuxx
     * @Date: 2020/10/23 13:38
     **/
    public PageResult<SysMessage> querySysMessageWithPage(String title,Integer readStatus,String userOid, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysMessageExample dbSysMessageExample = new DbSysMessageExample();
        DbSysMessageExample.Criteria criteria = dbSysMessageExample.createCriteria();
        if(StrUtil.isNotEmpty(title)){
            criteria.andTitleLike("%"+title.trim()+"%");
        }
        if(null!= readStatus){
            criteria.andReadStatusEqualTo(readStatus);
        }
        if(null!= userOid){
            criteria.andUserOidEqualTo(userOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysMessageExample.setOrderByClause("ID DESC ");
        Page<DbSysMessage> dbSysMessages = (Page<DbSysMessage>)dbSysMessageMapper.selectByExample(dbSysMessageExample);
        PageResult<SysMessage> pageResult = new PageResult<>(dbSysMessages.getPageNum(),dbSysMessages.getPageSize(),dbSysMessages.getTotal());
        List<SysMessage> sysMessageList = dbSysMessages.stream().map(dbSysMessage -> {
            SysMessage app = new SysMessage();
            BeanUtils.copyProperties(dbSysMessage,app);
            return app;
        }).collect(Collectors.toList());
        pageResult.setData(sysMessageList);
        return pageResult;
    }

    /**
     * @description:  分页查询消息信息列表--原生得查询
     * @author: wuxx
     * @Date: 2020/11/16 13:38
     **/
    public PageResult<DbSysMessage> queryDbSysMessageWithPage(String title,Integer readStatus,String userOid, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysMessageExample dbSysMessageExample = new DbSysMessageExample();
        DbSysMessageExample.Criteria criteria = dbSysMessageExample.createCriteria();
        if(StrUtil.isNotEmpty(title)){
            criteria.andTitleLike(title.trim()+"%");
        }
        if(null!= readStatus){
            criteria.andReadStatusEqualTo(readStatus);
        }
        if(null!= userOid){
            criteria.andUserOidEqualTo(userOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        //dbSysMessageExample.setOrderByClause("ID DESC ");
        Page<DbSysMessage> dbSysMessages = (Page<DbSysMessage>)dbSysMessageMapper.selectByExample(dbSysMessageExample);
        PageResult<DbSysMessage> pageResult = new PageResult<>(dbSysMessages.getPageNum(),dbSysMessages.getPageSize(),dbSysMessages.getTotal());
        pageResult.setData(dbSysMessages);
        return pageResult;
    }

    /**
     * @description:  根据用户oid获取未读消息
     * @author: wuxx
     * @Date: 2020/10/28 13:38
     **/
    public int getCountOfUnReadMessagebyUserOid(String userOid) {
        if(userOid == null)
            return 0;
        DbSysMessageExample dbSysMessageExample = new DbSysMessageExample();
        DbSysMessageExample.Criteria criteria = dbSysMessageExample.createCriteria();
        criteria.andUserOidEqualTo(userOid);
        criteria.andReadStatusEqualTo(BaseStaticParameter.N);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        int indexNum = dbSysMessageMapper.countByExample(dbSysMessageExample);
        return indexNum;
    }
}

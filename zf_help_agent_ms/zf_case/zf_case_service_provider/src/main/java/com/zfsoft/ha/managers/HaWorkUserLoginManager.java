package com.zfsoft.ha.managers;

import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.dbaccess.dao.DbHaOnlineMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaOnline;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaOnlineExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.util.IpUtil;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description //登录接口数据层实现
 * @Author: Wangyh
 * @Date: 2022/7/18 10:22
 */
@Service
@Slf4j
public class HaWorkUserLoginManager {
    /**
     * 登录接口db实现层
     */
    @Resource
    private DbHaWorkUserMapper DbHaWorkUserMapper;

    /**
     * 用户在线时长接口db实现层
     */
    @Resource
    private DbHaOnlineMapper dbHaOnlineMapper;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    @Lazy
    @Autowired
    private HaWorkQueueManager haWorkQueueManager;

    /**
     * 根据账户查询用户信息
     *
     * @param account
     * @return HaWorkUser 获取用户详细信息
     * @author: wangyh
     * @Date: 2022年7月25日13:04:17
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public DbHaWorkUserVo loginByAccounAndId(String account, Long workUserId) throws ServiceException {
        DbHaWorkUserVo dbHaWorkUserVo = null;
        //根据账户和用户id
        dbHaWorkUserVo = DbHaWorkUserMapper.queryloginByAccountAndId(account,workUserId);
        return dbHaWorkUserVo;
    }


    /**
     * 登录成功后 并记录到用户在线时长表
     *
     * @author: wangyh
     * @Date: 2022年7月25日13:04:17
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void insertOnline(HttpServletRequest request, DbHaWorkUserVo dbHaWorkUserVo, String token, String haType) throws ServiceException {
//        DbHaWorkUser dbHaWorkUser = new DbHaWorkUser();
        DbHaWorkUser dbHaWorkUser = DbHaWorkUserMapper.queryById(dbHaWorkUserVo.getId());
//        dbHaWorkUser.setId(dbHaWorkUserVo.getId());
        //登录后，将人员的状态修改为空闲
//        dbHaWorkUser.setStatus("3");
        dbHaWorkUser.setUpdateBy(dbHaWorkUserVo.getName());
        dbHaWorkUser.setUpdateDate(new Date());
        //不能设置hatype 会出现登录后hatype置空的现象
//        dbHaWorkUser.setHaType(haType);
        //根据当天工作人员的服务人数，每次登录时更新，避免出现昨天前台的办件没有结束服务而用户的服务人数不统一的问题
        dbHaWorkUser.setCurrentServiceNum(dbHaWorkQueueMapper.countWorkQueueByServiceStatus(dbHaWorkUserVo.getId(), Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay()));
        DbHaWorkUserMapper.update(dbHaWorkUser);
        //根据用户当前服务人数，更新用户状态
        haWorkQueueManager.updateServiceWorkStatus(dbHaWorkUserVo.getId());
        //登录成功后 并记录到用户在线时长表
        DbHaOnline dbHaOnline = new DbHaOnline();
        dbHaOnline.setWorkUserId(dbHaWorkUserVo.getId());
        //dbHaOnline表新增一个登录类型字段
        dbHaOnline.setLoginType(dbHaWorkUserVo.getHaType());
        dbHaOnline.setLoginTime(new Date());
        dbHaOnline.setLogoutTime(new Date());
        dbHaOnline.setOnlineTime(0L);
        dbHaOnline.setLogoutType("3");
        dbHaOnline.setLoginToken(token);
        //获取用户ip地址
        String ip = IpUtil.getIpAddress(request);
        dbHaOnline.setLoginIp(ip);
        dbHaOnline.setCreateBy(dbHaWorkUserVo.getName());
        dbHaOnline.setCreateDate(new Date());
        dbHaOnlineMapper.insert(dbHaOnline);
    }

    /**
     * 退出登录 并记录到用户在线时长表
     *
     * @author: wangyh
     * @Date: 2022年7月25日13:04:17
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void logout(String token, String name) throws ServiceException {
        DbHaOnline dbHaOnline = dbHaOnlineMapper.selectOnlineByToken(token);
        dbHaOnline.setLogoutTime(new Date());
        int onlineTime = DateUtil.calLastedTime(dbHaOnline.getLoginTime(), dbHaOnline.getLogoutTime());
        dbHaOnline.setOnlineTime((long) onlineTime);
        dbHaOnline.setLogoutType("1");
        dbHaOnline.setUpdateBy(name);
        dbHaOnline.setUpdateDate(new Date());
        DbHaOnlineExample dbHaOnlineExample = new DbHaOnlineExample();
        DbHaOnlineExample.Criteria criteria = dbHaOnlineExample.createCriteria();
        criteria.andIdEqualTo(dbHaOnline.getId());
        int index = dbHaOnlineMapper.updateByExampleSelective(dbHaOnline, dbHaOnlineExample);
        redisTemplate.delete(dbHaOnline.getLoginToken());
    }

    /**
     * @param // token  在header中
     * @description: 在线状态
     * @author: wangyh
     * @Date: 2022-08-2 10:50
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void online(String token, String name) throws ServiceException {
        DbHaOnline dbHaOnline = dbHaOnlineMapper.selectOnlineByToken(token);
        dbHaOnline.setLogoutTime(new Date()); //退出时间
        int onlineTime = DateUtil.calLastedTime(dbHaOnline.getLoginTime(), dbHaOnline.getLogoutTime());
        dbHaOnline.setOnlineTime((long) onlineTime); //在线时长
        dbHaOnline.setLogoutType("2");//退出类型  为2异常退出
        dbHaOnline.setUpdateBy(name); //修改人
        dbHaOnline.setUpdateDate(new Date());//修改时间
        DbHaOnlineExample dbHaOnlineExample = new DbHaOnlineExample();
        DbHaOnlineExample.Criteria criteria = dbHaOnlineExample.createCriteria();
        criteria.andIdEqualTo(dbHaOnline.getId());
        int index = dbHaOnlineMapper.updateByExampleSelective(dbHaOnline, dbHaOnlineExample);
    }


}

package com.zfsoft.ha.managers;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaMessage;
import com.zfsoft.ha.data.vo.HaMessageVo;
import com.zfsoft.ha.dbaccess.dao.DbHaMessageMapper;
import com.zfsoft.ha.dbaccess.data.DbHaMessage;
import com.zfsoft.ha.dbaccess.data.example.DbHaMessageExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaMessageVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description //消息接口实现
 * @Author: Wangyh
 * @Date: 2022/7/19 11:05
 */
@Service
@Slf4j
public class HaMessageManager {


    /**
     * 消息db层实现
     */
    @Resource
    private DbHaMessageMapper dbHaMessageMapper;

    /**
     * 获取未读消息的数量
     *
     * @return noReadNum 获取当前登录用户的未读消息数量
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public long getNoReadNum(HaLoginUserInfo haLoginUserInfo) throws ServiceException {
        DbHaMessageExample dbHaMessageExample = new DbHaMessageExample();
        DbHaMessageExample.Criteria criteria = dbHaMessageExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
        criteria.andReadStatusEqualTo("1"); //1-未读取，2-已读取
        criteria.andReceiveUserIdEqualTo(haLoginUserInfo.getId());//接收人
        long noReadNum = dbHaMessageMapper.countByExample(dbHaMessageExample);
        return noReadNum;
    }

    /**
     * 获取消息列表
     *
     * @param title     消息标题，模糊查询
     * @param beginDate 消息开始时间
     * @param endDate   消息结束时间
     * @return noReadNum 获取当前登录用户的消息列表
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaMessageVo> queryMessageList(HaLoginUserInfo haLoginUserInfo, String title, String beginDate, String endDate) throws ServiceException {
        DbHaMessageExample dbHaMessageExample = new DbHaMessageExample();
        DbHaMessageExample.Criteria criteria = dbHaMessageExample.createCriteria();
        criteria.andReceiveUserIdEqualTo(haLoginUserInfo.getId());
        if (StringUtils.isNotEmpty(title)) { //判断消息标题是否为空，不为空模糊查询
            String value = "%" + title + "%";
            criteria.andTitleLike(value);
        }
        if (StringUtils.isNotEmpty(beginDate)) { //判断时间是否为空
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
            Date date = ft.parse(beginDate);
            criteria.andCreateDateGreaterThanOrEqualTo(date);
        }
        if (StringUtils.isNotEmpty(endDate)) { //判断endDate时间是否为空
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
            Date date = ft.parse(endDate);
            criteria.andUpdateDateLessThanOrEqualTo(date);
        }
        criteria.andDeleteStatusEqualTo("1");//1-未删除，2-已删除
        dbHaMessageExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaMessage> thaMessageList = dbHaMessageMapper.selectByExample(dbHaMessageExample);
        //循环遍历将HaMessage对象集合复制到HaMessageVo集合中
        List<HaMessageVo> messageList = thaMessageList.stream().map(dbthaMessageService -> {
            HaMessageVo haMessageVo = new HaMessageVo();
            BeanUtils.copyProperties(dbthaMessageService, haMessageVo);
            return haMessageVo;
        }).collect(Collectors.toList());
        return messageList;
    }

    /**
     * 获取消息详细信息
     *
     * @param id
     * @return 获取当前登录用户的消息详细信息
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaMessageVo getMessageInfo(Long id) throws ServiceException {
        HaMessageVo haMessageVo = new HaMessageVo();
        DbHaMessage dbHaMessage = dbHaMessageMapper.selectByid(id);
        if (dbHaMessage != null) {
            BeanUtils.copyProperties(dbHaMessage, haMessageVo);
        } else {
            return null;
        }
        return haMessageVo;
    }

    /**
     * 批量删除消息
     *
     * @param ids
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void batchMessageid(String ids) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        List<String> stringList = Arrays.asList(ids.split(","));
        //将String类型的stringList集合转成Long类型的mesids集合
        List<Long> mesids = stringList.stream().map(Long::valueOf).collect(Collectors.toList());
        if (mesids.size() > 0) {
            for (Long messageid : mesids) {//循环遍历删除
                DbHaMessage dbHaMessage = dbHaMessageMapper.selectByid(Long.valueOf(messageid));
                if (dbHaMessage != null) {
                    dbHaMessage.setDeleteStatus("2");
                    dbHaMessage.setUpdateBy(haLoginUserInfo.getName());
                    dbHaMessage.setUpdateDate(new Date());
                    dbHaMessageMapper.update(dbHaMessage);
                }
            }
        }
    }


    /**
     * 查询消息列表
     *
     * @param receiver 消息发送者
     * @param sender   消息接收者
     * @param title    消息标题
     * @param pageNum  分页参数
     * @param pageSize 分页参数
     * @return 返回消息列表
     * @author yupeng
     * @date 2022年08月12 15:09:50
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaMessage> queryMessagePage(String receiver, String sender, String title, Integer pageNum, Integer pageSize) {

        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }

        PageHelper.startPage(pageNum, pageSize);

        Page<DbHaMessageVo> dbHaMessagePage = (Page<DbHaMessageVo>) dbHaMessageMapper.queryMessageList(receiver, sender, title);

        PageResult<HaMessage> pageResult = new PageResult<>(dbHaMessagePage.getPageNum(), dbHaMessagePage.getPageSize(), dbHaMessagePage.getTotal());

        List<HaMessage> data = dbHaMessagePage.stream().map(dbHaMessageVo -> {
            HaMessage message  = new HaMessage();
            BeanUtils.copyProperties(dbHaMessageVo,message);
            return  message;
        }).collect(Collectors.toList());

        pageResult.setData(data);

        return pageResult;

    }
}

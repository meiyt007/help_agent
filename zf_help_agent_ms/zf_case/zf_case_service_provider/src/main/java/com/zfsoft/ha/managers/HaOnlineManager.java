package com.zfsoft.ha.managers;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.BaseStaticParameter;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaOnline;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.SysDistrict;
import com.zfsoft.ha.data.requestData.HaOnlineRequestData;
import com.zfsoft.ha.data.requestData.HaWorkUserRequestData;
import com.zfsoft.ha.dbaccess.dao.DbHaOnlineMapper;
import com.zfsoft.ha.dbaccess.data.DbHaOnline;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaOnlineExample;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserExample;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description //在线时长表实现层
 * @Author: Wangyh
 * @Date: 2022/8/12 11:17
 */
@Service
@Slf4j
public class HaOnlineManager {
    /**
     * 在线时长表db层接口
     */
    @Resource
    private DbHaOnlineMapper dbHaOnlineMapper;

    /**
     * @param haOnlineRequestData
     * @param pageNumber
     * @param pageSize
     * @return
     * @description: 查询用户分页信息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaOnline> queryOnlineServiceWithPage(HaOnlineRequestData haOnlineRequestData, Integer pageNumber, Integer pageSize) throws ServiceException, ParseException {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbHaOnlineExample onlineExample = new DbHaOnlineExample();
        DbHaOnlineExample.Criteria criteria = onlineExample.createCriteria();
        criteria.andWorkUserIdEqualTo(haOnlineRequestData.getWorkUserId());
        if (StringUtils.isNotEmpty(haOnlineRequestData.getLoginTime())) { //大于等于登录时间
            criteria.andLoginTimeGreaterThanOrEqualTo(DateUtil.str2Date(haOnlineRequestData.getLoginTime()));
        }
        if (StringUtils.isNotEmpty(haOnlineRequestData.getLogoutTime())) { //小于登录登出时间
            criteria.andLogoutTimeLessThanOrEqualTo(DateUtil.str2Date(haOnlineRequestData.getLogoutTime()));
        }
        if(StringUtils.isNotEmpty(haOnlineRequestData.getLogoutType())){
            criteria.andLogoutTypeEqualTo(haOnlineRequestData.getLogoutType());
        }
        onlineExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaOnline> dbUserServices = (Page<DbHaOnline>) dbHaOnlineMapper.selectByExample(onlineExample);
        PageResult<HaOnline> pageResult = new PageResult<>(dbUserServices.getPageNum(), dbUserServices.getPageSize(), dbUserServices.getTotal());
        List<HaOnline> workUserList = dbUserServices.stream().map(dbUserServic -> {
            HaOnline haOnline = new HaOnline();
            BeanUtils.copyProperties(dbUserServic, haOnline);
            haOnline.setOnlineTime(dbUserServic.getOnlineTime()+"s");
            if(haOnline.getLogoutType().equals("1")){
                haOnline.setLogoutType("手动退出");
            }else if(haOnline.getLogoutType().equals("2")){
                haOnline.setLogoutType("异常退出");
            }
            else if(haOnline.getLogoutType().equals("3")){
                haOnline.setLogoutType("登录");
            }
            return haOnline;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }
}

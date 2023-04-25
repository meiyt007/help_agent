package com.zfsoft.ha.managers;

import cn.hutool.core.bean.BeanUtil;
import com.zfsoft.ha.data.HaCaseExpress;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.dbaccess.dao.DbHaCaseExpressMapper;
import com.zfsoft.ha.dbaccess.data.DbHaCaseExpress;
import com.zfsoft.ha.dbaccess.data.example.DbHaCaseExpressExample;
import com.zfsoft.ha.util.HaDockingHolder;
import com.zfsoft.ha.util.HaLoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/4/7 15:12
 */
@Service
@Slf4j
public class HaCaseExpressManager {

    @Resource
    DbHaCaseExpressMapper dbHaCaseExpressMapper;

    /**
     * @param haCaseExpress 政策库实体类
     * @return ApiResultSet 获取新增或者修改政策库信息标识
     * @description: 新增或者修改政策库信息
     * @author: zhaobf
     * @Date: 2023/3/20
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateCaseExpress(HaCaseExpress haCaseExpress) {
        Map<String, Object> map = new HashMap();
        //获取当前登录用户信息
        HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        String currentUser = HaDockingHolder.getCurrentUser();
        String name = loginUser!=null ? loginUser.getName() :currentUser;
        int index = 0;
        //判断是修改还是新增
        if (haCaseExpress.getId() != null) {
            //修改
            DbHaCaseExpress dbHaCaseExpress = dbHaCaseExpressMapper.selectByPrimaryKey(haCaseExpress.getId());
            BeanUtils.copyProperties(haCaseExpress, dbHaCaseExpress);
            dbHaCaseExpress.setUpdateBy(name); //获取当前登录用户名称
            dbHaCaseExpress.setUpdateDate(new Date()); //获取当前时间
            DbHaCaseExpressExample dbHaCaseExpressExample = new DbHaCaseExpressExample();
            DbHaCaseExpressExample.Criteria criteria = dbHaCaseExpressExample.createCriteria();
            criteria.andIdEqualTo(haCaseExpress.getId());
            int i = dbHaCaseExpressMapper.updateByExampleSelective(dbHaCaseExpress, dbHaCaseExpressExample);
            map.put("index", index);
        } else {
            //新增
            DbHaCaseExpress dbHaWorkGroup = new DbHaCaseExpress();
            BeanUtils.copyProperties(haCaseExpress, dbHaWorkGroup);
            dbHaWorkGroup.setCreateBy(name);
            dbHaWorkGroup.setCreateDate(new Date());
            dbHaWorkGroup.setUpdateBy(name); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            index = dbHaCaseExpressMapper.insertSelective(dbHaWorkGroup);
            map.put("index", index);
        }
        return map;
    }

    public HaCaseExpress selectByQlCaseId(String qlCaseId) {
        HaCaseExpress haCaseExpress = new HaCaseExpress();
        DbHaCaseExpress dbHaCaseExpress = dbHaCaseExpressMapper.selectByQlCaseId(qlCaseId);
//        if(dbHaCaseExpress==null)  return haCaseExpress;
        BeanUtil.copyProperties(dbHaCaseExpress, haCaseExpress);
        return haCaseExpress;
    }
}

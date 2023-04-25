package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaWorkLoginLocation;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkLoginLocationMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkLoginLocation;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkLoginLocationExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * //帮代办人员分组(组内划分）表实现层
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/7 13:40
 */
@Service
@Slf4j
public class HaWorkLoginLocationServiceManager {
    @Resource
    private DbHaWorkLoginLocationMapper dbHaWorkLoginLocationMapper;
    /**
     * @param name         组名
     * @param deleteStatus 是否删除
     * @param pageNumber
     * @param pageSize
     * @return
     * @description: 查询用户分页信息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaWorkLoginLocation> queryWithPage(String name, String deleteStatus, Integer pageNumber, Integer pageSize) throws Exception {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);

        DbHaWorkLoginLocationExample ex = new DbHaWorkLoginLocationExample();
        DbHaWorkLoginLocationExample.Criteria criteria = ex.createCriteria();
        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (StrUtil.isNotEmpty(deleteStatus)) {
            criteria.andDeleteStatusEqualTo(deleteStatus);
        } else {
            criteria.andDeleteStatusEqualTo("1");
        }
        ex.setOrderByClause("SORT");
        Page<DbHaWorkLoginLocation> splits = (Page<DbHaWorkLoginLocation>) dbHaWorkLoginLocationMapper.selectByExample(ex);
        PageResult<HaWorkLoginLocation> pageResult = new PageResult<>(splits.getPageNum(), splits.getPageSize(), splits.getTotal());
        List<HaWorkLoginLocation> workUserList = splits.stream().map(dbGroupServic -> {
            HaWorkLoginLocation haWorkGroup = new HaWorkLoginLocation();
            BeanUtils.copyProperties(dbGroupServic, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }


    /**
     * @param id 主键
     * @return ApiResultSet 获取删除帮代办人员分组信息标识
     * @description: 删除帮代办人员分组信息
     * @author: wangyh
     * @Date: 2022/8/4
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deleteGroupid(Long id) throws Exception {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaWorkLoginLocation dbHaWorkGroup = dbHaWorkLoginLocationMapper.queryById(id);
        dbHaWorkGroup.setDeleteStatus("2");
        dbHaWorkGroup.setUpdateBy(loginUser.getUserName());
        dbHaWorkGroup.setUpdateDate(new Date());
        DbHaWorkLoginLocationExample dbHaWorkGroupExample = new DbHaWorkLoginLocationExample();
        DbHaWorkLoginLocationExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbHaWorkLoginLocationMapper.updateByExampleSelective(dbHaWorkGroup, dbHaWorkGroupExample);
        return index;
    }

    /**
     * @param haWorkGroup 帮代办人员分组实体类
     * @return ApiResultSet 获取新增或者修改帮代办人员分组信息标识
     * @description: 新增或者修改帮代办人员分组信息
     * @author: wangyh
     * @Date: 2022/8/4
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateHaWorkGroup(HaWorkLoginLocation haWorkGroup) throws Exception {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        int index = 0;
        //判断是修改还是新增
        if (haWorkGroup.getId() != null) {
            //修改
            DbHaWorkLoginLocation dbHaWorkGroup = dbHaWorkLoginLocationMapper.queryById(haWorkGroup.getId());
            BeanUtils.copyProperties(haWorkGroup, dbHaWorkGroup);
            dbHaWorkGroup.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            DbHaWorkLoginLocationExample dbHaWorkGroupExample = new DbHaWorkLoginLocationExample();
            DbHaWorkLoginLocationExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
            criteria.andIdEqualTo(haWorkGroup.getId());
            int i = dbHaWorkLoginLocationMapper.updateByExampleSelective(dbHaWorkGroup, dbHaWorkGroupExample);
            map.put("index", index);
        } else {
            //新增
            DbHaWorkLoginLocation dbHaWorkGroup = new DbHaWorkLoginLocation();
            BeanUtils.copyProperties(haWorkGroup, dbHaWorkGroup);
            dbHaWorkGroup.setDeleteStatus("1");
            dbHaWorkGroup.setCreateBy(loginUser.getUserName());
            dbHaWorkGroup.setCreateDate(new Date());
            dbHaWorkGroup.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            index = dbHaWorkLoginLocationMapper.insertSelective(dbHaWorkGroup);
            map.put("index", index);
        }
        return map;
    }

    /**
     * @param id
     * @return ApiResultSet 获取根据id查询帮代办人员分组信息
     * @description: 根据id查询帮代办人员分组信息
     * @author: wangyh
     * @Date: 2022/8/4
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkLoginLocation selectByid(Long id) throws Exception {
        HaWorkLoginLocation haWorkLoginLocation = null;
        DbHaWorkLoginLocation DbHaWorkLoginLocation = dbHaWorkLoginLocationMapper.queryById(id);
        if (DbHaWorkLoginLocation != null) {
            haWorkLoginLocation = new HaWorkLoginLocation();
            BeanUtils.copyProperties(DbHaWorkLoginLocation, haWorkLoginLocation);
        }
        return haWorkLoginLocation;
    }

    /**
     * @description: 根据区划oid查询区划列表
     * @author: wangyh
     * @Date: 2022/8/2 14:32
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkLoginLocation> haWorkGroupList() {
        DbHaWorkLoginLocationExample DbHaWorkLoginLocationExample = new DbHaWorkLoginLocationExample();
        DbHaWorkLoginLocationExample.Criteria criteria = DbHaWorkLoginLocationExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        DbHaWorkLoginLocationExample.setOrderByClause("SORT");
        List<DbHaWorkLoginLocation> dbGroupsList = dbHaWorkLoginLocationMapper.selectByExample(DbHaWorkLoginLocationExample);
        List<HaWorkLoginLocation> collect = dbGroupsList.stream().map(groupService -> {
            HaWorkLoginLocation haWorkGroup = new HaWorkLoginLocation();
            BeanUtils.copyProperties(groupService, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        return collect;
    }
}

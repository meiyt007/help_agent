package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaWorkGroupSplit;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkGroupSplitMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkGroupSplit;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkGroupSplitExample;
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
public class HaWorkGroupSplitServiceManager {
    @Resource
    private DbHaWorkGroupSplitMapper dbHaWorkGroupSplitMapper;
    /**
     * @param name         组名
     * @param deleteStatus 是否删除
     * @param pageNumber
     * @param pageSize
     * @return
     * @description: 查询帮代办人员分组(组内划分）分页信息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaWorkGroupSplit> queryGroupServiceWithPage(String name,String groupId, String deleteStatus, Integer pageNumber, Integer pageSize) throws Exception {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);

        DbHaWorkGroupSplitExample splitExample = new DbHaWorkGroupSplitExample();
        DbHaWorkGroupSplitExample.Criteria criteria = splitExample.createCriteria();
        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (StrUtil.isNotEmpty(groupId)) {
            criteria.andGroupIdEqualTo(Long.valueOf(groupId));
        }
        if (StrUtil.isNotEmpty(deleteStatus)) {
            criteria.andDeleteStatusEqualTo(deleteStatus);
        } else {
            criteria.andDeleteStatusEqualTo("1");
        }
        splitExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaWorkGroupSplit> splits = (Page<DbHaWorkGroupSplit>) dbHaWorkGroupSplitMapper.selectByExample(splitExample);
        PageResult<HaWorkGroupSplit> pageResult = new PageResult<>(splits.getPageNum(), splits.getPageSize(), splits.getTotal());
        List<HaWorkGroupSplit> workUserList = splits.stream().map(dbGroupServic -> {
            HaWorkGroupSplit haWorkGroup = new HaWorkGroupSplit();
            BeanUtils.copyProperties(dbGroupServic, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }


    /**
     * @param id 主键
     * @return ApiResultSet 获取删除帮代办人员分组信息标识
     * @description: 删除帮代办人员分组(组内划分）信息
     * @author: zhaobf
     * @Date: 2023/3/4
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deleteGroupid(Long id) throws Exception {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaWorkGroupSplit dbHaWorkGroup = dbHaWorkGroupSplitMapper.queryById(id);
        dbHaWorkGroup.setDeleteStatus("2");
        dbHaWorkGroup.setUpdateBy(loginUser.getUserName());
        dbHaWorkGroup.setUpdateDate(new Date());
        DbHaWorkGroupSplitExample dbHaWorkGroupExample = new DbHaWorkGroupSplitExample();
        DbHaWorkGroupSplitExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbHaWorkGroupSplitMapper.updateByExampleSelective(dbHaWorkGroup, dbHaWorkGroupExample);
        return index;
    }

    /**
     * @param haWorkGroup 帮代办人员分组(组内划分）实体类
     * @return ApiResultSet 获取新增或者修改帮代办人员分组(组内划分）信息标识
     * @description: 新增或者修改帮代办人员分组(组内划分）信息
     * @author: zhaobf
     * @Date: 2023/3/4
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateHaWorkGroup(HaWorkGroupSplit haWorkGroup) throws Exception {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        int index = 0;
        //判断是修改还是新增
        if (haWorkGroup.getId() != null) {
            //修改
            DbHaWorkGroupSplit dbHaWorkGroup = dbHaWorkGroupSplitMapper.queryById(haWorkGroup.getId());
            BeanUtils.copyProperties(haWorkGroup, dbHaWorkGroup);
            dbHaWorkGroup.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            DbHaWorkGroupSplitExample dbHaWorkGroupExample = new DbHaWorkGroupSplitExample();
            DbHaWorkGroupSplitExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
            criteria.andIdEqualTo(haWorkGroup.getId());
            int i = dbHaWorkGroupSplitMapper.updateByExampleSelective(dbHaWorkGroup, dbHaWorkGroupExample);
            map.put("index", index);
        } else {
            //新增
            DbHaWorkGroupSplit dbHaWorkGroup = new DbHaWorkGroupSplit();
            BeanUtils.copyProperties(haWorkGroup, dbHaWorkGroup);
            dbHaWorkGroup.setDeleteStatus("1");
            dbHaWorkGroup.setCreateBy(loginUser.getUserName());
            dbHaWorkGroup.setCreateDate(new Date());
            dbHaWorkGroup.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            index = dbHaWorkGroupSplitMapper.insertSelective(dbHaWorkGroup);
            map.put("index", index);
        }
        return map;
    }

    /**
     * @param id
     * @return ApiResultSet 获取根据id查询帮代办人员分组(组内划分）信息
     * @description: 根据id查询帮代办人员分组(组内划分）信息
     * @author: zhaobf
     * @Date: 2023/3/4
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkGroupSplit selectByid(Long id) throws Exception {
        HaWorkGroupSplit haWorkGroupSplit = null;
        DbHaWorkGroupSplit dbHaWorkGroupSplit = dbHaWorkGroupSplitMapper.queryById(id);
        if (dbHaWorkGroupSplit != null) {
            haWorkGroupSplit = new HaWorkGroupSplit();
            BeanUtils.copyProperties(dbHaWorkGroupSplit, haWorkGroupSplit);
        }
        return haWorkGroupSplit;
    }

    /**
     * @description: 根据分组id获取小组列表
     * @author: zhaobf
     * @Date: 2023/3/2 14:32
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkGroupSplit> haWorkGroupList(String groupId) {
        DbHaWorkGroupSplitExample dbHaWorkGroupSplitExample = new DbHaWorkGroupSplitExample();
        DbHaWorkGroupSplitExample.Criteria criteria = dbHaWorkGroupSplitExample.createCriteria();
        if(StringUtil.isNotEmpty(groupId)){
            criteria.andGroupIdEqualTo(Long.valueOf(groupId));
        }
        criteria.andDeleteStatusEqualTo("1");
        dbHaWorkGroupSplitExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkGroupSplit> dbGroupsList = dbHaWorkGroupSplitMapper.selectByExample(dbHaWorkGroupSplitExample);
        List<HaWorkGroupSplit> haWorkGroups = dbGroupsList.stream().map(groupService -> {
            HaWorkGroupSplit haWorkGroup = new HaWorkGroupSplit();
            BeanUtils.copyProperties(groupService, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        return haWorkGroups;
    }
}

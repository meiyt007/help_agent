package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaPolicyBase;
import com.zfsoft.ha.dbaccess.dao.DbHaPolicyBaseMapper;
import com.zfsoft.ha.dbaccess.data.DbHaPolicyBase;
import com.zfsoft.ha.dbaccess.data.example.DbHaPolicyBaseExample;
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
public class HaPolicyBaseManager {
    @Resource
    private DbHaPolicyBaseMapper dbHaPolicyBaseMapper;

    /**
    * Description: 查询帮政策库分页信息列表
    * @param name 名称（模糊查询）
    * @param title 主题
    * @param organId 区划id* @param pageNumber 分页参数，页码
    * @param pageSize 页记录数
    * @author zhaobf
    * date: 2023/3/20 13:23
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaPolicyBase> queryPolicyBaseWithPage(String name,String title,String organId, Integer pageNumber, Integer pageSize) throws Exception {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);

        DbHaPolicyBaseExample splitExample = new DbHaPolicyBaseExample();
        DbHaPolicyBaseExample.Criteria criteria = splitExample.createCriteria();
        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (StrUtil.isNotEmpty(title)) {
            criteria.andTitleEqualTo(title);
        }
        if (StrUtil.isNotEmpty(organId)) {
            criteria.andOrganIdEqualTo(organId);
        } else {
            criteria.andDeleteStatusEqualTo("1");
        }
        splitExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaPolicyBase> splits = (Page<DbHaPolicyBase>) dbHaPolicyBaseMapper.selectByExample(splitExample);
        PageResult<HaPolicyBase> pageResult = new PageResult<>(splits.getPageNum(), splits.getPageSize(), splits.getTotal());
        List<HaPolicyBase> workUserList = splits.stream().map(dbGroupServic -> {
            HaPolicyBase haWorkGroup = new HaPolicyBase();
            BeanUtils.copyProperties(dbGroupServic, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }


    /**
     * @param id 主键
     * @return ApiResultSet
     * @description: 删除政策库信息
     * @author: zhaobf
     * @Date: 2023/3/20
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deletePolicyById(Long id) throws Exception {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaPolicyBase dbHaPolicyBase = dbHaPolicyBaseMapper.queryById(id);
        dbHaPolicyBase.setDeleteStatus("2");
        dbHaPolicyBase.setUpdateBy(loginUser.getUserName());
        dbHaPolicyBase.setUpdateDate(new Date());
        DbHaPolicyBaseExample dbHaWorkGroupExample = new DbHaPolicyBaseExample();
        DbHaPolicyBaseExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbHaPolicyBaseMapper.updateByExampleSelective(dbHaPolicyBase, dbHaWorkGroupExample);
        return index;
    }

    /**
     * @param haPolicyBase 政策库实体类
     * @return ApiResultSet 获取新增或者修改政策库信息标识
     * @description: 新增或者修改政策库信息
     * @author: zhaobf
     * @Date: 2023/3/20
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdatePolicyBase(HaPolicyBase haPolicyBase) throws Exception {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        int index = 0;
        //判断是修改还是新增
        if (haPolicyBase.getId() != null) {
            //修改
            DbHaPolicyBase dbHaPolicyBase = dbHaPolicyBaseMapper.queryById(haPolicyBase.getId());
            BeanUtils.copyProperties(haPolicyBase, dbHaPolicyBase);
            dbHaPolicyBase.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaPolicyBase.setUpdateDate(new Date()); //获取当前时间
            DbHaPolicyBaseExample dbHaPolicyBaseExample = new DbHaPolicyBaseExample();
            DbHaPolicyBaseExample.Criteria criteria = dbHaPolicyBaseExample.createCriteria();
            criteria.andIdEqualTo(haPolicyBase.getId());
            int i = dbHaPolicyBaseMapper.updateByExampleSelective(dbHaPolicyBase, dbHaPolicyBaseExample);
            map.put("index", index);
        } else {
            //新增
            DbHaPolicyBase dbHaWorkGroup = new DbHaPolicyBase();
            BeanUtils.copyProperties(haPolicyBase, dbHaWorkGroup);
            dbHaWorkGroup.setDeleteStatus("1");
            dbHaWorkGroup.setCreateBy(loginUser.getUserName());
            dbHaWorkGroup.setCreateDate(new Date());
            dbHaWorkGroup.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            index = dbHaPolicyBaseMapper.insertSelective(dbHaWorkGroup);
            map.put("index", index);
        }
        return map;
    }

    /**
     * @param id
     * @return ApiResultSet 获取根据id查询帮代办人员分组(组内划分）信息
     * @description: 根据id查询帮代办人员分组(组内划分）信息
     * @author: zhaobf
     * @Date: 2023/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaPolicyBase selectByid(Long id) throws Exception {
        HaPolicyBase HaPolicyBase = null;
        DbHaPolicyBase dbHaPolicyBase = dbHaPolicyBaseMapper.queryById(id);
        if (dbHaPolicyBase != null) {
            HaPolicyBase = new HaPolicyBase();
            BeanUtils.copyProperties(dbHaPolicyBase, HaPolicyBase);
        }
        return HaPolicyBase;
    }

}

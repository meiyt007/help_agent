package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaPlusProject;
import com.zfsoft.ha.dbaccess.dao.DbHaPlusProjectMapper;
import com.zfsoft.ha.dbaccess.data.DbHaPlusProject;
import com.zfsoft.ha.dbaccess.data.example.DbHaPlusProjectExample;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 加分项目信息管理实现层
 * @author dingsn
 * @date 2022/10/27  15:17
 */
@Service
@Slf4j
public class HaPlusProjectManager {
    /**
     * 加分项目信息db层接口掉用
     */
    @Resource
    private DbHaPlusProjectMapper dbHaPlusProjectMapper;

    /**
     * @description 查询加分项目维护管理信息分页列表
     * @param name
     * @param pageNumber
     * @param pageSize
     * @author: dingsn
     * @Date: 2022/10/27
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaPlusProject> queryPlusProjectServiceWithPage(String name, Integer pageNumber, Integer pageSize) throws Exception {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbHaPlusProjectExample dbHaPlusProjectExample = new DbHaPlusProjectExample();
        DbHaPlusProjectExample.Criteria criteria = dbHaPlusProjectExample.createCriteria();
        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeleteStatusEqualTo(Constants.DELETE_STATUS_NO);
        dbHaPlusProjectExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaPlusProject> dbHaPlusProjects = (Page<DbHaPlusProject>) dbHaPlusProjectMapper.selectByExample(dbHaPlusProjectExample);
        PageResult<HaPlusProject> pageResult = new PageResult<>(dbHaPlusProjects.getPageNum(), dbHaPlusProjects.getPageSize(), dbHaPlusProjects.getTotal());
        List<HaPlusProject> haPlusProjectList = dbHaPlusProjects.stream().map(dbHaPlusProject -> {
            HaPlusProject haPlusProject = new HaPlusProject();
            BeanUtils.copyProperties(dbHaPlusProject, haPlusProject);
            return haPlusProject;
        }).collect(Collectors.toList());
        pageResult.setData(haPlusProjectList);
        return pageResult;
    }

    /**
     * @description 新增或修改加分项目信息
     * @param haPlusProject
     * @author: dingsn
     * @return
     * @throws Exception
     * @Date: 2022/10/27
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public JSONObject saveOrUpdateHaPlusProject(HaPlusProject haPlusProject) throws Exception {
        JSONObject result = new JSONObject();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        int index = 0;
        //判断是修改还是新增
        if (haPlusProject.getId() != null) {
            //修改
            DbHaPlusProject dbHaPlusProject = dbHaPlusProjectMapper.selectByPrimaryKey(haPlusProject.getId());
            BeanUtils.copyProperties(haPlusProject, dbHaPlusProject);
            dbHaPlusProject.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaPlusProject.setUpdateDate(new Date()); //获取当前时间
            DbHaPlusProjectExample dbHaPlusProjectExample = new DbHaPlusProjectExample();
            DbHaPlusProjectExample.Criteria criteria = dbHaPlusProjectExample.createCriteria();
            criteria.andIdEqualTo(haPlusProject.getId());
            index = dbHaPlusProjectMapper.updateByExampleSelective(dbHaPlusProject, dbHaPlusProjectExample);
        } else {
            //新增
            DbHaPlusProject dbHaPlusProject = new DbHaPlusProject();
            BeanUtils.copyProperties(haPlusProject, dbHaPlusProject);
            dbHaPlusProject.setDeleteStatus(Constants.DELETE_STATUS_NO);
            dbHaPlusProject.setCreateBy(loginUser.getUserName());
            dbHaPlusProject.setCreateDate(new Date());
            index = dbHaPlusProjectMapper.insert(dbHaPlusProject);
        }
        result.put("index", index);
        return result;
    }

    /**
     * 根据id查询加分项目信息
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaPlusProject selectPlusProjectByid(Long id) throws Exception {
        DbHaPlusProject dbHaPlusProject = dbHaPlusProjectMapper.selectByPrimaryKey(id);
        if (dbHaPlusProject != null) {
            HaPlusProject haPlusProject = new HaPlusProject();
            BeanUtils.copyProperties(dbHaPlusProject, haPlusProject);
            return haPlusProject;
        }
        return null;
    }

    /**
     * 根据主键删除加分项目信息
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deletePlusProjectById(Long id) throws Exception {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaPlusProject dbHaPlusProject = dbHaPlusProjectMapper.selectByPrimaryKey(id);
        dbHaPlusProject.setDeleteStatus(Constants.DELETE_STATUS_YES);
        dbHaPlusProject.setUpdateBy(loginUser.getUserName());
        dbHaPlusProject.setUpdateDate(new Date());
        DbHaPlusProjectExample dbHaPlusProjectExample = new DbHaPlusProjectExample();
        DbHaPlusProjectExample.Criteria criteria = dbHaPlusProjectExample.createCriteria();
        criteria.andIdEqualTo(id);
        return dbHaPlusProjectMapper.updateByExampleSelective(dbHaPlusProject,dbHaPlusProjectExample);
    }

    /**
     * 查询加分项目列表信息
     * @return
     * @throws Exception
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaPlusProject> queryPlusProjectList() throws Exception {
        DbHaPlusProjectExample dbHaPlusProjectExample = new DbHaPlusProjectExample();
        DbHaPlusProjectExample.Criteria criteria = dbHaPlusProjectExample.createCriteria();
        criteria.andDeleteStatusEqualTo(Constants.DELETE_STATUS_NO);
        dbHaPlusProjectExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaPlusProject> dbHaPlusProjectList = dbHaPlusProjectMapper.selectByExample(dbHaPlusProjectExample);
        List<HaPlusProject> haPlusProjectList = dbHaPlusProjectList.stream().map(dbHaPlusProject -> {
            HaPlusProject haPlusProject = new HaPlusProject();
            BeanUtils.copyProperties(dbHaPlusProject, haPlusProject);
            return haPlusProject;
        }).collect(Collectors.toList());;
        return haPlusProjectList;
    }

    /**
     * 批量删除加分项目信息
     * @param ids
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void deletePlusProjectByIds(List<Long> ids) throws Exception {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        ids.forEach(id -> {
            DbHaPlusProject dbHaPlusProject = dbHaPlusProjectMapper.selectByPrimaryKey(id);
            if (dbHaPlusProject != null) {
                dbHaPlusProject.setDeleteStatus(Constants.DELETE_STATUS_YES);
                dbHaPlusProject.setUpdateBy(loginUser.getUserName());
                dbHaPlusProject.setUpdateDate(new Date());
                dbHaPlusProjectMapper.updateByPrimaryKey(dbHaPlusProject);
            }
        });
    }
}

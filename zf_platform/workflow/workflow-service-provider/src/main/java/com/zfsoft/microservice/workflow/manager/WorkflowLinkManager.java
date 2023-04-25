package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowLinkMapper;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowTypeMapper;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowLink;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowLinkExample;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowType;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WorkflowLinkManager
 * @Description: 流程环节类型操作link接口实现类
 * @Author wuxx
 * @Date 2020/9/7
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:link")
public class WorkflowLinkManager {

    @Resource
    private DbWorkflowLinkMapper dbWorkflowLinkMapper;

    @Resource
    private DbWorkflowTypeMapper dbWorkflowTypeMapper;

    @Resource
    @Lazy
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;
    /**
     * @description:  保存流程环节类型类
     * @param workflowLink 流程环节类型类
     * @author: wuxx
     * @Date: 2021/1/22 15:12
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveWorkflowLink(@ValidGroups(groups = {WorkflowLink.INSERT_GROUP.class})WorkflowLink workflowLink) {
        if (workflowLink == null) {
            throw new ResultInfoException("流程类型信息不正确!");
        }

        //判断编码是否重复
        if(checkTypeCode(workflowLink,workflowLink.getCode())){
            throw new ResultInfoException("当前编码已经存在!");
        }

        if (null == workflowLink.getId()) {
            workflowLink.setId(null);
            //生成业务主键
            workflowLink.setLinkOid(IdUtil.simpleUUID());
        } else {
            // 流程类型oid不为空
            WorkflowLink curDict = getWorkflowLinkById(workflowLink.getId());
            if (curDict == null) {
                throw new ResultInfoException("流程类型编号未查询到相应的流程类型信息!");
            }
        }
        if (null==workflowLink.getCreateDate()){
            workflowLink.setCreateDate(new Date());
        }
        // 设置流程类型信息的状态
        if (workflowLink.getIsDelete() == null) {
            workflowLink.setIsDelete(BaseStaticParameter.N);
        }
        if (workflowLink.getIsAble() == null) {
            workflowLink.setIsAble(BaseStaticParameter.Y);
        }
        DbWorkflowLink dbWorkflowLink = new DbWorkflowLink();
        BeanUtils.copyProperties(workflowLink,dbWorkflowLink);
        if (null == workflowLink.getId()) {
            return dbWorkflowLinkMapper.insert(dbWorkflowLink);
        }else {
            return dbWorkflowLinkMapper.updateByPrimaryKeySelective(dbWorkflowLink);
        }
    }

    /**
     * @description: 检查类型编码是否重复 true重复  false不重复
     * @param typeCode 类型编码
     * @author: wuxx
     * @Date: 2021/2/24 16:45
     **/
    public boolean checkTypeCode(WorkflowLink workflowLink,String typeCode){
        WorkflowLink workflowLinkCode = this.getWorkflowLinkByTypeCode(typeCode);
        if(null==workflowLinkCode){
            return false;
        }
        if(null!=workflowLink.getId() && workflowLink.getId().longValue() == workflowLinkCode.getId().longValue()){
            return false;
        }
        return true;
    }

    /**
     * 删除流程环节类型
     *
     * @param oid 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021/01/22 15:15
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteWorkflowLinkById(Long oid) {
        DbWorkflowLink dbWorkflowLink = dbWorkflowLinkMapper.selectByPrimaryKey(oid);
        //this.checkIsExitsByOid(dbWorkflowLink.getLinkOid(),0);
        if(dbWorkflowLink == null){
            throw new ResultInfoException("流程类型信息为空！");
        }
        dbWorkflowLinkMapper.deleteById(oid);
        return BaseStaticParameter.Y;
    }

    /**
     * @description:  检查业务主键是否存在关联
     * @param oid 业务主键
     * @param flag 0删除  1启禁用
     * @author: wuxx
     * @Date: 2021/5/7 13:28
     **/
    public void checkIsExitsByOid(String oid,int flag) {
        //设计部分
        WorkflowBussFlowStep bussInfo = new WorkflowBussFlowStep();
        bussInfo.setLinkOid(oid);
        List<WorkflowBussFlowStep> bussFlowStepList = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(bussInfo);
        int mainCount = bussFlowStepList.size();
        if(mainCount>0){
            if(0 == flag){
                throw new ResultInfoException("删除失败，当前类型环节存在未删除的流程信息！");
            }else{
                throw new ResultInfoException("禁用失败，当前类型环节存在启用的流程业务信息！");
            }
        }

    }

    /**
     * 启禁用流程环节类型
     *
     * @param id 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021/01/22 15:15
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableWorkflowLinkById(Long id) {
        DbWorkflowLink dbWorkflowLink = dbWorkflowLinkMapper.selectByPrimaryKey(id);
        if(dbWorkflowLink == null){
            throw new ResultInfoException("流程类型信息为空！");
        }
        Integer isAble = dbWorkflowLink.getIsAble();
        if(BaseStaticParameter.N==isAble){
            dbWorkflowLink.setIsAble(BaseStaticParameter.Y);
        }else {
            dbWorkflowLink.setIsAble(BaseStaticParameter.N);
        }
        dbWorkflowLinkMapper.updateByPrimaryKeySelective(dbWorkflowLink);
        return BaseStaticParameter.Y;
    }

    /**
     * @description: 根据主键获取流程环节类型信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2021/1/22 15:17
     **/
    @Cacheable(key = "'getWorkflowLinkById:'+#id", unless = "#result == null")
    public WorkflowLink getWorkflowLinkById(Long id) {
        DbWorkflowLink dbWorkflowLink = dbWorkflowLinkMapper.selectByPrimaryKey(id);
        if(dbWorkflowLink == null)
            return null;
        WorkflowLink workflowLink = new WorkflowLink();
        BeanUtils.copyProperties(dbWorkflowLink,workflowLink);
        //查询所属类型
        if(StrUtil.isNotEmpty(workflowLink.getTypeOid())){
            DbWorkflowType type = dbWorkflowTypeMapper.selectDbWorkflowLinkByTypeOid(workflowLink.getTypeOid());
            workflowLink.setTypeName(null!=type?type.getName():"");
        }
        return workflowLink;
    }
    /**
     * @description: 根据主键获取流程环节类型信息
     * @param linkOid 业务主键
     * @author: wuxx
     * @Date: 2021/1/22 15:17
     **/
    @Cacheable(key = "'getWorkflowLinkByLinkOid:'+#linkOid", unless = "#result == null")
    public WorkflowLink getWorkflowLinkByLinkOid(String linkOid) {
        DbWorkflowLink dbWorkflowLink = dbWorkflowLinkMapper.selectDbWorkflowLinkByLinkOid(linkOid);
        if(dbWorkflowLink == null)
            return null;
        WorkflowLink workflowLink = new WorkflowLink();
        BeanUtils.copyProperties(dbWorkflowLink,workflowLink);
        //查询所属类型
        if(StrUtil.isNotEmpty(workflowLink.getTypeOid())){
            DbWorkflowType type = dbWorkflowTypeMapper.selectDbWorkflowLinkByTypeOid(workflowLink.getTypeOid());
            workflowLink.setTypeName(null!=type?type.getName():"");
        }
        return workflowLink;
    }

    /**
     * @description:  分页查询
     * @param workflowLink 流程环节类型操作参数
     * @param pageNumber
     * @param pageSize
     * @author: wuxx
     * @Date: 2021/1/22 15:21
     **/
    public PageResult<WorkflowLink> queryWorkflowLinkWithPage(WorkflowLink workflowLink, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbWorkflowLinkExample dbWorkflowLinkExample = new DbWorkflowLinkExample();
        dbWorkflowLinkExample.setOrderByClause("ID DESC");
        DbWorkflowLinkExample.Criteria criteria = dbWorkflowLinkExample.createCriteria();
        if(null!=workflowLink){
            if(StrUtil.isNotEmpty(workflowLink.getName())){
                criteria.andNameLike("%"+workflowLink.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(workflowLink.getTypeOid())){
                criteria.andTypeOidEqualTo(workflowLink.getTypeOid());
            }
            if(StrUtil.isNotEmpty(workflowLink.getCode())){
                criteria.andCodeLike("%"+workflowLink.getCode().trim()+"%");
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbWorkflowLink> dbWorkflowLinks = (Page<DbWorkflowLink>)dbWorkflowLinkMapper.selectByExample(dbWorkflowLinkExample);
        PageResult<WorkflowLink> pageResult = new PageResult<>(dbWorkflowLinks.getPageNum(),dbWorkflowLinks.getPageSize(),dbWorkflowLinks.getTotal());
        List<WorkflowLink> workflowLinkList = dbWorkflowLinks.stream().map(dbWorkflowLink -> {
            WorkflowLink app = new WorkflowLink();
            BeanUtils.copyProperties(dbWorkflowLink,app);
            //查询所属类型
            if(StrUtil.isNotEmpty(app.getTypeOid())){
                DbWorkflowType type = dbWorkflowTypeMapper.selectDbWorkflowLinkByTypeOid(app.getTypeOid());
                app.setTypeName(null!=type?type.getName():"");
            }
            return app;
        }).collect(Collectors.toList());
        pageResult.setData(workflowLinkList);
        return pageResult;
    }

    /**
     * @description: 根据类型编码获取流程类型信息
     * @param linkCode 环节类型编码
     * @author: wuxx
     * @Date: 2021/2/24 15:17
     **/
    @Cacheable(key = "'getWorkflowLinkByTypeCode:'+#linkCode", unless = "#result == null")
    public WorkflowLink getWorkflowLinkByTypeCode(String linkCode) {
        DbWorkflowLinkExample dbWorkflowLinkExample = new DbWorkflowLinkExample();
        DbWorkflowLinkExample.Criteria criteria = dbWorkflowLinkExample.createCriteria();
        if(StrUtil.isNotEmpty(linkCode)){
            criteria.andCodeEqualTo(linkCode);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbWorkflowLinkExample.setOrderByClause("ID DESC ");
        List<DbWorkflowLink> dbWorkflowLinks = dbWorkflowLinkMapper.selectByExample(dbWorkflowLinkExample);
        DbWorkflowLink dbWorkflowLink = null!=dbWorkflowLinks && dbWorkflowLinks.size()>0?dbWorkflowLinks.get(0):null;
        if(null==dbWorkflowLink){
            return null;
        }
        WorkflowLink workflowLink = new WorkflowLink();
        BeanUtils.copyProperties(dbWorkflowLink,workflowLink);
        return workflowLink;
    }

    /**
     * @description:  查询流程环节类型
     * @param workflowLink 流程环节类型
     * @author: wuxx
     * @Date: 2021/1/22 15:23
     **/
    public List<WorkflowLink> queryWorkflowLink(WorkflowLink workflowLink) {
        DbWorkflowLinkExample dbWorkflowLinkExample = new DbWorkflowLinkExample();
        DbWorkflowLinkExample.Criteria criteria = dbWorkflowLinkExample.createCriteria();
        if(null!=workflowLink){
            if(StrUtil.isNotEmpty(workflowLink.getName())){
                criteria.andNameLike("%"+workflowLink.getName()+"%");
            }
            if(StrUtil.isNotEmpty(workflowLink.getTypeOid())){
                criteria.andTypeOidEqualTo(workflowLink.getTypeOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbWorkflowLink> dbWorkflowLinks = dbWorkflowLinkMapper.selectByExample(dbWorkflowLinkExample);
        List<WorkflowLink> workflowLinkList = dbWorkflowLinks.stream().map(dbWorkflowLink -> {
            WorkflowLink app = new WorkflowLink();
            BeanUtils.copyProperties(dbWorkflowLink, app);
            //查询所属类型
            if(StrUtil.isNotEmpty(app.getTypeOid())){
                DbWorkflowType type = dbWorkflowTypeMapper.selectDbWorkflowLinkByTypeOid(app.getTypeOid());
                app.setTypeName(null!=type?type.getName():"");
            }
            return app;
        }).collect(Collectors.toList());
        return workflowLinkList;

    }

    /**
     * @description:  查询流程环节类型
     * @param workflowLink 流程环节类型
     * @author: wuxx
     * @Date: 2021/1/22 15:23
     **/
    public List<WorkflowLink> queryWorkflowLinkList(WorkflowLink workflowLink) {
        DbWorkflowLinkExample dbWorkflowLinkExample = new DbWorkflowLinkExample();
        DbWorkflowLinkExample.Criteria criteria = dbWorkflowLinkExample.createCriteria();
        if(null!=workflowLink){
            if(StrUtil.isNotEmpty(workflowLink.getName())){
                criteria.andNameLike("%"+workflowLink.getName()+"%");
            }
            if(StrUtil.isNotEmpty(workflowLink.getTypeOid())){
                criteria.andTypeOidEqualTo(workflowLink.getTypeOid());
            }
            if(null != workflowLink.getIsDelete()){
                criteria.andIsDeleteEqualTo(workflowLink.getIsDelete());
            }
            if(null != workflowLink.getIsAble()){
                criteria.andIsAbleEqualTo(workflowLink.getIsAble());
            }
        }
        List<DbWorkflowLink> dbWorkflowLinks = dbWorkflowLinkMapper.selectByExample(dbWorkflowLinkExample);
        List<WorkflowLink> workflowLinkList = dbWorkflowLinks.stream().map(dbWorkflowLink -> {
            WorkflowLink app = new WorkflowLink();
            BeanUtils.copyProperties(dbWorkflowLink, app);
            return app;
        }).collect(Collectors.toList());
        return workflowLinkList;

    }

}

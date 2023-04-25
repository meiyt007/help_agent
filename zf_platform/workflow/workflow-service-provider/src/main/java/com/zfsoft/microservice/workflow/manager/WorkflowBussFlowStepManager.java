package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowBussFlowStepMapper;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowBussFlowStepExample;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WorkflowBussFlowStepManager
 * @Description: 工作流业务信息流程环节表
 * @Author wuxx
 * @Date 2021/01/25
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:step")
public class WorkflowBussFlowStepManager {
    @Resource
    private DbWorkflowBussFlowStepMapper dbWorkflowBussFlowStepMapper;

    /**
     * @description:  保存流程环节信息
     * @param workflowBussFlowStep 流程环节信息
     * @author: wuxx
     * @Date: 2021/1/22 15:12
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveWorkflowBussFlowStep(@ValidGroups(groups = {WorkflowBussFlowStep.INSERT_GROUP.class})WorkflowBussFlowStep workflowBussFlowStep) {
        if (workflowBussFlowStep == null) {
            throw new ResultInfoException("流程类型信息不正确!");
        }
        if (null == workflowBussFlowStep.getId()) {
            workflowBussFlowStep.setId(null);
            //生成业务主键
            workflowBussFlowStep.setStepOid(IdUtil.simpleUUID());
        } else {
            // 流程类型oid不为空
            WorkflowBussFlowStep curDict = getWorkflowBussFlowStepById(workflowBussFlowStep.getId());
            if (curDict == null) {
                throw new ResultInfoException("流程类型编号未查询到相应的流程类型信息!");
            }
        }
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = new DbWorkflowBussFlowStep();
        BeanUtils.copyProperties(workflowBussFlowStep,dbWorkflowBussFlowStep);
        if (null == workflowBussFlowStep.getId()) {
            return dbWorkflowBussFlowStepMapper.insert(dbWorkflowBussFlowStep);
        }else {
            return dbWorkflowBussFlowStepMapper.updateByPrimaryKey(dbWorkflowBussFlowStep);
        }
    }


    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveOrUpdateWorkflowBussFlowStep(@ValidGroups(groups = {WorkflowBussFlowStep.INSERT_GROUP.class})WorkflowBussFlowStep workflowBussFlowStep) {
        if (workflowBussFlowStep == null) {
            throw new ResultInfoException("流程类型信息不正确!");
        }
        if (null == workflowBussFlowStep.getId()) {
            workflowBussFlowStep.setId(null);
            //生成业务主键
            workflowBussFlowStep.setStepOid(IdUtil.simpleUUID());
        } else {
            // 流程类型oid不为空
            WorkflowBussFlowStep curDict = getWorkflowBussFlowStepById(workflowBussFlowStep.getId());
            if (curDict == null) {
                throw new ResultInfoException("流程类型编号未查询到相应的流程类型信息!");
            }
        }
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = new DbWorkflowBussFlowStep();
        BeanUtils.copyProperties(workflowBussFlowStep,dbWorkflowBussFlowStep);
        if (null == workflowBussFlowStep.getId()) {
            return dbWorkflowBussFlowStepMapper.insert(dbWorkflowBussFlowStep);
        }else {
            return dbWorkflowBussFlowStepMapper.updateByPrimaryKey(dbWorkflowBussFlowStep);
        }
    }

    /**
     * 删除流程类型
     *
     * @param id 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021/01/22 15:15
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteWorkflowBussFlowStepById(Long id) {
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = dbWorkflowBussFlowStepMapper.selectByPrimaryKey(id);
        if(dbWorkflowBussFlowStep == null){
            throw new ResultInfoException("流程类型信息为空！");
        }
        dbWorkflowBussFlowStepMapper.deleteByPrimaryKey(id);
        return BaseStaticParameter.Y;
    }

    /**
     * @description: 根据主键获取流程类型信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2021/1/22 15:17
     **/
    @Cacheable(key = "'getWorkflowBussFlowStepById:'+#id", unless = "#result == null")
    public WorkflowBussFlowStep getWorkflowBussFlowStepById(Long id) {
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = dbWorkflowBussFlowStepMapper.selectByPrimaryKey(id);
        if(dbWorkflowBussFlowStep == null)
            return null;
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        BeanUtils.copyProperties(dbWorkflowBussFlowStep,workflowBussFlowStep);
        return workflowBussFlowStep;
    }
    /**
     * @description: 根据主键获取流程类型信息
     * @param stepOid 业务主键
     * @author: wuxx
     * @Date: 2021/1/25 18:17
     **/
    @Cacheable(key = "'getWorkflowBussFlowStepByStepOid:'+#stepOid", unless = "#result == null")
    public WorkflowBussFlowStep getWorkflowBussFlowStepByStepOid(String stepOid) {
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = dbWorkflowBussFlowStepMapper.selectDbWorkflowLinkByStepOid(stepOid);
        if(dbWorkflowBussFlowStep == null)
            return null;
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        BeanUtils.copyProperties(dbWorkflowBussFlowStep,workflowBussFlowStep);
        return workflowBussFlowStep;
    }

    /**
     * @description:  根据用户任务的xmlid查询信息
     * @param activityId 用户任务的id
     * @param processDefId  对应activiti的act_re_procdef 表ID
     * @author: wuxx
     * @Date: 2021/01/25 15:17
     **/
    @Cacheable(key = "'getWorkflowBussFlowStepByActivityIdAndDefId:'+#activityId+#processDefId", unless = "#result == null")
    public WorkflowBussFlowStep getWorkflowBussFlowStepByActivityIdAndDefId(String activityId,String processDefId) {
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = dbWorkflowBussFlowStepMapper.getWorkflowBussFlowStepByActivityIdAndDefId(activityId,processDefId);
        if(dbWorkflowBussFlowStep == null)
            return null;
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        BeanUtils.copyProperties(dbWorkflowBussFlowStep,workflowBussFlowStep);
        return workflowBussFlowStep;
    }

    /**
     * @description:  根据用户任务的xmlid查询最新得一条信息
     * @param activityId 用户任务的id
     * @author: wuxx
     * @Date: 2021/01/25 15:17
     **/
    @Cacheable(key = "'getWorkflowBussFlowStepByActivityId:'+#activityId", unless = "#result == null")
    public WorkflowBussFlowStep getWorkflowBussFlowStepByActivityId(String activityId) {
        DbWorkflowBussFlowStepExample dbWorkflowBussFlowStepExample = new DbWorkflowBussFlowStepExample();
        dbWorkflowBussFlowStepExample.setOrderByClause("ID DESC");
        DbWorkflowBussFlowStepExample.Criteria criteria = dbWorkflowBussFlowStepExample.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        List<DbWorkflowBussFlowStep> dbWorkflowBussFlowSteps = dbWorkflowBussFlowStepMapper.selectByExample(dbWorkflowBussFlowStepExample);
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = null!=dbWorkflowBussFlowSteps&&dbWorkflowBussFlowSteps.size()>0?dbWorkflowBussFlowSteps.get(0):null;
        if(null == dbWorkflowBussFlowStep)
            return null;
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        BeanUtils.copyProperties(dbWorkflowBussFlowStep,workflowBussFlowStep);
        return workflowBussFlowStep;
    }

    /**
     * @description:  根据设计流程oid和用户任务的xmlid查询信息
     * @param infoOid 设计流程oid
     * @param activityId 用户任务的id
     * @author: wuxx
     * @Date: 2021/01/25 18:17
     **/
    @Cacheable(key = "'getWorkflowBussFlowStepByInfoIdAndActivityId:'+#infoOid+#activityId", unless = "#result == null")
    public WorkflowBussFlowStep getWorkflowBussFlowStepByInfoIdAndActivityId(String infoOid,String activityId) {
        List<DbWorkflowBussFlowStep> stepList = dbWorkflowBussFlowStepMapper.getWorkflowBussFlowStepByInfoIdAndActivityId(infoOid, activityId);
        if(stepList == null || stepList.size() == 0)
            return null;
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        BeanUtils.copyProperties(stepList.get(0),workflowBussFlowStep);
        return workflowBussFlowStep;
    }

    /**
     * @description:  根据设计流程oid查询processDefId是空的信息
     * @param infoOid 设计流程oid
     * @author: wuxx
     * @Date: 2021/01/25 18:17
     **/
    @Cacheable(key = "'queryWorkflowBussFlowStepByInfOidAndDefIdIsNull:'+#infoOid", unless = "#result == null")
    public List<WorkflowBussFlowStep> queryWorkflowBussFlowStepByInfOidAndDefIdIsNull(String infoOid) {
        if(StrUtil.isEmpty(infoOid)){
            return  null;
        }
        List<DbWorkflowBussFlowStep> dbWorkflowBussFlowSteps = dbWorkflowBussFlowStepMapper.queryWorkflowBussFlowStepByInfOidAndDefIdIsNull(infoOid);
        List<WorkflowBussFlowStep> workflowBussFlowStepList = dbWorkflowBussFlowSteps.stream().map(dbWorkflowBussFlowStep -> {
            WorkflowBussFlowStep type = new WorkflowBussFlowStep();
            BeanUtils.copyProperties(dbWorkflowBussFlowStep,type);
            return type;
        }).collect(Collectors.toList());
        return workflowBussFlowStepList;
    }

    /**
     * @description:  根据用户任务的xmlid查询processDefId是空的信息
     * @param activityId 用户任务的id
     * @author: wuxx
     * @Date: 2021/01/25 18:17
     **/
    @Cacheable(key = "'getWorkflowBussFlowStep:'+#infoOid+#activityId", unless = "#result == null")
    public WorkflowBussFlowStep getWorkflowBussFlowStepByInfoOidAndActivityIdAndDefIdIsNull(String infoOid,String activityId) {
        DbWorkflowBussFlowStep dbWorkflowBussFlowStep = dbWorkflowBussFlowStepMapper.getWorkflowBussFlowStepByInfoOidAndActivityIdAndDefIdIsNull(infoOid,activityId);
        if(dbWorkflowBussFlowStep == null)
            return null;
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        BeanUtils.copyProperties(dbWorkflowBussFlowStep,workflowBussFlowStep);
        return workflowBussFlowStep;
    }

    /**
     * @description:  分页查询
     * @param workflowBussFlowStep 流程类型操作参数
     * @param pageNumber
     * @param pageSize
     * @author: wuxx
     * @Date: 2021/1/22 15:21
     **/
    public PageResult<WorkflowBussFlowStep> queryWorkflowBussFlowStepWithPage(WorkflowBussFlowStep workflowBussFlowStep, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbWorkflowBussFlowStepExample dbWorkflowBussFlowStepExample = new DbWorkflowBussFlowStepExample();
        DbWorkflowBussFlowStepExample.Criteria criteria = dbWorkflowBussFlowStepExample.createCriteria();
        if(null!=workflowBussFlowStep){
            if(StrUtil.isNotEmpty(workflowBussFlowStep.getInfoOid())){
                criteria.andInfoOidEqualTo(workflowBussFlowStep.getInfoOid());
            }
            if(StrUtil.isNotEmpty(workflowBussFlowStep.getProcessDefId())){
                criteria.andProcessDefIdEqualTo(workflowBussFlowStep.getProcessDefId());
            }
        }
        Page<DbWorkflowBussFlowStep> dbWorkflowBussFlowSteps = (Page<DbWorkflowBussFlowStep>)dbWorkflowBussFlowStepMapper.selectByExample(dbWorkflowBussFlowStepExample);
        PageResult<WorkflowBussFlowStep> pageResult = new PageResult<>(dbWorkflowBussFlowSteps.getPageNum(),dbWorkflowBussFlowSteps.getPageSize(),dbWorkflowBussFlowSteps.getTotal());
        List<WorkflowBussFlowStep> workflowBussFlowStepList = dbWorkflowBussFlowSteps.stream().map(dbWorkflowBussFlowStep -> {
            WorkflowBussFlowStep type = new WorkflowBussFlowStep();
            BeanUtils.copyProperties(dbWorkflowBussFlowStep,type);
            return type;
        }).collect(Collectors.toList());
        pageResult.setData(workflowBussFlowStepList);
        return pageResult;
    }


    /**
     * @description:  查询流程环节类型列表
     * @param workflowBussFlowStep 流程类型
     * @author: wuxx
     * @Date: 2021/1/22 15:23
     **/
    public List<WorkflowBussFlowStep> queryWorkflowBussFlowStepList(WorkflowBussFlowStep workflowBussFlowStep) {
        DbWorkflowBussFlowStepExample dbWorkflowBussFlowStepExample = new DbWorkflowBussFlowStepExample();
        DbWorkflowBussFlowStepExample.Criteria criteria = dbWorkflowBussFlowStepExample.createCriteria();
        if(null!=workflowBussFlowStep){
            if(StrUtil.isNotEmpty(workflowBussFlowStep.getInfoOid())){
                criteria.andInfoOidEqualTo(workflowBussFlowStep.getInfoOid());
            }
            if(StrUtil.isNotEmpty(workflowBussFlowStep.getProcessDefId())){
                criteria.andProcessDefIdEqualTo(workflowBussFlowStep.getProcessDefId());
            }
            if(StrUtil.isNotEmpty(workflowBussFlowStep.getLinkOid())){
                criteria.andLinkOidEqualTo(workflowBussFlowStep.getLinkOid());
            }
            if(StrUtil.isNotEmpty(workflowBussFlowStep.getActivityId())){
                criteria.andActivityIdEqualTo(workflowBussFlowStep.getActivityId());
            }
        }
        List<DbWorkflowBussFlowStep> dbWorkflowBussFlowSteps = dbWorkflowBussFlowStepMapper.selectByExample(dbWorkflowBussFlowStepExample);
        List<WorkflowBussFlowStep> workflowBussFlowStepList = dbWorkflowBussFlowSteps.stream().map(dbWorkflowBussFlowStep -> {
            WorkflowBussFlowStep type = new WorkflowBussFlowStep();
            BeanUtils.copyProperties(dbWorkflowBussFlowStep, type);
            return type;
        }).collect(Collectors.toList());
        return workflowBussFlowStepList;

    }
}

package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.workflow.data.WorkflowFallback;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowFallbackMapper;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowTypeMapper;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowFallback;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowFallbackExample;
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
 * @ClassName WorkflowFallbackManager
 * @Description: 驳回退回操作link接口实现类
 * @Author wuxx
 * @Date 2020/9/7
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:fallback")
public class WorkflowFallbackManager {

    @Resource
    private DbWorkflowFallbackMapper dbWorkflowFallbackMapper;

    @Resource
    private DbWorkflowTypeMapper dbWorkflowTypeMapper;

    @Resource
    @Lazy
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;
    /**
     * @description:  保存驳回退回类
     * @param workflowFallback 驳回退回类
     * @author: wuxx
     * @Date: 2021/1/22 15:12
     **/
    @Transactional
    @ParamValid
    @CacheEvict(allEntries = true)
    public int saveWorkflowFallback(@ValidGroups(groups = {WorkflowFallback.INSERT_GROUP.class})WorkflowFallback workflowFallback) {
        if (workflowFallback == null) {
            throw new ResultInfoException("驳回退回记录信息不正确!");
        }

        if (null == workflowFallback.getId()) {
            workflowFallback.setId(null);
            //生成业务主键
            workflowFallback.setFallbackOid(IdUtil.simpleUUID());
        } else {
            // 驳回退回记录oid不为空
            WorkflowFallback curDict = getWorkflowFallbackById(workflowFallback.getId());
            if (curDict == null) {
                throw new ResultInfoException("驳回退回记录编号未查询到相应的驳回退回记录信息!");
            }
        }
        if (null==workflowFallback.getCreateDate()){
            workflowFallback.setCreateDate(new Date());
        }
        // 设置驳回退回记录信息的状态
        if (workflowFallback.getIsDelete() == null) {
            workflowFallback.setIsDelete(BaseStaticParameter.N);
        }
        DbWorkflowFallback dbWorkflowFallback = new DbWorkflowFallback();
        BeanUtils.copyProperties(workflowFallback,dbWorkflowFallback);
        if (null == workflowFallback.getId()) {
            synchronized (this){
                return dbWorkflowFallbackMapper.insert(dbWorkflowFallback);
            }
        }else {
            return dbWorkflowFallbackMapper.updateByPrimaryKeySelective(dbWorkflowFallback);
        }
    }

    /**
     * 删除驳回退回
     *
     * @param oid 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021/5/8 15:17
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteWorkflowFallbackById(Long oid) {
        DbWorkflowFallback dbWorkflowFallback = dbWorkflowFallbackMapper.selectByPrimaryKey(oid);
        //this.checkIsExitsByOid(dbWorkflowFallback.getLinkOid(),0);
        if(dbWorkflowFallback == null){
            throw new ResultInfoException("驳回退回记录信息为空！");
        }
        dbWorkflowFallbackMapper.deleteByPrimaryKey(oid);
        return BaseStaticParameter.Y;
    }

    /**
     * @description: 根据主键获取驳回退回信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2021/5/8 15:17
     **/
    @Cacheable(key = "'getWorkflowFallbackById:'+#id", unless = "#result == null")
    public WorkflowFallback getWorkflowFallbackById(Long id) {
        DbWorkflowFallback dbWorkflowFallback = dbWorkflowFallbackMapper.selectByPrimaryKey(id);
        if(dbWorkflowFallback == null)
            return null;
        WorkflowFallback workflowFallback = new WorkflowFallback();
        BeanUtils.copyProperties(dbWorkflowFallback,workflowFallback);
        return workflowFallback;
    }
    /**
     * @description: 根据主键获取驳回退回信息
     * @param fallbackOid 业务主键
     * @author: wuxx
     * @Date: 2021/5/8 15:17
     **/
    @Cacheable(key = "'getWorkflowFallbackByFallbackOid:'+#fallbackOid", unless = "#result == null")
    public WorkflowFallback getWorkflowFallbackByFallbackOid(String fallbackOid) {
        DbWorkflowFallback dbWorkflowFallback = dbWorkflowFallbackMapper.selectDbWorkflowFallbackByFallbackOid(fallbackOid);
        if(dbWorkflowFallback == null)
            return null;
        WorkflowFallback workflowFallback = new WorkflowFallback();
        BeanUtils.copyProperties(dbWorkflowFallback,workflowFallback);
        return workflowFallback;
    }

    /**
     * @description: 根据主键获取驳回退回信息
     * @param stepOid 环节主键
     * @author: wuxx
     * @Date: 2021/5/8 15:17
     **/
    @Cacheable(key = "'getWorkflowFallbackByStepOid:'+#stepOid+#fallbackType", unless = "#result == null")
    public WorkflowFallback getWorkflowFallbackByStepOid(String stepOid,Integer fallbackType) {
        DbWorkflowFallback dbWorkflowFallback = dbWorkflowFallbackMapper.selectDbWorkflowFallbackByStepOid(stepOid,fallbackType);
        if(dbWorkflowFallback == null)
            return null;
        WorkflowFallback workflowFallback = new WorkflowFallback();
        BeanUtils.copyProperties(dbWorkflowFallback,workflowFallback);
        return workflowFallback;
    }

    /**
     * @description:  分页查询
     * @param workflowFallback 驳回退回操作参数
     * @param pageNumber
     * @param pageSize
     * @author: wuxx
     * @Date: 2021/1/22 15:21
     **/
    public PageResult<WorkflowFallback> queryWorkflowFallbackWithPage(WorkflowFallback workflowFallback, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbWorkflowFallbackExample dbWorkflowFallbackExample = new DbWorkflowFallbackExample();
        DbWorkflowFallbackExample.Criteria criteria = dbWorkflowFallbackExample.createCriteria();
        if(null!=workflowFallback){
            if(null != workflowFallback.getFallbackType()){
                criteria.andFallbackTypeEqualTo(workflowFallback.getFallbackType());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbWorkflowFallback> dbWorkflowFallbacks = (Page<DbWorkflowFallback>)dbWorkflowFallbackMapper.selectByExample(dbWorkflowFallbackExample);
        PageResult<WorkflowFallback> pageResult = new PageResult<>(dbWorkflowFallbacks.getPageNum(),dbWorkflowFallbacks.getPageSize(),dbWorkflowFallbacks.getTotal());
        List<WorkflowFallback> workflowFallbackList = dbWorkflowFallbacks.stream().map(dbWorkflowFallback -> {
            WorkflowFallback app = new WorkflowFallback();
            BeanUtils.copyProperties(dbWorkflowFallback,app);
            return app;
        }).collect(Collectors.toList());
        pageResult.setData(workflowFallbackList);
        return pageResult;
    }

    /**
     * @description: 根据类型编码获取驳回退回记录信息
     * @param taskId 环节类型编码
     * @author: wuxx
     * @Date: 2021/2/24 15:17
     **/
    @Cacheable(key = "'getWorkflowFallbackByTaskId:'+#taskId+#fallbackType", unless = "#result == null")
    public WorkflowFallback getWorkflowFallbackByTaskId(String taskId, Integer fallbackType) {
        DbWorkflowFallbackExample dbWorkflowFallbackExample = new DbWorkflowFallbackExample();
        DbWorkflowFallbackExample.Criteria criteria = dbWorkflowFallbackExample.createCriteria();
        criteria.andTaskIdEqualTo(taskId);
        if(null!=fallbackType){
            criteria.andFallbackTypeEqualTo(fallbackType);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbWorkflowFallbackExample.setOrderByClause("ID DESC ");
        List<DbWorkflowFallback> dbWorkflowFallbacks = dbWorkflowFallbackMapper.selectByExample(dbWorkflowFallbackExample);
        DbWorkflowFallback dbWorkflowFallback = null!=dbWorkflowFallbacks && dbWorkflowFallbacks.size()>0?dbWorkflowFallbacks.get(0):null;
        if(null==dbWorkflowFallback){
            return null;
        }
        WorkflowFallback workflowFallback = new WorkflowFallback();
        BeanUtils.copyProperties(dbWorkflowFallback,workflowFallback);
        return workflowFallback;
    }

    /**
     * @description:  查询驳回退回
     * @param workflowFallback 驳回退回
     * @author: wuxx
     * @Date: 2021/1/22 15:23
     **/
    public List<WorkflowFallback> queryWorkflowFallback(WorkflowFallback workflowFallback) {
        DbWorkflowFallbackExample dbWorkflowFallbackExample = new DbWorkflowFallbackExample();
        DbWorkflowFallbackExample.Criteria criteria = dbWorkflowFallbackExample.createCriteria();
        if(null!=workflowFallback){
            if(StrUtil.isNotEmpty(workflowFallback.getTaskId())){
                criteria.andTaskIdEqualTo(workflowFallback.getTaskId());
            }
            if(StrUtil.isNotEmpty(workflowFallback.getActivityId())){
                criteria.andActivityIdEqualTo(workflowFallback.getActivityId());
            }
            if(null != workflowFallback.getFallbackType()){
                criteria.andFallbackTypeEqualTo(workflowFallback.getFallbackType());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbWorkflowFallback> dbWorkflowFallbacks = dbWorkflowFallbackMapper.selectByExample(dbWorkflowFallbackExample);
        List<WorkflowFallback> workflowFallbackList = dbWorkflowFallbacks.stream().map(dbWorkflowFallback -> {
            WorkflowFallback app = new WorkflowFallback();
            BeanUtils.copyProperties(dbWorkflowFallback, app);
            return app;
        }).collect(Collectors.toList());
        return workflowFallbackList;

    }


}

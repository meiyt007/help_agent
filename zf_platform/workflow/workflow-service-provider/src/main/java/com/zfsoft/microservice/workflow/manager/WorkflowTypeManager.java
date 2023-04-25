package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.data.WorkflowType;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowTypeMapper;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowType;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowTypeExample;
import com.zfsoft.microservice.workflow.feign.SysAppFeignService;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @ClassName WorkflowTpyeManager
 * @Description: 流程类型操作接口实现类
 * @Author wuxx
 * @Date 2021/01/22
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:type")
public class WorkflowTypeManager {
    @Resource
    private DbWorkflowTypeMapper dbWorkflowTypeMapper;

    @Autowired
    private SysAppFeignService sysAppFeignService;
    @Resource
    @Lazy
    private WorkflowBussInfoManager workflowBussInfoManager;

    @Resource
    @Lazy
    private WorkflowLinkManager workflowLinkManager;


    /**
     * @description:  保存流程类型类
     * @param workflowType 流程类型类
     * @author: wuxx
     * @Date: 2021/1/22 15:12
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveWorkflowType(@ValidGroups(groups = {WorkflowType.INSERT_GROUP.class})WorkflowType workflowType) {
        if (workflowType == null) {
            throw new ResultInfoException("流程类型信息不正确!");
        }

        //判断编码是否重复
        if(checkTypeCode(workflowType,workflowType.getCode())){
            throw new ResultInfoException("当前编码已经存在!");
        }
        if (null == workflowType.getId()) {
            workflowType.setId(null);
            //生成业务主键
            workflowType.setTypeOid(IdUtil.simpleUUID());
        } else {
            // 流程类型oid不为空
            WorkflowType curDict = getWorkflowTypeById(workflowType.getId());
            if (curDict == null) {
                throw new ResultInfoException("流程类型编号未查询到相应的流程类型信息!");
            }
        }
        if (null==workflowType.getCreateDate()){
            workflowType.setCreateDate(new Date());
        }
        // 设置流程类型信息的状态
        if (workflowType.getIsDelete() == null) {
            workflowType.setIsDelete(BaseStaticParameter.N);
        }
        if (workflowType.getIsAble() == null) {
            workflowType.setIsAble(BaseStaticParameter.Y);
        }
        DbWorkflowType dbWorkflowType = new DbWorkflowType();
        BeanUtils.copyProperties(workflowType,dbWorkflowType);
        if (null == workflowType.getId()) {
            return dbWorkflowTypeMapper.insert(dbWorkflowType);
        }else {
            return dbWorkflowTypeMapper.updateByPrimaryKeySelective(dbWorkflowType);
        }
    }


    /**
     * @description: 检查类型编码是否重复 true重复  false不重复
     * @param typeCode 类型编码
     * @author: wuxx
     * @Date: 2021/2/24 16:45
     **/
    public boolean checkTypeCode(WorkflowType workflowType,String typeCode){
        WorkflowType byTypeCode = this.getWorkflowTypeByTypeCode(typeCode);
        if(null==byTypeCode){
            return false;
        }
        if(null!=workflowType.getId() && workflowType.getId().longValue() == byTypeCode.getId().longValue()){
            return false;
        }
        return true;
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
    public int deleteWorkflowTypeById(Long id) {
        DbWorkflowType dbWorkflowType = dbWorkflowTypeMapper.selectByPrimaryKey(id);
        this.checkIsExitsByOid(dbWorkflowType.getTypeOid(),0);
        if(dbWorkflowType == null){
            throw new ResultInfoException("流程类型信息为空！");
        }
        dbWorkflowTypeMapper.deleteById(id);
        return BaseStaticParameter.Y;
    }


    /**
     * 启禁用流程类型
     *
     * @param id 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021/01/22 15:15
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableWorkflowTypeById(Long id) {
        DbWorkflowType dbWorkflowType = dbWorkflowTypeMapper.selectByPrimaryKey(id);
        if(dbWorkflowType == null){
            throw new ResultInfoException("流程类型信息为空！");
        }
        Integer isAble = dbWorkflowType.getIsAble();
        if(BaseStaticParameter.N==isAble){
            dbWorkflowType.setIsAble(BaseStaticParameter.Y);
        }else {
            this.checkIsExitsByOid(dbWorkflowType.getTypeOid(),1);
            dbWorkflowType.setIsAble(BaseStaticParameter.N);
        }
        dbWorkflowTypeMapper.updateByPrimaryKeySelective(dbWorkflowType);
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
        //流程环节
        WorkflowLink workflowLink = new WorkflowLink();
        workflowLink.setTypeOid(oid);
        workflowLink.setIsDelete(BaseStaticParameter.N);
        if(1 == flag){
            workflowLink.setIsAble(BaseStaticParameter.Y);
        }
        List<WorkflowLink> workflowLinkList = workflowLinkManager.queryWorkflowLinkList(workflowLink);
        int linkCount = workflowLinkList.size();
        System.out.println("flag"+flag);
        if(linkCount>0){
            if(0 == flag){
                throw new ResultInfoException("删除失败，当前流程类型存在未删除的流程类型环节！");
            }else{
                throw new ResultInfoException("禁用失败，当前流程类型存在启用的流程类型环节！");
            }
        }
        //流程业务
        WorkflowBussInfo bussInfo = new WorkflowBussInfo();
        bussInfo.setTypeOid(oid);
        bussInfo.setIsDelete(BaseStaticParameter.N);
        List<WorkflowBussInfo> workflowBussInfoList = workflowBussInfoManager.queryWorkflowBussInfoList(bussInfo);
        int mainCount = workflowBussInfoList.size();
        if(mainCount>0){
            if(0 == flag){
                throw new ResultInfoException("删除失败，当前流程类型存在未删除的流程业务信息！");
            }else{
                throw new ResultInfoException("禁用失败，当前流程类型存在启用的流程业务信息！");
            }
        }

    }

    /**
     * @description: 根据主键获取流程类型信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2021/1/22 15:17
     **/
    @Cacheable(key = "'getWorkflowTypeById:'+#id", unless = "#result == null")
    public WorkflowType getWorkflowTypeById(Long id) {
        DbWorkflowType dbWorkflowType = dbWorkflowTypeMapper.selectByPrimaryKey(id);
        if(dbWorkflowType == null)
            return null;
        WorkflowType workflowType = new WorkflowType();
        BeanUtils.copyProperties(dbWorkflowType,workflowType);
        if(StrUtil.isNotEmpty(workflowType.getAppOid())){
            SysApp sysApp = sysAppFeignService.getSysAppByAppOid(workflowType.getAppOid()).getData();
            workflowType.setAppName(null!=sysApp?sysApp.getName():"");
        }
        return workflowType;
    }
    /**
     * @description: 根据主键获取流程类型信息
     * @param typeOid 业务主键
     * @author: wuxx
     * @Date: 2021/1/22 15:17
     **/
    @Cacheable(key = "'getWorkflowTypeByTypeOid:'+#typeOid", unless = "#result == null")
    public WorkflowType getWorkflowTypeByTypeOid(String typeOid) {
        DbWorkflowType dbWorkflowType = dbWorkflowTypeMapper.selectDbWorkflowLinkByTypeOid(typeOid);
        if(dbWorkflowType == null)
            return null;
        WorkflowType workflowType = new WorkflowType();
        BeanUtils.copyProperties(dbWorkflowType,workflowType);
        if(StrUtil.isNotEmpty(workflowType.getAppOid())){
            SysApp sysApp = sysAppFeignService.getSysAppByAppOid(workflowType.getAppOid()).getData();
            workflowType.setAppName(null!=sysApp?sysApp.getName():"");
        }
        return workflowType;
    }

    /**
     * @description: 根据类型编码获取流程类型信息
     * @param typeCode 类型编码
     * @author: wuxx
     * @Date: 2021/2/24 15:17
     **/
    @Cacheable(key = "'getWorkflowTypeByTypeCode:'+#typeCode", unless = "#result == null")
    public WorkflowType getWorkflowTypeByTypeCode(String typeCode) {
        DbWorkflowTypeExample dbWorkflowTypeExample = new DbWorkflowTypeExample();
        DbWorkflowTypeExample.Criteria criteria = dbWorkflowTypeExample.createCriteria();
        if(StrUtil.isNotEmpty(typeCode)){
            criteria.andCodeEqualTo(typeCode);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbWorkflowTypeExample.setOrderByClause("ID DESC ");
        List<DbWorkflowType> workflowTypeList = dbWorkflowTypeMapper.selectByExample(dbWorkflowTypeExample);
        DbWorkflowType dbWorkflowType = null != workflowTypeList && workflowTypeList.size() > 0 ? workflowTypeList.get(0):null;
        if(null == dbWorkflowType)
            return null;
        WorkflowType workflowType = new WorkflowType();
        BeanUtils.copyProperties(dbWorkflowType,workflowType);
        return workflowType;
    }

    /**
     * @description:  分页查询
     * @param workflowType 流程类型操作参数
     * @param pageNumber
     * @param pageSize
     * @author: wuxx
     * @Date: 2021/1/22 15:21
     **/
    public PageResult<WorkflowType> queryWorkflowTypeWithPage(WorkflowType workflowType, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbWorkflowTypeExample dbWorkflowTypeExample = new DbWorkflowTypeExample();
        dbWorkflowTypeExample.setOrderByClause("ID DESC");
        DbWorkflowTypeExample.Criteria criteria = dbWorkflowTypeExample.createCriteria();
        if(null!=workflowType){
            if(StrUtil.isNotEmpty(workflowType.getName())){
                criteria.andNameLike("%"+workflowType.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(workflowType.getAppOid())){
                criteria.andAppOidEqualTo(workflowType.getAppOid());
            }
            if(StrUtil.isNotEmpty(workflowType.getCode())){
                criteria.andCodeLike("%"+workflowType.getCode().trim()+"%");
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbWorkflowType> dbWorkflowTypes = (Page<DbWorkflowType>)dbWorkflowTypeMapper.selectByExample(dbWorkflowTypeExample);
        PageResult<WorkflowType> pageResult = new PageResult<>(dbWorkflowTypes.getPageNum(),dbWorkflowTypes.getPageSize(),dbWorkflowTypes.getTotal());
        List<WorkflowType> workflowTypeList = dbWorkflowTypes.stream().map(dbWorkflowType -> {
            WorkflowType type = new WorkflowType();
            BeanUtils.copyProperties(dbWorkflowType,type);
            if(StrUtil.isNotEmpty(type.getAppOid())){
                SysApp sysApp = sysAppFeignService.getSysAppByAppOid(type.getAppOid()).getData();
                type.setAppName(null!=sysApp?sysApp.getName():"");
            }
            return type;
        }).collect(Collectors.toList());
        pageResult.setData(workflowTypeList);
        return pageResult;
    }


    /**
     * @description:  查询流程类型
     * @param workflowType 流程类型
     * @author: wuxx
     * @Date: 2021/1/22 15:23
     **/
    public List<WorkflowType> queryWorkflowType(WorkflowType workflowType) {
        DbWorkflowTypeExample dbWorkflowTypeExample = new DbWorkflowTypeExample();
        DbWorkflowTypeExample.Criteria criteria = dbWorkflowTypeExample.createCriteria();
        if(null!=workflowType){
            if(StrUtil.isNotEmpty(workflowType.getName())){
                criteria.andNameLike("%"+workflowType.getName()+"%");
            }
            if(StrUtil.isNotEmpty(workflowType.getAppOid())){
                criteria.andAppOidEqualTo(workflowType.getAppOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbWorkflowType> dbWorkflowTypes = dbWorkflowTypeMapper.selectByExample(dbWorkflowTypeExample);
        List<WorkflowType> workflowTypeList = dbWorkflowTypes.stream().map(dbWorkflowType -> {
            WorkflowType type = new WorkflowType();
            BeanUtils.copyProperties(dbWorkflowType, type);
            return type;
        }).collect(Collectors.toList());
        return workflowTypeList;

    }
}

package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.data.WorkflowType;
import com.zfsoft.microservice.workflow.data.dto.WorkflowBussInfoQueryDto;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbWorkflowBussInfoMapper;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowBussInfo;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowBussInfoExample;
import com.zfsoft.microservice.workflow.feign.SysAppFeignService;
import com.zfsoft.microservice.workflow.feign.SysDistrictFeignService;
import com.zfsoft.microservice.workflow.feign.SysOrganFeignService;
import com.zfsoft.microservice.workflow.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.bean.BeanUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidationUtils;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Model;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: kkfan
 * @create: 2021-01-26 09:40:29
 * @description: 流程信息管理
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:bussInfo")
public class WorkflowBussInfoManager {

    @Resource
    private DbWorkflowBussInfoMapper dbWorkflowBussInfoMapper;

    @Autowired
    private SysDistrictFeignService sysDistrictFeignService;

    @Autowired
    private SysOrganFeignService sysOrganFeignService;

    @Autowired
    private SysAppFeignService sysAppFeignService;

    @Autowired
    private WorkflowTypeManager workflowTypeManager;

    @Autowired
    private WorkflowLinkManager workflowLinkManager;

    @Autowired
    private ProcessDesignManager processDesignManager;

    /**
     * 保存/更新流程信息
     * @param workflowBussInfo 流程信息
     */
    @CacheEvict(allEntries = true)
    @Transactional
    public void saveOrUpdateWorkflowBussInfo(WorkflowBussInfo workflowBussInfo) throws UnsupportedEncodingException {
        if(workflowBussInfo == null) {
            throw new ResultInfoException("新增/更新对象不能为空！");
        }
        //判断编码是否重复
        if(checkTypeCode(workflowBussInfo,workflowBussInfo.getInfoCode())){
            throw new ResultInfoException("当前编码已经存在!");
        }

        DbWorkflowBussInfo dbWorkflowBussInfo = new DbWorkflowBussInfo();
        BeanUtils.copyProperties(workflowBussInfo, dbWorkflowBussInfo);
        if(null == dbWorkflowBussInfo.getId()) {
            dbWorkflowBussInfo.setCreateDate(new Date());
            dbWorkflowBussInfo.setInfoOid(UUID.randomUUID().toString(true));
            dbWorkflowBussInfo.setIsDelete(BaseStaticParameter.N);
            dbWorkflowBussInfo.setIsPublish(BaseStaticParameter.N);
            ValidationUtils.validateEntityPattern(workflowBussInfo, WorkflowBussInfo.INSERT.class);
            // WorkflowType workflowType = this.workflowTypeManager.getWorkflowTypeByTypeOid(workflowBussInfo.getTypeOid());
            // String modelId = processDesignManager.createModel(null, dbWorkflowBussInfo.getInfoOid(), dbWorkflowBussInfo.getWorkflowName(), workflowType.getName(), dbWorkflowBussInfo.getWorkflowMemo());
            // dbWorkflowBussInfo.setModelId(Integer.valueOf(modelId));
            this.dbWorkflowBussInfoMapper.insert(dbWorkflowBussInfo);
        } else {
            ValidationUtils.validateEntityPattern(workflowBussInfo, WorkflowBussInfo.UPDATE.class);
            WorkflowBussInfo workflowBussInfoTemp = this.getWorkflowBussInfoById(dbWorkflowBussInfo.getId());
            if(workflowBussInfoTemp == null) {
                throw new ResultInfoException("更新的实体不存在!");
            }
            if(StringUtils.isNotEmpty(workflowBussInfo.getWorkflowName()) || StringUtils.isNotBlank(workflowBussInfo.getTypeOid()))
            Assert.notNull(workflowBussInfoTemp, MessageFormat.format("更新的实体不存在，主键为：{0}", dbWorkflowBussInfo.getId()));
            if(StrUtil.isNotEmpty(workflowBussInfo.getWorkflowName()) || StrUtil.isNotBlank(workflowBussInfo.getTypeOid()))
            this.dbWorkflowBussInfoMapper.updateByPrimaryKeySelective(dbWorkflowBussInfo);
            /*if(null != workflowBussInfoTemp.getModelId()) {
                WorkflowType workflowType = this.workflowTypeManager.getWorkflowTypeByTypeOid(workflowBussInfo.getTypeOid());
                processDesignManager.createModel(workflowBussInfoTemp.getModelId().toString(), dbWorkflowBussInfo.getInfoOid(), dbWorkflowBussInfo.getWorkflowName(), workflowType.getName(), dbWorkflowBussInfo.getWorkflowMemo());
            }*/

        }
    }

    /**
     * 通过主键查询流程信息
     * @param oid   主键
     * @return
     */
    @Cacheable(key = "'getWorkflowBussInfoById:'+#oid", unless = "#result == null")
    public WorkflowBussInfo getWorkflowBussInfoById(Long oid) {
        if(oid == null) {
            throw new ResultInfoException("查询主键不能为空！");
        }
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        DbWorkflowBussInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(oid)
                .andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbWorkflowBussInfo> dbWorkflowBussInfoList = this.dbWorkflowBussInfoMapper.selectByExample(example);
        List<WorkflowBussInfo> workflowBussInfoList = BeanUtil.copyListProperties(dbWorkflowBussInfoList, WorkflowBussInfo::new);
        return CollectionUtils.isNotEmpty(workflowBussInfoList) ? workflowBussInfoList.get(0) : null;
    }

    /**
     * @description: 检查类型编码是否重复 true重复  false不重复
     * @param code 类型编码
     * @author: wuxx
     * @Date: 2021/2/24 16:45
     **/
    public boolean checkTypeCode(WorkflowBussInfo workflowBussInfo,String code){
        WorkflowBussInfo infoCode = this.getWorkflowBussInfoByInfoCode(code);
        if(null==infoCode){
            return false;
        }
        if(null!=workflowBussInfo.getId() && workflowBussInfo.getId().longValue() == infoCode.getId().longValue()){
            return false;
        }
        return true;
    }

    /**
     * 通过主键查询流程信息
     * @param infoOid   业务主键
     * @return
     */
    @Cacheable(key = "'getWorkflowBussInfoByInfoOid:'+#infoOid", unless = "#result == null")
    public WorkflowBussInfo getWorkflowBussInfoByInfoOid(String infoOid) {
        if(StrUtil.isBlank(infoOid)) {
            throw new ResultInfoException("查询业务主键不能为空！");
        }
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        DbWorkflowBussInfoExample.Criteria criteria = example.createCriteria();
        criteria.andInfoOidEqualTo(infoOid);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbWorkflowBussInfo> dbWorkflowBussInfoList = this.dbWorkflowBussInfoMapper.selectByExampleWithBLOBs(example);
        List<WorkflowBussInfo> workflowBussInfoList = BeanUtil.copyListProperties(dbWorkflowBussInfoList, WorkflowBussInfo::new);
        return CollectionUtils.isNotEmpty(workflowBussInfoList) ? workflowBussInfoList.get(0) : null;
    }

    /**
     * 根据流程编码获取流程类型操作信息
     * @param infoCode 流程code
     * @return
     */
    @Cacheable(key = "'getWorkflowBussInfoByInfoCode:'+#infoCode", unless = "#result == null")
    public WorkflowBussInfo getWorkflowBussInfoByInfoCode(String infoCode) {
        if(StrUtil.isEmpty(infoCode)) {
            throw new ResultInfoException("流程编码不能为空！");
        }
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        DbWorkflowBussInfoExample.Criteria criteria = example.createCriteria();
        criteria.andInfoCodeEqualTo(infoCode)
                .andIsDeleteEqualTo(BaseStaticParameter.N);
        example.setOrderByClause("ID DESC ");
        List<DbWorkflowBussInfo> dbWorkflowBussInfoList = this.dbWorkflowBussInfoMapper.selectByExample(example);
        List<WorkflowBussInfo> workflowBussInfoList = BeanUtil.copyListProperties(dbWorkflowBussInfoList, WorkflowBussInfo::new);
        return CollectionUtils.isNotEmpty(workflowBussInfoList) ? workflowBussInfoList.get(0) : null;
    }

    /**
     * @description: 根据model主键获取流程信息
     * @param modelId model主键
     * @author: wuxx
     * @Date: 2021/1/30 15:17
     **/
    @Cacheable(key = "'getWorkflowBussInfoByModelId:'+#modelId", unless = "#result == null")
    public WorkflowBussInfo getWorkflowBussInfoByModelId(Integer modelId) {
        DbWorkflowBussInfo dbWorkflowBussInfo = dbWorkflowBussInfoMapper.selectDbWorkflowBussInfoByModelId(modelId);
        if(dbWorkflowBussInfo == null)
            return null;
        WorkflowBussInfo workflowBussInfo = new WorkflowBussInfo();
        BeanUtils.copyProperties(dbWorkflowBussInfo,workflowBussInfo);
        return workflowBussInfo;
    }

    /**
     * 根据主键删除流程信息 支持批量删除 英文逗号分隔
     * @param oids   主键
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteWorkflowBussInfoByIds(String oids) {
        if(oids == null) {
            throw new ResultInfoException("删除数据主键不能为空！");
        }
        Optional.ofNullable(Arrays.asList(oids.split(",")))
                .orElseGet(Lists::newArrayList)
                .forEach(oid ->
                    deleteWorkflowBussInfoById(Long.valueOf(oid))
                );
    }

    @CacheEvict(allEntries = true)
    public void deleteWorkflowBussInfoById(Long oid) {
        if(oid == null) {
            throw new ResultInfoException("删除数据主键不能为空！");
        }
        WorkflowBussInfo workflowBussInfo = this.getWorkflowBussInfoById(oid);
        if(workflowBussInfo == null) {
            throw new ResultInfoException("删除的实体不存在！");
        }
        DbWorkflowBussInfo dbWorkflowBussInfo = new DbWorkflowBussInfo();
        BeanUtil.copyProperties(workflowBussInfo, dbWorkflowBussInfo);
        dbWorkflowBussInfo.setIsDelete(BaseStaticParameter.Y);
        //this.workflowHistoryManager.deleteModelByWorkflowInfoOid(workflowBussInfo.getInfoOid());
        this.dbWorkflowBussInfoMapper.updateByPrimaryKeySelective(dbWorkflowBussInfo);
    }

    /**
     * 分页查询流程列表
     * @param queryDto  查询实体
     * @return
     */
    public List<WorkflowBussInfo> queryWorkflowBussInfoWithPage(WorkflowBussInfoQueryDto queryDto) {
        PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize());

        return this.queryWorkflowBussInfo(queryDto);
    }

    public List<WorkflowBussInfo> queryWorkflowBussInfo(WorkflowBussInfoQueryDto queryDto) {
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        DbWorkflowBussInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        if(StrUtil.isNotEmpty(queryDto.getAppOid())) {
            criteria.andAppOidEqualTo(queryDto.getAppOid().trim());
        }
        if(StrUtil.isNotEmpty(queryDto.getDistrictOid())) {
            List<SysDistrict> districtList = sysDistrictFeignService.getSysDistrictListByPath(queryDto.getDistrictOid().trim()).getData();
            List<String> districtOidList = districtList.stream()
                    .map(SysDistrict::getDistrictOid)
                    .collect(Collectors.toList());
            //criteria.andDistrictOidEqualTo(queryDto.getDistrictOid().trim());
            if(CollectionUtils.isEmpty(districtOidList)) {
                throw new ResultInfoException("非法参数！");
            }
            criteria.andDistrictOidIn(districtOidList);
        }
        if(StrUtil.isNotEmpty(queryDto.getOrganOid())) {
            criteria.andOrganOidEqualTo(queryDto.getOrganOid().trim());
        }
        if(StrUtil.isNotEmpty(queryDto.getTypeOid())) {
            criteria.andTypeOidEqualTo(queryDto.getTypeOid().trim());
        }
        if(StrUtil.isNotEmpty(queryDto.getInfoCode())) {
            criteria.andInfoCodeEqualTo(queryDto.getInfoCode().trim());
        }
        if(null!=queryDto.getIsPublish()) {
            criteria.andIsPublishEqualTo(queryDto.getIsPublish());
        }
        if(StrUtil.isNotEmpty(queryDto.getWorkflowName())) {
            criteria.andWorkflowNameLike("%" + queryDto.getWorkflowName().trim() + "%");
        }
        example.setOrderByClause("CREATE_DATE desc");
        List<DbWorkflowBussInfo> dbWorkflowBussInfoList = this.dbWorkflowBussInfoMapper.selectByExample(example);

        return BeanUtil.copyListProperties(dbWorkflowBussInfoList, WorkflowBussInfo::new, (dbWorkflowBussInfoTemp, workflowBussInfoTemp) -> {
            if(StrUtil.isNotEmpty(workflowBussInfoTemp.getAppOid())) {
                ApiResultSet<SysApp> apiResult = this.sysAppFeignService.getSysAppByAppOid(workflowBussInfoTemp.getAppOid());
                if(apiResult.getCode() == 200 && apiResult.getData() != null) {
                    workflowBussInfoTemp.setAppName(apiResult.getData().getName());
                }
            }
            if(StrUtil.isNotEmpty(workflowBussInfoTemp.getDistrictOid())) {
                ApiResultSet<SysDistrict> apiResult = this.sysDistrictFeignService.getSysDistrictByDistrictOid(workflowBussInfoTemp.getDistrictOid());
                if(apiResult.getCode() == 200 && apiResult.getData() != null) {
                    workflowBussInfoTemp.setDistrictName(apiResult.getData().getName());
                }
            }
            if(StrUtil.isNotEmpty(workflowBussInfoTemp.getOrganOid())) {
                ApiResultSet<SysOrgan> apiResult = this.sysOrganFeignService.getSysOrganByOrganOid(workflowBussInfoTemp.getOrganOid());
                if(apiResult.getCode() == 200 && apiResult.getData() != null) {
                    workflowBussInfoTemp.setOrganName(apiResult.getData().getName());
                }
            }
            if(StrUtil.isNotEmpty(workflowBussInfoTemp.getTypeOid())) {
                WorkflowType workflowType = this.workflowTypeManager.getWorkflowTypeByTypeOid(workflowBussInfoTemp.getTypeOid());
                if(workflowType != null) {
                    workflowBussInfoTemp.setTypeName(workflowType.getName());
                }
            }
            if(null != workflowBussInfoTemp.getModelId()) {
                Model oneModel = this.processDesignManager.getOneModel(workflowBussInfoTemp.getModelId().toString());
                if(oneModel != null) {
                    workflowBussInfoTemp.setVersion(oneModel.getVersion().toString());
                    workflowBussInfoTemp.setDeploymentId(oneModel.getDeploymentId());
                }
            }
        });

    }

    /**
     * @description: 根据流程对象查询流程业务信息
     * @param workflowBussInfo
     * @author: wuxx
     * @Date: 2021/5/7 16:57
     **/
    public List<WorkflowBussInfo> queryWorkflowBussInfoList(WorkflowBussInfo workflowBussInfo) {
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        example.setOrderByClause("ID DESC");
        DbWorkflowBussInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        if(StrUtil.isNotEmpty(workflowBussInfo.getAppOid())) {
            criteria.andAppOidEqualTo(workflowBussInfo.getAppOid().trim());
        }
        if(StrUtil.isNotEmpty(workflowBussInfo.getDistrictOid())) {
            criteria.andDistrictOidEqualTo(workflowBussInfo.getDistrictOid().trim());
        }
        if(StrUtil.isNotEmpty(workflowBussInfo.getOrganOid())) {
            criteria.andOrganOidEqualTo(workflowBussInfo.getOrganOid().trim());
        }
        if(StrUtil.isNotEmpty(workflowBussInfo.getTypeOid())) {
            criteria.andTypeOidEqualTo(workflowBussInfo.getTypeOid().trim());
        }
        if(null != workflowBussInfo.getIsDelete()) {
            criteria.andIsDeleteEqualTo(workflowBussInfo.getIsDelete());
        }
        List<DbWorkflowBussInfo> dbWorkflowBussInfoList = this.dbWorkflowBussInfoMapper.selectByExample(example);
        List<WorkflowBussInfo> workflowBussInfoList = dbWorkflowBussInfoList.stream().map(dbWorkflowBussInfo -> {
            WorkflowBussInfo info = new WorkflowBussInfo();
            BeanUtils.copyProperties(dbWorkflowBussInfo, info);
            return info;
        }).collect(Collectors.toList());
        return workflowBussInfoList;
    }

    @CacheEvict(allEntries = true)
    public void deployByOid(Long oid, String modelId) throws Exception {
        if(oid == null) {
            throw new ResultInfoException("查询主键不能为空！");
        }
//        if(StringUtils.isEmpty(modelId)) {
//            throw new ResultInfoException("模型id不能为空！");
//        }
        WorkflowBussInfo workflowBussInfo = this.getWorkflowBussInfoById(Long.valueOf(oid));
        if(workflowBussInfo == null) {
            throw new ResultInfoException("部署流程信息不存在！");
        }
        if(StrUtil.isNotEmpty(modelId)){
            workflowBussInfo.setModelId(Integer.valueOf(modelId));
        }
        processDesignManager.deployModel(workflowBussInfo.getModelId().toString());
    }

    public List<WorkflowBussInfo> queryWorkflowBussInfoByTypeOid(String typeOid, String workflowName, Integer pageNum, Integer pageSize) {
        if(typeOid == null) {
            throw new ResultInfoException("类型主键不能为空！");
        }
        WorkflowBussInfoQueryDto queryDto = new WorkflowBussInfoQueryDto();
        queryDto.setTypeOid(typeOid);
        queryDto.setWorkflowName(workflowName);
        queryDto.setPageNum(pageNum);
        queryDto.setPageSize(pageSize);
        return this.queryWorkflowBussInfoWithPage(queryDto);
    }

    @CacheEvict(key = "'getWorkflowBussInfoByInfoOid:'+#infoOid")
    public void workflowCopy(String infoOid, String newInfoOid) throws Exception {
        if(StringUtils.isEmpty(infoOid)) {
            throw new ResultInfoException("源流程信息业务主键不能为空！");
        }
        if(StringUtils.isEmpty(newInfoOid)) {
            throw new ResultInfoException("目标流程信息业务主键不能为空！");
        }
        WorkflowBussInfo sourceWorkflowBussInfo = this.getWorkflowBussInfoByInfoOid(infoOid);
        WorkflowBussInfo targetWorkflowBussInfo = this.getWorkflowBussInfoByInfoOid(newInfoOid);
        if(sourceWorkflowBussInfo == null) {
            throw new ResultInfoException("要复制的流程信息不能为空！");
        }
        if(targetWorkflowBussInfo == null) {
            throw new ResultInfoException("目标流程信息不能为空！");
        }
        if(!StringUtils.equals(sourceWorkflowBussInfo.getTypeOid(), targetWorkflowBussInfo.getTypeOid())) {
            throw new ResultInfoException("业务类型不同，无法复制！");
        }
        if(StringUtils.isEmpty(sourceWorkflowBussInfo.getBpmnXml())) {
            throw new ResultInfoException("要复制的源流程信息流程图信息不能为空！");
        }

        Map<String, String> paramsMap = Maps.newHashMap();
        String bpmnXml = sourceWorkflowBussInfo.getBpmnXml();
        bpmnXml =  bpmnXml.replaceAll(infoOid,newInfoOid);
        paramsMap.put("bpmn_xml",bpmnXml);
        String modelId = this.processDesignManager.saveModelXml(newInfoOid, paramsMap);
        //不进行部署操作
        //this.deployByOid(targetWorkflowBussInfo.getId(), modelId);
    }

    public WorkflowBussInfo queryWorkflowBussInfoByProcessKey(String processKey) {
        if(processKey == null) {
            throw new ResultInfoException("processKey不能为空！");
        }
        PageHelper.startPage(1, 1);
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        example.createCriteria()
                .andProcessKeyEqualTo(processKey.trim())
                .andIsDeleteEqualTo(BaseStaticParameter.N);
        example.setOrderByClause(" CREATE_DATE desc ");

        List<DbWorkflowBussInfo> dbWorkflowBussInfos = this.dbWorkflowBussInfoMapper.selectByExample(example);
        List<WorkflowBussInfo> workflowBussInfoList = BeanUtil.copyListProperties(dbWorkflowBussInfos, WorkflowBussInfo::new);
        return CollectionUtils.isNotEmpty(workflowBussInfoList) ? workflowBussInfoList.get(0) : null;

    }

    /**
     * @description: 查询所有区划的组织机构、流程信息Tree
     * @author: wuxx
     * @Date: 2021/2/2 10:40
     **/
    @Cacheable( key = "'queryAllDistrictOrganBussInfoTree'", unless = "#result == null")
    public List<TreeSelect> queryAllDistrictOrganBussInfoTree() {
        List<SysDistrict> sysDistrictList =  sysDistrictFeignService.queryDistrictSimpleTreeList(null).getData();
        List<SysOrgan> organList = sysOrganFeignService.querySysOrganListByParentOid(null).getData();
        List<WorkflowBussInfo> workflowBussInfoList = this.queryWorkflowBussInfoWithParams(null,null,null,null);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildDistrictAndOrganAndWorkflowTreeSelect(sysDistrictList, organList, workflowBussInfoList);
        //treeSelects = GenDataTreeUtil.buildDisabledLastTree(treeSelects,GenDataTreeUtil.TYPE_FLOW);
        return treeSelects;
    }

    /**
     * @description:  根据名称、区划、组织机构查询流程的信息列表
     * @param workflowName 流程名称
     * @param districtOid 区划oid
     * @param organOid 组织机构oid
     * @param typeCode 所属类型typeCode
     * @author: wuxx
     * @Date: 2022/2/2 10:14
     **/
    public List<WorkflowBussInfo> queryWorkflowBussInfoWithParams(String workflowName,String districtOid,String organOid,String typeCode){
        DbWorkflowBussInfoExample example = new DbWorkflowBussInfoExample();
        DbWorkflowBussInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        if(StrUtil.isNotEmpty(districtOid)) {
            criteria.andDistrictOidEqualTo(districtOid);
        }
        if(StrUtil.isNotEmpty(organOid)) {
            if("null".equals(organOid)) {
                criteria.andOrganOidIsNull();
            }else {
                criteria.andOrganOidEqualTo(organOid);
            }
        }
        if(StrUtil.isNotEmpty(typeCode)) {
            WorkflowType type = workflowTypeManager.getWorkflowTypeByTypeCode(typeCode);
            if(null!=type){
                criteria.andTypeOidEqualTo(type.getTypeOid());
            }else{
                return null;
            }

        }
        if(StrUtil.isNotEmpty(workflowName)) {
            criteria.andWorkflowNameLike("%" + workflowName + "%");
        }
        criteria.andIsPublishEqualTo(BaseStaticParameter.Y);
        example.setOrderByClause("ID DESC");
        List<DbWorkflowBussInfo> dbWorkflowBussInfoList = dbWorkflowBussInfoMapper.selectByExample(example);
        List<WorkflowBussInfo> workflowBussInfoList = dbWorkflowBussInfoList.stream().map(dbWorkflowBussInfo -> {
            WorkflowBussInfo info = new WorkflowBussInfo();
            BeanUtils.copyProperties(dbWorkflowBussInfo, info);
            return info;
        }).collect(Collectors.toList());
        return workflowBussInfoList;
    }

    /**
     * @description: 根据infoOid查询流程类型环节集合
     * @param infoOid 流程信息主键
     * @author: wuxx
     * @Date: 2021/2/3 10:53
     **/
    public List<WorkflowLink> queryWorkflowLinkListByInfoOid(String infoOid){
        DbWorkflowBussInfo dbWorkflowBussInfo = dbWorkflowBussInfoMapper.selectDbWorkflowBussInfoByInfoOid(infoOid);
        if(null==dbWorkflowBussInfo){
            return null;
        }
        WorkflowLink workflowLink = new WorkflowLink();
        workflowLink.setTypeOid(dbWorkflowBussInfo.getTypeOid());
        List<WorkflowLink> workflowLinkList = workflowLinkManager.queryWorkflowLink(workflowLink);
        return workflowLinkList;
    };
}

package com.zfsoft.superwindow.manager.yxpz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.EntityWindowManage;
import com.zfsoft.superwindow.dbaccess.dao.DbEntityWindowManageMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManage;
import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManageExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: kkfan
 * @create: 2020-10-26 10:35:41
 * @description:    实体窗口管理
 */
@Service
@Slf4j
public class EntityWindowManageManager {
    @Resource
    private DbEntityWindowManageMapper dbEntityWindowManageMapper;
    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    public List<EntityWindowManage> queryList(EntityWindowManage entityWindowManage) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(null != currentLoginUser) {
            List<String>  districtOids = null;
            String path = null;

            if (StringUtils.isEmpty(entityWindowManage.getDistrictOid()) && !CurrentLoginUserHolder.getIsCompanyUser()) {// 不是实施人员
                if (CurrentLoginUserHolder.getIsAdminUser()) {
                    path= currentLoginUser.getDistrictOid().toString();
                } else {
                    entityWindowManage.setDistrictOid(currentLoginUser.getDistrictOid().toString());
                    String disName = currentLoginUser.getDistrictName();
                }
            }
            DbEntityWindowManageExample example = new DbEntityWindowManageExample();
            DbEntityWindowManageExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
            if(StringUtils.isNotEmpty(path)) {
                // TODO 这里原逻辑为筛选窗库区划下名称 因区划已剥离到基础服务  需先查询名称对应的区划id  再作为筛选条件筛选窗口
                ApiResultSet resultSet = this.sysDistrictFeginService.getSysDistrictListByPath(path);
                if(resultSet.getCode() == 200) {
                    List<SysDistrict> sysDistrictList = (List<SysDistrict>) resultSet.getData();
                    // TODO 暂无业务id  先取oid
                    districtOids = Optional.ofNullable(sysDistrictList).orElseGet(Lists::newArrayList).stream().map(SysDistrict::getDistrictOid).collect(Collectors.toList());
                } else {
                    log.info("获取区划信息为：{}", JSON.toJSONString(resultSet));
                    Assert.isTrue(true, "获取区划信息错误");
                }
            }
            if (null != entityWindowManage) {
                if (StringUtils.isNotEmpty(entityWindowManage.getDistrictOid())) {
                    String districtOid= entityWindowManage.getDistrictOid();
                    districtOid= null!=districtOid?districtOid.substring(districtOid.lastIndexOf('-')+1,districtOid.length()):"";
                    entityWindowManage.setDistrictOid(districtOid);
                    if (CollectionUtils.isEmpty(districtOids) || (!CollectionUtils.isEmpty(districtOids) && districtOids.contains(entityWindowManage.getDistrictOid().trim()))) {
                        criteria.andDistrictOidEqualTo(entityWindowManage.getDistrictOid().trim());
                    } else {
                        return Lists.newArrayList();
                    }
                } else if(!CollectionUtils.isEmpty(districtOids)) {
                    criteria.andDistrictOidIn(districtOids);
                }
                if (StringUtils.isNotEmpty(entityWindowManage.getOrganOid())) {
                    String organOid= entityWindowManage.getOrganOid();
                    organOid= null!=organOid?organOid.substring(organOid.lastIndexOf('-')+1,organOid.length()):"";
                    entityWindowManage.setOrganOid(organOid);
                    criteria.andOrganOidEqualTo(entityWindowManage.getOrganOid().trim());
                }
                if (StringUtils.isNotEmpty(entityWindowManage.getWindowName())) {
                    criteria.andWindowNameLike("%" + entityWindowManage.getWindowName().trim() + "%");
                }
            }
            List<DbEntityWindowManage> dbEntityWindowManageList = this.dbEntityWindowManageMapper.selectByExample(example);
            return BeanUtils.copyListProperties(dbEntityWindowManageList, EntityWindowManage::new);
        }
        return Lists.newArrayList();
    }

    /**
     * 保存/更新操作
     * @param entityWindowManage
     */
    public void saveOrUpdate(EntityWindowManage entityWindowManage) {
        if(StringUtils.isNotEmpty(entityWindowManage.getDistrictOid())){
            String districtOid= entityWindowManage.getDistrictOid();
            districtOid= null!=districtOid?districtOid.substring(districtOid.lastIndexOf('-')+1,districtOid.length()):"";
            entityWindowManage.setDistrictOid(districtOid);
        }
        if(StringUtils.isNotEmpty(entityWindowManage.getOrganOid())){
            String organOid= entityWindowManage.getOrganOid();
            organOid= null!=organOid?organOid.substring(organOid.lastIndexOf('-')+1,organOid.length()):"";
            entityWindowManage.setOrganOid(organOid);
        }
        if(null != entityWindowManage.getId()) {
            DbEntityWindowManage dbEntityWindowManage = this.selectByPrimaryKey(entityWindowManage.getId());
            Assert.notNull(dbEntityWindowManage, MessageFormat.format("更新对象不存在！对象id为{0}", entityWindowManage.getId()));
            BeanUtils.copyProperties(entityWindowManage, dbEntityWindowManage);
            dbEntityWindowManage.setModifyDate(new Date());
            this.dbEntityWindowManageMapper.updateByPrimaryKeySelective(dbEntityWindowManage);
            BeanUtils.copyProperties(dbEntityWindowManage, entityWindowManage);
        } else {
            DbEntityWindowManage dbEntityWindowManage = new DbEntityWindowManage();
            BeanUtils.copyProperties(entityWindowManage, dbEntityWindowManage);
            dbEntityWindowManage.setWindowOid(UUIDUtil.randomUUID());
            dbEntityWindowManage.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbEntityWindowManage.setCreateDate(new Date());
            dbEntityWindowManage.setModifyDate(new Date());
            this.dbEntityWindowManageMapper.insertSelective(dbEntityWindowManage);
            BeanUtils.copyProperties(dbEntityWindowManage, entityWindowManage);
        }
    }

    /**
     * 删除操作
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        Assert.hasLength(ids, "删除主键不能为空！");
        Optional.ofNullable(Arrays.asList(ids.split(",")))
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(id -> {
                    DbEntityWindowManage dbEntityWindowManage = this.selectByPrimaryKey(Long.valueOf(id));
                    Assert.notNull(dbEntityWindowManage, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbEntityWindowManage.setIsDelete(SysCode.DELETE_STATUS.YES);
                    dbEntityWindowManage.setModifyDate(new Date());
                    this.dbEntityWindowManageMapper.updateByPrimaryKeySelective(dbEntityWindowManage);
                });

    }

    public EntityWindowManage getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbEntityWindowManage dbEntityWindowManage = this.selectByPrimaryKey(Long.valueOf(id));
        EntityWindowManage entityWindowManage = new EntityWindowManage();
        BeanUtils.copyProperties(dbEntityWindowManage, entityWindowManage);
        if(StringUtils.isNotEmpty(entityWindowManage.getDistrictOid())){
            entityWindowManage.setDistrictOid("DISTRICT-"+entityWindowManage.getDistrictOid());
        }
        if(StringUtils.isNotEmpty(entityWindowManage.getOrganOid())){
            entityWindowManage.setOrganOid("ORGAN-"+entityWindowManage.getOrganOid());
        }
        return entityWindowManage;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    private DbEntityWindowManage selectByPrimaryKey(Long id) {
        DbEntityWindowManageExample example = new DbEntityWindowManageExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbEntityWindowManage> dbHumanEvidenceDeviceMgtList = this.dbEntityWindowManageMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbHumanEvidenceDeviceMgtList) ? null : dbHumanEvidenceDeviceMgtList.get(0);
    }

    /**
     * 根据窗口业务主键查询授权人员
     * @author wangwg
     * @param windoNo
     * @return
     */
    public EntityWindowManage queryEntityWindowByWindoNo(String windoNo) {
        DbEntityWindowManage dbEntityWindowManage = dbEntityWindowManageMapper.queryEntityWindowByWindoNo(windoNo);
        EntityWindowManage entityWindowManage = new EntityWindowManage();
        if(dbEntityWindowManage !=null){
            BeanUtils.copyProperties(dbEntityWindowManage, entityWindowManage);
        }
        return entityWindowManage;
    }

    public List<EntityWindowManage> querAllyConfigEntityWindow() {
        List<DbEntityWindowManage> dbEntityWindowManages = dbEntityWindowManageMapper.querAllyConfigEntityWindow();
        List<EntityWindowManage> entityWindowManageList  = dbEntityWindowManages.stream().map(dbEntityWindowManage -> {
            EntityWindowManage entityWindowManage = new EntityWindowManage();
            BeanUtils.copyProperties(dbEntityWindowManage, entityWindowManage);
            return entityWindowManage;
        }).collect(Collectors.toList());
        return entityWindowManageList;
    }

    public Boolean queryEntityWindowByUserOid(String userOid) {
        boolean flag =false;
        DbEntityWindowManage dbEntityWindowManage = dbEntityWindowManageMapper.queryEntityWindowByUserOid(userOid);
        if(dbEntityWindowManage != null ){
            flag=true;
        }
        return flag;
    }

    public DbEntityWindowManage getntityWindowByUserOid(String userOid){
        DbEntityWindowManage dbEntityWindowManage = dbEntityWindowManageMapper.queryEntityWindowByUserOid(userOid);
        return dbEntityWindowManage;
    }
}

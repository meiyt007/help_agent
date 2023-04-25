package com.zfsoft.superwindow.manager.yxpz;

import com.google.common.collect.Lists;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.HumanEvidenceDeviceMgt;
import com.zfsoft.superwindow.dbaccess.dao.DbEntityWindowManageMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbHumanEvidenceDeviceMgtMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManage;
import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManageExample;
import com.zfsoft.superwindow.dbaccess.data.DbHumanEvidenceDeviceMgt;
import com.zfsoft.superwindow.dbaccess.data.DbHumanEvidenceDeviceMgtExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: kkfan
 * @create: 2020-10-23 14:45:24
 * @description: 人证核验服务层
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HumanEvidenceDeviceMgtManager {

    private final DbHumanEvidenceDeviceMgtMapper dbHumanEvidenceDeviceMgtMapper;

    private final DbEntityWindowManageMapper dbEntityWindowManageMapper;

    /**
     * 查询认证核验配置里列表
     * @param humanEvidenceDeviceMgt
     * @return
     */
    public List<HumanEvidenceDeviceMgt> queryList(HumanEvidenceDeviceMgt humanEvidenceDeviceMgt) {
        DbHumanEvidenceDeviceMgt dbHumanEvidenceDeviceMgt = new DbHumanEvidenceDeviceMgt();
        BeanUtils.copyProperties(humanEvidenceDeviceMgt, dbHumanEvidenceDeviceMgt);
        List<DbHumanEvidenceDeviceMgt> dbHumanEvidenceDeviceMgtList = this.dbHumanEvidenceDeviceMgtMapper.queryDbHumanEvidenceDeviceMgtList(dbHumanEvidenceDeviceMgt);
        return BeanUtils.copyListProperties(dbHumanEvidenceDeviceMgtList, HumanEvidenceDeviceMgt::new);
    }

    /**
     * 保存/更新认证核验数据
     * @param humanEvidenceDeviceMgt
     * @return
     */
    public HumanEvidenceDeviceMgt saveOrUpdate(HumanEvidenceDeviceMgt humanEvidenceDeviceMgt) {
        if(StringUtils.isNotEmpty(humanEvidenceDeviceMgt.getDistrictOid())){
            String districtOid= humanEvidenceDeviceMgt.getDistrictOid();
            districtOid= null!=districtOid?districtOid.substring(districtOid.lastIndexOf('-')+1,districtOid.length()):"";
            humanEvidenceDeviceMgt.setDistrictOid(districtOid);
        }
        if(StringUtils.isNotEmpty(humanEvidenceDeviceMgt.getOrganOid())){
            String organOid= humanEvidenceDeviceMgt.getOrganOid();
            organOid= null!=organOid?organOid.substring(organOid.lastIndexOf('-')+1,organOid.length()):"";
            humanEvidenceDeviceMgt.setOrganOid(organOid);
        }
         if(null != humanEvidenceDeviceMgt.getId()) {
            DbHumanEvidenceDeviceMgt dbHumanEvidenceDeviceMgt = this.selectByPrimaryKey(humanEvidenceDeviceMgt.getId());
            Assert.notNull(dbHumanEvidenceDeviceMgt, MessageFormat.format("更新对象不存在！对象id为{0}", humanEvidenceDeviceMgt.getId()));
            BeanUtils.copyProperties(humanEvidenceDeviceMgt, dbHumanEvidenceDeviceMgt);
            dbHumanEvidenceDeviceMgt.setModifyDate(new Date());
            this.dbHumanEvidenceDeviceMgtMapper.updateByPrimaryKeySelective(dbHumanEvidenceDeviceMgt);
            BeanUtils.copyProperties(dbHumanEvidenceDeviceMgt, humanEvidenceDeviceMgt);
        } else {
            DbHumanEvidenceDeviceMgt dbHumanEvidenceDeviceMgt = new DbHumanEvidenceDeviceMgt();
            BeanUtils.copyProperties(humanEvidenceDeviceMgt, dbHumanEvidenceDeviceMgt);
            dbHumanEvidenceDeviceMgt.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbHumanEvidenceDeviceMgt.setModifyDate(new Date());
            dbHumanEvidenceDeviceMgt.setHumanEvidenceDeviceMgtOid(UUIDUtil.randomUUID());
            dbHumanEvidenceDeviceMgt.setCreateTime(new Date());
            this.dbHumanEvidenceDeviceMgtMapper.insertSelective(dbHumanEvidenceDeviceMgt);
            BeanUtils.copyProperties(dbHumanEvidenceDeviceMgt, humanEvidenceDeviceMgt);
        }
        return humanEvidenceDeviceMgt;
    }

    /**
     * 获取认证核验信息详情
     * @param id
     * @return
     */
    public HumanEvidenceDeviceMgt getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbHumanEvidenceDeviceMgt dbHumanEvidenceDeviceMgt = this.selectByPrimaryKey(Long.valueOf(id));
        HumanEvidenceDeviceMgt humanEvidenceDeviceMgt = new HumanEvidenceDeviceMgt();
        BeanUtils.copyProperties(dbHumanEvidenceDeviceMgt, humanEvidenceDeviceMgt);
        if(StringUtils.isNotEmpty(dbHumanEvidenceDeviceMgt.getBindingWindow())) {
            DbEntityWindowManageExample example = new DbEntityWindowManageExample();
            example.createCriteria()
                    .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO)
                    .andWindowOidEqualTo(dbHumanEvidenceDeviceMgt.getBindingWindow());
            List<DbEntityWindowManage> dbEntityWindowManages = this.dbEntityWindowManageMapper.selectByExample(example);
            if(!CollectionUtils.isEmpty(dbEntityWindowManages)) {
                humanEvidenceDeviceMgt.setDistrictOid(dbEntityWindowManages.get(0).getDistrictOid());
                humanEvidenceDeviceMgt.setOrganOid(dbEntityWindowManages.get(0).getOrganOid());
                humanEvidenceDeviceMgt.setBindingWindow(dbEntityWindowManages.get(0).getWindowOid());
                humanEvidenceDeviceMgt.setBindingWindowNum(dbEntityWindowManages.get(0).getWindowNum());
            }
        }
        if(StringUtils.isNotEmpty(humanEvidenceDeviceMgt.getDistrictOid())){
            humanEvidenceDeviceMgt.setDistrictOid("DISTRICT-"+humanEvidenceDeviceMgt.getDistrictOid());
        }
        if(StringUtils.isNotEmpty(humanEvidenceDeviceMgt.getOrganOid())){
            humanEvidenceDeviceMgt.setOrganOid("ORGAN-"+humanEvidenceDeviceMgt.getOrganOid());
        }
        return humanEvidenceDeviceMgt;
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
                    DbHumanEvidenceDeviceMgt dbHumanEvidenceDeviceMgt = this.selectByPrimaryKey(Long.valueOf(id));
                    Assert.notNull(dbHumanEvidenceDeviceMgt, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbHumanEvidenceDeviceMgt.setIsDelete(SysCode.DELETE_STATUS.YES);
                    dbHumanEvidenceDeviceMgt.setModifyDate(new Date());
                    this.dbHumanEvidenceDeviceMgtMapper.updateByPrimaryKeySelective(dbHumanEvidenceDeviceMgt);
                });
    }

    /**
     * 验证设备编号是否存在
     * @param deviceId
     * @param oid
     * @return
     */
    public Boolean validDeviceId(String deviceId, String oid) {
        // oid 不为空修改设备信息
        if (StringUtils.isNotEmpty(oid)) {
            DbHumanEvidenceDeviceMgt curDeviceMgt = this.selectByPrimaryKey(Long.valueOf(oid));
            if (null != curDeviceMgt) {
                if (curDeviceMgt.getDeviceId().trim().equals(deviceId.trim())) {
                    return false;
                } else {
                    DbHumanEvidenceDeviceMgt deviceMgt = this.getHumanEvidenceDeviceMgtByDeviceId(deviceId);
                    // 对象存在说明设备编号已存在
                    return (deviceMgt != null);
                }
            } else {
                DbHumanEvidenceDeviceMgt deviceMgt = this.getHumanEvidenceDeviceMgtByDeviceId(deviceId);
                // 对象存在说明设备编号已存在
                return (deviceMgt != null);
            }
        } else {
            DbHumanEvidenceDeviceMgt deviceMgt = this.getHumanEvidenceDeviceMgtByDeviceId(deviceId);
            // 对象存在说明设备编号已存在
            return (deviceMgt != null);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    private DbHumanEvidenceDeviceMgt selectByPrimaryKey(Long id) {
        DbHumanEvidenceDeviceMgtExample example = new DbHumanEvidenceDeviceMgtExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbHumanEvidenceDeviceMgt> dbHumanEvidenceDeviceMgtList = this.dbHumanEvidenceDeviceMgtMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbHumanEvidenceDeviceMgtList) ? null : dbHumanEvidenceDeviceMgtList.get(0);
    }

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    private DbHumanEvidenceDeviceMgt getHumanEvidenceDeviceMgtByDeviceId(String deviceId) {
        DbHumanEvidenceDeviceMgtExample example = new DbHumanEvidenceDeviceMgtExample();
        example.createCriteria()
                .andDeviceIdEqualTo(deviceId)
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbHumanEvidenceDeviceMgt> dbHumanEvidenceDeviceMgtList = this.dbHumanEvidenceDeviceMgtMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbHumanEvidenceDeviceMgtList) ? null : dbHumanEvidenceDeviceMgtList.get(0);
    }

    public String getDeviceIdByIpAddress(String ipAddress) {
        String deviceId = CurrentLoginUserHolder.getCurrentLoginUser().getOrganOid().toString();
        // 等待实体窗口管理
        //        EntityWindowManage winMag = entityWindowManageService.getEntityWindowManageByPcIp(ipAddress, organId);
//        if (null != winMag) {
//            HumanEvidenceDeviceMgt deviceMgt = humanEvidenceDeviceMgtDao
//                    .getHumanEvidenceDeviceMgtByWindow(winMag.getWindowOid());
//            if (null != deviceMgt) {
//                return deviceMgt.getDeviceId();
//            }
//        }
        return null;
    }
}

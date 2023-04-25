package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceLocationMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLocation;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLocationExample;
import com.zfsoft.service.util.MapUtil;
import com.zfsoft.service.sxService.data.SxServiceLocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceLocationServiceImpl
 * @Description: 实施清单-办理地址 实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:sxServiceLocation")
public class SxServiceLocationManager {

        @Resource
        private DbSxServiceLocationMapper dbSxServiceLocationMapper;

    //@Cacheable(key = "'getSxServiceLocationByExtendOid:extendOid=' + #extendOid", unless = "#result == null")
        public List<SxServiceLocation> getSxServiceLocationByExtendOid(String extendOid) {
            DbSxServiceLocationExample dbSxServiceLocationExample = new DbSxServiceLocationExample();
            DbSxServiceLocationExample.Criteria criteria = dbSxServiceLocationExample.createCriteria();
            if(StrUtil.isNotEmpty(extendOid)){
                criteria.andExtendOidEqualTo(extendOid);
            }
            criteria.andDelFlagEqualTo((short)0);
            List<DbSxServiceLocation> dbSxServiceLocations = dbSxServiceLocationMapper.selectByExample(dbSxServiceLocationExample);
            List<SxServiceLocation> sxServiceLocationList = dbSxServiceLocations.stream().map(sxServiceLocation -> {
                SxServiceLocation serviceLocation = new SxServiceLocation();
                BeanUtils.copyProperties(sxServiceLocation,serviceLocation);
                return serviceLocation;
            }).collect(Collectors.toList());
            return sxServiceLocationList;
        }

        public void saveOrUpdateSxServiceLocation(SxServiceLocation sxServiceLocation) {
            if (null != sxServiceLocation.getId()) {
                DbSxServiceLocation dbSxServiceLocation = this.dbSxServiceLocationMapper.selectByPrimaryKey(sxServiceLocation.getId());
                Assert.notNull(sxServiceLocation, MessageFormat.format("更新对象不存在！对象id为{0}", sxServiceLocation.getServiceLocationOid()));
                BeanUtils.copyProperties(sxServiceLocation, dbSxServiceLocation);
                dbSxServiceLocation.setModifyDate(new Date());
                this.dbSxServiceLocationMapper.updateByPrimaryKeySelective(dbSxServiceLocation);
            } else {
                DbSxServiceLocation dbSxServiceLocation = new DbSxServiceLocation();
                BeanUtils.copyProperties(sxServiceLocation, dbSxServiceLocation);
                dbSxServiceLocation.setModifyDate(new Date());
                dbSxServiceLocation.setDelFlag((short)0);
                dbSxServiceLocation.setServiceLocationOid(UUID.randomUUID().toString().replaceAll("-", ""));
                this.dbSxServiceLocationMapper.insert(dbSxServiceLocation);
            }
        }

        public List<DbSxServiceLocation> queryList(SxServiceLocation sxServiceLocation) {
            DbSxServiceLocation dbSxServiceLocation = new DbSxServiceLocation();
            BeanUtils.copyProperties(sxServiceLocation, dbSxServiceLocation);
            List<DbSxServiceLocation> dbSxServiceLocationList = this.dbSxServiceLocationMapper.queryDbSxServiceLocationList(dbSxServiceLocation);
            return dbSxServiceLocationList;
        }

        @Transactional(rollbackFor = Exception.class)
        public void delete(String id) {
            Assert.hasLength(id, "删除主键不能为空！");
            DbSxServiceLocation dbSxServiceLocation = this.dbSxServiceLocationMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxServiceLocation, MessageFormat.format("删除对象不存在！对象id为{0}", id));
            dbSxServiceLocation.setDelFlag((short)1);
            dbSxServiceLocation.setModifyDate(new Date());
            this.dbSxServiceLocationMapper.updateByPrimaryKeySelective(dbSxServiceLocation);
        }

        public DbSxServiceLocation getDetail(String id) {
            Assert.hasLength(id, "查询主键不能为空！");
            DbSxServiceLocation dbSxServiceLocation = this.dbSxServiceLocationMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxServiceLocation, MessageFormat.format("查询对象不存在！对象id为{0}", id));
            return dbSxServiceLocation;
        }

    /**
     *  获取事项的办理地点
     * @param extendOid
     * @return
     */
    public List<Map<String, Object>> getLocationInfo(String extendOid) {
        List<Map<String, Object>> location = null;
        //TODO 公司注册地址  目前写死，需要从用户登录信息获取
        String companyRegisterAddress = "山东省聊城市东昌府区新区办事处兴华西路水城华府小区2号楼1单元1052室";
        //获取办理地点信息
        DbSxServiceLocationExample dbSxServiceLocationExample = new DbSxServiceLocationExample();
        DbSxServiceLocationExample.Criteria criteria = dbSxServiceLocationExample.createCriteria();
        if(StrUtil.isNotEmpty(extendOid)){
            criteria.andExtendOidEqualTo(extendOid);
        }
        criteria.andDelFlagEqualTo((short)0);
        List<DbSxServiceLocation> dbSxServiceLocations = dbSxServiceLocationMapper.selectByExample(dbSxServiceLocationExample);
        if (dbSxServiceLocations !=null && dbSxServiceLocations.size()>0) {
            location = new ArrayList<>();
            for (DbSxServiceLocation dbSxServiceLocation: dbSxServiceLocations) {
                Map<String ,Object> item = new HashMap<>();
                String businessPlaceAddress = dbSxServiceLocation.getLocationAddr();
                item.put("businessPlaceName", dbSxServiceLocation.getLocationName());  //办事地点名称
                item.put("businessPlaceAddress", businessPlaceAddress);  //办事地点地址
                item.put("companyRegisterAddress", companyRegisterAddress);
                item.put("phone", dbSxServiceLocation.getPhone());  //咨询电话
                //计算办事地点到公司注册地址距离
                String distance = getDistance(companyRegisterAddress, businessPlaceAddress);
                item.put("distance", distance);
                location.add(item);
            }
        }
        return location;
    }

    /**
     *  计算距离
     * @param companyRegisterAddress
     * @param businessPlaceAddress
     * @return
     */
    private String getDistance(String companyRegisterAddress, String businessPlaceAddress) {
        String distance = "";
        //计算公司注册地址location
        String companyRegisterLocation = "";
        JSONObject crParam = new JSONObject();
        crParam.put("address",companyRegisterAddress);
        JSONObject crData = MapUtil.getLocation(crParam);
        if (crData.containsKey("geocodes")) {
            JSONArray geocodes = crData.getJSONArray("geocodes");
            if (geocodes !=null && geocodes.size()>0) {
                JSONObject item = geocodes.getJSONObject(0);
                if (item !=null) {
                    companyRegisterLocation = item.getString("location");
                }
            }
        }
        //计算办事地点地址
        String businessPlaceLocation = "";
        JSONObject bpParam = new JSONObject();
        bpParam.put("address",businessPlaceAddress);
        JSONObject bpData = MapUtil.getLocation(bpParam);
        if (bpData.containsKey("geocodes")) {
            JSONArray geocodes = bpData.getJSONArray("geocodes");
            if (geocodes !=null && geocodes.size()>0) {
                JSONObject item = geocodes.getJSONObject(0);
                if (item !=null) {
                    businessPlaceLocation = item.getString("location");
                }
            }
        }
        //计算两点距离
        if (StringUtils.isNotEmpty(companyRegisterLocation) && StringUtils.isNotEmpty(businessPlaceLocation)) {
            JSONObject disParam = new JSONObject();
            disParam.put("origins", companyRegisterLocation);
            disParam.put("destination", businessPlaceLocation);
            disParam.put("type","1");
            JSONObject disData = MapUtil.getDistance(disParam);
            if (disData.containsKey("results")) {
                JSONArray results = disData.getJSONArray("results");
                if (results !=null && results.size()>0) {
                    JSONObject item = results.getJSONObject(0);
                    if (item !=null) {
                        Integer disInteger = Integer.valueOf(item.getString("distance"));
                        double distanceDouble = disInteger * 1.0/1000;
                        distance = distanceDouble + "KM";
                    }
                }
            }
        }
        return distance;
    }

    /**
     *  获取事项的办理地点
     * @param extendOid
     * @param address
     * @return
     */
    public List<Map<String, Object>> getLocationInfoByExtendOidAndAddress(String extendOid, String address) {
        List<Map<String, Object>> location = null;
        //获取办理地点信息
        DbSxServiceLocationExample dbSxServiceLocationExample = new DbSxServiceLocationExample();
        DbSxServiceLocationExample.Criteria criteria = dbSxServiceLocationExample.createCriteria();
        if(StrUtil.isNotEmpty(extendOid)){
            criteria.andExtendOidEqualTo(extendOid);
        }
        criteria.andDelFlagEqualTo((short)0);
        List<DbSxServiceLocation> dbSxServiceLocations = dbSxServiceLocationMapper.selectByExample(dbSxServiceLocationExample);
        if (dbSxServiceLocations !=null && dbSxServiceLocations.size()>0) {
            location = new ArrayList<>();
            for (DbSxServiceLocation dbSxServiceLocation: dbSxServiceLocations) {
                Map<String ,Object> item = new HashMap<>();
                String businessPlaceAddress = dbSxServiceLocation.getLocationAddr();
                item.put("businessPlaceName", dbSxServiceLocation.getLocationName());  //办事地点名称
                item.put("businessPlaceAddress", businessPlaceAddress);  //办事地点地址
                item.put("phone", dbSxServiceLocation.getPhone());  //咨询电话
                //计算办事地点到终端点位距离
                String distance = getDistance(address, businessPlaceAddress);
                item.put("distance", distance);
                location.add(item);
            }
        }
        return location;
    }
}

package com.zfsoft.service.manager.sxDirectory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxDirectory.DbSxServiceTypeMapper;
import com.zfsoft.service.dbaccess.data.sxDirectory.DbSxServiceType;
import com.zfsoft.service.dbaccess.data.sxDirectory.DbSxServiceTypeExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxService.SxServiceManager;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.vo.ServiceTypeNum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceTypeManager
 * @Description: 事项类型实现类
 * @Author wangxl
 * @Date 2020/10/25
 **/
@Service
@Slf4j
public class SxServiceTypeManager {

        @Resource
        private DbSxServiceTypeMapper dbSxServiceTypeMapper;

        @Resource
        private SxServiceManager sxServiceManager;

        public SxServiceType getSxServiceTypeByOid(String oid) {
            DbSxServiceType dbSxServiceType = dbSxServiceTypeMapper.getDbSxServiceTypeByOid(oid);
            if(dbSxServiceType == null){ throw new ResultInfoException("事项类型信息为空！");}
            SxServiceType sxServiceType = new SxServiceType();
            BeanUtils.copyProperties(dbSxServiceType,sxServiceType);
            return sxServiceType;
        }

        public List<SxServiceType> getSxServiceTypeList(){
            DbSxServiceTypeExample  dbSxServiceTypeExample = new DbSxServiceTypeExample();
            DbSxServiceTypeExample.Criteria criteria = dbSxServiceTypeExample.createCriteria();
            criteria.andDelFlagEqualTo((short)0);
            criteria.andEnabledFlagEqualTo((short)1);
            dbSxServiceTypeExample.setOrderByClause(" `SERVICE_TYPE_CODE` ASC ");
            List<DbSxServiceType> dbServiceTypeList = dbSxServiceTypeMapper.selectByExample(dbSxServiceTypeExample);
            List<SxServiceType> serviceTypeList = dbServiceTypeList.stream().map(sxServiceType -> {
                SxServiceType serviceType = new SxServiceType();
                BeanUtils.copyProperties(sxServiceType,serviceType);
                return serviceType;
            }).collect(Collectors.toList());
            return serviceTypeList;
        }

    public PageResult<SxServiceType> querySxServiceTypeWithPage(SxServiceType sxServiceType, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxServiceTypeExample dbSxServiceTypeExample = new DbSxServiceTypeExample();
        DbSxServiceTypeExample.Criteria criteria = dbSxServiceTypeExample.createCriteria();
        if(null!=sxServiceType){
            if(StrUtil.isNotEmpty(sxServiceType.getServiceTypeOid())){
                criteria.andServiceTypeOidEqualTo(sxServiceType.getServiceTypeOid());
            }
            if(StrUtil.isNotEmpty(sxServiceType.getServiceTypeCode())){
                criteria.andServiceTypeCodeEqualTo(sxServiceType.getServiceTypeCode());
            }
            if(StrUtil.isNotEmpty(sxServiceType.getServiceTypeName())){
                criteria.andServiceTypeNameLike("%"+sxServiceType.getServiceTypeName()+"%");
            }
        }
        criteria.andDelFlagEqualTo((short)0);
        dbSxServiceTypeExample.setOrderByClause(" `SERVICE_TYPE_CODE` ASC ");
        Page<DbSxServiceType> dbSxServiceTypes = (Page<DbSxServiceType>)dbSxServiceTypeMapper.selectByExample(dbSxServiceTypeExample);
        PageResult<SxServiceType> pageResult = new PageResult<>(dbSxServiceTypes.getPageNum(),dbSxServiceTypes.getPageSize(),dbSxServiceTypes.getTotal());
        List<SxServiceType> serviceTypeList = dbSxServiceTypes.stream().map(dbSxServiceType -> {
            SxServiceType serviceType = new SxServiceType();
            BeanUtils.copyProperties(dbSxServiceType,serviceType);
            return serviceType;
        }).collect(Collectors.toList());
        pageResult.setData(serviceTypeList);
        return pageResult;
    }

    public List<ServiceTypeNum> getServiceTypeAndNumber(Integer serviceStatus) {
        List<ServiceTypeNum> numList = new ArrayList<>();
        List<DbSxServiceType> dbSxServiceTypes = dbSxServiceTypeMapper.getSxServiceTypeList();
        List<SxServiceType> serviceTypeList = dbSxServiceTypes.stream().map(dbSxServiceType -> {
            SxServiceType sxServiceType = new SxServiceType();
            BeanUtils.copyProperties(dbSxServiceType,sxServiceType);
            return sxServiceType;
        }).collect(Collectors.toList());
        // 原接口改造 modifyBy 20220625 wangyg
        SxService sxService =new SxService();
        sxService.setServiceStatus(Short.valueOf(serviceStatus.toString()));
        List<SxService> sxServiceList =  sxServiceManager.getSxServicList(sxService);
        Map<String, List<SxService>> sxServiceMap = sxServiceList.stream().collect(Collectors.groupingBy(SxService::getServiceTypeOid));
        for (SxServiceType sxServiceType : serviceTypeList) {
            ServiceTypeNum serviceTypeNum = new ServiceTypeNum();
            serviceTypeNum.setOid(sxServiceType.getServiceTypeOid());
            serviceTypeNum.setName(sxServiceType.getServiceTypeName());
            if(CollUtil.isNotEmpty(sxServiceMap.get(sxServiceType.getServiceTypeOid()))){
                serviceTypeNum.setNumber(sxServiceMap.get(sxServiceType.getServiceTypeOid()).size());
            }else {
                serviceTypeNum.setNumber(0);
            }
            numList.add(serviceTypeNum);
        }
        return numList;
    }

    private Integer getServiceNum(Integer serviceStatus, String serviceTypeOid) {
        SxService sxService =new SxService();
        sxService.setServiceStatus(Short.valueOf(serviceStatus.toString()));
        sxService.setServiceTypeOid(serviceTypeOid);
        List<SxService> sxServices =  sxServiceManager.getSxServicList(sxService);
        return  sxServices.size();
    }
}

package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceChargeMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceChargeParamMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceChargeTypeMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.sxService.data.SxServiceCharge;
import com.zfsoft.service.sxService.data.SxServiceChargeParam;
import com.zfsoft.service.sxService.data.SxServiceChargeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceChargeServiceImpl
 * @Description: 实施清单-收费信息 实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:sxServiceCharge")
public class SxServiceChargeManager {

        @Resource
        private DbSxServiceChargeMapper dbSxServiceChargeMapper;
        @Resource
        private DbSxServiceChargeParamMapper dbSxServiceChargeParamMapper;
        @Resource
        private DbSxServiceChargeTypeMapper dbSxServiceChargeTypeMapper;

    //@Cacheable(key = "'getSxServiceChargeByOid:oid=' + #oid", unless = "#result == null")
        public SxServiceCharge getSxServiceChargeByOid(String oid){
            DbSxServiceChargeWithBLOBs dbSxServiceChargeWithBLOBs = dbSxServiceChargeMapper.getSxServiceChargeByOid(oid);
            if(null == dbSxServiceChargeWithBLOBs){
                throw new ResultInfoException("实施清单收费信息为空！");
            }
            SxServiceCharge sxServiceCharge = new SxServiceCharge();
            BeanUtils.copyProperties(dbSxServiceChargeWithBLOBs,sxServiceCharge);
            return sxServiceCharge;
        }

    //@Cacheable(key = "'getSxServiceChargeByServiceOid:serviceOid=' + #serviceOid", unless = "#result == null")
        public List<SxServiceCharge> getSxServiceChargeByServiceOid(String serviceOid) {
            DbSxServiceChargeExample dbSxServiceChargeExample = new DbSxServiceChargeExample();
            DbSxServiceChargeExample.Criteria criteria = dbSxServiceChargeExample.createCriteria();
            if(StrUtil.isNotEmpty(serviceOid)){
                criteria.andServiceOidEqualTo(serviceOid);
            }
            criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
            DbSxServiceChargeParamExample dbSxServiceChargeParamExample = new DbSxServiceChargeParamExample();
            DbSxServiceChargeParamExample.Criteria paramCriteria = dbSxServiceChargeParamExample.createCriteria();
            List<DbSxServiceChargeWithBLOBs> dbSxServiceCharges = dbSxServiceChargeMapper.selectByExampleWithBLOBs(dbSxServiceChargeExample);
            List<SxServiceCharge> sxServiceChargeList = dbSxServiceCharges.stream().map(sxServiceCharge -> {
                SxServiceCharge serviceCharge = new SxServiceCharge();
                BeanUtils.copyProperties(sxServiceCharge,serviceCharge);
                paramCriteria.andChargeOidEqualTo(serviceCharge.getChargeOid());
                paramCriteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
                List<DbSxServiceChargeParam> dbSxServiceChargeParams = dbSxServiceChargeParamMapper.selectByExample(dbSxServiceChargeParamExample);
                List<SxServiceChargeParam> serviceChargeParamList = dbSxServiceChargeParams.stream().map(sxServiceChargeParam -> {
                    SxServiceChargeParam serviceChargeParam = new SxServiceChargeParam();
                    BeanUtils.copyProperties(sxServiceChargeParam,serviceChargeParam);
                    return serviceChargeParam;
                }).collect(Collectors.toList());
                serviceCharge.setChargeParamList(serviceChargeParamList);//收费分段参数配置
                DbSxServiceChargeType dbSxServiceChargeType = dbSxServiceChargeTypeMapper.queryById(sxServiceCharge.getChargeTypeOid());
                SxServiceChargeType sxServiceChargeType =null;
                if(dbSxServiceChargeType !=null){
                    sxServiceChargeType  = new  SxServiceChargeType();
                    BeanUtils.copyProperties(dbSxServiceChargeType,sxServiceChargeType);
                }
                serviceCharge.setChargeType(sxServiceChargeType);//收费类型
                return serviceCharge;
            }).collect(Collectors.toList());
            return sxServiceChargeList;
        }

}

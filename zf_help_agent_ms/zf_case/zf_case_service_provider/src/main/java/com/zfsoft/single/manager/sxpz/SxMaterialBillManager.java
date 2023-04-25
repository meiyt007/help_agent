package com.zfsoft.single.manager.sxpz;

import com.zfsoft.cases.manager.EvictSettingManager;
import com.zfsoft.single.data.yxpz.SxMaterialBill;
import com.zfsoft.single.data.yxpz.vo.SxMaterialBillVo;
import com.zfsoft.single.dbaccess.dao.DbSxMaterialBillMapper;
import com.zfsoft.single.dbaccess.data.DbSxMaterialBill;
import com.zfsoft.single.dbaccess.data.DbSxMaterialBillExample;
import com.zfsoft.single.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SxMaterialBillServiceImpl
 * @Description: 参数配置接口实现类
 * @Author wuxx
 * @Date 2020/9/1
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "combo:sxMaterialBill")
public class SxMaterialBillManager {

    @Resource
    private DbSxMaterialBillMapper dbSxMaterialBillMapper;

    @Cacheable(key = "'querySxMaterialBill'", unless = "#result == null")
    public List<SxMaterialBillVo> querySxMaterialBill() {
        DbSxMaterialBillExample dbSxMaterialBillExample = new DbSxMaterialBillExample();
        DbSxMaterialBillExample.Criteria criteria = dbSxMaterialBillExample.createCriteria();
        criteria.andIsDeleteEqualTo(0);
        criteria.andDirectoryStatusEqualTo("3");
        List<DbSxMaterialBill> dbSxMaterialBills = dbSxMaterialBillMapper.selectByExample(dbSxMaterialBillExample);
        List<SxMaterialBillVo> SxMaterialBillList = dbSxMaterialBills.stream().map(sxMaterialBill -> {
            SxMaterialBillVo config = new SxMaterialBillVo();
            BeanUtils.copyProperties(sxMaterialBill,config);
            return config;
        }).collect(Collectors.toList());
        return SxMaterialBillList;
    }

    /**
     * 根据业务主键查询信息
     *
     * @param
     * @return
     */
    @Cacheable(key = "'getSxMaterialBillByMaterOid:billOid=' + #billOid", unless = "#result == null")
    public SxMaterialBill getSxMaterialBillByMaterOid(String billOid) {
        DbSxMaterialBill dbSxMaterialBill = this.dbSxMaterialBillMapper.selectByBillOid(billOid);
        SxMaterialBill SxMaterialBill = new SxMaterialBill();
        if (null!=dbSxMaterialBill){
            BeanUtils.copyProperties(dbSxMaterialBill, SxMaterialBill);
        }
        return SxMaterialBill;
    }

    @Cacheable(key = "'querySxMaterialBillByBillOids'", unless = "#result == null")
    public List<SxMaterialBill> querySxMaterialBillByBillOids(List<String> billOids) {
        DbSxMaterialBillExample dbSxMaterialBillExample = new DbSxMaterialBillExample();
        DbSxMaterialBillExample.Criteria criteria = dbSxMaterialBillExample.createCriteria();
        criteria.andIsDeleteEqualTo(0);
        criteria.andDirectoryStatusEqualTo("3");
        if(billOids!=null && billOids.size()>0){
            criteria.andBillOidIn(billOids);
        }
        List<DbSxMaterialBill> dbSxMaterialBills = dbSxMaterialBillMapper.selectByExample(dbSxMaterialBillExample);
        List<SxMaterialBill> SxMaterialBillList = dbSxMaterialBills.stream().map(sxMaterialBill -> {
            SxMaterialBill config = new SxMaterialBill();
            BeanUtils.copyProperties(sxMaterialBill,config);
            return config;
        }).collect(Collectors.toList());
        return SxMaterialBillList;
    }


}

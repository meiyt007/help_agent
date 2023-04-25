package com.zfsoft.service.manager.sxService;

import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServicePrecheckMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServicePrecheck;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServicePrecheckExample;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.SxServicePrecheck;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前置事项核验信息接口实现
 * @author chenyq
 * @date 20220324
 */
@Service
@Slf4j
public class SxServicePrecheckManager {
    @Resource
    private DbSxServicePrecheckMapper dbSxServicePrecheckMapper;
    @Resource
    private SxSysAttaManager sxSysAttaManager;



    //查询列表
    public List<SxServicePrecheck> querySxServicePrecheckList(SxServicePrecheck sxServicePrecheck){
        DbSxServicePrecheckExample dbSxServicePrecheckExample = new DbSxServicePrecheckExample();
        DbSxServicePrecheckExample.Criteria criteria = dbSxServicePrecheckExample.createCriteria();
        criteria.andDelFlagEqualTo(0);
        if(StringUtils.isNotEmpty(sxServicePrecheck.getServiceOid())){
            criteria.andServiceOidEqualTo(sxServicePrecheck.getServiceOid());
        }else{
            return  null;
        }
        List<DbSxServicePrecheck> dbSxServicePrechecks
                = dbSxServicePrecheckMapper.selectByExample(dbSxServicePrecheckExample);
        List<SxServicePrecheck> sxServicePrechecks=  dbSxServicePrechecks.stream().map(co -> {
            SxServicePrecheck comd = new SxServicePrecheck();
            org.springframework.beans.BeanUtils.copyProperties(co, comd);
            if(StringUtils.isNotEmpty(co.getAttaOid())){
                SxSysAtta atta = sxSysAttaManager.getSxSysAttaByOid(co.getAttaOid());
                if(null!=atta){
                    comd.setAttaName(atta.getName());
                    comd.setAttaUrl(atta.getFilePath());
                }
            }
            return comd;
        }).collect(Collectors.toList());

        return sxServicePrechecks;
    }
    //查看
    public SxServicePrecheck getSxServicePrecheck(String precheckOid){
        DbSxServicePrecheckExample dbSxServicePrecheckExample = new DbSxServicePrecheckExample();
        DbSxServicePrecheckExample.Criteria criteria = dbSxServicePrecheckExample.createCriteria();
        criteria.andPrecheckOidEqualTo(precheckOid);
        List dbSxServicePrechecks
                = dbSxServicePrecheckMapper.selectByExample(dbSxServicePrecheckExample);
        SxServicePrecheck sxServicePrecheck = new SxServicePrecheck();
        BeanUtils.copyProperties(dbSxServicePrechecks.get(0),sxServicePrecheck);
        return sxServicePrecheck;
    }
    //保存
    public int save(SxServicePrecheck sxServicePrecheck){
        DbSxServicePrecheck dbSxServicePrecheck = new DbSxServicePrecheck();
        BeanUtils.copyProperties(sxServicePrecheck,dbSxServicePrecheck);
        int index = dbSxServicePrecheckMapper.insertSelective(dbSxServicePrecheck);
        return index;
    }
    //修改
    public int update(SxServicePrecheck sxServicePrecheck){
        DbSxServicePrecheck dbSxServicePrecheck = new DbSxServicePrecheck();
        BeanUtils.copyProperties(sxServicePrecheck,dbSxServicePrecheck);
        int index = dbSxServicePrecheckMapper.updateByPrimaryKey(dbSxServicePrecheck);
        return index;
    }
    //删除
    public int delete(String serviceOid){
        int index = dbSxServicePrecheckMapper.deleteByServiceOid(serviceOid);
        return index;
    }
}

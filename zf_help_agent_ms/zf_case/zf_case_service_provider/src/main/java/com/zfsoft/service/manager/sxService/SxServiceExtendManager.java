package com.zfsoft.service.manager.sxService;

import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceExtendMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceExtendWithBLOBs;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName SxServiceExtendServiceImpl
 * @Description: 实施清单扩展信息实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
public class SxServiceExtendManager {

    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;

    //@Cacheable(key = "'getSxServiceExtendByServiceOid:serviceOid=' + #serviceOid", unless = "#result == null")
    public SxServiceExtend getSxServiceExtendByServiceOid(String serviceOid) {
        DbSxServiceExtendWithBLOBs dbSxServiceExtendWithBLOBs = dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(serviceOid);
        if(dbSxServiceExtendWithBLOBs!=null){
            SxServiceExtend sxServiceExtend = new SxServiceExtend();
            BeanUtils.copyProperties(dbSxServiceExtendWithBLOBs, sxServiceExtend);
            return sxServiceExtend;
        }
        return null;

    }

    //@Cacheable(key = "'getSxServiceExtendByOid:oid=' + #oid", unless = "#result == null")
    public SxServiceExtend getSxServiceExtendByOid(String oid) {
        DbSxServiceExtendWithBLOBs dbSxServiceExtendWithBLOBs = dbSxServiceExtendMapper.getSxServiceExtendByOid(oid);
        if (dbSxServiceExtendWithBLOBs == null){
            throw new ResultInfoException("实施清单扩展信息为空！");
        }
        SxServiceExtend sxServiceExtend = new SxServiceExtend();
        BeanUtils.copyProperties(dbSxServiceExtendWithBLOBs, sxServiceExtend);
        return sxServiceExtend;
    }


    public SxServiceExtend getSxServiceExtendByResultOid(String resultOid) {
        DbSxServiceExtendWithBLOBs dbSxServiceExtendWithBLOBs = dbSxServiceExtendMapper.getSxServiceExtendByResultOid(resultOid);
        if(dbSxServiceExtendWithBLOBs!=null){
            SxServiceExtend sxServiceExtend = new SxServiceExtend();
            BeanUtils.copyProperties(dbSxServiceExtendWithBLOBs, sxServiceExtend);
            return sxServiceExtend;
        }
        return null;

    }
}

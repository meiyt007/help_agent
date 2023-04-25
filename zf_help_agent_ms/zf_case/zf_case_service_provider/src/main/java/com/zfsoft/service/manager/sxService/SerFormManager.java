package com.zfsoft.service.manager.sxService;

import com.zfsoft.service.dbaccess.dao.sxService.DbSerFormMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSerForm;
import com.zfsoft.service.sxService.data.SerForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName SerFormServiceImpl
 * @Description: 实施清单-表单配置 实现类
 * @Author wangxl
 * @Date 2020/10/27
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:serForm")
public class SerFormManager {

        @Resource
        private DbSerFormMapper dbSerFormMapper;

        //@Cacheable(key = "'getSerFormByService:serviceOid=' + #serviceOid", unless = "#result == null")
        public SerForm getSerFormByService(String serviceOid){
             DbSerForm dbSerForm= dbSerFormMapper.getSerFormByServiceOid(serviceOid);
             SerForm serForm = new SerForm();
             //无数据，不抛异常
             if(null  == dbSerForm){
//                 throw  new ResultInfoException("实施清单配置表单为空！");
                 dbSerForm = new DbSerForm();
             }
             BeanUtils.copyProperties(dbSerForm,serForm);
             return serForm;
        }

}

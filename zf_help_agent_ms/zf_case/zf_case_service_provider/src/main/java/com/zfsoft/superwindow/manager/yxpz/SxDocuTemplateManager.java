package com.zfsoft.superwindow.manager.yxpz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.superwindow.data.yxpz.SxDocuTemplate;
import com.zfsoft.superwindow.dbaccess.dao.DbSxDocuTemplateMapper;
import com.zfsoft.superwindow.dbaccess.data.DbSxDocuTemplate;
import com.zfsoft.superwindow.dbaccess.data.DbSxDocuTemplateExample;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SxDocuTemplateServiceImpl
 * @Description: 参数配置接口实现类
 * @Author liangxm
 * @Date 2020/11/1
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "manage:sxDocuTemplate")
public class SxDocuTemplateManager {

        @Resource
        private DbSxDocuTemplateMapper dbSxDocuTemplateMapper;

        @Transactional
        @ParamValid
        @ShardingTransactionType(TransactionType.LOCAL)
        @CacheEvict(allEntries = true)
        public int saveSxDocuTemplate(@ValidGroups(groups = {SxDocuTemplate.INSERT_GROUP.class}) SxDocuTemplate sxDocuTemplate) {

            // 设置区划信息的状态
            /*if (SxDocuTemplate.getIsDelete()==null) {
                SxDocuTemplate.setIsDelete(0);
            }*/
            sxDocuTemplate.setCreateDate(new Date());


            DbSxDocuTemplate dbSxDocuTemplate = new DbSxDocuTemplate();
            BeanUtils.copyProperties(sxDocuTemplate,dbSxDocuTemplate);
            int index=0;
            if (null == sxDocuTemplate.getId()) {
                //sxDocuTemplate.setUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                sxDocuTemplate.setEnabledFlag(SysCode.ABLE_STATUS.YES);
                sxDocuTemplate.setDelFlag(SysCode.DELETE_STATUS.NO);
                BeanUtils.copyProperties(sxDocuTemplate,dbSxDocuTemplate);
                index = dbSxDocuTemplateMapper.insert(dbSxDocuTemplate);
            }else {
                index = dbSxDocuTemplateMapper.updateByPrimaryKeySelective(dbSxDocuTemplate);
            }
            sxDocuTemplate.setId(dbSxDocuTemplate.getId());
            //清除缓存
            //evictSettingManager.evictInformation(dbSxDocuTemplate.getId());
            return index;
        }


    public PageResult<SxDocuTemplate> querySxDocuTemplateWithPage(SxDocuTemplate SxDocuTemplate, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxDocuTemplateExample dbSxDocuTemplateExample = new DbSxDocuTemplateExample();
        DbSxDocuTemplateExample.Criteria criteria = dbSxDocuTemplateExample.createCriteria();
        if(null!=SxDocuTemplate){
            if(StringUtil.isNotEmpty(SxDocuTemplate.getDocuTemplateName())){
                criteria.andDocuTemplateNameLike("%"+SxDocuTemplate.getDocuTemplateName()+"%");
            }

        }
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.NO);
        Page<DbSxDocuTemplate> dbSxDocuTemplates = (Page<DbSxDocuTemplate>)dbSxDocuTemplateMapper.selectByExample(dbSxDocuTemplateExample);
        PageResult<SxDocuTemplate> pageResult = new PageResult<>(dbSxDocuTemplates.getPageNum(),dbSxDocuTemplates.getPageSize(),dbSxDocuTemplates.getTotal());
        List<SxDocuTemplate> sysSxDocuTemplateList = dbSxDocuTemplates.stream().map(dbSxDocuTemplate -> {
            SxDocuTemplate pbpj = new SxDocuTemplate();
            BeanUtils.copyProperties(dbSxDocuTemplate,pbpj);
            return pbpj;
        }).collect(Collectors.toList());
        pageResult.setData(sysSxDocuTemplateList);
        return pageResult;
    }




    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSxDocuTemplateById(Long id) {

        int index = dbSxDocuTemplateMapper.deleteByPrimaryKey(id);

        DbSxDocuTemplate dbSxDocuTemplate = dbSxDocuTemplateMapper.selectByPrimaryKey(id);
        //清除缓存
       // evictSettingManager.evictInformation(dbSxDocuTemplate.getId());
        return index;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int ableSxDocuTemplateById(Long id) {
        DbSxDocuTemplate dbSxDocuTemplate = dbSxDocuTemplateMapper.selectByPrimaryKey(id);
        dbSxDocuTemplate.setEnabledFlag(dbSxDocuTemplate.getEnabledFlag().equals(SysCode.ABLE_STATUS.YES) ? SysCode.ABLE_STATUS.NO : SysCode.ABLE_STATUS.YES);
        int index = dbSxDocuTemplateMapper.updateByPrimaryKeySelective(dbSxDocuTemplate);
        //清除缓存
       // evictSettingManager.evictInformation(dbSxDocuTemplate.getId());
        return index;
    }

    @Transactional
    public SxDocuTemplate getSxDocuTemplateById(Long id) {
        DbSxDocuTemplate dbSxDocuTemplate = dbSxDocuTemplateMapper.selectByPrimaryKey(id);
        if(dbSxDocuTemplate == null){
            throw new ResultInfoException("参数配置信息为空！");
        }
        SxDocuTemplate SxDocuTemplate = new SxDocuTemplate();
        BeanUtils.copyProperties(dbSxDocuTemplate,SxDocuTemplate);
        return SxDocuTemplate;
    }

}

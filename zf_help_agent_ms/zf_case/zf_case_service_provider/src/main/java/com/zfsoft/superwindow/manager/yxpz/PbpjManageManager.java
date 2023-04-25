package com.zfsoft.superwindow.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.manager.EvictSettingManager;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.superwindow.data.yxpz.PbpjManage;
import com.zfsoft.superwindow.dbaccess.dao.DbPbpjManageMapper;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjManage;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjManageExample;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PbpjManageServiceImpl
 * @Description: 参数配置接口实现类
 * @Author liangxm
 * @Date 2020/9/1
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "manage:pbpj")
public class PbpjManageManager {

        @Resource
        private DbPbpjManageMapper dbPbpjManageMapper;

        @Resource
        private EvictSettingManager evictSettingManager;

        @Transactional(rollbackFor=Exception.class)
        @ParamValid
        @ShardingTransactionType(TransactionType.LOCAL)
        @CacheEvict(allEntries = true)
        public int savePbpjManage(@ValidGroups(groups = {PbpjManage.INSERT_GROUP.class}) PbpjManage pbpjManage) {

            // 设置区划信息的状态
            /*if (pbpjManage.getIsDelete()==null) {
                PbpjManage.setIsDelete(0);
            }*/

            DbPbpjManage dbPbpjManage = new DbPbpjManage();
            BeanUtils.copyProperties(pbpjManage,dbPbpjManage);
            int index=0;
            if (null == pbpjManage.getId()) {
                dbPbpjManage.setDateTime(new Date());
                dbPbpjManage.setStatus(1);
                dbPbpjManage.setIsDelete(0);
                index = dbPbpjManageMapper.insert(dbPbpjManage);
            }else {
                index = dbPbpjManageMapper.updateByPrimaryKeySelective(dbPbpjManage);
            }
            pbpjManage.setId(dbPbpjManage.getId());
            //清除缓存
            evictSettingManager.evictpbpjManage(dbPbpjManage.getId());
            return index;
        }


    public PageResult<PbpjManage> queryPbpjManageWithPage(PbpjManage pbpjManage, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        pbpjManage.setIsDelete(0);
        PageHelper.startPage(pageNumber,pageSize);
        DbPbpjManageExample dbPbpjManageExample = new DbPbpjManageExample();
        dbPbpjManageExample.setOrderByClause(" DATE_TIME DESC ");
        DbPbpjManageExample.Criteria criteria = dbPbpjManageExample.createCriteria();
        if(null!=pbpjManage){
            if(StrUtil.isNotEmpty(pbpjManage.getRunCode())){
                criteria.andRunCodeLike("%"+pbpjManage.getRunCode().trim()+"%");
            }
            if(StrUtil.isNotEmpty(pbpjManage.getName())){
                criteria.andNameLike("%"+pbpjManage.getName().trim()+"%");
            }
            if(null!= pbpjManage.getIsDelete()){
                criteria.andIsDeleteEqualTo(pbpjManage.getIsDelete());
            }
        }
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.NO);
        Page<DbPbpjManage> dbPbpjManages = (Page<DbPbpjManage>)dbPbpjManageMapper.selectByExample(dbPbpjManageExample);
        PageResult<PbpjManage> pageResult = new PageResult<>(dbPbpjManages.getPageNum(),dbPbpjManages.getPageSize(),dbPbpjManages.getTotal());
        List<PbpjManage> sysPbpjManageList = dbPbpjManages.stream().map(dbPbpjManage -> {
            PbpjManage pbpj = new PbpjManage();
            BeanUtils.copyProperties(dbPbpjManage,pbpj);
            return pbpj;
        }).collect(Collectors.toList());
        pageResult.setData(sysPbpjManageList);
        return pageResult;
    }




    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deletePbpjManageById(String id) {

        //int index = dbPbpjManageMapper.deleteByPrimaryKey(Long.valueOf(id));

        DbPbpjManage dbPbpjManage = dbPbpjManageMapper.selectByPrimaryKey(Long.valueOf(id));
        dbPbpjManage.setIsDelete(1);
        int index = dbPbpjManageMapper.updateByPrimaryKeySelective(dbPbpjManage);
        //清除缓存
        evictSettingManager.evictpbpjManage(dbPbpjManage.getId());
        return index;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int ablePbpjManageById(String id) {

        //int index = dbPbpjManageMapper.deleteByPrimaryKey(Long.valueOf(id));

        DbPbpjManage dbPbpjManage = dbPbpjManageMapper.selectByPrimaryKey(Long.valueOf(id));
        dbPbpjManage.setStatus(dbPbpjManage.getStatus().equals(SysCode.ABLE_STATUS.YES)  ? SysCode.ABLE_STATUS.NO : SysCode.ABLE_STATUS.YES);
        int index = dbPbpjManageMapper.updateByPrimaryKeySelective(dbPbpjManage);
        //清除缓存
        evictSettingManager.evictpbpjManage(dbPbpjManage.getId());
        return index;
    }
    @Transactional(rollbackFor=Exception.class)
    @Cacheable(key = "'getPbpjManageById:'+#id", unless = "#result == null")
    public PbpjManage getPbpjManageById(String id) {
        DbPbpjManage dbPbpjManage = dbPbpjManageMapper.selectByPrimaryKey(Long.valueOf(id));
        if(dbPbpjManage == null) {
            throw new ResultInfoException("参数配置信息为空！");
        }
        PbpjManage pbpjManage = new PbpjManage();
        BeanUtils.copyProperties(dbPbpjManage,pbpjManage);
        return pbpjManage;
    }

    public PbpjManage getPbpjManageByUserCode(String userCode) {
        DbPbpjManage dbPbpjManage = dbPbpjManageMapper.getPbpjManageByUserCode(userCode);
        PbpjManage pbpjManage = null;
        if(dbPbpjManage !=null){
            pbpjManage =new PbpjManage();
            BeanUtils.copyProperties(dbPbpjManage,pbpjManage);
        }
        return pbpjManage;
    }

    public PbpjManage getPbpjManageByRunCode(String runCode) {
        DbPbpjManage dbPbpjManage = dbPbpjManageMapper.getPbpjManageByRunCode(runCode);
        PbpjManage pbpjManage = null;
        if(dbPbpjManage !=null){
            pbpjManage =new PbpjManage();
            BeanUtils.copyProperties(dbPbpjManage,pbpjManage);
        }
        return pbpjManage;
    }

    @Transactional(rollbackFor=Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updatePbpjManage(PbpjManage pbpjManage) {
        if(pbpjManage.getId() == null) {
            throw new ResultInfoException("平板评价设备配置信息错误！");
        }
        DbPbpjManage dbPbpjManage = new DbPbpjManage();
        BeanUtils.copyProperties(pbpjManage,dbPbpjManage);
        int index = dbPbpjManageMapper.updateByPrimaryKeySelective(dbPbpjManage);
        return index;
    }
}

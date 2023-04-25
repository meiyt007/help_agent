package com.zfsoft.superwindow.manager.yxpz;

import com.zfsoft.cases.manager.EvictSettingManager;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.superwindow.data.yxpz.PbpjUser;
import com.zfsoft.superwindow.dbaccess.dao.DbPbpjUserMapper;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjUser;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName PbpjUserServiceImpl
 * @Description: 参数配置接口实现类
 * @Author liangxm
 * @Date 2020/9/1
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "pbpj:user")
public class PbpjUserManager {

        @Resource
        private DbPbpjUserMapper dbPbpjUserMapper;

        @Resource
        private EvictSettingManager evictSettingManager;

        @Transactional
        @ParamValid
        @ShardingTransactionType(TransactionType.LOCAL)
        @CacheEvict(allEntries = true)
        public int savePbpjUser(@ValidGroups(groups = {PbpjUser.INSERT_GROUP.class}) PbpjUser pbpjUser) {

            // 设置区划信息的状态
            /*if (PbpjUser.getIsDelete()==null) {
                PbpjUser.setIsDelete(0);
            }*/
            pbpjUser.setModifyDate(new Date());
            DbPbpjUser dbPbpjUser = new DbPbpjUser();
            BeanUtils.copyProperties(pbpjUser,dbPbpjUser);
            DbPbpjUser pbpjUserOld = dbPbpjUserMapper.selectByUserOid(pbpjUser.getUserOid());
            int index=0;
            if (null == pbpjUserOld) {
                index = dbPbpjUserMapper.insert(dbPbpjUser);
                pbpjUser.setId(dbPbpjUser.getId());

            }else {
                pbpjUser.setId(pbpjUserOld.getId());
                index = dbPbpjUserMapper.updateByPrimaryKeySelective(dbPbpjUser);
            }

            //清除缓存
            evictSettingManager.evictpbpjUser(dbPbpjUser.getId());
            return index;
        }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int appraiseById(String id) {

        //int index = dbPbpjUserMapper.deleteByPrimaryKey(Long.valueOf(id));
        int index = 0;
        DbPbpjUser dbPbpjUser = dbPbpjUserMapper.selectByUserOid(id);
        if(dbPbpjUser!=null){
            dbPbpjUser.setAppraiseFlag(dbPbpjUser.getAppraiseFlag().equals(SysCode.ABLE_STATUS.YES) ? SysCode.ABLE_STATUS.NO : SysCode.ABLE_STATUS.YES);
            index = dbPbpjUserMapper.updateByPrimaryKeySelective(dbPbpjUser);
        }else{
            DbPbpjUser dbPbpjUserNew = new DbPbpjUser();
            PbpjUser pbpjUser = new PbpjUser();
            dbPbpjUserNew.setModifyDate(new Date());
            dbPbpjUserNew.setUserOid(id);
            dbPbpjUserNew.setAppraiseFlag(SysCode.ABLE_STATUS.YES);
            dbPbpjUserNew.setConfirmFlag(SysCode.ABLE_STATUS.NO);
            //BeanUtils.copyProperties(pbpjUser,dbPbpjUser);
            index = dbPbpjUserMapper.insert(dbPbpjUserNew);
            //pbpjUser.setId(dbPbpjUser.getId());
        }
        //清除缓存
        //evictSettingManager.evictpbpjUser(dbPbpjUser.getId());
        return index;
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int confirmById(String id) {

        //int index = dbPbpjUserMapper.deleteByPrimaryKey(Long.valueOf(id));
        int index = 0;
        DbPbpjUser dbPbpjUser = dbPbpjUserMapper.selectByUserOid(id);
        if(dbPbpjUser!=null){
            if(dbPbpjUser.getConfirmFlag()!=null){
                dbPbpjUser.setConfirmFlag(dbPbpjUser.getConfirmFlag().equals(SysCode.ABLE_STATUS.YES) ? SysCode.ABLE_STATUS.NO : SysCode.ABLE_STATUS.YES);
            }else{
                dbPbpjUser.setConfirmFlag( SysCode.ABLE_STATUS.YES);
            }
            index = dbPbpjUserMapper.updateByPrimaryKeySelective(dbPbpjUser);
        }else{
            DbPbpjUser dbPbpjUserNew = new DbPbpjUser();
            PbpjUser pbpjUser = new PbpjUser();
            dbPbpjUserNew.setModifyDate(new Date());
            dbPbpjUserNew.setUserOid(id);
            dbPbpjUserNew.setConfirmFlag(SysCode.ABLE_STATUS.YES);
            dbPbpjUserNew.setAppraiseFlag(SysCode.ABLE_STATUS.NO);
            //BeanUtils.copyProperties(pbpjUser,dbPbpjUser);
            index = dbPbpjUserMapper.insert(dbPbpjUserNew);
        }


        return index;
    }

    public PbpjUser getPbpjUserByUserOid(String userOid) {
        DbPbpjUser dbPbpjUser = dbPbpjUserMapper.selectByUserOid(userOid);
        PbpjUser pbpjUser = new PbpjUser();
        if(dbPbpjUser !=null){
            BeanUtils.copyProperties(dbPbpjUser, pbpjUser);
        }
        return pbpjUser;
    }

   /* public PageResult<PbpjUser> queryPbpjUserWithPage(PbpjUser PbpjUser, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbPbpjUserExample dbPbpjUserExample = new DbPbpjUserExample();
        DbPbpjUserExample.Criteria criteria = dbPbpjUserExample.createCriteria();
        *//*if(null!=PbpjUser){
            if(StrUtil.isNotEmpty(PbpjUser.getCode())){
                criteria.andCodeLike("%"+PbpjUser.getCode()+"%");
            }
            if(StrUtil.isNotEmpty(sysConfig.getName())){
                criteria.andNameLike("%"+PbpjUser.getName()+"%");
            }
            if(null!= sysConfig.getParentOid()){
                criteria.andParentOidEqualTo(sysConfig.getParentOid());
            }
        }*//*
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.NO);
        Page<DbPbpjUser> dbPbpjUsers = (Page<DbPbpjUser>)dbPbpjUserMapper.selectByExample(dbPbpjUserExample);
        PageResult<PbpjUser> pageResult = new PageResult<>(dbPbpjUsers.getPageNum(),dbPbpjUsers.getPageSize(),dbPbpjUsers.getTotal());
        List<PbpjUser> sysPbpjUserList = dbPbpjUsers.stream().map(dbPbpjUser -> {
            PbpjUser pbpj = new PbpjUser();
            BeanUtils.copyProperties(dbPbpjUser,pbpj);
            return pbpj;
        }).collect(Collectors.toList());
        pageResult.setData(sysPbpjUserList);
        return pageResult;
    }*/


}

package com.zfsoft.service.manager.sxSign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.manager.EvictSettingManager;
import com.zfsoft.service.dbaccess.dao.sxSign.DbSignInfoMapper;
import com.zfsoft.service.dbaccess.data.sxSign.DbSignInfo;
import com.zfsoft.service.dbaccess.data.sxSign.DbSignInfoExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.service.scSign.data.SxSign;
import com.zfsoft.service.scSign.data.SxSignDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author ChangSheng
 * @Date 15:40 2022/6/16
 * @Description 签名配置
 **/
@Service
@Slf4j
public class SxSignManager {

    @Resource
    private DbSignInfoMapper signInfoMapper;

    @Resource
    private EvictSettingManager evictSettingManager;

    //查询列表
    public List<SxSign> getSignList(SxSign sxSign){
        DbSignInfoExample db = new DbSignInfoExample();
        DbSignInfoExample.Criteria criteria = db.createCriteria();
        if(null!=sxSign){
            //材料id
            if(StrUtil.isNotEmpty(sxSign.getMaterialOid())){
                criteria.andMaterialOidEqualTo(sxSign.getMaterialOid());
            }
        }
        criteria.andDeleteStatusEqualTo(BaseStaticParameter.NO);
        // 增加排序
        db.setOrderByClause(" sort asc");
        List<DbSignInfo> dbSignInfoList = signInfoMapper.selectByExample(db);
        List<SxSign> signList = new ArrayList<>();
        for(DbSignInfo dbSignInfo : dbSignInfoList){
            SxSign sign = new SxSign();
            BeanUtil.copyProperties(dbSignInfo,sign);
            signList.add(sign);
        }
        return signList;
    }

    //新增/修改签名配置
    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int saveOrUpdateSxSign(SxSignDto signDto) throws Exception{
        Set<String> hashSet = new HashSet<String>();
        List<String> list = new ArrayList<>();
        int index=0;
        for (SxSign temp : signDto.getSignList()) {
            hashSet.add(temp.getRoleName());
            hashSet.add(String.valueOf(temp.getSort()));
            list.add(temp.getRoleName());
            list.add(String.valueOf(temp.getSort()));
            if (hashSet.size() != list.size()) {
               //角色名重复
                throw new Exception("角色名或排序重复，请重新添加");
            }
            DbSignInfo dbSignInfo = new DbSignInfo();
            BeanUtils.copyProperties(temp,dbSignInfo);
            if (null == temp.getId()) {
                dbSignInfo.setMaterialOid(signDto.getMaterialOid());
                dbSignInfo.setCreateDate(new Date());
                dbSignInfo.setDeleteStatus("N");
                index = signInfoMapper.insert(dbSignInfo);
            }else {
                index = signInfoMapper.updateByPrimaryKeySelective(dbSignInfo);
            }
            temp.setId(dbSignInfo.getId());
            //清除缓存
            evictSettingManager.evictpbpjManage(dbSignInfo.getId());
        }
        return index;
    }

    //逻辑删除
    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int deleteSxSignById(String id) {
        DbSignInfo dbSignInfo = signInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        dbSignInfo.setDeleteStatus("Y");
        int index = signInfoMapper.updateByPrimaryKeySelective(dbSignInfo);
        //清除缓存
        evictSettingManager.evictpbpjManage(dbSignInfo.getId());
        return index;
    }

    //根据id查询签署角色数据
    public SxSign getSignById(String id){
        DbSignInfo dbSignInfo = signInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        SxSign sign = new SxSign();
        BeanUtil.copyProperties(dbSignInfo,sign);
        return sign;
    }
}

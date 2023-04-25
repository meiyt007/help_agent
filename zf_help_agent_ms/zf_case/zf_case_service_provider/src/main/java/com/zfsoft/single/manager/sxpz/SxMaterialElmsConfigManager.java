package com.zfsoft.single.manager.sxpz;

import cn.hutool.core.util.IdUtil;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.dbaccess.dao.DbSxMaterialElmsConfigMapper;
import com.zfsoft.single.dbaccess.data.DbSxMaterialElmsConfig;
import com.zfsoft.single.dbaccess.data.DbSxMaterialElmsConfigExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName SxMaterialElmsConfigManagerImpl
 * @Description: 证照目录关联接口实现类
 * @Author liangxm
 * @Date 2020/11/08
 **/
@Service
@Slf4j
public class SxMaterialElmsConfigManager {
    @Resource
    private DbSxMaterialElmsConfigMapper dbSxMaterialElmsConfigMapper;


    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public void saveOrUpdate(@ValidGroups(groups = {SxMaterialElmsConfig.INSERT_GROUP.class}) SxMaterialElmsConfig SxMaterialElmsConfig) {
        DbSxMaterialElmsConfig dbSxMaterialElmsConfig = null;
        if (null!=SxMaterialElmsConfig.getId()) {
             dbSxMaterialElmsConfig = this.dbSxMaterialElmsConfigMapper.selectByPrimaryKey(SxMaterialElmsConfig.getId());
            Assert.notNull(SxMaterialElmsConfig, MessageFormat.format("更新对象不存在！对象id为{0}", SxMaterialElmsConfig.getId()));
            SxMaterialElmsConfig.setConfigOid(dbSxMaterialElmsConfig.getConfigOid());
            BeanUtils.copyProperties(SxMaterialElmsConfig, dbSxMaterialElmsConfig);
            dbSxMaterialElmsConfig.setModifyDate(new Date());
            this.dbSxMaterialElmsConfigMapper.updateByPrimaryKeySelective(dbSxMaterialElmsConfig);

        } else {
            dbSxMaterialElmsConfig  = new DbSxMaterialElmsConfig();
            BeanUtils.copyProperties(SxMaterialElmsConfig, dbSxMaterialElmsConfig);
            dbSxMaterialElmsConfig.setConfigOid(IdUtil.simpleUUID());
            dbSxMaterialElmsConfig.setModifyDate(new Date());

            dbSxMaterialElmsConfig.setCreateDate(new Date());
            dbSxMaterialElmsConfig.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
            this.dbSxMaterialElmsConfigMapper.insert(dbSxMaterialElmsConfig);
        }
        SxMaterialElmsConfig.setId(dbSxMaterialElmsConfig.getId());

    }

    /**
     * 根据业务主键查询信息
     * @param oid
     * @return
     */
    public SxMaterialElmsConfig getSxMaterialElmsConfigByOid(String oid) {
            Assert.hasLength(oid, "主键不能为空！");
            DbSxMaterialElmsConfig dbSxMaterialElmsConfig = this.dbSxMaterialElmsConfigMapper.selectByPrimaryKey(Long.valueOf(oid));
            SxMaterialElmsConfig SxMaterialElmsConfig= new SxMaterialElmsConfig();
            BeanUtils.copyProperties(dbSxMaterialElmsConfig,SxMaterialElmsConfig);

            return SxMaterialElmsConfig;
        }
    /**
     * 根据业务主键查询信息
     * @param materialOid
     * @return
     */
    public SxMaterialElmsConfig getSxMaterialElmsConfigByMaterOid(String materialOid,String serviceOid) {
        Assert.hasLength(materialOid, "材料id不能为空！");
        DbSxMaterialElmsConfig dbSxMaterialElmsConfig = this.dbSxMaterialElmsConfigMapper.selectByMaterialOid(materialOid);
        if(dbSxMaterialElmsConfig==null){
            dbSxMaterialElmsConfig = new DbSxMaterialElmsConfig();
            dbSxMaterialElmsConfig.setMaterialOid(materialOid);

        }
        dbSxMaterialElmsConfig.setConfigOid(serviceOid);

        SxMaterialElmsConfig sxMaterialElmsConfig= new SxMaterialElmsConfig();
        BeanUtils.copyProperties(dbSxMaterialElmsConfig,sxMaterialElmsConfig);

        return sxMaterialElmsConfig;
    }

    public SxMaterialElmsConfig getElecConfigByMaterialOid(String materialOid) {
        Assert.hasLength(materialOid, "材料id不能为空！");
        DbSxMaterialElmsConfig dbSxMaterialElmsConfig = this.dbSxMaterialElmsConfigMapper.selectByMaterialOid(materialOid);
        if(dbSxMaterialElmsConfig!=null){
            SxMaterialElmsConfig sxMaterialElmsConfig= new SxMaterialElmsConfig();
            BeanUtils.copyProperties(dbSxMaterialElmsConfig,sxMaterialElmsConfig);
            return sxMaterialElmsConfig;

        }
        return null;
    }

    public List<SxMaterialElmsConfig> getElecConfigByMaterialOids(List<String> materialOids){
        DbSxMaterialElmsConfigExample example=new DbSxMaterialElmsConfigExample();
        DbSxMaterialElmsConfigExample.Criteria cratic=example.createCriteria();
        if(materialOids!=null&&materialOids.size()>0){
            cratic.andMaterialOidIn(materialOids);
        }
       List<DbSxMaterialElmsConfig> list= this.dbSxMaterialElmsConfigMapper.selectByExample(example);
        if(list!=null && list.size()>0){
          return  list.stream().map(material->{
                SxMaterialElmsConfig sxMaterialElmsConfig=new SxMaterialElmsConfig();
                BeanUtils.copyProperties(material,sxMaterialElmsConfig);
                return sxMaterialElmsConfig;
            }).collect(Collectors.toList());
        }
        return null;
    }

}

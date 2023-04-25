package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxService.DbRepeatFieldConfigMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbRepeatFieldConfig;
import com.zfsoft.service.dbaccess.data.sxService.DbRepeatFieldConfigExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.RepeatFieldConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName RepeatFieldConfigManager
 * @Description 字段重复设置manager
 * @Author xiayj
 * @Date 2021/7/20 13:58
 **/
@Service
@Slf4j
public class RepeatFieldConfigManager {


    @Resource
    private DbRepeatFieldConfigMapper dbRepeatFieldConfigMapper;


    /**
     * @param isAble
     * @param pageNumber
     * @param pageSize
     * @description: 字段去重配置分页查询
     * @return: com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.repeatField.RepeatFieldConfig>
     * @author: xiayj
     * @date: 2021/7/20
     */
    public PageResult<RepeatFieldConfig> queryRepeatFieldWithPage(Integer isAble, String masterField, Integer pageNumber, Integer pageSize){
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbRepeatFieldConfig entity =  new DbRepeatFieldConfig();
        if(isAble!=null){
            entity.setIsAble(isAble);
        }
        if(StringUtils.isNotEmpty(masterField)){
            entity.setMasterField(masterField);
        }
        entity.setDelFlag(0);
        Page<DbRepeatFieldConfig> dbRepeatFieldConfigs = (Page<DbRepeatFieldConfig>) dbRepeatFieldConfigMapper.selectMasterFieldList(entity);
        PageResult<RepeatFieldConfig> pageResult = new PageResult<>(dbRepeatFieldConfigs.getPageNum(),dbRepeatFieldConfigs.getPageSize(),dbRepeatFieldConfigs.getTotal());
        List<RepeatFieldConfig> collect = dbRepeatFieldConfigs.stream().map(dbRepeatFieldConfig -> {
            RepeatFieldConfig repeatFieldConfig = new RepeatFieldConfig();
            BeanUtils.copyProperties(dbRepeatFieldConfig, repeatFieldConfig);
            repeatFieldConfig.setSlaveFieldList(dbRepeatFieldConfigMapper.selectSlaveFieldByMasterField(repeatFieldConfig.getMasterField()));
            if(!repeatFieldConfig.getSlaveFieldList().isEmpty()){
                repeatFieldConfig.setSlaveField(repeatFieldConfig.getSlaveFieldList().toString());
            }
            return repeatFieldConfig;
        }).collect(Collectors.toList());
        pageResult.setData(collect);
        return pageResult;
    }

    /***
     * @param configOid
     * @description: 获取去重字段详细信息
     * @return: com.zfsoft.data.repeatField.RepeatFieldConfig
     * @author: xiayj
     * @date: 2021/7/20
     */
    public RepeatFieldConfig getRepeatFieldConfig(String configOid){
        DbRepeatFieldConfig dbRepeatFieldConfig = dbRepeatFieldConfigMapper.selectDbRepeatFieldConfigByOid(configOid);
        if(dbRepeatFieldConfig==null){
            throw new ResultInfoException("找不到业务主键对应数据");
        }
        RepeatFieldConfig repeatFieldConfig = new RepeatFieldConfig();
        BeanUtils.copyProperties(dbRepeatFieldConfig,repeatFieldConfig);
        repeatFieldConfig.setSlaveFieldList(dbRepeatFieldConfigMapper.selectSlaveFieldByMasterField(repeatFieldConfig.getMasterField()));
        return repeatFieldConfig;
    }




    /***
     * @param repeatFieldConfig
     * @description: 保存/更新repeatFieldConfig
     * @return: void
     * @author: xiayj
     * @date: 2021/7/20
     */
    public void saveOrUpdateRepeatFieldConfig(RepeatFieldConfig repeatFieldConfig){
        /*
         * 保存方法需要先确定主字段，然后删除所有主字段对应数据，重新生成所有从字段数据。
         */
        //修改方法先删除之前的数据
        if(!StringUtils.isEmpty(repeatFieldConfig.getConfigOid())){
            DbRepeatFieldConfig dbRepeatFieldConfig = dbRepeatFieldConfigMapper.selectDbRepeatFieldConfigByOid(repeatFieldConfig.getConfigOid());
            dbRepeatFieldConfigMapper.deleteByMaterField(dbRepeatFieldConfig.getMasterField());
        }
        String[] split = repeatFieldConfig.getSlaveField().split(",");
        Set<String> set = new HashSet<>(Arrays.asList(split));
        for(String slaveField : set){
            List<DbRepeatFieldConfig> dbRepeatFieldConfigs = this.queryDbRepeatFieldConfigBySlaveName(slaveField);
            if(!dbRepeatFieldConfigs.isEmpty()){
                throw new ResultInfoException("已存在相同的从字段");
            }
            DbRepeatFieldConfig dbRepeatFieldConfig = new DbRepeatFieldConfig();
            dbRepeatFieldConfig.setSlaveField(slaveField);
            dbRepeatFieldConfig.setMasterField(repeatFieldConfig.getMasterField());
            dbRepeatFieldConfig.setConfigOid(IdUtil.simpleUUID());
            dbRepeatFieldConfig.setDelFlag(0);
            dbRepeatFieldConfig.setIsAble(1);
            dbRepeatFieldConfig.setCreateDate(new Date());
            dbRepeatFieldConfig.setModifyDate(new Date());
            dbRepeatFieldConfigMapper.insert(dbRepeatFieldConfig);
        }
    }

    /***
     * @param configOid
     * @description: 删除去重配置
     * @return: void
     * @author: xiayj
     * @date: 2021/7/20
     */
    public void deleteRepeatFieldConfig(String configOid){
        DbRepeatFieldConfig dbRepeatFieldConfig = dbRepeatFieldConfigMapper.selectDbRepeatFieldConfigByOid(configOid);
        if(dbRepeatFieldConfig==null){
            throw new ResultInfoException("找不到业务主键对应数据");
        }
        dbRepeatFieldConfigMapper.deleteByMaterField(dbRepeatFieldConfig.getMasterField());
    }


    /***
     * @param configOid
     * @description: 启用/禁用去重配置
     * @return: void
     * @author: xiayj
     * @date: 2021/7/20
     */
    public void isAbleRepeatFieldConfig(String configOid){
        DbRepeatFieldConfig dbRepeatFieldConfig = dbRepeatFieldConfigMapper.selectDbRepeatFieldConfigByOid(configOid);
        if(dbRepeatFieldConfig==null){
            throw new ResultInfoException("找不到业务主键对应数据");
        }
        if(dbRepeatFieldConfig.getIsAble()==1){
            dbRepeatFieldConfigMapper.isAbleMasterFieldConfig(dbRepeatFieldConfig.getMasterField(),0);
        }else {
            dbRepeatFieldConfig.setIsAble(1);
            dbRepeatFieldConfigMapper.isAbleMasterFieldConfig(dbRepeatFieldConfig.getMasterField(),1);
        }

    }

    /***
     * @description: 查询去重配置并转化成map
     * @return: Map<String, String>
     * @author: xiayj
     * @date: 2021/7/28
     */
    public Map<String,String> queryConfigMapInfo(){
        List<DbRepeatFieldConfig> dbRepeatFieldConfigs = this.queryAllDbRepeatFieldConfig();
        Map<String,String> map = new HashMap<>(dbRepeatFieldConfigs.size());
        for(DbRepeatFieldConfig config : dbRepeatFieldConfigs){
            map.put(config.getSlaveField(),config.getMasterField());
        }
        return map;
    }

    /***
     * @description: 获取所有在用配置
     * @return: java.util.List<com.zfsoft.dbaccess.data.sxService.DbRepeatFieldConfig>
     * @author: xiayj
     * @date: 2021/7/28
     */
    private List<DbRepeatFieldConfig> queryAllDbRepeatFieldConfig(){
        DbRepeatFieldConfigExample example = new DbRepeatFieldConfigExample();
        DbRepeatFieldConfigExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andIsAbleEqualTo(1);
        return dbRepeatFieldConfigMapper.selectByExample(example);
    }



    /***
     * 查询从字段名称对应数据
     * @param slaveName
     * @return: java.util.List<com.zfsoft.dbaccess.data.sxService.DbRepeatFieldConfig>
     * @author: xiayj
     * @date: 2021/8/2
     */
    private  List<DbRepeatFieldConfig> queryDbRepeatFieldConfigBySlaveName(String slaveName){
        DbRepeatFieldConfigExample example = new DbRepeatFieldConfigExample();
        DbRepeatFieldConfigExample.Criteria criteria = example.createCriteria();
        criteria.andSlaveFieldEqualTo(slaveName);
        criteria.andDelFlagEqualTo(0);
        return dbRepeatFieldConfigMapper.selectByExample(example);
    }

}

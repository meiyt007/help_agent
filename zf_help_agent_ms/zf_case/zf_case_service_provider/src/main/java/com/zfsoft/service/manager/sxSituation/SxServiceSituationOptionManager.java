package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceSituationOptionMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceSituationOption;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceSituationOptionExample;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.SxServiceSituationOption;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 情形、选项关系表 实现类
 * @date 2020/11/3 13:26
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceSituationOptionManager {

    @Resource
    private DbSxServiceSituationOptionMapper dbSxServiceSituationOptionMapper;
    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;
    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;

    /**
     * 单条情形，少见
     * @param situationOid
     * @return
     */
    public SxServiceSituationOption getSxServiceSituationOptionBySituationOid(String situationOid) {

        SxServiceSituationOption sxServiceSituationOption =new SxServiceSituationOption();

        DbSxServiceSituationOption dbSxServiceSituationOption = dbSxServiceSituationOptionMapper.selectBySituationOid(situationOid);

        if (null == dbSxServiceSituationOption) {
            dbSxServiceSituationOption = new DbSxServiceSituationOption();
        }

        BeanUtils.copyProperties(dbSxServiceSituationOption,sxServiceSituationOption);

        return sxServiceSituationOption;
    }

    /**
     * 批量查询
     * @param situationOid
     * @return
     */
    public List<SxServiceSituationOption> getSxServiceSituationOptionsBySituationOid(String situationOid) {
        DbSxServiceSituationOptionExample dbSxServiceSituationOptionExample = new DbSxServiceSituationOptionExample();
        DbSxServiceSituationOptionExample.Criteria criteria = dbSxServiceSituationOptionExample.createCriteria();
        if(StrUtil.isNotEmpty(situationOid)){
            criteria.andSituationOidEqualTo(situationOid);
        }
        criteria.andDeleteStatusEqualTo((short)0);
        List<DbSxServiceSituationOption> dbSxServiceSituationOptions = dbSxServiceSituationOptionMapper.selectByExample(dbSxServiceSituationOptionExample);
        List<SxServiceSituationOption> sxServiceSituationOptions = dbSxServiceSituationOptions.stream().map(dbSxServiceSituationOption -> {
            SxServiceSituationOption sxServiceSituationOption = new SxServiceSituationOption();
            BeanUtils.copyProperties(dbSxServiceSituationOption,sxServiceSituationOption);
            return sxServiceSituationOption;
        }).collect(Collectors.toList());
        return sxServiceSituationOptions;
    }

    /**
     * 查询情形下所有选项值和标题
     * @param situationOid
     * @return
     */
    public Map<String, Object> getOptionTitleAndValsOfSituation(String situationOid) {
        Map<String, Object> returnMap = new HashMap<>();
        //取出相同情形下的选项值oid
        List<String> optionValOids;
        //标题集合
        List<SxServiceOptionTitle> sxServiceOptionTitles = Lists.newArrayList();
        if (!StringUtils.isEmpty(situationOid)) {
            List<SxServiceSituationOption> sxServiceSituationOptions = this.getSxServiceSituationOptionsBySituationOid(situationOid);
            if(!CollectionUtils.isEmpty(sxServiceSituationOptions)){
                //相同的情形放一个节点就行
                SxServiceSituationOption situationOption = sxServiceSituationOptions.get(0);
                //取出相同情形下的选项值oid
                optionValOids = sxServiceSituationOptions.stream().map(SxServiceSituationOption::getOptionOid).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(optionValOids)) {
                    for (String optionValOid : optionValOids) {
                        //根据sxServiceSituationOption的 optionOid 事项选项主键，查询对应的选项值
                        SxServiceOptionVal sxServiceOptionVal = this.sxServiceOptionValManager.getSxServiceOptionValByOid(optionValOid);
                        if (!ObjectUtils.isEmpty(sxServiceOptionVal)) {
                            //查询对应的选项标题的全部选项值
                            List<SxServiceOptionVal> sxServiceOptionVals = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionVal.getTitleOid());
                            //查询选项值所对应的选项标题
                            SxServiceOptionTitle sxServiceOptionTitle = this.sxServiceOptionTitleManager.getServiceOptionTitleByOid(sxServiceOptionVal.getTitleOid());
                            if (!CollectionUtils.isEmpty(sxServiceOptionVals)) {
                                for (SxServiceOptionVal serviceOptionVal : sxServiceOptionVals) {
                                    //判断是否被选中
                                    if (sxServiceOptionVal.getOid().equals(serviceOptionVal.getOid())) {
                                        serviceOptionVal.setIsSelected("1");
                                    }
                                }
                                if (sxServiceOptionTitle !=null) {
                                    sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
                                    sxServiceOptionTitles.add(sxServiceOptionTitle);
                                }
                            }
                        }
                    }
                    situationOption.setSxServiceOptionTitles(sxServiceOptionTitles);
                }
                returnMap.put("sxServiceSituationOption",situationOption);
            }
        }
        //多此一举是用来去除returnMap里面的 重复节点
        JSONObject jsonObject = JSONObject.fromObject(returnMap);
        returnMap = JSON.parseObject(jsonObject.toString(), HashMap.class);
        return returnMap;
    }

    @Transactional(rollbackFor = Exception.class)
    public int saveSxServiceSituations(List<SxServiceSituationOption> sxServiceSituationOptions) {
        if(sxServiceSituationOptions==null||sxServiceSituationOptions.size()<=0){
            return -1;
        }
        if(sxServiceSituationOptions.size()==1){
            SxServiceSituationOption  s =  sxServiceSituationOptions.get(0);
            String  situationOid = s.getSituationOid();
            if("-1".equalsIgnoreCase(s.getOptionOid())){
                DbSxServiceSituationOption  dbSxServiceSituationOption = new  DbSxServiceSituationOption();
                dbSxServiceSituationOption.setDeleteStatus((short)1);
                DbSxServiceSituationOptionExample delEx = new DbSxServiceSituationOptionExample();
                DbSxServiceSituationOptionExample.Criteria delExCriteria = delEx.createCriteria();
                delExCriteria.andSituationOidEqualTo(situationOid);
                delExCriteria.andDeleteStatusEqualTo((short)0);
                dbSxServiceSituationOptionMapper.updateByExampleSelective(dbSxServiceSituationOption,delEx);
                return 1;
            }
        }
        String  situationOid =  sxServiceSituationOptions.get(0).getSituationOid();
        DbSxServiceSituationOptionExample dbSxServiceSituationOptionExample = new DbSxServiceSituationOptionExample();
        DbSxServiceSituationOptionExample.Criteria criteria = dbSxServiceSituationOptionExample.createCriteria();
        criteria.andSituationOidEqualTo(situationOid);
        criteria.andDeleteStatusEqualTo((short)0);
        List<DbSxServiceSituationOption> dbSxServiceSituationOptions = dbSxServiceSituationOptionMapper.selectByExample(dbSxServiceSituationOptionExample);

        List<String>  dbOptionOids = new ArrayList<>();
        List<SxServiceSituationOption> sxServiceSituationOptionList = new ArrayList<>();
        for(DbSxServiceSituationOption dbSxServiceSituationOption : dbSxServiceSituationOptions){
            dbOptionOids.add(dbSxServiceSituationOption.getOptionOid());
            SxServiceSituationOption  ds = new  SxServiceSituationOption();
            BeanUtils.copyProperties(dbSxServiceSituationOption,ds);
            sxServiceSituationOptionList.add(ds);
        }
        List<String>  webOptionOids = new ArrayList<>();
        for(SxServiceSituationOption  sxServiceSituationOption : sxServiceSituationOptions){
            webOptionOids.add(sxServiceSituationOption.getOptionOid());
        }
        //数据库没有，前端有，新增。
        for(SxServiceSituationOption sxServiceSituationOption : sxServiceSituationOptions){
            if(!dbOptionOids.contains(sxServiceSituationOption.getOptionOid())){
                DbSxServiceSituationOption  dbSxServiceSituationOption = new  DbSxServiceSituationOption();
                BeanUtils.copyProperties(sxServiceSituationOption,dbSxServiceSituationOption);
                dbSxServiceSituationOption.setDeleteStatus((short)0);
                dbSxServiceSituationOption.setCreateDate(new Date());
                dbSxServiceSituationOptionMapper.insert(dbSxServiceSituationOption);
            }
        }
        //前端没有，数据库有，删除
        for(SxServiceSituationOption sxServiceSituationOption : sxServiceSituationOptionList){
            if(!webOptionOids.contains(sxServiceSituationOption.getOptionOid())){
                DbSxServiceSituationOption  dbSxServiceSituationOption = new  DbSxServiceSituationOption();
                BeanUtils.copyProperties(sxServiceSituationOption,dbSxServiceSituationOption);
                dbSxServiceSituationOption.setDeleteStatus((short)1);
                dbSxServiceSituationOption.setModifyDate(new Date());
                //dbSxServiceSituationOptionMapper.up(dbSxServiceSituationOption);
                DbSxServiceSituationOptionExample delEx = new DbSxServiceSituationOptionExample();
                DbSxServiceSituationOptionExample.Criteria delExCriteria = delEx.createCriteria();
                delExCriteria.andSituationOidEqualTo(situationOid);
                delExCriteria.andOptionOidEqualTo(sxServiceSituationOption.getOptionOid());
                delExCriteria.andDeleteStatusEqualTo((short)0);
                dbSxServiceSituationOptionMapper.updateByExampleSelective(dbSxServiceSituationOption,delEx);
            }
        }
        return 1;
    }


}

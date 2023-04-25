package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbServiceOptionRelMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbServiceOptionRel;
import com.zfsoft.service.dbaccess.data.sxSituation.DbServiceOptionRelExample;
import com.zfsoft.service.dbaccess.data.sxSituation.DbServiceOptionRelWithBLOBs;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxService.SxServiceManager;
import com.zfsoft.service.sxSituation.data.ServiceOptionRel;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 大综窗选项关联信息 实现类
 * @date 2020/11/3 10:45
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class ServiceOptionRelManager {

    @Resource
    private DbServiceOptionRelMapper dbServiceOptionRelMapper;
    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;
    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;
    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;
    @Resource
    private SxServiceManager sxServiceManager;

    public ServiceOptionRel getServiceOptionRelByOid(String oid){
        DbServiceOptionRel dbServiceOptionRel = dbServiceOptionRelMapper.selectByOid(oid);
        ServiceOptionRel serviceOptionRel = null;
        if (null == dbServiceOptionRel) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询大综窗选项关联信息表单为空！");
        }else{
            serviceOptionRel = new ServiceOptionRel();
            BeanUtils.copyProperties(dbServiceOptionRel,serviceOptionRel);
        }
        return serviceOptionRel;
    }

    public List<ServiceOptionRel> getServiceOptionRelByServiceOid(String serviceOid) {
        DbServiceOptionRelExample dbServiceOptionRelExample = new DbServiceOptionRelExample();
        DbServiceOptionRelExample.Criteria criteria = dbServiceOptionRelExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short)0);
        List<DbServiceOptionRelWithBLOBs> dbServiceOptionRels = dbServiceOptionRelMapper.selectByExampleWithBLOBs(dbServiceOptionRelExample);
        List<ServiceOptionRel> serviceOptionRels = dbServiceOptionRels.stream().map(serviceOptionRel -> {
            ServiceOptionRel sOptionRel = new ServiceOptionRel();
            BeanUtils.copyProperties(serviceOptionRel,sOptionRel);
            return sOptionRel;
        }).collect(Collectors.toList());
        return serviceOptionRels;
    }

    /**
     * 根据这 serviceOid optionValOid 查询和该选项值关联的选项标题和选项值及其 optionValOid 对应的材料
     * 根据这 serviceOid optionTitleOid 查询该选项标题是否存在与其关联的选项
     * @param serviceOid
     * @param optionValOid
     * @param optionTitleOid
     * @return
     */
    public Map<String, Object> getOptionRelationDataById(String serviceOid, String optionValOid, String optionTitleOid) {
        Map<String, Object> returnMap = Maps.newHashMap();
        //关联的 选项标题集合
        List<SxServiceOptionTitle> sxServiceOptionTitles = Lists.newArrayList();
        //若 optionValOid 为空且 optionTitleOid 不为空，就是查询该选项标题下是否存在与其关联的选项  0-无关联  1-有关联  默认无关联 0
        if (!StringUtils.isEmpty(optionTitleOid) && StringUtils.isEmpty(optionValOid)) {
            List<String> titleOids = this.assertOptioanTitelCorrelation(serviceOid,optionTitleOid);
            //查询关联选项标题对应的选项值
            this.getTitleAndOptions(returnMap, sxServiceOptionTitles, titleOids);
        }else if(StringUtils.isEmpty(optionTitleOid) && !StringUtils.isEmpty(optionValOid)){
            //查询全部的关联数据查询和该选项值关联的选项标题和选项值 及其 optionValOid 对应的材料
            List<String> titleOids = this.getTitleOidListByServiceOidAndOptionOid(serviceOid, optionValOid);
            //根据过滤到的数据查询关联的标题和选项值
            this.getTitleAndOptions(returnMap, sxServiceOptionTitles, titleOids);
            //查询传过来的 optionValOid 对应的选项值对应的材料
            //查询细化材料选项值关系表
            if (!StringUtils.isEmpty(optionValOid)) {
                List<SxServiceMateOptRel> sxServiceMateOptRels = this.sxServiceMateOptRelManager.getSxServiceMateOptRelsByOids(optionValOid,null,null);
                if(!CollectionUtils.isEmpty(sxServiceMateOptRels)){
                    this.sxServiceManager.getSxServiceMateOptRels(sxServiceMateOptRels);
                    returnMap.put("sxServiceMateOptRels",sxServiceMateOptRels);
                }
            }
            //多此一举是用来去除returnMap里面的 重复节点
            JSONObject jsonObject = JSONObject.fromObject(returnMap);
            returnMap = JSON.parseObject(jsonObject.toString(), HashMap.class);
        }else{
            String msg = "optionValOid和optionTitleOid，不要都传。传optionTitleOid 查询与该选项标题所关联的选项标题及选项值；传optionValOid则查询和该选项值关联的选项标题和选项值及其 optionValOid 对应的材料。";
            returnMap.put("msg",msg);
        }
        return returnMap;
    }

    /**
     * 查询关联选项标题对应的选项值
     * @param returnMap
     * @param sxServiceOptionTitles
     * @param titleOids
     */
    private void getTitleAndOptions(Map<String, Object> returnMap, List<SxServiceOptionTitle> sxServiceOptionTitles, List<String> titleOids) {
        if (!CollectionUtils.isEmpty(titleOids)){
            for (int i = 0; i < titleOids.size(); i++) {
                SxServiceOptionTitle sxServiceOptionTitle = this.sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleOids.get(i));
                if(!ObjectUtils.isEmpty(sxServiceOptionTitle)){
                    //查询对应的选项标题的全部选项值
                    List<SxServiceOptionVal> sxServiceOptionVals = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(titleOids.get(i));
                    if (!CollectionUtils.isEmpty(sxServiceOptionVals)){
                        sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
                    }
                    sxServiceOptionTitles.add(sxServiceOptionTitle);
                }
            }
            returnMap.put("sxServiceOptionTitles", sxServiceOptionTitles);
        }
    }

    /**
     * 根据这 serviceOid optionTitleOid 查询该选项标题是否存在与其关联的选项
     * @param serviceOid
     * @param optionTitleOid
     * @return
     */
    private List<String> assertOptioanTitelCorrelation(String serviceOid, String optionTitleOid) {
        //关联的 选项标题oid集合
        List<String> returnTitleOids = Lists.newArrayList();
        List<ServiceOptionRel> serviceOptionRels = this.getServiceOptionRelByServiceOid(serviceOid);
        if (!ObjectUtils.isEmpty(serviceOptionRels)) {
            for(ServiceOptionRel serviceOptionRel:serviceOptionRels){
                if(!StringUtils.isEmpty(serviceOptionRel.getValueOids())){
                    List<String> templList = Lists.newArrayList();
                    List<String> titleOids =  Arrays.asList(serviceOptionRel.getTitleOids().split(","));
                    templList.addAll(titleOids);
                    //过滤出
                    if (!CollectionUtils.isEmpty(templList) && templList.contains(optionTitleOid)) {
                        //把入参的数据移除，查询剩下所关联的
                        templList.remove(optionTitleOid);
                        returnTitleOids.addAll(titleOids);
                    }
                }
            }
        }
        return returnTitleOids;
    }

    /**
     * 获取关联的选项标题 - sxServiceManager也在用
     * @param serviceOid 事项oid
     * @param optionValOid 选项值oid
     * @return
     */
    public List<String> getTitleOidListByServiceOidAndOptionOid(String serviceOid, String optionValOid) {
        //关联的 选项标题oid集合
        List<String> returnTitleOids = Lists.newArrayList();
        List<ServiceOptionRel> serviceOptionRels = this.getServiceOptionRelByServiceOid(serviceOid);
        if (!ObjectUtils.isEmpty(serviceOptionRels)) {
            for(ServiceOptionRel serviceOptionRel:serviceOptionRels){
                if(!StringUtils.isEmpty(serviceOptionRel.getValueOids())){
                    List<String> valueOids =  Arrays.asList(serviceOptionRel.getValueOids().split(","));
                    //过滤出
                    if (!CollectionUtils.isEmpty(valueOids) && valueOids.contains(optionValOid)) {
                        if(!StringUtils.isEmpty(serviceOptionRel.getTitleOids())){
                            List<String> titleOids = Arrays.asList(serviceOptionRel.getTitleOids().split(","));
                            returnTitleOids.addAll(titleOids);
                        }
                    }
                }
            }
        }
        return returnTitleOids;
    }

    public List<ServiceOptionRel> getServiceOptionRelListByOids(String serviceOid, String valueOid) {
        if(StrUtil.isEmpty(serviceOid)){
            throw new ResultInfoException("所属事项业务主键为空！");
        }
        if(StrUtil.isEmpty(valueOid)){
            throw new ResultInfoException("标题下选项值业务主键为空！");
        }
        List<DbServiceOptionRel> dbServiceOptionRels = dbServiceOptionRelMapper.getServiceOptionRelListByOids(serviceOid,valueOid);
        List<ServiceOptionRel> serviceOptionRelList = dbServiceOptionRels.stream().map(dbServiceOptionRel -> {
            ServiceOptionRel serviceOptionRel = new ServiceOptionRel();
            BeanUtils.copyProperties(dbServiceOptionRel,serviceOptionRel);
            return serviceOptionRel;
        }).collect(Collectors.toList());
        return serviceOptionRelList;
    }

    public List<ServiceOptionRel> getServiceOptionRelListByOidsAndValueOidNull(String serviceOid, String valueOid) {
        if(StrUtil.isEmpty(serviceOid)){
            throw new ResultInfoException("所属事项业务主键为空！");
        }
        if(StrUtil.isEmpty(valueOid)){
            //throw new ResultInfoException("标题下选项值业务主键为空！");
            return null;
        }
        List<DbServiceOptionRel> dbServiceOptionRels = dbServiceOptionRelMapper.getServiceOptionRelListByOids(serviceOid,valueOid);
        List<ServiceOptionRel> serviceOptionRelList = dbServiceOptionRels.stream().map(dbServiceOptionRel -> {
            ServiceOptionRel serviceOptionRel = new ServiceOptionRel();
            BeanUtils.copyProperties(dbServiceOptionRel,serviceOptionRel);
            return serviceOptionRel;
        }).collect(Collectors.toList());
        return serviceOptionRelList;
    }


    public PageResult<ServiceOptionRel> queryServiceOptionRelDto(String serviceOid, Integer pageNum, Integer pageSize){
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum,pageSize);
        //根据事项查询所有关系
       DbServiceOptionRelExample example=new DbServiceOptionRelExample();
       DbServiceOptionRelExample.Criteria critra=example.createCriteria();
       if(StringUtil.isNotEmpty(serviceOid)){
           critra.andServiceOidEqualTo(serviceOid);
       }
        critra.andDeleteStatusEqualTo((short) 0);
       example.setOrderByClause(" modify_date desc");
        Page<DbServiceOptionRelWithBLOBs> list= (Page)dbServiceOptionRelMapper.selectByExampleWithBLOBs(example);
        PageResult<ServiceOptionRel> pageResult = new PageResult<>(list.getPageNum(),list.getPageSize(),list.getTotal());
        List<ServiceOptionRel> relList=list.stream().map(optRel->{
            ServiceOptionRel rel=new ServiceOptionRel();
            BeanUtils.copyProperties(optRel,rel);
            return rel;
        }).collect(Collectors.toList());
        pageResult.setData(relList);
        return pageResult;
    }

    public String saveOrUpdateServiceOptionRel(ServiceOptionRel serviceOptionRel){
        if(serviceOptionRel==null){
            return "请选择关联信息！";
        }
        if(StringUtil.isEmpty(serviceOptionRel.getTitleOids())){
            return "请选择关联标题！";
        }
        if(StringUtil.isEmpty(serviceOptionRel.getValueOids())){
            return "请选择关联选项！";
        }
        // 校验重复数据 start
        // 一组选项值 只能添加一次
        //通过事项id查询所有的关联列表
        List<ServiceOptionRel> serviceOptionRelList = getServiceOptionRelByServiceOid(serviceOptionRel.getServiceOid());
        if(serviceOptionRelList != null){
            String valueOids = serviceOptionRel.getValueOids().trim();
            List<String> valueOidList = Arrays.asList(valueOids.split(","));
            for (ServiceOptionRel optionRel : serviceOptionRelList) {
                if(StrUtil.isNotBlank(serviceOptionRel.getOid()) && serviceOptionRel.getOid().equals(optionRel.getOid())) {
                    continue;
                }
                /*if(valueOids.length() != optionRel.getValueOids().length()) {
                    continue;
                }*/
                // 已存在
                if(compareStrListNoSort(valueOidList,Arrays.asList(optionRel.getValueOids().split(",")))) {
                    return "当前选项值组合已存在，不能添加，请修改";
                }
            }
        }
        //选项值不能勾选自己的标题
        List<String> titleOidList = Arrays.asList(serviceOptionRel.getTitleOids().split(","));
        List<String> valueOidList = Arrays.asList(serviceOptionRel.getValueOids().trim().split(","));
        for(String titleOid : titleOidList){
            List<SxServiceOptionVal> vals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(titleOid);
            if(vals!=null&&vals.size()>0){
                for(SxServiceOptionVal  sxServiceOptionVal : vals){
                    if(valueOidList.contains(sxServiceOptionVal.getOid())){
                        return "选项值不能勾选自己的标题";
                    }
                }
            }
        }

        if(StringUtil.isNotEmpty(serviceOptionRel.getOid())){
            //查询信息
            DbServiceOptionRelWithBLOBs dbServiceOptionRel = dbServiceOptionRelMapper.selectByOid(serviceOptionRel.getOid());
            if(dbServiceOptionRel!=null){
                dbServiceOptionRel.setValueOids(serviceOptionRel.getValueOids());
                dbServiceOptionRel.setTitleOids(serviceOptionRel.getTitleOids());
                dbServiceOptionRel.setModifyDate(new Date());
                dbServiceOptionRelMapper.updateByPrimaryKeyWithBLOBs(dbServiceOptionRel);
            }
        }else{
            DbServiceOptionRelWithBLOBs dbServiceOptionRelWithBLOBs=new DbServiceOptionRelWithBLOBs();
            BeanUtils.copyProperties(serviceOptionRel,dbServiceOptionRelWithBLOBs);
            dbServiceOptionRelWithBLOBs.setOid(UUID.randomUUID().toString().replaceAll("-",""));
            dbServiceOptionRelWithBLOBs.setDeleteStatus((short) 0);
            dbServiceOptionRelWithBLOBs.setModifyDate(new Date());
            dbServiceOptionRelMapper.insert(dbServiceOptionRelWithBLOBs);
        }
        return null;
    }

    /**
     * 比较两个list元素是否一样 不考虑顺序
     */
    public static boolean compareStrListNoSort(List<String> list1, List<String> list2) {
        if(list1 == null && list2 == null){return true;}
        if(list1 == null && list2 != null){ return false;}
        if(list2 == null && list1 != null){return false;}
        if(list1.size() != list2.size()){return false;}
        return list1.containsAll(list2) && list2.containsAll(list1);
    }

    /**
     * 批量删除
     * @param relOids
     */
    public void delBatchRel(String relOids){
        if(StringUtil.isNotEmpty(relOids)){
            String[] arr=relOids.split(",");
            for(String oid:arr){
                DbServiceOptionRelWithBLOBs dbServiceOptionRel = dbServiceOptionRelMapper.selectByOid(oid);
                if(dbServiceOptionRel!=null){
                    dbServiceOptionRel.setDeleteStatus((short) 1);
                    dbServiceOptionRel.setModifyDate(new Date());
                    dbServiceOptionRelMapper.updateByPrimaryKeyWithBLOBs(dbServiceOptionRel);
                }
            }
        }
    }
}

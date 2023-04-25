package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysAttaMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysDistrictMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysOrganMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.*;
import com.zfsoft.microservice.platform.feign.settings.SysDictFeignService;
import com.zfsoft.microservice.platform.util.BeanUtils;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SysDistrictServiceImpl
 * @Description 区划接口实现类
 * @Author wuxx
 * @Date2020-08-25 13:14
 * @Version V1.0
 **/

@Service
@Slf4j
@CacheConfig(cacheNames = "sys:district")
public class SysDistrictManager {

    @Resource
    private DbSysDistrictMapper dbSysDistrictMapper;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private EvictPlatformManager platformManager;

    @Resource
    private DbSysOrganMapper dbSysOrganMapper;

    @Resource
    private DbSysAttaMapper dbSysAttaMapper;

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysDistrict(@ValidGroups(groups = {SysDistrict.INSERT_GROUP.class}) SysDistrict sysDistrict) {
        if(null==sysDistrict){
            throw new ResultInfoException("区划信息不能为空！");
        }
        DbSysDistrict topDbSysDistrict = dbSysDistrictMapper.selectTopDbSysDistrict();
        if (null == sysDistrict.getParentOid() && null!=topDbSysDistrict && (null== sysDistrict.getId() || sysDistrict.getId().longValue()!=topDbSysDistrict.getId().longValue())) {
            throw new ResultInfoException("一级区划信息只能有一个");
        }
        if(null==sysDistrict.getId() && null == sysDistrict.getParentOid()){
            throw new ResultInfoException("没有上级区划信息，不能新增！");
        }
        SysDistrict sysDistrictParent = this.getSysDistrictByDistrictOid(sysDistrict.getParentOid());
        if(null == sysDistrictParent){
            throw new ResultInfoException("上级区划信息出错，请刷新重试！");
        }
        if(sysDistrictParent.getIsAble() == BaseStaticParameter.N) {
            throw new ResultInfoException("上级区划处于禁用状态，暂不能操作！");
        }
        String message = this.checkIsExistesDistrictCode(sysDistrict.getId(), sysDistrict.getCode());
        if(StrUtil.isNotEmpty(message)){
            throw new ResultInfoException(message);
        }
        // 设置区划信息的状态
        if (null==sysDistrict.getIsDelete()) {
            sysDistrict.setIsDelete(BaseStaticParameter.N);
        }
        if (null==sysDistrict.getIsAble()) {
            sysDistrict.setIsAble(BaseStaticParameter.Y);
        }
        if(null!=sysDistrict.getParentOid()){
            SysDistrict parentDistrict = this.getSysDistrictByDistrictOid(sysDistrict.getParentOid());
            if (null!= sysDistrict.getParentOid() &&  parentDistrict== null) {
                throw new ResultInfoException("上级区划信息不正确，无法保存！");
            }
        }
        DbSysDistrictExample example= new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = example.createCriteria();
        if(null!=sysDistrict.getParentOid()){
            criteria.andParentOidEqualTo(sysDistrict.getParentOid());
            criteria.andNameEqualTo(sysDistrict.getName());
            criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
            criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
            List<DbSysDistrict> distList = dbSysDistrictMapper.selectByExample(example);
            if (distList != null && distList.size() > 0) {
                // 当仅仅查询到一个区划信息时
                if (distList.size() == 1) {
                    DbSysDistrict dict = distList.get(0);
                    if (!dict.getId().equals(sysDistrict.getId())) {
                        throw new ResultInfoException("同一上级区划中，区划名称不能相同！");
                    }
                } else {
                    throw new ResultInfoException("同一上级区划中，区划名称不能相同！");
                }
            }
        }
        if(null == sysDistrict.getId()){
            //新增操作
            sysDistrict.setDistrictOid(IdUtil.simpleUUID());
        }
        DbSysDistrict dbSysDistrict = new DbSysDistrict();
        BeanUtils.copyProperties(sysDistrict,dbSysDistrict);
        dbSysDistrict.setDisNameStr(getSysDistrictDisName(null,dbSysDistrict));
        dbSysDistrict.setModifyDate(new Date());
        if(null == sysDistrict.getId()){
            dbSysDistrictMapper.insertSelective(dbSysDistrict);
        }
        sysDistrict.setDistrictOid(dbSysDistrict.getDistrictOid());
        sysDistrict.setId(dbSysDistrict.getId());
        sysDistrict.setDisNameStr(dbSysDistrict.getDisNameStr());
        if(null!=dbSysDistrict.getParentOid()){
            SysDistrict parentDistrict = this.getSysDistrictByDistrictOid(sysDistrict.getParentOid());
            sysDistrict.setParentName(parentDistrict.getName());
            if (parentDistrict != null) {
                dbSysDistrict.setPath(parentDistrict.getPath() + dbSysDistrict.getDistrictOid() + ".");
            }
        }else {
            dbSysDistrict.setPath("-1." + dbSysDistrict.getDistrictOid() + ".");
        }
        int index = dbSysDistrictMapper.updateByPrimaryKeySelective(dbSysDistrict);
        //清除缓存
        platformManager.evictSysDistrict(dbSysDistrict.getId());
        platformManager.evictSysDistrict(dbSysDistrict.getDistrictOid());
        return index;
    }

    /**
     * 验证输入的code，是否已经存在
     *
     * @param oid  不参与验证的oid
     * @param code 需要验证的code
     * @return
     * @throws Exception
     */
    public String checkIsExistesDistrictCode(Long oid, String code) {
        DbSysDistrict sysDistrict = dbSysDistrictMapper.selectAbleDbSysDistrictByCode(code);
        if (sysDistrict != null && (null==oid ||sysDistrict.getId().longValue()!=oid.longValue())) {
            return "当前区划代码已经存在！";
        }
        return null;
    }



    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysDistrictById(Long districtOid) {
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByPrimaryKey(districtOid);
        if(null!=dbSysDistrict){
            //先判断当前区划是否存在下级区划
            List<SysDistrict> sysDistrictList = this.querySysDistrictListByParentOid(dbSysDistrict.getDistrictOid());
            if(null!=sysDistrictList && sysDistrictList.size()>0){
                throw new ResultInfoException("删除失败，当前区划存在未删除的下级区划信息！");
            }
            //先判断当前区划是否存在组织机构
            if(this.checkOrganBydistrictOid(dbSysDistrict.getDistrictOid(),false)){
                throw new ResultInfoException("删除失败，当前区划存在关联的组织机构信息！");
            }
        }
        int index = dbSysDistrictMapper.deleteByOid(districtOid);
        if(index==0){
            throw new ResultInfoException("区划删除失败，请稍后再试！");
        }
        //清除缓存
        platformManager.evictSysDistrict(dbSysDistrict.getId());
        platformManager.evictSysDistrict(dbSysDistrict.getDistrictOid());
        return index;
    }

    /**
     * @description:  根据主键查询对应的实体类
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/28 9:54
     **/
    @Cacheable(key = "'getSysDistrictById:'+#id", unless = "#result == null")
    public SysDistrict getSysDistrictById(Long id) {
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByPrimaryKey(id);
        if(dbSysDistrict == null)
            throw new ResultInfoException("区划信息为空！");
        SysDistrict sysDistrict = new SysDistrict();
        BeanUtils.copyProperties(dbSysDistrict,sysDistrict);
        if(StrUtil.isNotBlank(sysDistrict.getParentOid())){
            DbSysDistrict parent = dbSysDistrictMapper.selectByDistrictOid(sysDistrict.getParentOid());
            if(parent!=null){
                sysDistrict.setParentName(parent.getName());
            }
        }
        if(null != sysDistrict.getLevelDictOid()){
            //获取级别
            SysDict dict =  sysDictFeignService.getSysDictByDictOid(sysDistrict.getLevelDictOid()).getData();
            sysDistrict.setLevelDictName(null!=dict?dict.getName():"");
        }
        if(null != sysDistrict.getImageAttaOid()){
            //附件名称
            DbSysAtta sysAtta = dbSysAttaMapper.selectByAttaOid(sysDistrict.getImageAttaOid());
            sysDistrict.setAttaName(null!=sysAtta?sysAtta.getOriginName():"");
        }
        return sysDistrict;
    }

    /**
     * @description:  根据业务主键查询对应的实体类
     * @param districtOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/28 9:54
     **/
    @Cacheable(key = "'getSysDistrictByDistrictOid:'+#districtOid", unless = "#result == null")
    public SysDistrict getSysDistrictByDistrictOid(String districtOid) {
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByDistrictOid(districtOid);
        if(dbSysDistrict == null)
            return null;
        SysDistrict sysDistrict = new SysDistrict();
        BeanUtils.copyProperties(dbSysDistrict,sysDistrict);
        if(StrUtil.isNotBlank(sysDistrict.getParentOid())){
            DbSysDistrict parent = dbSysDistrictMapper.selectByDistrictOid(sysDistrict.getParentOid());
            if(parent!=null){
                sysDistrict.setParentName(parent.getName());
            }
        }
        if(null != sysDistrict.getLevelDictOid()){
            //获取级别
            SysDict dict =  sysDictFeignService.getSysDictByDictOid(sysDistrict.getLevelDictOid()).getData();
            sysDistrict.setLevelDictName(null!=dict?dict.getName():"");
        }
        if(null != sysDistrict.getImageAttaOid()){
            //附件名称
            DbSysAtta sysAtta = dbSysAttaMapper.selectByAttaOid(sysDistrict.getImageAttaOid());
            sysDistrict.setAttaName(null!=sysAtta?sysAtta.getOriginName():"");
        }
        return sysDistrict;
    }

    /**
     * @description:  获取区划的信息
     * @param code 区划实体类业务编码
     * @author: wuxx
     * @Date: 2021/2/3 10:14
     **/
    @Cacheable(key = "'getSysDistrictByDistrictCode:'+#code", unless = "#result == null")
    public SysDistrict getSysDistrictByDistrictCode(String code) {
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByDistrictCode(code);
        if(dbSysDistrict == null)
           return null;
        SysDistrict sysDistrict = new SysDistrict();
        BeanUtils.copyProperties(dbSysDistrict,sysDistrict);
        if(StrUtil.isNotBlank(sysDistrict.getParentOid())){
            DbSysDistrict parent = dbSysDistrictMapper.selectByDistrictOid(sysDistrict.getParentOid());
            if(parent!=null){
                sysDistrict.setParentName(parent.getName());
            }
        }
        if(null != sysDistrict.getLevelDictOid()){
            //获取级别
            SysDict dict =  sysDictFeignService.getSysDictByDictOid(sysDistrict.getLevelDictOid()).getData();
            sysDistrict.setLevelDictName(null!=dict?dict.getName():"");
        }
        if(null != sysDistrict.getImageAttaOid()){
            //附件名称
            DbSysAtta sysAtta = dbSysAttaMapper.selectByAttaOid(sysDistrict.getImageAttaOid());
            sysDistrict.setAttaName(null!=sysAtta?sysAtta.getOriginName():"");
        }
        return sysDistrict;
    }


    /**
     * @description:  根据父类oid查询区划
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/10/31 17:08
     **/
    @Cacheable(key = "'querySysDistrictListByParentOid:'+#parentOid", unless = "#result == null")
    public List<SysDistrict> querySysDistrictListByParentOid(String parentOid) {
        DbSysDistrictExample dbSysDistrictExample = new DbSysDistrictExample();
        dbSysDistrictExample.setOrderByClause("sort asc");
        DbSysDistrictExample.Criteria criteria = dbSysDistrictExample.createCriteria();
        if(StrUtil.isNotEmpty(parentOid)){
            criteria.andParentOidEqualTo(parentOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysDistrict> dbSysDistricts = dbSysDistrictMapper.selectByExample(dbSysDistrictExample);
        List<SysDistrict> userList = dbSysDistricts.stream().map(dbSysDistrict -> {
            SysDistrict district = new SysDistrict();
            BeanUtils.copyProperties(dbSysDistrict,district);
            //获取级别
            if(null != district.getLevelDictOid()){
                //获取级别
                SysDict dict =  sysDictFeignService.getSysDictByDictOid(district.getLevelDictOid()).getData();
                district.setLevelDictName(null!=dict?dict.getName():"");
            }
            return district;
        }).collect(Collectors.toList());
        return  userList;
    }

    public PageResult<SysDistrict>  querySysDistrictWithPage(SysDistrict sysDistrict, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysDistrictExample dbSysDistrictExample = new DbSysDistrictExample();
        dbSysDistrictExample.setOrderByClause("sort asc");
        DbSysDistrictExample.Criteria criteria = dbSysDistrictExample.createCriteria();
        if(null!=sysDistrict){
            if(StrUtil.isNotEmpty(sysDistrict.getCode())){
                criteria.andCodeLike("%"+sysDistrict.getCode().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysDistrict.getName())){
                criteria.andNameLike("%"+sysDistrict.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysDistrict.getParentOid())){
                criteria.andParentOidEqualTo(sysDistrict.getParentOid());
            }else {
                String districtOid = CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid();
                criteria.andPathLike("%"+districtOid+"%");
            }
            if(StrUtil.isNotEmpty(sysDistrict.getLevelDictOid())){
                criteria.andLevelDictOidEqualTo(sysDistrict.getLevelDictOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbSysDistrict> dbSysDistricts = (Page<DbSysDistrict>)dbSysDistrictMapper.selectByExample(dbSysDistrictExample);
        PageResult<SysDistrict> pageResult = new PageResult<>(dbSysDistricts.getPageNum(),dbSysDistricts.getPageSize(),dbSysDistricts.getTotal());
        List<SysDistrict> userList = dbSysDistricts.stream().map(dbSysDistrict -> {
            SysDistrict district = new SysDistrict();
            BeanUtils.copyProperties(dbSysDistrict,district);
            if(StrUtil.isNotBlank(district.getParentOid())){
                DbSysDistrict parent = dbSysDistrictMapper.selectByDistrictOid(district.getParentOid());
                if(parent!=null){
                    district.setParentName(parent.getName());
                }
            }
            //获取级别
            if(null != district.getLevelDictOid()){
                //获取级别
                SysDict dict =  sysDictFeignService.getSysDictByDictOid(district.getLevelDictOid()).getData();
                district.setLevelDictName(null!=dict?dict.getName():"");
            }
            return district;
        }).collect(Collectors.toList());
        pageResult.setData(userList);
        return pageResult;
    }

    /**
     * @description: 检测父类是否存在禁用的状态(true 存在、fasle不存在)
     * @param oid 对象
     * @author: wuxx
     * @Date: 2021/3/22 15:45
     **/
    private boolean checkParentIsAble(String oid){
        if(StrUtil.isBlank(oid)){
            return false;
        }
        SysDistrict district = this.getSysDistrictByDistrictOid(oid);
        if(null==district){
            return false;
        }
        if(null!=district && BaseStaticParameter.N == district.getIsAble()){
            return true;
        }
        this.checkParentIsAble(district.getParentOid());
        return false;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public int ableSysDistrictById(Long oid) {
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByPrimaryKey(oid);
        if(dbSysDistrict == null)
            throw new ResultInfoException("区划信息为空！");
        Integer isAble = dbSysDistrict.getIsAble();
        if(BaseStaticParameter.N == isAble){
            if(this.checkParentIsAble(dbSysDistrict.getParentOid())){
                throw new ResultInfoException("启用失败，当前区划存在禁用的上级区划信息！");
            }
            dbSysDistrict.setIsAble(BaseStaticParameter.Y);
        }else {
            //先判断当前区划是否存在下级区划
            List<SysDistrict> sysDistrictList = this.querySysDistrictListByParentOid(dbSysDistrict.getDistrictOid());
            if(null!=sysDistrictList && sysDistrictList.size()>0){
                throw new ResultInfoException("禁用失败，当前区划存在启用的下级区划信息！");
            }
            //先判断当前区划是否存在组织机构
            if(this.checkOrganBydistrictOid(dbSysDistrict.getDistrictOid(),true)){
                throw new ResultInfoException("禁用失败，当前区划存在关联的组织机构信息！");
            }
            dbSysDistrict.setIsAble(BaseStaticParameter.N);
        }
        int index = dbSysDistrictMapper.updateByPrimaryKeySelective(dbSysDistrict);
        if(index==0){
            throw new ResultInfoException("区划启禁用失败，请稍后再试！");
        }
        //清除缓存
        platformManager.evictSysDistrict(dbSysDistrict.getId());
        platformManager.evictSysDistrict(dbSysDistrict.getDistrictOid());
        return index;
    }

    @Cacheable(key = "'queryDistrictSimpleTreeList:'+#districtOid", unless="#result == null")
    public List<SysDistrict> queryDistrictSimpleTreeList(String districtOid) {
        List<SysDistrict> sysDistricts = this.queryLowerLevelDistrictByOid(districtOid);
        return sysDistricts;
    }

    @Cacheable(key = "'queryDistrictSimpleTreeList:'+#districtOid + #districtOids", unless="#result == null")
    public List<SysDistrict> queryDistrictSimpleTreeList(String districtOid, String districtOids) {
        List<SysDistrict> sysDistricts = this.queryLowerLevelDistrictByOid(districtOid, districtOids);
        return sysDistricts;
    }

    @Cacheable(key = "'queryDistrictSimpleTree:'+#districtOid + #districtOids", unless="#result == null")
    public List<TreeSelect> queryDistrictSimpleTree(String districtOid, String districtOids) {
        if(StrUtil.isEmpty(districtOid)){
            districtOid = CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid();
        }
        List<SysDistrict> sysDistrictList = this.queryDistrictSimpleTreeList(districtOid, districtOids);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildSysDistrictTreeSelect(sysDistrictList);
        return treeSelects;
    }


    public List<SysDistrict> queryDistrictTreeNoIncludeList(String districtOid, String noIncludeOids) {
        DbSysDistrictExample example= new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = example.createCriteria();
        //区划oid为null 查询所有记录
        if(null!=districtOid){
            DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByDistrictOid(districtOid);
            if(dbSysDistrict == null){
                throw new ResultInfoException("区划信息为空！");
            }
            criteria.andPathLike("%"+districtOid+"%");
        }else {
            criteria.andPathLike("%%");
        }
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        if(StrUtil.isNotEmpty(noIncludeOids)){
            String[] oids = noIncludeOids.split(",");
            List<String> includeList = new ArrayList<>(oids.length);
            for (String s : oids) {
                //获取同级以及下级的区划oid
                List<SysDistrict> districtList = this.queryLowerLevelDistrictByOid(s);
                districtList.forEach(dist->{
                    includeList.add(dist.getDistrictOid());
                });
            }
            if(null!=includeList && includeList.size()>0){
                criteria.andDistrictOidNotIn(includeList);
            }
        }
        List<DbSysDistrict> distList = dbSysDistrictMapper.selectByExample(example);
        List<SysDistrict> sysDistrict = distList.stream().map(dist -> {
            SysDistrict district = new SysDistrict();
            BeanUtils.copyProperties(dist,district);
            return district;
        }).collect(Collectors.toList());
        return sysDistrict;
    }

    @Cacheable(key = "'queryLowerLevelDistrictByOid:'+#districtOid", unless="#result == null")
    public List<SysDistrict> queryLowerLevelDistrictByOid(String districtOid) {
        List<SysDistrict> sysDistrict =new ArrayList<>();
        DbSysDistrictExample example= new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = example.createCriteria();
        //区划oid为null 查询所有记录
        if(StrUtil.isNotEmpty(districtOid)){
            DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByDistrictOid(districtOid);
            if(dbSysDistrict == null){
                return null;
            }
            criteria.andPathLike("%"+districtOid+"%");
        }else {
            criteria.andPathLike("%%");
        }
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysDistrict> distList = dbSysDistrictMapper.selectByExample(example);
        sysDistrict = distList.stream().map(dist -> {
            SysDistrict district = new SysDistrict();
            BeanUtils.copyProperties(dist,district);
            return district;
        }).collect(Collectors.toList());
        return sysDistrict;
    }

    @Cacheable(key = "'queryLowerLevelDistrictByOid:'+#districtOid+#districtOids", unless="#result == null")
    public List<SysDistrict> queryLowerLevelDistrictByOid(String districtOid, String districtOids) {
        List<SysDistrict> sysDistrict = new ArrayList<>();
        DbSysDistrictExample example= new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = example.createCriteria();
        List<String> districtOidsList = Optional.ofNullable(StringUtils.isNotEmpty(districtOids) ? Arrays.asList(districtOids.split(";")) : null).orElseGet(Lists::newArrayList);
        List<DbSysDistrict> excludeSysDistrictList = new ArrayList<>();
        //区划oid为null 查询所有记录
        if(StrUtil.isNotEmpty(districtOid)){
            DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByDistrictOid(districtOid);
            if(dbSysDistrict == null){
                return null;
            }
            districtOidsList.stream().forEach(item -> {
                DbSysDistrict dbSysDistrict1 = this.dbSysDistrictMapper.selectByDistrictOid(item);
                if(null != dbSysDistrict1) {
                    if(!(StringUtils.equals(item, districtOid) || dbSysDistrict1.getPath().contains(districtOid))) {
                        excludeSysDistrictList.add(dbSysDistrict1);
                        if(StringUtils.isNotEmpty(dbSysDistrict1.getParentOid())) {
                            List<SysDistrict> sysDistrictListByPath = this.getParentListByDistrictOid(dbSysDistrict1.getParentOid());
                            excludeSysDistrictList.addAll(BeanUtils.copyListProperties(sysDistrictListByPath, DbSysDistrict::new));
                        }
                    }
                }
            });
            if(CollectionUtils.isNotEmpty(excludeSysDistrictList) && StringUtils.isNotEmpty(dbSysDistrict.getParentOid())) {
                List<SysDistrict> tempList = this.getParentListByDistrictOid(dbSysDistrict.getParentOid());
                tempList.forEach(item -> item.setDisabled(true));
                sysDistrict.addAll(tempList);
            }
            criteria.andPathLike("%"+districtOid+"%");
        }else {
            criteria.andPathLike("%%");
        }
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysDistrict> distList = dbSysDistrictMapper.selectByExample(example);
        sysDistrict.addAll(BeanUtils.copyListProperties(distList, SysDistrict::new));
        if(CollectionUtils.isNotEmpty(excludeSysDistrictList)) {
            sysDistrict.addAll(BeanUtils.copyListProperties(excludeSysDistrictList, SysDistrict::new, (excludeSysDistrict, sysDistrict1) -> sysDistrict1.setDisabled(true)));
        }
        // 根据id去重
        return sysDistrict.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(SysDistrict::getId))), ArrayList::new));
    }

    private List<SysDistrict> getParentListByDistrictOid(String districtOid) {
        List<SysDistrict> sysDistrictList = new ArrayList<>();
        Boolean flag = true;
        while (flag) {
            SysDistrict sysDistrictByDistrictOid = this.getSysDistrictByDistrictOid(districtOid);
            sysDistrictList.add(sysDistrictByDistrictOid);
            if(StringUtils.isEmpty(sysDistrictByDistrictOid.getParentOid())) {
                flag = false;
            } else {
                districtOid = sysDistrictByDistrictOid.getParentOid();
            }
        }
        return sysDistrictList;


    }


    public List<SysDistrict> queryTopDistrictByOid(String districtOid) {
        DbSysDistrictExample dbSysDistrictExample=new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = dbSysDistrictExample.createCriteria();
        if(StrUtil.isEmpty(districtOid)){
            criteria.andParentOidIsNull();
        }else{
            criteria.andDistrictOidEqualTo(districtOid);
        }
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysDistrictExample.setOrderByClause("sort");
        List<DbSysDistrict> dbSysDistricts = dbSysDistrictMapper.selectByExample(dbSysDistrictExample);
        List<SysDistrict> sysDistrictList = dbSysDistricts.stream().map(dbSysDistrict -> {
            SysDistrict sysDistrict = new SysDistrict();
            BeanUtils.copyProperties(dbSysDistrict, sysDistrict);
            return sysDistrict;
        }).collect(Collectors.toList());
        return sysDistrictList;
    }

    @Cacheable(key = "'queryDistrictChildrenByParentOid:'+#parentOid", unless="#result == null")
    public List<SysDistrict> queryDistrictChildrenByParentOid(String parentOid) {
        /*if(null==parentOid){
            return null;
        }*/
        DbSysDistrictExample dbSysDistrictExample=new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = dbSysDistrictExample.createCriteria();
        if(StrUtil.isNotEmpty(parentOid)){
            criteria.andParentOidEqualTo(parentOid);
        }else {
            criteria.andParentOidIsNull();
        }
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysDistrictExample.setOrderByClause("sort");
        List<DbSysDistrict> dbSysDistricts = dbSysDistrictMapper.selectByExample(dbSysDistrictExample);
        List<SysDistrict> sysDistrictList = dbSysDistricts.stream().map(dbSysDistrict -> {
            SysDistrict sysDistrict = new SysDistrict();
            BeanUtils.copyProperties(dbSysDistrict, sysDistrict);
            return sysDistrict;
        }).collect(Collectors.toList());
        return sysDistrictList;
    }

    /**
     * @description: 获取当前区划所在路径名称
     * @param dbSysDistrict 当前区划对象
     * @author: wuxx
     * @Date: 2020/8/31 16:08
     **/
    private String getSysDistrictDisName(String disNameStr,DbSysDistrict dbSysDistrict){
        //安徽省-xx市-xx市-xx街道
        if(StrUtil.isEmpty(disNameStr)){
            disNameStr = "";
        }
        if(null!=dbSysDistrict){
            disNameStr =  dbSysDistrict.getName() +"-" + disNameStr ;
            if(null!=dbSysDistrict.getParentOid()){
                DbSysDistrict parentSysDistrict= dbSysDistrictMapper.selectByDistrictOid(dbSysDistrict.getParentOid());
                return this.getSysDistrictDisName(disNameStr,parentSysDistrict);
            }
        }
        if(StrUtil.isNotEmpty(disNameStr)){
            disNameStr = disNameStr.substring(0,disNameStr.length()-1);
        }
        return disNameStr;

    }

    /**
     * @description:  根据path获取区划list
     * @param path 路径
     * @author: wuxx
     * @Date: 2020/10/26 14:14
     **/
    @Cacheable(key = "'getSysDistrictListByPath:'+#path", unless="#result == null")
    public List<SysDistrict> getSysDistrictListByPath(String path) {
        if(StrUtil.isEmpty(path)){
            return null;
        }
        DbSysDistrictExample dbSysDistrictExample=new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = dbSysDistrictExample.createCriteria();
        criteria.andPathLike("%"+path+"%");
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysDistrictExample.setOrderByClause("sort");
        List<DbSysDistrict> dbSysDistricts = dbSysDistrictMapper.selectByExample(dbSysDistrictExample);
        List<SysDistrict> sysDistrictList = dbSysDistricts.stream().map(dbSysDistrict -> {
            SysDistrict sysDistrict = new SysDistrict();
            BeanUtils.copyProperties(dbSysDistrict, sysDistrict);
            return sysDistrict;
        }).collect(Collectors.toList());
        return sysDistrictList;
    }

    /**
     * @description: 根据区划oid查询组织机构是否存在
     * @param districtOid
     * @author: wuxx
     * @Date: 2020/11/13 14:32
     **/
    private boolean checkOrganBydistrictOid(String districtOid,boolean ableFlag){
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andDistrictOidEqualTo(districtOid);
        if(ableFlag){
            criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        }
        List<DbSysOrgan> organList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        if(null!=organList && organList.size()>0){
            return true;
        }
        return false;
    }

    /**
     * @description:  获取区划父类对象
     * @author: wuxx
     * @Date: 2020/11/30 10:14
     **/
    @Cacheable(key = "'getTopSysDistrict'", unless = "#result == null")
    public SysDistrict getTopSysDistrict() {
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectTopDbSysDistrict();
        if(null==dbSysDistrict){
            return null;
        }
        SysDistrict sysDistrict = new SysDistrict();
        BeanUtils.copyProperties(dbSysDistrict, sysDistrict);
        return sysDistrict;
    }

    /**
     * @description:  根据区划级别id获取区划list
     * @param levelDictOid 区划级别id
     * @author: wuxx
     * @Date: 2020/10/26 14:14
     **/
    @Cacheable(key = "'querySysDistrictListByLevelDictOid:'+#levelDictOid", unless="#result == null")
    public List<SysDistrict> querySysDistrictListByLevelDictOid(String levelDictOid) {
        if(StrUtil.isEmpty(levelDictOid)){
            return null;
        }
        DbSysDistrictExample dbSysDistrictExample=new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = dbSysDistrictExample.createCriteria();
        criteria.andLevelDictOidEqualTo(levelDictOid);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysDistrictExample.setOrderByClause("sort");
        List<DbSysDistrict> dbSysDistricts = dbSysDistrictMapper.selectByExample(dbSysDistrictExample);
        List<SysDistrict> sysDistrictList = dbSysDistricts.stream().map(dbSysDistrict -> {
            SysDistrict sysDistrict = new SysDistrict();
            BeanUtils.copyProperties(dbSysDistrict, sysDistrict);
            return sysDistrict;
        }).collect(Collectors.toList());
        return sysDistrictList;
    }
    /**
     * @description: 根据区划oid查询区划列表
     * @author: wangyh
     * @Date: 2022/8/2 14:32
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<SysDistrict> distList(){
        List<SysDistrict> sysDistrict =new ArrayList<>();
        DbSysDistrictExample example= new DbSysDistrictExample();
        DbSysDistrictExample.Criteria criteria = example.createCriteria();
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysDistrict> distList = dbSysDistrictMapper.selectByExample(example);
        sysDistrict = distList.stream().map(dist -> {
            SysDistrict district = new SysDistrict();
            BeanUtils.copyProperties(dist,district);
            return district;
        }).collect(Collectors.toList());
        return sysDistrict;
    }


}

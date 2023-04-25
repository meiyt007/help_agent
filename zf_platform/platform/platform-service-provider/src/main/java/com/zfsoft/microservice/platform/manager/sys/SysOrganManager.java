package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.vo.SysOrganVo;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysOrganMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysUserMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysOrgan;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysOrganExample;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysUser;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysUserExample;
import com.zfsoft.microservice.platform.feign.settings.SysDictFeignService;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @ClassName SysOrganServiceImpl
 * @Description: 组织机构接口实现类
 * @Author wuxx
 * @Date 2020/9/7
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:organ")
public class SysOrganManager {

    @Resource
    private DbSysOrganMapper dbSysOrganMapper;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private EvictPlatformManager evictPlatformManager;

    @Resource
    private DbSysUserMapper dbSysUserMapper;

    @Autowired
    private SysDistrictManager sysDistrictManager;

    @Transactional
    @ParamValid
    @CacheEvict(allEntries = true)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int saveSysOrgan(@ValidGroups(groups = {SysOrgan.INSERT_GROUP.class})SysOrgan sysOrgan) {
        /*if(null == sysOrgan.getParentOid()){
            DbSysOrgan topSysOrgan = dbSysOrganMapper.selectTopSysOrgan();
            if (null != topSysOrgan && null==sysOrgan.getOid()) {
                throw new ResultInfoException("一级组织机构只能有一个");
            }
        }*/
        String message = this.checkIsExistesOrganCode(sysOrgan.getId(), sysOrgan.getCode());
        if(StrUtil.isNotEmpty(message)){
            throw new ResultInfoException(message);
        }
        if (null == sysOrgan.getId()) {
            sysOrgan.setId(null);
            sysOrgan.setOrganOid(IdUtil.simpleUUID());
        } else {
            // 组织机构oid不为空
            SysOrgan curDict = getSysOrganById(sysOrgan.getId());
            if (curDict == null) {
                throw new ResultInfoException("组织机构编号未查询到相应的字典信息!");
            }
        }
        if(null == sysOrgan.getParentOid()){
            sysOrgan.setPath("-1.");
            sysOrgan.setParentOid(null);
        }else {
            SysOrgan parentDict = this.getSysOrganByOrganOid(sysOrgan.getParentOid());
            // 查询上一级组织机构
            if (parentDict == null) {
                sysOrgan.setPath("-1.");
                sysOrgan.setParentOid(null);
            } else {
                String path = parentDict.getPath() + parentDict.getOrganOid() + ".";
                sysOrgan.setPath(path);
            }
        }
        // 设置组织机构信息的状态
        if (null==sysOrgan.getIsDelete()) {
            sysOrgan.setIsDelete(BaseStaticParameter.N);
        }
        if (null==sysOrgan.getIsAble()) {
            sysOrgan.setIsAble(BaseStaticParameter.Y);
        }
        sysOrgan.setModifyDate(new Date());
        DbSysOrgan dbSysOrgan = new DbSysOrgan();
        BeanUtils.copyProperties(sysOrgan,dbSysOrgan);
        int index = 0;
        if (null == sysOrgan.getId()) {
            index = dbSysOrganMapper.insert(dbSysOrgan);
            //return dbSysOrganMapper.insertSelective(dbSysOrgan);
        }else {
            index = dbSysOrganMapper.updateByPrimaryKeySelective(dbSysOrgan);
        }
        //清除缓存
        evictPlatformManager.evictSysOrgan(dbSysOrgan.getId(),dbSysOrgan.getDistrictOid());
        evictPlatformManager.evictSysOrgan(dbSysOrgan.getOrganOid(),dbSysOrgan.getDistrictOid());
        return index;
    }


    @Transactional
    @CacheEvict(allEntries = true)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int deleteSysOrganById(Long oid) {
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByPrimaryKey(oid);
        if(dbSysOrgan == null) {
            throw new ResultInfoException("组织机构信息为空！");
        }
        List<SysOrgan> organList = this.queryOrganChildrenByParentOid(dbSysOrgan.getOrganOid());
        if(null!=organList && organList.size()>0){
            throw new ResultInfoException("删除失败，当前组织机构存在下级组织机构！");
        }
        DbSysUserExample dbSysUserExample = new DbSysUserExample();
        DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andOrganOidEqualTo(dbSysOrgan.getOrganOid());
        List<DbSysUser> userList = dbSysUserMapper.selectByExample(dbSysUserExample);
        if(null!=userList && userList.size()>0){
            throw new ResultInfoException("删除失败，当前组织机构存在用户信息！");
        }
        int index =  dbSysOrganMapper.deleteByOid(oid);
        if(0 ==index){
            throw new ResultInfoException("组织机构信息删除失败！");
        }
        //清除缓存
        evictPlatformManager.evictSysOrgan(dbSysOrgan.getId(),dbSysOrgan.getDistrictOid());
        evictPlatformManager.evictSysOrgan(dbSysOrgan.getOrganOid(),dbSysOrgan.getDistrictOid());
        return index;
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
        SysOrgan sysOrgan = this.getSysOrganByOrganOid(oid);
        if(null==sysOrgan){
            return false;
        }
        if(null!=sysOrgan && BaseStaticParameter.N == sysOrgan.getIsAble()){
            return true;
        }
        this.checkParentIsAble(sysOrgan.getParentOid());
        return false;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public int ableSysOrganById(Long oid) {
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByPrimaryKey(oid);
        if(dbSysOrgan == null)
            throw new ResultInfoException("组织机构信息为空！");
        Integer isAble = dbSysOrgan.getIsAble();
        if(BaseStaticParameter.N == isAble){
            if(this.checkParentIsAble(dbSysOrgan.getParentOid())){
                throw new ResultInfoException("启用失败，当前区划存在禁用的上级组织机构信息！");
            }
            dbSysOrgan.setIsAble(BaseStaticParameter.Y);
        }else {
            List<SysOrgan> organList = this.queryOrganChildrenByParentOid(dbSysOrgan.getOrganOid());
            if(null!=organList && organList.size()>0){
                throw new ResultInfoException("禁用失败，当前组织机构存在启用的下级组织机构！");
            }
            DbSysUserExample dbSysUserExample = new DbSysUserExample();
            DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
            criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
            criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
            criteria.andOrganOidEqualTo(dbSysOrgan.getOrganOid());
            List<DbSysUser> userList = dbSysUserMapper.selectByExample(dbSysUserExample);
            if(null!=userList && userList.size()>0){
                throw new ResultInfoException("禁用失败，当前组织机构存在用户信息！");
            }
            dbSysOrgan.setIsAble(BaseStaticParameter.N);
        }
        int index = dbSysOrganMapper.updateByPrimaryKeySelective(dbSysOrgan);
        //清除缓存
        evictPlatformManager.evictSysOrgan(dbSysOrgan.getId(),dbSysOrgan.getDistrictOid());
        evictPlatformManager.evictSysOrgan(dbSysOrgan.getOrganOid(),dbSysOrgan.getDistrictOid());
        return index;
    }
    /**
     * @description:  根据主键查询对应的实体类
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/28 9:54
     **/
    @Cacheable(key = "'getSysOrganById:'+#id", unless = "#result == null")
    public SysOrgan getSysOrganById(Long id) {
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByPrimaryKey(id);
        if(dbSysOrgan == null)
            throw new ResultInfoException("组织机构信息为空！");
        SysOrgan sysOrgan = new SysOrgan();
        BeanUtils.copyProperties(dbSysOrgan,sysOrgan);
        if(null!=sysOrgan.getTypeDictOid()){
            //获取级别
            SysDict dict =  sysDictFeignService.getSysDictByDictOid(sysOrgan.getTypeDictOid()).getData();
            sysOrgan.setTypeDictName(null!=dict?dict.getName():"");
        }
        if(StrUtil.isNotBlank(sysOrgan.getParentOid())){
            //获取上级组织机构
            DbSysOrgan dborgan = dbSysOrganMapper.selectByOrganOid(sysOrgan.getParentOid());
            sysOrgan.setParentName(null!=dborgan?dborgan.getName():"");
        }
        if(StrUtil.isNotBlank(sysOrgan.getDistrictOid())){
            //获取区划
            SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(sysOrgan.getDistrictOid());
            sysOrgan.setDistrictName(null!=district?district.getName():"");
        }
        return sysOrgan;
    }

    /**
     * @description:  根据业务主键查询对应的实体类
     * @param organOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/28 9:54
     **/
    @Cacheable(key = "'getSysOrganByOrganOid:'+#organOid", unless = "#result == null")
    public SysOrgan getSysOrganByOrganOid(String organOid) {
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByOrganOid(organOid);
        if(dbSysOrgan == null)
            return null;
        SysOrgan sysOrgan = new SysOrgan();
        BeanUtils.copyProperties(dbSysOrgan,sysOrgan);
        if(null!=sysOrgan.getTypeDictOid()){
            //获取级别
            SysDict dict =  sysDictFeignService.getSysDictByDictOid(sysOrgan.getTypeDictOid()).getData();
            sysOrgan.setTypeDictName(null!=dict?dict.getName():"");
        }
        if(StrUtil.isNotBlank(sysOrgan.getParentOid())){
            //获取上级组织机构
            DbSysOrgan dborgan = dbSysOrganMapper.selectByOrganOid(sysOrgan.getParentOid());
            sysOrgan.setParentName(null!=dborgan?dborgan.getName():"");
        }
        if(StrUtil.isNotBlank(sysOrgan.getDistrictOid())){
            //获取区划
            SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(sysOrgan.getDistrictOid());
            sysOrgan.setDistrictName(null!=district?district.getName():"");
        }
        return sysOrgan;
    }

    /**
     * @description:  根据编码查询机构
     * @param code 编码
     * @author: wuxx
     * @Date: 2021/7/16 17:08
     **/
    @Cacheable(key = "'querySysOrganByCode:'+#code", unless = "#result == null")
    public SysOrgan querySysOrganByCode(String code) {
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByDbSysOrganByCode(code);
        if(dbSysOrgan == null)
            return null;
        SysOrgan sysOrgan = new SysOrgan();
        BeanUtils.copyProperties(dbSysOrgan,sysOrgan);
        if(null!=sysOrgan.getTypeDictOid()){
            //获取级别
            SysDict dict =  sysDictFeignService.getSysDictByDictOid(sysOrgan.getTypeDictOid()).getData();
            sysOrgan.setTypeDictName(null!=dict?dict.getName():"");
        }
        if(StrUtil.isNotBlank(sysOrgan.getParentOid())){
            //获取上级组织机构
            DbSysOrgan dborgan = dbSysOrganMapper.selectByOrganOid(sysOrgan.getParentOid());
            sysOrgan.setParentName(null!=dborgan?dborgan.getName():"");
        }
        if(StrUtil.isNotBlank(sysOrgan.getDistrictOid())){
            //获取区划
            SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(sysOrgan.getDistrictOid());
            sysOrgan.setDistrictName(null!=district?district.getName():"");
        }
        return sysOrgan;
    }

    /**
     * @description:  根据父类oid查询机构
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/10/31 17:08
     **/
    @Cacheable(key = "'querySysOrganListByParentOid:'+#parentOid", unless = "#result == null")
    public List<SysOrgan> querySysOrganListByParentOid(String parentOid) {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("id asc");
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(StrUtil.isNotEmpty(parentOid)){
            criteria.andParentOidEqualTo(parentOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysOrgan> dbSysOrgans = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrgans.stream().map(dbSysOrgan -> {
            SysOrgan organ = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,organ);
            if(null!=organ.getTypeDictOid()){
                //获取级别
                SysDict dict =  sysDictFeignService.getSysDictByDictOid(organ.getTypeDictOid()).getData();
                organ.setTypeDictName(null!=dict?dict.getName():"");
            }
           /* if(null!=organ.getDistrictOid()){
                //获取区划
                SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(organ.getDistrictOid());
                organ.setDistrictName(null!=district?district.getName():"");
            }*/
            return organ;
        }).collect(Collectors.toList());
        return  sysOrganList;
    }
    public PageResult<SysOrgan> querySysOrganWithPage(SysOrgan sysOrgan, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("sort asc");
        String loginDistrictOid = CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid();
        if(StrUtil.isNotEmpty(loginDistrictOid)){
            dbSysOrganExample.setLoginDistrictOid("%"+loginDistrictOid+"%");
        }
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(null!=sysOrgan){
            if(StrUtil.isNotEmpty(sysOrgan.getCode())){
                criteria.andCodeLike("%"+sysOrgan.getCode().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysOrgan.getName())){
                criteria.andNameLike("%"+sysOrgan.getName().trim()+"%");
            }
            if(StrUtil.isEmpty(sysOrgan.getParentOid())){
                if(StrUtil.isNotEmpty(loginDistrictOid)){
                    criteria.andParentOidIsNull();
                }
            }
            if(StrUtil.isNotEmpty(sysOrgan.getParentOid())){
                criteria.andParentOidEqualTo(sysOrgan.getParentOid());
            }
            if(StrUtil.isNotEmpty(sysOrgan.getDistrictOid())){
                criteria.andDistrictOidEqualTo(sysOrgan.getDistrictOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbSysOrgan> dbSysOrgans = (Page<DbSysOrgan>)dbSysOrganMapper.selectDbSysOrganExampleByExample(dbSysOrganExample);
        PageResult<SysOrgan> pageResult = new PageResult<>(dbSysOrgans.getPageNum(),dbSysOrgans.getPageSize(),dbSysOrgans.getTotal());
        List<SysOrgan> sysOrganList = dbSysOrgans.stream().map(dbSysOrgan -> {
            SysOrgan organ = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,organ);
            if(null!=organ.getTypeDictOid()){
                //获取级别
                SysDict dict =  sysDictFeignService.getSysDictByDictOid(organ.getTypeDictOid()).getData();
                organ.setTypeDictName(null!=dict?dict.getName():"");
            }
            if(StrUtil.isNotBlank(organ.getParentOid())){
                //获取区划
                DbSysOrgan dborgan = dbSysOrganMapper.selectByOrganOid(organ.getParentOid());
                organ.setParentName(null!=dborgan?dborgan.getName():"");
            }
            if(StrUtil.isNotBlank(organ.getDistrictOid())){
                //获取区划
                SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(organ.getDistrictOid());
                organ.setDistrictName(null!=district?district.getName():"");
            }
            return organ;
        }).collect(Collectors.toList());
        pageResult.setData(sysOrganList);
        return pageResult;
    }

    public PageResult<SysOrgan> querySysOrganShieldWithPage(SysOrgan sysOrgan, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(null!=sysOrgan){
            if(StrUtil.isNotEmpty(sysOrgan.getName())){
                criteria.andNameLike("%"+sysOrgan.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysOrgan.getShield())){
                criteria.andShieldEqualTo(sysOrgan.getShield());
            }else{
                criteria.andShieldEqualTo("0");
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysOrganExample.setOrderByClause("MODIFY_DATE desc");
        Page<DbSysOrgan> dbSysOrgans = (Page<DbSysOrgan>)dbSysOrganMapper.selectByExample(dbSysOrganExample);
        PageResult<SysOrgan> pageResult = new PageResult<>(dbSysOrgans.getPageNum(),dbSysOrgans.getPageSize(),dbSysOrgans.getTotal());
        List<SysOrgan> sysOrganList = dbSysOrgans.stream().map(dbSysOrgan -> {
            SysOrgan organ = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,organ);
            return organ;
        }).collect(Collectors.toList());
        pageResult.setData(sysOrganList);
        return pageResult;
    }

    @Cacheable(key = "'queryOrganTreeList:'+#districtOid", unless = "#result == null")
    public List<SysOrgan> queryOrganTreeList(String districtOid) {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("sort asc");
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(StrUtil.isNotEmpty(districtOid)){
            criteria.andDistrictOidEqualTo(districtOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrganList.stream().map(dbSysOrgan -> {
            SysOrgan dict = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysOrganList;
    }


    public List<SysOrgan> queryOrganChildrenByParentOid(String parentOid) {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("sort");
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(null!= parentOid){
            criteria.andParentOidEqualTo(parentOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrganList.stream().map(dbSysOrgan -> {
            SysOrgan organ = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,organ);
            return organ;
        }).collect(Collectors.toList());
        return sysOrganList;
    }


    public List<SysOrgan> queryOrganChildrenByDistrictOid(String districtOid) {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("sort asc");
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(StrUtil.isNotEmpty(districtOid)){
            criteria.andDistrictOidEqualTo(districtOid);
        }else {
            criteria.andParentOidIsNull();
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrganList.stream().map(dbSysOrgan -> {
            SysOrgan dict = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysOrganList;
    }

    /**
     * @description:  根据区划oid查询组织机构tree
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2020/11/12 16:28
     **/
    public List<SysOrgan> queryOrganTreeByDistrictOid(String districtOid) {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("sort asc");
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(StrUtil.isNotEmpty(districtOid)){
            criteria.andDistrictOidEqualTo(districtOid);
            criteria.andParentOidIsNull();
        }else {
            criteria.andParentOidIsNull();
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrganList.stream().map(dbSysOrgan -> {
            SysOrgan dict = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysOrganList;
    }

    /**
     * 检查组织机构的code是否存在
     *
     * @return
     * @throws Exception
     */
    public String checkIsExistesOrganCode(Long id, String code) {
        DbSysOrgan sysOrgan = dbSysOrganMapper.selectByDbSysOrganByCode(code);
        if (sysOrgan != null && (null==id || sysOrgan.getId().longValue() != id.longValue())) {
            return "当前组织机构代码已经存在！";
        }
        return null;
    }

    /**
     * @description: 根据组织机构的oids集合获取名称集合
     * @param oids 组织机构的oids集合
     * @author: wuxx
     * @Date: 2020/11/6 9:56
     **/
    public List<String> getOrganNameListByOids(List<String> oids) {
        if(null==oids || oids.size()==0){
            return null;
        }
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andOrganOidIn(oids);
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<String> nameList = dbSysOrganList.stream().map(dbSysOrgan -> {
            return dbSysOrgan.getName();
        }).collect(Collectors.toList());
        return nameList;
    }

    /**
     * @description:  根据区划编号获取组织机构列表，用于生成区划组织机构树
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2021/01/04 10:14
     **/
    public List<TreeSelect> queryDistrictOrganTree(String districtOid) {
        List<TreeSelect> treeSelectList= null;
        List<SysDistrict> sysDistricts = null ;
        if(StrUtil.isEmpty(districtOid)){
            sysDistricts = sysDistrictManager.queryLowerLevelDistrictByOid(null);
        }else{
            sysDistricts = sysDistrictManager.getSysDistrictListByPath(districtOid);
        }
        List<SysOrgan> sysOrgans = this.queryOrganTreeByDistrictOid(districtOid);
        treeSelectList= GenDataTreeUtil.buildOnlyDistrictAndOrganTreeSelect(sysDistricts,sysOrgans);
        return treeSelectList;
    }

    /**
     * @description:  根据区划oid查询组织机构List
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2021/1/8 11:28
     **/
    public List<SysOrgan> querySysOrganListByDistrictOid(String districtOid) {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        dbSysOrganExample.setOrderByClause("sort asc");
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        if(StrUtil.isNotEmpty(districtOid)){
            criteria.andDistrictOidEqualTo(districtOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andShieldEqualTo(BaseStaticParameter.SHIELD_YES);
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrganList.stream().map(dbSysOrgan -> {
            SysOrgan dict = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysOrganList;
    }

    public Long synSaveSysOrgan(SysOrganVo sysOrganVo) {
        Long id =null;
        int index =0;
        //根据业务主键查询组织机构表
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByOrganOid(sysOrganVo.getOrganOid());
        DbSysOrgan dbSysOrgan1 = new DbSysOrgan();
        if(dbSysOrgan == null){
            // 设置组织机构信息的状态
            dbSysOrgan1.setIsDelete(BaseStaticParameter.N);
            dbSysOrgan1.setIsAble(BaseStaticParameter.Y);
            dbSysOrgan1.setModifyDate(new Date());
            BeanUtils.copyProperties(sysOrganVo,dbSysOrgan1);
            index = dbSysOrganMapper.insert(dbSysOrgan1);
            id = dbSysOrgan1.getId();
        }else{
            dbSysOrgan1.setIsDelete(BaseStaticParameter.N);
            dbSysOrgan1.setIsAble(BaseStaticParameter.Y);
            BeanUtils.copyProperties(sysOrganVo,dbSysOrgan1);
            index = dbSysOrganMapper.updateByPrimaryKeySelective(dbSysOrgan1);
            id = dbSysOrgan1.getId();
        }
        return id;
    }


    /**
     * 获取机构分组列表
     * @author wangyh
     * @Date: 2020/9/09 14:14
     */
    public List<SysOrgan> querySysOrganList() {
        DbSysOrganExample dbSysOrganExample = new DbSysOrganExample();
        DbSysOrganExample.Criteria criteria = dbSysOrganExample.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        dbSysOrganExample.setOrderByClause("sort asc");
        List<DbSysOrgan> dbSysOrganList = dbSysOrganMapper.selectByExample(dbSysOrganExample);
        List<SysOrgan> sysOrganList = dbSysOrganList.stream().map(dbSysOrgan -> {
            SysOrgan dict = new SysOrgan();
            BeanUtils.copyProperties(dbSysOrgan,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysOrganList;
    }

    /**
     * @param ids 主键
     * @return
     * @description: 批量删除用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void shieldid(List<Long> ids,String isshield) throws Exception{
        for (Long userid : ids) {
            DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByPrimaryKey(userid);
            if (dbSysOrgan != null) {
                if(isshield.equals("1")){ //标识执行还原屏蔽操作
                    dbSysOrgan.setShield("0");//'0-未屏蔽,1-已屏蔽'
                }else{
                    dbSysOrgan.setShield("1");//'0-未屏蔽,1-已屏蔽'
                }
                dbSysOrganMapper.updateByPrimaryKeySelective(dbSysOrgan);
            }
        }

    }
    /**
     * @param id
     * @return
     * @description: 删除用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public SysOrgan shieldId(Long id,String isshield) throws Exception {
        DbSysOrgan dbSysOrgan = dbSysOrganMapper.selectByPrimaryKey(id);
        if(isshield.equals("1")){ //标识执行还原屏蔽操作
            dbSysOrgan.setShield("0");//'0-未屏蔽,1-已屏蔽'
        }else{
            dbSysOrgan.setShield("1");//'0-未屏蔽,1-已屏蔽'
        }
        dbSysOrganMapper.updateByPrimaryKeySelective(dbSysOrgan);
        SysOrgan sysOrgan = new SysOrgan();
        BeanUtils.copyProperties(dbSysOrgan, sysOrgan);
        return sysOrgan;
    }
}

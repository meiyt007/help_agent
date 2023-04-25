package com.zfsoft.superwindow.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.SysAreaSite;
import com.zfsoft.superwindow.data.yxpz.SysSiteDistrictRelation;
import com.zfsoft.superwindow.dbaccess.dao.DbSysAreaSiteMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbSysSiteDistrictRelationMapper;
import com.zfsoft.superwindow.dbaccess.data.DbSysAreaSite;
import com.zfsoft.superwindow.dbaccess.data.DbSysAreaSiteExample;
import com.zfsoft.superwindow.dbaccess.data.DbSysSiteDistrictRelation;
import com.zfsoft.superwindow.dbaccess.data.DbSysSiteDistrictRelationExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: kkfan
 * @create: 2020-10-22 14:20:58
 * @description: 受理辖区服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysAreaSiteManager {

    private final DbSysAreaSiteMapper dbSysAreaSiteMapper;

    private final DbSysSiteDistrictRelationMapper dbSysSiteDistrictRelationMapper;
    @Resource
    private SysDistrictFeignService sysDistrictFeginService;
    /**
     * 查询受理辖区列表
     * @param siteName
     * @return
     */
    public List<SysAreaSite> queryList(String siteName) {
        DbSysAreaSiteExample dbSysAreaSiteExample = new DbSysAreaSiteExample();
        DbSysAreaSiteExample.Criteria criteria = dbSysAreaSiteExample.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        if(StringUtils.isNotEmpty(siteName)) {
            criteria.andSiteNameLike("%" + siteName.trim() + "%");
        }
        dbSysAreaSiteExample.setOrderByClause("MODIFY_DATE desc");
        List<DbSysAreaSite> dbSysAreaSiteList = this.dbSysAreaSiteMapper.selectByExample(dbSysAreaSiteExample);

        return BeanUtils.copyListProperties(dbSysAreaSiteList, SysAreaSite::new);
    }

    public PageResult<SysAreaSite> querySysAreaSiteWithPage(String siteName, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        DbSysAreaSiteExample dbSysAreaSiteExample = new DbSysAreaSiteExample();
        DbSysAreaSiteExample.Criteria criteria = dbSysAreaSiteExample.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        if(StringUtils.isNotEmpty(siteName)) {
            criteria.andSiteNameLike("%" + siteName.trim() + "%");
        }
        dbSysAreaSiteExample.setOrderByClause("MODIFY_DATE desc");
        List<DbSysAreaSite> dbSysAreaSiteList = (Page<DbSysAreaSite>)this.dbSysAreaSiteMapper.selectByExample(dbSysAreaSiteExample);
        List<SysAreaSite> sysAreaSiteList = BeanUtils.copyListProperties(dbSysAreaSiteList, SysAreaSite::new);
        PageResult<SysAreaSite> pageResult = new PageResult<>(((Page) sysAreaSiteList).getPageNum(), ((Page) sysAreaSiteList).getPageSize(), ((Page) sysAreaSiteList).getTotal());
        sysAreaSiteList = dbSysAreaSiteList.stream().map(dbSysAreaSite -> {
            SysAreaSite site = new SysAreaSite();
            org.springframework.beans.BeanUtils.copyProperties(dbSysAreaSite,site);
            DbSysSiteDistrictRelationExample example = new DbSysSiteDistrictRelationExample();
            example.createCriteria()
                    .andSysAreaSiteOidEqualTo(dbSysAreaSite.getSysAreaSiteOid())
                    .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
            example.setOrderByClause("MODIFY_DATE desc");
            List<DbSysSiteDistrictRelation> dbSysSiteDistrictRelations = this.dbSysSiteDistrictRelationMapper.selectByExample(example);
            site.setDistrictOids(Optional.ofNullable(dbSysSiteDistrictRelations).orElseGet(Lists::newArrayList).stream().map(DbSysSiteDistrictRelation::getDistrictOid).collect(Collectors.joining(";")));
            if(StringUtil.isNotEmpty(site.getDistrictOids())){
                    String districtOidNames = "";
                    String [] districtOidArr = site.getDistrictOids().split(";");
                    for(String districtOid : districtOidArr){
                        ApiResultSet<SysDistrict> district = sysDistrictFeginService.getSysDistrictByDistrictOid(districtOid.replace("DISTRICT-",""));
                        if(district!=null && district.getData()!=null){
                            districtOidNames += district.getData().getName()+";";
                        }
                    }
                    if(StrUtil.isNotEmpty(districtOidNames)){
                        site.setDistrictionName(districtOidNames.substring(0,districtOidNames.length()-1));
                    }
                }
            return site;
        }).collect(Collectors.toList());
        pageResult.setData(sysAreaSiteList);
        return pageResult;
    }


    /**
     * 新增/更新受理辖区
     * @param sysAreaSite
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysAreaSite sysAreaSite) {
        if(null != sysAreaSite.getId()) {
            DbSysAreaSite dbSysAreaSite = this.dbSysAreaSiteMapper.selectByPrimaryKey(sysAreaSite.getId());
            Assert.notNull(sysAreaSite, MessageFormat.format("更新对象不存在！对象id为{0}", sysAreaSite.getId()));
            BeanUtils.copyProperties(sysAreaSite, dbSysAreaSite);
            dbSysAreaSite.setModifyDate(new Date());
            this.dbSysAreaSiteMapper.updateByPrimaryKeySelective(dbSysAreaSite);
            sysAreaSite.setSysAreaSiteOid(dbSysAreaSite.getSysAreaSiteOid());
        } else {
            DbSysAreaSite dbSysAreaSite = new DbSysAreaSite();
            BeanUtils.copyProperties(sysAreaSite, dbSysAreaSite);
            dbSysAreaSite.setCreateDate(new Date());
            dbSysAreaSite.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbSysAreaSite.setModifyDate(new Date());
            dbSysAreaSite.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
            // dbSysAreaSite.setCreateUser("00000000000000000000000000000000");
            dbSysAreaSite.setSysAreaSiteOid(UUIDUtil.randomUUID());
            dbSysAreaSite.setIsAble(SysCode.ABLE_STATUS.YES);
            this.dbSysAreaSiteMapper.insertSelective(dbSysAreaSite);
            sysAreaSite.setSysAreaSiteOid(dbSysAreaSite.getSysAreaSiteOid());
        }

        DbSysSiteDistrictRelationExample example = new DbSysSiteDistrictRelationExample();
        example.createCriteria()
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO)
                .andSysAreaSiteOidEqualTo(sysAreaSite.getSysAreaSiteOid().trim());
        List<DbSysSiteDistrictRelation> dbSysSiteDistrictRelations = dbSysSiteDistrictRelationMapper.selectByExample(example);
        Optional.ofNullable(dbSysSiteDistrictRelations)
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(temp -> dbSysSiteDistrictRelationMapper.deleteByPrimaryKey(temp.getId()));

        if (!CollectionUtils.isEmpty(dbSysSiteDistrictRelations)) {
            for (DbSysSiteDistrictRelation sysSiteDistrictRelation : dbSysSiteDistrictRelations) {
                dbSysSiteDistrictRelationMapper.deleteByPrimaryKey(sysSiteDistrictRelation.getId());
            }
        }

        if (StringUtils.isNotEmpty(sysAreaSite.getDistrictOids())) {
            List<String> result = Arrays.asList(sysAreaSite.getDistrictOids().split(";"));

            Stream.iterate(0, i -> i + 1).limit(Optional.ofNullable(result).orElseGet(Lists::newArrayList).size())
                    .forEach(ind -> {
                        SysSiteDistrictRelation sysSiteDistrictRelation = SysSiteDistrictRelation.builder()
                                .sysSiteDistrictRelationOid(UUIDUtil.randomUUID())
                                .sysAreaSiteOid(sysAreaSite.getSysAreaSiteOid())
                                .districtOid(result.get(ind))
                                .modifyDate(new Date())
                                .isDelete(SysCode.DELETE_STATUS.NO)
                                .sort(ind + 1)
                                .build();
                        DbSysSiteDistrictRelation dbSysSiteDistrictRelation = new DbSysSiteDistrictRelation();
                        BeanUtils.copyProperties(sysSiteDistrictRelation, dbSysSiteDistrictRelation);
                        dbSysSiteDistrictRelationMapper.insertSelective(dbSysSiteDistrictRelation);
                    });
        }

    }

    /**
     * 删除受理辖区数据  支持批量删除
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        Assert.hasLength(ids, "删除主键不能为空！");
        Optional.ofNullable(Arrays.asList(ids.split(",")))
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(id -> {
                    DbSysAreaSite dbSysAreaSite = this.dbSysAreaSiteMapper.selectByPrimaryKey(Long.valueOf(id));
                    Assert.notNull(dbSysAreaSite, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbSysAreaSite.setIsDelete(SysCode.DELETE_STATUS.YES);
                    dbSysAreaSite.setModifyDate(new Date());
                    this.dbSysAreaSiteMapper.updateByPrimaryKeySelective(dbSysAreaSite);
                });
    }

    /**
     * 获取受理辖区详情
     * @param id
     * @return
     */
    public SysAreaSite getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbSysAreaSite dbSysAreaSite = this.dbSysAreaSiteMapper.selectByPrimaryKey(Long.valueOf(id));
        SysAreaSite sysAreaSite = new SysAreaSite();
        BeanUtils.copyProperties(dbSysAreaSite, sysAreaSite);
        DbSysSiteDistrictRelationExample example = new DbSysSiteDistrictRelationExample();
        example.createCriteria()
                .andSysAreaSiteOidEqualTo(dbSysAreaSite.getSysAreaSiteOid())
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbSysSiteDistrictRelation> dbSysSiteDistrictRelations = this.dbSysSiteDistrictRelationMapper.selectByExample(example);
        sysAreaSite.setDistrictOids(Optional.ofNullable(dbSysSiteDistrictRelations).orElseGet(Lists::newArrayList).stream().map(DbSysSiteDistrictRelation::getDistrictOid).collect(Collectors.joining(";")));
        sysAreaSite.setDistrictOidsForParam(sysAreaSite.getDistrictOids().replaceAll("DISTRICT-",""));
        return sysAreaSite;
    }

    /**
     * 禁用受理辖区
     * @param id
     */
    public void ableSite(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbSysAreaSite dbSysAreaSite = this.dbSysAreaSiteMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbSysAreaSite, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbSysAreaSite.setIsAble(dbSysAreaSite.getIsAble().equals(SysCode.ABLE_STATUS.YES) ? SysCode.ABLE_STATUS.NO : SysCode.ABLE_STATUS.YES);
        this.dbSysAreaSiteMapper.updateByPrimaryKeySelective(dbSysAreaSite);
    }

    public ApiResultSet<List<SysAreaSite>> queryList(){
        DbSysAreaSiteExample dbSysAreaSiteExample = new DbSysAreaSiteExample();
        DbSysAreaSiteExample.Criteria criteria = dbSysAreaSiteExample.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andIsAbleEqualTo(SysCode.DELETE_STATUS.YES);//0禁用 1启用
        dbSysAreaSiteExample.setOrderByClause("MODIFY_DATE desc");
        List<DbSysAreaSite> dbSysAreaSiteList = this.dbSysAreaSiteMapper.selectByExample(dbSysAreaSiteExample);
        if(dbSysAreaSiteList!=null){
            List<SysAreaSite> areaSiteList= BeanUtils.copyListProperties(dbSysAreaSiteList, SysAreaSite::new);
           return new ApiResultSet(areaSiteList);
        }
        return null;
    }


}

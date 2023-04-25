package com.zfsoft.single.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zfsoft.cases.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.single.data.yxpz.ServiceSiteAuthorize;
import com.zfsoft.single.dbaccess.dao.DbServiceSiteAuthorizeMapper;
import com.zfsoft.single.dbaccess.data.DbServiceSiteAuthorize;
import com.zfsoft.single.dbaccess.data.DbServiceSiteAuthorizeExample;
import com.zfsoft.single.util.*;
import com.zfsoft.superwindow.data.yxpz.SysAreaSite;
import com.zfsoft.superwindow.service.yxpz.SysAreaSiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: dongxl
 * @create: 2020-11-2
 * @description: 辖区授权服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceSiteAuthorizeManager {

    private final DbServiceSiteAuthorizeMapper dbServiceSiteAuthorizeMapper;

    private final SysAreaSiteService sysAreaSiteFeginService;


    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ServiceSiteAuthorize serviceSiteAuthorize) {
        //根据事项id查询所有的授权登记信息
        Assert.hasLength(serviceSiteAuthorize.getServiceOid(), "事项主键不能为空！");
        DbServiceSiteAuthorizeExample example = new DbServiceSiteAuthorizeExample();
        example.createCriteria()
                .andServiceOidEqualTo(serviceSiteAuthorize.getServiceOid())
                .andDelFlagEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbServiceSiteAuthorize> dbServiceSiteAuthorize = this.dbServiceSiteAuthorizeMapper.selectByExample(example);
        Map<String,DbServiceSiteAuthorize> authorizeMap = new HashMap<>();
        if(null != dbServiceSiteAuthorize && dbServiceSiteAuthorize.size()>0){
            for(DbServiceSiteAuthorize authorize : dbServiceSiteAuthorize){
                authorizeMap.put(authorize.getSiteOid(),authorize);
            }
        }

        //重新生成授权信息
        if (StringUtils.isNotEmpty(serviceSiteAuthorize.getSiteOid())) {
            List<String> result = Arrays.asList(serviceSiteAuthorize.getSiteOid().split(";"));
            Stream.iterate(0, i -> i + 1).limit(Optional.ofNullable(result).orElseGet(Lists::newArrayList).size())
                    .forEach(ind -> {
                        if(null == authorizeMap.get(result.get(ind))){
                            DbServiceSiteAuthorize dbsxreg=new DbServiceSiteAuthorize();
                            dbsxreg.setAuthorizeOid(UUIDUtil.randomUUID());
                            dbsxreg.setCreateDate(new Date());
                            dbsxreg.setServiceName(serviceSiteAuthorize.getServiceName());
                            dbsxreg.setServiceType(serviceSiteAuthorize.getServiceType());
                            dbsxreg.setDelFlag(String.valueOf(SysCode.DELETE_STATUS.NO));
                            dbsxreg.setModifyDate(new Date());
                            dbsxreg.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                            dbsxreg.setServiceOid(serviceSiteAuthorize.getServiceOid());
                            dbsxreg.setSiteOid(result.get(ind));
                            this.dbServiceSiteAuthorizeMapper.insertSelective(dbsxreg);
                        }else{
                            authorizeMap.remove(result.get(ind));
                        }
                    });
        }
        if(null != authorizeMap && authorizeMap.size()>0){
            DbServiceSiteAuthorize siteAuthorize = null;
            for(Map.Entry<String,DbServiceSiteAuthorize> auth : authorizeMap.entrySet()){
                siteAuthorize = auth.getValue();
                siteAuthorize.setDelFlag(String.valueOf(SysCode.DELETE_STATUS.YES));
                this.dbServiceSiteAuthorizeMapper.updateByPrimaryKey(siteAuthorize);
            }
        }
    }

    public String getSiteOidsByServiceOid(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbServiceSiteAuthorizeExample example = new DbServiceSiteAuthorizeExample();
        example.createCriteria()
                .andServiceOidEqualTo(id)
                .andDelFlagEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbServiceSiteAuthorize> dbServiceSiteAuthorize = this.dbServiceSiteAuthorizeMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(dbServiceSiteAuthorize)){
            String str=Optional.ofNullable(dbServiceSiteAuthorize).orElseGet(Lists::newArrayList).stream().map(DbServiceSiteAuthorize::getSiteOid).collect(Collectors.joining(";"));
            return str;
        }
        return null;
    }

    /**
     * dongxl
     * 授权辖区树
     * @return
     */
    public List<TreeSelect> querySiteAuthorizeTree() {
        ApiResultSet<List<SysAreaSite>> rest= sysAreaSiteFeginService.queryList();
        if(rest!=null&&rest.getData()!=null&&rest.getData().size()>0){
            List<SysAreaSite> areaSiteList= BeanUtils.copyListProperties(rest.getData(), SysAreaSite::new);
            List<TreeSelect> treeSelects = GenDataTreeUtil.buildSiteAuthorizeTreeSelect(areaSiteList);
            return treeSelects;
        }
        return null;
    }

    /**
     * 根据事项OID取消授权
     * @param serviceOid
     */
    public int delServiceSiteAuthorizeByServiceOid(String serviceOid) {
        Assert.hasLength(serviceOid, "事项主键不能为空！");
        if(StrUtil.isNotEmpty(serviceOid)){
            int index = this.dbServiceSiteAuthorizeMapper.delServiceSiteAuthorizeByServiceOid(serviceOid);
            return index;
        }
        return 0;
    }

    public List<ServiceSiteAuthorize> selectDbServiceSiteAuthorizList(ServiceSiteAuthorize ss) {
        if(ss!=null){
            DbServiceSiteAuthorize authorize=new DbServiceSiteAuthorize();
            BeanUtils.copyProperties(ss,authorize);
            List<DbServiceSiteAuthorize> dbServiceSiteAuthorizes =dbServiceSiteAuthorizeMapper.selectDbServiceSiteAuthorizList(authorize);
            if(dbServiceSiteAuthorizes!=null&&dbServiceSiteAuthorizes.size()>0){
                return BeanUtils.copyListProperties(dbServiceSiteAuthorizes,ServiceSiteAuthorize::new);
            }
        }
        return null;

    }
}

package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysPost;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysDistrictMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysOrganMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysPostMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPost;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPostExample;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysPostServiceImpl
 * @Description: 岗位接口实现类
 * @Author wuxx
 * @Date 2020/9/7
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:post")
public class SysPostManager {

    @Resource
    private DbSysPostMapper dbSysPostMapper;
    @Resource
    private DbSysDistrictMapper dbSysDistrictMapper;
    @Resource
    private DbSysOrganMapper dbSysOrganMapper;

    @Autowired
    private SysDistrictManager sysDistrictManager;
    @Autowired
    private SysOrganManager sysOrganManager;

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysPost(@ValidGroups(groups = {SysPost.INSERT_GROUP.class})SysPost sysPost) {
        if (sysPost == null) {
            throw new ResultInfoException("岗位信息不正确!");
        }
        if (null == sysPost.getId()) {
            sysPost.setId(null);
            //生成业务主键
            sysPost.setPostOid(IdUtil.simpleUUID());
        } else {
            // 岗位oid不为空
            SysPost curDict = getSysPostById(sysPost.getId());
            if (curDict == null) {
                throw new ResultInfoException("岗位编号未查询到相应的岗位信息!");
            }
        }

        // 设置岗位信息的状态
        if (sysPost.getIsDelete() == null) {
            sysPost.setIsDelete(BaseStaticParameter.N);
        }
        if (sysPost.getIsAble() == null) {
            sysPost.setIsAble(BaseStaticParameter.Y);
        }
        DbSysPost dbSysPost = new DbSysPost();
        BeanUtils.copyProperties(sysPost,dbSysPost);
        if (null == sysPost.getId()) {
            return dbSysPostMapper.insert(dbSysPost);
        }else {
            return dbSysPostMapper.updateByPrimaryKeySelective(dbSysPost);
        }
    }


    /**
     * 删除岗位
     *
     * @param oid 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021-01-21 16:29
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysPostById(Long oid) {
        DbSysPost dbSysPost = dbSysPostMapper.selectByPrimaryKey(oid);
        if(dbSysPost == null){
            throw new ResultInfoException("岗位信息为空！");
        }
        if (dbSysPost.getPostOid() != null) {
            /*int i = dbSysRoleMapper.selectRoleByAppOid(dbSysPost.getAppOid());
            if (i > 0){
                // 岗位下存在未删除的角色时禁止删除岗位
                return BaseStaticParameter.N;
            }*/
        }
        dbSysPostMapper.deleteByOid(oid);
        return BaseStaticParameter.Y;
    }


    /**
     * 启禁用岗位
     *
     * @param oid 主键id
     * @return int 1表示成功，0表示失败
     * @author wuxx
     * @Date 2021-01-21 16:29
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableSysPostById(Long oid) {
        DbSysPost dbSysPost = dbSysPostMapper.selectByPrimaryKey(oid);
        if(dbSysPost == null){
            throw new ResultInfoException("岗位信息为空！");
        }
        Integer isAble = dbSysPost.getIsAble();
        if(BaseStaticParameter.N==isAble){
            dbSysPost.setIsAble(BaseStaticParameter.Y);
        }else {
            // 判断岗位下是否存在启用状态角色
            if (dbSysPost.getPostOid() != null) {
                /*int i = dbSysRoleMapper.selectAbleRoleByAppOid(dbSysPost.getAppOid());
                if (i > 0){
                    return BaseStaticParameter.N;
                }*/
            }
            dbSysPost.setIsAble(BaseStaticParameter.N);
        }
        dbSysPostMapper.updateByPrimaryKeySelective(dbSysPost);
        return BaseStaticParameter.Y;
    }

    /**
     * @description: 根据主键获取岗位信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2021/1/21 16:32
     **/
    @Cacheable(key = "'getSysPostById:'+#id", unless = "#result == null")
    public SysPost getSysPostById(Long id) {
        DbSysPost dbSysPost = dbSysPostMapper.selectByPrimaryKey(id);
        if(dbSysPost == null)
            throw new ResultInfoException("岗位信息为空！");
        SysPost sysPost = new SysPost();
        BeanUtils.copyProperties(dbSysPost,sysPost);
        //所属区划
        if(StrUtil.isNotEmpty(sysPost.getDistrictOid())){
            String name = dbSysDistrictMapper.selectNameByDistrictOid(sysPost.getDistrictOid());
            sysPost.setDistrictName(name);
        }
        //所属机构
        if(StrUtil.isNotEmpty(sysPost.getOrganOid())){
            String name = dbSysOrganMapper.selectNameByOrganOid(sysPost.getOrganOid());
            sysPost.setOrganName(name);
        }
        return sysPost;
    }

    /**
     * @description: 根据业务主键获取岗位信息
     * @param postOid 主业务键
     * @author: wuxx
     * @Date: 2021/1/21 16:32
     **/
    @Cacheable(key = "'getSysPostByPostOid:'+#postOid", unless = "#result == null")
    public SysPost getSysPostByPostOid(String postOid) {
        DbSysPost dbSysPost = dbSysPostMapper.selectDbSysPostByPostOid(postOid);
        if(dbSysPost == null)
            return null;
        SysPost sysPost = new SysPost();
        BeanUtils.copyProperties(dbSysPost,sysPost);
        //所属区划
        if(StrUtil.isNotEmpty(sysPost.getDistrictOid())){
            String name = dbSysDistrictMapper.selectNameByDistrictOid(sysPost.getDistrictOid());
            sysPost.setDistrictName(name);
        }
        //所属机构
        if(StrUtil.isNotEmpty(sysPost.getOrganOid())){
            String name = dbSysOrganMapper.selectNameByOrganOid(sysPost.getOrganOid());
            sysPost.setOrganName(name);
        }
        return sysPost;
    }

    /**
     * @description: 分页查询
     * @param sysPost 岗位
     * @param pageNumber 页数
     * @param pageSize 页大小
     * @author: wuxx
     * @Date: 2021/1/21 16:36
     **/
    public PageResult<SysPost> querySysPostWithPage(SysPost sysPost, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysPostExample dbSysPostExample = new DbSysPostExample();
        dbSysPostExample.setOrderByClause("sort");
        DbSysPostExample.Criteria criteria = dbSysPostExample.createCriteria();
        if(null!=sysPost){
            if(StrUtil.isNotEmpty(sysPost.getName())){
                criteria.andNameLike("%"+sysPost.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysPost.getDistrictOid())){
                criteria.andDistrictOidEqualTo(sysPost.getDistrictOid());
            }
            if(StrUtil.isNotEmpty(sysPost.getOrganOid())){
                criteria.andOrganOidEqualTo(sysPost.getOrganOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbSysPost> dbSysPosts = (Page<DbSysPost>)dbSysPostMapper.selectByExample(dbSysPostExample);
        PageResult<SysPost> pageResult = new PageResult<>(dbSysPosts.getPageNum(),dbSysPosts.getPageSize(),dbSysPosts.getTotal());
        List<SysPost> sysPostList = dbSysPosts.stream().map(dbSysPost -> {
            SysPost post = new SysPost();
            BeanUtils.copyProperties(dbSysPost,post);
            //所属区划
            if(StrUtil.isNotEmpty(post.getDistrictOid())){
                String name = dbSysDistrictMapper.selectNameByDistrictOid(post.getDistrictOid());
                post.setDistrictName(name);
            }
            //所属机构
            if(StrUtil.isNotEmpty(post.getOrganOid())){
                String name = dbSysOrganMapper.selectNameByOrganOid(post.getOrganOid());
                post.setOrganName(name);
            }
            return post;
        }).collect(Collectors.toList());
        pageResult.setData(sysPostList);
        return pageResult;
    }


    /**
     * @description: 根据岗位查询岗位列表
     * @param sysPost 岗位
     * @author: wuxx
     * @Date: 2021/1/21 16:43
     **/
    @Cacheable(key = "'querySysPostList:'+#sysPost.districtOid+#sysPost.organOid", unless = "#result == null")
    public List<SysPost> querySysPostList(SysPost sysPost) {
        DbSysPostExample dbSysPostExample = new DbSysPostExample();
        DbSysPostExample.Criteria criteria = dbSysPostExample.createCriteria();
        if(null!=sysPost){
            /*if(StrUtil.isNotEmpty(sysPost.getName())){
                criteria.andNameLike("%"+sysPost.getName()+"%");
            }*/
            if(StrUtil.isNotEmpty(sysPost.getDistrictOid())){
                criteria.andDistrictOidEqualTo(sysPost.getDistrictOid());
            }
            if(StrUtil.isNotEmpty(sysPost.getOrganOid())){
                criteria.andOrganOidEqualTo(sysPost.getOrganOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysPost> dbSysPosts = dbSysPostMapper.selectByExample(dbSysPostExample);
        List<SysPost> sysPostList = dbSysPosts.stream().map(dbSysPost -> {
            SysPost post = new SysPost();
            BeanUtils.copyProperties(dbSysPost, post);
            //所属区划
            if(StrUtil.isNotEmpty(post.getDistrictOid())){
                String name = dbSysDistrictMapper.selectNameByDistrictOid(post.getDistrictOid());
                post.setDistrictName(name);
            }
            //所属机构
            if(StrUtil.isNotEmpty(post.getOrganOid())){
                String name = dbSysOrganMapper.selectNameByOrganOid(post.getOrganOid());
                post.setOrganName(name);
            }
            return post;
        }).collect(Collectors.toList());
        return sysPostList;

    }

    /**
     * @description: 查询所有区划的组织机构、岗位信息Tree
     * @author: wuxx
     * @Date: 2021/1/26 10:40
     **/
    @Cacheable( key = "'queryAllDistrictOrganPostTree'", unless = "#result == null")
    public List<TreeSelect> queryAllDistrictOrganPostTree() {
        List<SysDistrict> sysDistrictList = sysDistrictManager.queryDistrictSimpleTreeList(null);
        List<SysOrgan> organList = sysOrganManager.queryOrganTreeList(null);
        List<SysPost> postList = this.querySysPostList(null);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildDistrictAndOrganAndPostTreeSelect(sysDistrictList, organList, postList);
        treeSelects = GenDataTreeUtil.buildDisabledLastTree(treeSelects,GenDataTreeUtil.TYPE_POST);
        return treeSelects;
    }
}

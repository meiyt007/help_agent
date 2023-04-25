package com.zfsoft.superwindow.manager.zsgl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.zsgl.ZskDict;
import com.zfsoft.superwindow.dbaccess.dao.DbZskDictMapper;
import com.zfsoft.superwindow.dbaccess.data.DbZskDict;
import com.zfsoft.superwindow.dbaccess.data.DbZskDictExample;
import com.zfsoft.superwindow.dbaccess.vo.ZskDictVo;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: kkfan
 * @create: 2020-10-28 14:04:41
 * @description:   知识库管理
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseClassificationManager {

    private final DbZskDictMapper dbZskDictMapper;

    /**
     * 查询知识库字段树
     * @return
     */
    public List<ZskDict> queryKnowledgeBaseClassificationTree(String parentOid) {
        List<ZskDictVo> zskDictVoList = this.dbZskDictMapper.queryKnowledgeBaseClassificationTree(parentOid);
        return BeanUtils.copyListProperties(zskDictVoList, ZskDict::new);
    }

    /**
     * 查询知识库列表
     * @param zskDict
     * @return
     */
    public List<ZskDict> queryList(ZskDict zskDict) {
        DbZskDictExample example = new DbZskDictExample();
        DbZskDictExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        if(null != zskDict) {
            if(StringUtils.isNotEmpty(zskDict.getParentOid())) {
                criteria.andParentOidEqualTo(zskDict.getParentOid().trim());
            } else {
                criteria.andParentOidIsNull();
            }
            if(StringUtils.isNotEmpty(zskDict.getName())) {
                criteria.andNameLike("%" + zskDict.getName().trim() + "%");
            }
            if(StringUtils.isNotEmpty(zskDict.getCode())) {
                criteria.andCodeLike("%" + zskDict.getCode().trim() + "%");
            }
        }
        //example.setOrderByClause("SORT asc, MODIFY_DATE desc");
        example.setOrderByClause(" SORT ASC,CREATE_DATE DESC ");
        List<DbZskDict> dbZskDictList = this.dbZskDictMapper.selectByExample(example);
        return BeanUtils.copyListProperties(dbZskDictList, ZskDict::new);
    }

    /**
     * 根据父元素oid查询列表数据
     * @param parendOid
     * @return
     */
    public List<ZskDict> queryDictListByParentOid(String parendOid) {
        Assert.hasLength(parendOid, "parendOid不能为空！");
        DbZskDictExample example = new DbZskDictExample();
        DbZskDictExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andParentOidEqualTo(parendOid);
        //example.setOrderByClause("SORT asc, MODIFY_DATE desc");
        example.setOrderByClause(" CREATE_DATE DESC ");
        List<DbZskDict> dbZskDictList = this.dbZskDictMapper.selectByExample(example);
        return BeanUtils.copyListProperties(dbZskDictList, ZskDict::new);
    }

    /**
     * 保存/更新知识库分类字典
     * @param zskDict
     */
    public void saveOrUpdate(ZskDict zskDict) {
        if(null != zskDict.getId()) {
            DbZskDict dbZskDict = this.selectByPrimaryKey(zskDict.getId());
            Assert.notNull(dbZskDict, MessageFormat.format("更新对象不存在！对象id为{0}", dbZskDict.getId()));
            BeanUtils.copyProperties(zskDict, dbZskDict);
            dbZskDict.setModifyDate(new Date());
            this.dbZskDictMapper.updateByPrimaryKeySelective(dbZskDict);
            BeanUtils.copyProperties(dbZskDict, zskDict);
        } else {
            DbZskDict dbZskDict = new DbZskDict();
            BeanUtils.copyProperties(zskDict, dbZskDict);
            dbZskDict.setZskDictOid(UUIDUtil.randomUUID());
            dbZskDict.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbZskDict.setCreateDate(new Date());
            dbZskDict.setCreatUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
            dbZskDict.setModifyDate(new Date());
            this.dbZskDictMapper.insertSelective(dbZskDict);
            BeanUtils.copyProperties(dbZskDict, zskDict);
        }
    }

    /**
     * 递归删除分类信息 包含子元素  支持批量删除
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        Assert.hasLength(ids, "删除主键不能为空！");
        Optional.ofNullable(Arrays.asList(ids.split(",")))
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(id -> {
                    DbZskDict dbZskDict = this.selectByPrimaryKey(Long.valueOf(id));
                    Assert.notNull(dbZskDict, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbZskDict.setIsDelete(SysCode.DELETE_STATUS.YES);
                    dbZskDict.setModifyDate(new Date());
                    this.dbZskDictMapper.updateByPrimaryKeySelective(dbZskDict);
                    if(StringUtils.isNotEmpty(dbZskDict.getZskDictOid())) {
                        List<ZskDict> zskDictList = this.queryDictListByParentOid(dbZskDict.getZskDictOid());
                        String childIds = Optional.ofNullable(zskDictList).orElseGet(Lists::newArrayList).stream().map(zskDict -> zskDict.getId().toString()).collect(Collectors.joining(","));
                        if(StringUtils.isNotEmpty(childIds)) {
                            this.delete(childIds);
                        }
                    }

                });
    }

    /**
     * 获取分类信息
     * @param id
     * @return
     */
    public ZskDict getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbZskDict dbZskDict = selectByPrimaryKey(Long.valueOf(id));
        ZskDict zskDict = new ZskDict();
        BeanUtils.copyProperties(dbZskDict, zskDict);
        return zskDict;
    }

    /**
     * 根据主键id查询分类信息
     * @param id
     * @return
     */
    private DbZskDict selectByPrimaryKey(Long id) {
        Assert.notNull(id, "查询id不能为空！");
        DbZskDictExample example = new DbZskDictExample();
        DbZskDictExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andIdEqualTo(id);
        List<DbZskDict> dbZskDictList = this.dbZskDictMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbZskDictList) ? null : dbZskDictList.get(0);
    }

    public String validateRepeat(String zskDictOid, String code, String name, String parentOid) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("zskDictOid", zskDictOid);
        param.put("code", code);
        param.put("name", name);
        param.put("parentOid", parentOid);
        List<DbZskDict> dbZskDictList = dbZskDictMapper.validateRepeat(param);
        if (dbZskDictList != null && dbZskDictList.size() > 0) {
            return "知识库字典代码或名称不能重复添加！";
        }
        return "";
    }
}

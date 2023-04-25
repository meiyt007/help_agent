package com.zfsoft.superwindow.manager.zsgl;

import com.google.common.collect.Lists;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.zsgl.KnowledgeBase;
import com.zfsoft.superwindow.dbaccess.dao.DbKnowledgeBaseMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbZskDictMapper;
import com.zfsoft.superwindow.dbaccess.data.DbKnowledgeBase;
import com.zfsoft.superwindow.dbaccess.data.DbKnowledgeBaseExample;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: kkfan
 * @create: 2020-10-29 15:29:05
 * @description:    知识库管理业务逻辑层
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseManager {

    private final DbKnowledgeBaseMapper dbSysAreaSiteExample;

    private final DbZskDictMapper dbZskDictMapper;

    /**
     * 查询知识库列表
     * @param knowledgeBase
     * @return
     */
    public List<KnowledgeBase> queryList(KnowledgeBase knowledgeBase) {
        DbKnowledgeBaseExample example = new DbKnowledgeBaseExample();
        DbKnowledgeBaseExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        if(null != knowledgeBase) {
            if(StringUtils.isNotBlank(knowledgeBase.getKnowledgeNumber())) {
                criteria.andKnowledgeNumberEqualTo(knowledgeBase.getKnowledgeNumber().trim());
            }
            if(StringUtils.isNotBlank(knowledgeBase.getKnowledgeTitle())) {
                criteria.andKnowledgeTitleLike("%" + knowledgeBase.getKnowledgeTitle().trim() + "%");
            }
            if(StringUtils.isNotBlank(knowledgeBase.getServiceName())) {
                criteria.andServiceNameLike("%" + knowledgeBase.getServiceName().trim() + "%");
            }
            if(StringUtils.isNotBlank(knowledgeBase.getZskDictOid())) {
                criteria.andZskDictOidEqualTo(knowledgeBase.getZskDictOid());
            }
        }
       // example.setOrderByClause("MODIFY_DATE desc");
        example.setOrderByClause(" CREATE_DATE DESC ");
        List<DbKnowledgeBase> dbKnowledgeBaseList = this.dbSysAreaSiteExample.selectByExample(example);
        return BeanUtils.copyListProperties(dbKnowledgeBaseList, KnowledgeBase::new, (dbKnowledgeBase, knowledgeBase1) -> knowledgeBase1.setZskDictTxt(this.knowledgeBaseClassificationText(dbKnowledgeBase.getZskDictOid(), true)));
    }

    /**
     * 保存/更新知识库信息
     * @param knowledgeBase
     */
    public void saveOrUpdate(KnowledgeBase knowledgeBase) {
        if(null != knowledgeBase.getId()) {
            DbKnowledgeBase dbKnowledgeBase = this.selectByPrimaryKey(knowledgeBase.getId());
            Assert.notNull(dbKnowledgeBase, MessageFormat.format("更新对象不存在！对象id为{0}", knowledgeBase.getId()));
            BeanUtils.copyProperties(knowledgeBase, dbKnowledgeBase);
            dbKnowledgeBase.setModifyDate(new Date());
            this.dbSysAreaSiteExample.updateByPrimaryKeySelective(dbKnowledgeBase);
            BeanUtils.copyProperties(dbKnowledgeBase, knowledgeBase);
        } else {
            DbKnowledgeBase dbKnowledgeBase = new DbKnowledgeBase();
            BeanUtils.copyProperties(knowledgeBase, dbKnowledgeBase);
            dbKnowledgeBase.setKnowledgeBaseOid(UUIDUtil.randomUUID());
            dbKnowledgeBase.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbKnowledgeBase.setCreateDate(new Date());
            dbKnowledgeBase.setCreatUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
            dbKnowledgeBase.setModifyDate(new Date());
            dbKnowledgeBase.setKnowledgeNumber(new SimpleDateFormat("MdHms").format(new Date()));
            this.dbSysAreaSiteExample.insertSelective(dbKnowledgeBase);
            BeanUtils.copyProperties(dbKnowledgeBase, knowledgeBase);
        }
    }

    /**
     * 删除知识库信息 支持批量删除
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        Assert.hasLength(ids, "删除主键不能为空！");
        Optional.ofNullable(Arrays.asList(ids.split(",")))
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(id -> {
                    DbKnowledgeBase dbKnowledgeBase = this.selectByPrimaryKey(Long.valueOf(id));
                    Assert.notNull(dbKnowledgeBase, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbKnowledgeBase.setIsDelete(SysCode.DELETE_STATUS.YES);
                    dbKnowledgeBase.setModifyDate(new Date());
                    this.dbSysAreaSiteExample.updateByPrimaryKeySelective(dbKnowledgeBase);
                });
    }

    /**
     * 获取一条知识库信息
     * @param id
     * @return
     */
    public KnowledgeBase getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbKnowledgeBase dbKnowledgeBase = this.selectByPrimaryKey(Long.valueOf(id));
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        BeanUtils.copyProperties(dbKnowledgeBase, knowledgeBase);
        return knowledgeBase;
    }

    /**
     * 根据主键id查询知识信息
     * @param id
     * @return
     */
    private DbKnowledgeBase selectByPrimaryKey(Long id) {
        Assert.notNull(id, "查询id不能为空！");
        DbKnowledgeBaseExample example = new DbKnowledgeBaseExample();
        DbKnowledgeBaseExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andIdEqualTo(id);
        List<DbKnowledgeBase> dbZskDictList = this.dbSysAreaSiteExample.selectByExample(example);
        return CollectionUtils.isEmpty(dbZskDictList) ? null : dbZskDictList.get(0);
    }

    /**
     * 查询分类名
     * @param zskDictOid 分类逻辑主键
     * @param fullPath  是否保存父分类名称
     * @return
     */
    private String knowledgeBaseClassificationText(String zskDictOid, Boolean fullPath) {
        List<ZskDictVo> zskDictVoList;
        if(fullPath) {
            zskDictVoList = this.dbZskDictMapper.queryAllParents(zskDictOid);
        } else {
            zskDictVoList = this.dbZskDictMapper.queryKnowledgeBaseByZskDictOid(zskDictOid);
        }
        List<String> tempList = Lists.newArrayList();
        return ListTurnString(zskDictVoList, tempList);
    }

    /**
     * 转化分类字典字符串
     * @param zskDictVoList
     * @param tempList
     * @return
     */
    private String ListTurnString(List<ZskDictVo> zskDictVoList, List<String> tempList) {
        if(!CollectionUtils.isEmpty(zskDictVoList) && !CollectionUtils.isEmpty(zskDictVoList.get(0).getChildrenList())) {
            tempList.add(0, zskDictVoList.get(0).getName());
            return ListTurnString(zskDictVoList.get(0).getChildrenList(), tempList);
        }
        if(CollectionUtils.isEmpty(zskDictVoList)) {
            return null;
        }
        return String.join("::", tempList);
    }


}

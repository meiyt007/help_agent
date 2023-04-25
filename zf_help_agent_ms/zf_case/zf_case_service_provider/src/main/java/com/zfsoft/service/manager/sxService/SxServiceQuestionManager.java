package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceQuestionMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceQuestion;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceQuestionExample;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceQuestionWithBLOBs;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxServiceQuestion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceQuestionServiceImpl
 * @Description: 实施清单-常见问题 实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:sxServiceQuestion")
public class SxServiceQuestionManager {

        @Resource
        private DbSxServiceQuestionMapper dbSxServiceQuestionMapper;

    //@Cacheable(key = "'getSxServiceQuestionByServiceOid:serviceOid=' + #serviceOid", unless = "#result == null")
        public List<SxServiceQuestion> getSxServiceQuestionByServiceOid(String serviceOid) {
            DbSxServiceQuestionExample dbSxServiceQuestionExample = new DbSxServiceQuestionExample();
            DbSxServiceQuestionExample.Criteria criteria = dbSxServiceQuestionExample.createCriteria();
            if(StrUtil.isNotEmpty(serviceOid)){
                criteria.andServiceOidEqualTo(serviceOid);
            }
            criteria.andDelFlagEqualTo((short)0);
            dbSxServiceQuestionExample.setOrderByClause(" sort desc");
            List<DbSxServiceQuestion> dbSxServiceQuestions = dbSxServiceQuestionMapper.selectByExample(dbSxServiceQuestionExample);
            List<SxServiceQuestion> sxServiceQuestionList = dbSxServiceQuestions.stream().map(sxServiceQuestion -> {
                SxServiceQuestion serviceQuestion = new SxServiceQuestion();
                BeanUtils.copyProperties(sxServiceQuestion,serviceQuestion);
                return serviceQuestion;
            }).collect(Collectors.toList());
            return sxServiceQuestionList;
        }

    //@Cacheable(key = "'getSxServiceQuestionByQuestionOid:questionOid=' + #questionOid", unless = "#result == null")
        public  SxServiceQuestion getSxServiceQuestionByQuestionOid(String questionOid){
            DbSxServiceQuestion dbSxServiceQuestion = dbSxServiceQuestionMapper.getSxServiceQuestionByOid(questionOid);
            if(null == dbSxServiceQuestion){
                throw  new ResultInfoException("实施清单 常见问题信息为空");
            }
            SxServiceQuestion sxServiceQuestion = new SxServiceQuestion();
            BeanUtils.copyProperties(dbSxServiceQuestion,sxServiceQuestion);
            return sxServiceQuestion;
        }

        public List<DbSxServiceQuestionWithBLOBs> queryList(SxServiceQuestion sxServiceQuestion) {
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = new DbSxServiceQuestionWithBLOBs();
            BeanUtils.copyProperties(sxServiceQuestion, dbSxServiceQuestionWithBLOBs);
            List<DbSxServiceQuestionWithBLOBs> dbSxServiceQuestionWithBLOBsList = this.dbSxServiceQuestionMapper.queryDbSxServiceQuestionList(dbSxServiceQuestionWithBLOBs);
            return dbSxServiceQuestionWithBLOBsList;
        }

        public void saveOrUpdateSxServiceQuestion(SxServiceQuestion sxServiceQuestion) {
            if (null != sxServiceQuestion.getId()) {
                DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = this.dbSxServiceQuestionMapper.selectByPrimaryKey(sxServiceQuestion.getId());
                Assert.notNull(sxServiceQuestion, MessageFormat.format("更新对象不存在！对象id为{0}", sxServiceQuestion.getId()));
                BeanUtils.copyProperties(sxServiceQuestion, dbSxServiceQuestionWithBLOBs);
                dbSxServiceQuestionWithBLOBs.setModifyDate(new Date());
                this.dbSxServiceQuestionMapper.updateByPrimaryKeySelective(dbSxServiceQuestionWithBLOBs);
            } else {
                DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = new DbSxServiceQuestionWithBLOBs();
                BeanUtils.copyProperties(sxServiceQuestion, dbSxServiceQuestionWithBLOBs);
                dbSxServiceQuestionWithBLOBs.setCreateDate(new Date());
                dbSxServiceQuestionWithBLOBs.setModifyDate(new Date());
                dbSxServiceQuestionWithBLOBs.setDelFlag((short)0);
                dbSxServiceQuestionWithBLOBs.setQuestionOid(UUID.randomUUID().toString().replaceAll("-", ""));
                this.dbSxServiceQuestionMapper.insert(dbSxServiceQuestionWithBLOBs);
            }
        }

        @Transactional(rollbackFor = Exception.class)
        public void delete(String id) {
            Assert.hasLength(id, "删除主键不能为空！");
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = this.dbSxServiceQuestionMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxServiceQuestionWithBLOBs, MessageFormat.format("删除对象不存在！对象id为{0}", id));
            dbSxServiceQuestionWithBLOBs.setDelFlag((short)1);
            dbSxServiceQuestionWithBLOBs.setModifyDate(new Date());
            this.dbSxServiceQuestionMapper.updateByPrimaryKeySelective(dbSxServiceQuestionWithBLOBs);
        }

        public DbSxServiceQuestion getDetail(String id) {
            Assert.hasLength(id, "查询主键不能为空！");
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = this.dbSxServiceQuestionMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxServiceQuestionWithBLOBs, MessageFormat.format("查询对象不存在！对象id为{0}", id));
            return dbSxServiceQuestionWithBLOBs;
        }

        public void enableQuestion(String id) {
            Assert.hasLength(id, "删除主键不能为空！");
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = this.dbSxServiceQuestionMapper.getSxServiceQuestionByOid(id);
            Assert.notNull(dbSxServiceQuestionWithBLOBs, MessageFormat.format("删除对象不存在！对象id为{0}", id));
            dbSxServiceQuestionWithBLOBs.setEnabledFlag((short)(dbSxServiceQuestionWithBLOBs.getEnabledFlag() == 0?1:0));
            dbSxServiceQuestionWithBLOBs.setModifyDate(new Date());
            this.dbSxServiceQuestionMapper.updateByPrimaryKeySelective(dbSxServiceQuestionWithBLOBs);
        }
}

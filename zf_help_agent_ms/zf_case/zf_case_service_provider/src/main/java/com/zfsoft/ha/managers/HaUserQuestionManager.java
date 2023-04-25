package com.zfsoft.ha.managers;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaQuestion;
import com.zfsoft.ha.data.vo.HaQuestionVo;
import com.zfsoft.ha.dbaccess.dao.DbHaUserQuestionMapper;
import com.zfsoft.ha.dbaccess.data.DbHaUserQuestion;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaUserQuestionExample;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户问题管理manager
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/15 上午10:07
 */
@Service
@Slf4j
public class HaUserQuestionManager {
    /**
     * 用户问题管理mapper
     */
    @Resource
    private DbHaUserQuestionMapper dbHaUserQuestionMapper;

    /**
     * @description: 查询用户常见问题分页信息
     * @params：[ question 用户问题, workUserId 用户ID, pageNumber 当前页码, pageSize 分页数量]
     * @return: PageResult<DbHaUserQuestion> 用户常见问题分页信息
     * @author: kangax
     * @date: 2022-07-28 00:15
     */
    public PageResult<DbHaUserQuestion> queryInfoWithPage(String question, String workUserId, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbHaUserQuestionExample dbHaUserQuestionExample = new DbHaUserQuestionExample();
        dbHaUserQuestionExample.setOrderByClause("CREATE_DATE DESC");
        DbHaUserQuestionExample.Criteria criteria = dbHaUserQuestionExample.createCriteria();
        if (StringUtils.isNotEmpty(question)) {
            criteria.andQuestionLike("%" + question.trim() + "%");
        }
        if (StringUtils.isNotEmpty(workUserId)) {
            criteria.andWorkUserIdEqualTo(Long.parseLong(workUserId));
        }
        dbHaUserQuestionExample.setOrderByClause("CREATE_DATE desc");
        //删除状态未删除作为查询条件
        criteria.andDeleteStatusEqualTo(Constants.DELETE_STATUS_NO);
        Page<DbHaUserQuestion> dbHaUserQuestions = (Page<DbHaUserQuestion>) dbHaUserQuestionMapper.selectByExample(dbHaUserQuestionExample);
        PageResult<DbHaUserQuestion> pageResult = new PageResult<>(dbHaUserQuestions.getPageNum(), dbHaUserQuestions.getPageSize(), dbHaUserQuestions.getTotal());
        pageResult.setData(dbHaUserQuestions);
        return pageResult;
    }

    /**
     * @description: 新增或修改用户常见问题信息
     * @params： [haQuestion 用户常见问题实体]
     * @author: kangax
     * @date: 2022-07-28 00:17
     */
    @Transactional
    public int saveUserQuestion(HaQuestion haQuestion) {
        DbHaUserQuestion dbHaUserQuestion = new DbHaUserQuestion();
        int index = 0;
        if (null == haQuestion.getId()) {
            haQuestion.setCreateDate(new Date());
            //新增
            haQuestion.setDeleteStatus(Constants.DELETE_STATUS_NO);//设置删除状态
            BeanUtils.copyProperties(haQuestion, dbHaUserQuestion);
            index = dbHaUserQuestionMapper.insert(dbHaUserQuestion);
        } else {
            //修改
            haQuestion.setUpdateDate(new Date());//设置更新时间
            BeanUtils.copyProperties(haQuestion, dbHaUserQuestion);
            index = dbHaUserQuestionMapper.updateByPrimaryKeySelective(dbHaUserQuestion);
        }
        haQuestion.setId(dbHaUserQuestion.getId());
        return index;
    }

    /**
     * @description: 获取单个用户常见问题
     * @params： [id 主键]
     * @return: HaQuestion 单个用户常见问题详细信息
     * @author: kangax
     * @date: 2022-07-28 00:19
     */
    @Transactional
    public HaQuestion getHaQuestionById(Long id) {
        DbHaUserQuestion dbHaUserQuestion = dbHaUserQuestionMapper.selectByPrimaryKey(id);
        if (dbHaUserQuestion != null) {
            HaQuestion haQuestion = new HaQuestion();
            BeanUtils.copyProperties(dbHaUserQuestion, haQuestion);
            DbHaWorkUser dbThaWorkUser = dbHaUserQuestion.getDbThaWorkUser();
            haQuestion.setWorkUserName(dbThaWorkUser.getName());
            return haQuestion;
        }
        return null;
    }

    /**
     * @description: 根据ID删除用户问题信息
     * @params： [id 主键]
     * @author: kangax
     * @date: 2022-07-28 00:20
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteHaQuestionById(Long id) {
        int index = 0;
        DbHaUserQuestion dbHaUserQuestion = dbHaUserQuestionMapper.selectByPrimaryKey(id);
        if (dbHaUserQuestion == null) {
            throw new ResultInfoException("用户问题信息为空");
        } else {
            index = dbHaUserQuestionMapper.deleteByUserQuestionId(Constants.DELETE_STATUS_YES, dbHaUserQuestion.getId());
            if (index == 0) {
                throw new ResultInfoException("用户问题信息删除失败，请稍后再试！");
            }
        }
        return index;
    }

    /**
     * @description: 用户常见问题列表获取 当question不为空时为模糊查询
     * @params： [question 用户常见问题]
     * @return: List<HaQuestionVo> 用户常见问题list
     * @author: kangax
     * @date: 2022-07-28 00:37
     */
    public List<HaQuestionVo> queryHaQuestionListByLikeQuestion(String question) {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        List<DbHaUserQuestion> dbHaUserQuestionList = dbHaUserQuestionMapper.queryHaQuestionListByLikeQuestion(currentHaLoginUserInfo.getId(), question);
        return getHaQuestionVos(dbHaUserQuestionList);
    }

    /**
     * @description: 根据ID获取用户常见问题对像
     * @params： [id主键]
     * @return: HaQuestionVo 用户常见问题详细信息
     * @author: kangax
     * @date: 2022-07-28 00:55
     */
    public HaQuestionVo getQuestionInfoById(Long id) {
        DbHaUserQuestion dbHaUserQuestion = dbHaUserQuestionMapper.selectByPrimaryKey(id);
        HaQuestionVo haQuestionVo = new HaQuestionVo();
        if (dbHaUserQuestion != null) {
            haQuestionVo.setId(dbHaUserQuestion.getId());
            haQuestionVo.setQuestion(dbHaUserQuestion.getQuestion());
            haQuestionVo.setAnswer(dbHaUserQuestion.getAnswer());
            haQuestionVo.setWorkUserId(dbHaUserQuestion.getWorkUserId());
            haQuestionVo.setCreateDate(dbHaUserQuestion.getCreateDate());
            return haQuestionVo;
        }
        return null;
    }

    /**
     * @description: 批量删除用户常见问题
     * @params： [ids 主键]
     * @return: void
     * @author: kangax
     * @date: 2022-07-28 01:19
     */
    public void deleteHaQuestionByIds(List<Long> ids) {
        for (Long id : ids) {
            DbHaUserQuestion dbHaUserQuestion = dbHaUserQuestionMapper.selectByPrimaryKey(id);
            if (dbHaUserQuestion != null) {
                dbHaUserQuestionMapper.deleteByUserQuestionId(Constants.DELETE_STATUS_YES, dbHaUserQuestion.getId());
            }
        }
    }


    /**
     * 获取全部问题
     * @author: zhaobf
     * @date: 2022-08-16 10:19
     */
    public List<HaQuestionVo> queryHaQuestionList() {
        List<DbHaUserQuestion> dbHaUserQuestionList = dbHaUserQuestionMapper.queryHaQuestionList();
        return getHaQuestionVos(dbHaUserQuestionList);
    }

    /**
     * 封装List<HaQuestionVo>
     * @author: zhaobf
     * @date: 2022-08-16 10:19
     */
    private List<HaQuestionVo> getHaQuestionVos(List<DbHaUserQuestion> dbHaUserQuestionList) {
        List<HaQuestionVo> haQuestionList = dbHaUserQuestionList.stream().map(dbHaUserQuestion -> {
            HaQuestionVo haQuestionVo = new HaQuestionVo();
            haQuestionVo.setId(dbHaUserQuestion.getId());
            haQuestionVo.setQuestion(dbHaUserQuestion.getQuestion());
            haQuestionVo.setAnswer(dbHaUserQuestion.getAnswer());
            haQuestionVo.setWorkUserId(dbHaUserQuestion.getWorkUserId());
            haQuestionVo.setCreateDate(dbHaUserQuestion.getCreateDate());
            return haQuestionVo;
        }).collect(Collectors.toList());
        return haQuestionList;
    }

}

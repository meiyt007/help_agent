package com.zfsoft.ha.managers;

import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.requestData.HaEvalResultRequestData;
import com.zfsoft.ha.data.responseData.HaEvalItemResponseData;
import com.zfsoft.ha.data.responseData.HaEvalResultResponseData;
import com.zfsoft.ha.dbaccess.dao.DbHaEvalItemMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaEvalResultItemMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaEvalResultMapper;
import com.zfsoft.ha.dbaccess.data.DbHaEvalItem;
import com.zfsoft.ha.dbaccess.data.DbHaEvalResult;
import com.zfsoft.ha.dbaccess.data.DbHaEvalResultItem;
import com.zfsoft.ha.dbaccess.data.example.DbHaEvalItemExample;
import com.zfsoft.ha.util.HaDockingHolder;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.superwindow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 帮代办评价manager
 * @author: kangax
 * @date: 2022-08-12 14:36
 **/
@Service
@Slf4j
public class HaEvalManager {

    /**
     * 评价项信息mapper
     */
    @Resource
    private DbHaEvalItemMapper dbHaEvalItemMapper;

    /**
     * 评价结果mapper
     */
    @Resource
    private DbHaEvalResultMapper dbHaEvalResultMapper;

    /**
     * 评价结果评价项信息mapper
     */
    @Resource
    private DbHaEvalResultItemMapper dbHaEvalResultItemMapper;

    /**
     * @description: 获取评价项信息
     * @author: kangax
     * @date: 2022-08-12 14:48
     */
    public List<HaEvalItemResponseData> getEvalItem() {
        DbHaEvalItemExample dbHaEvalItemExample = new DbHaEvalItemExample();
        DbHaEvalItemExample.Criteria criteria = dbHaEvalItemExample.createCriteria();
        //删除状态为未删除的
        criteria.andDeleteStatusEqualTo(Constants.DELETE_STATUS_NO);
        //创建时间排序
        dbHaEvalItemExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaEvalItem> dbHaEvalItems = dbHaEvalItemMapper.selectByExample(dbHaEvalItemExample);
        return dbHaEvalItems.stream().map(dbHaEvalItem -> {
            HaEvalItemResponseData data = new HaEvalItemResponseData();
            BeanUtils.copyProperties(dbHaEvalItem, data);
            return data;
        }).collect(Collectors.toList());
    }

    /**
     * @param haEvalResultRequestData 评价结果详细信息
     * @description: 保存评价结果
     * @author: kangax
     * @date: 2022-08-13 22:37
     */
    @Transactional(rollbackFor = ServiceException.class)
    public HaEvalResultResponseData saveEvalResult(HaEvalResultRequestData haEvalResultRequestData) {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //评价结果
        DbHaEvalResult dbHaEvalResult = new DbHaEvalResult();
        //工作人员编号
        dbHaEvalResult.setWorkUserId(currentHaLoginUserInfo.getId());
        //队列编号
        dbHaEvalResult.setQueueId(haEvalResultRequestData.getQueueId());
        //评价时间
        dbHaEvalResult.setEvalTime(DateUtil.getDate());
        //计算评价结果得分 计算多个评价项的平均得分，保存到评价结果中
        //计算评价项总分
        BigDecimal countScore = new BigDecimal("0");
        List<HaEvalResultRequestData.Result> itemResult = haEvalResultRequestData.getResult();
        for (HaEvalResultRequestData.Result result : itemResult) {
            String evalItemScoreStr = result.getEvalItemScore();
            BigDecimal itemScore = new BigDecimal(evalItemScoreStr);
            //总分
            countScore = countScore.add(itemScore);
        }
        //计算平均得分保留两位小数
        BigDecimal avgScore = countScore.divide(BigDecimal.valueOf(itemResult.size()), 2, BigDecimal.ROUND_HALF_UP);
        //计算多个评价项的平均得分，保存到评价结果中
        dbHaEvalResult.setEvalScore(avgScore);
        //评价内容
        dbHaEvalResult.setEvalContent(haEvalResultRequestData.getEvalContent());
        //创建人
        dbHaEvalResult.setCreateBy(currentHaLoginUserInfo.getName());
        //创建时间
        dbHaEvalResult.setCreateDate(DateUtil.getDate());
        //插入评价结果表
        dbHaEvalResultMapper.insert(dbHaEvalResult);

        haEvalResultRequestData.setId(dbHaEvalResult.getId());
        //评价项结果
        List<HaEvalResultRequestData.Result> resultList = haEvalResultRequestData.getResult();
        if (resultList.size() > 0) {
            for (HaEvalResultRequestData.Result result : resultList) {
                //评价结果评价项信息
                DbHaEvalResultItem dbHaEvalResultItem = new DbHaEvalResultItem();
                //评价结果编号
                dbHaEvalResultItem.setResultId(dbHaEvalResult.getId());
                //评价项编号
                dbHaEvalResultItem.setItemId(result.getEvalItemId());
                //评价项得分
                BigDecimal itemScore = new BigDecimal(result.getEvalItemScore());
                dbHaEvalResultItem.setItemScore(itemScore);
                //评价内容
                dbHaEvalResultItem.setItemContent(result.getEvalContent());
                //评价时间
                dbHaEvalResultItem.setEvalTime(DateUtil.getDate());
                //创建人
                dbHaEvalResultItem.setCreateBy(currentHaLoginUserInfo.getName());
                //创建时间
                dbHaEvalResultItem.setCreateDate(DateUtil.getDate());
                //插入评价结果评价项信息表
                dbHaEvalResultItemMapper.insert(dbHaEvalResultItem);
                result.setId(dbHaEvalResultItem.getId());
            }
        }
        haEvalResultRequestData.setResult(resultList);
        HaEvalResultResponseData haEvalResultResponseData = new HaEvalResultResponseData();
        BeanUtils.copyProperties(haEvalResultRequestData,haEvalResultResponseData);
        haEvalResultResponseData.setWorkUserId(currentHaLoginUserInfo.getId()+"");
        haEvalResultResponseData.setWorkUserName(currentHaLoginUserInfo.getName());
        haEvalResultResponseData.setWorkUserNum(currentHaLoginUserInfo.getWorkNumber());
        haEvalResultResponseData.setWorkUserHeadIcon(currentHaLoginUserInfo.getImage());
        return haEvalResultResponseData;
    }

    /**
     * @param haEvalResultRequestData 评价结果详细信息
     * @description: 保存评价结果
     * @author: kangax
     * @date: 2022-08-13 22:37
     */
    @Transactional(rollbackFor = ServiceException.class)
    public void updateEvalResult(HaEvalResultRequestData haEvalResultRequestData) {
        //获取当前登录用户信息
//        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        String currentUser = HaDockingHolder.getCurrentUser();
        //评价结果
        DbHaEvalResult dbHaEvalResult = dbHaEvalResultMapper.selectByPrimaryKey(haEvalResultRequestData.getId());

        //评价时间
        dbHaEvalResult.setEvalTime(DateUtil.getDate());
        //计算评价结果得分 计算多个评价项的平均得分，保存到评价结果中
        //计算评价项总分
        BigDecimal countScore = new BigDecimal("0");
        List<HaEvalResultRequestData.Result> itemResult = haEvalResultRequestData.getResult();
        for (HaEvalResultRequestData.Result result : itemResult) {
            String evalItemScoreStr = result.getEvalItemScore();
            BigDecimal itemScore = new BigDecimal(evalItemScoreStr);
            //总分
            countScore = countScore.add(itemScore);
        }
        //计算平均得分保留两位小数
        BigDecimal avgScore = countScore.divide(BigDecimal.valueOf(itemResult.size()), 2, BigDecimal.ROUND_HALF_UP);
        //计算多个评价项的平均得分，保存到评价结果中
        dbHaEvalResult.setEvalScore(avgScore);
        //评价内容
        dbHaEvalResult.setEvalContent(haEvalResultRequestData.getEvalContent());
        //创建人
        dbHaEvalResult.setUpdateBy(currentUser);
        //创建时间
        dbHaEvalResult.setUpdateDate(DateUtil.getDate());
        //插入评价结果表
        dbHaEvalResultMapper.updateByPrimaryKeySelective(dbHaEvalResult);
        //评价项结果
        List<HaEvalResultRequestData.Result> resultList = haEvalResultRequestData.getResult();
        if (resultList.size() > 0) {
            for (HaEvalResultRequestData.Result result : resultList) {
                //评价结果评价项信息
                DbHaEvalResultItem dbHaEvalResultItem = dbHaEvalResultItemMapper.selectByPrimaryKey(result.getId());

                //评价结果编号
                dbHaEvalResultItem.setResultId(dbHaEvalResult.getId());
                //评价项编号
                dbHaEvalResultItem.setItemId(result.getEvalItemId());
                //评价项得分
                BigDecimal itemScore = new BigDecimal(result.getEvalItemScore());
                dbHaEvalResultItem.setItemScore(itemScore);
                //评价内容
                dbHaEvalResultItem.setItemContent(result.getEvalContent());
                //评价时间
                dbHaEvalResultItem.setEvalTime(DateUtil.getDate());
                //创建人
                dbHaEvalResultItem.setUpdateBy(currentUser);
                //创建时间
                dbHaEvalResultItem.setUpdateDate(DateUtil.getDate());
                //插入评价结果评价项信息表
                dbHaEvalResultItemMapper.updateByPrimaryKeySelective(dbHaEvalResultItem);
            }
        }
    }

    /**
     * 获取工作人员的平均评价得分
     *
     * @return 工作人员萍爵分
     * @author yupeng
     * @date 2022年08月15 14:03:09
     */
    public BigDecimal getWorkUserEvalScore(Long workUserId) {

        BigDecimal evalScore = dbHaEvalResultMapper.getWorkUserEvalScore(workUserId);
        if (evalScore == null) {
            //默认5分
            evalScore = new BigDecimal(5);
        }
        //查询平均分
        return evalScore.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}

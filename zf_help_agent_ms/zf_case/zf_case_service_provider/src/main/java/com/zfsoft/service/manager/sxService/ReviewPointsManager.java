package com.zfsoft.service.manager.sxService;

import com.zfsoft.service.dbaccess.dao.sxService.DbReviewPointsMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.sxService.data.ReviewPoints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName reviewPointsManager
 * @Description: 审查要点实现类
 * @Author liangss
 * @Date 2021/07/12
 **/
@Service
@Slf4j
public class ReviewPointsManager {


    @Resource
    private DbReviewPointsMapper dbReviewPointsMapper;


    /**
     * 根据细化材料oid查询审查要点列表
     * @param refinedMaterialOid
     * @return
     */
    public List<ReviewPoints> getReviewPointsListByRefinedMaterialOid(String refinedMaterialOid) {
        DbReviewPointsExample dbReviewPointsExample=new DbReviewPointsExample();
        DbReviewPointsExample.Criteria criteria=dbReviewPointsExample.createCriteria();
        dbReviewPointsExample.setOrderByClause(" SERIAL_NUMBER ASC");
        if(StringUtils.isNotEmpty(refinedMaterialOid)){
            criteria.andRefinedMaterialOidEqualTo(refinedMaterialOid);
        }
        criteria.andDeleteStatusEqualTo((short) 0);
        List<DbReviewPoints> dbReviewPointss=dbReviewPointsMapper.selectByExample(dbReviewPointsExample);
        List<ReviewPoints> refinedMaterialList=dbReviewPointss.stream().map(dbReviewPoints -> {
            ReviewPoints reviewPoints = new ReviewPoints();
            BeanUtils.copyProperties(dbReviewPoints, reviewPoints);
            return reviewPoints;
        }).collect(Collectors.toList());
        return refinedMaterialList;
    }

    /**
     * 根据主键查询审查要点
     * @param id
     * @return
     */
    public ReviewPoints getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbReviewPoints dbReviewPoints=this.dbReviewPointsMapper.selectByPrimaryKey(Long.valueOf(id));
        ReviewPoints reviewPoints = new ReviewPoints();
        BeanUtils.copyProperties(dbReviewPoints, reviewPoints);
        return reviewPoints;
    }




    /**
     * 更新保存审查要点
     * @param reviewPoints
     * @throws Exception
     */
    public void saveOrUpdateReviewPoints(ReviewPoints reviewPoints) throws Exception {
        DbReviewPoints dbReviewPoints;
        if (null != reviewPoints.getId()) {
            dbReviewPoints=this.dbReviewPointsMapper.selectByPrimaryKey(reviewPoints.getId());
            Assert.notNull(dbReviewPoints, MessageFormat.format("更新对象不存在！对象id为{0}", dbReviewPoints.getId()));
            BeanUtils.copyProperties(reviewPoints, dbReviewPoints);
            dbReviewPoints.setModifyDate(new Date());
            this.dbReviewPointsMapper.updateByPrimaryKeySelective(dbReviewPoints);

        } else {
            dbReviewPoints = new DbReviewPoints();
            BeanUtils.copyProperties(reviewPoints, dbReviewPoints);
            dbReviewPoints.setDeleteStatus((short)0);
            dbReviewPoints.setCreateDate(new Date());
            dbReviewPoints.setModifyDate(new Date());
            dbReviewPoints.setOid(UUID.randomUUID().toString().replaceAll("-", ""));
            this.dbReviewPointsMapper.insert(dbReviewPoints);
        }

    }

/*

    public List<ReviewPoints> getReviewPointsListByRefinedMaterialOid(String refinedMaterialOid) {
        DbReviewPointsExample dbReviewPointsExample=new DbReviewPointsExample();
        DbReviewPointsExample.Criteria criteria=dbReviewPointsExample.createCriteria();
        if(StringUtils.isNotEmpty(refinedMaterialOid)){
            criteria.andRefinedMaterialOidEqualTo(refinedMaterialOid);
        }
        criteria.andDeleteStatusEqualTo((short) 0);
        List<DbReviewPoints> dbReviewPointss=dbReviewPointsMapper.selectByExample(dbReviewPointsExample);
        List<ReviewPoints> refinedMaterialList=dbReviewPointss.stream().map(dbReviewPoints -> {
            ReviewPoints reviewPoints = new ReviewPoints();
            BeanUtils.copyProperties(dbReviewPoints, reviewPoints);
            return reviewPoints;
        }).collect(Collectors.toList());
        return refinedMaterialList;
    }
*/




}

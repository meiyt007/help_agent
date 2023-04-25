package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaServiceCommonProblem;
import com.zfsoft.ha.data.vo.HaServiceCommonProblemVo;
import com.zfsoft.ha.dbaccess.dao.DbHaServiceCommonProblemMapper;
import com.zfsoft.ha.dbaccess.data.DbHaServiceCommonProblem;
import com.zfsoft.ha.dbaccess.data.example.DbHaServiceCommonProblemExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceOptionTitleMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionTitle;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionTitleExample;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/5 13:58
 */
@Service
@Slf4j
public class HaServiceCommonProblemManager {
    @Resource
    private DbHaServiceCommonProblemMapper dbHaServiceCommonProblemMapper;
    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;
    @Resource
    private DbSxServiceOptionTitleMapper dbSxServiceOptionTitleMapper ;

    /**
     * 保存常见问题表
     * @param haSerCP
     * @date 2022/8/11 20:04
     */
    @Transactional(rollbackFor = Exception.class)
    public int saveHaServiceCommonProblem(HaServiceCommonProblem haSerCP) {
        DbHaServiceCommonProblem dbHaServiceCommonProblem = new DbHaServiceCommonProblem();
        BeanUtils.copyProperties(haSerCP, dbHaServiceCommonProblem);
        dbHaServiceCommonProblem.setDeleteStatus(Short.valueOf(Constants.DELETE_STATUS_NO));
        dbHaServiceCommonProblem.setModifyDate(new Date());
        int index = 0;
        if (null == dbHaServiceCommonProblem.getId()) {
            index = dbHaServiceCommonProblemMapper.insert(dbHaServiceCommonProblem);
        } else {
            index = dbHaServiceCommonProblemMapper.update(dbHaServiceCommonProblem);
        }
        return index;
    }

    /**
     * 常见问题分页
     * @param haServiceCommonProblem 实例对象
     * @param pageNumber 页码
     * @param pageSize 分页大小
     * @return
     */
    public PageResult<HaServiceCommonProblem> queryInfoWithPage(HaServiceCommonProblem haServiceCommonProblem, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbHaServiceCommonProblemExample dbHaServiceCommonProblemExample = new DbHaServiceCommonProblemExample();
        dbHaServiceCommonProblemExample.setOrderByClause("MODIFY_DATE DESC");
        DbHaServiceCommonProblemExample.Criteria criteria = dbHaServiceCommonProblemExample.createCriteria();
        if (StringUtils.isNotEmpty(haServiceCommonProblem.getServiceOid())) {
            criteria.andServiceOidEqualTo(haServiceCommonProblem.getServiceOid());
        }
        //删除状态未删除作为查询条件
        criteria.andDeleteStatusEqualTo(Short.valueOf(Constants.DELETE_STATUS_NO));
        Page<DbHaServiceCommonProblem> page = (Page<DbHaServiceCommonProblem>) dbHaServiceCommonProblemMapper.selectByExample(dbHaServiceCommonProblemExample);
        PageResult<HaServiceCommonProblem> pageResult = new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal());
        List<HaServiceCommonProblem> haServiceCommonProblems = page.stream().map(dbHaServiceCommonProblem -> {
            HaServiceCommonProblem haUserResource = new HaServiceCommonProblem();
            org.springframework.beans.BeanUtils.copyProperties(dbHaServiceCommonProblem, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        pageResult.setData(haServiceCommonProblems);
        return pageResult;
    }

    /**
     * 常见问题
     * @param haServiceCommonProblem 实例对象

     * @return
     */
    public List<HaServiceCommonProblemVo> queryInfo(HaServiceCommonProblemVo haServiceCommonProblem) {

        DbHaServiceCommonProblemExample dbHaServiceCommonProblemExample = new DbHaServiceCommonProblemExample();
        dbHaServiceCommonProblemExample.setOrderByClause("MODIFY_DATE DESC");
        DbHaServiceCommonProblemExample.Criteria criteria = dbHaServiceCommonProblemExample.createCriteria();
        if (StringUtils.isNotEmpty(haServiceCommonProblem.getServiceOid())) {
            criteria.andServiceOidEqualTo(haServiceCommonProblem.getServiceOid());
        }
        if (StringUtils.isNotEmpty(haServiceCommonProblem.getOptionVal())) {
            criteria.andValueOidsLike("%"+haServiceCommonProblem.getOptionVal()+"%");
        }
        //删除状态未删除作为查询条件
        criteria.andDeleteStatusEqualTo(Short.valueOf(Constants.DELETE_STATUS_NO));
        List<DbHaServiceCommonProblem> dbHaServiceCommonProblems = dbHaServiceCommonProblemMapper.selectByExample(dbHaServiceCommonProblemExample);
        List<HaServiceCommonProblemVo> haServiceCommonProblems = dbHaServiceCommonProblems.stream().map(dbHaServiceCommonProblem -> {
            HaServiceCommonProblemVo result = new HaServiceCommonProblemVo();
            org.springframework.beans.BeanUtils.copyProperties(dbHaServiceCommonProblem, result);
            String optionValOids = result.getValueOids();
            List<String> optionValNames = new ArrayList<>();
            if(StrUtil.isNotEmpty(optionValOids)) {
                for(String oid : optionValOids.split(",")) {
                    SxServiceOptionVal optionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(oid);
                    if(null != optionVal) {
                        optionValNames.add(optionVal.getName());
                    }
                }
                result.setOptionNames(optionValNames);
            }

            return result;
        }).collect(Collectors.toList());
        return haServiceCommonProblems;
    }

    /**
     * 根据ids删除常见问题
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Transactional(rollbackFor = Exception.class)
    public HaServiceCommonProblem deleteCommonProblemById(String id) throws ServiceException {
        DbHaServiceCommonProblem dbHaServiceCommonProblem = dbHaServiceCommonProblemMapper.selectByPrimaryKey(Long.valueOf(id));
        dbHaServiceCommonProblem.setDeleteStatus(Short.valueOf(Constants.DELETE_STATUS_YES));
        dbHaServiceCommonProblem.setModifyDate(new Date());

        dbHaServiceCommonProblemMapper.update(dbHaServiceCommonProblem);
        HaServiceCommonProblem haServiceCommonProblem = new HaServiceCommonProblem();
        BeanUtils.copyProperties(dbHaServiceCommonProblem, haServiceCommonProblem);
        return haServiceCommonProblem;
    }

    /**
     * 初始化选项常见问题
     * @param serviceOid 事项oid
     * @param oid 常见问题oid
     * @return
     */
    public Map<String, Object> initCommonProblemInfo(String serviceOid, String oid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(StrUtil.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        DbSxServiceOptionTitleExample optionTitleExample = new DbSxServiceOptionTitleExample();
        DbSxServiceOptionTitleExample.Criteria criteria_title = optionTitleExample.createCriteria();
        criteria_title.andServiceOidEqualTo(serviceOid);
        criteria_title.andDeleteStatusEqualTo((short)0);
        List<DbSxServiceOptionTitle> optionTitles = dbSxServiceOptionTitleMapper.selectByExample(optionTitleExample);
        List<SxServiceOptionTitle> sxServiceOptionTitles = optionTitles.stream().map(dbSxServiceOptionTitle -> {
            SxServiceOptionTitle sxServiceOptionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(dbSxServiceOptionTitle, sxServiceOptionTitle);
            List<SxServiceOptionVal> sxServiceOptionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
            sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
            return sxServiceOptionTitle;
        }).collect(Collectors.toList());
        HaServiceCommonProblem haServiceCommonProblem = new HaServiceCommonProblem();
        if(StrUtil.isNotEmpty(oid)) {
            DbHaServiceCommonProblem dbHaServiceCommonProblem = dbHaServiceCommonProblemMapper.selectByPrimaryKey(Long.valueOf(oid));

            BeanUtils.copyProperties(dbHaServiceCommonProblem, haServiceCommonProblem);
            if(StrUtil.isNotEmpty(haServiceCommonProblem.getValueOids())) {
                String[] arr = haServiceCommonProblem.getValueOids().split(",");
                for(String valOid : arr) {
                    SxServiceOptionVal optionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(valOid);
                    for(SxServiceOptionTitle title : sxServiceOptionTitles) {
                        if(optionVal.getTitleOid().equals(title.getOid())) {
                            title.setIsSelected(1);
                        }
                    }
                }
            }
        }
        resultMap.put("optionTitleList", sxServiceOptionTitles);
        resultMap.put("comPro", haServiceCommonProblem);
        return resultMap;
    }
}


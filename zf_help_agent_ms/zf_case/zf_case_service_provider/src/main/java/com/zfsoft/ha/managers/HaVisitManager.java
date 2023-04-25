package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaVisit;
import com.zfsoft.ha.data.requestData.HaScanHelpInfoRequestData;
import com.zfsoft.ha.dbaccess.dao.DbHaVisitMapper;
import com.zfsoft.ha.dbaccess.data.DbHaVisit;
import com.zfsoft.ha.dbaccess.data.example.DbHaVisitExample;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/11 20:04
 */
@Service
@Slf4j
public class HaVisitManager {

    @Resource
    private DbHaVisitMapper dbHaVisitMapper;

    /**
     * 根据来访姓名和类型获取来访信息分页
     * @param name 姓名
     * @param visitType 类型
     * @date 2022/8/11 20:04
     */
    public PageResult<HaVisit> queryHaVisitByNameAndVisitType(String name, String visitType) {
        Page<DbHaVisit> dbHaUserResourcePage = (Page<DbHaVisit>) dbHaVisitMapper.queryHaVisitByNameAndVisitType(name, visitType);
        PageResult<HaVisit> pageResult = new PageResult<>(dbHaUserResourcePage.getPageNum(), dbHaUserResourcePage.getPageSize(), dbHaUserResourcePage.getTotal());
        List<HaVisit> haUserResourceList = dbHaUserResourcePage.stream().map(dbHaUserResource -> {
            HaVisit haUserResource = new HaVisit();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        pageResult.setData(haUserResourceList);
        return pageResult;
    }

    /**
     * 根据来身份证号获取来访信息
     * @param cardNo 身份证号 正常情况下只返回一条有用数据的列表
     * @date 2022/8/11 20:04
     */
    public List<HaVisit> queryHaVisitByCardNo(String cardNo){
        DbHaVisitExample dbHaVisitExample = new DbHaVisitExample();
        DbHaVisitExample.Criteria criteria = dbHaVisitExample.createCriteria();
        if (StrUtil.isNotEmpty(cardNo)) {
             criteria.andCardNoEqualTo(cardNo);
        }
        dbHaVisitExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaVisit> dbHaVisits = dbHaVisitMapper.selectByExample(dbHaVisitExample);
        List<HaVisit> haVisits = dbHaVisits.stream().map(dbHaUserResource -> {
            HaVisit haUserResource = new HaVisit();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        return haVisits;
    }

    /**
     * 根据实例获取来访列表
     * @param haVisit 实例
     * @date 2022/8/11 20:04
     */
    public List<HaVisit> queryHaVisitByExampleo(HaVisit haVisit){
        DbHaVisitExample dbHaVisitExample = new DbHaVisitExample();
        DbHaVisitExample.Criteria criteria = dbHaVisitExample.createCriteria();
        if (StrUtil.isNotEmpty(haVisit.getCardNo())) {
            criteria.andCardNoEqualTo(haVisit.getCardNo());
        }
        if (StrUtil.isNotEmpty(haVisit.getVisitType())) {
            criteria.andVisitTypeEqualTo(haVisit.getVisitType());
        }
        dbHaVisitExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaVisit> dbHaVisits = dbHaVisitMapper.selectByExample(dbHaVisitExample);
        List<HaVisit> haVisits = dbHaVisits.stream().map(dbHaUserResource -> {
            HaVisit haUserResource = new HaVisit();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        return haVisits;
    }

    public HaVisit getPhoneByCardNo(String cardNo) {

        DbHaVisit dbHaVisits = dbHaVisitMapper.selectByCardNo(cardNo);
        HaVisit haUserResource = new HaVisit();
        BeanUtils.copyProperties(dbHaVisits, haUserResource);
        return haUserResource;
    }

    /**
     * 根据扫码信息保存来访信息
     * @param requestData 来访信息
     * @date 2022/8/11 20:04
     */
    public boolean saveHavisitByhelpInfo(HaScanHelpInfoRequestData requestData){
        /**
         * 2022-08-15 zhaobf 修改，扫码信息插入来访表
         */
        int count = 0;
        if(StringUtil.isNotEmpty(requestData.getCardNo())){
            List<HaVisit> haVisitByCardNo = this.queryHaVisitByCardNo(requestData.getCardNo());
            HaVisit haVisit;
            if(haVisitByCardNo!=null && haVisitByCardNo.size()>0 && "1".equals(haVisitByCardNo.get(0).getVisitType())){
                haVisit = haVisitByCardNo.get(0);
            }else{
                haVisit= new HaVisit();
            }
            haVisit.setName(requestData.getName());
            haVisit.setCardNo(requestData.getCardNo());
            haVisit.setPhone(requestData.getPhone());
            haVisit.setVisitType("1");
            count += this.saveHaVisit(haVisit);
        }
        if(StringUtil.isNotEmpty(requestData.getCompanyCode())){
            List<HaVisit> haVisitByCompanyCode = this.queryHaVisitByCardNo(requestData.getCompanyCode());
            HaVisit haVisit;
            if(haVisitByCompanyCode!=null && haVisitByCompanyCode.size()>0 &&  "2".equals(haVisitByCompanyCode.get(0).getVisitType())){
                haVisit = haVisitByCompanyCode.get(0);
            }else{
                haVisit= new HaVisit();
            }
            haVisit.setName(requestData.getCompanyName());
            haVisit.setCardNo(requestData.getCompanyCode());
            haVisit.setPhone(requestData.getPhone());
            haVisit.setVisitType("2");
            count += this.saveHaVisit(haVisit);
        }
        return count > 0;

    }

    /**
     * 保存来访 信息
     * @param haVisit
     * @date 2022/8/11 20:04
     */
    @Transactional(rollbackFor = Exception.class)
    public int saveHaVisit(HaVisit haVisit) {
        DbHaVisit dbHaVisit = new DbHaVisit();
        BeanUtils.copyProperties(haVisit, dbHaVisit);
        int index = 0;
        if (null == haVisit.getId()) {
            dbHaVisit.setCreateDate(new Date());
            dbHaVisit.setUpdateDate(new Date());
            index = dbHaVisitMapper.insert(dbHaVisit);
        } else {
            dbHaVisit.setUpdateDate(new Date());
            index = dbHaVisitMapper.update(dbHaVisit);
        }
        return index;
    }







}

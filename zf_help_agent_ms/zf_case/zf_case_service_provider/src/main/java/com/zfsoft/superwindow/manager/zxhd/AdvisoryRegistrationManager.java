package com.zfsoft.superwindow.manager.zxhd;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.zxhd.AdvisoryRegistration;
import com.zfsoft.superwindow.dbaccess.dao.DbAdvisoryRegistrationMapper;
import com.zfsoft.superwindow.dbaccess.data.DbAdvisoryRegistration;
import com.zfsoft.superwindow.dbaccess.data.DbAdvisoryRegistrationExample;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName AdvisoryRegistrationImpl
 * @Description: 咨询登记接口实现类
 * @Author liangss
 * @Date 2020/10/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvisoryRegistrationManager {
        @Resource
        private DbAdvisoryRegistrationMapper dbAdvisoryRegistrationMapper;

    public PageResult<AdvisoryRegistration> queryAdvisoryRegistrationWithPage
            (AdvisoryRegistration advisoryRegistration, String advisoryStartDate, String advisoryEndDate, String districtStr,
             Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbAdvisoryRegistrationExample dbAdvisoryRegistrationExampl = new DbAdvisoryRegistrationExample();
        DbAdvisoryRegistrationExample.Criteria criteria =dbAdvisoryRegistrationExampl.createCriteria();
        if(null!=advisoryRegistration){
            if(null!= advisoryRegistration.getDistrictOid()){
                criteria.andDistrictOidEqualTo(advisoryRegistration.getDistrictOid());
            }
            if(null!= advisoryRegistration.getAdvisoryCode()){
                criteria.andAdvisoryCodeLike("%"+advisoryRegistration.getAdvisoryCode().trim()+"%");
            }
            if(null!= advisoryRegistration.getName()){
                criteria.andNameLike("%"+advisoryRegistration.getName().trim()+"%");
            }
            if (null!=districtStr) {
                String[] districtOids = districtStr.split(";");
                List list = Arrays.asList(districtOids);
                if (districtOids != null && districtOids.length > 0) {
                    criteria.andDistrictOidIn(list);
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            /*String newDateStr = simpleDateFormat.format(date);*/
            Date startDate = null;
            Date endDate = null;
            try {
                if(null!= advisoryStartDate) {
                    advisoryStartDate=advisoryStartDate+" 00:00:00";
                    startDate = simpleDateFormat.parse(advisoryStartDate);
                }
                if(null!= advisoryEndDate) {
                    advisoryEndDate=advisoryEndDate+" 23:59:59";
                    endDate = simpleDateFormat.parse(advisoryEndDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

           if(null!= advisoryStartDate){
                criteria.andCreateDateGreaterThanOrEqualTo(startDate);
            }
            if(null!= advisoryEndDate){
                criteria.andCreateDateLessThanOrEqualTo(endDate);
            }

        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbAdvisoryRegistrationExampl.setOrderByClause(" CREATE_DATE DESC ");
        Page<DbAdvisoryRegistration> dbAdvisoryRegistrations = (Page<DbAdvisoryRegistration>)dbAdvisoryRegistrationMapper.selectByExample(dbAdvisoryRegistrationExampl);
        PageResult<AdvisoryRegistration> pageResult = new PageResult<>(dbAdvisoryRegistrations.getPageNum(),dbAdvisoryRegistrations.getPageSize(),dbAdvisoryRegistrations.getTotal());
        List<AdvisoryRegistration> advisoryRegistrationList = dbAdvisoryRegistrations.stream().map(dbAdvisoryRegistration -> {
            AdvisoryRegistration dict = new AdvisoryRegistration();
            BeanUtils.copyProperties(dbAdvisoryRegistration,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(advisoryRegistrationList);
        return pageResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(AdvisoryRegistration advisoryRegistration) {
        if (null!=advisoryRegistration.getId()) {
            DbAdvisoryRegistration dbAdvisoryRegistration = this.dbAdvisoryRegistrationMapper.selectByPrimaryKey(advisoryRegistration.getId());
            Assert.notNull(advisoryRegistration, MessageFormat.format("更新对象不存在！对象id为{0}", advisoryRegistration.getId()));
            BeanUtils.copyProperties(advisoryRegistration, dbAdvisoryRegistration);
            dbAdvisoryRegistration.setModifyDate(new Date());
            dbAdvisoryRegistration.setDelFlag(SysCode.DELETE_STATUS.NO);
            this.dbAdvisoryRegistrationMapper.updateByPrimaryKeySelective(dbAdvisoryRegistration);

        } else {
            DbAdvisoryRegistration dbAdvisoryRegistration  = new DbAdvisoryRegistration();
            BeanUtils.copyProperties(advisoryRegistration, dbAdvisoryRegistration);
            dbAdvisoryRegistration.setModifyDate(new Date());
            dbAdvisoryRegistration.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbAdvisoryRegistration.setCreateDate(new Date());
            if(null!=CurrentLoginUserHolder.getCurrentLoginUser()) {
                dbAdvisoryRegistration.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                dbAdvisoryRegistration.setDistrictOid(CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid().toString());
                dbAdvisoryRegistration.setOrganOid(CurrentLoginUserHolder.getCurrentLoginUser().getOrganOid().toString());
            }
            //咨询编码
            String caseSource = SysCode.ZHYC_FLAG_PLAT;
            String advisoryCode = "ZX-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"-"+caseSource;
            dbAdvisoryRegistration.setAdvisoryCode(advisoryCode);
            dbAdvisoryRegistration.setAdvisoryRegidtrationOid(UUIDUtil.randomUUID());
            this.dbAdvisoryRegistrationMapper.insert(dbAdvisoryRegistration);

        }

    }

    public List<DbAdvisoryRegistration> queryList(String districtOid, String advisoryCode,String name,String advisoryStartDate,String advisoryEndDate) {
        DbAdvisoryRegistrationExample dbAdvisoryRegistrationExample = new DbAdvisoryRegistrationExample();
        if(null!= districtOid){
            dbAdvisoryRegistrationExample.createCriteria().andDistrictOidEqualTo(districtOid);
        }
        List<DbAdvisoryRegistration> advisoryRegistrationList = this.dbAdvisoryRegistrationMapper.selectByExample(dbAdvisoryRegistrationExample);
        return advisoryRegistrationList;
    }

    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public AdvisoryRegistration getOne(String id) {
            Assert.hasLength(id, "主键不能为空！");
            DbAdvisoryRegistration dbAdvisoryRegistration = this.dbAdvisoryRegistrationMapper.selectByPrimaryKey(Long.valueOf(id));
            AdvisoryRegistration advisoryRegistration= new AdvisoryRegistration();
            BeanUtils.copyProperties(dbAdvisoryRegistration,advisoryRegistration);
            return advisoryRegistration;
        }

    /**
     * 根据主键删除登记咨询信息
      * @param id
     */
    public void delAdvisoryRegistration(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbAdvisoryRegistration dbAdvisoryRegistration = this.dbAdvisoryRegistrationMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbAdvisoryRegistration, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbAdvisoryRegistration.setDelFlag(dbAdvisoryRegistration.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbAdvisoryRegistrationMapper.updateByPrimaryKeySelective(dbAdvisoryRegistration);
    }

}

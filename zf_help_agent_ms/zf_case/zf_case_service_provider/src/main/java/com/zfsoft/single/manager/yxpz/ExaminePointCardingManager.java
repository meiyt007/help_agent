package com.zfsoft.single.manager.yxpz;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.yxpz.ExaminePointCarding;
import com.zfsoft.single.data.yxpz.vo.ExaminePointCardingVo;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.dbaccess.dao.DbExaminePointCardingMapper;
import com.zfsoft.superwindow.dbaccess.data.DbExaminePointCarding;
import com.zfsoft.superwindow.dbaccess.data.DbExaminePointCardingExample;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName ExaminePointCardingManager
 * @Description: 审查要点
 * @Author liangss
 * @Date 2020-11-16 14:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExaminePointCardingManager {

    @Resource
    private DbExaminePointCardingMapper dbExaminePointCardingMapper;

    /**
     * 分页查询列表
     * @param examinePointCarding
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<ExaminePointCarding> queryExaminePointCardingWithPage
    (ExaminePointCarding examinePointCarding, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbExaminePointCardingExample dbExaminePointCardingExample=new DbExaminePointCardingExample();
        DbExaminePointCardingExample.Criteria criteria=dbExaminePointCardingExample.createCriteria();
        if(null!=examinePointCarding){
            if(null!=examinePointCarding.getAhsSamplePicInfoOid()){
                criteria.andAhsSamplePicInfoOidEqualTo(examinePointCarding.getAhsSamplePicInfoOid());
            }
            if(null!=examinePointCarding.getAttaOid()){
                criteria.andAttaOidEqualTo(examinePointCarding.getAttaOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbExaminePointCarding> dbExaminePointCardings= (Page<DbExaminePointCarding>)dbExaminePointCardingMapper.selectByExample(dbExaminePointCardingExample);
        PageResult<ExaminePointCarding> pageResult = new PageResult<>(dbExaminePointCardings.getPageNum(),dbExaminePointCardings.getPageSize(),dbExaminePointCardings.getTotal());
        List<ExaminePointCarding> examinePointCardingList= dbExaminePointCardings.stream().map(dbExaminePointCarding -> {
            ExaminePointCarding examinePointCarding1 = new ExaminePointCarding();
            BeanUtils.copyProperties(dbExaminePointCarding,examinePointCarding1);
            return examinePointCarding1;
        }).collect(Collectors.toList());
        pageResult.setData(examinePointCardingList);
        return pageResult;
    }


    /**
     * 查询列表
     * @param examinePointCarding
     * @return
     */
    public List<ExaminePointCarding> queryExaminePointCardingList(ExaminePointCarding examinePointCarding) {
        DbExaminePointCardingExample dbExaminePointCardingExample=new DbExaminePointCardingExample();
        DbExaminePointCardingExample.Criteria criteria=dbExaminePointCardingExample.createCriteria();
        if(null!=examinePointCarding){
            if(null!=examinePointCarding.getAhsSamplePicInfoOid()){
                criteria.andAhsSamplePicInfoOidEqualTo(examinePointCarding.getAhsSamplePicInfoOid());
            }
            if(null!=examinePointCarding.getAttaOid()){
                criteria.andAttaOidEqualTo(examinePointCarding.getAttaOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbExaminePointCarding> dbExaminePointCardingList=this.dbExaminePointCardingMapper.selectByExample(dbExaminePointCardingExample);
        return BeanUtils.copyListProperties(dbExaminePointCardingList, ExaminePointCarding::new);
    }



    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public ExaminePointCarding getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbExaminePointCarding dbExaminePointCarding=this.dbExaminePointCardingMapper.selectByPrimaryKey(Long.valueOf(id));
        ExaminePointCarding examinePointCarding=new ExaminePointCarding();
        BeanUtils.copyProperties(dbExaminePointCarding,examinePointCarding);
        return examinePointCarding;
    }


    /**
     * 根据条件对象查询
     * @param examinePointCarding
     * @return
     */
    public DbExaminePointCarding getExaminePointCardingByExaminePointCarding(ExaminePointCarding examinePointCarding) {
        DbExaminePointCardingExample dbExaminePointCardingExample=new DbExaminePointCardingExample();
        DbExaminePointCardingExample.Criteria criteria=dbExaminePointCardingExample.createCriteria();
        if(null!=examinePointCarding){
            if(null!=examinePointCarding.getAhsSamplePicInfoOid()){
                criteria.andAhsSamplePicInfoOidEqualTo(examinePointCarding.getAhsSamplePicInfoOid());
            }
            if(null!=examinePointCarding.getAttaOid()){
                criteria.andAttaOidEqualTo(examinePointCarding.getAttaOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbExaminePointCarding> dbExaminePointCardingList=this.dbExaminePointCardingMapper.selectByExample(dbExaminePointCardingExample);
        return CollectionUtils.isEmpty(dbExaminePointCardingList) ? null : dbExaminePointCardingList.get(0);

    }





    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbExaminePointCarding dbExaminePointCarding=this.dbExaminePointCardingMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbExaminePointCarding, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbExaminePointCarding.setDelFlag(dbExaminePointCarding.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbExaminePointCardingMapper.updateByPrimaryKeySelective(dbExaminePointCarding);
    }


    /**
     * 修改/保存信息
     * @param examinePointCarding
     * @throws Exception
     */
    public void saveOrUpdate(ExaminePointCarding examinePointCarding) {
        DbExaminePointCarding dbExaminePointCarding;
        if (null != examinePointCarding.getId()) {
            dbExaminePointCarding=this.dbExaminePointCardingMapper.selectByPrimaryKey(Long.valueOf(examinePointCarding.getId()));
            Assert.notNull(dbExaminePointCarding, MessageFormat.format("更新对象不存在！对象id为{0}", dbExaminePointCarding.getId()));
            BeanUtils.copyProperties(examinePointCarding, dbExaminePointCarding);
            dbExaminePointCarding.setModifyDate(new Date());
            this.dbExaminePointCardingMapper.updateByPrimaryKeySelective(dbExaminePointCarding);
        } else {
            dbExaminePointCarding = new DbExaminePointCarding();
            BeanUtils.copyProperties(examinePointCarding, dbExaminePointCarding);
            dbExaminePointCarding.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbExaminePointCarding.setCreateDate(new Date());
            dbExaminePointCarding.setModifyDate(new Date());
            dbExaminePointCarding.setExaminePointCardingOid(UUIDUtil.randomUUID());
            this.dbExaminePointCardingMapper.insert(dbExaminePointCarding);
        }

    }

    public void saveOrUpdateList( List<ExaminePointCarding> examinePointCardingList) {
        for(ExaminePointCarding examinePointCarding:examinePointCardingList){
            DbExaminePointCarding dbExaminePointCarding;
            if (null != examinePointCarding.getId()) {
                dbExaminePointCarding=this.dbExaminePointCardingMapper.selectByPrimaryKey(Long.valueOf(examinePointCarding.getId()));
                Assert.notNull(dbExaminePointCarding, MessageFormat.format("更新对象不存在！对象id为{0}", dbExaminePointCarding.getId()));
                BeanUtils.copyProperties(examinePointCarding, dbExaminePointCarding);
                dbExaminePointCarding.setModifyDate(new Date());
                this.dbExaminePointCardingMapper.updateByPrimaryKeySelective(dbExaminePointCarding);
            } else {
                dbExaminePointCarding = new DbExaminePointCarding();
                BeanUtils.copyProperties(examinePointCarding, dbExaminePointCarding);
                dbExaminePointCarding.setDelFlag(SysCode.DELETE_STATUS.NO);
                dbExaminePointCarding.setCreateDate(new Date());
                dbExaminePointCarding.setModifyDate(new Date());
                dbExaminePointCarding.setExaminePointCardingOid(UUIDUtil.randomUUID());
                this.dbExaminePointCardingMapper.insert(dbExaminePointCarding);
            }
        }
    }


    public void delExaminePointCarding(ExaminePointCarding examinePointCarding) {
        List<ExaminePointCarding>  examinePointCardingList=this.queryExaminePointCardingList(examinePointCarding);
        for(ExaminePointCarding examinePointCarding1:examinePointCardingList){
            DbExaminePointCarding dbExaminePointCarding=this.dbExaminePointCardingMapper.selectByPrimaryKey(examinePointCarding1.getId());
            Assert.notNull(dbExaminePointCarding, MessageFormat.format("操作对象不存在！对象id为{0}", examinePointCarding1.getId()));
            dbExaminePointCarding.setDelFlag(SysCode.DELETE_STATUS.YES);
            this.dbExaminePointCardingMapper.updateByPrimaryKeySelective(dbExaminePointCarding);
        }
    }
    public DbExaminePointCarding getExaminePointCardingByExaminePointCardingOid(String examinePointCardingOid) {
        DbExaminePointCardingExample dbExaminePointCardingExample=new DbExaminePointCardingExample();
        DbExaminePointCardingExample.Criteria criteria=dbExaminePointCardingExample.createCriteria();
        if(null!=examinePointCardingOid){
                criteria.andExaminePointCardingOidEqualTo(examinePointCardingOid);
        }
        //criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbExaminePointCarding> dbExaminePointCardingList=this.dbExaminePointCardingMapper.selectByExample(dbExaminePointCardingExample);
        return CollectionUtils.isEmpty(dbExaminePointCardingList) ? null : dbExaminePointCardingList.get(0);

    }
    public String  saveOrUpdateExaminePointCarding(ExaminePointCardingVo examinePointCardingVo) {
        String materiaOid=examinePointCardingVo.getMateriaOid();
        String serviceOid=examinePointCardingVo.getServiceOid();
        String ahsSamplePicInfoOid=examinePointCardingVo.getAhsSamplePicInfoOid();
        String attaOid=examinePointCardingVo.getAttaOid();
        //删除之前数据
        ExaminePointCarding examinePointCardingNew=new ExaminePointCarding();
        examinePointCardingNew.setExaminePointCardingOid(attaOid);
        examinePointCardingNew.setAhsSamplePicInfoOid(ahsSamplePicInfoOid);
        this.delExaminePointCarding(examinePointCardingNew);

        List<ExaminePointCarding> examinePointCardingList=examinePointCardingVo.getExaminePointCardingList();
        for(ExaminePointCarding epc:examinePointCardingList){
            DbExaminePointCarding dbExaminePointCarding;
                dbExaminePointCarding = new DbExaminePointCarding();
                dbExaminePointCarding.setAhsSamplePicInfoOid(ahsSamplePicInfoOid);
                dbExaminePointCarding.setAttaOid(attaOid);
                dbExaminePointCarding.setX(epc.getX());
                dbExaminePointCarding.setY(epc.getY());
                dbExaminePointCarding.setEx(epc.getEx());
                dbExaminePointCarding.setEy(epc.getEy());
                dbExaminePointCarding.setName(epc.getName());
                dbExaminePointCarding.setExamineInfo(epc.getExamineInfo());
                dbExaminePointCarding.setDelFlag(SysCode.DELETE_STATUS.NO);
                dbExaminePointCarding.setCreateDate(new Date());
                dbExaminePointCarding.setModifyDate(new Date());
                if(null!= CurrentLoginUserHolder.getCurrentLoginUser()){
                    dbExaminePointCarding.setUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                    dbExaminePointCarding.setUserName(CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString());
                }
                dbExaminePointCarding.setExaminePointCardingOid(UUIDUtil.randomUUID());
                this.dbExaminePointCardingMapper.insert(dbExaminePointCarding);
        }
        return  null;
    }

}

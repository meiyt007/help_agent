package com.zfsoft.single.manager.yxpz;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.yxpz.AhsSamplePicInfo;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.dbaccess.dao.DbAhsSamplePicInfoMapper;
import com.zfsoft.superwindow.dbaccess.data.DbAhsSamplePicInfo;
import com.zfsoft.superwindow.dbaccess.data.DbAhsSamplePicInfoExample;
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
 * @Description: 样表
 * @Author liangss
 * @Date 2020-11-16 15:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AhsSamplePicInfoManager {

    @Resource
    private DbAhsSamplePicInfoMapper dbAhsSamplePicInfoMapper;


    /**
     * 分页查询列表
     * @param ahsSamplePicInfo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<AhsSamplePicInfo> queryAhsSamplePicInfoWithPage
    (AhsSamplePicInfo ahsSamplePicInfo, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbAhsSamplePicInfoExample dbAhsSamplePicInfoExample=new DbAhsSamplePicInfoExample();
        DbAhsSamplePicInfoExample.Criteria criteria=dbAhsSamplePicInfoExample.createCriteria();
        if(null!=ahsSamplePicInfo){
            if(null!=ahsSamplePicInfo.getSampleInfoOid()){
                criteria.andSampleInfoOidEqualTo(ahsSamplePicInfo.getSampleInfoOid());
            }
            if(null!=ahsSamplePicInfo.getComboDirectoryOid()){
                criteria.andComboDirectoryOidEqualTo(ahsSamplePicInfo.getComboDirectoryOid());
            }
            if(null!=ahsSamplePicInfo.getMateriaOid()){
                criteria.andMateriaOidEqualTo(ahsSamplePicInfo.getMateriaOid());
            }
            if(null!=ahsSamplePicInfo.getAhsSamplePicInfoOid()){
                criteria.andAhsSamplePicInfoOidEqualTo(ahsSamplePicInfo.getAhsSamplePicInfoOid());
            }
            if(null!=ahsSamplePicInfo.getAttaOid()){
                criteria.andAttaOidEqualTo(ahsSamplePicInfo.getAttaOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbAhsSamplePicInfo> dbAhsSamplePicInfos= (Page<DbAhsSamplePicInfo>)dbAhsSamplePicInfoMapper.selectByExample(dbAhsSamplePicInfoExample);
        PageResult<AhsSamplePicInfo> pageResult = new PageResult<>(dbAhsSamplePicInfos.getPageNum(),dbAhsSamplePicInfos.getPageSize(),dbAhsSamplePicInfos.getTotal());
        List<AhsSamplePicInfo> ahsSamplePicInfoList= dbAhsSamplePicInfos.stream().map(dbAhsSamplePicInfo -> {
            AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
            BeanUtils.copyProperties(dbAhsSamplePicInfo,ahsSamplePicInfo1);
            return ahsSamplePicInfo1;
        }).collect(Collectors.toList());
        pageResult.setData(ahsSamplePicInfoList);
        return pageResult;
    }


    /**
     * 查询列表
     * @param ahsSamplePicInfo
     * @return
     */
    public List<AhsSamplePicInfo> queryAhsSamplePicInfoList(AhsSamplePicInfo ahsSamplePicInfo) {
        DbAhsSamplePicInfoExample dbAhsSamplePicInfoExample=new DbAhsSamplePicInfoExample();
        DbAhsSamplePicInfoExample.Criteria criteria=dbAhsSamplePicInfoExample.createCriteria();
        if(null!=ahsSamplePicInfo){
            if(null!=ahsSamplePicInfo.getSampleInfoOid()){
                criteria.andSampleInfoOidEqualTo(ahsSamplePicInfo.getSampleInfoOid());
            }
            if(null!=ahsSamplePicInfo.getComboDirectoryOid()){
                criteria.andComboDirectoryOidEqualTo(ahsSamplePicInfo.getComboDirectoryOid());
            }
            if(null!=ahsSamplePicInfo.getMateriaOid()){
                criteria.andMateriaOidEqualTo(ahsSamplePicInfo.getMateriaOid());
            }
            if(null!=ahsSamplePicInfo.getAhsSamplePicInfoOid()){
                criteria.andAhsSamplePicInfoOidEqualTo(ahsSamplePicInfo.getAhsSamplePicInfoOid());
            }
            if(null!=ahsSamplePicInfo.getAttaOid()){
                criteria.andAttaOidEqualTo(ahsSamplePicInfo.getAttaOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbAhsSamplePicInfo> dbAhsSamplePicInfoList=this.dbAhsSamplePicInfoMapper.selectByExample(dbAhsSamplePicInfoExample);
        return BeanUtils.copyListProperties(dbAhsSamplePicInfoList, AhsSamplePicInfo::new);
    }



    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public AhsSamplePicInfo getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbAhsSamplePicInfo dbAhsSamplePicInfo=this.dbAhsSamplePicInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        AhsSamplePicInfo ahsSamplePicInfo=new AhsSamplePicInfo();
        BeanUtils.copyProperties(dbAhsSamplePicInfo,ahsSamplePicInfo);
        return ahsSamplePicInfo;
    }


    /**
     * 根据条件对象查询
     * @param ahsSamplePicInfo
     * @return
     */
    public DbAhsSamplePicInfo getAhsSamplePicInfoByAhsSamplePicInfo(AhsSamplePicInfo ahsSamplePicInfo) {
        DbAhsSamplePicInfoExample dbAhsSamplePicInfoExample=new DbAhsSamplePicInfoExample();
        DbAhsSamplePicInfoExample.Criteria criteria=dbAhsSamplePicInfoExample.createCriteria();
        if(null!=ahsSamplePicInfo){
            if(null!=ahsSamplePicInfo.getAhsSamplePicInfoOid()){
                criteria.andAhsSamplePicInfoOidEqualTo(ahsSamplePicInfo.getAhsSamplePicInfoOid());
            }
            if(null!=ahsSamplePicInfo.getAttaOid()){
                criteria.andAttaOidEqualTo(ahsSamplePicInfo.getAttaOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbAhsSamplePicInfo> dbAhsSamplePicInfoList=this.dbAhsSamplePicInfoMapper.selectByExample(dbAhsSamplePicInfoExample);
        return CollectionUtils.isEmpty(dbAhsSamplePicInfoList) ? null : dbAhsSamplePicInfoList.get(0);

    }

    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbAhsSamplePicInfo dbAhsSamplePicInfo=this.dbAhsSamplePicInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbAhsSamplePicInfo, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbAhsSamplePicInfo.setDelFlag(dbAhsSamplePicInfo.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbAhsSamplePicInfoMapper.updateByPrimaryKeySelective(dbAhsSamplePicInfo);
    }


    /**
     * 修改/保存信息
     * @param ahsSamplePicInfo
     * @throws Exception
     */
    public void saveOrUpdate(AhsSamplePicInfo ahsSamplePicInfo) {
        DbAhsSamplePicInfo dbAhsSamplePicInfo;
        if (null != ahsSamplePicInfo.getId()) {
            dbAhsSamplePicInfo=this.dbAhsSamplePicInfoMapper.selectByPrimaryKey(Long.valueOf(ahsSamplePicInfo.getId()));
            Assert.notNull(dbAhsSamplePicInfo, MessageFormat.format("更新对象不存在！对象id为{0}", dbAhsSamplePicInfo.getId()));
            BeanUtils.copyProperties(ahsSamplePicInfo, dbAhsSamplePicInfo);
            dbAhsSamplePicInfo.setModifyDate(new Date());
            this.dbAhsSamplePicInfoMapper.updateByPrimaryKeySelective(dbAhsSamplePicInfo);
        } else {
            dbAhsSamplePicInfo = new DbAhsSamplePicInfo();
            BeanUtils.copyProperties(ahsSamplePicInfo, dbAhsSamplePicInfo);
            dbAhsSamplePicInfo.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbAhsSamplePicInfo.setCreateDate(new Date());
            dbAhsSamplePicInfo.setModifyDate(new Date());
            dbAhsSamplePicInfo.setAhsSamplePicInfoOid(UUIDUtil.randomUUID());
            this.dbAhsSamplePicInfoMapper.insert(dbAhsSamplePicInfo);
        }

    }


}

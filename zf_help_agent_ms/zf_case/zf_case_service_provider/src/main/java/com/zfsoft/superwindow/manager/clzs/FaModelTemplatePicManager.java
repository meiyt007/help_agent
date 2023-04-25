package com.zfsoft.superwindow.manager.clzs;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplatePic;
import com.zfsoft.superwindow.dbaccess.dao.DbFaModelTemplatePicMapper;
import com.zfsoft.superwindow.dbaccess.data.DbFaModelTemplatePic;
import com.zfsoft.superwindow.dbaccess.data.DbFaModelTemplatePicExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
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
 * @ClassName FaModelTemplatePicManager
 * @Description: 模板底图实现类
 * @Author liangss
 * @Date 2020-11-07 16:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaModelTemplatePicManager {
    @Resource
    private DbFaModelTemplatePicMapper dbFaModelTemplatePicMapper;

    /**
     * 分页查询列表
     * @param faModelTemplatePic
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<FaModelTemplatePic> queryFaModelTemplatePicWithPage
    (FaModelTemplatePic faModelTemplatePic, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFaModelTemplatePicExample dbFaModelTemplatePicExample=new DbFaModelTemplatePicExample();
        DbFaModelTemplatePicExample.Criteria criteria=dbFaModelTemplatePicExample.createCriteria();
        if(null!=faModelTemplatePic){
            if(null!=faModelTemplatePic.getFilePath()){
                criteria.andFilePathEqualTo(faModelTemplatePic.getFilePath());
            }
            if(null!=faModelTemplatePic.getOriginName()){
                criteria.andOriginNameEqualTo("%"+faModelTemplatePic.getOriginName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbFaModelTemplatePic> dbFaModelTemplatePics= (Page<DbFaModelTemplatePic>)dbFaModelTemplatePicMapper.selectByExample(dbFaModelTemplatePicExample);
        PageResult<FaModelTemplatePic> pageResult = new PageResult<>(dbFaModelTemplatePics.getPageNum(),dbFaModelTemplatePics.getPageSize(),dbFaModelTemplatePics.getTotal());
        List<FaModelTemplatePic> faModelTemplatePicList = dbFaModelTemplatePics.stream().map(dbFaModelTemplatePic -> {
            FaModelTemplatePic faModelTemplatePic1 = new FaModelTemplatePic();
            BeanUtils.copyProperties(dbFaModelTemplatePic,faModelTemplatePic1);
            return faModelTemplatePic1;
        }).collect(Collectors.toList());
        pageResult.setData(faModelTemplatePicList);
        return pageResult;
    }


    public List<FaModelTemplatePic> getFaModelTemplatePicList(FaModelTemplatePic faModelTemplatePic) {
        DbFaModelTemplatePicExample dbFaModelTemplatePicExample=new DbFaModelTemplatePicExample();
        DbFaModelTemplatePicExample.Criteria criteria=dbFaModelTemplatePicExample.createCriteria();
        if(null!=faModelTemplatePic){
            if(null!=faModelTemplatePic.getFilePath()){
                criteria.andFilePathEqualTo(faModelTemplatePic.getFilePath());
            }
            if(null!=faModelTemplatePic.getOriginName()){
                criteria.andOriginNameLike("%"+faModelTemplatePic.getOriginName().trim()+"%");
            }
            if(null!=faModelTemplatePic.getName()){
                criteria.andNameLike("%"+faModelTemplatePic.getName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplatePic> dbFaModelTemplatePicList=this.dbFaModelTemplatePicMapper.selectByExample(dbFaModelTemplatePicExample);
        return BeanUtils.copyListProperties(dbFaModelTemplatePicList, FaModelTemplatePic::new);
    }


    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public FaModelTemplatePic getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplatePic dbFaModelTemplatePic=this.dbFaModelTemplatePicMapper.selectByPrimaryKey(Long.valueOf(id));
        FaModelTemplatePic faModelTemplatePic=new FaModelTemplatePic();
        BeanUtils.copyProperties(dbFaModelTemplatePic,faModelTemplatePic);
        return faModelTemplatePic;
    }


    /**
     * 根据条件对象查询
     * @param faModelTemplatePic
     * @return
     */
    public DbFaModelTemplatePic getFaModelTemplatePicByFaModelTemplatePic(FaModelTemplatePic faModelTemplatePic) {
        DbFaModelTemplatePicExample dbFaModelTemplatePicExample=new DbFaModelTemplatePicExample();
        DbFaModelTemplatePicExample.Criteria criteria=dbFaModelTemplatePicExample.createCriteria();
        if(null!=faModelTemplatePic){
            if(null!=faModelTemplatePic.getFilePath()){
                criteria.andFilePathEqualTo(faModelTemplatePic.getFilePath().trim());
            }
            if(null!=faModelTemplatePic.getOriginName()){
                criteria.andOriginNameEqualTo("%"+faModelTemplatePic.getOriginName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplatePic>  dbFaModelTemplatePicList=this.dbFaModelTemplatePicMapper.selectByExample(dbFaModelTemplatePicExample);
        return CollectionUtils.isEmpty(dbFaModelTemplatePicList) ? null : dbFaModelTemplatePicList.get(0);

    }

    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplatePic dbFaModelTemplatePic=this.dbFaModelTemplatePicMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbFaModelTemplatePic, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbFaModelTemplatePic.setDelFlag(dbFaModelTemplatePic.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbFaModelTemplatePicMapper.updateByPrimaryKeySelective(dbFaModelTemplatePic);

    }

    /**
     * 更新/保存信息
     * @param faModelTemplatePic
     * @throws Exception
     */
    public void saveOrUpdate(FaModelTemplatePic faModelTemplatePic) throws Exception {
        DbFaModelTemplatePic dbFaModelTemplatePic;
        if (null != faModelTemplatePic.getId()) {
            dbFaModelTemplatePic = this.dbFaModelTemplatePicMapper.selectByPrimaryKey(faModelTemplatePic.getId());
            Assert.notNull(dbFaModelTemplatePic, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelTemplatePic.getId()));
            BeanUtils.copyProperties(faModelTemplatePic, dbFaModelTemplatePic);
            dbFaModelTemplatePic.setModifyDate(new Date());
            this.dbFaModelTemplatePicMapper.updateByPrimaryKeySelective(dbFaModelTemplatePic);
        } else {
            dbFaModelTemplatePic = new DbFaModelTemplatePic();
            BeanUtils.copyProperties(faModelTemplatePic, dbFaModelTemplatePic);
            dbFaModelTemplatePic.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbFaModelTemplatePic.setCreateDate(new Date());
            dbFaModelTemplatePic.setModifyDate(new Date());
            dbFaModelTemplatePic.setFaModelTemplatePicOid(UUIDUtil.randomUUID());
            this.dbFaModelTemplatePicMapper.insert(dbFaModelTemplatePic);
        }

    }


}

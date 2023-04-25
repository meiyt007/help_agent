package com.zfsoft.single.manager.clzs;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.dbaccess.dao.DbFaMaterialPicOcrResultMapper;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResult;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResultExample;
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
 * @ClassName FaMaterialPicOcrResultManager
 * @Description: 材料目录图片识别结果实现类
 * @Author liangss
 * @Date 2020-11-07 16:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaMaterialPicOcrResultManager {

    @Resource
    private DbFaMaterialPicOcrResultMapper dbFaMaterialPicOcrResultMapper;
    /**
     * 分页查询列表
     * @param faMaterialPicOcrResult
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<FaMaterialPicOcrResult> queryFaMaterialPicOcrResultWithPage
    (FaMaterialPicOcrResult faMaterialPicOcrResult, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFaMaterialPicOcrResultExample dbFaMaterialPicOcrResultExample=new DbFaMaterialPicOcrResultExample();
        DbFaMaterialPicOcrResultExample.Criteria criteria=dbFaMaterialPicOcrResultExample.createCriteria();
        if(null!=faMaterialPicOcrResult){
            if(null!=faMaterialPicOcrResult.getMaterialOid()){
                criteria.andMaterialOidEqualTo(faMaterialPicOcrResult.getMaterialOid());
            }
            if(null!=faMaterialPicOcrResult.getCataCode()){
                criteria.andCataCodeLike("%"+faMaterialPicOcrResult.getCataCode()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbFaMaterialPicOcrResult> dbFaMaterialPicOcrResults= (Page<DbFaMaterialPicOcrResult>)dbFaMaterialPicOcrResultMapper.selectByExample(dbFaMaterialPicOcrResultExample);
        PageResult<FaMaterialPicOcrResult> pageResult = new PageResult<>(dbFaMaterialPicOcrResults.getPageNum(),dbFaMaterialPicOcrResults.getPageSize(),dbFaMaterialPicOcrResults.getTotal());
        List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = dbFaMaterialPicOcrResults.stream().map(dbFaMaterialPicOcrResult -> {
            FaMaterialPicOcrResult faMaterialPicOcrResult1 = new FaMaterialPicOcrResult();
            BeanUtils.copyProperties(dbFaMaterialPicOcrResult,faMaterialPicOcrResult1);
            return faMaterialPicOcrResult1;
        }).collect(Collectors.toList());
        pageResult.setData(faMaterialPicOcrResultList);
        return pageResult;
    }



    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public FaMaterialPicOcrResult getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult=this.dbFaMaterialPicOcrResultMapper.selectByPrimaryKey(Long.valueOf(id));
        FaMaterialPicOcrResult faMaterialPicOcrResult=new FaMaterialPicOcrResult();
        BeanUtils.copyProperties(dbFaMaterialPicOcrResult,faMaterialPicOcrResult);
        return faMaterialPicOcrResult;
    }


    /**
     * 根据条件对象查询
     * @param faMaterialPicOcrResult
     * @return
     */
    public DbFaMaterialPicOcrResult getFaMaterialPicOcrResultByFaMaterialPicOcrResult(FaMaterialPicOcrResult faMaterialPicOcrResult) {
        DbFaMaterialPicOcrResultExample dbFaMaterialPicOcrResultExample=new DbFaMaterialPicOcrResultExample();
        DbFaMaterialPicOcrResultExample.Criteria criteria=dbFaMaterialPicOcrResultExample.createCriteria();
        if(null!=faMaterialPicOcrResult){
            if(null!=faMaterialPicOcrResult.getMaterialOid()){
                criteria.andMaterialOidEqualTo(faMaterialPicOcrResult.getMaterialOid());
            }
            if(null!=faMaterialPicOcrResult.getUuid()){
                criteria.andUuidEqualTo(faMaterialPicOcrResult.getUuid());
            }
            if(null!=faMaterialPicOcrResult.getCataCode()){
                criteria.andCataCodeLike("%"+faMaterialPicOcrResult.getCataCode()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaMaterialPicOcrResultExample.setOrderByClause(" CREATE_DATE desc");
        List<DbFaMaterialPicOcrResult>  dbFaMaterialPicOcrResultList=this.dbFaMaterialPicOcrResultMapper.selectByExample(dbFaMaterialPicOcrResultExample);
        return CollectionUtils.isEmpty(dbFaMaterialPicOcrResultList) ? null : dbFaMaterialPicOcrResultList.get(0);

    }




    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult=this.dbFaMaterialPicOcrResultMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbFaMaterialPicOcrResult, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbFaMaterialPicOcrResult.setDelFlag(dbFaMaterialPicOcrResult.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbFaMaterialPicOcrResultMapper.updateByPrimaryKeySelective(dbFaMaterialPicOcrResult);

    }



    public void saveOrUpdate(FaMaterialPicOcrResult faMaterialPicOcrResult){
        try {
            DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult;
            if (null != faMaterialPicOcrResult.getId()) {
                dbFaMaterialPicOcrResult = this.dbFaMaterialPicOcrResultMapper.selectByPrimaryKey(faMaterialPicOcrResult.getId());
                Assert.notNull(dbFaMaterialPicOcrResult, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaMaterialPicOcrResult.getId()));
                BeanUtils.copyProperties(faMaterialPicOcrResult, dbFaMaterialPicOcrResult);
                dbFaMaterialPicOcrResult.setModifyDate(new Date());
                this.dbFaMaterialPicOcrResultMapper.updateByPrimaryKeySelective(dbFaMaterialPicOcrResult);
            } else {
                dbFaMaterialPicOcrResult = new DbFaMaterialPicOcrResult();
                BeanUtils.copyProperties(faMaterialPicOcrResult, dbFaMaterialPicOcrResult);
                dbFaMaterialPicOcrResult.setDelFlag(SysCode.DELETE_STATUS.NO);
                dbFaMaterialPicOcrResult.setCreateDate(new Date());
                dbFaMaterialPicOcrResult.setModifyDate(new Date());
                dbFaMaterialPicOcrResult.setFaMaterialPicOcrResultOid(UUIDUtil.randomUUID());
                this.dbFaMaterialPicOcrResultMapper.insert(dbFaMaterialPicOcrResult);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public List<FaMaterialPicOcrResult> getFaMaterialPicOcrResultByFmpor(FaMaterialPicOcrResult faMaterialPicOcrResult) {
        DbFaMaterialPicOcrResultExample dbFaMaterialPicOcrResultExample=new DbFaMaterialPicOcrResultExample();
        DbFaMaterialPicOcrResultExample.Criteria criteria=dbFaMaterialPicOcrResultExample.createCriteria();
        if(null!=faMaterialPicOcrResult){
            if(null!=faMaterialPicOcrResult.getUuid()){
                criteria.andUuidEqualTo(faMaterialPicOcrResult.getUuid());
            }
            if(null!=faMaterialPicOcrResult.getMaterialOid()){
                criteria.andMaterialOidEqualTo(faMaterialPicOcrResult.getMaterialOid());
            }
            if(null!=faMaterialPicOcrResult.getCataCode()){
                criteria.andCataCodeLike("%"+faMaterialPicOcrResult.getCataCode()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaMaterialPicOcrResult>  dbFaMaterialPicOcrResultList=this.dbFaMaterialPicOcrResultMapper.selectByExample(dbFaMaterialPicOcrResultExample);
        return BeanUtils.copyListProperties(dbFaMaterialPicOcrResultList, FaMaterialPicOcrResult::new);


    }

    /**
     * 根据办件材料oid更新删除办件的审核ocr结果记录
     * @param caseMaterialOid
     */
    public void  updateDbFaMaterialPicOcrResultByCaseMaterialOid(String caseMaterialOid){
        //this.dbFaMaterialPicOcrResultMapper.updateDbFaMaterialPicOcrResultByCaseMaterialOid(caseMaterialOid);
    }

    /**
     * 根据办件oid更新删除办件的审核ocr结果记录
     * @param uuid
     */
    public void  updateDbFaMaterialPicOcrResultByCaselOid(String uuid){
        //this.dbFaMaterialPicOcrResultMapper.updateDbFaMaterialPicOcrResultByCaselOid(uuid);
    }

}

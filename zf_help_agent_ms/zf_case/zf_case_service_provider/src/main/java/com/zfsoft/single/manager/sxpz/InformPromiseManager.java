package com.zfsoft.single.manager.sxpz;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.sxpz.InformPromise;
import com.zfsoft.single.data.sxpz.vo.InformPromiseVo;
import com.zfsoft.single.dbaccess.dao.sxpz.DbInformPromiseMapper;
import com.zfsoft.single.dbaccess.data.sxpz.DbInformPromise;
import com.zfsoft.single.dbaccess.data.sxpz.DbInformPromiseExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * 告知承诺清单管理
 * dongxiuli
 **/
@Service
@Slf4j
public class InformPromiseManager {
    @Resource
    private DbInformPromiseMapper dbInformPromiseMapper;

    /**
     * 告知承诺清单列表查询
     * @param informPromise
     * @return
     */
    public PageResult<InformPromise> queryPage(InformPromise informPromise, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);

        DbInformPromiseExample dbInformPromiseExample = new DbInformPromiseExample();
        DbInformPromiseExample.Criteria criteria = dbInformPromiseExample.createCriteria();
        if(null!=informPromise){
            if(StrUtil.isNotEmpty(informPromise.getImplementCode())){
                criteria.andImplementCodeEqualTo(informPromise.getImplementCode());
            }
            if(StrUtil.isNotEmpty(informPromise.getServiceName())){
                criteria.andServiceNameLike("%"+informPromise.getServiceName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.STATUS.NO);
        dbInformPromiseExample.setOrderByClause(" MODIFY_DATE DESC ");
        List<DbInformPromise> dbInformPromise=dbInformPromiseMapper.selectByExample(dbInformPromiseExample);
        List<InformPromise> listPromise= BeanUtils.copyListProperties(dbInformPromise, InformPromise::new);
        PageResult<InformPromise> pageResult = new PageResult<>(pageNum,pageSize,listPromise.size());
        pageResult.setData(listPromise);
        return pageResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(InformPromiseVo informPromise) {
        if(informPromise!=null){
            if(informPromise.getInforList()!=null&&informPromise.getInforList().size()>0){
                for(Object infor: informPromise.getInforList()){
                    InformPromise promise=(InformPromise)infor;
                    //判断是否存在告知清单中，不存在则保存
                    DbInformPromise dbOldInform=dbInformPromiseMapper.selectByServiceOid(promise.getServiceOid());
                    if(dbOldInform!=null){
                    }else{
                        promise.setPromiseOid(UUIDUtil.randomUUID());
                        promise.setDelFlag(SysCode.STATUS.NO);
                        promise.setModifyDate(new Date());
                        DbInformPromise dbInformPromise=new DbInformPromise();
                        BeanUtils.copyProperties(promise,dbInformPromise);
                        dbInformPromiseMapper.insertSelective(dbInformPromise);
                    }
                }
            }
            //删除多余的
            if(informPromise.getDelServiceOids()!=null && informPromise.getDelServiceOids().size()>0){
                batchInformInfo(informPromise.getDelServiceOids());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteInform(Long id) {
      dbInformPromiseMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取所有授权的id
     * @return
     */
    public String allInformServiceOids() {
        DbInformPromiseExample example = new DbInformPromiseExample();
        example.createCriteria().andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbInformPromise> dbInformPromise = this.dbInformPromiseMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(dbInformPromise)){
            String str= Optional.ofNullable(dbInformPromise).orElseGet(Lists::newArrayList).stream().map(DbInformPromise::getServiceOid).collect(Collectors.joining(","));
            return str;
        }
        return null;
    }

    public InformPromise getInformByServiceOid(String serviceOid) {
        DbInformPromise dbInformPromise = this.dbInformPromiseMapper.selectByServiceOid(serviceOid);
        if(dbInformPromise!=null){
            InformPromise informPromise= new InformPromise();
            BeanUtils.copyProperties(dbInformPromise,informPromise);
            return informPromise;
        }
        return null;
    }

    /**
     * 根据事项主键批量删除信息
     */
    public void batchInformInfo(List<String> delServiceOids){
        if(delServiceOids!=null && delServiceOids.size()>0){
            for(String serviceOid:delServiceOids){
                DbInformPromise dbInformPromise = this.dbInformPromiseMapper.selectByServiceOid(serviceOid);
                if(dbInformPromise!=null){
                    dbInformPromise.setDelFlag(1);
                    dbInformPromiseMapper.updateByPrimaryKey(dbInformPromise);
                }
            }

        }
    }

}

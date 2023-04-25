package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceOptionValMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.*;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 选项值表 实现类
 * @date 2020/11/3 11:37
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceOptionValManager {

    @Resource
    private DbSxServiceOptionValMapper dbSxServiceOptionValMapper;

    public SxServiceOptionVal getSxServiceOptionValByOid(String oid){
        DbSxServiceOptionVal dbSxServiceOptionVal = dbSxServiceOptionValMapper.selectByOid(oid);
        SxServiceOptionVal sxServiceOptionVal = null ;
        if (null == dbSxServiceOptionVal) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询选项值表单为空！");
        }else{
            sxServiceOptionVal = new SxServiceOptionVal();
            BeanUtils.copyProperties(dbSxServiceOptionVal, sxServiceOptionVal);
        }
        return sxServiceOptionVal;

    }

    public List<SxServiceOptionVal> getSxServiceOptionValsByTitleOid(String titleOid) {
        DbSxServiceOptionValExample dbSxServiceOptionValExample = new DbSxServiceOptionValExample();
        DbSxServiceOptionValExample.Criteria criteria = dbSxServiceOptionValExample.createCriteria();
        if(StrUtil.isNotEmpty(titleOid)){
            criteria.andTitleOidEqualTo(titleOid);
        }
        criteria.andDeleteStatusEqualTo("0");
        dbSxServiceOptionValExample.setOrderByClause(" sort ");
        List<DbSxServiceOptionVal> dbSxServiceOptionVal = dbSxServiceOptionValMapper.selectByExample(dbSxServiceOptionValExample);
        List<SxServiceOptionVal> sxServiceOptionVals = dbSxServiceOptionVal.stream().map(sxServiceOptionVal -> {
            SxServiceOptionVal sOptionVal = new SxServiceOptionVal();
            BeanUtils.copyProperties(sxServiceOptionVal,sOptionVal);
            return sOptionVal;
        }).collect(Collectors.toList());
        return sxServiceOptionVals;
    }

    /**
     * 保存或修改选项值信息
     * @param sxServiceOptionVal
     */
    public void saveOrUpdate(SxServiceOptionVal sxServiceOptionVal){
        if(null==sxServiceOptionVal.getOid()){
            DbSxServiceOptionVal record = new  DbSxServiceOptionVal();
            BeanUtils.copyProperties(sxServiceOptionVal,record);
            record.setOid(UUID.randomUUID().toString().replaceAll("-",""));
            record.setDeleteStatus((short)0);
            record.setCreateDate(new Date());
            dbSxServiceOptionValMapper.insertSelective(record);
        }else{
            DbSxServiceOptionVal record = new  DbSxServiceOptionVal();
            BeanUtils.copyProperties(sxServiceOptionVal,record);
            record.setModifyDate(new Date());
            dbSxServiceOptionValMapper.updateByPrimaryKeySelective(record);
        }
    }

}

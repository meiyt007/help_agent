package com.zfsoft.service.manager.sxSys;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxSys.DbSxSysDictMapper;
import com.zfsoft.service.dbaccess.data.sxSys.DbSxSysDict;
import com.zfsoft.service.dbaccess.data.sxSys.DbSxSysDictExample;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.sxSys.data.SxSysDict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SxSysDictServiceImpl
 * @Description: 数据字典接口实现类
 * @Author wangxl
 * @Date 2020/10/25
 **/
@Service
@Slf4j
//@CacheConfig(cacheNames = "sxpt:sysDict")
public class SxSysDictManager {

        @Resource
        private DbSxSysDictMapper dbSxSysDictMapper;

        //@Cacheable(key = "'getSxSysDictByOid:'+#oid", unless = "#result == null")
        public SxSysDict getSxSysDictByOid(String oid) {
            DbSxSysDict dbSxSysdict = dbSxSysDictMapper.getDbSxSysDictByOid(oid);
            if(dbSxSysdict == null){
                throw new ResultInfoException("数据字典信息为空！");
            }
            SxSysDict sxSysDict = new SxSysDict();
            BeanUtils.copyProperties(dbSxSysdict,sxSysDict);
            return sxSysDict;
        }

        public SxSysDict getSxSysDictByCode(String code){
            DbSxSysDict dbSxSysdict = dbSxSysDictMapper.getDbSxSysDictByCode(code);
            if(dbSxSysdict == null){
                throw new ResultInfoException("数据字典信息为空！");
            }
            SxSysDict sxSysDict = new SxSysDict();
            BeanUtils.copyProperties(dbSxSysdict,sxSysDict);
            return sxSysDict;
        }

        public List<SxSysDict> querySxSysDictListByParentCode(String parentCode){
            DbSxSysDictExample dbSxSysDictExample = new DbSxSysDictExample();
            DbSxSysDictExample.Criteria criteria = dbSxSysDictExample.createCriteria();
            //行使层级添加判断启用状态
            Short able= 1;
            criteria.andIsAbleEqualTo(able);
            if(StrUtil.isNotEmpty(parentCode)){
                SxSysDict sxSysDict = this.getSxSysDictByCode(parentCode);
                if(null != sxSysDict){
                    criteria.andParentOidEqualTo(sxSysDict.getOid());
                }
            }
            criteria.andIsDeleteEqualTo(SxptBaseStaticParameter.ZERO);
            dbSxSysDictExample.setOrderByClause(" sort");
            List<DbSxSysDict> dbSxSysDicts = dbSxSysDictMapper.selectByExample(dbSxSysDictExample);
            List<SxSysDict> sxSysDictList = dbSxSysDicts.stream().map(sxSysDict -> {
                SxSysDict dict = new SxSysDict();
                BeanUtils.copyProperties(sxSysDict,dict);
                return dict;
            }).collect(Collectors.toList());
            return sxSysDictList;
        }

    /*public List<SxSysDict> querySxSysDictListByDictOids(List<String> dictOids) {
        DbSxSysDictExample dbSxSysDictExample = new DbSxSysDictExample();
        DbSxSysDictExample.Criteria criteria = dbSxSysDictExample.createCriteria();
        if(null!= dictOids){
            criteria.andOidIn(dictOids);
        }
        List<DbSxSysDict> dbSysDicts = dbSxSysDictMapper.selectByExample(dbSxSysDictExample);
        List<SxSysDict> sxSysDictList = dbSysDicts.stream().map(sxSysDict -> {
            SxSysDict dict = new SxSysDict();
            BeanUtils.copyProperties(sxSysDict,dict);
            return dict;
        }).collect(Collectors.toList());
        return sxSysDictList;
    }*/

    public String getSxSysDictNameByDictOids(String dictOids){
        if (StrUtil.isEmpty(dictOids)) {
            return "";
        }
        String[] dictOidArr = dictOids.split(",");
        if (dictOidArr == null || dictOidArr.length < 1) {
            return "";
        }
        DbSxSysDictExample dbSxSysDictExample = new DbSxSysDictExample();
        DbSxSysDictExample.Criteria criteria = dbSxSysDictExample.createCriteria();
        if(StrUtil.isNotEmpty(dictOids)){
            if(dictOidArr.length>0){
                criteria.andOidIn(Arrays.asList(dictOidArr));
            }else{
                criteria.andOidEqualTo(dictOids);
            }
        }
        List<DbSxSysDict> dbSysDicts = dbSxSysDictMapper.selectByExample(dbSxSysDictExample);
        if(null != dbSysDicts && dbSysDicts.size()>0){
            StringBuffer dictName = new StringBuffer("");
            for(DbSxSysDict dbSxSysDict : dbSysDicts){
                dictName.append(dbSxSysDict.getName()+"  ");
            }
            /*if(dictName.length()>0){
                return dictName.substring(0,dictName.length()-1);
            }*/
            return dictName.toString();
        }
        return null;
    }
}

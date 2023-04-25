package com.zfsoft.superwindow.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.superwindow.data.yxpz.SysSiteDistrictRelation;
import com.zfsoft.superwindow.dbaccess.dao.DbSysSiteDistrictRelationMapper;
import com.zfsoft.superwindow.dbaccess.data.DbSysSiteDistrictRelation;
import com.zfsoft.superwindow.dbaccess.data.DbSysSiteDistrictRelationExample;
import com.zfsoft.superwindow.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2020-10-26 10:35:41
 * @description:    辖区区划管理
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysSiteDistrictRelationManager {
    private final DbSysSiteDistrictRelationMapper dbSysSiteDistrictRelationMapper;

  public List<SysSiteDistrictRelation> querySiteDistrictRelByDistOid(Map<String, Object> paramRelMap){
     List<DbSysSiteDistrictRelation> list= dbSysSiteDistrictRelationMapper.querySiteDistrictRelByDistOid(paramRelMap);
     if(list!=null){
         return BeanUtils.copyListProperties(list,SysSiteDistrictRelation::new);
     }
     return null;
    }

    public List<SysSiteDistrictRelation> siteDistrictRelListByDistOid(String districtOid){
        DbSysSiteDistrictRelationExample example=new DbSysSiteDistrictRelationExample();
        DbSysSiteDistrictRelationExample.Criteria criteria= example.createCriteria();
        if(StrUtil.isNotEmpty(districtOid)){
            criteria.andDistrictOidEqualTo(districtOid);
        }
        criteria.andIsDeleteEqualTo(0);
        List<DbSysSiteDistrictRelation> list= dbSysSiteDistrictRelationMapper.selectByExample(example);
        if(list!=null){
            return BeanUtils.copyListProperties(list,SysSiteDistrictRelation::new);
        }
        return null;
    }

}

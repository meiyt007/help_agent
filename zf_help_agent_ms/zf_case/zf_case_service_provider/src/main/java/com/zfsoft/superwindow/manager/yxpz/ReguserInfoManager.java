package com.zfsoft.superwindow.manager.yxpz;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.ReguserInfo;
import com.zfsoft.superwindow.dbaccess.dao.DbReguserInfoMapper;
import com.zfsoft.superwindow.dbaccess.data.DbReguserInfo;
import com.zfsoft.superwindow.dbaccess.data.DbReguserInfoExample;
import com.zfsoft.superwindow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class ReguserInfoManager {

    @Resource
    private DbReguserInfoMapper dbReguserInfoMapper;

    public ReguserInfo saveOrUpdate(ReguserInfo reguserInfo){
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(reguserInfo!=null){
            if(StringUtil.isNotEmpty(currentLoginUser.getUserOid())){
                //查询信息
                DbReguserInfoExample example=new DbReguserInfoExample();
                DbReguserInfoExample.Criteria criter= example.createCriteria();
                criter.andUserOidEqualTo(currentLoginUser.getUserOid());
               List<DbReguserInfo> list= dbReguserInfoMapper.selectByExample(example);
               if(list!=null && list.size()>0){
                   DbReguserInfo info=list.get(0);
                   info.setBussVenueDistrictOid(reguserInfo.getBussVenueDistrictOid());
                   info.setPostCode(reguserInfo.getPostCode());
                   info.setSpecificLocation(reguserInfo.getSpecificLocation());
                   dbReguserInfoMapper.updateByPrimaryKey(info);
               }else{
                   reguserInfo.setUserOid(currentLoginUser.getUserOid());
                   DbReguserInfo info=new DbReguserInfo();
                   BeanUtils.copyProperties(reguserInfo,info);
                   dbReguserInfoMapper.insert(info);
               }
            }
        }
        return reguserInfo;
    }

    public ReguserInfo getRegUserInfoByUserOid(){
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            if(StringUtil.isNotEmpty(currentLoginUser.getUserOid())){
                //查询信息
                DbReguserInfoExample example=new DbReguserInfoExample();
                DbReguserInfoExample.Criteria criter= example.createCriteria();
                criter.andUserOidEqualTo(currentLoginUser.getUserOid());
                List<DbReguserInfo> list= dbReguserInfoMapper.selectByExample(example);
                if(list!=null && list.size()>0){
                    ReguserInfo info=new ReguserInfo();
                    BeanUtils.copyProperties(list.get(0),info);
                    return info;
                }
            }
        return null;
    }

}

package com.zfsoft.superwindow.manager.yxpz;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.WitnessComparison;
import com.zfsoft.superwindow.dbaccess.dao.DbWitnessComparisonMapper;
import com.zfsoft.superwindow.dbaccess.data.DbWitnessComparison;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.util.Base64FileUitl;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 人证比对
 */
@Service
@Slf4j
public class WitnessComparisonManager {

    @Resource
    private DbWitnessComparisonMapper dbWitnessComparisonMapper;

    @Resource
    private SysAttaFeginService sysAttaFeginService;

    public WitnessComparison saveOrUpdate(WitnessComparison witnessComparison){
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(witnessComparison!=null){
            if(StringUtil.isEmpty(witnessComparison.getCompareOid())){
                witnessComparison.setCompareOid(UUIDUtil.randomUUID());
            }
            witnessComparison.setCreateDate(new Date());
            witnessComparison.setCreateUser(currentLoginUser.getUserOid());
            DbWitnessComparison dbWitnessComparison=new DbWitnessComparison();
            BeanUtils.copyProperties(witnessComparison,dbWitnessComparison);
            dbWitnessComparisonMapper.insert(dbWitnessComparison);
            return witnessComparison;
        }
        return null;

    }

    public WitnessComparison queryWitnessComparisonByCaseOid(String caseOid) {
        List<DbWitnessComparison>  lists = this.dbWitnessComparisonMapper.queryWitnessComparisonByCaseOid(caseOid);
        if(lists.size()>0){
            DbWitnessComparison db = lists.get(0);
            WitnessComparison witnessComparison = new WitnessComparison();
            BeanUtils.copyProperties(db,witnessComparison);
            return  witnessComparison;
        }
        return null;
    }

    public Map<String,String> queryBase64ImgWitnessComparisonByCaseOid(String caseOid) {
        Map<String,String> map=new HashMap<>();
        WitnessComparison witnessComparison = this.queryWitnessComparisonByCaseOid(caseOid);
        if(witnessComparison !=null){
            String imgAttaOid = witnessComparison.getImgAttaOid();
            if(!StringUtils.isEmpty(imgAttaOid)){
                ApiResultSet<SysAtta> resultSet = sysAttaFeginService.getSysAttaByAttaOid(imgAttaOid);
                SysAtta sysAtta = resultSet.getData();
                if(sysAtta !=null){
                  String image2Base64 = Base64FileUitl.Image2Base64(sysAtta.getFastdfsNginxUrl());
                    map.put("image2Base64",image2Base64);
                }
            }
            if(!StringUtils.isEmpty(witnessComparison.getIdcardAttaOid())){
                ApiResultSet<SysAtta> resultSet = sysAttaFeginService.getSysAttaByAttaOid(witnessComparison.getIdcardAttaOid());
                SysAtta sysAtta = resultSet.getData();
                if(sysAtta !=null){
                    String idCard2Base64 = Base64FileUitl.Image2Base64(sysAtta.getFastdfsNginxUrl());
                    map.put("idCard2Base64",idCard2Base64);
                }
            }
        }
        return  map;
    }
}

package com.zfsoft.superwindow.manager.pbpj;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.superwindow.data.yxpz.EvaluationStandard;
import com.zfsoft.superwindow.dbaccess.dao.DbEvaluationStandardMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEvaluationStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wangwg
 * @description 一件事平板评价评价选项实现类
 * @date 2021-02-25
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class EvaluationStandardManager {

    @Resource
    private DbEvaluationStandardMapper dbEvaluationStandardMapper;


    public PageResult<EvaluationStandard> listEvaluationStandardPage(String standardName, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbEvaluationStandard dbEvaluationStandard =new DbEvaluationStandard();
        dbEvaluationStandard.setStandardName(standardName);
        Page<DbEvaluationStandard> dbComboEvaluationStandardPage = (Page<DbEvaluationStandard>) dbEvaluationStandardMapper.queryAll(dbEvaluationStandard);
        PageResult<EvaluationStandard> pageResult = new PageResult<>(dbComboEvaluationStandardPage.getPageNum(),
                dbComboEvaluationStandardPage.getPageSize(), dbComboEvaluationStandardPage.getTotal());
        List<EvaluationStandard> comboEvaluationStandardList = dbComboEvaluationStandardPage.stream().map(dbComboEvaluationStandard1 -> {
            EvaluationStandard comboEvaluationStandard = new EvaluationStandard();
            BeanUtils.copyProperties(dbComboEvaluationStandard1,comboEvaluationStandard);
            return comboEvaluationStandard;
        }).collect(Collectors.toList());
        pageResult.setData(comboEvaluationStandardList);
        return pageResult;
    }

    public HashMap<String, String> saveEvaluationStandard(EvaluationStandard evaluationStandard, CurrentLoginUser loginUser) {
        HashMap<String, String> map =new HashMap<String, String>();
        evaluationStandard.setCreateBy(loginUser.getUserOid());
        evaluationStandard.setDelFlag(1);
        if(evaluationStandard.getId() == null){
            evaluationStandard.setId(null);
            evaluationStandard.setStandardOid(UUID.randomUUID().toString().replaceAll("-",""));
            evaluationStandard.setCreateDate(new Date());
        }
        DbEvaluationStandard dbEvaluationStandard =new DbEvaluationStandard();
        BeanUtils.copyProperties(evaluationStandard,dbEvaluationStandard);
        int index = 0;
        if(evaluationStandard.getId() == null){
            index = dbEvaluationStandardMapper.insert(dbEvaluationStandard);
        }else{
            index = dbEvaluationStandardMapper.update(dbEvaluationStandard);
        }
        if(index > 0){
            map.put("standardOid",evaluationStandard.getStandardOid());
        }
        return map;
    }

    public void delEvaluationStandard(String standardOid) {
        DbEvaluationStandard dbEvaluationStandard = dbEvaluationStandardMapper.getEvaluationStandardByOid(standardOid);
        if(dbEvaluationStandard !=null){
            dbEvaluationStandard.setDelFlag(0);
            dbEvaluationStandardMapper.update(dbEvaluationStandard);
        }
    }
}

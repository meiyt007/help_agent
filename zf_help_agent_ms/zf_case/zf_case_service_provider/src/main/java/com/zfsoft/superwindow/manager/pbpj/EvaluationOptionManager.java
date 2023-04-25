package com.zfsoft.superwindow.manager.pbpj;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.superwindow.data.yxpz.EvaluationOption;
import com.zfsoft.superwindow.dbaccess.dao.DbEvaluationOptionMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEvaluationOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wangwg
 * @description 一件事平板评价评价选项现类
 * @date 2021-02-25
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class EvaluationOptionManager {

    @Resource
    private DbEvaluationOptionMapper dbEvaluationOptionMapper;

    public HashMap<String, String> saveEvaluationOption(List<EvaluationOption> evaluationOptions, CurrentLoginUser loginUser) {
        HashMap<String, String> map =new HashMap<String, String>();
        List<EvaluationOption> evaluationOptionList = new ArrayList<EvaluationOption>();
        if(evaluationOptions !=null){
            for (EvaluationOption evaluationOption: evaluationOptions) {
                evaluationOption.setCreateBy(loginUser.getUserOid());
                evaluationOption.setDelFlag(1);
                if(evaluationOption.getId() == null){
                    evaluationOption.setId(null);
                    evaluationOption.setOptionOid(UUID.randomUUID().toString().replaceAll("-",""));
                    evaluationOption.setCreateDate(new Date());
                }
                evaluationOptionList.add(evaluationOption);
            }
        }
        List<DbEvaluationOption> dbEvaluationOptionList = evaluationOptionList.stream().map(evaluationOption -> {
            DbEvaluationOption dbEvaluationOption =new DbEvaluationOption();
            BeanUtils.copyProperties(evaluationOption,dbEvaluationOption);
            return dbEvaluationOption;
        }).collect(Collectors.toList());
        AtomicInteger index = new AtomicInteger();
        dbEvaluationOptionList.stream().forEach(dbEvaluationOption -> {
            int num = 0;
            if(dbEvaluationOption.getId() ==null){
                 num = dbEvaluationOptionMapper.insert(dbEvaluationOption);
            }else{
                 num = dbEvaluationOptionMapper.update(dbEvaluationOption);
            }
            index.addAndGet(num);
        });
        if(index.get() > 0){
            map.put("success","ok");
        }
        return map;
    }

    public List<EvaluationOption> queryEvaluationOptionList(String standardOid) {
        List<DbEvaluationOption> dbEvaluationOptions = dbEvaluationOptionMapper.queryEvaluationOptionListByStandardOid(standardOid);
        List<EvaluationOption> evaluationOptionList = dbEvaluationOptions.stream().map(dbEvaluationOption -> {
            EvaluationOption evaluationOption =new EvaluationOption();
            BeanUtils.copyProperties(dbEvaluationOption,evaluationOption);
            return evaluationOption;
        }).collect(Collectors.toList());
        return evaluationOptionList;
    }

    public void delEvaluationOption(String standardOid) {
        List<DbEvaluationOption> dbComboEvaluationOptions = dbEvaluationOptionMapper.queryEvaluationOptionListByStandardOid(standardOid);
        if(dbComboEvaluationOptions !=null){
            dbComboEvaluationOptions.stream().forEach(dbComboEvaluationOption -> {
                dbComboEvaluationOption.setDelFlag(0);
                dbEvaluationOptionMapper.update(dbComboEvaluationOption);
            });
        }
    }

    public void delEvaluationOption(Long id) {
        DbEvaluationOption evaluationOption = dbEvaluationOptionMapper.queryById(id);
        evaluationOption.setDelFlag(0);
        dbEvaluationOptionMapper.update(evaluationOption);
    }
}

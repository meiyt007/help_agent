package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlCaseSituationTitleValRelation;
import com.zfsoft.cases.dbaccess.dao.QlCaseTitleValueRelationMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseSituationTitleValRelation;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (TQlCaseTitleValueRelation)表服务实现类W
 *
 * @author makejava
 * @since 2020-11-30 10:23:02
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:qlcaserelation")
public class QlCaseTitleValueRelationManager {

    @Resource
    private QlCaseTitleValueRelationMapper qlCaseTitleValueRelationMapper;


    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public List<Map<String, String>> saveQlCaseTitleValueRelations(List<QlCaseSituationTitleValRelation> relations) {
        List<Map<String, String>> list  = new ArrayList<Map<String, String>>();
        if(relations.size() > 0){
            for (QlCaseSituationTitleValRelation relation : relations) {
                if(!"0".equals(relation.getCaseOid())) {
                    if (null == relation.getId()) {
                        relation.setId(null);
                    }
                    if(StringUtil.isEmpty(relation.getRelationOid())){
                        relation.setRelationOid(UUID.randomUUID().toString().replaceAll("-",""));
                    }
                    DbQlCaseSituationTitleValRelation dbTitleValRelation=new DbQlCaseSituationTitleValRelation();
                    BeanUtils.copyProperties(relation, dbTitleValRelation);
                    int index = 0;
                    if (null == relation.getId()) {
                        index = qlCaseTitleValueRelationMapper.insert(dbTitleValRelation);
                    } else {
                        index = qlCaseTitleValueRelationMapper.update(dbTitleValRelation);
                    }
                    //保存成功
                    if(index > 0){
                        Map<String, String> map=new HashMap<String, String>();
                        map.put("relationOid",relation.getRelationOid());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    //@Cacheable(key = "'queryTitleValueRelationByCaseOid:caseOid=' + #caseOid ", unless = "#result == null")
    public List<QlCaseSituationTitleValRelation> queryTitleValueRelationByCaseOid(String caseOid) {
        if (StringUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件业务主键为空！");
        }
        List<DbQlCaseSituationTitleValRelation>  titleValRelations =qlCaseTitleValueRelationMapper.selectQlCaseTitleValRelationList(caseOid);
        List<QlCaseSituationTitleValRelation> relationList = titleValRelations.stream().map(dbTitleValRelation -> {
            QlCaseSituationTitleValRelation titleValRelation = new QlCaseSituationTitleValRelation();
            BeanUtils.copyProperties(dbTitleValRelation,titleValRelation);
            return titleValRelation;
        }).collect(Collectors.toList());
       return relationList;
    }

    public int deleteQlCaseTitleValueRelation(Long id) {
        int index = qlCaseTitleValueRelationMapper.deleteById(id);
        if(index==0){
            throw new ResultInfoException("参数删除失败，请稍后再试！");
        }
        return index;
    }

}

package com.zfsoft.cases.dbaccess.dao;


import com.zfsoft.cases.dbaccess.data.DbQlCaseSituationTitleValRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (DbQlCaseSituationTitleValRelation)表服务接口
 *
 * @author wangwg
 * @since 2020-11-30
 */
@Mapper
public interface QlCaseTitleValueRelationMapper {


    /**
     * 新增数据
     *
     * @param DbQlCaseSituationTitleValRelation 实例对象
     * @return 实例对象
     */
    int insert(DbQlCaseSituationTitleValRelation DbQlCaseSituationTitleValRelation);

    /**
     * 修改数据
     *
     * @param DbQlCaseSituationTitleValRelation 实例对象
     * @return 实例对象
     */
    int update(DbQlCaseSituationTitleValRelation DbQlCaseSituationTitleValRelation);

    /**
     * 根据办件查询关系数据查询
     * @param caseOid
     * @return
     */
    List<DbQlCaseSituationTitleValRelation> selectQlCaseTitleValRelationList(String caseOid);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteById(Long id);
}
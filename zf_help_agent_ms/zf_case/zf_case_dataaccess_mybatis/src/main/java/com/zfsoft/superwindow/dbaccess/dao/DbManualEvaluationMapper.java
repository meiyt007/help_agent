package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbManualEvaluation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 手动评价表(TManualEvaluation)表数据库访问层
 *
 * @author makejava
 * @since 2021-08-12 15:34:53
 */
@Mapper
public interface DbManualEvaluationMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbManualEvaluation queryById(String id);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param dbManualEvaluation 实例对象
     * @return 对象列表
     */
    List<DbManualEvaluation> queryAll(DbManualEvaluation dbManualEvaluation);

    /**
     * 新增数据
     *
     * @param dbManualEvaluation 实例对象
     * @return 影响行数
     */
    int insert(DbManualEvaluation dbManualEvaluation);

    /**
     * 修改数据
     *
     * @param dbManualEvaluation 实例对象
     * @return 影响行数
     */
    int update(DbManualEvaluation dbManualEvaluation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    List<DbManualEvaluation> getSatisfaction(String createUserOid);

    List<DbManualEvaluation> queryByVirtualBusinessNum(String virtualBusinessNum);
}


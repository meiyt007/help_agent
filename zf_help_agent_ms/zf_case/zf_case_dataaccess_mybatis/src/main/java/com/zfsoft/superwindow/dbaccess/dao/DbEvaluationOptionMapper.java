package com.zfsoft.superwindow.dbaccess.dao;


import com.zfsoft.superwindow.dbaccess.data.DbEvaluationOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DbComboEvaluationOption)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-25 10:58:29
 */
@Mapper
public interface DbEvaluationOptionMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbEvaluationOption queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbEvaluationOption> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param DbComboEvaluationOption 实例对象
     * @return 对象列表
     */
    List<DbEvaluationOption> queryAll(DbEvaluationOption DbComboEvaluationOption);

    /**
     * 新增数据
     *
     * @param DbComboEvaluationOption 实例对象
     * @return 影响行数
     */
    int insert(DbEvaluationOption DbComboEvaluationOption);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbComboEvaluationOption> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbEvaluationOption> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbComboEvaluationOption> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbEvaluationOption> entities);

    /**
     * 修改数据
     *
     * @param DbComboEvaluationOption 实例对象
     * @return 影响行数
     */
    int update(DbEvaluationOption DbComboEvaluationOption);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 查询列表
     * @param standardOid
     * @return
     */
    List<DbEvaluationOption> queryEvaluationOptionListByStandardOid(String standardOid);

    DbEvaluationOption getEvaluationOptionByStandardOid(String standardOid);
}
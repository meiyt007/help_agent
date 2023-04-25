package com.zfsoft.cases.dbaccess.dao;

import com.zfsoft.cases.dbaccess.data.DbQlCaseExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (QlCaseExtMapper)表数据库访问层
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Mapper
public interface QlCaseExtMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbQlCaseExt queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbQlCaseExt> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param qlCaseExt 实例对象
     * @return 对象列表
     */
    List<DbQlCaseExt> queryAll(DbQlCaseExt qlCaseExt);

    /**
     * 新增数据
     *
     * @param qlCaseExt 实例对象
     * @return 影响行数
     */
    int insert(DbQlCaseExt qlCaseExt);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TQlCaseExt> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbQlCaseExt> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TQlCaseExt> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbQlCaseExt> entities);

    /**
     * 修改数据
     *
     * @param qlCaseExt 实例对象
     * @return qlCaseExt
     */
    int update(DbQlCaseExt qlCaseExt);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 通过办件查询办件扩展信息
     *
     * @param caseOid
     * @author wangwg
     * @date 2020/10/26

     * @return com.zfsoft.dbaccess.data.DbQlCaseExt
     **/
    DbQlCaseExt queryQlCaseExtByCaseOid(String caseOid);
}
package com.zfsoft.cases.dbaccess.dao;

import com.zfsoft.cases.dbaccess.data.DbQlCaseLinkResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (QlCaseLinkResultMapper)表数据库访问层
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Mapper
public interface QlCaseLinkResultMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbQlCaseLinkResult queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbQlCaseLinkResult> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tQlCaseLinkResult 实例对象
     * @return 对象列表
     */
    List<DbQlCaseLinkResult> queryAll(DbQlCaseLinkResult tQlCaseLinkResult);

    /**
     * 新增数据
     *
     * @param tQlCaseLinkResult 实例对象
     * @return 影响行数
     */
    int insert(DbQlCaseLinkResult tQlCaseLinkResult);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TQlCaseLinkResult> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbQlCaseLinkResult> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TQlCaseLinkResult> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbQlCaseLinkResult> entities);

    /**
     * 修改数据
     *
     * @param tQlCaseLinkResult 实例对象
     * @return 影响行数
     */
    int update(DbQlCaseLinkResult tQlCaseLinkResult);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 办件业务编码获取办件审批环节信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return java.util.List<com.zfsoft.dbaccess.data.DbQlCaseLinkResult>
     **/
    List<DbQlCaseLinkResult> queryQlCaseLinkResultListByCaseOid(String caseOid);

    /**
     * 查询进入受理环节的办件
     * @param caseOid
     * @return
     */
    DbQlCaseLinkResult querySlQlCaseLinkResultByCaseOid(String caseOid);

    /**
     * 通过办件业务主键获取办件办结环节审核信息
     *
     * @author wangwg
     * @date 2021/03/31
     * @param caseOid
     **/
    DbQlCaseLinkResult queryBjQlCaseLinkResultByCaseOid(String caseOid);
}
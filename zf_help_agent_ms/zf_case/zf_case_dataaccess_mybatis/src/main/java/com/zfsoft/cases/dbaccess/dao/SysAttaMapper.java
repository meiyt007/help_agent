package com.zfsoft.cases.dbaccess.dao;

import com.zfsoft.cases.dbaccess.data.DbQlSysAtta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysAttaMapper)表数据库访问层
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Mapper
public interface SysAttaMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbQlSysAtta queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbQlSysAtta> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tSysAtta 实例对象
     * @return 对象列表
     */
    List<DbQlSysAtta> queryAll(DbQlSysAtta tSysAtta);

    /**
     * 新增数据
     *
     * @param tSysAtta 实例对象
     * @return 影响行数
     */
    int insert(DbQlSysAtta tSysAtta);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TSysAtta> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbQlSysAtta> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TSysAtta> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbQlSysAtta> entities);

    /**
     * 修改数据
     *
     * @param tSysAtta 实例对象
     * @return 影响行数
     */
    int update(DbQlSysAtta tSysAtta);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 附件业务主键查看附件信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param attaOid
     * @return com.zfsoft.dbaccess.data.DbQlSysAtta
     **/
    DbQlSysAtta querySysAttaByOid(String attaOid);
}
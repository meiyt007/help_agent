package com.zfsoft.cases.dbaccess.dao;

import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterialAtta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (QlCaseMaterialAttaMapper)表数据库访问层
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Mapper
public interface QlCaseMaterialAttaMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbQlCaseMaterialAtta queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbQlCaseMaterialAtta> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param qlCaseMaterialAtta 实例对象
     * @return 对象列表
     */
    List<DbQlCaseMaterialAtta> queryAll(DbQlCaseMaterialAtta qlCaseMaterialAtta);

    /**
     * 新增数据
     *
     * @param qlCaseMaterialAtta 实例对象
     * @return 影响行数
     */
    int insert(DbQlCaseMaterialAtta qlCaseMaterialAtta);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<qlCaseMaterialAtta> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbQlCaseMaterialAtta> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<qlCaseMaterialAtta> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbQlCaseMaterialAtta> entities);

    /**
     * 修改数据
     *
     * @param qlCaseMaterialAtta 实例对象
     * @return 影响行数
     */
    int update(DbQlCaseMaterialAtta qlCaseMaterialAtta);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据办件材料业务主键删除办件附件
     * @param caseMaterialOid
     * @return
     */
    int deleteByCaseMaterialOid(String caseMaterialOid);




    /**
     * 办件材料业务主键查询附件材料信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseMaterialOid
     * @return java.util.List<com.zfsoft.dbaccess.data.DbQlCaseMaterialAtta>
     **/
    List<DbQlCaseMaterialAtta> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid);

    /**
     * 根据业务主键查询信息
     * @param materialAttaOid
     * @returnQlCaseMaterialAttaMap
     */
    DbQlCaseMaterialAtta queryQlCaseMaterialByOid(String materialAttaOid);

    /**
     * 根据id查询一条
     * @param attaOid
     * @param caseMaterialOid
     * @return
     */
    DbQlCaseMaterialAtta getSelectoneByCaseMaterialOid(String attaOid, String caseMaterialOid);

    /**
     * 根据目录材料查询一条数据
     * @param
     * @param caseMaterialOid
     * @return
     */
    DbQlCaseMaterialAtta queryQlCaseMaterialAttaByCatalogOid(String caseMaterialOid, String materialCatalogOid);

    /**
     * 根据材料id和附件id查询附件关联表
     * @param caseMaterialOid
     * @param attaOid
     * @return
     */
    DbQlCaseMaterialAtta queryQlCaseMaterialAttaByCatalogOidAndAttaOid(String caseMaterialOid, String attaOid);


    /**
     * 通过业务主键删除
     * @param materialAttaOid
     * @return
     */
    int deleteByOid(String materialAttaOid);

    /**
     * 根据精细化材料oid查询办件关联列表
     * @param refinedMaterialOid
     * @return
     */
    List<DbQlCaseMaterialAtta> queryQlCaseMaterialAttaByRefinedMaterialOid(String refinedMaterialOid, String caseMaterialOid);
}
package com.zfsoft.cases.dbaccess.dao;

import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (QlCaseMaterialMapper)表数据库访问层
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Mapper
public interface QlCaseMaterialMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbQlCaseMaterial queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbQlCaseMaterial> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param qlCaseMaterial 实例对象
     * @return 对象列表
     */
    List<DbQlCaseMaterial> queryAll(DbQlCaseMaterial qlCaseMaterial);

    /**
     * 新增数据
     *
     * @param qlCaseMaterial 实例对象
     * @return 影响行数
     */
    int insert(DbQlCaseMaterial qlCaseMaterial);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<qlCaseMaterial> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbQlCaseMaterial> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<qlCaseMaterial> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbQlCaseMaterial> entities);

    /**
     * 修改数据
     *
     * @param qlCaseMaterial 实例对象
     * @return 影响行数
     */
    int update(DbQlCaseMaterial qlCaseMaterial);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 办件业务编码获取办件材料
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return java.util.List<com.zfsoft.dbaccess.data.DbQlCaseMaterial>
     **/
    List<DbQlCaseMaterial> queryQlCaseLinkResultListByCaseOid(String caseOid);

    /**
     * 根据办件主建查询容缺后补材料
     * dongxl
     * @param caseOid
     * @return
     */
    List<DbQlCaseMaterial> queryRqhbMaterialByCaseOid(String caseOid);

    /**
     * 根据办件业务主键和事项材料业务主键查询已保存的办件材料
     *
     * @author wangwg
     * @date 2020/11/19
     * @param caseOid
     * @param materialOid
     * @return
     */
    DbQlCaseMaterial queryQlCaseMaterialByOid(String caseOid, String materialOid);

    /**
     * 获取办件材料信息
     * dongxl
     * @param caseMaterialOid
     * @return
     */
    DbQlCaseMaterial queryMaterialByCaseMaterialOid(String caseMaterialOid);

    /**
     * 根据材料业务主键和办件业务主键返回列表
     * @param materialOid
     * @param caseOid
     * @return
     */
    List<DbQlCaseMaterial> queryMaterialListByMaterialOid(String materialOid, String caseOid);

    /**
     * 查询承诺告知材料
     * @param caseOid
     * @return
     */
    List<DbQlCaseMaterial> queryCngzMaterialByCaseOid(String caseOid);

    List<DbQlCaseMaterial> queryMaterialByCaseOidNotAttaFile(String caseOid);

    List<DbQlCaseMaterial> queryQlCaseMaterialByBillOid(String caseOid, String billOid);
}

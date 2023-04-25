package com.zfsoft.single.dbaccess.dao.ywbl;

import com.zfsoft.single.dbaccess.data.ywbl.DbSfCaseCharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 办件收费表表数据库访问层
 *
 * @author wangwg
 * @since 2021-06-10
 */
@Mapper
public interface DbSfCaseChargeMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param caseChargeOid 主键
     * @return 实例对象
     */
    DbSfCaseCharge queryById(String caseChargeOid);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbSfCaseCharge> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tSfCaseCharge 实例对象
     * @return 对象列表
     */
    List<DbSfCaseCharge> queryAll(DbSfCaseCharge tSfCaseCharge);

    /**
     * 新增数据
     *
     * @param tSfCaseCharge 实例对象
     * @return 影响行数
     */
    int insert(DbSfCaseCharge tSfCaseCharge);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbSfCaseCharge> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbSfCaseCharge> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbSfCaseCharge> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbSfCaseCharge> entities);

    /**
     * 修改数据
     *
     * @param tSfCaseCharge 实例对象
     * @return 影响行数
     */
    int update(DbSfCaseCharge tSfCaseCharge);

    /**
     * 通过主键删除数据
     *
     * @param caseChargeOid 主键
     * @return 影响行数
     */
    int deleteById(String caseChargeOid);

    /**
     * 分页查询列表
     * @param map
     * @return
     */
    List<DbSfCaseCharge> querySfCaseChargePageList(Map<String, String> map);
}


package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbAveragePrescription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 办理时间表(TAveragePrescription)表数据库访问层
 *
 * @author makejava
 * @since 2021-07-16 13:10:19
 */
@Mapper
public interface DbAveragePrescriptionMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbAveragePrescription queryById(Long id);

    /**
     * 通过工作人员ID查询数据
     *
     * @param userOid 工作人员ID
     * @return 对象列表
     */
    List<DbAveragePrescription> queryByUserOid(String userOid);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbAveragePrescription> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tAveragePrescription 实例对象
     * @return 对象列表
     */
    List<DbAveragePrescription> queryAll(DbAveragePrescription tAveragePrescription);

    /**
     * 新增数据
     *
     * @param tAveragePrescription 实例对象
     * @return 影响行数
     */
    int insert(DbAveragePrescription tAveragePrescription);

    /**
     * 修改数据
     *
     * @param tAveragePrescription 实例对象
     * @return 影响行数
     */
    int update(DbAveragePrescription tAveragePrescription);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}


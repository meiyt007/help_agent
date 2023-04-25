package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbSaveCallRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DbSaveCallRecord)表数据库访问层
 *
 * @author makejava
 * @since 2021-08-16 11:32:36
 */
@Mapper
public interface DbSaveCallRecordMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbSaveCallRecord queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbSaveCallRecord> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tSaveCallRecord 实例对象
     * @return 对象列表
     */
    List<DbSaveCallRecord> queryAll(DbSaveCallRecord tSaveCallRecord);

    /**
     * 新增数据
     *
     * @param tSaveCallRecord 实例对象
     * @return 影响行数
     */
    int insert(DbSaveCallRecord tSaveCallRecord);

    /**
     * 修改数据
     *
     * @param tSaveCallRecord 实例对象
     * @return 影响行数
     */
    int update(DbSaveCallRecord tSaveCallRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    /**
     *  通过oid查询单条数据
     * @param oid
     * @return
     */
    DbSaveCallRecord queryByOid(@Param("oid") String oid);

    /**
     * 查询当天的所有的叫号记录
     * @param time
     * @param userOid
     * @return
     */
    List<DbSaveCallRecord> selectCallNums(@Param("createTime") String createTime, @Param("createBy") String createBy);

}


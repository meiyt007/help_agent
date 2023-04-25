package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbCaseSignRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbCaseSignRecordMapper {

    /**
     * 通过实体作为筛选条件查询
     *
     * @param DbCaseSignRecord 实例对象
     * @return 对象列表
     */
    List<DbCaseSignRecord> queryAll(DbCaseSignRecord DbCaseSignRecord);

    /**
     * 新增数据
     *
     * @param DbCaseSignRecord 实例对象
     * @return 影响行数
     */
    int insert(DbCaseSignRecord DbCaseSignRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbCaseSignRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbCaseSignRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbCaseSignRecord> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbCaseSignRecord> entities);

    /**
     * 修改数据
     *
     * @param DbCaseSignRecord 实例对象
     * @return 影响行数
     */
    int update(DbCaseSignRecord DbCaseSignRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    DbCaseSignRecord getSignRecordByCaseOid(String caseOid);

    DbCaseSignRecord querySignImgPath(String caseOid);
}


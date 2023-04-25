package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbVirtualBusinessRecord;
import com.zfsoft.superwindow.dbaccess.data.DbVirtualBusinessRecordManual;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (TVirtualBusinessRecord)表数据库访问层
 *
 * @author makejava
 * @since 2021-08-12 15:53:20
 */
@Mapper
public interface DbVirtualBusinessRecordMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbVirtualBusinessRecord queryById(Long id);

    /**
     * 修改数据
     *
     * @param tVirtualBusinessRecord 实例对象
     * @return 影响行数
     */
    int update(DbVirtualBusinessRecord tVirtualBusinessRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    int insert(DbVirtualBusinessRecord record);

    List<DbVirtualBusinessRecordManual> queryAll(DbVirtualBusinessRecordManual dbVirtualBusinessRecord);

    DbVirtualBusinessRecord queryByOid(String oid);

}


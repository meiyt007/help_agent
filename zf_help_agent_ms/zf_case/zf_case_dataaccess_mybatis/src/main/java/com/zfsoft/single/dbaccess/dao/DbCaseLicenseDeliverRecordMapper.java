package com.zfsoft.single.dbaccess.dao;

import com.zfsoft.single.dbaccess.data.DbCaseLicenseDeliverRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbCaseLicenseDeliverRecordMapper {

    /**
     * 修改数据
     *
     * @param dbCaseLicenseDeliverRecord 实例对象
     * @return 影响行数
     */
    int update(DbCaseLicenseDeliverRecord dbCaseLicenseDeliverRecord);

    int insert(DbCaseLicenseDeliverRecord record);

    DbCaseLicenseDeliverRecord queryByOid(@Param("oid") String oid);

    DbCaseLicenseDeliverRecord queryByCaseOid(@Param("caseOid") String caseOid);
}

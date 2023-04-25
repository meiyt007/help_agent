package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbHandleServiceStar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbHandleServiceStarMapper {
    DbHandleServiceStar queryHandleServiceStarByCode(String caseNumber);

    DbHandleServiceStar queryHandleServiceStarByVirtualBusinessOid(@Param("virtualBusinessOid") String virtualBusinessOid);

    int update(DbHandleServiceStar serviceStar);

    int insert(DbHandleServiceStar dbHandleServiceStar);
}

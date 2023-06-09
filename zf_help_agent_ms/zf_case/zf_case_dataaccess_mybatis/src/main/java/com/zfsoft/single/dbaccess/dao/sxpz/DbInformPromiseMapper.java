package com.zfsoft.single.dbaccess.dao.sxpz;

import com.zfsoft.single.dbaccess.data.sxpz.DbInformPromise;
import com.zfsoft.single.dbaccess.data.sxpz.DbInformPromiseExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbInformPromiseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int countByExample(DbInformPromiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int deleteByExample(DbInformPromiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int insert(DbInformPromise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int insertSelective(DbInformPromise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    List<DbInformPromise> selectByExample(DbInformPromiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    DbInformPromise selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbInformPromise record, @Param("example") DbInformPromiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbInformPromise record, @Param("example") DbInformPromiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbInformPromise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inform_promise
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbInformPromise record);

    /**
     * 根据事项主键查询
     * @param serviceOid
     * @return
     */
    DbInformPromise selectByServiceOid(String serviceOid);
}
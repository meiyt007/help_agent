package com.zfsoft.microservice.workflow.dbaccess.dao;

import com.zfsoft.microservice.workflow.dbaccess.data.DbProAcceptExample;
import com.zfsoft.microservice.workflow.dbaccess.data.DbProAcceptExampleExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DbProAcceptExampleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int countByExample(DbProAcceptExampleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int deleteByExample(DbProAcceptExampleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int insert(DbProAcceptExample record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int insertSelective(DbProAcceptExample record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    List<DbProAcceptExample> selectByExample(DbProAcceptExampleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    DbProAcceptExample selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbProAcceptExample record, @Param("example") DbProAcceptExampleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbProAcceptExample record, @Param("example") DbProAcceptExampleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbProAcceptExample record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_accept_example
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbProAcceptExample record);
}

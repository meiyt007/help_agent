package com.zfsoft.single.dbaccess.dao;

import com.zfsoft.single.dbaccess.data.DbClassifyRec;
import com.zfsoft.single.dbaccess.data.DbClassifyRecExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbClassifyRecMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int countByExample(DbClassifyRecExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int deleteByExample(DbClassifyRecExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int insert(DbClassifyRec record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int insertSelective(DbClassifyRec record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    List<DbClassifyRec> selectByExample(DbClassifyRecExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    DbClassifyRec selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbClassifyRec record, @Param("example") DbClassifyRecExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbClassifyRec record, @Param("example") DbClassifyRecExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbClassifyRec record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_classify_rec
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbClassifyRec record);


    /**
     * 根据oid查询
     * @param oid
     * @return
     */
    DbClassifyRec queryDbClassifyRecByOid(String oid);

    /**
     *
     * @param paramMap
     * @return
     */
    DbClassifyRec selectDbClassifyRec(Map<String, Object> paramMap);
}
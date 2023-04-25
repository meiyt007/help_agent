package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaMassesNucleic;
import com.zfsoft.ha.dbaccess.data.example.DbHaMassesNucleicExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DbHaMassesNucleicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    long countByExample(DbHaMassesNucleicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaMassesNucleicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    int insert(DbHaMassesNucleic record);

    /**
     * 修改数据
     *
     * @param dbHaMassesNucleic 实例对象
     * @return 影响行数
     */
    int update(DbHaMassesNucleic dbHaMassesNucleic);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    int insertSelective(DbHaMassesNucleic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    List<DbHaMassesNucleic> selectByExample(DbHaMassesNucleicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaMassesNucleic record, @Param("example") DbHaMassesNucleicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_masses_nucleic
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaMassesNucleic record, @Param("example") DbHaMassesNucleicExample example);

    /**
     * 判断是否有该核酸检测编码的记录
     * @param nucleicCode
     * @return
     * @throws Exception
     */
    @Select({"SELECT count(*) FROM t_ha_masses_nucleic WHERE NUCLEIC_CODE = #{nucleicCode} LIMIT 1"})
    int getIsHaMassesNucleicByNucleicCode(String nucleicCode )throws Exception;
}
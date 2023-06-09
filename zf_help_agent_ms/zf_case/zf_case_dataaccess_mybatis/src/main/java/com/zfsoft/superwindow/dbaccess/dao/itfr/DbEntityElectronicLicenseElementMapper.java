package com.zfsoft.superwindow.dbaccess.dao.itfr;

import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicenseElement;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicenseElementExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbEntityElectronicLicenseElementMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int countByExample(DbEntityElectronicLicenseElementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int deleteByExample(DbEntityElectronicLicenseElementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int insert(DbEntityElectronicLicenseElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int insertSelective(DbEntityElectronicLicenseElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    List<DbEntityElectronicLicenseElement> selectByExample(DbEntityElectronicLicenseElementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    DbEntityElectronicLicenseElement selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbEntityElectronicLicenseElement record, @Param("example") DbEntityElectronicLicenseElementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbEntityElectronicLicenseElement record, @Param("example") DbEntityElectronicLicenseElementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbEntityElectronicLicenseElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_element
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbEntityElectronicLicenseElement record);

    DbEntityElectronicLicenseElement selectOneByOid(String oid);
}

package com.zfsoft.superwindow.dbaccess.dao.itfr;

import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicense;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityElectronicLicenseExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbEntityElectronicLicenseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int countByExample(DbEntityElectronicLicenseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int deleteByExample(DbEntityElectronicLicenseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int insert(DbEntityElectronicLicense record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int insertSelective(DbEntityElectronicLicense record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    List<DbEntityElectronicLicense> selectByExample(DbEntityElectronicLicenseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    DbEntityElectronicLicense selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbEntityElectronicLicense record, @Param("example") DbEntityElectronicLicenseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbEntityElectronicLicense record, @Param("example") DbEntityElectronicLicenseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbEntityElectronicLicense record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_electronic_license_interface
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbEntityElectronicLicense record);
}
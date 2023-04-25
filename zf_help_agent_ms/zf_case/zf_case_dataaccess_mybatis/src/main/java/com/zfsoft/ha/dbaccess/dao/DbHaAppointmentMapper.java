package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaAppointment;
import com.zfsoft.ha.dbaccess.data.example.DbHaAppointmentExample;
import java.util.List;

import com.zfsoft.ha.dbaccess.data.vo.DbHaAppointmentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DbHaAppointmentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    long countByExample(DbHaAppointmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaAppointmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int insert(DbHaAppointment record);
    /**
     * 修改数据
     *
     * @param dbHaAppointment 实例对象
     * @return 影响行数
     */
    int update(DbHaAppointment dbHaAppointment);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int insertSelective(DbHaAppointment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    List<DbHaAppointment> selectByExample(DbHaAppointmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    DbHaAppointment selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaAppointment record, @Param("example") DbHaAppointmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaAppointment record, @Param("example") DbHaAppointmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbHaAppointment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_appointment
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbHaAppointment record);

    /**
     * 查询预约人员信息
     *

     * @return 预约信息集合
     * @author yupeng
     * @date 2022年08月12 10:04:25
     * @date 2022年09月26 15:04:25 zhaobf 修改
     */
    List<DbHaAppointmentVo> queryAppointmentList(DbHaAppointment record);

    /**
     * 根据身份证获取预约信息
     * @return
     */
    List<DbHaAppointmentVo> queryAppointmentByCardNo(String cardNo,String  startTime,String  endTime,String appointmentStatus);

}
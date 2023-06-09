package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaWorkGroup;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkGroupExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DbHaWorkGroupMapper {
    /**
     * 根据id查询分组表信息
     */
    DbHaWorkGroup queryById(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    long countByExample(DbHaWorkGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaWorkGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    int insert(DbHaWorkGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    int insertSelective(DbHaWorkGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    List<DbHaWorkGroup> selectByExample(DbHaWorkGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaWorkGroup record, @Param("example") DbHaWorkGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_work_user_group
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaWorkGroup record, @Param("example") DbHaWorkGroupExample example);
}

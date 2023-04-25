package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManage;
import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManageExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbEntityWindowManageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int countByExample(DbEntityWindowManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int deleteByExample(DbEntityWindowManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int insert(DbEntityWindowManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int insertSelective(DbEntityWindowManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    List<DbEntityWindowManage> selectByExample(DbEntityWindowManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    DbEntityWindowManage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbEntityWindowManage record, @Param("example") DbEntityWindowManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbEntityWindowManage record, @Param("example") DbEntityWindowManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbEntityWindowManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_entity_window_manage
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbEntityWindowManage record);

    /**
     * 根据窗口业务主键查询授权人员
     * @param windoNo
     * @return
     */
    DbEntityWindowManage queryEntityWindowByWindoNo(String windoNo);

    /**
     * 查询所有已配置人员的窗口信息
     * @return
     */
    List<DbEntityWindowManage> querAllyConfigEntityWindow();

    /**
     * 根据用户id查询授权窗口信息
     * @param userOid
     * @return
     */
    DbEntityWindowManage queryEntityWindowByUserOid(String userOid);
}
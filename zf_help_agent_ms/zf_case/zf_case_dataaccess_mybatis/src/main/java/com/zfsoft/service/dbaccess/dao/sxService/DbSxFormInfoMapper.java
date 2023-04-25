package com.zfsoft.service.dbaccess.dao.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxFormInfo;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFormInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSxFormInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int countByExample(DbSxFormInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxFormInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int insert(DbSxFormInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int insertSelective(DbSxFormInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    List<DbSxFormInfo> selectByExample(DbSxFormInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    DbSxFormInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxFormInfo record, @Param("example") DbSxFormInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxFormInfo record, @Param("example") DbSxFormInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxFormInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_form_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxFormInfo record);

    /**
     * 创建表
     *
     * @author yuy
     * @date 2021-8-3
     * @param tableName
     * @param cloums
     */
    void createTableResult(@Param("tableName") String tableName, @Param("cloums") List<String> cloums);

    /**
     * 判断表是否存在
     *
     * @author yuy
     * @date 2021-8-3
     * @param tableName
     */
    int isTableExist(@Param("tableName") String tableName);

    /**
     * 删除表
     *
     * @author yuy
     * @date 2021-8-3
     * @param tableName
     */
    void dropTable(@Param("tableName") String tableName);

    DbSxFormInfo getSxFormInfoByDesignOid(String designOid);

    /**
     * 根据业务主键查询事项表单信息
     * @param formOid
     * @return
     */
    DbSxFormInfo getSxFormInfoByFormOid(String formOid);
}
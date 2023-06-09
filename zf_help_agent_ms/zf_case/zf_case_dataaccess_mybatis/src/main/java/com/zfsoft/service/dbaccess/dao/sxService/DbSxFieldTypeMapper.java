package com.zfsoft.service.dbaccess.dao.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxFieldType;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFieldTypeExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DbSxFieldTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int countByExample(DbSxFieldTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxFieldTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int insert(DbSxFieldType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int insertSelective(DbSxFieldType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    List<DbSxFieldType> selectByExample(DbSxFieldTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    DbSxFieldType selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxFieldType record, @Param("example") DbSxFieldTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxFieldType record, @Param("example") DbSxFieldTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxFieldType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_field_type
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxFieldType record);


    /**
     * @description:  逻辑删除对象
     * @author: wuxx
     * @Date: 2021/7/14 10:46
     **/
    @Update({"update t_sx_field_type set IS_DELETE='1' where FIELD_TYPE_OID = #{fieldTypeOid,jdbcType=VARCHAR}"
    })
    int deleteByFieldTypeOid(String fieldTypeOid);

    /**
     * @description: 根据授权可以获取授权对象
     * @param fieldTypeOid 业务主键
     * @author: wuxx
     * @Date: 2021/7/14 10:46
     **/
    DbSxFieldType selectByFieldTypeOid(String fieldTypeOid);

    /**
     * 查询事项下面所有的分类
     * @param serviceOid
     * @return
     */
    List<DbSxFieldType> querySxFieldTypeListByServiceOid(String serviceOid);

    /**
     * 查询一件事下面的分类类型
     * @param thingOid
     * @return
     */
    List<DbSxFieldType> queryComboFieldTypeListByThingOid(String thingOid);
}

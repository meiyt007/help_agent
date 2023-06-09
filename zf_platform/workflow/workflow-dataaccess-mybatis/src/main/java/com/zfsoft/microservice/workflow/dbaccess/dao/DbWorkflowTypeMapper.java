package com.zfsoft.microservice.workflow.dbaccess.dao;

import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowLink;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowType;
import com.zfsoft.microservice.workflow.dbaccess.data.DbWorkflowTypeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DbWorkflowTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int countByExample(DbWorkflowTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int deleteByExample(DbWorkflowTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @description:  根据业务主键逻辑删除
     * @author: wuxx
     * @Date: 2021/1/22 14:23
     **/
    @Update({"update t_workflow_type set IS_DELETE='1' where TYPE_OID = #{typeOid,jdbcType=VARCHAR} "
    })
    int deleteByTypeOid(String typeOid);

    /**
     * @description:  根据主键逻辑删除
     * @author: wuxx
     * @Date: 2021/1/22 14:23
     **/
    @Update({"update t_workflow_type set IS_DELETE='1' where ID = #{id,jdbcType=BIGINT} "
    })
    int deleteById(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int insert(DbWorkflowType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int insertSelective(DbWorkflowType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    List<DbWorkflowType> selectByExample(DbWorkflowTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    DbWorkflowType selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbWorkflowType record, @Param("example") DbWorkflowTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbWorkflowType record, @Param("example") DbWorkflowTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbWorkflowType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_type
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbWorkflowType record);


    /**
     * @description:  根据业务主键查询信息
     * @param typeOid 业务主键
     * @author: wuxx
     * @Date: 2021/01/22 15:17
     **/
    DbWorkflowType selectDbWorkflowLinkByTypeOid(String typeOid);

}

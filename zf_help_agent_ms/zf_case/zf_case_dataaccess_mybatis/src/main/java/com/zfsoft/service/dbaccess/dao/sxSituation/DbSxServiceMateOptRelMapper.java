package com.zfsoft.service.dbaccess.dao.sxSituation;

import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceMateOptRel;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceMateOptRelExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSxServiceMateOptRelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int countByExample(DbSxServiceMateOptRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxServiceMateOptRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String oid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int insert(DbSxServiceMateOptRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int insertSelective(DbSxServiceMateOptRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    List<DbSxServiceMateOptRel> selectByExample(DbSxServiceMateOptRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    DbSxServiceMateOptRel selectByOid(String oid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxServiceMateOptRel record, @Param("example") DbSxServiceMateOptRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxServiceMateOptRel record, @Param("example") DbSxServiceMateOptRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxServiceMateOptRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_mate_opt_rel
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxServiceMateOptRel record);

    /**
     * 根据选项主键获取与选项关联的精细化材料
     * @param optionValOid
     * @return
     */
    List<DbSxServiceMateOptRel> selectMateOptRelsByOptionValOid(String optionValOid);
}
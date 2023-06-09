package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbZskDict;
import com.zfsoft.superwindow.dbaccess.data.DbZskDictExample;
import com.zfsoft.superwindow.dbaccess.vo.ZskDictVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface DbZskDictMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int countByExample(DbZskDictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int deleteByExample(DbZskDictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int insert(DbZskDict record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int insertSelective(DbZskDict record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    List<DbZskDict> selectByExample(DbZskDictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    DbZskDict selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbZskDict record, @Param("example") DbZskDictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbZskDict record, @Param("example") DbZskDictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbZskDict record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_zsk_dict
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbZskDict record);

    /**
     * 递归查询分类字典树
     * @param parentOid
     * @return
     */
    List<ZskDictVo> queryKnowledgeBaseClassificationTree(String parentOid);

    /**
     * 递归查询父元素
     * @param zskDictOid
     * @return
     */
    List<ZskDictVo> queryAllParents(String zskDictOid);

    /**
     * 根据逻辑主键查询
     * @param zskDictOid    逻辑主键
     * @return
     */
    List<ZskDictVo> queryKnowledgeBaseByZskDictOid(@Param(value = "zskDictOid") String zskDictOid);

    /*
     * @Description: 验证数据是否重复
     * @Author: liyanqing
     * @Date: 2021-03-16 18:01
     * @param code:
     * @param name:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    List<DbZskDict> validateRepeat(Map<String, Object> param);
}
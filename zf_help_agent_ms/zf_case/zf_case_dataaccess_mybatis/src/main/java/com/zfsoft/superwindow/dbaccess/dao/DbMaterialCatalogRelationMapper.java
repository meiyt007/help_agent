package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalogRelation;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalogRelationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbMaterialCatalogRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int countByExample(DbMaterialCatalogRelationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int deleteByExample(DbMaterialCatalogRelationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int insert(DbMaterialCatalogRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int insertSelective(DbMaterialCatalogRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    List<DbMaterialCatalogRelation> selectByExample(DbMaterialCatalogRelationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    DbMaterialCatalogRelation selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbMaterialCatalogRelation record, @Param("example") DbMaterialCatalogRelationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbMaterialCatalogRelation record, @Param("example") DbMaterialCatalogRelationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbMaterialCatalogRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_material_catalog_relation
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbMaterialCatalogRelation record);

    /**
     * 根据业务oid查询
     * @param materialCatalogRelationOid
     * @return
     */
    DbMaterialCatalogRelation getMaterialCatalogRelationByOid(String materialCatalogRelationOid);

    /**
     * 根据目录oid查询目录关联
     * @param materialCatalogOid
     * @return
     */
    List<DbMaterialCatalogRelation> queryListByMaterialCatalogOid(String materialCatalogOid);
}
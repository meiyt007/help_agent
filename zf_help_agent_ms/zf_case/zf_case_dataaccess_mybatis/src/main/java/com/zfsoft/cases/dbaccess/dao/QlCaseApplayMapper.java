package com.zfsoft.cases.dbaccess.dao;

import com.zfsoft.cases.dbaccess.data.DbQlCaseApplay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (QlCaseApplayMapper)表数据库访问层
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Mapper
public interface QlCaseApplayMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbQlCaseApplay queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DbQlCaseApplay> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param qlCaseApplay 实例对象
     * @return 对象列表
     */
    List<DbQlCaseApplay> queryAll(DbQlCaseApplay qlCaseApplay);
    /**
     * 通过申请人姓名，身份证号码作为筛选条件查询
     *
     * @param applyUserName 申请人姓名
     * @param credentialNumber 身份证号码
     * @return 对象列表
     * @author zhaobf
     * @Date: 2022/8/5 9:30
     */
    List<DbQlCaseApplay> queryQlCaseApplayByApplyUserNameAndCredentialNumber(String applyUserName,String credentialNumber);


    /**
     * 新增数据
     *
     * @param qlCaseApplay 实例对象
     * @return 影响行数
     */
    int insert(DbQlCaseApplay qlCaseApplay);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<qlCaseApplay> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbQlCaseApplay> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<qlCaseApplay> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbQlCaseApplay> entities);

    /**
     * 修改数据
     *
     * @param qlCaseApplay 实例对象
     * @return 影响行数
     */
    int update(DbQlCaseApplay qlCaseApplay);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 办件业务主键查询办件扩展信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return com.zfsoft.dbaccess.data.DbQlCaseApplay
     **/
    DbQlCaseApplay queryQlCaseApplayByCaseOid(String caseOid);

    List<DbQlCaseApplay> selectByName(String applyUserName);
}
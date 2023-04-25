package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaUserService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 事项授权或帮代办人员授权mapper
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月03日 14:50:30
 */
@Mapper
public interface DbHaUserServiceMapper {

    /**
     * 保存或更新事项事项授权或人员授权信息
     *
     * @param dbHaUserService 事项授权或帮代办人员授权db
     * @return int 保存结果
     * @author yupeng
     * @date 2022年08月03 15:22:51
     */
    int saveUserService(DbHaUserService dbHaUserService);

    /**
     * 根据事项oid查询事项授权集合
     *
     * @param serviceOid 事项表主键id
     * @return List 事项授权集合
     * @author yupeng
     * @date 2022年08月04 09:21:57
     */
    List<DbHaUserService> queryUserServiceByServiceId(String serviceOid);

    /**
     * 根据事项id ,服务类型,帮代办人员id查询授权信息
     *
     * @param serviceId   事项id
     * @param serviceType 服务类型
     * @param workUserId  帮代办人员id
     * @return 授权信息
     * @author yupeng
     * @date 2022年08月04 11:51:11
     */
    DbHaUserService queryUserServiceByServiceIdAndServiceTypeAndWorkUserId(String serviceId, String serviceType, Long workUserId);


    /**
     * 修改授权信息
     *
     * @param dbHaUserService 事项授权或帮代办人员授权db
     * @return int 修改结果
     * @author yupeng
     * @date 2022年08月04 11:55:28
     */
    int updateUserService(DbHaUserService dbHaUserService);


    /**
     * 查询事项授权信息集合
     *
     * @param serviceIds 事项id集合
     * @param userId     办代办用户id
     * @return 事项授权集合
     * @author zhaobf
     * @date 2022年08月08 11:12:41
     */
    List<DbHaUserService> queryUserServiceByServiceIdsAndUserId(List<String> serviceIds, Long userId);

}

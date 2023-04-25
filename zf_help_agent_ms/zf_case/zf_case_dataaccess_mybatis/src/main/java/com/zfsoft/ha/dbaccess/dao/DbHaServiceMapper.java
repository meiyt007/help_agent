package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.vo.DbHaServiceListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 对接mapper
 *
 * @author yupeng
 * @date 2022年08月09 17:05:59
 */
@Mapper
public interface DbHaServiceMapper {


    /**
     * 根据身份证号码、姓名、企业名称、统一社会信用代码证获取帮代办服务的列表
     *
     * @param cardNo      身份证号码
     * @param name        姓名
     * @param companyName 企业名称
     * @param companyCode 统一社会信用代码证
     * @return 帮代办服务数据集合
     * @author yupeng
     * @date 2022年08月09 17:34:42
     */
    List<DbHaServiceListVo> getHaServiceList(String cardNo, String name, String companyName, String companyCode);
}

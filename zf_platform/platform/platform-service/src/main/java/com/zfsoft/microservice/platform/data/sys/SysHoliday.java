package com.zfsoft.microservice.platform.data.sys;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;


/**
 * @description:  节假日表
 * @author: wuxx
 * @Date: 2020/10/20 10:06
 **/
@Data
@ToString
public class SysHoliday{

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /** 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /** 日期 */
    @NotNull(message = "日期不能为空",groups = {INSERT_GROUP.class})
    private String holidayDate;

    /** 类型 */
    @NotNull(message = "类型 不能为空",groups = {INSERT_GROUP.class})
    private String holidayType;

    /** 备注 */
    private String memo;

}

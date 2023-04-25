package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description //帮代办人员分组实体类
 * @Author: Wangyh
 * @Date: 2022/8/4 13:56
 */
@Data
@ToString
public class HaWorkGroup {
    /** 主键 */
    private Long id;

    /** 组名 */
    private String name;

    /** 备注 */
    private String memo;

    /** 删除状态;1-未删除，2-已删除 */
    private String deleteStatus;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;

    private String takeNumStatus;

    private String deskStatus;


}

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
public class HaWorkLoginLocation {

    private Long id;

    private String name;

    private String sort;

    private String deleteStatus;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;


}

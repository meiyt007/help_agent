package com.zfsoft.ha.data.vo;


import com.zfsoft.ha.data.HaWorkUserSchedule;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description //员工排班Vo
 * @Author: zhaobf
 * @Date: 2022/9/23 15:44
 */
@Data
@ToString
public class HaWorkUserVo_Schedule {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;
    /**
     * 分组编号
     */
    private Long groupId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 工号
     */
    private String workNumber;


    private List<HaWorkUserSchedule> haWorkUserScheduleList;
}

package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 帮代办员工vo
 *
 * @author：yupeng
 * @date：2022年08月02日 13:17:03
 * @version：1.0
 */
@Data
@ToString
public class HaWorkUserRegistrarVo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 分组职务
     */
    private String groupPost;

    /**
     * 帮代办人员账号
     */
    private String workUserName;
}

package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //用户表
 * @Author: Wangyh
 * @Date: 2022/7/15 13:11
 */
@Data
@ToString
public class HaWorkUserVo {
    /** 主键 */
    private Long id;

    /** 姓名 */
    private String name;

    /** 工号 */
    private String workNumber ;

    /** 头像 */
    private String image;

    private String memo;

}

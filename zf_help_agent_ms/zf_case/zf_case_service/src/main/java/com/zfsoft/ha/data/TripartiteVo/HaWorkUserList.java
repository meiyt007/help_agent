package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //获取所有的圆桌办事人员名字及其目前所登录的帮办区域响应类
 * @Author: Wangyh
 * @Date: 2023/4/7 15:36
 */
@Data
@ToString
public class HaWorkUserList {
    /**
     * 帮办人员姓名
     */
    private String name;

    /**
     * 所登录的帮办区域
     */
    private String loginLocationName;

    /**
     * 帮办人员的状态
     */
    private String status;

    /**
     * 帮办人员好评率
     */
    private String evalScore;
}

package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/4/10 14:39
 */
@Data
@ToString
public class NumberOfServiceVo {
    /**
     *  快捷今日已服务人数
     */
    private Integer todayServiceKJAllNum;
    /**
     *  快捷今年已服务人数
     */
    private Integer toYearServiceKJAllNum;
    /**
     *  圆桌今日已服务人数
     */
    private Integer todayServiceYZAllNum;
    /**
     *  圆桌今年已服务人数
     */
    private Integer toYearServiceYZAllNum;
}

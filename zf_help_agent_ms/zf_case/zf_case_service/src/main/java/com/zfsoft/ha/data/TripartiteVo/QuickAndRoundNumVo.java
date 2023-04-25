package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/4/10 13:19
 */
@Data
@ToString
public class QuickAndRoundNumVo {
    /**
     * 今日等待的人数（快捷）
     */
    private Integer waitIngKJAllNum;
    /**
     * 今日等待的人数（圆桌）
     */
    private Integer waitIngYZAllNum;
}

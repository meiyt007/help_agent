package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //获取排队人数及预计耗时接口,响应类
 * @Author: Wangyh
 * @Date: 2022/9/27 18:07
 */
@Data
@ToString
public class WorkUserRespVo {
    /**
     * 等待的人数
     */
    private Integer waitingNum;
    /**
     * 预计等待时间
     */
    private Integer esuimateTime;
}

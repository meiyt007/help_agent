package com.zfsoft.single.data.ywbl;

import lombok.*;

import java.util.Date;

/**
 * 服务办件星级实体类
 *
 * @author wangwg
 * @since 2021-06-17
 */
@Data
@ToString
public class HandleServiceStar {

    private Long id;
    /**
     * 办理人编号
     */
    private String code;
    /**
     * 平均星数
     */
    private Integer starValue;

    private Date createDate;

    private String createUser;

}

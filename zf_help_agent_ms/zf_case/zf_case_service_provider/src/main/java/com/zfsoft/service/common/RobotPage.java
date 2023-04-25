package com.zfsoft.service.common;

import lombok.Data;
import lombok.ToString;
import java.util.HashMap;
import java.util.List;

/**
 * @author hutao
 * @date 2022/5/17
 * 问答机器人分页数据结构
 */
@Data
@ToString
public class RobotPage<T> {

    /** 每页显示记录数 */
    private Integer pageRows;
    /** 当前页码 */
    private Integer curr;
    /** 总页数 */
    private Integer allPage;
    /** 总记录数 */
    private Integer allRows;
    /** 数据 */
    private List<T> data;
    /** 当前查询的关键字 */
    private String kws;
    /** 会话id */
    private String talkId;
    /** 扩展数据 */
    private HashMap<String,Object> extraData = new HashMap<>();
    /** 统计数据 */
    private String countData;

    public RobotPage(){

    }

    public RobotPage(List<T> data,
                     int curr,
                     int allRows,
                     int pageRows) {
        this.data = data;
        // 设置当前页码
        this.curr = curr;
        // 每页显示记录数
        this.pageRows = pageRows;
        // 设置总的记录数
        this.allRows = allRows;
        // 计算出总的页数
        this.allPage = this.allRows / this.pageRows
                + (this.allRows % this.pageRows == 0 ? 0 : 1);
    }

}

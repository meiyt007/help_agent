package com.zfsoft.ha.data.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.ToString;

/**
 * 事项授权tree 使用
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月04日 10:10:14
 */
@Data
@ToString
public class HaServiceRegistrarVo {


    /**
     * 封装所有的tree node
     */
    private JSONArray treeArray;


    /**
     * 封装所有的子节点信息
     */
    private JSONArray allChildNodeArray;

    /**
     * 封装选中的tree的子节点id
     */
    private JSONArray defaultCheckedArray;

    /**
     * 封装已选中的子节点信息
     */
    private JSONArray checkedArray;

}

package com.zfsoft.single.data.yxpz.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName 树形选择
 **/
@Data
@ToString
public class CascaderTreeVo {

    private String value;

    private String label;

    private List<CascaderTreeVo> children;

    private String parentOid;
}

package com.zfsoft.service.sxSys.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**事项字典
 * @author wangxialing
 * @Description:com.zfsoft.sys.data
 * @date 2020/10/22
 */
@Data
@ToString
public class SxSysDict {
    /**
     * 主健
     */
    private Long id;
    /**
     * 业务主健
     */
    private String oid;
    /**
     * 代码
     */
    private String code;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 上级字典主键
     */
    private String parentOid;
    /**
     * 字典主键路径
     */
    private String path;
    /**
     * 启用状态(0否、1是)
     */
    private Integer isAble;
    /**
     *删除状态(0否、1是)
     */
    private Integer isDelete;
    /**
     * 创建人
     */
    private String createUserOid;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 字典拼音
     */
    private String phonetic;
    /**
     * 简称
     */
    private String abbreviation;
    /**
     * 图标一
     */
    private String iconOne;
    /**
     * 图标二
     */
    private String iconTwo;
    /**
     * 国家标准编码
     */
    private String nationCode;
    /**
     * 说明
     */
    private String memo;

    private List<SxSysDict> childSysDict;
}

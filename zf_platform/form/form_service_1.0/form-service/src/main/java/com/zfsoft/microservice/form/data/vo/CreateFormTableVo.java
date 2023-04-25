package com.zfsoft.microservice.form.data.vo;

import com.zfsoft.microservice.form.data.FormTable;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CreateFormTableVo
 * @Description: 根据表结构创建表
 * @Author wuxx
 * @Date 2021/9/8
 **/
@Data
public class CreateFormTableVo {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 存储数据库主键
     */
    private String datasourceOid;

    /**
     * 是否是新建 默认为0新建   1修改
     */
    private Integer isCreate;

    /**
     * 表字段属性集合
     */
    private List<FormTable> formTableList;

    /**
     * 主键是否varchar类型  ——目前只针对综窗的mysql  1是  其他不是
     */
    private String idIsVarchar;
}

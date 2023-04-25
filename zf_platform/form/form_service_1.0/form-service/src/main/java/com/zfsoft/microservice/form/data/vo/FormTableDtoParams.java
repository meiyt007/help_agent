package com.zfsoft.microservice.form.data.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName FormColumnVo
 * @Description:
 * @Author wuxx
 * @Date 2021/8/6
 **/
@Data
public class FormTableDtoParams {

    //数据库字段集合配置JSON数组
    private String formTableDtoJson;

    //数据源id
    private String datasourceOid;

    //表名
    private String tableName;

    //表单编码
    private String formCode;

    /**
     * 是否是新建 默认为0新建   1修改
     */
    private Integer isCreate;

    /**
     * 主键是否varchar类型  ——目前只针对综窗的mysql  1是  其他不是
     */
    private String idIsVarchar;

    /**
     * 存储对象主键
     */
    private String objectOid;
}

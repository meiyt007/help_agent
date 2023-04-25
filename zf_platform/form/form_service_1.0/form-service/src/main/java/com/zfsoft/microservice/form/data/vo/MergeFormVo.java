package com.zfsoft.microservice.form.data.vo;

import lombok.Data;

/**
 * @ClassName FormColumnVo
 * @Description:
 * @Author wuxx
 * @Date 2021/8/6
 **/
@Data
public class MergeFormVo {

    //表单业务主键集合，多个用逗号分割
    private String formMainOids;

    //表单编码集合，多个用逗号分割
    private String formCodes;

    //新存储对象的业务主键
    private String objectOid;

    //数据库存储类型  0本地  1API  2数据库
    private Integer saveDataType;

    //合并规则json
    private String formConfigJson;

    //合并后的表单code (非必填)
    private String formCode;

    //合并后的表单formMainOid (非必填)
    private String formMainOid;

    /**
     * 是否包含字子表单  默认false  不包含  true包含
     **/
    private Boolean includeChild;
}

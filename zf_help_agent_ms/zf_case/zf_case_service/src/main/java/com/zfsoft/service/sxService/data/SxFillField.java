package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @ClassName SxFillField
 * @Description 事项表单字段实体类
 * @Author dxl
 **/
@Data
@ToString
public class SxFillField {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    private Long id;

    private String fieldOid;

    private String fieldTypeOid;

    private String fieldTypeName;

    private String labelOid;

    private String labelName;

    private String serviceOid;

    private String serviceName;

    private String fieldName;

    private String fieldCode;

    private String columnType;

    private String columnLenght;

    private Integer delFlag;

    private Date createDate;

    private Date modifyDate;

    //存储对象类型
    private Integer dataType;

    private String formOid;

    private List<String> typeLabelTree;

//    private String interId;

    /** 预检规则返回值ID */
    private String interApiValId;
}

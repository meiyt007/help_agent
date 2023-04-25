package com.zfsoft.microservice.form.data.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ExportFormVo
 * @Description: 表单导出wordVo
 * @Author wuxx
 * @Date 2021/8/6
 **/
@Data
public class ExportFormVo {

    //设计主键-填报主键
    private String designAndReportOid;

    //关联设计主键-填报主键，多个用逗号隔开
    private String linkDesignAndReportOids;

    //模板主键
    private String docxTemplateOid;

    //初始化map
    private HashMap<String, Object> initDataMap;

    /**
     * 是否生成pdf  默认false
     **/
    private Boolean isPdf;
}

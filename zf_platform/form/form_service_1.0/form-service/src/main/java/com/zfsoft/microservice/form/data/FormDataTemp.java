package com.zfsoft.microservice.form.data;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表单数据解析
 */
@Data
public class FormDataTemp {

    private boolean updata;
    private Object id;
    private Map<String, Object> savaDataMap;
    private Map<String, Object> apiDataMap;

    public FormDataTemp() {
        savaDataMap = new LinkedHashMap<>();
        apiDataMap = new LinkedHashMap<>();
    }
}

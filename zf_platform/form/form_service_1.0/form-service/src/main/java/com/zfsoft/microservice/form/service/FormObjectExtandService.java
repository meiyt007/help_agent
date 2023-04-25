package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormObjectExtand;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 存储对象扩展信息接口定义
 */
@RequestMapping("/manager/security/objectExtand")
public interface FormObjectExtandService {

    /**
     * 查询存储对象扩展列表
     * @param objectOid 存储对象主键
     * @return list
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryFormObjectExtandList/{objectOid}")
    ApiResultSet<List<FormObjectExtand>> queryFormObjectExtandList(@PathVariable("objectOid") String objectOid);
}

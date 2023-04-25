package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.BaseFormField;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  基础表单字段管理
 */
@RequestMapping(value = "/basicFormField")
public interface BasicFormFieldService {


    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryInfoWithPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryInfoWithPage(@RequestParam(value = "fieldKey", required = false) String fieldKey,
                                               @RequestParam(value = "fieldName", required = false) String fieldName,
                                               @RequestParam(value = "fieldType", required = false) String fieldType,
                                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                               @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne", method = {RequestMethod.POST})
    ApiResultSet getOne(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    ApiResultSet delete(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody BaseFormField baseFormField);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getBasicFieldListByType", method = {RequestMethod.POST})
    ApiResultSet getBasicFieldListByType(@RequestParam(value = "type", required = false) String type);

    /**
     * 根据条件查询所有的配置信息
     * @param baseFormField
     * @return
     */
    @GetMapping(value = "/queryBaseFormFieldList")
    ApiResultSet<List<BaseFormField>> queryBaseFormFieldList(BaseFormField baseFormField);

    @GetMapping(value = "/getOneByOid")
    @ProcessFeignCalledResult
    ApiResultSet<BaseFormField> getOneByOid(@RequestParam(value = "oid") String oid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkHasRepeat", method = {RequestMethod.GET})
    ApiResultSet checkHasRepeat(@RequestParam(value = "fieldType", required = false) String fieldType,
                                @RequestParam(value = "fieldKey", required = false) String fieldKey,
                                @RequestParam(value = "oid", required = false) String oid);

}

package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.BaseFormField;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityBasicFormField;
import com.zfsoft.superwindow.manager.clzs.BasicFormFieldManager;
import com.zfsoft.superwindow.service.clzs.BasicFormFieldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-03 10:49:29
 * @description: 材料类别控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasicFormFieldController implements BasicFormFieldService {

    //基础表单字段信息
    private final BasicFormFieldManager basicFormFieldManager;


    /**
     * 分页查询基础表单
     * @param fieldKey
     * @param fieldName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult> queryInfoWithPage(String fieldKey, String fieldName, String fieldType, Integer pageNum, Integer pageSize) {
        log.info("参数 fieldKey: {},  fieldName: {}", fieldKey, fieldName);
        PageResult<DbEntityBasicFormField> pageResult=this.basicFormFieldManager.queryInfoWithPage(fieldKey, fieldName, fieldType, pageNum,pageSize);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }


    /**
     * 根据id查询基础表单字段信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet getOne(String id) {
        log.info("参数 id: {}", id);
        DbEntityBasicFormField dbEntityBasicFormField=this.basicFormFieldManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(dbEntityBasicFormField));
        return new ApiResultSet(dbEntityBasicFormField);
    }


    /**
     * 保存/更新基础表单字段信息
     * @param baseFormField
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdate(BaseFormField baseFormField) {
        ApiResultSet apiResultSet = new ApiResultSet();
        this.basicFormFieldManager.saveOrUpdate(baseFormField);
        log.info("保存/更新材料类别信息成功：{}", JSON.toJSONString(baseFormField));
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<BaseFormField>> queryBaseFormFieldList(BaseFormField baseFormField) {
       List<BaseFormField> list= basicFormFieldManager.queryBaseFormFieldList(baseFormField);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<BaseFormField> getOneByOid( String oid) {
       BaseFormField filed= basicFormFieldManager.selectByOid(oid);
        return new ApiResultSet<>(filed);
    }

    /**
     *  检查一个所属类型下面是否有同一个key
     * @param fieldKey
     * @param fieldType
     * @return
     */
    private boolean checkTypeField(String fieldKey, String fieldType) {
        return this.basicFormFieldManager.checkTypeField(fieldKey, fieldType);
    }

    /**
     * 根据id删除材料类别信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet delete(String id) {
        this.basicFormFieldManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    /**
     * 根据type查询list信息 （1 单办   2 一件事）
     * @param type
     * @return
     */
    @Override
    public ApiResultSet getBasicFieldListByType(String type) {
        log.info("param type: {}", type);
        List<DbEntityBasicFormField> basicFieldListByType = this.basicFormFieldManager.getBasicFieldListByType(type);
        return new ApiResultSet(basicFieldListByType);
    }

    @Override
    public ApiResultSet checkHasRepeat(String fieldType, String fieldKey, String oid) {
        log.info("param fieldType: {}, fieldKey: {}, oid: {}", fieldType, fieldKey, oid);
        String result  = this.basicFormFieldManager.checkHasRepeat(fieldType, fieldKey, oid);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(result);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

}

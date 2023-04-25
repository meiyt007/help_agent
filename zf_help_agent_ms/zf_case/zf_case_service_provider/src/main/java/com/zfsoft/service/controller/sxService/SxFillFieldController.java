package com.zfsoft.service.controller.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxFillField;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxFillFieldManager;
import com.zfsoft.service.manager.sxService.SxFormInfoManager;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.SxOptionField;
import com.zfsoft.service.sxService.service.SxFillFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FillFieldController
 * @Author xiayj
 * @Date 2021/7/14 11:43
 **/
@RestController
@Slf4j
public class SxFillFieldController implements SxFillFieldService {

    @Resource
    private SxFillFieldManager sxFillFieldManager;

    @Resource
    private SxFormInfoManager sxFormInfoManager;

    /***
     * @param contentOid
     * @param fieldName
     * @param fieldCode
     * @param labelOid
     * @param pageNum
     * @param pageSize
     * @description: 查询事项表字段分页
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet<PageResult<SxFillField>> queryFillFieldPage(String contentOid, String fieldName, String fieldCode, String labelOid, Integer pageNum, Integer pageSize) {
        PageResult<SxFillField> pageResult = sxFillFieldManager.querySxFillFieldPage(contentOid,fieldName,fieldCode,labelOid,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /***
     * @param fieldOid
     * @description: 获取字段信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.formConfig.data.SxFillField>
     * @author: xiayj
     * @date: 2021/7/15
     */
    @Override
    public ApiResultSet<SxFillField> getFillField(String fieldOid) {
        SxFillField sxFillField = sxFillFieldManager.getSxFillFieldByOid(fieldOid);
        return new ApiResultSet<>(sxFillField);

    }

    /***
     * @param sxFillField
     * @description: 保存更新字段信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet saveOrUpdateFillField(SxFillField sxFillField) {
        sxFillFieldManager.saveOrUpdateSxFillField(sxFillField);
        return new ApiResultSet();
    }

    /**
     * @param fieldOid
     * @description: 删除字段
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet deleteFillField(String fieldOid) {
        sxFillFieldManager.deleteSxFillField(fieldOid);
        return new ApiResultSet();
    }

    /***
     * @param serviceOid
     * @description: 根据serviceOid查询对应字段数据
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFillField>>
     * @author: xiayj
     * @date: 2021/7/28
     */
    @Override
    public ApiResultSet<List<SxFillField>> queryFieldList(String serviceOid, String fieldTypeOid) {
        List<SxFillField> list = sxFillFieldManager.queryFieldList(serviceOid, fieldTypeOid, null);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<SxFillField>> queryFieldListByOids(String serviceOid, String fieldTypeOid, String labelOid) {
        List<SxFillField> list = sxFillFieldManager.queryFieldListByOids(serviceOid,fieldTypeOid,labelOid);
        return new ApiResultSet<>(list);
    }

    /***
     * 更新字段集合
     * @param sxFillFields
     * @author lushuai
     * @date 2021/8/27 15:58
     */
    @Override
    public ApiResultSet batchUpdateFillField(List<SxFillField> sxFillFields) {
        sxFillFieldManager.batchUpdateFillField(sxFillFields);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<List<SxFillField>> queryFieldListInfo(String serviceOid, String fieldTypeOid, String labelOid) {
        List<SxFillField> list=sxFillFieldManager.queryFieldListInfo(serviceOid,fieldTypeOid,labelOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<SxFormInfo> saveOrUpdateField(SxFormInfo sxFormInfo) {
        if(sxFormInfo!=null){
            sxFormInfoManager.saveOrUpdateSxFormInfo(sxFormInfo);
            //保存字段列表
            if(sxFormInfo.getFieldList()!=null &&sxFormInfo.getFieldList().size()>0){
                sxFillFieldManager.saveOrUpdateField(sxFormInfo.getFieldList(), sxFormInfo.getFormOid());
            }
        }
        return new ApiResultSet<>(sxFormInfo);
    }

    @Override
    public ApiResultSet<SxFillField> getDbSxFillFieldByOid(String fieldOid) {
       DbSxFillField field= sxFillFieldManager.getDbSxFillFieldByOid(fieldOid);
       if(field!=null){
           SxFillField fillField=new SxFillField();
           BeanUtils.copyProperties(field,fillField);
           return new ApiResultSet<>(fillField);
       }
        return null;
    }

    @Override
    public ApiResultSet saveOrUpdateOptionField(SxOptionField sxOptionField) {
        ApiResultSet apiResultSet = new ApiResultSet();
        int i = sxFillFieldManager.saveOrUpdateOptionField(sxOptionField);
        apiResultSet.setData(i);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryOptionFieldListByValOid(String valOid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        List<SxOptionField> sxOptionFields = sxFillFieldManager.queryOptionFieldListByValOid(valOid);
        apiResultSet.setData(sxOptionFields);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxOptionField>> queryOptionFieldListByServiceOid(String serviceOid) {
        List<SxOptionField> sxOptionFields = sxFillFieldManager.queryOptionFieldListByServiceOid(serviceOid);
        return new ApiResultSet<>(sxOptionFields);
    }

    @Override
    public ApiResultSet<List<SxFillField>> queryFillFieldList(String serviceOid, String fieldTypeOid, String formOid) {
        List<SxFillField> list = sxFillFieldManager.queryFieldList(serviceOid, fieldTypeOid, formOid);
        return new ApiResultSet<>(list);
    }
}

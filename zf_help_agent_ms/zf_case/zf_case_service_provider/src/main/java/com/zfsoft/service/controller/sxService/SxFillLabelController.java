package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxFillLabelManager;
import com.zfsoft.service.sxService.data.FieldTree;
import com.zfsoft.service.sxService.data.SxFillLabel;
import com.zfsoft.service.sxService.service.SxFillLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SxFillLabelController
 * @Description 字段标签类
 * @Author xiayj
 * @Date 2021/7/14 16:15
 **/
@RestController
@Slf4j
public class SxFillLabelController implements SxFillLabelService {


    @Resource
    private SxFillLabelManager sxFillLabelManager;

    /***
     * @param contentOid
     * @param labelName
     * @param labelCode
     * @param typeOid
     * @param pageNum
     * @param pageSize
     * @description: 查询事项表单标签分页
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet<PageResult<SxFillLabel>> queryFillLabelPage(String contentOid, String labelName, String labelCode, String typeOid, Integer pageNum, Integer pageSize) {
        PageResult<SxFillLabel> pageResult = sxFillLabelManager.querySxFillLabelPage(contentOid,labelName,labelCode,typeOid,pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /***
     * @param labelOid
     * @description: 获取标签详情
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.formConfig.data.SxFillLabel>
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet<SxFillLabel> getFillLabel(String labelOid) {
        SxFillLabel sxFillLabel = sxFillLabelManager.getSxFillLabelByOid(labelOid);
        return new ApiResultSet<>(sxFillLabel);
    }

    /***
     * @param sxFillLabel
     * @description: 保存更新标签信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet saveOrUpdateFillLabel(SxFillLabel sxFillLabel) {
        sxFillLabelManager.saveOrUpdateSxFillLabel(sxFillLabel);
        return new ApiResultSet();
    }

    /**
     * @param labelOid
     * @description: 删除标签（必须保证标签没有关联字段）
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @Override
    public ApiResultSet<String> deleteFillLabel(String labelOid) {
        sxFillLabelManager.deleteSxFillLabel(labelOid);
        return new ApiResultSet<>();
    }

    /***
     * @description: 查看分类标签树
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.dataChange.data.FieldTree>>
     * @author: xiayj
     * @date: 2021/7/16
     */
    @Override
    public ApiResultSet<List<FieldTree>> querySxFieldTypeAndLabelTree(String serviceOid) {
        List<FieldTree> sxFieldTypes = sxFillLabelManager.querySxFieldTypeAndLabelTree(serviceOid);
        return new ApiResultSet<>(sxFieldTypes);
    }

    /***
     *
     * 根据事项查询标签列表
     * @param serviceOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFillLabel>>
     * @author: xiayj
     * @date: 2021/8/4
     */
    @Override
    public ApiResultSet<List<SxFillLabel>> querySxFillLabelList(String serviceOid,String typeOid) {
        List<SxFillLabel> sxFillLabels = sxFillLabelManager.querySxFillLabelList(serviceOid,typeOid);
        return new ApiResultSet<>(sxFillLabels);
    }

    @Override
    public ApiResultSet<List<FieldTree>> querySxFieldTypeAndLabelAndSxFillFieldTree(String serviceOid) {
        List<FieldTree> sxFieldTypes = sxFillLabelManager.querySxFieldTypeAndLabelAndSxFillFieldTree(serviceOid);
        return new ApiResultSet<>(sxFieldTypes);
    }
}

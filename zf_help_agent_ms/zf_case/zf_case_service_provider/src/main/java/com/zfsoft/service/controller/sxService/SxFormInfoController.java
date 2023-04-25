package com.zfsoft.service.controller.sxService;

import com.zfsoft.service.dto.FormTableDto;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SxFormInfoManager;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.ZcFormInfo;
import com.zfsoft.service.sxService.service.SxFormInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SxFormInfoController
 * @Description 事项字段关联controller
 * @Author xiayj
 * @Date 2021/7/21 15:50
 **/
@RestController
public class SxFormInfoController implements SxFormInfoService {

    @Resource
    private SxFormInfoManager sxFormInfoManager;

    /***
     * @param serviceOid
     * @description: 获取表单关联列表
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFormInfo>>
     * @author: xiayj
     * @date: 2021/7/21
     */
    @Override
    public ApiResultSet<List<SxFormInfo>> getSxFormInfoList(String serviceOid) {
        List<SxFormInfo> sxFormInfoList = sxFormInfoManager.getSxFormInfoList(serviceOid,null);
        return new ApiResultSet<>(sxFormInfoList);
    }

    /***
     * @param connectOid
     * @description: 查询关联信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.formConfig.data.SxFormInfo>
     * @author: xiayj
     * @date: 2021/7/22
     */
    @Override
    public ApiResultSet<SxFormInfo> getSxFormInfoByOid(String connectOid) {
        SxFormInfo sxFormInfo = sxFormInfoManager.getSxFormInfoByOid(connectOid);
        return new ApiResultSet<>(sxFormInfo);
    }

    /**
     * @param sxFormInfo
     * @description: 保存更新关联信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/7/22
     */
    @Override
    public ApiResultSet<String> saveSxFormInfo(SxFormInfo sxFormInfo) {
        sxFormInfoManager.saveOrUpdateSxFormInfo(sxFormInfo);
        return new ApiResultSet<>();
    }

    /***
     * @param connectOid
     * @description: 删除关联信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/7/22
     */
    @Override
    public ApiResultSet<String> deleteSxFormInfo(String connectOid) {
        sxFormInfoManager.deleteSxFormInfo(connectOid);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<String> ableSxFormInfo(Long id) {
        String str=sxFormInfoManager.ableSxFormInfo(id);
        return new ApiResultSet<>(str);
    }

    @Override
    public ApiResultSet<String> saveOrUpdateCreateTable(String url, String userName, String password, List<String> list) throws Exception {
        sxFormInfoManager.saveOrUpdateCreateTable(url, userName, password, list);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<String> createTableSqlByParams(FormTableDto formTableDto) throws Exception {
        String sql = sxFormInfoManager.createTableSqlByParams(formTableDto);
        return new ApiResultSet<>(sql);
    }

    /***
     * 获取事项表单列表
     * @param contentOid
     * @param typeOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFormInfo>>
     * @author: xiayj
     * @date: 2021/8/12
     */
    @Override
    public ApiResultSet<List<SxFormInfo>> getFormInfoList(String contentOid, String typeOid) {
        List<SxFormInfo> sxFormInfoList = sxFormInfoManager.getSxFormInfoList(contentOid, typeOid);
        return new ApiResultSet<>(sxFormInfoList);
    }

    /**
     * 更新表单的designOid
     *
     * @param oid
     * @param designOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/8/26
     */
    @Override
    public ApiResultSet<String> updateDesign(String oid, String designOid,String formMainOid,String serviceOid) {
        sxFormInfoManager.updateDesign(oid,designOid,formMainOid,serviceOid);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<List<SxFormInfo>> getDesignFormList(String serviceOid) {
        List<SxFormInfo> sxFormInfoList = sxFormInfoManager.getDesignFormfoList(serviceOid);
        return new ApiResultSet<>(sxFormInfoList);
    }

    @Override
    public ApiResultSet<List<SxFormInfo>> queryFormInfoExist(String serviceOid, String fieldTypeOid) {
        List<SxFormInfo> sxFormInfoList = sxFormInfoManager.queryFormInfoExist(serviceOid,fieldTypeOid);
        return new ApiResultSet<>(sxFormInfoList);
    }

    @Override
    public ApiResultSet<String> updateDesignChildName(String oid, String childFormName) {
        sxFormInfoManager.updateDesignChildName(oid,childFormName);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<SxFormInfo> getSxFormInfoByDesignOid(String designOid) {
        SxFormInfo sxFormInfo =sxFormInfoManager.getSxFormInfoByDesignOid(designOid);
        return new ApiResultSet<>(sxFormInfo);
    }

    @Override
    public ApiResultSet<List<SxFormInfo>> getDesignFormListByServiceOidAndTypeOid(String serviceOid, String typeOid) {
        List<SxFormInfo> sxFormInfoList = sxFormInfoManager.getDesignFormListByServiceOidAndTypeOid(serviceOid,typeOid);
        return new ApiResultSet<>(sxFormInfoList);
    }

    @Override
    @ApiOperation(value = "查询电子表单")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项oid", dataType = "string", example = "")})
    public ApiResultSet<List<SxFormInfo>> getBrowserDesignFormList(String serviceOid) {
        List<SxFormInfo> sxFormInfoList = sxFormInfoManager.getDesignFormfoList(serviceOid);
        return new ApiResultSet<>(sxFormInfoList);
    }

    @Override
    public ApiResultSet<List<ZcFormInfo>> getZcFormInfoList(String serviceOid, String caseOid, String valOids) {
        List<ZcFormInfo> zcFormInfos = sxFormInfoManager.getZcFormInfoList(serviceOid, caseOid, valOids);
        return new ApiResultSet<>(zcFormInfos);
    }
}

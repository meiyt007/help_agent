package com.zfsoft.service.controller.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxSituation.ServiceMaterialManager;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import com.zfsoft.service.sxSituation.service.ServiceMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangns
 * @description 事项颗粒材料
 * @date 2020/11/3 10:08
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class ServiceMaterialController implements ServiceMaterialService {

    @Resource
    private ServiceMaterialManager serviceMaterialManager;

    @Override
    public ApiResultSet<ServiceMaterial> getServiceMaterialByOid(String oid){

        ServiceMaterial serviceMaterial = serviceMaterialManager.getServiceMaterialByOid(oid);

        ApiResultSet<ServiceMaterial> apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(serviceMaterial);

        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<ServiceMaterial>> queryServiceMaterialWithPage(String serviceOid, String materialName, String baiduTemplateIds, Integer pageNum, Integer pageSize) {
        ServiceMaterial serviceMaterial=new ServiceMaterial();
        if(StrUtil.isNotEmpty(serviceOid)){
            serviceMaterial.setServiceOid(serviceOid);
        }
        if(StrUtil.isNotEmpty(materialName)) {
            serviceMaterial.setMaterialName(materialName);
        }
        if(StrUtil.isNotEmpty(baiduTemplateIds)) {
            serviceMaterial.setBaiduTemplateIds(baiduTemplateIds);
        }
        PageResult<ServiceMaterial> pageResult = serviceMaterialManager.queryServiceMaterialWithPage(serviceMaterial,pageNum,pageSize);
        ApiResultSet<PageResult<ServiceMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;

    }

    @Override
    public ApiResultSet<ServiceMaterial> updateServiceMaterial(ServiceMaterial serviceMaterial) {
        serviceMaterialManager.updateServiceMaterial(serviceMaterial);
        return new ApiResultSet(serviceMaterial);
    }

    @Override
    public ApiResultSet<List<ServiceMaterial>> getServiceMaterialsByServiceOid(String serviceOid) {
        List<ServiceMaterial>  serviceMaterialList= serviceMaterialManager.getServiceMaterialsByServiceOid(serviceOid);
        return new ApiResultSet(serviceMaterialList);
    }


}

package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.RefinedMaterialManager;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.service.RefinedMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @ClassName RefinedMaterialController
 * @Description 细化材料控制类
 * @Author liangss
 * @Date 2021-07-12
 * @Version V1.1
 **/
@RestController
@Slf4j
public class RefinedMaterialController implements RefinedMaterialService {

    @Resource
    private RefinedMaterialManager refinedMaterialManager;

    @Override
    public ApiResultSet<List<RefinedMaterial>> getRefinedMaterialListByMaterialOid(String materialOid) {
        List<RefinedMaterial> refinedMaterialList=refinedMaterialManager.getRefinedMaterialListByMaterialOid(materialOid);
        ApiResultSet<List<RefinedMaterial>> apiResultSet = new  ApiResultSet<List<RefinedMaterial>>();
        apiResultSet.setData(refinedMaterialList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<RefinedMaterial> getRefinedMaterialByOid(String oid) {
        RefinedMaterial refinedMaterial=refinedMaterialManager.getRefinedMaterialByOid(oid);
        ApiResultSet<RefinedMaterial> apiResultSet = new ApiResultSet<RefinedMaterial>();
        apiResultSet.setData(refinedMaterial);
        return apiResultSet;
    }

    @Override
    public ApiResultSet updateRefinedMaterialList(List<RefinedMaterial> refinedMaterialList) {
        String materialCatalogOids="";
        try {
            materialCatalogOids= refinedMaterialManager.updateRefinedMaterialList(refinedMaterialList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ApiResultSet(materialCatalogOids);

    }

    @Override
    public ApiResultSet saveOrUpdateRefinedMaterial(RefinedMaterial refinedMaterial) {
        try{
            if (!StringUtils.isEmpty(refinedMaterial.getRefinedMaterialName())) {
                refinedMaterialManager.saveOrUpdateRefinedMaterial(refinedMaterial);
        } else {
                return new ApiResultSet("细化材料名称不能为空");
            }
        }catch (Exception e){
            return new ApiResultSet("保存失败");
        }
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet delete(String id) {
        refinedMaterialManager.delete(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet<List<RefinedMaterial>> getOnlyRefinedMaterialByServiceOid(String serviceOid) {
       List<RefinedMaterial> list= refinedMaterialManager.getOnlyRefinedMaterialByServiceOid(serviceOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<RefinedMaterial>> getOnlyRefinedMaterialByComboDirectoryOid(String comboDirectoryOid) {
        List<RefinedMaterial> list= refinedMaterialManager.getOnlyRefinedMaterialByComboDirectoryOid(comboDirectoryOid);
        return new ApiResultSet<>(list);
    }


}

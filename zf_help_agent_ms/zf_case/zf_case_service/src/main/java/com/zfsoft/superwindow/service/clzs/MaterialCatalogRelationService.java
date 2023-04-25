package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogRelation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description:  材料目录关联记录接口
 * @Author: liangss
 * @Date: 2021/1/25 15:27
 */
@RequestMapping("/materialCatalogRelation")
public interface MaterialCatalogRelationService {
    /**
     * @Description: 保存更新材料目录关联记录接口
     * @Author: liangss
     * @Date: 2021/1/25 15:27
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody MaterialCatalogRelation materialCatalogRelation) throws Exception;

    /**
     * @Description: 删除材料目录关联记录接口
     * @Author: liangss
     * @Date: 2021/1/25 15:28
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    ApiResultSet del(@RequestBody String id);


    /**
     * @Description: 根据业务oid查询材料目录关联记录
     * @Author: liangss
     * @Date: 2021/1/25 15:29
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getMaterialCatalogRelationByOid",method = {RequestMethod.GET})
    ApiResultSet<MaterialCatalogRelation> getMaterialCatalogRelationByOid(@RequestParam(value = "materialCatalogRelationOid", required = false) String materialCatalogRelationOid);

    /**
     * @Description:  根据目录oid查询目录关联记录
     * @Author: liangss
     * @Date: 2021/1/25 15:29
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryList", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalogRelation>> queryList(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);

}


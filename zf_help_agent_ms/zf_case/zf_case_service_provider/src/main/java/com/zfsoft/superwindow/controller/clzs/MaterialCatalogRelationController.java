package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogRelation;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogRelationManager;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-04 10:49:29
 * @description: 材料目录控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialCatalogRelationController implements MaterialCatalogRelationService {

    private final MaterialCatalogRelationManager materialCatalogRelationManager;



    @Override
    public ApiResultSet saveOrUpdate(MaterialCatalogRelation materialCatalogRelation) throws Exception {
        this.materialCatalogRelationManager.saveOrUpdate(materialCatalogRelation);
        log.info("保存/更新目录关联记录信息成功：{}", JSON.toJSONString(materialCatalogRelation));
        return new ApiResultSet(materialCatalogRelation);
    }

    @Override
    public ApiResultSet del(String id) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        this.materialCatalogRelationManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet<MaterialCatalogRelation> getMaterialCatalogRelationByOid(String materialCatalogRelationOid) {
        Assert.hasLength(materialCatalogRelationOid, "主键不能为空！");
        MaterialCatalogRelation materialCatalogRelation=this.materialCatalogRelationManager.getMaterialCatalogRelationByOid(materialCatalogRelationOid);
        log.info("详情获取成功：{}", JSON.toJSONString(materialCatalogRelation));
        return new ApiResultSet(materialCatalogRelation);
    }

    @Override
    public ApiResultSet<List<MaterialCatalogRelation>> queryList(String materialCatalogOid) {
        List<MaterialCatalogRelation> materialCatalogRelationList=this.materialCatalogRelationManager.queryList(materialCatalogOid);
        return new ApiResultSet(materialCatalogRelationList);
    }
}




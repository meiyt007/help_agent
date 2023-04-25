package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplate;
import com.zfsoft.superwindow.data.clzs.FaModelTemplateBlock;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.superwindow.manager.clzs.FaModelTemplateBlockManager;
import com.zfsoft.superwindow.manager.clzs.FaModelTemplateManager;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogElementManager;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogElementService;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-04 10:49:29
 * @description: 材料目录元素控制层
 */
@Slf4j
@RestController
public class MaterialCatalogElementController implements MaterialCatalogElementService {

    @Resource
    private MaterialCatalogElementManager materialCatalogElementManager;

    @Resource
    private FaModelTemplateManager faModelTemplateManager;

    @Resource
    private FaModelTemplateBlockManager faModelTemplateBlockManager;

    /**
     * 分页查询材料目录元素数据
     * @param elementCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<MaterialCatalogElement>> queryMaterialCatalogElementWithPage(String elementCode, String elementName, String materialCatalogOid, Integer pageNum, Integer pageSize) {
        MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
        materialCatalogElement.setElementCode(elementCode);
        materialCatalogElement.setElementName(elementName);
        materialCatalogElement.setMaterialCatalogOid(materialCatalogOid);
        PageResult<MaterialCatalogElement> pageResult=this.materialCatalogElementManager.queryMaterialCatalogElementWithPage(materialCatalogElement,pageNum,pageSize);
        log.info("获取材料目录元素列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * 查询材料目录元素
     * @param id
     * @return
     */
    @Override
    public ApiResultSet getone(String id) {
        Assert.hasLength(id, "主键不能为空！");
        MaterialCatalogElement materialCatalogElement=this.materialCatalogElementManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(materialCatalogElement));
        return new ApiResultSet(materialCatalogElement);
    }

    /**
     * 保存/更新材料目录元素信息
     * @param materialCatalogElement
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdate(MaterialCatalogElement materialCatalogElement) {
        this.materialCatalogElementManager.saveOrUpdate(materialCatalogElement);
        log.info("保存/更新材料目录元素信息信息成功：{}", JSON.toJSONString(materialCatalogElement));
        return new ApiResultSet(materialCatalogElement);
    }

    /**
     * 根据id删除材料目录元素信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet delete(String id) {
        this.materialCatalogElementManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }



    /**
     * 验证已启用的识别模板是否选用该元素
     * @param materialCatalogElementOid
     * @return
     */
    @Override
    public ApiResultSet checkIsRelation(String materialCatalogElementOid){
        String isExist="N";
        //查询已发布列表
        List<FaModelTemplate> faModelTemplateList=this.faModelTemplateManager.queryFaModelTemplateList(null, "YFB");
            for(FaModelTemplate f:faModelTemplateList){
                String faModelTemplateOid=f.getFaModelTemplateOid();
                FaModelTemplateBlock faModelTemplateBlock=new FaModelTemplateBlock();
                faModelTemplateBlock.setFaModelTemplateOid(faModelTemplateOid);
                faModelTemplateBlock.setMaterialCatalogElementOid(materialCatalogElementOid);
                //查询区块
                List<FaModelTemplateBlock> faModelTemplateBlockList=this.faModelTemplateBlockManager.queryFaModelTemplateBlockList(faModelTemplateBlock);
                if(faModelTemplateBlockList.size()>0){
                    isExist="Y";
                    break;
                }
            }

        log.info("校验已启用的识别模板是否选用该元素成功：{}", materialCatalogElementOid+"****"+isExist);
        return new ApiResultSet(isExist);
    }

    @Override
    public ApiResultSet<List<MaterialCatalogElement>> queryList(MaterialCatalogElement materialCatalogElement) {
        List<MaterialCatalogElement> catalogElements = this.materialCatalogElementManager.queryList(materialCatalogElement);
        log.info("获取材料目录元素列表调用成功结果为：{}", JSON.toJSONString(catalogElements));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(catalogElements);
        return apiResultSet;
    }

    @Override
    public ApiResultSet checkMaterialCatalogChlidRepeat(String materialCatalogElementOid, String materialParentOid, String elementName) {
        String isExist="0";
        MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
        materialCatalogElement.setMaterialCatalogOid(materialParentOid);
        materialCatalogElement.setElementName(elementName);
        List<MaterialCatalogElement> catalogElements = this.materialCatalogElementManager.queryListByName(materialCatalogElement);
        if(null!=catalogElements&&catalogElements.size()>=1){
            if(catalogElements.size()==1){
                materialCatalogElement=catalogElements.get(0);
                if(StringUtils.isNotEmpty(materialCatalogElementOid)&&materialCatalogElement.getMaterialCatalogElementOid().equals(materialCatalogElementOid.trim())){
                    isExist="0";
                }else{
                    isExist="1";
                }
            }else{
                isExist="1";
            }
        }
        return new ApiResultSet(isExist);
    }

    @Override
    public ApiResultSet<List<MaterialCatalogElement>> queryMaterialCatalogElementList(String materialCatalogOid) {
        MaterialCatalogElement materialCatalogElement=new  MaterialCatalogElement();
        materialCatalogElement.setMaterialCatalogOid(materialCatalogOid);
        List<MaterialCatalogElement> catalogElements = this.materialCatalogElementManager.queryList(materialCatalogElement);
        log.info("获取材料目录元素列表调用成功结果为：{}", JSON.toJSONString(catalogElements));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(catalogElements);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<MaterialCatalogElement>> queryMaterialCatalogElementByCatalogOids(List<String> materialCatalogOid) {
        List<MaterialCatalogElement> catalogElements = this.materialCatalogElementManager.queryMaterialCatalogElementByCatalogOids(materialCatalogOid);
        return new ApiResultSet<>(catalogElements);
    }
}

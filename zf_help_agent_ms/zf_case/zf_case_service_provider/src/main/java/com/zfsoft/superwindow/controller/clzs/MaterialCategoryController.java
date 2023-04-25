package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.MaterialCategory;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogManager;
import com.zfsoft.superwindow.manager.clzs.MaterialCategoryManager;
import com.zfsoft.superwindow.service.clzs.MaterialCategoryService;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-03 10:49:29
 * @description: 材料类别控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/materialCategory")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialCategoryController implements MaterialCategoryService {
    //材料类别
    private final MaterialCategoryManager materialCategoryManager;
    //材料目录
    private final MaterialCatalogManager materialCatalogManager;
    /**
     * 分页查询材料类别
     * @param materialCategory
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @PostMapping(value = "/queryMaterialCategoryWithPage")
    public ApiResultSet<PageResult<MaterialCategory>> queryMaterialCategoryWithPage(MaterialCategory materialCategory, Integer pageNum, Integer pageSize) {
        PageResult<MaterialCategory> pageResult=this.materialCategoryManager.queryMaterialCategoryWithPage(materialCategory,pageNum,pageSize);
        //log.info("获取材料类别列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }


    /**
     * 根据id查询材料类别信息
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/getOne")
    public ApiResultSet getOne(String id) {
        MaterialCategory materialCategory=this.materialCategoryManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(materialCategory));
        return new ApiResultSet(materialCategory);
    }


    /**
     * 保存/更新材料类别信息
     * @param materialCategory
     * @return
     */
    @Override
    @PostMapping(value = "/saveOrUpdate")
    public ApiResultSet saveOrUpdate(@RequestBody MaterialCategory materialCategory) {
        this.materialCategoryManager.saveOrUpdate(materialCategory);
        log.info("保存/更新材料类别信息成功：{}", JSON.toJSONString(materialCategory));
        return new ApiResultSet(materialCategory);
    }

    /**
     * 根据id删除材料类别信息
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/delete")
    public ApiResultSet delete(String id) {
        this.materialCategoryManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }
    /**
     * 检验是否有材料目录管理通过材料类别
     * @param materialCategoryOid
     * @return
     */
    @Override
    @GetMapping(value = "/checkIsExist")
    public ApiResultSet checkIsExist(String materialCategoryOid){
        String isExist="N";
        Long shuliang=this.materialCatalogManager.queryMaterialCatalogCountBymaterialCategoryOid(materialCategoryOid);
        if(shuliang>0){
             isExist="Y";
        }
        log.info("校验是否有材料目录管理通过材料类别接口调用程成功：{}", materialCategoryOid+"****"+isExist);
        return new ApiResultSet(isExist);
    }

    /**
     * @description 材料类别是否重复
     * @param materialCategoryOid, categoryName, categoryCode
     * @return com.zfsoft.platform.common.data.ApiResultSet
     * @author chenjm
     * @date 2021/4/7 13:36
     **/
    @Override
    @GetMapping(value = "/checkHasRepeat")
    public ApiResultSet checkHasRepeat(String materialCategoryOid, String categoryName, String categoryCode) {
        String isExist="0";
        MaterialCategory materialCategory=new MaterialCategory();
        materialCategory.setCategoryName(categoryName);
        materialCategory.setCategoryCode(categoryCode);
        List<MaterialCategory> materialCategoryList=  this.materialCategoryManager.getMaterialCategoryList(materialCategory);
        if(null!=materialCategoryList&&materialCategoryList.size()>=1){
            if(materialCategoryList.size()==1){
                materialCategory=materialCategoryList.get(0);
                if(StringUtils.isNotEmpty(materialCategoryOid)&&materialCategory.getMaterialCategoryOid().equals(materialCategoryOid.trim())){
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

    /**
     * 查询材料分类列表
     * @return
     */
    @PostMapping(value = "/queryMaterialCategoryList")
    public ApiResultSet queryMaterialCategoryList(){
        List<MaterialCategory>   materialCategoryList= this.materialCategoryManager.queryList(null);
        log.info("查询材料分类列表：{}", JSON.toJSONString(materialCategoryList));
        return new ApiResultSet<>(materialCategoryList);
    }


}

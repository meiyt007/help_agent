package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogRelation;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalog;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogManager;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogRelationManager;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogService;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.FaStaticParam;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: liangss
 * @create: 2020-11-04 10:49:29
 * @description: 材料目录控制层
 */
@Slf4j
@RestController
public class MaterialCatalogController implements MaterialCatalogService {

    //材料目录
    @Resource
    private MaterialCatalogManager materialCatalogManager;

    @Resource
    private MaterialCatalogRelationManager materialCatalogRelationManager;

/*    @Resource
    private  SxServiceMaterialFeginService sxServiceMaterialFeginService;*/

    /**
     * 分页查询材料目录数据
     * @param catalogName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<MaterialCatalog>> queryMaterialCatalogWithPage(String  catalogName, String catalogCode, Integer pageNum, Integer pageSize) {
        MaterialCatalog materialCatalog =new  MaterialCatalog();
        materialCatalog.setCatalogName(catalogName);
        materialCatalog.setCatalogCode(catalogCode);
        PageResult<MaterialCatalog> pageResult=this.materialCatalogManager.queryMaterialCatalogWithPage(materialCatalog,pageNum,pageSize);
        log.info("获取材料目录列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }


    /**
     * 初始化数据
     * @return
     */
    @Override
    public ApiResultSet initAll() {

        Map<String, Object> resultMap = new HashMap<>();
        String A3= FaStaticParam.SIZE_TYPE_A3;
        String A4=FaStaticParam.SIZE_TYPE_A4;
        String WIDTH=FaStaticParam.SIZE_TYPE_WIDTH;
        String HEIGHT=FaStaticParam.SIZE_TYPE_HEIGHT;
        resultMap.put("A3",A3);
        resultMap.put("A4",A4);
        resultMap.put("WIDTH",WIDTH);
        resultMap.put("HEIGHT",HEIGHT);

        Map<String, String> SIZE_TYPE_A4_MAP =FaStaticParam.SIZE_TYPE_A4_MAP;
        Map<String, String> SIZE_TYPE_A3_MAP =FaStaticParam.SIZE_TYPE_A3_MAP;
        Map<String, String> ANGLE_TYPE =FaStaticParam.ANGLE_TYPE;
        Map<String, String> LICE_OCR_TYPE_MAP =FaStaticParam.LICE_OCR_TYPE_MAP;

        resultMap.put("SIZE_TYPE_A4_MAP",SIZE_TYPE_A4_MAP);
        resultMap.put("SIZE_TYPE_A3_MAP",SIZE_TYPE_A3_MAP);
        resultMap.put("ANGLE_TYPE",ANGLE_TYPE);
        resultMap.put("LICE_OCR_TYPE_MAP",LICE_OCR_TYPE_MAP);

        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdate(MaterialCatalog materialCatalog) {
        this.materialCatalogManager.saveOrUpdate(materialCatalog);
        log.info("保存/更新材料目录信息成功：{}", JSON.toJSONString(materialCatalog));
        return new ApiResultSet(materialCatalog);
    }

    @Override
    public ApiResultSet saveOrUpdateMaterialCatalogAndSubitem(MaterialCatalog materialCatalog) throws Exception {
        this.materialCatalogManager.saveOrUpdateMaterialCatalogAndSubitem(materialCatalog);
        log.info("保存/更新材料目录信息成功：{}", JSON.toJSONString(materialCatalog));
        return new ApiResultSet(materialCatalog);
    }

    /**
     * 查询材料目录以及目录子项
     * @param id
     * @return
     */
    @Override
    public ApiResultSet getMaterialCatalogAndSubitem(String id) {
        Assert.hasLength(id, "主键不能为空！");
        MaterialCatalog materialCatalog=this.materialCatalogManager.getMaterialCatalogAndSubitem(id);
        log.info("详情获取成功：{}", JSON.toJSONString(materialCatalog));
        return new ApiResultSet(materialCatalog);
    }

    /**
     * 删除材料目录以及目录子项信息
     * @param id
     */
    @Override
    public ApiResultSet del(String id) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        this.materialCatalogManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }
    /**
     * @description:  查询所有一级材料目录
     * @author: chenjm
     * @Date: 2020/11/05 15:14
     **/
    @Override
    public ApiResultSet<List<MaterialCatalog>> querySysConfigListByParentCode(){
        List<MaterialCatalog> catalogList=this.materialCatalogManager.queryListNoParent();
        return new ApiResultSet(catalogList);
    }

    @Override
    public ApiResultSet saveOrUpdateMater(@RequestBody String  json) {
        JSONObject jsonObj = JSONObject.parseObject(json);
        String serviceOid= jsonObj.get("serviceOid").toString();
        JSONArray materialOidsJsonArray =  jsonObj.getJSONArray("materialOids");
        JSONArray materialCatalogOidsJsonArray =  jsonObj.getJSONArray("materialCatalogOids");
        for (int i = 0; i < materialOidsJsonArray.size(); i++) {
            String materialOid=materialOidsJsonArray.get(i).toString();  //材料主键
            String materialCatalogOid=materialCatalogOidsJsonArray.get(i).toString();//目录主键
            System.out.println("materialOid:"+materialOid+"    materialCatalogOid:"+materialCatalogOid);
        }
        return null;
    }


     /**
     * 判断是否关联事项
     * @param
     * @return
     */
    @Override
    public ApiResultSet checkIsReationSx(String materialCatalogOid){
        String isExist="N";
       /* ApiResultSet<List<SxServiceMaterial>> ms=sxServiceMaterialFeginService.getSxServiceMaterialListByMaterialCatalogOid(materialCatalogOid);
        List<SxServiceMaterial> sxServiceMaterialList=ms.getData();*/
        List<MaterialCatalogRelation> materialCatalogRelationList=this.materialCatalogRelationManager.queryList(materialCatalogOid);
        if(materialCatalogRelationList.size()>0){
            isExist="Y";
        }
        log.info("校验是否有材料目录管理通过材料类别接口调用程成功：{}", materialCatalogOid+"****"+isExist);
        return new ApiResultSet(isExist);
    }

    @Override
    public ApiResultSet<List<MaterialCatalog>> queryList(String materialParentOid) {
        List<MaterialCatalog> catalogList = this.materialCatalogManager.queryList(materialParentOid);
        return new ApiResultSet(catalogList);
    }

    @Override
    public ApiResultSet<MaterialCatalog> getMaterialCatalogByOid(String materialCatalogOid) {
        MaterialCatalog catalog = materialCatalogManager.getMaterialCatalogByOid(materialCatalogOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(catalog);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<MaterialCatalog>> queryListByMaterialCatalog(MaterialCatalog materialCatalog) {
       List<MaterialCatalog> list= materialCatalogManager.queryList(materialCatalog);
        return new ApiResultSet(list);
    }

    /**
     * @description 判断目录是否重复
     * @param materialCatalogOid, catalogName
     * @return com.zfsoft.platform.common.data.ApiResultSet
     * @author chenjm
     * @date 2021/4/7 14:49
     **/
    @Override
    public ApiResultSet checkMaterialCatalogRepeat(String materialCatalogOid, String catalogName) {
        String isExist="0";
        MaterialCatalog materialCatalog=new MaterialCatalog();
        materialCatalog.setCatalogName(catalogName);
        List<MaterialCatalog> list= materialCatalogManager.queryParentListByName(materialCatalog);
        if(null!=list&&list.size()>=1){
            if(list.size()==1){
                materialCatalog=list.get(0);
                if(StringUtils.isNotEmpty(materialCatalogOid)&&materialCatalog.getMaterialCatalogOid().equals(materialCatalogOid.trim())){
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
    public ApiResultSet getMaterialCatalogOid(String materialCatalogOid) {
        DbMaterialCatalog catalog=this.materialCatalogManager.getMaterialCatalogOid(materialCatalogOid);
        MaterialCatalog material=new MaterialCatalog();
        BeanUtils.copyProperties(catalog,material);
        return new ApiResultSet(material);
    }


    @Override
    public ApiResultSet<List<MaterialCatalog>> querySubitemMaterialCatalogList() {
        List<MaterialCatalog> materialCatalogList=  materialCatalogManager.querySubitemMaterialCatalogList();
        return new ApiResultSet(materialCatalogList);
    }

    @Override
    public ApiResultSet<List<MaterialCatalog>> queryAllCatalogList() {
        List<MaterialCatalog> materialCatalogList=materialCatalogManager.queryAllCatalogList();
        return new ApiResultSet(materialCatalogList);
      /*  List<MaterialCatalogTreeSelect> treeSelects = GenDataMaterialCatalogTreeUtil.buildMaterialCatalogTreeSelect(materialCatalogList);
        return new ApiResultSet(treeSelects);*/

    }

    @Override
    public ApiResultSet<List<MaterialCatalog>> queryAllCatalogByCatalogOidsList(List<String> materialCatalogOids) {
        List<MaterialCatalog> materialCatalogList=materialCatalogManager.queryAllCatalogByCatalogOidsList(materialCatalogOids);
        return new ApiResultSet(materialCatalogList);
    }
}




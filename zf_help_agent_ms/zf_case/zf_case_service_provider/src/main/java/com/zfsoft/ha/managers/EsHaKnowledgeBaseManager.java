package com.zfsoft.ha.managers;

import com.alibaba.fastjson.JSONArray;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.vo.EsHaKnowledgeBase;
import com.zfsoft.ha.data.vo.EsHaMaterial;
import com.zfsoft.ha.data.vo.HaFileKnowledgeBase;
import com.zfsoft.ha.front.FHaKnowledgeBaseService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxAcceptConditionManager;
import com.zfsoft.service.manager.sxService.SxServiceManager;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.manager.sxService.SxServiceQuestionManager;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.service.util.ElasticSearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * es 知识库
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022-07-20
 */
@Service
@Slf4j
public class EsHaKnowledgeBaseManager {
    /**
     * es索引名称
     */
    public static final String KNOWLEDGE_INDEX_NAME = "knowledge";

    /**
     * es索引名称 材料
     */
    public static final String MATERIAL_INDEX_NAME = "material";

    /**
     * 实施清单服务定义接口
     */
    @Resource
    private SxServiceService sxServiceFeginService;

    /**
     * 实施清单实现类
     */
    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private SxSysAttaManager sxSysAttaManager;
    /**
     * 查询事项列表
     */
    @Resource
    private HaKnowledgeServiceManager haKnowledgeServiceManager;
    /**
     * 实施清单-受理条件 实现类
     */
    @Resource
    private SxAcceptConditionManager sxAcceptConditionManager;

    /**
     * 实施清单-常见问题 实现类
     */
    @Resource
    private SxServiceQuestionManager sxServiceQuestionManager;
    /**
     * 实施清单-申请材料 实现类
     */
    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    /**
     * 知识库文件接口
     */
    @Resource
    private FHaKnowledgeBaseService fHaKnowledgeBaseService;
    /**
     * 初始化知识库
     *
     * @return
     * @throws Exception
     */
    public void importMaterialAll() throws Exception {
        if (ElasticSearchUtil.isExistsIndexExce(MATERIAL_INDEX_NAME)) {
            ElasticSearchUtil.deleteIndexExce(MATERIAL_INDEX_NAME);
        }

        List<EsHaMaterial> ehkbs = new ArrayList<>();
        /**
         * 初始化获取文件知识库列表集合
         */
        ApiResultSet<List<HaFileKnowledgeBase>> knowledgeServiceList= fHaKnowledgeBaseService.queryKnowledgeServiceList();
        List<HaFileKnowledgeBase> baseList = knowledgeServiceList.getData();
         for(int i=0;i<baseList.size();i++){
             HaFileKnowledgeBase haFileKnowledgeBase = baseList.get(i);
             EsHaMaterial esHaMaterial = new EsHaMaterial();
             esHaMaterial.setMaterialName(haFileKnowledgeBase.getFileName());
             esHaMaterial.setMaterialUrl(haFileKnowledgeBase.getFastdfsNginxUrl());
             esHaMaterial.setServiceOid(haFileKnowledgeBase.getServiceOid());
             if(StringUtil.isNotEmpty(haFileKnowledgeBase.getServiceOid())){
                 SxService sxService = sxServiceManager.getSxServiceByOidEs(haFileKnowledgeBase.getServiceOid());
                 if(StringUtil.isNotEmpty(sxService.getServiceName())){
                     esHaMaterial.setServiceName(sxService.getServiceName());
                 }else{
                     esHaMaterial.setServiceName("");
                 }
             }
             ehkbs.add(esHaMaterial);
         }
        /**
         * 初始化获取材料库列表集合
         */
        List<SxServiceMaterial> allSxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(null);
         for(int i = 0 ;i<allSxServiceMaterial.size();i++){
             EsHaMaterial esHaMaterial = new EsHaMaterial();
             SxServiceMaterial sxServiceMaterial = allSxServiceMaterial.get(i);
             if(StringUtil.isNotEmpty(sxServiceMaterial.getMaterialSampleAddrYl()) &&StringUtil.isNotEmpty(sxServiceMaterial.getMaterialName()) ){
                 esHaMaterial.setMaterialName(sxServiceMaterial.getMaterialName());
                 esHaMaterial.setMaterialUrl(sxServiceMaterial.getMaterialSampleAddrYl());
                 esHaMaterial.setServiceOid(sxServiceMaterial.getServiceOid());
                 if(StringUtil.isNotEmpty(sxServiceMaterial.getServiceOid())){
                     SxService sxService = sxServiceManager.getSxServiceByOidEs(sxServiceMaterial.getServiceOid());
                     if(StringUtil.isNotEmpty(sxService.getServiceName())){
                         esHaMaterial.setServiceName(sxService.getServiceName());
                     }else{
                         esHaMaterial.setServiceName("");
                     }
                 }
                 ehkbs.add(esHaMaterial);
             }

             if(StringUtil.isNotEmpty(sxServiceMaterial.getMaterialEmptyAddr())){
                 SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                 if(StringUtil.isNotEmpty(sxSysAtta.getOriginName())){
                     esHaMaterial.setMaterialName(sxSysAtta.getOriginName());
                     esHaMaterial.setMaterialUrl(sxSysAtta.getFilePath());
                     esHaMaterial.setServiceOid(sxServiceMaterial.getServiceOid());
                     if(StringUtil.isNotEmpty(sxServiceMaterial.getServiceOid())){
                         SxService sxService = sxServiceManager.getSxServiceByOidEs(sxServiceMaterial.getServiceOid());
                         if(StringUtil.isNotEmpty(sxService.getServiceName())){
                             esHaMaterial.setServiceName(sxService.getServiceName());
                         }else{
                             esHaMaterial.setServiceName("");
                         }
                     }
                     ehkbs.add(esHaMaterial);
                 }
             }
         }
        ElasticSearchUtil.insertDataBatch(MATERIAL_INDEX_NAME, "materialList", JSONArray.toJSONString(ehkbs));

    }
    /**
     * 初始化知识库
     *
     * @return
     * @throws Exception
     */
    public void importAll() throws Exception {
        if (ElasticSearchUtil.isExistsIndexExce(KNOWLEDGE_INDEX_NAME)) {
            ElasticSearchUtil.deleteIndexExce(KNOWLEDGE_INDEX_NAME);
        }

        List<EsHaKnowledgeBase> ehkbs = new ArrayList<>();


        ApiResultSet<List<SxService>> sxServicListByDistrictOid = sxServiceFeginService.getSxServicListByDistrictOid("");
        List<SxService> sxServices = sxServicListByDistrictOid.getData();

        //遍历事项信息
        if(sxServices==null||sxServices.size()==0){
            ElasticSearchUtil.insertDataBatch(KNOWLEDGE_INDEX_NAME, "serviceId", JSONArray.toJSONString(ehkbs));
            return;
        }
        for (SxService s : sxServices) {
            SxService sxService = sxServiceManager.getSxServiceByOid(s.getServiceOid());
            List<Map<String,String>> materialList = new ArrayList<>();
            StringBuilder keyword = new StringBuilder();
            EsHaKnowledgeBase ehkb = new EsHaKnowledgeBase();

            if(null!=sxService) {

                //服务名称
                stringAppend(keyword,sxService.getServiceName());
                //机构名称
//                keyword.append(sxService.getDistrictName()).append(";");
                stringAppend(keyword,sxService.getOrganName());
                //主题分类
                stringAppend(keyword,sxService.getSubjectClassificationName());
                //事项结果名称
                stringAppend(keyword,sxService.getSxServiceExtend().getResultName());
                //事项受理条件
                List<SxAcceptCondition> sxAcceptConditions = sxAcceptConditionManager.getSxAcceptConditionByServiceOid(sxService.getServiceOid());
                String accept = sxAcceptConditions.stream().map(SxAcceptCondition::getConditionText).collect(Collectors.joining(","));
                stringAppend(keyword,accept);
                //事项材料
//                List<SxServiceMaterial> materials = sxService.getMaterials();
//                for (SxServiceMaterial material : materials) {
//                    Map<String,String> map = new HashMap<>();
//                    map.put("materialOid",material.getMaterialOid());
//                    map.put("materialName",material.getMaterialName());
//                    materialList.add(map);
//                }
                List<SxServiceMaterial> sxMaterialFormTempList = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(sxService.getServiceOid());
                for (SxServiceMaterial material : sxMaterialFormTempList) {
                    Map<String,String> map = new HashMap<>();
                    map.put("materialOid",material.getMaterialOid());
                    map.put("materialName",material.getMaterialName());
                    materialList.add(map);
                }
                //常见问题
                List<SxServiceQuestion> sxServiceQuestions = sxServiceQuestionManager.getSxServiceQuestionByServiceOid(sxService.getServiceOid());
                String question = sxServiceQuestions.stream().map(sxServiceQuestion -> {
                    StringBuilder quest = new StringBuilder();
                    quest.append(sxServiceQuestion.getKeyWord()).append(",")
                            .append(sxServiceQuestion.getTitle()).append(",")
                            .append(sxServiceQuestion.getDescription()).append(",")
                            .append(sxServiceQuestion.getAnswer()).append(",");
                    return quest.toString();
                }).collect(Collectors.joining(","));
                stringAppend(keyword,question);


                ehkb.setServiceId(sxService.getId());
                ehkb.setServiceOid(sxService.getServiceOid());
                ehkb.setName(sxService.getServiceName());
                ehkb.setTitleName(sxService.getServiceName());
                ehkb.setMaterialList(materialList);
                ehkb.setKeyword(keyword.toString());
            }
            ehkbs.add(ehkb);
        }
//       分段插入
//        int i = (ehkbs.size()/ 1000)+1;
//        for(int j = 0 ; j < i; j++){
//            int end = (j==i-1?(ehkbs.size()-1):(((j + 1) * 1000) - 1)) ;
//            List<EsHaKnowledgeBase> esHaKnowledgeBases = ehkbs.subList(j*1000,end);
//            ElasticSearchUtil.insertDataBatch(KNOWLEDGE_INDEX_NAME, "serviceId", JSONArray.toJSONString(esHaKnowledgeBases));
//            log.info("知识库：导入下标"+j*1000+"-"+end+"的数据成功");
//        }
        ElasticSearchUtil.insertDataBatch(KNOWLEDGE_INDEX_NAME, "serviceId", JSONArray.toJSONString(ehkbs));

        /**
         * springboot封装的save方法，和esUtil有依赖或者版本冲突，弃用

         Iterable<EsHaKnowledgeBase> esHaKnowledgeBases = esHaKnowledgeBaseRepository.saveAll(ehkbs);
         Iterator<EsHaKnowledgeBase> iterator = esHaKnowledgeBases.iterator();
         int result = 0;
         while (iterator.hasNext()) {
         result++;
         iterator.next();
         }

         //        return result;*/
    }
    /**
     * 根据关键词查询知识库
     */
    public PageResult<EsHaMaterial> searchMaterialList(String keyword,Integer pageNumber,Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        QueryBuilder queryBuilder = null;
        if(StringUtil.isNotEmpty(keyword)){
             queryBuilder = QueryBuilders.multiMatchQuery(keyword, "materialName","serviceName");

        }else{
             queryBuilder = QueryBuilders.matchAllQuery();

        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field("materialName").preTags("<span style='color:red'>").postTags("</span>");
//        highlightBuilder.field("serviceName").preTags("<span style='color:red'>").postTags("</span>");
        PageResult<EsHaMaterial> pageResult = ElasticSearchUtil.searchDataPage(EsHaMaterial.class,queryBuilder,highlightBuilder,pageNumber,pageSize,MATERIAL_INDEX_NAME);
        return pageResult;
    }

    /**
     * 根据关键词查询知识库
     */
    public List<EsHaKnowledgeBase> search(String keyword) {
        /**
         * springboot封装的save方法，和esUtil有依赖或者版本冲突，弃用

         //        return esHaKnowledgeBaseRepository.findByNameOrMemo(keyword,keyword);
         */
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "name","titleName","materialList.materialName", "keyword");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("titleName").preTags("<span style='color:red'>").postTags("</span>");
//        highlightBuilder.field("materialList.materialName").preTags("<span style='color:red'>").postTags("</span>");
//        highlightBuilder.field("keyword").preTags("<span style='color:red'>").postTags("</span>");

        List<EsHaKnowledgeBase> ts = ElasticSearchUtil.searchDataList(EsHaKnowledgeBase.class,
                queryBuilder, highlightBuilder, KNOWLEDGE_INDEX_NAME);
        return ts;
    }


    public void stringAppend(StringBuilder sb,String s){
        if(StringUtil.isNotEmpty(s)){
            sb.append(s).append(";");
        }
    }
}

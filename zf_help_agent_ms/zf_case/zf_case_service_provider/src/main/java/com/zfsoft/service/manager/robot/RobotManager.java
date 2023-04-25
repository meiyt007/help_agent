package com.zfsoft.service.manager.robot;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceExtendMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceQuestionMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxService;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceExample;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceExtendWithBLOBs;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceQuestionWithBLOBs;
import com.zfsoft.service.document.RobotDocument;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.common.RobotPage;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.manager.sxService.SxServiceLocationManager;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.util.ElasticSearchUtil;
import com.zfsoft.service.sxService.data.SxServiceLocation;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hutao
 * @date 2022/5/18
 * 机器人对话方法管理类
 */
@Service
@Slf4j
public class RobotManager {

    /** es索引名称 */
    public static final String ROBOT_INDEX_NAME = "robot_data";

    /** 办事指南类型 */
    public static final String MODEL_TYPE_1 = "1";
    /** 常见问题类型 */
    public static final String MODEL_TYPE_2 = "2";

    /** 默认当前页码 */
    public static final Integer DEFAULT_PAGE_NUMBER = 1;
    /** 默认分页每页显示记录数 */
    public static final Integer DEFAULT_PAGE_SIZE = 5;

    @Resource
    private DbSxServiceMapper dbSxServiceMapper;

    @Resource
    private SysDistrictFeignService sysDistrictFeignService;

    @Resource
    private SysOrganFeginService sysOrganFeignService;

    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;

    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    @Resource
    private SxServiceLocationManager sxServiceLocationManager;

    @Resource
    private DbSxServiceQuestionMapper dbSxServiceQuestionMapper;

    /**
     * 删除数据
     */
    public void deleteData() {
        ElasticSearchUtil.deleteIndex(ROBOT_INDEX_NAME);
    }

    /**
     * 生成数据
     */
    public void generateData() {
        // 办事指南数据
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        criteria.andExistChildItemEqualTo("0");
        criteria.andServiceStatusEqualTo((short) 3);
        List<DbSxService> dbSxServices = dbSxServiceMapper.selectByExample(dbSxServiceExample);
        List<RobotDocument> robotDocumentList = dbSxServices.stream().map(dbSxService -> {
            RobotDocument robotDocument = new RobotDocument();
            robotDocument.setModelType(MODEL_TYPE_1);
            robotDocument.setDocId(dbSxService.getServiceOid());
            robotDocument.setServiceName(dbSxService.getServiceName());
            robotDocument.setDistrictOid(dbSxService.getDistrictOid());
            //区划名称
            if(StrUtil.isNotEmpty(dbSxService.getDistrictOid())){
                SysDistrict district = sysDistrictFeignService.getSysDistrictByDistrictOid(dbSxService.getDistrictOid()).getData();
                if(null != district){
                    robotDocument.setDistrictName(district.getName());
                    robotDocument.setDistrictPath(district.getPath());
                }
            }
            //实施部门名称
            if(StrUtil.isNotEmpty(dbSxService.getOrganOid())){
                SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(dbSxService.getOrganOid()).getData();
                if(null != sysOrgan){
                    robotDocument.setOrganName(sysOrgan.getName());
                }
            }
            // 扩展信息
            DbSxServiceExtendWithBLOBs serviceExtend = dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(dbSxService.getServiceOid());
            if(serviceExtend!=null){
                // 承诺期限
                robotDocument.setPromiseLimit(serviceExtend.getPromiseLimit() +
                        SxptBaseStaticParameter.LIMIT_TYPE_MAP.get(serviceExtend.getPromiseLimitType()));
                // 办理地址
                StringBuilder serviceLocation = new StringBuilder();
                // 公交路线（未找到咨询电话字段，使用公交路线代替）
                StringBuilder busRoute = new StringBuilder();
                List<SxServiceLocation> sxServiceLocations = sxServiceLocationManager.getSxServiceLocationByExtendOid(serviceExtend.getExtendOid());
                if(null != sxServiceLocations && sxServiceLocations.size()>0){
                    for (int i = 0; i < sxServiceLocations.size(); i++) {
                        SxServiceLocation location = sxServiceLocations.get(i);
                        if (i > 0) {
                            serviceLocation.append("</br>");
                            busRoute.append("</br>");
                        }
                        serviceLocation.append(location.getLocationAddr()).
                                append(location.getLocationName()).
                                append("(").append(location.getAcceptDate()).append(")");
                        busRoute.append(location.getBusRoute());
                    }
                }
                robotDocument.setHandleLocation(serviceLocation.toString());
                robotDocument.setBusRoute(busRoute.toString());
            }
            //申请材料
            List<SxServiceMaterial> sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(dbSxService.getServiceOid());
            StringBuilder serviceMaterial = new StringBuilder();
            if(null != sxServiceMaterials && sxServiceMaterials.size()>0){
                for (int i = 0; i < sxServiceMaterials.size(); i++) {
                    SxServiceMaterial material = sxServiceMaterials.get(i);
                    if (i > 0) {
                        serviceMaterial.append("</br>");
                    }
                    serviceMaterial.append(material.getMaterialName());
                }
                robotDocument.setServiceMaterial(serviceMaterial.toString());
            }
            robotDocument.setCreateDate(dbSxService.getCreateDate());
            robotDocument.setUpdateDate(dbSxService.getModifyDate());
            return robotDocument;
        }).collect(Collectors.toList());

        // 常见问题数据
        List<DbSxServiceQuestionWithBLOBs> sxServiceQuestionList = this.dbSxServiceQuestionMapper.queryDbSxServiceQuestionList(new DbSxServiceQuestionWithBLOBs());
        for (DbSxServiceQuestionWithBLOBs question : sxServiceQuestionList) {
            RobotDocument robotDocument = new RobotDocument();
            robotDocument.setModelType(MODEL_TYPE_2);
            robotDocument.setDocId(question.getQuestionOid());
            robotDocument.setServiceOid(question.getServiceOid());
            robotDocument.setAskTitle(question.getTitle());
            robotDocument.setAskAnswer(question.getAnswer());
            robotDocument.setCreateDate(question.getCreateDate());
            robotDocument.setUpdateDate(question.getModifyDate());
            robotDocumentList.add(robotDocument);
        }

        ElasticSearchUtil.insertDataBatch(ROBOT_INDEX_NAME, "docId", JSONArray.toJSONString(robotDocumentList));
    }

    /**
     * 关键字联想查询
     * @param kws 关键字
     * @param curSiteId 当前区划站点
     * @return 问答分页数据
     */
    public RobotPage<RobotDocument> associationSearch(String kws, String curSiteId) {
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(kws, "serviceName", "askTitle");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("serviceName").preTags("<span style='color:red'>").postTags("</span>");
        highlightBuilder.field("askTitle").preTags("<span style='color:red'>").postTags("</span>");

        PageResult<RobotDocument> pageResult = ElasticSearchUtil.searchDataPage(RobotDocument.class,
                queryBuilder, highlightBuilder, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, ROBOT_INDEX_NAME);
        return new RobotPage<>(pageResult.getData(), pageResult.getPageNum(),
                (int)pageResult.getTotal(), pageResult.getPageSize());
    }

    /**
     *
     * @param kws 关键字
     * @param curSiteId 当前区划站点
     * @param modelType 数据类型
     * @param pageNumber 页码
     * @return 问答分页数据
     */
    public RobotPage<RobotDocument> analysisSearch(String kws, String curSiteId, String modelType, Integer pageNumber) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        QueryBuilder kwsMatchQuery = QueryBuilders.multiMatchQuery(kws, "serviceName", "askTitle");
        queryBuilder.must(kwsMatchQuery);
        if (StrUtil.isNotEmpty(modelType)) {
            QueryBuilder modelTypeMatchQuery = QueryBuilders.matchQuery("modelType", modelType);
            queryBuilder.must(modelTypeMatchQuery);
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("serviceName").preTags("<span style='color:red'>").postTags("</span>");
        highlightBuilder.field("askTitle").preTags("<span style='color:red'>").postTags("</span>");

        // 查询分页数据
        PageResult<RobotDocument> pageResult = ElasticSearchUtil.searchDataPage(RobotDocument.class,
                queryBuilder, highlightBuilder, pageNumber, DEFAULT_PAGE_SIZE, ROBOT_INDEX_NAME);
        RobotPage<RobotDocument> robotPage = new RobotPage<>(pageResult.getData(),
                pageResult.getPageNum(), (int)pageResult.getTotal(), pageResult.getPageSize());

        // 查询扩展数据
        int total = ElasticSearchUtil.searchDataCount(kwsMatchQuery, ROBOT_INDEX_NAME);
        int serNum = ElasticSearchUtil.searchDataCount(QueryBuilders.boolQuery()
                .must(kwsMatchQuery)
                .must(QueryBuilders.matchQuery("modelType", MODEL_TYPE_1)), ROBOT_INDEX_NAME);
        int quesNum = ElasticSearchUtil.searchDataCount(QueryBuilders.boolQuery()
                .must(kwsMatchQuery)
                .must(QueryBuilders.matchQuery("modelType", MODEL_TYPE_2)), ROBOT_INDEX_NAME);
        robotPage.getExtraData().put("allNum", total);
        robotPage.getExtraData().put("quesNum", quesNum);
        robotPage.getExtraData().put("serNum", serNum);

        return robotPage;
    }

    /**
     * 根据id查询问答数据
     * @param docId 主键
     * @param modelType 数据类型
     * @return 单个数据
     */
    public RobotDocument searchDataById(String docId, String modelType) {
        return ElasticSearchUtil.searchDataById(RobotDocument.class, ROBOT_INDEX_NAME, docId);
    }

    /**
     * 常见问题查询
     * @return 数据列表
     */
    public List<RobotDocument> commonQuestion() {
        List<DbSxServiceQuestionWithBLOBs> sxServiceQuestionList = this.dbSxServiceQuestionMapper.queryDbSxServiceQuestionList(new DbSxServiceQuestionWithBLOBs());
        List<RobotDocument> robotDocumentList = new ArrayList<>();
        int i = 0;
        for (DbSxServiceQuestionWithBLOBs serviceQuestion : sxServiceQuestionList) {
            i++;
            if (i > 10) {
                break;
            }
            RobotDocument robotDocument = new RobotDocument();
            robotDocument.setDocId(serviceQuestion.getQuestionOid());
            robotDocument.setAskTitle(serviceQuestion.getTitle());
            robotDocumentList.add(robotDocument);
        }
        return robotDocumentList;
    }

}

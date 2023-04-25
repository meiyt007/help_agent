package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.vo.EsHaKnowledgeBase;
import com.zfsoft.ha.data.vo.EsHaMaterial;
import com.zfsoft.ha.front.EsHaKnowledgeBaseService;
import com.zfsoft.ha.managers.EsHaKnowledgeBaseManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class EsHaKnowledgeBaseController implements EsHaKnowledgeBaseService {

    @Resource
    private EsHaKnowledgeBaseManager esHaKnowledgeBaseManager;

    @Override
    public ApiResultSet<List<EsHaMaterial>> queryMaterialList(String keyword,Integer pageNumber,Integer pageSize){
        log.info("知识库(智能问答）：进入根据关键词获取es数据,入参:"+keyword);
        PageResult<EsHaMaterial> search = null;
        try {
            search = esHaKnowledgeBaseManager.searchMaterialList(keyword,pageNumber,pageSize);
            log.info("知识库(智能问答）：根据关键词获取es数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("知识库(智能问答）：根据关键词获取es数据失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"知识库(智能问答）：根据关键词获取es数据失败",e.getMessage());
        }
        return ApiResultSet.ok("知识库(智能问答）：根据关键词获取es数据成功",search);

    }
    @Override
    public ApiResultSet<List<EsHaKnowledgeBase>> query(String keyword){
        log.info("知识库(智能问答）：进入根据关键词获取es数据,入参:"+keyword);
        List<EsHaKnowledgeBase> search = new ArrayList<>();
        try {
//            int i = esHaKnowledgeBaseManager.importAll();
            search = esHaKnowledgeBaseManager.search(keyword);
            log.info("知识库(智能问答）：根据关键词获取es数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("知识库(智能问答）：根据关键词获取es数据失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"知识库(智能问答）：根据关键词获取es数据失败",e.getMessage());
        }
        return ApiResultSet.ok("知识库(智能问答）：根据关键词获取es数据成功",search);

    }

    /**
     * 知识库(智能问答）初始化
     * @return
     */
    @Override
    public ApiResultSet init(){
        log.info("知识库(智能问答）：进入初始化");
        try {
            esHaKnowledgeBaseManager.importAll();
            log.info("知识库(智能问答）：初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("知识库(智能问答）：初始化失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"知识库(智能问答）：初始化失败",e.getMessage());
        }
        return ApiResultSet.ok("知识库(智能问答）：初始化成功");
    }

    /**
     * 知识库(智能问答）初始化
     * @return
     */
    @Override
    public ApiResultSet initMaterial(){
        log.info("知识库(智能问答）材料：进入初始化");
        try {
            esHaKnowledgeBaseManager.importMaterialAll();
            log.info("知识库(智能问答）材料：初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("知识库(智能问答）材料：初始化失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"知识库(智能问答）材料：初始化失败",e.getMessage());
        }
        return ApiResultSet.ok("知识库(智能问答）材料：初始化成功");
    }
}

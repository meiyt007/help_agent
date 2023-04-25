package com.zfsoft.ha.front;


import com.zfsoft.ha.data.vo.EsHaKnowledgeBase;
import com.zfsoft.ha.data.vo.EsHaMaterial;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 知识库接口
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/21
 */
@RequestMapping("/ha/knowledge")
public interface EsHaKnowledgeBaseService {
    /**
     * 通过关键字获取es里面的相关信息
     * @param keyword
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialList",method = RequestMethod.GET)
    ApiResultSet<List<EsHaMaterial>> queryMaterialList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);
    /**
     * 通过关键字获取es里面的相关信息
     * @param keyword
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    ApiResultSet<List<EsHaKnowledgeBase>> query(@RequestParam(value = "keyword", required = true) String keyword);

    /**
     * 知识库初始化
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/init",method = RequestMethod.GET)
    ApiResultSet init();

    /**
     * 知识库材料初始化
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/initMaterial",method = RequestMethod.GET)
    ApiResultSet initMaterial();
}

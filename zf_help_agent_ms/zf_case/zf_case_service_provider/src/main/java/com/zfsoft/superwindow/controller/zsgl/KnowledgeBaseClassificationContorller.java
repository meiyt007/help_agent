package com.zfsoft.superwindow.controller.zsgl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zsgl.ZskDict;
import com.zfsoft.superwindow.manager.zsgl.KnowledgeBaseClassificationManager;
import com.zfsoft.superwindow.service.zsgl.KnowledgeBaseClassificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-10-28 14:03:28
 * @description: 知识库分类管理
 */
@Slf4j
@RestController
@RequestMapping(value = "/knowledgeBaseClassification")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseClassificationContorller implements KnowledgeBaseClassificationService {

    private final KnowledgeBaseClassificationManager knowledgeBaseClassificationManager;
    @Override
    @PostMapping(value = "/queryKnowledgeBaseClassificationTree")
    public ApiResultSet queryKnowledgeBaseClassificationTree(String parentOid) {
        List<ZskDict> zskDictVoList = this.knowledgeBaseClassificationManager.queryKnowledgeBaseClassificationTree(parentOid);
        log.info("获取知识库分类树调用成功结果为：{}", JSON.toJSONString(zskDictVoList));
        return new ApiResultSet(zskDictVoList);
    }

    @Override
    @PostMapping(value = "/pageList")
    public ApiResultSet pageList(ZskDict zskDict,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ZskDict> sysAreaSiteList = this.knowledgeBaseClassificationManager.queryList(zskDict);
        PageResult<ZskDict> pageResult = new PageResult<>(((Page) sysAreaSiteList).getPageNum(), ((Page) sysAreaSiteList).getPageSize(), ((Page) sysAreaSiteList).getTotal());
        pageResult.setData(sysAreaSiteList);
        log.info("获取知识库分类列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    @PostMapping(value = "/queryDictListByParentOid")
    public ApiResultSet queryDictListByParentOid(String parendOid) {
        List<ZskDict> sysAreaSiteList = this.knowledgeBaseClassificationManager.queryDictListByParentOid(parendOid);
        return new ApiResultSet(sysAreaSiteList);
    }

    @Override
    @PostMapping(value = "/saveOrUpdate")
    public ApiResultSet saveOrUpdate(@RequestBody @Validated ZskDict zskDict) {
        this.knowledgeBaseClassificationManager.saveOrUpdate(zskDict);
        log.info("受理辖区信息新增/更新成功：{}", JSON.toJSONString(zskDict));
        return new ApiResultSet(zskDict);
    }

    /*
     * @Description: 验证数据是否重复
     * @Author: liyanqing
     * @Date: 2021-03-16 18:01
     * @param code:
     * @param name:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @Override
    @GetMapping(value = "/validateRepeat")
    public ApiResultSet validateRepeat(String zskDictOid, String code, String name, String parentOid) {
        String info = this.knowledgeBaseClassificationManager.validateRepeat(zskDictOid, code, name, parentOid);
        return new ApiResultSet(info);
    }

    @Override
    @PostMapping(value = "/delete")
    public ApiResultSet delete(String ids) {
        this.knowledgeBaseClassificationManager.delete(ids);
        log.info("删除成功：{}", ids);
        return new ApiResultSet(ids);
    }

    @Override
    @PostMapping(value = "/getOne")
    public ApiResultSet getOne(String id) {
        ZskDict zskDict = this.knowledgeBaseClassificationManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(zskDict));
        return new ApiResultSet(zskDict);
    }

}

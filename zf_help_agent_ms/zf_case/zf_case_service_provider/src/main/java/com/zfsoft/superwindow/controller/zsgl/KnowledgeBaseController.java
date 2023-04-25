package com.zfsoft.superwindow.controller.zsgl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zsgl.KnowledgeBase;
import com.zfsoft.superwindow.manager.zsgl.KnowledgeBaseManager;
import com.zfsoft.superwindow.service.zsgl.KnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-10-29 13:32:42
 * @description: 知识库管理控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/knowledgeBase")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseController implements KnowledgeBaseService {

    private final KnowledgeBaseManager knowledgeBaseManager;

    @Override
    @PostMapping(value = "/pageList")
    public ApiResultSet queryPageList(KnowledgeBase knowledgeBase,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<KnowledgeBase> sysAreaSiteList = this.knowledgeBaseManager.queryList(knowledgeBase);
        PageResult<KnowledgeBase> pageResult = new PageResult<>(((Page) sysAreaSiteList).getPageNum(), ((Page) sysAreaSiteList).getPageSize(), ((Page) sysAreaSiteList).getTotal());
        pageResult.setData(sysAreaSiteList);
        log.info("获取知识库列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    @PostMapping(value = "/saveOrUpdate")
    public ApiResultSet saveOrUpdate(@RequestBody @Validated KnowledgeBase knowledgeBase) {
        this.knowledgeBaseManager.saveOrUpdate(knowledgeBase);
        log.info("知识库新增/更新成功：{}", JSON.toJSONString(knowledgeBase));
        return new ApiResultSet(knowledgeBase);
    }

    @Override
    @PostMapping(value = "/delete")
    public ApiResultSet delete(String ids) {
        this.knowledgeBaseManager.delete(ids);
        log.info("删除成功：{}", ids);
        return new ApiResultSet(ids);
    }

    @Override
    @PostMapping(value = "/getOne")
    public ApiResultSet getOne(String id) {
        KnowledgeBase knowledgeBase = this.knowledgeBaseManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(knowledgeBase));
        return new ApiResultSet(knowledgeBase);
    }

    /**
     * 检验是否有关联
     * @param zskDictOid
     * @return
     */

    @Override
    @GetMapping(value = "/checkIsExist")
    public ApiResultSet checkIsExist(String zskDictOid){
        String isExist="N";
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setZskDictOid(zskDictOid);
        List<KnowledgeBase> sysAreaSiteList = this.knowledgeBaseManager.queryList(knowledgeBase);
        if(sysAreaSiteList.size()>0){
            isExist="Y";
        }
        log.info("校验是否有关联接口调用程成功：{}", zskDictOid+"****"+isExist);
        return new ApiResultSet(isExist);
    }


}

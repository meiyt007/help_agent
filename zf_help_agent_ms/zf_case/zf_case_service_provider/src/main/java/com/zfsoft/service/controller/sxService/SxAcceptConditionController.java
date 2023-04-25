package com.zfsoft.service.controller.sxService;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxAcceptCondition;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxAcceptConditionManager;
import com.zfsoft.service.sxService.data.SxAcceptCondition;
import com.zfsoft.service.sxService.service.SxAcceptConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @ClassName SxAcceptConditionController
 * @Description 实施清单受理条件
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxAcceptConditionController implements SxAcceptConditionService {
    @Resource
    private SxAcceptConditionManager sxAcceptConditionManager;

    @Override
    public ApiResultSet<SxAcceptCondition> getSxAcceptConditionByOid(String conditionOid) {
        SxAcceptCondition sxAcceptCondition = sxAcceptConditionManager.getSxAcceptConditionByOid(conditionOid);
        ApiResultSet<SxAcceptCondition> apiResultSet = new ApiResultSet<SxAcceptCondition>();
        apiResultSet.setData(sxAcceptCondition);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxAcceptCondition>> getSxAcceptConditionListByServiceOid(String serviceOid) {
        List<SxAcceptCondition> conditionResult = sxAcceptConditionManager.getSxAcceptConditionByServiceOid(serviceOid);
        ApiResultSet<List<SxAcceptCondition>> apiResultSet = new ApiResultSet<List<SxAcceptCondition>>();
        apiResultSet.setData(conditionResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet pageList(SxAcceptCondition sxAcceptCondition,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbSxAcceptCondition> dbSxAcceptConditionList = sxAcceptConditionManager.queryList(sxAcceptCondition);
        PageResult<DbSxAcceptCondition> pageResult = new PageResult<>(
                ((Page) dbSxAcceptConditionList).getPageNum(),
                ((Page) dbSxAcceptConditionList).getPageSize(),
                ((Page) dbSxAcceptConditionList).getTotal()
        );
        pageResult.setData(dbSxAcceptConditionList);
        log.info("获取常见问题信息列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet saveOrUpdateSxAcceptCondition(SxAcceptCondition sxAcceptCondition) {
        sxAcceptConditionManager.saveOrUpdateSxAcceptCondition(sxAcceptCondition);
        return new ApiResultSet(sxAcceptCondition);
    }

    @Override
    public ApiResultSet delete(String id) {
        sxAcceptConditionManager.delete(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet getSxAcceptConditionById(String id) {
        DbSxAcceptCondition dbSxAcceptCondition = sxAcceptConditionManager.getDetail(id);
        log.info("查询成功：{}", id);
        return new ApiResultSet(dbSxAcceptCondition);
    }
}

package com.zfsoft.service.controller.sxService;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceQuestion;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceQuestionWithBLOBs;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxServiceQuestionManager;
import com.zfsoft.service.sxService.data.SxServiceQuestion;
import com.zfsoft.service.sxService.service.SxServiceQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @ClassName SxServiceQuestionController
 * @Description 实施清单常见问题
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxServiceQuestionController implements SxServiceQuestionService {
    @Resource
    private SxServiceQuestionManager sxServiceQuestionManager;
    @Override
    public ApiResultSet<SxServiceQuestion> getSxServiceQuestionByOid(String questionOid) {
        SxServiceQuestion sxServiceQuestion = sxServiceQuestionManager.getSxServiceQuestionByQuestionOid(questionOid);
        ApiResultSet<SxServiceQuestion> apiResultSet = new ApiResultSet<SxServiceQuestion>();
        apiResultSet.setData(sxServiceQuestion);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceQuestion>> getSxServiceQuestionListByServiceOid(String serviceOid) {
        List<SxServiceQuestion> sxServiceQuestionList = sxServiceQuestionManager.getSxServiceQuestionByServiceOid(serviceOid);
        ApiResultSet<List<SxServiceQuestion>> apiResultSet = new ApiResultSet<List<SxServiceQuestion>>();
        apiResultSet.setData(sxServiceQuestionList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet pageList(SxServiceQuestion sxServiceQuestion,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbSxServiceQuestionWithBLOBs> dbSxServiceQuestionWithBLOBsList = sxServiceQuestionManager.queryList(sxServiceQuestion);
        PageResult<DbSxServiceQuestionWithBLOBs> pageResult = new PageResult<>(
                ((Page) dbSxServiceQuestionWithBLOBsList).getPageNum(),
                ((Page) dbSxServiceQuestionWithBLOBsList).getPageSize(),
                ((Page) dbSxServiceQuestionWithBLOBsList).getTotal()
        );
        pageResult.setData(dbSxServiceQuestionWithBLOBsList);
        log.info("获取常见问题信息列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet saveOrUpdateSxServiceQuestion(SxServiceQuestion sxServiceQuestion) {
        this.sxServiceQuestionManager.saveOrUpdateSxServiceQuestion(sxServiceQuestion);
        return new ApiResultSet(sxServiceQuestion);
    }

    @Override
    public ApiResultSet delete(String id) {
        this.sxServiceQuestionManager.delete(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet getSxServiceQuestionById(String id) {
        DbSxServiceQuestion dbSxServiceQuestion = this.sxServiceQuestionManager.getDetail(id);
        log.info("查询成功：{}", id);
        return new ApiResultSet(dbSxServiceQuestion);
    }

    @Override
    public ApiResultSet enableQuestion(String id) {
        this.sxServiceQuestionManager.enableQuestion(id);
        log.info("启用成功：{}", id);
        return new ApiResultSet(id);
    }
}

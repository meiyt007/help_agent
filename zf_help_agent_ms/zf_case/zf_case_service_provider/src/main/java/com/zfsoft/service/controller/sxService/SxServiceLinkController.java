package com.zfsoft.service.controller.sxService;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLinkWithBLOBs;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxServiceLinkManager;
import com.zfsoft.service.sxService.data.SxServiceLink;
import com.zfsoft.service.sxService.service.SxServiceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @ClassName SxServiceLinkController
 * @Description 实施清单办理环节
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxServiceLinkController implements SxServiceLinkService {
    @Resource
    private SxServiceLinkManager sxServiceLinkManager;
    @Override
    public ApiResultSet<SxServiceLink> getSxServiceLinkByOid(String serviceLinkOid) {
        SxServiceLink  sxServiceLink = sxServiceLinkManager.getSxServiceLinkByOid(serviceLinkOid);
        ApiResultSet<SxServiceLink> apiResultSet = new ApiResultSet<SxServiceLink>();
        apiResultSet.setData(sxServiceLink);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceLink>> getSxServiceLinkListByServiceOid(String serviceOid) {
        List<SxServiceLink> sxServiceLinkList = sxServiceLinkManager.getSxServiceLinkByServiceOid(serviceOid);
        ApiResultSet<List<SxServiceLink>> apiResultSet = new ApiResultSet<List<SxServiceLink>>();
        apiResultSet.setData(sxServiceLinkList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet pageList(SxServiceLink sxServiceLink, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbSxServiceLinkWithBLOBs> dbSxServiceLinkWithBLOBsList = sxServiceLinkManager.queryList(sxServiceLink);
        PageResult<DbSxServiceLinkWithBLOBs> pageResult = new PageResult<>(
                ((Page) dbSxServiceLinkWithBLOBsList).getPageNum(),
                ((Page) dbSxServiceLinkWithBLOBsList).getPageSize(),
                ((Page) dbSxServiceLinkWithBLOBsList).getTotal()
        );
        pageResult.setData(dbSxServiceLinkWithBLOBsList);
        log.info("获取办理环节信息列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet saveOrUpdateSxServiceLink(SxServiceLink sxServiceLink) {
        this.sxServiceLinkManager.saveOrUpdateSxServiceLink(sxServiceLink);
        return new ApiResultSet(sxServiceLink);
    }

    @Override
    public ApiResultSet delete(String id) {
        this.sxServiceLinkManager.delete(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet getSxServiceQuestionById(String id) {
        DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = this.sxServiceLinkManager.getDetail(id);
        log.info("查询成功：{}", id);
        return new ApiResultSet(dbSxServiceLinkWithBLOBs);
    }
}

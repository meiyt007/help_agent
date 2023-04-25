package com.zfsoft.single.controller.sxpz;

import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.sxpz.InformPromise;
import com.zfsoft.single.data.sxpz.vo.InformPromiseVo;
import com.zfsoft.single.manager.sxpz.InformPromiseManager;
import com.zfsoft.single.service.sxpz.InformPromiseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 告知承诺清单管理
 * dongxiuli
 **/
@RestController
@Slf4j
public class InformPromiseController implements InformPromiseService {
    @Resource
    private InformPromiseManager informPromiseManager;

    @Override
    public ApiResultSet<PageResult<InformPromise>> queryPage(InformPromise informPromise, Integer pageNum, Integer pageSize) {
        PageResult<InformPromise> listInformPromise=informPromiseManager.queryPage(informPromise,pageNum,pageSize);
        log.info("获取告知承诺清单调用成功结果为：{}", JSON.toJSONString(listInformPromise));
        return new ApiResultSet(listInformPromise);
    }

    @Override
    public ApiResultSet saveOrUpdate(InformPromiseVo informPromise) {
        informPromiseManager.saveOrUpdate(informPromise);
        return new ApiResultSet(informPromise);
    }

    @Override
    public ApiResultSet deleteInformPromise(Long id) {
        informPromiseManager.deleteInform(id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet allInformServiceOids() {
       String str= informPromiseManager. allInformServiceOids();
        return new ApiResultSet(str);
    }

    @Override
    public ApiResultSet getInformByServiceOid(String serviceOid) {
        InformPromise promise= informPromiseManager.getInformByServiceOid(serviceOid);
        return new ApiResultSet(promise);
    }
}

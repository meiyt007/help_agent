package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SxServiceExtendManager;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.service.SxServiceExtendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @ClassName SxServiceExtendController
 * @Description 实施清单扩展控制类
 * @Author wangxl
 * @Date 2020-11-14
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxServiceExtendController implements SxServiceExtendService {

    @Resource
    private SxServiceExtendManager sxServiceExtendManager;

    /**
     * 获取事项主表信息
     * @param serviceOid 业务主键
     * @return
     */
    @Override
    public ApiResultSet<SxServiceExtend> getSxServiceExtendByServiceOid(String serviceOid) {
        SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
        ApiResultSet<SxServiceExtend> apiResultSet = new ApiResultSet<SxServiceExtend>();
        apiResultSet.setData(sxServiceExtend);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxServiceExtend> getSxServiceExtendByOid(String oid) {
        SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByOid(oid);
        ApiResultSet<SxServiceExtend> apiResultSet = new ApiResultSet<SxServiceExtend>();
        apiResultSet.setData(sxServiceExtend);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxServiceExtend> getSxServiceExtendByResultOid(String resultOid) {
        SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByResultOid(resultOid);
        ApiResultSet<SxServiceExtend> apiResultSet = new ApiResultSet<SxServiceExtend>();
        apiResultSet.setData(sxServiceExtend);
        return apiResultSet;
    }

}

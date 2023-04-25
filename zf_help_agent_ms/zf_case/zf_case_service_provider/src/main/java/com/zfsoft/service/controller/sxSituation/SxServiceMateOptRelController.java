package com.zfsoft.service.controller.sxSituation;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxSituation.SxServiceMateOptRelManager;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import com.zfsoft.service.sxSituation.service.SxServiceMateOptRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangns
 * @description 细化材料选项值关系
 * @date 2020/11/2 17:16
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceMateOptRelController implements SxServiceMateOptRelService {

    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;

    @Override
    public ApiResultSet<SxServiceMateOptRel> getSxServiceMateOptRelByOid(String oid) {

        SxServiceMateOptRel sxServiceMateOptRel = sxServiceMateOptRelManager.getSxServiceMateOptRelByService(oid);

        ApiResultSet<SxServiceMateOptRel> apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(sxServiceMateOptRel);

        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceMateOptRel>> getSxServiceMateOptRelsByOptionValOid(String valOid) {
       List<SxServiceMateOptRel> list= sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(valOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<String> saveOrUpdateMaterOptRel(String valOid, String materialOids) {
        sxServiceMateOptRelManager.saveOrUpdateMaterOptRel(valOid,materialOids);
        return new ApiResultSet<>(valOid);
    }

}

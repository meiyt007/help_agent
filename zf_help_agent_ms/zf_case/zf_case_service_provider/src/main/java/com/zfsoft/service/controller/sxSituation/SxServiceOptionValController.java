package com.zfsoft.service.controller.sxSituation;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.service.SxServiceOptionValService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangns
 * @description 选项值表
 * @date 2020/11/3 11:41
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceOptionValController implements SxServiceOptionValService {

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;

    /**
     * @param oid 业务主键
     * @description: 根据选项值业务主健获取选项值信息
     * @author: wangns
     * @Date: 2020/11/3 11:09
     **/
    @Override
    public ApiResultSet<SxServiceOptionVal> getSxServiceOptionValByOid(String oid) {

        SxServiceOptionVal sxServiceOptionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(oid);

        ApiResultSet<SxServiceOptionVal> apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(sxServiceOptionVal);

        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> getSxServiceOptionTitleValStr(String oid) {
        Map<String, String> map = null;
        SxServiceOptionVal sxServiceOptionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(oid);
        if(sxServiceOptionVal !=null){
            map = new HashMap<String, String>();
            map.put("valueName",sxServiceOptionVal.getName());
            SxServiceOptionTitle title = sxServiceOptionTitleManager.getServiceOptionTitleByOid(sxServiceOptionVal.getTitleOid());
            if(title != null){
                map.put("titleName",title.getName());
            }
        }
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }
}

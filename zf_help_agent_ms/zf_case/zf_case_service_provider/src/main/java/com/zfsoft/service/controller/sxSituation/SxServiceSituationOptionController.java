package com.zfsoft.service.controller.sxSituation;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxSituation.SxServiceSituationOptionManager;
import com.zfsoft.service.sxSituation.data.SxServiceSituationOption;
import com.zfsoft.service.sxSituation.service.SxServiceSituationOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wangns
 * @description
 * @date 2020/11/3 13:31
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceSituationOptionController implements SxServiceSituationOptionService {

    @Resource
    private SxServiceSituationOptionManager sxServiceSituationOptionManager;

    /**
     * @param situationOid
     * @description: 根据情形、选项关系业务主健获取情形、选项关系信息
     * @author: wangns
     * @Date: 2020/11/3 13:24
     **/
    @Override
    public ApiResultSet getSxServiceSituationOptionBySituationOid(String situationOid) {

        SxServiceSituationOption sxServiceSituationOption = sxServiceSituationOptionManager.getSxServiceSituationOptionBySituationOid(situationOid);

        ApiResultSet apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(sxServiceSituationOption);

        return apiResultSet;

    }

    /**
     * 根据 情形业务主键 获取情形下的 所有选项和选项值
     *
     * @param situationOid
     * @return
     */
    @Override
    public ApiResultSet<Map<String, Object>> getOptionTitleAndValsOfSituation(String situationOid) {
        //返回结果集
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object> returnMap = sxServiceSituationOptionManager.getOptionTitleAndValsOfSituation(situationOid);
        apiResultSet.setData(returnMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveSxServiceSituations(List<SxServiceSituationOption> sxServiceSituationOptions) {
        int result = sxServiceSituationOptionManager.saveSxServiceSituations(sxServiceSituationOptions);
        if(result>0){
            return new ApiResultSet("保存成功");
        }else{
            ApiResultSet re = new ApiResultSet("保存失败");
            re.setCode(ApiResultSet.UNKNOWN_ERROR);
            return  re;
        }
    }

}

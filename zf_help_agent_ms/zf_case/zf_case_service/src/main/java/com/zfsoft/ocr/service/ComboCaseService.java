package com.zfsoft.ocr.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: ComboCaseService
 * @description: 一件事办件基本信息服务定义接口
 * @author: liangss
 * @date: 2021/04/21
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/comboCaseService")
public interface ComboCaseService {

    @ProcessFeignCalledResult
    @RequestMapping( value = "/selectComboCaseListByTypePage",method = {RequestMethod.GET})
    ApiResultSet selectComboCaseListByTypePage(
            @RequestParam(value="comboDireOid",required = false) String comboDireOid,
            @RequestParam(value="caseType",required = false) String caseType,
            @RequestParam(value="themeOid",required = false) String themeOid,
            @RequestParam(value="startDate",required = false) String startDate,
            @RequestParam(value="endDate",required = false) String endDate,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

}

package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 卡证目录识别管理接口
 */
@RequestMapping("/cardCatalogueElement")
public interface CardCatalogueElementService {

    /***
    * @Description: 根据卡证目录id查询目录元素列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [cardCatalogueOid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCardCatalogueElementList", method = {RequestMethod.GET})
    ApiResultSet  getCardCatalogueElementList(@RequestParam(value = "cardCatalogueOid", required = false) String cardCatalogueOid);

}


package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.CardCatalogue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 卡证目录识别管理接口
 */
@RequestMapping("/cardCatalogue")
public interface CardCatalogueService {
    /***
    * @Description:  获取卡证目录分页列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [cardCatalogueName, pageNum, pageSize]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryCardCatalogueWithPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryCardCatalogueWithPage(@RequestParam(value = "cardCatalogueName", required = false) String cardCatalogueName,
                                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize);
   /***
   * @Description:  更新或保存卡证目录
   * @Author:liangss
   * @Date:2021/11/5
   * @Param: [cardCatalogue]
   */
   @ProcessFeignCalledResult
   @RequestMapping(value = "/saveOrUpdateCatalogue", method = {RequestMethod.POST})
   ApiResultSet saveOrUpdateCatalogue(@RequestBody CardCatalogue cardCatalogue) throws Exception;

    /***
    * @Description:根据oid查询卡证目录信息
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [oid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCardCatalogueByOid", method = {RequestMethod.GET})
    ApiResultSet getCardCatalogueByOid(@RequestParam(value = "oid", required = false) String oid);

    /***
    * @Description: 根据oid删除卡证目录信息
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: [oid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteByOid", method = {RequestMethod.GET})
    ApiResultSet deleteByOid(@RequestParam(value = "oid", required = false) String oid);

    /***
    * @Description: 查询所有卡证列表
    * @Author:liangss
    * @Date:2021/11/5
    * @Param: []
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getAllCardCatalogueList", method = {RequestMethod.GET})
    ApiResultSet  getAllCardCatalogueList();
}


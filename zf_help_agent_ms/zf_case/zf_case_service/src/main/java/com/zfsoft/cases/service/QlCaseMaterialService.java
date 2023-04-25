package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @（#）: QlCaseMaterialService
 * @description: 办件材料信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlCaseMaterialService")
public interface QlCaseMaterialService {

    /**
     * 保存办件材料信息
     *
     * @param qlCaseMaterialList
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseMaterial>
     * @author wangwg
     * @date 2020/10/24
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveQlCaseMaterial", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> saveQlCaseMaterial(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

    /**
     * 办件业务主键获取办件材料
     *
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseMaterial>
     * @author wangwg
     * @date 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterial>> queryQlCaseMaterialByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * 办件材料业务主键获取办件材料信息
     *
     * @param caseMaterialOid 办件材料业务主键
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseMaterial>
     * @author wangwg
     * @date 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQlCaseMaterialByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseMaterial> getQlCaseMaterialByCaseMaterialOid(String caseMaterialOid);
    /**
     * 根据办件主键查询容缺后补材料
     * @param caseOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryRqhbMaterialByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterial>> queryRqhbMaterialByCaseOid(@RequestParam("caseOid") String caseOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseMaterial> queryMaterialByCaseMaterialOid(@RequestParam("caseMaterialOid") String caseMaterialOid);


    /**
     * 根据办件业务主键和事项材料业务主键查询已保存的办件材料
     * @param caseOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialByOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseMaterial> queryQlCaseMaterialByOid(@RequestParam("caseOid") String caseOid, @RequestParam("materialOid") String materialOid);



    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateQlCaseMaterialList", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialList(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);


    /**
    * @Description:  更新办件信息
    * @Author:liangss
    * @Date:2021/7/28
    * @Param: [QlCaseMaterial]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateQlCaseMaterial", method = {RequestMethod.POST})
    ApiResultSet updateQlCaseMaterial(@RequestBody QlCaseMaterial QlCaseMaterial);

    /**
     * 根据办件主键查询告知承诺材料
     * @param caseOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryCngzMaterialByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterial>> queryCngzMaterialByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * 根据办件主键查询材料不包含材料附件
     * @param caseOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialByCaseOidNotAttaFile", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterial>> queryMaterialByCaseOidNotAttaFile(@RequestParam("caseOid") String caseOid);


    /**
     * 通过billOid获取数据
     *
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseMaterial>
     * @author wangwg
     * @date 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialByBillOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterial>> queryQlCaseMaterialByBillOid(@RequestParam("caseOid") String caseOid,
                                                                    @RequestParam("billOid") String billOid);

    /**
     * @description 查询办件材料信息
     * @param caseOid
     * @author meiyt 
     * @date 2022/6/1
     * @return 
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * @description 网站端更新材料关联
     * @param qlCaseMaterialList
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateQlCaseMaterialListForWeb", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

    /**
     * @description 窗口端获取材料列表（新）
     * @param caseOid
     * @author meiyt
     * @date 2022/6/17
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOidForZC", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOidForZC(@RequestParam("caseOid") String caseOid);
}

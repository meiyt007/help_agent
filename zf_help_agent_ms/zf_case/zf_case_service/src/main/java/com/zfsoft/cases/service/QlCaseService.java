package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.cases.data.vo.SxFormInfoVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @（#）: QlCaseService
 * @description: 办件基本信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlCaseService")
public interface QlCaseService {


    /**
     * 办件业务编码, 机构编码获取办件基本信息
     *
     * @param caseNumber 办件业务编码
     * @param organOid 机构主键
     * @param pageNum
     * @param pageSize
     * @author wangwg
     * @date 2020/10/26
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.QlCase>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseWithPage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCaseWithPage(@RequestParam(value = "caseNumber") String caseNumber,
                                                         @RequestParam(value = "organOid") String organOid,
                                                         @RequestParam(value = "pageNum") Integer pageNum,
                                                         @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 通过事项获取办件基本信息
     *
     * @param serviceOid 事项主键
     * @param serviceType 事项类型
     * @param pageNum
     * @param pageSize
     * @author wangwg
     * @date 2020/10/26
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.QlCase>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseWithPageByService",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCaseWithPageByService(@RequestParam(value = "serviceOid") String serviceOid,
                                                                  @RequestParam(value = "serviceType") String serviceType,
                                                                  @RequestParam(value = "pageNum") Integer pageNum,
                                                                  @RequestParam(value = "pageSize") Integer pageSize);


    /**
     * 办件业务编码, 机构编码获取办件基本信息
     *
     * @param packageCaseOid 套餐主键
     * @param pageNum
     * @param pageSize
     * @author wangwg
     * @date 2020/10/26
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.QlCase>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseWithPageByPackage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCaseWithPageByPackage(@RequestParam(value = "packageCaseOid") String packageCaseOid,
                                                                  @RequestParam(value = "pageNum") Integer pageNum,
                                                                  @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 已办办件列表
     *
     * @param caseNumber 办件业务编码
     * @param organOid 机构主键
     * @param pageNum
     * @param pageSize
     * @author wangwg
     * @date 2020/10/26
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.QlCase>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryDoneQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryDoneQlCasePage(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                         @RequestParam(value = "organOid", required = false) String organOid,
                                                         @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                         @RequestParam(value = "caseStatus", required = false) Integer caseStatus,
                                                         @RequestParam(value = "startDate", required = false) String startDate,
                                                         @RequestParam(value = "endDate", required = false) String endDate,
                                                         @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                         @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                         @RequestParam(value = "credentialNumber", required = false) String credentialNumber,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * 暂存办件列表
     *
     * @param caseNumber 办件业务编码
     * @param pageNum
     * @param pageSize
     * @author wangwg
     * @date 2020/11/29
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.QlCase>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryTemporaryQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryTemporaryQlCasePage(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                              @RequestParam(value = "projectName", required = false) String projectName,
                                                              @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                              @RequestParam(value = "serviceType", required = false) String serviceType,
                                                              @RequestParam(value = "startDate", required = false) String startDate,
                                                              @RequestParam(value = "endDate", required = false) String endDate,
                                                              @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                              @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                              @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryRqhbQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryRqhbQlCasePage(@RequestParam(value = "caseOid", required = false) String caseOid,
                                                         @RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                         @RequestParam(value = "organOid", required = false) String organOid,
                                                         @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                         @RequestParam(value = "projectName", required = false) String projectName,
                                                         @RequestParam(value = "caseStatus", required = false) Integer caseStatus,
                                                         @RequestParam(value = "startDate", required = false) String startDate,
                                                         @RequestParam(value = "endDate", required = false) String endDate,
                                                         @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                         @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCasePage(@RequestParam(value = "caseOid", required = false) String caseOid,
                                                     @RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                     @RequestParam(value = "organOid", required = false) String organOid,
                                                     @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                     @RequestParam(value = "caseStatus", required = false) Integer caseStatus,
                                                     @RequestParam(value = "startDate", required = false) String startDate,
                                                     @RequestParam(value = "endDate", required = false) String endDate,
                                                     @RequestParam(value = "sourceType", required = false) String sourceType,
                                                     @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                     @RequestParam(value = "dataAuthor", required = false) Integer dataAuthor,
                                                     @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                     @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryKstbQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryKstbQlCasePage(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                         @RequestParam(value = "organOid", required = false) String organOid,
                                                         @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                         @RequestParam(value = "caseStatus", required = false) Integer caseStatus,
                                                         @RequestParam(value = "startDate", required = false) String startDate,
                                                         @RequestParam(value = "endDate", required = false) String endDate,
                                                         @RequestParam(value = "sourceType", required = false) String sourceType,
                                                         @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                         @RequestParam(value = "dataAuthor", required = false) Integer dataAuthor,
                                                         @RequestParam(value = "loginUserOid", required = false) String loginUserOid,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryWebQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryWebQlCasePage(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                        @RequestParam(value = "credentialNumber", required = false) String credentialNumber,
                                                        @RequestParam(value = "organOid", required = false) String organOid,
                                                        @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                        @RequestParam(value = "caseStatus", required = false) Integer caseStatus,
                                                        @RequestParam(value = "startDate", required = false) String startDate,
                                                        @RequestParam(value = "endDate", required = false) String endDate,
                                                        @RequestParam(value = "sourceType", required = false) String sourceType,
                                                        @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                        @RequestParam(value = "dataAuthor", required = false) Integer dataAuthor,
                                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/selectQlCaseSuperviseList",method = {RequestMethod.POST})
    ApiResultSet<PageResult<QlCase>> selectQlCaseSuperviseList(@RequestBody QlCaseVo doneCaseVo,
                                                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * 办件下一步暂存基本信息
     *
     * @param object
     * @author wangwg
     * @date 2020/10/23
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/nextStepSaveQlCase", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> nextStepSaveQlCase(@RequestBody Object object);


    /**
     * 办件基本信息保存
     *
     * @param qlCase
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.vo.QlCaseVo>
     * @author wangwg
     * @date 2020/10/23
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveQlCase", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveQlCase(@RequestBody QlCase qlCase);


    /**
     * 办件更新表单信息
     * @param qlCase
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.vo.QlCaseVo>
     * @author wangwg
     * @date 2021/08/03
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateFormInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> updateQlCaseFormInfo(@RequestBody QlCase qlCase);



    /**
     * 办件预审
     *
     * @param caseOid
     * @param caseStatus
     * @author wangwg
     * @date 2020/10/23
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/yuShenQlCase", method = {RequestMethod.GET})
    ApiResultSet<Map<String, String>> yuShenQlCase(@RequestParam(value = "caseOid") String caseOid,
                                                   @RequestParam(value = "caseStatus") Integer caseStatus);


    /**
     * 根据caseOid的办件信息
     *
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author wangwg
     * @date 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<QlCase> queryQlCaseByCaseOid(@RequestParam("caseOid") String caseOid);


    /**
     * 根据caseNumber的办件信息
     *
     * @param caseNumber
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author wangwg
     * @date 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseByCaseNumber", method = {RequestMethod.GET})
    ApiResultSet<QlCase> queryQlCaseByCaseNumber(@RequestParam("caseNumber") String caseNumber);



    /**
     * 删除指定caseOid的办件信息
     *
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author wangwg
     * @date 2020/10/24
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteQlCaseByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<String> deleteQlCaseByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * 删除指定caseNuber的办件信息
     *
     * @author wangwg
     * @date 2020/10/24
     * @param caseNumber
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteQlCaseByCaseNumber", method = {RequestMethod.GET})
    ApiResultSet<String> deleteQlCaseByCaseNumber(@RequestParam("caseNumber") String caseNumber);

    /**
     * 获取登录用户信息
     *
     * @author wangwg
     * @date 2020/11/19
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getLoginUser", method = {RequestMethod.GET})
    ApiResultSet<Map<String,String>> getLoginUser();

    /**
     * 办件受理
     *
     * @author wangwg
     * @date 2020/11/19
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveCaseAccpet", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveCaseAccpet(@RequestParam("caseOid") String caseOid
            , @RequestParam("userOid") String userOid, @RequestParam("userName") String userName
            , @RequestParam("finalOpinion") String finalOpinion
            , @RequestParam("finalOpinionDesc") String finalOpinionDesc
            , @RequestParam(value = "rqbzDueDate", required = false) String rqbzDueDate
            , @RequestParam(value = "sjStatus", required = false) Integer sjStatus) throws Exception;



    /**
     * 生成办件编号
     *
     * @author wangwg
     * @date 2020/11/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/createCaseNumber", method = {RequestMethod.GET})
    ApiResultSet<Map<String,String>> createCaseNumber(@RequestParam("serviceOid") String serviceOid);


    /**
     * 根据caseOid获取已保存办件的基本信息 申请信息 扩展信息
     *
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<Map<String,String>>
     * @author wangwg
     * @date 2020/10/24
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getAllQlCaseByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String,Object>> getAllQlCaseByCaseOid(@RequestParam("caseOid") String caseOid);


    /**
     * 根据packageCaseOid获取已保存办件的基本信息
     *
     * @param packageCaseOid
     * @author wangwg
     * @date 2020/12/07
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQlCaseByPackageCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCase>> getQlCaseByPackageCaseOid(@RequestParam("packageCaseOid") String packageCaseOid);

    /**
     * 根据packageCaseOid和serviceOid获取已保存办件的基本信息
     *
     * @param packageCaseOid
     * @author wangwg
     * @date 2021/03/31
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQlCaseByOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCase>> getQlCaseByOid(@RequestParam("packageCaseOid") String packageCaseOid,
                                              @RequestParam("serviceOid") String serviceOid);


    /**
     * 办件作废
     *
     * @author wangwg
     * @date 2020/12/08
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveCaseInvalid", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveCaseInvalid(@RequestParam("caseOid") String caseOid, @RequestParam("invalidReason") String invalidReason, @RequestParam(value = "caseStatus", required = false) Integer caseStatus);


    /**
     * 办件业务编码, 机构编码获取办件基本信息-暂存受理
     *
     * @param caseNumber 办件业务编码
     * @param startDate  开始时间
     * @param endDate 结束时间
     * @param projectName  项目名
     * @param applyUserName  申请人
     * @param serviceType  事项类型
     * @param pageNum
     * @param pageSize
     * @author chenjm
     * @date 2020/12/15
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.QlCase>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseWithPageForZcsl",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCaseWithPageForZcsl(@RequestParam(value = "caseNumber") String caseNumber,
                                                                @RequestParam(value = "startDate") String startDate,
                                                                @RequestParam(value = "endDate") String endDate,
                                                                @RequestParam(value = "projectName") String projectName,
                                                                @RequestParam(value = "applyUserName") String applyUserName,
                                                                @RequestParam(value = "serviceType") String serviceType,
                                                                @RequestParam(value = "pageNum") Integer pageNum,
                                                                @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 更新办件状态
     * @param caseOid
     * @param caseStatus
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateQlCase", method = {RequestMethod.POST})
    ApiResultSet updateQlCase(@RequestParam(value = "caseOid") String caseOid,
                              @RequestParam(value = "caseStatus") Integer caseStatus);

    /**
     *获取所有已经超期的荣却后补办件
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOverDueAllCase", method = {RequestMethod.GET})
    ApiResultSet<List<QlCase>> getOverDueAllCase();

    /**
     * 根据packageCaseOid获取一件事所属子办件
     *
     * @param caseOid
     * @author wangwg
     * @date 2021/04/14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseFinishListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCase>> queryQlCaseFinishListByCaseOid(@RequestParam("caseOid") String caseOid);



    /**
     * 根据packageCaseOid获取一件事管理办件
     *
     * @param qlCaseVo
     * @author wangwg
     * @date 2021/05/25
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseListByPackageCaseOid", method = {RequestMethod.POST})
    ApiResultSet<List<QlCase>> queryQlCaseListByPackageCaseOid(@RequestBody QlCaseVo qlCaseVo);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFzOnthingCase", method = {RequestMethod.GET})
    ApiResultSet<List<QlCase>> getFzOnthingCase(@RequestParam("packageCaseOid") String packageCaseOid);

    /**
     * 根据证件号查询办件和申请人信息
     * @param credentialNumber
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseByCredentialNumber",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCaseByCredentialNumber(@RequestParam(value = "credentialNumber") String credentialNumber,
                                                                   @RequestParam(value = "applyUserType") String applyUserType,
                                                                   @RequestParam(value = "projectName", required = false) String projectName,
                                                                   @RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                                   @RequestParam(value = "pageNum") Integer pageNum,
                                                                   @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 统计当前登录人受理量，个人登记量、法人登记量
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCaseTjInfo", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Integer>> getCaseTjInfo(@RequestParam(value = "userOid", required = false) String userOid);

    /**
     * 工作台预审办件
     * @param serviceOids 当前登录人授权事项
     * @return
     */

    @ProcessFeignCalledResult
    @RequestMapping( value = "/taskYsCase",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> taskYsCase(@RequestParam(value = "serviceOids") List<String> serviceOids, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 工作台容缺补正办件
     * @param serviceOids 当前登录人授权事项
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/taskRqhbCaseList",method = {RequestMethod.GET})
    ApiResultSet taskRqhbCaseList(@RequestParam(value = "serviceOids") List<String> serviceOids);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/taskRqhbCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> taskRqhbCasePage(@RequestParam(value = "serviceOids") List<String> serviceOids, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize);
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseInfo",method = {RequestMethod.GET})
    ApiResultSet queryQlCaseInfo(@RequestParam(value = "queueNum") String queueNum,
                                 @RequestParam(value = "date") String date);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFormInfoByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfoVo>> queryFormInfoByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     *  网站端保存基本信息
     * @param object
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/webSaveQlCase", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> browserSaveQlCase(@RequestBody Object object);

    /**
     * 网站办件关联更新表单信息
     * @param qlCase
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/webUpdateFormInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> browserUpdateFormInfo(@RequestBody QlCase qlCase);


    /**
     * @description 网站端历史办件列表
     * @param
     * @author meiyt
     * @date 2022/6/10
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryDoneQlCasePageForWeb",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryDoneQlCasePageForBrowers(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                                   @RequestParam(value = "serviceName", required = false) String serviceName,
                                                                   @RequestParam(value = "netUserId", required = false) String netUserId,
                                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 删除指定caseOid的暂存办件信息
     *
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author wangyg
     * @date 2022/6/20
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteZcQlCaseByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<String> deleteZcQlCaseByCaseOid(@RequestParam("caseOid") String caseOid);
}

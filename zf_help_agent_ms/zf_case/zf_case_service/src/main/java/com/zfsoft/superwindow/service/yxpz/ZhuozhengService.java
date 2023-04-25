package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName SxDocuTemplateService
 * @Description 文书模板服务定义接口
 * @Author liangxm
 * @Date 2020-11-1 11:33
 * @Version V1.0
 **/
@RequestMapping("/manage/zhuozheng")
public interface ZhuozhengService {

    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDocuTemplateFilePath",method = {RequestMethod.GET})
    ApiResultSet<String> getDocuTemplateFilePath(@RequestParam(value = "appCode", required = false) String appCode,
                                                 @RequestParam(value = "docTypeCode", required = false) String docTypeCode);

    @RequestMapping(value="/word", method= RequestMethod.GET)
    public ModelAndView showWord(Map<String, Object> map, @RequestParam(value = "oid", required = false) String oid);


    @RequestMapping( value = "/save",method = RequestMethod.POST)
    public void saveFile();

    /**
     * 材料流转通知单打印
     * @param request
     * @param caseOid
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value="/initTickertape", method= RequestMethod.GET)
    public ModelAndView initTickertape(HttpServletRequest request, @RequestParam(value = "caseOid", required = false) String caseOid,
                                       @RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "map", required = false) Map<String, Object> map);

    /**
     * 不予受理通知书打印
     * @param
     * @param
     * @param map
     * @return
     */
    @RequestMapping(value="/notAcceptedNoticePrint")
    public ModelAndView notAcceptedNoticePrint(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                               @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                               @RequestParam(value = "sqTime", required = false) String sqTime,
                                               Map<String, Object> map);


    /**
     * 容缺补正受理通知书打印
     * @param
     * @param map
     * @return
     */
    @RequestMapping(value="/rqbzAcceptedNoticePrint", method= RequestMethod.GET)
    public ModelAndView rqbzAcceptedNoticePrint(@RequestParam(value = "caseName", required = false) String caseName,
                                                @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                @RequestParam(value = "sqTime", required = false) String sqTime,
                                                Map<String, Object> map);


    /**
     * 补正补齐承诺告知书
     * @param
     * @param
     * @param map
     * @return
     */
    @RequestMapping(value="/cngzNoticePrint")
    public ModelAndView cngzNoticePrint(@RequestParam(value = "caseName", required = false) String caseName,
                                        @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                        @RequestParam(value = "sqTime", required = false) String sqTime,
                                        Map<String, Object> map);


    /**
     * 一件事容缺受理通知书打印
     * @author wangwg
     * @date  2020-01-26
     * @param map
     * @return
     */
    @RequestMapping(value="/printComboCaseRqhbNotice")
    public ModelAndView printComboCaseRqhbNotice(Map<String, Object> map,
                                                 @RequestParam(value = "rqhbTime", required = false) String rqhbTime,
                                                 @RequestParam(value = "sqrName", required = false) String sqrName,
                                                 @RequestParam(value = "caseName", required = false) String caseName);

    /**
     * 一件事通不予受理知书打印
     * @author wangwg
     * @date  2020-01-26
     * @param map
     * @return
     */
    @RequestMapping(value="/printComboCaseNotAcceptNotice")
    public ModelAndView printComboCaseNotAcceptNotice(Map<String, Object> map,
                                                      @RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                      @RequestParam(value = "sqrName", required = false) String sqrName,
                                                      @RequestParam(value = "sqTime", required = false) String sqTime);

    @RequestMapping(value="/printGrdzqybg")
    public ModelAndView printGrdzqybg(Map<String, Object> map, @RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * 受理通知书
     * @param object
     * @param map
     * @return
     */
    @RequestMapping(value="/acceptedNoticePrint")
    public ModelAndView acceptedNoticePrint(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                            @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                            @RequestParam(value = "sqTime", required = false) String sqTime,
                                            Map<String, Object> map);

    /**
     * 利用pageoffice打开文件
     * @param fileUrl 文件路径
     * @param map
     * @return
     */
    @RequestMapping(value="/pageOfficePrint")
    public ModelAndView pageOfficePrint(@RequestParam(value = "fileUrl", required = false) String fileUrl, Map<String, Object> map);


}

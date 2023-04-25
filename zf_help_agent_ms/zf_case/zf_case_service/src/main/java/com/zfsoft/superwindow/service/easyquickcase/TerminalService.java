package com.zfsoft.superwindow.service.easyquickcase;

import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.yxpz.GuidSpeech;
import com.zfsoft.superwindow.service.easyquickcase.data.CasePrintCertificateDto;
import com.zfsoft.superwindow.service.easyquickcase.data.ReqPersonAuthInfoDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description 自助端接口
 * @author wangyg
 * @date 2022/6/16
 * @return
 */
@RequestMapping("/terminalServer")
public interface TerminalService {

    /**
     * @description 用户登录
     * @param reqPersonAuthInfoDto
     * @author wangyg
     * @date 2022/6/16
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    ApiResultSet<String> login(@RequestBody ReqPersonAuthInfoDto reqPersonAuthInfoDto);

    /**
     * @description 用户退出登录
     * @param request
     * @author wangyg
     * @date 2022/6/16
     * @return
     */
    @RequestMapping(value = "/loginOut", method = {RequestMethod.GET})
    ApiResultSet<String> loginOut(@ApiIgnore HttpServletRequest request);

    /**
     * @description 根据code获取语音详细信息
     * @param guidSpeechCode
     * @author wangyg
     * @date 2022/6/16
     * @return
     */
    @RequestMapping( value = "/selectByGuidSpeechCode",method = {RequestMethod.GET})
    ApiResultSet<GuidSpeech> selectByGuidSpeechCode(@RequestParam(value = "guidSpeechCode") String guidSpeechCode);

    /**
     * @description 初始化打印办件凭证
     * @param caseNumber
     * @author wangyg
     * @date 2022/6/20
     * @return
     */
    @RequestMapping( value = "/initCasePrintCertificate",method = {RequestMethod.GET})
    ApiResultSet<CasePrintCertificateDto> initCasePrintCertificate(@RequestParam(value = "caseNumber") String caseNumber);

    /**
     * @description 获取区划信息
     * @param
     * @author wangyg
     * @date 2022/6/20
     * @return
     **/
    @RequestMapping(value = "/getDistListByParentOid", method = RequestMethod.GET)
    ApiResultSet<List<SysDistrict>> getDistListByParentOid(@RequestParam(value = "parentOid", required = false) String parentOid);

    /**
     * @description 获取事项办理地点
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/24
     * @return
     */
    @RequestMapping( value = "/getLocationInfo",method = {RequestMethod.GET})
    ApiResultSet getLocationInfoByServiceOid(@RequestParam(value = "serviceOid") String serviceOid,
                                             @RequestParam(value = "terminalCode") String terminalCode);

    /**
     * @description 营业执照ocr识别
     * @param picBase64
     * @author wangyg
     * @date 2022/7/8
     * @return
     */
    @RequestMapping( value = "/discernBusiness",method = {RequestMethod.POST})
    ApiResultSet discernBusiness(@RequestParam(value = "picBase64") String picBase64);
}

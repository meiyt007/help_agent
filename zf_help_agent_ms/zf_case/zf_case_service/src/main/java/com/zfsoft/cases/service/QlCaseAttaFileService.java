package com.zfsoft.cases.service;


import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @（#）: QlCaseMaterialAttaController
 * @description: 材料附件信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlcaseAtta")
public interface QlCaseAttaFileService {

    /**
     * @description 获取二维码uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getPhoneQrScanRandom",method = {RequestMethod.GET})
    ApiResultSet<String> getPhoneQrScanUuid();

    /**
     * @description 获取扫码上传状态
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getPhoneUploadStatus",method = {RequestMethod.GET})
    ApiResultSet<String> getPhoneUploadStatus(@RequestParam(value = "uuid", required = false) String uuid);

    /**
     * @description 初始化上传信息
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/initPhoneUpload",method = {RequestMethod.POST})
    ApiResultSet<String> initPhoneUpload(@RequestParam(value = "uuid", required = false) String uuid,
                                         @RequestParam(value = "caseMaterialOid", required = false) String caseMaterialOid);

    /**
     * @description 检测上传状态
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/checkPhoneUploadStatus",method = {RequestMethod.GET})
    ApiResultSet<Boolean> checkPhoneUploadStatus(@RequestParam(value = "uuid", required = false) String uuid);

    /**
     * @description 保存上传信息
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveTempPhoneUploadAtta",method = {RequestMethod.POST})
    ApiResultSet saveTempPhoneUploadAtta(@RequestParam(value = "uuid", required = false) String uuid,
                                         @RequestParam(value = "attaOids", required = false) String attaOids);

    /**
     * @description 获取上传信息
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getTempPhoneUploadAtta",method = {RequestMethod.GET})
    ApiResultSet<List<QlSysAtta>> getTempPhoneUploadAtta(@RequestParam(value = "uuid", required = false) String uuid);

    /**
     * @description 上传办件材料附件
     * @param request
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/uploadCaseMaterialFile",method = {RequestMethod.POST}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers = "content-type=multipart/form-data")
    ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request,
                                                         @RequestPart(value = "files", required = false) MultipartFile[] files);
}

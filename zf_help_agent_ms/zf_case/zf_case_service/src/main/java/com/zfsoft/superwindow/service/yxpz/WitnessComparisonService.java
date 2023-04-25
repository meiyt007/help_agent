package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.WitnessComparison;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 人证比对表
 */
@RequestMapping("/witnessComparison")
public interface WitnessComparisonService {
    /**
     保存人证比对信息
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet<WitnessComparison> saveOrUpdate(@RequestBody WitnessComparison witnessComparison);

    @ProcessFeignCalledResult
    @PostMapping(value = "/uploadCompareFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet<List<SysAtta>> uploadCompareFile(HttpServletRequest request, MultipartFile[] files) throws Exception;

    @RequestMapping(value="/queryWitnessComparisonByCaseOid", method= RequestMethod.GET)
    public ApiResultSet queryWitnessComparisonByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * 从已保存的人证比对信息中获取牌照图片
     * @autor wangwg
     * @date 2021/10/20
     * @param caseOid
     * @return
     */
    @RequestMapping(value="/queryBase64ImgWitnessComparisonByCaseOid", method= RequestMethod.GET)
    public ApiResultSet queryBase64ImgWitnessComparisonByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);



}

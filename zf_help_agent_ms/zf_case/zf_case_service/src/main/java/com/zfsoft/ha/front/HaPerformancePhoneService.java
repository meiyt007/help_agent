package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaPerformancePhoneRecord;
import com.zfsoft.ha.data.requestData.HaPerPhoneRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description 帮代办人员电话绩效接口
 * @Author: zhaobf
 * @Date: 2023/3/27 13:43
 */
@RequestMapping("/ha/perPhone")
public interface HaPerformancePhoneService {
    /**
     * @description:  查询帮代办人员电话绩效分页信息列表
     * @author: zhaobf
     * @Date: 2023/3/27 14:04:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryPerPhoneWithPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaPerformancePhoneRecord>> queryPerPhoneWithPage(
            HaPerPhoneRequestData haPerPhoneRequestData) throws Exception;

    /**
     * 导入电话绩效文件
     * @param request
     * @param files
     * @return
     */
    @PostMapping(value = "/ImportPhoneExcel")
    ApiResultSet<Map<String, String>> ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files);

}

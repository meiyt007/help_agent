package com.zfsoft.ha.manager;

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
 * @Date: 2023/3/22 13:43
 */
@RequestMapping("/work/perPhone")
public interface HaPerformancePhoneManagerService {
    /**
     * @description:  查询帮代办人员电话绩效分页信息列表
     * @author: zhaobf
     * @Date: 2023/3/22 14:04:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryPerPhoneWithPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaPerformancePhoneRecord>> queryPerPhoneWithPage(
            HaPerPhoneRequestData haPerPhoneRequestData) throws Exception;

    /**
     * @description:  删除帮代办人员电话绩效信息
     * @param id 主键
     * @author: zhaobf
     * @Date: 2023/3/22
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deletePerPhoneById", method = {RequestMethod.GET})
    ApiResultSet deletePerPhoneById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description: 新增或者修改帮代办人员电话绩效信息
     * @param phoneRecord 帮代办人员电话绩效实体类
     * @author: zhaobf
     * @Date: 2023/3/22
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdatePerPhone",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdatePerPhone(@RequestBody HaPerformancePhoneRecord phoneRecord) throws Exception;

    /**
     * @description:  根据id查询帮代办人员电话绩效信息
     * @param id
     * @author: zhaobf
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getPerPhoneById",method = {RequestMethod.GET})
    ApiResultSet getPerPhoneById(@RequestParam("id") Long id) throws Exception;

    /**
     * 导入电话绩效文件
     * @param request
     * @param files
     * @return
     */
    @PostMapping(value = "/ImportPhoneExcel")
    ApiResultSet<Map<String, String>> ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files);

}

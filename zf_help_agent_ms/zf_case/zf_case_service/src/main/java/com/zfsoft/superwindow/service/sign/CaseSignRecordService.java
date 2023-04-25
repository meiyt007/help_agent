package com.zfsoft.superwindow.service.sign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.wgpj.CaseSignRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 办件签名记录表服务接口
 *
 * @author wangwg
 * @date 2021-08-16
 */
@RequestMapping("/signRecord")
public interface CaseSignRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param id
     * @return 实例对象
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryById", method = {RequestMethod.GET})
    CaseSignRecord queryById(@RequestParam(value = "id", required = false) Long id);


    /**
     * 保存数据
     *
     * @param CaseSignRecord 实例对象
     * @return 实例对象
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveSignRecord", method = {RequestMethod.POST})
    ApiResultSet<Map<String,String>> saveSignRecord(@RequestBody CaseSignRecord CaseSignRecord);


    /**
     * 获取签名图片
     *
     * @param caseOid String
     * @return String
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySignImgPath", method = {RequestMethod.GET})
    ApiResultSet querySignImgPath(@RequestParam(value = "caseOid", required = false) String caseOid);
}

package com.zfsoft.single.service.ywbl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.ywbl.vo.SignImageVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @（#）: CaseSignService
 * @description: 办件签名记录接口
 * @author: wangwg
 * @date: 2021/8/17
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping(value = "/caseSign")
public interface CaseSignService {

    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateSignImg",method = {RequestMethod.POST})
    ApiResultSet updateSignImg(@RequestBody SignImageVo signImageVo);
}

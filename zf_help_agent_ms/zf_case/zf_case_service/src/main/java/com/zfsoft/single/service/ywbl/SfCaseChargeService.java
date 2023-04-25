package com.zfsoft.single.service.ywbl;

import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.ywbl.SfCaseCharge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * @（#）: SfCaseChargeService
 * @description: 办件收费
 * @author: wangwg
 * @date: 2021/06/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping(value = "/sfCaseCharge")
public interface SfCaseChargeService {

    /**
     * 查询办件收费列表
     *
     * @author: wangwg
     * @date: 2021/06/10
     */
    @PostMapping(value = "/pageList")
    ApiResultSet<PageResult<SfCaseCharge>> queryPageList(QlCaseVo qlCaseVo, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);


}

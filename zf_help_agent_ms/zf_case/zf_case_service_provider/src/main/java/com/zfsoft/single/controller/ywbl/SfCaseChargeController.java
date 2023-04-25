package com.zfsoft.single.controller.ywbl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.ywbl.SfCaseCharge;
import com.zfsoft.single.manager.ywbl.SfCaseChargeManager;
import com.zfsoft.single.service.ywbl.SfCaseChargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @（#）: SxServiceChargeController
 * @description: 办件收费控制类
 * @author: wangwg
 * @date: 2021/6/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@RestController
public class SfCaseChargeController implements SfCaseChargeService {

    @Resource
   private SfCaseChargeManager sfCaseChargeManager;


    @Override
    public ApiResultSet<PageResult<SfCaseCharge>> queryPageList(QlCaseVo qlCaseVo, Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<SfCaseCharge> caseChargeList = sfCaseChargeManager.queryPageList(qlCaseVo);
        PageResult<SfCaseCharge> pageResult = new PageResult<>(((Page) caseChargeList).getPageNum(), ((Page) caseChargeList).getPageSize(), ((Page) caseChargeList).getTotal());
        pageResult.setData(caseChargeList);
        return null;
    }
}

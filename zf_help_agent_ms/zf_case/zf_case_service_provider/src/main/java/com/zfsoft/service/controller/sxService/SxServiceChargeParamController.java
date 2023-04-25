package com.zfsoft.service.controller.sxService;

import com.zfsoft.service.manager.sxService.SxServiceChargeParamManager;
import com.zfsoft.service.sxService.service.SxServiceChargeParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @（#）: SxServiceChargeParamController
 * @description: 事项收费费用价格区间信息控制类
 * @author: wangwg
 * @date: 2021/6/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceChargeParamController implements SxServiceChargeParamService {

    @Resource
    private SxServiceChargeParamManager sxServiceChargeParamManager;
}

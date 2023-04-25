package com.zfsoft.service.manager.sxService;

import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceChargeParamMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @（#）: SxServiceChargeParamManager
 * @description: 事项收费费用价格区间信息实现类
 * @author: wangwg
 * @date: 2021/06/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceChargeParamManager {

    @Resource
    private DbSxServiceChargeParamMapper dbSxServiceChargeParamMapper;
}

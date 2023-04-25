package com.zfsoft.service.controller.sxService;

import cn.hutool.core.collection.CollUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceExtendMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceExtendWithBLOBs;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SxConditionalRulesManager;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRulesDto;
import com.zfsoft.service.sxConditionalRules.service.SxConditionalRulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName SxConditionalRulesController
 * @Description 条件预检配置controller
 * @Author WangKe
 * @Date 2022-05-24
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxConditionalRulesController implements SxConditionalRulesService {

    @Resource
    private SxConditionalRulesManager sxConditionalRulesManager;

    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;

    @Override
    public ApiResultSet saveOrUpdateSxConditionalRules(SxConditionalRulesDto sxConditionalRulesDto) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        List<SxConditionalRules> rulesList = sxConditionalRulesDto.getTableData();
        sxConditionalRulesManager.savaOrUpdateConditionalRules(rulesList);
        if (CollUtil.isNotEmpty(rulesList)) {
            SxConditionalRules sxConditionalRules = rulesList.get(0);
            String serviceOid = sxConditionalRules.getServiceOid();
            String ruleType = sxConditionalRules.getRuleType();
            // 当为秒批规则时，存入文案
            if (!"1".equals(ruleType)) {
                //根据事项主键查询扩展信息
                DbSxServiceExtendWithBLOBs extend = dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(serviceOid);
                //获取扩展信息
                if (null != extend) {
                    extend.setModifyDate(new Date());
                    extend.setMpPass(sxConditionalRulesDto.getMpPass());
                    extend.setMpNoPass(sxConditionalRulesDto.getMpNoPass());
                    dbSxServiceExtendMapper.updateByPrimaryKeyWithBLOBs(extend);
                } else {
                    extend = new DbSxServiceExtendWithBLOBs();
                    extend.setExtendOid(UUID.randomUUID().toString().replace("-", ""));
                    extend.setServiceOid(serviceOid);
                    extend.setModifyDate(new Date());
                    extend.setMpPass(sxConditionalRulesDto.getMpPass());
                    extend.setMpNoPass(sxConditionalRulesDto.getMpNoPass());
                    dbSxServiceExtendMapper.insert(extend);
                }
            }
        }
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxConditionalRulesDto> queryAllConditionalRules(SxConditionalRules cond) {
        List<SxConditionalRules> rulesList = sxConditionalRulesManager.queryAllSxConditionalRules(cond);
        SxConditionalRulesDto sxConditionalRulesDto = new SxConditionalRulesDto();
        sxConditionalRulesDto.setTableData(rulesList);
        DbSxServiceExtendWithBLOBs extend = dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(cond.getServiceOid());
        sxConditionalRulesDto.setMpPass(extend.getMpPass());
        sxConditionalRulesDto.setMpNoPass(extend.getMpNoPass());
        ApiResultSet<SxConditionalRulesDto> listApiResultSet = new ApiResultSet<>(sxConditionalRulesDto);
        return listApiResultSet;
    }

    @Override
    public ApiResultSet<SxConditionalRules> queryRulesByCodeAndServiceOid(SxConditionalRules rules) {
        ApiResultSet<SxConditionalRules> apiResultSet = new ApiResultSet<>();
        SxConditionalRules sxConditionalRules = sxConditionalRulesManager.selectByRules(rules);
        if(sxConditionalRules != null){
            apiResultSet.setData(sxConditionalRules);
            apiResultSet.setCode(200);
        }else{
            apiResultSet.setMessage("没有查询到相关配置信息！");
            apiResultSet.setCode(500);
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet delete(String id) {
        sxConditionalRulesManager.delete(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }
}

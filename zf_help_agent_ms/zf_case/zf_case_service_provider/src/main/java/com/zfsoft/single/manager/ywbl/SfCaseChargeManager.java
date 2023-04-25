package com.zfsoft.single.manager.ywbl;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.single.data.ywbl.SfCaseCharge;
import com.zfsoft.single.dbaccess.dao.ywbl.DbSfCaseChargeMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbSfCaseCharge;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @（#）: SfCaseChargeManager
 * @description: 办件收费实现类
 * @author: wangwg
 * @date: 2021/06/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@Service
public class SfCaseChargeManager  {

    @Resource
    private DbSfCaseChargeMapper dbSfCaseChargeMapper;

    public List<SfCaseCharge> queryPageList(QlCaseVo qlCaseVo) {
        Map<String,String> map = new HashMap<String,String>();
        if(null!=qlCaseVo){
            if(StrUtil.isNotEmpty(qlCaseVo.getCaseNumber())){
                map.put("caseNumber",qlCaseVo.getCaseNumber());
            }
            if(StrUtil.isNotEmpty(qlCaseVo.getApplyUserName())){
                map.put("applyUserName",qlCaseVo.getApplyUserName());
            }
            if(StrUtil.isNotEmpty(qlCaseVo.getStartDate())){
                map.put("startDate", DateUtil.startDateFormat(qlCaseVo.getStartDate()));
            }
            if(StrUtil.isNotEmpty(qlCaseVo.getEndDate())){
                map.put("endDate",DateUtil.endDateFormat(qlCaseVo.getEndDate()));
            }
        }
        List<DbSfCaseCharge> dbSfCaseCharges=dbSfCaseChargeMapper.querySfCaseChargePageList(map);
        return BeanUtils.copyListProperties(dbSfCaseCharges, SfCaseCharge::new);
    }
}

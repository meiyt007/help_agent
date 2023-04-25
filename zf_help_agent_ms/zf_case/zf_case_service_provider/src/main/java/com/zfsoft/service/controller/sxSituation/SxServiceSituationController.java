package com.zfsoft.service.controller.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxSituation.SxServiceSituationManager;
import com.zfsoft.service.sxSituation.data.SxServiceSituation;
import com.zfsoft.service.sxSituation.service.SxServiceSituationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangns
 * @description 事项情形表
 * @date 2020/11/3 11:59
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceSituationController implements SxServiceSituationService {

    @Resource
    private SxServiceSituationManager sxServiceSituationManager;

    /**
     * @param oid 业务主键
     * @description: 根据事项情形业务主健获取事项情形信息
     * @author: wangns
     * @Date: 2020/11/3 11:09
     **/
    @Override
    public ApiResultSet<SxServiceSituation> getSxServiceSituationByOid(String oid) {

        SxServiceSituation sxServiceSituation = sxServiceSituationManager.getSxServiceSituationByOid(oid);

        ApiResultSet<SxServiceSituation> apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(sxServiceSituation);

        return apiResultSet;

    }

    @Override
    public ApiResultSet<List<SxServiceSituation>> getSxServiceHotSituations(String serviceOid) {
        List<SxServiceSituation> sxServiceSituations = null;
        if (StringUtils.isNotEmpty(serviceOid)) {
            //事项情形
            sxServiceSituations = sxServiceSituationManager.getSxServiceSituationsByServiceOid(serviceOid);
        }
        ApiResultSet<List<SxServiceSituation>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sxServiceSituations);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<SxServiceSituation>> queryxHotSituationsPag(String situationName, String serviceOid, Integer pageNum, Integer pageSize) {

        ApiResultSet<PageResult<SxServiceSituation>> list=  sxServiceSituationManager.querySituations(situationName,serviceOid,pageNum,pageSize);

        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet saveSxServiceSituation(SxServiceSituation serviceMaterial) {
        Long sort = serviceMaterial.getSort();
        if(sort!=null&&sort.toString().length()>10){
            return new ApiResultSet(50001,"排序字段最大长度为10位，请检查输入","too long");
        }

        boolean flag= sxServiceSituationManager.saveSxServiceSituation(serviceMaterial);
       if(flag) {
           return new ApiResultSet("操作成功");
       }
       return new ApiResultSet(400,"该情形名称已经存在，请重新输入情形名称","exist");
    }

    @Override
    public ApiResultSet delSxServiceSituationByOid(String oid) {
        sxServiceSituationManager.delSxServiceSituationByOid(oid);
        return new ApiResultSet("删除成功");
    }

    @Override
    public ApiResultSet getSituationOptionListByServiceOid(String serviceOid) {
        if(StrUtil.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        List<SxServiceSituation> sxServiceSituations = sxServiceSituationManager.getSxServiceSituationsByServiceOid(serviceOid);
        return new ApiResultSet(sxServiceSituations);
    }
}

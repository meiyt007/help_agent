package com.zfsoft.service.controller.sxService;

import cn.hutool.core.util.IdUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SxServicePrecheckManager;
import com.zfsoft.service.sxService.data.SxServicePrecheck;
import com.zfsoft.service.sxService.service.SxServicePrecheckService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 前置事项核验信息接口
 * @author chenyq
 * @date 20220324
 */
@RestController
@Slf4j
public class SxServicePrecheckController implements SxServicePrecheckService {

    @Resource
    private SxServicePrecheckManager sxServicePrecheckManager;

    @Override
    @ApiOperation(value ="querySxServicePrecheckList" ,notes = "查询前置核验列表")
    public ApiResultSet querySxServicePrecheckList(SxServicePrecheck sxServicePrecheck) {
        List<SxServicePrecheck> list = sxServicePrecheckManager.querySxServicePrecheckList(sxServicePrecheck);
        return new ApiResultSet(list);
    }

    @Override
    @ApiOperation(value ="getSxServicePrecheck" ,notes = "查看前置核验详情")
    public ApiResultSet getSxServicePrecheck(String precheckOid) {
        SxServicePrecheck sxServicePrecheck = sxServicePrecheckManager.getSxServicePrecheck(precheckOid);
        return new ApiResultSet(sxServicePrecheck);
    }

    @Override
    @ApiOperation(value ="saveOrUpdate" ,notes = "保存前置核验")
    public ApiResultSet saveOrUpdate(SxServicePrecheck sxServicePrecheck) {
        int index = 0;
        if(StringUtils.isEmpty(sxServicePrecheck.getPrecheckOid())){
            sxServicePrecheck.setDelFlag(0);
            sxServicePrecheck.setCreateDate(new Date());
            sxServicePrecheck.setPrecheckOid(IdUtil.simpleUUID());
            index = sxServicePrecheckManager.save(sxServicePrecheck);
        }else{
            sxServicePrecheck.setDelFlag(0);
            index = sxServicePrecheckManager.update(sxServicePrecheck);
        }
        return new ApiResultSet(index);
    }
    @ApiOperation(value ="saveOrUpdateList" ,notes = "保存前置核验列表")
    public ApiResultSet saveOrUpdateList(List<SxServicePrecheck> sxServicePrechecks){
        if(sxServicePrechecks.size()>0){
            //不管是新增还是修改 都将原来的数据删除掉 然后重新入库
            sxServicePrecheckManager.delete(sxServicePrechecks.get(0).getServiceOid());
            //批量删除当前事项下的前置核验数据，根据事项主键
            sxServicePrechecks.forEach(sxServicePrecheck -> {
                sxServicePrecheck.setId(null);
                sxServicePrecheck.setDelFlag(0);
                sxServicePrecheck.setCreateDate(new Date());
                if(StringUtils.isEmpty(sxServicePrecheck.getPrecheckOid())){
                    sxServicePrecheck.setPrecheckOid(IdUtil.simpleUUID());
                }
                sxServicePrecheckManager.save(sxServicePrecheck);
            });
        }
        return new ApiResultSet();
    }

    @ApiOperation(value ="delete" ,notes = "删除前置核验")
    @Override
    public ApiResultSet delete(String precheckOid) {
        SxServicePrecheck sxServicePrecheck = new SxServicePrecheck(precheckOid,1);
        int index = sxServicePrecheckManager.update(sxServicePrecheck);
        if(index>0){
            return new ApiResultSet(index);
        }else{
            return null;
        }
    }
}

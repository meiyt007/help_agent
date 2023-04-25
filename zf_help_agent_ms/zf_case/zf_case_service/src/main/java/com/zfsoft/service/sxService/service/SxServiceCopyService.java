package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName SxServiceCopyService
 * @Description 实施清单环节信息服务一键复制接口
 * @Author chenyq
 * @Date 2022-03-22
 * @Version V1.0
 **/
@RequestMapping("/affair/sxServiceCopy")
public interface SxServiceCopyService {
    /**
     * @description:  根据事项主键和事项名称、事项实施编码复制事项信息
     * @param sxService 事项数据
     * @author: chenyq
     * @Date 2022-03-22
     **/
    @ProcessFeignCalledResult
    @PostMapping( value = "/copySxService")
    ApiResultSet  copySxService(@RequestBody SxService sxService);

    /**
     * 复制颗粒化材料
     * @param materialOidOld 原材料主键
     * @param materialOidNew 新材料主键
     * @param comboDirectoryOid
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping( value = "/comboRefinedInfo")
    ApiResultSet<Map>  comboRefinedInfo(@RequestParam(value = "materialOidOld") String materialOidOld
            , @RequestParam(value = "materialOidNew") String materialOidNew
            , @RequestParam(value = "comboDirectoryOid") String comboDirectoryOid);
}

package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wangns
 * @description 细化材料选项值关系定义接口
 * @date 2020/11/2 17:11
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceMateOptRel")
public interface SxServiceMateOptRelService {

    /**
     * @description:  根据实细化材料选项值关系业务主健获取细化材料选项值关系消息
     * @param oid 业务主键
     * @author: wangns
     * @Date: 2020/11/2 17:11
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMateOptRelByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceMateOptRel> getSxServiceMateOptRelByOid(@PathVariable("oid") String oid);

    /**
     * 根据选项值主键查询关联材料
     * @param valOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMateOptRelsByOptionValOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceMateOptRel>> getSxServiceMateOptRelsByOptionValOid(@RequestParam("valOid") String valOid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateMaterOptRel",method = {RequestMethod.POST})
    ApiResultSet<String> saveOrUpdateMaterOptRel(@RequestParam("valOid") String valOid, @RequestParam("materialOids") String materialOids);


}

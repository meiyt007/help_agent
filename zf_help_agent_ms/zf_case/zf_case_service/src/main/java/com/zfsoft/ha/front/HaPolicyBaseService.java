package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaPolicyBase;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/20 13:25
 */
@RequestMapping(value ="/ha/policy")
public interface HaPolicyBaseService {
    /**
     * Description: 查询帮政策库分页信息列表
     * @param name 名称（模糊查询）
     * @param title 主题
     * @param organId 区划id
     * @param pageNumber 分页参数，页码
     * @param pageSize 页记录数
     * @author zhaobf
     * date: 2023/3/20 13:23
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryPolicyBaseWithPage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaPolicyBase>> queryPolicyBaseWithPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "organId", required = false) String organId,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @param id 主键
     * @return ApiResultSet
     * @description: 删除政策库信息
     * @author: zhaobf
     * @Date: 2023/3/20
     **/
    @ProcessFeignCalledResult
    @GetMapping(value = "/deletePolicyById")
    ApiResultSet deletePolicyById( @RequestParam(value = "id") Long id) throws Exception;

    /**
     * @description: 新增或者修改帮代办人员小组(组内分组）信息
     * @param haPolicyBase 帮代办人员小组(组内分组）实体类
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取新增或者修改帮代办人员小组(组内分组）信息标识
     **/
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdatePolicyBase")
    ApiResultSet saveOrUpdatePolicyBase(@RequestBody HaPolicyBase haPolicyBase) throws Exception;

    /**
     * @description:  根据id查询帮代办人员小组(组内分组）信息
     * @param id
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取根据id查询帮代办人员小组(组内分组）信息
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getPolicyBaseById")
    ApiResultSet getPolicyBaseById(@RequestParam(value = "id") Long id) throws Exception;

    /**
     * 获取机构分组列表
     * @author wangyh
     * @Date: 2020/9/09 14:14
     */
    @RequestMapping(value = "/querySysOrganWithPage", method = {RequestMethod.GET})
    ApiResultSet querySysOrganWithPage(String districtOid);

}

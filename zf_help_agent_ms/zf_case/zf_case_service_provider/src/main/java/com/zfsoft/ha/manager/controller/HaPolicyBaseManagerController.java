package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaPolicyBase;
import com.zfsoft.ha.manager.HaPolicyBaseManagerService;
import com.zfsoft.ha.managers.HaPolicyBaseManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description 帮代办人员小组(组内分组）表控制层
 * @Author: 
 * @Date: 2023/3/20 13:58
 */
@RestController
@Slf4j
public class HaPolicyBaseManagerController implements HaPolicyBaseManagerService {
    /**
     * 帮代办人员小组(组内分组）表实现层
     */
    @Resource
    private HaPolicyBaseManager haPolicyBaseManager;
    /**
     * Description: 查询帮政策库分页信息列表
     * @param name 名称（模糊查询）
     * @param title 主题
     * @param organId 区划id* @param pageNumber 分页参数，页码
     * @param pageSize 页记录数
     * @author zhaobf
     * date: 2023/3/20 13:23
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @Override
    public ApiResultSet<PageResult<HaPolicyBase>> queryPolicyBaseWithPage(String name,String title,String organId, Integer pageNumber, Integer pageSize) throws Exception {
        log.info("查询帮代办人员小组(组内分组）分页信息列表， name:{},deleteStatus:{},pageNumber:{},pageSize:{}",name,title,organId,pageNumber,pageSize);
        PageResult<HaPolicyBase> pageResult = haPolicyBaseManager.queryPolicyBaseWithPage(name,title,organId,pageNumber,pageSize);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<HaPolicyBase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @param id 主键
     * @return ApiResultSet
     * @description: 删除政策库信息
     * @author: zhaobf
     * @Date: 2023/3/20
     **/
    @Override
    public ApiResultSet deletePolicyById(Long id) throws Exception {
        log.info("删除帮代办人员小组(组内分组）信息， id:{}",id);
        Integer index = haPolicyBaseManager.deletePolicyById(id);
        if (index == 1) {
            return ApiResultSet.ok("帮代办人员小组(组内分组）信息删除成功");
        } else {
            return new ApiResultSet(500, "帮代办人员小组(组内分组）信息删除失败");
        }
    }

    /**
     * @description: 新增或者修改帮代办人员小组(组内分组）信息
     * @param haPolicyBase 帮代办人员小组(组内分组）实体类
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取新增或者修改帮代办人员小组(组内分组）信息标识
     **/
    @Override
    public ApiResultSet saveOrUpdatePolicyBase(HaPolicyBase haPolicyBase) throws Exception {
        log.info("新增或者修改帮代办人员小组(组内分组）信息， haWorkGroup:{}",haPolicyBase);
        Map<String, Object> map = haPolicyBaseManager.saveOrUpdatePolicyBase(haPolicyBase);
        int index = (int) map.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @description:  根据id查询帮代办人员小组(组内分组）信息
     * @param id
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取根据id查询帮代办人员小组(组内分组）信息
     */
    @Override
    public ApiResultSet getPolicyBaseById(Long id) throws Exception {
        log.info("根据id查询帮代办人员小组(组内分组）信息， id:{}",id);
        HaPolicyBase haPolicyBase = haPolicyBaseManager.selectByid(id);
        return ApiResultSet.ok("接口调用成功", haPolicyBase);
    }

}

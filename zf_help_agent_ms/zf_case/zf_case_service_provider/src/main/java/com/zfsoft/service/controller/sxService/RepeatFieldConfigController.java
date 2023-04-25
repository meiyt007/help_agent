package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.RepeatFieldConfigManager;
import com.zfsoft.service.sxService.data.RepeatFieldConfig;
import com.zfsoft.service.sxService.service.RepeatFieldConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName RepeatFieldConfigController
 * @Description 字段重复去重Controller
 * @Author xiayj
 * @Date 2021/7/20 13:55
 **/

@Slf4j
@RestController
public class RepeatFieldConfigController implements RepeatFieldConfigService {

    @Resource
    private RepeatFieldConfigManager repeatFieldConfigManager;


    /**
     * @param isAble
     * @param pageNum
     * @param pageSize
     * @description: 查询字段重复配置的信息列表
     * @author: xiayj
     * @Date: 2021/7/19 16:14
     */
    @Override
    public ApiResultSet<PageResult<RepeatFieldConfig>> queryRepeatFieldWithPage(Integer isAble, String masterField, Integer pageNum, Integer pageSize) {
        PageResult<RepeatFieldConfig> pageResult = repeatFieldConfigManager.queryRepeatFieldWithPage(isAble, masterField, pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @param configOid
     * @description: 获取重复字段配置详细信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.repeatField.RepeatField>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @Override
    public ApiResultSet<RepeatFieldConfig> getRepeatFieldConfig(String configOid) {
        RepeatFieldConfig repeatFieldConfig = repeatFieldConfigManager.getRepeatFieldConfig(configOid);
        return new ApiResultSet<>(repeatFieldConfig);
    }

    /**
     * @param configOid
     * @description: 删除重复字段配置信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.Integer>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @Override
    public ApiResultSet<String> deleteRepeatField(String configOid) {
        repeatFieldConfigManager.deleteRepeatFieldConfig(configOid);
        return new ApiResultSet<>();
    }

    /**
     * @param configOid
     * @description: 启用/禁用重复字段配置。
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.Integer>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @Override
    public ApiResultSet<String> ableRepeatFieldByOid(String configOid) {
        repeatFieldConfigManager.isAbleRepeatFieldConfig(configOid);
        return new ApiResultSet<>();
    }

    /**
     * @param repeatFieldConfig
     * @description: 保存重复字段配置
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @Override
    public ApiResultSet<String> saveRepeatFieldConfig(RepeatFieldConfig repeatFieldConfig) {
        repeatFieldConfigManager.saveOrUpdateRepeatFieldConfig(repeatFieldConfig);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<Map<String, String>> queryConfigMapInfo() {
        Map<String, String> map = repeatFieldConfigManager.queryConfigMapInfo();
        return new ApiResultSet<>(map);
    }
}

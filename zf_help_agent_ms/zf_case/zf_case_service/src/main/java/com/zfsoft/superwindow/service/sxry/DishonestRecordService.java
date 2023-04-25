package com.zfsoft.superwindow.service.sxry;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.sxry.DishonestRecord;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname DishonestRecordService
 * @Description 失信记录接口
 * @Date 2021-01-11 10:11
 * @Created by liyaniqng
 */
@RequestMapping("/dishonestRecord")
public interface DishonestRecordService {

    /*
     * @Description: 失信记录列表
     * @Author: liyanqing
     * @Date: 2021-01-11 10:12
     * @param dishonestPerson:
     * @param pageNum:
     * @param pageSize:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @PostMapping(value = "/pageList")
    @ProcessFeignCalledResult
    ApiResultSet queryPageList(DishonestRecord dishonestRecord,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /*
     * @Description: 保存失信记录信息
     * @Author: liyanqing
     * @Date: 2021-01-11 14:25
     * @param null:
     * @return: null
     **/
    @PostMapping(value = "/saveOrUpdate")
    @ProcessFeignCalledResult
    ApiResultSet saveOrUpdate(@RequestBody @Validated DishonestRecord dishonestRecord);

    /*
     * @Description: 删除失信记录
     * @Author: liyanqing
     * @Date: 2021-01-11 15:24
     * @param id:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @PostMapping(value = "/delete/{id}")
    @ProcessFeignCalledResult
    ApiResultSet delete(@PathVariable(value = "id", required = false) Long id);

    /*
     * @Description: 获取失信记录
     * @Author: liyanqing
     * @Date: 2021-01-11 15:24
     * @param id:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @GetMapping(value = "/getOne/{id}")
    @ProcessFeignCalledResult
    ApiResultSet getOne(@PathVariable(value = "id", required = false) Long id);

}

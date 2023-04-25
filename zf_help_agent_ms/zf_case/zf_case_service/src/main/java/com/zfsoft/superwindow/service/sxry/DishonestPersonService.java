package com.zfsoft.superwindow.service.sxry;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.sxry.DishonestPerson;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname DishonestPersonService
 * @Description 失信人员接口
 * @Date 2021-01-11 10:11
 * @Created by Loafer
 */
@RequestMapping("/dishonestPerson")
public interface DishonestPersonService {

    /**
     * @Description: 失信人员执行列表
     * @Author: liyanqing
     * @Date: 2021-01-11 10:12
     * @param dishonestPerson:
     * @param pageNum:
     * @param pageSize:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @RequestMapping(value = "/pageList", method = {RequestMethod.POST})
    ApiResultSet queryPageList(DishonestPerson dishonestPerson,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * @Description: 获取失信人执行信息
     * @Author: liyanqing
     * @Date: 2021-01-11 14:24
     * @param id:
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.tcbl.DishonestPerson>
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne/{id}", method = {RequestMethod.GET})
    ApiResultSet<DishonestPerson>  getOne(@PathVariable(value = "id", required = false) Long id);

    /**
     * @Description: 保存执行人信息
     * @Author: liyanqing
     * @Date: 2021-01-11 14:25
     * @param dishonestPerson
     * @return: null
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody @Validated DishonestPerson dishonestPerson);

    /**
     * @Description: 执行人信息 撤销
     * @Author: liyanqing
     * @Date: 2021-01-11 15:24
     * @param dishonestPerson:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @RequestMapping(value = "/dishonestCancel", method = {RequestMethod.POST})
    ApiResultSet dishonestCancel(@RequestBody @Validated DishonestPerson dishonestPerson);

    /**
     * @Description: 执行人信息 删除
     * @Author: liyanqing
     * @Date: 2021-01-11 15:24
     * @param id:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET})
    ApiResultSet delete(@PathVariable(value = "id", required = false) Long id);

    /**
     * 根据用户姓名和身份证号查询失信人员
     * @param
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getDishonestPerson",method = {RequestMethod.GET})
    ApiResultSet<DishonestPerson> getDishonestPerson(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "cardNumber", required = false) String cardNumber);

}

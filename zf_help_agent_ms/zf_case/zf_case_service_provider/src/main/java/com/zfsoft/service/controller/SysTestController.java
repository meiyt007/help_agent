package com.zfsoft.service.controller;

import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/security/test")
@Slf4j
public class SysTestController {

    @Autowired
    private SysDictFeignService sysDictFeignService;

    /**
     * @description:  查询区划的信息列表
     * @param name 区划名称
     * @param code 区划编码
     * @param parentOid 区划父类oid
     * @author: wuxx
     * @Date: 2020/10/20 10:14
     **/
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    public ApiResultSet querySysDistrictWithPage(String name,String code,Long parentOid,Integer pageNum,
                                                 Integer pageSize){
       /* ApiResultSet<PageResult<SysDict>> pageResultApiResultSet = sysDictFeignService.querySysDictWithPage(name, code, parentOid, pageNum, pageSize);
        return pageResultApiResultSet;*/
        return  null;
    }
}

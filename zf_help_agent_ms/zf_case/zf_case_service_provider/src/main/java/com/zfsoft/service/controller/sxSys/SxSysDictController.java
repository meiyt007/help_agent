package com.zfsoft.service.controller.sxSys;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxSys.SxSysDictManager;
import com.zfsoft.service.sxSys.data.SxSysDict;
import com.zfsoft.service.sxSys.service.SxSysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典控制类
 * @ClassName SxSysDictController
 * @Description 数据字典的实现类
 * @Author wangxl
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxSysDictController implements SxSysDictService {
    @Resource
    private SxSysDictManager sxSysDictManager;

    /**
     * @description:  初始化数据字典的信息
     * @param oid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @Override
    public ApiResultSet initSxSysDict(String oid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=oid){
            SxSysDict  sxSysDict = sxSysDictManager.getSxSysDictByOid(oid);
            resultMap.put("sxSysDict",sxSysDict);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxSysDict> getSxSysDictByOId(String oid) {
        SxSysDict sxSysDict = sxSysDictManager.getSxSysDictByOid(oid);
        ApiResultSet<SxSysDict> apiResultSet = new ApiResultSet<SxSysDict>();
        apiResultSet.setData(sxSysDict);
        return apiResultSet;
    }
}
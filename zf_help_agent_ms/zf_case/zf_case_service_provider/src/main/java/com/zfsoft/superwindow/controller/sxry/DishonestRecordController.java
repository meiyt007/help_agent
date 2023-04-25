package com.zfsoft.superwindow.controller.sxry;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.sxry.DishonestRecord;
import com.zfsoft.superwindow.manager.sxry.DishonestRecordManager;
import com.zfsoft.superwindow.service.sxry.DishonestRecordService;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname DishonestPersonController
 * @Description 失信人员管理控制层
 * @Date 2021-01-11 10:05
 * @Created by liyanqing
 */
@Slf4j
@RestController
public class DishonestRecordController implements DishonestRecordService {

    @Resource
    private DishonestRecordManager dishonestRecordManager;

    @Override
    public ApiResultSet queryPageList(DishonestRecord dishonestRecord,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DishonestRecord> caseMaterialList = dishonestRecordManager.queryPageList(dishonestRecord);
        PageResult<DishonestRecord> pageResult = new PageResult<>(((Page) caseMaterialList).getPageNum()
                , ((Page) caseMaterialList).getPageSize(), ((Page) caseMaterialList).getTotal());
        pageResult.setData(caseMaterialList);
        log.info("获取失信记录列表");
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet saveOrUpdate(DishonestRecord dishonestRecord) {
        this.dishonestRecordManager.saveOrUpdate(dishonestRecord);
        log.info("失信人员执行信息新增/更新成功：{}", JSON.toJSONString(dishonestRecord));
        return new ApiResultSet(200);
    }

    @Override
    public ApiResultSet delete(Long id) {
        DishonestRecord dishonestRecord = dishonestRecordManager.getOne(id);
        dishonestRecord.setDelFlag(String.valueOf(SysCode.DELETE_STATUS.YES));
        this.dishonestRecordManager.saveOrUpdate(dishonestRecord);
        return new ApiResultSet(200);
    }

    @Override
    public ApiResultSet getOne(Long id) {
        DishonestRecord dishonestRecord = dishonestRecordManager.getOne(id);
        return new ApiResultSet(dishonestRecord);
    }
}

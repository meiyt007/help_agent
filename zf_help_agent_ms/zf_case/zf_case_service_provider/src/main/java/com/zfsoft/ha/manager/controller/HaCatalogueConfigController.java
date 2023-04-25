package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaTakeCatalogue;
import com.zfsoft.ha.data.requestData.CataloguePageRequestData;
import com.zfsoft.ha.manager.HaCatalogueConfigService;
import com.zfsoft.ha.managers.HaTakeCatalogueManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/29 15:56
 */
@RestController
@Slf4j
public class HaCatalogueConfigController implements HaCatalogueConfigService {
    @Resource
    HaTakeCatalogueManager haTakeCatalogueManager;

    @Override
    public ApiResultSet<String> getAndLoadWandaCatalogue() throws Exception {
        log.info("获取万达取号目录并保存到本地数据库");
        boolean wandaCatalogue = haTakeCatalogueManager.getAndLoadWandaCatalogue();
        if (wandaCatalogue) {
            log.info("获取万达取号目录并保存到本地数据库成功");
            return ApiResultSet.ok();
        } else {
            log.info("获取万达取号目录并保存到本地数据库错误");
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "获取万达取号目录错误");
        }
    }

    @Override
    public ApiResultSet queryCataloguePage(CataloguePageRequestData cataloguePageRequestData) {
        log.info("获取目录配置分页");
        PageResult<HaTakeCatalogue> wandaCatalogue = haTakeCatalogueManager.queryCataloguePage(cataloguePageRequestData);
        return ApiResultSet.ok(wandaCatalogue);
    }
    
    @Override
    public ApiResultSet saveAndUpload(HaTakeCatalogue haTakeCatalogue){

        log.info("窗口取号管理：进入修改或保存窗口取号信息：入参："+haTakeCatalogue);
        String message;
        try {
            if(haTakeCatalogue.getId()!=null){
                haTakeCatalogue.setDeleteFlag("0");
            }else{
                haTakeCatalogue.setCreateDate(new Date());
                haTakeCatalogue.setDeleteFlag("0");
            }
            int index = haTakeCatalogueManager.saveOrUpdate(haTakeCatalogue);
            if(index != 0){
                if(haTakeCatalogue.getId()!=null){
                    //说明有新增或修改
                    message="修改成功";
                }else{
                    message="新增成功";
                }
            }else{
                message="已存在，没有更改";
            }
            log.info("窗口取号管理：修改或保存窗口取号信息成功"+message);
        }catch (ServiceException e){
            log.info("窗口取号管理：修改或保存窗口取号信息失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"窗口取号管理：修改或保存窗口取号信息失败",e.getMessage());
        }
        return ApiResultSet.ok("窗口取号管理：进入修改或保存窗口取号信息成功",haTakeCatalogue);
    }

    @Override
    public ApiResultSet<HaTakeCatalogue> deleteById(String id) {
        log.info("窗口取号管理：进入根据id删除取号目录：入参：id{}"+id);
        HaTakeCatalogue haTakeCatalogue = null;
        try {
            haTakeCatalogue = haTakeCatalogueManager.deleteUserid(id);
            log.info("窗口取号管理：根据id删除取号目录成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("窗口取号管理：根据id删除取号目录失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"窗口取号管理：根据id删除取号目录失败",e.getMessage());
        }
        return ApiResultSet.ok("窗口取号管理：根据id删除目录成功",haTakeCatalogue);
    }
    @Override
    public ApiResultSet getById(String id){
        HaTakeCatalogue haTakeCatalogue = null;
        try {
        haTakeCatalogue =  haTakeCatalogueManager.selectByPrimaryKey(id);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("窗口取号管理：根据id获取目录失败:"+e.getMessage());
            return  new ApiResultSet<>(500,"窗口取号管理：根据id获取目录失败",e.getMessage());
        }
        return ApiResultSet.ok("窗口取号管理：根据id获取目录成功",haTakeCatalogue);
    }
    
}

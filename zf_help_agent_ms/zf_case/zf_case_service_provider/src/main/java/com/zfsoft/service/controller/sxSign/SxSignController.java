package com.zfsoft.service.controller.sxSign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.scSign.data.SxSign;
import com.zfsoft.service.scSign.data.SxSignDto;
import com.zfsoft.service.scSign.service.SxSignService;
import com.zfsoft.service.manager.sxSign.SxSignManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ChangSheng
 * @Date 15:11 2022/6/16
 * @Description 签名配置
 **/
@RestController
@Slf4j
public class SxSignController implements SxSignService {

    @Resource
    private SxSignManager sxSignManager;

    //获取签名列表，最多就三四条，不需要分页
    @Override
    public ApiResultSet<List<SxSign>> getSignList(SxSign sxSign) {
        log.info("查询签名配置列表：{}",sxSign.toString());
        List<SxSign>  signList = sxSignManager.getSignList(sxSign);
        ApiResultSet<List<SxSign>> listApiResultSet = new ApiResultSet<>(signList);
        return listApiResultSet;
    }

    @Override
    public ApiResultSet<List<SxSign>> getSignListByMaterialOid(String materialOid) {
        log.info("查询签名配置列表：{}",materialOid);
        SxSign sign = new SxSign();
        sign.setMaterialOid(materialOid);
        List<SxSign>  signList = sxSignManager.getSignList(sign);
        ApiResultSet<List<SxSign>> listApiResultSet = new ApiResultSet<>(signList);
        return listApiResultSet;
    }

    @Override
    public ApiResultSet<SxSign> getSignById(String id) {
        log.info("根据id查询签署角色数据：{}",id);
        SxSign sxSign = sxSignManager.getSignById(id);
        ApiResultSet<SxSign> listApiResultSet = new ApiResultSet<>(sxSign);
        return listApiResultSet;
    }

    //新增或修改签名配置
    @Override
    public ApiResultSet<SxSign> saveOrUpdateSxSign(SxSignDto sxSignDto) {
        log.info("修改或保存签名配置");
        ApiResultSet<SxSign> apiResultSet = new ApiResultSet<>();
        try {
            int index = sxSignManager.saveOrUpdateSxSign(sxSignDto);
            if(index != 0){
                //说明有新增或修改
                apiResultSet.setCode(200);
            }else{
                apiResultSet.setCode(201);
            }
        }catch (Exception e){
            log.warn("新增或修改签名配置报错：{}",e.getMessage());
            apiResultSet.setCode(201);
            apiResultSet.setMessage(e.getMessage());
        }
        return apiResultSet;
    }

    //逻辑删除签名配置
    @Override
    public ApiResultSet<Integer> deleteSxSignById(String id) {
        log.info("删除签名配置：{}",id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        try {
            int index = sxSignManager.deleteSxSignById(id);
            if(index != 0){
                //说明有新增或修改
                apiResultSet.setCode(200);
            }else{
                apiResultSet.setCode(201);
            }
        }catch (Exception e){
            log.warn("删除签名配置报错：{}",e.getMessage());
            apiResultSet.setCode(201);
            apiResultSet.setMessage("删除失败，请稍后再试！");
        }
        return apiResultSet;
    }
}

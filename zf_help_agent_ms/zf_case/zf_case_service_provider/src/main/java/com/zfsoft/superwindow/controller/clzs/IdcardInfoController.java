package com.zfsoft.superwindow.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.manager.clzs.IdcardInfoManager;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.dto.IdcardInfo;
import com.zfsoft.superwindow.data.clzs.vo.IdCardInfoVo;
import com.zfsoft.superwindow.service.clzs.IdcardInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: wangwg
 * @create: 2021-01-11
 * @description: 身份证信息控制层
 */
@Slf4j
@RestController
public class IdcardInfoController implements IdcardInfoService {

    @Resource
    private IdcardInfoManager idcardInfoManager;

    @Override
    public ApiResultSet<IdcardInfo> getIdcardFrontInfo(String picBase64) {
        IdcardInfo idcardFrontInfo = idcardInfoManager.getIdcardFrontInfo(picBase64);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(idcardFrontInfo);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> discernIdcardInfo(IdcardInfo idcardInfo, MaterialCatalog cata) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        try {
            Map<String, Object> map = idcardInfoManager.discernIdcardInfo(idcardInfo, cata);
            apiResultSet.setData(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet<IdcardInfo> getTempIdcardInfo(String picBase64)  {
        IdcardInfo idcardFrontInfo = idcardInfoManager.getTempIdcardInfo(picBase64);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(idcardFrontInfo);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<IdcardInfo> getIdcardBackInfo(String picBase64) {
        IdcardInfo idcardFrontInfo = idcardInfoManager.getIdcardBackInfo(picBase64);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(idcardFrontInfo);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> discernIdcardInfo(IdCardInfoVo idCardInfoVo) throws Exception {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        try {
            if(idCardInfoVo!=null){
                Map<String, Object> map = idcardInfoManager.discernIdcardInfo(idCardInfoVo.getIdcardInfo(),idCardInfoVo.getMaterialCatalog());
                apiResultSet.setData(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return apiResultSet;
    }
}

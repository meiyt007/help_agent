package com.zfsoft.single.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.clzs.dto.BusinessLiceInfo;
import com.zfsoft.single.manager.clzs.BusinessLiceManager;
import com.zfsoft.single.service.clzs.BusinessLiceService;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: wangwg
 * @create: 2021-01-11
 * @description: 营业执照信息控制层
 */
@Slf4j
@RestController
public class BusinessLiceController  implements BusinessLiceService {

    @Resource
    private BusinessLiceManager businessLiceManager;

    @Override
    public ApiResultSet<BusinessLiceInfo> getBusinessLiceInfo(String picBase64){
        BusinessLiceInfo businessLiceInfo = businessLiceManager.getBusinessLiceInfo(picBase64);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(businessLiceInfo);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> discernBusinessBaiDu(BusinessLiceInfo businessLiceInfo, String materialOid, MaterialCatalog cata){
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Map<String, Object> objectMap = businessLiceManager.discernBusiness(businessLiceInfo, materialOid, cata ,request);
            apiResultSet.setData(objectMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return apiResultSet;
    }
}

package com.zfsoft.service.controller.sxDirectory;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxDirectory.SxServiceTypeManager;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxDirectory.service.SxServiceTypeService;
import com.zfsoft.service.sxService.data.vo.ServiceTypeNum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName SxServiceTypeController
 * @Description 事项类型控制类
 * @Author wangxl
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxServiceTypeController implements SxServiceTypeService {
    @Resource
    private SxServiceTypeManager sxServiceTypeManager;

    /**
     * @description:  初始化事项类型的信息
     * @param serviceTypeOid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @Override
    public ApiResultSet initSxServiceType(String serviceTypeOid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=serviceTypeOid){
            SxServiceType sxServiceType = sxServiceTypeManager.getSxServiceTypeByOid(serviceTypeOid);
            resultMap.put("sxServiceType",sxServiceType);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }
    /**
     * @description:  获取事项类型的信息
     * @param serviceTypeOid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @Override
    public ApiResultSet<SxServiceType> getSxServiceTypeByOid(String serviceTypeOid) {
        SxServiceType sxServiceType = sxServiceTypeManager.getSxServiceTypeByOid(serviceTypeOid);
        ApiResultSet<SxServiceType> apiResultSet = new ApiResultSet<SxServiceType>();
        apiResultSet.setData(sxServiceType);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceType>> getDbSxServiceTypeList() {
        List<SxServiceType> serviceTypeList = sxServiceTypeManager.getSxServiceTypeList();
        if(null != serviceTypeList && serviceTypeList.size()>0){
            return new ApiResultSet(serviceTypeList);
        }
        return null;
    }

    @Override
    public ApiResultSet<PageResult<SxServiceType>> querySxServiceTypeWithPage(String serviceTypeOid, String serviceTypeName, String serviceTypeCode, Integer pageNum, Integer pageSize) {
        SxServiceType sxServiceType = new SxServiceType();
        sxServiceType.setServiceTypeOid(serviceTypeOid);
        sxServiceType.setServiceTypeName(serviceTypeName);
        sxServiceType.setServiceTypeCode(serviceTypeCode);
        PageResult<SxServiceType> pageResult = sxServiceTypeManager.querySxServiceTypeWithPage(sxServiceType,pageNum,pageSize);
        ApiResultSet<PageResult<SxServiceType>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<ServiceTypeNum>> getServiceTypeAndNumber(Integer serviceStatus) {
        List<ServiceTypeNum> list = sxServiceTypeManager.getServiceTypeAndNumber(serviceStatus);
        return new ApiResultSet(list);
    }
}

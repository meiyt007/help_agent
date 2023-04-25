package com.zfsoft.cases.feign;

import com.zfsoft.microservice.platform.service.CommonService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.web.TreeSelect;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * @author: wangwg
 * @create: 2020-11-20
 * @description: 部门信息外部模块调用接口
 */
@FeignClient(value = "${zfsoft.feign.platform}", contextId = "common")
public interface CommonFeignService extends CommonService {

    /**
     * @description:  根据区划编号获取组织机构列表，用于生成组织机构树
     * @param districtOid 区划oid
     * @author: zhaobf
     * @Date: 2023/3/21
     **/
    default ApiResultSet<List<TreeSelect>>querySysOrganWithPageFeign(String districtOid) {
        ApiResultSet<List<TreeSelect>> sysOrganSet = querySysOrganWithPage(districtOid);
        if (sysOrganSet != null && sysOrganSet.getCode() == ApiResultSet.SUCCESS) {
//                Map<String, Object> sysOrgan = (Map<String, Object>) sysOrganSet.getData();
            return sysOrganSet;
        }
        return null;
    }
}

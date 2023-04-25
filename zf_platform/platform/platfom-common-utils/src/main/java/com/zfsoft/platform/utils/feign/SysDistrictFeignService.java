package com.zfsoft.platform.utils.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysDistrict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName SysDistrictService
 * @Description 区划组件服务定义接口
 * @Author wuxx
 * @Date 2020-08-31 11:33
 **/
@FeignClient(value = "platform-service-provider",contextId ="platform-service" )
public interface SysDistrictFeignService {

    /**
     * @description:  获取区划的信息
     * @param districtOid 区划实体类业务主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/security/district/getSysDistrictByDistrictOid/{districtOid}",method = {RequestMethod.GET})
    ApiResultSet<SysDistrict>  getSysDistrictByDistrictOid(@PathVariable("districtOid")String districtOid);

}

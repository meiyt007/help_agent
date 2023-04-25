package com.zfsoft.cases.feign;

import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.service.sys.SysDistrictService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: WangKe
 * @create: 2022-06-14
 * @description: 机构信息外部模块调用接口
 */
@FeignClient(value ="${zfsoft.feign.platform}",contextId = "district")
public interface SysDistrictFeignService extends SysDistrictService {

	/**
	 * 根据区划oid查询区划code值
	 * @Description: 根据区划oid查询机构的oid集合数据
	 * @param districtOid 区划oid
	 * @return List<String>
	 */
	default String queryDistrictCodeByOid(String districtOid) {
		if (StringUtils.isNotEmpty(districtOid)) {
			ApiResultSet<SysDistrict> districtSet = getSysDistrictByDistrictOid(districtOid);
			if (districtSet != null && districtSet.getCode() == ApiResultSet.SUCCESS) {
				SysDistrict data = districtSet.getData();
				return data.getCode();
			}
		}else{
			return "150600000000";
		}
		return null;
	}
}

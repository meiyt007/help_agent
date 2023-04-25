package com.zfsoft.cases.feign;

import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.service.sys.SysUserService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: wangwg
 * @create: 2020-11-20
 * @description: 用户信息外部模块调用接口
 */
@FeignClient(value ="${zfsoft.feign.platform}",contextId = "user")
public interface SysUserFeginService extends SysUserService {
	/**
	 * 
	 * @Title: queryUserOidsByOrganOid
	 * @Description: 根据机构oid查询下面所有用户的userOid集合数据
	 * @param organOid
	 * @return List<String>
	 */
	default List<String> queryUserOidsByOrganOid(String organOid) {

		if (StringUtils.isNotEmpty(organOid)) {

			ApiResultSet<List<SysUser>> api = getSysUserListByOrganOid(organOid);
			if (api != null && api.getCode() == ApiResultSet.SUCCESS) {
				List<SysUser> listSysUser = api.getData();
				if (listSysUser != null && !listSysUser.isEmpty()) {

					List<String> resList = new ArrayList<>();
					listSysUser.forEach(su -> {
						if (StringUtils.isNotEmpty(su.getUserOid())) {
							resList.add(su.getUserOid());
						}
					});

					return resList;
				}
			}

		}

		return Collections.emptyList();
	}
}

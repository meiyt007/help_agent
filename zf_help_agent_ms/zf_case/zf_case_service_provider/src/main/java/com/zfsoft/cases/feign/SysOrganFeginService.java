package com.zfsoft.cases.feign;

import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.vo.SysOrganVo;
import com.zfsoft.microservice.platform.service.sys.SysOrganService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: wangwg
 * @create: 2020-11-20
 * @description: 部门信息外部模块调用接口
 */
@FeignClient(value = "${zfsoft.feign.platform}", contextId = "organ")
public interface SysOrganFeginService extends SysOrganService {

    /**
     * @param districtOid
     * @return List<String>
     * @Title: queryOrganOidByDistrictOid
     * @Description: 根据区划oid查询机构的oid集合数据
     */
    default List<String> queryOrganOidByDistrictOid(String districtOid) {

        if (StringUtils.isNotEmpty(districtOid)) {

            ApiResultSet<List<SysOrgan>> api = querySysOrganListByDistrictOid(districtOid);
            if (api != null && api.getCode() == ApiResultSet.SUCCESS) {

                List<SysOrgan> listSysOrgan = api.getData();
                if (listSysOrgan != null && !listSysOrgan.isEmpty()) {

                    List<String> resList = new ArrayList<>();
                    listSysOrgan.forEach(so -> {
                        resList.add(so.getOrganOid());
                    });

                    return resList;
                }
            }
        }

        return Collections.emptyList();
    }


    /**
     * 通过部门Oid查询部门code
     *
     * @param organOid 部门oid
     * @return
     */
    default String queryOrganCodeByOid(String organOid) {
        if (StringUtils.isNotEmpty(organOid)) {
            ApiResultSet<SysOrgan> sysOrganSet = getSysOrganByOrganOid(organOid);
            if (sysOrganSet != null && sysOrganSet.getCode() == ApiResultSet.SUCCESS) {
                SysOrgan sysOrgan = sysOrganSet.getData();
                return sysOrgan.getCode();
            }
        }
        return null;
    }

    /**
     * 根据部门oid查询部门名称
     *
     * @param organOid 部门oid
     * @return 部门名称
     * @author yupeng
     * @date 2022年08月10 17:53:08
     */
    default String queryOrganNameByOid(String organOid) {
        if (StringUtils.isNotEmpty(organOid)) {
            ApiResultSet<SysOrgan> sysOrganSet = getSysOrganByOrganOid(organOid);
            if (sysOrganSet != null && sysOrganSet.getCode() == ApiResultSet.SUCCESS) {
                SysOrgan sysOrgan = sysOrganSet.getData();
                return sysOrgan.getName();
            }
        }
        return null;
    }

    /**
     * 根据部门oid查询部门名称
     *
     * @param sysOrganVo
     * @author wangyh
     * @date 2022年08月10 17:53:08
     */
    default Map<String, Object> saveSysOrganSyn(SysOrganVo sysOrganVo) {
            ApiResultSet<SysOrganVo> sysOrganSet = synSaveSysOrgan(sysOrganVo);
            if (sysOrganSet != null && sysOrganSet.getCode() == ApiResultSet.SUCCESS) {
                Map<String, Object> sysOrgan = (Map<String, Object>) sysOrganSet.getData();
                return sysOrgan;
            }
            return null;
    }

    /**
     * 获取机构列表
     * @param serviceOid 事项id
     * @author wangyh
     * @date 2022年08月10 17:53:08
     */
    default ApiResultSet<List<SysOrgan>>getOrganList() {
        ApiResultSet<List<SysOrgan>> sysOrganSet = queryOrganSelectOptions();
        if (sysOrganSet != null && sysOrganSet.getCode() == ApiResultSet.SUCCESS) {
//                Map<String, Object> sysOrgan = (Map<String, Object>) sysOrganSet.getData();
            return sysOrganSet;
        }
        return null;
    }
}

package com.zfsoft.single.common;

import com.zfsoft.cases.util.GenDataTreeUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.service.sys.SysUserService;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.microservice.settings.service.SysDictService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxDirectory.service.SxServiceTypeService;
import com.zfsoft.single.service.common.CommonService;
import com.zfsoft.single.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @（#）: CommonController
 * @description: 通用方法实现类
 * @author: wangwg
 * @date: 2020/10/31
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class TreeSelectController implements CommonService {

    @Resource
    private SxServiceTypeService sxServiceTypeFeginService;

    @Resource
    private SysUserService sysUserFeginService;

    @Resource
    private SysDictService sysDictFeignService;

    @Override
    public ApiResultSet queryServiceTypeSimpleTree(String serviceType) {
        ApiResultSet<List<SxServiceType>> resultSet = sxServiceTypeFeginService.getDbSxServiceTypeList();
        if(resultSet!=null){
            List<SxServiceType> serviceTypeList = resultSet.getData();
            List<TreeSelect> treeSelects = GenDataTreeUtil.buildServiceTypeTreeSelect(serviceTypeList);
            return new ApiResultSet(treeSelects);
        }
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<Integer> getGrantUserType() {
        Integer type = 1;
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        ApiResultSet<SysUser> resultSet = sysUserFeginService.getSysUserByUserOid(userOid);
        SysUser user = null;
        if(resultSet.getData() !=null){
            user = resultSet.getData();
            if(user.getType() !=null){
                ApiResultSet<SysDict> dictResult = sysDictFeignService.getSysDictByDictOid(user.getType());
                if(dictResult.getData() !=null){
                    if(dictResult.getData().getCode().equals(SysCode.GRANT_USER_TYPE.ADMINISTRATION_CODE)){
                        type = 2;
                    }
                }
            }
        }
        return new ApiResultSet(type);
    }
}

package com.zfsoft.superwindow.controller.yxpz;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.ShowInformation;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.service.yxpz.ShowInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName ShowInformationController
 * @Description 参数配置管理的实现类
 * @Author liangxm
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class    ShowInformationController implements ShowInformationService {

    @Resource
    private SysAttaFeginService sysAttaFeginService;

    @Override
    public ApiResultSet<ShowInformation> saveShowInformation(ShowInformation ShowInformation) {
        return null;
    }

    @Override
    public ApiResultSet<PageResult<ShowInformation>> queryShowInformationWithPage(ShowInformation showInformation, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public ApiResultSet initShowInformation(String id) {
        return null;
    }

    @Override
    public Integer deleteShowInformationById(String id) {
        return null;
    }

    @Override
    public Integer ableShowInformationById(String id) {
        return null;
    }

    @Override
    public ApiResultSet<ShowInformation> getShowInformationById(String id) {
        return null;
    }

    @Override
    public ApiResultSet<SysAtta> getShowInformationAttaById(String attaOid) {
        return null;
    }

    @Override
    public ApiResultSet<ShowInformation> getShowInformationByDistOid(String districtOid) {
        return null;
    }
}

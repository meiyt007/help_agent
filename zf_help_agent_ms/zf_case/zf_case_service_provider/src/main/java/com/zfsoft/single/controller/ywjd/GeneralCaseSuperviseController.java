package com.zfsoft.single.controller.ywjd;

import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.data.sys.SysHoliday;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.single.feign.settings.SysHolidayServiceFeginService;
import com.zfsoft.single.manager.ywbl.WindowAcceptanceManager;
import com.zfsoft.single.service.ywjd.GeneralCaseSuperviseService;
import com.zfsoft.single.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @（#）: GeneralCaseSuperviseController
 * @description: 办件监督接口实现类
 * @author: wangwg
 * @date: 2021/5/17
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@RestController
public class GeneralCaseSuperviseController implements GeneralCaseSuperviseService {

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private SysHolidayServiceFeginService sysHolidayServiceFeginService;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Resource
    private WindowAcceptanceManager windowAcceptanceManager;

    @Override
    public ApiResultSet<PageResult<QlCase>> queryGeneralCaseSupervisePage(String caseNumber, String registerUser, String applyUserName, String startDate, String endDate, String overTime, Integer sourceApp, String serviceOids, Integer pageNum, Integer pageSize) {
        List<SysHoliday> holidays =null;
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        ApiResultSet<List<SysHoliday>> holidayResult = sysHolidayServiceFeginService.querySysHolidayListByYear(String.valueOf(DateUtil.getYear()), null);
        if(holidayResult.getData() !=null){
            holidays = holidayResult.getData();
        }
        QlCaseVo doneCaseVo = new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setRegisterUser(registerUser);
        doneCaseVo.setApplyUserName(applyUserName);
        if(startDate !=null){
            doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
        }
        if(endDate !=null){
            doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
        }
        doneCaseVo.setOverTime(overTime);
        doneCaseVo.setSourceApp(sourceApp);
        doneCaseVo.setHolidays(holidays);
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(serviceOids!=null){
                doneCaseVo.setServiceOids(Arrays.asList(serviceOids.split(",").clone()));
            }else{
                return apiResultSet;
            }
        }

        ApiResultSet<PageResult<QlCase>> resultSet = qlCaseServiceFeginService.selectQlCaseSuperviseList(doneCaseVo, pageNum, pageSize);
        PageResult<QlCase> pageResult = resultSet.getData();
        apiResultSet.setData(pageResult);
        return new ApiResultSet<>(pageResult);
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> getOrganUserList() {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        ApiResultSet<List<SysUser>> resultSet = sysUserFeginService.getSysUserListByOrganOid(loginUser.getOrganOid());
        List<SysUser> userList = resultSet.getData();
        return new ApiResultSet(userList);
    }

    @Override
    public ApiResultSet queryUserSimpleTree() {
        List<TreeSelect> serviceTree= windowAcceptanceManager.queryUserSimpleTree();
        return new ApiResultSet(serviceTree);
    }
}

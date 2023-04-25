package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.manager.HaAppointmentManagerService;
import com.zfsoft.ha.managers.HaAppointmentManager;
import com.zfsoft.ha.managers.HaWorkUserServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预约人员查询controller
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月11日 20:02:54
 */
@Slf4j
@RestController
public class HaAppointmentManagerController implements HaAppointmentManagerService {

    /**
     * 用户表实现manager
     */
    @Resource
    private HaWorkUserServiceManager haWorkUserServiceManager;

    /**
     * 预约信息manager
     */
    @Resource
    private HaAppointmentManager haAppointmentManager;

    /**
     * 查询所有的预约的工作人员
     *
     * @return 工作人员集合
     * @author yupeng
     * @date 2022年08月11 20:04:04
     */
    @Override
    public ApiResultSet<List<HaWorkUser>> getAllHelpWorkUser() {
        List<HaWorkUser> haWorkUserList = haWorkUserServiceManager.queryAllWorkUser();
        return ApiResultSet.ok(haWorkUserList);
    }

    /**
     * 查询预约人员分页数据
     *
     * @param name       预约人姓名
     * @param workUserId 预约的工作人员id
     * @param status     预约状态
     * @param pageNum    分页参数
     * @param pageSize   分页参数
     * @return 分页结果集
     * @author yupeng
     * @date 2022年08月12 10:33:51
     */
    @Override
    public ApiResultSet<PageResult<HaAppointment>> queryAppointmentPage(String name, String workUserId, String status, Integer pageNum, Integer pageSize) {
        log.info("查询预约人员分页数据，name：{}，workUserId：{}，status：{}，pageNum：{}，pageSize：{}", name, workUserId, status, pageNum, pageSize);
        PageResult<HaAppointment> dbHaAppointmentPage = haAppointmentManager.queryAppointmentPage(name,null, workUserId, status, pageNum, pageSize);
        return ApiResultSet.ok(dbHaAppointmentPage);
    }
}

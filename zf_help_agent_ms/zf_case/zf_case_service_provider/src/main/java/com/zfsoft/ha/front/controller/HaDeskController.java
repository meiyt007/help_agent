package com.zfsoft.ha.front.controller;


import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.requestData.HaDeskPageRequestData;
import com.zfsoft.ha.data.requestData.HaDeskRequestData;
import com.zfsoft.ha.data.responseData.HaWorkUserGroupResponseData;
import com.zfsoft.ha.data.vo.HaDeskDepVo;
import com.zfsoft.ha.data.vo.HaDeskVo;
import com.zfsoft.ha.front.HaDeskService;
import com.zfsoft.ha.managers.HaDeskManager;
import com.zfsoft.ha.managers.HaWorkGroupServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zfsoft.ha.constant.Constants.DESK_YES;
import static com.zfsoft.ha.constant.Constants.GROUP_LEADER;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/12/26 11:13
 */
@Slf4j
@RestController
public class HaDeskController  implements HaDeskService {

    @Resource
    private HaDeskManager haDeskManager;

    @Resource
    private HaWorkGroupServiceManager haWorkGroupServiceManager;

    /**
     * 帮代办人员分组表实现层
     */
    @Resource
    private HaWorkGroupServiceManager haWorkGrouoServiceManager;

    @Override
    public ApiResultSet saveDesk(@Valid HaDeskRequestData haDeskRequestData) {
        log.info("进入 服务结束，保存一桌联办记录，haDeskRequestData:  {}", haDeskRequestData);
        String code = haDeskManager.saveDesk(haDeskRequestData);
        return new ApiResultSet(code);
    }

    @Override
    public ApiResultSet getGroupById(Long id) throws Exception {
        log.info("根据id查询帮代办人员分组信息， id:{}",id);
        HaWorkGroup haWorkGroup = haWorkGrouoServiceManager.selectByid(id);
        return ApiResultSet.ok("接口调用成功", haWorkGroup);
    }

    @Override
    public ApiResultSet getListWithPage(HaDeskPageRequestData haDeskPage, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取一桌联办记录列表(分页)，haWorkQueueVo:  {}", haDeskPage);
        PageResult<HaDeskVo> resultSet = haDeskManager.getListWithPage(haDeskPage, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    @Override
    public ApiResultSet getDeskGroup(Long deskId) {
        if(deskId==null) {
            log.info("进入获取一桌联办所有部门");
            List<HaWorkUserGroupResponseData> resultSet = haWorkGroupServiceManager.getGroupList(GROUP_LEADER,"",DESK_YES);
            return ApiResultSet.ok("请求成功", resultSet);
        }else{
            return this.getDeskGroupByDesk(deskId);
        }

    }

    @Override
    public ApiResultSet appDepartment(String deskId, String groupIds, Date appDate) {
        log.info("进入一桌联办发起预约,deskId:{},groupIds:{},appDate:{}",deskId, groupIds, appDate);
        return haDeskManager.appDepartment(deskId, groupIds, appDate);
    }

    @Override
    public ApiResultSet getAppCondition(String deskId) {
        log.info("进入一桌联办查看预约进度,deskId:{}",deskId);
        List<HaDeskDepVo> haDeskDeps =  haDeskManager.getAppCondition(deskId);
        return ApiResultSet.ok("请求成功", haDeskDeps);
    }

    @Override
    public ApiResultSet isDeskAppointment() {
        log.info("是否有一桌联办预约确认信息");
        int isDesk = haDeskManager.isDeskAppointment();
        Map<String,Object> map = new HashMap<>();
        map.put("deskNum",isDesk);
        if(isDesk>0){
            Map<String,Object> desMes  = haDeskManager.getDeskDepMessage();
            map.put("desMes",desMes);
        }
        return ApiResultSet.ok("请求成功", map);
    }

    @Override
    public ApiResultSet conAppointment(Long id, String confirmFlag, String desc) {
        log.info("进入一桌联办各部门预约确认,id:{},confirmFlag:{},desc:{}",id,confirmFlag,desc);
        return haDeskManager.conAppointment(id,confirmFlag,desc);
    }

    @Override
    public ApiResultSet startDesk(Long deskQueueId, Long deskId) {
        log.info("进入发起一桌联办,deskQueueId:{},deskId:{}",deskQueueId,deskId);
        return haDeskManager.startDesk(deskQueueId,deskId);
    }


    public ApiResultSet getDeskGroupByDesk(Long deskId) {
        log.info("进入查询一桌联办部门确认,deskId:{}",deskId);
        return haDeskManager.getDeskGroup(deskId);
    }
}

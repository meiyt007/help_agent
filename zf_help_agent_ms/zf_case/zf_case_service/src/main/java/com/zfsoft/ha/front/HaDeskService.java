package com.zfsoft.ha.front;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/12/26 11:15
 */

import com.zfsoft.ha.data.requestData.HaDeskPageRequestData;
import com.zfsoft.ha.data.requestData.HaDeskRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;

@RequestMapping("/ha/desk")
public interface HaDeskService {
    /**
    * Description: 服务结束，保存一桌联办记录
    * @param haDeskRequestData 
    * @author zhaobf
    * date: 2022/12/27 15:45
    */
    @PostMapping("/saveDesk")
    ApiResultSet saveDesk(@RequestBody @Valid HaDeskRequestData haDeskRequestData);

    @GetMapping("/getGroupById")
    ApiResultSet getGroupById(Long id) throws Exception;
    /**
    * Description: 获取一桌联办记录列表(分页)
    * @param haDeskPage
    * @param pageNum
    * @param pageSize
    * @author zhaobf
    * date: 2022/12/27 15:45
    */
    @GetMapping("/getListWithPage")
    ApiResultSet getListWithPage(HaDeskPageRequestData haDeskPage, Integer pageNum, Integer pageSize) throws Exception;

    /**
    * Description: 获取一桌联办所有部门
    * @author zhaobf
    * date: 2022/12/27 15:46
    */
    @GetMapping("/getDeskGroup")
    ApiResultSet getDeskGroup(Long deskId);

    /**
    * Description: 一桌联办发起预约（保存联办部门信息
    * @param deskId
    * @param groupIds
    * @param appDate
    * @author zhaobf
    * date: 2022/12/27 15:46
    */
    @GetMapping("/appDepartment")
    ApiResultSet appDepartment(String deskId, String groupIds, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date appDate);

    /**
    * Description: 查看预约进度
    * @param deskId
    * @author zhaobf
    * date: 2022/12/27 15:46
    */
    @GetMapping("/getAppCondition")
    ApiResultSet getAppCondition(String deskId);

    /**
    * Description: 是否有一桌联办预约确认信息
    * @author zhaobf
    * date: 2022/12/27 15:47
    */
    @GetMapping("/isDeskAppointment")
    ApiResultSet isDeskAppointment();

    /**
    * Description: 确认一桌联办预约
    * @param id
    * @param confirmFlag
    * @param desc
    * @author zhaobf
    * date: 2022/12/27 15:47
    */
    @GetMapping("/conAppointment")
    ApiResultSet conAppointment(Long id,String confirmFlag,String desc);

    /**
    * Description: 发起一桌联办
    * @param deskQueueId
    * @param deskId
    * @author zhaobf
    * date: 2022/12/27 15:47
    */
    @GetMapping("/startDesk")
    ApiResultSet startDesk(Long deskQueueId,Long deskId);

}

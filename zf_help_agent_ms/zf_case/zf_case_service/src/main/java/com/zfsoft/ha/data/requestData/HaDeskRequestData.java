package com.zfsoft.ha.data.requestData;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/12/26 11:21
 */
@Data
public class HaDeskRequestData {

    @NotNull(message = "队列id不能为空")
    private Long queueId;

    @NotNull(message = "公司名称不能为空")
    private String companyName;

    @NotNull(message = "公司统一社会信用代码不能为空")
    private String companyCode;

    private String desc;

    @NotNull(message = "圆桌状态不为空")
    private String deskStatus;

    private Date appDate;

    @NotNull(message = "发起一桌联办的帮办人不为空")
    private Long workUserId;

    private String workUserName;
}

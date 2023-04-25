package com.zfsoft.ha.data.responseData;

import com.zfsoft.ha.data.HaWorkUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @description: 获取分组列表信息响应类
 * @author: kangax
 * @date: 2022-08-11 13:58
 **/
@Data
@ToString
public class HaWorkUserGroupResponseData {
    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 用户vo
     */
    List<HaWorkUser> haWorkUsers;

    private String Status;
}

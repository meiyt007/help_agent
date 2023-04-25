package com.zfsoft.microservice.workflow.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 办理结果实例
 */
@Data
@ToString
public class ProResultExample {
    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP {
    }

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP {
    }

    /* 主键 */
    @NotNull(message = "id不能为空", groups = {ProAcceptExample.UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空", groups = {ProAcceptExample.UPDATE_GROUP.class})
    private String resultOid;

    /**
     * 办件编号
     */
    private String projectNo;
}

package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 加分项目管理实体类
 * @author dingsn
 * @date 2022/10/27  14:59
 */
@Data
@ToString
public class HaPlusProject {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /** 主键 */
    @NotNull(message = "id不能为空",groups = {HaPlusProject.INSERT_GROUP.class})
    private Long id;

    /** 加分项目名称 */
    private String name;

    /** 备注 */
    private String memo;

    /** 删除状态;1-未删除，2-已删除 */
    private String deleteStatus;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;

}

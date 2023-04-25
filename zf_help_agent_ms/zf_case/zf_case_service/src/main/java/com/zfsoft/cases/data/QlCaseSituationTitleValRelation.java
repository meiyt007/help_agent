package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotNull;

/**
 * 记录办件信息与选择材料情形选型关系表(QlCaseSituationTitleValRelation)实体类
 *
 * @author wangwg
 * @date  2020-11-30
 */
@Data
@ToString
public class QlCaseSituationTitleValRelation {

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = {INSERT_GROUP.class})
    private String relationOid;

    /**
     * 办件业务主键
     */
    @NotNull(message = "办件业务主键不能为空",groups = {INSERT_GROUP.class})
    private String caseOid;

    /**
     * 情形业务主键
     */
    private String situationOid;

    /**
     * 标题业务主键
     */
    @NotNull(message = "标题业务主键不能为空",groups = {INSERT_GROUP.class})
    private String titleOid;

    /**
     * 选项值业务主键
     */
    @NotNull(message = "选项值业务主键不能为空",groups = {INSERT_GROUP.class})
    private String valueOid;

}

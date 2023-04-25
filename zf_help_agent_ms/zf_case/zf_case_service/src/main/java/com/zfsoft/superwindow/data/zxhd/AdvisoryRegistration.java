package com.zfsoft.superwindow.data.zxhd;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:  咨询登记表
 * @author: liangss
 * @Date: 2020/10/23 09:49
 **/
@Data
@ToString
public class AdvisoryRegistration {
    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键  */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    private String advisoryRegidtrationOid;

    /* 姓名 */
    private String name;

    /* 电话 */
    private String telePhone;

    /* 身份证号 */
    private String cardNumber;

    /* 咨询内容 */
    private String advisoryContent;

    /* 处理结果*/
    private String result;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createDate;

    /* 删除状态 */
    private Integer delFlag;

    /* 修改时间 */
    private Date modifyDate;

    /* 咨询编号 */
    private String advisoryCode;

    /* 附件 */
    private String attaOid;

    /* 附件名称 */
    private String attaName;

    /* 所属区划 */
    private String districtOid;

    /* 所属组织机构 */
    private String organOid;


}

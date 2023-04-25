package com.zfsoft.ha.data.vo;

import com.zfsoft.ha.data.HaUserService;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 事项信息
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月08日 10:26:52
 */
@Data
@ToString
public class HaServiceVo {

    /**
     * 事项id
     */
    private String serviceOid;

    /**
     * 所属区划
     */
    private String districtName;

    /**
     * 所属机构
     */
    private String organName;

    /**
     * 事项名称
     */
    private String serviceName;

    /**
     * 实施编码
     */
    private String implementCode;

    /**
     * 事项类型
     */
    private String serviceTypeName;

    /**
     * 材料准备状态，默认2，无权限
     */
    private String prepareMaterialStatus = "2";

    /**
     * 收件状态，默认2，无权限
     */
    private String receiveMaterialStatus = "2";


}

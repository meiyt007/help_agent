package com.zfsoft.platform.utils.dict.enums;

import com.zfsoft.platform.utils.dict.DictCTT;

/**
 * @author: kkfan
 * @create: 2022-04-27 13:34:33
 * @description: 字典类型枚举
 */
public enum TypeEnums {

    /**
     * 对应字典表 （默认）<br>
     *      字典表默认根据字典code查询, 其它表根据业务id查询<br>
     *      字典表如需按业务id查询 请配置 {@link DictCTT#isDictOid()}  为 true
     */
    DICT,

    /**
     * 对应配置表
     */
    CONFIG,

    /**
     * 对应区划表
     */
    DISTRICT,

    /**
     * 对应机构表
     */
    ORGAN,

    /**
     * 对应职位表
     */
    POST,

    /**
     * 对应权限表
     */
    ROLE,

    /**
     * 对应用户表
     */
    USER
}

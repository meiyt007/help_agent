package com.zfsoft.es.service.enums;


/**
 * @author: kkfan
 * @create: 2021-01-18 13:11:08
 * @description: 操作类型枚举
 */
public enum OperationTypeEnum {
    /** 新增或更新（根据id更新） */
    SAVE_OR_UPDATE,
    /** 删除 根据id删除 */
    DELETE,
    /** 查询 默认根据id查询 */
    QUERY
}

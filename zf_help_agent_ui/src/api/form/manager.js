import request from '@/utils/request'

let applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH;


// 查询存储对象信息列表
export function page (query) {
    return request({
        url: applicationName + '/manager/security/object/page',
        method: 'get',
        params: query
    })
}

// 初始化存储对象信息
export function getFormObjectByObjectOid (objectOid) {
    if (objectOid == undefined) {
        objectOid = '';
    }
    return request({
        url: applicationName + '/manager/security/object/getFormObjectByObjectOid/' + objectOid,
        method: 'get'
    })
}

// 存储对象的新增或者修改，不生成表单
export function save (data) {
    return request({
        url: applicationName + '/manager/security/object/save',
        method: 'post',
        data: data
    })
}

// 存储对象的新增或者修改,生成表单
export function saveObject (data) {
    return request({
        url: applicationName + '/manager/security/object/saveObject',
        method: 'post',
        data: data
    })
}


// 获取单个存储对象信息
export function getFormObjectByObejctOid (obejctOid) {
    return request({
        url: applicationName + '/manager/security/object/getFormObjectByObjectOid/' + obejctOid,
        method: 'get'
    })
}

//存储对象列表
export function objectlist (query) {
    return request({
        url: applicationName + '/manager/security/object/queryFormObjectList',
        method: 'get',
        params: query
    })
}
//数据源存储对象
export function dataSourceObjectlist (query) {
    return request({
        url: applicationName + '/manager/security/object/queryDataSourceFormObjectList',
        method: 'get',
        params: query
    })
}


//数据库配置列表
export function dataSourcelist (query) {
    return request({
        url: applicationName + '/manager/security/datasource/queryFormDataSourceList',
        method: 'get',
        params: query
    })
}

//模块列表
export function modulelist (query) {
    return request({
        url: applicationName + '/manager/security/module/queryFormModuleList',
        method: 'get',
        params: query
    })
}


// 从数据库获取单个表结构信息
export function datasourceColumn (query) {
    return request({
        url: applicationName + '/manager/security/column/getDatasourceColumn',
        method: 'get',
        params: query
    })
}

//表结构列表
export function queryFormColumnListByObjectOid (query) {
    return request({
        url: applicationName + '/manager/security/column/queryFormColumnListByObjectOid',
        method: 'get',
        params: query
    })
}

// 获取单个对象信息 -表单表
export function getFormMainByFormMainOid (formMainOid) {
    return request({
        url: applicationName + '/manager/getFormMainByFormMainOid/' + formMainOid,
        method: 'get'
    })
}

// 获取单个对象信息 -表单表
export function getFormMainByFormCode (formCode) {
    return request({
        url: applicationName + '/manager/getFormMainByFormCode?formCode=' + formCode,
        method: 'get'
    })
}

/**
 * @description:  获取表存储对象的信息
 * @param formCode 表单编码
 * @author: wuxx
 * @Date: 2021/6/1 13:07
 **/
export function getFormObjectByFormCode (formCode) {
    return request({
        url: applicationName + '/manager/getFormObjectByFormCode?formCode=' + formCode,
        method: 'get'
    })
}
/**
 * @description:  获取表存储对象的信息
 * @param formMainOid 表单业务主键
 * @author: wuxx
 * @Date: 2021/6/1 13:07
 **/
export function getFormObjectByFormMainOid (formMainOid) {
    return request({
        url: applicationName + '/manager/getFormObjectByFormMainOid?formMainOid=' + formMainOid,
        method: 'get'
    })
}

/**
 * @description: 根据数据库字段集合配置创建表或者更新
 * @param formTableDtoJson 数据库字段集合配置JSON数组
 * @param datasourceOid 数据源id
 * @param tableName 表名
 * @author: wuxx
 * @Date: 2021/8/6 14:03
 **/
export function createTableByFormTableDto (formTableDtoJson, datasourceOid, tableName, formCode) {
    let params = {
        formTableDtoJson: formTableDtoJson,
        datasourceOid: datasourceOid,
        tableName: tableName,
        formCode: formCode,
        idIsVarchar: '1'
    }
    return request({
        url: applicationName + '/manager/security/table/createTableByFormTableDto',
        method: 'post',
        data: params
    })
}

/**
 * @description: 根据表单编码集合合并表单成新的表单
 * @param formCodes 表单编码集合，多个用逗号分割
 * @param objectOid 新存储对象的业务主键
 * @param saveDataType 数据库存储类型  0本地  1API  2数据库
 * @param formConfigJson 合并规则json
 * @param formCode 合并后的表单code (非必填)
 * @author: wuxx
 * @Date: 2021/8/5 9:30
 **/
export function mergeFormByFormCodes (params) {
    return request({
        url: applicationName + '/manager/mergeForm',
        method: 'post',
        data: params
    })
}

/**
 * @description: 根据表单编码集合合并表单成新的表单
 * @param formMainOids 表单业务主键集合，多个用逗号分割
 * @param objectOid 新存储对象的业务主键
 * @param saveDataType 数据库存储类型  0本地  1API  2数据库
 * @param formConfigJson 合并规则json
 * @param formMainOid 合并后的表单formMainOid (非必填)
 * @author: wuxx
 * @Date: 2021/8/5 9:30
 **/
export function mergeFormByFormMainOids (formMainOids, objectOid, saveDataType, formConfigJson, formMainOid) {
    let params = {
        formMainOids: formMainOids,
        objectOid: objectOid,
        saveDataType: saveDataType,
        formConfigJson: formConfigJson,
        formMainOid: formMainOid
    }
    return request({
        url: applicationName + '/manager/mergeForm',
        method: 'post',
        data: params
    })
}

/**
 * @description:  表单的发布
 * @param formCode 表单的业务主键
 * @author: wuxx
 * @Date: 2021/08/11 10:14
 **/
export function deployByFormCode (formCode) {
    return request({
        url: applicationName + '/manager/security/main/deployByFormCode?formCode=' + formCode,
        method: 'post'
    })
}

//拼接建表sql
export function createTableSql (data) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/createTableSqlByParams',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
        },
        data: data
    })
}


export function queryChildFormName (authorizeKey, formMainOid) {
    return request({
        url: applicationName + '/manager/queryFormObjectExtandListByFormMainCode?authorizeKey=' + authorizeKey + '&formMainOid=' + formMainOid,
        method: 'get'
    })
}

export const saveTable = (params) => {
    return request.post(applicationName + '/manager/security/table/createFormTableByFormPhysicalDataModel', params);
}

export const getTable = (params) => {
    return request.get(applicationName + '/manager/security/table/getFormPhysicalDataModelByFormCode', { params });
}

// 获取类型 datasourceOid
export const getDataSourceTypeList = (params) => {
    return request.get(applicationName + '/manager/security/table/getDataSourceTypeList', { params });
}
// 获取字段类型字典
export const getDataSourceTypeByNameList = (params) => {
    return request.get(applicationName + '/manager/security/table/getDataSourceTypeByNameList', { params });
}

// 获取存储对象类型字典数据
export const getObjectFieldSaveTypeList = (params) => {
    return request.get(applicationName + '/manager/security/column/getObjectFieldSaveTypeList', { params });
}

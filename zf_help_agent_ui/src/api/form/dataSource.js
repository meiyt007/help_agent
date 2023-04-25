import request from '@/utils/request'

let applicationName = 'form';



// 查询数据库配置信息列表
export function page (query) {
    return request({
        url: applicationName + '/manager/security/datasource/page',
        method: 'get',
        params: query
    })
}

// 初始化数据库配置信息
export function init (id) {
    if (id == undefined || 'undefined' == id) {
        id = '';
    }
    return request({
        url: applicationName + '/manager/security/datasource/init?id=' + id,
        method: 'get'
    })
}

// 数据库配置的新增或者修改
export function save (data) {
    return request({
        url: applicationName + '/manager/security/datasource/save',
        method: 'post',
        data: data
    })
}

//删除数据库配置管理
export function del (id) {
    return request({
        url: applicationName + '/manager/security/datasource/delete/' + id,
        method: 'delete'
    })
}

// 启禁用数据库配置信息
export function able (id) {
    return request({
        url: applicationName + '/manager/security/datasource/able/' + id,
        method: 'post'
    })
}

// 获取单个数据库配置信息
export function getOne (id) {
    return request({
        url: applicationName + '/manager/security/datasource/getOne/' + id,
        method: 'get'
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

// 检查数据库配置是否正常连接
export function checkConnection (data) {
    return request({
        url: applicationName + '/manager/security/datasource/checkConnection',
        method: 'post',
        data: data
    })
}

//获取数据字典数据
export function getDataSourceDictList (code) {
    return request({
        url: applicationName + '/manager/security/datasource/getDataSourceDictList/' + code,
        method: 'get'
    })
}

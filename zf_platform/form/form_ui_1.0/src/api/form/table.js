import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/table';


// 表结构的新增或者修改  1
export function saveFormTableVo(data) {
  return request({
    url: applicationName + '/saveFormTableVo',
    method: 'post',
    data: data
  })
}

// 获取单个表结构信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}

// 从数据库获取字段类型
export function getDataSourceTypeList(datasourceOid) {
  return request({
    url: applicationName + '/getDataSourceTypeList?datasourceOid='+datasourceOid,
    method: 'get'
  })
}

//表结构列表
export function queryFormTableList(query) {
  return request({
    url: applicationName + '/queryFormTableList',
    method: 'get',
    params: query
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
export function createTableByFormTableDto(formTableDtoJson,datasourceOid,tableName,formCode) {
  let params = {
    formTableDtoJson:formTableDtoJson,
    datasourceOid:datasourceOid,
    tableName:tableName,
    formCode:formCode
  };
  return request({
    url: process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/table/createTableByFormTableDto',
    method: 'post',
    data:params
  })
}

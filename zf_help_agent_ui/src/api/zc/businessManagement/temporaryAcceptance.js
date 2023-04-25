/**
 * 暂存受理接口api
 * @author: wangwg
 * @date: 2020-11-29
 */
import request from '@/utils/request';

// 分页查询列表操作
export function page (query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryTemporaryQlCasePage',
    method: 'get',
    params: query
  })
}

// 获取办件与情形标题选项关系信息
export function getCaseTitleValueList (caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/relationService/getCaseTitleValueList?caseOid=' + caseOid,
    method: 'get'
  })
}

// 获取办件与情形标题选项关系信息
export function queryAllCaseByOid (caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/getAllQlCaseByCaseOid?caseOid=' + caseOid,
    method: 'get'
  })
}

// 获取证件名称
export function getCertificateTypeByOid (oid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/sysDictService/getSysDictByDictOid?dictOid=' + oid,
    method: 'get'
  })
}

export function intelligentPretrialmaterialPrePrialAll (caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/preTrial/intelligentPretrialmaterialPrePrialAll',
    params: {
      caseOid: caseOid,
    },
    method: 'get'
  })
}

export function intelligentPretrialmaterialPrePrial (caseOid, caseFileRecOid, caseMaterialOid, cataOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/preTrial/intelligentPretrialmaterialPrePrial',
    params: {
      caseOid: caseOid,
      caseFileRecOid: caseFileRecOid,
      caseMaterialOid: caseMaterialOid,
      cataOid: cataOid,
    },
    method: 'get'
  })
}

export function intelligentPretrial (caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/preTrial/intelligentPretrial',
    params: {
      caseOid: caseOid,
    },
    method: 'get'
  })
}

export function viewResult (caseOid, caseFileRecOid, caseMaterialOid, cataOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/preTrial/viewResult',
    params: {
      caseOid: caseOid,
      caseFileRecOid: caseFileRecOid,
      caseMaterialOid: caseMaterialOid,
      catalogOid: cataOid,
    },
    method: 'get'
  })
}

// 基础表单和电子表单赋值
export function getAllBasicFieldByOid (caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/getAllBasicFieldByOid',
    params: {
      caseOid: caseOid,
    },
    method: 'get'
  })
}

// 基础表单和电子表单字段对应关系
export function getBasicAndFormFieldRel (serviceOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/getBasicAndFormFieldRel',
    params: {
      serviceOid: serviceOid,
    },
    method: 'get'
  })
}

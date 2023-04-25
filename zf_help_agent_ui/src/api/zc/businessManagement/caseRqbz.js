import request from '@/utils/request';

// 分页查询列表操作
export function page (query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryRqhbQlCasePage',
    method: 'get',
    params: query
  })
}
//查询需要容缺补正的材料
export function rqhbMaterialByCaseOid (caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseMaterialService/queryRqhbMaterialByCaseOid?caseOid=' + caseOid,
    method: 'get'
  })
}
//保存或者修改
export function saveOrUpdate (data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/saveOrUpdateRqbz',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

// 附件上传
export function uploadFile () {
  const baseURL = process.env.VUE_APP_BJFW_ROUTE_PATH;
  return baseURL + "/qlCaseMaterialAttaService/uploadCaseMaterialFile";
}

//作废
export function saveStopRqbz (caseOid, caseStatus) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/updateQlCase?caseOid=' + caseOid + "&caseStatus=" + caseStatus,
    method: 'post',
  })
}

//保存失信人员
export function saveOrUpdateOut (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/saveOrUpdate',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

//保存失信记录
export function saveOrUpdateRecord (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/saveOrUpdate',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

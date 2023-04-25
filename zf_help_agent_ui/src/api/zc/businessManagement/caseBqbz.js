import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/pageList',
    method: 'post',
    params: query
  })
}

// 办件申请人信息查询
export function getOneApplyPerson(caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseApplayService/queryQlCaseApplayByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

//保存或者修改
export function saveOrUpdateNotice(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/saveOrUpdateNotice',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 办件补正信息
export function getOneByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/getOneByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}
export function getOneByid(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/getOneById?id='+id,
    method: 'post'
  })
}

export function getCorrectMaterialInfo(correctionOid,caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/getOneMaterialInfo?correctionOid='+correctionOid+"&caseOid="+caseOid,
    method: 'post'
  })
}

//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//终止
export function saveStopCorrection(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/saveStopCorrection?id='+id,
    method: 'post',
  })
}

//保存失信人员
export function saveOrUpdateOut(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//保存失信记录
export function saveOrUpdateRecord(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}



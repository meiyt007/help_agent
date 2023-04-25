import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryQlCasePage',
    method: 'get',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/doneBusiness/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
//获取登录机构主键
export function getOrganCurr() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/doneBusiness/getOrganCurr',
    method: 'post'
  })
}

//获取登录机构主键
export function checkedCase(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/doneBusiness/checkedCase?caseOid='+caseOid,
    method: 'post'
  })
}

// 新增或者修改
export function saveOrUpdateCaseLicenseManage(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/saveOrUpdateCaseLicenseManage',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

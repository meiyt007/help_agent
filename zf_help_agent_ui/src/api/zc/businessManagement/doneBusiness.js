import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryDoneQlCasePage',
    method: 'get',
    params: query
  })
}

// 获取详情
export function getOneByCaseOidForYbyw(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/doneBusiness/getOneByCaseOidForYbyw?caseOid=' + caseOid,
    method: 'post',
  })
}

// 获取签名图片
export function querySignImgPath(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/signRecord/querySignImgPath?caseOid=' + caseOid,
    method: 'get',
  })
}


import request from '@/utils/request';

//查询当前登录者所授权的一件事数据列表
export function pageIndustryCase(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/accept/industryCasePage',
    method: 'get',
    params: query
  })
}

// 保存办件受理信息
export function saveCaseAbolish(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/caseAbolish',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data:data
  })
}

// 办件中止
export function breakIndustryCase(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/breakIndustryCase?caseOid='+caseOid,
    method: 'get'
  })
}
// 进入失信人名单
export function enterDishonestPerson(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/enterDishonestPerson?caseOid='+caseOid,
    method: 'get'
  })
}
// 进入失信人名单
export function dishonestPersonByUser(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/dishonestPersonByUser',
    method: 'get'
  })
}



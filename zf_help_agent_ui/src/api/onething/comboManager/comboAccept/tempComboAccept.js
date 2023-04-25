import request from '@/utils/request';

//查询当前登录者所授权的一件事数据列表
export function pageComboCase(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/accept/comboCasePage',
    method: 'get',
    params: query
  })
}

// 保存办件受理信息
export function saveCaseAbolish(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/caseAbolish',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data:data
  })
}

// 办件中止
export function breakComboCase(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/breakComboCase?caseOid='+caseOid,
    method: 'get'
  })
}
// 进入失信人名单
export function enterDishonestPerson(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/enterDishonestPerson?caseOid='+caseOid,
    method: 'get'
  })
}
// 进入失信人名单
export function dishonestPersonByUser() {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/dishonestPersonByUser',
    method: 'get'
  })
}
// 查询是失信人
export function checkApplyUserInDishonest(name,cardNumber) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/checkApplyUserInDishonest?name='+name+'&cardNumber='+cardNumber,
    method: 'get'
  })
}


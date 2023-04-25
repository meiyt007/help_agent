import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/queryWithPageRqhb',
    method: 'get',
    params: query
  })
}
//查询需要容缺补正的材料
export function rqhbMaterialByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseCorrection/getRqhbMaterialInfo?caseOid='+caseOid,
    method: 'post'
  })
}
//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseCorrection/saveOrUpdateRqbz',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 查询办件容缺后补时间是否过期
export function checkTimeExpire(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/checkTimeExpire?caseOid='+caseOid,
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

export function getPubMaterialInfo(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/getComboDirectoryMaterialByOid/'+oid,
    method: 'get'
  })
}




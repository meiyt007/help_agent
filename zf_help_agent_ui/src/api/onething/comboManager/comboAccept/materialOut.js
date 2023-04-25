import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/pageList',
    method: 'post',
    params: query
  })
}
//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
export function getOneMaterialInfo(caseOid){
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseCorrection/getOneMaterialInfoByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}
// 获取详情
export function getOneByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/getOneByCaseNumber?caseNumber=' + caseNumber,
    method: 'post',
  })
}

export function batchOutMaterial(ids) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/batchOutMaterial?ids='+ids,
    method: 'post',
  })
}

export function printOutMaterial(ids) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/printOutMaterial?ids='+ids,
    method: 'post',
  })
}

// 获取详情
export function getOneMaterialInfoById(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/getOneMaterialInfo?id=' + id,
    method: 'post',
  })
}

//查询已出库信息列表
export function batchOutList(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/batchOutList?caseOid=' + caseOid,
    method: 'post',
  })
}


export function updateComboMaterialOutOfstock(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/updateComboMaterialOutOfstock',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


//保存材料流转记录用于材料出库打印
export function saveComboMaterialOutOfStockRecordByPrint(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/saveComboMaterialOutOfStockRecordByPrint?id=' + id,
    method: 'post',
  })
}

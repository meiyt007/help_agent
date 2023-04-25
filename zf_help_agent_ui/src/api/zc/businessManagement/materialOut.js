import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/pageList',
    method: 'post',
    params: query
  })
}
//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function updateCaseMaterialOutOfStock(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/updateCaseMaterialOutOfStock',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}



// 获取详情
export function getOneByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/getOneByCaseNumber?caseNumber=' + caseNumber,
    method: 'post',
  })
}

export function batchOutMaterial(ids) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/batchOutMaterial?ids='+ids,
    method: 'post',
  })
}

export function printOutMaterial(ids) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/printOutMaterial?ids='+ids,
    method: 'post',
  })
}

// 获取详情
export function getOneMaterialInfo(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/getOneMaterialInfo?id=' + id,
    method: 'post',
  })
}

export function getOpenRobot() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/yanshi/openRabot',
    method: 'get',
  })
}


//保存材料流转记录用于材料出库打印
export function saveCaseMaterialOutOfStockRecordByPrint(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/saveCaseMaterialOutOfStockRecordByPrint?id=' + id,
    method: 'post',
  })
}


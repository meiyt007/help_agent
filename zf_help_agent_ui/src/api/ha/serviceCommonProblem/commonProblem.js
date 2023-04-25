import request from '@/utils/request';






/*=======*/
// 查询填充值列表
export function getFieldFillValList (serviceOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/service/commonProblem/queryComPro?serviceOid=' + serviceOid,
    method: 'get'
  })
}

// 初始化字段填充
export function initOptionFieldValInfo (serviceOid, id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/help/service/commonProblem/initCommonProblemInfo?serviceOid=' + serviceOid
      + "&id=" + id,
    method: 'get'
  })
}


//保存
export function saveSxOptionFieldValInfo(data){
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/help/service/commonProblem/saveServiceComPro',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//删除
export function delSxOptionFieldVal(oid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/help/service/commonProblem/deleteById?oid=' + oid,
    method: 'get'
  })
}

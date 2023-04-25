import request from '@/utils/request'


export function saveIndustryResult(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/combo/industryResult/saveOrupdateResult',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}
// 查询一件事目录统一结果列表
export function queryIndustryResultList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/industryResult/queryIndustryResultByComboDireOid/'+comboDireOid,
    method: 'get'
  })
}

//
export function getIndustryResultsByResultOid(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/industryResult/initindustryResult?oid='+oid,
    method: 'get'
  })
}

// 查询一件事目录被选择事项结果列表
export function querySxServiceResultList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/service/querySxServiceResultList/'+comboDireOid,
    method: 'get'
  })
}
//确认一件事目录统一结果
export function setRtStatus(oid){
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/industryResult/setResultStatus/' + oid,
    method: 'get'
  })
}

//删除一件事目录统一结果
export function del(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/industryResult/delete/' + oid,
    method: 'delete'
  })
}

//删除事项结果与一件事目录关联
export function delIndustryResult(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/industryResult/delIndustryResult/' + id,
    method: 'delete'
  })
}

//获取综合许可证照要素信息
export function getIndustryCommonResultsElements() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/combo/industryResult/getIndustryCommonResultsElements',
    method: 'get'
  })
}



import request from '@/utils/request'
import http  from '@/utils/http'
// 查询目录清单信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/queryFillLabelPage',
    method: 'get',
    params: query
  })
}

// 初始化目录清单信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/getFillLabel',
    method: 'get',
    params:{labelOid:oid}
  })
}

// 目录清单的新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/saveFillLabel',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除目录清单
export function del(oid) {
  return http({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/deleteFillLabel',
    method: 'post',
    data:{labelOid:oid}
  })
}


// 获取字段分类列表
export function getFieldTypeList(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillForm/querySxFieldTypeList',
    method: 'get',
    params:{thingOid:oid}
  })
}

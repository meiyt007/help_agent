import request from '@/utils/request'

// 查询一件事主题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/page',
    method: 'get',
    params: query
  })
}

export function saveComboService(serviceOids,comboDirectoryOid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/saveComboService?serviceOids='+serviceOids+'&comboDirectoryOid='+comboDirectoryOid,
    method: 'get',
    data:''
  })
}

//删除一件事目录管理
export function delSxservice(ids) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/delSxservice/' + ids,
    method: 'delete'
  })
}


//删除一件事目录管理
export function delComboService(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/delComboService/' + id,
    method: 'delete'
  })
}
//选择
export function selectSpecial(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/selectSpecial/' + id,
    method: 'delete'
  })
}

//配置目录事项
export function configService(ids,type) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/configService?ids=' + ids+'&type=' + type,
    method: 'get'
  })
}

//配置目录事项
export function configAllService(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/configAllService/' + comboDireOid,
    method: 'get'
  })
}

// 判断事项是否配置公共材料
export function pubMaterialNumByServiceOid(comboDireOid,oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/pubMaterialNumByServiceOid?comboDireOid='+comboDireOid+"&serviceOid="+oid,
    method: 'get'
  })
}

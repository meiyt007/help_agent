import request from '@/utils/request'

// 查询一件事主题信息列表
export function pageService(query,valOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/page?valOid='+valOid,
    method: 'get',
    params: query
  })
}

export function saveOptionService(serviceOid,valOid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionService/saveOptionService?serviceOid='+serviceOid+'&valOid='+valOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
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
export function pubMaterialNumByServiceOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/pubMaterialNumByServiceOid/'+oid,
    method: 'get'
  })
}

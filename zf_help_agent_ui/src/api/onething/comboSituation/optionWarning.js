import request from '@/utils/request'

//保存
export function saveOptionWarning(data){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionWarning/saveOptionWarning',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//分页查询
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionWarning/page',
    method: 'get',
    params: query
  })
}

//初始化阻塞配置
export function initWarningRel(comboDireOid, relOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionWarning/initOptionWarning?comboDirectoryOid='+comboDireOid+'&relOid='+relOid,
    method: 'get'
  })
}

//删除阻塞配置
export function delWarningRel(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionWarning/delWarningRel/' + oid,
    method: 'delete'
  })
}

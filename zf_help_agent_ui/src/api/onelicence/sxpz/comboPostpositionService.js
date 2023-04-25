import request from '@/utils/request'



//配置选项
export function getOne(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/position/getOne/' + oid+'?oid='+oid,
    method: 'get'
  })
}
//区划机构目录树
export function queryComboDirectoryTree() {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryComboDirectoryTree',
    method: 'post',
  })
}
//区划机构事项树
export function queryServiceTree() {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryServiceTree',
    method: 'post',
  })
}

// 一件事选项的保存
export function save(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/position/saveComboPostpositionService',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

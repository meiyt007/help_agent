import request from '@/utils/request'

// 查询一件事主题信息列表
export function querySxServicePage(query,comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/sxServicePage?comboDirectoryOid='+comboDirectoryOid,
    method: 'POST',
    params: query
  })
}



export function saveComboService(serviceOids,comboDirectoryOid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/saveComboService?serviceOids='+serviceOids+'&comboDirectoryOid='+comboDirectoryOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}


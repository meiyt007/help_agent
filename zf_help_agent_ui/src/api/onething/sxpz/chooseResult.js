import request from '@/utils/request'

// 查询一件事事项结果列表
export function querySxServiceResultPage(query,direOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/queryResultpage?comboDirectoryOid='+direOid,
    method: 'get',
    params: query
  })
}



export function saveComboResult(serviceOids,comboDirectoryOid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/saveOrupdateSxResult?serviceOids='+serviceOids+'&comboDirectoryOid='+comboDirectoryOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}


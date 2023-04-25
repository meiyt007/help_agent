import request from '@/utils/request'

// 查询一件事事项结果列表
export function queryChooseSxMaterList(query,direOid) {
  console.log(direOid);
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryChooseSxMaterList/'+direOid,
    method: 'get',
    params: query
  })
}



export function saveComboMaterial(materialOids,comboDirectoryOid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/saveComboMaterial?materialOids='+materialOids+'&comboDirectoryOid='+comboDirectoryOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}


import request from '@/utils/request';


export function queryServiceMaterialWithPage(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/queryServiceMaterialWithPage',
    params:data,
    method: 'get',
  })
}



// 保存或者修改
export function saveOrUpdateServiceMaterial(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/updateServiceMaterial',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}







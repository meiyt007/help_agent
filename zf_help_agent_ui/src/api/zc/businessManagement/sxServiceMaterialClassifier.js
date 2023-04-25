import request from '@/utils/request';


// 保存或者修改
export function saveOrUpdateSxServiceMaterialClassifier(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/saveOrUpdateSxServiceMaterialClassifier',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}







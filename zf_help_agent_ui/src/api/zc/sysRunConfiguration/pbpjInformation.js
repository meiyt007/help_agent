import request from '@/utils/request';

//查询平板信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/page',
    method: 'get',
    params: query
  })
}



// 获取一条平板信息详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/getOne/'+id+'?id=' + id,
    method: 'get',
  })
}


// 获取一条平板信息详情
export function getOneFile(attaOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/getOneFile/'+attaOid+'?attaOid=' + attaOid,
    method: 'get',
  })
}

// 确认平板信息信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/delete/'+id,
    params: {
      id: id
    },
    method: 'post'
  })

}
// 启禁用平板信息信息
export function able(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/able/'+id,
    params: {
      id: id
    },
    method: 'post'
  })
}
// 平板信息新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

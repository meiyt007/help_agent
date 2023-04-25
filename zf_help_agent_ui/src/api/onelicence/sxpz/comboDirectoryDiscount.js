import request from '@/utils/request'

// 查询一件事主题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryDiscount/page',
    method: 'get',
    params: query
  })
}

//getOne
export function getOne(id) {
  if(id == undefined){
    return;
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryDiscount/getDirectoryDiscountById/' + id,
    method: 'get'
  })
}

//保存
export function save(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryDiscount/saveDirectoryDiscount',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除
export function del(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryDiscount/delDirectoryDiscount/'+id,
    method: 'delete'
  })
}

// 获取一条平板信息详情
export function getOneFile(attaOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/getOneFile/'+attaOid+'?attaOid=' + attaOid,
    method: 'get',
  })
}

import request from '@/utils/request';

// 处理上下架
export function handleComboUpper(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/upperShelf/save',
    method: 'post',
    params: data
  })
}
// 查询上下架记录
export function getOne(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/upperShelf/getOne/'+comboDirectoryOid,
    method: 'get'
  })
}


//查询一件事上下架管理数据列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/upperShelf/page',
    method: 'get',
    params: query
  })
}
//查询已上下架管理数据列表
export function pageUpperShelf(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/upperShelf/pageUpperShelf',
    method: 'get',
    params: query
  })
}



// 设置排序号
export function setSort(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/upperShelf/setSort',
    method: 'post',
    params: data
  })
}
// 设置排序号
export function setSortUpAndDown(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/upperShelf/setSortUpAndDown',
    method: 'post',
    params: data
  })
}


import request from '@/utils/request'

// 查询终端列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/terminal/page',
    method: 'post',
    params: query
  })
}

//新增或修改终端信息
export function saveTerminal(data){
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/terminal/save',
    method: 'post',
    data: data
  })
}

//查看单个终端信息
export function getOne(id){
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/terminal/getOne',
    params: {
      id: id
    },
    method: 'post'
  })
}

//删除
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/terminal/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

//获取点位数据
export function getLonLat() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/point/getMap',
    method: 'post'
  })
}

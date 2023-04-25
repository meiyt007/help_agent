import request from '@/utils/request'

// 查询点位列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/point/page',
    method: 'post',
    params: query
  })
}

//新增或修改点位信息
export function saveOrUpdatePoint(data){
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/point/save',
    method: 'post',
    data: data
  })
}

//查看单个点位信息
export function getPoint(id){
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/point/getOne',
    params: {
      id: id
    },
    method: 'post'
  })
}

//删除
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/point/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

//根据地址查询坐标
export function getCoordinate(address){
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/point/getCoordinate',
    params: {
      address: address
    },
    method: 'post'
  })
}

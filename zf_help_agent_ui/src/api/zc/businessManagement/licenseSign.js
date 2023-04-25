import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/pageList',
    method: 'post',
    params: query
  })
}

//保存或者修改
export function saveOrUpdateSign(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/saveOrUpdateSign',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}



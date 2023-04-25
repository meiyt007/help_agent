import request from '@/utils/request';
//查询列表
export function page(query) {
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/banner/BannerServicePage',
      method: 'post',
      params: query
    })
  }
// 新增或者修改
export function saveOrUpdateBanner(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/banner/saveOrUpdata',
    method: 'post',
    data: data
  })
}

// 根据id banner信息
export function initBanner(id) {
    return request({
      url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/banner/getThaBannerById?id='+id,
      method: 'get'
    })
  }

  // 根据id删除banner信息
export function deleteBannerById(id) {
    return request({
      url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/banner/deleteBannerid?id='+id,
      method: 'get'
    })
  }

  //批量删除
export function batchRemoveBanner(idList){
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/banner/deleteBannerids?ids='+idList,
      method: 'get',
    })
  }
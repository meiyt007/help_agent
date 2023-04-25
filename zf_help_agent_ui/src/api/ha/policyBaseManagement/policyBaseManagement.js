import request from '@/utils/request';
//查询列表
export function page(query) {
    console.log(query);
      return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/base/policy/queryPolicyBaseWithPage',
        method: 'post',
        params: query
      })
    }

// 新增或者修改
export function saveOrUpdate(data) {
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/base/policy/saveOrUpdatePolicyBase',
      method: 'post',
      data: data
    })
  }

  // 根据id banner信息
  export function init(id) {
      return request({
        url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/base/policy/getPolicyBaseById?id='+id,
        method: 'get'
      })
    }

    // 根据id删除banner信息
  export function deleteById(id) {
      return request({
        url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/base/policy/deletePolicyById?id='+id,
        method: 'get'
      })
    }

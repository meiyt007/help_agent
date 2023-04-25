import request from '@/utils/request';
//查询列表
export function page(query) {
    console.log(query);
      return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/groupSplit/groupServicePage',
        method: 'post',
        params: query
      })
    }

// 新增或者修改
export function saveOrUpdateGroup(data) {
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/groupSplit/saveGroup',
      method: 'post',
      data: data
    })
  }

  // 根据id banner信息
  export function initGroup(id) {
      return request({
        url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/groupSplit/getGroupById?id='+id,
        method: 'get'
      })
    }

    // 根据id删除banner信息
  export function deleteGroupById(id) {
      return request({
        url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/groupSplit/deleteGroupOid?id='+id,
        method: 'get'
      })
    }

import request from '@/utils/request';

//重置密码
export function reserpassword(id){
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/video/ResetPassword?id='+id,
    method: 'get',
  })
}
// 获取机构分组列表
export function queryOrganSelectOptions() {
  return request({
    // url:'/paltform/security/organ/queryOrganSelectOptions?serviceOid='+serviceOid,
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/affair/sxService/queryOrganSelectOptions",
    method: "get"
  });
}

//查询列表
export function page(query) {
    console.log(query);
      return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/video/queryVideoPage',
        method: 'post',
        params: query
      })
    }

//单调删除
export function deleteByOid(id){
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/video/deleteByOid?id='+id,
      method: 'get',
    })
  }

  // 新增或修改
export function saveVideo(data) {
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/video/save',
      method: 'post',
      data: data
    })
  }

  // 根据id User信息
export function getById(id) {
    return request({
      url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/video/getById?id='+id,
      method: 'get'
    })
  }

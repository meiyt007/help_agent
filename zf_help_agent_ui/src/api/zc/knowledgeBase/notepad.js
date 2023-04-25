import request from '@/utils/request';

//查询个人记事本列表
export function presonalpage(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/notepad/queryPersonalNotepadWithPage',
    method: 'post',
    params: query
  })
}


//查询记事本列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/notepad/queryNotepadWithPage',
    method: 'post',
    params: query
  })
}

// 获取一条记事本详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/notepad/getNotepadById',
    params: {
      id: id
    },
    method: 'post',
  })
}
// 记事本新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/notepad/saveNotepad',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


// 删除记事本信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/notepad/deleteNotepadById',
    params: {
      id: id
    },
    method: 'post'
  })
}

  // 共享记事本信息
  export function share(id) {
    return request({
      url: process.env.VUE_APP_ZC_ROUTE_PATH + '/notepad/shareFlagNotepadById',
      params: {
        id: id
      },
      method: 'post'
    })
}



import requestInterface from '@/utils/requestInterface'
var applicationName = process.env.VUE_APP_INTERFACE_API_PATH?process.env.VUE_APP_INTERFACE_API_PATH:'/interface';

// request 请求
export function requestData(data) {
  return requestInterface({
    url: applicationName + '/manager/request?time='+new Date().getTime(),
    method: 'post',
    data: data
  })
}

const baseURL = process.env.VUE_APP_BASE_API;

// 附件上传
export function uploadFile() {
  return baseURL + applicationName + "/interfaceFile/uploadFile";
}

// change 请求
export function changeRequestData(data) {
  return requestInterface({
    url: applicationName + '/manager/change',
    method: 'post',
    data: data
  })
}



// 查询信息列表
export function page(query) {
  return requestInterface({
    url: applicationName + '/manager/page',
    method: 'get',
    params: query
  })
}

// 的新增或者修改
export function save(data) {
  return requestInterface({
    url: applicationName + '/manager/save',
    method: 'post',
    data: data
  })
}

//删除管理
export function del(flag) {
  return requestInterface({
    url: applicationName + '/manager/delete?flag=' + flag,
    method: 'delete'
  })
}

// 获取单个信息
export function getOne(flag) {
  return requestInterface({
    url: applicationName + '/manager/getOne?flag='+flag,
    method: 'get'
  })
}

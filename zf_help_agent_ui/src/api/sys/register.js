import request from '@/utils/request'


// 验证上传注册文件是否正确
export function checkRegisterLicense(data) {
  return request({
    url: '/register/checkRegisterLicense',
    method: 'post',
    data: data
  })
}
//初始化页面
export function initRegister() {
  return request({
    url: '/register/initRegister',
    method: 'get'
  })
}
// 文件上传后数据的解析
export function uploadRegister(attaOid) {
  return request({
    url: '/register/uploadRegister?attaOid='+attaOid,
    method: 'get'
  })
}

// 注册文件上传
export function uploadFile() {
  const baseURL = process.env.VUE_APP_BASE_API;
  return baseURL + "/platform/security/atta/uploadRegisterFile";
}

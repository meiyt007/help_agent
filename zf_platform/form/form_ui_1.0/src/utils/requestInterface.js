import axios from 'axios'
import { Message, MessageBox, Notification } from 'element-ui'
import store from '@/store'
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const serviceInterface = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 60000
})
// request拦截器
serviceInterface.interceptors.request.use(config => {
  if(config.type&&config.type=="form"&&config.data)
    config.data=toformdata(config.data)
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

function toformdata(data) {
  var str="";
  var index = 0;
  for(name in data){
    if(index++==0)
      str+=name+"="+data[name];
    else
      str+="&"+name+"="+data[name];

  }
  return str;
}

// 响应拦截器
serviceInterface.interceptors.response.use(res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code;
    // 获取错误信息
    const message = res.data.message || res.data.msg
    if (code == 401) {
      MessageBox.confirm(
        '登录状态已过期，您可以继续留在该页面或重新登录',
        '系统提示',
        {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        store.dispatch('LogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      })
    } else if (code === 500) {
      Message({
        message: message,
        type: 'error'
      })
      return Promise.reject(new Error(message))
    }else if (code === 501 || code === 503) {
      Message({
        message: res.data.message,
        type: 'error'
      })
      return Promise.reject(new Error(res.data.message))
    }else if (code !== 200) {
      Notification.error({
        title: message
      })
      return Promise.reject('error')
    } else {
      return JSON.parse(JSON.stringify(res))
    }
  },
  error => {
    if(undefined == error.response){
      return false;
    }
    if('0000_24' == error.response.data.status && '身份鉴权失败'==error.response.data.message){
      MessageBox.confirm(
        '登录状态已过期，您可以继续留在该页面或重新登录',
        '系统提示',
        {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        store.dispatch('LogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      })
    }else {
      var message = error.response.data.message;
      if(undefined == error.response.data.data.status){
        message = '数据异常，请稍后再试！';
      }
      Message({
        message: message,
        //message: error.response.data.message,
        type: 'error',
        duration: 5 * 1000
      })
    }
    return Promise.reject(error)
  }
)

export default serviceInterface

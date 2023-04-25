import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: process.env.VUE_APP_BASE_API,
    // 超时
    timeout: 30000
})

const SERVICE_MESSAGE = {
    '500': '系统出现错误，请联系系统管理员！',
    '503': '系统服务不可用, 请稍后重试!',
    '1000': '用户被锁定！请联系工作人员',
    '1001': '用户名或密码错误！',
    '401': '身份信息过期,请重新登录',
};

// request拦截器
service.interceptors.request.use(config => {
    if (config.type && config.type == "form" && config.data)
        config.data = toformdata(config.data)
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    // isGpy 如果是连接高拍仪服务 不携带token
    if (getToken() && !isToken && !config.isGpy) {
        config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
}, error => {
    console.log(error)
    Promise.reject(error)
})
function toformdata (data) {
    var str = "";
    var index = 0;
    for (name in data) {
        if (index++ == 0)
            str += name + "=" + data[name];
        else
            str += "&" + name + "=" + data[name];

    }
    return str;
}


let pending = []; //声明一个数组用于存储每个ajax请求的取消函数和ajax标识
let cancelToken = axios.CancelToken;//初始化取消请求的构造函数
let removePending = (config, f) => {
    let flagUrl = config.url + '&' + config.method
    if (pending.indexOf(flagUrl) !== -1) {
        /*if(f){
          f()
        }else{
          setTimeout(function () {
            pending.splice(pending.indexOf(flagUrl), 1)
          },200);
        }*/
        setTimeout(function () {
            pending.splice(pending.indexOf(flagUrl), 1)
        }, 200);
    } else {
        if (f) {
            pending.push(flagUrl)
        }
    }
}

//添加请求拦截器(解决ajax重复提交的问题)
service.interceptors.request.use(config => {
    if (config.method === 'post') {
        config.cancelToken = new cancelToken((c) => {
            removePending(config, c);
        });
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 响应拦截器
service.interceptors.response.use(res => {
    if (res.config && 'string' != typeof (res.config) && res.config.method === 'post') {
        removePending(res.config)
    }
    // 未设置状态码则默认成功状态
    const code = (res.data.code || res.data.status || 200) == '0000_0' ? 200 : (res.data.code || res.data.status || 200);
    // 获取错误信息
    const message = res.data.message || res.data.msg || errorCode[code] || errorCode['default']

    if (code == 401) {
        if (window.isDidLogInAction) {
            window.isDidLogInAction = false;
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
            }).catch(() => {
                window.isDidLogInAction = true;
            })
        }
    } else if (code === 500) {
        if(message) {
          Message({
            message: message,
            type: 'error'
          })
        } else {
          Message({
            message: SERVICE_MESSAGE['500'],
            type: 'error'
          })
        }
        return Promise.reject(new Error(message))
    } else if (code === 501 || code === 503) {
        Message({
            message: res.data.message==null?SERVICE_MESSAGE['503']:res.data.message,
            type: 'error'
        })
        return Promise.reject(new Error(res.data.message))
    } else if (code == 1) {
        if (message.indexOf("平台未注册") != -1) {
            MessageBox.confirm(
                message,
                '系统提示',
                {
                    confirmButtonText: '去注册',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                location.href = "./initRegister";
            })
        } else if (message.indexOf("授权已到期") != -1) {
            MessageBox.confirm(
                '授权已到期，请申请新的注册文件！',
                '系统提示',
                {
                    confirmButtonText: '去注册',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                location.href = "./initRegister";
            })
        } else {
            Message({
                message: message,
                //message: error.response.data.message,
                type: 'error',
                duration: 5 * 1000
            })
        }
    } else if (code !== 200) {
        Message({
          message: message,
          type: 'error'
        })
        return Promise.reject(new Error(message))
    } else {
        return JSON.parse(JSON.stringify(res.data))
    }
},
    error => {
        console.log('%c [error]:', 'color:red;font-weight:700;', error.response);
        if (undefined == error.response) {
            return false;
        }
        if (error.response.config && 'string' != typeof (error.response.config) && error.response.config.method === 'post') {
            removePending(error.response.config)
        }
        if ('0000_24' == error.response.data.status && '身份鉴权失败' == error.response.data.message) {
            // 如果是配置中存在`isConfig=true` 就直接return
            if (error.response.config.isConfig) {
                return Promise.reject(error);
            }
            MessageBox.confirm(
                '登录状态已过期，您可以继续留在该页面或重新登录',
                '系统提示',
                {
                    confirmButtonText: '重新登录',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                console.log('%c [重新登录 response error]:', 'color:red;font-weight:700;', location.pathname);
                store.dispatch('LogOut').then(() => {
                    if (location.pathname === '/login') {
                        return;
                    }
                    location.reload() // 为了重新实例化vue-router对象 避免bug
                })
            })
        } else if (error.response.data.message.indexOf("平台未注册") != -1) {
            MessageBox.confirm(
                error.response.data.message,
                '系统提示',
                {
                    confirmButtonText: '去注册',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                location.href = "./initRegister";
            })
        } else if (error.response.data.message.indexOf("授权已到期") != -1) {
            MessageBox.confirm(
                '授权已到期，请申请新的注册文件！',
                '系统提示',
                {
                    confirmButtonText: '去注册',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                location.href = "./initRegister";
            })
        } else if ('0000_24' == error.response.data.status && '用户被锁定！' == error.response.data.message) {
            Message({
                message: SERVICE_MESSAGE['1000'],
                type: 'error',
                duration: 5 * 1000
            })
        } else if ('0000_24' == error.response.data.status && '用户名或密码错误！' == error.response.data.message) {
            Message({
                message: SERVICE_MESSAGE['1001'],
                type: 'error',
                duration: 5 * 1000
            })
        } else if (error.response.data.status === 500 && error.response.data.message.includes('验证码')) {
            Message({
                message: error.response.data.message || '验证码不正确!',
                type: 'error',
                duration: 5 * 1000
            })
        }else if(error.response.data.status === 500 && error.response.data.message.includes('手机号')){
          Message({
            message: error.response.data.message ,
            type: 'error',
            duration: 3 * 1000
          })
        }else if (error.response.data.status === 500 ){
          Message({
            message: error.response.data.message ,
            type: 'error',
            duration: 3 * 1000
          })
        } else {
            // var message = error.response.data.message;
            // if (undefined == error.response.data.status) {
            //   message = '数据异常，请稍后再试！';
            // }
            Message({
                message: SERVICE_MESSAGE['500'],
                type: 'error',
                duration: 5 * 1000
            })
        }
        return Promise.reject(error)
    }
)

export default service

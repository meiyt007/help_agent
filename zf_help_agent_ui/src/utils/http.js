import axios from 'axios'
import qs from "qs";
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
    timeout: 10000,
    /**在发送前处理data数据 */
    transformRequest: [
        data => {
            if (!data || typeof data === "string") {
                return data;
            }

            // 如果是Form表单就直接跳过JSON转换
            if (data instanceof FormData) {
                return data;
            }

            return qs.stringify(data, {
                arrayFormat: "brackets",
                strictNullHandling: false
            });
        }
    ],
})
// request拦截器
service.interceptors.request.use(config => {
    if (config.type && config.type == "form" && config.data)
        config.data = toformdata(config.data)
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    if (getToken() && !isToken) {
        config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    if (config.method === 'post' || config.method === 'POST') {
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded'
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
let arr = []//区分是请求还是响应的头部
let removePending = (config, f) => {
    let flagUrl = config.url + '&' + config.method
    if (pending.indexOf(flagUrl) !== -1) {
        if (f) {
            f()
        } else {
            setTimeout(function () {
                pending.splice(pending.indexOf(flagUrl), 1)
            }, 200);
        }
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
    const code = res.data.code || 200;
    // 获取错误信息
    const message = errorCode[code] || res.data.msg || errorCode['default']
    if (code === 401) {
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
        Message({
            message: message,
            type: 'error'
        })
        return Promise.reject(new Error(message))
    } else if (code === 501 || code === 503) {
        Message({
            message: res.data.message,
            type: 'error'
        })
        return Promise.reject(new Error(res.data.message))
    } else if (code !== 200) {
        Notification.error({
            title: message
        })
        return Promise.reject('error')
    } else {
        return res.data
    }
},
    error => {
        if (undefined == error.response) {
            return false;
        }
        if (error.response.config && 'string' != typeof (error.response.config) && error.response.config.method === 'post') {
            removePending(error.response.config)
        }

        if ('0000_24' == error.response.data.status && '身份鉴权失败' == error.response.data.message) {
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
        } else {
            var message = error.response.data.message;
            if (undefined == error.response.data.status) {
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

export default service

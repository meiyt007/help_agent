import axios from "axios";
import { Notification, MessageBox, Message } from "element-ui";
// import store from "@/store";
import { getToken } from "@/utils/auth";
// import errorCode from "@/utils/errorCode";

axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8";
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  // baseURL: process.env.VUE_APP_BASE_API,
  baseURL: "",
  // 超时
  timeout: 300000,
});
// request拦截器
service.interceptors.request.use(
  (config) => {
    if (!navigator.onLine) {
      Message({
        message: "网络错误",
        type: "error",
      });
      return;
    }
    if (config.type && config.type == "form" && config.data)
      config.data = toformdata(config.data);
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false;

    if (getToken() && !isToken) {
      config.headers["Token"] = getToken(); // 让每个请求携带自定义token 请根据实际情况自行编辑
    }
    return config;
  },
  (error) => {
    console.log(error);
    Promise.reject(error);
  }
);
function toformdata(data) {
  var str = "";
  var index = 0;
  for (name in data) {
    if (index++ == 0) str += name + "=" + data[name];
    else str += "&" + name + "=" + data[name];
  }
  return str;
}

let pending = []; //声明一个数组用于存储每个ajax请求的取消函数和ajax标识
let cancelToken = axios.CancelToken; //初始化取消请求的构造函数
let removePending = (config, f) => {
  let flagUrl = config.url + "&" + config.method;
  if (pending.indexOf(flagUrl) !== -1) {
    if (f) {
      f();
    } else {
      setTimeout(function () {
        pending.splice(pending.indexOf(flagUrl), 1);
      }, 200);
    }
  } else {
    if (f) {
      pending.push(flagUrl);
      //10s后清除访问的连接地址
      setTimeout(function () {
        pending.splice(pending.indexOf(flagUrl), 1);
      }, 10000);
    }
  }
};

//添加请求拦截器(解决ajax重复提交的问题)
service.interceptors.request.use(
  (config) => {
    if (config.method === "post") {
      config.cancelToken = new cancelToken((c) => {
        removePending(config, c);
      });
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
// 响应拦截器
service.interceptors.response.use(
  (res) => {
    if (
      res.config &&
      "string" != typeof res.config &&
      res.config.method === "post"
    ) {
      removePending(res.config);
    }
    // 未设置状态码则默认成功状态
    const code =
      (res.data.code || res.data.status || 200) == "0000_0"
        ? 200
        : res.data.code || res.data.status || 200;
    // 获取错误信息
    const message =
      res.data.message ||
      res.data.msg ;
    //   errorCode[code] ||
    //   errorCode["default"];
    if (code == 501) {
      // MessageBox.confirm(
      //   "登录状态已过期，您可以继续留在该页面或重新登录",
      //   "系统提示",
      //   {
      //     confirmButtonText: "重新登录",
      //     cancelButtonText: "取消",
      //     type: "warning",
      //   }
      // ).then(() => {
      //   // store.dispatch("LogOut").then(() => {
      //   //   location.reload(); // 为了重新实例化vue-router对象 避免bug
      //   // });
      // });
    } else if (code === 500) {
      Message({
        message: message,
        type: "error",
      });
      return Promise.reject(new Error(message));
    } else if (code === 503) {
      Message({
        message: res.data.message,
        type: "error",
      });
      return Promise.reject(new Error(res.data.message));
    } else if (code == 1) {
      // if (message.indexOf("平台未注册") != -1) {
      //   MessageBox.confirm(message, "系统提示", {
      //     confirmButtonText: "去注册",
      //     cancelButtonText: "取消",
      //     type: "warning",
      //   }).then(() => {
      //     location.href = "./initRegister";
      //   });
      // } else if (message.indexOf("授权已到期") != -1) {
      //   MessageBox.confirm("授权已到期，请申请新的注册文件！", "系统提示", {
      //     confirmButtonText: "去注册",
      //     cancelButtonText: "取消",
      //     type: "warning",
      //   }).then(() => {
      //     location.href = "./initRegister";
      //   });
      // } else {
      //   message &&
      //     Message({
      //       message: message,
      //       //message: error.response.data.message,
      //       type: "error",
      //       duration: 5 * 1000,
      //     });
      // }
    } else if (code !== 200) {
      message &&
        Notification.error({
          title: message,
        });
      return Promise.reject("error");
    } else {
      return JSON.parse(JSON.stringify(res.data));
    }
  },
  (error) => {
    if (undefined == error.response) {
      return false;
    }
    if (
      error.response.config &&
      "string" != typeof error.response.config &&
      error.response.config.method === "post"
    ) {
      removePending(error.response.config);
    }
    if (
      "0000_24" == error.response.data.status &&
      "身份鉴权失败" == error.response.data.message
    ) {
      // MessageBox.confirm(
      //   "登录状态已过期，您可以继续留在该页面或重新登录",
      //   "系统提示",
      //   {
      //     confirmButtonText: "重新登录",
      //     cancelButtonText: "取消",
      //     type: "warning",
      //   }
      // ).then(() => {
      //   // store.dispatch("LogOut").then(() => {
      //   //   location.reload(); // 为了重新实例化vue-router对象 避免bug
      //   // });
      // });
    } else if (error.response.data.message.indexOf("平台未注册") != -1) {
      // MessageBox.confirm(error.response.data.message, "系统提示", {
      //   confirmButtonText: "去注册",
      //   cancelButtonText: "取消",
      //   type: "warning",
      // }).then(() => {
      //   location.href = "./initRegister";
      // });
    } else if (error.response.data.message.indexOf("授权已到期") != -1) {
      // MessageBox.confirm("授权已到期，请申请新的注册文件！", "系统提示", {
      //   confirmButtonText: "去注册",
      //   cancelButtonText: "取消",
      //   type: "warning",
      // }).then(() => {
      //   location.href = "./initRegister";
      // });
    } else {
      var message = error.response.data.message;
      if (undefined == error.response.data.status) {
        message = "数据异常，请稍后再试！";
      }
      message &&
        Message({
          message: message,
          //message: error.response.data.message,
          type: "error",
          duration: 5 * 1000,
        });
    }
    return Promise.reject(error);
  }
);

export default service;

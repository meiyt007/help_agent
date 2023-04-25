/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-15 15:22:35
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-15 15:29:04
 * @FilePath: \helpGetNumber\js\http.js
 * @Description: 封装axios请求
 */
/** axios封装
 * 请求拦截、相应拦截、错误统一处理
 */
 const httprequest = axios.create({
    baseURL: "http://localhost:8091/venus",
    timeout: 300000,
    headers: {
      token: "",
    },
  });
   
   
  //响应拦截
  httprequest.interceptors.response.use(
    function (response) {
      const res = response.data;
      if (res.code === 500) {
        alert('请求失败')
      }
      return response;
    },
    function (error) {
      return Promise.reject(error);
    }
  );
   
  function get(url, params) {
    return new Promise((resolve, reject) => {
      httprequest
        .get(url, { params: params })
        .then((res) => {
          resolve(res.data);
        })
        .catch((err) => {
          reject(err.data);
        });
    });
  }
  // qs.stringify(data)
  function post(url, data) {
    return new Promise((resolve, reject) => {
      httprequest
        .post(url, data)
        .then((res) => {
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  }
   
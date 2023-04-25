/**
 * 高拍仪硬件驱动js
 */

import axios from "axios";

//定义端口号和配置硬件连接路径
const baseUrl = `${process.env.VUE_APP_GAOPAIYI_API}`;

const req = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: baseUrl,
  // 超时
  timeout: 10000
});

const singleOpenTypeHigh = "high";
const singleOpenTypeFace = "face";

const GPYDrive = {
  isOpen: false,
  singleOpenType: "",
  //打开高拍仪
  openHighCamera() {
    const that = this;
    return new Promise((resolve, reject) => {
      req
        .get("/openHighCamera")
        .then(data => {
          that.isOpen = true;
          that.singleOpenType = singleOpenTypeHigh;
          console.log("打开高拍仪成功", data);
          resolve({ code: 200, data: data, msg: "" });
        })
        .catch(err => {
          console.error(`打开高拍仪出错了${err}`);
          resolve({ code: 500, data: null, msg: "打开高拍仪出错" });
        });
    });
  },

  //拍照
  getHighPicture() {
    return new Promise((resolve, reject) => {
      req
        .get("/getHighPicture")
        .then(data => {
          if (data && data.status === 200) {
            let content = JSON.parse(data.data);

            resolve({ code: 200, data: content.data, msg: "" });
          } else {
            resolve({ code: 200, data: null, msg: "拍照出错" });
          }
          resolve({ code: 200, data: data, msg: "" });
        })
        .catch(err => {
          console.error(`拍照出错了${err}`);
          resolve({ code: 500, data: null, msg: "拍照出错" });
        });
    });
  },

  //openFaceCamera 打开人脸摄像头
  openFaceCamera() {
    const that = this;
    return new Promise((resolve, reject) => {
      req
        .get("/openFaceCamera")
        .then(data => {
          that.isOpen = true;
          that.singleOpenType = singleOpenTypeFace;
          console.log("打开人脸相机成功", data);
          resolve({ code: 200, data: data, msg: "" });
        })
        .catch(err => {
          console.error(`打开人脸摄像头出错了${err}`);
          resolve({ code: 500, data: null, msg: "打开人脸摄像头出错" });
        });
    });
  },

  //人脸相机拍照
  getFacePicture() {
    return new Promise((resolve, reject) => {
      req
        .get("/getFacePicture")
        .then(data => {
          if (data && data.status === 200) {
            let content = JSON.parse(data.data);

            resolve({ code: 200, data: content.data, msg: "" });
          } else {
            resolve({ code: 200, data: null, msg: "拍照出错" });
          }
          resolve({ code: 200, data: data, msg: "" });
        })
        .catch(err => {
          console.error(`拍照出错了${err}`);
          resolve({ code: 500, data: null, msg: "拍照出错" });
        });
    });
  },

  //关闭人脸相机
  closeFaceCamera() {
    const that = this;
    return new Promise((resolve, reject) => {
      req
        .get("/closeFaceCamera")
        .then(data => {
          that.isOpen = false;
          console.log(`关闭人脸摄像成功`);
          resolve({ code: 200, data: data, msg: "" });
        })
        .catch(err => {
          console.error(`关闭人脸摄像出错了${err}`);
          resolve({ code: 500, data: null, msg: "关闭关闭人脸摄像出错" });
        });
    });
  },

  //读取身份证
  readCardInfo() {
    return new Promise((resolve, reject) => {
      try {
        req
          .get("/readCardInfo")
          .then(data => {
            if (data && data.data) {
              let content = JSON.parse(data.data);
              resolve({ code: 200, data: JSON.parse(content.data), msg: "" });
            } else {
              resolve({ code: 500, data: null, msg: "读取身份证信息失败" });
            }
          })
          .catch(err => {
            console.error(`读取身份证信息失败${err}`);
            resolve({ code: 500, data: null, msg: "读取身份证信息失败" });
          });
      } catch (err) {
        console.error(`读取身份证信息失败${err}`);
        resolve({ code: 500, data: null, msg: "读取身份证信息失败" });
      }
    });
  },

  //人证比对
  personVerify() {
    return new Promise((resolve, reject) => {
      try {
        req
          .get("/personVerify")
          .then(data => {
            if (data && data.data) {
              let content = JSON.parse(data.data);

              if (!content.success) {
                resolve({ code: 500, data: "", msg: content.message });
              }
              resolve({ code: 200, data: content.data, msg: "" });
            } else {
              resolve({ code: 500, data: null, msg: "人证比对失败" });
            }
          })
          .catch(err => {
            console.error(`人证比对失败${err}`);
            resolve({ code: 500, data: null, msg: "人证比对失败" });
          });
      } catch (err) {
        console.error(`人证比对失败${err}`);
        resolve({ code: 500, data: null, msg: "人证比对失败" });
      }
    });
  },

  //关闭高拍仪
  closeHighCamera() {
    const that = this;
    return new Promise((resolve, reject) => {
      req
        .get("/closeHighCamera")
        .then(data => {
          that.isOpen = false;
          console.log(`关闭高拍仪成功`, data);
          resolve({ code: 200, data: data, msg: "" });
        })
        .catch(err => {
          console.error(`关闭高拍仪出错了${err}`);
          resolve({ code: 500, data: null, msg: "关闭高拍仪出错" });
        });
    });
  }
};

export default GPYDrive;

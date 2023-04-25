<template>
  <div class="hi-speed-camera">
    <div class="hi-speed-camera--title">
      <span>{{ isGpy ? "正在拍照：" : "正在扫描：" }}</span>
      <div v-if="!notUseGsy" class="hi-speed-camera--title-device">
        <el-checkbox v-model="checked" true-label="1" false-label="0">高拍仪</el-checkbox>
        <el-checkbox v-model="checked" :disabled="loadingGpy" true-label="0" false-label="1">高扫仪</el-checkbox>
      </div>
    </div>
    <div class="scan-area" v-loading="(loadingGpy && gotItVisible) || loadingMaterials" :element-loading-text="loadingText" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
      <div class="scan-area__mask" v-if="isGpy && !gotItVisible && !isGpyOpen">
        <img src="@/assets/image/gpy/gpy-tip.png" height="209" width="230" :key="0" :style="{ objectFit: 'contain' }" alt />
        <div class="scan-area__mask-tips">
          <span>请确认高拍仪</span>
          <span class="color">对焦完成</span>
          <span>且图片</span>
          <span class="color">预览清晰</span>
          <span>时点击拍摄按钮</span>
        </div>
        <div class="scan-area__mask-btn" v-animate @click="gotIt">明白了</div>
        <div class="scan-area__mask-check">
          <el-checkbox v-model="isNotShowGotItVisible" @change="handleChange">下次不再提示此条信息</el-checkbox>
        </div>
      </div>
      <div v-else-if="isGpy && (gotItVisible || isGpyOpen)" style="overflow: hidden; width: 100%; height: 100%">
        <img ref="gpyImg" :src="gpyImgSrc" :key="1" height="100%" width="100%" :style="{ objectFit: isGpyOpen ? 'contain' : 'unset' }" alt />
      </div>
      <img v-else :src="require('@/assets/image/gpy/gsy-bg.png')" :key="2" height="260px" width="100%" :style="{
          objectFit: 'contain',
          margin: '70px auto 0',
          zIndex: isGsy && visibleGsy ? 10001 : 'inherit',
          position: isGsy && visibleGsy ? 'absolute' : '',
        }" alt />
      <div v-if="isGsy && visibleGsy" class="scan-doing">
        <div class="scaning"></div>
        <span>
          正在扫描中
          <span class="dotting"></span>
        </span>
      </div>
      <div class="scan-btn" :style="{
          'justify-content': isGsy ? 'center' : 'space-between',
          background: 'rgba(9, 11, 21,.9)',
        }">
        <!-- <img id="bigPriDev" src="" alt=""> -->
        <template v-if="isGpy">
          <!-- 左旋转 -->
          <div class="rotate-left" @click="qWebChannelDialog ? Rrotate() : handleRotate('left')" />

          <!-- 拍摄按钮 -->
                    <!-- 良田高拍仪 -->
          <img v-if="gpyType==='GUO_CHAN_HUA'" v-animate id="gpyStart" @mouseover="showHintInfo" @mouseleave="hideHintInfo" @click="$emit('takePhoto')" src="@/assets/image/scan-btn.png" alt />
          <img  v-else v-animate id="gpyStart" @mouseover="showHintInfo" @mouseleave="hideHintInfo" @click="startGpyView" src="@/assets/image/scan-btn.png" alt />
          <!-- 右旋转 -->
          <div class="rotate-right" @click="qWebChannelDialog ? Rrotate() : handleRotate('right')" />
        </template>

        <template v-else>
          <div v-if="!visibleGsy" class="scan-btn--start" @click="handleStartScan">
            开始扫描
          </div>
          <div v-else class="scan-btn--over" @click="handleCloseScan">
            结束扫描
          </div>
        </template>
        <!-- 当前模式：智能分类上传 -->
        <div class="intelligent-classify">
          <img @mouseover="showModeText = true" @mouseleave="showModeText = false" v-if="targetMode === 0" src="@/assets/image/gpy/intell.png" width="21" height="21" alt />
          <img @mouseover="showModeText = true" @mouseleave="showModeText = false" v-else src="@/assets/image/gpy/intell1.png" width="21" height="21" alt />
          <div v-if="showModeText" @mouseover="showModeText = true" @mouseleave="showModeText = false" class="intelligent-classify--mode">
            {{ targetModeText }}
          </div>
        </div>
      </div>
      <div v-if="isGpy" class="hint" :class="{ active: showHint }" @mouseover="showHintInfo" @mouseleave="hideHintInfo">
        拍照按钮(快速拍照：Enter)
      </div>
    </div>
    <!-- 是否正在扫描文件 -->
    <ScanLoading v-if="visibleGsy" />
  </div>
</template>

<script>
  // 接口api
  import { QWebChannel } from "@/utils/qwebchannel.js";
  // src\views\zc\businessManagement\windowAcceptance\dialogs\gpy-upload.js
  // import {} from '@/views/zc/businessManagement/windowAcceptance/dialogs/gpy-upload.js'
  import { uploadMaterialsFile } from "@/api/materialCategory.js";
  import {
    startPreview,
    getFrame,
    rotateVideo,
    getCamerasNum,
    stopPreview,
  } from "@/api/gaopaiyi.js";
  import {
    startnewPreview,
    stopnewPreview,
    takenewPhoto,
    rotateVideo as rotateVideoV3,
  } from "@/api/handwarev3.js";
  import { getConfigByCode } from "@/api/sys/config";
  import sleep from "@/utils/sleep";

  import ScanLoading from "./scan-loading.vue";
  import GPYDrive from "@/api/handwareDrive.js";

  const GPY_FLAG = "1",
    GSY_FLAG = "0";

  import DEVEICETYPE, {
    GAO_PAI_YI_V1,
    GAO_PAI_YI_V2,
    GAO_PAI_YI_V3,
    GAO_PAI_YI_V4,
  } from "./config.js";
  export default {
    name: "HiSpeedCamera",
    components: { ScanLoading },
    props: {
      /** 不适用高扫仪 */
      notUseGsy: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        GPY_FLAG, // 高拍仪标志
        GSY_FLAG, // 高扫仪标志
        gpyImgSrc: require("@/assets/image/gpy/gpy-bg.png"), // 高拍仪照片
        timer: null,
        loadingGpy: false,
        isGpyOpen: false,
        showHint: false, // 触摸
        rotateCount: 3, // 高拍仪旋转角度 0: 0度; 1：90度；2：180度；3：270度（可以在视频打开之前调用)
        failCounts: 0, // 失败次数
        checked: "1",
        websocket: null,
        visibleGsy: false,
        loadingText: "正在启动设备",
        loadingMaterials: false, // 加载材料
        gotItVisible: false, // 明白了
        isNotShowGotItVisible: false, // 不在提示
        taskCamera: null,
        taskCamerav3: null,
        targetMode: 0, // 0 智能分类 1 指定材料
        showModeText: false,
        //BEGIN SETUP
        //拍照数据（base64）

        baseUrl: "ws://127.0.0.1:12345",
        socket: "",
        qWebChannelDialog: null,
        paizhaoImg: "",
        counts: 1,
        targetUploadFolder: null,
        fujianPhotoinfo: null,
        gpyType:''
      };
    },
    computed: {
      /** 是否是高拍仪 */
      isGpy({ checked, GPY_FLAG }) {
        return checked === GPY_FLAG;
      },
      /** 是否是高扫仪 */
      isGsy({ checked, GSY_FLAG }) {
        return checked === GSY_FLAG;
      },

      targetModeText() {
        return `当前模式：${this.targetMode === 0 ? "智能分类" : "指定材料"}上传`;
      },
    },
    watch: {
      checked: {
        immediate: true,
        handler(value) {
          if (!value) return;
          if (value === this.GPY_FLAG && !this.isNotShowGotItVisible) {
            this.gotItVisible = false;
          }
          this.$nextTick(() => {
            this.initDevice();
          });
        },
      },
    },
    created() {
      // if (localStorage.NOT_SHOW_GOT_IT === "true") {
      //   this.isNotShowGotItVisible = true;
      //   this.gotItVisible = true;
      // }
      // if (this.notUseGsy) {
      //   this.checked = this.GPY_FLAG;
      // } else {
      //   this.getConfigByCode();
      // }
      // // 快捷键enter键拍照
      // this.keyBoardEvent = e => {
      //   if (e.keyCode === 13) {
      //     this.isGpy ? this.$emit("takePhoto") : this.handleStartScan();
      //   }
      // };
      // document.addEventListener("keyup", this.keyBoardEvent);
    },
    mounted() {
      this.gpyType = DEVEICETYPE.GPY_CONFIG
      this.startCamera();
    },
  async beforeDestroy () {
    try {
      this.isGpy && this.closeGpy();
      this.isGsy && this.closeWebsocket();
      document.removeEventListener("keyup", this.keyBoardEvent);
      if (GPYDrive.isOpen) {
        await GPYDrive.closeHighCamera();
      }
      if (this.taskCamera) {
        clearInterval(this.taskCamera);
      }
      if (this.taskCamerav3) {
        clearInterval(this.taskCamerav3);
      }
    } catch (error) {
      console.log("%c [beforeDestroy]:", "color:red;font-weight:700;", error);
    }
  },

    destroyed() {
      if (this.qWebChannelDialog) {
        this.qWebChannelDialog.get_actionType("closeSignal");
      }
      if (this.socket) {
        this.socket.close();
      }
    },
    methods: {
      // 鄂尔多斯项目方法起始点1111111111111111111111111111111111111111111111111
      // 拍照
      startGpyView() {
        // this.openSocket(),
        // this.socket.close()
        console.log("startGpyView--");
        this.getSaveImg();

        // console.log(this.fujianPhotoinfo)
        // that.$bus.$emit('handleMaterialClassify',this.fujianPhotoinfo)
        // this.socket.close();
        // this.openSocket();
        //  this.qWebChannelDialog.devChanged("primaryDev_:WW690R" );
        //  this.socket.close();
      },
      //启动高拍仪
      openSocket() {
        console.log("this.socket");
        const that = this;
        this.socket = new WebSocket(this.baseUrl);
        this.socket.onclose = function () {
          console.error("web channel closed");
        };
        this.socket.onerror = function (error) {
          console.error("web channel error: " + error);
        };

        this.socket.onopen = function () {
          new QWebChannel(that.socket, function (channel) {
            that.qWebChannelDialog = channel.objects.dialog;
            that.qWebChannelDialog.devChanged("primaryDev_:WW690R");
            //  that.qWebChannelDialog.devChanged("primaryDev_:设备1" );
            console.log("-------------------------");
            that.qWebChannelDialog.send_priImgData.connect(function (message) {
              that.gpyImgSrc = `data:image/jpeg;base64,${message}`;
            });

            that.qWebChannelDialog.html_loaded("two");
          });
        };
      },
      // 获取鄂尔多斯良田高拍仪拍照图片并设置名称进行保存

      getSaveImg() {
        const that = this
        return new Promise((resolve, reject) => {
          this.qWebChannelDialog.send_priPhotoData.connect(function (message) {
            console.log('message11111');
            that.$bus.$emit('message', message)

            that.paizhaoImg = message;
            // let res = that.uploadGPYImg(that.paizhaoImg)
            //huoqufanhuijieguo
            // res.then(
            //   (result)=>{
            //    that.fujianPhotoinfo = result.data
            //    that.$bus.$emit('handleMaterialClassify',result.data)

            //   }

            // )
            // console.log('wodeshu',res)
            //   console.log(
            //     "paizhao这里代码后执行了,我想让这异步代码后执行",
            //     that.paizhaoImg
            //   );
            resolve(that.paizhaoImg);
            // console.log(that.paizhaoImg)
            // 照片保存地址
            // this.qWebChannelDialog.set_configValue("set_savePath:C:/eloamFile/pdf");
          });
          // console.log("this.qWebChannelDialog", this.qWebChannelDialog);
          // console.log('下面代码先执行了')
          // that.$bus.$emit('paizhao',this.paizhaoImg)
          this.counts++;
          this.qWebChannelDialog.set_configValue(
            `set_saveName:图片材料${this.counts}`
          );
          this.qWebChannelDialog.photoBtnClicked("primaryDev_");
          this.qWebChannelDialog.get_actionType("savePhotoPriDev");
          // console.log('11111111111',this.paizhaoImg)
          // this.uploadGPYImg(that.paizhaoImg)
        }).then(() => {
          that.$emit("takePhoto");
          console.log("下面代码先执行了");
          that.$bus.$emit("paizhao", this.paizhaoImg);
        });
      },
      // 图片左转
      Lrotate() {
        this.qWebChannelDialog.get_actionType("rotateLeft");
        alert("向左转");
      },
      // 图片右转
      Rrotate() {
        this.qWebChannelDialog.get_actionType("rotateRight");
      },
      // 拍照并处理
      uploadGPYImg(img) {
        let file = this.base64ToFile(img, Date.now() + ".jpg");
        let formData = new FormData();
        formData.append("files", file);
        // this.handleUploadImg(formData);
        return uploadMaterialsFile(formData);
        // const res = await takePic();
        //     if (res.StateCode != 0) {
        //     this.$message.warning(returnMsg || '拍摄失败');
        //     this.startDistinguish = false;
        //     this.clearDotsFolder();
        //     return;
        //   }
      },
      // startnewPreview() {
      //   this.rotateCount = 2;
      //   this._handleRotate({ count: this.rotateCount });
      //   startnewPreview()
      //     .then((res) => {
      //       if (res.StateCode == 0 || res.StateCode == 12) {
      //         this.getPic();
      //       } else {
      //         this.loadingGpy = false;
      //         this.isGpyOpen = false;
      //         return this.$message.warning("请确认设备连接是否正常");
      //       }
      //     })
      //     .catch(this.openGpyFail);
      // },
      // 图片转文件对象
      base64ToFile(urlData, fileName) {
        let bytes = atob(urlData); // 解码base64
        let n = bytes.length;
        let ia = new Uint8Array(n);
        while (n--) {
          ia[n] = bytes.charCodeAt(n);
        }
        return new File([ia], fileName, {
          type: "image/jpeg",
        });
      },
      // 鄂尔多斯项目方法起始点11111111111111111111111111111111111111111111111111

      /** 判断默认是高拍仪还是高扫仪 */
      getConfigByCode() {
        getConfigByCode("MUDC")
          .then(({ code, data }) => {
            if (code === 200) {
              this.checked = data?.value ?? this.GPY_FLAG;
            } else {
              this.checked = this.GPY_FLAG;
            }
          })
          .catch(() => {
            this.checked = this.GPY_FLAG;
          });
      },
      /** 初始化设备 */
      async initDevice() {
        if (
          this.checked === this.GPY_FLAG &&
          !this.gotItVisible &&
          !this.isNotShowGotItVisiblestartCamera
        )
          return;
        this.loadingGpy = this.isGpy;
        try {
          await this.closeGpy();
          this.closeWebsocket();
          if (this.isGpy) {
            this.startCamera();
          } else {
            this.startScan();
          }
        } catch (error) {
          console.log("%c [initDevice]:", "color:red;font-weight:700;", error);
        }
      },

      /** 启动高拍仪 */
      async startCamera() {
        console.log("ddddddd",DEVEICETYPE.GPY_CONFIG)
        switch (DEVEICETYPE.GPY_CONFIG) {
          case GAO_PAI_YI_V2:
            this.startHighCamera();
            break;
          case GAO_PAI_YI_V1:
            this.startGetCamerasNum();
            break;
          case GAO_PAI_YI_V3:
            this.startnewPreview();
            break;
          case GAO_PAI_YI_V4:
            this.loadingGpy = false;
            this.isGpyOpen = true;
            this.openSocket();
            break;
        }
      },

      /** --------------------------------------- 良田高拍仪 版本2 --------------------------------------- */
      async startHighCamera() {
        const res = await GPYDrive.openHighCamera();
        if (!res || res.code !== 200) {
          this.loadingGpy = false;
          this.isGpyOpen = false;
          return this.msgWarning("高拍仪摄像头连接失败！");
        }
        this.startCameraTask(); //开启高拍仪任务
      },

      startCameraTask() {
        //开启高拍仪任务
        const that = this;
        //开始预览拍照任务，模拟摄像持续 200 ms执行一次
        this.taskCamera = setInterval(async () => {
          let res = null;

          if (GPYDrive.isOpen) {
            if (GPYDrive.singleOpenType === "face") {
              res = await GPYDrive.getFacePicture();
            }

            if (GPYDrive.singleOpenType === "high") {
              res = await GPYDrive.getHighPicture();
            }
            if (res && res.code === 200) {
              if (res.data && res.data !== "undefined") {
                that.gpyImgSrc = `data:image/jpeg;base64,${res.data}`;
                this.loadingGpy = false;
                this.isGpyOpen = true;
              }
              this.$forceUpdate();
            }
          }
        }, 200);
      },
      /** --------------------------------------- 良田高拍仪结尾 版本2 --------------------------------------- */

      /** --------------------------------------- 综窗api高拍仪 版本3 --------------------------------------- */
      startnewPreview() {
        // 先转个角度
        this.rotateCount = 2;
        this._handleRotate({ count: this.rotateCount });
        startnewPreview()
          .then((res) => {
            if (res.StateCode == 0 || res.StateCode == 12) {
              this.getPic();
            } else {
              this.loadingGpy = false;
              this.isGpyOpen = false;
              return this.$message.warning("请确认设备连接是否正常");
            }
          })
          .catch(this.openGpyFail);
      },
      getPic() {
        this.taskCamerav3 = setInterval(() => {
          takenewPhoto().then((res) => {
            const that = this;
            if (res && res.StateCode == 0) {
              if (res.data && res.data !== "undefined") {
                that.gpyImgSrc = `data:image/jpeg;base64,${res.data}`;
                this.loadingGpy = false;
                this.isGpyOpen = true;
              }
              this.$forceUpdate();
            } else {
              clearInterval(this.taskCamerav3);
              return this.$message.error("读取图像失败");
            }
          });
        }, 200);
      },
      /** --------------------------------------- 综窗api高拍仪结尾 版本3 --------------------------------------- */

      /** --------------------------------------- 启动高扫仪 --------------------------------------- */
      startScan() {
        //判断当前浏览器是否支持WebSocket
        if ("WebSocket" in window) {
          const wss = "ws://127.0.0.1:9097";
          this.websocket = new WebSocket(wss); // 这里会传递一些数据，
        } else {
          return this.$message.error("当前浏览器 Not support websocket");
        }
        //连接发生错误的回调方法
        this.websocket.onerror = () => {
          console.log("高扫仪WebSocket连接发生错误");
          this.$message.error("高扫仪服务连接失败, 请联系系统管理员");
          this.closeWebsocket();
        };
        //连接成功建立的回调方法
        this.websocket.onopen = () => {
          console.log("高扫仪WebSocket连接成功");
          // this.$message.success('已连接到高扫仪');
        };
        //接收到消息的回调方法
        this.websocket.onmessage = (event) => {
          const data = JSON.parse(event?.data || "{}");
          if (data.pagenum && data.pagenum < 0) {
            console.log(
              "%c [高扫仪扫描失败]:",
              "color:red;font-weight:700;",
              event
            );
            this.$message.error("高扫仪扫描失败,请联系管理员");
            this.handleCloseScan();
            return;
          }

          if (data.pagenum && data.message !== "") {
            this.$emit("sendScanMessage", data);
          }

          /** 结束 */
          if (data.pagenum === "0") {
            this.visibleGsy = false;
          }
        };
        //连接关闭的回调方法
        this.websocket.onclose = () => {
          console.log("高扫仪WebSocket连接关闭");
        };
      },

      closeWebsocket() {
        try {
          if (this.websocket) {
            this.websocket.close();
            this.websocket = null;
          }
        } catch (error) {
          console.log(error);
        }
      },

      handleStartScan() {
        // 开始
        this.websocket.send("startscaner");
        this.visibleGsy = true;
      },

      handleCloseScan() {
        this.websocket.send("stopscaner");
        this.visibleGsy = false;
      },

      /** --------------------------------------- 结束高扫仪 --------------------------------------- */

      showHintInfo() {
        this.showHint = true;
      },

      hideHintInfo() {
        this.showHint = false;
      },

      /** --------------------------------------- 高拍仪 版本1 --------------------------------------- */
      // 高拍仪旋转代码
      handleRotate(driection) {
        // 0: 0度; 1：90度；2：180度；3：270度（可以在视频打开之前调用)
        if (driection === "left") {
          if (--this.rotateCount === -1) {
            this.rotateCount = 3;
          }
        } else {
          if (++this.rotateCount === 4) {
            this.rotateCount = 0;
          }
        }
        this._handleRotate({ count: this.rotateCount });
      },

      _handleRotate(params) {
        switch (DEVEICETYPE.GPY_CONFIG) {
          case GAO_PAI_YI_V1:
            rotateVideo(params);
            break;
          case GAO_PAI_YI_V3:
            // rotateVideoV3({ value: params.count });
            if (this.$refs.gpyImg) {
              this.$refs.gpyImg.style = `object-fit:${this.isGpyOpen ? "contain" : "unset"
                };transform:rotate(${(this.rotateCount + 1) * 90}deg)`;
            }
            break;
        }
      },

      startGetCamerasNum() {
        this.getCamerasNum()
          .then(({ returnCode, returnMsg, data }) => {
            if (data === 0) {
              this.loadingGpy = false;
              this.isGpyOpen = false;
              return this.$message.warning("请确认设备连接是否正常");
            }
            if (returnCode !== 0) {
              return this.openGpyFail(returnMsg);
            } else {
              this.openGpy();
            }
          })
          .catch(this.openGpyFail);
      },

      // 启动高拍仪服务
      getCamerasNum() {
        return getCamerasNum();
      },

      // 高拍仪开始失败
      openGpyFail() {
        this.loadingGpy = false;
        this.isGpyOpen = false;
        this.$message.warning("设备加载失败");
      },

      openGpy() {
        startPreview({
          res_id: 0,
          pixfmt: "pixfmt",
        })
          .then(async (data) => {
            if (data.returnCode === 0) {
              // 先转个角度
              await this._handleRotate({ count: this.rotateCount });
              // 定时器轮训 延迟一秒请求
              await sleep();

              this.startGetFrames();
            } else {
              this.openGpyFail(returnMsg);
            }
          })
          .catch(this.openGpyFail);
      },

      // 获取高拍仪视频图片
      startGetFrames() {
        this.timer && clearInterval(this.timer);
        this.timer = setInterval(async () => {
          const { data: _data, returnCode } = await getFrame();
          if (returnCode !== 0) {
            // 做兜底操作 失败大于10次 就停止请求
            this.failCounts++;
            if (this.failCounts > 10) {
              console.warn("设备加载失败", this.failCounts);
              clearInterval(this.timer);
              return this.closeGpy();
            }
            return;
          }
          this.gpyImgSrc = "data:image/jpg;base64," + _data.img;
          this.loadingGpy = false;
          this.isGpyOpen = true;
        }, 200);
      },

      async closeGpy() {
        if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1) {
          clearInterval(this.timer);
          return stopPreview({
            dev_idx: 1,
          }).then(() => {
            this.isGpyOpen = false;
            this.loadingGpy = false;
            this.$nextTick(() => {
              this.gpyImgSrc = require("@/assets/image/gpy/gpy-bg.png");
            });
          });
        }
        if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
          if (GPYDrive.isOpen) {
            await GPYDrive.closeHighCamera();
          }
          clearInterval(this.taskCamera);
          if (GPYDrive.isOpen) {
            return closeHighCamera().then(() => {
              this.isGpyOpen = false;
              this.loadingGpy = false;
              this.$nextTick(() => {
                this.gpyImgSrc = require("@/assets/image/gpy/gpy-bg.png");
              });
            });
          }
        }
        if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
          clearInterval(this.taskCamerav3);
          return stopnewPreview().then(() => {
            this.isGpyOpen = false;
            this.loadingGpy = false;
            this.$nextTick(() => {
              this.gpyImgSrc = require("@/assets/image/gpy/gpy-bg.png");
            });
          });
        }
      },

      restart() {
        this.loadingGpy = true;
        this.getCamerasNum()
          .then(({ returnCode, returnMsg, data }) => {
            if (returnCode !== 0) {
              return this.openGpyFail(returnMsg);
            } else {
              this.openGpy();
            }
          })
          .catch(this.openGpyFail);
      },

      /** --------------------------------------- 高拍仪结尾 版本1 --------------------------------------- */

      gotIt() {
        this.gotItVisible = true;
        this.loadingGpy = true;
        this.initDevice();
      },

      handleChange(value) {
        localStorage.setItem("NOT_SHOW_GOT_IT", value);
      },

      setModeToggle() {
        this.targetMode = this.targetMode === 0 ? 1 : 0;
      },
    },
  };
</script>

<style scoped lang="scss">
  .hi-speed-camera {
    .hi-speed-camera--title {
      font-size: 16px;
      font-weight: 400;
      color: #232f51;
      // display: block;
      height: 45px;
      background: #cfd3df;
      // padding-top: 17px;
      padding-left: 20px;
      border-radius: 5px 5px 0px 0px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .hi-speed-camera--title-device {
        background: url('~@/assets/image/gpy/gpy2gsy.png') center no-repeat;
        background-size: 100% 100%;
        height: 100%;
        width: 230px;
        display: flex;
        align-items: center;
        justify-content: center;

        ::v-deep .el-checkbox {
          .el-checkbox__label {
            color: #232f51;
          }

          .el-checkbox__input.is-checked + .el-checkbox__label {
            color: #fff;
          }
        }
      }
    }

    .scan-area {
      background-color: #000;
      border-radius: 0px 0px 5px 5px;
      height: 496px;
      position: relative;

      .scan-area__mask {
        display: flex;
        align-items: center;
        flex-direction: column;

        > img {
          margin-top: 60px;
        }

        .scan-area__mask-tips {
          margin-top: 18px;
          font-size: 16px;
          font-weight: 400;
          color: #b5dafd;

          .color {
            font-size: 16px;
            font-weight: bold;
            color: #00ccff;
            line-height: 27px;
          }
        }

        .scan-area__mask-btn {
          width: 72px;
          background: rgba(24, 144, 255, 0.5);
          border: 1px solid #1890ff;
          border-radius: 17px;
          margin-top: 18px;
          font-size: 16px;
          color: #3297ff;
          cursor: pointer;
          user-select: none;
          box-sizing: border-box;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 3px;
        }

        .scan-area__mask-check {
          font-size: 12px;
          font-weight: 400;
          color: #737b83;
          margin-top: 30px;
        }
      }

      .scan-pic {
        width: 100%;
        height: 587px;
      }

      .scan-doing {
        position: absolute;
        left: 165px;
        bottom: 100px;
        display: flex;
        align-items: center;
        z-index: 10001;

        .scaning {
          background: url('~@/assets/image/gpy/scaning.png') no-repeat;
          width: 70px;
          height: 35px;
          animation: scanner 1.5s steps(13) infinite;
        }

        span {
          font-size: 16px;
          font-weight: 400;
          color: #2b5279;
        }
      }

      .scan-btn {
        background-color: rgba(42, 44, 47, 1);
        border-radius: 0px 0px 5px 5px;
        position: absolute;
        width: 100%;
        height: 74px;
        bottom: 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20px;

        img {
          width: 55px;
          height: 55px;
          cursor: pointer;
        }

        .rotate-right,
        .rotate-left {
          width: 32px;
          height: 30px;
          cursor: pointer;
        }

        .rotate-left {
          background: url(~@/assets/image/gpy/rotate-left.png) no-repeat center;
          background-size: 100% 100%;

          &:hover {
            background: url(~@/assets/image/gpy/rotate-left-hover.png) no-repeat
              center;
          }
        }

        .rotate-right {
          background: url(~@/assets/image/gpy/rotate-right.png) no-repeat center;
          background-size: 100% 100%;

          &:hover {
            background: url(~@/assets/image/gpy/rotate-right-hover.png) no-repeat
              center;
          }
        }

        .scan-btn--start {
          width: 102px;
          height: 27px;
          background: rgba(24, 144, 255, 0.5);
          border: 1px solid #1890ff;
          border-radius: 17px;
          font-size: 16px;
          font-weight: 400;
          color: #3297ff;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .scan-btn--over {
          position: absolute;
          left: 203px;
          bottom: 22px;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 102px;
          height: 27px;
          background: rgba(251, 162, 34, 0.3);
          border: 1px solid #fba222;
          border-radius: 17px;
          font-size: 16px;
          font-weight: 400;
          color: #ffb932;
          z-index: 10001;
          cursor: pointer;
        }
      }

      .hint {
        width: 250px;
        height: 35px;
        color: #fff;
        font-size: 18px;
        background: url(~@/assets/image/scan-bg.png) no-repeat;
        background-size: 100% 100%;
        line-height: 44px;
        text-align: center;
        position: absolute;
        bottom: -10px;
        left: 49%;
        transform: translateX(-50%);
        display: none;
      }

      .hint.active {
        display: block;
      }

      .intelligent-classify {
        position: absolute;
        width: 21px;
        height: 21px;
        left: 360px;

        img {
          width: 21px;
          height: 21px;
        }

        .intelligent-classify--mode {
          width: 163px;
          height: 30px;
          background: url(~@/assets/image/scan-bg.png) no-repeat;
          background-size: 100% 100%;
          font-size: 12px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
          text-align: center;
          position: absolute;
          left: -72px;
          padding-top: 10px;
        }
      }
    }
  }

  @keyframes scanner {
    from {
      background-position-x: 0px;
    }

    to {
      background-position-x: -910px;
    }
  }
</style>

<template>
  <div class="gpy-upload">
    <div class="scan-picture">
      <!-- 高拍仪 -->
      <div class="scan-picture-left">
        <span class="scan-picture--title">请将材料放置在高拍仪镜头下方：</span>
        <div
          class="scan-area"
          v-loading="loadingGpy"
          element-loading-text="正在启动设备"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
        >
          <div class="scan-area__mask" v-if="!gotItVisible && !isGpyOpen">
            <img
              src="@/assets/image/gpy/gpy-tip.png"
              height="209"
              width="230"
              :key="0"
              :style="{ objectFit: 'contain' }"
              alt
            />
            <div class="scan-area__mask-tips">
              <span>请确认高拍仪</span>
              <span class="color">对焦完成</span>
              <span>且图片</span>
              <span class="color">预览清晰</span>
              <span>时点击拍摄按钮</span>
            </div>
            <div class="scan-area__mask-btn" v-animate @click="gotIt">
              明白了
            </div>
            <div class="scan-area__mask-check">
              <el-checkbox
                v-model="isNotShowGotItVisible"
                @change="handleChange"
                >下次不再提示此条信息</el-checkbox
              >
            </div>
          </div>
          <div v-else style="overflow: hidden; width: 100%; height: 100%">
            <img
              ref="gpyImg"
              :src="gpyImgSrc"
              height="496"
              width="100%"
              :style="{ objectFit: isGpyOpen ? 'contain' : 'unset' }"
              alt
            />
          </div>
          <div class="scan-btn">
            <!-- 左旋转 -->
            <div class="rotate-left" @click="handleRotate('left')" />
            <!-- 拍摄按钮 -->
            <img
              @mouseover="showHintInfo"
              @mouseleave="hideHintInfo"
              @click="takePhoto"
              src="@/assets/image/scan-btn.png"
              alt
            />
            <!-- 右旋转 -->
            <div class="rotate-right" @click="handleRotate('right')" />
          </div>
          <div
            class="hint"
            :class="{ active: showHint }"
            @mouseover="showHintInfo"
            @mouseleave="hideHintInfo"
          >
            拍照按钮（ 快捷键：Enter ）
          </div>
        </div>
      </div>
      <div class="scan-picture-right--content">
        <span class="scan-picture--title">已上传材料：</span>
        <!-- 高拍仪拍照文件夹列表 -->
        <div class="scan-result-dic">
          <div
            v-if="uploadPicList.length > 0"
            ref="scanResultList"
            class="scan-result-dic-list"
            :style="{
              'overflow-y': overflowY,
            }"
          >
            <!-- 高拍仪拍照文件夹列表 -->
            <div
              class="scan-result-dic-list--item"
              v-for="(item, idx) in uploadPicList"
              :key="idx"
            >
              <!-- popover 主内容区域 -->
              <div class="content" :id="`scan-popover-${idx}`">
                <div class="content-item drag-item">
                  <!-- 是否为空表图标 -->
                  <div
                    class="if-blank"
                    @click="
                      blankHandleItem(item.picRecordId, item.isEmpty, idx)
                    "
                  >
                    <img
                      src="@/assets/image/blank-icon.png"
                      alt
                      srcset
                      v-if="item.isEmpty == 1"
                    />
                    <img
                      src="@/assets/image/blank-icon-active.png"
                      alt
                      srcset
                      v-else-if="item.isEmpty == 0"
                    />
                  </div>

                  <img
                    src="@/assets/image/delete.png"
                    alt
                    width="22"
                    height="22"
                    class="content-item-close"
                    @click="deleteImageItem(idx, item.picRecordId)"
                  />
                  <el-image
                    style="width: 120px; height: 120px; z-indx: 1"
                    :src="previewImageUrl+item.fastdfsNginxUrl"
                    @click="handleImgClick(idx)"
                  >
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                  <el-image-viewer
                    v-if="showViewer"
                    :on-close="closeViewer"
                    :url-list="previewList"
                  />
                  <span
                    class="content-item-preview"
                    @click="handleImgClick(idx)"
                    >点击预览</span
                  >
                </div>
              </div>
            </div>
          </div>

          <el-empty v-else description="暂无材料"></el-empty>
          <!-- 底部按钮 -->
          <div class="scan-btn">
            <el-upload
              class="upload-picture"
              action="#"
              :http-request="uploadImage"
              :headers="myHeaders"
              list-type="picture"
              :show-file-list="false"
              accept=".gif, .GIF, .jpg, .JPG, .jpeg, .JPEG, .png, .PNG, .bmp, .BMP"
            >
              <el-button size="small" type="primary">本地上传</el-button>
            </el-upload>
          </div>
        </div>
      </div>
    </div>
    <!-- 说明 -->
    <div class="gyp-desrition">
      <h3>说明</h3>
      <div class="des-text">
        <p>
          材料分为
          <span>证照</span>和
          <span>表格两个大类:</span>
        </p>
        <span>【证照方面】</span>
        <p>
          要求根据《C0123+国家政务服务平台证照类型代码及目录信息》录入“
          <span>证照类型名称</span>”“ <span>证照类型代码</span>”两个字段。
          交付人员能够在增加训练数据图片之前，通过证照名称模糊匹配到“证照类型名称”和“证照类型代码”这两个字段。
          并且通过高拍仪拍摄10份证照材料
          <span>（必须有一份空模版，在命名中体现出来）</span
          >，一页一张，不能有遮挡，
          照片拍摄清晰无反光无光影，根据业务需求来判断是否要拍摄证照封面。
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import sleep from "@/utils/sleep";
import { QWebChannel } from "@/utils/qwebchannel.js";
// import DEVEICETYPE,{
//   GAO_PAI_YI_V1,
//   GAO_PAI_YI_V2,
//   GAO_PAI_YI_V3,
//   GAO_PAI_YI_V4,
// } from "@/components/HiSpeedCamera/config.js";
import { isShowTooltip, debounce } from "@/utils/utils.js";
// 接口api
import {
  startPreview,
  stopPreview,
  getFrame,
  rotateVideo,
  takePhoto,
  getCamerasNum,
  getRotate,
} from "@/api/gaopaiyi.js";
import {
  uploadMaterialsFile,
  materialClassifyPrePrial,
} from "@/api/materialCategory.js";

import {
  uploadImage,
  deleteImageItem,
  blankTableItem,
  allMaterialImage,
} from "@/api/zc/businessManagement/dict.js";
import { mapGetters } from "vuex";
import ZfImageLoading from "@/components/ZfImageLoading";
import ImgLoading from "@/components/ImgLoading";

import DEVEICETYPE, {
  GAO_PAI_YI_V1,
  GAO_PAI_YI_V2,
  GAO_PAI_YI_V3,
  GAO_PAI_YI_V4,
} from "@/components/HiSpeedCamera/config.js";

import {
  startnewPreview,
  stopnewPreview,
  takenewPhoto,
  takePic,
} from "@/api/handwarev3.js";
export default {
  name: "GpyUpload",
  inheritAttrs: false,
  components: {
    ZfImageLoading,
    ImgLoading,
    "el-image-viewer": () =>
      import("element-ui/packages/image/src/image-viewer"),
  },
  data() {
    return {
      gpyImgSrc: require("@/assets/image/gpy/gpy-bg.png"), // 高拍仪照片
      timer: null,
      loadingGpy: false,
      isGpyOpen: false,
      showHint: false, // 触摸
      // 正在识别
      dots: [
        { id: 1, active: false },
        { id: 2, active: true },
        { id: 3, active: true },
      ],
      previewImageUrl: '/case-api/pic/previewImage?fastdfsNginxUrl=',
      dotsTimer: null,
      // 顶部滚动盒子
      arrowList: [],
      folderList: [],
      currentHoverPopoverIndex: 0, // 当前hover状态下打开的popover index
      openDelay: 500, // popover打开延迟
      overflowY: "unset", // 是否展示滚动条
      loading: false, // loading 动画
      startDistinguish: false, // 开始识别
      rotateCount: 3, // 高拍仪旋转角度 0: 0度; 1：90度；2：180度；3：270度（可以在视频打开之前调用)
      currentMovingFile: {}, // 当前移动的文件
      uploadPicList: [],
      previewList: [],
      showViewer: false,
      uploadData: {
        serviceId: this.dictParamsId.serviceId,
        refinedMaterialId: this.refinedMaterialId,
        isEmpty: 1,
        materialId: this.dictParamsId.materialId,
        picType: "png",
        licenceOid: this.dictParamsId.licenceOid,
        licenceName: this.dictParamsId.licenceName,
      },
      myHeaders: { Authorization: this.token },
      gotItVisible: false, // 明白了
      isNotShowGotItVisible: false, // 不在提示
      baseUrl: "ws://127.0.0.1:12345",
      socket: "",
      qWebChannelDialog: null,
      paizhaoImg: "", //鄂尔多斯拍照保存图片
      counts:0,
    };
  },
  props: {
    dictParamsId: {
      type: Object,
      required: true,
    },
    refinedMaterialId: {
      type: String,
      required: true,
    },
    allMaterialList: {
      type: Array,
      required: true,
    },
  },
  computed: {
    ...mapGetters(["token"]),

    currentPopoverIndex() {
      const idx = this.folderList.findIndex((item) => item.showPopover);
      return idx === -1 ? this.currentHoverPopoverIndex : idx;
    },
  },
  watch: {
    allMaterialList: {
      handler(val) {
        /* 细化材料上传图片 */
        this.uploadPicList = [];
        this.uploadPicList = this.allMaterialList;
      },
    },
  },
  created() {
    if (localStorage.NOT_SHOW_GOT_IT === "true") {
      this.isNotShowGotItVisible = true;
      this.gotItVisible = true;
    }

    // 快捷键enter键拍照
    this.keyBoardEvent = (e) => {
      if (e.keyCode === 13) {
        this.takePhoto();
      }
    };

    this.onresize = () => {
      const oneLine = Math.floor(this.$refs.scanResultList.offsetWidth / 130);
      if (this.folderList.length > oneLine * 2) {
        this.overflowY = "scroll";
      } else {
        this.overflowY = "unset";
      }
    };

    document.addEventListener("keyup", this.keyBoardEvent);
    window.addEventListener("resize", this.onresize);
  },
  mounted() {
    // this.openSocket()
    if (this.isNotShowGotItVisible) {
      this.initDevice();
    }
  },
  beforeDestroy() {
    this.dotsTimer && clearInterval(this.dotsTimer);
    this.closeGpy();
    document.removeEventListener("keyup", this.keyBoardEvent);
    window.removeEventListener("resize", this.onresize);
  },
  methods: {
    // 启动鄂尔多斯高拍仪4
    openSocket() {
      // alert(11)
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
    Lrotate() {
      this.qWebChannelDialog.get_actionType("rotateLeft");
    },
    // 图片右转
    Rrotate() {
      this.qWebChannelDialog.get_actionType("rotateRight");
    },

    getSaveImg() {
      const that = this;
      return new Promise((resolve, reject) => {
        this.qWebChannelDialog.send_priPhotoData.connect(function (message) {
          console.log("message");
          that.$bus.$emit("message", message);

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
          `set_saveName:数据集管理图片材料${this.counts}`
        );
        this.qWebChannelDialog.photoBtnClicked("primaryDev_");
        this.qWebChannelDialog.get_actionType("savePhotoPriDev");
        // console.log('11111111111',this.paizhaoImg)
        // this.uploadGPYImg(that.paizhaoImg)
      }).then(() => {
        // that.$emit("takePhoto");
        console.log(this.paizhaoImg);
        // that.$bus.$emit("paizhao", this.paizhaoImg);
        this.uploadGPYImg(this.paizhaoImg);
        // debugger
      });
    },

    isShowTooltip,
    initDevice() {
      this.loadingGpy = true;
      this.startCamera();
    },
    startGetCamerasNum() {
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
    async startCamera() {
      switch (DEVEICETYPE.GPY_CONFIG) {
        case GAO_PAI_YI_V1:
          this.startGetCamerasNum();
          break;
        case GAO_PAI_YI_V3:
          this.startnewPreview();
          break;
        case GAO_PAI_YI_V4:
          this.loadingGpy = false;
          this.isGpyOpen = false;
          this.openSocket();
          break;
      }
    },
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
      this.timer = setInterval(() => {
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
            clearInterval(this.timer);
            return this.$message.error("读取图像失败");
          }
        });
      }, 200);
    },
    /*-----------------------加载细化材料上传图片 --------------------*/
    loadRefineData() {
      allMaterialImage({ refinedMaterialId: this.refinedMaterialId }).then(
        (res) => {
          if (res.code == 200) {
            console.log(res);
            this.uploadPicList = res.data;
            // console.log(this.allMaterialList)
          }
        }
      );
    },

    /*--------------------- 图片上传-本地上传 ------------------------------ */
    uploadImage(param) {
      const formData = new FormData();
      if (this.dictParamsId.refinedMaterialId) {
        formData.append("file", param.file);
        formData.append("serviceId", this.dictParamsId.serviceId);
        formData.append(
          "refinedMaterialId",
          this.dictParamsId.refinedMaterialId
        );
        formData.append("isEmpty", 1);
        formData.append("materialId", this.dictParamsId.materialId);
        formData.append("picType", "png");
        formData.append("licenceOid", this.dictParamsId.licenceOid);
        formData.append("licenceName", this.dictParamsId.licenceName);

        uploadImage(formData)
          .then((response) => {
            let { attachmentAddress, picRecordId, originName } = response.data;
            this.uploadPicList.push({
              serviceId: this.dictParamsId.serviceId,
              refinedMaterialId: this.dictParamsId.refinedMaterialId,
              isEmpty: 1,
              materialId: this.dictParamsId.materialId,
              picType: originName,
              licenceOid: this.dictParamsId.licenceOid,
              licenceName: this.dictParamsId.licenceName,
              fastdfsNginxUrl: attachmentAddress,
              picRecordId: picRecordId,
            });
          })
          .catch((response) => {
            console.log("图片上传失败");
            console.log(this.dictParamsId);
          });
      } else {
        this.$message.error("图片上传失败！请选择细化材料");
      }
    },
    /*-------------------------- 图片预览 --------------------------*/
    handleImgClick(index) {
      this.showViewer = true;
      let tempImgList = []; //所有图片地址
      this.uploadPicList.map((item) => {
        tempImgList.push(this.previewImageUrl+item.fastdfsNginxUrl);
      });
      let temp = [];
      for (let i = 0; i < index; i++) {
        //shift() 方法用于把数组的第一个元素从其中删除，并返回第一个元素的值。
        temp.push(tempImgList.shift());
      }
      this.previewList = tempImgList.concat(temp); //将当前图片调整成点击缩略图的那张图片
    },
    onPreview() {
      this.showViewer = true;
    },
    // 关闭查看器
    closeViewer() {
      (this.showViewer = false), (document.body.style.overflow = "auto");
    },
    /*-------------------------- 删除操作 --------------------------*/
    deleteImageItem(index, id) {
      console.log(id);
      this.$confirm("确定删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteImageItem({ picRecordId: id })
            .then((res) => {
              if (res.code == 200) {
                this.$message.success(res.data);
                console.log(res);
                this.$nextTick(() => {
                  this.loadRefineData();
                });
              }
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    /*--------------------------- 空表设置 ---------------------------------*/
    blankHandleItem(picRecordId, isEmpty, index) {
      isEmpty == 0 ? (isEmpty = 1) : (isEmpty = 0);
      blankTableItem({
        picRecordId,
        isEmpty,
      }).then((res) => {
        if (res.code == 200) {
          this.loadRefineData();
        }
      });
    },

    /** ---------------------------------------------------------- 高拍仪操作代码 ---------------------------------------------------------- */
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
        dev_idx: 1,
        res_id: 0,
        pixfmt: "pixfmt",
      })
        .then(async (data) => {
          // console.log(data);
          if (data.returnCode === 0) {
            // 先转个角度
            await rotateVideo({
              count: this.rotateCount,
            });
            // 定时器轮训 延迟一秒请求
            await sleep();

            this.loadingGpy = false;
            this.isGpyOpen = true;

            this.startGetFrames();
          } else {
            this.openGpyFail(returnMsg);
          }
        })
        .catch(this.openGpyFail);
    },

    // 获取高拍仪视频图片
    startGetFrames() {
      let count = 0;
      this.timer && clearInterval(this.timer);
      this.timer = setInterval(async () => {
        const { data: _data, returnCode } = await getFrame();
        if (returnCode !== 0) {
          // 做兜底操作 失败大于10次 就停止请求
          count++;
          // // console.log('%c [count]:', 'color:red;font-weight:700;', count);
          if (count > 10) {
            return this.closeGpy();
          }
          return;
        }
        this.gpyImgSrc = "data:image/jpg;base64," + _data.img;
      }, 200);
    },

    stopGetFrames() {
      this.timer && clearInterval(this.timer);
    },

    takePhoto: debounce(
      async function () {
        if (this.isGpyOpen) return this.$message.warning("请先开启高拍仪设备!");
        if (this.startDistinguish)
          return this.$message.warning("正在识别中, 请稍后重试!");

        try {
          // 开始识别
          this.startDistinguish = true;

          if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1) {
            const { data, returnCode, returnMsg } = await takePhoto({
              savepath: "/tmp",
              quality: 80,
            });
            if (returnCode !== 0) {
              this.$message.warning(returnMsg || "拍摄失败");
              this.startDistinguish = false;
              this.clearDotsFolder();
              return;
            }

            // 上传图片
            const uploadRet = await this.uploadGPYImg(data.img);
            // console.log(uploadRet)
          }

          if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
            const res = await takePic();
            if (res.StateCode != 0) {
              this.$message.warning(returnMsg || "拍摄失败");
              this.startDistinguish = false;
              this.clearDotsFolder();
              return;
            }

            await this.uploadGPYImg(res.data);
          }
          if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V4) {
            // alert(1111)
            const res = await this.openSocket();
            this.getSaveImg();
            // console.log(res)
            // debugger
            // if (res.StateCode != 0) {
            //   this.$message.warning(returnMsg || '拍摄失败');
            //   this.startDistinguish = false;
            //   this.clearDotsFolder();
            //   return;
            // }

            await this.uploadGPYImg(res.data);
          }
          // 分类
          const ret = await materialClassifyPrePrial({
            caseOid: this.$attrs.caseOid,
            attaOid: uploadRetData.attaOid || "",
          });

          if (ret.code !== 200) {
            this.startDistinguish = false;
            return this.$message.warning("材料分类请求失败, 请重新拍摄");
          }
          // 清除动画
          this.startDistinguish = false;
        } catch (error) {
          this.startDistinguish = false;
        }
      },
      500,
      true
    ),

    closeGpy() {
      this.timer && clearInterval(this.timer);
      this.timer = null;
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
        return stopnewPreview().then(() => {
          this.isGpyOpen = false;
          this.loadingGpy = false;
          this.$nextTick(() => {
            this.gpyImgSrc = require("@/assets/image/gpy/gpy-bg.png");
          });
        });
      } else if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1) {
        stopPreview({
          dev_idx: 1,
        }).then(() => {
          this.isGpyOpen = false;
          this.$nextTick(() => {
            this.gpyImgSrc = require("@/assets/image/gpy/gpy-bg.png");
          });
        });
      }
    },
    // 上传高拍仪图片
    uploadGPYImg(img) {
      // if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V4) {
      //   let file = this.base64ToFile(img, Date.now() + ".jpg");
      //   let formData = new FormData();
      //   formData.append("files", file);
      //   alert(111)
      //   return uploadImage(formData)
      // }

      // this.handleUploadImg(formData);
      // return uploadMaterialsFile(formData);

      let file = this.base64ToFile(img, Date.now() + ".jpg");
      let formData = new FormData();
      if (this.dictParamsId.refinedMaterialId) {
        formData.append("file", file);
        formData.append("serviceId", this.dictParamsId.serviceId);
        formData.append(
          "refinedMaterialId",
          this.dictParamsId.refinedMaterialId
        );
        formData.append("isEmpty", 1);
        formData.append("materialId", this.dictParamsId.materialId);
        formData.append("picType", "png");
        formData.append("licenceOid", this.dictParamsId.licenceOid);
        formData.append("licenceName", this.dictParamsId.licenceName);
        return uploadImage(formData)
          .then((response) => {
            let { attachmentAddress, picRecordId, originName } = response.data;
            this.uploadPicList.push({
              serviceId: this.dictParamsId.serviceId,
              refinedMaterialId: this.dictParamsId.refinedMaterialId,
              isEmpty: 1,
              materialId: this.dictParamsId.materialId,
              picType: originName,
              licenceOid: this.dictParamsId.licenceOid,
              licenceName: this.dictParamsId.licenceName,
              fastdfsNginxUrl: attachmentAddress,
              picRecordId: picRecordId,
            });
          })
          .catch((response) => {
            console.log("图片上传失败");
            console.log(this.dictParamsId);
          });

        return uploadMaterialsFile(formData);
      } else {
        this.$message.error("高拍仪拍照上传失败！请选择细化材料");
      }
    },

    // 高拍仪旋转代码
    handleRotate(driection) {
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V4 && driection === "left") {
        this.Lrotate();
      }
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V4 && driection === "right") {
        this.Rrotate();
      }

      if (!this.isGpyOpen) return;
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
      // rotateVideo({ count: this.rotateCount });
      this._handleRotate();
      this.Lrotate();
    },

    _handleRotate() {
      if (this.$refs.gpyImg) {
        this.$refs.gpyImg.style = `object-fit:${
          this.isGpyOpen ? "contain" : "unset"
        };transform:rotate(${(this.rotateCount + 1) * 90}deg)`;
      }
    },

    /** ---------------------------------------------------------- 公共代码 ---------------------------------------------------------- */

    showHintInfo() {
      this.showHint = true;
    },

    hideHintInfo() {
      this.showHint = false;
    },

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

    /** ---------------------------------------------------------- 文件夹相关操作 ---------------------------------------------------------- */

    // 预览
    handlePreview(ref) {
      const refs = this.$refs[`${ref}`][0];
      if (refs) {
        if (refs.loading) return this.$message.warning("正在加载中,请稍后");
        refs.handlePreview();
      }
    },

    gotIt() {
      this.gotItVisible = true;
      this.loadingGpy = true;
      this.initDevice();
    },

    handleChange(value) {
      localStorage.setItem("NOT_SHOW_GOT_IT", value);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin/index.scss";
.scan-result-dic {
  .content {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    @include util-change-scrollbar(#b1b5c1, #c8ccd6);

    .el-icon-loading {
      left: 38%;
      top: 28%;
      font-size: 2em;
    }

    &-item {
      background: #fff;
      box-shadow: 0px 4px 7px 0px rgba(45, 63, 116, 0.27);
      position: relative;
      // margin-right: 10px;
      // margin-bottom: 15px;
      cursor: pointer;
      width: 100%;
      height: 100%;

      &-close {
        position: absolute;
        right: 0;
        top: 0;
        z-index: 2;
        display: block;
      }

      &-preview {
        position: absolute;
        width: 100%;
        bottom: 0;
        z-index: 9;
        left: 0;
        font-size: 12px;
        background: rgba(43, 43, 43, 1);
        color: #fff;
        text-align: center;
        padding: 5px 0;
        display: block;
      }
    }
  }
}

.el-dialog.dialog-gpy {
  height: 714px;
  width: 60%;

  .el-dialog__body {
    height: calc(100% - 56px);
    padding: 26px 24px 0px 24px;
  }
}

// 高拍仪
.scan-picture {
  padding: 10px 0;
  display: flex;
  justify-content: space-between;

  &-left {
    width: 605px;
    margin-right: 10px;
  }

  &-right--content {
    flex: 1;
  }

  &--title {
    font-size: 16px;
    font-weight: 400;
    color: #232f51;
    display: block;
    height: 45px;
    background: #cfd3df;
    padding-top: 15px;
    padding-left: 20px;
    border-radius: 5px 5px 0px 0px;
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
  }

  .scan-result-dic {
    position: relative;
    height: 496px;
    background: #f2f4f8;
    border: 1px solid #cfd3df;
    border-radius: 0px 0px 5px 5px;
    padding: 10px;

    &-list {
      max-height: calc(100% - 50px);
      display: flex;
      flex-wrap: wrap;
      justify-content: start;
      overflow-y: auto !important;
      @include util-change-scrollbar(#b1b5c1, #c8ccd6);

      &--item {
        box-sizing: border-box;
        border-radius: 3px;
        margin: 0px 15px 15px 0px;
        // &:nth-child(3n) {
        //   margin-right: unset;
        // }

        &-title {
          height: 28px;
          width: 100%;
          padding: 0px 12px;
          line-height: 28px;
          border-radius: 3px 3px 0px 0px;
          cursor: default;
          @include overflow-ellipsis();
        }

        span:not(.el-image-viewer__btn) {
          border: 1px solid #2b2b2b;
          border-top: unset;
          width: 100%;
          padding: 0;
          height: 30px;
          line-height: 30px;
          position: absolute;
          bottom: 0px;
          left: 0px;
        }

        &-content {
          padding: 13px 27px;
          width: 100%;
          height: 100%;
          position: relative;
          display: flex;
          align-items: center;

          .dots {
            width: 100%;
            height: 20px;
            background: #2f384a;
            border: 1px solid #1890ff;
            border-radius: 3px;
            display: flex;
            justify-content: space-around;
            align-items: center;

            &-item {
              width: 12px;
              height: 12px;
              background: #ffffff;
              opacity: 0.6;
              border-radius: 50%;

              &.active {
                opacity: 0.2;
              }
            }
          }
        }
      }
    }

    .scan-btn {
      background-color: rgba(228, 230, 236, 0.85);
      text-align: center;
      position: absolute;
      width: 100%;
      bottom: 0;
      left: 0;
      height: 50px;
      line-height: 50px;
      border-top: 1px solid #c8ccd6;
      border-radius: 0px 0px 5px 5px;

      button {
        height: 35px;
      }
    }
  }
}
.gyp-desrition {
  background: #eef3ff;
  border: 1px solid #0172f6;
  border-radius: 3px;
  margin-top: 10px;
  .des-text {
    padding: 15px;
    font-size: 14px;
    color: #193150;
    line-height: 2;
    p {
      margin: 0;
    }
    span {
      font-weight: bold;
    }
  }
  h3 {
    margin: 0;
    width: 60px;
    height: 33px;
    line-height: 33px;
    background-color: #d3def8;
    font-weight: normal;
    font-size: 14px;
    text-align: center;
  }
}
.scan-picture-right--content {
  position: relative;
  .num-content {
    position: absolute;
    right: 0px;
    top: 15px;
    font-size: 14px;
    color: #007ee8;
  }
}
.if-blank {
  display: inline-block;
  position: absolute;
  left: 3px;
  top: 3px;
  z-index: 10;
}
::v-deep.el-image__placeholder {
  background: url("~@/assets/image/loading.gif") no-repeat 100%% 100%;
  background-size: 100% 100%;
  width: 100%;
  height: 100%;
}
.content-item-close {
  margin: 3px 3px 0 0;
}

@media screen and (min-width: 1280px) and (max-width: 1300px) {
  .el-dialog.dialog-gpy {
    width: 90%;
    height: 714px;
  }
}
</style>

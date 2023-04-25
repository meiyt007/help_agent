<template>
  <div class="take-photo">
    <div
      class="take-photo-area"
      v-loading="loading"
      element-loading-text="正在启动设备"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
    >
      <img :src="faceImgTemp" height="496" width="100%" alt />
      <div class="take-photo-area--btn">
        <!-- <div class="take-photo-area--btn-text">
          镜头可智能识别人像进行自动拍照
        </div> -->
        <img
          v-animate
          @click="getImageRes"
          src="@/assets/image/scan-btn.png"
          alt
        />
      </div>
    </div>
  </div>
</template>

<script>
import { openCamera, getImage } from "@/api/sys/hardwareScan";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE,{
  GAO_PAI_YI_V1,
  GAO_PAI_YI_V2,
  GAO_PAI_YI_V3
} from "@/components/HiSpeedCamera/config.js"
export default {
  name: "TakePhoto",
  mixins: [],
  props: {},
  components: {},
  data() {
    return {
      timer: "", //定时器
      returnBase64: "",
      faceImg: "",
      faceImgTemp: require("@/assets/image/gpy/gpy-bg.png"),
      loading: true
    };
  },
  mounted() {
    this.getReceiveCamera();
  },

  async beforeDestroy() {
    clearInterval(this.timer);
    if (GPYDrive.isOpen) {
      await GPYDrive.closeFaceCamera();
    }
  },

  methods: {
    //调用摄像头获取图像信息
    getImageCamera() {
      //设置定时器获取图像信息，每200毫秒秒一次
      clearInterval(this.timer);
      this.timer = setInterval(this.getImageInfo, 200);
    },

    //调用摄像头打开方法
    async getReceiveCamera() {
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1 || DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
        openCamera()
          .then(response => {
            this.loading = false;
            let res = response;
            if (res) {
              //0:成功-1:设备已经打开-2：设备打开失败
              if (res.state == "sucess" || res.StateCode == -1) {
                //调用获取图像信息接口
                this.getImageCamera();
              } else if (res.StateCode == -2) {
                this.$message.error("摄像头打开失败！");
                clearInterval(this.timer);
              }
            } else {
              this.$message.error("连接服务失败！");
              clearInterval(this.timer);
            }
          })
          .catch(() => {
            this.loading = false;
            clearInterval(this.timer);
          });
      }

      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V2) {
        const res = await GPYDrive.openFaceCamera();
        // console.log(res);
        if (!res || res.code !== 200) {
          this.loading = false;
          clearInterval(this.timer);
          return this.msgWarning("高拍仪人脸摄像头连接失败！");
        }
        this.loading = false;
        //调用获取图像信息接口
        this.getImageCamera();
      }
    },

    //获取图像
    async getImageInfo() {
      let res = null;

      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1 || DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
        getImage().then(response => {
          let res = response;
          if (res) {
            //code 0:成功-1:设备没有打开-2:没有获取到图像-3:没有初始化对象
            if (res.state == "sucess") {
              if (res.FaceLen && res.FaceLen != 0) {
                //图像信息
                this.faceImgTemp = "data:image/jpeg;base64," + res.Face;
                this.getImageRes();
                clearInterval(this.timer);
              } else {
                this.faceImgTemp = "data:image/jpeg;base64," + res.Image;
              }
            } else if (res.StateCode == -1) {
              //失败，说明摄像头没打开调用打开摄像头方法
              this.getReceiveCamera();
            }
          } else {
            this.$message.error("请检查设备或连接是否正常");
            clearInterval(this.timer);
          }
        });
      }

      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V2) {
        if (GPYDrive.isOpen) {
          if (GPYDrive.singleOpenType === "face") {
            res = await GPYDrive.getFacePicture();
          }

          if (GPYDrive.singleOpenType === "high") {
            res = await GPYDrive.getHighPicture();
          }
        }

        if (res && res.code === 200) {
          if (res.data && res.data !== "undefined") {
            this.faceImgTemp = `data:image/jpeg;base64,${res.data}`;
          }
          this.$forceUpdate();
        } else {
          this.$message.error("请检查设备或连接是否正常");
          clearInterval(this.timer);
        }
      }
    },
    //点击拍照，获取最后的图片
    getImageRes() {
      clearInterval(this.timer);
      this.faceImg = this.faceImgTemp;
      this.returnBase64 = this.faceImg ? this.faceImg.split(",")[1] : "";
      this.$emit("getPhotoInfo", {
        faceImg: this.faceImg,
        returnBase64: this.returnBase64
      });
      this.$message.success("拍照成功");
    }
  }
};
</script>

<style scoped lang="scss">
.take-photo {
  background-color: #000;
  border-radius: 0px 0px 5px 5px;
  height: 496px;
  width: 515px;
  margin: 0 auto;
  position: relative;

  .take-photo-area--btn {
    background-color: #030719;
    text-align: center;
    position: absolute;
    width: 100%;
    height: 74px;
    bottom: 0;
    line-height: 9;
    display: flex;
    justify-content: center;
    align-items: center;

    &-text {
      width: 300px;
      height: 40px;
      background: rgba(26, 29, 47, 0.7);
      border-radius: 10px;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #a2a8c9;
    }

    img {
      width: 55px;
      height: 55px;
      cursor: pointer;
    }
  }
}
</style>

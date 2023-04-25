/** * @Author: dxl */
<template>
  <div>
    <!-- 人证比对 -->
    <!--    <el-dialog
      :visible.sync="dialogVisible" width="80%" :before-close="handleClose" :close-on-click-modal="false" append-to-body>-->
    <el-row :gutter="25" class="scan-picture">
      <el-col :span="24" :xl="12" :lg="12" :xs="12">
        <span class="scan-picture-title">正在拍照：</span>
        <div
          class="scan-picture-area"
          v-loading="loading"
          element-loading-text="正在启动设备"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
        >
          <img
            class="scan-picture-area--pic"
            :src="faceImgTemp || require('@/assets/image/gpy/gpy-bg.png')"
          />
          <div class="scan-picture-area--btn">
            <!-- <img
              @mouseover.stop="showHintInfo"
              @mouseleave.stop="hideHintInfo"
              src="../../assets/image/scan-btn.png"
              @click="getImageRes"
            /> -->
            <div
              style="width:405px"
              class="scan-picture-area--btn-text"
              v-if="isGUOCHANHUA"
            >
              请确保申请人人脸清晰可见时单击右侧人证比对按钮
            </div>
            <div class="scan-picture-area--btn-text" v-if="isGPY">
              镜头可智能识别人像进行自动拍照
            </div>
          </div>
          <!-- <p class="hint" :class="{ active: showHint }">
            拍照按钮（ 快捷键：Enter ）
          </p> -->
        </div>
      </el-col>
      <el-col :span="24" :xl="12" :lg="12" :xs="12">
        <span class="scan-picture-title">拍照结果对比：</span>
        <div class="scan-result">
          <div class="contrast clearfix">
            <div>
              <div style="width: 100%; height: 100%; border: 1px solid">
                <img v-if="faceImg" :src="faceImg" />
              </div>
              <p>当前照片</p>
            </div>
            <img src="../../assets/image/contrast.png" />
            <div>
              <div style="width: 100%; height: 100%; border: 1px solid">
                <img v-if="idCardImg" :src="idCardImg" />
              </div>
              <p>身份证照片</p>
            </div>
          </div>
          <table class="result-show" cellpadding="0" cellspacing="0">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <thead>
              <tr>
                <th colspan="4">人证核验结果</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>姓名：</td>
                <td>{{ CNName }}</td>
                <td>性别：</td>
                <td>{{ CNNsex }}</td>
              </tr>
              <tr>
                <td>身份证号：</td>
                <td>{{ carID }}</td>
                <td>核验时间：</td>
                <td>{{ pickerOptionsEnd }}</td>
              </tr>
              <tr>
                <td>核验结果：</td>
                <td>
                  <span v-if="rzbdResult == '1'">通过</span>
                  <span v-if="rzbdResult == '2'">不通过</span>
                </td>
                <td>相似度：</td>
                <td>{{ similarity ? similarity : "" }}</td>
              </tr>
              <!-- <tr>
              <td colspan="4"></td>
            </tr> -->
            </tbody>
          </table>
          <div class="verification">
            <img src="../../assets/image/pass.png" v-if="rzbdResult == '1'" />
            <img
              v-if="rzbdResult == '2'"
              src="../../assets/image/no-pass.png"
            />
          </div>
          <div class="scan-btn">
            <el-button
              type="info"
              @click="getIdcardData"
              style="background-color: #4065ce"
              v-if="isGUOCHANHUA"
              >人证比对</el-button
            >
            <el-button
              v-if="isGPY"
              type="info"
              @click="resetImage"
              style="background-color: #4065ce"
              >重新拍照</el-button
            >
            <el-button
              type="primary"
              :disabled="!isCompleteCompare"
              @click="handleCompareComplete"
            >
              完成核验
            </el-button>
            <el-button type="primary" v-if="isGPY" @click="scanCard"
              >扫描身份证</el-button
            >
            <el-button
              v-if="rzbdResult == '2' || isModify"
              type="primary"
              @click="ignoreCompareResult"
            >
              忽略
            </el-button>
            <!-- <el-button type="primary" @mouseover.native="showVfHintInfo" @mouseleave.native="hideVfHintInfo" @click="getCompareResult">人证核验</el-button>
              <p class="vf-hint" :class="{active:showVfHint}">请先面对摄像头拍照后核验</p> -->
          </div>
        </div>
      </el-col>
    </el-row>
    <!--    </el-dialog>-->
  </div>
</template>

<script>
import {
  openCamera,
  getImage,
  getIdCardInfo,
  openIdCard,
  findIdCard,
  comparsionFace,
  jxImage,
  saveRzbdInfo,
  uploadCompareFile,
  queryWitnessComparisonByCaseOid,
  getWitnessComparisonBase64Img
} from "@/api/sys/hardwareScan";
import {
  aotuAudioSpeech,
  getAudioInfo,
  getSpeechConfigInfo,
  showCallMessage
} from "@/api/zc/businessManagement/doubleScreenInteraction";
import { getSysAttaByAttaOid } from "@/api/sys/atta";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE,{
  GAO_PAI_YI_V1,
  GAO_PAI_YI_V2,
  GAO_PAI_YI_V3
} from "@/components/HiSpeedCamera/config.js"
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";
export default {
  name: "hardwareScan",
  props: {
    caseOid: String,
    loginUser: Object
    /*isShowSmall: {
      type: Boolean,
      default: true
    }*/
  },
  components: {},
  data() {
    return {
      dialogVisible: false,
      showHint: false,
      showVfHint: false,
      faceImg: "", //人脸图像
      timeImg: "", //定时器
      idCardImg: "", //身份证图片
      CNName: "", //姓名
      CNNsex: "", //性别
      carID: "",
      pickerOptionsEnd: "", //时间
      similarity: undefined,
      returnBase64: "",
      idCardImge: "",
      faceImgTemp: "", //临时的视频人脸像
      rzbdResult: "", //1通过，2不通过
      isCompleteCompare: false,
      formCompare: {},
      loading: false,
      isModify: false, // 是否是修改
      startTime: null,
      smallspeech: "N", //是否打开引导语
      speechAddress: ""
    };
  },
  computed: {
    isGPY() {
      return DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1 || DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3;
    },
    isGUOCHANHUA() {
      return DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V2;
    },
    // isCZGPY() {
    //   return DEVEICE_TYPE === NEW_ZONGCHUANG;
    // }
  },
  async mounted() {
    this.queryWitnessComparisonByCaseOid();
  },
  async beforeDestroy() {
    clearInterval(this.timeImg);
    if (GPYDrive.isOpen) {
      await GPYDrive.closeHighCamera();
      await GPYDrive.closeFaceCamera();
    }
  },
  methods: {
    init() {
      this.getReceiveCamera();
      // 判断是否缓存过身份信息
      const idCardInfo = localStorage?.idCardInfo
        ? JSON.parse(localStorage.idCardInfo)
        : false;
      // 如果有身份证图像信息 就自动带入身份证信息
      // 如果没有就不操作 防止二次刷身份证
      // if (idCardInfo?.imgData) {
      //   // openIdCard().then(() => {
      //   // });
      //   this.handleIdCardInfo(idCardInfo);
      // }
    },
    /** 查询之前的人证核验信息 */
    queryWitnessComparisonByCaseOid() {
      if (this.caseOid) {
        queryWitnessComparisonByCaseOid(this.caseOid)
          .then(({ code, data }) => {
            if (code !== 200 || !data) {
              this.init();
              return;
            }
            this.isModify = true;

            this.rzbdResult = data.compareResult;

            this.CNName = data.name;

            this.CNNsex = data.sex;

            this.carID = data.cardNo;

            this.similarity = data.distance;
            if (this.similarity <= 0) {
              this.similarity = "0%";
            } else {
              this.similarity = (this.similarity * 100).toFixed(1) + "%";
            }

            this.pickerOptionsEnd = data.createDate;

            getSysAttaByAttaOid(data.imgAttaOid).then(({ data: _data }) => {
              if (_data) {
                // this.faceImg = this.faceImgTemp = _data.fastdfsNginxUrl;
                this.faceImg = _data.fastdfsNginxUrl;
                this.init();
              }
            });
            getSysAttaByAttaOid(data.idcardAttaOid).then(({ data: _data }) => {
              if (_data) {
                this.idCardImg = _data.fastdfsNginxUrl;
              }
            });
          })
          .catch(() => {
            this.init();
          });
      } else {
        this.init();
      }
    },

    handleClose() {
      clearInterval(this.timeImg);
      this.dialogVisible = false;
    },
    getCurrentTime() {
      //获取当前时间并打印
      let yy = new Date().getFullYear();
      let mm = new Date().getMonth() + 1;
      let dd = new Date().getDate();
      let hh = new Date().getHours();
      let mf =
        new Date().getMinutes() < 10
          ? "0" + new Date().getMinutes()
          : new Date().getMinutes();
      let ss =
        new Date().getSeconds() < 10
          ? "0" + new Date().getSeconds()
          : new Date().getSeconds();
      this.pickerOptionsEnd =
        yy + "/" + mm + "/" + dd + " " + hh + ":" + mf + ":" + ss;
    },
    showHintInfo() {
      this.showHint = true;
    },
    hideHintInfo() {
      this.showHint = false;
    },
    showVfHintInfo() {
      this.showVfHint = true;
    },
    hideVfHintInfo() {
      this.showVfHint = false;
    },
    //调用摄像头打开方法
    async getReceiveCamera() {
      this.dialogVisible = true;
      this.loading = true;
      if (this.isGPY) {
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
                clearInterval(this.timeImg);
              }
            } else {
              this.$message.error("连接服务失败！");
              clearInterval(this.timeImg);
            }
          })
          .catch(() => {
            this.loading = false;
          });
      }
      if (this.isGUOCHANHUA) {
        const faceRes = await GPYDrive.openFaceCamera();
        if (!faceRes || faceRes.code !== 200) {
          this.loading = false;
          return this.msgWarning("高拍仪摄像头连接失败！");
        }
        this.loading = false;
        //调用获取图像信息接口
        this.getImageCamera();
      }
    },
    //调用摄像头获取图像信息
    async getImageCamera() {
      //设置定时器获取图像信息，每200毫秒秒一次
      this.startTime = Date.now();
      clearInterval(this.timeImg);
      if (this.isGPY) {
        this.timeImg = setInterval(this.getImageInfo, 200);
      }
      if (this.isGUOCHANHUA) {
        const res = await GPYDrive.getFacePicture();
        if (res && res.data) {
          this.timeImg = setInterval(this.getImageInfo, 200);
        } else {
          return this.$message.error("请检查设备或连接是否正常");
        }
      }
    },
    //获取图像
    async getImageInfo() {
      if (this.isGPY) {
        getImage().then(response => {
          let res = response;
          console.log(res);
          if (res) {
            //code 0:成功-1:设备没有打开-2:没有获取到图像-3:没有初始化对象
            if (res.state == "sucess") {
              if (res.FaceLen && res.FaceLen != 0) {
                let time = Date.now();
                // 3秒延迟
                if (time - this.startTime > 3000) {
                  this.startTime = null;
                  //图像信息
                  this.faceImgTemp = "data:image/jpeg;base64," + res.Face;
                  this.getImageRes();
                  clearInterval(this.timeImg);
                  //推送首页到小屏
                  // this.openWelcome();
                  // 人证比对
                  if (this.faceImg && this.idCardImg) {
                    this.handleCompareResult();
                  }
                } else {
                  this.faceImgTemp = "data:image/jpeg;base64," + res.Image;
                }
              } else {
                this.faceImgTemp = "data:image/jpeg;base64," + res.Image;
              }
            } else if (res.StateCode == -1) {
              //失败，说明摄像头没打开调用打开摄像头方法
              this.getReceiveCamera();
            }
          } else {
            this.$message.error("请检查设备或连接是否正常");
            clearInterval(this.timeImg);
          }
        });
      }
      if (this.isGUOCHANHUA) {
        const res = await GPYDrive.getFacePicture();
        if (!res || res.code !== 200) {
          clearInterval(this.timeImg);
          return this.$message.error("请检查设备或连接是否正常");
        }
        if (res.data && res.data !== "undefined") {
          let time = Date.now();

          if (time - this.startTime > 3000) {
            this.startTime = null;
            //图像信息
            this.faceImgTemp = `data:image/jpeg;base64,${res.data}`;
            // this.getImageRes();
            //推送首页到小屏
            //this.openWelcome();
          }
        }
      }
    },
    //点击拍照，获取最后的图片
    getImageRes() {
      this.faceImg = this.faceImgTemp;
      this.returnBase64 = this.faceImg ? this.faceImg.split(",")[1] : "";
    },
    //停止定时器
    stopInterval() {
      clearInterval(this.timeImg);
    },
    //图片重置按钮
    resetImage() {
      //设置定时器获取图像信息，每一秒一次
      // this.getReceiveCamera();
      this.getImageCamera();
      // this.showSmall();
      this.faceImg = "";
      // this.idCardImg = "";
      // this.idCardImge = "";
      // this.CNName = ""; //姓名
      // this.CNNsex = ""; //性别
      // this.carID = "";
      this.pickerOptionsEnd = ""; //时间
      this.similarity = "";
      this.returnBase64 = "";
      this.rzbdResult = "";
      //this.scanCard();
      if (this.isGUOCHANHUA) {
        this.imageCompare();
      }
    },
    async getIdcardData() {
      this.CNName = "";
      this.CNNsex = "";
      this.idCardImg = "";
      this.carID = "";
      this.idCardImge = "";
      this.pickerOptionsEnd = ""; //时间
      this.similarity = "";
      this.rzbdResult = "";
      this.isCompleteCompare = false;
      if (this.isGPY) {
        getdataIdcardv3().then(resData => {
          if (resData.state == "sucess" && resData.StateCode == 0) {
            let resInfo = JSON.parse(resData.data);
            let res = {
              CNName: resInfo.name,
              sex: resInfo.sex,
              carID: resInfo.number,
              nation: resInfo.nation,
              address: resInfo.department,
              bron: resInfo.birthday,
              imgData:  resInfo.Image
            };
            this.handleIdCardInfo(res);
          } else if (resData.StateCode == -1) {
            //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
            //判断有没有设备
            //打开身份证
            openIdCard().then(response => {
              if ((response.state = "sucess")) {
                this.getIdcardData(); //重新获取身份证信息
              }
            });
          } else {
            this.$message.error(resData.tips);
            return false;
          }
        });
      }
      if (this.isGUOCHANHUA) {
        const resData = await GPYDrive.readCardInfo();
        if (!resData || resData.code !== 200) {
          this.$message.error("读取身份证信息失败");
          return false;
        }
        let res = {
          CNName: resData.data.name,
          sex: resData.data.sex,
          carID: resData.data.number,
          nation: resData.data.nation,
          address: resData.data.address,
          bron: resData.data.birthday,
          imgData: resData.data.image
        };

        this.idCardImg = "data:image/png;base64,"+ res.imgData;
        // let res = cardInfo.data;
        this.handleIdCardInfo(res);
        //拍照
        this.getImageRes();
        //进行人证对比
        this.handleCompareResult();
      }
    },
    handleIdCardInfo(res) {
      if (this.isGPY) {
        this.CNName = res.CNName.trim();
        this.carID = res.carID;
        this.CNNsex = res.sex;
        // this.jxImage(res.imgData);
        this.idCardImg = "data:image/jpeg;base64," + res.imgData;
        this.idCardImge = res.imgData;
        localStorage.setItem("idCardInfo", JSON.stringify(res));


        // 人证比对
        if (this.faceImg && this.idCardImge) {
          this.handleCompareResult();
        }
      }
      if (this.isGUOCHANHUA) {
        this.CNName = res.CNName.trim();
        this.carID = res.carID;
        this.CNNsex = res.sex;
        localStorage.setItem("idCardInfo", JSON.stringify(res));
      }
    },
    scanCard() {
      if (this.isGPY) {
        openIdcardv3().then(res => {
          if (res.StateCode == 0 || res.StateCode == -1) {
            return this.getIdcardData(); //获取身份证信息
          } else {
            return this.$message.warning("请确认设备连接是否正常");
          }
        });
      }
      if (this.isGUOCHANHUA) {
        this.getIdcardData(); //重新获取身份证信息
      }
    },
    //解析十六进制的头像图片
    jxImage(imgData) {
      jxImage(imgData).then(response => {
        let res = response.data;
        this.idCardImg = "data:image/jpeg;base64," + res.imgData;
        this.idCardImge = res.imgData;

        // 人证比对
        if (this.faceImg && this.idCardImge) {
          this.handleCompareResult();
        }
      });
    },
    getCompareResult() {
      if (!this.faceImg) {
        this.$message.error("请先对准摄像头拍照！");
        return false;
      }
      clearInterval(this.timeImg);
      if (!this.idCardImg) {
        this.$message.error("请放置身份证！");
        return false;
      }
      this.handleCompareResult();
    },
    async handleCompareResult() {
      let formImg = {};
      if (!this.returnBase64 || !this.idCardImge) {
        const { code, data } = await getWitnessComparisonBase64Img(
          this.caseOid
        );
        if (code !== 200) return;
        if (!this.returnBase64) {
          this.returnBase64 = data.image2Base64;
        }
        if (!this.idCardImge) {
          this.idCardImge = data.idCard2Base64;
        }
      }
      formImg.base64ImageOne = this.returnBase64; //人脸头像
      formImg.base64ImageTwo = this.idCardImge; //身份证头像
      //人证对比
      if (this.isGUOCHANHUA) {
        const res = await GPYDrive.personVerify();
        if (!res || res.code !== 200) {
          this.$message.error(res.msg);
          return false;
        } else {
          if (res.data < 40) {
            this.$message.error("人证比对不通过，请核验身份证和本人是否符合！");
            this.rzbdResult = "2"; //不通过
          } else {
            this.rzbdResult = "1";
          }
          this.similarity = res.data + "%";
          if (this.caseOid) {
            this.formCompare = {};
            //上传比对图片到附件库
            let file1 = this.base64ToFile(
              formImg.base64ImageOne,
              "base64ImageOne.jpg"
            );
            formImg.base64ImageTwo = this.idCardImg;
            let file2 = this.base64ToFile(
              formImg.base64ImageTwo.split(",")[1],
              "base64ImageTwo.jpg"
            );
            let formD = new FormData();
            formD.append("files", file1);
            formD.append("files", file2);
            uploadCompareFile(formD).then(res => {
              let resArr = res.data;
              if (resArr) {
                this.formCompare.imgAttaOid = resArr[0].attaOid;
                this.formCompare.idcardAttaOid = resArr[1].attaOid;
                this.formCompare.distance = res.data.similarity;
                this.formCompare.compareResult = this.rzbdResult;
                this.formCompare.caseOid = this.caseOid;
                this.formCompare.name = this.CNName;
                this.formCompare.sex = this.CNNsex;
                this.formCompare.cardNo = this.carID;
                this.formCompare.caseType = 1; //先塞成单办的
                this.saveWitnessCompare();
              }
            });
          }
          // 是否完成了核验
          this.isCompleteCompare = true;
        }
      }
      if (this.isGPY) {
        comparsionFace(formImg)
          .then(response => {
            if (response.data) {
              if (response.data.result && response.data.result == true) {
                this.rzbdResult = "1";
              } else {
                this.rzbdResult = "2"; //不通过
              }
              this.similarity = response.data.similarity;
              if (this.similarity <= 0) {
                this.similarity = "0%";
              } else {
                this.similarity = (this.similarity * 100).toFixed(1) + "%";
              }
              if (this.caseOid) {
                this.formCompare = {};
                //上传比对图片到附件库
                let file1 = this.base64ToFile(
                  formImg.base64ImageOne,
                  "base64ImageOne.jpg"
                );
                let file2 = this.base64ToFile(
                  formImg.base64ImageTwo,
                  "base64ImageTwo.jpg"
                );
                let formD = new FormData();
                formD.append("files", file1);
                formD.append("files", file2);
                uploadCompareFile(formD).then(res => {
                  let resArr = res.data;
                  if (resArr) {
                    this.formCompare.imgAttaOid = resArr[0].attaOid;
                    this.formCompare.idcardAttaOid = resArr[1].attaOid;
                    this.formCompare.distance = response.data.similarity;
                    this.formCompare.compareResult = this.rzbdResult;
                    this.formCompare.caseOid = this.caseOid;
                    this.formCompare.name = this.CNName;
                    this.formCompare.sex = this.CNNsex;
                    this.formCompare.cardNo = this.carID;
                    this.formCompare.caseType = 1; //先塞成单办的
                    this.saveWitnessCompare();
                  }
                });
              }
            } else {
              this.$message.error("暂时无法比对！");
            }

            // 是否完成了核验
            this.isCompleteCompare = true;
          })
          .catch(() => {
            // 是否完成了核验
            this.isCompleteCompare = true;
          });
      }

      //获取当前时间
      this.getCurrentTime();
    },
    returnInfo() {
      let info = {};
      if (this.rzbdResult == "1") {
        info.faceImg = this.returnBase64;
      }
      return info;
    },
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(",");
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: "image/jpeg" });
    },

    /** 完成核验 */
    handleCompareComplete() {
      this.$emit("close", this.rzbdResult);
    },

    //保存人证比对信息
    saveWitnessCompare() {
      saveRzbdInfo(this.formCompare).then(respon => {
        console.log(respon.data);
      });
    },
    //忽略结果
    ignoreCompareResult() {
      this.rzbdResult = "1";
      this.$emit("close", this.rzbdResult);
    }
  }
};
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
.scan-picture {
  padding: 0 10px 10px;

  .scan-picture-title {
    color: #333;
    display: block;
    height: 45px;
    background: #cfd3df;
    border-radius: 5px 5px 0px 0px;
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #232f51;
    line-height: 45px;
    padding-left: 20px;
  }

  .scan-picture-area {
    background-color: #000;
    height: 587px;
    position: relative;

    .scan-picture-area--pic {
      width: 100%;
      height: 587px;
    }
    .scan-picture-area--btn {
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

    .hint {
      width: 253px;
      height: 37px;
      color: #fff;
      font-size: 17px;
      background: url(../../assets/image/scan-bg.png) no-repeat;
      background-size: 100% 100%;
      line-height: 45px;
      text-align: center;
      position: absolute;
      bottom: -22px;
      left: 50%;
      display: none;
      transform: translateX(-51%);
    }

    .hint.active {
      display: block;
    }
  }

  .scan-result {
    position: relative;
    height: 587px;
    padding-top: 28px;
    background-color: #eff1f5;
    .contrast {
      width: 408px;
      margin: 0 auto;
      height: 220px;
      > div {
        width: 164px;
        height: 184px;
        text-align: center;
        font-size: 18px;
        color: #333;
        float: left;
        img {
          width: 100%;
          height: 100%;
        }
        p {
          margin: 5px 0;
        }
      }
      > img {
        float: left;
        margin: 64px 25px;
      }
    }
    .result-show {
      border-collapse: collapse;
      width: 100%;
      margin-top: 48px;
      font-size: 18px;
      thead tr {
        th {
          background-color: #6685a3;
          color: #fff;
          padding: 15px;
        }
      }
      tbody {
        tr {
          td {
            color: #48627b;
            padding: 15px 5px 15px 5px;
            text-align: left;
            text-align-last: right;
          }
          td:nth-child(2n) {
            padding: 15px;
            color: #333;
            text-align-last: left;
          }
        }
        tr:nth-child(2n) {
          background-color: #e0e3ea;
        }
        tr:nth-child(2n + 1) {
          background-color: #eff1f5;
        }
        tr:last-child {
          height: 50px;
        }
      }
    }
    .verification {
      position: absolute;
      bottom: 120px;
      left: 50%;
      z-index: 1;
      width: 123px;
      height: 97px;
      margin-left: -60px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .scan-btn {
      background-color: #e4e6ec;
      text-align: center;
      position: absolute;
      width: 100%;
      height: 74px;
      bottom: 0;
      line-height: 6;
      button {
        width: 101px;
        height: 35px;
      }
      .vf-hint {
        width: 253px;
        height: 49px;
        color: #fff;
        font-size: 17px;
        background: url(../../assets/image/scan-bg.png) no-repeat;
        background-size: 100% 100%;
        line-height: 55px;
        text-align: center;
        position: absolute;
        bottom: -20px;
        left: 40%;
        display: none;
      }
      .vf-hint.active {
        display: block;
      }
    }
  }
}
</style>

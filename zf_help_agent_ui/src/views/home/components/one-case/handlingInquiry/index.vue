<template>
  <div class="handlingInquiry">
    <div class="card-title">
      <img src="@/assets/image/bjcx-icon.png" alt srcset /> 办件查询
    </div>
    <div class="card-box">
      <h4>根据办件编号、证件号查询办件信息</h4>
      <el-row :gutter="10">
        <el-form
          :model="ruleFormHandle"
          :rules="rules"
          ref="ruleFormHandle"
          label-width="0"
        >
          <el-col :span="20">
            <div class="card-item">
              <el-form-item prop="code">
                <el-input
                  v-model="ruleFormHandle.code"
                  placeholder="请输入办件编号"
                ></el-input>
              </el-form-item>
              <el-form-item prop="idCard">
                <el-input
                  v-model="ruleFormHandle.idCard"
                  placeholder="请输入证件号查询"
                ></el-input>
                <el-button
                  type="primary"
                  class="read-btn"
                  @click="openRead"
                ></el-button>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="card-item">
              <el-button
                type="primary"
                class="search-btn"
                @click="submitForm('ruleFormHandle')"
              >
                <img src="@/assets/image/handlesearch-icon.png" alt srcset /> 查
                询
              </el-button>
            </div>
          </el-col>
        </el-form>
      </el-row>
    </div>
  </div>
</template>

<script>
import {
  openScanningGun,
  activeScanningGun,
  getActiveMessage
} from "@/api/zc/businessManagement/charge";
import { getIdCardInfo, openIdCard, findIdCard } from "@/api/sys/hardwareScan";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE,{
    ID_CARD_V1,
    ID_CARD_V2,
    ID_CARD_V3
  } from '@/components/HiSpeedCamera/config.js'
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";
export default {
  name: "handlingInquiryIndex",
  data() {
    //验证身份证号格式是否正确
    var checkIdNum = (rule, value, callback) => {
      //const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
      if (!value) {
        // return callback(new Error("证件号码不能为空"));
        callback();
        // } else if (!reg.test(value)) {
        //  return callback(new Error("证件号码不正确"));
      } else {
        callback();
      }
    };
    return {
      ruleFormHandle: {
        code: "",
        idCard: ""
      },
      rules: {
        /*code: [{ required: true, message: "请输入办件编号", trigger: "blur" }],
        idCard: [
          {
            required: false,
            trigger: "blur",
            validator: checkIdNum
          }
        ]*/
      }
    };
  },
  components: {},
  methods: {
    //办件查询校验
    submitForm(ruleFormHandle) {
      this.$emit("submitHandle", this.ruleFormHandle.code);

      this.$refs[ruleFormHandle].validate(valid => {
        if (valid) {
          this.$router.push({
            path: "/businessManagement/doneBusiness",
            query: {
              caseNumber: this.ruleFormHandle.code,
              credentialNumber: this.ruleFormHandle.idCard
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //打开扫码枪
    openScan() {
      openScanningGun().then(res => {
        if (res.StateCode == 0) {
          //成功后激活扫码枪
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.ruleFormHandle.code = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else if (res.StateCode == -1) {
          this.$message.error(res.tips);
        } else if (res.StateCode == -4) {
          //扫码枪已打开
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.ruleFormHandle.code = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          this.$message.error("加载配置失败");
        }
      });
    },
    openRead() {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav1();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardDatav2();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
        this.getIdcardDatav3();
      }
    },
   async getIdcardDatav3(){
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(); //获取身份证信息
        } else {
          return this.$message.warning("请确认设备连接是否正常");
        }
      })
    },
   async getIdcardDataByv3(){
      getdataIdcardv3().then(resData=>{
        if (resData.state == 'sucess' && resData.StateCode == 0){
          let resInfo = JSON.parse(resData.data)
          this.ruleFormHandle.userName = resInfo.name.trim();
          this.ruleFormHandle.idCard = resInfo.number;
        }else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              this.getIdcardDataByv3(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(resData.tips);
          return false;
        }
      })

    },
    async getIdcardDatav1() {
      openIdCard().then(response => {
        //打开设备
        if (response.state == "sucess") {
          this.getIdcardDataByv1(); //重新获取身份证信息
        } else {
          //查找设备
          findIdCard().then(resFind => {
            if (resFind.state == "sucess" && resFind.StateCode == 0) {
              this.getIdcardDataByv1();
            } else {
              this.$message.error("无法找到设备");
              return false;
            }
          });
        }
      });
    },
    async getIdcardDataByv1() {
      getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查设备或连接是否正常!");
          return false;
        }
        if (res.state == "sucess") {
          this.ruleFormHandle.userName = res.CNName.trim();
          this.ruleFormHandle.idCard = res.carID;
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(resp => {
            if (resp.state == "sucess") {
              this.getIdcardDataByv1(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(res.tips);
          return false;
        }
      });
    },
    async getIdcardDatav2() {
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("请检查设备或连接是否正常!");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        carID: resData.data.number
      };
      this.ruleFormHandle.userName = res.CNName.trim();
      this.ruleFormHandle.idCard = res.carID;
    }
  }
};
</script>

<style lang="less" scoped>
.card-title {
  height: 48px;
  line-height: 48px;
  background: #e4e9ec;
  border-radius: 5px 5px 0px 0px;
  padding-left: 9px;
  box-sizing: border-box;
  font-size: 16px;
  color: #2a344c;
  font-weight: bold;
  img {
    display: inline-block;
    vertical-align: middle;
    margin: 0 7px 0 0;
  }
}
.handlingInquiry {
  .card-box {
    margin-top: 0px;
    padding: 30px 50px;
    background-color: #fff;
    height: 200px;
    h4 {
      margin: 0 0 15px 0;
      font-size: 14px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #2c4577;
    }
    .card-item {
      .el-form-item {
        position: relative;
      }
      .el-input {
        margin-bottom: 0px;
        height: 42px;
        background: #d7dfe4;
        border-radius: 3px;
        ::v-deep input {
          height: 42px !important;
          background: #d7dfe4 !important;
          border-radius: 3px;
          &::-webkit-placeholder {
            color: #8f9da6 !important;
          }
        }
        &:last-child {
          margin-bottom: 0;
        }
      }
      .search-btn {
        height: 105px;
        width: 100%;
        background: #0172f6;
        box-shadow: 0px 0px 9px 0px #0172f6;
        border-radius: 3px;
        font-weight: bold;
        img {
          display: block;
          margin: auto;
          width: 23px;
          height: 23px;
          margin-bottom: 10px;
        }
      }
    }
  }
  .el-form-item {
    margin-bottom: 20px;
  }
  .scan-btn {
    position: absolute;
    background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center
      center !important;
    border: none;
    height: 40px;
    box-shadow: none;
    right: 1px;
    top: 1px;
  }
  .read-btn {
    position: absolute;
    background: #c1d0d9 url(~@/assets/image/read-icon.png) no-repeat center
      center !important;
    border: none;
    height: 40px;
    box-shadow: none;
    right: 1px;
    top: 1px;
  }
}
</style>

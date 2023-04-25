<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-04 14:53:34
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-02 14:32:42
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\essentialInformation.vue
 * @Description: 基本信息
-->
<template>
  <div class="intelligentFormFilling-container">
    <!-- <div class="tabHeader" v-if="serviceType === '2'">
      <p
        class="tabLeft"
        @click="changeTab('1')"
        :class="tabType === '1' ? 'active' : ''"
      >
        基本信息
      </p>
      <p
        class="tabRight"
        @click="changeTab('2')"
        :class="tabType === '2' ? 'active' : ''"
        v-html="baseUserInfo.specificMatters.serviceName"
      ></p>
    </div> -->
    <div class="tabContainer">
      <div class="tab1">
        <div class="formArea">
          <p class="dialogContentTitle">
            {{
              baseUserInfo.fillUserInfo.applyUserType === "1"
                ? "申请人"
                : "申请单位"
            }}基本信息
          </p>
          <el-form
            :model="fillUserInfo"
            :rules="baseInfoRules"
            ref="baseUserInfoForm1"
            label-width="20rem"
          >
            <el-form-item
              :label="
                baseUserInfo.fillUserInfo.applyUserType === '1'
                  ? '申请人姓名'
                  : '申请单位名称'
              "
              prop="applyUserName"
            >
              <el-input v-model="fillUserInfo.applyUserName"> </el-input>
            </el-form-item>
            <el-form-item label="证件类型" prop="credentialType">
              <el-select
                v-model.trim="fillUserInfo.credentialType"
                placeholder="请选择证件类型"
                style="min-width: auto; width: 100%"
                :popper-append-to-body="false"
                @change="changeType"
              >
                <el-option
                  v-for="certificateType in certificateTypeList"
                  :key="certificateType.dictOid"
                  :label="certificateType.name"
                  :value="certificateType.dictOid"
                  class="category_style"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="证件号码" prop="credentialNumber">
              <el-input
                v-model.trim="fillUserInfo.credentialNumber"
                name="credentialNumber"
                maxlength="25"
                show-word-limit
                @blur="
                  () => {
                    handleBlur();
                  }
                "
              />
            </el-form-item>
            <el-form-item
              :label="
                baseUserInfo.fillUserInfo.applyUserType === '1'
                  ? '申请人手机'
                  : '申请单位手机'
              "
              prop="applyUserPhone"
            >
              <el-input
                v-model="fillUserInfo.applyUserPhone"
                maxlength="11"
                show-word-limit
              ></el-input>
            </el-form-item>
            <el-form-item
              :label="
                baseUserInfo.fillUserInfo.applyUserType === '1'
                  ? '申请人电话'
                  : '申请单位电话'
              "
              prop="applyUserTel"
            >
              <el-input
                v-model="fillUserInfo.applyUserTel"
                maxlength="11"
                show-word-limit
              ></el-input>
            </el-form-item>
            <el-form-item label="通讯地址" prop="applyUserAddress">
              <el-input v-model="fillUserInfo.applyUserAddress"></el-input>
            </el-form-item>
            <el-form-item
              label="法定代表人："
              prop="legalPersonName"
              v-if="baseUserInfo.fillUserInfo.applyUserType != '1'"
            >
              <el-input v-model="fillUserInfo.legalPersonName"></el-input>
            </el-form-item>
            <el-form-item label="发证方式：" prop="certWay">
              <el-select v-model="fillUserInfo.certWay" placeholder="请选择">
                <el-option
                  v-for="item in certWayList"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="配送公司："
              prop="expressCompany"
              v-if="fillUserInfo.certWay === '物流'"
            >
              <el-select
                v-model="fillUserInfo.expressCompany"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in expressCompanyList"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="地址选择："
              prop="chooseAddress"
              v-if="fillUserInfo.certWay === '物流'"
            >
              <el-cascader
                size="large"
                :options="provinceCityOptions"
                v-model="fillUserInfo.chooseAddress"
                clearable
              >
              </el-cascader>
            </el-form-item>
            <el-form-item
              label="详细地址："
              prop="addresseeDetailAddress"
              v-if="fillUserInfo.certWay === '物流'"
            >
              <el-input
                v-model="fillUserInfo.addresseeDetailAddress"
              ></el-input>
            </el-form-item>

            <el-form-item
              label="邮政编码："
              prop="addresseePostCode"
              v-if="fillUserInfo.certWay === '物流'"
            >
              <el-input v-model="fillUserInfo.addresseePostCode"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="guidance-foot">
        <p @click="toLastStep">上一步</p>
        <p @click="toNextStep('next')">下一步</p>
        <p @click="toNextStep('confirm')" v-if="isVideoOpen">信息确认</p>
      </div>
    </div>
  </div>
</template>
<script>
import { sendMessage,receiveMessage } from "@/api/modules/helpAgent";
import {
  getFormFillInfos,
  nextStepSaveQlCase,
  getSelectCertificateType,
  getAllBasicFieldByOid,
  updateFormInfo,
  saveData,
} from "@/api/modules/business";
import { regionData, CodeToText, TextToCode } from "element-china-area-data";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { rules } from "./constant.js";
import { deepClone } from "@/utils/index";
import { cloneDeep } from "lodash";
export default {
  name: "IntelligentFormFilling",
  components: { Treeselect },
  props: {
    specificMatters: {
      //办理事项数据
      type: Object,
      default: () => {},
    },
    serviceType: {
      type: String,
      default: () => null,
    },
    qlCaseTitleValList: {
      type: Array,
      default: () => [],
    },
    fillUserInfo: {
      type: Object,
      default: () => {},
    },
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    currentServiceIndex: {
      type: Number,
      default: () => 0,
    },
    staffInformation: {
      type: Array,
      default: () => [],
    },
    formFillInfosList: {
      type: Array,
      default: () => [],
    },
    skipSmartForms: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      provinceCityOptions: regionData,
      tabType: "1",
      certificateTypeList: [],
      isZc: 0,
      jump: false,
      baseInfoRules: deepClone(rules),
      formReportList: {},
      loadingForm: false,
      formFieldResult: {},
      formOids: [],
      formFieldRelList: [],
      certWayList: ["自取", "物流", "无结果物"],
      expressCompanyList: ["EMS", "顺丰快递", "其他"],
      name: '',
      cardNo: '',
      wdDataVoVo: {
        name: "",
        cardNo: "",
        phone: "",
        caseOid: "",
      },
      isVideoOpen:false,
    };
  },
  computed: {},
  mounted() {
    if(localStorage.getItem('videoNum')){
      this.isVideoOpen = true;
    }
    console.log(this.fillUserInfo);
    this.getCertificateTypeList();
    this.getFillData();
    // this.getBasicAndFormFieldRel();
    this.getFormFillInfos();
    this.saveWdData();
  },
  // activated() {
  //   this.getCertificateTypeList()
  //   this.getFillData()
  //   this.getFormFillInfos()
  // },
  deactivated() {
    this.$emit("setFillUserInfo", this.fillUserInfo);
  },
  methods: {
    // 信息确认
    toConfirm(){
      let sendContent = [];
      let fillUserInfo = this.fillUserInfo;
      if(this.baseUserInfo.fillUserInfo.applyUserType === "1"){
        sendContent = [
          {
            key:'applyUserName',
            name:'申请人',
            value:fillUserInfo.applyUserName
          },
          {
            key:'credentialType',
            name:'证件类型',
            value:fillUserInfo.credentialType
          },
          {
            key:'credentialNumber',
            name:'证件号码',
            value:fillUserInfo.credentialNumber
          },
          {
            key:'applyUserPhone',
            name:'申请人手机',
            value:fillUserInfo.applyUserPhone
          },
          {
            key:'applyUserAddress',
            name:'通讯地址',
            value:fillUserInfo.applyUserAddress
          },
          {
            key:'legalPersonName',
            name:'法定代表人',
            value:fillUserInfo.legalPersonName
          },
          {
            key:'certWay',
            name:'发证方式',
            value:fillUserInfo.certWay
          },
          {
            key:'expressCompany',
            name:'配送公司',
            value:fillUserInfo.expressCompany
          },
          {
            key:'chooseAddress',
            name:'地址选择',
            value:fillUserInfo.chooseAddress
          },
          {
            key:'addresseeDetailAddress',
            name:'详细地址',
            value:fillUserInfo.addresseeDetailAddress
          },
          {
            key:'addresseePostCode',
            name:'邮政编码',
            value:fillUserInfo.addresseePostCode
          },
          
        ]
      }else{
        sendContent = [  
          {
              key:'applyUserName',
              name:'申请单位',
              value:fillUserInfo.applyUserName
          },
          {
            key:'credentialType',
            name:'证件类型',
            value:fillUserInfo.credentialType
          },
          {
            key:'credentialNumber',
            name:'证件号码',
            value:fillUserInfo.credentialNumber
          },
          {
            key:'applyUserPhone',
            name:'申请单位手机',
            value:fillUserInfo.applyUserPhone
          },
          {
            key:'applyUserAddress',
            name:'通讯地址',
            value:fillUserInfo.applyUserAddress
          },
          {
            key:'certWay',
            name:'发证方式',
            value:fillUserInfo.certWay
          },
          {
            key:'expressCompany',
            name:'配送公司',
            value:fillUserInfo.expressCompany
          },
          {
            key:'chooseAddress',
            name:'地址选择',
            value:fillUserInfo.chooseAddress
          },
          {
            key:'addresseeDetailAddress',
            name:'详细地址',
            value:fillUserInfo.addresseeDetailAddress
          },
          {
            key:'addresseePostCode',
            name:'邮政编码',
            value:fillUserInfo.addresseePostCode
          },
        ]
      }
      let sendData= {
        title:'申请人基本信息',
        sendContent
      }
      let params = {
        "roomOid":localStorage.getItem('roomId'),
        "accessOid":localStorage.getItem('accessId'),
        "userName":this.$store.state.user.basicUserInfo.name,
        "contentType":"3",
        "fileType":"",
        "sureType":"1",
        "sendContent":JSON.stringify(sendData),
        "videoNum":localStorage.getItem('videoNum'),
        "receiveMessageOid":"43",
        "informationStatus":""
      }
      sendMessage(params).then(res=>{
      
      })
    },
    saveWdData(){
      this.wdDataVoVo.name =  this.fillUserInfo.GrName
      this.wdDataVoVo.cardNo= this.fillUserInfo.GrCardNo
      this.wdDataVoVo.phone= this.fillUserInfo.GrPhone
      this.wdDataVoVo.caseOid=this.baseUserInfo.caseOid
      saveData(this.wdDataVoVo).then(response => {
        console.log(response)
      });
    },
    handleBlur(event) {
      this.$emit("blur", event);
    },
    //获取选择的地址中文全称
    changeAreaCode() {
      //因为上边选择好城市之后打印出来的是编码，这里是将编码转换为文字
      let province = CodeToText[this.fillUserInfo.chooseAddress[0]]; //省
      let city = CodeToText[this.fillUserInfo.chooseAddress[1]]; //市
      let area = CodeToText[this.fillUserInfo.chooseAddress[2]]; //区(三级联动带区的话可以加上这个，二级联动不需要这个)
      if (city == "市辖区") {
        city = province;
      }
      //这里是请求赋值
      return province + "," + city + "," + area;
    },
    //获取证件类型
    getCertificateTypeList() {
      getSelectCertificateType({
        type: this.baseUserInfo.fillUserInfo.applyUserType,
      }).then((res) => {
        if (res.code === 200) {
          this.certificateTypeList = res.data;
        }
      });
    },
    //表单字段及预填信息
    getFormFillInfos() {
      const data = {
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        caseOid: this.baseUserInfo.caseOid,
        valOids: this.baseUserInfo.valOids,
      };
      getFormFillInfos(data)
        .then((res) => {
          if (res.code === 200) {
            if (res.data.length) {
              this.$emit("setSkipSmartForms", false);
            } else {
              this.$emit("setSkipSmartForms", true);
            }
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingForm = false;
        });
    },

    getFillData() {
      // this.fillUserInfo.applyUserName = this.baseUserInfo.fillUserInfo.cegisterType === '1' ? this.baseUserInfo.name : this.baseUserInfo.companyName
      // this.fillUserInfo.credentialType = this.baseUserInfo.fillUserInfo.credentialType
      this.fillUserInfo.applyUserPhone = this.baseUserInfo.phone;
      // this.fillUserInfo.credentialNumber = this.baseUserInfo.fillUserInfo.cegisterType === '1' ? this.baseUserInfo.cardNo : this.baseUserInfo.companyCode
      this.fillUserInfo.projectName =
        this.baseUserInfo.specificMatters.serviceName;
      this.fillUserInfo.legalPersonName =
        this.baseUserInfo.fillUserInfo.legalPersonName;
      this.$emit("setFillUserInfo", this.fillUserInfo);
    },

    // changeTab(type) {
    //   this.tabType = type;
    // },
    //选择证件类型
    changeType() {},
    changeProxyFlag() {},
    changeDeliveryWay() {},
    // jumpNext() {
    //   this.jump = true;
    //   this.toNextStep();
    // },
    toNextStep(type) {
      const that = this;
      that.$refs["baseUserInfoForm1"].validate((valid, obj) => {
        if (valid) {
          if(type == 'next'){
            that.nextStepSaveQlCase();
            that.saveWdData();
          }else{
            that.toConfirm();
          }
          
        } else {
          var message = "";
          Object.keys(obj).forEach((item) => {
            message += `<p>${obj[item][0].message}</p>`;
          });
          console.log(Object.keys(obj));
          that.$notify.error({
            title: "表单填写错误",
            dangerouslyUseHTMLString: true,
            message: message,
          });
        }
      });
      // let newArr = [];
      // for (let i = 0; i < 4; i++) {
      //   if (this.$refs[`baseUserInfoForm${i + 1}`]) {
      //     checkForm(this.$refs[`baseUserInfoForm${i + 1}`]);
      //   }
      // }
      // Promise.all(newArr)
      //   .then(() => {})
      //   .catch(function (e) {
      //     console.log(e);
      //   });
      // function checkForm(form) {
      //   //根据form表单，动态生成promise，再对表单校验，都通过了再去处理
      //   let result = new Promise((resolve, reject) => {
      //     form.validate((valid, obj) => {
      //       if (valid) {
      //         resolve();
      //       } else {
      //         that.$notify.error({
      //           title: "表单填写错误",
      //           message: obj[Object.keys(obj)[0]][0].message,
      //         });

      //         reject(form);
      //       }
      //     });
      //   });
      //   newArr.push(result); //push 得到promise的结果
      // }
    },

    nextStepSaveQlCase() {
      this.baseUserInfo.fillUserInfo.addresseeAddress = this.changeAreaCode();
      nextStepSaveQlCase({
        ...this.baseUserInfo.fillUserInfo,
        id: "",
        applyOid: "",
        applyNumber: "1",
        extOid: "",
        caseOid: this.baseUserInfo.caseOid,
        caseStatus: "1",
        applyUserType: this.baseUserInfo.fillUserInfo.applyUserType,
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        sourceType: "1",
        certWay: this.baseUserInfo.fillUserInfo.certWay,
        expressCompany: this.baseUserInfo.fillUserInfo.expressCompany,
        caseNumber: this.baseUserInfo.caseNumber,
        sxServiceMaterialList: [],
        qlCaseTitleValList: this.baseUserInfo.qlCaseTitleValList,
      }).then((res) => {
        if (res.code === 200) {
          this.$store.commit("setServiceOperateStatus", true);
          if (this.baseUserInfo.serviceType === "2") {
            if (this.baseUserInfo.skipSmartForms) {
              this.$emit("nextStep", "materialUpload", "4");
            } else {
              this.$emit("nextStep", "intelligentFormFilling", "3");
            }
          }
        }
      });
    },
    toLastStep() {
      this.$emit("setFormFillInfosList", []);
      this.$emit("nextStep", "situationGuidance", "1");
    },
  },
  watch: {
    currentServiceIndex: {
      handler(val) {
        this.getFillData();
      },
    },
  },
};
</script>
<style lang="scss" scoped>
p {
  padding: 0;
  margin: 0;
}

.intelligentFormFilling-container {
  width: 100%;
  height: 100%;
  background: #ffffff;
  border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;
  padding: 1.625rem 2.8125rem 0 0.8125rem;

  .tabHeader {
    width: 100%;
    height: 4.375rem;
    background: #f3f6f8;
    border-radius: 2.1875rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      width: 50%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.625rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #373737;
      cursor: pointer;
    }

    .active {
      height: 3.75rem;
      // background: linear-gradient(90deg, #bf9e63 0%, #dfca98 100%);
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 1.875rem;
      color: #ffffff;
    }
  }

  .tabContainer {
    width: 100%;
    height: calc(100% - 4.375rem);
    padding-top: 2.625rem;

    // 基本表单样式
    .tab1 {
      width: 100%;
      height: calc(100% - 9.375rem);
      overflow-x: hidden;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 0.4375rem;
        background-color: #fff;
      }

      &::-webkit-scrollbar-thumb {
        width: 0.4375rem;
        height: 0.625rem !important;
        // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 4px;
      }

      .formArea {
        width: 100%;
        height: auto;

        .dialogContentTitle {
          text-align: left;
          padding-left: 1.375rem;
          margin-left: 3rem;
          font-size: 1.625rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          color: #2a344c;
          position: relative;

          &::before {
            content: "";
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 0.5625rem;
            height: 1.375rem;
            background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
            border-radius: 0.3125rem;
          }
        }

        .el-form {
          width: 100%;
          height: auto;
          margin-top: 2.625rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          flex-wrap: wrap;

          .el-form-item {
            height: 3.75rem;
            width: 50%;

            ::v-deep .el-form-item__label {
              font-size: 1.5rem;
              font-family: Source Han Sans CN;
              font-weight: 500;
              color: #373737;
            }

            ::v-deep .el-form-item__content {
              height: 100%;
            }
            .el-cascader {
              width: 100%;
            }
            .el-select {
              width: 100%;

              >>> .el-select-dropdown {
                width: 100%;
                min-width: 100% !important;
              }
            }

            .el-radio-group {
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: flex-start;
            }
          }

          .long {
            width: 100%;
          }
        }
      }
    }

    //电子表单样式
    .tab2 {
      width: 100%;
      height: calc(100% - 9.375rem);
      padding-left: 1.5rem;
      box-sizing: border-box;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 0.4375rem;
        background-color: #fff;
      }

      &::-webkit-scrollbar-thumb {
        width: 0.4375rem;
        height: 0.625rem !important;
        // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 4px;
      }

      .dialogContentTitle {
        text-align: left;
        padding-left: 1.375rem;
        font-size: 1.625rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #2a344c;
        position: relative;

        &::before {
          content: "";
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 0.5625rem;
          height: 1.375rem;
          background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
          border-radius: 0.3125rem;
        }
      }

      .el-table__header-wrapper {
        text-align: left;

        .ele-form {
          text-align: left;

          ::v-deep .el-row {
            text-align: left;

            .el-col {
              text-align: left;

              .el-form {
                .el-row {
                  text-align: left;

                  .el-col {
                    text-align: left;

                    .el-form-item {
                      .el-form-item__content {
                        .el-checkbox-group {
                          height: 100%;
                          text-align: left;
                        }

                        .el-radio-group {
                          text-align: left;
                          line-height: 50px;
                        }

                        .ele-form-tip {
                          color: #2473ff;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      .el-form {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        flex-wrap: wrap;
        margin-top: 2.25rem;

        .el-form-item {
          height: 3.75rem;
          width: 50%;
          text-align: left;
          line-height: 32px;

          ::v-deep .el-form-item__label {
            font-size: 1.5rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #373737;
          }

          .el-form-item__content {
            .el-checkbox-group {
              text-align: left;
              line-height: 32px;
            }

            .el-radio-group {
              text-align: left;
              line-height: 32px;
            }
          }
        }
      }
    }

    .guidance-foot {
      width: 100%;
      height: 9.375rem;
      display: flex;
      align-items: center;
      justify-content: center;

      p {
        cursor: pointer;
        padding: 0 3.75rem;
        width: auto;
        height: 5.1667rem;
        background: #ffffff;
        border: 1px solid #4988f2;
        box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
        border-radius: 2.5833rem;
        font-size: 1.8333rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #2473ff;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 2.1875rem;

        &:nth-child(3) {
          color: #ffffff;
          background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
          box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
        }
      }
    }
  }
}
</style>

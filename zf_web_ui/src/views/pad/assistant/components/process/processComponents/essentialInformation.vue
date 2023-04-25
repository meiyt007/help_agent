<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-11-10 09:53:02
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 14:29:24
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\processComponents\essentialInformation.vue
 * @Description: 办事人基本信息填写
-->
<template>
  <div class="intelligentFormFilling-container">
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
        <p @click="toLastStep"><span>上一步</span></p>
        <p @click="toNextStep">下一步</p>
      </div>
    </div>
  </div>
</template>
<script>
import {
  getFormFillInfos,
  nextStepSaveQlCase,
  getSelectCertificateType,
} from "@/api/modules/business";
import { regionData, CodeToText } from "element-china-area-data";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { rules } from "../../../../../pc/operationFlow/components/constant.js";
import { deepClone } from "@/utils/index";
export default {
  name: "IntelligentFormFilling",
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
    };
  },
  computed: {},
  mounted() {
    this.getCertificateTypeList();
    this.getFillData();
    // this.getBasicAndFormFieldRel();
    this.getFormFillInfos();
  },
  deactivated() {
    this.$emit("setFillUserInfo", this.fillUserInfo);
  },
  methods: {
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
    toNextStep() {
      const that = this;
      that.$refs["baseUserInfoForm1"].validate((valid, obj) => {
        if (valid) {
          this.nextStepSaveQlCase();
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
    // padding-top: 2.625rem;

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
          font-size: 1.6667rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          color: #373737;
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
              font-size: 1.3333rem;
              font-family: Source Han Sans CN;
              font-weight: 500;
              color: #373737;
            }

            ::v-deep .el-form-item__content {
              height: 100%;
              .el-input {
                .el-input__inner {
                  background: rgba(255, 255, 255, 0.55);
                  border: 1px solid #c8cdd3;
                  box-shadow: -1px 0px 8px 0px rgba(85, 139, 220, 0.61);
                  border-radius: 1.872rem;
                }
              }
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
        padding: 1px !important;
        background: linear-gradient(#e1eeff, #0977ff) !important;
        box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.08) !important;
        border-radius: 2.1875rem !important;
        font-size: 1.8571rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #6890e7;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 2.1875rem;
        span {
          padding: 1.375rem 3.75rem;
          display: block;
          width: 100%;
          height: 100%;
          border-radius: 2.1875rem;
          background-color: #fff;
        }

        &:nth-child(2) {
          padding: 1.375rem 3.75rem !important;
          border: none;
          background: linear-gradient(
            90deg,
            #6d93e8 0%,
            #77b0fe 100%
          ) !important;
          box-shadow: 0px 0.4375rem 0px 0px rgb(106 159 231 / 31%),
            inset 0 0.4375rem 0.4375rem 0 rgb(188 212 251),
            inset -0.4375rem 0 0 0 rgb(114 173 249),
            inset 7px 0 0 0 rgb(107 146 230) !important;
          color: #ffffff;
        }
      }
    }
  }
}
</style>

<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-25 14:44:05
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 15:06:24
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\intelligentFormFilling.vue
 * @Description: 电子表单
-->
<template>
  <div class="intelligentFormFilling-container">
    <div class="tabContainer">
      <div
        class="tab2"
        v-loading="loadingForm"
        element-loading-text="拼命加载中"
        element-loading-background="transparent"
      >
        <div
          class="el-table__header-wrapper"
          v-for="item in formFillInfosList"
          :key="item.id"
          :label="item.formName"
          :name="item.name"
        >
          <FormReport
            v-if="item.reportForm.designOid"
            :fillUserInfo="item.fillUserInfo"
            :ref="`reportForm_${item.designOid}_${baseUserInfo.id}`"
            :key="item.designOid"
            :reportForm="item.reportForm"
            :authorizeKey="item.authorizeKey"
            :formNames="item.formNames"
            :isZc="isZc"
            :isOneLevelBindFormData="true"
            @sendRes="item.sendRes"
          />
        </div>
      </div>
      <div class="guidance-foot">
        <p @click="toLastStep"><span>上一步</span></p>
        <p @click="jumpNext"><span>跳过</span></p>
        <p @click="toNextStep">下一步</p>
      </div>
    </div>
  </div>
</template>
<script>
import {
  getFormFillInfos,
  getAllBasicFieldByOid,
  getBasicAndFormFieldRel,
  updateFormInfo,
} from "@/api/modules/business";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { deepClone } from "@/utils/index";
import { cloneDeep } from "lodash";
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
  },
  data() {
    return {
      tabType: "1",
      certificateTypeList: [],
      isZc: 0,
      jump: false,
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
    this.getBasicAndFormFieldRel();
  },
  deactivated() {
    this.$emit("setFillUserInfo", this.fillUserInfo);
  },
  methods: {
    //表单字段及预填信息
    getFormFillInfos() {
      const data = {
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        caseOid: this.baseUserInfo.caseOid,
        valOids: this.baseUserInfo.valOids,
      };

      this.loadingForm = true;
      getFormFillInfos(data)
        .then((res) => {
          if (res.code === 200) {
            this.formOids = [];
            res.data.forEach((item) => {
              const obj = {
                designOid: item.designOid,
                authorizeKey: item.authorizeKey,
                formCode: item.formCode,
                formName: item.formName,
              };
              this.formOids.push(obj);
            });
            this.loadingForm = false;
            let formFillInfosList = [];
            formFillInfosList =
              res.data?.map?.((item, idx) => {
                return {
                  ...item,
                  childFormName: (item?.childFormName?.split(",") ?? []).filter(
                    (item) => item
                  ),
                  name: String(idx + 2),
                  elecFormTimer: null,
                  fillUserInfo: deepClone(this.baseUserInfo.fillUserInfo),
                  reportForm: deepClone({
                    designOid: item.designOid,
                    authorizeKey: item.authorizeKey,
                    reportOid: this.baseUserInfo.caseOid,
                    formData: JSON.stringify(
                      Object.assign(item.formVals, this.formFieldResult)
                    ),
                  }),
                  // 保存表单后返回的接口数据
                  sendRes: (ret) => {
                    return JSON.parse(ret);
                  },
                };
              }) ?? [];
            this.$emit("setFormFillInfosList", cloneDeep(formFillInfosList));
            this.updateFormInfo();
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingForm = false;
        });
    },

    //办件关联更新表单信息
    updateFormInfo() {
      const arr = [];
      const params = {
        caseOid: this.baseUserInfo.caseOid,
        formOids: JSON.stringify(this.formOids),
      };
      updateFormInfo(params).then((res) => {
        if (res.code === 200) {
        }
      });
    },

    //基础表单和电子表单关联字段
    getBasicAndFormFieldRel() {
      const params = {
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
      };
      getBasicAndFormFieldRel(params)
        .then((res) => {
          if (res.code === 200) {
            this.formFieldRelList = res.data;
            this.getAllBasicFieldByOid();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //表单预填信息
    getAllBasicFieldByOid() {
      const data = {
        caseOid: this.baseUserInfo.caseOid,
      };
      getAllBasicFieldByOid(data).then((res) => {
        if (res.code === 200) {
          this.formFieldRelList.forEach((item) => {
            this.formFieldResult[item.formField] =
              this.baseUserInfo.fillUserInfo[item.baseFormField];
          });
          this.formFieldResult = Object(
            this.formFieldResult,
            res.data.formFieldResult
          );
          this.getFormFillInfos();
        }
      });
    },

    jumpNext() {
      this.jump = true;
      this.toNextStep();
    },
    async toNextStep() {
      if (!this.jump) {
        const [reportValid, designOids] = await this.validateReportForm();
        if (reportValid) {
          const loading = this.$loading({
            lock: true,
            text: "正在保存表单信息",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          const { success, formName, error } = await this.saveReportForm();
          if (success) {
            loading.close();
            this.nextStepSaveQlCase();
          }
          if (!success) {
            loading.close();
            return this.$message.warning(
              `【${formName}】，${error.message || "保存失败"}`
            );
          }
        }
      } else {
        this.nextStepSaveQlCase();
      }
    },

    // 电子表单校验
    // validateReportForm() {
    //   for (const item of this.formFillInfosList) {
    //     if (
    //       this.$refs[`reportForm_${item.designOid}_${this.baseUserInfo.id}`][0]
    //     ) {
    //       this.$refs[`reportForm_${item.designOid}_${this.baseUserInfo.id}`][0]
    //         .validateForm()
    //         .then((result) => {
    //           if (result.status) {
    //             for (const item of this.formFillInfosList) {
    //               if (
    //                 this.$refs[
    //                   `reportForm_${item.designOid}_${this.baseUserInfo.id}`
    //                 ][0] &&
    //                 !this.jump
    //               ) {
    //                 this.$refs[
    //                   `reportForm_${item.designOid}_${this.baseUserInfo.id}`
    //                 ][0].saveFormDataAsync();
    //               }
    //             }
    //             this.nextStepSaveQlCase();
    //           }
    //         })
    //         .catch((err) => {
    //           console.log(err);
    //         });
    //     }
    //   }
    // },

    // 电子表单校验
    async validateReportForm() {
      const result = [];
      for (const item of this.formFillInfosList) {
        if (
          this.$refs[`reportForm_${item.designOid}_${this.baseUserInfo.id}`][0]
        ) {
          let ret = await this.$refs[
            `reportForm_${item.designOid}_${this.baseUserInfo.id}`
          ][0].validateForm();
          if (!ret.status) {
            ret.designOid = item.designOid;
          }
          result.push(ret);
        }
      }
      return [
        result.every((item) => item.status),
        result.filter((item) => item.designOid)[0],
      ];
    },

    // 保存电子表单
    async saveReportForm() {
      for (const form of this.formFillInfosList) {
        if (
          this.$refs[`reportForm_${form.designOid}_${this.baseUserInfo.id}`][0]
        ) {
          try {
            this.$refs[
              `reportForm_${form.designOid}_${this.baseUserInfo.id}`
            ][0].formData.caseOid = this.caseOid;
            await this.$refs[
              `reportForm_${form.designOid}_${this.baseUserInfo.id}`
            ][0].saveFormDataAsync();
          } catch (error) {
            console.log("%c [error]:", "color:red;font-weight:700;", error);
            return {
              success: false,
              formName: form.formName,
              error,
            };
          }
        } else {
          return {
            success: false,
            formName: form.formName,
          };
        }
      }
      return {
        success: true,
      };
    },
    nextStepSaveQlCase() {
      this.$store.commit("setServiceOperateStatus", true);
      if (this.baseUserInfo.serviceType === "2") {
        this.$emit("nextStep", "materialUpload", "4");
      }
    },
    toLastStep() {
      this.$emit("setFormFillInfosList", []);
      this.$emit("nextStep", "essentialInformation", "2");
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
                        .el-input {
                          .el-input__inner {
                            background: rgba(255, 255, 255, 0.55);
                            border: 1px solid #c8cdd3;
                            box-shadow: -1px 0px 8px 0px
                              rgba(85, 139, 220, 0.61);
                            border-radius: 1.872rem;
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

        &:nth-child(3) {
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
.vue-treeselect--append-to-body
  .vue-treeselect__menu-container
  .vue-treeselect__menu,
.vue-treeselect--append-to-body
  .vue-treeselect__menu-container
  .vue-treeselect__menu
  ul {
  overflow-y: auto !important;
}
</style>
<style lang="scss">
.vue-treeselect__portal-target {
  .vue-treeselect__menu-container {
    .vue-treeselect__menu {
      overflow-y: auto !important;
    }
  }
}
</style>

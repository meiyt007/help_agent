<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-04 14:54:29
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-26 15:38:14
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\materialCompletion.vue
 * @Description: 材料完成
-->
<template>
  <div class="materialCompletion">
    <div class="content">
      <div class="header">
        <p>材料下载列表</p>
      </div>
      <div class="body-content">
        <div class="materialList">
          <el-checkbox-group
            v-model="checkeMaterialList"
            @change="changeChooseMaterial"
            class="chooseBlock"
          >
            <el-checkbox
              v-for="material in materialList"
              :key="material.materialOid"
              :label="material.materialOid"
            >
              {{ material.materialName }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="showMaterial" v-if="chooseMaterialList.length">
          <iframe
            ref="materialUrl"
            :src="materialUrl"
            style="height: 90%; width: 90%"
          ></iframe>
        </div>
      </div>
    </div>
    <div class="guidance-foot">
      <p @click="toLastStep">上一步</p>
      <p @click="downLoadMaterial">下载材料</p>
      <p @click="toNextStep">已下载材料，下一步</p>
    </div>
    <el-dialog
      :visible.sync="complateVisible"
      width="35rem"
      center
      :show-close="false"
      class="complateVisible"
      append-to-body
      v-dialogDrag
    >
      <img :src="require('@/assets/images/pad/complateProgress.png')" alt="" />
      <p>材料准备已完成，是否继续办理其他业务？</p>
      <div slot="footer" class="dialog-footer">
        <p @click="serviceEnd">否，结束服务</p>
        <p type="primary" @click="continueHandle">是，继续办理业务</p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  queryQlCaseMaterialListByCaseOid,
  nextStepSaveQlCase,
} from "@/api/modules/business.js";
import { completeService } from "@/api/modules/helpAgent";
import { encode } from "@/utils/index";
export default {
  name: "MaterialCompletion",
  props: {
    serviceType: {
      type: String,
      default: () => null,
    },
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    qlCaseTitleValList: {
      type: Array,
      default: () => [],
    },
    sxServiceMaterialList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      checkeMaterialList: [],
      materialList: this.baseUserInfo.sxServiceMaterialList ?? [],
      complateVisible: false,
      materialUrl: "",
      afterService: "",
      chooseMaterialList: [],
    };
  },
  mounted() {
    this.getSituationMaterialListByOids();
  },
  computed: {},
  methods: {
    //切换选中文件
    changeChooseMaterial(data) {
      this.chooseMaterialList = [];
      data.forEach((item) => {
        const arr = this.materialList.filter((ite) => ite.materialOid === item);
        this.chooseMaterialList = [...this.chooseMaterialList, ...arr];
      });
      this.materialUrl =
        this.chooseMaterialList?.[this.chooseMaterialList.length - 1]
          ?.qlCaseMaterialAttaList?.[0]?.templatePdfUrl ?? "";
    },
    //获取材料列表
    getSituationMaterialListByOids() {
      this.materialList = [];
      const data = {
        caseOid: this.baseUserInfo.caseOid,
        // serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        // optionValOids: this.baseUserInfo.valOids.replace(/,/g, ";")
        // optionValOids: this.valOids.(/,/g, ";")
      };
      queryQlCaseMaterialListByCaseOid(data).then((res) => {
        if (res.code === 200) {
          this.materialList = [
            ...res.data.autoProduceMaterialList,
            ...res.data.needUploadMaterialList,
            ...res.data.noSubmissionMaterialList,
          ];
          this.materialList.forEach((item, index) => {
            item.rowIndex = index;
          });
          this.$emit("setSxServiceMaterialList", this.materialList);
        }
      });
    },
    toLastStep() {
      this.$emit("nextStep", "intelligentFormFilling", "3");
    },
    downLoadMaterial() {
      this.chooseMaterialList.forEach((ite) => {
        if (ite.materialSampleAddrYl) {
          const link = document.createElement("a"); //创建下载a标签
          link.href = ite.materialSampleAddrYl;
          link.download =
            ite.materialName +
            "." +
            ite.materialSampleAddrYl.split(".")[
              ite.materialSampleAddrYl.split(".").length - 1
            ]; //下载后文件名
          link.style.display = "none"; //默认隐藏元素
          document.body.appendChild(link); // body中添加元素
          link.click(); // 执行点击事件
          document.body.removeChild(link);
        }
      });
    },
    toNextStep() {
      this.complateVisible = true;
    },

    nextStepSaveQlCase() {
      nextStepSaveQlCase({
        ...this.baseUserInfo.fillUserInfo,
        id: "",
        applyOid: "",
        applyNumber: "1",
        extOid: "",
        caseOid: this.baseUserInfo.caseOid,
        caseStatus: this.baseUserInfo.caseStatus,
        applyUserType: this.baseUserInfo.fillInfo.applyUserType,
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        sourceType: "1",
        caseNumber: this.baseUserInfo.caseNumber,
        sxServiceMaterialList: this.baseUserInfo.sxServiceMaterialList ?? [],
        qlCaseTitleValList: this.baseUserInfo.qlCaseTitleValList,
      }).then((res) => {
        if (res.code === 200) {
          this.$store.commit("setServiceOperateStatus", true);
          this.completeService();
        }
      });
    },
    completeService() {
      let data = {
        workQueueId: this.baseUserInfo.id,
        serviceType: this.baseUserInfo.serviceType,
        serviceMemo: this.baseUserInfo.specificMatters.serviceName
          ? encode(this.baseUserInfo.specificMatters.serviceName)
          : "",
        sxServiceId: this.baseUserInfo.specificMatters.serviceOid,
        qlCaseId: this.baseUserInfo.caseOid,
        pushMemo: "",
        nextServiceAdvise: "3",
        distributionAdvise: "",
        caseNumber: this.baseUserInfo.caseNumber,
      };
      completeService(data)
        .then((res) => {
          if (res.code === 200) {
            this.complateVisible = false;
            if (this.afterService === "end") {
              this.$emit("nextStep", "followServiceSuggestions");
            } else {
              this.$emit("nextStep", "mattersHandling");
            }
          }
        })
        .catch((err) => {
          console.log(err);
          this.complateVisible = false;
          if (this.afterService === "end") {
            this.$emit("nextStep", "followServiceSuggestions");
          } else {
            this.$emit("nextStep", "mattersHandling");
          }
        });
    },
    serviceEnd() {
      this.afterService = "end";
      this.nextStepSaveQlCase();
    },
    continueHandle() {
      this.afterService = "continue";
      this.nextStepSaveQlCase();
    },
  },
  watch: {
    sxServiceMaterialList: {
      handler(val) {
        if (val) {
          this.materialList = val;
        }
      },
      deep: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.materialCompletion {
  width: 100%;
  height: 100%;
  background: #ffffff;
  border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;
  padding: 2.5rem 1.8125rem 0 2.0625rem;

  .content {
    width: 100%;
    height: calc(100% - 9.375rem);
    background: rgba(111, 112, 199, 0.07);
    border-radius: 0.75rem;

    .header {
      width: 100%;
      height: 4.0625rem;
      background: rgba(111, 149, 199, 0.07);
      border-radius: 0.75rem 0.75rem 0px 0px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      padding-left: 1.5rem;

      p {
        font-size: 1.625rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #645c4b;
      }
    }

    .body-content {
      width: 100%;
      height: calc(100% - 4.0625rem);
      display: flex;
      align-items: flex-start;

      .materialList {
        width: 33%;
        height: 100%;
        overflow-y: auto;

        .el-checkbox-group {
          width: 100%;
          height: 100%;
          padding: 1.8125rem 1.625rem;
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          justify-content: flex-start;

          .el-checkbox {
            margin-bottom: 2.1875rem;
          }
        }
      }

      .showMaterial {
        flex: 1;
        height: 100%;
        overflow-y: auto;
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

.complateVisible {
  ::v-deep .el-dialog {
    width: 40rem;
    // height: 25.125rem;
    margin-top: calc(50vh - 12.5625rem) !important;

    .el-dialog__header {
      background: #fff !important;
    }

    .el-dialog__body {
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 0 !important;

      img {
        width: 9rem;
      }

      p {
        font-size: 1.5rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #373737;
        margin: 0.9375rem;
        padding: 0;
        margin: 0;
      }
    }

    .el-dialog__footer {
      height: 4.6875rem;
      margin-top: 5.9375rem;

      .dialog-footer {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;

        p {
          width: 50%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          padding: 0;
          margin: 0;

          &:nth-child(1) {
            background: #a0bce1;
            border-radius: 0px 0px 0px 0.625rem;
            color: #645c4b;
          }

          &:nth-child(2) {
            background: #6090e3;
            color: #ffffff;
            border-radius: 0px 0px 0.625rem 0;
          }
        }
      }
    }
  }
}
</style>
<style lang="scss">
.el-checkbox-group {
  text-align: left;

  .is-checked {
    .el-checkbox__inner {
      background: #6090e3;
      border: 1px solid #6090e3;

      &:hover {
        border: 1px solid #6090e3 !important;
      }
    }

    >>> .el-checkbox__inner:focus {
      border-color: #6090e3 !important;
    }

    .el-checkbox__label {
      font-size: 1.5rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #6090e3;
    }
  }
}

.el-radio-group {
  .is-checked {
    .el-radio__input {
      .el-radio__inner {
        border-color: #6090e3;
        background: #6090e3;
      }
    }

    .el-radio__label {
      color: #6090e3;
    }
  }
}
</style>

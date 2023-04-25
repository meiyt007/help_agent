<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-22 10:06:57
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 16:50:58
 * @FilePath: \hpNewHall\src\views\pad\assistant\components\process\situationGuidance.vue
 * @Description: 办事流程情形引导
-->
<template>
  <div class="guidance-container" v-loading="loadingMaterial">
    <div class="guidance-body">
      <template v-if="situationCheckList.length > 0">
        <div
          class="situationSelection animate fadeInRightBig"
          v-for="(data, index) in situationCheckList"
          :key="index"
          fadeInRightBig
          :style="{ 'animation-duration': (index + 10) * 80 + 'ms' }"
        >
          <p class="problem" :class="data.fillFlag === 1 ? 'fillFlag' : ''">
            {{ data.name }}
          </p>
          <!-- 单选 -->
          <template v-if="data.moreStatus === 0">
            <el-radio-group v-model.trim="data.isSelected" class="chooseOption">
              <el-radio
                v-for="item in data.answers"
                :key="item.oid"
                :disabled="item.isDisabled"
                :label="item.oid"
                @change="handleQA(item.oid, item.titleOid)"
                >{{ item.name }}</el-radio
              >
            </el-radio-group>
          </template>
          <!-- 多选 -->
          <template v-if="data.moreStatus === 1">
            <el-checkbox-group
              v-model.trim="data.isSelected"
              class="chooseOption"
            >
              <el-checkbox
                v-for="item in data.answers"
                :disabled="item.isDisabled"
                :key="item.oid"
                :id="item.oid"
                :label="item.oid"
                @change="handleQA(item.oid, item.titleOid)"
                >{{ item.name }}
              </el-checkbox>
            </el-checkbox-group>
          </template>
        </div>
      </template>
      <div
        v-else
        style="
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        "
      >
        暂无数据
      </div>
    </div>
    <div class="guidance-foot">
      <p @click="toLastStep">上一步</p>
      <p @click="initCase('0', 'save')">保存</p>
      <p @click="toBeforeNextStep">下一步</p>
    </div>
    <div class="animateDom" v-show="showAnimate">
      <!-- <img :src="animateUrl" alt="" /> -->
      <div class="animateImg" :class="showAnimate ? 'animateArea' : ''"></div>
    </div>
    <el-dialog
      title="办事指南"
      :visible.sync="guidelinesForHandlingAffairsDialog"
      width="80%"
      class="guidelinesForHandlingAffairs"
      v-dialogDrag
    >
      <onceInformed
        v-if="guidelinesForHandlingAffairsDialog"
        :serviceOid="baseUserInfo.specificMatters.serviceOid"
        @setMaterialList="setMaterialList"
        @setTransmissionData="setTransmissionData"
        :valOids="oids"
        :showSituation="false"
        :transmissionData="transmissionData"
      >
      </onceInformed>
      <div slot="footer" class="dialog-footer">
        <p @click="oneKeyTransmission">一键传递</p>
        <p @click="compliteNext">
          {{ serviceType === "1" ? "好的，已告知办事人" : "下一步" }}
        </p>
      </div>
    </el-dialog>
    <el-dialog
      title="用户申请信息"
      :visible.sync="informationEntryPopupDialog"
      custom-class="informationEntryPopup"
      width="80%"
      v-dialogDrag
    >
      <informationEntryPopup
        v-if="informationEntryPopupDialog"
        @closeInformationEntryPopup="closeInformationEntryPopup"
        :baseUserInfo="baseUserInfo"
        :situationCheckList="situationCheckList"
        :specificMatters="baseUserInfo.specificMatters"
      />
    </el-dialog>
  </div>
</template>
<script>
import informationEntryPopup from "./informationEntryPopup.vue";
import {
  getSxServiceTitlesNoRelation,
  querySxServiceSituationRelation,
  getSituationMaterialListByOids,
  handlingOnekeyPush,
  nextStepSaveQlCase,
} from "@/api/modules/business";
import { completeService, updateService } from "@/api/modules/helpAgent";
import onceInformed from "./onceInformed.vue";
import {
  formatSituationCheckList,
  getAllSelectedOids,
  getAllSelectedOidsList,
  checkAllSituationsSelected,
  getQlTitleValList,
  removeDuplicate,
} from "./util.js";
import { encode } from "@/utils/index";
export default {
  name: "SituationGuidance",
  components: {
    informationEntryPopup,
    onceInformed,
  },
  props: {
    serviceType: {
      type: String,
      default: () => null,
    },
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    specificMatters: {
      type: Object,
      default: () => {},
    },
    situationCheckList: {
      type: Array,
      default: () => [],
    },
    valOids: {
      type: String,
      default: () => "",
    },
    qlCaseTitleValList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      showAnimate: false,
      caseTitleValList: this.qlCaseTitleValList,
      oids: this.valOids,
      guidelinesForHandlingAffairsDialog: false,
      informationEntryPopupDialog: false,
      loadingMaterial: false,
      transmissionData: false,
      materialList: [],
      materialColumnList: [
        {
          prop: "materialName",
          label: "材料名称",
        },
        {
          prop: "materialType",
          label: "材料类型",
        },
        {
          prop: "materialFormat",
          label: "材料形式",
        },
        {
          prop: "paperNumber",
          label: "份数",
        },
        {
          prop: "materialMustFlag",
          label: "必要性",
        },
      ],
      animateUrl: "",
    };
  },
  mounted() {
    this.getSxServiceTitlesNoRelation();
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  // activated() {
  //   this.getSxServiceTitlesNoRelation()
  // },
  methods: {
    setTransmissionData(data) {
      this.transmissionData = data;
    },
    setMaterialList(data) {
      this.materialList = data;
    },
    // 获取默认情形下 选项信息
    getSxServiceTitlesNoRelation() {
      this.loading = true;
      this.materialTableData = [];

      if (this.situationCheckList.length) {
        const serviceOid = this.situationCheckList[0].serviceOid;
        if (
          this.situationCheckList[0].serviceOid ===
          this.specificMatters.serviceOid
        ) {
          return;
        }
      }
      this.loadingMaterial = true;
      getSxServiceTitlesNoRelation({
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
      })
        .then((res) => {
          this.loadingMaterial = false;
          this.loading = false;
          if (res.code === 200) {
            if (!res.data.length) {
              this.guidelinesForHandlingAffairsDialog = true;
              this.$emit(
                "setSituationCheckList",
                formatSituationCheckList(res.data)
              );
            } else {
              this.$emit(
                "setSituationCheckList",
                formatSituationCheckList(res.data)
              );
            }
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingMaterial = false;
        });
    },

    //根据事项的选项获取标题
    getQuerySxServiceSituationRelation(oid, titleOid) {
      const oids = getAllSelectedOids(this.situationCheckList);
      const params = {
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        valOids: oids,
        currentOid: oid,
        currentTitleOid: titleOid,
        rootTitleOid: this.situationCheckList[0].oid,
      };
      querySxServiceSituationRelation(params)
        .then((res) => {
          if (res.code === 200) {
            // this.situationCheckList = removeDuplicate(formatSituationCheckList(res.data))
            // this.situationCheckList = [...this.situationCheckList]

            this.$emit(
              "setSituationCheckList",
              removeDuplicate(formatSituationCheckList(res.data))
            );
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    handleQA(oid, titleOid) {
      this.getQuerySxServiceSituationRelation(oid, titleOid);
    },

    //跳转下一步
    toBeforeNextStep() {
      const results = this.situationCheckList.every((item) => {
        if (item.fillFlag === 1) {
          if (typeof item.isSelected === "string") {
            return item.isSelected;
          }
          if (typeof item.isSelected === "object") {
            return item.isSelected.length;
          }
        }
      });
      if (!results) {
        const isAllFill = checkAllSituationsSelected(this.situationCheckList);
        if (isAllFill.name) {
          return this.$message.warning(`${isAllFill.name}为必选项!`);
        }
      }
      this.$emit("setSituationCheckList", this.situationCheckList);
      let oids = getAllSelectedOids(this.situationCheckList);
      this.oids = oids.replace(/,/g, ";");
      this.$emit("setValOids", oids);
      if (this.baseUserInfo.specificMatters) {
        this.guidelinesForHandlingAffairsDialog = true;
        this.$emit("showDialog");
      } else {
        setTimeout(() => {
          this.guidelinesForHandlingAffairsDialog = false;
          this.informationEntryPopupDialog = true;
        }, 100);
      }
    },
    toLastStep() {
      this.$emit("nextStep", "mattersHandling");
    },

    //一键传递
    oneKeyTransmission() {
      const that = this;
      let data = {
        workQueueId: that.baseUserInfo.id,
        serviceType: "4",
        pushType: "1",
        serviceMemo: "",
        sxServiceId: that.specificMatters.serviceOid,
        qlCaseId: that.baseUserInfo.caseOid,
        pushMemo: "",
      };
      completeService(data).then((res) => {
        if (res.code === 200) {
          this.guidelinesForHandlingAffairsDialog = false;
          this.playAnimate();
          this.$message.success("已成功传递");
          this.initCase("4",'',function(){
            that.pushHandling();
          });
          //this.pushHandling();
          // this.transmissionData = true;
        }
      });
    },

    //一键推送接口
    pushHandling() {
      const obj = {
        caseOid: this.baseUserInfo.caseOid,
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        pushType: "1",
        name: this.baseUserInfo.name,
        cardNo: this.baseUserInfo.cardNo,
        companyName: this.baseUserInfo.companyName,
        companyCode: this.baseUserInfo.companyCode,
      };
      handlingOnekeyPush(obj).then((res) => {
        if (res.code === 200) {
          // this.pushUser = true;
          this.$message.success("推送成功");
        }
      });
    },

    playAnimate() {
      this.showAnimate = true;
      setTimeout(() => {
        this.showAnimate = false;
        this.compliteNext();
      }, 2500);
      // let timer = setInterval(() => {
      //   for (var i = 0; i < 46; i++) {
      //     this.animateUrl = require(`@/assets/images/continuityAnmate/animate-${i}.png`);
      //     if (i === 45) {
      //       clearInterval(timer);
      //     }
      //   }
      // }, 100);
    },

    //已告知
    compliteNext() {
      this.guidelinesForHandlingAffairsDialog = false;
      if (this.serviceType === "1") {
        this.$emit("nextStep", "mattersHandling");
      } else {
        this.informationEntryPopupDialog = true;
      }
    },

    //情形引导暂存
    initCase(status, type,callBack) {
      this.caseTitleValList = getQlTitleValList(this.situationCheckList);
      this.$emit("setQlCaseTitleValList", this.caseTitleValList);
      const loading = this.$loading({
        lock: true,
        text: "正在保存信息",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      nextStepSaveQlCase({
        ...this.baseUserInfo.fillUserInfo,
        id: "",
        caseOid: this.baseUserInfo.caseOid ?? "",
        applyOid: "",
        extOid: "",
        applyUserType: this.baseUserInfo.fillUserInfo.applyUserType,
        serviceOid: this.baseUserInfo.specificMatters.serviceOid,
        caseStatus: status,
        sourceType: "1",
        projectName: this.baseUserInfo.specificMatters.serviceName,
        caseNumber: "",
        sxServiceMaterialList: this.materialList ?? [],
        qlCaseTitleValList: this.caseTitleValList,
      })
        .then((response) => {
          loading.close();
          if (response.code === 200 && response.data) {
            this.$store.commit("setServiceOperateStatus", true);
            this.$message.success("保存办件成功！");
            // this.$emit("setData", {
            //   caseOid: response.data.caseOid,
            //   caseNumber: response.data.caseNumber,
            //   caseStatus: response.data.caseStatus,
            //   serviceTypeName: response.data.serviceTypeName,
            //   caseMaterialOids: response.data.caseMaterialOids,
            //   createDate: response.data.createDate,
            // });
            this.baseUserInfo.caseOid = response.data.caseOid;
            this.$store.commit("setCaseOid", response.data.caseOid);
            if (status != "4" && !type) {
              this.completeService();
              this.$emit("setFillUserInfo", this.baseUserInfo.fillUserInfo);
            }
            if(callBack){
              callBack();
            }
          } else {
            this.$store.commit("setSaveComponentIndex", 0);
            this.$message.warning("保存办件失败！");
            return false;
          }
        })
        .catch(() => {
          this.$store.commit("setSaveComponentIndex", 0);
          loading.close();
        });
    },

    //情形引导确认用户信息，调用一次服务完成更新服务
    completeService() {
      const that = this;
      let data = {
        workQueueId: this.baseUserInfo.id,
        serviceType: this.baseUserInfo.serviceType,
        serviceMemo: this.baseUserInfo.specificMatters.serviceName
          ? encode(this.baseUserInfo.specificMatters.serviceName)
          : "",
        sxServiceId: this.baseUserInfo.specificMatters.serviceOid,
        qlCaseId: this.baseUserInfo.caseOid,
        pushMemo: "",
        pushType: "",
        distributionAdvise: "",
        serviceStatus: "2",
        caseNumber: this.baseUserInfo.caseNumber,
      };
      completeService(data)
        .then((res) => {
          if (res.code === 200) {
            that.$emit("setHaWorkServiceId", res.data.haWorkServiceId);
            that.toUpdateService(res.data.haWorkServiceId);
          }
        })
        .catch((err) => {
          // this.complateVisible = false
        });
    },

    toUpdateService(haWorkServiceId) {
      const params = {
        haWorkServiceId: haWorkServiceId,
        serviceStatus: "2",
      };
      updateService(params).then((res) => {
        if (res.code === 200) {
          this.nextStep();
        }
      });
    },
    closeInformationEntryPopup(data, type) {
      this.informationEntryPopupDialog = false;
      if (data && type === "next") {
        this.initCase("0");
      }
    },

    nextStep() {
      if (this.serviceType != "1") {
        this.$emit("nextStep", "essentialInformation", "2");
      } else {
        this.$emit("nextStep", "mattersHandling");
      }
    },
  },
  watch: {
    "baseUserInfo.specificMatters": {
      handler(val) {
        if (val) {
          // this.getSxServiceTitlesNoRelation()
        }
      },
      deep: true,
    },
    qlCaseTitleValList: {
      handler(val) {
        this.caseTitleValList = val;
      },
    },
  },
};
</script>

<style lang="scss" scoped>
.guidance-container {
  width: 100%;
  height: 100%;
  background: #ffffff;
  border-radius: 0px 0px 0.5rem 0.5rem;
  padding: 1.625rem 2.8125rem 0 0.8125rem;
  position: relative;

  p {
    padding: 0;
    margin: 0;
  }
  .animateDom {
    width: 100vw;
    height: 100vh;
    // opacity: 0.5;
    // background: #000;
    position: absolute;
    right: 0;
    bottom: 0;
    .animateImg {
      width: 100vw;
      height: 100vh;
    }
    .animateArea {
      background-size: 100% 100%;
      background: url("@/assets/images/continuityAnmate/animate-0.png"),
        url("@/assets/images/continuityAnmate/animate-1.png"),
        url("@/assets/images/continuityAnmate/animate-2.png"),
        url("@/assets/images/continuityAnmate/animate-3.png"),
        url("@/assets/images/continuityAnmate/animate-4.png"),
        url("@/assets/images/continuityAnmate/animate-5.png"),
        url("@/assets/images/continuityAnmate/animate-6.png"),
        url("@/assets/images/continuityAnmate/animate-7.png"),
        url("@/assets/images/continuityAnmate/animate-8.png"),
        url("@/assets/images/continuityAnmate/animate-9.png"),
        url("@/assets/images/continuityAnmate/animate-10.png"),
        url("@/assets/images/continuityAnmate/animate-11.png"),
        url("@/assets/images/continuityAnmate/animate-12.png"),
        url("@/assets/images/continuityAnmate/animate-13.png"),
        url("@/assets/images/continuityAnmate/animate-14.png"),
        url("@/assets/images/continuityAnmate/animate-15.png"),
        url("@/assets/images/continuityAnmate/animate-16.png"),
        url("@/assets/images/continuityAnmate/animate-17.png"),
        url("@/assets/images/continuityAnmate/animate-18.png"),
        url("@/assets/images/continuityAnmate/animate-19.png"),
        url("@/assets/images/continuityAnmate/animate-20.png"),
        url("@/assets/images/continuityAnmate/animate-21.png"),
        url("@/assets/images/continuityAnmate/animate-22.png"),
        url("@/assets/images/continuityAnmate/animate-23.png"),
        url("@/assets/images/continuityAnmate/animate-24.png"),
        url("@/assets/images/continuityAnmate/animate-25.png"),
        url("@/assets/images/continuityAnmate/animate-26.png"),
        url("@/assets/images/continuityAnmate/animate-27.png"),
        url("@/assets/images/continuityAnmate/animate-28.png"),
        url("@/assets/images/continuityAnmate/animate-29.png"),
        url("@/assets/images/continuityAnmate/animate-30.png"),
        url("@/assets/images/continuityAnmate/animate-31.png"),
        url("@/assets/images/continuityAnmate/animate-32.png"),
        url("@/assets/images/continuityAnmate/animate-33.png"),
        url("@/assets/images/continuityAnmate/animate-34.png"),
        url("@/assets/images/continuityAnmate/animate-35.png"),
        url("@/assets/images/continuityAnmate/animate-36.png"),
        url("@/assets/images/continuityAnmate/animate-37.png"),
        url("@/assets/images/continuityAnmate/animate-38.png"),
        url("@/assets/images/continuityAnmate/animate-39.png"),
        url("@/assets/images/continuityAnmate/animate-40.png"),
        url("@/assets/images/continuityAnmate/animate-41.png"),
        url("@/assets/images/continuityAnmate/animate-42.png"),
        url("@/assets/images/continuityAnmate/animate-43.png"),
        url("@/assets/images/continuityAnmate/animate-44.png"),
        url("@/assets/images/continuityAnmate/animate-45.png");
      animation: sprite5 3s linear;
    }
    @keyframes sprite5 {
      0% {
        background: url("@/assets/images/continuityAnmate/animate-0.png")
          no-repeat;
      }
      2% {
        background: url("@/assets/images/continuityAnmate/animate-1.png")
          no-repeat;
      }
      4% {
        background: url("@/assets/images/continuityAnmate/animate-2.png")
          no-repeat;
      }
      6% {
        background: url("@/assets/images/continuityAnmate/animate-3.png")
          no-repeat;
      }
      8% {
        background: url("@/assets/images/continuityAnmate/animate-4.png")
          no-repeat;
      }
      10% {
        background: url("@/assets/images/continuityAnmate/animate-5.png")
          no-repeat;
      }
      12% {
        background: url("@/assets/images/continuityAnmate/animate-6.png")
          no-repeat;
      }
      14% {
        background: url("@/assets/images/continuityAnmate/animate-7.png")
          no-repeat;
      }
      16% {
        background: url("@/assets/images/continuityAnmate/animate-8.png")
          no-repeat;
      }
      18% {
        background: url("@/assets/images/continuityAnmate/animate-9.png")
          no-repeat;
      }
      20% {
        background: url("@/assets/images/continuityAnmate/animate-10.png")
          no-repeat;
      }
      22% {
        background: url("@/assets/images/continuityAnmate/animate-11.png")
          no-repeat;
      }
      24% {
        background: url("@/assets/images/continuityAnmate/animate-12.png")
          no-repeat;
      }
      26% {
        background: url("@/assets/images/continuityAnmate/animate-13.png")
          no-repeat;
      }
      28% {
        background: url("@/assets/images/continuityAnmate/animate-14.png")
          no-repeat;
      }
      30% {
        background: url("@/assets/images/continuityAnmate/animate-15.png")
          no-repeat;
      }
      32% {
        background: url("@/assets/images/continuityAnmate/animate-16.png")
          no-repeat;
      }
      34% {
        background: url("@/assets/images/continuityAnmate/animate-17.png")
          no-repeat;
      }
      36% {
        background: url("@/assets/images/continuityAnmate/animate-18.png")
          no-repeat;
      }
      38% {
        background: url("@/assets/images/continuityAnmate/animate-19.png")
          no-repeat;
      }
      40% {
        background: url("@/assets/images/continuityAnmate/animate-20.png")
          no-repeat;
      }
      42% {
        background: url("@/assets/images/continuityAnmate/animate-21.png")
          no-repeat;
      }
      44% {
        background: url("@/assets/images/continuityAnmate/animate-22.png")
          no-repeat;
      }
      46% {
        background: url("@/assets/images/continuityAnmate/animate-23.png")
          no-repeat;
      }
      48% {
        background: url("@/assets/images/continuityAnmate/animate-24.png")
          no-repeat;
      }
      50% {
        background: url("@/assets/images/continuityAnmate/animate-25.png")
          no-repeat;
      }
      52% {
        background: url("@/assets/images/continuityAnmate/animate-26.png")
          no-repeat;
      }
      54% {
        background: url("@/assets/images/continuityAnmate/animate-27.png")
          no-repeat;
      }
      56% {
        background: url("@/assets/images/continuityAnmate/animate-28.png")
          no-repeat;
      }
      58% {
        background: url("@/assets/images/continuityAnmate/animate-29.png")
          no-repeat;
      }
      60% {
        background: url("@/assets/images/continuityAnmate/animate-30.png")
          no-repeat;
      }
      62% {
        background: url("@/assets/images/continuityAnmate/animate-31.png")
          no-repeat;
      }
      64% {
        background: url("@/assets/images/continuityAnmate/animate-32.png")
          no-repeat;
      }
      66% {
        background: url("@/assets/images/continuityAnmate/animate-33.png")
          no-repeat;
      }
      68% {
        background: url("@/assets/images/continuityAnmate/animate-34.png")
          no-repeat;
      }
      70% {
        background: url("@/assets/images/continuityAnmate/animate-35.png")
          no-repeat;
      }
      72% {
        background: url("@/assets/images/continuityAnmate/animate-36.png")
          no-repeat;
      }
      74% {
        background: url("@/assets/images/continuityAnmate/animate-37.png")
          no-repeat;
      }
      76% {
        background: url("@/assets/images/continuityAnmate/animate-38.png")
          no-repeat;
      }
      78% {
        background: url("@/assets/images/continuityAnmate/animate-39.png")
          no-repeat;
      }
      80% {
        background: url("@/assets/images/continuityAnmate/animate-40.png")
          no-repeat;
      }
      82% {
        background: url("@/assets/images/continuityAnmate/animate-41.png")
          no-repeat;
      }
      84% {
        background: url("@/assets/images/continuityAnmate/animate-42.png")
          no-repeat;
      }
      86% {
        background: url("@/assets/images/continuityAnmate/animate-43.png")
          no-repeat;
      }
      88% {
        background: url("@/assets/images/continuityAnmate/animate-44.png")
          no-repeat;
      }
      100% {
        background: url("@/assets/images/continuityAnmate/animate-45.png")
          no-repeat;
      }
    }
  }
  .guidance-body {
    width: 100%;
    height: calc(100% - 7.375rem);
    overflow-y: auto;
    overflow-x: hidden;

    .situationSelection {
      width: 100%;
      height: auto;

      .problem {
        width: 100%;
        height: 3.125rem;
        text-align: left;
        background: #f3f6f8;
        border-radius: 1.5625rem;
        padding-left: 1.375rem;
        line-height: 3.125rem;
        font-size: 1.625rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #373737;
      }

      .fillFlag {
        &:before {
          content: "*";
          color: #f56c6c;
          margin-right: 4px;
        }
      }

      .chooseOption {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        flex-wrap: wrap;
        padding: 1.5rem 0 1.5rem 1.8125rem;

        .el-radio {
          min-width: 13.875rem;
          height: 3.9375rem;
          background: rgba(237, 240, 244, 0.55);
          border: 1px solid #c8cdd3;
          border-radius: 1.9375rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 3rem 0 1.3125rem;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          margin-right: 2.875rem;
          margin-bottom: 1.5rem;

          ::v-deep .el-radio__inner {
            display: inline-block;
            width: 1.625rem;
            height: 1.625rem;
            background: url("@/assets/images/pad/choose.png") no-repeat;
            background-size: 100% 100%;
          }

          // img {
          //   width: 1.625rem;
          //   margin-right: 0.6875rem;
          // }

          span {
            color: #3f3f3f;
          }
        }

        .is-checked {
          background: linear-gradient(
            90deg,
            #2473ff 0%,
            #56b1fd 100%
          ) !important;
          box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
          border: none;

          ::v-deep .el-radio__label {
            color: #fff;
          }

          ::v-deep .el-checkbox__label {
            color: #fff;
          }

          ::v-deep .el-checkbox__inner {
            background: #fff;

            &:after {
              border-color: #6090e3;
            }
          }

          ::v-deep .el-radio__inner {
            background: url("@/assets/images/pad/chooseActivate.png") no-repeat;
            background-size: 100% 100%;

            &:after {
              display: none;
            }
          }
        }

        .el-checkbox {
          min-width: 13.875rem;
          height: 3.9375rem;
          background: rgba(237, 240, 244, 0.55);
          border: 1px solid #c8cdd3;
          border-radius: 1.9375rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 3rem 0 1.3125rem;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          margin-right: 2.875rem;
          margin-bottom: 1.5rem;
        }
      }
    }
  }

  .guidance-foot {
    width: 100%;
    height: 6.375rem;
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

.content-body {
  width: 100%;
  height: auto;

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

  .essentialinformation {
    width: 100%;
    height: auto;

    .essentialinformationForm {
      width: 100%;
      height: auto;
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      border: 0.0625rem solid #c8cdd3;
      margin-top: 1.0625rem;
      box-sizing: content-box;
      flex-wrap: wrap;

      .items {
        width: 50%;
        height: 3.1875rem;
        border-collapse: collapse;

        &:nth-child(odd) {
          .el-input {
            border-right: 0.0625rem solid #c8cdd3;
          }
        }

        &:nth-last-child(2) {
          width: 100%;
          height: auto;
          min-height: 3.1875rem;
        }

        &:nth-last-child(1) {
          width: 100%;
        }

        .el-input {
          width: 100%;
          height: 3.1875rem;
          border-bottom: 0.0625rem solid #c8cdd3;
          box-sizing: content-box;
          border-collapse: collapse;
          display: flex;
          align-items: center;
          justify-content: flex-start;

          ::v-deep .el-input-group__prepend {
            width: 13.85rem;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            background: rgba(237, 240, 244, 0.55);
            border: none;
            padding: 0;
            padding-right: 1.5rem;
            font-size: 1.375rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #121b2f;
            border-right: 0.0625rem solid #c8cdd3;
            border-collapse: collapse;
            text-align: right;
          }

          ::v-deep .el-input__inner {
            width: calc(100% - 13.85rem);
            height: 90%;
            line-height: 90%;
            border: none;
            padding: 0 0.9375rem !important;
          }
        }

        .step {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;

          .left {
            width: 13.85rem;
            height: 100%;
            background: rgba(237, 240, 244, 0.55);
            border-right: 1px solid #c8cdd3;
            font-size: 1.375rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #121b2f;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding-right: 1.5rem;
          }

          .right {
            width: calc(100% - 13.85rem);
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            justify-content: space-around;
            padding: 0 0.9375rem;
          }

          p {
            padding: 0;
            margin: 0;
          }
        }
      }
    }
  }

  .materialList {
    width: 100%;
    height: auto;
    margin-top: 2.75rem;

    .el-table {
      margin-top: 1.125rem;
    }
  }
}

.dialog-footer {
  p {
    &:nth-child(1) {
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      color: #2473ff;
      margin-right: 2.1875rem;
      cursor: pointer;
    }

    &:nth-child(2) {
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      color: #ffffff;
    }
  }
}

.bage-necessery {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  span {
    margin: 0;
    width: 5.7857rem;
    height: 2.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 1.25rem;
    background-color: #d3e6f5;
    font-size: 1.2857rem;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #3ba6ff;
  }
}

.guidelinesForHandlingAffairs {
  ::v-deep .el-dialog {
    height: 80vh;
  }
}

.informationEntryPopup {
  ::v-deep .el-dialog {
    height: auto !important;
  }
}
</style>

<style>
.carHistory-enter {
  animation: carHistory-dialog-fade-in 3s ease;
}
.carHistory-leave {
  animation: carHistory-dialog-fade-out 3s ease forwards;
}
.carHistory-enter-active {
  animation: carHistory-dialog-fade-in 3s ease;
}
.carHistory-leave-active {
  animation: carHistory-dialog-fade-out 3s ease forwards;
}

@keyframes carHistory-dialog-fade-in {
  0% {
    transform: translate3d(-100%, 0, 0);
    opacity: 1;
  }
  100% {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}
@keyframes carHistory-dialog-fade-out {
  0% {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
  100% {
    transform: translate3d(-100%, 0, 0);
    opacity: 1;
  }
}
.fullscreen-enter {
  animation: fullscreen-dialog-fade-in 3s ease;
}
.fullscreen-leave {
  animation: fullscreen-dialog-fade-out 3s ease forwards;
}
.fullscreen-enter-active {
  animation: fullscreen-dialog-fade-in 3s ease;
}
.fullscreen-leave-active {
  animation: fullscreen-dialog-fade-out 3s ease forwards;
}

@keyframes fullscreen-dialog-fade-in {
  0% {
    transform: translate3d(0, -100%, 0);
    opacity: 1;
  }
  100% {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}

@keyframes fullscreen-dialog-fade-out {
  0% {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
  100% {
    transform: translate3d(0, -100%, 0);
    opacity: 1;
  }
}
.right-enter,
.right-leave-to {
  transform: translateX(-130px);
  opacity: 0;
}

.right-leave-active,
.right-enter-active {
  transition: all 3s linear;
}
</style>

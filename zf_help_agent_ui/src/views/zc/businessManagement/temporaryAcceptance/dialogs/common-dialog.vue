<template>
  <el-dialog
    v-dialog-drag
    title="办件修改"
    width="1200px"
    height="800px"
    top="10vh"
    :visible.sync="visible"
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    custom-class="common-dialog"
  >
    <!-- 面包屑 -->
    <div class="breadcrumb">
      <div class="breadcrumb-container">
        <div class="breadcrumb-item-oneline-bg" :class="{breadcrumbItemSixBg:clzsFlag}" />
        <div
          class="breadcrumb-item-oneline" :class="{breadcrumbItemSix:clzsFlag}"
          :style="{ width: calcOnelineWidth }"
        />
        <div
          v-for="item in BREADCRUMB"
          :key="item.id"
          :class="['breadcrumb-item', item.isActive ? 'active' : '']"
          :style="{ left: item.left }"
        >
          <span class="breadcrumb-item-num">{{ item.id }}</span>
          <span class="breadcrumb-item-title">{{ item.title }}</span>
        </div>
      </div>
    </div>

    <!-- 主要内容 -->
    <keep-alive
      :include="[
        'IntelligentQA',
        'InfoKnowForOnce',
        'IntelligentFormFilling',
        'ElectronizationOfMaterials',
        'IntelligentAudit',
        'BusinessAcceptance',
      ]"
    >
      <component
        class="common-dialog-container"
        ref="component"
        :is="componentId"
        :serviceOid="serviceOid"
        :caseOid="caseOid"
        :serviceName="serviceName"
        :cegisterType="cegisterType"
        :loginUser="loginUser"
        :sxServiceMaterialList="sxServiceMaterialList"
        :situationCheckList="situationCheckList"
        :currentHotSituationName="currentHotSituationName"
        :qlCaseTitleValList="qlCaseTitleValList"
        :systemType="systemType"
        :caseNumber="caseNumber"
        :ruleForm="ruleForm"
        :serviceForm="serviceForm"
        :dueDate="dueDate"
        :caseMaterialOids="caseMaterialOids"
        :situationAnswerList="situationAnswerList"
        :onlyMounted="true"
        :rqbzDueDate="rqbzDueDate"
        :rqbzMaterialNames="rqbzMaterialNames"
        @nextStep="nextStep"
        @lastStep="lastStep"
        @setQlCaseTitleValList="(list) => (qlCaseTitleValList = list)"
        @systemType="(data) => (systemType = data)"
        @case-close="caseClose"
        @setRuleForm="(data) => (ruleForm = data)"
        @setServiceFormData="(data) => (serviceForm = data)"
        @setDueData="(date) => (dueDate = date)"
        @caseMaterialOids="(data) => (caseMaterialOids = data)"
        @setSxServiceMaterialList="(data) => (sxServiceMaterialList = data)"
        @setSituationCheckList="(data) => (situationCheckList = data)"
        @setHotSituationName="(data) => (currentHotSituationName = data)"
        @setRqbzTime="(data) => (rqbzDueDate = data)"
        @setRqbzMaterialNames="(data) => (rqbzMaterialNames = data)"
        @close="$emit('update:commmonVisible', false)"
      />
    </keep-alive>
  </el-dialog>
</template>

<script>
import IntelligentQA from "../components/intelligentQA.vue"; // 智能问答
import InfoKnowForOnce from "../components/infoKnowForOnce.vue"; // 一件告知
import ElectronizationOfMaterials from "../../windowAcceptance/components/electronizationOfMaterials.vue"; // 电子材料
import IntelligentFormFilling from "../components/intelligentFormFilling.vue"; // 智能填表
import IntelligentAudit from "../components/intelligentAudit.vue"; // 智能审核
import BusinessAcceptance from "../../windowAcceptance/components/businessAcceptance.vue"; // 业务受理
import intelligentPrequalification from '../../windowAcceptance/components/intelligentPrequalification.vue' //秒批秒办智能审核
import businessSubmission from '../../windowAcceptance/components/businessSubmission.vue' //秒批秒办办件提交
import { BREAD_CRUMB_TEMP, BREAD_CRUMB_BOOTSTRAP } from './constant.js';
import { mapGetters } from "vuex";

export default {
  name: "CommonDialog",
  inheritAttrs: false,
  props: {
    commmonVisible: {
      type: Boolean,
      default: false
    },
    serviceOid: {
      type: String,
      default: ""
    },
    loginUser: {
      type: Object,
      default: () => ({})
    },
    serviceName: {
      type: String,
      default: ""
    },
    situationAnswerList: {
      type: Array,
      default: () => []
    },
    caseOid: {
      type: String,
      default: ""
    },
    caseNumber: [Number, String],
    cegisterType: [String, Number],
    /** 是否是暂存受理组件 */
    isTempComponent: {
      type: Boolean,
      default: false
    },
    sxServiceMaterialList:{
      type:Array,
      default:[]
    },
    handleType:String,
    ruleForm:{
      type:Object,
      default:{}
    }
  },
  components: {
    IntelligentQA,
    InfoKnowForOnce,
    ElectronizationOfMaterials,
    IntelligentFormFilling,
    IntelligentAudit,
    BusinessAcceptance,
    intelligentPrequalification,
    businessSubmission
  },
  data () {
    return {
      BREADCRUMB: [],
      qlCaseTitleValList: [],
      serviceTypeName: "",
      createDate: "",
      systemType: "",
      serviceForm: {},
      dueDate: "", // 补正时间
      caseMaterialOids: {},
      timer: null,
      situationCheckList: [],
      currentHotSituationName: "",
      rqbzDueDate: '', // 容缺补正时间
      rqbzMaterialNames: ""
    };
  },
  computed: {
     ...mapGetters([
      "clzsFlag"
    ]),

    visible: {
      get () {
        return this.commmonVisible;
      },
      set () {
        this.$emit("update:commmonVisible", false);
      }
    },

    calcOnelineWidth () {
      // 1 => 50 2=> 50+162 3=>50+162+162 4=>50+162+162+162 5=>50+162+162+162+162 6=>50+162+162+162+162+102
      const len = this.BREADCRUMB.filter(item => item.isActive).length;
      let totalLen = this.BREADCRUMB.length;
      if (len === 1) {
        return "50px";
      } else if (len > 1 && len < totalLen) {
        return this.clzsFlag ? (50 + (len - 1) * 162) + 'px' : (50 + (len - 1) * 197) + 'px';
      } else {
        return this.clzsFlag ? '800px' : '770px'
      }
    },

    componentId () {
      const len = this.BREADCRUMB.filter(item => item.isActive).length;
      return this.BREADCRUMB[len - 1].componentId || "";
    }
  },
  watch: {
    commmonVisible: {
      handler () {
        // 清除缓存
        localStorage.removeItem("idCardInfo");
      },
      immediate: true
    }
  },
  created () {
    this.BREADCRUMB = this.setBreadcrumb();

     /** 关闭智能审核 */
    if(!this.clzsFlag){
      this.BREADCRUMB.length=5;
      this.BREADCRUMB[4].title='业务受理';
      this.BREADCRUMB[4].componentId='BusinessAcceptance';
      this.BREADCRUMB[0].left='75px';
      this.BREADCRUMB[1].left='270px';
      this.BREADCRUMB[2].left='458px';
      this.BREADCRUMB[3].left='662px';
      this.BREADCRUMB[4].left='858px';
    }
  },
  methods: {
    setBreadcrumb () {
    const breadCrumbTemp = JSON.parse(JSON.stringify( BREAD_CRUMB_TEMP))
const breadCrumbBootStrap = JSON.parse(JSON.stringify( BREAD_CRUMB_BOOTSTRAP))
const bannerList =  this.isTempComponent? breadCrumbTemp : breadCrumbBootStrap
debugger
  if(this.handleType==="2"){
    this.$set(bannerList,[4],{
    id: 5,
    left: "697px",
    isActive: false,
    title: "智能审核",
    componentId: "intelligentPrequalification"
  })

    this.$set(bannerList,[5],{
    id: 6,
    left: "858px",
    isActive: false,
    title: "办件提交",
    componentId: "businessSubmission"
  })
  }
      return bannerList;
    },

    nextStep (id) {
      const next = this.BREADCRUMB.find(item => item.id === id);
      next.isActive = true;
    },

    lastStep (id) {
      const index = this.BREADCRUMB.findIndex(item => item.id === id);
      this.BREADCRUMB.forEach((item, idx) => {
        if (idx > index) {
          item.isActive = false;
        }
      });
    },

    caseClose (data) {
      this.$emit("update:commmonVisible", false);
      this.$emit("case-close", data);
    }
  }
};
</script>

<style lang="scss">
@import '@/assets/styles/mixin/index.scss';
.el-dialog.common-dialog {
  background: rgba(227, 232, 235, 1);

  .breadcrumb {
    height: 78px;
    background: #ffffff;
    border-radius: 5px;
    padding: 0 90px;

    .breadcrumb-container {
      background-image: url(~@/assets/image/step-bg.png);
      background-size: 100% 100%;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      cursor: default;

      .breadcrumb-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        position: absolute;
        top: 6px;

        &-oneline {
          width: 50px;
          border: 2px solid #2966ff;
          background: #2966ff;
          position: absolute;
          top: 24px;
          left: 120px;
        }

        &-oneline-bg {
          width: 788px;
          border: 1px dashed #2966ff;
          opacity: 0.2;
          position: absolute;
          top: 25px;
        }

        &.active {
          .breadcrumb-item-num {
            background-color: #fff;
            background-image: url(~@/assets/image/step-item-bg.png);
            background-size: 100% 100%;
            color: #fff;
          }

          .breadcrumb-item-title {
            color: rgba(46, 125, 255, 1);
          }
        }

        &-num {
          width: 37px;
          height: 37px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #e6e8eb;
          border-radius: 50%;
          color: #8b9bb4;
          font-family: DIN Black;
        }

        &-title {
          margin-top: 8px;
          font-size: 14px;
          font-weight: 400;
          color: #2a344c;
        }
      }
      .breadcrumbItemSixBg{
        width: 796px;
      }
      .breadcrumbItemSix{
        left: 95px;
      }
    }
  }

  .common-dialog-container {
    margin-top: 10px;
    height: calc(100% - 90px);
    background: #ffffff;
    border-radius: 5px;
    // overflow-y: scroll;
    // @include util-change-scrollbar(#b1b5c1, #c8ccd6);

    .common-dialog--title {
      font-size: 14px;
      font-weight: bold;
      color: #2a344c;
      position: relative;
      padding-left: 10px;
      margin-bottom: 25px;

      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 3px;
        height: 16px;
        background: #2e7dff;
        border-radius: 2px;
      }
    }

    .common-dialog-content {
      height: calc(100% - 36px - 25px);
      padding: 30px 20px;
      position: relative;
    }
    .common-dialog-footer {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 25px;

      .el-button {
        margin-left: 30px;
        margin-right: unset;
      }
    }
  }

  form {
    margin: unset;
    padding: unset;
    height: 100% !important;
  }

  .data-table {
    td {
      background-color: #fff !important;
      text-align: center !important;
    }
  }

  .common-dalog-table:not(.el-table__header):not(.el-table__body) {
    width: 100%;
    margin-bottom: 30px;
    border: 1px solid #e0e6f0;
    border-collapse: collapse;

    th {
      // height: 40px;
      padding: 12px 0;
      background: #edf0f5;
      color: #121b2f;
      font-size: 14px;
      border: 1px solid #e0e6f0;
    }

    td {
      border: 1px solid #e0e6f0;
      // padding: unset;
      // height: 40px;
      padding: 12px 0;
      font-size: 14px;
      font-weight: 400;

      &:nth-of-type(2n + 1) {
        background: #edf0f5;
        text-align: right;
        color: #121b2f;
      }

      &:nth-of-type(2n) {
        background: rgba(237, 240, 245, 0);
        // width: 67px;
        color: #2a344c;
        padding: 0 10px;
      }
    }
  }
}
</style>

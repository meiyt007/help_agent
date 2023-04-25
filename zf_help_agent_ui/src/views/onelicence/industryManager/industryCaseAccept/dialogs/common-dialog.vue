<template>
  <el-dialog
    v-dialog-drag
    :visible.sync="visible"
    width="1158px"
    height="800px"
    top="10vh"
    :title="comboDirectoryName"
    custom-class="common-dialog"
    append-to-body
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
        'ElectronizationOfMaterials',
        'IntelligentFormFilling',
        'IntelligentAudit',
        'BusinessAcceptance',
      ]"
    >
      <component
        class="common-dialog-container"
        ref="component"
        :is="componentId"
        :comboDirectoryOid="comboDirectoryOid"
        :comboDirectoryName="comboDirectoryName"
        :pCegisterType="pCegisterType"
        :loginUser="loginUser"
        :sxServiceMaterialList="sxServiceMaterialList"
        :situationCheckList="situationCheckList"
        :currentHotSituationName="currentHotSituationName"
        :qlCaseTitleValList="qlCaseTitleValList"
        :caseOid="caseOid"
        :caseId="caseId"
        :systemType="systemType"
        :caseNumber="caseNumber"
        :ruleForm="ruleForm"
        :serviceForm="serviceForm"
        :dueDate="dueDate"
        :caseMaterialOids="caseMaterialOids"
        :situationAnswerList="situationAnswerList"
        :isQlCaseChanged="isQlCaseChanged"
        :situation="situation"
        :isTempComponent="isTempComponent"
        :rqbzDueDate="rqbzDueDate"
        @setServiceObject="
          (data) => {
            $emit('update:pCegisterType', data)
          }
        "
        :comeFormArtific="comeFormArtific"
        :serviceRoot="serviceRoot"
        :rqbzMaterialNames="rqbzMaterialNames"
        :lastComponentId="lastComponentId"
        @nextStep="nextStep"
        @lastStep="lastStep"
        @setQlCaseTitleValList="(list) => (qlCaseTitleValList = list)"
        @setData="setData"
        @systemType="(data) => (systemType = data)"
        @case-close="caseClose"
        @setRuleForm="(data) => (ruleForm = data)"
        @setServiceFormData="(data) => (serviceForm = data)"
        @setDueData="(date) => (dueDate = date)"
        @caseMaterialOids="(data) => (caseMaterialOids = data)"
        @setSxServiceMaterialList="(data) => (sxServiceMaterialList = data)"
        @setSituationCheckList="(data) => (situationCheckList = data)"
        @setHotSituationName="(data) => (currentHotSituationName = data)"
        @setIsQlCaseChanged="(data) => (isQlCaseChanged = data)"
        @close="$emit('update:commmonVisible', false)"
        @setSituation="(data) => (situation = data)"
        @setRqbzMaterialNames="(data) => (rqbzMaterialNames = data)"
        @setRqbzTime="(data) => (rqbzDueDate = data)"
      />
    </keep-alive>
  </el-dialog>
</template>

<script>
import IntelligentQA from '../components/intelligentQA.vue'; // 智能问答
import InfoKnowForOnce from '../components/infoKnowForOnce.vue'; // 一件告知
import ElectronizationOfMaterials from '../components/electronizationOfMaterials.vue'; // 电子材料
import IntelligentFormFilling from '../components/intelligentFormFilling.vue'; // 智能填表
import IntelligentAudit from '../components/intelligentAudit.vue'; // 智能审核
import BusinessAcceptance from '../components/businessAcceptance.vue'; // 业务受理

/** 暂存受理组件 */
import TempIntelligentQA from '../components/temp_intelligentQA.vue'; // 智能问答

import { BREAD_CRUMB_TEMP, BREAD_CRUMB_BOOTSTRAP } from './constant.js';
import { mapGetters } from "vuex";

export default {
  name: 'CommonDialog',
  inheritAttrs: false,
  props: {
    commmonVisible: {
      type: Boolean,
      default: false,
    },
    comboDirectoryOid: {
      type: String,
      default: ''
    },
    pCegisterType: {
      type: [String, Number],
      default: '0'
    },
    loginUser: {
      type: Object,
      default: () => ({})
    },
    comboDirectoryName: {
      type: String,
      default: ''
    },
    situationAnswerList: {
      type: Array,
      default: () => []
    },
    artificialService: Boolean,
    isNotFullScreen: Boolean,
    /** 是否是暂存受理组件 */
    isTempComponent: {
      type: Boolean,
      default: false
    },
    comboCaseOid: String,
    comboCaseId: String,

    serviceRoot: {
      type: Array,
      default: () => ([true, false])
    },

    comeFormArtific: Boolean,
    comboCaseNumber: String
  },
  components: {
    IntelligentQA,
    InfoKnowForOnce,
    ElectronizationOfMaterials,
    IntelligentFormFilling,
    IntelligentAudit,
    BusinessAcceptance,
    TempIntelligentQA,
  },
  data () {
    return {
      BREADCRUMB: [],
      sxServiceMaterialList: [],
      qlCaseTitleValList: [],
      caseOid: '',
      caseNumber: '',
      caseId: '',
      systemType: '',
      ruleForm: {},
      serviceForm: {},
      dueDate: '', // 补正时间
      caseMaterialOids: '',
      timer: null,
      situationCheckList: [],
      currentHotSituationName: '',
      isQlCaseChanged: true, // 判断情形是否修改
      situation: {}, // 情形相关信息
      rqbzMaterialNames: '',
      rqbzDueDate: '', // 容缺补正时间
      lastComponentId: null, // 点击上一步的id
    }
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
        this.$emit('update:commmonVisible', false);
      }
    },

    calcOnelineWidth () {
      // 1 => 50 2=> 50+162 3=>50+162+162 4=>50+162+162+162 5=>50+162+162+162+162 6=>50+162+162+162+162+102
      const len = this.BREADCRUMB.filter(item => item.isActive).length;
      let totalLen = this.BREADCRUMB.length;
      if (len === 1) {
        return '50px';
      } else if (len > 1 && len < totalLen) {
        return this.clzsFlag ? (50 + (len - 1) * 162) + 'px' : (50 + (len - 1) * 197) + 'px';
      } else {
        return this.clzsFlag ? '800px' : '770px'
      }
    },

    componentId () {
      const len = this.BREADCRUMB.filter(item => item.isActive).length;
      return this.BREADCRUMB[len - 1].componentId || '';
    },

    zIndex () {
      return document.querySelector('.el-dialog.common-dialog').parentElement.style.zIndex;
    }
  },
  watch: {
    commmonVisible: {
      handler (value, oldValue) {
        // 清除缓存
        localStorage.removeItem('idCardInfo');
        if (value === oldValue && value) return;
        if (value) {
          // if (screen.width === 1920) return;
          // 3秒之后关闭智能咨询
          clearTimeout(this.timer);
          this.timer = setTimeout(() => {
            this.$emit('close-artificial-service');
          }, 3000);
        }
      },
      immediate: true
    },
    // 智能问答如果显示
    artificialService: {
      handler (value) {
        if (value) {
          document.body.classList.add('intelligent-visible');
        } else {
          document.body.classList.remove('intelligent-visible');
        }
      },
      immediate: true
    },
    isNotFullScreen: {
      handler (value) {
        if (value) {
          document.body.classList.add('intelligeng-container--not-fullscreen');
        } else {
          document.body.classList.remove('intelligeng-container--not-fullscreen');
        }
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

    /** 如果是暂存受理页面组件 */
    if (this.comboCaseOid && this.isTempComponent) {
      this.caseOid = this.comboCaseOid;
    }

    if (this.comboCaseId && this.isTempComponent) {
      this.caseId = this.comboCaseId;
    }

    if (this.comboCaseNumber && this.isTempComponent) {
      this.caseNumber = this.comboCaseNumber;
    }
  },
  methods: {
    setBreadcrumb () {
      return this.isTempComponent ? JSON.parse(JSON.stringify(BREAD_CRUMB_TEMP)) : JSON.parse(JSON.stringify(BREAD_CRUMB_BOOTSTRAP));
    },

    nextStep (id) {
      const next = this.BREADCRUMB.find(item => item.id === id);
      next.isActive = true;
    },

    lastStep (id) {
      this.lastComponentId = id + 1;
      const index = this.BREADCRUMB.findIndex(item => item.id === id);
      this.BREADCRUMB.forEach((item, idx) => {
        if (idx > index) {
          item.isActive = false;
        }
      })
    },

    /** 业务代码 */
    setData (data) {
      this.caseOid = data.caseOid;
      this.caseNumber = data.caseNumber;
      this.caseId = data.caseId;
    },

    caseClose (data) {
      this.$emit('update:commmonVisible', false)
      this.$emit('case-close', data)
    }
  }
}
</script>

<style lang="scss">
@import '@/assets/styles/mixin/index.scss';
.el-dialog.common-dialog {
  background-color: rgba(227, 232, 235, 1);

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

@media screen and (min-width: 1920px) {
  .intelligent-visible {
    .el-dialog.common-dialog,
    .el-dialog.dialog-gpy,
    .el-dialog.hardware-scan,
    .el-dialog.apply-user-list,
    .el-dialog.common-dialog,
    .el-dialog.case-view,
    .el-dialog.preview-dialog {
      left: 360px !important;
      top: 146px !important;
    }

    &.intelligeng-container--not-fullscreen {
      .el-dialog.common-dialog,
      .el-dialog.dialog-gpy,
      .el-dialog.hardware-scan,
      .el-dialog.apply-user-list,
      .el-dialog.common-dialog,
      .el-dialog.case-view,
      .el-dialog.preview-dialog {
        left: 360px !important;
        top: 73.5px !important;
      }
    }
  }

  .intelligent-container.intelligent-container--visible {
    height: calc(100% - 290px) !important;
    top: 146px !important;

    &.intelligeng-container--not-fullscreen {
      height: calc(100% - 145px) !important;
      top: 73.5px !important;
    }
  }
}
</style>

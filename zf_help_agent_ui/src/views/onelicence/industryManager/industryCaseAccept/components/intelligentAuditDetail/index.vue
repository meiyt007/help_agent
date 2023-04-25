<template>
  <el-dialog
    v-dialog-drag
    :title="
      isManualVerification ? '查看材料样本及审查要点 ' : '材料智审对比查看'
    "
    width="1158px"
    height="800px"
    top="10vh"
    scrollbar
    :visible.sync="visible"
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    :custom-class="`common-dialog intelligent-audit-detail ${customDeatilClass} ${
      isManualVerification ? 'intelligent-audit-detail--is-manual' : ''
    }`"
    append-to-body
    center
    ref="dialog"
  >
    <div
      class="intelligent-audit-detail-container"
      v-if="intelligentDetailList.length > 0"
    >
      <el-radio-group v-model="intelligentRadio" @change="handleRadioChange">
        <el-radio-button label="1">全部材料({{ total }})</el-radio-button>
        <el-radio-button label="2" :disabled="!intelligentNeedFllow">
          需关注的({{ intelligentNeedFllow }})
        </el-radio-button>
      </el-radio-group>

      <!--
          localAuditImageUrl: 扫描地址
          materialSampleAddr: 样表地址
          reviewPointsList：审核要点
          manualAuditList：人工审核点
          notPassList：审核不通过
          passList：审核通过
         -->
      <IntelligentPane
        v-for="(item, index) in intelligentDetailList"
        :key="index"
        :index="index+1"
        :total="total"
        :localAuditImageUrl="item.localAuditImageUrl || ''"
        :materialSampleAddr="item.materialSampleAddr || ''"
        :reviewPointsList="item.reviewPointsList"
        :manualAuditList="item.manualAuditList"
        :notPassList="item.notPassList"
        :passList="item.passList"
        :refinedMaterialName="item.refinedMaterialName"
        :isManualVerification="isManualVerification"
        :resultStatus="item.resultStatus"
      />
    </div>
    <el-empty v-else style="height: 100%" />

    <span
      v-if="!isManualVerification && !onlyShow"
      slot="footer"
      class="dialog-footer"
    >
      <el-button type="primary" :disabled="hasConfirmed" @click="handleConfirm">
        确认材料无误
      </el-button>
      <el-button
        type="primary"
        :disabled="hasConfirmed"
        @click="handleChangeMaterial"
      >
        纠正材料
      </el-button>
    </span>

    <GpyUplaodDialog
      v-if="dialogVisible && !onlyShow"
      :caseOid="$attrs.caseOid"
      :serviceOid="$attrs.serviceOid"
      :serviceName="$attrs.serviceName"
      :dialogVisible.sync="dialogVisible"
      :qlCaseMaterial="$attrs.qlCaseMaterial"
      changed
      @refreshIntelligentList="$emit('refreshIntelligentList')"
    />
  </el-dialog>
</template>

<script>
import IntelligentPane from './intelligent-pane.vue';
import GpyUplaodDialog from '../../dialogs/gpy-upload-dialog.vue';

import { confirmComboCaseMaterial } from '@/api/onething/comboManager/comboAccept/materialCategory.js';
import {confirmIndustryCaseMaterial} from '@/api/onelicence/industryManager/industryCaseAccept/materialClassifiler.js'
export default {
  name: 'intelligentAuditDetail',
  props: {
    visibleDetail: {
      type: Boolean,
      default: false,
    },
    intelligentDetailList: {
      type: Array,
      default: () => []
    },
    isManualVerification: Boolean,
    customDeatilClass: {
      type: String,
      default: ''
    },
    onlyShow: Boolean,
    total: Number
  },
  components: { IntelligentPane, GpyUplaodDialog },
  data () {
    return {
      intelligentRadio: '1',
      dialogVisible: false,
    }
  },
  computed: {
    visible: {
      get () {
        return this.visibleDetail;
      },
      set () {
        this.$emit('update:visibleDetail', false);
      }
    },

    intelligentNeedFllow () {
      return this.isManualVerification ?
        this.intelligentDetailList
          .filter(item => item.reviewPointsList.length > 0).length
        : this.intelligentDetailList
          .filter(item => item.notPassList?.length > 0 || item.manualAuditList?.length > 0).length;
    },

    hasConfirmed ({ $attrs }) {
      return $attrs?.qlCaseMaterial?.confirmStatus === 'Y';
    },

  },
  mounted () {
    this.keyboardListenter = (e) => {
      const wrap = this.$refs?.dialog?.$refs?.scrollbar?.wrap;
      if (wrap && this.total > 1) {
        // 上
        if (e.keyCode === 38) {
          wrap.scrollTop -= 550;
        }

        // 下
        if (e.keyCode === 40) {
          wrap.scrollTop += 550;
        }
      }
    }

    document.addEventListener('keyup', this.keyboardListenter);
    this.intelligentRadio = this.intelligentNeedFllow > 0 ? '2' : '1';
  },
  beforeDestroy () {
    document.removeEventListener('keyup', this.keyboardListenter);
  },
  methods: {
    handleRadioChange (radio) {
      if (radio === '1') {
        this.$emit('setAll');
      }

      if (radio === '2') {
        this.$emit('setNoPass');
      }
    },

    handleConfirm () {
      confirmIndustryCaseMaterial({ caseMaterialOid: this.$attrs.qlCaseMaterial?.caseMaterialOid })
        .then(({ code, message, data }) => {
          if (code !== 200) return this.$message.warning(message || '请求失败');
          this.$message.success(`已确认材料${this.$attrs.qlCaseMaterial.materialName}无误`);
          this.$emit('refreshIntelligentList');
        })
        .catch(() => { })
    },

    handleChangeMaterial () {
      this.dialogVisible = true;
    }
  }
}
</script>

<style scoped lang="scss">
>>> .el-scrollbar {
  height: 100%;

  .el-scrollbar__wrap {
    scroll-behavior: smooth;
  }
}
>>> .el-dialog.intelligent-audit-detail {
  &.intelligent-audit-detail--is-manual {
    .el-dialog__body {
      height: calc(100% - 46px);
    }
  }
  .el-dialog__body {
    padding-bottom: 15px;
  }
  .el-dialog__footer {
    padding-top: 0px;
    padding-bottom: 10px;
  }
}
.intelligent-audit-detail {
  &-container {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    >>> .el-radio-group {
      margin: 0 0 25px 0;

      .el-radio-button.is-active {
        .el-radio-button__inner {
          color: #fff !important;
        }
      }

      .el-radio-button__inner {
        color: #203042;
      }
      .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background: #879099;
        border: 1px solid #707982;
        color: #3a4652;
        box-shadow: unset;
        &:hover {
          color: #3a4652;
        }
      }
    }
  }
}
</style>

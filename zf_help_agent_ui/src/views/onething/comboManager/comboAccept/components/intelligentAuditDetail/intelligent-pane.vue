<template>
  <div class="intelligent-pane">
    <div class="intelligent-pane--materials">
      <div
        :class="{
          'intelligent-pane--materials-item': true,
          'intelligent-pane--materials-manual-verification':
            isManualVerification,
        }"
      >
        <div class="intelligent-pane--materials-item__title">样本</div>
        <div class="intelligent-pane--materials-item__content">
          <ZfImageLoading
            alt="暂无图片信息"
            title="点击预览"
            style="width: 100%; height: 100%"
            fit="contain"
            :src="materialSampleAddr"
            :preview-src-list="[materialSampleAddr]"
          />
        </div>
      </div>

      <div
        v-if="!isManualVerification"
        class="
          intelligent-pane--materials-item intelligent-pane--materials-scan
        "
      >
        <div class="intelligent-pane--materials-item__title">
          扫描材料名称-{{ refinedMaterialName }}
        </div>
        <div class="intelligent-pane--materials-item__content">
          <ZfImageLoading
            alt="暂无图片信息"
            title="点击预览"
            style="width: 100%; height: 100%"
            fit="contain"
            :src="localAuditImageUrl"
            :preview-src-list="[localAuditImageUrl]"
          />
        </div>
      </div>
    </div>

    <IntelligentTabs
      class="intelligent-pane--tabs"
      :index="index"
      :total="total"
      :reviewPointsList="reviewPointsList"
      :manualAuditList="manualAuditList"
      :notPassList="notPassList"
      :passList="passList"
      :isManualVerification="isManualVerification"
      :resultStatus="resultStatus"
    />
  </div>
</template>

<script>
import ZfImageLoading from "@/components/ZfImageLoading";
import IntelligentTabs from './intelligent-tabs.vue';
export default {
  name: 'IntelligentPane',
  props: {
    index: String,
    total: Number,
    localAuditImageUrl: String,
    materialSampleAddr: String,
    refinedMaterialName: String,
    isManualVerification: Boolean,
    resultStatus: String,
    reviewPointsList: {
      type: Array,
      default: () => []
    },
    manualAuditList: {
      type: Array,
      default: () => []
    },
    notPassList: {
      type: Array,
      default: () => []
    },
    passList: {
      type: Array,
      default: () => []
    },
  },
  components: { ZfImageLoading, IntelligentTabs },
}
</script>

<style scoped lang="scss">
.intelligent-pane {
  width: 100%;
  height: 550px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-bottom: 25px;

  &--materials {
    flex: 2;
    height: 100%;
    background: #f9fdff;
    box-shadow: 0px 0px 13px 0px rgba(190, 203, 210, 0.95);
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 20px;

    &-item {
      width: 48%;
      height: 100%;
      background-color: #b4c9dd;
      border-radius: 10px;

      &__title {
        height: 34px;
        border-radius: 10px;
        line-height: 34px;
        font-size: 16px;
        font-family: Microsoft YaHei;
        font-weight: bold;
        color: #384a5b;
        padding-left: 20px;
      }

      &__content {
        width: 97%;
        margin: 0 auto;
        height: calc(100% - 40px);
        border-radius: 0px 0px 12px 12px;
      }
    }

    &-scan {
      background: #f7d599;
      .intelligent-pane--materials-item__title {
        color: #6b4605;
      }
    }

    &-manual-verification {
      width: 96%;
      margin: 0 auto;
    }
  }

  &--tabs {
    flex: 1;
    height: 100%;
    background: #ffffff;
    box-shadow: 0px 0px 13px 0px rgba(190, 203, 210, 0.95);
    border-radius: 5px;
    overflow: hidden;
  }
}
</style>

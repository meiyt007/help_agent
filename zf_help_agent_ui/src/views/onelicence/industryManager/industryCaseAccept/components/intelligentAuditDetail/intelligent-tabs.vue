<template>
  <div class="intelligent-tabs">
    <div class="intelligent-tabs-header">
      <div class="intelligent-tabs-header--title">材料要素审核</div>
      <div class="intelligent-tabs-header--pagination">
        第&nbsp;<span style="color: #ff4e00">{{ index }}</span
        >/{{ total }}&nbsp;页
      </div>
    </div>

    <el-tabs v-model="activeName" v-if="!isManualVerification">
      <el-tab-pane label="需关注的" name="1">
        <IntelligentTabsList
          :list="[...notPassList, ...manualAuditList]"
          :resultStatus="resultStatus"
          type="2"
        />
      </el-tab-pane>
      <el-tab-pane label="预审通过" name="2">
        <IntelligentTabsList
          :list="passList"
          type="1"
          :resultStatus="resultStatus"
        />
      </el-tab-pane>
      <!-- <el-tab-pane label="需人工审核" name="3">
        <IntelligentTabsList :list="manualAuditList" type="3" />
      </el-tab-pane> -->
    </el-tabs>

    <!-- 人工核验的 展示审核要点 -->
    <div class="intelligent-tabs--is-manual" v-else>
      <IntelligentTabsList
        :list="reviewPointsList"
        type="3"
        isManual
        :resultStatus="resultStatus"
      />
    </div>
  </div>
</template>

<script>
import IntelligentTabsList from './intelligent-tabs-list.vue';
export default {
  name: 'IntelligentTabs',
  props: {
    index: String,
    total: Number,
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
    isManualVerification: Boolean,
  },
  components: { IntelligentTabsList },
  data () {
    return {
      activeName: "1",
    }
  },
}
</script>

<style scoped lang="scss">
.intelligent-tabs {
  display: flex;
  flex-direction: column;

  &-header {
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 15px;
    padding-left: 20px;

    &--title {
      font-size: 14px;
      font-weight: bold;
      color: #2a344c;
      position: relative;
      padding-left: 10px;

      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 0;
        width: 3px;
        height: 16px;
        background: #ff6000;
        border-radius: 2px;
      }
    }

    &--pagination {
      height: 100%;
      width: 120px;
      background: #dce7f3;
      border-radius: 0px 5px 0px 0px;
      font-size: 20px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #607386;
      text-align: center;
      line-height: 48px;
    }
  }

  >>> .el-tabs {
    height: calc(100% - 63px);
    .el-tabs__nav.is-top {
      width: 100%;

      .el-tabs__item {
        width: 33%;
        text-align: center;
      }
    }

    .el-tabs__content {
      width: 100%;
      height: calc(100% - 57px);
      padding: 0 8px 15px 8px;

      .el-tab-pane {
        height: 100%;
      }
    }
  }

  &--is-manual {
    height: calc(100% - 63px);
    padding: 0 8px;
  }
}
</style>

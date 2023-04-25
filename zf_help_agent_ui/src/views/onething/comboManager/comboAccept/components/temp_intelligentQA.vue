<!-- 智能问答 暂存受理 -->
<template>
  <div class="onething-intelligent-qa" v-loading="loading">
    <el-scrollbar class="common-dialog-content">
      <div class="common-dialog--title">情景选项信息</div>
      <div class="situation-box-check">
        <div class="situation-box-check--title">选项名称：</div>
        <div class="situation-box-check--content">
          <template v-if="situationCheckList.length > 0">
            <div
              v-for="(data, index) in situationCheckList"
              :key="index"
              class="situation-box-check--content-item"
            >
              <span class="situation-box-check--content-item__title">
                {{ data.situationName }}
                <span style="font-weight: 500">
                  【{{
                    data.moreStatus !== 3
                      ? data.situationValueName
                      : data.txtContent
                  }}】
                </span>
              </span>
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
      </div>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="nextStep">下一步</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TempIntelligentQA',
  inheritAttrs: false,
  props: {
    situationCheckList: {
      type: Array,
      default: []
    },
  },
  methods: {
    nextStep () {
      return this.$emit('nextStep', 2);
    },
  }
}
</script>

<style lang="scss">
.onething-intelligent-qa {
  .situation-box {
    &-check {
      display: flex;
      min-height: 37px;

      &--title {
        width: 168px;
        background: #edf0f5;
        border: 1px solid #e0e6f0;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }

      &--content {
        flex: 1;
        border: 1px solid #e0e6f0;
        border-left: unset;
        display: flex;
        align-items: center;
        flex-direction: column;

        &-item {
          display: flex;
          flex-direction: column;
          //   padding: 25px 0 20px 35px;
          padding: 10px;
          width: 100%;
          border-bottom: 1px solid #e0e6f0;

          &:last-child {
            border-bottom: unset;
          }

          &__title {
            // margin-bottom: 20px;
            font-size: 14px;
            font-weight: bold;
            color: #121b2f;
          }
        }
      }
    }
  }
}
</style>

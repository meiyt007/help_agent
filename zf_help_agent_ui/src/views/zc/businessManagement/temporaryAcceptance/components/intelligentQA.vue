<!-- 智能问答 -->
<template>
  <div class="intelligent-qa-temporary">
    <el-scrollbar class="common-dialog-content">
      <div class="situation-box">
        <div class="common-dialog--title">情形选择</div>
        <div class="select-list-content">
          <div
            v-for="(item, index) in selectData"
            :key="item.index"
            :class="{ current: index == currentSituationNum }"
            class="select-list-item"
            @click="handleSituationChange(item, index)"
          >
            <img
              :src="
                index == currentSituationNum
                  ? require('@/assets/image/intelligent/checked.png')
                  : require('@/assets/image/intelligent/unchecked.png')
              "
              alt=""
              width="16"
              height="16"
            />
            <span style="margin-left: 10px">{{ item.title }}</span>
          </div>
        </div>
      </div>
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
                {{ data.titleName }}
                <span style="font-weight: 500">【{{ data.valueName }}】</span>
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
  name: 'IntelligentQA',
  inheritAttrs: false,
  props: {
    serviceOid: {
      type: String,
      default: ''
    },
    caseOid: {
      type: String,
      default: ''
    },
    cegisterType: String,
    situationCheckList: {
      type: Array,
      default: () => ([])
    }
  },
  data () {
    return {
      selectData: [
        {
          index: 0,
          title: "默认自定情形",
        }
      ],
      currentSituationNum: 0,
    }
  },
  mounted () {
    this.selectData[0].title = this.situationCheckList.find(item => item.situationName)?.situationName || '默认自定情形';
  },
  methods: {
    nextStep () {
      return this.$emit('nextStep', 2);
    },
  }
}
</script>

<style lang="scss">
.intelligent-qa-temporary {
  .situation-box {
    margin-bottom: 15px;

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

  .select-list-content {
    display: flex;
    flex-wrap: wrap;
    .select-list-item {
      margin-bottom: 15px;
      background: rgba(237, 240, 244, 0.55);
      border: 1px solid #c8cdd3;
      border-radius: 5px;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 12px;
      font-weight: 400;
      color: #2a344c;
      cursor: pointer;
      padding: 10px 20px;

      &.current {
        height: 37px;
        background: #2e7dff;
        color: #ffffff;
      }
    }
  }
}
</style>

<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-11-09 10:49:31
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-09 14:43:30
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\assembly\numericScrolling.vue
 * @Description: 数字滚动插件
-->
<template>
  <div class="num-area">
    <p
      class="numValue"
      :class="
        numType === 'add'
          ? 'newAddNum'
          : numType === 'reduce'
          ? 'newReduceNum'
          : ''
      "
    >
      {{ newNum }}
    </p>
    <p
      class="numValue"
      :class="
        numType === 'add'
          ? 'oldAddNum'
          : numType === 'reduce'
          ? 'oldReduceNum'
          : ''
      "
    >
      {{ oldNum }}
    </p>
  </div>
</template>
<script>
export default {
  name: "numericScrolling",
  props: {
    numValue: {
      type: Number,
      default: () => 0,
    },
  },
  data() {
    return {
      newNum: 0,
      oldNum: this.numValue,
      numType: null,
    };
  },
  watch: {
    numValue: {
      handler(val) {
        const that = this;
        if (val) {
          const num = that.newNum;
          that.newNum = val;
          that.oldNum = num;
          if (that.newNum > that.oldNum) {
            that.numType = "add";
          } else {
            that.numType = "reduce";
          }
          setTimeout(() => {
            that.numType = null;
          }, 1000);
        }
      },
    },
  },
};
</script>
<style lang="less" scoped>
.num-area {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
  p {
    padding: 0;
    margin: 0;
    font-size: 5rem;
    font-family: DIN;
    font-weight: bold;
    color: #323f53;
  }
  .newAddNum {
    position: absolute;
    top: 1rem;
    right: 0;
    animation: newAddNum 1s linear;
  }
  .oldAddNum {
    position: absolute;
    bottom: -6rem;
    right: 0;
    animation: newOldNum 1s linear;
  }
  @keyframes newAddNum {
    0% {
      top: -6rem;
      right: 0;
    }
    100% {
      top: 1rem;
      right: 0;
    }
  }
  @keyframes newOldNum {
    0% {
      bottom: 1rem;
      right: 0;
    }
    100% {
      ottom: -6rem;
      right: 0;
    }
  }
  .newReduceNum {
    position: absolute;
    bottom: 1rem;
    right: 0;
    animation: newReduceNum 1.5s linear;
  }
  .oldReduceNum {
    position: absolute;
    top: -6rem;
    right: 0;
    animation: oldReduceNum 1.5s linear;
  }
  @keyframes newReduceNum {
    0% {
      bottom: -6rem;
      right: 0;
    }
    100% {
      bottom: 1rem;
      right: 0;
    }
  }
  @keyframes oldReduceNum {
    0% {
      bottom: 1rem;
      right: 0;
    }
    100% {
      top: -6rem;
      right: 0;
    }
  }
}
</style>

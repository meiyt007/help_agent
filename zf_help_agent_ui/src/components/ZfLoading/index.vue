<template>
  <transition name="zf-loading-fade">
    <div
      v-show="visible"
      class="zf-loading-mask"
      :style="{ backgroundColor: background || '' }"
      :class="[customClass]"
    >
      <div class="zf-loading-spinner">
        <div class="zf-loading-spinner--content">
          <slot v-if="$slots.default" />
          <img v-if="!$slots.default && imgSrc" :src="imgSrc" alt="" />
        </div>
        <p v-if="text" class="zf-loading-text">{{ text }}</p>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'ZfLoading',
  props: {
    background: Object,

    customClass: String,

    imgSrc: {
      type: String,
      default: require('@/assets/gifs/waiting.gif')
    },

    text: {
      type: String,
      default: '正在加载中...'
    }
  },
  components: {},
  data () {
    return {
      visible: true,
    }
  },
}
</script>

<style scoped lang="scss">
.zf-loading-fade-enter,
.zf-loading-fade-leave-active {
  opacity: 0;
}

.zf-loading-mask {
  position: fixed;
  z-index: 10000;
  background-color: rgba(0, 0, 0, 0.8);
  margin: 0;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  transition: opacity 0.3s;

  .zf-loading-spinner {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);

    .zf-loading-spinner--content {
      background-color: rgba(20, 20, 20, 0.3);
      /* width: 540px; */
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 10px;
      border-radius: 3px;
    }

    .zf-loading-text {
      font-size: 18px;
      color: #409eff;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: rgba(20, 20, 20, 0.3);
      padding: 10px 0;
      border-radius: 3px;
      font-family: 'Microsoft YaHei';
    }
  }
}
</style>

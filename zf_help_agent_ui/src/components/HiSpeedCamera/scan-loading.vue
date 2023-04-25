<template>
  <transition name="el-loading-fade">
    <div
      class="scan-loading-mask"
      :style="{ backgroundColor: background || '' }"
      :class="[customClass, { 'is-fullscreen': fullscreen }]"
    >
      <slot />
    </div>
  </transition>
</template>

<script>
export default {
  name: 'ScanLoading',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      background: null,
      fullscreen: true,
      customClass: ''
    };
  },
};
</script>

<style lang="scss" scoped>
.scan-loading-mask {
  position: absolute;
  z-index: 10000;
  background-color: rgba(0, 0, 0, 0.7);
  margin: 0;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  transition: opacity 0.5s;

  &.is-fullscreen {
    position: fixed;
  }
}
</style>
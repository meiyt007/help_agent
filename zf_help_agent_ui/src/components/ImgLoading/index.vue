<template>
  <div class="img-loading">
    <img
      v-if="loading"
      width="66"
      height="66"
      src="@/assets/image/gpy/img-bg.png"
      alt
      :style="{
        objectFit: 'none',
        position: 'absolute',
        left: pic.left,
        top: pic.top,
        width: pic.width,
        height: pic.height,
        zIndex: pic.zIndex,
      }"
    />
    <img
      v-show="!loading"
      :src="pic.src"
      width="66"
      height="66"
      alt
      :draggable="false"
      :class="pic.class"
      :style="{
        position: 'absolute',
        left: pic.left,
        top: pic.top,
        width: pic.width,
        height: pic.height,
        zIndex: pic.zIndex,
      }"
      @load="handleLoad"
    />
  </div>
</template>

<script>
export default {
  name: 'ImgLoading',
  props: {
    pic: {
      type: Object,
      default: () => ({})
    }
  },
  data () {
    return {
      loading: true
    }
  },
  methods: {
    handleLoad () {
      this.loading = false;
    }
  }
}
</script>
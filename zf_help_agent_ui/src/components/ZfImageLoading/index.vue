<template>
  <div class="zf-image-loading">
    <el-image ref="elImage" v-bind="$attrs" @load="onLoadSuccess" @error="onError" :title="titleText" />
    <i v-if="loading" :style="loadingStyle" class="el-icon-loading zf-image-icon--loading" />
  </div>
</template>

<script>
import ElImage from '@/components/ElImage/main.vue';
export default {
  name: 'ZfImageLoading',
  inheritAttrs: false,
  props: {
    loadingStyle: {
      type: Object,
      default: () => ({})
    }
  },
  components: { ElImage },
  data () {
    return {
      loading: true,
    }
  },
  watch: {
    '$attrs.src': {
      handler (val, oldVal) {
        if (val !== oldVal) {
          this.loading = true;
        }
      },
      immediate: true
    }
  },
  computed: {
    titleText () {
      return this.loading ? '正在加载中' : (this.$attrs.title || '点击预览');
    }
  },
  methods: {
    onLoadSuccess () {
      this.loading = false;
    },

    handlePreview () {
      this.$refs.elImage.clickHandler();
    },

    onError () {
      this.loading = false;
    }
  }
}
</script>

<style scoped lang="scss">
.zf-image-loading {
  width: 100%;
  height: 100%;
  position: relative;
  background-color: #fff;

  >>> .el-image {
    width: 100%;
    height: 100%;
  }

  i {
    position: absolute;
    left: 45%;
    top: 45%;
    font-size: 3em;
    color: #5cb6ff;
  }
}
</style>

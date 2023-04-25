<template>
  <el-dialog
    v-dialog-drag
    v-if="visible"
    :visible.sync="visible"
    width="600px"
    title="拍照"
  >
    <TakePhoto v-bind="$attrs" @getPhotoInfo="getPhotoInfo" />
  </el-dialog>
</template>

<script>
import TakePhoto from './index.vue';
export default {
  name: 'TakePhotoDialog',
  props: {
    visibleDialog: Boolean
  },
  components: { TakePhoto },
  computed: {
    visible: {
      get () {
        return this.visibleDialog
      },
      set () {
        this.$emit('update:visibleDialog', false);
      }
    }
  },
  methods: {
    getPhotoInfo (data) {
      this.$emit('getPhotoInfo', data);
      this.$emit('update:visibleDialog', false);
    }
  }
}
</script>
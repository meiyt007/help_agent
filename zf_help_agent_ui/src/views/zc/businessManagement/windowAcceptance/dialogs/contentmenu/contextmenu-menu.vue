<template>
  <el-scrollbar class="contextmenu-menu">
    <ContextmenuNode v-for="(node, idx) in nodes" :key="idx" :node="node" />
  </el-scrollbar>
</template>

<script>
import ContextmenuNode from './contextmenu-node';
export default {
  name: 'ContextmenuMenu',
  inject: ['panel'],
  props: {
    nodes: {
      type: Array,
      default: () => []
    },
    index: {
      type: Number,
      default: 0
    }
  },
  components: { ContextmenuNode },
}
</script>

<style scoped lang="scss">
.contextmenu-menu {
  flex: 1;
  border-right: 1px solid #e4e7ed;
  width: 180px;
  box-sizing: border-box;
  color: #606266;
  height: 100%;

  &:last-child {
    border-right: none;
  }
}
</style>

<template>
  <div
    :class="[
      'contextmenu-node',
      node.isExtend ? 'contextmenu-node__active' : '',
      node.isDelete ? 'contextmenu-node__delete' : '',
      panel.direction === 'right' ? 'contextmenu-node__right' : '',
    ]"
    @mouseenter="handleNodeEnter"
    @click="handleNodeClick"
  >
    <el-tooltip v-if="node.showTooltip" effect="dark" :content="node.label" :placement="panel.direction">
      <span class="contextmenu-node__label">{{ node.label }}</span>
    </el-tooltip>
    <span v-else class="contextmenu-node__label">{{ node.label }}</span>
    <!-- <i
      class="el-icon-caret-left"
      v-if="node.hasChildren && panel.direction === 'right'"
    />-->
    <i class="el-icon-arrow-right" v-if="node.hasChildren" />
    <i class="el-icon-check" v-else-if="node.hasUpload" style="color: #1890ff" />
  </div>
</template>

<script>
export default {
  name: 'ContextmenuNode',
  inject: ['panel'],
  props: {
    node: {
      required: true
    },
  },
  data () {
    return {
      timer: null
    }
  },
  methods: {
    handleNodeEnter () {
      this.panel.handleMenusChild(this.node);
    },

    handleNodeClick () {
      if (this.node.isDelete) {
        return this.panel.$emit('remove-node', this.node);
      }
      if (!this.node.hasChildren) {
        this.panel.$emit('move-node', this.node);
      }
    }
  }
}
</script>

<style scoped lang="scss">
.contextmenu-node {
  position: relative;
  display: flex;
  align-items: center;
  padding: 0 10px;
  height: 34px;
  line-height: 34px;
  outline: 0;
  cursor: pointer;

  // &.contextmenu-node__right {
  //   flex-direction: row-reverse;

  //   .contextmenu-node__label {
  //     text-align: right;
  //   }
  // }

  &:hover,
  &.contextmenu-node__active {
    color: #409eff;
    font-weight: 700;
    background: #f5f7fa;
  }

  &.contextmenu-node__delete {
    color: #ff3203;
    border-top: 1px solid rgba(0, 0, 0, 0.05);
  }

  &__label {
    flex: 1;
    padding: 0 10px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>

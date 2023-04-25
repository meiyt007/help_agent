<template>
  <div class="contextmenu-panel" :style="{ ...contextMenuStyle, ...style }">
    <ContextmenuMenu ref="menu" v-for="(menu, index) in menus" :index="index" :key="index" :nodes="menu" />
  </div>
</template>

<script>
import ContextmenuMenu from './contextmenu-menu';
export default {
  name: 'ContextmenuPanel',
  provide () {
    return {
      panel: this
    }
  },
  props: {
    contextMenuStyle: {
      type: Object,
      default: {}
    },
    contextmenuList: {
      type: Array,
      default: () => []
    },
    direction: {
      type: String,
      default: 'left',
    }
  },
  components: { ContextmenuMenu },
  data () {
    return {
      menus: [],
      style: {}
    }
  },
  mounted () {
    document.body.appendChild(this.$el)
    this.handleMenusInit();
    this.handlePosition();
  },
  methods: {
    handleMenusInit () {
      // 第一级
      this.menus = [this.contextmenuList.filter(item => item.level === 1)];
    },

    handleMenusChild (node) {
      if (node.level === 1) {
        this.handleMenusInit();
        this.menus[0].forEach(item => item.isExtend = false);
        node.isExtend = true;
        if (node.hasChildren) {
          node.children.forEach(item => item.isExtend = false);
          this.menus.push(node.children);
        }
      }

      if (node.level === 2) {
        if (!node.hasChildren) {
          this.menus.splice(2, 1);
        } else {
          node.children.forEach(item => item.isExtend = false);
          this.$set(this.menus, 2, node.children);
        }
        this.menus[1].forEach(item => item.isExtend = false);
        node.isExtend = true;
      }

      this.handlePosition();
    },

    handlePosition () {
      if (this.direction === 'right') {
        this.style['flex-direction'] = 'row-reverse';
        if (this.menus.length === 1) {
          this.style.left = this.contextMenuStyle.left;
        }

        if (this.menus.length === 2) {
          let left = this.contextMenuStyle.left.split('px')[0];
          this.style.left = left - 180 + 'px';
        }

        if (this.menus.length === 3) {
          let left = this.contextMenuStyle.left.split('px')[0];
          this.style.left = left - 180 * 2 + 'px';
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
.contextmenu-panel {
  position: fixed;
  left: 0;
  top: 50%;
  height: 208px;
  z-index: 10000;
  background: #fff;
  display: flex;
  font-size: 14px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0px 0px 18px 0px rgba(45, 63, 116, 0.35);
}
</style>

<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 18:31:19
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-07-26 11:46:10
 * @FilePath: \hpNewHall\src\layout\components\AppMain.vue
 * @Description: 路由页面
-->
<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive>
        <router-view :key="key" :outLink="outLink" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
export default {
  name: "AppMain",
  computed: {
    key() {
      return this.$route.path;
    },
    outLink() {
      if (this.$route.meta.outLink) {
        return this.$route.meta.outLink;
      }
      return "";
    },
  },
};
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  // min-height: calc(100vh - 100px);
  height: 100%;
  width: 100%;
  position: relative;
  // overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}
.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    height: calc(100vh - 90px);
    padding: 15px;
  }

  .fixed-header + .app-main {
    padding-top: 90px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>

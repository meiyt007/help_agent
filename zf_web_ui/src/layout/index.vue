<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 18:31:19
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-08-08 14:10:28
 * @FilePath: \hpNewHall\src\layout\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="app-wrapper">
    <div class="headNavbr">
      <navbar />
    </div>
    <div class="main-container">
      <app-main />
    </div>
  </div>
</template>

<script>
import { AppMain, Navbar, Settings } from "./components";
import { mapState } from "vuex";
import { getLoginUrl } from "@/utils/auth";
export default {
  name: "Layout",
  components: {
    AppMain,
    Navbar,
  },
  mounted() {
  },
  methods: {
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          //location.reload()
          location.href = getLoginUrl();
        });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;

  .headNavbr {
    width: 100%;
    height: 52px;
  }

  .main-container {
    width: 100%;
    height: calc(100% - 52px);
    background-size: 100% 100%;
    // padding: 10px 15px 15px 15px;
  }

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.home-tab {
  display: none;
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: 100%;
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.mobile .fixed-header {
  width: 100%;
}
</style>

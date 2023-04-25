<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 10:05:28
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-13 14:28:36
 * @FilePath: \hpNewHall\src\App.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style lang="scss">
@import "styles/layout";
@import "styles/flex.css";
@import "styles/base.css";

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

html,
body {
  height: 100%;
  overflow: hidden;
  user-select: none;
  overflow-x: hidden;
  overflow-y: auto;
  background: #f5f5f5;
  width: 100% !important;
  font-size: 0.6rem;
}

.v-modal {
  z-index: 1 !important;
}

#app {
  margin: 0;
}

.green-dialog {
  .el-dialog__header {
    height: 40px;
    padding: 0 18px 0 22px;
    line-height: 40px;
    text-align: left;

    .el-dialog__title {
      color: #ffffff;
      font-size: 16px;
    }

    .el-dialog__headerbtn {
      top: 0px;

      .el-dialog__close {
        color: #ffffff;
      }
    }
  }
}

.el-dialog__headerbtn .el-dialog__close {
  color: #ffffff !important;
}

.page-pagination {
  &.is-background .el-pager li:not(.disabled).active {
    background-color: #429777 !important;
    border-radius: 4px;
  }

  .btn-prev,
  .btn-next {
    padding: 0 8px !important;
  }

  .el-pager {
    .number:hover {
      color: #429777 !important;
    }

    .active:hover {
      color: #fff !important;
    }
  }
}
.el-tooltip__popper {
  max-width: 20rem !important;
  color: #000 !important;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  background-color: #fff !important;
}
</style>

<template>
  <div class="welcome-content">
    <div class="welcome-content-guide">
      <span>Hi</span>
      <span style="font-size: 32px; font-weight: normal">，你好~</span>
    </div>

    <div class="welcome-content-version">
      <span>欢迎使用&nbsp;&nbsp;</span>
      <!-- <span :key="title" class="welcome-content-version--title">
        {{ title }}
      </span> -->
      <span class="welcome-content-version--title"> 帮代办 </span>
    </div>

    <!-- <div class="welcome-content-remark">
      <div>提供按照平行引导的方式进行各种事项、</div>
      <div>一件事受理与材料证照管理服务</div>
    </div> -->
  </div>
</template>

<script>
// import { mapGetters } from 'vuex';
export default {
  name: "welcome",
  // computed: {
  //   ...mapGetters([
  //     "appList",
  //     "app_oid",
  //   ]),
  //   /** 当前路由表 */
  //   currentRouterEnv ({ appList, app_oid }) {
  //     return appList.flat().find(item => item.oid === app_oid);
  //   },

  //   title ({ currentRouterEnv }) {
  //     return currentRouterEnv.name.includes('平台') ? currentRouterEnv.name : currentRouterEnv.name + '平台';
  //   },
  // },
}
</script>
<style lang="scss" scoped>
.welcome-content {
  background: #fff url(~@/assets/image/welcome.png) center no-repeat;
  background-size: 100% 100%;
  width: 100%;
  height: calc(100vh - 90px);
  position: relative;

  .welcome-content-guide {
    font-size: 40px;
    font-weight: bold;
    font-style: italic;
    color: #ffffff;
    position: absolute;
    left: 50px;
    top: 350px;
  }

  .welcome-content-version {
    font-weight: bold;
    font-size: 32px;
    color: #ffffff;
    position: absolute;
    left: 50px;
    top: 410px;

    .welcome-content-version--title {
      color: #4cfffb;
      // animation: show 1s;
    }
  }

  .welcome-content-remark {
    font-size: 24px;
    font-weight: 400;
    color: #c3cffd;
    line-height: 36px;
    position: absolute;
    left: 50px;
    top: 470px;
  }
}

@keyframes show {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

@media screen and (max-width: 1280px) {
  .welcome-content {
    background: #fff url(~@/assets/image/welcome-mini.png) center no-repeat;
  }
}
</style>

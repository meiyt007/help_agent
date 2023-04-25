<template>
  <div class="home">
    <component :is="componentId" />
  </div>
</template>

<script>
import OneCase from './oneCase';
import OneThing from './oneThing';
/** 欢迎页 */
import Welcome from '@/views/welcome';

import { mapGetters } from 'vuex';
export default {
  name: 'home',
  components: { OneThing, OneCase, Welcome },
  computed: {
    ...mapGetters([
      "appList",
      "app_oid",
    ]),
    /** 当前路由表 */
    currentRouterEnv ({ appList, app_oid }) {
      return appList.flat().find(item => item.oid === app_oid);
    },

    /** 是否是一件事 */
    isOnethingRouter ({ currentRouterEnv }) {
      return currentRouterEnv?.name?.includes?.('一件事');
    },

    /** 是否是单办事项 */
    isOneCaseRouter ({ currentRouterEnv }) {
      return currentRouterEnv?.name?.includes?.('单办事项');
    },

    componentId ({ isOnethingRouter, isOneCaseRouter }) {
      if (isOnethingRouter) {
        return 'OneThing';
      } else if (isOneCaseRouter) {
        return 'OneCase';
      } else {
        return 'Welcome';
      }
    }
  },
}
</script>

<style scoped lang="scss">
</style>

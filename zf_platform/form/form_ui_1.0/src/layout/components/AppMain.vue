<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" :outLink="outLink" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
import { isExistQueryVariable } from '../../utils/url';
import { getToken } from '@/utils/auth';
export default {
  name: 'AppMain',
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    },
    outLink(){
      let url = this.$route.meta.outLink,
        access_token = getToken();
      if (url) {
        let extraInfo =  '&base_url=' + window.location.origin + process.env.VUE_APP_BASE_API
        return isExistQueryVariable(url) ? (url + '&access_token=' +  access_token + extraInfo) : (url + '?access_token=' +  access_token + extraInfo);
      }
      return "";
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  // min-height: calc(100vh - 100px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header+.app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 100px);
  }

  .fixed-header+.app-main {
    padding-top: 84px;
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

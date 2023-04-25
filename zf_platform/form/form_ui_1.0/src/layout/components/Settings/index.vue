<template>
  <div class="drawer-container">
    <div>
      <h3 class="drawer-title">系统布局配置</h3>

      <div class="drawer-item">
        <span>主题颜色</span>
        <theme-picker style="float: right;height: 26px;margin: -3px 8px 0 0;" @change="themeChange" />
      </div>

      <div class="drawer-item">
        <span>开启 Tags-Views</span>
        <el-switch v-model="tagsView" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>固定 Header</span>
        <el-switch v-model="fixedHeader" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>显示 Logo</span>
        <el-switch v-model="sidebarLogo" class="drawer-switch" />
      </div>

    </div>
  </div>
</template>

<script>
  import ThemePicker from '@/components/ThemePicker'
  import store from "@/store";
  import { updateUserSkinClassname } from "@/api/sys/user";
  export default {
    components: { ThemePicker },
    data() {
      return {}
    },
    computed: {
      fixedHeader: {
        get() {
          return this.$store.state.settings.fixedHeader
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'fixedHeader',
            value: val
          })
        }
      },
      tagsView: {
        get() {
          return this.$store.state.settings.tagsView
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'tagsView',
            value: val
          })
        }
      },
      sidebarLogo: {
        get() {
          return this.$store.state.settings.sidebarLogo
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'sidebarLogo',
            value: val
          })
        }
      },
    },
    watch: {
      tagsView: (newVal) => {
        localStorage.setItem("tagsView", newVal)
      },
      fixedHeader: (newVal) => {
        localStorage.setItem("fixedHeader", newVal)
      },
      sidebarLogo: (newVal) => {
        localStorage.setItem("sidebarLogo", newVal)
      }
    },
    created() {
      if(null!=store.getters.skinClassname && ''!=store.getters.skinClassname){
        this.updateThemeChange(store.getters.skinClassname);
      }
    },
    methods: {
      themeChange(val) {
        let className = val.substring(1);
        updateUserSkinClassname(store.getters.userOid,className).then(res => {
          store.commit('SET_SKINCLASSNAME',val);
          this.$store.dispatch('settings/changeSetting', {
            key: 'theme',
            value: val
          });
        })
      },
      updateThemeChange(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'theme',
          value: val
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  .drawer-container {
    padding: 24px;
    font-size: 14px;
    line-height: 1.5;
    word-wrap: break-word;

    .drawer-title {
      margin-bottom: 12px;
      color: rgba(0, 0, 0, .85);
      font-size: 14px;
      line-height: 22px;
    }

    .drawer-item {
      color: rgba(0, 0, 0, .65);
      font-size: 14px;
      padding: 12px 0;
    }

    .drawer-switch {
      float: right
    }
  }
</style>

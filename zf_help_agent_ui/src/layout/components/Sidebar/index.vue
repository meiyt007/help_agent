<template>
  <div :class="{ 'has-logo': showLogo }">
    <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="settings.theme"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in permission_routes"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>

    <div class="siderBar-fixed">
      <el-dropdown class="gjx-dropdown" trigger="click">
        <span class="el-dropdown-link">
          <i class="colorIconfont icon-shezhi-caise"></i>
          <span class="set-name">设置</span>
        </span>
        <el-dropdown-menu slot="dropdown">
          <!-- <el-dropdown-item>
            <zfsoft-message id="read-message1" class="right-menu-item hover-effect"></zfsoft-message>
          </el-dropdown-item>-->
          <el-dropdown-item>
            <zfsoft-doc
              id="help-doc"
              class="right-menu-item hover-effect"
            ></zfsoft-doc>
          </el-dropdown-item>
          <el-dropdown-item>
            <size-select
              id="size-select"
              class="right-menu-item hover-effect"
            />
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";
import Hamburger from "@/components/Hamburger";
import SizeSelect from "@/components/SizeSelect";
import ZfsoftDoc from "@/components/zfsoft/doc";
export default {
  components: { SidebarItem, Logo, Hamburger, SizeSelect, ZfsoftDoc },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["permission_routes", "sidebar"]),
    activeMenu () {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo () {
      return this.$store.state.settings.sidebarLogo;
    },
    variables () {
      return variables;
    },
    isCollapse () {
      return !this.sidebar.opened;
    },
  },
  methods: {
    toggleSideBar () {
      this.$store.dispatch("app/toggleSideBar");
    },
  },
};
</script>

<style lang="scss">
.hamburger-container {
  position: absolute;
  top: 30px;
  right: -16px;
  width: 16px;
  height: 40px;
  background: #3c434c;
  border-radius: 0 7px 7px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  float: left;
  cursor: pointer;
  transition: transform 0.28s;
}
.hideSidebar .siderBar-fixed {
  transition: none;
}
.siderBar-fixed {
  position: fixed;
  bottom: 0;
  left: 0;
  height: 40px;
  line-height: 40px;
  width: 200px;
  transition: width 0.1s 0.2s;
  -webkit-animation: width 0.1s 0.2s;
  font-size: 14px;
  text-align: center;
  cursor: pointer;
  background-color: rgb(48, 56, 65);
  .el-dropdown {
    color: #99a8b4;
    i {
      vertical-align: middle;
      padding: 0 5px 0 0;
    }
  }
}
</style>

<style scoped lang="scss">
.el-dropdown-menu.el-popper {
  overflow: unset;
}
</style>

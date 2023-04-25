<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }">
    <transition name="sidebarLogoFade">
      <router-link
        v-if="collapse"
        key="collapse"
        class="sidebar-logo-link"
        to="/"
      >
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title" :title="title">{{ reversedTitle }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <!-- <img v-if="logo" :src="logo" class="sidebar-logo" /> -->
        <!-- <h1 class="sidebar-title" :title = "title">{{ reversedTitle }}<em>{{type}}</em></h1> -->
        <img
          class="sidebar-system-title"
          src="@/assets/logo/yiciban.png"
          alt=""
        />
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    // 计算属性的 getter
    reversedTitle: function () {
      if (this.title) {
        if (this.title.length > 6) {
          return this.title.slice(0, 6) + '...';
        }
        return this.title;
      }
      return ''
    }

  },
  data () {
    return {
      title: this.Common.simpleTitle,
      type: this.Common.version,
      logo: logoImg
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 80px;
  line-height: 80px;
  background: #303841;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 42px;
      height: 39px;
      vertical-align: middle;
    }

    .sidebar-system-title {
      vertical-align: middle;
      margin-left: 10px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0 0 0 5px;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 20px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
      em {
        font-style: normal;
        font-size: 12px;
        display: inline-block;
        vertical-align: top;
        font-weight: normal;
        margin-left: 3px;
      }
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>

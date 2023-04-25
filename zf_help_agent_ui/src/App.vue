<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  mounted () {
    if (this.$route.path.includes('/login')) return;

    /** 查询所有硬件前缀配置 */
    // this.$store.dispatch('getAllConfig')
  }
};
</script>

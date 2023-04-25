<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-21 10:37:41
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:10:04
 * @FilePath: \hpNewHall\src\views\quickAssistant\index.vue
 * @Description: 快捷帮办首页
-->
<template>
  <div @click="getRoute">我是快捷帮办</div>
</template>
<script>
export default {
  name: "QuickAssistant",
  methods: {
    getRoute() {
      this.$router.push({ path: "/" });
    },
  },
};
</script>
<style lang="scss" scoped></style>

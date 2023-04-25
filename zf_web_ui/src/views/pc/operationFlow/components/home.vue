<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-12 15:18:27
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:14:44
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\home.vue
 * @Description: 操作流程配置
-->
<template>
  <div class="body-content">
    <keep-alive>
      <component
        :is="processStep"
        @nextStep="nextStep"
        :baseUserInfo="baseUserInfo"
      ></component>
    </keep-alive>
  </div>
</template>
<script>
import baseInfo from "./components/baseInfo.vue";
import mattersHandling from "./components/mattersHandling.vue";
import situationGuidance from "./components/situationGuidance.vue";
import intelligentFormFilling from "./components/intelligentFormFilling.vue";
import materialCompletion from "./components/materialCompletion.vue";
import goodBadComment from "./components/goodBadComment.vue";
export default {};
</script>
<style lang="scss" scoped></style>

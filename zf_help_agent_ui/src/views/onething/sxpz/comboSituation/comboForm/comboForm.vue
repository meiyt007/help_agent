<template>
  <div class="app-container">
    <el-tabs v-model="activeName">
      <el-tab-pane label="表单管理" name="formManage">
        <form-manage :comboDirectoryOid = comboDirectoryOid v-if="activeName === 'formManage'"></form-manage>
      </el-tab-pane>
      <el-tab-pane label="字段配置" name="optionFieldRel">
        <situation-field-rel :comboDirectoryOid = comboDirectoryOid v-if="activeName === 'optionFieldRel'"></situation-field-rel>
      </el-tab-pane>
      <el-tab-pane label="字段值配置" name="optionFieldValRel">
        <situation-field-val-rel :comboDirectoryOid = comboDirectoryOid v-if="activeName === 'optionFieldValRel'"></situation-field-val-rel>
      </el-tab-pane>
<!--      <el-tab-pane label="表单会签" name="formSign">
          <form-sign-manage-index :comboDirectoryOid = comboDirectoryOid v-if="activeName === 'formSign'"></form-sign-manage-index>
      </el-tab-pane>-->
    </el-tabs>
  </div>
</template>
<script>
  import formManage from "@/views/onething/sxpz/comboSituation/comboForm/formManage.vue";
  import situationFieldRel from "@/views/onething/sxpz/comboSituation/situationField/situationFieldRelIndex.vue";
  import situationFieldValRel from "@/views/onething/sxpz/comboSituation/situationFieldVal/situationFieldValRelIndex.vue";
  //import formSignManageIndex from "@/views/oneThing/comboSituation/formSignManage/formSignManageIndex";

  export default {
    components: {
      formManage,
      situationFieldRel,
      situationFieldValRel,
     // formSignManageIndex
    },
    name: "comboForm",
    //定义获取父类传过来值的格式
    props: ["comboDirectoryOid"],
    data() {
      return {
        activeName: 'formManage',
      };
    },
  }
</script>

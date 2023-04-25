<template>
  <div> 
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table"> 
          <colgroup>
            <col width="20%" />
            <col/> 
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>目录名称：</b>
            </td>
            <td>
              <el-form-item prop="comboDirectoryName">
                <el-input
                  v-model.trim="form.comboDirectoryName"
                  placeholder="请输入复制后的目录名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          
          <tr>
            <td colspan="3"  style="text-align: center">
              <el-button @click="submitForm">保 存</el-button>
              <el-button @click="viewCancel">关 闭</el-button>
            </td> 
          </tr>
      </table> 
      </el-form>
  </div>
</template>
<script> 

import {save } from "@/api/onething/sxpz/comboDirectoryCopy";
export default {
  name: "comboDirectoryCopy", 
  props: ["comboDirectoryOid"],
  data () {
    return {     
      form: { 
        comboDirectoryName: "",  
        comboDirectoryOid: this.comboDirectoryOid
      },
      rules: {
        comboDirectoryName: [
          { required: true, message: "目录名称不能为空", trigger: "blur" }
        ] 
      }
    };
  },  
  methods: {  
    submitForm () { 
      this.$refs.form.validate(valid => {
        if (valid) {
          save(this.form)
            .then(response => {
              if (response.code == 200) {
                this.$message.success("保存成功");
                this.$emit("copyClose");
              } else {
                this.$message.error("保存失败！");
              }
            })
            .catch(function () {
              this.$emit("copyClose");
            });
        }
      });
    },
    viewCancel () {
     this.$emit("copyClose"); 
    }
  }
}
</script>
<style scoped>
.postClass .treeselect {
  width: 98% !important;
}
</style>

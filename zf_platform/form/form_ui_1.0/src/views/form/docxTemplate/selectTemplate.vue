<template>
  <div>
    <div  class="el-table__header-wrapper dialog-table">
      <!--<h3>事项标题</h3>-->
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>word模板：</b></td>
            <td colspan="3">
              <el-form-item id="app-select-temp" prop="docxTemplateOid">
                <el-select v-model="form.docxTemplateOid" clearable filterable placeholder="请选择word模板">
                  <el-option
                    v-for="te in templateOptions"
                    :key="te.docxTemplateOid"
                    :label="te.docxTemplateName"
                    :value="te.docxTemplateOid"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>测试关联设计主键-填报主键(多个用逗号隔开)：</b></td>
            <td colspan="3">
              <el-form-item prop="linkDesignAndReportOids">
                <el-input v-model.trim="form.linkDesignAndReportOids" placeholder="请输入关联填报主键" maxlength="300" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div class="export-footer">
        <el-button type="primary" @click="exportDocx(false)">导出word</el-button>
        <el-button type="primary" @click="exportDocx(true)">导出pdf</el-button>
      </div>
    </div >
  </div>

</template>

<script>
  import {querylist,exportFormDataToDocx,exportFormDataToDocxPost } from "@/api/form/docxTemplate";
  export default {
    name: "SelectTemplate",
    props: {
      templateParams: {
        type: Object,
        default: {
          isPdf:false,
          appendReportOids:''
        }
      },
    },
    data() {
      return {
        form: {},
        templateOptions:[],
        fullscreenLoading:null,
        // 表单校验
        rules: {
          docxTemplateOid: [
            { required: true, message: "请选择word模板", trigger: "change" },
          ],
        },
      }
    },
    created() {
      this.getTemplateList();
    },
    methods:{
      exportDocx(isPdf){
        this.$refs["form"].validate(valid => {
          if (valid) {

            /**
             * @description: 填报数据导出docx或者pdf
             * @param reportOid 主填报主键
             * @param appendReportOids 关联的填报主键，多个用逗号隔开
             * @param docxTemplateOid 模板主键
             * @param isPdf 是否导出pdf
             * @author: wuxx
             * @Date: 2021/12/3 13:42
             **/
            let query = {
              designAndReportOid : this.templateParams.designAndReportOid?this.templateParams.designAndReportOid:'',
              linkDesignAndReportOids : this.form.linkDesignAndReportOids?this.form.linkDesignAndReportOids:'',
              docxTemplateOid : this.form.docxTemplateOid,
              isPdf : isPdf,
            }
            exportFormDataToDocx(query);
          }else {
            return false;
          }
        });
      },
      getTemplateList(){
        let query = {
          objectOid:this.templateParams.objectOid,
          formCode:this.templateParams.formCode,
          designOid:this.templateParams.designOid
        }
        querylist(query).then(response => {
          this.templateOptions = response.data;
        }).catch(function () {

        });
      }

    }
  };
</script>
<style>
  .export-footer{
    text-align: center;
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .el-select el-select--medium{
    width: 100% !important;
  }
</style>

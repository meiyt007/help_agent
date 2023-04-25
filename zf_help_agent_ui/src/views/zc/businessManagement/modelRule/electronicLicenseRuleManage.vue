
<template>
  <div>
    <el-col :span="1.5">
      <el-button type="primary" icon="el-icon-plus" @click="addHtml" style="margin-bottom: 10px">增加</el-button>
    </el-col>

    <el-form ref="form" :model="form" :rules="rules" label-width="0px">
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="220" />
          <col width="220" />
          <col width="50" />
        </colgroup>
        <tr>
          <th>材料目录元素</th>
          <th>电子证照照面元素</th>
          <th>操作</th>
        </tr>
        <template v-for="(ruleForm, index) in form.faModelRuleValidationList">
          <template v-if="ruleForm.delFlag === 0">
            <tr>
              <td>
                <el-form-item
                  :prop="
                    'faModelRuleValidationList.' + index + '.elementLabelTree'
                  "
                  :rules="rules.elementLabelTree"
                >
                  <el-tooltip
                    effect="dark"
                    :content="
                      $refs[`cascader-${index}`] ? $refs[`cascader-${index}`][0].inputValue : ''
                    "
                    :disabled="!$refs[`cascader-${index}`] || !$refs[`cascader-${index}`][0].inputValue"
                    placement="top"
                  >
                    <el-cascader
                      :ref="`cascader-${index}`"
                      v-model="ruleForm.elementLabelTree"
                      :options="materialElementList"
                      placeholder="材料目录元素"
                      size="small"
                      style="width: 100%"
                      clearable
                      filterable
                    ></el-cascader>
                  </el-tooltip>
                </el-form-item>
              </td>

              <td>
                <!--                {{ruleForm.licenseTree}}-->
                <el-form-item :prop="'faModelRuleValidationList.' + index + '.licenseTree'" :rules="rules.licenseTree">
                  <el-tooltip
                    effect="dark"
                    :content="
                      $refs[`cascader1-${index}`] ? $refs[`cascader1-${index}`][0].inputValue : ''
                    "
                    :disabled="
                      !$refs[`cascader1-${index}`] || !$refs[`cascader1-${index}`][0].inputValue
                    "
                    placement="top"
                  >
                    <el-cascader
                      :ref="`cascader1-${index}`"
                      v-model="ruleForm.licenseTree"
                      :options="fieldTypeList"
                      placeholder="电子证照照面元素"
                      size="small"
                      style="width: 100%"
                      clearable
                      filterable
                    ></el-cascader>
                  </el-tooltip>
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-button style="border: 0px" icon="el-icon-delete" @click="delHtml(index)" class="handle-btn"></el-button>
                </el-form-item>
              </td>
            </tr>
          </template>
        </template>
      </table>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">保存</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </div>
</template>

<script>
import {
  querySxFieldTypeAndLabelAndSxFillFieldTree
} from "@/api/zc/sxService/serviceFormConfig/formFillField";
import {
  queryMaterialElementTreeSelect,
  queryMaterialAndCataAndElementTree,
  queryFaModelRuleValidationList,
  saveOrUpdateFaModelRuleValidation,
  queryElectronicLicenseTree
} from "@/api/zc/businessManagement/faModelRuleValidation.js";
export default {
  props: ['serviceOid'],
  name: "FromRuleManage",
  data () {
    return {
      fieldTypeList: [],
      materialElementList: [],
      // 查询参数
      queryParams: {
        serviceOid: this.serviceOid,
      },
      //查询电子表单规则参数
      faModelRuleValidationParams: {
        serviceOid: this.serviceOid,
        ruleType: 'DZZZ',
      },
      faModelRuleValidationList: [],
      form: {
        faModelRuleValidationList: [],
      },
      // 表单校验
      rules: {
        elementLabelTree: [{
          required: true,
          message: "材料目录元素不能为空",
          trigger: "blur"
        }],
        licenseTree: [{
          required: true,
          message: "电子证照照面元素不能为空",
          trigger: "blur"
        }],
      }
    };
  },
  created () {
    /* this.getSxFieldTypeAndLabelTree();*/
    this.queryMaterialElementTreeSelect();
    this.queryFaModelRuleValidationList();
    this.queryElectronicLicenseTree();
  },
  methods: {
    //添加子项模块
    addHtml: function () {
      this.form.faModelRuleValidationList.push({
        delFlag: 0,
        serviceOid: this.serviceOid,
        ruleType: 'DZZZ',
        validateType: 'CONTENT_THAN',
        licenseTree: "",


      })
    },
    //删除子项模块
    delHtml: function (index) {
      if (this.form.faModelRuleValidationList[index].faModelRuleValidationOid != '') {
        this.form.faModelRuleValidationList[index].delFlag = 1;
      } else {
        this.form.faModelRuleValidationList.splice(index, 1);
      }
    },

    /*   getSxFieldTypeAndLabelTree () {
         querySxFieldTypeAndLabelAndSxFillFieldTree(this.serviceOid).then(response => {
           this.fieldTypeList = response.data;
         });
       },*/
    queryElectronicLicenseTree () {
      queryElectronicLicenseTree(this.serviceOid).then(response => {
        this.fieldTypeList = response.data;
      });
    },



    queryMaterialElementTreeSelect () {
      queryMaterialAndCataAndElementTree(this.queryParams).then(response => {
        this.materialElementList = response.data;
      });
    },

    queryFaModelRuleValidationList () {
      queryFaModelRuleValidationList(this.faModelRuleValidationParams).then(response => {
        this.form.faModelRuleValidationList = response.data;
        /*  alert(JSON.stringify(response.data));*/
      });
    },
    submitForm () {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateFaModelRuleValidation(this.form.faModelRuleValidationList).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.loading = false;
              setTimeout(() => {
                this.$emit('father-click', 'N');//调用父页面关闭子页面的功能
              }, 10);
            }
          });
        }
      });


    },
    cancel () {
      this.$emit('father-click', 'N');//调用父页面关闭子页面的功能
    },
  },
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
</style>

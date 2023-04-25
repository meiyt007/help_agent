/**
* 一件事目录关联管理
* @author: chenjm
* @date: 2020-11-06
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--一件事目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="158px"
          @submit.native.prevent
        >
          <el-form-item label="一件事目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入一件事目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
              class="btn-reset"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="comboDirectoryList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="所属主题"
            align="center"
            prop="themeName"
            :show-overflow-tooltip="true"
          />
          <!--<el-table-column label="一件事目录编号" align="center" prop="comboDirectoryCode" />-->
          <el-table-column
            label="一件事目录名称"
            align="center"
            prop="comboDirectoryName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属区划"
            align="center"
            prop="districtName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="主办部门"
            align="center"
            prop="mainOrganName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                >目录关联</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- 添加或修改信息对话框 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
          width="910px"
        >
          <colgroup>
            <col width="10%" />
            <col width="40%" />
            <col width="10%" />
            <col width="40%" />
          </colgroup>
          <tr>
            <td colspan="1">
              <b>一件事名称：</b>
            </td>
            <td colspan="3">
              {{ form.comboDirectoryName }}
            </td>
          </tr>
        </table>
        <br />
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table--common zf-zc-table--td-center"
        >
          <colgroup>
            <col width="30" />
            <col width="120" />
            <col width="120" />
            <col width="150" />
            <col width="150" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>材料名称</th>
            <th>细化材料名称</th>
            <th>目录名称</th>
            <th>证照类型</th>
          </tr>
          <template v-for="(ruleForm, index) in form.refinedMaterials">
            <tr :key="index">
              <td>
                {{ index + 1 }}
              </td>
              <td>
                {{ ruleForm.materialName }}
              </td>
              <td>
                {{ ruleForm.refinedMaterialName }}
              </td>

              <td>
                <el-form-item prop="materialCatalogOid">
                  <treeselect
                    class="themeTreeselect"
                    :defaultExpandLevel="1"
                    noOptionsText="暂无数据"
                    noResultsText="暂无数据"
                    :show-count="true"
                    v-model="ruleForm.materialCatalogOid"
                    :disable-branch-nodes="true"
                    @input="updateGroupCompany"
                    :options="catalogOptions"
                    placeholder="请选择对应目录"
                    append-to-body
                  />
                  <!-- <el-select
                      placeholder="请选择对应目录"
                      v-model="ruleForm.materialCatalogOid"
                      :clearable="true"
                    >
                      <el-option
                        v-for="catalog in catalogList"
                        :key="catalog.id"
                        :label="catalog.catalogName"
                        :value="catalog.materialCatalogOid"
                      ></el-option>
                    </el-select> -->
                </el-form-item>
              </td>

              <td>
                <el-form-item prop="licenceOid">
                  <el-select
                    placeholder="请选择对应证照"
                    v-model="ruleForm.licenceOid"
                    :clearable="true"
                  >
                    <el-option
                      v-for="dict in licenceList"
                      :key="dict.dictOid"
                      :label="dict.name"
                      :value="dict.dictOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
          </template>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :visible.sync="openView"
      v-if="openView"
      title="目录信息"
      width="1100px"
      append-to-body
    >
      <combo-directory-view
        :comboDirectoryOid="comboDirectoryOidView"
        @combo-directory="comboDirectoryClose"
      ></combo-directory-view>
    </el-dialog>
  </div>
</template>

<script>
import { queryComboRefinedMaterialByComboDireOid, queryDirectoryMaterialList, pageList, page, getMealOne, saveOrUpdateComboDirectoryMaterial } from "@/api/onething/clzs/mealCatalogRelatedManage/mealCatalogRelatedManage.js";
import { updateRefinedMaterialList, queryCatalogTree } from "@/api/zc/clzs/catalogRelatedManage/catalogRelatedManage.js";
import { updateComboFaModelRuleValidation} from "@/api/onething/clzs/modelManagement/comboFaModelRuleValidation";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import { getDictList } from "@/api/sys/common";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import ComboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
export default {
  name: "MealCatalogRelatedManage",
  components: { ComboDirectoryView, Treeselect },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 一件事目录数据
      comboDirectoryList: [],
      // 事项类型
      serviceTypeOptions: [],
      //目录列表
      catalogList: [],
      //目录树
      catalogOptions: [],
      //事项类型
      licenceList: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查看显示弹出层目录主键
      comboDirectoryOidView: '',
      comboDirectoryOid:'',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: ""
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        comboDirectoryName: "",
        serviceMaterList: [],
        refinedMaterials: [],
        materialCatalogOid: [],
        catalogName: [],
      },
      // 表单校验
      rules: {},
    };
  },
  computed: {
    // 计算属性的 getter
  },
  created () {
    this.getList();
    this.getCatalogTree();
    this.getDictList();
  },
  watch: {
    //切换所属主题分类

  },
  methods: {
    updateGroupCompany (val, ruleForm) {
      // alert(val)
      console.log(JSON.stringify(ruleForm))

    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      //console.log(this.queryParams)
    },
    //获取目录树
    getCatalogTree () {
      queryCatalogTree().then(response => {
        this.catalogOptions = response.data;
        //console.log(JSON.stringify(this.catalogOptions))
      });
    },

    //获取证照类型
    getDictList () {
      let _that = this;
      // 查询楼层字典数据
      getDictList('LICENCE_TYPE').then(response => {
        _that.licenceList = response.data;
      });
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看一件事目录信息";
    },
    comboDirectoryClose () {
      this.openView = false;
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      this.comboDirectoryOid=row.comboDirectoryOid;
      _that.reset();
      _that.form.comboDirectoryName = row.comboDirectoryName;
      if (row.comboDirectoryOid) {
        queryComboRefinedMaterialByComboDireOid(row.comboDirectoryOid).then(response => {
          _that.openInit = true;
          this.form.refinedMaterials = response.data;
          //alert(this.form.refinedMaterials)
        });

      } else {
        _that.openInit = false;
      }
      _that.title = "一件事目录关联配置";

    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      let querys={comboDirectoryOid:_that.form.comboDirectoryOid};
      // this.form1.serviceOid=_that.form.serviceOid;
      console.log(_that.form.refinedMaterials)
      /* return;*/
      updateRefinedMaterialList(_that.form.refinedMaterials).then(response => {
        _that.msgSuccess("保存成功");
        _that.addDialogShow = false;

     /*   updateComboFaModelRuleValidation*/
        _that.addDialogShow = false;
        setTimeout(() => {
          _that.getList();
          _that.openInit = false;
        }, 10);
          //修改目录后调整对应的规则
         /* updateFaModelRuleValidation(querys).then(response => {
          });*/
      });
      /*   let _that = this;
         saveOrUpdateComboDirectoryMaterial(this.form.serviceMaterList).then(response => {
           if (response.code === 200) {
             //如果关联目录发生变化，删除已配置的验证规则 这个功能没有做
             _that.msgSuccess("保存成功");
             _that.addDialogShow = false;
             setTimeout(() => {
               _that.getList();
               _that.openInit = false;
             }, 10);
           }
         });*/
    }

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
// .dialog-table table {
//   width: 100%;
// }
// .dialog-table table {
//   border: 1px solid #dfe6ec;
//   border-collapse: collapse;
// }

.dialog-table table tr td {
  // border: 1px solid #dfe6ec;
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
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.primary-table {
  padding: 20px;
  box-sizing: border-box;
}
.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}
.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}
.primary-table .el-form-item:first-child {
  margin-left: 8px;
}
.primary-table .el-form-item {
  margin-bottom: 0;
  width: 130px;
  margin-right: 0;
  margin-left: 14px;
  font-size: 13px;
}
.primary-table .el-form-item__content {
  font-size: 13px;
}
.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}
.primary-table .inputTable .el-form-item {
  width: 100px;
}
.el-select {
  width: 90%;
}
.table1 table tr td {
  background-color: transparent !important;
}
</style>

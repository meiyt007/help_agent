/**
* 颗粒化材料修改
* @author: liangss
* @date: 2021-06-16
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="108px"
        >
          <el-form-item label="材料名称" prop="materialName">
            <el-input
              v-model.trim="queryParams.materialName"
              placeholder="请输入事项名称"
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

        <el-table
          v-loading="loading"
          :data="serviceMaterialList"
          border
          :fit="true"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="材料名称"
            width="360"
            align="center"
            prop="materialName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="份数"
            width="200"
            align="center"
            prop="paperNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="材料类型"
            align="center"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <template
                v-if="scope.row.materialType && scope.row.materialType == '0'"
              >
                原件
              </template>
              <template
                v-if="scope.row.materialType && scope.row.materialType == '1'"
              >
                复印件
              </template>
              <template
                v-if="scope.row.materialType && scope.row.materialType == '2'"
              >
                原件或者复印件
              </template>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <!--              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>-->
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                >百度模板配置</el-button
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
      width="1000px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          width="910px"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>材料名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="materialName">
                <el-col :span="24">
                  {{ form.materialName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>百度模板id：</b></td>
            <td colspan="3">
              <el-form-item prop="baiduTemplateIds">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.baiduTemplateIds"
                    name="baiduTemplateIds"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
        <br />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSxServiceOne } from "@/api/zc/clzs/catalogRelatedManage/catalogRelatedManage.js";
import { queryServiceMaterialWithPage, saveOrUpdateServiceMaterial } from "@/api/zc/businessManagement/serviceMaterialClassifier.js";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "CatalogRelatedManage",
  components: { Treeselect },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      serviceMaterialList: [],
      // 应用表格数据
      sxServiceList: [],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      proxy_show: false,
      expandedKeys: [],
      userExpandedKeys: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceOid: "",
        materialName: "",
        baiduTemplateIds: ""

      },
      defaultProps: {
        children: "children",
        label: "label"
      },

      // 表单参数
      form: {

      },

      // 表单校验
      rules: {},
    };
  },
  created () {
    /* alert(JSON.stringify(this.form))*/
    this.getList();

  },
  methods: {
    /** 查询列表 */
    getList () {
      this.loading = true;
      queryServiceMaterialWithPage(this.queryParams).then(response => {
        /* alert(JSON.stringify(response))*/
        this.serviceMaterialList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      console.log(this.queryParams)
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
      let _that = this;
      _that.reset();
      const oid = row.serviceOid;
      getSxServiceOne(oid).then(response => {
        _that.form = response.data.sxService;
        _that.openView = true;
        _that.title = "查看事项信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.openInit = true;
      _that.form = row;
      _that.title = "百度模板id配置";

    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      saveOrUpdateServiceMaterial(_that.form).then(response => {
        _that.msgSuccess("保存成功");
        _that.openInit = false;

        /* alert(response)*/
        /* if (response.code === 200) {
           _that.msgSuccess("保存成功");
           _that.addDialogShow = false;
           setTimeout(() => {
             _that.getList();
             _that.openInit = false;
           }, 10);
         }*/
      });
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
</style>

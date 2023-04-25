<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="类别名称" prop="fieldTypeName">
        <el-input
          v-model.trim="queryParams.fieldTypeName"
          placeholder="请输入类别名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="启禁用">
        <el-select
          v-model="queryParams.isAble"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in isAbleMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
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
          class="ml5"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['sx:fieldType:save']"
          >新增</el-button
        >
      </el-col>
    </el-row>
    <el-table
      ref="multipleTable"
      :data="fieldTypeList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="类别名称"
        align="center"
        prop="fieldTypeName"
        show-overflow-tooltip
      />
      <el-table-column
        label="排序号"
        align="center"
        prop="sort"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        show-overflow-tooltip
      />
      <el-table-column
        label="启用状态"
        align="center"
        width="100"
        prop="isAble"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
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
            v-hasPermi="['sx:fieldType:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sx:fieldType:update']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sx:fieldType:delete']"
            >删除</el-button
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

    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" v-model="form.id" />
        <el-input v-show="false" v-model="form.isAble" />
        <el-input v-show="false" v-model="form.isDelete" />
        <el-input v-show="false" v-model="form.createUserOid" />
        <el-input v-show="false" v-model="form.createDate" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>类别名称：</b></td>
            <td colspan="3">
              <el-form-item prop="fieldTypeName">
                <el-input
                  v-model.trim="form.fieldTypeName"
                  placeholder="请输入类别名称"
                  maxlength="20"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td colspan="3">
              <el-form-item prop="sort">
                <el-input-number v-model="form.sort" :min="1" :max="9999" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-input
                  v-model.trim="form.remark"
                  type="textarea"
                  placeholder="请输入备注"
                  maxlength="1000"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
    >
      <div>
        <el-form label-width="0px">
          <!--<h3>事项标题</h3>-->
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table"
          >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>类别名称：</b></td>
              <td colspan="3">
                {{ form.fieldTypeName }}
              </td>
            </tr>
            <tr>
              <td><b>排序号：</b></td>
              <td>
                {{ form.sort }}
              </td>
              <td><b>创建时间：</b></td>
              <td>
                {{ form.createDate }}
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                {{ form.remark }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  init,
  getOne,
  save,
  able,
  del
} from "@/api/zc/dataChange/sxFieldType";
export default {
  components: {},
  name: "SxFieldType",
  data () {
    return {
      // 遮罩层
      loading: true,
      isAbleMap: { "0": "禁用", "1": "启用" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      fieldTypeList: [],
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parentOid: ""
      },
      form: {
        sort: 1
      },
      rules: {
        fieldTypeName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ],
        sort: [{ required: true, message: "排序号不能为空", trigger: "blur" }]
      }
    };
  },
  watch: {},
  created () {
    this.getList();
  },
  methods: {
    /** 查询业务层级列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.fieldTypeList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.openIconSelectView = false;
      this.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    // 初始化新增
    handleInit (row) {
      const oid = row.id;
      if (oid === undefined) {
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        init(oid).then(response => {
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams = {};
      this.handleQuery();
    },
    // 表单重置
    reset () {
      this.form = {};
      this.resetForm("form");
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.openView = false;
      this.reset();
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.id;
      this.$confirm(
        "删除类别可能会导致对应标签数据出错，是否确认删除?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return del(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },
    /** 启禁用按钮操作 */
    handleAble (row) {
      const oid = row.id;
      let ableMessage = "";
      let msg = "";
      if (row.isAble === 1) {
        ableMessage = "你确定要启用吗？";
        msg = "启用";
      } else {
        ableMessage = "禁用类别可能会导致对应标签数据出错，是否确认禁用?";
        msg = "禁用";
      }
      this.$confirm(ableMessage, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return able(oid);
        })
        .then(() => {
          this.msgSuccess(msg + "成功");
        })
        .catch(function () {
          row.isAble = row.isAble === 0 ? 1 : 0;
        });
    }
  }
};
</script>
<style></style>

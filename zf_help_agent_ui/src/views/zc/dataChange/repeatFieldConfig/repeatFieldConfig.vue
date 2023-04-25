<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="显示字段" prop="masterField">
        <el-input
          v-model.trim="queryParams.masterField"
          placeholder="请输入显示字段名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="启禁用" prop="isAble">
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
      :data="repeatFieldConfigList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="显示字段"
        align="center"
        prop="masterField"
        show-overflow-tooltip
      />
      <el-table-column
        label="字段去重集合"
        align="center"
        prop="slaveField"
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
      width="70%"
      append-to-body
    >
      <div style="padding:10px 0;width:96%;margin:0 auto;">
        <font style="color:#de1e1e;font-size: 14px;vertical-align:middle;">
          填入多个显示字段时中间以英文逗号[,]作为分隔</font
        >
      </div>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="0px"
          class="demo-ruleForm"
        >
          <el-input v-show="false" v-model="form.id" />
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
              <td><i class="require">*</i><b>显示字段：</b></td>
              <td colspan="3">
                <el-form-item prop="masterField">
                  <el-input
                    v-model.trim="form.masterField"
                    placeholder="请输入显示字段"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>字段去重集合：</b></td>
              <td colspan="3">
                <el-form-item prop="slaveField">
                  <el-input
                    v-model.trim="form.slaveField"
                    maxlength="100"
                    show-word-limit
                    placeholder="请输入字段去重集合"
                  />
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看应用信息详细 -->
    <el-dialog title="查看" :visible.sync="openView" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
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
            <td><b>显示字段：</b></td>
            <td colspan="3">
              {{ form.masterField }}
            </td>
          </tr>
          <tr>
            <td><b>字段去重集合：</b></td>
            <td>
              {{ form.slaveField }}
            </td>
            <td><b>创建时间：</b></td>
            <td>
              {{ form.createDate }}
            </td>
          </tr>
          <tr>
            <td><b>启用状态：</b></td>
            <td colspan="3">
              {{ form.isAble }}
            </td>
          </tr>
        </table>
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
  save,
  able,
  del
} from "@/api/zc/dataChange/repeatFieldConfig";
export default {
  components: {},
  name: "RepeatFieldConfig",
  data() {
    return {
      // 遮罩层
      loading: true,
      isAbleMap: { "0": "禁用", "1": "启用" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      repeatFieldConfigList: [],
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,

      slaveList: [],
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryParams: {
        masterField: "",
        pageNum: 1,
        pageSize: 10,
        isAble: ""
      },
      form: {},
      rules: {
        masterField: [
          { required: true, message: "显示字段不能为空", trigger: "blur" }
        ],
        slaveField: [
          { required: true, message: "字段去重集合不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询业务层级列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.repeatFieldConfigList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.openIconSelectView = false;
      this.reset();
      const oid = row.configOid;
      init(oid).then(response => {
        this.form = response.data;
        this.form.isAble = this.form.isAble === 1 ? "是" : "否";
        this.form.slaveField = this.form.slaveFieldList.toString();
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    // 初始化新增
    handleInit(row) {
      const oid = row.configOid;
      if (oid === undefined) {
        this.openInit = true;
        this.form = {};
        this.title = "新增";
      } else {
        init(oid).then(response => {
          this.form = response.data;
          this.form.slaveField = this.form.slaveFieldList.toString();
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    /** 提交按钮 */
    submitForm: function() {
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
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.openView = false;
      this.reset();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.configOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return del(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    /** 启禁用按钮操作 */
    handleAble(row) {
      const oid = row.configOid;
      let ableMessage = row.isAble === 1 ? "启用" : "禁用";
      this.$confirm("你确定要" + ableMessage + "吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return able(oid);
        })
        .then(() => {
          this.msgSuccess(ableMessage + "成功");
        })
        .catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0;
        });
    },

    handleAddSlave() {
      this.$prompt("请输入从字段名称", "从字段", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        inputErrorMessage: "从字段不能为空"
      })
        .then(({ value }) => {
          //需要判断是否有该字段
          if (this.slaveList.indexOf(value) === -1) {
            this.slaveList.push(value);
            this.form.slaveField = this.slaveList.toString();
            this.$forceUpdate();
          } else {
            this.$message({
              type: "error",
              message: "从字段名称已存在"
            });
          }
        })
        .catch(() => {});
    }
  }
};
</script>

/** * @Author: xiayj * @date:2021-07-15 */
<template>
  <div>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="字段名称" prop="fieldName">
        <el-input
          v-model="queryParams.fieldName"
          placeholder="字段名称"
          clearable
          size="small"
          maxlength="50"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字段编码" prop="fieldCode">
        <el-input
          v-model="queryParams.fieldCode"
          placeholder="字段编码"
          clearable
          size="small"
          maxlength="50"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属标签" prop="typeLabelTree">
        <el-cascader
          v-model="typeLabelTree"
          :options="fieldTypeList"
          :props="{ expandTrigger: 'hover' }"
          :show-all-levels="false"
          placeholder="所属标签"
          size="small"
          style="width: 240px"
          clearable
          filterable
        >
        </el-cascader>
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
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="fieldList" border="">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <!--  <el-table-column
        label="所属事项"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      />-->
      <el-table-column
        label="字段名称"
        align="center"
        prop="fieldName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="字段编码"
        align="center"
        prop="fieldCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属标签"
        align="center"
        prop="labelName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属分类"
        align="center"
        prop="fieldTypeName"
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
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
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
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openView"
      v-if="openView"
      width="1100px"
      append-to-body
    >
      <!--<h3>事项标题</h3>-->
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>所属事项：</b></td>
          <td>{{ form.serviceName }}</td>
          <td><b>所属标签：</b></td>
          <td>
            {{ form.labelName }}
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>字段名称：</b></td>
          <td>
            {{ form.fieldName }}
          </td>
          <td><i class="require">*</i><b>字段编码：</b></td>
          <td>
            {{ form.fieldCode }}
          </td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="close">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <el-input v-show="false" v-model="form.fieldOid" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>所属事项：</b></td>
            <td>{{ serviceName }}</td>
            <td><i class="require">*</i><b>所属标签：</b></td>
            <td>
              <el-form-item prop="typeLabelTree">
                <el-cascader
                  v-model="form.typeLabelTree"
                  :options="fieldTypeList"
                  :props="{ expandTrigger: 'hover' }"
                  :show-all-levels="false"
                  placeholder="所属标签"
                  size="small"
                  style="width: 240px"
                  clearable
                  filterable
                >
                </el-cascader>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>字段名称：</b></td>
            <td>
              <el-form-item prop="fieldName">
                <el-input
                  v-model.trim="form.fieldName"
                  placeholder="请输入字段名称"
                  @blur="setCodeInput($event)"
                  maxlength="20"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>字段编码：</b></td>
            <td>
              <el-form-item prop="fieldCode">
                <el-input
                  v-model.trim="form.fieldCode"
                  placeholder="请输入字段编码"
                  maxlength="20"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import pyConst from "@/utils/pyConst";
import {
  page,
  init,
  save,
  del,
  getSxFieldTypeAndLabelTree
} from "@/api/zc/sxService/serviceFormConfig/formFillField";
import { validateLegalStrNoNumber } from "@/utils/validate";
export default {
  components: {},
  props: ["serviceOid", "serviceName"],
  name: "formFillFieldList",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      fieldList: [],
      fieldTypeList: [],
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      typeLabelTree: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contentOid: "",
        labelOid: "",
        fieldName: "",
        fieldCode: ""
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fieldName: [
          { required: true, message: "字段名称不能为空", trigger: "blur" }
        ],
        fieldCode: [
          { required: true, message: "字段编码不能为空", trigger: "blur" },
          {
            validator: validateLegalStrNoNumber,
            trigger: "blur"
          }
        ],
        typeLabelTree: [
          { required: true, message: "所属标签不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created () {
    this.getList();
    this.getSxFieldTypeAndLabelTree();
  },
  watch: {
    /*openField: {
      deep: true,*/
    handler: function (newVal) {
      if (newVal) {
        this.getList();
        this.getSxFieldTypeAndLabelTree();
      }
    }
    // }
  },
  methods: {

    /** 查询目录清单列表 */
    getList () {
      this.loading = true;
      this.queryParams.contentOid = this.serviceOid;
      if (this.typeLabelTree && this.typeLabelTree.length === 2) {
        this.queryParams.labelOid = this.typeLabelTree[1];
      } else {
        this.queryParams.labelOid = '';
      }
      page(this.queryParams).then(response => {
        this.fieldList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    getSxFieldTypeAndLabelTree () {
      getSxFieldTypeAndLabelTree(this.serviceOid).then(response => {
        this.fieldTypeList = response.data;
      });
    },
    close () {
      this.openView = false;
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.typeLabelTree = [];
      this.queryParams.labelOid = "";
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.openView = true;
      this.title = "字段信息查看";
      init(row.fieldOid).then(response => {
        this.form = response.data;
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      this.reset();
      const oid = row.fieldOid;
      if (oid === undefined) {
        this.form = {
          serviceOid: this.serviceOid,
          fieldCode: ""
        };
        this.openInit = true;
        this.title = "字段信息新增";
      } else {
        init(oid).then(response => {
          this.form = response.data;
          this.openInit = true;
          this.title = "字段信息修改";
        });
      }
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let arr = this.form;
          debugger
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
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.fieldOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return del(oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        })
        .catch(function () { });
    },
    //根据输入的名称自动填充code
    setCodeInput (event) {
      let ccode = pyConst.chineseToPinYin(event.target.value);
      this.form.fieldCode = ccode;
      // 如果长度大于100，只取前100位
      if (ccode.length > 20) {
        this.form.fieldCode = ccode.substring(0, 20);
      }
      //this.$refs["form"].validate();
    }
  }
};
</script>

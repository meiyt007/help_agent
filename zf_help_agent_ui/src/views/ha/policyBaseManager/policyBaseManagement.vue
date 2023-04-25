/** * @Author: zhaobf */
<!-- 请求条件 -->
<template>
  <div class="app-container" style="height:auto">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="所属机构">
        <treeselect
          v-model="queryParams.organOidSelect"
          :options="listOrganOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属机构"
        />
      </el-form-item>
      <el-form-item label="主题" prop="deleteStatus">
        <el-col :span="24">
          <el-select
            v-model.trim="queryParams.title"
            placeholder="请选择主题类型"
            size="small">
            <el-option label="资金扶持" value="资金扶持"></el-option>
            <el-option label="就业创业" value="就业创业"></el-option>
            <el-option label="行业发展" value="行业发展"></el-option>
            <el-option label="中小企业" value="中小企业"></el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入政策名称"
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
        >搜索
        </el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
        >重置
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:groupSplit:save']"
        >新增
        </el-button
        >
      </el-col>
    </el-row>
    <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="policyBaseList"
      stripe
      style="width: 100%" @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="主题"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="政策名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />

      <el-table-column
        label="更新时间"
        align="center"
        prop="updateDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:groupSplit:view']"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:groupSplit:update']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="removeById(scope.row.id)"
            v-hasPermi="['im:groupSplit:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!-- 信息详细 -->
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
        <el-input v-show="false"/>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>政策名称</b></td>
            <td>
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="请输入政策名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>主题</b></td>
            <td>
              <el-form-item prop="title">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.title"
                    placeholder="请选择主题"
                    size="small">
                    <el-option label="资金扶持" value="资金扶持"></el-option>
                    <el-option label="就业创业" value="就业创业"></el-option>
                    <el-option label="行业发展" value="行业发展"></el-option>
                    <el-option label="中小企业" value="中小企业"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>受理机构</b></td>
            <td>
              <el-form-item prop="organOidSelect">
                <treeselect
                  v-model="form.organOidSelect"
                  :options="listOrganOptions"
                  noOptionsText="暂无数据"
                  :defaultExpandLevel="1"
                  placeholder="请输入所属机构"
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td colspan="3">
              <el-form-item prop="sort">
                <el-table-column label="排序号" align="center">
                  <el-input-number style="width: 200px" placeholder="请输入排序号" v-model="form.sort" :min="1" :max="9999"/>
                </el-table-column>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require"></i><b>政策链接：</b></td>
            <td colspan="3">
              <el-form-item prop="policyLink">
                <el-input
                  v-model.trim="form.policyLink"
                  placeholder="请输入政策链接"
                  maxlength="2000"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>与该政策相关的涉企办事服务：</b></td>
            <td colspan="3">
              <el-form-item prop="serviceName">
                <el-input
                  v-model.trim="form.serviceName"
                  placeholder="请输入与该政策相关的涉企办事服务"
                  maxlength="2000"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>线上申报链接：</b></td>
            <td colspan="3">
              <el-form-item prop="declareLink">
                <el-input
                  v-model.trim="form.declareLink"
                  placeholder="请输入线上申报链接"
                  maxlength="2000"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>体检名称：</b></td>
            <td colspan="3">
              <el-form-item prop="experienceName">
                <el-input
                  v-model.trim="form.experienceName"
                  placeholder="请输入体检名称"
                  maxlength="2000"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>体检链接：</b></td>
            <td colspan="3">
              <el-form-item prop="experienceLink">
                <el-input
                  v-model.trim="form.experienceLink"
                  placeholder="请输入体检链接"
                  maxlength="2000"
                  show-word-limit
                  type="textarea"
                />
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
    <!-- 查看group信息 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      scrollbar
    >
      <div class="zf-zc-table--title">分组信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>政策名称：</b></td>
          <td>
            {{ policyBaseInfo.name }}
          </td>
          <td><b>主题：</b></td>
          <td>
            {{ policyBaseInfo.title }}
          </td>
        </tr>
        <tr>
          <td><b>受理机构：</b></td>
          <td>
            {{ policyBaseInfo.organName }}
          </td>
          <td><b>排序：</b></td>
          <td>
            {{ policyBaseInfo.sort }}
          </td>
        </tr>
        <tr>
          <td><b>政策链接：</b></td>
          <td colspan="3">
            {{ policyBaseInfo.policyLink }}
          </td>
        </tr>
        <tr>
          <td><b>与该政策相关的涉企办事服务：</b></td>
          <td colspan="3">
            {{ policyBaseInfo.serviceName }}
          </td>
        </tr>
        <tr>
          <td><b>线上申报链接：</b></td>
          <td colspan="3">
            {{ policyBaseInfo.declareLink }}
          </td>
        </tr>
        <tr>
          <td><b>体检名称：</b></td>
          <td colspan="3">
            {{ policyBaseInfo.experienceName }}
          </td>
        </tr>
        <tr>
          <td><b>体检链接：</b></td>
          <td colspan="3">
            {{ policyBaseInfo.experienceLink }}
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  deleteById,
  init,
  page,
  saveOrUpdate
} from "@/api/ha/policyBaseManagement/policyBaseManagement.js";
import {queryOrganTree} from "@/api/sys/organ";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: "policyBaseManagement",
  components: { Treeselect },
  props: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      policyBaseList: [],
      policyBaseInfo: {},
      groupIdOptions: [],
      //列表的机构数据
      listOrganOptions: [],
      multipleSelection: [],  //批量选择中选择的记录列表
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      // 查看显示弹出层
      openView: false,
      openInit: false,
      // 查看显示弹出层
      // openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        title: null,
        organId: null,
        organOidSelect: null
      },
      // 表单参数
      form: {
        name: '',
        title: null,
        organOidSelect: null,
        organId: null,
        sort: null,
        policyLink: null,
        serviceName: null,
        declareLink: null,
        experienceName: null,
        experienceLink: null
      },
      // 表单校验
      rules: {
        name: [
          {required: true, message: "必填项", trigger: "blur"}
        ],

      },

    };
  },
  watch:{
    'queryParams.organOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.queryParams.organId = dataId;
      }
    },
    'form.organOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.form.organId = dataId;
      }
    },
    'policyBaseInfo.organOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.policyBaseInfo.organId = dataId;
      }
    },
  },
  created() {
    this.getList();
    this.getListOrganTree('4028545d665734290166b02711c20073');//只有一个黄浦区
    // alert(this.groupId);
  },
  methods: {
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(this.form).then(response => {
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
    /** 查看按钮操作 */
    handleView(row) {
      //this.reset();
      let result = row;
      if (null != result.organId && '' != result.organId) {
        result.organOidSelect = 'ORGAN-' + result.organId;
        var obj= this.listOrganOptions.find(function (obj) {
          return obj.id === result.organOidSelect;//取出this.studentL里的id为1的那条数据所有的信息
        });
        result.organName = obj.label;
      }
      this.policyBaseInfo = result;
      // this.policyBaseInfo = row;
      this.openView = true;
      this.title = "查看应用信息";
    },
    //删除
    removeById(id) {
      this.$confirm('此操作将该用户数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        //调用接口方法
        deleteById(id)
          .then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getList()
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 初始化新增或修改 */
    handleInit(row) {
      const id = row.id;
      if (id === undefined) {
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        init(id).then(response => {
          let result = response.data;
          if (null != result.organId && '' != result.organId) {
            result.organOidSelect = 'ORGAN-' + result.organId;
          }
          this.form = result;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },

    /** 获取机构数*/
    getListOrganTree (districtOid) {
      this.organOptions = [];
      this.queryParams.organOid = null;
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.listOrganOptions = response.data;
          // console.log("-----"+JSON.stringify(this.listOrganOptions))
        });
      } else {
        this.listOrganOptions = [];
      }
    },
    /** 查询列表 */
    getList() {

      this.loading = true;
      page(this.queryParams).then(response => {
        this.policyBaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    //获取选择复选框的id值
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    }
  },
};
</script>
<style lang="scss" scoped>
//头像样式start
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

// end
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

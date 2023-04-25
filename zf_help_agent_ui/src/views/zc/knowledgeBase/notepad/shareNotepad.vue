/**
* @Author: liangss
* @Date: 2020-10-30 13:04:48
* @Last Modified by: liangss
* @Last Modified time: 2020-10-30 11:49:59
* @Description: 共享记事本列表
*/
<template>
  <div class="app-container">
    <!--咨询数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="知识类别" prop="zslbDictOid">
        <el-select
          v-model="queryParams.zslbDictOid"
          placeholder="请选择知识类别"
        >
          <el-option
            v-for="dict in typeDictOptions"
            :key="dict.oid"
            :label="dict.name"
            :value="dict.dictOid"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="知识标题" prop="knowledgeTitle">
        <el-input
          v-model.trim="queryParams.knowledgeTitle"
          placeholder="请输入知识标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分享人" prop="createUserName">
        <el-input
          v-model.trim="queryParams.createUserName"
          placeholder="请输入分享人"
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
      :data="advisoryRegistrationList"
      border
      height="calc(100% - 62px - 63px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="知识标题" align="center" prop="knowledgeTitle" />
      <el-table-column
        label="知识类别"
        width="100"
        align="center"
        prop="zslbDictName"
      />

      <el-table-column
        label="分享人"
        width="180"
        align="center"
        prop="createUserName"
      />
      <el-table-column
        label="分享时间"
        width="250"
        align="center"
        prop="shareDate"
      />

      <el-table-column
        label="操作"
        align="center"
        width="200"
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
          <!--<el-button  v-show="scope.row.shareFlag == 1 "  size="mini" type="text" icon="iconfont zfsoft-chexiao" @click="qxhandleShare(scope.row)" v-hasPermi="['sys:district:share']">取消共享</el-button>-->
          <el-button
            v-show="scope.row.createUserOid == loginUserOid"
            size="mini"
            type="text"
            icon="iconfont zfsoft-chexiao"
            @click="qxhandleShare(scope.row)"
            >取消共享</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改咨询登记对话框 -->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table"
      >
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="el-table__body"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>知识类别：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeCategory">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.knowledgeCategory"
                    placeholder="请输入知识类别"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>知识标题：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeTitle">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.knowledgeTitle"
                    placeholder="请输入知识标题"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td>
              <b>知识内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeContent">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    placeholder="请输入知识内容"
                    v-model.trim="form.knowledgeContent"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="note">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    placeholder="请输入备注"
                    v-model.trim="form.note"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 咨询登记信息详细 -->
    <el-dialog
    v-dialog-drag
      :title="title"
      :visible.sync="openView"
      width="900px"
      v-if="openView"
      append-to-body
    >

        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>知识类别：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  {{ form.zslbDictName }}
                </el-col>

            </td>
          </tr>
          <tr>
            <td>
              <b>知识标题：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  {{ form.knowledgeTitle }}
                </el-col>

            </td>
          </tr>
          <tr>
            <td>
              <b>分享状态：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  <template v-if="form.shareFlag === 0"> 未分享 </template>
                  <template v-else-if="form.shareFlag === 1"> 已分享 </template>
                </el-col>

            </td>
          </tr>
          <tr>
            <td>
              <b>分享人：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  {{ form.createUserName }}
                </el-col>

            </td>
          </tr>
          <tr>
            <td>
              <b>分享时间：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  {{ form.shareDate }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td>
              <b>知识内容：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  {{ form.knowledgeContent }}
                </el-col>

            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">

                <el-col :span="24">
                  {{ form.note }}
                </el-col>

            </td>
          </tr>
        </table>

      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, getOne, saveOrUpdate, del, share } from "@/api/zc/knowledgeBase/notepad.js";
import { queryDistrictSimpleTree } from "@/api/sys/district";

import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "advisoryRegistration",
  components: { Treeselect },
  data () {
    return {
      selectArr: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      advisoryRegistrationList: [],
      typeDictOptions: {},
      // 弹出层标题
      title: "",
      //当前登录人
      loginUserOid: null,
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shareFlag: 1,
        createUserName: null,
        shareUser: null,
        zslbDictOid: null,
        knowledgeTitle: null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        knowledgeTitle: '', // 知识标题
        zslbDictName: '', // 知识类别
        knowledgeContent: '', // 知识内容
        note: '', // 备注
        createUserName: '',//分享人
        shareFlag: 0,  // 分享标识
        createDate: '', // 登记时间
        sort: 0,  // 排序号
      },
      // 表单校验
      rules: {
        knowledgeCategory: [
          { required: true, message: "知识类别为空", trigger: "blur" }
        ],
        knowledgeTitle: [
          { required: true, message: "知识标题为空", trigger: "blur" }
        ],
        knowledgeContent: [
          { required: true, message: "知识内容为空", trigger: "blur" }
        ],
        note: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created () {
    this.getList();
    /** 数据字典中保存知识类别的数据项 */
    this.getDictList("ZSLB").then(response => {
      this.typeDictOptions = response.data;
    });
  },
  methods: {
    /** 查询我的记事本列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.advisoryRegistrationList = response.data.data;
        this.total = response.data.total;
        this.loginUserOid = response.message;
        this.loading = false;
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
      let _that = this;
      _that.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看记事本信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          /* alert(JSON.stringify(response.data));*/
        });
      } else {
        _that.openInit = true;
      }
      _that.title = row.id ? "修改记事本信息" : "新增记事本信息";
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.openInit = false;
              setTimeout(() => {
                _that.getList();
              }, 10);

            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return del(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },
    /** 共享按钮操作 */
    handleShare (row) {
      const oid = row.id;
      this.$confirm("是否确认共享?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return share(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("共享成功");
        })
        .catch(function () { });
    },
    /** 取消共享**/
    qxhandleShare (row) {
      const oid = row.id;
      this.$confirm("是否确认取消共享?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return share(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("取消共享成功");
        })
        .catch(function () { });
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

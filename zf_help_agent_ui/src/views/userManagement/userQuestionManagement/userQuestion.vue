/**
* @Description: 用户问题管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
          <el-form-item label="常见问题" prop="question">
            <el-input v-model.trim="queryParams.question" placeholder="请输入关键字" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="所属工作人员" prop="workUserId" label-width="111px">
            <el-select
              v-model="queryParams.workUserId"
              placeholder="请选择工作人员"
            >
              <el-option
                v-for="workUserInfo in getWorkPersonInfoList"
                :key="workUserInfo.id"
                :label="workUserInfo.name"
                :value="workUserInfo.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
            </el-button>
          </el-form-item>
        </el-form>


        <el-row :gutter="10" class="mb8">

          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
            >新增
            </el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="userQuestionList"  :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="常见问题" align="center" prop="question" :show-overflow-tooltip="true"/>
          <el-table-column label="答案" align="center" prop="answer" :show-overflow-tooltip="true"/>
          <el-table-column label="所属工作人员" align="center" prop="dbThaWorkUser.name" :show-overflow-tooltip="true"/>
          <el-table-column label="更新时间" align="center" prop="updateDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)">查看
              </el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)">修改
              </el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改问题信息 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit"
               width="900px" append-to-body>
      <div class="dia-content" style="width:100%; min-height:300px; max-height:700px; overflow-y:scroll; ">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%"/>
              <col width="30%"/>
              <col width="20%"/>
              <col width="30%"/>
            </colgroup>
            <tr>
              <td>
                <b><i class="require">*</i>所属工作人员：</b>
              </td>
              <td colspan="3">

                <el-form-item prop="workUserId">
                  <el-select v-model="form.workUserId">
                    <el-option v-for="workUserInfo in getWorkPersonInfoList" :key="workUserInfo.id"
                               :label="workUserInfo.name" :value="workUserInfo.id"></el-option>
                  </el-select>

                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b><i class="require">*</i>问题：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="question">
                  <el-col :span="24">
                    <el-input type="textarea" v-model.trim="form.question" placeholder="请输入问题" maxlength="200"
                              show-word-limit/>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b><i class="require">*</i>答案：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="answer">
                  <el-col :span="24">
                    <el-input type="textarea" v-model.trim="form.answer" placeholder="请输入答案" maxlength="200"
                              show-word-limit/>
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
                    <el-input type="textarea" placeholder="请输入备注" v-model.trim="form.note" maxlength="200"
                              show-word-limit></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>

            <!--          <tr>
                        <td><i class="require">*</i><b>排序号：</b></td>
                        <td colspan="3">
                          <el-form-item prop="sort">
                            <el-input-number placeholder="请输入排序号" v-model="form.sort" :min="1" :max="9999"/>
                          </el-form-item>
                        </td>
                      </tr>-->
          </table>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 问题管理信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="800px" v-if="openView" append-to-body>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td>
            <b>所属工作人员：</b>
          </td>
          <td colspan="3">
            {{form.workUserName}}
          </td>
        <tr/>
        <tr>
          <td>
            <b>问题：</b>
          </td>
          <td colspan="3">
            {{form.question}}
          </td>
        <tr/>
        <tr>
          <td>
            <b>答案：</b>
          </td>
          <td colspan="3">
            {{form.answer}}
          </td>
        </tr>


        <tr>
          <td>
            <b>备注：</b>
          </td>
          <td colspan="3">
            {{form.note}}
          </td>
        </tr>

        <!--          <tr>
                    <td><b>排序号：</b></td>
                    <td colspan="3">
                      <el-form-item prop="sort">
                        {{form.sort}}
                      </el-form-item>
                    </td>
                  </tr>-->


      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {page, getOne, saveOrUpdate, del, getWorkPersonInfo} from "@/api/zc/userManagement/userQuestion.js";
  import Treeselect from '@riophae/vue-treeselect';
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "userQuestion",
    components: {Treeselect},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        userQuestionList: [],
        userQuestionOid: "",
        //工作人员
        getWorkPersonInfoList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          question: null,
          answer: null
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {
          id: '', //逻辑主键
          workUserId: '',//工作人员主键
          workUserName:'',//工作人员姓名
          question: '', // 问题
          answer: '', // 类别编码
          note: '', // 备注
          //sort: 0,//排序
        },
        // 表单校验
        rules: {
          question: [
            {required: true, message: "问题不能为空", trigger: "blur"}
          ],
          answer: [
            {required: true, message: "答案不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getInitData();
    },
    methods: {
      /** 查询用户问题列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          console.log(response.data.data);
          this.userQuestionList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      /*获取工作人员信息*/
      getInitData() {
        let _that = this;
        getWorkPersonInfo().then(response => {
          _that.getWorkPersonInfoList = response.data.data;
        });
      },


      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
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
      /** 查看按钮操作 */
      handleView(row) {
        let _that = this;
        _that.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          _that.form = response.data;
          _that.openView = true;
          _that.title = "查看用户常见问题信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let _that = this;
        _that.reset();
        if (row.id) {
          getOne(row.id).then(response => {
            _that.openInit = true;
            _that.form = response.data;

          });
        } else {
          this.userQuestionOid = "";
          _that.openInit = true;
        }
        _that.title = row.id ? "修改用户常见问题信息" : "新增用户常见问题信息";
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
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          del(oid);
        }).then(() => {

          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
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
</style>

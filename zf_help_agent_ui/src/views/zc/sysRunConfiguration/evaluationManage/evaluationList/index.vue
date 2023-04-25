<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="120px" @submit.native.prevent>
      <el-form-item label="评价维度名称" prop="standardName">
        <el-input v-model.trim="queryParams.standardName" placeholder="请输入评价维度名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit"
          v-hasPermi="['onething:evaluation:init']">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-dialog-drag v-loading="loading" :data="evaluationList" border="" height="calc(100% - 160px)">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="评价维度名称" align="center" prop="standardName" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createDate" width="180">
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
            v-hasPermi="['onething:evaluation:view']">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
            v-hasPermi="['onething:evaluation:init']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
            v-hasPermi="['onething:evaluation:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <!-- 添加或修改评价 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if='openInit'
      width="900px" height="800px" scrollbar append-to-body>
      <el-form ref="form" :model="form.info" :rules="rules" label-width="0px" class="demo-ruleForm">
        <el-input v-show="false" v-model="form.info.id" />
        <el-input v-show="false" v-model="form.info.createBy" />
        <el-input v-show="false" v-model="form.info.createDate" />
        <el-input v-show="false" v-model="form.info.delFlag" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table zf-zc-table--td-center">
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>评价维度名称：</b></td>
            <td>
              <el-form-item prop="standardName">
                <el-input v-model.trim="form.info.standardName" placeholder="请输入评价维度名称" maxlength="10"
                  show-word-limit />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <el-form ref="optionForm" :model="form" :rules="options.rules" @keydown.native.enter.prevent ="searchHandle">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="addOption">新增
            </el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="form.options" border>
          <el-table-column label="评价选项内容" align="center" min-width="10%">
            <template slot-scope="scope">
              <el-form-item :prop="'options.' + scope.$index + '.optionContent'" :rules="options.rules.optionContent">
                <el-input placeholder="请输入评价选项内容" v-model.trim="scope.row.optionContent" maxlength="200"
                  show-word-limit @keydown.enter.native.prevent="searchHandle" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="90" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu"
                @click="deleteOption(scope.$index, form.options)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="openInit = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看应用信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="900px" append-to-body>
      <!--<h3>事项标题</h3>-->

      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="80%" />

        </colgroup>
        <tr>
          <td><b>评价维度名称：</b></td>
          <td>
            {{ form.info.standardName }}
          </td>
        </tr>
        <tr>
          <td><b>评价选项内容：</b></td>
          <td>
            <div v-for="(data, index) in form.options" :key='index'>
              {{ index + 1 }}、{{ data.optionContent }}
            </div>
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
  import {
    page,
    save,
    init,
    saveOptions,
    del,
    delEvaluationOption
  } from "@/api/zc/sysRunConfiguration/evaluationManage";
  import {
    validChinese
  } from '@/utils/validate';
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import iconfont from '@/views/common/iconfontSelect'
  export default {
    components: {
      Treeselect,
      iconfont
    },
    name: "App",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        evaluationList: [],
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
          standardName: ''
        },
        // 表单参数
        form: {
          info: {},
          options: []
        },
        // 表单校验
        rules: {
          standardName: [{
              required: true,
              message: "评价维度名称不能为空",
              trigger: "blur"
            },
            {
              validator: validChinese,
              trigger: 'blur'
            }
          ],
        },
        options: {
          rules: {
            optionContent: [{
              required: true,
              message: '评价选项内容不能为空',
              trigger: 'blur'
            }, ]
          }
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询应用列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.evaluationList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      // 表单重置
      reset() {
        this.form = {
          standardName: null
        };
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
      searchHandle() {},
      /** 添加评价选项*/
      addOption() {
        let option = {}
        if (!this.form.options) {
          this.form.options = []
        }
        this.form.options.push(option)
      },
      /** 删除评价选项*/
      deleteOption(index, rows) {
        //若是已有数据则后台执行删除
        if (rows[index].id) {
          delEvaluationOption(rows[index].id);
        }
        rows.splice(index, 1)
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.form.info = {};
        this.form.options = [];
        if (row.standardOid != undefined) {
          this.form.info = row
          init(row.standardOid).then(response => {
            this.form.options = response.data;
            this.openView = true;
            this.title = "查看评价内容信息";
          })
        }
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.form.info = {};
        this.form.options = [];
        if (row.standardOid != undefined) {
          this.form.info = row
          init(row.standardOid).then(response => {
            this.form.options = response.data
          })
        }
        this.openInit = true;
        this.title = "新增评价内容信息";
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$refs["optionForm"].validate(optionValid => {
              if (optionValid) {
                save(this.form.info).then(res => {
                  if (res.data != "") {
                    this.form.info.standardOid = res.data.standardOid
                    for (var val of this.form.options) {
                      val.standardOid = this.form.info.standardOid
                    }
                    saveOptions(this.form.options).then(res => {
                      if (res.data != "") {
                        this.msgSuccess('保存成功');
                        this.openInit = false
                        this.getList()
                      }
                    });
                  }
                });
              } else {
                return false;
              }
            });
          } else {
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.standardOid;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return del(oid);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {});
      },
    }
  };

</script>

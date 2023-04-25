<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['qdgl:question:addUpdate']">新增
        </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="placeList" border>
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column label="主题词" align="center" prop="keyWord" :show-overflow-tooltip="true" />
      <el-table-column label="问题标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="问题描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="问题解答" align="center" prop="answer" :show-overflow-tooltip="true" />
      <el-table-column label="是否启用" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enabledFlag" :active-value="1" :inactive-value="0"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="排序号" align="center" prop="sort" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="260" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
            v-hasPermi="['qdgl:question:view']">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleUpdate(scope.row)"
            v-hasPermi="['qdgl:question:addUpdate']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
            v-hasPermi="['qdgl:question:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 信息新增修改 -->
    <el-dialog v-dialog-drag :visible.sync="addUpdateFlag" v-if="addUpdateFlag" :title="addUpdateTitle" width="1100px"
      append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <tr>
            <td width="15%"><i class="require">*</i><b>主题词：</b></td>
            <td width="85%">
              <el-form-item prop="keyWord">
                <el-input v-model.trim="form.keyWord" placeholder="请输入主题词" maxlength="500" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>问题标题：</b></td>
            <td width="85%">
              <el-form-item prop="title">
                <el-input v-model.trim="form.title" placeholder="请输入问题标题" maxlength="500" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>是否启用：</b></td>
            <td width="85%">
              <el-form-item prop="enabledFlag">
                <el-radio v-model="form.enabledFlag" label="1">启用</el-radio>
                <el-radio v-model="form.enabledFlag" label="0">禁用</el-radio>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>排序号：</b></td>
            <td width="85%">
              <el-form-item prop="sort">
                <el-input-number v-model.trim="form.sort" placeholder="请输入排序号" :min="1" :max="9999" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>问题描述：</b></td>
            <td width="85%">
              <el-form-item prop="description">
                <el-input type="textarea" v-model.trim="form.description" placeholder="请输入问题描述" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>问题解答：</b></td>
            <td width="85%">
              <el-form-item prop="answer">
                <el-input type="textarea" v-model.trim="form.answer" placeholder="请输入问题解答" />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看用户配置对话框 -->
    <el-dialog v-dialog-drag title="查看常见问题信息" :visible.sync="questionView" v-if="questionView"
      :close-on-click-modal="false" width="1100px" append-to-body>

      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td width="15%"><b>主题词：</b></td>
          <td width="85%">
            {{ form.keyWord }}
          </td>
        </tr>
        <tr>
          <td width="15%"><b>问题标题：</b></td>
          <td width="85%">
            {{ form.title }}
          </td>
        </tr>
        <tr>
          <td width="15%"><b>是否启用：</b></td>
          <td width="85%">
            {{ reversedSex }}
          </td>
        </tr>
        <tr>
          <td width="15%"><b>排序号：</b></td>
          <td width="85%">
            {{ form.sort }}
          </td>
        </tr>
        <tr>
          <td width="15%"><b>问题描述：</b></td>
          <td width="85%">
            {{ form.description }}
          </td>
        </tr>
        <tr>
          <td width="15%"><b>问题解答：</b></td>
          <td width="85%">
            {{ form.answer }}
          </td>
        </tr>
      </table>

      <div slot="footer" style="text-align: center">
        <el-button @click="viewCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    page,
    able,
    save,
    detail,
    del
  } from '@/api/zc/qdgl/commonQuestion'

  export default {
    name: 'commonQuestion',
    components: {},
    props: ['createUser', 'serviceOid'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        placeList: [],
        addUpdateTitle: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          serviceOid: this.serviceOid
        },
        addUpdateFlag: false,
        questionView: false,
        form: {
          id: "",
          questionOid: "",
          keyWord: "",
          title: "",
          enabledFlag: "1",
          sort: "",
          description: "",
          answer: "",
          createUser: this.createUser,
          serviceOid: this.serviceOid,
        },
        rules: {
          keyWord: [{
            required: true,
            message: '主题词不能为空',
            trigger: 'blur'
          }],
          title: [{
            required: true,
            message: '问题标题不能为空',
            trigger: 'blur'
          }],
          description: [{
            required: true,
            message: '问题描述不能为空',
            trigger: 'blur'
          }],
          sort: [{
            required: true,
            message: '排序号不能为空',
            trigger: 'blur'
          }],
          answer: [{
            required: true,
            message: '问题解答不能为空',
            trigger: 'blur'
          }],
        },
      };
    },
    computed: {
      // 计算属性的 getter
      reversedSex: function () {
        return this.form.enabledFlag == "1" ? '启用' : '禁用'
      }

    },
    created() {
      this.getList()
    },
    methods: {
      /** 查询常见问题列表 */
      getList() {
        this.loading = true
        let that = this;
        page(this.queryParams).then(response => {
          this.placeList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      handleAdd() {
        this.addUpdateTitle = "新增常见问题";
        this.addUpdateFlag = true;
        this.form.id = ""
        this.form.questionOid = ""
        this.form.keyWord = ""
        this.form.title = ""
        this.form.sort = ""
        this.form.description = ""
        this.form.answer = ""
        this.form.enabledFlag = "1"
      },
      handleUpdate(row) {
        this.addUpdateTitle = "修改常见问题";
        this.addUpdateFlag = true;
        detail(row.id).then(res => {
          if (res.code == 200) {
            this.form.id = res.data.id;
            this.form.questionOid = res.data.questionOid;
            this.form.keyWord = res.data.keyWord;
            this.form.title = res.data.title;
            this.form.sort = res.data.sort;
            this.form.description = res.data.description;
            this.form.answer = res.data.answer;
            this.form.enabledFlag = res.data.enabledFlag + '';
          }
        })
      },
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form).then(response => {
              if (response.code == 200) {
                this.$message.success("保存成功");
                this.addUpdateFlag = false;
                this.getList();
              } else {
                this.$message.error("保存失败！");
              }
            }).catch(function () {
              this.addUpdateFlag = false;
            });
          }
        })
      },
      cancel() {
        this.addUpdateFlag = false;
        this.form.id = ""
        this.form.questionOid = ""
        this.form.keyWord = ""
        this.form.title = ""
        this.form.sort = ""
        this.form.description = ""
        this.form.answer = ""
        this.form.enabledFlag = "1";
      },
      handleView(row) {
        detail(row.id).then(res => {
          if (res.code == 200) {
            this.questionView = true;
            this.form.keyWord = res.data.keyWord;
            this.form.title = res.data.title;
            this.form.sort = res.data.sort;
            this.form.description = res.data.description;
            this.form.answer = res.data.answer;
            this.form.enabledFlag = res.data.enabledFlag;
          } else {
            this.questionView = false;
          }
        })
      },
      viewCancel() {
        this.questionView = false;
        this.form.id = ""
        this.form.questionOid = ""
        this.form.keyWord = ""
        this.form.title = ""
        this.form.sort = ""
        this.form.description = ""
        this.form.answer = ""
        this.form.enabledFlag = "1";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const name = row.title
        this.$confirm('是否确认删除"' + name + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          return del(row.id)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function () {})
      },
      // 状态修改
      handleStatusChange(row) {
        let text = row.enabledFlag == 1 ? '启用' : '禁用'
        this.$confirm('确认要"' + text + '""' + row.keyWord + '"吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          return able(row.questionOid)
        }).then(() => {
          this.msgSuccess(text + '成功')
        }).catch(function () {
          row.enabledFlag = row.enabledFlag == 0 ? 1 : 0
        })
      },
    },
  }

</script>

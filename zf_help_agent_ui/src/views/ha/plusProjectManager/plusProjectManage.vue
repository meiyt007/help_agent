/** * @Author: dingsn */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
    >
      <el-form-item label="加分项目名称" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入加分项目名称"
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
        >重置</el-button>
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
          v-hasPermi="['im:plusProject:save']"
        >新增</el-button
        >
      </el-col>
      <el-button type="danger" size="mini" v-hasPermi="['im:plusProject:delete']" @click="removeRows()">批量删除</el-button>
    </el-row>
    <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="plusProjectList"
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
        label="加分项目名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="备注"
        align="center"
        prop="memo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="更新人"
        align="center"
        prop="updateBy"
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
            v-hasPermi="['im:plusProject:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:plusProject:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="removePlusProjectById(scope.row.id)"
            v-hasPermi="['im:plusProject:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
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
        <el-input v-show="false" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>加分项目名称</b></td>
            <td>
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="请输入加分项目名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="memo">
                <el-input
                  v-model.trim="form.memo"
                  placeholder="请输入备注"
                  maxlength="500"
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
      <div class="zf-zc-table--title">加分项目信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
        </colgroup>
        <tr>
          <td><b>加分项目名称：</b></td>
          <td>
            {{ plusProject.name }}
          </td>
        </tr>
        <tr>
          <td><b>备注：</b></td>
          <td>
            {{ plusProject.memo }}
          </td>
        </tr>
        <tr>
          <td><b>创建人：</b></td>
          <td>
            {{ plusProject.createBy }}
          </td>
        </tr>
        <tr>
          <td><b>创建时间：</b></td>
          <td>
            {{ plusProject.createDate }}
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
  import { page,saveOrUpdatePlusProject,initPlusProject,deletePlusProjectById,batchRemovePlusProject} from "@/api/ha/plusProjectManager/plusProjectManage.js";
  export default {
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        plusProjectList: [],
        plusProject : {},
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
          pageNumber: 1,
          pageSize: 10,
          name: "",
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            { required: true, message: "必填项", trigger: "blur" }
          ],

        },

      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            saveOrUpdatePlusProject(this.form).then(response => {
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
      handleView (row) {
        // this.reset();
        console.log(row);
        this.plusProject = row;
        this.openView = true;
        this.title = "查看加分项目信息";

      },
      //删除
      removePlusProjectById(id){
        this.$confirm('此操作将删除该加分项目数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          //调用接口方法
          deletePlusProjectById(id)
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
      //批量删除按钮
      removeRows(){
        this.$confirm('此操作将批量删除加分项目信息, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          var idList =[];
          //遍历数组得到每个id值，设置到idlist中
          for(var i=0;i<this.multipleSelection.length;i++){
            var obj = this.multipleSelection[i];
            var id = obj.id;
            idList.push(id);
          }
          //调用接口方法
          batchRemovePlusProject(idList)
            .then(response => {
              //提示信息
              this.$message({
                type: 'success',
                message: '批量删除成功!'
              })
              //刷新页面
              this.getList()
            })
        })
      },

      /** 初始化新增或修改 */
      handleInit (row) {
        const id = row.id;
        if (id === undefined) {
          this.form = {};
          this.openInit = true;
          this.title = "新增";
        } else {
          initPlusProject(id).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改";
          });
        }
      },

      /** 查询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.plusProjectList = response.data.data;
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
        this.queryParams.pageNumber = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },

      //获取选择复选框的id值
      handleSelectionChange(selection){
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

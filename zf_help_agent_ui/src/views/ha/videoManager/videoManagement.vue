/** * @Author: wangns */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model.trim="queryParams.name" placeholder="姓名" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model.trim="queryParams.phone" placeholder="手机号" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所在部门" prop="organOid">
                  <el-col :span="24">
                    <el-select
                      v-model.trim="queryParams.organOid"
                      placeholder="请选择人员部门"
                      size="small"
                      clearable>
                      <el-option
                        v-for="item in organIdOptions"
                        :key="item.organOid"
                        :label="item.name"
                        :value="item.organOid">
                      </el-option>
                    </el-select>
                  </el-col>
        </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery"
          class="btn-reset">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit"
          v-hasPermi="['im:user:save']">新增</el-button>
      </el-col>
    </el-row>
    <!-- 表头 -->
    <el-table v-loading="loading" :data="districtList" stripe style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="工号" align="center" prop="workNumber" :show-overflow-tooltip="true" />
      <el-table-column label="房间号" align="center" prop="roomNumber" :show-overflow-tooltip="true" />
      <el-table-column label="部门" align="center" prop="organOid" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="340">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
            v-hasPermi="['im:user:update']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="removeVideoById(scope.row.id)"
            v-hasPermi="['im:user:delete']">删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            class="btn-reset"
            @click="resetPassword(scope.row)"
            v-hasPermi="['im:user:reset']"
          >重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;" layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList" />

    <!-- 信息详细 -->
    <!-- 添加或修改应用信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
        <el-input v-show="false" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>姓名：</b></td>
            <td>
              <el-form-item prop="name">
                <el-input v-model.trim="form.name" placeholder="请输入姓名" maxlength="100" show-word-limit />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>手机号码：</b></td>
            <td>
              <el-form-item prop="phone">
                <el-input v-model.trim="form.phone" placeholder="请输入手机号码：" maxlength="11" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>身份证号码：</b></td>
            <td>
              <el-form-item prop="idNo">
                <el-input v-model.trim="form.idNo" placeholder="请输入身份证号码" maxlength="18" show-word-limit />
              </el-form-item>
            </td>
            <td><i class="require"></i><b>电子邮箱：</b></td>
            <td>
              <el-form-item prop="email">
                <el-input v-model.trim="form.email" placeholder="请输入电子邮箱" maxlength="50" show-word-limit type="" />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="title === '新增'">
            <td><i class="require">*</i><b>账号：</b></td>
            <td>
              <el-form-item prop="account">
                <el-input v-model.trim="form.account" placeholder="请输入账号" maxlength="50" show-word-limit type="" />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>登录密码：</b></td>
            <td>
              <el-form-item prop="password">
                <el-input placeholder="请输入登录密码" v-model.trim="form.password" show-password></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>工号：</b></td>
            <td>
              <el-form-item prop="workNumber">
                <el-input v-model.trim="form.workNumber" placeholder="请输入工号：" maxlength="255" show-word-limit />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>房间号：</b></td>
            <td>
              <el-form-item prop="roomNumber">
                <el-input v-model.trim="form.roomNumber" placeholder="请输入房间号：" maxlength="255" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>部门：</b></td>
            <td>
              <!-- <template prop="organOid">
                <el-select v-model="form.organOid" clearable placeholder="请输入部门：" maxlength="255" show-word-limit>
                  <el-option
                    v-for="item in organIdOptions"
                    :key="item.organOid"
                    :label="item.name"
                    :value="item.organOid">
                  </el-option>
                </el-select>
              </template> -->
              <el-form-item prop="organOid">
                  <el-select v-model.trim="form.organOid" clearable placeholder="请选择">
                    <el-option
                      v-for="item in organIdOptions"
                      :key="item.organOid"
                      :label="item.name"
                      :value="item.organOid">
                    </el-option>
                  </el-select>
                </el-form-item>
            </td>
            <td><i class="require"></i><b>排序：</b></td>
            <td>
              <el-form-item prop="sort">
                <el-input v-model.trim="form.sort" placeholder="请输入房间号：" maxlength="255" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="memo">
                <el-input v-model.trim="form.memo" placeholder="请输入备注" maxlength="500" show-word-limit
                  type="textarea" />
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
  </div>
</template>
<script>
import { page, saveVideo, deleteByOid,getById,queryOrganSelectOptions,reserpassword  } from "@/api/ha/videoManagement/videoManagement.js";
import Treeselect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],
      //部门集合
      organIdOptions: [],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phone: "", //手机号
        name: "", //姓名
        organOid: "", //部门
      },
      // 查询参数
      queryOrganParams: {
        districtOid: '4028545d665734290166b02711c20073',
        handleType: '',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        account: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        workNumber: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        roomNumber: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        organOid: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        password: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
      },

    };
  },

  created() {
    this.getList();
    this.getListOrgan();
  },
  methods: {
      //重置密码
      resetPassword(row){
       this.$confirm('此操作将用户密码重置,重置密码为初始密码:123456 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          //调用接口方法
           reserpassword(row.id)
           .then(response => {
               //提示信息
                this.$message({
                type: 'success',
                message: '重置成功'
              })
               //刷新页面
               this.getList()
           })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消重置'
          });
        });
    },
    /** 获取部门集合 */
    getListOrgan(){
      debugger
      queryOrganSelectOptions(this.queryOrganParams).then(response => {
        this.organIdOptions = response.data;
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveVideo(this.form).then(response => {
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

    //删除
    removeVideoById(id) {
      this.$confirm('此操作将该视频咨询用户数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        //调用接口方法
        deleteByOid(id)
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
        getById(id).then(response => {
          let result = response.data;
          this.form = result;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },

    /** 查询列表 */
    getList() {
      this.loading = true;
      const asa = this.queryParams.pageNum
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
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
  height: 100px;
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

.zf-zc-table tr td {
  padding: 2px 10px !important;
}
</style>

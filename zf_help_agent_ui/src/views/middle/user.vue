<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model.trim="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="企业名称" prop="companyName">
        <el-input
          v-model.trim="queryParams.companyName"
          placeholder="请输入企业名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="用户状态">
        <el-select
          v-model="queryParams.userStatus"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in statusMap"
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
          v-hasPermi="['im:user:save']"
        >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="middleUserList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="用户名称"
        align="center"
        prop="userName"
        show-overflow-tooltip
      />
      <el-table-column
        label="用户类型"
        align="center"
        prop="middleUser.userName"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.userType==0">服务商</p>
          <p v-if="scope.row.userType==1">开发商</p>
        </template>
      </el-table-column>
      <el-table-column
        label="企业名称"
        align="center"
        prop="companyName"
        show-overflow-tooltip
      />
      <el-table-column
        label="用户状态"
        align="center"
        prop="status"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.userStatus==0">新建</p>
          <p v-if="scope.row.userStatus==1">待审核</p>
          <p v-if="scope.row.userStatus==2">审核成功</p>
          <p v-if="scope.row.userStatus==3">审核失败</p>
        </template>
      </el-table-column>
      <el-table-column
        label="启禁用状态"
        align="center"
        width="100"
        prop="ableStatus"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.ableStatus"
            :active-value="'Y'"
            :inactive-value="'N'"
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
            v-hasPermi="['im:user:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:user:update']"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['im:user:delete']"
          >删除</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhe"
            v-if="isShowAuth(scope.row)"
            @click="handleCheck(scope.row)"
            v-hasPermi="['im:user:audit']"
          >审核</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
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
        <el-input v-show="false" v-model="form.userStatus='1'" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>用户名称：</b></td>
            <td>
              <el-form-item prop="userName">
                <el-input
                  v-model.trim="form.userName"
                  placeholder="请输入用户名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>密码：</b></td>
            <td>
              <el-form-item prop="userPassword">
                <el-input
                  v-model.trim="form.userPassword"
                  placeholder="请输入密码"
                  maxlength="200"
                  show-word-limit
                  type="password"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>企业名称：</b></td>
            <td colspan="3">
              <el-form-item prop="companyName">
                <el-input
                  v-model.trim="form.companyName"
                  placeholder="请输入企业名称"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>电子邮箱：</b></td>
            <td>
              <el-form-item prop="email">
                <el-input
                  v-model.trim="form.email"
                  placeholder="请输入电子邮箱"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>手机号码：</b></td>
            <td>
              <el-form-item prop="mobile" v-if="form.oid==null||form.oid==''">
                <el-input
                  v-model.trim="form.mobile"
                  placeholder="请输入手机号码"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item prop="mobile" v-else>
                <el-input
                  v-model.trim="form.mobile"
                  placeholder="请输入手机号码"
                  maxlength="20"
                  :disabled="true"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>钉钉token信息：</b></td>
            <td colspan="3">
              <el-form-item prop="dingDingToken">
                <el-input
                  v-model.trim="form.dingDingToken"
                  placeholder="请输入钉钉token信息"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>职位：</b></td>
            <td colspan="3">
              <el-form-item prop="position">
                <el-input
                  v-model.trim="form.position"
                  placeholder="请输入职位"
                  maxlength="100"
                  show-word-limit
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

    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">服务商用户基本信息</div>
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
          <td><b>用户名称：</b></td>
          <td>
            {{ middleUser.userName }}
          </td>
          <td><b>启禁用状态：</b></td>
          <td>
            {{ middleUser.ableStatus == 'N'?'禁用':'启用' }}
          </td>
        </tr>
        <tr>
          <td><b>用户类型：</b></td>
          <td>
            {{ middleUser.userType == '0'?'服务商':'开发商' }}
          </td>
          <td><b>用户状态：</b></td>
          <td>
            {{ middleUser.userStatus | statusHandle }}
          </td>
        </tr>
        <tr>
          <td><b>企业名称：</b></td>
          <td colspan="3">
            {{ middleUser.companyName }}
          </td>
        </tr>
        <tr>
          <td><b>电子邮箱：</b></td>
          <td>
            {{ middleUser.email }}
          </td>
          <td><b>手机号码：</b></td>
          <td>
            {{ middleUser.mobile }}
          </td>
        </tr>
        <tr>
          <td><b>钉钉token信息：</b></td>
          <td colspan="3">
            {{ middleUser.dingDingToken }}
          </td>
        </tr>
        <tr>
          <td><b>职位：</b></td>
          <td colspan="3">
            {{ middleUser.position }}
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">审核记录列表</div>
      <el-table v-loading="loading" :data="userAuthRecList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="审核员名称"
          align="center"
          width="200"
          prop="userName"
        />
        <el-table-column
          label="审核状态"
          align="center"
          width="100"
          prop="userStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.userStatus==0">未提交</p>
            <p v-if="scope.row.userStatus==1">待审核</p>
            <p v-if="scope.row.userStatus==2">审核通过</p>
            <p v-if="scope.row.userStatus==3">审核不通过</p>
            <p v-if="scope.row.userStatus==4">停用</p>
            <p v-if="scope.row.userStatus==5">已发布</p>
            <p v-if="scope.row.userStatus==6">待升级</p>
            <p v-if="scope.row.userStatus==7">升级待审核</p>
            <p v-if="scope.row.userStatus==8">升级审核未通过</p>
            <p v-if="scope.row.userStatus==9">升级审核通过</p>
            <p v-if="scope.row.userStatus==10">停用待审核</p>
            <p v-if="scope.row.userStatus==11">停用审核不通过</p>
          </template>
        </el-table-column>
        <el-table-column
          label="审核日期"
          width="200"
          align="center"
          prop="createDate"
        />
        <el-table-column
          label="原因"
          width="300"
          align="center"
          prop="memo"
        />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 审核应用信息详细 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openCheck"
      width="900px"
      append-to-body
    >
      <el-form
        ref="formCheck"
        :model="formCheck"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>用户名称：</b></td>
            <td>
              {{ middleUser.userName }}
            </td>
            <td><b>启禁用状态：</b></td>
            <td>
              {{ middleUser.ableStatus == 'N'?'禁用':'启用' }}
            </td>
          </tr>
          <tr>
            <td><b>用户类型：</b></td>
            <td>
              {{ middleUser.userType == '0'?'服务商':'开发商' }}
            </td>
            <td><b>用户状态：</b></td>
            <td>
              {{ middleUser.userStatus | statusHandle }}
            </td>
          </tr>
          <tr>
            <td><b>企业名称：</b></td>
            <td colspan="3">
              {{ middleUser.companyName }}
            </td>
          </tr>
          <tr>
            <td><b>电子邮箱：</b></td>
            <td>
              {{ middleUser.email }}
            </td>
            <td><b>手机号码：</b></td>
            <td>
              {{ middleUser.mobile }}
            </td>
          </tr>
          <tr>
            <td><b>钉钉token信息：</b></td>
            <td colspan="3">
              {{ middleUser.dingDingToken }}
            </td>
          </tr>
          <tr>
            <td><b>职位：</b></td>
            <td colspan="3">
              {{ middleUser.position }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>审核状态：</b></td>
            <td colspan="3">
              <el-radio-group v-model="formCheck.userStatus">
                <el-radio :label="'2'">审核通过</el-radio>
                <el-radio :label="'3'">审核不通过</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td><b>原因：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-input
                  v-model.trim="formCheck.memo"
                  type="textarea"
                  placeholder="请输入原因"
                  maxlength="500"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitFormCheck">审 核</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  pageUser,
  initUser,
  getOne,
  saveUser,
  able,
  del,
  checkView,
  saveCheck
} from "@/api/middle/developUser";
import {
  validatePhone,
  validatePostCode,
  validateEmails,
  validUnifiedCredit
} from '@/utils/validate';
export default {
  components: {},
  name: "DevelopUser",
  data () {
    return {
      // 遮罩层
      loading: true,
      statusMap: { "0": "新建", "1": "待审核", "2": "审核成功", "3": "审核失败" },
      userStatus: { "N": "禁用", "Y": "启用" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      middleUserList: [],
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 审核
      openCheck: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        userName: "",
        companyName: "",
        userStatus: ""
      },
      middleUser:{},
      userAuthRecList:[],
      form: {
      },
      formCheck: {
        userStatus: "2",
        memo: "",
        middleUserOid: ""
      },
      rules: {
        serverStatus: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        mobile: [
          { required: true, message: '必填项', trigger: 'blur' },
          { validator:validatePhone, trigger: 'blur' }
        ],
        userName: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        userPassword: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        companyName: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        email: [
          {required:true,message:'必填项',trigger:"blur"},
          { validator:validateEmails, trigger: 'blur' }
        ]


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
      pageUser(this.queryParams).then(response => {
        this.middleUserList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.oid;
      getOne(oid).then(response => {
        this.middleUser = response.data.middleUser;
        this.userAuthRecList = response.data.userAuthRecList;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    // 是否展示个人登记按钮
    isShowAuth (row) {
      return row.userStatus && row.userStatus.indexOf("1") != -1;
    },
    // 初始化新增
    handleInit (row) {
      const oid = row.oid;
      if (oid === undefined) {
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        initUser(oid).then(response => {
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    handleCheck (row) {
      const oid = row.oid;
      checkView(oid).then(response => {
        this.middleUser = response.data.middleUser;
        this.formCheck.middleUserOid =  response.data.middleUser.oid;
        this.openCheck = true;
        this.title = "审核";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveUser(this.form).then(response => {
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
    // 审核提交
    submitFormCheck: function () {
      this.$refs["formCheck"].validate(valid => {
        if (valid) {
          saveCheck(this.formCheck).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openCheck = false;
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
      this.queryParams.pageNumber = 1;
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
      this.openCheck = false;
      this.reset();
    },
    handleAble (row) {
      const oid = row.oid;
      let ableMessage = "";
      let msg = "";
      if (row.ableStatus === 'Y') {
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
          row.ableStatus = row.ableStatus === 'N' ? 'Y' : 'N';
        });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.oid;
      this.$confirm(
        "删除服务商可能会导致对应标签数据出错，是否确认删除?",
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
  },
  filters:{
    statusHandle(data) {
      if(!data) {
        return '待审核'
      }
      const statusMap = {
        0: '新建',
        1: '待审核',
        2: '审核成功',
        3: '审核失败'
      }
      return statusMap[data]
    }
  }
};
</script>
<style></style>

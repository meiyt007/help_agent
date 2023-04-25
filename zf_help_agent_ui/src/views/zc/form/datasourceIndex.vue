<template>
  <div>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="连接名" prop="connectionName">
        <el-input
          v-model="queryParams.connectionName"
          placeholder="请输入连接名"
          clearable
          size="small"
        />
      </el-form-item>
      <!--<el-form-item label="授权key" prop="authorizeKeyApi">
         <el-input
           v-model="queryParams.authorizeKeyApi"
           placeholder="请输入授权key"
           clearable
           size="small"
         />
       </el-form-item>-->
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

    <el-button
      size="mini"
      icon="el-icon-plus"
      type="primary"
      @click="handleInit"
      style="margin-bottom: 10px"
    >
      新增
    </el-button>

    <el-table v-loading="loading" :data="dataSourceList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="连接名"
        align="center"
        prop="connectionName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="数据库名"
        align="center"
        prop="datasourceName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="模块名称"
        align="center"
        prop="moduleName"
        width="140"
      />
      <el-table-column label="类型" align="center" prop="type" width="120" />
      <el-table-column label="状态" align="center" width="90" prop="isAble">
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
        width="220"
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
          <!--<el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
          >删除</el-button>-->
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

    <!-- 添加或修改信息对话框 -->
    <el-dialog
      v-dialog-drag
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
        <el-input v-show="false" v-model="form.id" />
        <el-input v-show="false" v-model="form.datasourceOid" />
        <el-input v-show="false" v-model="form.isAble" />
        <el-input v-show="false" v-model="form.isDelete" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>连接名：</b></td>
            <td>
              <el-form-item prop="connectionName">
                <el-input
                  v-model.trim="form.connectionName"
                  placeholder="请输入连接名"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>所属模块：</b></td>
            <td>
              <el-form-item prop="moduleOid">
                <el-select
                  v-model="form.moduleOid"
                  filterable
                  clearable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="ty in moduleOptions"
                    :key="ty.moduleOid"
                    :label="ty.moduleName"
                    :value="ty.moduleOid"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>类型：</b></td>
            <td>
              <el-form-item prop="type">
                <el-select
                  v-model="form.type"
                  filterable
                  clearable
                  @change="typeChange"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="ty in typeOptions"
                    :key="ty.code"
                    :label="ty.name"
                    :value="ty.code"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-input v-show="false" v-model.trim="form.driverClassName" />
            </td>
            <td><i class="require">*</i><b>数据库名：</b></td>
            <td>
              <el-form-item prop="datasourceName">
                <el-input
                  v-model.trim="form.datasourceName"
                  placeholder="请输入数据库名"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <!--<tr>
              <td><i class="require">*</i><b>驱动名称：</b></td>
              <td colspan="3">
                <el-form-item prop="driverClassName">
                  <el-input v-model.trim="form.driverClassName" placeholder="请输入驱动名称" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>-->

          <tr>
            <td><i class="require">*</i><b>主机：</b></td>
            <td>
              <el-form-item prop="host">
                <el-input
                  v-model.trim="form.host"
                  placeholder="请输入主机"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>端口：</b></td>
            <td>
              <el-form-item prop="port">
                <el-input
                  v-model.trim="form.port"
                  placeholder="请输入端口"
                  maxlength="5"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>用户名：</b></td>
            <td>
              <el-form-item prop="username">
                <el-input
                  v-model.trim="form.username"
                  placeholder="请输入用户名"
                  maxlength="40"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>密码：</b></td>
            <td>
              <el-form-item prop="password">
                <el-input
                  type="password"
                  v-model.trim="form.password"
                  placeholder="请输入密码"
                  maxlength="40"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.type == 'oracle'">
            <td><i class="require">*</i><b>服务名：</b></td>
            <td colspan="3">
              <el-form-item prop="serviceName">
                <el-input
                  v-model.trim="form.serviceName"
                  placeholder="请输入服务名"
                  maxlength="10"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>测试连接：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-button type="primary" size="mini" @click="datasourceTest"
                  >测试连接</el-button
                >
              </el-form-item>
              <span v-if="tipInfo" style="color: #14d234; font-size: 14px"
                >连接成功</span
              >
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看信息详细 -->
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="openView"
      width="900px"
      v-loading="fullscreenLoading"
      append-to-body
    >
      <el-form :model="form" label-width="0px" class="demo-ruleForm">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>连接名：</b></td>
            <td>
              {{ form.connectionName }}
            </td>
            <td><b>所属模块：</b></td>
            <td>
              {{ form.moduleName }}
            </td>
          </tr>
          <tr>
            <td><b>数据库名：</b></td>
            <td>
              {{ form.datasourceName }}
            </td>
            <td><b>类型：</b></td>
            <td>
              <span v-for="type in typeOptions" v-if="type.code == form.type">{{
                type.name
              }}</span>
            </td>
          </tr>
          <!--<tr>
              <td><b>驱动名称：</b></td>
              <td colspan="3">
                {{ form.driverClassName }}
              </td>
            </tr>-->
          <tr>
            <td><b>主机：</b></td>
            <td>
              {{ form.host }}
            </td>
            <td><b>端口：</b></td>
            <td>
              {{ form.port }}
            </td>
          </tr>
          <tr>
            <td><b>登录名：</b></td>
            <td>
              {{ form.username }}
            </td>
            <td><b>密码：</b></td>
            <td>****</td>
          </tr>
          <!--<tr>
              <td><b>状态：</b></td>
              <td colspan="3" v-for="(value, key) in ableMap" :key="key" v-if="form.isAble == key ">
                {{value}}
              </td>
            </tr>-->
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  init,
  save,
  del,
  getOne,
  page,
  able,
  checkConnection,
  getDataSourceDictList
} from "@/api/form/dataSource";
import { encryptPwd } from "@/utils/jsencrypt";
import { modulelist } from "@/api/form/manager";
export default {
  name: "Datasource",
  props: ["authorizeKey", "moduleOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      fullscreenLoading: null,
      // 总条数
      total: 0,
      //授权id
      // 表格数据
      dataSourceList: [],
      //模块列表
      moduleOptions: [],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 数据类型
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: ""
      },
      tipInfo: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        connectionName: [
          { required: true, message: "连接名不能为空", trigger: "blur" }
        ],
        moduleOid: [
          { required: true, message: "请选择所属模块", trigger: "blur" }
        ],
        type: [{ required: true, message: "请选择类型", trigger: "change" }],
        datasourceName: [
          { required: true, message: "数据库名不能为空", trigger: "blur" }
        ],
        host: [{ required: true, message: "主机不能为空", trigger: "blur" }],
        port: [{ required: true, message: "端口不能为空", trigger: "blur" }],
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        serviceName: [
          { required: true, message: "服务名不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created () {
    this.getList();
    this.queryMoudleList();
    /** 获取数据字典中的数据源类型 */
    getDataSourceDictList("FORM_DATASOURCE_TYPE").then(response => {
      this.typeOptions = response.data;
      if (!this.typeOptions) {
        this.typeOptions = [
          { code: "mysql", name: "mysql" }
          //   ,{code: "oracle", name: "oracle"},
          // {code: "sqlserver", name: "sqlserver"},
          // {code: "mongoDB", name: "mongoDB"},
          // {code: "dm", name: "达蒙"},
          // {code: "postgreSQL", name: "postgreSQL"}
        ];
      }
    });
  },
  methods: {
    /** 查询列表 */
    getList () {
      this.loading = true;
      let that = this;
      if (undefined == this.queryParams.authorizeKey)
        this.queryParams.authorizeKey = this.authorizeKey;
      if (undefined == this.queryParams.moduleOid)
        this.queryParams.moduleOid = this.moduleOid;
      page(this.queryParams)
        .then(response => {
          this.dataSourceList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        })
        .catch(function () {
          that.loading = false;
        });
    },
    queryMoudleList () {
      let par = { authorizeKey: this.authorizeKey, moduleOid: this.moduleOid };
      modulelist(par).then(response => {
        this.moduleOptions = response.data;
      });
    },
    // 启用状态
    statusFormat (row) {
      return this.selectMapLabel(this.ableMap, row.isAble);
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
      this.queryParams = {};
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      this.tipInfo = false;
      this.reset();
      const oid = row.id;
      if (oid) {
        this.title = "修改数据源配置信息";
      } else {
        this.title = "新增数据源配置信息";
      }
      this.form = {};
      if (undefined == this.form.authorizeKey) {
        this.form.authorizeKey = this.authorizeKey;
      }
      init(oid).then(response => {
        if (undefined != response.data.formDataSource) {
          this.form = response.data.formDataSource;
          this.form.publicKey = response.data.publicKey;
          ////let Base64 = require('js-base64').Base64
          //let password = Base64.decode(Base64.decode(this.form.password));
          this.form.password = this.hexCharCodeToStr(this.form.password);
        }
        this.openInit = true;
      });
    },
    hexCharCodeToStr (hexCharCodeStr) {
      var trimedStr = hexCharCodeStr.trim();
      var rawStr =
        trimedStr.substr(0, 2).toLowerCase() === "0x"
          ? trimedStr.substr(2)
          : trimedStr;
      var len = rawStr.length;
      if (len % 2 !== 0) {
        return "";
      }
      var curCharCode;
      var resultStr = [];
      for (var i = 0; i < len; i = i + 2) {
        curCharCode = parseInt(rawStr.substr(i, 2), 16); // ASCII Code Value
        resultStr.push(String.fromCharCode(curCharCode));
      }
      return resultStr.join("");
    },
    /** 提交按钮 */
    submitForm: function () {
      if (null == this.form.authorizeKey)
        this.form.authorizeKey = this.authorizeKey;
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (null != this.form.password) {
            this.form.EncryptPassword = encryptPwd(
              this.form.publicKey,
              this.form.password
            );
          }
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
    /** 启禁用按钮操作 */
    handleAble (row) {
      const oid = row.id;
      let ableMessage = row.isAble === 1 ? "启用" : "禁用";
      this.$confirm("你确定要" + ableMessage + "吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return able(oid);
        })
        .then(() => {
          this.msgSuccess(ableMessage + "成功");
        })
        .catch(function () {
          row.isAble = row.isAble === 0 ? 1 : 0;
        });
    },
    openFullScreen () {
      this.fullscreenLoading = this.$loading({
        lock: true,
        text: "测试连接中",
        spinner: "el-icon-loading",
        background: "rgba(179,176,175,0.02)"
      });
    },
    closeFullScreen () {
      this.fullscreenLoading.close();
    },
    //测试连接
    datasourceTest () {
      this.tipInfo = false;
      let that = this;
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.openFullScreen();
          checkConnection(this.form)
            .then(response => {
              that.closeFullScreen();
              if (response.code === 200) {
                this.msgSuccess("连接成功");
                this.tipInfo = true;
              }
            })
            .catch(function () {
              that.closeFullScreen();
            });
        } else {
          return false;
        }
      });
    },
    typeChange (value) {
      this.$refs["form"].clearValidate();
      for (let i = 0; i < this.typeOptions.length; i++) {
        if (this.typeOptions[i].id == value) {
          this.form.driverClassName = this.typeOptions[i].driverClassName;
          break;
        }
      }
    }
  }
};
</script>
<style scoped></style>

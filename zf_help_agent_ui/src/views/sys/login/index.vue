<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="所属区划" prop="districtOidSelect">
        <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
      </el-form-item>
      <el-form-item label="所属机构" prop="organOidSelect">
        <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.organOidSelect" :options="organOptions" placeholder="请输入所属机构" />
      </el-form-item>
      <el-form-item label="用户名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录名" prop="account">
        <el-input
          v-model="queryParams.account"
          placeholder="请输入登录名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="锁定状态">
        <el-radio-group v-model="queryParams.lockStatus">
          <el-radio
            v-for="(status,key) in statusOptions"
            :key="key"
            :label="key"
          >{{status}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="loginList" border>
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true"/>
      <el-table-column label="所属机构" align="center" prop="organName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" placement="top">
            <div v-html="scope.row.organName" slot="content"></div>
            <div class="oneLine">{{scope.row.organName}}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="用户名" align="center" prop="userName" :show-overflow-tooltip="true"/>
      <el-table-column label="登录名"  align="center" prop="account" :show-overflow-tooltip="true"/>
      <el-table-column label="启用状态" align="center"  width="100" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:login:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:login:update']"
          >重置密码</el-button>
          <el-button  v-show="scope.row.lockStatus == '1'"
                      size="mini"
                      type="text"
                      icon="iconfont zfsoft-jiesuo"
                      @click="handleDelock(scope.row)"
                      v-hasPermi="['sys:login:deLock']"
          >解锁</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <!-- 添加或修改 -->
    <el-dialog :close-on-click-modal="false" title="重置密码" :visible.sync="openInit" width="70%" append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.loginOid" />
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>登录名：</b></td>
              <td>
                {{form.account}}
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>重置密码：</b></td>
              <td>
                <span class="require">默认密码为123456<br/>请及时联系对应用户，登录进行修改密码！</span>
              </td>
            </tr>
            <!--<tr>
              <td><i class="require">*</i><b>新密码：</b></td>
              <td>
                <el-form-item prop="password">
                  <el-input v-model.trim="form.password" type="password" placeholder="请输入新密码" maxlength="50" />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>确认密码：</b></td>
              <td>
                <el-form-item prop="confirmPassword">
                  <el-input v-model.trim="form.confirmPassword" type="password" placeholder="请输入确认密码" maxlength="50" />
                </el-form-item>
              </td>
            </tr>-->
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 查看登录信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>所属区划：</b></td>
              <td>
                {{form.districtName}}
              </td>
              <td><b>所属机构：</b></td>
              <td>
                {{ form.organName }}
              </td>
            </tr>
            <tr>
              <td><b>登录名：</b></td>
              <td>
                {{form.account}}
              </td>
              <td><b>所属用户：</b></td>
              <td>
                {{form.userName}}
              </td>
            </tr>
            <tr>
              <td><b>锁定状态：</b></td>
              <td v-for="(value, key) in statusOptions" :key="key" v-if="form.lockStatus == key ">
                {{value}}
              </td>
              <td><b>登录状态：</b></td>
              <td v-for="(value, key) in ableMap" :key="key" v-if="form.isAble == key ">
                {{value}}
              </td>
            </tr>
            <tr>
              <td><b>角色：</b></td>
              <td colspan="3">
                <span v-for="item in form.sysLoginRoleList">{{item.roleName}}&#12288; </span>
              </td>
            </tr>
          </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { getKey } from "@/api/login";
  import {updatePassword, getOne, page, able, handleUnLock} from "@/api/sys/login";
  import { validatePassword } from '@/utils/validate';
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import { encryptPwd } from '@/utils/jsencrypt'
  export default {
    name: "Login",
    components: { Treeselect },
    data() {
      const equalToPassword = (rule, value, callback) => {
        if (this.form.password !== value) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      };
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        loginList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //消息
        ableMap:{'1':'启用','0':'禁用'},
        // 锁定状态
        statusOptions: {'0':'未锁定','1':'已锁定'},
        //区划Tree
        districtOptions:[],
        //组织机构Tree
        organOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          lockStatus:'0',
          districtOid: null,
          organOid: null,
          districtOidSelect: null,
          organOidSelect: null,
          userName: '',
          account:''
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          /*password: [
            { required: true, message: "新密码不能为空", trigger: "blur" },
            { required: true, validator:validatePassword, trigger: 'blur'}
          ],
          confirmPassword: [
            { required: true, message: "确认密码不能为空", trigger: "blur" },
            { required: true, validator:validatePassword, trigger: 'blur'},
            { required: true, validator: equalToPassword, trigger: "blur" }
          ]*/
        }
      };
    },
    created() {
      this.getDistrictTree();
      this.getList();
    },
    watch: {
      'queryParams.districtOidSelect': {
        handler(val,oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          //机构加载
          this.queryParams.districtOid = dataId;
          this.$set(this.queryParams,'organOid',null);
          this.$set(this.queryParams,'organOidSelect',null);
          if(dataId) {
            this.getOrganTree(dataId);
          } else {
            this.organOptions = [];
          }
        },
        deep: true
      },
      'queryParams.organOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.queryParams.organOid = dataId;
        }
      }
    },
    methods: {
      /** 查询应用列表 */
      getList() {
        this.loading = true;
        let that = this;
        if('' == this.queryParams.districtOid){
          this.queryParams.districtOid = null;
        }
        if('' == this.queryParams.organOid){
          this.queryParams.organOid = null;
        }
        page(this.queryParams).then(response => {
          this.loginList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function() {that.loading = false;});
      },
      // 消息
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        this.queryDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
        });
      },
      /** 获取区划树 */
      getOrganTree(districtOid) {
        this.queryOrganTree(districtOid).then(response => {
          this.organOptions = response.data;
        });
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          account: '',
          lockStatus: '',
          districtOid: undefined,
          organOid: undefined
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
        // this.resetForm("queryForm");
        Object.assign(this.queryParams, this.$options.data().queryParams)
        // this.$set(this.queryParams,'districtOid',null);
        // this.$set(this.queryParams,'organOid',null);
        // this.$set(this.queryParams,'districtOidSelect',null);
        this.queryParams.districtOid=null;
        this.queryParams.organOid=null;
        this.queryParams.organOidSelect=null;
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看登录信息";
        });
      },
      /** 重置密码按钮操作 */
      handleInit(row) {
        this.reset();
        getKey().then(res => {
          this.form.publicKey = res.data.publicKey
          this.form.config = res.data.config
        })
        this.form.account = row.account;
        this.form.id = row.id;
        this.openInit = true;
        this.title = "重置密码";
      },
      // 解锁登录用户
      handleDelock(row) {
        let id = row.id;
        this.$confirm('你确定要解锁吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return handleUnLock(id);
        }).then(() => {
          this.getList();
          this.msgSuccess("解锁成功");
        }).catch(function () {});
      },
      /** 提交按钮 */
      submitForm: function () {
        let that = this ;
        this.$refs["form"].validate(valid => {
          if (valid) {
            that.$confirm('你确定要重置密码为123456吗？', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function () {
              //let password=this.form.password
              //let confirmPassword=this.form.confirmPassword
              let password='123456'
              let confirmPassword='123456'
              if("1"===that.form.config){
                password=encryptPwd(that.form.publicKey,password);
                confirmPassword=encryptPwd(that.form.publicKey,confirmPassword);
              }
              const data = {
                password: password,
                confirmPassword: confirmPassword,
                id: that.form.id
              }

              updatePassword(data).then(response => {
                if (response.code === 200) {
                  that.msgSuccess("重置密码成功");
                  that.openInit = false;
                  that.getList();
                }
              });

            }).catch(function () {
              //alert(3)
            });

          }
        });

      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const id = row.id;
        let ableMessage = row.isAble == 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return able(id);
        }).then(() => {
          this.msgSuccess(ableMessage + "成功");
        }).catch(function () {
          row.isAble = row.isAble == 0 ? 1 : 0
        });
      }
    }
  };
</script>
<style>
  .treeselect{
    width: 200px;
  }
  .oneLine {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
</style>


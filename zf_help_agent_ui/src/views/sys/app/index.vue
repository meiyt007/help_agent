<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" @submit.native.prevent>
      <el-form-item label="应用名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入应用名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['sys:app:init']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sys:app:listExp']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="appList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="应用名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="显示名称" align="center" prop="displayName" :show-overflow-tooltip="true"/>
      <el-table-column label="访问根路径"  align="center" prop="accessRootAddr" :show-overflow-tooltip="true"/>
      <el-table-column label="图标"  width="100" align="center" prop="iconName" >
        <template slot-scope="scope">
          <i :class="scope.row.iconName"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createDate" width="180">
        <!--<template slot-scope="scope">
          <span>{{scope.row.createDate}}</span>
        </template>-->
      </el-table-column>
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
      <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:app:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:app:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:app:delete']"
          >删除</el-button>
          <!--<el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-jinyong"
            @click="handleAble(scope.row)"
            v-hasPermi="['sys:app:able']"
          >启禁用</el-button>-->
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


    <!-- 添加或修改应用信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" height="600px" scrollbar append-to-body>
      <div>
        <!--<h3>事项标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>应用名称：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入应用名称" maxlength="10" show-word-limit/>
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>显示名称：</b></td>
              <td>
                <el-form-item  prop="displayName">
                  <el-input v-model.trim="form.displayName" placeholder="请输入显示名称" maxlength="10" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>登录后首页地址：</b></td>
              <td colspan="3">
                <el-form-item  prop="loginSuccessAddr">
                  <el-input v-model.trim="form.loginSuccessAddr" placeholder="请输入登录后首页地址" maxlength="100" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>应用访问根路径：</b></td>
              <td colspan="3">
                <el-form-item prop="accessRootAddr">
                  <el-input v-model.trim="form.accessRootAddr" placeholder="请输入应用访问根路径" maxlength="100" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>远程注册地址：</b></td>
              <td colspan="3">
                <el-form-item  prop="remoteRegistryAddr">
                  <el-input v-model.trim="form.remoteRegistryAddr" placeholder="请输入远程注册地址" maxlength="100" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-input-number v-model="form.sort" :min="1" :max="9999"/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>第三方系统：</b></td>
              <td colspan="3">
                <el-form-item >
                  <el-radio-group v-model="form.isOther">
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><b>描述：</b></td>
              <td colspan="3">
                <el-form-item>
                  <el-input v-model.trim="form.memo" type="textarea" placeholder="请输入描述" maxlength="500" show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>图标：</b></td>
              <td colspan="3">
                <el-form-item prop="iconName">
                  <el-input v-show="false" v-model="form.iconName" />
                  <i style="font-size: 30px;" :class="form.iconName" />
                  <el-button
                    type="primary"
                    size="mini"
                    @click="handleIconSelect"
                  >选择图标</el-button>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 查看应用信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div>
        <!--<h3>事项标题</h3>-->
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>应用名称：</b></td>
            <td>
              {{ form.name }}
            </td>
            <td><b>显示名称：</b></td>
            <td>
              {{ form.displayName }}
            </td>
          </tr>
          <tr>
            <td><b>登录后首页地址：</b></td>
            <td colspan="3">
              {{ form.loginSuccessAddr }}
            </td>
          </tr>
          <tr>
            <td><b>应用访问根路径：</b></td>
            <td colspan="3">
              {{ form.accessRootAddr }}
            </td>
          </tr>
          <tr>
            <td><b>远程注册地址：</b></td>
            <td colspan="3">
              {{ form.remoteRegistryAddr }}
            </td>
          </tr>
          <tr>
            <td><b>排序号：</b></td>
            <td>
              {{ form.sort }}
            </td>
            <td><b>第三方系统：</b></td>
            <td>
              <span v-if="form.isOther == 1">是</span>
              <span v-if="form.isOther == 0">否</span>
            </td>
          </tr>
          <tr>
            <td><b>图标：</b></td>
            <td>
              <i style="font-size: 30px;" :class="form.iconName"></i>
            </td>
            <td><b>创建时间：</b></td>
            <td>
              {{ parseTime(form.createDate) }}
            </td>
          </tr>
          <tr>
            <td><b>描述：</b></td>
            <td colspan="3">
              {{ form.memo }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--引入图标选择弹出层-->
    <el-dialog  :close-on-click-modal="false" title="选择按钮图标" :visible.sync="openIconSelectView" width="900px" append-to-body>
      <el-scrollbar style="height:500px;">
        <iconfont  @father-click="getSelectClass" ></iconfont>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able,listExp } from "@/api/sys/app";
  import { validChinese,validIntNoZero } from '@/utils/validate';
  import iconfont from '@/views/common/iconfontSelect';
  export default {
    components: {
      iconfont
    },
    name: "App",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        appList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 第三方系统状态
        otherOptions: [],
        //图标样式弹出层
        openIconSelectView:false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: ''
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            { required: true, message: "应用名称不能为空", trigger: "blur" },
            {validator:validChinese, trigger: 'blur'}
          ],
          displayName: [
            { required: true, message: "显示名称不能为空", trigger: "blur" },
            {validator:validChinese, trigger: 'blur'}
          ],
          loginSuccessAddr: [
            { required: true, message: "登录后首页地址不能为空", trigger: "blur" }
          ],
          accessRootAddr: [
            { required: true, message: "应用访问根路径不能为空", trigger: "blur" }
          ],
          remoteRegistryAddr: [
            { required: true, message: "远程注册地址不能为空", trigger: "blur" }
          ],
          sort: [
            { required: true, message: "排序号不能为空", trigger: "blur" },
            {validator:validIntNoZero, trigger: 'blur'}
          ],
          iconName: [
            { required: true, message: "图标不能为空", trigger: "blur" },
          ]
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询应用列表 */
      getList() {
        let that = this;
        this.loading = true;
        page(this.queryParams).then(response => {
          this.appList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function() {that.loading = false;});
      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      // 取消按钮
      cancel() {
        this.openIconSelectView = false;
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          name: null
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
      /** 查看按钮操作 */
      handleView(row) {
        this.openIconSelectView = false;
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看应用信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.openIconSelectView = false;
        this.reset();
        const oid = row.id;
        if(oid == undefined){
          this.form = {isOther:0};
          this.openInit = true;
          this.title = "新增应用信息";
        }else {
          init(oid).then(response => {
            this.form = response.data.app;
            this.openInit = true;
            this.title = "修改应用信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInit = false;
                this.getList();
              }
            });
          }else {
            return false;
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
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble === 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
        }).catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          listExp(queryParams);
        }).catch(function() {});
      },
      /** 选择按钮图标 */
      handleIconSelect() {
        this.openIconSelectView = true;
      },
      //从子组件中获取选择的图标
      getSelectClass(className){
        //this.form.iconName = className;
        this.$set(this.form, "iconName", className);
        this.openIconSelectView = false;
      }
    }
  };
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="流程类型">
        <el-select
          v-model="queryParams.typeOid"
          placeholder="请选择流程类型"
          size="small"
          filterable
          style="width: 240px"
          clearable
        >
          <el-option
            v-for="type in typeList"
            :key="type.typeOid"
            :label="type.name"
            :value="type.typeOid"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入编码"
          clearable
          size="small"
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
          v-hasPermi="['workflow:link:init']"
        >新增</el-button>
      </el-col>

    </el-row>

    <el-table v-loading="loading" :data="linkList" border="">
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属类型" align="center" prop="typeName" :show-overflow-tooltip="true" />
      <el-table-column label="名称" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="编码"  align="center" prop="code" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createDate" width="220">
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
      <el-table-column label="操作" width="250" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['workflow:link:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['workflow:link:delete']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:link:delete']"
          >删除</el-button>
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


    <!-- 添加或修改信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
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
              <td><i class="require">*</i><b>名称：</b></td>
              <td>
                <el-form-item  prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入名称" maxlength="20" show-word-limit/>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>编码：</b></td>
              <td >
                <el-form-item  prop="code">
                  <el-input v-model.trim="form.code" placeholder="请输入编码" maxlength="20" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>所属类型：</b></td>
              <td>
                <el-form-item  prop="typeOid">
                  <el-select
                    v-model="form.typeOid"
                    placeholder="请选择所属类型"
                    size="small"
                    filterable
                    style="width: 240px"
                    clearable
                  >
                    <el-option
                      v-for="type in typeList"
                      :key="type.typeOid"
                      :label="type.name"
                      :value="type.typeOid"
                    />
                  </el-select>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>环节操作(请求地址)：</b></td>
              <td >
                <el-form-item  prop="actionName">
                  <el-input v-model.trim="form.actionName" placeholder="请输入环节操作" maxlength="250" show-word-limit />
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                <el-form-item>
                  <el-input v-model.trim="form.memo" type="textarea" placeholder="请输入备注" maxlength="800" show-word-limit></el-input>
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


    <!-- 查看信息详细 -->
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
              <td><b>名称：</b></td>
              <td>
                {{ form.name }}
              </td>
              <td><b>编码：</b></td>
              <td>
                {{ form.code }}
              </td>
            </tr>
            <tr>
              <td><b>所属类型：</b></td>
              <td>
                {{ form.typeName }}
              </td>
              <td><b>环节操作(请求地址)：</b></td>
              <td>
                {{ form.actionName }}
              </td>
            </tr>
            <tr>
              <td><b>创建时间：</b></td>
              <td colspan="3">
                {{ parseTime(form.createDate) }}
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
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
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able } from "@/api/workflow/link";
  import { typeList } from "@/api/workflow/type";
  export default {
    name: "Link",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        linkList: [],
        //所属类型
        typeList:[],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
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
          typeOid: [
            { required: true, message: "所属类型不能为空", trigger: "change" },
          ],
          name: [
            { required: true, message: "名称不能为空", trigger: "blur" },
          ],
          code: [
            { required: true, message: "编码不能为空", trigger: "blur" },
          ]
        }
      };
    },
    created() {
      this.getTypeList();
      this.getList();
    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.linkList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function() {that.loading = false;});
      },
      //查询所属类型
      getTypeList() {
        typeList(this.queryParams).then(response => {
          this.typeList = response.data;
        });
      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      // 取消按钮
      cancel() {
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
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看流程类型信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.openIconSelectView = false;
        this.reset();
        const oid = row.id;
        if(oid == undefined){
          this.form = {};
          this.openInit = true;
          this.title = "新增流程类型环节信息";
        }else {
          init(oid).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改流程类型环节信息";
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
      }
    }
  };
</script>

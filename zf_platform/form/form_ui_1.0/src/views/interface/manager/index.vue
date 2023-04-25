<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="接口描述" prop="desc">
        <el-input
          v-model="queryParams.desc"
          placeholder="请输入接口描述"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="唯一标识" prop="flag">
        <el-input
          v-model="queryParams.flag"
          placeholder="请输入唯一标识"
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
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="apiRuleEntityList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="接口描述" align="center" prop="desc" :show-overflow-tooltip="true" />
      <el-table-column label="唯一标识" align="center" prop="flag" :show-overflow-tooltip="true" />
      <el-table-column label="请求url" align="center" prop="url" :show-overflow-tooltip="true"/>
      <el-table-column label="请求方式" align="center" prop="method" :show-overflow-tooltip="true"/>
      <el-table-column label="操作"  align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
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


    <!-- 添加或修改对话框 -->
    <el-dialog :close-on-click-modal="false" title="接口配置" :visible.sync="api.show"
               v-for="api in dialogOptions"
               @close="closeDialogView"
               width="95%" append-to-body>
      <IndexTab @handleColse="closeDialogView" :apiRuleEntity="api.apiRuleEntity"></IndexTab>
      <!--<div slot="footer" class="dialog-footer">
        <el-button @click="closeDialogView">关 闭</el-button>
      </div>-->
    </el-dialog>


    <!-- 查看 -->
    <el-dialog :title="title" :visible.sync="openView" width="90%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form :model="form" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>接口描述：</b></td>
              <td>
                {{ form.desc }}
              </td>
              <td><b>请求方式：</b></td>
              <td>
                {{ form.method }}
              </td>
            </tr>
            <tr>
              <td><b>唯一标识：</b></td>
              <td colspan="3" style="color: red">
                {{form.flag}}
              </td>
            </tr>
            <tr>
              <td><b>请求url：</b></td>
              <td colspan="3">
                {{form.url}}
              </td>
            </tr>
            <tr>
              <td><b>请求内容：</b></td>
              <td colspan="3">
                {{form}}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {del ,getOne, page } from "@/api/interface/manager";
  import IndexTab from './indexTab';
  export default {
    name: "Manager",
    components: {IndexTab },
    props:['flag'],
    data() {
      return {
        //授权id
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        apiRuleEntityList: [],
        dialogOptions: [],
        //参数
        params:{},
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
          name: ''
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
        }
      };
    },
    created() {
      this.getList();
    },
    watch:{
    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.apiRuleEntityList = response.data.data.data;
          this.total = response.data.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
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
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        /*this.form = row;
        this.openView = true;
        this.title = "查看";*/
        const oid = row.flag;
        getOne(oid).then(response => {
          this.form = response.data.data;
          this.openView = true;
          this.title = "查看";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const flag = row.flag;
        if(flag == undefined){
          let item = {show:true};
          this.dialogOptions.push(item);
        }else {
          let item = {show:true,apiRuleEntity:row};
          this.dialogOptions.push(item);
          /*const oid = row.flag;
          getOne(oid).then(response => {
            let item = {show:true,apiRuleEntity:response.data.data};
            this.dialogOptions.push(item);
          });*/
        }
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.flag;
        this.$confirm('是否确认删除吗?', "警告", {
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
      //打开弹框
      openDialogView(row){
        let item = {show:true,flag:row.flag};
        this.dialogOptions.push(item);
      },
      //关闭表结构弹框
      closeDialogView(data){
        this.dialogOptions.pop();
        if(data){
          this.getList();
        }
      }
    }
  };
</script>
<style >

</style>

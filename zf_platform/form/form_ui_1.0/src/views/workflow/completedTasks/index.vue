<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="办件编号" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入办件编号"
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

    <el-table v-loading="loading" :data="processList" border="">
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="办件编号"  align="center" prop="projectNo" :show-overflow-tooltip="true"/>
      <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
      <el-table-column label="申请人" align="center" prop="applyerName" :show-overflow-tooltip="true"/>
      <el-table-column label="审批环节" width="100" align="center" prop="processName" :show-overflow-tooltip="true"/>
      <el-table-column label="开始时间"  align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="办结时间"  align="center" prop="endDate" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-liuchengchakan"
            @click="handleFlowView(scope.row,0)"
          >查看流程图
          </el-button>
          <el-button
            v-if="'' != scope.row.superProcessInstanceId && undefined != scope.row.superProcessInstanceId"
            size="mini"
            type="text"
            icon="iconfont zfsoft-liuchengchakan"
            @click="handleFlowView(scope.row,1)"
          >查看整体流程图
          </el-button>
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

  </div>
</template>

<script>
  import  {completedTasksPage} from "@/api/workflow/accept";
  export default {
    name: "Accept",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        processList: [],
        taskCondition:[],
        flowList:[],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 审核弹出层
        openReview: false,
        step:'',
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        descp:'',
        dialogFlowViewOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: ''
        },
        params:{
          row: ''
        },
        // 表单参数
        form: {
          metaInfo:{
            description:''
          }
        },
        processExample:{
          eventName:"通过"
        },
        // 表单校验
        rules: {

        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      //关闭弹框
      outerEditVisible(){
        this.dialogEditOptions.pop();
      },
      outerViewVisible(){
        this.dialogViewOptions.pop();
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that =this;
        completedTasksPage(this.queryParams).then(response => {
          this.processList = response.data.data;
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
        this.openInit = false;
        this.reset();
      },

      // 表单重置
      reset() {
        this.descp='';
        this.form = {};
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
      handleFlowView(row,index) {
        if(1 == index ){
          window.open("/flowView?processInstanceId="+row.superProcessInstanceId,'_blank')
        }else{
          window.open("/flowView?processInstanceId="+row.processInstanceId,'_blank')
        }

      },
      handleFlowViewModelVisiable(object) {
        this.dialogFlowViewOptions.pop();
      },
      //关闭进行的流程弹框
      outerFlowViewVisible(){
        this.dialogFlowViewOptions.pop();
      },
    }
  };
</script>
<style>
  .greenSpan{
    color: green;
  }
  .redSpan{
    color: red;
  }
</style>

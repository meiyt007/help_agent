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

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['workflow:accept:init']"
        >登记办件</el-button>
      </el-col>
    </el-row>

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
      <el-table-column label="审批环节"  align="center" prop="processName" :show-overflow-tooltip="true"/>
      <el-table-column label="开始时间"  align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" width="350" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhe"
            @click="handleReview(scope.row)"
            v-hasPermi="['workflow:accept:init']"
          >审核
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-liuchengchakan"
            @click="handleFlowView(scope.row)"
          >查看进行的流程图
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


    <!-- 添加或修改信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>项目名称：</b></td>
              <td>
                <el-form-item prop="projectName">
                  <el-input v-model.trim="form.projectName" placeholder="请输入项目名称" maxlength="200" show-word-limit/>
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>申请人：</b></td>
              <td>
                <el-form-item  prop="applyerName">
                  <el-input v-model.trim="form.applyerName" placeholder="请输入申请人" maxlength="200" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>流程：</b></td>
              <td>
                <el-form-item  prop="infoOid">
                  <el-col :span="24">
                    <el-select
                      v-model="form.infoOid"
                      placeholder="请选择流程"
                      filterable
                      style="width: 100%"
                      clearable
                    >
                      <el-option
                        v-for="item in flowList"
                        :key="item.infoOid"
                        :label="item.workflowName"
                        :value="item.infoOid"
                      />
                    </el-select>
                  </el-col>
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

    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openReview" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>标题</h3>-->
        <el-form ref="processExample" :model="processExample" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="processExample.id" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>项目名称：</b></td>
              <td>
                <el-form-item prop="projectName">
                  {{processExample.projectName}}
                </el-form-item>
              </td>

              <td><b>申请人：</b></td>
              <td>
                <el-form-item  prop="applyerName">
                  {{processExample.applyerName}}
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>办理动作：</b></td>
              <td colspan="3">
                <el-form-item prop="eventName" >
                  <el-col :span="24">
                    <el-radio-group v-model="processExample.eventName">
                      <el-radio label="通过" >通过</el-radio>
                      <el-radio label="不通过">不通过</el-radio>
                      <el-radio v-for="(value, key) in processExample.rejectTaskIdMap" :key="key" :label="value +'~'+ key" >{{value}}</el-radio>
                      <el-radio v-for="(value, key) in processExample.fallbackTaskIdMap" :key="key" :label="value+'~'+key" >{{value}}</el-radio>
                    </el-radio-group>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>办理条件：</b></td>
              <td colspan="3">
                <el-form-item v-for="item in taskCondition">
                  {{item}}
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>办理参数键：</b></td>
              <td>
                <el-form-item  prop="handleParamKey">
                  <el-input v-model.trim="processExample.handleParamKey" placeholder="办理参数键" maxlength="200" show-word-limit/>
                </el-form-item>
                <div style="color: red;margin-top: 5px;">多个参数键，使用英文模式下逗号,隔开</div>
              </td>
              <td><b>办理参数值：</b></td>
              <td>
                <el-form-item  prop="handleParamValue">
                  <el-input v-model.trim="processExample.handleParamValue" placeholder="办理参数值" maxlength="200" show-word-limit/>
                </el-form-item>
                <div style="color: red;margin-top: 5px;">多个参数值，使用英文模式下逗号,隔开</div>
              </td>
            </tr>
            <tr>
              <td><b>办理意见：</b></td>
              <td colspan="3">
                <el-form-item  prop="handleExplain">
                  <el-input v-model.trim="processExample.handleExplain" type="textarea" placeholder="办理意见" show-word-limit/>
                </el-form-item>
              </td>
            </tr>

          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAccept">确 定</el-button>
        <el-button @click="cancelAccept">取 消</el-button>
      </div>
    </el-dialog>

    <!--<el-dialog v-dialog-drag :fullscreen="true" :visible.sync="item.show" v-for="item in dialogFlowViewOptions" @close="outerFlowViewVisible"
               :title="item.title" width="90%" append-to-body>
      <FlowView :params="item.processInstanceId" @handleViewModelVisiable="handleFlowViewModelVisiable"></FlowView>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerFlowViewVisible">关 闭</el-button>
      </div>
    </el-dialog>-->
  </div>
</template>

<script>
  import  {init, save, page,next,getTaskCondition,list} from "@/api/workflow/accept";
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
          eventName:"通过",
          rejectTaskIdMap:[],
          fallbackTaskIdMap:[]
        },
        // 表单校验
        rules: {
          projectName: [
            { required: true, message: "项目名称不能为空", trigger: "blur" },
          ],
          applyerName: [
            { required: true, message: "申请人不能为空", trigger: "blur" },
          ],
          infoOid: [
            { required: true, message: "流程不能为空", trigger: "blur" },
          ]
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
      /** 查询应用列表 */
      getList() {
        this.loading = true;
        let that =this;
        page(this.queryParams).then(response => {
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
      cancelAccept(){
        this.openReview = false;
        this.reset();
      },
      submitAccept(){
        this.$refs["processExample"].validate(valid => {
          if (valid) {
            next(this.processExample).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openReview = false;
                this.queryParams.pageNum = 1;
                this.queryParams.pageSize = 10;
                this.getList();
              }
            });
          }else {
            return false;
          }
        });
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
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        this.openInit = true;
        this.title = "办件登记";
        list('').then(response => {
          if (response.code === 200) {
            this.flowList=response.data
          }
        });
      },
      handleReview(row) {
        this.reset();
        this.openReview = true;
        this.title = "办件审核";
        this.processExample=row
        this.$set(this.processExample,'eventName',"通过")
        this.taskCondition=[]
        getTaskCondition(row.taskId).then(response => {
          if (response.code === 200) {
            this.taskCondition=response.data.taskCondition;
            //this.step = response.data.step;
            if(undefined != response.data.rejectTaskIdMap){
              this.processExample.rejectTaskIdMap = response.data.rejectTaskIdMap;
            }
            if(undefined!=response.data.fallbackTaskIdMap){
              this.processExample.fallbackTaskIdMap = response.data.fallbackTaskIdMap;
            }
          }
        });
      },
      handleFlowView(row) {
       /* let item = {
          show:true,
          title:row.projectName,
          processInstanceId:row.processInstanceId
        };
        this.dialogFlowViewOptions.push(item);
*/
        window.open("/flowView?processInstanceId="+row.processInstanceId,'_blank')
      },
      handleFlowViewModelVisiable(object) {
        this.dialogFlowViewOptions.pop();
      },
      //关闭进行的流程弹框
      outerFlowViewVisible(){
        this.dialogFlowViewOptions.pop();
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            // var des = this.descp;
            // this.form.descp = des ;
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
      }
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

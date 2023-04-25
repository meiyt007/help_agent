<template>
    <div>
        <el-form-item label="名称">
            {{name}}
        </el-form-item>
      <el-form-item label="办理时限">
        {{timeLimit}}
        <span v-if="'W' == timeUnit">工作日</span>
        <span v-if="'N' == timeUnit">自然日</span>
        {{timeLimitHour}}&nbsp;&nbsp;小时
      </el-form-item>
        <!--<el-form-item label="内嵌流程" v-show="true">
            <el-input type="calledElement" v-model="documentation" readonly></el-input>
        </el-form-item>-->
      <el-form-item label="内嵌流程">
        <span>{{innerFlowName}}</span>
        <span><el-button
          class="lookView"
          size="mini"
          type="text"
          icon="iconfont zfsoft-liuchengchakan"
          @click="handleFlowView()"
        >查看流程图</el-button></span>
      </el-form-item>
      <el-form-item label="环节状态">
        <!--当前环节状态 （1.等待办理 2.正在办理 3.按时办理 4.超期未办理  5.超期办理 6跳过 7.暂停 8终止流程,9 驳回退回 10审核不通过）-->
        <span v-if="'1' == handleStatus">等待办理</span>
        <span v-if="'2' == handleStatus">正在办理</span>
        <span v-if="'3' == handleStatus">按时办理</span>
        <span v-if="'4' == handleStatus">超期未办理</span>
        <span v-if="'5' == handleStatus">超期办理</span>
        <span v-if="'6' == handleStatus">跳过</span>
        <span v-if="'7' == handleStatus">暂停</span>
        <span v-if="'8' == handleStatus">中止</span>
        <span v-if="'9' == handleStatus">驳回/退回</span>
        <span v-if="'10' == handleStatus">审核不通过</span>
      </el-form-item>
      <el-form-item label="办理情况" v-show="taskMapSize > 0">
        <div class="handling" v-for="(workflowList,key,index) in workflowTaskVoListMap" :key="key">
          <div slot="header" class="clearfix">
            <span>第{{index+1}}次办理</span>
          </div>
          <div class="handling-detail" v-for="(workflow,index1) in workflowList">
            <div class="text item">开始时间：{{workflow.startDate}}</div>
            <div class="text item">应办时间：{{workflow.limitDate}}</div>
            <div class="text item">结束时间：{{workflow.endDate}}</div>
            <!--办理状态 （1审核通过  2审核不通过 3驳回）-->
            <div class="text item">
              办理结果：
              <span v-if="'1' == workflow.handleResult">通过</span>
              <span v-if="'2' == workflow.handleResult">审核不通过</span>
              <span v-if="'3' == workflow.handleResult">驳回</span>
              <span v-if="'4' == workflow.handleResult">退回</span>
              <span v-if="'5' == workflow.handleResult">跳过</span>
            </div>
          </div>
        </div>
      </el-form-item>


      <!--内嵌流程图弹框-->
      <el-dialog v-dialog-drag :visible.sync="item.show" v-for="item in dialogViewOptions" @close="outerViewVisible"
                 :title="item.title" width="90%" append-to-body>
        <InnerFlowViewer :params="item.flowOid" @handleViewModelVisiable="outerViewVisible"></InnerFlowViewer>
        <div slot="footer" class="dialog-footer">
          <el-button @click="outerViewVisible">关 闭</el-button>
        </div>
      </el-dialog>

    </div>

</template>
<script>
  import {
    getInnerViewFlowByProcessInstanceIdAndActivityId,
  } from "@/api/workflow/bussinfo";
  import InnerFlowViewer from './InnerFlowViewer'
  import InnerViewer from './InnerViewer'
export default {
    props:['element', "params"],
    components: {
      InnerFlowViewer,
      InnerViewer
    },
    data(){
        return {
            id: this.element.id || '',
            name: '',
            documentation:'',
            calledElement: '',
            dialogViewOptions:[],
          dialogNoStartViewOptions:[],
            positions:[],
            timeUnit: "W",
            timeLimit: "0",
            timeLimitHour: "0",
            innerFlowOid:'',
            innerFlowName:'',
            processInstanceId:'',
            paramsView :{},
            taskMapSize: 0,
            workflowTaskVoList: [],
            workflowTaskVoListMap: {},
            handleStatus:'1'
        }
    },
    mounted() {
      this.getWorkflowTaskVo();
    },
    methods:{
      outerViewVisible(){
        this.dialogViewOptions.pop();
        this.dialogNoStartViewOptions.pop();
      },
      /** 查看流程图按钮操作 */
      handleFlowView(){
        if(undefined != this.processInstanceId && ''!=this.processInstanceId){
          this.handleProcessView();
        }else{
          this.handleNoStartProcessView();
        }
      },
      /** 查看已启动流程图按钮操作 */
      handleProcessView() {
        let item = {
          show:true,
          title:'流程查看（'+this.innerFlowName+'）',
          flowOid:{processInstanceId:this.processInstanceId,flowName:this.innerFlowName}
        };
        this.dialogViewOptions.push(item);
        //window.open("/innerFlowViewer?processInstanceId="+this.processInstanceId,'_blank')
      },
      /** 查看未启动流程图按钮操作 */
      handleNoStartProcessView() {
        let item = {
          show:true,
          title:'流程查看（'+this.innerFlowName+'）',
          flowOid:{innerFlowOid:this.innerFlowOid,flowName:this.innerFlowName}
        };
        this.dialogViewOptions.push(item);
        //window.open("/innerFlowViewer?innerFlowOid="+this.innerFlowOid,'_blank')
      },
      getWorkflowTaskVo() {
        const that = this;
        //从环节表中获取数据
        var data = { processInstanceId: this.params, activityId: this.id };
        getInnerViewFlowByProcessInstanceIdAndActivityId(data).then((response) => {
          this.paramsView.name = response.data.flowName;
          this.innerFlowName = response.data.flowName;
          this.taskMapSize = response.data.taskMapSize;
          this.innerFlowOid = response.data.innerFlowOid;
          this.processInstanceId = response.data.calledProcessInstanceId;
          this.workflowTaskVoListMap = response.data.taskMap;
          let workflowTask = response.data.workflowTaskVo;
          if (null != workflowTask) {
            this.handleStatus = workflowTask.handleStatus;
          }
          const step = response.data.taskStep;
          this.timeLimitHour = undefined == step.timeLimitHour ? "0" : step.timeLimitHour;
          this.timeUnit = undefined == step.timeUnit ? "W" : step.timeUnit;
          this.timeLimit = undefined == step.timeLimit ? "0" : step.timeLimit;
        });
      },
    },
    watch:{
        //监控的元素的变化
        element:{
            deep: true,
            immediate: true,
            handler:function(newVal,oldVal){
                const businessObject = newVal.businessObject;
                this.name = businessObject.name;
            }
        }
    }
}
</script>
<style>
  .lookView{
    margin-left: 10px !important;
  }
</style>

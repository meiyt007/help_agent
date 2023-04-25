<template>
  <div>
    <el-form-item label="编号" v-show="false">
      <el-input v-model="id" readonly></el-input>
    </el-form-item>
    <el-form-item label="环节名称">{{name}}</el-form-item>
    <el-form-item label="办理时限">
      {{timeLimit}}
      <span v-if="'W' == timeUnit">工作日</span>
      <span v-if="'N' == timeUnit">自然日</span>
      {{timeLimitHour}}&nbsp;&nbsp;小时
    </el-form-item>
    <el-form-item label="办理人">{{handleUserNames}}</el-form-item>
    <el-form-item label="办理岗位">{{handlePostNames}}</el-form-item>
    <el-form-item label="环节状态">
      <!--当前环节状态 （1.等待办理 2.正在办理 3.按时办理 4.超期未办理  5.超期办理 6跳过 7.暂停）-->
      <span v-if="'1' == handleStatus">等待办理</span>
      <span v-if="'2' == handleStatus">正在办理</span>
      <span v-if="'3' == handleStatus">按时办理</span>
      <span v-if="'4' == handleStatus">超期未办理</span>
      <span v-if="'5' == handleStatus">超期办理</span>
      <span v-if="'6' == handleStatus">跳过/中止</span>
      <span v-if="'7' == handleStatus">暂停</span>
    </el-form-item>
    <el-form-item label="办理情况" v-show="taskMapSize > 0">
      <div class="handling" v-for="(workflowList,key,index) in workflowTaskVoListMap" :key="key">
        <div slot="header" class="first-handle clearfix">
          <span>第{{index+1}}次办理</span>
        </div>
        <div class="handling-detail" v-for="(workflow,index1) in workflowList">
          <div class="handle-box">
            <div class="text item">开始时间：{{workflow.startDate}}</div>
            <div class="text item">应办时间：{{workflow.limitDate}}</div>
            <div class="text item">结束时间：{{workflow.endDate}}</div>
            <!--办理状态 （1审核通过  2审核不通过 3驳回）-->
            <div class="text item">
              办理结果：
              <span v-if="'1' == workflow.handleResult">通过</span>
              <span v-if="'2' == workflow.handleResult">审核不通过</span>
              <span v-if="'3' == workflow.handleResult">驳回</span>
            </div>
            <div class="text item">办理人：{{workflow.userName}}</div>
          </div>
        </div>
      </div>
    </el-form-item>
  </div>
</template>
<script>
const forEach = require("lodash/forEach");
import {
  getWorkflowTaskVoByInstanceIdAndActivityId,
  getWorkflowStepByInstanceIdAndActivityId,
  getWorkflowBussFlowStepByInfoIdAndActivityId,
} from "@/api/workflow/bussinfo";
// 用户任务属性组件
export default {
  props: ["element", "params"],
  inject: ["bpmnModeler"],
  data() {
    return {
      // 共有
      id: this.element.id || "",
      name: "",
      formKey: "",
      timeUnit: "W",
      timeLimitHour: "0",
      handleUserType: "1",
      handleType: "1",
      handleUserNames: "",
      handlePostNames: "",
      taskMapSize: 0,
      workflowTaskVoList: [],
      workflowTaskVoListMap: {},
      timeLimit: "0",
      //当前环节状态 （1.等待办理 2.正在办理 3.按时办理 4.超期未办理  5.超期办理 6跳过 7.暂停）
      handleStatus: "1",
    };
  },
  methods: {
    getWorkflowTaskVo() {
      const that = this;
      let processInstanceId =
        "string" == typeof this.params
          ? this.params
          : this.params.processInstanceId;
      if (
        undefined == processInstanceId &&
        undefined != this.params.innerFlowOid
      ) {
        //从环节表中获取数据
        var data = { infoOid: this.params.innerFlowOid, activityId: this.id };
        getWorkflowBussFlowStepByInfoIdAndActivityId(data).then((resp) => {
          const step = resp.data;
          that.timeLimitHour =
            undefined == step.timeLimitHour ? "0" : step.timeLimitHour;
          that.timeUnit = undefined == step.timeUnit ? "W" : step.timeUnit;
          that.timeLimit = undefined == step.timeLimit ? "0" : step.timeLimit;
          that.handleUserNames =
            "" == step.handleUserNames ? "无" : step.handleUserNames;
          that.handlePostNames =
            "" == step.handlePostNames ? "无" : step.handlePostNames;
        });
      } else {
        var data = {
          processInstanceId: processInstanceId,
          activityId: this.id,
        };
        getWorkflowTaskVoByInstanceIdAndActivityId(data).then((response) => {
          //that.workflowTaskVoListMap = Object.entries(response.data.taskMap);
          that.workflowTaskVoListMap = response.data.taskMap;
          that.taskMapSize = response.data.taskMapSize;
          let workflowTask = response.data.workflowTaskVo;
          if (null != workflowTask) {
            that.timeLimitHour =
              undefined == workflowTask.timeLimitHour
                ? "0"
                : workflowTask.timeLimitHour;
            that.timeUnit =
              undefined == workflowTask.timeUnit ? "W" : workflowTask.timeUnit;
            that.timeLimit =
              undefined == workflowTask.timeLimit
                ? "0"
                : workflowTask.timeLimit;
            that.handleUserNames =
              "" == workflowTask.handleUserNames
                ? "无"
                : workflowTask.handleUserNames;
            that.handlePostNames =
              "" == workflowTask.handlePostNames
                ? "无"
                : workflowTask.handlePostNames;
            that.handleStatus = workflowTask.handleStatus;
          } else {
            //从环节表中获取数据
            getWorkflowStepByInstanceIdAndActivityId(data).then((resp) => {
              const step = resp.data;
              that.timeLimitHour =
                undefined == step.timeLimitHour ? "0" : step.timeLimitHour;
              that.timeUnit = undefined == step.timeUnit ? "W" : step.timeUnit;
              that.timeLimit =
                undefined == step.timeLimit ? "0" : step.timeLimit;
              that.handleUserNames =
                "" == step.handleUserNames ? "无" : step.handleUserNames;
              that.handlePostNames =
                "" == step.handlePostNames ? "无" : step.handlePostNames;
            });
          }
        });
      }
    },
  },
  mounted() {
    this.getWorkflowTaskVo();
  },
  watch: {
    //监视元素变化
    element: {
      deep: true,
      immediate: true,
      handler(newVal, oldVal) {
        if (newVal.type == "bpmn:UserTask") {
          const businessObject = newVal.businessObject;
          this.name = businessObject.name;
        }
      },
    },
  },
};
</script>
<style>
.handling {
  padding: 10px;
}
.handling-detail {
  padding: 10px;
  border: 1px solid #e1e5de;
  margin-bottom: 10px;
}
.handle-box{
  border-bottom: 1px solid rgba(51, 83, 30, 0.13);
  padding-bottom: 5px;
}
.first-handle{
  display: inline-block;
  padding: 0px 5px;
  height: 30px;
  line-height: 30px;
  margin-bottom: 5px;
  color: #fff;
  font-size: 14px;
  border-radius: 5px;
  background-color: #e6a23c;
}
.handling-detail .item span{
  font-weight: bold;
}

</style>

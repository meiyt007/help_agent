<template>
  <div class="user-task-props-readonly">
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
    <el-form-item label="办理人">
      {{handleUserNames}}
    </el-form-item>
    <el-form-item label="办理岗位">
      {{handlePostNames}}
    </el-form-item>
    <el-form-item label="环节办理类型">
      <span v-if="'1' == handleType">任意人员办理</span>
      <span v-if="'2' == handleType">全部人员办理</span>
    </el-form-item>
    <el-form-item label="人员设定类型">
      <span v-if="'1' == handleUserType">环节自定义</span>
      <span v-if="'2' == handleUserType">上一环节设置</span>
    </el-form-item>
    <el-form-item label="驳回环节">
      <span v-if="'' == rejectTaskId || null== rejectTaskId ">无</span>
      <span v-for="p in userTaskIdList" v-if="p.taskId == rejectTaskId">{{p.taskName}}</span>
    </el-form-item>
    <el-form-item label="退回环节">
      <span v-if="'' == fallbackTaskId || null== fallbackTaskId ">无</span>
      <span v-for="p in userTaskIdList" v-if="p.taskId == fallbackTaskId">{{p.taskName}}</span>
    </el-form-item>
    <el-form-item label="表单标识" v-show="false">{{formKey}}</el-form-item>
  </div>
</template>
<script>
  import {
    getWorkflowBussFlowStepByProcessDefIdAndActivityId,
  } from "@/api/workflow/bussinfo";
  // 用户任务属性组件
  export default {
    props: ["element",'params'],
    inject: ["bpmnModeler"],
    data() {
      return {
        // 共有
        id: this.element.id || "",
        name: "",
        formKey: "",
        timeUnit: "W",
        timeLimitHour: "0",
        operatingType: "0",
        handleUserType: "1",
        handleType: "1",
        handleUserOids: [],
        handleUserNames: [],
        handlePostOids: [],
        handlePostNames: [],
        rejectTaskId: "",
        fallbackTaskId: "",
        timeLimit: "0",
        assignee: "",
        //候选人
        candidateUsers: "",
        // 原子节点属性
        postOidsOptions: [],
        positions: [],
        priority: "",
        innerFlowOid: "没有受让人",
        //办理人
        userOptions: [],
        //办理岗位
        postOptions: [],
        userTaskIdList: [],
      };
    },
    methods: {
      getWorkflowStep(){
        //从环节表中获取数据
        let that = this;
        var data = { processDefId: this.params.processDefId, activityId: this.id };
        //从环节表中获取数据
        console.log(data)
        getWorkflowBussFlowStepByProcessDefIdAndActivityId(data).then((resp) => {
          const step = resp.data;
          that.linkOid = step.linkOid;
          that.timeLimitHour =
            undefined == step.timeLimitHour ? "0" : step.timeLimitHour;
          that.timeUnit = undefined == step.timeUnit ? "W" : step.timeUnit;
          that.timeLimit =
            undefined == step.timeLimit ? "0" : step.timeLimit;
          that.handleUserType =
            step.handleUserType != undefined
              ? step.handleUserType
              : "1";
          that.handleType =
            step.handleType != undefined
              ? step.handleType
              : "1";
          that.operatingType =
            step.operatingType != undefined
              ? step.operatingType
              : "0";
          that.handleUserNames =
            step.handleUserNames != undefined
              ? step.handleUserNames
              : '无';
          that.handlePostNames =
            step.handlePostNames != undefined
              ? step.handlePostNames
              : '无';
          that.innerFlowOid =
            step.innerFlowOid != undefined
              ? step.innerFlowOid
              : "";
          that.rejectTaskId =
            step.rejectTaskId != undefined
              ? step.rejectTaskId
              : "";
          that.fallbackTaskId =
            step.fallbackTaskId != undefined
              ? step.fallbackTaskId
              : "";
          that.name = step.name;
        });
      }
    },
    mounted() {
      const bpmnModeler = this.bpmnModeler();
      var that = this;
      const userBackTaskList = bpmnModeler
        .get("elementRegistry")
        .filter((item) => item.type === "bpmn:UserTask");
      that.userTaskIdList = [];
      for (let i = 0; i < userBackTaskList.length; i++) {
        const task = userBackTaskList[i];
        if (that.id == task.id) {
          break;
        }
        const taskInfo = {
          taskId: task.id,
          taskName: undefined == task.name ? task.businessObject.name : task.name,
        };
        that.userTaskIdList.push(taskInfo);
      }
    },
    watch: {
      //监视元素变化
      element: {
        deep: true,
        immediate: true,
        handler(newVal, oldVal) {
          if (newVal.type == "bpmn:UserTask") {
            const businessObject = newVal.businessObject;

            let handleUserOids =
              businessObject.$attrs.handleUserOids != undefined
                ? businessObject.$attrs.handleUserOids
                : '';
            let handlePostOids =
              businessObject.$attrs.handlePostOids != undefined
                ? businessObject.$attrs.handlePostOids
                : '';
            if(''==handleUserOids && ''==handlePostOids){
              //为空，可能是历史流程
              this.getWorkflowStep();
            }else{
              this.assignee = businessObject.assignee;
              this.candidateUsers = businessObject.candidateUsers;
              this.timeUnit =
                businessObject.$attrs.timeUnit != undefined
                  ? businessObject.$attrs.timeUnit
                  : "W";
              this.timeLimit =
                businessObject.$attrs.timeLimit != undefined
                  ? businessObject.$attrs.timeLimit
                  : "0";
              this.timeLimitHour =
                businessObject.$attrs.timeLimitHour != undefined
                  ? businessObject.$attrs.timeLimitHour
                  : "0";
              this.handleUserType =
                businessObject.$attrs.handleUserType != undefined
                  ? businessObject.$attrs.handleUserType
                  : "1";
              this.handleType =
                businessObject.$attrs.handleType != undefined
                  ? businessObject.$attrs.handleType
                  : "1";
              this.operatingType =
                businessObject.$attrs.operatingType != undefined
                  ? businessObject.$attrs.operatingType
                  : "0";
              this.handleUserNames =
                businessObject.$attrs.handleUserNames != undefined
                  ? businessObject.$attrs.handleUserNames
                  : '无';
              this.handlePostNames =
                businessObject.$attrs.handlePostNames != undefined
                  ? businessObject.$attrs.handlePostNames
                  : '无';
              this.innerFlowOid =
                businessObject.$attrs.innerFlowOid != undefined
                  ? businessObject.$attrs.innerFlowOid
                  : "";
              this.rejectTaskId =
                businessObject.$attrs.rejectTaskId != undefined
                  ? businessObject.$attrs.rejectTaskId
                  : "";
              this.fallbackTaskId =
                businessObject.$attrs.fallbackTaskId != undefined
                  ? businessObject.$attrs.fallbackTaskId
                  : "";
              this.name = businessObject.name;
              this.formKey = businessObject.get("formKey");
            }



          }
        },
      },
    },
  };
</script>
<style>
  .user-task-props-readonly .el-form-item--mini.el-form-item{
    border: 1px solid #dcdfe6;
    padding: 5px;
    box-sizing: border-box;
  }
  .user-task-props-readonly .el-form-item--mini .el-form-item__content {
    font-size: 12px;
  }
  .user-task-props-readonly .el-form-item--mini .el-form-item__label {
    line-height: 23px!important;
    font-size: 13px;
    color: #333;
    position: relative;
    padding-left: 8px!important;
  }
  .user-task-props-readonly .el-form-item--mini .el-form-item__label:after{
    position: absolute;
    content: '';
    width: 3px;
    height: 12px;
    background-color: #409eff;
    left: 0;
    top: 5px;
  }
</style>

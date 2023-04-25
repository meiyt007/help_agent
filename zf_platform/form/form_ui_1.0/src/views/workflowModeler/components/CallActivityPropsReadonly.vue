<template>
    <div class="user-task-props-readonly">
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
          @click="handleProcessView()"
        >查看流程图</el-button></span>
      </el-form-item>
      <!--内嵌流程图弹框-->
      <el-dialog v-dialog-drag :visible.sync="item.show" v-for="item in dialogViewOptions" @close="outerViewVisible"
                 :title="item.title" width="90%" append-to-body>
        <InnerViewer :params="params" @handleViewModelVisiable="outerViewVisible"></InnerViewer>
        <div slot="footer" class="dialog-footer">
          <el-button @click="outerViewVisible">关 闭</el-button>
        </div>
      </el-dialog>
    </div>

</template>
<script>
import {processView,getWorkflowBussFlowStepByInfoIdAndActivityId} from "@/api/workflow/bussinfo";
import InnerViewer from './InnerViewer'
export default {
    components: {
      InnerViewer
    },
    props:['element'],
    data(){
        return {
            id: this.element.id || '',
            name: '',
            documentation:'',
            calledElement: '',
            dialogViewOptions:[],
            positions:[],
            timeUnit: "W",
            timeLimit: "0",
            timeLimitHour: "0",
            innerFlowOid:'',
            innerFlowName:'',
            params :{}
        }
    },
    mounted() {

    },
    methods:{
      outerViewVisible(){
        this.dialogViewOptions.pop();
      },
      /** 查看流程图按钮操作 1*/
      handleProcessView() {
        let item = {
          show:true,
          title:'流程查看（'+this.innerFlowName+'）',
          dialogViewModelVisible:this.dialogViewModelVisible,
          params:this.params,
        };
        this.dialogViewOptions.push(item);
      }
    },
    watch:{
        //监控的元素的变化
        element:{
            deep: true,
            immediate: true,
            handler:function(newVal,oldVal){
                const businessObject = newVal.businessObject;
                this.name = businessObject.name;
                // documentation和userTaskNode存储的值一样
                this.calledElement = businessObject.get('calledElement');
                //this.documentation = businessObject.get('activiti:documentation');
                this.innerFlowOid =
                  businessObject.$attrs.innerFlowOid != undefined
                    ? businessObject.$attrs.innerFlowOid
                    : "";
                this.timeLimitHour =
                  undefined == businessObject.$attrs.timeLimitHour ? "0" : businessObject.$attrs.timeLimitHour;
                this.timeUnit = undefined == businessObject.$attrs.timeUnit ? "W" : businessObject.$attrs.timeUnit;
                this.timeLimit = undefined == businessObject.$attrs.timeLimit ? "0" : businessObject.$attrs.timeLimit;
                let infoOid = ''!=this.calledElement?this.calledElement.replace('Process_',''):'';
                if(undefined == this.params.bpmnXml && ''!=infoOid){
                    processView(infoOid).then(response => {
                      this.params.bpmnXml = response.data.bpmnXml;
                      this.params.modelId = response.data.modelId;
                      this.params.name = response.data.name;
                      this.innerFlowName = response.data.name;
                    });
                }
                let parentInfoOid = ''!=this. $parent.id?businessObject.$parent.id.replace('Process_',''):'';
                if(''!=parentInfoOid){
                  //从环节表中获取数据
                  let that = this ;
                  var data = { infoOid: parentInfoOid, activityId: businessObject.id };
                  getWorkflowBussFlowStepByInfoIdAndActivityId(data).then((resp) => {
                    const step = resp.data;
                    that.timeLimitHour =
                      undefined == step.timeLimitHour ? "0" : step.timeLimitHour;
                    that.timeUnit = undefined == step.timeUnit ? "W" : step.timeUnit;
                    that.timeLimit = undefined == step.timeLimit ? "0" : step.timeLimit;
                  });
                }
            }
        }
    }
}
</script>
<style scoped>
  .lookView{
    margin-left: 10px !important;
  }
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

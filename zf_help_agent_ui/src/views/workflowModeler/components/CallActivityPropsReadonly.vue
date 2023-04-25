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
import {processView} from "@/api/workflow/bussinfo";
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
        console.log(this.params)
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
                let infoOid = ''!=this.calledElement?this.calledElement.replace('Process_',''):'';
                if(undefined == this.params.bpmnXml && ''!=infoOid){
                    processView(infoOid).then(response => {
                      //console.log(response.data)
                      this.params.bpmnXml = response.data.bpmnXml;
                      this.params.modelId = response.data.modelId;
                      this.params.name = response.data.name;
                      this.innerFlowName = response.data.name;
                    });
                  }
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

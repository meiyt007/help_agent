<template>
    <div>
        <el-form-item label="编号" v-show="false">
            <el-input v-model= "id"></el-input>
        </el-form-item>
      <el-form-item label="环节名称" required>
        <el-select
          v-model="linkOid"
          @change="changeWorkflowLink"
          size="small"
          filterable
          placeholder="请选择环节名称"
        >
          <el-option
            v-for="p in workflowLinkList"
            :key="p.linkOid"
            :label="p.name"
            :value="p.linkOid"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="环节显示名称">
        <el-input v-model="name" placeholder="请输入环节显示名称"></el-input>
      </el-form-item>
      <el-form-item label="办理时限">
      <span style="display:block;margin-bottom:5px">
        <el-input class="input_modeler_left" placeholder="办理时限" v-model="timeLimit"></el-input>
        <el-radio-group v-model="timeUnit" class="input_modeler_right">
          <el-radio label="W">工作日</el-radio>
          <el-radio label="N">自然日</el-radio>
        </el-radio-group>
      </span>
        <el-input placeholder="小时" class="input_modeler_left" v-model="timeLimitHour"></el-input>&nbsp;&nbsp;小时
      </el-form-item>
        <!--<el-form-item label="内嵌流程" v-show="true">
            <el-input v-model="calledElement"></el-input>
        </el-form-item>-->
      <el-form-item label="内嵌流程" required>
        <treeselect
          class="treeselect"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          :disable-branch-nodes="true"
          v-model="innerFlowOid"
          @input="handleNodeClick"
          :options="innerWorkflowPositions"
          placeholder="请选择内嵌流程"
        />
      </el-form-item>
    </div>

</template>
<script>
const forEach = require('lodash/forEach'),
      find = require('lodash/find');
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  queryAllDistrictOrganBussInfoTree,
} from "@/api/workflow/bussinfo";
export default {
    props:['element'],
    components: { Treeselect },
    inject: ['bpmnModeler', "params"],
    data(){
        return {
            modeling: null,
            id: this.element.id || '',
            name: '',
            calledElement: '',
            documentation:'',
            workflowLinkList: [],
            timeUnit: "W",
            timeLimit: "0",
            timeLimitHour: "0",
            linkOid:'',
          innerWorkflowPositions:[],
          innerFlowOid: undefined,
        }
    },
    mounted() {
        const bpmnModeler = this.bpmnModeler();
        this.modeling = bpmnModeler.get('modeling');

    },
    created() {
      this.infoOid = this.params.infoOid;
      this.workflowLinkList = this.params.workflowLinkList;
      this.innerWorkflowPositions = this.params.districtOrganBussInfoTree;
      if (undefined == this.params.districtOrganBussInfoTree || this.params.districtOrganBussInfoTree.length == 0) {
        this.queryAllDistrictOrganBussInfo();
      }
    },
  methods:{
      changeWorkflowLink(oid) {
        const workflowLink = this.workflowLinkList.find(
          (item) => item.linkOid == oid
        );
        this.name = workflowLink.name;
      },
      //查询所有区划的组织机构、流程信息Tree
      queryAllDistrictOrganBussInfo() {
        queryAllDistrictOrganBussInfoTree().then((response) => {
          this.innerWorkflowPositions = response.data;
        });
      },
      // 内嵌流程节点单击事件
      handleNodeClick(data) {
        //let data = object.id;
        if (data.indexOf("FLOW-") == -1) {
          this.innerFlowOid = null;
          this.$set(this, "innerFlowOid", null);
          this.msgError("内嵌流程只能选择流程节点！");
          return;
        } else if(data.indexOf(this.infoOid) > -1){
          this.innerFlowOid = null;
          this.$set(this, "innerFlowOid", null);
          this.msgError("内嵌流程不能设置当前流程！");
          return;
        }else{
          this.innerFlowOid = data;
        }
      },
    },
    watch:{
        name: {
            handler(newVal, oldVal) {
                this.modeling.updateProperties(this.element,{
                    name: newVal
                });
            }
        },
        //监控的元素的变化
        element:{
            deep: true,
            immediate: true,
            handler:function(newVal,oldVal){
                const bpmnModeler = this.bpmnModeler();
                const modeling = bpmnModeler.get('modeling');
                const businessObject = newVal.businessObject;
                this.name = businessObject.name;
                this.calledElement = businessObject.get('calledElement') || '';
                this.documentation = businessObject.get('documentation') || '';
                this.innerFlowOid =
                businessObject.$attrs.innerFlowOid != undefined
                  ? businessObject.$attrs.innerFlowOid
                  : undefined;
                this.linkOid =
                businessObject.$attrs.linkOid != undefined
                  ? businessObject.$attrs.linkOid
                  : "";
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
                modeling.updateProperties(newVal,{
                    name: this.name,
                    calledElement: this.calledElement,
                });

            }
        },
        linkOid: {
          handler(newVal, oldVal) {
            this.modeling.updateProperties(this.element, { linkOid: newVal });
          },
        },
        innerFlowOid: {
          handler(newVal, oldVal) {
            if (newVal.indexOf("FLOW-") > -1) {
              let infoOid = null!=newVal?newVal.substring(newVal.lastIndexOf('-')+1,newVal.length):'';
              this.calledElement = 'Process_'+infoOid;
              this.modeling.updateProperties(this.element, { innerFlowOid: newVal });
            }
          },
        },
        calledElement: {
            handler(newVal, oldVal) {
                this.modeling.updateProperties(this.element,{'calledElement':newVal});
            }
        },
      timeUnit: {
        handler(newVal, oldVal) {
          newVal = null == newVal ? "W" : newVal;
          this.modeling.updateProperties(this.element, { timeUnit: newVal });
        },
      },
      timeLimit: {
        handler(newVal, oldVal) {
          var that = this;
          let numberReg = /^\d+$|^\d+[.]?\d+$/
          if (newVal !== '') {
            if (!numberReg.test(newVal)) {
              that.timeLimit ='0';
              that.msgError('请输入数字');
            } else {
              this.modeling.updateProperties(this.element, { timeLimit: newVal });
            }
          }

        },
      },
      timeLimitHour: {
        handler(newVal, oldVal) {
          var that = this;
          let numberReg = /^\d+$|^\d+[.]?\d+$/
          if (newVal !== '') {
            if (!numberReg.test(newVal)) {
              that.timeLimitHour ='0';
              that.msgError('请输入数字');
            } else {
              this.modeling.updateProperties(this.element, { timeLimitHour: newVal });
            }
          }
        },
      },


    }
}
</script>

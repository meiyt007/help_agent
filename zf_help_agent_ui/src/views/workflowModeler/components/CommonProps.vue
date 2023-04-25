<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="常规" name="first">
        <el-form-item label="编号" v-show="false">
          <el-input v-model="id" readonly></el-input>
        </el-form-item>
        <el-form-item label="流程名称" required>
          <el-input v-model="name"></el-input>
        </el-form-item>
       <!-- <el-form-item label="目标命名空间" v-show="true">
          <el-input v-model="process_namespace"></el-input>
        </el-form-item>-->
        <!--<el-form-item label="候选开始组">
          <el-input v-model="candidateStarterGroups"></el-input>
        </el-form-item>
        <el-form-item label="候选开始用户">
          <el-input v-model="candidateStarterUsers"></el-input>
        </el-form-item>-->
        <!--<el-form-item label="文档" v-show="true">
          <el-input type="textarea" v-model="documentation"></el-input>  &lt;!&ndash; documentation   ? &ndash;&gt;
        </el-form-item>-->
      </el-tab-pane>
      <!--<el-tab-pane label="监听" name="second">
        <el-form-item label="事件类型" >
          <el-select v-model="eventType" placeholder="请选择" multiple>
            <el-option label="开始" value="start"></el-option>
            <el-option label="结束" value="end"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监听器类型" v-show="isShow">
          <el-select v-model="listenerType" placeholder="请选择">
            <el-option label="java类" value="class"></el-option>
            <el-option label="表达式" value="expression"></el-option>
            <el-option label="代理表达式" value="delegateExpression"></el-option>
            <el-option label="脚本" value="script"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-bind:label="listenerValueLabel" v-show="isShow">
          <el-input v-model="listenerValue"></el-input>
        </el-form-item>
      </el-tab-pane>-->
    </el-tabs>

  </div>
</template>
<script>
  import bpmnHelper from '../js/helper/BpmnHelper';
  import elementHelper from '../js/helper/ElementHelper';
  export default {
    //依赖注入
    props: ['element'],
    inject: ['bpmnModeler', 'params'],
    data () {
      return {
        id: 'Process_'+this.params.modelId,
        name: this.params.name,
        //process_namespace:  '',
        documentation: '',
        activeName: 'first',
        candidateStarterGroups:'',
        candidateStarterUsers: '',
        listenerType: '',
        eventType: '',
        listenerValue: '',
        isShow: false,
        listenerValueLabel: 'Java类'

      }
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      changeName(name){
        this.name = name;
      }
    },
    created() {
      this.name = this.params.name;
    },
    watch: {
      //不给修改 无需监控
      eventType(newVal, oldVal){
        if(newVal) {
          this.isShow = true
        }
      },
      listenerType(newVal, oldVal){
        if(newVal === 'class') {
          this.listenerValueLabel = 'java类'
        } else if(newVal === 'expression') {
          this.listenerValueLabel = '表达式'
        } else if(newVal === 'delegateExpression') {
          this.listenerValueLabel = '代理表达式'
        }
      },
      listenerValue(newVal, oldVal){
        if(newVal) {
          const bpmnModeler = this.bpmnModeler();
          const bpmnFactory = bpmnModeler.get('bpmnFactory');
          let extensionElements = this.element.businessObject.get('extensionElements');
          if(!extensionElements) {
            extensionElements = elementHelper.createElement('bpmn:ExtensionElements', null, this.element, bpmnFactory);
          }
          const length = extensionElements.get('values').length;
          for (let i = 0; i < length; i++) {
            // 清除旧值
            extensionElements.get('values').pop();
          }
          this.eventType.forEach(evt => {
            const executionListener = elementHelper.createElement('activiti:ExecutionListener', null, this.element, bpmnFactory);
            executionListener.$attrs['event'] = evt;
            executionListener.$attrs[this.listenerType] = newVal;
            extensionElements.get('values').push(executionListener)

          });
          const modeling = bpmnModeler.get('modeling');
          modeling.updateProperties(this.element, {
            extensionElements: extensionElements
          })
        }
      },
    }
  }

</script>

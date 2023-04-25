<template>
  <div>
    <h3 class="title">流程查看</h3>
    <el-container :style="'height:'+fullHeight+'px;'">
      <el-aside width="80%" style="border: 1px solid #DCDFE6;border-right:none">
        <div ref="canvas" style="width: 100%;height: 100%"></div>
      </el-aside>
      <el-main style="border: 1px solid #DCDFE6;background-color:#FAFAFA">
        <el-form label-width="auto" size="mini" label-position="top">
          <!-- 动态显示属性面板 -->
          <component :is="propsComponent" :element="element" :params="params" ref="commonChild" :key="key"></component>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { CustomViewer } from "../js/customBpmn";
import bpmnHelper from "../js/helper/BpmnHelper";
import CommonPropsReadonly from "./CommonPropsReadonly.vue";
import ProcessProps from "./ProcessProps.vue";
import UserTaskPropsReadonly from "./UserTaskPropsReadonly.vue";
import {processView,flowViewByProcessDefId} from "@/api/workflow/bussinfo";
export default {
  props: ["params"],
  components: {
    CommonPropsReadonly,
    ProcessProps,
    UserTaskPropsReadonly,
  },
  provide: function () {
    return {
      bpmnModeler: this.getBpmnModeler,
    };
  },
  data() {
    return {
      propsComponent: "CommonPropsReadonly",
      bpmnModeler: null,
      canvas: null,
      element: null,
      key: "1",
      defaultData: {
        "bpmn:StartEvent": "开始",
        "bpmn:EndEvent": "结束",
        "bpmn:IntermediateThrowEvent": "终止",
      },
      fullHeight: document.documentElement.clientHeight - 45,
    };
  },
  watch: {
    fullHeight(val) {
      //监控浏览器高度变化
      if (!this.timer) {
        this.fullHeight = val;
        this.timer = true;
        let that = this;
        setTimeout(function () {
          that.timer = false;
        }, 400);
      }
    },
  },
  methods: {
    //初始化BpmnModeler
    initBpmnModeler() {
      let canvas = this.$refs.canvas;
      // var customTranslateModule = {
      //     translate: [ 'value', customTranslate ]
      // };
      this.bpmnModeler = new CustomViewer({
        container: canvas,
      });
      this.importBpmnXml();
    },
    get_bodyHeight() {
      //动态获取浏览器高度
      const that = this;
      window.onresize = () => {
        return (() => {
          window.fullHeight = document.documentElement.clientHeight;
          that.fullHeight = window.fullHeight - 45;
        })();
      };
    },
    //导入xml文档
    importBpmnXml(){
      const that = this;
      let infoOid = that.params.infoOid;
      if(undefined !=infoOid && ''!=infoOid){
        processView(infoOid).then(response => {
          that.params.bpmnXml = response.data.bpmnXml;
          that.params.modelId = response.data.modelId;
          that.params.name = response.data.name;
          this.$refs.commonChild.changeName(that.params.name);
          this.bpmnModelerImportBpmnXml();
        });
      }else{
        let processDefId = that.params.processDefId;
        flowViewByProcessDefId(processDefId).then(response => {
          that.params.bpmnXml = response.data.bpmnXml;
          that.params.name = response.data.name;
          this.$refs.commonChild.changeName(that.params.name);
          this.bpmnModelerImportBpmnXml();
        });
      }

    },
    bpmnModelerImportBpmnXml(){
      let that = this ;
      let bpmnXml = bpmnHelper.getBpmnTempate(that.params.infoOid);
      if(that.params.bpmnXml) {
        bpmnXml = that.params.bpmnXml
      }
      that.bpmnModeler.importXML(bpmnXml,function(err){
        if(err){
          console.log('bpmn文档导入失败');
        } else {
          // 绑定监听事件
          that.success();
          //设置颜色
          that.setBackColor();
        }
      });
    },
    getBpmnModeler() {
      return this.bpmnModeler;
    },
    success() {
      this.addModelerListener();
      this.addEventBusListener();
    },
    addModelerListener() {
      // 监听 modeler
      //const bpmnjs = this.bpmnModeler
      //const that = this
      // 以下代码为：为节点注册鼠标悬浮事件
      // const eventBus = this.bpmnModeler.get('eventBus');
      //const overlays = this.bpmnModeler.get('overlays');
      /*eventBus.on('element.hover', (e) => {
          const $overlayHtml = '<div class="tipBox">你好，我是悬浮框里的内容</div>';
          overlays.add(e.element.id, {
            position: {top: e.element.height, left: 0},
            html: $overlayHtml
          });
        });
        eventBus.on('element.out', () => {
          overlays.clear();
        });*/
      //  注册悬浮事件结束
    },
    addEventBusListener() {
      // 监听 element
      let that = this;
      const eventBus = this.bpmnModeler.get("eventBus");
      const eventTypes = [
        "element.click",
        "element.changed",
        "selection.changed",
      ];
      eventTypes.forEach(function (eventType) {
        eventBus.on(eventType, function (e) {
          if (eventType === "element.changed") {
            that.elementChanged(e);
          } else if (eventType === "element.click") {
            if ("bpmn:UserTask" == e.element.type) {
              //console.log('点击了element');
              if (!e || e.element.type == "bpmn:Process") {
                that.key = "1";
                that.propsComponent = "CommonPropsReadonly";
                that.element = e.element;
              } else {
                // 展示新增图形的属性
                that.key = e.element.id;
                that.propsComponent = bpmnHelper.getComponentByEleType(
                  e.element.type + "Readonly"
                );
                that.element = e.element;
              }
            } else if ("bpmn:CallActivity" == e.element.type) {
              // 展示新增图形的属性
              that.key = e.element.id;
              that.propsComponent = bpmnHelper.getComponentByEleType(
                e.element.type + "Readonly"
              );
              that.element = e.element;
            }
          }
        });
      });
    },
    //设置驳回和退回线的颜色
    setBackColor() {
      const canvas = this.bpmnModeler.get("canvas");
      // 获取到所有的线节点
      const elementRegistry = this.bpmnModeler.get("elementRegistry");
      const sequenceFlowList = elementRegistry.filter(
        (reg) => reg.type == "bpmn:SequenceFlow"
      );
      const bhSequenceFlowList = sequenceFlowList.filter(
        (reg) => reg.businessObject.$attrs.flowClass == "bh"
      );
      if (bhSequenceFlowList.length > 0) {
        bhSequenceFlowList.forEach((key) => {
          canvas.addMarker(key.id, "highlight-bh");
        });
      }
      const thSequenceFlowList = sequenceFlowList.filter(
        (reg) => reg.businessObject.$attrs.flowClass == "th"
      );
      if (thSequenceFlowList.length > 0) {
        thSequenceFlowList.forEach((key) => {
          canvas.addMarker(key.id, "highlight-th");
        });
      }
    },
    getShape(id) {
      var elementRegistry = this.bpmnModeler.get("elementRegistry");
      return elementRegistry.get(id);
    },
    setDefaultProperties() {
      //console.log('setDefaultProperties');
      const that = this;
      const { element } = that;
      if (element) {
        // 这里可以拿到当前点击的节点的所有属性
        const { type, businessObject } = element;
        const { name } = businessObject;
        if (that.verifyIsEvent(type)) {
          const eventType = businessObject.eventDefinitions
            ? businessObject.eventDefinitions[0]["$type"]
            : "";
          //console.log(eventType);
        } else if (this.verifyIsTask(type)) {
          const taskType = type;
          //console.log(taskType);
        }
        that.element["name"] = name || that.defaultData[element.type];
      }
    },
    verifyIsEvent(type) {
      return type.includes("Event");
    },
    verifyIsTask(type) {
      return type.includes("Task");
    },
  },
  mounted() {
    //初始化模型编辑器
    this.initBpmnModeler();
    this.get_bodyHeight();
    var a = document.querySelector(".bjs-powered-by");
    try {
      a.childNodes[0].remove();
      a.remove();
    }catch (e) {
      //解决ie兼容模式下的问题
    }

  },
};
</script>

<style>
.el-form-item__label {
  font-size: 13px;
}
.el-main {
  padding: 10px;
}
.el-input--mini .el-input__inner {
  height: 23px;
  line-height: 23px;
}
.el-form--label-top .el-form-item__label {
  padding: 0 0 0px;
}
.el-form-item--mini .el-form-item__content,
.el-form-item--mini .el-form-item__label {
  line-height: 23px;
}
.el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 3px;
}
.el-checkbox__label {
  padding-left: 10px;
  line-height: 19px;
  font-size: 13px;
}
/* .el-input__inner {
  padding: 0 5px;
} */
.el-textarea__inner {
  padding: 5px 5px;
}
.el-input--mini .el-input__icon {
  line-height: 23px;
}
.tipBox {
  width: 200px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  padding: 12px;
}
svg g:hover {
  background-color: #e0f0ff !important;
}

/*驳回*/
.highlight-bh .djs-visual > :nth-child(1) {
  stroke: red !important;
  width: 200px !important;
  height: 200px !important;
}

/*退回*/
.highlight-th .djs-visual > :nth-child(1) {
  stroke: #409eff !important;
  width: 200px !important;
  height: 200px !important;
}
.el-form-item--mini .el-form-item__label {
  position: relative;
}
.el-form-item--mini .el-form-item__label:before {
  right: -10px;
  position: absolute;
}
</style>
<style scoped>
.title {
  height: 45px;
  line-height: 45px;
  font-size: 16px;
  margin: 0;
  font-weight: normal;
  padding-left: 10px;
  text-align: left;
  color: #333;
}
</style>

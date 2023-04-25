<template>
  <div class="flow-viewer">
    <h3 class="title">流程查看</h3>
    <el-container :style="'height:'+fullHeight+'px;'">
      <el-aside
        width="80%"
        style="border: 1px solid #DCDFE6;border-right:none;margin-bottom:0px;overflow-y:hidden"
      >
        <div class="tag-group">
          <el-tag type effect="plain">等待办理</el-tag>
          <el-tag type="success" effect="dark">已完成</el-tag>
          <el-tag type="warning" effect="dark">正在办理</el-tag>
          <el-tag type="info" effect="dark">跳过/中止/审核不通过</el-tag>
          <el-tag type="danger" effect="dark">超期</el-tag>
          <el-tag color="#afc0e2" effect="dark">暂停</el-tag>
          <el-tag color="#7a7369" effect="dark">驳回/退回</el-tag>
        </div>
        <div ref="canvas" style="width: 100%;height: 100%"></div>
      </el-aside>

      <el-main style="border: 1px solid #DCDFE6;background-color:#FAFAFA;padding:10px">
        <el-form label-width="auto" size="mini" label-position="top">
          <!-- 动态显示属性面板 -->
          <component
            ref="commonChild"
            :is="propsComponent"
            :params="params"
            :element="element"
            :flowName="flowName"
            :key="key"
          ></component>
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
  import UserTaskPropsView from "./UserTaskPropsView.vue";
  import { viewFlow } from "@/api/workflow/bussinfo";
  const forEach = require("lodash/forEach");

  export default {
    props: ["params"],
    components: {
      CommonPropsReadonly,
      ProcessProps,
      UserTaskPropsView,
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
        modeling: null,
        canvas: null,
        element: null,
        executedFlowList: [],
        //跳过的环节id
        skipFlowList: [],
        havingList: [],
        workflowTaskVoList: [],
        //环节配置
        stepList: [],
        processDefId: "",
        bpmnXml: "",
        flowName: "",
        key: "1",
        defaultData: {
          "bpmn:StartEvent": "交易开始",
          "bpmn:EndEvent": "交易完成",
          "bpmn:IntermediateThrowEvent": "交易终止",
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
      //根据流程实例ID获取流程图已办的节点
      queryHighLighted() {
        const that = this;
        viewFlow(this.params).then((response) => {
          if(undefined == response.data){
            that.msgError('没查询到流程图信息');
          }
          that.flowName = response.data.flowName;
          this.$refs.commonChild.changeName(that.flowName);
          that.bpmnXml = response.data.bpmnXml;
          that.executedFlowList = response.data.allFlow;
          that.havingList = response.data.havingList;
          that.workflowTaskVoList = response.data.workflowTaskVoList;
          that.processDefId = response.data.processDefId;
          //初始化模型编辑器
          that.initBpmnModeler();
        });
      },
      setColor() {
        var that = this;
        // access viewer components
        const canvas = this.bpmnModeler.get("canvas");
        // 获取到全部节点
        const allShapes = this.bpmnModeler.get("elementRegistry").getAll();
        //循环节点添加颜色
        allShapes.forEach((element) => {
          const shapeId = element.businessObject.id;
          //console.log(shapeId+"===="+element.businessObject.name+"===="+element.businessObject.$type)
          //正在办理的环节
          const havingNode = that.havingList.find((h) => h === shapeId);
          //只处理用户任务
          if ("bpmn:UserTask" == element.businessObject.$type || "bpmn:CallActivity" == element.businessObject.$type) {
            const workflowTaskArr = that.workflowTaskVoList.filter(
              (d) => d.activityId == shapeId
            );
            if (workflowTaskArr.length == 1) {
              workflowTaskArr.forEach((havingTask, index) => {
                if (
                  havingTask.handleStatus == "2" ||
                  havingTask.handleStatus == "4"
                ) {
                  //先移除已有的
                  let removeArr = ["3", "5", "6", "7"];
                  removeArr.forEach((item) => {
                    canvas.removeMarker(shapeId, "highlight-" + item);
                  });
                  //正在办理的
                  var i = 0;
                  this.timer = setInterval(() => {
                    i++;
                    //定时器的回调函数中需要注意 this 指向
                    if (i % 2) {
                      canvas.addMarker(
                        shapeId,
                        "highlight-" + havingTask.handleStatus
                      );
                      canvas.removeMarker(shapeId, "havingstroke");
                    } else {
                      canvas.addMarker(shapeId, "havingstroke");
                      canvas.removeMarker(
                        shapeId,
                        "highlight-" + havingTask.handleStatus
                      );
                    }
                  }, 500);
                } else {
                  canvas.addMarker(
                    shapeId,
                    "highlight-" + havingTask.handleStatus
                  );
                }
              });
            }else if (workflowTaskArr.length > 1) {
              //创建map开始
              //workflowTaskArr.sort()
              let taskMap = new Map();
              let taskDateArr = [];
              workflowTaskArr.forEach((task, index) => {
                let startDateStr =  task.startDate.substr(0,16);
                if(taskDateArr.indexOf(startDateStr) == -1){
                  taskDateArr.push(startDateStr);
                }
              });
              taskDateArr.forEach((dateArr, index) => {
                let newTaskArr = [];
                workflowTaskArr.forEach((task, index) => {
                  let startDateStr =  task.startDate.substr(0,16);
                  if(dateArr == startDateStr){
                    newTaskArr.push(task);
                  }
                });
                taskMap.set(dateArr,newTaskArr);
              });
              let lastTaskArr = taskMap.get(taskDateArr[taskDateArr.length-1]);
              //创建map 结束
              //全部办理的情况
              let qbblHaving = this.isContained(lastTaskArr,that.havingList);
              let runingTask = lastTaskArr[lastTaskArr.length-1];
              //let noPassTask = workflowTaskArr.find(item => item.handleStatus == '10');
              let bhthTask = lastTaskArr.find(item => item.handleStatus == '9');
              let errorTaskArr = lastTaskArr.filter(item => item.handleStatus == '10' || item.handleStatus == '6' || item.handleStatus == '8');
              let newErrorTask = errorTaskArr.sort(this.sortData);
              let errorTask = null;
              if(newErrorTask.length>0){
                 errorTask = newErrorTask[0];
              }
              if(qbblHaving){
                //正在办理的
                var i = 0;
                this.timer = setInterval(() => {
                  i++;
                  //定时器的回调函数中需要注意 this 指向
                  if (i % 2) {
                    canvas.addMarker(
                      shapeId,
                      "highlight-"+qbblHaving.handleStatus
                    );
                    canvas.removeMarker(shapeId, "havingstroke");
                  } else {
                    canvas.addMarker(shapeId, "havingstroke");
                    canvas.removeMarker(
                      shapeId,
                      "highlight-"+qbblHaving.handleStatus
                    );
                  }
                }, 500);
              }else if(undefined != errorTask){
                canvas.addMarker(shapeId, "highlight-"+errorTask.handleStatus);
              }else if(undefined != bhthTask){
                canvas.addMarker(shapeId, "highlight-"+bhthTask.handleStatus);
              }else if(undefined != runingTask && (runingTask.handleStatus == '2' || qbblHaving)){
                //正在办理的
                var i = 0;
                this.timer = setInterval(() => {
                  i++;
                  //定时器的回调函数中需要注意 this 指向
                  if (i % 2) {
                    canvas.addMarker(
                      shapeId,
                      "highlight-"+qbblHaving.handleStatus
                    );
                    canvas.removeMarker(shapeId, "havingstroke");
                  } else {
                    canvas.addMarker(shapeId, "havingstroke");
                    canvas.removeMarker(
                      shapeId,
                      "highlight-"+qbblHaving.handleStatus
                    );
                  }
                }, 500);
              }else{
                canvas.addMarker(
                  shapeId,
                  "highlight-"+runingTask.handleStatus
                );
              }
            }
          } else {
            if (undefined == havingNode) {
              const shapeNode = that.executedFlowList.find((d) => d === shapeId);
              //已办理
              if (undefined != shapeNode) {
                if (element.businessObject.$type != "bpmn:Group") {
                  if (element.businessObject.$type == "bpmn:SequenceFlow") {
                    canvas.addMarker(shapeId, "highlight-21");
                  } else {
                    canvas.addMarker(shapeId, "highlight-3");
                  }
                }
              } else {
                //未办理 havingdashed
                if (element.businessObject.$type == "bpmn:SequenceFlow") {
                  canvas.addMarker(shapeId, "havingdashed");
                }
              }
            }
          }
        });
        //设置驳回线和退回线
        const flows = allShapes.filter((d) => d.type == "bpmn:SequenceFlow");
        flows.forEach((item) => {
          //驳回线
          if (undefined != item && item.id.indexOf("Flow_REJECT_") > -1) {
            canvas.addMarker(item.id, "highlight-bh");
          }
          //退回线
          if (undefined != item && item.id.indexOf("Flow_FALLBACK_") > -1) {
            canvas.addMarker(item.id, "highlight-th");
          }
        });
      },
      sortData(a, b) {
        return Date.parse(b.endTime) - Date.parse(a.endTime);
      },
      isContained(aa, bb) {
        if(!(aa instanceof Array) || !(bb instanceof Array)) {
          return false;
        }
        if(bb.length == 0){
          return false;
        }
        var flag = null;
        for (var i = 0; i < bb.length; i++) {
          for(var j = 0; j < aa.length; j++){
            if(aa[j].activityId == bb[i] && (aa[j].handleStatus == 2 || aa[j].handleStatus == 4)){
              flag = aa[j];
              break;
            }
          }
          if(flag){
            return flag;
          }
        }
        return flag;
      },
      endExclusiveGateway(sourNode, canvas, allShapes) {
        if (
          undefined != sourNode &&
          undefined != sourNode.target &&
          sourNode.target.type == "bpmn:ExclusiveGateway"
        ) {
          canvas.addMarker(sourNode.id, "highlight-26");
          //结束标识
          return false;
        } else if (undefined != sourNode) {
          //debugger
          //设置为跳过
          if (sourNode.type == "bpmn:SequenceFlow") {
            canvas.addMarker(sourNode.id, "highlight-26");
          } else {
            canvas.addMarker(sourNode.id, "highlight-6");
            this.skipFlowList.push(sourNode.id);
          }
          //将线右连接置灰
          if (sourNode.target.type == "bpmn:SequenceFlow") {
            canvas.addMarker(sourNode.target.id, "highlight-26");
          } else {
            this.skipFlowList.push(sourNode.target.id);
            canvas.addMarker(sourNode.target.id, "highlight-6");
          }
          const workflowTaskArr = allShapes.filter(
            (d) => d.businessObject.$type == "bpmn:SequenceFlow"
          );
          //console.log(workflowTaskArr)
          const nextNodes = workflowTaskArr.filter(
            (d) => undefined != d.source && d.source.id == sourNode.target.id
          );
          for (var i = 0; i < nextNodes.length; i++) {
            this.endExclusiveGateway(nextNodes[i], canvas, allShapes);
          }
        }
      },
      tipMessage(elementId, $overlayHtml) {
        // 以下代码为：为节点注册鼠标悬浮事件
        const overlays = this.bpmnModeler.get("overlays");
        const eventBus = this.bpmnModeler.get("eventBus");
        const tipHtml =
          '<div ref="tipHtml" v-show="false">' + $overlayHtml + "</div>";
        eventBus.on("element.hover", (e) => {
          overlays.add(elementId, {
            position: { top: e.element.height, left: 0 },
            html: tipHtml,
          });
        });
        eventBus.on("element.out", () => {
          overlays.clear();
        });
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
      importBpmnXml() {
        const that = this;
        let bpmnXml = that.bpmnXml;
        that.bpmnModeler.importXML(bpmnXml, function (err) {
          if (err) {
            console.log("bpmn文档导入失败");
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
        //this.addModelerListener()
        this.addEventBusListener();
        this.setColor();
        var a = document.querySelector(".bjs-powered-by");
        try {
          a.childNodes[0].remove();
        }catch (e) {
          //解决ie兼容模式下的问题
        }

      },
      addModelerListener() {
        // 监听 modeler
        // const that = this
        // 以下代码为：为节点注册鼠标悬浮事件
        const eventBus = this.bpmnModeler.get("eventBus");
        const overlays = this.bpmnModeler.get("overlays");
        /*const $overlayHtml =this.$refs.boxCard.$el.innerHTML;
          console.log($overlayHtml)
          //this.$el.boxCard;
          eventBus.on('element.hover', (e) => {
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
              if ("bpmn:UserTask" == e.element.type || 'bpmn:CallActivity' == e.element.type) {
                const skipFlow = that.skipFlowList.find((d) => d == e.element.id);
                if (undefined == skipFlow) {
                  // 展示新增图形的属性
                  that.key = e.element.id;
                  that.propsComponent = bpmnHelper.getComponentByEleType(
                    e.element.type + "View"
                  );
                  that.element = e.element;
                }
              } else if ("bpmn:Process" == e.element.type) {
                /*that.key = "1";
                that.propsComponent = "CommonPropsReadonly";
                that.element = e.element;*/
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
        console.log("setDefaultProperties");
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
      this.get_bodyHeight();
      this.queryHighLighted();
      /*var a = document.querySelector(".bjs-powered-by");
        a.childNodes[0].remove();*/
    },
  };
</script>

<style lang="less">
  .flow-viewer {
    .el-form-item__label {
      font-size: 13px;
    }
    .el-main {
      padding: 10px !important;
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
    .el-input__inner {
      padding: 0 5px;
    }
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
    // .djs-container {
    //   --blue-darken-48: inherit!important;
    //   --blue-base-65: inherit!important
    // }
    /*1.等待办理*/
    /*.highlight-1 .djs-visual > :nth-child(1) {
        stroke: #FFFFFF !important;
        fill:  #FFFFFF !important;
      }*/
    /*2.正在办理 节点*/
    .highlight-2 .djs-visual > :nth-child(1) {
      stroke: #ffba00 !important;
      fill: #ffba00 !important;
    }
    /*3.按时办理节点*/
    .highlight-3 .djs-visual > :nth-child(1) {
      stroke: #13ce66 !important;
      fill: #13ce66 !important;
    }
    /*4.超期未办理*/
    .highlight-4 .djs-visual > :nth-child(1) {
      stroke: #f56c6c !important;
      fill: #f56c6c !important;
    }
    /*5.超期办理*/
    .highlight-5 .djs-visual > :nth-child(1) {
      stroke: #f56c6c !important;
      fill: #f56c6c !important;
    }
    /*6跳过 */
    .highlight-6 .djs-visual > :nth-child(1) {
      stroke: #909399 !important;
      fill: #909399 !important;
    }
    /*7.暂停*/
    .highlight-7 .djs-visual > :nth-child(1) {
      stroke: #afc0e2 !important;
      fill: #afc0e2 !important;
    }
    /* （1.等待办理 2.正在办理 3.按时办理 4.超期未办理  5.超期办理 6跳过 7.暂停 8中止流程,9 驳回退回 10审核不通过） */
    /*8.中止流程*/
    .highlight-8 .djs-visual > :nth-child(1) {
      stroke: #909399 !important;
      fill: #909399 !important;
    }
    /*9.驳回、退回*/
    .highlight-9 .djs-visual > :nth-child(1) {
      stroke: #7a7369 !important;
      fill: #7a7369 !important;
    }
    /*10.审核不通过*/
    .highlight-10 .djs-visual > :nth-child(1) {
      stroke: #909399 !important;
      fill: #909399 !important;
    }
    /* 连线 */
    /*已办理*/
    .highlight-21 .djs-visual > :nth-child(1) {
      stroke: #13ce66 !important;
    }
    /*正在办理*/
    .highlight-22 .djs-visual > :nth-child(1) {
      stroke: #ffba00 !important;
    }
    /*驳回*/
    .highlight-23 .djs-visual > :nth-child(1) {
      stroke: #f56c6c !important;
      stroke-dasharray : 0,0;
    }
    /*6跳过 */
    .highlight-26 .djs-visual > :nth-child(1) {
      stroke: #909399 !important;
    }
    .havingstroke .djs-visual > :nth-child(1) {
      stroke: #b4d75b !important;
    }
    .havingdashed .djs-visual > :nth-child(1) {
      stroke: #999797 !important;
      /*stroke-dasharray : 0,0;*/
    }

    .el-tag {
      margin-right: 10px;
    }

    /*驳回*/
    .highlight-bh .djs-visual > :nth-child(1) {
      stroke: red !important;
      stroke-dasharray : 0,0;
    }
    /*退回*/
    .highlight-th .djs-visual > :nth-child(1) {
      stroke: #409eff !important;
      stroke-dasharray : 0,0;
    }
    .el-form-item--mini.el-form-item {
      border: 1px solid #dcdfe6;
      padding: 5px;
      box-sizing: border-box;
    }
    .el-form-item--mini .el-form-item__content {
      font-size: 12px;
    }
    .el-form-item--mini .el-form-item__label {
      line-height: 23px !important;
      font-size: 13px;
      color: #333;
      position: relative;
      padding-left: 8px !important;
    }
    .el-form-item--mini .el-form-item__label:after {
      position: absolute;
      content: "";
      width: 3px;
      height: 12px;
      background-color: #409eff;
      left: 0;
      top: 5px;
    }
    .el-form--label-top .el-form-item__label {
      padding: 0px 0px 0px 10px !important;
    }
    .djs-group .djs-element {
      cursor: pointer;
    }

    .el-form-item--mini.el-form-item {
      margin-bottom: 10px !important;
    }
    .title{
      height: 45px;
      line-height: 45px;
      font-size: 16px;
      margin: 0;
      font-weight: normal;
      padding-left: 10px
    }
    .el-tag--dark{
      border-color: #fff !important;
    }
  }
</style>
<style>
.djs-container {
  --blue-darken-48: inherit!important;
  --blue-base-65: inherit!important
}
</style>
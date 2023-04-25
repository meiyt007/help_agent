<template>
  <div>
    <h3 class="title">流程编辑（{{params.name}}）</h3>
    <el-container :style="'height:'+fullHeight+'px;'">
      <el-aside width="80%" style="border: 1px solid #DCDFE6;border-right:none">
        <div class="modelerBar">
          <el-upload
            action
            accept=".bpmn"
            :on-change="onUploadChange"
            :auto-upload="false"
            :show-file-list="false"
          >
            <el-button title="导入本地流程">导入流程</el-button>
          </el-upload>
        </div>
        <div class="modelerBar">
          <el-button title="将流程保存到本地" v-on:click="watchXML">保存流程</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="撤销" v-on:click="handlerUndo">撤销</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="恢复" v-on:click="handlerRedo">恢复</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="放大" v-on:click="handlerZoom(0.1)">放大</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="缩小" v-on:click="handlerZoom(-0.1)">缩小</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="还原" v-on:click="handlerZoom(0)">还原</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="清空流程" v-on:click="clearAllModeler">清空流程</el-button>
        </div>
        <div class="modelerBar">
          <el-button title="清空链接" v-on:click="clearModeler">清空链接</el-button>
        </div>

        <div class="modelerBarRight">
          <el-button @click="handleCloseClick">关 闭</el-button>
        </div>
        <div class="modelerBarRight">
          <el-button class="whitebg" type="primary" @click="handleOk">保 存</el-button>
        </div>
        <div class="modelerBarRight">
          <el-button class="whitebg" title="校验" type="danger" v-on:click="checkBpmnEditXML">校 验</el-button>
        </div>
        <div ref="canvas" class="containers" styl e="width: 100%;height: 100%"></div>
      </el-aside>
      <el-main style="border: 1px solid #DCDFE6;background-color:#FAFAFA">
        <el-form label-width="auto" size="mini" label-position="top">
          <!-- 动态显示属性面板 -->
          <component :is="propsComponent" :element="element" ref="commonChild" :key="key"></component>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import BpmnModeler from "bpmn-js/lib/Modeler";
  import "bpmn-js/dist/assets/diagram-js.css";
  import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";
  import bpmnHelper from "../js/helper/BpmnHelper";
  import CommonProps from "./CommonProps.vue";
  import ProcessProps from "./ProcessProps.vue";
  import StartEventProps from "./StartEventProps.vue";
  import EndEventProps from "./EndEventProps.vue";
  import IntermediateThrowEventProps from "./IntermediateThrowEventProps.vue";
  import ExclusiveGatewayProps from "./ExclusiveGatewayProps.vue";
  import ParallelGatewayProps from "./ParallelGatewayProps.vue";
  import InclusiveGatewayProps from "./InclusiveGatewayProps.vue";
  import UserTaskProps from "./UserTaskProps.vue";
  import SequenceFlowProps from "./SequenceFlowProps.vue";
  import customTranslate from "../js/customTranslate/customTranslate";
  import CallActivityProps from "./CallActivityProps.vue";
  import customControlsModule from "../js/customControls";
  import {processView} from "@/api/workflow/bussinfo";
  import BpmData from "./BpmData";
  import {
    saveModelBpmn,
    queryAllDistrictOrganUserTree,
    queryAllDistrictOrganPostTree,
    queryAllDistrictOrganBussInfoTree,
    queryWorkflowLinkListByInfoOid,
  } from "@/api/workflow/bussinfo";
  const forEach = require("lodash/forEach");

  export default {
    components: {
      CommonProps,
      ProcessProps,
      StartEventProps,
      EndEventProps,
      IntermediateThrowEventProps,
      ExclusiveGatewayProps,
      ParallelGatewayProps,
      InclusiveGatewayProps,
      UserTaskProps,
      SequenceFlowProps,
      CallActivityProps
    },
    props: ["params"],
    provide: function () {
      return {
        bpmnModeler: this.getBpmnModeler,
        params: this.params,
      };
    },
    data() {
      return {
        propsComponent: "CommonProps",
        bpmData: new BpmData(),
        bpmnModeler: null,
        canvas: null,
        element: null,
        scale: 1,
        key: "1",
        timeLimit:'0',
        timeUnit:'W',
        defaultData: {
          "bpmn:StartEvent": "开始",
          "bpmn:EndEvent": "结束",
          "bpmn:IntermediateThrowEvent": "终止",
        },
        fullHeight: document.documentElement.clientHeight - 70,
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
      onUploadChange(file) {
        var that = this;
        const isBpmn = file.name.endsWith(".bpmn");
        if (!isBpmn) {
          this.$message.error("上传文件只能是bpmn格式!");
          return false;
        }
        var reader = new FileReader();
        //reader.readAsDataURL(file.raw);
        reader.readAsText(file.raw, "utf-8");
        reader.onload = function (e) {
          that.params.bpmnXml = this.result;
          //初始化
          let bpmnXml = bpmnHelper.getBpmnTempate(that.params.modelId);
          if (undefined != that.params.bpmnXml && "" != that.params.bpmnXml) {
            bpmnXml = that.params.bpmnXml;
          }
          that.bpmnModeler.importXML(bpmnXml, function (err) {
            if (err) {
              that.msgError("流程导入失败！");
              let bpmnXmlErr = bpmnHelper.getBpmnTempate(that.params.modelId);
              that.bpmnModeler.importXML(bpmnXmlErr, function (err) {});
            } else {
              // 绑定监听事件
              that.msgSuccess("流程导入成功！");
            }
          });
        };
      },
      clearModelerFlow() {
        var that = this;
        let bpmnXml = bpmnHelper.getBpmnTempate(this.params.infoOid);
        that.bpmnModeler.importXML(bpmnXml, function (err) {
          if (err) {
            that.msgError("清空流程图失败！");
          } else {
            // 绑定监听事件
            that.msgSuccess("清空流程图成功！");
          }
        });
      },
      //清空所有的
      clearAllModeler() {
        var that = this;
        this.$confirm("是否确认清空所有流程图?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            that.clearModelerFlow();
          })
          .catch(function () {});
      },
      //清空链接
      clearModeler() {
        var that = this;
        this.$confirm("是否确认清空所有链接?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            const elementRegistry = that.bpmnModeler.get("elementRegistry");
            const modeling = that.bpmnModeler.get("modeling");
            const processArr = elementRegistry.getAll();
            processArr.forEach(function (reg, index) {
              if (reg.type == "bpmn:SequenceFlow") {
                modeling.removeConnection(reg);
              }
            });
          })
          .catch(function () {});
      },
      //效验
      checkBpmnEditXML() {
        var that = this;
        if (this.checkbpmnModelerXML()) {
          that.msgSuccess("校验通过！");
        }
      },
      checkbpmnModelerXML() {
        var that = this;
        var timeLimitAllStep = 0;
        const elementRegistry = this.bpmnModeler.get("elementRegistry");
        const startEventArr = elementRegistry.filter(
          (reg) => reg.type == "bpmn:StartEvent"
        );
        const endEventArr = elementRegistry.filter(
          (reg) => reg.type == "bpmn:EndEvent"
        );
        if (startEventArr.length == 1) {
          const otherEventArr = elementRegistry.filter(
            (reg) =>
              reg.type != "bpmn:StartEvent" &&
              reg.type != "bpmn:EndEvent" &&
              reg.type != "bpmn:Process" &&
              reg.type != "label" &&
              reg.type != "bpmn:SequenceFlow"
          );
          if (otherEventArr.length == 0) {
            that.msgError("请绘制正确的流程图！");
            return false;
          }
          for (var i = 0; i < otherEventArr.length; i++) {
            const itemInComing = otherEventArr[i].incoming;
            const itemOutGoing = otherEventArr[i].outgoing;
            if (itemInComing.length == 0 || itemOutGoing.length == 0) {
              that.msgError("请绘制正确的流程图！");
              return false;
            }
          }
        } else {
          that.msgError("流程的开始节点只能为一个！");
          return;
        }
        const userEventArr = elementRegistry.filter(
          (reg) => reg.type == "bpmn:UserTask"
        );
        if (userEventArr.length == 0) {
          that.msgError("流程图中至少包含一个用户任务！");
          return false;
        } else {
          //验证用户信息
          for (var i = 0; i < userEventArr.length; i++) {
            const userEvent = userEventArr[i];
            //环节名称
            const name = userEvent.businessObject.name;
            //linkOid
            const linkOid = userEvent.businessObject.$attrs.linkOid;
            if (undefined == linkOid || "" == linkOid) {
              that.msgError("请选择环节名称！");
              return false;
            }
            if (name == linkOid || "" == name) {
              that.msgError("请输入环节显示名称！");
              return false;
            }
            const handleUserType = userEvent.businessObject.$attrs.handleUserType;
            if (undefined != handleUserType && "2" != handleUserType) {
              //操作人
              const handleUserOids =
                userEvent.businessObject.$attrs.handleUserOids;
              //岗位
              const handlePostOids =
                userEvent.businessObject.$attrs.handlePostOids;
              if (undefined == handleUserOids && undefined == handlePostOids) {
                that.msgError("请选择环节（" + name + "）办理人或者办理岗位！");
                return false;
              }
            }
            let timeLimitStr = userEvent.businessObject.$attrs.timeLimit != undefined
              ? userEvent.businessObject.$attrs.timeLimit : "0";
            let timeLimitStep = parseInt(timeLimitStr);
            let timeLimitHourStr = userEvent.businessObject.$attrs.timeLimitHour != undefined
              ? userEvent.businessObject.$attrs.timeLimitHour : "0";
            if('0'==timeLimitStr && '0'==timeLimitHourStr){
              that.msgError("环节（" + name + "）办理时限不能都为0");
              return false;
            }
            //计算办理时限
            let timeUnitStr = userEvent.businessObject.$attrs.timeUnit != undefined
              ? userEvent.businessObject.$attrs.timeUnit : "W";
            if(that.timeUnit == timeUnitStr){
              timeLimitAllStep = timeLimitAllStep + timeLimitStep;
            }
          }
        }
        if (endEventArr.length == 0 || endEventArr.length > 1) {
          that.msgError("流程的结束节点只能为一个！");
          return false;
        }

        //内嵌流程
        const callActivityArr = elementRegistry.filter(reg => reg.type == "bpmn:CallActivity");
        for (var i = 0; i < callActivityArr.length; i++) {
          const callActivity = callActivityArr[i];


          //环节名称
          const name = callActivity.businessObject.name;
          if (undefined == name || "" == name) {
            that.msgError("请输入环节显示名称！");
            return false;
          }
          //innerFlowOid 内嵌流程
          const innerFlowOid = callActivity.businessObject.$attrs.innerFlowOid;
          if (undefined == innerFlowOid || "" == innerFlowOid) {
            that.msgError("请选择内嵌流程！");
            return false;
          }
          //计算办理时限
          let timeUnitStr = callActivity.businessObject.$attrs.timeUnit != undefined
            ? callActivity.businessObject.$attrs.timeUnit : "W";
          let timeLimitStr = callActivity.businessObject.$attrs.timeLimit != undefined
            ? callActivity.businessObject.$attrs.timeLimit : "0";
          let timeLimitStep = parseInt(timeLimitStr);
          let timeLimitHourStr = callActivity.businessObject.$attrs.timeLimitHour != undefined
            ? callActivity.businessObject.$attrs.timeLimitHour : "0";
          if('0'==timeLimitStr && '0'==timeLimitHourStr){
            that.msgError("环节（" + name + "）办理时限不能都为0");
            return false;
          }
          //计算办理时限
          if(that.timeUnit == timeUnitStr){
            timeLimitAllStep = timeLimitAllStep + timeLimitStep;
          }
        }
        //计算办理时限
        if(timeLimitAllStep > that.timeLimit){
          that.msgError("所有环节的办理时限不能大于流程设置的办理时限！");
          return false;
        }
        return true;
      },
      get_bodyHeight() {
        //动态获取浏览器高度
        const that = this;
        window.onresize = () => {
          return (() => {
            window.fullHeight = document.documentElement.clientHeight;
            that.fullHeight = window.fullHeight - 70;
          })();
        };
      },
      //放大缩小还原
      handlerZoom(radio) {
        const newScale = !radio ? 1.0 : this.scale + radio;
        this.bpmnModeler.get("canvas").zoom(newScale);
        this.scale = newScale;
      },
      //恢复
      handlerRedo() {
        this.bpmnModeler.get("commandStack").redo();
      },
      //撤销
      handlerUndo() {
        this.bpmnModeler.get("commandStack").undo();
      },
      //下载xml内容
      sequenceDownload(name, content) {
        const element = document.createElement("a");
        element.setAttribute(
          "href",
          "data:text/plain;charset=utf-8," + encodeURIComponent(content)
        );
        element.setAttribute("download", name);
        element.style.display = "none";
        element.click();
      },
      //保存文档
      watchXML() {
        var that = this;
        this.bpmnModeler.saveXML({ format: true }, function (err, xml) {
          if (err) {
            that.msgError("流程数据生成失败");
            console.log(err);
            return;
          }
          that.sequenceDownload(that.params.name + ".bpmn", xml);
        });
      },
      saveXML() {
        const that = this;
        this.$confirm("是否确认保存?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            that.bpmnModeler.saveXML({ format: true }, function (err, xml) {
              if (err) {
                that.msgError("流程数据生成失败");
                console.log(err);
                return;
              }
              // var modelId = that.params.modelId;
              var infoOid = that.params.infoOid;
              const datatemp = {
                bpmn_xml: xml,
              };
              const bpmnDataJson = { bpmnDataJson: datatemp };
              saveModelBpmn(bpmnDataJson, infoOid).then((response) => {
                if (response.code === 200) {
                  that.msgSuccess("保存成功");
                  localStorage.setItem("workflowStorage",'true');
                  that.handleClose();
                }
              });
            });
          })
          .catch(function () {});
      },
      //初始化BpmnModeler
      initBpmnModeler() {
        let canvas = this.$refs.canvas;
        var customTranslateModule = {
          translate: ["value", customTranslate],
        };
        // 禁止双击节点出现label编辑框
        var labelEditingProviderModule = {
          labelEditingProvider: ["value", ""],
        };
        this.bpmnModeler = new BpmnModeler({
          container: canvas,
          keyboard: {
            bindTo: window,
          },
          //添加属性面板，添加翻译模块
          additionalModules: [
            customTranslateModule,
            customControlsModule,
            //不能去除禁用，否则出现编辑器用户任务的兼容问题
            labelEditingProviderModule,
          ],
          //模块拓展，拓展activiti的描述
          /*moddleExtensions: {
                activiti: activitiModdleDescriptor
            }*/
        });
        this.importBpmnXml();
      },
      //导入xml文档
      importBpmnXml() {
        const that = this;
        processView(that.params.infoOid).then(response => {
          that.params.bpmnXml = response.data.bpmnXml;
          that.params.modelId = response.data.modelId;
          that.params.name = response.data.name;
          that.timeLimit = response.data.timeLimit;
          that.timeUnit = response.data.timeUnit;
          this.$refs.commonChild.changeName(that.params.name);
          let bpmnXml = bpmnHelper.getBpmnTempate(that.params.infoOid);
          if (undefined != that.params.bpmnXml && "" != that.params.bpmnXml) {
            bpmnXml = that.params.bpmnXml;
          }
          that.bpmnModeler.importXML(bpmnXml, function (err) {
            if (err) {
              that.msgError("流程导入失败！");
            } else {
              // 绑定监听事件
              that.success();
            }
          });
        }).catch(function () {
          let bpmnXml = bpmnHelper.getBpmnTempate(that.params.infoOid);
          that.bpmnModeler.importXML(bpmnXml, function (err) {
            if (err) {
              that.msgError("流程导入失败！");
            } else {
              // 绑定监听事件
              that.success();
            }
          });
        });

      },
      getBpmnModeler() {
        return this.bpmnModeler;
      },
      success() {
        //console.log('创建成功!');
        // 初始化element
        const elementRegistry = this.bpmnModeler.get("elementRegistry");
        const shape = elementRegistry.get("Process_" + this.params.modelId);
        this.element = shape;
        this.addModelerListener();
        this.addEventBusListener();
        // 调控左侧工具栏
        this.adjustPalette();
        //设置驳回和退回线的颜色
        this.setBackColor();
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
      addModelerListener() {
        // 监听 modeler
        const bpmnjs = this.bpmnModeler;
        const that = this;
        // 'shape.removed', 'connect.end', 'connect.move'
        const events = ["shape.added", "shape.move.end", "shape.removed"];
        events.forEach(function (event) {
          that.bpmnModeler.on(event, (e) => {
            var elementRegistry = bpmnjs.get("elementRegistry");
            var shape = e.element ? elementRegistry.get(e.element.id) : e.shape;
            if (event === "shape.added") {
              //console.log('新增了shape');
              // 展示新增图形的属性
              that.key = e.element.id.replace("_label", "");
              that.propsComponent = bpmnHelper.getComponentByEleType(shape.type);
              that.element = e.element;
            } else if (event === "shape.move.end") {
              //console.log('移动了shape')
              // 展示新增图形的属性
              that.key = shape.id;
              that.propsComponent = bpmnHelper.getComponentByEleType(shape.type);
              that.element = e.shape;
            } else if (event === "shape.removed") {
              //console.log('删除了shape')
              // 展示默认的属性
              that.propsComponent = "CommonProps";
            }
          });
        });
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
              console.log("点击了element");
              if (!e || e.element.type == "bpmn:Process") {
                that.key = "1";
                that.propsComponent = "CommonProps";
                that.element = e.element;
              } else {
                // 展示新增图形的属性
                that.key = e.element.id;
                that.propsComponent = bpmnHelper.getComponentByEleType(
                  e.element.type
                );
                that.element = e.element;
              }
            }
          });
        });
      },
      isInvalid(param) {
        // 判断是否是无效的值
        return param === null || param === undefined || param === "";
      },
      isSequenceFlow(type) {
        // 判断是否是线
        return type === "bpmn:SequenceFlow";
      },
      elementChanged(e) {
        const id = e.element.id.replace("_label", "");
        var shape = this.getShape(id);
        this.element = shape;
        const that = this;
        if (!shape) {
          // 若是shape为null则表示删除, 无论是shape还是connect删除都调用此处
          console.log("无效的shape");
          // 上面已经用 shape.removed 检测了shape的删除, 要是删除shape的话这里还会被再触发一次
          console.log("删除了shape和connect");
          return;
        }
        if (!this.isInvalid(shape.type)) {
          if (this.isSequenceFlow(shape.type)) {
            console.log("改变了线");
            that.propsComponent = bpmnHelper.getComponentByEleType(
              e.element.type
            );
          } else {
            that.setDefaultProperties();
          }
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

      // 调整左侧工具栏排版
      adjustPalette() {
        try {
          // 获取 bpmn 设计器实例
          const canvas = this.$refs.canvas;
          const djsPalette = canvas.children[0].children[1].children[4];
          const djsPalStyle = {
            width: "130px",
            padding: "5px",
            background: "white",
            left: "20px",
            borderRadius: 0,
          };
          for (var key in djsPalStyle) {
            djsPalette.style[key] = djsPalStyle[key];
          }
          const palette = djsPalette.children[0];
          const allGroups = palette.children;
          var groupGateway = allGroups[2].innerHTML;
          var groupUserTask = allGroups[3].innerHTML;
          allGroups[3].innerHTML = groupGateway;
          allGroups[2].innerHTML = groupUserTask;
          allGroups[1].style["display"] = "none";
          allGroups[4].style["display"] = "none";
          allGroups[5].style["display"] = "none";
          allGroups[6].style["display"] = "none";
          allGroups[7].style["display"] = "none";
          // 修改控件样式
          for (var gKey in allGroups) {
            let group = allGroups[gKey];
            for (var cKey in group.children) {
              const control = group.children[cKey];
              //console.log(control.className)
              //console.log(control.dataset)
              //bpmn-icon-gateway-parallel 并行
              //bpmn-icon-gateway-xor 排他
              const controlStyle = {
                display: "flex",
                justifyContent: "flex-start",
                alignItems: "center",
                width: "100%",
                padding: "5px",
              };
              if (
                control.className &&
                control.dataset &&
                control.className.indexOf("entry") !== -1
              ) {
                const controlProps = this.bpmData.getControl(
                  control.dataset.action
                );
                control.innerHTML = `<div title="${controlProps["title"]}" style='font-size: 14px;font-weight:500;margin-left:15px;'>${controlProps["title"]}</div>`;
                for (var csKey in controlStyle) {
                  control.style[csKey] = controlStyle[csKey];
                }
              }
            }
          }
          //allGroups[0].children[1].style["display"] = "none";
          allGroups[0].children[2].style["display"] = "none";
          allGroups[0].children[3].style["display"] = "none";
          allGroups[0].children[4].style["display"] = "none";
          allGroups[2].children[0].style["display"] = "none";
          allGroups[2].children[1].style["display"] = "none";
          //allGroups[2].children[3].style["display"] = "none";
        } catch (e) {
          console.log(e);
        }
      },
      handleOk() {
        var that = this;
        if (this.checkbpmnModelerXML()) {
          this.saveXML();
        }
      },
      handleClose() {
        //this.$emit("handleClose");
        window.opener=null;
        window.close();
      },
      handleCloseClick() {
        var that = this;
        this.$confirm("是否确认关闭当前窗口?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            that.handleClose();
          })
          .catch(function () {});

      },
      //查询所有区划的组织机构、用户信息Tree
      queryAllDistrictOrganUser() {
        queryAllDistrictOrganUserTree().then((response) => {
          this.params.districtOrganUserTree = response.data;

        });
      },
      //查询所有区划的组织机构、岗位信息Tree
      queryAllDistrictOrganPost() {
        queryAllDistrictOrganPostTree().then((response) => {
          this.params.districtOrganPostTree = response.data;
        });
      },
      //查询所有区划的组织机构、流程信息Tree
      queryAllDistrictOrganBussInfo() {
        queryAllDistrictOrganBussInfoTree().then((response) => {
          this.params.districtOrganBussInfoTree = response.data;
        });
      },
      queryWorkflowLinkList() {
        queryWorkflowLinkListByInfoOid(this.params.infoOid).then((response) => {
          this.params.workflowLinkList = response.data;
        });
      },
    },
    mounted() {
      //初始化模型编辑器
      this.initBpmnModeler();
      this.get_bodyHeight();
      this.queryAllDistrictOrganUser();
      this.queryAllDistrictOrganBussInfo();
      this.queryAllDistrictOrganPost();
      this.queryWorkflowLinkList();
      var a = document.querySelector(".bjs-powered-by");
      try {
        a.childNodes[0].remove();
      }catch (e) {
        //解决ie兼容模式下的问题
      }
    },
  };
</script>

<style scoped>
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
    padding: 0 10px 0px !important;
  }
  .el-radio__label {
    font-size: 12px !important;
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
  .saveXml {
    display: flex;
    margin-left: 350px;
  }
  .containers {
    background: white;
    overflow: auto;
    background-image: linear-gradient(
      90deg,
      rgba(220, 220, 220, 0.5) 6%,
      transparent 0
    ),
    linear-gradient(rgba(192, 192, 192, 0.5) 6%, transparent 0);
    background-size: 12px 12px;
    width: 100%;
    height: calc(100vh - 82px);
    -webkit-tap-highlight-color: rgba(255, 255, 255, 0);
  }
  .el-dialog__body {
    padding: 0px 20px !important;
  }
  .el-button--medium {
    padding: 10px 10px !important;
    margin-bottom: 5px !important;
  }
  aside {
    margin-bottom: 0px !important;
  }
  .modelerBar {
    float: left;
    margin-left: 5px;
  }
  .modelerBarRight {
    float: right;
    margin-right: 10px;
  }
  .whitebg {
    color: white;
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
  .el-aside {
    overflow-y: hidden !important;
  }
  .djs-element > .djs-hit-all,.djs-palette .entry{
    cursor: pointer;
  }
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


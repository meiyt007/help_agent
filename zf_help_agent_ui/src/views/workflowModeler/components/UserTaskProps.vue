<template>
  <div>
    <el-form-item v-show="false" label="编号" required>
      <el-input v-model="id"></el-input>
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
    <el-form-item label="环节显示名称" required>
      <el-input v-model="name" placeholder="请输入环节显示名称"></el-input>
    </el-form-item>
    <el-form-item label="办理时限">
      <span style="display: block; margin-bottom: 5px">
        <el-input
          class="input_modeler_left"
          placeholder="办理时限"
          v-model="timeLimit"
        ></el-input>
        <el-radio-group v-model="timeUnit" class="input_modeler_right">
          <el-radio label="W">工作日</el-radio>
          <el-radio label="N">自然日</el-radio>
        </el-radio-group>
      </span>
      <el-input
        placeholder="小时"
        class="input_modeler_left"
        v-model="timeLimitHour"
      ></el-input
      >&nbsp;&nbsp;小时
    </el-form-item>
    <el-form-item label="环节办理类型">
      <el-radio-group v-model="handleType">
        <el-radio label="1">任意人员办理</el-radio>
        <el-radio label="2">全部人员办理</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="人员设定类型">
      <el-radio-group v-model="handleUserType">
        <el-radio label="1">环节自定义</el-radio>
        <el-radio label="2">上一环节设置</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="办理人" required>
      <div class="block" style="margin-top: 3px">
        <treeselect
          class="treeselect"
          ref="cascaderUser"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="handleUserOids"
          value-consists-of="LEAF_PRIORITY"
          :multiple="true"
          @close="getCheckedUserNames"
          :options="userOptions"
          placeholder="请选择办理人"
          :limit="3"
          :limitText="limitText"
        >
        </treeselect>
      </div>
    </el-form-item>
    <el-form-item label="办理岗位" required>
      <div class="block">
        <treeselect
          class="treeselect"
          ref="cascaderPost"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="handlePostOids"
          value-consists-of="LEAF_PRIORITY"
          :multiple="true"
          @close="getCheckedPostNames"
          :options="postOptions"
          placeholder="请选择办理岗位"
          :limit="3"
          :limitText="limitText"
        >
        </treeselect>
      </div>
    </el-form-item>
    <el-form-item label="驳回环节">
      <el-select
        v-model="rejectTaskId"
        @change="changeRejectTaskId"
        size="small"
        filterable
        clearable
        no-data-text=""
        placeholder="请选择驳回环节"
      >
        <el-option
          v-for="p in userTaskIdList"
          :key="p.taskId"
          :label="p.taskName"
          :value="p.taskId"
        ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="退回环节">
      <el-select
        v-model="fallbackTaskId"
        size="small"
        @change="changeFallbackTaskId"
        filterable
        clearable
        placeholder="请选择退回环节"
      >
        <el-option
          v-for="p in userTaskIdList"
          :key="p.taskId"
          :label="p.taskName"
          :value="p.taskId"
        ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="表单标识" v-show="false">
      <el-input v-model="formKey"></el-input>
    </el-form-item>
  </div>
</template>
<script>
const forEach = require("lodash/forEach");
import {
  queryAllDistrictOrganUserTree,
  queryAllDistrictOrganPostTree,
} from "@/api/workflow/bussinfo";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
// 用户任务属性组件
export default {
  props: ["element"],
  components: { Treeselect },
  inject: ["bpmnModeler", "params"],
  data () {
    return {
      modeling: null,
      id: this.element.id || "",
      name: "",
      formKey: "",
      timeUnit: "W",
      timeLimitHour: "0",
      handleUserType: "1",
      handleType: "1",
      handleUserOids: [],
      handleUserNames: [],
      handlePostOids: [],
      handlePostNames: [],
      workflowLinkList: [],
      //环节oid
      linkOid: "",
      rejectTaskId: "",
      fallbackTaskId: "",
      timeLimit: "0",
      assignee: "",
      //候选人
      candidateUsers: "",
      // 原子节点属性
      postOidsOptions: [],
      innerWorkflowPositions: [],
      priority: "",
      //办理人
      userOptions: [],
      //办理岗位
      postOptions: [],
      userTaskIdList: [],
    };
  },
  methods: {
    limitText (count) {
      return `更多+${count}`;
    },
    changeWorkflowLink (oid) {
      const workflowLink = this.workflowLinkList.find(
        (item) => item.linkOid == oid
      );
      /*if(''==this.name || null ==this.name){
         this.name = workflowLink.name;
       }*/
      this.name = workflowLink.name;
    },
    //驳回
    changeRejectTaskId (toOid) {
      let that = this;
      const canvas = this.bpmnModeler().get("canvas");
      const elementRegistry = this.bpmnModeler().get("elementRegistry");
      const process = elementRegistry.find(
        (item) => item.id == "Process_" + this.params.infoOid
      );

      const startTask = elementRegistry.find((item) => item.id == this.id);
      const toTask = elementRegistry.find((item) => item.id == toOid);
      let exitsFlag = false;

      //查找已经创建好的线
      var map = elementRegistry._elements;
      let elementRegistryMap = Object.keys(map);
      elementRegistryMap.forEach(function (id, index) {
        var reg = elementRegistry.find((item) => item.id == id);
        if (undefined != reg && reg.type == "bpmn:SequenceFlow") {
          if (reg.source.id == startTask.id && reg.target.id == toTask.id && reg.businessObject.$attrs.flowClass == 'bh') {
            exitsFlag = true;
            return;
          } else if (reg.source.id == startTask.id && reg.businessObject.$attrs.flowClass == 'bh') {
            let labelFlow = reg.labels[0];
            that.modeling.removeShape(labelFlow);
            canvas.removeConnection(reg);
          }
        }
      });
      if (!exitsFlag) {
        const waypoints = [];
        //start
        const xs = startTask.x + 50;
        const ys = startTask.y + 80;
        //to
        const xe = toTask.x + 50;
        const ye = toTask.y + 80;

        const ycha = startTask.y - toTask.y;
        let startOriginal = { original: { x: xs, y: ys }, x: xs, y: ys };
        let fristOriginal = {};
        let sencondOriginal = {};
        if (ycha > 0) {
          fristOriginal = { original: { x: xs, y: ys + 50 }, x: xs, y: ys + 50 };
          sencondOriginal = {
            original: { x: xe, y: ye + 50 + ycha },
            x: xe,
            y: ye + 50 + ycha,
          };
        } else {
          let ychas = toTask.y - startTask.y;
          fristOriginal = {
            original: { x: xs, y: ys + 50 + ychas },
            x: xs,
            y: ys + 50 + ychas,
          };
          sencondOriginal = {
            original: { x: xe, y: ye + 50 },
            x: xe,
            y: ye + 50,
          };
        }
        let endOriginal = { original: { x: xe, y: ye }, x: xe, y: ye };
        waypoints.push(startOriginal);
        waypoints.push(fristOriginal);
        waypoints.push(sencondOriginal);
        waypoints.push(endOriginal);
        const newSequenceFlow = this.modeling.createConnection(
          startTask,
          toTask,
          { type: "bpmn:SequenceFlow", waypoints: waypoints },
          process
        );
        //this.modeling.updateWaypoints(newSequenceFlow, waypoints);
        let name = undefined == toTask.businessObject.name ? "" : "至" + toTask.businessObject.name;
        that.modeling.updateProperties(newSequenceFlow, {
          flowClass: 'bh',
          startTaskId: startTask.id,
          name: "驳回" + name
        });
        const waypointsJson = JSON.stringify(waypoints).replace(/\"/g, "'");
        this.modeling.updateProperties(this.element, {
          rejectTaskId: toOid,
          //rejectFlowId:newSequenceFlow.id,
          rejectWaypoints: waypointsJson
        });
        canvas.addMarker(newSequenceFlow.id, "highlight-bh");
      }
    },
    //退回
    changeFallbackTaskId (toOid) {
      let that = this;
      const elementRegistry = this.bpmnModeler().get("elementRegistry");
      const canvas = this.bpmnModeler().get("canvas");
      const process = elementRegistry.find(
        (item) => item.id == "Process_" + this.params.infoOid
      );
      const startTask = elementRegistry.find((item) => item.id == this.id);
      const toTask = elementRegistry.find((item) => item.id == toOid);
      let exitsFlag = false;
      //查找已经创建好的线
      var map = elementRegistry._elements;
      let elementRegistryMap = Object.keys(map);
      elementRegistryMap.forEach(function (id, index) {
        var reg = elementRegistry.find((item) => item.id == id);
        if (undefined != reg && reg.type == "bpmn:SequenceFlow") {
          if (reg.source.id == startTask.id && reg.target.id == toTask.id && reg.businessObject.$attrs.flowClass == 'th') {
            exitsFlag = true;
            return;
          } else if (reg.source.id == startTask.id && reg.businessObject.$attrs.flowClass == 'th') {
            let labelFlow = reg.labels[0];
            that.modeling.removeShape(labelFlow);
            canvas.removeConnection(reg);
          }
        }
      });
      if (!exitsFlag) {
        const waypoints = [];
        //start
        const xs = startTask.x + 50;
        const ys = startTask.y;
        //to
        const xe = toTask.x + 50;
        const ye = toTask.y - 0;

        const ycha = startTask.y - toTask.y;
        let startOriginal = { original: { x: xs, y: ys }, x: xs, y: ys };
        let fristOriginal = {};
        let sencondOriginal = {};
        if (ycha > 0) {
          fristOriginal = { original: { x: xs, y: ys - 50 - ycha }, x: xs, y: ys - 50 - ycha };
          sencondOriginal = {
            original: { x: xe, y: ye - 50 },
            x: xe,
            y: ye - 50,
          };
        } else {
          let ychas = toTask.y - startTask.y;
          fristOriginal = {
            original: { x: xs, y: ys - 50 },
            x: xs,
            y: ys - 50,
          };
          sencondOriginal = {
            original: { x: xe, y: ye - 50 - ychas },
            x: xe,
            y: ye - 50 - ychas,
          };
        }
        let endOriginal = { original: { x: xe, y: ye }, x: xe, y: ye };
        waypoints.push(startOriginal);
        waypoints.push(fristOriginal);
        waypoints.push(sencondOriginal);
        waypoints.push(endOriginal);
        const newSequenceFlow = this.modeling.createConnection(
          startTask,
          toTask,
          { type: "bpmn:SequenceFlow", waypoints: waypoints },
          process
        );
        this.modeling.updateWaypoints(newSequenceFlow, waypoints);
        let name = undefined == toTask.businessObject.name ? "" : "至" + toTask.businessObject.name;
        that.modeling.updateProperties(newSequenceFlow, {
          flowClass: 'th',
          startTaskId: startTask.id,
          name: "退回" + name
        });
        const waypointsJson = JSON.stringify(waypoints).replace(/\"/g, "'");
        this.modeling.updateProperties(this.element, {
          fallbackTaskId: toOid,
          //rejectFlowId:newSequenceFlow.id,
          fallbackWaypoints: waypointsJson
        });
        canvas.addMarker(newSequenceFlow.id, "highlight-th");
      }

    },
    //查询所有区划的组织机构、用户信息Tree
    queryAllDistrictOrganUser () {
      queryAllDistrictOrganUserTree().then((response) => {
        this.userOptions = response.data;
      });
    },
    //查询所有区划的组织机构、岗位信息Tree
    queryAllDistrictOrganPost () {
      queryAllDistrictOrganPostTree().then((response) => {
        this.postOptions = response.data;
      });
    },
    //办理人
    getCheckedUserNames (data) {
      var string = this.$refs.cascaderUser.$el.innerText;
      //替换所有的换行符
      string = string.replace(/\r\n/g, "<br>");
      string = string.replace(/\n/g, "<br>");
      //替换所有的空格（中文空格、英文空格都会被替换）
      string = string.replace(/\s/g, ",");
      var index = string.indexOf("<br>");
      string = string.substring(0, index);
      var userNamesArr = this.unique(string.split(","));
      if (userNamesArr.indexOf("请选择办理人") == -1) {
        this.handleUserNames = userNamesArr.join(",");
      } else {
        this.handleUserNames = "";
      }
      // console.log('checkName:'+this.handleUserNames)
    },
    //岗位
    getCheckedPostNames (data) {
      var string = this.$refs.cascaderPost.$el.innerText;
      //替换所有的换行符
      string = string.replace(/\r\n/g, "<br>");
      string = string.replace(/\n/g, "<br>");
      //替换所有的空格（中文空格、英文空格都会被替换）
      string = string.replace(/\s/g, ",");
      var index = string.indexOf("<br>");
      string = string.substring(0, index);
      var userPostNamesArr = this.unique(string.split(","));
      if (userPostNamesArr.indexOf("请选择办理岗位") == -1) {
        this.handlePostNames = userPostNamesArr.join(",");
      } else {
        this.handlePostNames = "";
      }
      //console.log('checkPostName:'+this.handlePostNames)
    },
    unique (arr) {
      var hash = [];
      for (var i = 0; i < arr.length; i++) {
        if ("" != arr[i] && hash.indexOf(arr[i]) == -1) {
          hash.push(arr[i]);
        }
      }
      return hash;
    },
    //办理人切换
    userOptionsChange (data) {
      var that = this;
      that.handleUserNames = [];
      let nodesObj = this.$refs["cascaderUser"].getCheckedNodes();
      data.forEach(function (item, index) {
        //  item:元素 index:下标
        item.forEach(function (itemUser, itemIndex) {
          //  itemUser:元素 index:下标
          if (itemUser.indexOf("USER-") > -1) {
            let user = nodesObj.find((itemObj) => itemObj.value == itemUser);
            that.handleUserNames.push(user.label);
          }
        });
      });
    },
    //岗位切换
    postOptionsChange (data) {
      var that = this;
      that.handlePostNames = [];
      let nodesObj = this.$refs["cascaderPost"].getCheckedNodes();
      data.forEach(function (item, index) {
        //  item:元素 index:下标
        item.forEach(function (itemUser, itemIndex) {
          //  itemUser:元素 index:下标
          if (itemUser.indexOf("POST-") > -1) {
            let post = nodesObj.find((itemObj) => itemObj.value == itemUser);
            that.handlePostNames.push(post.label);
          }
        });
      });
    },
    getBackTaskList () {
      const bpmnModeler = this.bpmnModeler();
      this.modeling = bpmnModeler.get("modeling");
      var that = this;
      const userAllBackTaskList = bpmnModeler
        .get("elementRegistry")
        .filter((item) => item.type === "bpmn:UserTask");
      const userBackTaskList = userAllBackTaskList.filter(
        (reg) => reg.id != that.element.id
      );
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
  },
  created () {
    //初始化办理人、岗位、流程Tree数据
    this.userOptions = this.params.districtOrganUserTree;
    this.postOptions = this.params.districtOrganPostTree;
    this.workflowLinkList = this.params.workflowLinkList;
    if (
      undefined == this.params.districtOrganUserTree ||
      this.params.districtOrganUserTree.length == 0
    ) {
      this.queryAllDistrictOrganUser();
    }
    if (
      undefined == this.params.districtOrganPostTree ||
      this.params.districtOrganPostTree.length == 0
    ) {
      this.queryAllDistrictOrganPost();
    }
  },
  mounted: function () {
    this.getBackTaskList();
  },
  watch: {
    name: {
      handler (newVal, oldVal) {
        this.modeling.updateProperties(this.element, {
          name: newVal,
        });
      },
    },
    //监视元素变化
    element: {
      deep: true,
      immediate: true,
      handler (newVal, oldVal) {
        if (newVal.type == "bpmn:UserTask") {
          const bpmnModeler = this.bpmnModeler();
          const modeling = bpmnModeler.get("modeling");
          const businessObject = newVal.businessObject;
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
          const handUsersStr =
            businessObject.$attrs.handleUserOids != undefined
              ? businessObject.$attrs.handleUserOids
              : "";
          this.handleUserOids =
            "" != handUsersStr ? handUsersStr.split("~") : [];
          this.handleUserNames =
            businessObject.$attrs.handleUserNames != undefined
              ? businessObject.$attrs.handleUserNames
              : [];

          const handlePostOidsStr =
            businessObject.$attrs.handlePostOids != undefined
              ? businessObject.$attrs.handlePostOids
              : "";
          this.handlePostOids =
            "" != handlePostOidsStr ? handlePostOidsStr.split("~") : [];
          this.handlePostNames =
            businessObject.$attrs.handlePostNames != undefined
              ? businessObject.$attrs.handlePostNames
              : [];
          this.rejectTaskId =
            businessObject.$attrs.rejectTaskId != undefined
              ? businessObject.$attrs.rejectTaskId
              : "";
          this.fallbackTaskId =
            businessObject.$attrs.fallbackTaskId != undefined
              ? businessObject.$attrs.fallbackTaskId
              : "";
          this.linkOid =
            businessObject.$attrs.linkOid != undefined
              ? businessObject.$attrs.linkOid
              : "";
          this.name = businessObject.name;
          this.formKey = businessObject.get("formKey");

          //更新默认的值
          modeling.updateProperties(this.element, { timeUnit: this.timeUnit });
          modeling.updateProperties(this.element, {
            handleUserType: this.handleUserType,
          });
          modeling.updateProperties(this.element, {
            handleType: this.handleType,
          });
        }
      },
    },
    linkOid: {
      handler (newVal, oldVal) {
        this.modeling.updateProperties(this.element, { linkOid: newVal });
      },
    },
    handleUserOids: {
      handler (newVal, oldVal) {
        const newValStr = newVal.join("~");
        this.modeling.updateProperties(this.element, {
          handleUserOids: newValStr,
        });
      },
    },
    handlePostOids: {
      handler (newVal, oldVal) {
        const newValStr = newVal.join("~");
        this.modeling.updateProperties(this.element, {
          handlePostOids: newValStr,
        });
      },
    },
    handleUserNames: {
      handler (newVal, oldVal) {
        this.modeling.updateProperties(this.element, {
          handleUserNames: newVal,
        });
      },
    },
    handlePostNames: {
      handler (newVal, oldVal) {
        this.modeling.updateProperties(this.element, {
          handlePostNames: newVal,
        });
      },
    },
    rejectTaskId: {
      handler (newVal, oldVal) {
        if ('' != newVal) {
          const userTaskId = this.userTaskIdList.find(
            (reg) => reg.id == newVal
          );
          if (undefined == userTaskId) {
            this.rejectTaskId = '';
          }
        }
        this.modeling.updateProperties(this.element, { rejectTaskId: newVal });
      },
    },
    fallbackTaskId: {
      handler (newVal, oldVal) {
        if ('' != newVal) {
          const fallTaskId = this.userTaskIdList.find(
            (reg) => reg.id == newVal
          );
          if (undefined == fallTaskId) {
            this.fallbackTaskId = '';
          }
        }
        this.modeling.updateProperties(this.element, {
          fallbackTaskId: newVal,
        });
      },
    },
    assignee: {
      handler (newVal, oldVal) {
        this.modeling.updateProperties(this.element, { assignee: newVal });
      },
    },
    candidateUsers: {
      handler (newVal, oldVal) {
        this.modeling.updateProperties(this.element, {
          candidateUsers: newVal,
        });
      },
    },
    handleUserType: {
      handler (newVal, oldVal) {
        newVal = null == newVal ? "1" : newVal;
        this.modeling.updateProperties(this.element, {
          handleUserType: newVal,
        });
      },
    },
    handleType: {
      handler (newVal, oldVal) {
        newVal = null == newVal ? "1" : newVal;
        this.modeling.updateProperties(this.element, { handleType: newVal });
      },
    },
    timeUnit: {
      handler (newVal, oldVal) {
        newVal = null == newVal ? "W" : newVal;
        this.modeling.updateProperties(this.element, { timeUnit: newVal });
      },
    },
    timeLimit: {
      handler (newVal, oldVal) {
        var that = this;
        let numberReg = /^\d+$|^\d+[.]?\d+$/
        if (newVal !== '') {
          if (!numberReg.test(newVal)) {
            that.timeLimit = '0';
            that.msgError('请输入数字');
          } else {
            this.modeling.updateProperties(this.element, { timeLimit: newVal });
          }
        }

      },
    },
    timeLimitHour: {
      handler (newVal, oldVal) {
        var that = this;
        let numberReg = /^\d+$|^\d+[.]?\d+$/
        if (newVal !== '') {
          if (!numberReg.test(newVal)) {
            that.timeLimitHour = '0';
            that.msgError('请输入数字');
          } else {
            if (parseInt(newVal) >= 24 || parseInt(newVal) < 0) {
              that.msgError('办理时限小时数只能为1-24');
              that.timeLimitHour = '0';
            } else {
              this.modeling.updateProperties(this.element, { timeLimitHour: newVal });
            }
          }
        }
      },
    },
  },
};
</script>
<style lang="scss" scoped>
.input_modeler_left {
  width: 90px !important;
}
.input_modeler_right {
  margin-left: 5px;
  width: 146px !important;
}
>>> .el-radio {
  margin-right: 15px !important;
}
>>> .el-cascader--small {
  width: 100% !important;
}
>>> .el-select--small {
  width: 100% !important;
}
>>> .el-main {
  padding: 10px !important;
}
>>> .el-form-item--mini.el-form-item {
  margin-bottom: 10px !important;
}
>>> .el-radio__label {
  font-size: 12px;
  padding-left: 5px;
}
>>> .el-form-item--mini .el-form-item__label {
  line-height: 25px !important;
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
</style>

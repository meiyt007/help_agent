<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-10 17:46:56
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-03 17:28:07
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\serviceTransfer.vue
 * @Description: 服务转派
-->
<template>
  <div class="intelligentFormFilling-container" v-loading="turnServiceLoading">
    <div class="tabHeader" v-if="basicUserInfo.groupPost === '1'">
      <p
        class="tabLeft"
        @click="changeTab('1')"
        :class="tabType === '1' ? 'active' : ''"
      >
        本组
      </p>
      <p
        class="tabRight"
        @click="changeTab('2')"
        :class="tabType === '2' ? 'active' : ''"
      >
        其他小组
      </p>
    </div>
    <div class="body-content" v-loading="loadingData">
      <div class="tab" ref="tab1" v-show="tabType === '1'">
        <el-table
          ref="table1"
          :height="tableHeight1"
          :data="groupServiceList"
          :cell-style="{ 'text-align': 'center' }"
          :header-cell-style="{ 'text-align': 'center' }"
          border
        >
          <el-table-column :key="Math.random()">
            <template slot-scope="scope">
              <el-radio
                v-model="tableradioGroup"
                :label="scope.row"
                @change="changeService(scope.row, 'group')"
                ><i />
              </el-radio>
            </template>
          </el-table-column>
          <template v-for="(item, index) in assistantColumnList">
            <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
              :width="item.width"
              v-if="item.prop === 'haType'"
            >
              <template slot-scope="scope">
                <span>{{
                  scope.row.haType === "1" ? "快捷帮办" : "圆桌帮办"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :key="index"
              :label="item.label"
              :width="item.width"
              v-else-if="item.prop === 'status'"
            >
              <template slot-scope="scope">
                <span
                  :class="
                    scope.row.status === '1'
                      ? 'offLine'
                      : scope.row.status === '2'
                      ? 'busy'
                      : scope.row.status === '3'
                      ? 'free'
                      : 'inservice'
                  "
                  >{{
                    scope.row.status === "1"
                      ? "离线"
                      : scope.row.status === "2"
                      ? "忙碌"
                      : scope.row.status === "3"
                      ? "空闲"
                      : "服务中"
                  }}</span
                >
              </template>
            </el-table-column>
            <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
              :width="item.width"
              v-else
            ></el-table-column>
          </template>
        </el-table>
      </div>
      <div class="tab other-group" ref="tab2" v-show="tabType === '2'">
        <div class="table-title">请选择转派接收组：</div>
        <div class="table-btn">
          <el-radio-group v-model="tableradioGroup" class="chooseOption">
            <el-radio :key="data" v-for="data in allServiceList" :label="data"
                @change="changeService(data)"> {{ data.groupName }} </el-radio>
          </el-radio-group>
        </div>
        <div class="table-title">请选择转派接收组长：</div>
        <div class="table-btn">
          <el-radio-group v-model="groupLeaderId" class="chooseOption">
            <el-radio   :key="item.id" v-for="item in haWorkUsers" :label="item.id">
              {{ item.name }}
            </el-radio>
          </el-radio-group>
        </div>
          
        <!-- <el-table
          ref="table2"
          :height="tableHeight2"
          :data="allServiceList"
          :cell-style="{ 'text-align': 'center' }"
          :header-cell-style="{ 'text-align': 'center' }"
          border
        >
          <el-table-column type="index" width="100" label="序号">
          </el-table-column>
          <template v-for="(item, index) in tableColumns">
            <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
            ></el-table-column>
          </template>
          <el-table-column :key="Math.random()" width="200" label="选择转派组">
            <template slot-scope="scope">
              <el-radio
                v-model="tableradioGroup"
                :label="scope.row"
                @change="changeService(scope.row)"
                ><i />
              </el-radio>
            </template>
          </el-table-column>
        </el-table> -->
      </div>
    </div>
    <div class="dialog-footer">
      <el-input placeholder="请填写转派原因" v-model="turnMemo" />
      <p @click="cancelDialog">取消</p>
      <p @click="serviceTurn">确定转派</p>
    </div>
    <el-dialog
      v-dialog-drag
      title="选择组长"
      :visible.sync="showChooseGroupLeader"
      class="choose-dialog"
      width="600px"
      append-to-body
    >
      <el-select v-model="groupLeaderId" filterable placeholder="请选择">
        <el-option
          v-for="item in haWorkUsers"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <p @click="chooseLeader">确定</p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getAllUserServiceByGroupSplit, getGroupList } from "@/api/modules/helpAgent";
import {
  serviceTurn,
  turnIsVerify,
  turnServiceVerify,
} from "@/api/modules/helpAgent";
export default {
  name: "QueueCondition",
  props: {
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    currentServiceIndex: {
      type: Number,
      default: () => 0,
    },
    showServiceTransfer: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      loadingData: false,
      showChooseGroupLeader: false,
      tabType: "2",
      haType: "",
      groupLeaderId: "",
      haWorkUsers: [],
      turnServiceLoading: false,
      tableHeight1: 200,
      tableHeight2: 200,
      reassignmentCurrentServiceIndex: 0,
      groupServiceList: [],
      allServiceList: [],
      assistantColumnList: [
        { label: "服务状态", prop: "status", width: 150 },
        { label: "帮办人员", prop: "name", width: 150 },
        { label: "帮办类型", prop: "haType", width: 150 },
        { label: "正在服务人数", prop: "inServiceNum", width: 150 },
        { label: "等待人数", prop: "waitingNum", width: 150 },
        { label: "预计等待时间", prop: "esuimateTime", width: 150 },
      ],
      tableColumns: [
        { label: "分组名", prop: "groupName" },
        // { label: "帮办人员", prop: "name" },
        // { label: "所在分组", prop: "groupName" },
        // { label: "帮办类型", prop: "haType" },
        // { label: "正在服务人数", prop: "inServiceNum" },
        // { label: "等待人数", prop: "waitingNum" },
        // { label: "预计等待时间", prop: "esuimateTime" },
        // { label: "服务状态", prop: "status" },
      ],
      queryCriteria: "",
      tableradioGroup: {},
      tableradioAll: {},
      turnMemo: "",
      chooseData: {},
      chooseGroupData: {},
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  mounted() {
    console.log(this.baseUserInfo)
    this.groupLeaderId = '';
    this.haType = this.basicUserInfo.haType;
    this.getGroupList();
    this.getWorkUserList();
  },
  methods: {
    chooseLeader() {
      if (!this.groupLeaderId) {
        this.$message.warning("请选择组长");
        return;
      }
      this.showChooseGroupLeader = false;
    },
    //获取分组列表
    getGroupList() {
      this.loadingData = true;
      getGroupList()
        .then((res) => {
          this.loadingData = false;
          if (res.code === 200) {
            this.allServiceList = res.data;
            this.tableHeight2 = this.$refs.tab2.clientHeight;
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingData = false;
        });
    },

    getWorkUserList() {
      const data = {
        groupId: this.basicUserInfo.groupId,
        groupSplitId: this.basicUserInfo.groupSplitId,
        name: "",
      };
      getAllUserServiceByGroupSplit(data).then((res) => {
        if (res.code === 200) {
          res.data.forEach((item) => {
            if (item.id != this.basicUserInfo.id) {
              this.groupServiceList.push(item);
            }
          });
          this.tableHeight1 = this.$refs.tab2.clientHeight;
        }
      });
    },

    //选择转派人员
    changeService(data, type) {
      this.groupLeaderId = '';  //转派组长清空
      if (type) {
        this.chooseGroupData = data;
      } else {
        this.chooseData = data;
        // this.showChooseGroupLeader = true;
        this.haWorkUsers = data.haWorkUsers;
      }
    },
    //确定转派
    serviceTurn() {
      if (this.tabType === "2") {
        if (!this.groupLeaderId) {
          this.$message.warning("请选择转派组长");
          return;
        }
      }
      if (!this.turnMemo) {
        this.$message.warning("请填写转派原因");
        return;
      }
      const data = {
        groupId:
          this.tabType === "2"
            ? this.chooseData.groupId
            : this.chooseGroupData.groupId,
        queueId: this.baseUserInfo.id,
        turnMemo: this.turnMemo,
        groupLeaderId:
          this.tabType === "2" ? this.groupLeaderId : this.basicUserInfo.id,
      };
      this.turnServiceLoading = true;
      serviceTurn(data)
        .then((res) => {
          if (res.code === 200) {
            this.groupLeaderId = '';
            this.haWorkUsers = [];
            this.tableradioGroup = {};
            this.turnServiceLoading = false;
            this.reassignmentCurrentServiceIndex = this.currentServiceIndex;
            this.$emit(
              "spliceStaffInformation",
              this.reassignmentCurrentServiceIndex
            );
            if (this.tabType === "2") {
              this.$message.success("服务已转派请等待接收");
              this.getServiceTurnStatus(res.data.turnRecordId);
              this.$emit("changeServiceTransfer", false);
              this.$store.commit("setServiceOperateStatus", true);
            } else {
              this.groupServiceTurn(res.data.turnRecordId);
            }
          }
        })
        .catch((err) => {
          this.turnServiceLoading = false;
        });
    },

    //组长转派本组
    //转派
    groupServiceTurn(turnRecordId) {
      const params = {
        turnRecordId: turnRecordId,
        turnStatus: "2",
        rollbackMemo: this.turnMemo ?? "",
        workUserId: this.chooseGroupData.id,
      };
      turnServiceVerify(params)
        .then((res) => {
          if (res.code === 200) {
            this.$message.success("服务已转派");
            this.$store.commit("setServiceOperateStatus", true);
            this.$emit("changeServiceTransfer", false);
          }
        })
        .catch((err) => {
          console.log(err);
          this.showExamine = false;
          // this.getServiceTurnByNoVerify();
        });
    },

    getServiceTurnStatus(turnRecordId) {
      const that = this;
      this.timer = setInterval(() => {
        turnIsVerify({ turnRecordId: turnRecordId })
          .then((res) => {
            if (res.code === 200) {
              
              if (res.data.turnIsVerify != "1") {
                that.$emit('transferData',true);
                clearInterval(that.timer);
                if (res.data.turnIsVerify === "2") {
                  this.$message.success("服务转派成功");
                }
                if (res.data.turnIsVerify === "3") {
                  this.$message.warning(res.data.rollbackMemo);
                }
              }else{
                that.$emit('transferData',false);
              }
            }
          })
          .catch((err) => {
            console.log(err);
            clearInterval(this.timer);
          });
      }, 30000);
    },

    changeTab(type) {
      this.tableradioGroup = {}; //切换组别清空数据
      this.tabType = type;
      if (type === "1") {
        this.haType = this.basicUserInfo.haType;
      } else {
        this.haType = "";
      }
    },
    cancelDialog() {
      this.groupLeaderId = '';
      this.haWorkUsers = [];
      this.tableradioGroup = {};
      this.$emit("changeServiceTransfer", false);
    },
  },
  watch: {
    showServiceTransfer: {
      handler(val) {
        this.turnMemo = "";
        this.tableradioGroup = {};
      },
    },
  },
};
</script>
<style lang="scss" scoped>
  @import './css/serviceTransfer.scss';
</style>

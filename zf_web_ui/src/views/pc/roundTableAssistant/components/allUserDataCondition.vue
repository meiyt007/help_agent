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
    <div class="tabHeader">
      <p
          class="tabRight"
          @click="changeTab('1')"
          :class="tabType === '1' ? 'active' : ''"
          style="width:100%"
      >
        {{ basicUserInfo.groupName }}
      </p>
    </div>
    <div class="body-content" v-loading="loadingData">
      <div class="tab" ref="tab1" v-show="tabType === '1'">
        <el-table
            ref="table1"
            :data="groupServiceList"
            :cell-style="{ 'text-align': 'center' }"
            :header-cell-style="{ 'text-align': 'center' }"
            border
        >
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
    </div>
  </div>
</template>
<script>
import { getAllUserService } from "@/api/modules/helpAgent";
export default {
  name: "allUserDataCondition",
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
      tabType: "1",
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
        { label: "帮办人员", prop: "name"},
        { label: "帮办类型", prop: "haType"},
        { label: "等待人数", prop: "waitingNum"},
        { label: "正在服务人数", prop: "inServiceNum"},
        { label: "今日已服务人数", prop: "todayServiceNum"},
        { label: "预计等待时间", prop: "esuimateTime"},
        { label: "服务状态", prop: "status"},
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
    console.log("++++++++++++++++++++++++"+this.baseUserInfo)
    this.groupLeaderId = '';
    this.haType = this.basicUserInfo.haType;
    // this.changeTab('1');
    this.getWorkUserList();
  },
  methods: {


    getWorkUserList() {
      const data = {
        groupId: this.basicUserInfo.groupId,
        name: "",
      };
      getAllUserService(data).then((res) => {
        if (res.code === 200) {
          res.data.forEach((item) => {
            if (item.groupId === this.basicUserInfo.groupId) {
              this.groupServiceList.push(item);
            }
          });
          this.$nextTick(() => {
            this.$refs.table.doLayout();
          });
        }
      });
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
<style lang="scss">
@import './css/serviceTransfer.scss';
</style>

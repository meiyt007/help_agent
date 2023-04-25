<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-09 09:44:05
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-09 11:22:30
 * @FilePath: \zf_web_ui\src\layout\components\transferService.vue
 * @Description: 转派服务审核列表
-->
<template>
  <div class="transferService">
    <div class="tab-header">
      <div class="left">
        <el-input
          v-model="name"
          clearable
          @keyup.enter.native="getServiceTurnByNoVerify"
          placeholder="请输入办事人姓名"
        ></el-input>
      </div>
      <div class="right">
        <el-button
          @click="getServiceTurnByNoVerify"
          type="primary"
          icon="el-icon-search"
          >搜索</el-button
        >
      </div>
    </div>
    <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="turnVerifyList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in columnList">
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-if="item.prop === 'operate'"
            show-overflow-tooltip
            :width="item.width"
          >
            <template slot-scope="scope">
              <el-button type="primary" @click="examine(scope.row)"
                >审核</el-button
              >
            </template>
          </el-table-column>

          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else
            show-overflow-tooltip
            :width="item.width"
          >
          </el-table-column>
        </template>
      </el-table>
    </div>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[10, 20, 30, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <el-dialog
      title="审核"
      :visible.sync="showExamine"
      width="90%"
      custom-class="serviceRecord"
      v-dialogDrag
    >
      <div class="content-body">
        <el-form
          :model="serviceTransfer"
          :rules="serviceTransferRules"
          ref="serviceTransfer"
          label-width="10rem"
        >
          <el-form-item label="转派原因" prop="turnMemo">
            <el-input
              type="textarea"
              v-model="serviceTransfer.turnMemo"
              readonly
            ></el-input>
          </el-form-item>
          <el-form-item label="审核结果" prop="findingsAudit">
            <el-radio-group
              v-model="serviceTransfer.findingsAudit"
              @change="changefindingsAudit"
            >
              <el-radio label="2">通过</el-radio>
              <el-radio label="3">不通过</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-if="serviceTransfer.findingsAudit === '3'"
            label="不通过原因"
            prop="failureReason"
          >
            <el-input
              type="textarea"
              v-model="serviceTransfer.failureReason"
            ></el-input>
          </el-form-item>
        </el-form>
        <div
          class="tab1"
          ref="tab1"
          v-if="serviceTransfer.findingsAudit === '2'"
        >
          <el-table
            ref="table1"
            height="300"
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
                v-else
              ></el-table-column>
            </template>
            <el-table-column :key="Math.random()" width="50">
              <template slot-scope="scope">
                <el-radio
                  v-model="tableradioGroup"
                  :label="scope.row"
                  @change="changeService(scope.row, 'group')"
                  ><i />
                </el-radio>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div class="dialog-footer">
        <p @click="showExamine = false">取消</p>
        <p @click="serviceTurn">确定</p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getAllUserService } from "@/api/modules/helpAgent";
import {
  getServiceTurnByNoVerify,
  turnServiceVerify,
} from "@/api/modules/helpAgent.js";
export default {
  name: "transferService",
  props: {
    turnRecordId: {
      type: String,
      default: () => "",
    },
  },
  data() {
    return {
      loadingTableData: false,
      showExamine: false,
      turnVerifyList: [],
      groupServiceList: [],
      tableHeight: 300,
      columnList: [
        { label: "办事人姓名", prop: "name" },
        {
          label: "申请转派的帮办人员",
          prop: "handleWorkUserName",
          width: "",
        },
        { label: "转派时间", prop: "turnTime", width: "" },
        { label: "转派原因", prop: "turnMemo", width: "" },
        { label: "操作", prop: "operate", width: "" },
      ],
      assistantColumnList: [
        { label: "帮办人员", prop: "name" },
        { label: "所在分组", prop: "groupName" },
        { label: "帮办类型", prop: "haType" },
        { label: "正在服务人数", prop: "inServiceNum" },
        { label: "等待人数", prop: "waitingNum" },
        { label: "预计等待时间", prop: "esuimateTime" },
        { label: "服务状态", prop: "status" },
      ],
      name: "",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      rowData: {},
      chooseData: {},
      serviceTransfer: {
        turnMemo: "",
        findingsAudit: "",
        failureReason: "",
      },
      tableradioGroup: {},
      serviceTransferRules: {
        findingsAudit: [
          { required: true, message: "请选择审核结果", trigger: "change" },
        ],
        failureReason: [
          { required: true, message: "请输入不通过原因", trigger: "blur" },
        ],
      },
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  mounted() {
    this.getServiceTurnByNoVerify();
    this.getWorkUserList();
  },
  methods: {
    //选择转派人员
    changeService(data, type) {
      if (type === "group") {
        this.tableradioAll = {};
      }
      if (type === "all") {
        this.tableradioGroup = {};
      }
      this.chooseData = data;
    },
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
    changefindingsAudit() {
      console.log(this.serviceTransfer.findingsAudit);
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getServiceTurnByNoVerify();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getServiceTurnByNoVerify();
    },
    //获取转派服务待审核列表
    getServiceTurnByNoVerify() {
      const params = {
        name: this.name,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.loadingTableData = true;
      getServiceTurnByNoVerify(params)
        .then((res) => {
          this.loadingTableData = false;
          if (res.code === 200) {
            this.tableHeight = this.$refs.tableArea.clientHeight;
            this.turnVerifyList = res.data.data;
            this.total = res.data.total;
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingTableData = false;
        });
    },
    //打开审核弹窗
    examine(row) {
      this.showExamine = true;
      this.rowData = row;
      this.serviceTransfer.turnMemo = row.turnMemo;
    },
    //转派
    serviceTurn() {
      this.$refs.serviceTransfer.validate((valid) => {
        if (valid) {
          this.turnServiceVerify();
        }
      });
    },
    turnServiceVerify() {
      const params = {
        turnRecordId: this.turnRecordId,
        turnStatus: this.serviceTransfer.findingsAudit,
        rollbackMemo: this.serviceTransfer.failureReason ?? "",
        workUserId:
          this.serviceTransfer.findingsAudit === "2"
            ? this.chooseData.id
            : this.rowData.oldServiceWorkUserId,
      };
      if (!params.workUserId && this.serviceTransfer.findingsAudit === "2") {
        this.$message.warning("请选择转派人员");
        return;
      }
      turnServiceVerify(params)
        .then((res) => {
          if (res.code === 200) {
            this.showExamine = false;
            if (this.serviceTransfer.findingsAudit === "2") {
              this.$message.success("转派成功");
            } else {
              this.$message.success("拒绝转派");
            }

            this.getServiceTurnByNoVerify();
          }
        })
        .catch((err) => {
          console.log(err);
          this.showExamine = false;
          this.getServiceTurnByNoVerify();
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.transferService {
  width: 100%;
  height: 100%;
  .tab-header {
    width: 100%;
    height: 5.7143rem;
    display: flex;
    align-content: center;
    justify-content: space-between;
    .left {
      width: 50%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      .el-input {
        width: calc(100% - 5rem);
        margin-left: 1.5rem;
      }
    }
    .right {
      width: 50%;
      display: flex;
      align-items: center;
      justify-content: flex-end;
    }
  }
  .tableArea {
    width: 100%;
    height: calc(100% - 10rem);
  }
  .el-pagination {
    margin-top: 1rem;
  }
  .el-form {
    width: 100%;
    height: auto;
    .el-form-item {
      width: 100%;
      height: 5rem;
      ::v-deep .el-form-item__label {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 1.5rem;
      }
      ::v-deep .el-form-item__content {
        width: calc(100% - 10rem);
        height: 100%;
        .el-radio-group {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;
        }
        .el-form-item__error {
          line-height: 20px;
        }
      }
    }
  }
}
::v-deep .el-dialog {
  height: 80vh !important;

  .el-dialog__body {
    height: calc(100% - 3.75rem);
    max-height: 100%;
  }
}
.dialog-footer {
  width: 100%;
  height: 4.2857rem;
  margin-top: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;

  p {
    width: auto;
    height: 100%;
    padding: 0 3rem;
    border-radius: 0.7143rem;
    display: flex;
    align-items: center;
    justify-content: center;

    &:nth-child(1) {
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      color: #2473ff;
      margin-right: 2.1875rem;
      cursor: pointer;
    }

    &:nth-child(2) {
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      color: #ffffff;
    }
  }
}
</style>

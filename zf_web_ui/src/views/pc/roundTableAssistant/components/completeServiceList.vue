<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-23 14:51:38
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-13 13:40:34
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\completeServiceList.vue
 * @Description: 完成服务列表
-->
<template>
  <div class="app-container">
    <div class="tab-header">
      <el-form :model="searchParams" ref="searchForm">
        <el-form-item label="办理日期">
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="dateValue"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :append-to-body="false"
            @change="changeDate"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="办事人姓名">
          <el-input v-model="searchParams.name"></el-input>
        </el-form-item>
        <el-form-item label="办事人手机号">
          <el-input v-model="searchParams.phone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="getQueryHelpServiceHistoryList"
            type="primary"
            size="medium"
            icon="el-icon-search"
            >搜索</el-button
          >
          <el-button
            size="medium"
            @click="resetData"
            icon="el-icon-refresh-right"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="completeServiceList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in columnList">
          <el-table-column
            v-if="item.prop === 'serviceStatus'"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{
                scope.row.serviceStatus === "1"
                  ? "等待服务"
                  : scope.row.serviceStatus === "2"
                  ? "服务中"
                  : "服务完成"
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else-if="item.prop === 'operate'"
          >
            <template slot-scope="scope">
              <span class="operate" @click="toDetail(scope.row)">查看</span>
            </template>
          </el-table-column>
          <el-table-column
            v-else
            :key="index"
            :label="item.label"
            :prop="item.prop"
            show-overflow-tooltip
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
      title="服务记录"
      :visible.sync="showServiceRecord"
      width="90%"
      custom-class="serviceRecord"
      v-dialogDrag
    >
      <serviceRecord v-if="showServiceRecord" :serviceData="serviceData" />
    </el-dialog>
  </div>
</template>
<script>
// import { queryHelpServiceHistoryList } from "@/api/modules/workingGroup";
import { queryAllWorkQueueListBylogin } from "@/api/modules/helpAgent";
import { second } from "@/utils/index";
import serviceRecord from "../../operationFlow/components/serviceRecord.vue";
export default {
  name: "stagingServiceList",
  components: {
    serviceRecord,
  },
  data() {
    return {
      loadingTableData: false,
      showServiceRecord: false,
      completeServiceList: [],
      serviceData: {},
      tableHeight: 200,
      dateValue: "",
      searchParams: {
        beginTime: null,
        endTime: null,
        name: "",
        phone: "",
      },
      columnList: [
        { label: "办事人姓名", prop: "name" },
        { label: "办事人手机号", prop: "phone" },
        { label: "办理日期", prop: "serviceBeginTime" },
        { label: "服务时长", prop: "serviceDuration" },
        { label: "服务状态", prop: "serviceStatus" },
        { label: "备注", prop: "adviseMemo" },
        { label: "操作", prop: "operate" },
      ],
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  mounted() {
    this.getQueryHelpServiceHistoryList();
  },
  methods: {
    //切换时间
    changeDate() {
      this.searchParams.beginTime = this.dateValue[0];
      this.searchParams.endTime = this.dateValue[1];
    },
    resetData() {
      this.searchParams = {
        beginTime: null,
        endTime: null,
        name: "",
        phone: "",
      };
      this.dateValue = "";
      this.getQueryHelpServiceHistoryList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getQueryHelpServiceHistoryList();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getQueryHelpServiceHistoryList();
    },

    //查看服务详细
    toDetail(data) {
      this.showServiceRecord = true;
      this.serviceData = data;
    },

    //获取完成服务列表
    getQueryHelpServiceHistoryList() {
      const params = {
        name: this.searchParams.name,
        phone: this.searchParams.phone,
        cardNo: "",
        companyName: "",
        queueStatus: "",
        serviceStatus: "3",
        beginTime: this.searchParams.beginTime
          ? this.searchParams.beginTime + " 00:00:00"
          : "",
        endTime: this.searchParams.endTime
          ? this.searchParams.endTime + " 23:59:59"
          : "",
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.loadingTableData = true;
      queryAllWorkQueueListBylogin(params)
        .then((res) => {
          this.loadingTableData = false;
          if (res.code === 200) {
            this.tableHeight = this.$refs["tableArea"].clientHeight;
            this.completeServiceList = res.data.data;
            this.total = res.data.total;
            this.completeServiceList.forEach((item) => {
              if (item.serviceDuration) {
                item.serviceDuration = second(item.serviceDuration);
              }
              if (item.companyName) {
                item.applyUserName = item.companyName;
              } else {
                item.applyUserName = item.name;
              }
            });
          }
        })
        .catch((err) => {
          this.loadingTableData = false;
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.app-container {
  width: 100%;
  height: 100%;
  .tab-header {
    width: 100%;
    height: 5.7143rem;
    padding: 0.7143rem 0;

    .el-form {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .el-form-item {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 23%;
        &:nth-child(1) {
          width: 30%;
          ::v-deep .el-form-item__content {
            .el-date-editor {
              .el-range-separator {
                width: 2rem;
              }
            }
          }
        }
        &:nth-last-child(1) {
          width: 16%;
        }
        ::v-deep .el-form-item__label {
          width: 10rem;
          height: 100%;
        }

        ::v-deep .el-form-item__content {
          flex: 1;

          .el-input {
            width: 100%;
          }

          .el-date-editor {
            width: 100%;
          }
        }
      }
    }
  }

  .tableArea {
    width: 100%;
    height: calc(100% - 12rem);
  }
  .el-pagination {
    margin-top: 1.5rem;
    text-align: right;
  }
  .operateArea {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    p {
      margin: 0;
      cursor: pointer;
      width: auto;
      height: 2.5rem;
      padding: 0 1.2rem;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 1.25rem;
      font-size: 1.2857rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      &:nth-child(1) {
        background-color: #3ba6ff;
        color: #fff;
      }
      &:nth-child(2) {
        margin-left: 1.2rem;
        background: #edad0a;
        color: #fff;
      }
    }
  }
}
.operate {
  cursor: pointer;
  color: #3ba6ff;
}
</style>

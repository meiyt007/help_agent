<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-26 10:31:12
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-23 13:30:38
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\visitingRecords.vue
 * @Description: 来访记录
-->
<template>
  <div class="visitingRecords">
    <div class="tab-header">
      <el-form :model="searchParams">
        <el-form-item label="来访日期">
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
          <!-- <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="searchParams.beginTime"
            type="date"
            placeholder="选择日期"
            :append-to-body="false"
          >
          </el-date-picker> -->
        </el-form-item>
        <el-form-item label="服务状态">
          <el-select
            v-model="searchParams.serviceStatus"
            placeholder="请选择"
            :popper-append-to-body="false"
          >
            <el-option
              v-for="item in serviceStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="getQueryAllWorkQueueList"
            type="primary"
            icon="el-icon-search"
            >搜索</el-button
          >
          <el-button @click="resetData" icon="el-icon-refresh-right"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="visitingRecordList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <template v-for="(item, index) in columnList">
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-if="item.prop === 'serviceStatus'"
            show-overflow-tooltip
            :width="item.width"
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
            show-overflow-tooltip
            :width="item.width"
          >
            <template slot-scope="scope">
              <p class="seeOp" @click="toServiceRecord(scope.row)">
                <span>查看</span>
              </p>
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
      title="服务记录"
      :visible.sync="showServiceRecord"
      width="90%"
      custom-class="serviceRecord"
      v-dialogDrag
    >
      <serviceRecord :serviceData="serviceData" />
    </el-dialog>
  </div>
</template>
<script>
import { queryAllWorkQueueListBylogin } from "@/api/modules/helpAgent";
import serviceRecord from "./serviceRecord.vue";
import { second } from "@/utils/index";
export default {
  name: "VisitingRecords",
  components: {
    serviceRecord,
  },
  props: {
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      tableHeight: 300,
      visitingRecordList: [],
      showServiceRecord: false,
      loadingTableData: false,
      dateValue: "",
      searchParams: {
        beginTime: null,
        endTime: null,
        serviceStatus: null,
      },
      columnList: [
        // { label: '经办企业信息', prop: 'companyInfo', width: '200' },
        { label: "来访时间", prop: "createDate", width: "200" },
        { label: "办事人姓名", prop: "name", width: "" },
        { label: "服务开始时间", prop: "serviceBeginTime", width: "" },
        { label: "服务结束时间", prop: "serviceEndTime", width: "" },
        { label: "服务时长", prop: "serviceDuration", width: "" },
        { label: "服务人员", prop: "serviceWorkUserName", width: "" },
        { label: "服务状态", prop: "serviceStatus", width: "" },
        { label: "操作", prop: "operate" },
      ],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      serviceData: {},
      serviceStatusOptions: [
        { label: "全部", value: "" },
        { label: "等待服务", value: "1" },
        { label: "服务中", value: "2" },
        { label: "服务完成", value: "3" },
      ],
    };
  },
  mounted() {
    this.getQueryAllWorkQueueList();
  },
  methods: {
    //切换时间
    changeDate() {
      this.searchParams.beginTime = this.dateValue[0];
      this.searchParams.endTime = this.dateValue[1];
    },
    //查看服务记录
    toServiceRecord(data) {
      this.showServiceRecord = true;
      this.serviceData = data;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getQueryAllWorkQueueList();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getQueryAllWorkQueueList();
    },
    resetData() {
      this.searchParams = {
        serviceStatus: null,
        beginTime: null,
        endTime: null,
      };
      this.getQueryAllWorkQueueList();
    },
    getQueryAllWorkQueueList() {
      const params = {
        name: "",
        cardNo: this.baseUserInfo ? this.baseUserInfo.cardNo : "",
        companyName: "",
        queueStatus: "",
        serviceStatus: this.searchParams.serviceStatus ?? "",
        beginTime: this.searchParams.beginTime ?this.searchParams.beginTime+' 00:00:00': "",
        endTime: this.searchParams.endTime ?this.searchParams.endTime+' 23:59:59':"",
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.loadingTableData = true;
      queryAllWorkQueueListBylogin(params)
        .then((res) => {
          this.tableHeight = this.$refs.tableArea.clientHeight;
          this.loadingTableData = false;
          if (res.code === 200) {
            this.visitingRecordList = res.data.data;
            this.visitingRecordList.forEach((item) => {
              if (item.serviceDuration) {
                item.serviceDuration = second(item.serviceDuration);
              }
              if (item.serviceBeginTime) {
                item.serviceBeginTime = item.serviceBeginTime.split(" ")[1];
              }
              if (item.serviceEndTime) {
                item.serviceEndTime = item.serviceEndTime.split(" ")[1];
              }
              item.companyInfo =
                item.companyName +
                (item.companyCode ? `(${item.companyCode})` : "");
            });
            this.total = res.data.total;
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingTableData = false;
        });
    },
  },
  watch: {},
};
</script>
<style lang="scss" scoped>
.visitingRecords {
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
        &:nth-child(1) {
          width: 45%;
        }
        &:nth-child(2) {
          width: 28%;
        }
        &:nth-child(3) {
          width: 23%;
        }

        ::v-deep .el-form-item__label {
          width: 8rem;
          height: 100%;
        }

        ::v-deep .el-form-item__content {
          flex: 1;

          .el-select {
            width: 80%;
          }

          .el-date-editor {
            width: 80%;
          }
        }
      }
    }
  }

  .tableArea {
    width: 100%;
    height: calc(100% - 10rem);
  }
}

.el-pagination {
  text-align: right;
  margin-top: 1rem;
  padding-right: 0;
}

.seeOp {
  padding: 0;
  margin: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  span {
    margin: 0;
    cursor: pointer;
    width: 5.7857rem;
    height: 2.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 1.25rem;
    background-color: #d3e6f5;
    font-size: 1.2857rem;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #3ba6ff;
  }
}

::v-deep .el-dialog {
  height: 80vh !important;

  .el-dialog__body {
    height: calc(100% - 3.75rem);
    max-height: 100%;
  }
}
</style>

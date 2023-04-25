<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-26 11:52:24
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-08 09:14:08
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\serviceRecord.vue
 * @Description: 服务记录
-->
<template>
  <div class="serviceRecord-content">
    <div class="tab-header"></div>
    <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="serviceRecordList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <template v-for="(item, index) in columnList">
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-if="item.prop === 'sxServiceName'"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <p class="">
                {{ scope.row.sxServiceName }}
              </p>
              <!-- <p class="serviceName" @click="toOnceInfo(scope.row)">
                {{ scope.row.sxServiceName }}
              </p> -->
            </template>
          </el-table-column>
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else-if="item.prop === 'serviceMemo'"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span v-html="scope.row.serviceMemo"></span>
            </template>
          </el-table-column>
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else-if="item.prop === 'serviceType'"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{
                scope.row.serviceType === "1"
                  ? "咨询"
                  : scope.row.serviceType === "2"
                  ? "材料准备"
                  : scope.row.serviceType === "3"
                  ? "收件"
                  : "一键推送"
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
              v-else-if="item.prop === 'pushType'"
              show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{
                scope.row.pushType === "1"
                    ? "办事指南"
                    : scope.row.pushType === "2"
                    ? "办件信息"
                    : scope.row.pushType === "3"
                        ? "材料信息"
                        : ""
                }}</span>
            </template>
          </el-table-column>
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else-if="item.prop === 'serviceStatus'"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span v-if="scope.row.serviceStatus">{{
                scope.row.serviceStatus === "1" ? "已完成" : "暂存"
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else-if="item.prop === 'qlCaseId'"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <!-- <p class="serviceName" @click="toHandlingInformation(scope.row)">
                {{ scope.row.qlCaseId }}
              </p> -->
              <p class="">
                {{ scope.row.qlCaseId }}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else
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
      title="办事指南"
      :visible.sync="guidelinesForHandlingAffairsDialog"
      width="90%"
      class="guidelinesForHandlingAffairs"
      v-dialogDrag
    >
      <onceInformed
        v-if="guidelinesForHandlingAffairsDialog"
        :serviceOid="serviceOid"
        :caseOid="caseOid"
        :showSituation="true"
      >
      </onceInformed>
    </el-dialog>
    <el-dialog
      title="办件信息"
      :visible.sync="handlingInformation"
      width="90%"
      class="guidelinesForHandlingAffairs"
      v-dialogDrag
    >
      <handlingInformation
        v-if="handlingInformation"
        :caseOid="caseOid"
        :useInfo="useInfo"
      >
      </handlingInformation>
    </el-dialog>
  </div>
</template>
<script>
import { queueServiceList } from "@/api/modules/helpAgent";
import onceInformed from "./onceInformed.vue";
import handlingInformation from "./handlingInformation.vue";
export default {
  name: "ServiceRecord",
  components: {
    onceInformed,
    handlingInformation,
  },
  props: {
    serviceData: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      loadingTableData: false,
      guidelinesForHandlingAffairsDialog: false,
      handlingInformation: false,
      tableHeight: 300,
      serviceRecordList: [],
      serviceOid: "",
      caseOid: "",
      useInfo: {},
      columnList: [
        { label: "办事人姓名", prop: "name" },
        { label: "经办企业信息", prop: "companyName" },
        { label: "事项名称", prop: "sxServiceName" },
        { label: "服务内容", prop: "serviceMemo" },
        { label: "服务类型", prop: "serviceType" },
        { label: "推送类型", prop: "pushType" },
        { label: "推送时间", prop: "pushDate" },
        { label: "办件编号", prop: "qlCaseId" },
        { label: "服务状态", prop: "serviceStatus" },
      ],
      pageSize: 10,
      pageNum: 1,
      total: 0,
    };
  },
  mounted() {
    this.getQueueServiceList();
  },
  methods: {
    //打开办事指南弹窗
    toOnceInfo(row) {
      this.caseOid = row.qlCaseId;
      this.serviceOid = row.sxServiceId;
      this.guidelinesForHandlingAffairsDialog = true;
    },
    //打开办件详情
    toHandlingInformation(data) {
      this.caseOid = data.qlCaseId;
      this.useInfo = data;
      this.handlingInformation = true;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getQueueServiceList();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getQueueServiceList();
    },
    getQueueServiceList() {
      const data = {
        queueId: this.serviceData.id ?? this.serviceData.queueId,
        serviceStatus: this.serviceData.serviceStatus,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      queueServiceList(data).then((res) => {
        if (res.code === 200) {
          this.tableHeight = this.$refs.tableArea.clientHeight;
          this.serviceRecordList = res.data.data;
          this.total = res.data.total;
          this.serviceRecordList.forEach((item) => {
            if (item.serviceMemo) {
              let arr = decodeURI(item.serviceMemo);
              if (arr.indexOf("[") > -1) {
                arr = JSON.parse(arr);
                console.log(typeof arr);
                arr.forEach((ite) => {
                  item.serviceMemo = ite.answer;
                });
              } else {
                item.serviceMemo = arr;
              }
            }
          });
        }
      });
    },
  },
  watch: {
    serviceData: {
      handler(val) {
        this.getQueueServiceList();
      },
    },
  },
};
</script>
<style lang="scss" scoped>
.serviceRecord-content {
  width: 100%;
  height: 100%;

  .tab-header {
    width: 100%;
    height: 3.7143rem;
  }

  .tableArea {
    width: 100%;
    height: calc(100% - 8rem);

    .serviceName {
      color: rgb(48, 99, 239);
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }

  .el-pagination {
    text-align: right;
    margin-top: 1rem;
    padding-right: 0;
  }
}

.guidelinesForHandlingAffairs {
  ::v-deep .el-dialog {
    height: 80vh;
  }
}
</style>

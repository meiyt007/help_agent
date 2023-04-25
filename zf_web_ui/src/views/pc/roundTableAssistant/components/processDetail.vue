<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-23 13:07:46
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-13 13:33:31
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\stagingServiceList.vue
 * @Description: 一桌联办服务列表
-->
<template>
    <div class="app-container">
      <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
        <el-table
          ref="table"
          :height="tableHeight"
          :data="stagingServiceList"
          :cell-style="{ 'text-align': 'center' }"
          :header-cell-style="{ 'text-align': 'center' }"
          border
        >
          <el-table-column type="index" width="50" label="序号">
          </el-table-column>
          <template v-for="(item, index) in columnList">
            <el-table-column
              v-if="item.prop === 'serviceType'"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <div class="">
                  <div v-if="scope.row.serviceType == '1'">咨询</div>
                  <div v-else-if="scope.row.serviceType == '2'">材料准备</div>
                  <div v-else-if="scope.row.serviceType == '3'">收件</div>
                  <div v-else-if="scope.row.serviceType == '4'">一键推送</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-else-if="item.prop === 'serviceStatus'"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <div class="">
                  <div v-if="scope.row.serviceStatus == '1'">已完成</div>
                  <div v-else-if="scope.row.serviceStatus == '2'">暂存（需要后续提交）</div>
                </div>
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
    </div>
  </template>
  <script>
  // import { queueServiceList } from "@/api/modules/workingGroup";
  import { queueServiceList } from "@/api/modules/helpAgent";
  export default {
    name: "processDetail",
    components: {
    },
    props:{
        deskId: {
            type: String,
            default: () => "",
        },
        queueId: {
            type: String,
            default: () => "",
        },
    },
    data() {
      return {
        loadingTableData: false,
        stagingServiceList: [],
        tableHeight: 200,
        searchParams: {
          date: "",
          name: "",
          phone: "",
        },
        columnList: [
          { label: "办事人姓名", prop: "name" },
          { label: "经办企业信息", prop: "companyName" },
          { label: "事项名称", prop: "sxServiceName" },
          { label: "服务类型", prop: "serviceType" },
          { label: "办件编号", prop: "caseNumber" },
          { label: "服务状态", prop: "serviceStatus" },
        ],
        pageNum: 1,
        pageSize: 10,
        total: 0,
        handlingInformation: false,
        dateValue: "",
        caseOid: "",
        useInfo: {},
        input3:'',
        select:'companyCode',
        processDetailShow:false,
      };
    },
    computed: {
      staffInformation() {
        return this.$store.state.pageData.staffInformation;
      },
    },
    mounted() {
      this.getQueryHelpServiceHistoryList();
    },
    methods: {
      closeDepart(){
          this.handlingInformation = false;
          this.getQueryHelpServiceHistoryList();
      },
      //切换时间
      changeDate() {
        this.searchParams.beginTime = this.dateValue[0];
        this.searchParams.endTime = this.dateValue[1];
      },
  
      //继续办件
      proceedStep(row) {
        this.$emit("setProceedStepData", row);
      },
  
      resetData() {
        this.searchParams = {
          date: "",
          name: "",
          phone: "",
          input3:''
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
      //获取暂存办件记录
      getQueryHelpServiceHistoryList() {
        const params = {
          queueId:this.queueId,
          serviceStatus:'',
          pageNum:this.pageNum,
          pageSize:this.pageSize,
        };
        this.loadingTableData = true;
        queueServiceList(params)
          .then((res) => {
            this.loadingTableData = false;
            if (res.code === 200) {
              this.tableHeight = this.$refs["tableArea"].clientHeight;
              this.stagingServiceList = res.data.data;
              this.total = res.data.total;
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
            min-width: 200px;
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
      height: calc(100% - 11rem);
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
      padding-left: 10%;
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
  </style>
  
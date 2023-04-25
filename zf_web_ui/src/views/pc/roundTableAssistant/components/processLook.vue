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
      <div class="tab-header">
      </div>
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
              v-if="item.prop === 'operate'"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              width="200"
            >
              <template slot-scope="scope">
                <div class="operateArea">
                  <p v-if="deskStatus == '3' && scope.row.deskQueueId" @click="lookDetail(scope.row)">查看</p>
                  <p v-else  class="disabled">查看</p>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-else-if="item.prop === 'confirmFlag'"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <div class="">
                  <div v-if="scope.row.confirmFlag == '0'">未确认</div>
                  <div v-else-if="scope.row.confirmFlag == '1'">已确认</div>
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
    <!-- 联办进度详情-->
      <el-dialog
        title="联办进度查看"
        :visible.sync="processDetailShow"
        width="90%"
        class="guidelinesForHandlingAffairs"
        v-dialogDrag
      >
        <processDetail
          v-if="processDetailShow"
          :queueId="queueId"
          @closeDepart="closeDepart"
        >
        </processDetail>
      </el-dialog>
    </div>
  </template>
  <script>
  // import { queryHelpServiceHistoryList } from "@/api/modules/workingGroup";
  import { getAppCondition } from "@/api/modules/helpAgent";
  import processDetail from  "./processDetail.vue";
  export default {
    name: "processLook",
    components: {
        processDetail
    },
    props:{
        deskId: {
            type: String,
            default: () => "",
        },
        deskStatus: {
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
          { label: "联办部门", prop: "groupName" },
          { label: "预约状态", prop: "confirmFlag" },
          { label: "预约确认时间", prop: "conDate" },
          { label: "备注", prop: "desc" },
          { label: "操作", prop: "operate" },
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
        queueId:''
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
        lookDetail(row){
            this.queueId = row.deskQueueId;
            this.processDetailShow = true;
        },
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
            deskId:this.deskId
        };
        this.loadingTableData = true;
        getAppCondition(params)
          .then((res) => {
            this.loadingTableData = false;
            if (res.code === 200) {
              this.tableHeight = this.$refs["tableArea"].clientHeight;
              this.stagingServiceList = res.data;
              this.total = res.data.length;
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
        &.disabled{
          background: rgb(190, 190, 190);
          cursor: none;
        }
      }
    }
  }
  </style>
  
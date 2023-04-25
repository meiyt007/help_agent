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
        <el-form :model="searchParams" ref="searchForm">
            <el-input style="width:60%;margin-bottom: 15px;" placeholder="请输入查询内容" v-model="input3" class="input-with-select">
            <el-select v-model="select" slot="prepend" style="width:160px">
                <el-option label="统一社会信用代码" value="companyCode">统一社会信用代码</el-option>
                <el-option label="企业名称" value="companyName">企业名称</el-option>
            </el-select>
        </el-input>
          <el-form-item>
            <el-button
              @click="getQueryHelpServiceHistoryList"
              type="primary"
              icon="el-icon-search"
              size="medium"
              >搜索</el-button>
            <!-- <el-button
              size="medium"
              @click="resetData"
              icon="el-icon-refresh-right"
              >重置</el-button> -->
          </el-form-item>
        </el-form>
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
          <template v-for="(item, index) in columnListPro">
            <el-table-column
              v-if="item.prop === 'operate'"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              width="200"
            >
              <template slot-scope="scope">
                <div class="operateArea">
                  <p @click="seeDetail(scope.row)">发起联办</p>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-else-if="item.prop === 'deskStatus'"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <div class="appoint-status">
                  <div class="toAppoint" v-if="scope.row.deskStatus == '0'">未预约</div>
                  <div v-else-if="scope.row.deskStatus == '1'">预约中</div>
                  <div v-else-if="scope.row.deskStatus == '2'">已预约</div>
                  <div v-else-if="scope.row.deskStatus == '3'">已发起一桌联办</div>
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
      <el-dialog
        title="一桌联办"
        :visible.sync="handlingInformation"
        width="90%"
        class="guidelinesForHandlingAffairs"
        custom-class="serviceTransfer"
        v-dialogDrag
      >
        <depart
          v-if="handlingInformation"
          :deskId="deskId"
          :deskQueueId="deskQueueId"
          @closeDepart="closeDepart"
        >
        </depart>
      </el-dialog>
    </div>
  </template>
  <script>
  // import { queryHelpServiceHistoryList } from "@/api/modules/workingGroup";
  import { getListWithPage } from "@/api/modules/helpAgent";
  import depart from "./depart.vue";
  export default {
    name: "stagingServiceList",
    components: {
      depart,
    },
    props: {
      isHelp: {
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
          { label: "企业名称", prop: "companyName" },
          { label: "经办人", prop: "haWorkQueueVo.name" },
          { label: "经办人手机号", prop: "haWorkQueueVo.phone" },
          { label: "预约时间", prop: "appDate" },
          { label: "操作", prop: "operate" },
        ],
        columnListGroup: [
          { label: "办事主体名称", prop: "haDeskVo.haWorkQueueVo.companyName" },
          { label: "预约时间", prop: "haDeskVo.appDate" },
          { label: "上次办理日期", prop: "haDeskVo.haWorkQueueVo.serviceBeginTime" },
          { label: "预约发起人", prop: "haDeskVo.workUserName" },
          { label: "备注", prop: "desc" },
          { label: "预约状态", prop: "haDeskVo.deskStatus" }
        ],
        columnListPro:[],
        pageNum: 1,
        pageSize: 10,
        total: 0,
        handlingInformation: false,
        dateValue: "",
        caseOid: "",
        useInfo: {},
        input3:'',
        select:'companyCode',
        deskId:'',
        deskQueueId:'',
        processLookShow:false,
      };
    },
    computed: {
      staffInformation() {
        return this.$store.state.pageData.staffInformation;
      },
    },
    mounted() {
      //是否部门老师  1是 2否
      if(this.isHelp == '1'){
        this.columnListPro = this.columnListGroup;
      }else{
        this.columnListPro = this.columnList;
      }
      this.getQueryHelpServiceHistoryList();
    },
    methods: {
      closeDepart(){
          this.processLookShow = false;
          this.handlingInformation = false;
          this.getQueryHelpServiceHistoryList();
      },
      //切换时间
      changeDate() {
        this.searchParams.beginTime = this.dateValue[0];
        this.searchParams.endTime = this.dateValue[1];
      },
      //发起联办
      seeDetail(row) {
        console.log(row);
        this.deskId = row.id;
        this.deskQueueId = row.queueId;
        this.handlingInformation = true;
      },
      lookDetail(row){
        this.processLookShow = true;
        this.deskId = row.id;
        this.deskQueueId = row.queueId;
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
        let nameVal = '';
        let codeVal = '';
        if(this.select == 'companyName'){
          nameVal = this.input3;
        }else{
          codeVal = this.input3;
        }
        const params = {
          name: this.searchParams.name,
          phone: this.searchParams.phone,
          companyName: nameVal,
          companyCode:codeVal,
          deskStatus: "2",
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
        getListWithPage(params)
          .then((res) => {
            this.loadingTableData = false;
            if (res.code === 200) {
              this.tableHeight = this.$refs["tableArea"].clientHeight;
              this.stagingServiceList = res.data.data;
              this.total = res.data.total;
              this.stagingServiceList.forEach((item) => {
                item.staging = false;
                if (this.staffInformation.length) {
                  this.staffInformation.forEach((ite) => {
                    if (
                      item.name === ite.name &&
                      item.phone === ite.phone &&
                      item.companyCode === ite.companyCode
                    ) {
                      item.staging = true;
                    }
                  });
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
    .appoint-status{
      &>div{
        color: rgb(23, 132, 252);
      }
      .toAppoint{
        color: rgb(189, 49, 36);
      }
    }
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
  
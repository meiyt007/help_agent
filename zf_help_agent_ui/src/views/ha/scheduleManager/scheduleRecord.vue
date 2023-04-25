<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-22 13:57:21
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-27 09:10:34
 * @FilePath: \zf_help_agent_ui\src\views\ha\scheduleManager\scheduleRecord.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="app-container container">
    <el-form :model="queryParams" ref="queryMessage" :inline="true" label-width="110px">
      <el-form-item label="月份" prop="yearMonth">
        <el-date-picker
          v-model="queryParams.yearMonth"
          type="month"
          value-format="yyyy-MM"
          @change="changeMonth"
          placeholder="选择月">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="小组" prop="groupId">
        <el-select v-model.trim="queryParams.groupId" placeholder="请选择小组" size="small">
          <el-option
            v-for="group in groupList"
            :key="group.groupId"
            :label="group.name"
            :value="group.groupId">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="姓名" prop="workUserName">
        <el-input v-model.trim="queryParams.workUserName" placeholder="请输入姓名" clearable/>
      </el-form-item>

      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-date" size="mini" @click="handleInit">
          生成该月排班
        </el-button>
        <el-button type="primary" icon="el-icon-date" size="mini" @click="handleDelect">
          删除该月排班
        </el-button>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          搜索
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">
          重置
        </el-button>
      </el-form-item>
    </el-form>
    <div class="tableArea" ref="tableArea" v-loading="loadingData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="scheduleRecordList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <template v-for="(item, index) in columnList">
          <el-table-column
            v-if="item.prop.indexOf('amOrPm')>-1"
            :key="index"
            :label="item.label"
            :prop="item.prop"
          >
            <template #header>
              <p>{{item.label.split(',')[0]}}</p>
              <p>{{item.label.split(',')[1]}}</p>
            </template>
            <template slot-scope="scope">
              <div class="shift" :class="scope.row[MonthDayMenu[index-3].status]?'reservable':'beOverdue'"
                   @click="changeModifyInfo(scope.row,scope.row[MonthDayMenu[index-3].prop],scope.row[MonthDayMenu[index-3].scheduleStatus],scope.row[MonthDayMenu[index-3].id],MonthDayMenu[index-3].date,scope.row[MonthDayMenu[index-3].status])">
                <p class="shiftType" :class="`shiftType_${scope.row[MonthDayMenu[index-3].prop]}`">
                  {{scope.row[MonthDayMenu[index - 3].prop] === '1' ? '上午' : scope.row[MonthDayMenu[index - 3].prop] === '2' ? '下午' : scope.row[MonthDayMenu[index - 3].prop] === '3' ? '全天' : scope.row[MonthDayMenu[index - 3].prop] === '4' ? '休息' : '/'}}</p>
                <p class="scheduleStatus"
                   :class="scope.row[MonthDayMenu[index-3].scheduleStatus]?`scheduleStatus_${scope.row[MonthDayMenu[index-3].scheduleStatus]}`:''">
                  {{scope.row[MonthDayMenu[index - 3].scheduleStatus] === '1' ? '可约' : scope.row[MonthDayMenu[index - 3].scheduleStatus] === '2' ? '停约' : ''}}</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            v-else
            :key="index"
            :label="item.label"
            :prop="item.prop"
            :width="item.width"
            fixed="left"
            show-overflow-tooltip
          >
          </el-table-column>
        </template>
      </el-table>
    </div>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                style="padding: 30px 0; text-align: center;" layout="total, sizes, prev, pager, next, jumper"
                @pagination="getQueryScheduleList"/>
    <el-dialog
      title="修改排班班次"
      :visible.sync="showModifyInfo"
      width="80%"
      v-dialogDrag
      append-to-body
    >
      <div class="userInfoList">
        <div class="essentialinformationForm">
          <el-form
            :model="userInfo"
            class="baseFillInfo"
            :rules="userInfoRules"
            ref="baseFillInfo"
            label-width="13.85rem"
          >
            <el-form-item label="工号：" prop="workNum">
              <el-input v-model="userInfo.workNum" disabled></el-input>
            </el-form-item>
            <el-form-item label="姓名：" prop="name">
              <el-input v-model="userInfo.name" disabled></el-input>
            </el-form-item>
            <el-form-item label="帮办小组：" prop="helpGroup">
              <el-input v-model="userInfo.helpGroup" disabled></el-input>
            </el-form-item>
            <el-form-item label="要修改日期：" prop="modifyDate">
              <el-input v-model="userInfo.modifyDate" disabled></el-input>
            </el-form-item>
            <el-form-item label="排班班次：" prop="amOrPm">
              <el-select
                style="width: 100%"
                v-model="userInfo.amOrPm"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in amOrPmList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="可预约状态：" prop="scheduleStatus">
              <el-radio-group v-model="userInfo.scheduleStatus">
                <el-radio label="1">可预约</el-radio>
                <el-radio label="2">不可预约</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>
        <div class="dialog-footer">
          <el-button @click="showModifyInfo=false">取消</el-button>
          <el-button type="primary" @click="changeModify">确定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {getGroupListSub, queryScheduleList,initScheduleList, scheduleUpdate,delectScheduleList} from "@/api/ha/rosteringInfo/rosteringInfo.js";
import {newFormatDate} from '@/utils/index.js'

export default {
  name: "ScheduleRecord",
  data() {
    return {
      showModifyInfo: false,
      loadingData: false,
      queryParams: {
        yearMonth: '',
        groupId: '',
        workUserName: '',
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      groupList: [],
      MonthDayMenu: [],
      tableHeight: 500,
      scheduleRecordList: [],
      modifyId: {},
      oldColumnList: [
        {label: '工号', prop: 'id', width: 200},
        {label: '姓名', prop: 'name', width: 100},
        {label: '小组', prop: 'groupName'}
      ],
      columnList: [],
      userInfo: {
        workNum: '',
        name: '',
        helpGroup: '',
        modifyDate: '',
        amOrPm: '',
        scheduleStatus: ''
      },
      amOrPmList: [
        {label: '上午', value: '1'},
        {label: '下午', value: '2'},
        {label: '全天', value: '3'},
        {label: '休息', value: '4'},
      ],
      userInfoRules: {
        amOrPm: [
          {required: true, message: "请选择排班班次", trigger: "change"}
        ],
        scheduleStatus: [
          {required: true, message: "请选择可预约状态", trigger: "change"}
        ],
      }
    }
  },
  mounted() {
    this.getGroupListSub()
    this.getDefaultMonth()
    this.getDays()
    this.getQueryScheduleList()
  },
  methods: {
//获取组列表
    getGroupListSub() {
      getGroupListSub().then(res => {
        if (res.code === 200) {
          this.groupList = res.data.businessGrouping
        }
      })
    },

//获取当前月
    getDefaultMonth() {
      const date = new Date()
      let y = date.getFullYear();
      let m = date.getMonth();
      m = Number(m + 1) < 10 ? '0' + Number(m + 1) : '' + Number(m + 1);
      this.queryParams.yearMonth = y + '-' + m
    },
    //选择月份
    changeMonth(val) {
      // this.getDays()
    },
    handleInit() {
      this.getInitScheduleList()
    },
    handleDelect() {
      this.getDelectScheduleList()
    },
    handleQuery() {
      this.getQueryScheduleList()
    },
    //获取当月天数
    getDays() {
      let MonthDayNum = 0;
      var date = this.queryParams.yearMonth ? new Date(this.queryParams.yearMonth) : new Date();
      let y = date.getFullYear();
      let m = date.getMonth();
      // let m = 3;
      let m_zf = Number(m + 1) < 10 ? '0' + Number(m + 1) : '' + Number(m + 1);
      MonthDayNum = this.mGetDate(y, m_zf);
      let menu = [];
      for (var i = 1; i <= MonthDayNum; i++) {
        let i_zf = i < 10 ? '0' + i : '' + i;
        let info = {
          prop: y + '-' + m_zf + '-' + i_zf + ' 00:00:00_amOrPm',
          scheduleStatus: y + '-' + m_zf + '-' + i_zf + ' 00:00:00_scheduleStatus',
          id: y + '-' + m_zf + '-' + i_zf + ' 00:00:00_id',
          date: y + '-' + m_zf + '-' + i_zf + ' 00:00:00',
          status: y + '-' + m_zf + '-' + i_zf + ' 00:00:00_status',
          label: this.getWeek(date, y + '-' + m_zf + '-' + i_zf) + ',' + m_zf + '/' + i_zf,
        }
        menu.push(info);
      }
      this.MonthDayMenu = menu;

      this.columnList = this.oldColumnList.concat(menu)
      this.$nextTick(() => {
        this.columnList = [...this.columnList]
      })

    },
// 获取月份数据
    mGetDate(y, m) {
      var date = new Date(y, m);
      var year = date.getFullYear();
      var month = date.getMonth();
      var d = new Date(year, month, 0);
      return d.getDate();
    },
    // 获取星期
    getWeek(date, dateString) {
      var dateArray = dateString.split("-");
      date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2]);
      // return "周" + "日一二三四五六".charAt(date.getDay());
      return "周" + "日一二三四五六".charAt(date.getDay());
    },
    resetQuery() {
      this.resetForm("queryMessage");
      this.getDefaultMonth()
      this.getQueryScheduleList()
    },
    getInitScheduleList(){
      var _this = this;
      this.loadingData = true
      initScheduleList(this.queryParams).then(res => {
        this.loadingData = false
        if (res.code === 200) {
          this.msgSuccess(res.message);
          _this.getQueryScheduleList();
        }else{
          console.log(res.message)
        }
      }).catch((err) =>{
        this.loadingData = false
        console.log(err);
      })
    },
    getDelectScheduleList(){
      var _this = this;
      this.$confirm('此操作将删除'+this.queryParams.yearMonth+'排班, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        _this.loadingData = true
        delectScheduleList(_this.queryParams).then(res => {
          _this.loadingData = false
          if (res.code === 200) {
            _this.msgSuccess(res.message);
            _this.getQueryScheduleList();
          }else{
            console.log(res.message)
          }
        }).catch((err) =>{
          _this.loadingData = false
          console.log(err);
        })
      });

    },
    //获取排班数据
    getQueryScheduleList() {
      this.getDays()
      this.loadingData = true
      queryScheduleList(this.queryParams).then(res => {
        this.loadingData = false
        if (res.code === 200) {
          this.tableHeight = this.$refs.tableArea.clientHeight;
          this.total = res.data.total
          this.scheduleRecordList = []
          if (res.data.data.length) {
            res.data.data.forEach(item => {
              item.haWorkUserScheduleList.forEach(ite => {
                this.MonthDayMenu.forEach(it => {
                  if (ite.workDate === it.date) {
                    item[it.prop] = ite.amOrPm
                    item[it.scheduleStatus] = ite.scheduleStatus
                    item[it.id] = ite.id
                    const nowDate = newFormatDate(new Date(), 'yyyy-MM-dd')
                    if (ite.amOrPm === '1') {
                      let workDate = ite.workDate.replace(' 00:00:00', ' 11:59:59')
                      if (new Date(workDate).getTime() >= new Date().getTime()) {
                        item[it.status] = true
                      }
                    } else if (ite.amOrPm === '2') {
                      let workDate = ite.workDate.replace(' 00:00:00', ' 17:59:59')
                      if (new Date(workDate).getTime() >= new Date().getTime()) {
                        item[it.status] = true
                      }
                    } else {
                      if (new Date(ite.workDate).getTime() >= new Date(nowDate + ' 00:00:00').getTime()) {
                        item[it.status] = true
                      } else {
                        item[it.status] = false
                      }
                    }
                  }
                })
              })
            })
            this.$nextTick(() => {
              this.scheduleRecordList = [...res.data.data]
              this.$refs.table.doLayout()
            })

          }
        }
      })
    },
    //打开修改弹窗
    changeModifyInfo(row, amOrPm, scheduleStatus, id, date, status) {
      if (!status) {
        return
      }
      this.showModifyInfo = true
      this.userInfo.workNum = row.workNumber
      this.userInfo.name = row.name
      this.userInfo.helpGroup = row.groupName
      this.userInfo.modifyDate = date.split(' ')[0]
      this.userInfo.amOrPm = amOrPm
      this.userInfo.scheduleStatus = scheduleStatus
      this.modifyId = id
    },
    //修改排班信息
    changeModify() {
      const params = {
        id: this.modifyId,
        amOrPm: this.userInfo.amOrPm,
        scheduleStatus: this.userInfo.scheduleStatus
      }
      scheduleUpdate(params).then(res => {
        if (res.code === 200) {
          this.showModifyInfo = false
          this.$message.success('修改成功')
          this.getQueryScheduleList()
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.container {
  // background-color: #fff;
  .tableArea {
    width: 100%;
    height: calc(100% - 150px);

    .el-table {

      /deep/ .el-table__body-wrapper {

        // height: 400px!important;

        .el-table__body {

          tbody {
            background-color: red;

            .el-table--striped tr {
              background-color: #fff !important;
            }

            .el-table__row {
              height: 40px !important;

              .el-table__cell {
                padding: 0;
                margin: 0;
                height: 100% !important;
                background-color: #fff !important;

                .cell {
                  width: 100%;
                  height: 40px;
                  padding: 0;
                  margin: 0;
                  display: flex;
                  align-items: center;
                  justify-content: center;

                  .shift {
                    width: 100%;
                    height: 100%;
                    position: relative;

                    .shiftType {
                      padding: 0;
                      margin: 0;
                      width: 100%;
                      height: 100%;
                      position: absolute;
                      top: 0;
                      left: 0;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                    }

                    .scheduleStatus {
                      position: absolute;
                      font: 12px Sans-Serif;
                      text-align: center;
                      transform: rotate(45deg);
                      position: relative;
                      padding: 0px;
                      left: 25px;
                      top: 6px;
                      width: 80px;
                      color: #f7f7f7;
                      box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.3);

                      &:before {
                        left: 0;
                      }

                      &:after {
                        right: 0;
                      }
                    }

                    .scheduleStatus_1 {
                      background: #dba219;
                    }

                    .scheduleStatus_2 {
                      background: #707070;
                    }

                    .shiftType_1 {
                      background: #018095;
                      color: #fff;
                    }

                    .shiftType_2 {
                      background: #409eff;
                      color: #fff;
                    }

                    .shiftType_3 {
                      background: #74d7cb;
                      color: #fff;
                    }

                    .shiftType_4 {
                      background: #d9dbdf;
                      color: #fff;
                    }
                  }

                  .beOverdue {
                    opacity: 0.5;
                    cursor: not-allowed;
                  }

                  .reservable {
                    cursor: pointer;
                  }
                }

              }
            }
          }

        }
      }

      /deep/ .el-table__fixed {
        .el-table__fixed-body-wrapper {
          .el-table__body {
            tbody {
              .el-table__row {
                height: 40px !important;

                .el-table__cell {
                  padding: 0;
                  margin: 0;
                  height: 100% !important;
                  background-color: #fff !important;

                  .cell {
                    width: 100%;
                    height: 40px;
                    padding: 0;
                    margin: 0;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                  }

                }
              }
            }
          }
        }
      }
    }
  }
}

.userInfoList {
  width: 100%;
  height: auto;

  .essentialinformationForm {
    width: 100%;
    height: auto;
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
    border: 0.0625rem solid #c8cdd3;
    margin-top: 1.0625rem;
    box-sizing: content-box;
    flex-wrap: wrap;

    .baseFillInfo {
      width: 100%;
      height: auto;
      display: flex;
      flex-wrap: wrap;
      align-items: flex-start;
      justify-content: flex-start;

      .el-form-item {
        width: 50%;
        height: 50px;
        margin-bottom: 0;
        border-bottom: 1px solid #e0e6f0;

        &:nth-last-child(1) {
          border-bottom: none;
        }

        &:nth-last-child(2) {
          border-bottom: none;
        }

        // &:nth-child(3),
        // &:nth-child(4) {
        //   border: none;
        // }

        ::v-deep .el-form-item__label {
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #edf0f5;
        }

        ::v-deep .el-form-item__content {
          height: 100%;
          width: calc(100% - 13.85rem);

          .el-input {
            width: 100%;
            height: 100%;

            .el-input__inner {
              width: 100%;
              height: 100% !important;
              border: none;
            }
          }

          .el-select {
            height: 100% !important;
          }

          .el-radio-group {
            height: 100% !important;
            display: flex;
            align-items: center;
            justify-content: center;
          }

          .el-form-item__error {
            top: 70%;
          }
        }
      }
    }
  }

  .dialog-footer {
    width: 100%;
    height: 50px;
    margin-top: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>

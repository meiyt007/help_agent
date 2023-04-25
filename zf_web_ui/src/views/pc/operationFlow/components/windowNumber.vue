<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-10 17:46:56
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-03 17:28:07
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\serviceTransfer.vue
 * @Description: 服务转派
-->
<template>
    <div class="intelligentFormFilling-container window-container" v-loading="turnServiceLoading">
      <div class="tabHeader" v-if="basicUserInfo.groupPost === '1'">
        <p
          class="tabLeft"
          @click="changeTab('1')"
          :class="tabType === '1' ? 'active' : ''"
        >
          标准取号
        </p>
        <p
          class="tabRight"
          @click="changeTab('2')"
          :class="tabType === '2' ? 'active' : ''"
        >
          指定取号
        </p>
      </div>
      <div class="body-content" v-loading="loadingData">
        <!-- <div class="tab" ref="tab1" v-show="tabType === '1'">
          <el-table
            ref="table1"
            :height="tableHeight1"
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
          </el-table>
        </div> -->
        <div class="tab" ref="tab2" v-show="tabType === '2'">
          <div class="table-title">请选择业务分区：</div>
          <div class="table-btn">
            <el-radio v-model="tableradioGroup" :key="data.cateGoryId" v-for="data in allServiceList" :label="data"
                  @change="changeService(data)"> {{ data.cateGoryName }} </el-radio>
          </div>
          <div class="table-title" v-show="haWorkUsers.length">请选择指定窗口：</div>
          <div class="table-btn">
            <el-radio  v-model="groupLeaderId"  :key="item.cateGoryId" v-for="item in haWorkUsers" :label="item">
              {{ item.cateGoryName }}
            </el-radio>
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
      <div class="dialog-footer center">
        <p @click="cancelDialog">取消</p>
        <p @click="serviceTurn">确定</p>
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
        </div>
      </el-dialog>
    </div>
  </template>
  <script>
  import { getAllUserService, getAppointCatalogue,takePriorityNumber,winNumbPush } from "@/api/modules/helpAgent";
  import { sendHPSms } from "@/api/modules/business.js";
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
        groupLeaderId: {},
        haWorkUsers: [],
        turnServiceLoading: false,
        tableHeight1: 200,
        tableHeight2: 200,
        reassignmentCurrentServiceIndex: 0,
        groupServiceList: [],
        allServiceList: [],
        assistantColumnList: [
          { label: "帮办人员", prop: "name", width: 150 },
          { label: "帮办类型", prop: "haType", width: 150 },
          { label: "正在服务人数", prop: "inServiceNum", width: 150 },
          { label: "等待人数", prop: "waitingNum", width: 150 },
          { label: "预计等待时间", prop: "esuimateTime", width: 150 },
          { label: "服务状态", prop: "status", width: 150 },
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
      
      this.groupLeaderId = {};
      this.haType = this.basicUserInfo.haType;
      this.getGroupList();
    },
    methods: {
      //获取分组列表
      getGroupList() {
        this.loadingData = true;
        getAppointCatalogue()
          .then((res) => {
            this.loadingData = false;
            if (res.code === 200) {
              this.allServiceList = res.data;
            //   this.tableHeight2 = this.$refs.tab2.clientHeight;
            }
          })
          .catch((err) => {
            console.log(err);
            this.loadingData = false;
          });
      },
  
      //选择转派人员
      changeService(data, type) {
        this.groupLeaderId = {};  //转派组长清空
        if (type) {
          this.chooseGroupData = data;
        } else {
          this.chooseData = data;
          // this.showChooseGroupLeader = true;
          this.haWorkUsers = data.childMachineCategory;
        }
      },
      //窗口取号
      serviceTurn() {

        if (this.tabType === "2") {
          if (!this.tableradioGroup.cateGoryName) {
            this.$message.warning("请选择业务分区！");
            return;
          }
        }
        if(!this.groupLeaderId.groupId){
            this.$message.warning("请选择窗口！");
            return;
        }
        console.log(this.groupLeaderId);
        let that = this;
        let obj  = {
            queueId: this.baseUserInfo.id ?? this.baseUserInfo.queueId,
            hallCode:'',
            clintId:'',
            groupId:this.groupLeaderId.groupId,
            cateGoryId:this.groupLeaderId.cateGoryId,
            username:this.baseUserInfo.name,
            mobile:this.baseUserInfo.phone,
            certno:this.baseUserInfo.cardNo,
        }
        takePriorityNumber(JSON.stringify(obj)).then(res=>{
            if(res.code == 200){
                // console.log(res.data);
                let dataObj = res.data.tickerMsg;
                dataObj['name'] = that.baseUserInfo.name;
                dataObj['cardNo'] = that.baseUserInfo.cardNo;
                dataObj['companyCode'] = '';
                dataObj['companyName'] = '';
                that.$alert('<div>取号成功,您的窗口号票为：'+ res.data.tickerMsg.stNumber +'</div><div>备注：'+res.data.tickerMsg.stDesc+'</div>', '', {
                  dangerouslyUseHTMLString: true,
                  center: true
                });
                //this.sendHPSms("N0001","二楼c01");
                that.sendHPSms(res.data.tickerMsg.stNumber,res.data.tickerMsg.stDesc);
                // 号票推送
                winNumbPush(dataObj).then(list =>{
                  // console.log(list);
                })
                that.tableradioGroup = {};  //清空业务分区
                that.groupLeaderId = {};   //清空窗口
                that.haWorkUsers = [];
                that.$message.success("取号成功！");
                that.$emit("closeWindow");
            }else{
                that.$message.warning("服务器故障！");
            }
            
        }).catch((err) => {
          
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
        this.tableradioGroup = {};  //清空业务分区
        this.groupLeaderId = {};   //清空窗口
        this.haWorkUsers = [];
        this.$emit("closeWindow");
      },
      sendHPSms(stNumber,stDesc) {
        // console.log("basicUserInfo============"+JSON.stringify(this.basicUserInfo));
        // console.log("baseUserInfo============"+JSON.stringify(this.baseUserInfo));
        /**title,phone,message,workUserId,workUserName**/
        const data = {
          title: "窗口取号提醒",
          phone: this.baseUserInfo.phone,
          message: this.baseUserInfo.name+"您好，您的窗口号票为"+stNumber+"，备注："+stDesc+"。",
          workUserId: this.basicUserInfo.id,
          workUserName: this.basicUserInfo.name,
        };
        sendHPSms(data);
      },
    },
    watch: {
      
    },
  };
  </script>
  <style lang="scss" scoped>
    @import './css/serviceTransfer.scss';
    .window-container .dialog-footer {
        justify-content: center !important;
        p {
            &:nth-child(1) {
                cursor: pointer;
                padding: 0 3.75rem;
                width: auto;
                height: 5.1667rem;
                background: #ffffff;
                border: 1px solid #4988f2;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
                border-radius: 2.5833rem;
                font-size: 1.8333rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #2473ff;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 2.1875rem;
            }

            &:nth-child(2) {
                cursor: pointer;
                padding: 0 3.75rem;
                width: auto;
                height: 5.1667rem;
                background: #ffffff;
                border: 1px solid #4988f2;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
                border-radius: 2.5833rem;
                font-size: 1.8333rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #2473ff;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 2.1875rem;
                color: #ffffff !important;
                background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%) !important;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%) !important;
            }
        }
    }
  </style>
  
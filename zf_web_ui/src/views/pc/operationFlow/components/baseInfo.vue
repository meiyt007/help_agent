<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-20 18:25:09
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-31 14:24:21
 * @FilePath: \hpNewHall\src\views\operationFlow\components\baseInfo.vue
 * 基本信息配置
-->
<template>
  <div class="base-container" v-loading="loadingInfo">
    <div
      class="content noService"
      v-if="!baseUserInfo || JSON.stringify(baseUserInfo) === '{}'"
    >
      <p>
        <img :src="require('@/assets/images/home/nodata.png')" alt="" />
        <span>当前没有正在服务的客户</span>
      </p>
      <!-- <p @click="toServiceUser">开始接待</p> -->
    </div>
    <div class="content" v-else>
      <ul>
        <li>
          <div class="title">
            <p class="left">
              <img :src="require('@/assets/images/home/userIcon.png')" alt="" />
              <span>个人基本信息</span>
            </p>
          </div>
          <div class="detail">
            <div class="detail-item">
              <span>姓名：</span>
              <span>{{ baseUserInfo.name }}</span>
            </div>
            <div class="detail-item">
              <span>证件类型：</span>
              <span>身份证</span>
            </div>
            <div class="detail-item">
              <span>证件号码：</span>
              <span>{{ baseUserInfo.cardNo.replace(/(\w{6})\w*(\w{4})/, '$1********$2') }}</span>
            </div>
            <div class="detail-item">
              <span>联系电话：</span>
              <span>{{ baseUserInfo.phone }}</span>
            </div>
            <div class="detail-item">
              <span>来访时间：</span>
              <span>{{ baseUserInfo.createDate }}</span>
            </div>
            <div class="detail-item">
              <span>开始服务时间：</span>
              <span>{{ baseUserInfo.serviceBeginTime }}</span>
            </div>
            <div class="detail-item">
              <span>企业名称：</span>
              <span>{{ baseUserInfo.companyName }}</span>
            </div>
            <div class="detail-item">
              <span>企业代码：</span>
              <span>{{ baseUserInfo.companyCode }}</span>
            </div>
            <div class="detail-item">
              <span>企业类型：</span>
              <span>{{ baseUserInfo.companyType }}</span>
            </div>
            <div class="detail-item">
              <span>法定代表人：</span>
              <span>{{ baseUserInfo.legalPerson }}</span>
            </div>
          </div>
        </li>
<!--        <li>-->
<!--          <div class="title">-->
<!--            <p class="left">-->
<!--              <img-->
<!--                :src="require('@/assets/images/home/nucleicAcid.png')"-->
<!--                alt=""-->
<!--              />-->
<!--              <span>核酸检测信息</span>-->
<!--            </p>-->
<!--          </div>-->
<!--          <div class="nucleicAcidInformation">-->
<!--            <template v-if="nucleicInfo.length">-->
<!--              <div-->
<!--                class="nucleic-item"-->
<!--                v-for="(item, index) in nucleicInfo"-->
<!--                :key="index"-->
<!--                :class="-->
<!--                  item.nucleicResultCode === '1' ? 'negative' : 'positive'-->
<!--                "-->
<!--              >-->
<!--                <div class="detail-item">-->
<!--                  <span>核酸检测：</span>-->
<!--                  <span>{{-->
<!--                    item.nucleicResultCode === "1"-->
<!--                      ? "阴性"-->
<!--                      : item.nucleicResultCode === "2"-->
<!--                      ? "阳性"-->
<!--                      : "暂无"-->
<!--                  }}</span>-->
<!--                </div>-->
<!--                <div class="detail-item">-->
<!--                  <span>核酸检测时间：</span>-->
<!--                  <span>{{ item.nucleicTestingTime }}</span>-->
<!--                </div>-->
<!--                <div class="detail-item">-->
<!--                  <span>核酸状态：</span>-->
<!--                  <span>{{ item.nucleicStatus }}</span>-->
<!--                </div>-->
<!--              </div>-->
<!--            </template>-->
<!--            <template v-else>-->
<!--              <p class="noData">暂无核酸记录</p>-->
<!--            </template>-->
<!--          </div>-->
<!--        </li>-->
        <li>
          <div class="title">
            <p class="left">
              <img
                  style="width: 1.35rem"
                  :src="require('@/assets/images/home/temporaryStorage.png')"
                  alt=""
              />
              <span>经办人暂存办件记录</span>
            </p>
          </div>
          <div class="historyService">
            <template v-if="temporaryStorageList.length">
              <template v-for="(item, index) in temporaryStorageList">
                <div
                    class="detail historyInfo"
                    :key="index"
                    v-if="item.qlCaseId"
                    @click="continueProcessing(item)"
                >
                  <div class="detail-item">
                    <span>来访时间：</span>
                    <span>{{ item.createDate }}</span>
                  </div>
                  <div class="detail-item">
                    <span>姓名：</span>
                    <span>{{ item.name }}</span>
                  </div>
                  <div class="detail-item">
                    <span>服务内容：</span>
                    <span>{{ item.serviceMemo }}</span>
                  </div>
                  <div class="detail-item">
                    <span>公司名称：</span>
                    <span>{{ item.companyName }}</span>
                  </div>
                  <div class="detail-item">
                    <span>办理人员：</span>
                    <span>{{ item.createBy }}</span>
                  </div>
                </div>
              </template>
            </template>
            <template v-else>
              <p class="noData">无暂存办件</p>
            </template>
          </div>
          <div class="title temporary">
            <p class="left">
              <img :src="require('@/assets/images/home/history.png')" alt="" />
              <span>经办人历史帮办记录</span>
            </p>
            <span
              class="right"
              v-if="serviceHistoryInfo.length"
              @click="showMore"
              >更多＞</span
            >
          </div>
          <div class="historyService">
            <div class="process" v-show="percentage != 100">
              <el-progress
                :percentage="percentage"
                :color="'#409eff'"
              ></el-progress>
              <el-progress
                :percentage="percentage"
                :color="'#409eff'"
              ></el-progress>
            </div>
            <template v-if="serviceHistoryInfo.length">
              <div
                class="detail historyInfo"
                @click="toServiceRecord(item)"
                v-for="(item, index) in serviceHistoryInfo"
                :key="index"
              >
                <div class="detail-item">
                  <span>来访时间：</span>
                  <span>{{ item.createDate }}</span>
                </div>
                <div class="detail-item">
                  <span>开始服务时间：</span>
                  <span>{{ item.serviceBeginTime }}</span>
                </div>
                <div class="detail-item">
                  <span>结束服务时间：</span>
                  <span>{{ item.serviceEndTime }}</span>
                </div>
                <div class="detail-item">
                  <span>办理人员：</span>
                  <span>{{ item.serviceWorkUserName }}</span>
                </div>
              </div>
            </template>
            <template v-else>
              <p class="noData">暂无历史帮办记录</p>
            </template>
          </div>

        </li>
        <li>
          <div class="title">
            <p class="left">
              <img
                  style="width: 1.35rem"
                  :src="require('@/assets/images/home/temporaryStorage.png')"
                  alt=""
              />
              <span>企业暂存办件记录</span>
            </p>
          </div>
          <div class="historyService">
            <template v-if="companyHistoryStopList.length">
              <template v-for="(item, index) in companyHistoryStopList">
                <div
                    class="detail historyInfo"
                    :key="index"
                    v-if="item.qlCaseId"
                    @click="continueProcessing(item)"
                >
                  <div class="detail-item">
                    <span>来访时间：</span>
                    <span>{{ item.createDate }}</span>
                  </div>
                  <div class="detail-item">
                    <span>姓名：</span>
                    <span>{{ item.name }}</span>
                  </div>
                  <div class="detail-item">
                    <span>服务内容：</span>
                    <span>{{ item.serviceMemo }}</span>
                  </div>
                  <div class="detail-item" v-if="item.applyUserType === '1' ">
                    <span >法人名称：</span>
                    <span>{{ item.applyUserName }}</span>
                  </div>
                  <div class="detail-item"  v-if="item.applyUserType === '2' ">
                    <span>公司名称：</span>
                    <span>{{ item.applyUserName }}</span>
                  </div>
                  <div class="detail-item">
                    <span>办理人员：</span>
                    <span>{{ item.workUserName }}</span>
                  </div>
                </div>
              </template>
            </template>
            <template v-else>
              <p class="noData">无暂存办件</p>
            </template>
          </div>
          <div class="title temporary">
            <p class="left">
              <img :src="require('@/assets/images/home/history.png')" alt="" />
              <span>企业历史办件记录</span>
            </p>
            <span
                class="right"
                v-if="serviceHistoryInfo.length"
                @click="toServicCompanyeRecord()"
            >更多＞</span
            >
          </div>
          <div class="historyService">
            <div class="process" v-show="percentage != 100">
              <el-progress
                  :percentage="percentage"
                  :color="'#409eff'"
              ></el-progress>
              <el-progress
                  :percentage="percentage"
                  :color="'#409eff'"
              ></el-progress>
            </div>
            <template v-if="companyHistoryInfo.length">
              <div
                  class="detail historyInfo"
                  @click="toHandlingInformation(item)"
                  v-for="(item, index) in companyHistoryInfo"
                  :key="index"
              >
                <div class="detail-item">
                  <span>来访时间：</span>
                  <span>{{ item.createDate }}</span>
                </div>
                <div class="detail-item" v-if="item.applyUserType === '1' ">
                  <span >法人名称：</span>
                  <span>{{ item.applyUserName }}</span>
                </div>
                <div class="detail-item"  v-if="item.applyUserType === '2' ">
                  <span>公司名称：</span>
                  <span>{{ item.applyUserName }}</span>
                </div>
                <div class="detail-item">
                  <span>办理人员：</span>
                  <span>{{ item.workUserName }}</span>
                </div>
              </div>
            </template>
            <template v-else>
              <p class="noData">暂无历史帮办记录</p>
            </template>
          </div>

        </li>
      </ul>
      <div class="foot">
        <div @click="toNext">办理</div>
      </div>
    </div>
    <!-- 来访记录 -->
    <el-dialog
      :title="visitingRecordsTitle"
      :visible.sync="showVisitingRecords"
      width="90%"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <visitingRecords
        :baseUserInfo="baseUserInfo"
        v-if="showVisitingRecords"
        @setShowVisitingRecords="setShowVisitingRecords"
      />
    </el-dialog>
    <!-- 服务记录 -->
    <el-dialog
      title="服务记录"
      :visible.sync="showServiceRecord"
      width="90%"
      custom-class="serviceRecord"
      v-dialogDrag
    >
      <serviceRecord :serviceData="serviceData" />
    </el-dialog>
    <el-dialog
        title="企业办件记录"
        :visible.sync="showServiceCompanyRecord"
        width="90%"
        custom-class="serviceCompanyRecord"
        v-dialogDrag
    >
      <serviceCompanyRecord :serviceData="serviceData" />
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
import {
  queryWorkQueueList,
  queueServiceList,
} from "@/api/modules/helpAgent.js";

import {
  getMassesInfo,
  // getCompanyInfo,
  queryHelpServiceHistoryList,
  queryCompanyHistoryList,
} from "@/api/modules/workingGroup";
import {
  queryQlCaseByCaseOid,
  listSxServicePage,
  getCaseTitleValueList,
} from "@/api/modules/business";
import serviceRecord from "./serviceRecord.vue";
import visitingRecords from "./visitingRecords.vue";
import serviceCompanyRecord from "./serviceCompanyRecord.vue";
import handlingInformation from "./handlingInformation.vue";
export default {
  name: "BaseInfo",
  components: {
    serviceRecord,
    visitingRecords,
    serviceCompanyRecord,
    handlingInformation,
  },
  props: {
    companyHistoryInfo: {
      type: Array,
      default: () => [],
    },
    companyHistoryStopList: {
      type: Array,
      default: () => [],
    },
    staffInformation: {
      type: Array,
      default: () => [],
    },
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    currentServiceIndex: {
      type: Number,
      default: () => 0,
    },
  },
  data() {
    return {
      serviceTime: "",
      serviceNum: 0,
      serviceName: "",
      percentage: 0,
      serviceHistoryInfo: [],
      // companyHistoryInfo: [],
      nucleicInfo: [],
      temporaryStorageList: [],
      // companyHistoryStopList: [],
      showVisitingRecords: false,
      showServiceRecord: false,
      showServiceCompanyRecord: false,
      handlingInformation: false,
      visitingRecordsTitle: "",
      serviceData: {},
      loadingInfo: false,
      companyInfo: {},
      pageNum: 1,
      pageSize: 100,
      noNextOperate:localStorage.getItem('noNextOperate'),
      basicUserId:this.$store.state.user.basicUserInfo.id,
      nextBtnShow:'',
    };
  },
  mounted() {
    this.toServiceUser();
    console.log(this.nextBtnShow)
  },
  // activated() {

  // },
  methods: {
    //来访记录操作
    setShowVisitingRecords(data) {
      this.showVisitingRecords = data;
    },
    //查看更多历史帮办记录
    showMore() {
      this.visitingRecordsTitle =
        this.staffInformation[this.currentServiceIndex].name +
        `   (${this.staffInformation[this.currentServiceIndex].phone}，${
          this.staffInformation[this.currentServiceIndex].cardNo
        })  来访记录`;
      this.showVisitingRecords = true;
    },
    //查看当前服务记录
    toServiceRecord(data) {
      this.showServiceRecord = true;
      this.serviceData = data;
    },
    //查看服务记录
    toServicCompanyeRecord() {
      this.showServiceCompanyRecord = true;
      this.serviceData = data;
    },
    //获取帮办服务记录
    getQueryHelpServiceHistoryList() {
      if (!this.baseUserInfo) {
        return;
      }
      const params = {
        name: this.baseUserInfo.name,
        cardNo: this.baseUserInfo.cardNo,
        companyName: this.baseUserInfo.companyName,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        serviceStatus: "2",
      };
      queryHelpServiceHistoryList(params).then((res) => {
        if (res.code === 200) {
          this.temporaryStorageList = res.data.data;
        }
      });
      //TODO 前端调用
      // this.getQueryCompanyHistoryList();
    },
    //打开办件详情
    toHandlingInformation(data) {
      this.caseOid = data.qlCaseId;
      this.useInfo = data;
      this.handlingInformation = true;
    },

    //获取帮办服务记录
    getQueryCompanyHistoryList() {
      if (!this.baseUserInfo) {
        return;
      }
      const params = {
        credentialType: "402881945c147ae2015c1575b1980012",
        credentialNumber: "91310000MA1FPM1R8D",
        applyUserName: "上海卓繁信息技术股份有限公司",
        serviceStatus: "",
        pageNum: this.pageNum,
        pageSize: this.pageSize,

      };
      queryCompanyHistoryList(params).then((res) => {
        if (res.code === 200) {
          this.companyHistoryInfo = res.data.data;
          this.companyHistoryStopList = res.data.data.filter((item) => {
            return item.serviceStatus === '2'
          });
          console.log(this.companyHistoryInfo.length+":"+this.companyHistoryStopList.length)
        }
      });
    },

    toNext() {
      this.$emit("nextStep", "mattersHandling");
    },
    toServiceUser() {
      const data = {
        name: "",
        cardNo: "",
        companyName: "",
        queueStatus: "2",
        serviceStatus: "2",
      };
      queryWorkQueueList(data).then((res) => {
        if (res.code === 200) {
          if (res.data.length) {
            this.$emit("setStaffInformation", res.data);
            setTimeout(() => {
              this.getQueryHelpServiceHistoryList();

              this.getUserInfo();
              // this.getCompanyInfo();
            });
          } else {
            // this.$message.warning("当前无可接待用户");
          }
        }
      });
    },
    getUserInfo() {
      if (!this.baseUserInfo) {
        return;
      }
      const data = {
        queueId: this.baseUserInfo.id ?? this.baseUserInfo.queueId,
      };
      this.loadingInfo = true;
      this.percentage = 50;
      getMassesInfo(data)
        .then((res) => {
          this.loadingInfo = false;
          if (res.code === 200) {
            this.percentage = 100;
            this.serviceHistoryInfo = res.data.serviceHistoryInfo;
            let serTime = new Date(this.serviceHistoryInfo[0].serviceBeginTime).getTime();
            let serTime2 = new Date(this.baseUserInfo.serviceBeginTime).getTime();
            if (Math.abs(serTime-serTime2) < 5000) {
            // if (
            //   this.serviceHistoryInfo[0].serviceBeginTime ===
            //   this.baseUserInfo.serviceBeginTime
            // ) {
              this.serviceHistoryInfo.splice(0, 1);
            }
            this.nucleicInfo = res.data.nucleicInfo;
            this.nucleicInfo.forEach((item, index) => {
              if (item.nucleicTestingTime) {
                const nucleicStatus = Math.ceil(
                  (new Date().getTime() -
                    new Date(item.nucleicTestingTime).getTime()) /
                    1000 /
                    60 /
                    60 /
                    24
                );
                item.nucleicStatus = nucleicStatus * 24 + "小时核酸";
              }
            });
          }
        })
        .catch((err) => {
          this.loadingInfo = false;
        });
    },
    // getCompanyInfo() {
    //   if (!this.baseUserInfo) {
    //     return;
    //   }
    //   const data = {
    //     companyName: this.baseUserInfo.companyName,
    //     companyCode: this.baseUserInfo.companyCode,
    //   };
    //   getCompanyInfo(data).then((res) => {
    //     if (res.code === 200) {
    //       this.companyInfo = res.data;
    //     }
    //   });
    // },
    //继续办件
    continueProcessing(data) {
      this.$emit("setData", {
        caseOid: data.qlCaseId,
        caseNumber: data.caseNumber,
        haWorkServiceId: data.id,
      });
      this.$emit("setServiceType", data.serviceType);
      this.getQueryQlCaseApplayByCaseOid(data.qlCaseId);
      this.getDataList(data);
      this.getCaseTitleValueList(data.qlCaseId);
    },
    //获取办件申请信息
    getQueryQlCaseApplayByCaseOid(caseOid) {
      queryQlCaseByCaseOid({ caseOid: caseOid }).then((res) => {
        if (res.code === 200) {
          res.data.applay.certWay = res.data.certWay;
          res.data.applay.expressCompany = res.data.expressCompany;
          if (res.data.applay.chooseAddress) {
            res.data.applay.chooseAddress = JSON.parse(
              res.data.applay.chooseAddress
            );
          } else {
            res.data.applay.chooseAddress = [];
          }
          const data = Object.assign(
            this.baseUserInfo.fillUserInfo,
            res.data.applay
          );
          this.$emit("setFillUserInfo", data);
        }
      });
    },
    //获取事项列表
    getDataList(data) {
      const params = {
        serviceName: data.serviceMemo,
        // districtOid: '',
        districtOid: "4028545d665734290166b02711c20073",
        organOid: "",
        handleType: "",
        pageNum: "",
        pageSize: "",
        serviceType: data.serviceType,
      };
      listSxServicePage(params)
        .then((res) => {
          if (res.code === 200) {
            if (res.data.data.length) {
              res.data.data.forEach((item) => {
                if (item.serviceOid === data.sxServiceId) {
                  this.$emit("setSpecificMatters", item);
                  this.$emit("nextStep", "intelligentFormFilling", "2");
                }
              });
            }
          }
        })
        .catch((err) => {
          this.loadingService = false;
          console.log(err);
        });
    },
    //根据办件编号获取情形选项信息
    getCaseTitleValueList(caseOid) {
      getCaseTitleValueList({ caseOid: caseOid }).then((res) => {
        if (res.code === 200) {
          const arr = [];
          res.data.forEach((item) => {
            const obj = {
              titleOid: item.titleOid,
              valueOid: item.valueOid,
            };
            arr.push(obj);
          });
          this.$emit("setQlCaseTitleValList", arr);
        }
      });
    },
  },
  watch: {
    "staffInformation.length": {
      handler(val) {
        if (val) {
          this.getUserInfo();
          // this.getCompanyInfo();
          this.getQueryHelpServiceHistoryList();
        }
      },
    },
  },
};
</script>
<style lang="scss" scoped>
.base-container {
  width: 100%;
  height: 100%;

  .noService {
    padding-top: 2.5714rem;
    padding-bottom: 3.5714rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;

    p {
      padding: 0;
      margin: 0;

      img {
        width: 30%;
      }

      span {
        font-size: 1.2857rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #b0b0b6;
      }

      &:nth-child(1) {
        width: 100%;
        height: 60%;
        display: flex;
        flex-direction: column;
        align-items: center;
      }

      &:nth-child(2) {
        width: 17.1429rem;
        height: 4.8571rem;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 2.0714rem 0px rgba(204, 177, 121, 0.31);
        border-radius: 0.7143rem;
        font-size: 1.7143rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #ffffff;
        text-shadow: 0px 0px 0.3571rem rgba(0, 56, 156, 0.63);
      }
    }
  }

  .content {
    width: 100%;
    height: 100%;

    border-radius: 0 0.375rem 0.375rem 0.375rem;
    padding: 1.875rem 2.5rem;

    &:nth-child(1) {
      background-color: #f5f5f5;
    }

    &:nth-child(2) {
      background-color: #fff;
    }
  }

  ul {
    width: 100%;
    height: calc(100% - 5rem);
    padding: 0;
    margin: 0;
    display: flex;
    justify-content: space-between;

    li {
      list-style: none;
      width: 32%;
      height: 100%;
      background: linear-gradient(180deg, #e2e9f3 0%, #f5f5f5 100%);
      padding: 0.9375rem 1.25rem;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 0.4375rem;
        background-color: #fff;
      }

      &::-webkit-scrollbar-thumb {
        width: 0.4375rem;
        height: 0.625rem !important;
        // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 4px;
      }

      .title {
        width: 100%;
        height: 1.875rem;
        padding: 0;
        margin: 0;
        margin-bottom: 1.25rem;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .left {
          width: auto;
          height: 100%;
          display: flex;
          align-items: center;
          font-size: 1rem;
          font-family: Microsoft YaHei;
          font-weight: bold;
          color: #333333;

          img {
            width: 1.875rem;
            margin-right: 0.5rem;
          }
        }

        .right {
          font-size: 1.5714rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #656f83;
        }
      }
      .temporary {
        margin-top: 1.25rem;
      }
      .historyService {
        width: 100%;
        height: auto;
        overflow-y: auto;
        max-height: 45%;
        &::-webkit-scrollbar {
          width: 0.4375rem;
          background-color: #e2e9f3;
        }

        &::-webkit-scrollbar-thumb {
          width: 0.4375rem;
          height: 0.625rem !important;
          // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
          background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
          border-radius: 4px;
        }
        .historyInfo {
          cursor: pointer;
        }
        .noData {
          width: 100%;
          height: 3rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-left: 2.375rem;
        }
        .process {
          width: 100%;
          height: 2px;
          display: flex;
          align-items: center;
          justify-content: center;
          ::v-deep .el-progress {
            width: 50%;
            height: 2px;
            display: flex;
            align-items: center;
            &:nth-child(1) {
              transform: rotate(180deg);
            }
            .el-progress-bar {
              padding: 0;
              margin: 0;
              width: 100%;
              height: 2px;
              .el-progress-bar__outer {
                width: 100%;
                height: 2px !important;
                display: flex;
                align-items: center;
              }
            }
            .el-progress__text {
              display: none;
            }
          }
        }
      }
      .detail {
        width: 100%;
        background-color: #f1f3f5;
        padding: 0 0.6375rem;
        margin-bottom: 1.25rem;
        padding-bottom: 1rem;

        .detail-item {
          width: 100%;
          height: 3rem;
          border-bottom: 1px solid #e0e5eb;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-left: 1.25rem;
          text-align: left;
        }
      }
      .nucleicAcidInformation {
        flex: 1;
        width: 100%;
        overflow-y: auto;
        padding-bottom: 1.5rem;

        &::-webkit-scrollbar {
          width: 0.4375rem;
          background-color: #fff;
        }

        &::-webkit-scrollbar-thumb {
          width: 0.4375rem;
          height: 0.625rem !important;
          // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
          background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
          border-radius: 4px;
        }

        .nucleic-item {
          background-color: #f4f5f6;
          padding: 1rem 0;
          margin-bottom: 1rem;

          .detail-item {
            width: 100%;
            height: 3rem;
            border-bottom: 1px solid #bfc1c4;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            padding-left: 1.25rem;
            text-align: left;
          }
        }

        .negative {
          background-color: #68c968;
          color: #fff;
        }

        .positive {
          background: #e53333;
          color: #fff;
        }
      }
    }
  }

  .foot {
    width: 100%;
    height: 3.0625rem;
    margin-top: 1.3rem;
    display: flex;
    justify-content: center;

    div {
      width: 17.1429rem;
      height: 4.8571rem;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 2.0714rem 0px rgba(204, 177, 121, 0.31);
      border-radius: 0.7143rem;
      font-size: 1.7143rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #ffffff;
      text-shadow: 0px 0px 0.3571rem rgba(0, 56, 156, 0.63);
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
</style>

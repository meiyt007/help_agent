<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-04 10:52:20
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-29 13:34:31
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\mattersHandling.vue
 * @Description: 事项选择
-->
<template>
  <div class="mattersHandling" v-loading="loading">
    <div class="left">
      <div class="leftHeader">部门</div>
      <div class="leftContent">
        <p
          class="orgItem"
          v-for="(item, index) in institutionsList"
          :class="organOid === item.organOid ? 'active' : ''"
          @click="toGetService(item)"
          :key="index"
        >
          <el-tooltip
            :content="item.name"
            placement="top"
            effect="light"
            :disabled="tooltipDisable"
          >
            <p @mouseenter="isShowTooltip" class="itemSelect">
              {{ item.name }}
            </p>
          </el-tooltip>
        </p>
      </div>
    </div>
    <div class="selectContainer">
      <div class="header">
        <el-input
          placeholder="输入关键词搜索"
          v-model="serviceName"
          prefix-icon="el-icon-search"
          @keyup.enter.native="searchKey"
        ></el-input>
        <p @click="searchKey" class="searchKey">搜索</p>
      </div>
      <div class="box" v-loading="loadingService">
        <div class="boxArea" ref="scroll" id="box">
          <div
            class="item_box"
            v-for="(item, index) in itemSelectionList"
            :key="index"
          >
            <el-tooltip
              :content="item.serviceName"
              placement="top"
              effect="light"
            >
              <p class="itemSelect">
                {{ item.serviceName }}
              </p>
            </el-tooltip>
            <p class="company" v-if="item.hostOffices">
              {{ item.hostOffices }}
            </p>
            <div class="operate">
              <p @click="toDetailItemSelect(item, '1')">咨询</p>
              <!-- <p @click="toDetailItemSelect(item, '3')">收件</p> -->
              <!-- {{ item.serviceTypeList }} -->
              <template v-for="ite in item.serviceTypeList">
                <!-- <p v-show="ite == 'CLZB'" @click="toDetailItemSelect(item, '2')">准备材料</p> -->
                <p v-show="ite == 'SJ'" @click="toDetailItemSelect(item, '2')">
                  准备材料
                </p>
              </template>
            </div>
          </div>
        </div>
      </div>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[15, 20, 30, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import {
  listSxServicePage,
  serviceType,
  listOrganByDistrictAndService,
} from "@/api/modules/business";
export default {
  name: "ItemSelection",
  props: {
    basicUserInfo: {
      type: Object,
      default: () => {},
    },
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    serviceType: {
      type: String,
      default: () => "",
    },
  },
  data() {
    return {
      loading: false, //loading显示
      tooltipDisable: false,
      loadingService: false,
      itemSelectionList: [],
      serviceName: "",
      total: 0,
      pageNum: 1,
      pageSize: 15,
      serviceOids: "",
      organOid: "",
      institutionsList: [],
    };
  },
  mounted() {
    this.getListOrganByDistrictAndService();
    this.getDataList();
  },
  methods: {
    //获取机构服务
    toGetService(data) {
      if (this.organOid === data.organOid) {
        this.organOid = "";
      } else {
        this.organOid = data.organOid;
      }
      this.getDataList();
    },

    //获取机构列表
    getListOrganByDistrictAndService() {
      const data = {
        districtOid: "4028545d665734290166b02711c20073",
        handleType: "",
      };
      this.loading = true;
      listOrganByDistrictAndService(data)
        .then((res) => {
          this.loading = false;
          if (res.code === 200) {
            this.institutionsList = res.data;
            this.$nextTick(() => {
              this.institutionsList = [...this.institutionsList];
            });
          }
        })
        .catch((err) => {
          console.log(err);
          this.loading = false;
        });
    },
    searchKey() {
      this.getDataList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getDataList();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getDataList();
    },
    isShowTooltip(e) {
      let clientWidth = e.target.clientWidth;
      let scrollWidth = e.target.scrollWidth;
      if (scrollWidth > clientWidth) {
        this.$nextTick(() => {
          this.tooltipDisable = false;
        });
      } else {
        this.$nextTick(() => {
          this.tooltipDisable = true;
        });
      }
    },
    toDetailItemSelect(data, type) {
      this.$emit("setSpecificMatters", data);
      this.$emit("setServiceType", type);
      this.$emit("nextStep", "situationGuidance", 1, type);
    },
    //获取事项列表
    getDataList() {
      const data = {
        serviceName: this.serviceName,
        // districtOid: '',
        districtOid: "4028545d665734290166b02711c20073",
        organOid: this.organOid,
        handleType: "",
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.loadingService = true;
      listSxServicePage(data)
        .then((res) => {
          this.loadingService = false;
          if (res.code === 200) {
            this.total = res.data.total;
            this.itemSelectionList = res.data.data;
            const serviceOids = [];
            this.itemSelectionList.forEach((item) => {
              item.serviceTypeList = [];
              serviceOids.push(item.serviceOid);
            });
            this.serviceOids = serviceOids.toString();
            this.getServiceType();
          }
        })
        .catch((err) => {
          this.loadingService = false;
          console.log(err);
        });
    },
    //用户服务类型权限
    getServiceType() {
      const data = {
        serviceOids: this.serviceOids,
      };
      serviceType(data).then((res) => {
        if (res.code === 200) {
          res.data.forEach((item) => {
            this.itemSelectionList.forEach((ite) => {
              if (ite.serviceOid === item.serviceId) {
                ite.serviceTypeList.push(item.serviceType);
              }
            });
            // const obj = {
            //   serviceStatus: item.serviceStatus,
            //   serviceType: item.serviceType,
            // }
            // this.serviceTypeList.push(obj)
          });
          this.$nextTick(() => {
            this.itemSelectionList = [...this.itemSelectionList];
          });
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.mattersHandling {
  width: 100%;
  height: 100%;
  background-color: #fff;
  display: flex;
  align-items: flex-start;

  .left {
    width: 15rem;
    height: 100%;

    .leftHeader {
      width: 100%;
      height: 4.6429rem;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #6eb2ff;
      font-size: 18px;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #ffffff;
      text-shadow: 0px 0px 5px rgba(0, 56, 156, 0.63);
    }

    .leftContent {
      width: 100%;
      height: calc(100% - 6rem);
      padding: 1rem;
      display: flex;
      flex-wrap: wrap;
      align-items: flex-start;
      justify-content: space-between;
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

      .orgItem {
        width: 100%;
        padding: 0.8rem 0;
        margin: 0 0 1.2rem 0;
        text-align: center;
        background-color: #f5f5f5;
        font-size: 1.2857rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        border-radius: 1.0714rem;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;

        p {
          width: 100%;
          height: 100%;
          padding: 0;
          margin: 0;
        }

        &:hover {
          background: linear-gradient(90deg, #0093e7 0%, #17c3f5 100%);
          box-shadow: 0px 0px 2.0714rem 0px rgba(204, 177, 121, 0.31);
          text-shadow: 0px 0px 0.3571rem rgba(0, 56, 156, 0.63);
          color: #ffffff;
        }
      }

      .active {
        background: linear-gradient(90deg, #0093e7 0%, #17c3f5 100%);
        box-shadow: 0px 0px 2.0714rem 0px rgba(204, 177, 121, 0.31);
        text-shadow: 0px 0px 0.3571rem rgba(0, 56, 156, 0.63);
        color: #ffffff;
      }
    }
  }

  .selectContainer {
    width: calc(100% - 15rem);
    height: 100%;
    background-color: #fff;
    padding-top: 1.6667rem;

    .header {
      width: 100%;
      height: 3.75rem;
      display: flex;
      justify-content: center;
      margin-bottom: 2.625rem;

      .el-input {
        width: 60%;
        height: 3.75rem;
        background: rgba(255, 255, 255, 0.55);
        border: 1px solid #b8c9dd;
        border-radius: 1.875rem;
        display: flex;
        align-items: center;
        justify-content: center;

        ::v-deep .el-input__inner {
          width: 100%;
          height: 95%;
          border-radius: 1.875rem;
          border: none;
          background: rgba(255, 255, 255, 0.55);
        }

        ::v-deep .el-input__prefix {
          .el-input__icon {
            line-height: 3.56rem;
          }
        }
      }

      .searchKey {
        padding: 1.3125rem 2.875rem 1.4375rem 2.9375rem;
        margin: 0;
        margin-left: 1rem;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #2473ff;
        border-radius: 1.75rem;
        font-size: 1.6667rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #fff;
        cursor: pointer;
      }
    }

    .box {
      width: 100%;
      height: calc(100% - 12.1875rem);
      padding: 0 1.25rem;

      .boxArea {
        width: 100%;
        height: 100%;
        display: flex;
        align-content: flex-start;
        justify-content: flex-start;
        flex-wrap: wrap;
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
      }

      .item_box {
        min-width: 150px;
        width: 18%;
        height: 15.8333rem;
        background: url("@/assets/images/home/itemSelectBack.png") no-repeat;
        background-size: 100% 100%;
        margin-right: 2%;
        margin-bottom: 2.4375rem;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        padding: 0 1.625rem;
        border: 1px solid #eae6de;
        box-shadow: 0px 0px 1.5rem 0px rgba(30, 83, 67, 0.1);
        border-radius: 0.4167rem;

        p {
          padding: 0;
          margin: 0;
        }

        .itemSelect {
          width: 100%;
          height: auto;
          max-height: 8.5rem;
          margin-bottom: 0.9375rem;
          font-size: 1.8rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #393f55;
          overflow: hidden; // 溢出隐藏
          display: -webkit-box; //  自适应布局 弹性伸缩盒子
          -webkit-box-orient: vertical; //垂直排列子元素 伸缩盒子的子元素排列
          -webkit-line-clamp: 3; //最多显示几行 多出部分。。。显示
          text-overflow: ellipsis; // 显示省略号
        }

        .company {
          width: auto;
          height: auto;
          border: 1px solid #94bdfd;
          border-radius: 0.25rem;
          font-size: 1.5rem;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #657083;
          padding: 0.5rem 0.5625rem;
        }

        .operate {
          display: none;
          width: 100%;
          height: 3.5rem;
          margin-top: 2.3333rem;
          align-items: center;
          justify-content: space-around;

          p {
            padding: 0.9167rem 1.2rem;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(143deg, #fdfbf8 0%, #e4ebf7 100%);
            border-radius: 1.75rem;
            font-size: 1.6667rem;
            font-family: Source Han Sans CN;
            font-weight: 400;
            color: #4072c7;
            cursor: pointer;
          }
        }

        &:hover {
          border: none;
          background: url("@/assets/images/home/materialHoverBak.png") no-repeat;

          .itemSelect {
            color: #ffffff;
            text-shadow: 0px 0px 8px rgba(55, 83, 175, 0.45);
          }

          .company {
            display: none;
          }

          .operate {
            display: flex;
          }
        }
      }
    }
  }
}
</style>

<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-03 09:52:20
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 14:57:17
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\mattersHandling.vue
 * @Description: 事项选择
-->
<template>
  <div
    class="selectContainer"
    v-loading="loadingService"
    element-loading-text="拼命加载中"
    element-loading-background="transparent"
  >
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
    <div class="rightContainer">
      <div class="header">
        <el-input
          placeholder="输入关键词搜索"
          v-model="serviceName"
          prefix-icon="el-icon-search"
          @keyup.enter.native="searchKey"
        ></el-input>
        <p @click="searchKey" class="searchKey">搜索</p>
      </div>
      <div
        class="box"
        :style="{ transition: `all .${number}s`, top: `${translateY}px` }"
        @touchend="touchend"
        @touchmove="touchmove"
        @touchstart="touchstart"
      >
        <div class="loadingBox" v-if="touchstartTitleShow">释放可刷新...</div>
        <div class="loadingBox" v-if="touchEndTitleShow">加载中...</div>
        <div class="boxArea" ref="scroll" id="box">
          <div
            class="item_box"
            v-for="(item, index) in itemSelectionList"
            :key="index"
            @click="toDetailItemSelect(item)"
          >
            <p class="itemSelect">
              <span>{{ item.serviceName }}</span>
            </p>
            <p class="company" v-if="item.hostOffices">{{ item.hostOffices }}</p>
          </div>
        </div>
        <div class="loadingBox" v-if="loading">加载中...</div>
        <div class="loadingBox" v-if="!loading && !hasNext">已加载全部内容</div>
      </div>
      <div class="footBtn" v-if="hasNext">
        <p>
          <img :src="require('@/assets/images/pad/upup.png')" alt="" />
          向上滑动加载更多
        </p>
      </div>
    </div>
    <el-dialog
      v-dialog-drag
      :title="chooseServiceName"
      :visible.sync="showSelectBusinessType"
      class="select-dialog"
      width="80%"
      append-to-body
    >
      <div class="select-Body">
        <p class="tips">请选择该事项的帮办业务类型</p>
        <div class="select-content">
          <div class="item" @click="toNext(specificMatters, '1')">
            <img
              :src="require('@/assets/images/pad/consultingService.png')"
              alt=""
            />
            <p>咨询</p>
          </div>
          <div
            class="item"
            v-show="serviceTypeList.length"
            @click="toNext(specificMatters, '2')"
          >
            <img
              :src="require('@/assets/images/pad/storesReserve.png')"
              alt=""
            />
            <p>准备材料</p>
          </div>
        </div>
      </div>
    </el-dialog>
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
      showSelectBusinessType: false, //事项选项弹窗
      touchEndTitleShow: false, //控制手指离开屏幕的title显示
      touchstartTitleShow: false, //控制手指按下屏幕的title显示
      number: 0, //列表回弹动画时间
      translateY: 50, //列表随手指下拉而偏移的量
      startY: 0, //手指按住的位置的y坐标，也就是起始坐标
      hasNext: true, //是否还有下一页
      loading: false, //loading显示
      itemSelectionList: [],
      serviceName: "",
      serviceOids: "",
      pageNum: 1,
      pageSize: 16,
      total:0,
      loadingService: false,
      chooseServiceName: "",
      serviceTypeList: [],
      specificMatters: {},
      organOid: "",
      institutionsList: [],
      tooltipDisable: false,
    };
  },
  mounted() {
    this.getListOrganByDistrictAndService();
    this.getData();
  },
  methods: {
    //获取机构服务
    toGetService(data) {
      if (this.organOid === data.organOid) {
        this.organOid = "";
      } else {
        this.organOid = data.organOid;
      }
      this.pageNum = 1;
      this.hasNext = true;
      this.itemSelectionList =[];
      this.getData();
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
    //手指触碰到屏幕
    touchstart(e) {
      this.number = 0;
      let y = e.targetTouches[0].pageY;
      this.startY = y;
    },
    //手指开始滑动
    touchmove(e) {
      let y = e.targetTouches[0].pageY;
      if (y > this.startY && this.$refs.scroll.scrollTop == 0) {
        this.touchstartTitleShow = true;
        //如果当前移动距离大于初始点击坐标，则视为是下拉。并且要处于顶部才刷新，不能影响正常的列表滑动。
        this.translateY = (y - this.startY) / 2;
      } else {
        this.initScrollChange();
      }
    },
    //手指松开
    touchend(e) {
      let y = e.changedTouches[0].pageY;
      if (y > this.startY) {
        this.number = 4;
        this.translateY = 0;
        this.touchstartTitleShow = false;
        this.touchEndTitleShow = true;
        setTimeout(() => {
          this.touchEndTitleShow = false;
        }, 1000);
        this.startY = 0;
      }
    },
    initScrollChange() {
      this.$refs.scroll.onscroll = (e) => {
        const offsetHeight = this.$refs.scroll.offsetHeight; //可视区域的高度
        const scrollHeight = this.$refs.scroll.scrollHeight; //元素全部高度
        const scrollTop = this.$refs.scroll.scrollTop; //滚动条滚动距离
        //可视区域高度加上滚动条滚动距离大于等于元素全部高度则表示滚动到底
        if (offsetHeight + scrollTop - scrollHeight >= -1) {
          console.log("到底啦");
          if (!this.loading && this.hasNext) {
            this.pageNum += 1;
            if(this.pageNum <= Math.ceil(this.total/this.pageSize)){
              this.getData();
              this.hasNext = true;
            }else{
              this.hasNext = false;
            }
            
          }
        }
      };
    },
    searchKey() {
      this.pageNum = 1;
      this.itemSelectionList = [];
      this.getData();
    },
    getData() {
      if(!this.hasNext){
        return;
      }
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
            // this.itemSelectionList = res.data.data;
            const serviceOids = [];
            this.total = res.data.total;
            //判断是否有下一页  关闭加载提示
            if(this.pageNum < this.total/this.pageSize){
              this.hasNext = true
            }else{
              this.hasNext = false;
            }
            res.data.data.forEach((item) => {
              item.serviceTypeList = [];
              serviceOids.push(item.serviceOid);
              // this.itemSelectionList.push(item);
            });
            this.itemSelectionList = this.itemSelectionList.concat(res.data.data);
            this.serviceOids = serviceOids.toString();
            this.getServiceType();
          }
        })
        .catch((err) => {
          this.loadingService = false;
          console.log(err);
        });
    },
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
    toDetailItemSelect(data) {
      this.specificMatters = data;
      this.showSelectBusinessType = true;
      this.chooseServiceName = data.serviceName;
      this.serviceTypeList = data.serviceTypeList;
      //
    },
    toNext(data, type) {
      this.showSelectBusinessType = false;
      this.$emit("setSpecificMatters", data);
      this.$emit("setServiceType", type);
      this.$emit("nextStep", "situationGuidance", 1, type);
    },
  },
};
</script>
<style lang="scss" scoped>
  .left {
    width: 15rem;
    height: 100%;
    float: left;
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
      padding: 1rem 1rem 1rem 2rem;
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
  .rightContainer{
    width: calc(100% - 15rem);
    float: right;
    height: 100%;
  }
.selectContainer {
  width: 100%;
  height: 100%;

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
      box-shadow: -1px 0px 0.5rem 0px rgba(85, 139, 220, 0.61);
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
        margin-top: 2px;
      }
      ::v-deep  .el-input__prefix{
        top: -4px;
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
      width: 20.75rem;
      width: calc((100% - 7.0588rem)/3);
      height: 13.3125rem;
      background: url("@/assets/images/pad/itemSelectBack.png") no-repeat;
      background-size: 100%;
      border: 1px solid #d2ddef;
      box-shadow: 0px 0.4375rem 0px 0px rgba(213, 238, 251, 0.31);
      border-radius: 0.3125rem;
      margin-right: 1.1765rem;
      margin-left: 1.1765rem;
      margin-bottom: 2.4375rem;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      padding: 1.625rem 1rem;

      p {
        padding: 0;
        margin: 0;

        &:nth-child(1) {
          width: 100%;
          height: auto;
          font-size: 1.4706rem;
          line-height: 1.6471rem;
          max-height: 4.9412rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #357bef;
          text-align: center;
          margin-bottom: 0.9375rem;
          overflow: hidden; // 溢出隐藏
          display: -webkit-box; //  自适应布局 弹性伸缩盒子
          -webkit-box-orient: vertical; //垂直排列子元素 伸缩盒子的子元素排列
          -webkit-line-clamp: 3; //最多显示几行 多出部分。。。显示
          text-overflow: ellipsis; // 显示省略号
          span {
            display: inline-block;
            height: auto;
            text-align: left;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 3;
            text-overflow: ellipsis;
            font-size: 12px;
            line-height: 14px;
            max-height: 50px;
            text-align: center;
          }
        }

        &:nth-child(2) {
          width: auto;
          height: 2.9412rem;
          font-size: 1.125rem;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #6ba1ff;
          padding: 0 0.5625rem;
          border: 1px solid #6ba1ff;
          border-radius: 0.1765rem;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }

  .footBtn {
    width: 100%;
    height: 3.125rem;
    margin-top: 2.6875rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      height: 3.125rem;
      width: auto;
      padding: 0 1.75rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 1.25rem;
      background-color: rgba(59, 166, 255, 0.1);
      border-radius: 1.5625rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #3ba6ff;

      img {
        width: 0.8125rem;
        margin-right: 0.5rem;
      }
    }
  }
}
.select-dialog {
  ::v-deep .el-dialog {
    // height: 50vh !important;
    .el-dialog__header {
      .el-dialog__title {
        display: inline-block;
        overflow: hidden;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        -webkit-text-overflow: ellipsis;
        -moz-text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    .select-Body {
      width: 100%;
      padding: 0.7059rem 0;
      .tips {
        font-size: 1.5714rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #568fff;
      }
      .select-content {
        width: 100%;
        height: auto;
        padding: 2.4286rem 0;
        display: flex;
        align-items: center;
        justify-content: center;
        .item {
          width: 22.1429rem;
          // height: 17.3571rem;
          padding: 1.3571rem 0;
          background: #ffffff;
          border: 1px solid #94bdfd;
          box-shadow: 0px 0.4118rem 0px 0px rgba(213, 238, 251, 0.31);
          border-radius: 0.2941rem;
          background: url("@/assets/images/pad/itemSelect.png") no-repeat;
          background-size: 100% 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
          &:nth-child(1) {
            margin-right: 2.5rem;
          }
          img {
            height: 11.6429rem;
          }
          p {
            width: 11.5714rem;
            height: 3.2857rem;
            background: rgba(148, 189, 253, 0.24);
            border-radius: 1.6429rem;
            font-size: 2rem;
            font-family: Source Han Sans CN;
            font-weight: 400;
            color: #4a7ae6;
            text-align: center;
            line-height: 3.2857rem;
          }
        }
      }
    }
  }
}
</style>

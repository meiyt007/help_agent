<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-20 18:25:09
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 13:26:32
 * @FilePath: \zf_web_ui\src\views\pc\guideClothing\components\baseInfo.vue
 * 基本信息配置
-->
<template>
  <div class="base-container">
    <ul>
      <li>
        <p class="title">
          <img :src="require('@/assets/images/home/userIcon.png')" alt="" />
          <span>个人基本信息</span>
        </p>
        <div class="detail">
          <div class="detail-item">
            <span>姓名：</span>
            <span>{{ componentData.name }}</span>
          </div>
          <div class="detail-item">
            <span>证件类型：</span>
            <span>身份证</span>
          </div>
          <div class="detail-item">
            <span>证件号码：</span>
            <span>{{ componentData.cardNo }}</span>
          </div>
          <div class="detail-item">
            <span>联系电话：</span>
            <span>{{ componentData.phone }}</span>
          </div>
        </div>
      </li>
      <li>
        <div class="title">
          <p class="left">
            <img
              :src="require('@/assets/images/home/componenyService.png')"
              alt=""
            />
            <span>经办企业信息</span>
          </p>
        </div>
        <div class="detail">
          <div class="detail-item">
            <span>企业名称：</span>
            <span>{{ componentData.companyName }}</span>
          </div>
          <div class="detail-item">
            <span>企业组织代码：</span>
            <span>{{ componentData.companyCode }}</span>
          </div>
          <div class="detail-item">
            <span>法人代表：</span>
            <span>{{ componentData.name }}</span>
          </div>
          <div class="detail-item">
            <span>企业地址：</span>
            <span></span>
          </div>
        </div>
      </li>
      <li>
        <div class="title">
          <p class="left">
            <img :src="require('@/assets/images/home/history.png')" alt="" />
            <span>历史服务记录</span>
          </p>
          <span class="right">更多＞</span>
        </div>
        <div class="detail">
          <div class="detail-item">
            <span>最近来访时间：</span>
            <span></span>
          </div>
          <div class="detail-item">
            <span>近30天来访次数：</span>
            <span></span>
          </div>
          <div class="detail-item">
            <span>近期办理事项：</span>
            <span></span>
          </div>
          <div class="detail-item">
            <span>手动分配人员</span>
            <span></span>
          </div>
        </div>
      </li>
    </ul>
    <div class="guidance-foot">
      <p @click="toLastStep">返回</p>
      <p @click="windowHandling">窗口办理</p>
      <p @click="manualAssignment">手动分配帮办人员</p>
      <p @click="automaticAllocation">系统自动分配帮办人员</p>
    </div>
    <el-dialog
      title="手动分配帮代办人员"
      :visible.sync="showManualAssignment"
      width="80%"
      v-dialogDrag
      append-to-body
    >
      <div class="tab2">
        <el-table
          :data="allServiceList"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center' }"
          border
        >
          <template v-for="(item, index) in tableColumns">
            <el-table-column
              :prop="item.prop"
              v-if="item.prop === 'chooseItem'"
            >
              <template slot-scope="scope">
                <el-radio
                  v-model="tableradioAll"
                  :label="scope.row"
                  @change="changeService(scope.row, 'all')"
                  ><i />
                </el-radio>
              </template>
            </el-table-column>
            <el-table-column
              :label="item.label"
              :prop="item.prop"
              v-else
            ></el-table-column>
          </template>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <p @click="cancelDialog">取消</p>
        <p @click="serviceTurn">确定分配</p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  distributeWorkUser,
  getWorkUserList,
} from "@/api/modules/guideService";
import { completeService } from "@/api/modules/helpAgent";
export default {
  name: "BaseInfo",
  props: {
    componentData: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      showManualAssignment: false,
      allServiceList: [],
      tableradioGroup: {},
      chooseData: {},
      tableColumns: [
        { label: "", prop: "chooseItem" },
        { label: "帮办人员", prop: "name" },
        { label: "所在分组", prop: "groupName" },
        { label: "帮办类型", prop: "haType" },
        { label: "正在服务人数", prop: "inServiceNum" },
        { label: "等待人数", prop: "waitingNum" },
        { label: "预计等待时间", prop: "esuimateTime" },
      ],
    };
  },
  mounted() {
    this.getWorkUserList();
  },
  methods: {
    getWorkUserList() {
      const data = {
        name: "",
        haType: "",
      };
      getWorkUserList(data).then((res) => {
        if (res.code === 200) {
          this.allServiceList = res.data;
        }
      });
    },

    //选择转派人员
    changeService(data) {
      // this.tableradioGroup = {}
      this.chooseData = data;
    },

    toLastStep() {},
    //手动分配
    manualAssignment() {
      this.showManualAssignment = true;
    },
    cancelDialog() {
      this.cancelDialog = false;
    },
    //确定手动分配
    serviceTurn() {
      const data = {
        workUserId: this.chooseData.id,
        queueId: this.componentData.id,
        distributeStatus: "1",
        windowsNumbe: "",
      };
      distributeWorkUser(data).then((res) => {
        if (res.code === 200) {
          this.$message.success("服务已转派请等待接收");
          this.getServiceTurnStatus(res.data.turnRecordId);
        }
      });
    },
    //自动分配
    automaticAllocation() {
      const data = {
        workUserId: "",
        queueId: this.componentData.id,
        distributeStatus: "2",
        windowsNumbe: "",
      };
      distributeWorkUser().then((res) => {
        if (res.code === 200) {
          this.$message.success("已成功分配帮代办人员");
        }
      });
    },

    //窗口办理
    windowHandling() {
      const data = {
        workUserId: "",
        queueId: this.componentData.id,
        distributeStatus: "3",
        windowsNumbe: "",
      };
      distributeWorkUser().then((res) => {
        if (res.code === 200) {
          this.$message.success("请去窗口办理");
          this.completeService();
        }
      });
    },

    //服务完成
    completeService() {
      const that = this;
      let data = {
        workQueueId: that.componentData.id,
        serviceType: "",
        pushType: "",
        serviceMemo: "",
        sxServiceId: "",
        qlCaseId: "",
        pushMemo: "",
        caseNumber: "",
      };
      completeService(data).then((res) => {
        if (res.code === 200) {
          this.$message.success("已完成服务");
        }
      });
    },

    toNext() {
      this.$emit("nextStep", "mattersHandling");
    },
  },
};
</script>
<style lang="scss" scoped>
.base-container {
  width: 100%;
  height: 100%;
  background-color: #fff;
  border-radius: 0 0.375rem 0.375rem 0.375rem;
  padding: 1.875rem 2.5rem;

  ul {
    width: 100%;
    height: 80%;
    padding: 0;
    margin: 0;
    display: flex;
    justify-content: space-between;

    li {
      list-style: none;
      width: 32%;
      background: linear-gradient(180deg, #e2e9f3 0%, #f5f5f5 100%);
      padding: 0.9375rem 1.25rem;

      .title {
        width: 100%;
        height: 1.875rem;
        font-size: 1rem;
        font-family: Microsoft YaHei;
        font-weight: bold;
        color: #333333;
        padding: 0;
        margin: 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;

        img {
          width: 1.875rem;
          margin-right: 0.5rem;
        }
      }

      .detail {
        width: 100%;
        max-height: 23rem;
        background-color: #f1f3f5;
        padding: 0 0.6375rem;
        margin-top: 1.25rem;
        overflow-y: auto;
        padding-bottom: 1.5rem;

        .detail-item {
          width: 100%;
          height: 3.75rem;
          border-bottom: 1px solid #e0e5eb;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-left: 1.25rem;
        }
      }
    }
  }

  .guidance-foot {
    width: 100%;
    height: 9.375rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      cursor: pointer;
      padding: 0 3.75rem;
      width: auto;
      height: 5.1667rem;
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
      border-radius: 2.5833rem;
      font-size: 1.8333rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #2473ff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 2.1875rem;

      &:nth-child(3) {
        border: none;
        color: #ffffff;
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
      }

      &:nth-child(4) {
        border: none;
        color: #ffffff;
        background: linear-gradient(90deg, #0093e7 0%, #17c3f5 100%);
        box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
      }
    }
  }
}
</style>

<template>
  <div class="frequent-list">
    <div class="card-title">
      <img src="@/assets/image/xxjs-icon.png" alt srcset /> 常办清单列表
    </div>
    <div class="frequent-list__context">
      <el-table v-if="tableData.length > 0" :data="tableData" style="width: 100%" height="100%">
        <el-table-column prop="serviceName" label="事项名称" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" header-align="center" width="200" label="操作">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="iconfont zfsoft-dengji"
              v-if="isShowPersonalRegisterCase(scope.row)"
              @click="registerCase(scope.row, '0')"
              v-hasPermi="['business:acceptance:grregister']"
            >个人登记</el-button>
            <el-button
              type="text"
              size="mini"
              icon="iconfont zfsoft-dengji"
              @click="registerCase(scope.row, '1')"
              v-if="isShowBusinessRegisterCase(scope.row)"
              v-hasPermi="['business:acceptance:grregister']"
            >法人登记</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无数据" />
    </div>

    <CommonDialog
      v-if="commonVisible"
      ref="commonDialog"
      :commmonVisible.sync="commonVisible"
      :cegisterType.sync="cegisterType"
      :serviceOid="serviceOid"
      :loginUser="loginUser"
      :serviceName="serviceName"
      :serviceRoot="serviceRoot"
      @case-close="closeCaseRegisterView"
    />
  </div>
</template>

<script>
// apis
import { getSxServiceCountByUserOid } from '@/api/tjfx/qlCase/qlCaseStatistics.js';
import { getloginUser } from "@/api/zc/businessManagement/windowAcceptance";
// components
import CommonDialog from "@/views/zc/businessManagement/windowAcceptance/dialogs/common-dialog.vue";
export default {
  name: 'FrequentList',
  components: { CommonDialog },
  data () {
    return {
      tableData: [],
      serviceRoot: [false, false], // 服务对象权限
      comeFormArtific: false,
      //登记类型 法人1 自然人0
      cegisterType: "",
      commonVisible: false,
      loginUser: {},
      serviceOid: '',
      serviceName: ''
    }
  },
  created () {
    this.init();
    this.queryLoginInfo();
  },
  methods: {
    init () {
      // 暂时屏蔽统计接口
      this.tableData = [];
      // this.$getResponse(getSxServiceCountByUserOid(this.$store.state.user.userOid), (error, res) => {
      //   if (error || res.code !== 200) return;
      //   this.tableData = res.data || [];
      // })
    },

    /** 登录信息 */
    queryLoginInfo () {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },

    // 点击弹框
    registerCase (row, cegisterType) {
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.cegisterType = cegisterType;
      this.comeFormArtific = false;
      this.serviceRoot = [this.isShowPersonalRegisterCase(row), this.isShowBusinessRegisterCase(row)];
      this.commonVisible = true;
    },

    // 是否展示法人登记按钮
    isShowBusinessRegisterCase (row) {
      return (
        row.serviceObject &&
        (row.serviceObject.indexOf("2") != -1 ||
          row.serviceObject.indexOf("3") != -1 ||
          row.serviceObject.indexOf("4") != -1 ||
          row.serviceObject.indexOf("5") != -1 ||
          row.serviceObject.indexOf("6") != -1 ||
          row.serviceObject.indexOf("9") != -1)
      );
    },

    // 是否展示个人登记按钮
    isShowPersonalRegisterCase (row) { 
      return row.serviceObject && row.serviceObject.indexOf("1") != -1;
    },

    // 办件修改关闭按钮调用wps打印
    closeCaseRegisterView (obj) {
      let mesTitle = "";
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        mesTitle = "是否打印容缺补正受理通知书?";
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        mesTitle = "是否打印不予受理通知单?";
      } else if (obj && obj.isSl == "true") {
        mesTitle = "是否打印受理通知单?";
      } else if (obj && obj.isGzSl == "true") {
        mesTitle = "是否打印告知承诺单?";
      }
      if (!mesTitle) {
        return this.$router.push("materialOut");
      }
      this.$confirm(mesTitle, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        type: "warning"
      })
        .then(() => {
          this.downGzWord(obj);
        })
        .catch(action => {
          if (action === "cancel") {
            console.log("打印取消...");
          }
          this.$router.push("materialOut");
        });
    },

    downGzWord (obj) {
      downGzcnsWord(obj).then(response => {
        let url = response.data;
        //调用pageoffice的打印接口
        POBrowser.openWindowModeless(
          process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/pageOfficePrint?fileUrl=" + url, "width=1200px;height=800px;"
        );
      })

    }
  }
}
</script>

<style scoped lang="scss">
.frequent-list {
  .card-title {
    height: 48px;
    line-height: 48px;
    background: #e4e9ec;
    border-radius: 5px 5px 0px 0px;
    padding-left: 9px;
    box-sizing: border-box;
    font-size: 16px;
    color: #2a344c;
    font-weight: bold;
    img {
      display: inline-block;
      vertical-align: middle;
      margin: 0 7px 0 0;
    }
  }

  .frequent-list__context {
    height: 418px;
    background: #fff;
  }
}
</style>

<!--author:ningzz-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="108px">
          <el-form-item label="办件编号" prop="caseNumber">
            <div class="card-item">
              <el-input
                v-model.trim="queryParams.caseNumber"
                placeholder="请输入办件编号"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              >
                <!-- <template slot="append"
                ><el-button
                  class="scan-btn"
                  type="primary"
                  @click="openScan"
                ></el-button
                ></template>-->
              </el-input>
            </div>
          </el-form-item>
          <el-form-item label="申请人" prop="name">
            <el-input
              v-model.trim="queryParams.applyUserName"
              placeholder="请输入申请人名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="登记日期">
            <!-- <el-date-picker
              v-model="queryParams.caseStartDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始时间">
            </el-date-picker>
            -
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束时间">
            </el-date-picker>-->
            <el-date-picker
              v-model="queryParams.caseStartDate"
              type="date"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsStart"
              placeholder="请选择开始日期"
            ></el-date-picker>-
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsEnd"
              placeholder="请选择结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <!--已办业务列表-->
        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="一件事目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true" />
          <el-table-column label="办件状态" align="center" prop="caseStatus" width="100px">
            <template slot-scope="scope">
              <p v-if="scope.row.caseStatus == 3">办结</p>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xinwen"
                @click="openSignList(scope.row)"
                v-hasPermi="['sys:sendAndReceive:sign']"
              >签收信息</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-fenxiang"
                @click="openIssuedList(scope.row)"
                v-hasPermi="['sys:sendAndReceive:issued']"
              >发证信息</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 查看证照签收记录-->
    <el-dialog
      v-dialog-drag
      :visible.sync="signView"
      v-if="signView"
      :title="signTitle"
      width="1100px"
      height="700px"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      append-to-body
    >
      <el-scrollbar>
        <license-sign-page @case-close="closeSignView" :comboCaseOid="comboCaseOid"></license-sign-page>
      </el-scrollbar>
    </el-dialog>

    <!-- 查看证照发证记录-->
    <el-dialog
      v-dialog-drag
      :visible.sync="issuedView"
      v-if="issuedView"
      :title="issuedTitle"
      width="1100px"
      height="700px"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      append-to-body
    >
      <el-scrollbar>
        <license-issued-page @case-close="closeView" :comboCaseOid="comboCaseOid"></license-issued-page>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
import { page } from "@/api/onething/comboManager/comboAccept/sendAndReceive";
import licenseSignPage from "@/views/onething/fzgl/sendAndReceive/licenseSignPage";
import licenseIssuedPage from "@/views/onething/fzgl/sendAndReceive/licenseIssuedPage";
import { activeScanningGun, openScanningGun } from "@/api/zc/businessManagement/charge";
export default {
  name: "SendAndReceive",
  components: { licenseSignPage, licenseIssuedPage },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseList: [],
      comboCaseOid: "",
      signTitle: "",
      issuedTitle: "",
      // 弹出层标题
      title: "",
      // 列表查看
      openView: false,
      signView: false,
      issuedView: false,
      // 查询参数默认值
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseStartDate: '',
        caseEndDate: '',
        caseNumber: ''
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.caseEndDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date((new Date(this.queryParams.caseStartDate) - 24 * 60 * 60 * 1000)).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal - 0
          }
        }
      },
      // 表单参数
      form: { info: {}, licenseQS: {}, licenseFZ: {} },
      verifyImg: "",
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 查询办结业务列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboCaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams.applyUserName = '';
      this.queryParams.caseNumber = '';
      this.queryParams.caseEndDate = null;
      this.queryParams.caseStartDate = null;
      this.handleQuery()
    },
    openSignList (row) {
      let _that = this;
      _that.comboCaseOid = row.caseOid;
      _that.signView = true;
      _that.signTitle = "证照签收记录";
    },
    openIssuedList (row) {
      let _that = this;
      _that.comboCaseOid = row.caseOid;
      _that.issuedView = true;
      _that.issuedTitle = "证照发证记录";
    },
    // 关闭按钮
    closeView () {
      let _that = this;
      _that.issuedView = false;
      _that.getList();
    },
    // 关闭按钮
    closeSignView () {
      let _that = this;
      _that.signView = false;
      _that.getList();
    },
    //打开扫码枪
    openScan () {
      openScanningGun().then(res => {
        if (res.StateCode == 0) {
          //成功后激活扫码枪
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.queryParams.caseNumber = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else if (res.StateCode == -1) {
          this.$message.error(res.tips);
        } else if (res.StateCode == -4) {
          //扫码枪已打开
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.queryParams.caseNumber = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          this.$message.error("加载配置失败");
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.card-item {
  .el-form-item {
    position: relative;
  }
  .el-input {
    margin-bottom: 0px;
    border-radius: 3px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
.scan-btn {
  position: absolute;
  height: 30px;
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
</style>

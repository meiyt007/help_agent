/**
* @Author: dxl
* @Date: 2020-11-27
* @Description: 普通办件监督
*/
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-form-item label="登记人" prop="registerUser">
        <treeselect
          v-model="queryParams.registerUser"
          :options="userOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入登记人"
        />
      </el-form-item>
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请输入办件编号"
            clearable
            size="100"
            @keyup.enter.native="handleQuery">
            <!-- <template slot="append"><el-button
              class="scan-btn"
              type="primary"
              @click="openScan"
            ></el-button></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model.trim="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--        <el-form-item label="超期状态">
          <el-radio-group v-model="queryParams.overTime" @change="handleRowChange">
            <el-radio v-for="(status,key) in statusOptions" :key="key" :label="key">{{status}}</el-radio>
          </el-radio-group>
        </el-form-item>-->
      <el-form-item label="登记日期" label-width="80px">
        <el-date-picker
          v-model.trim="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        ></el-date-picker
        >-
        <el-date-picker
          v-model.trim="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="caseRegList"
      border
      :fit="true"
      :height="calcHeight1"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="办件编号"
        align="center"
        prop="caseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项类型"
        align="center"
        prop="serviceTypeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applay.applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件号"
        align="center"
        prop="applay.credentialNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="应办结时间"
        align="center"
        prop="shouldConcludeDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="超期状态" align="center" prop="overTime">
        <template slot-scope="scope">
          <span v-if="scope.row.overTime == '0'">未超期</span>
          <span v-if="scope.row.overTime == '1'">已超期</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:done:view']"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="queryParams.total > 0"
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      title="查看办件信息"
      width="1100px"
      height="700px"
      scrollbar
      v-if="openView"
      append-to-body
    >
      <view-case-info
        :caseNumber="indexCaseNumber"
      ></view-case-info>
      <div slot="footer" style="text-align:center">
        <el-button @click="openView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  getOneByCaseOid,
  queryUserSimpleTree,
} from "@/api/zc/businessManagement/generalCaseSupervise.js";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import vueQr from "vue-qr";
import { mapGetters, mapGtters } from "vuex";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";
import Resolution from '@/mixins/resolution.js';
import { activeScanningGun, openScanningGun } from '@/api/zc/businessManagement/charge'
export default {
  name: "GeneralCaseSupervise",
  mixins: [Resolution],
  components: { Treeselect, viewCaseInfo, vueQr },
  data () {
    return {
      caseRegList: [],
      // 区划树
      userOptions: [],
      // 弹窗标题
      dialogTitle: "",
      addDialogShow: false,
      detailDialogShow: false,
      childDsitrict: [],

      indexCaseNumber: "",
      openView: false,

      // 预审状态
      statusOptions: { 0: "未超期", 1: "已超期" },

      printCaseInfo: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        applyUserName: "",
        overTime: "0",
        registerUser: null,
        startDate: null,
        endDate: new Date(),
        sourceApp: 1,
        serviceOids: "",
      },
      pickerOptionsStart: {
        disabledDate: (time) => {
          const endDateVal = new Date(this.queryParams.endDate).getTime();
          if (endDateVal) {
            return time.getTime() > endDateVal - 0;
          }
        },
      },
      pickerOptionsEnd: {
        disabledDate: (time) => {
          const beginDateVal = new Date().getTime();
          if (beginDateVal) {
            return time.getTime() > beginDateVal - 0;
          }
        },
      },
    };
  },
  computed: {
    ...mapGetters(["code"]),
  },
  methods: {
    initStartTime () {
      let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
      this.queryParams.startDate = time;
    },
    handleRowChange (data) {
      this.queryParams.overTime = data;
      this.handleQuery();
    },
    viewCaseClose () {
      this.openView = false;
    },
    /** 获取用户树 采用延迟异步加载方式*/
    getUserTree (organOid) {
      queryUserSimpleTree(organOid).then((response) => {
        this.userOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.getList();
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      //首页查询编号赋值
      if (this.code != null && this.code != "" && this.code != undefined) {
        this.queryParams.caseNumber = this.code
      }
      page(this.queryParams).then((response) => {
        if(response.data){
          this.caseRegList = response.data.data;
          this.queryParams.total = response.data.total;
        }
        this.loading = false;
      });
    },

    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    handlePrint (row) {
      this.addDialogShow = true;
      const oid = row.caseOid;
      getOneByCaseOid(oid).then((response) => {
        this.printCaseInfo = response.data;
      });
    },
    //查询所有授权事项
    getRegSxServiceOids () {
      sxServiceOidsListByUserOid().then(respon => {
        if (respon.code === 200) {
          if (respon.data) {
            this.queryParams.serviceOids = respon.data.join(",");
          }
          this.getList();
        }
      })
    },
    //打开扫码枪
    openScan() {
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
  watch: {
    "form.districtOid": function (val, oldVal) {
      if (!val) {
        this.form.districtName = null;
      }
      this.form.organOid = null;
      this.form.organName = null;
      //机构加载
      this.getListOrganTree(val);
    },
    /*'form.organOid': function (val, oldVal) {
        if(!val) {
          this.form.organName = null;
        }
      },*/
  },
  created () {
    this.initStartTime();
    this.getUserTree();
    this.getRegSxServiceOids();
  },
};
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
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
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center
  center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
</style>

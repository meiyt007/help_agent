/** * @Author: dxl * @Date: 2020-11-9 * @Description: 办件预审 */
<template>
  <div class="app-container">
    <!--列表数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model.trim="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请输入办件编号"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          >
            <!-- <template slot="append"
              ><el-button
                class="scan-btn"
                type="primary"
                @click="openScan"
              ></el-button
            ></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="登记日期">
        <el-date-picker
          v-model.trim="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker>
        -
        <el-date-picker
          v-model.trim="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预审状态" label-width="108px">
        <el-radio-group v-model="queryParams.caseStatus">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
            >{{ status }}</el-radio
          >
        </el-radio-group>
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
    <!-- 列表信息-->
    <el-table
      :data="caseRegList"
      v-loading="loading"
      border
      :fit="true"
      :height="calcHeight"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请项目名称"
        align="center"
        prop="projectName"
        :show-overflow-tooltip="true"
      />
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
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件号"
        align="center"
        prop="credentialNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="200"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:internetCasePre:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="scope.row.caseStatus == 1"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:internetCasePre:init']"
            >预审</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 办件预审信息 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="openInit"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>预审状态：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-radio-group v-model="form.auditResult">
                  <el-radio :label="2">受理</el-radio>
                  <el-radio :label="3">预审不通过</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>意见说明：</b></td>
            <td colspan="3">
              <el-form-item prop="resultDesc">
                <el-input
                  v-model.trim="form.resultDesc"
                  type="textarea"
                  placeholder="请输入意见说明"
                  maxlength="200"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看办件信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="查看办件信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-case-info
        :caseNumber="indexCaseNumber"
        @view-case="viewCaseClose"
      />
      <div slot="footer" class="zf-text-center">
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
  saveOrUpdate,
  saveOrUpdateCase
} from "@/api/zc/businessManagement/internetCasePre.js";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";
import Resolution from "@/mixins/resolution.js";
import {
  activeScanningGun,
  openScanningGun
} from "@/api/zc/businessManagement/charge";
export default {
  name: "InternetCasePre",
  mixins: [Resolution],
  components: { viewCaseInfo },
  data() {
    return {
      loading: true,
      // 列表数据
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      // 弹窗标题
      dialogTitle: "",
      openView: false,
      // 新增/修改显示弹出层
      openInit: false,
      // 表单参数
      form: { caseOid: "", caseStatus: 2, auditResult: 2, resultDesc: "" },
      // 表单校验
      rules: {
        resultDesc: [
          { required: true, message: "意见说明不能为空", trigger: "blur" }
        ]
      },

      indexCaseNumber: "",
      // 预审状态
      statusOptions: { "1": "待预审", "2": "预审通过" },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        caseStatus: "1",
        sourceType: "2",
        sourceApp: 1,
        startDate: null,
        endDate: new Date(),
        serviceOids: "",
        total: 0
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.endDate).getTime();
          if (endDateVal) {
            return time.getTime() > endDateVal - 0;
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date().getTime();
          if (beginDateVal) {
            return time.getTime() > beginDateVal - 0;
          }
        }
      }
    };
  },
  computed: {
    calcHeight() {
      return (this.resolutionHeight === 1080 &&
        this.resolutionWidth === 1280) ||
        (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
        ? "calc(100% - 165px)"
        : "calc(100% - 112px)";
    }
  },
  methods: {
    initStartTime() {
      let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
      this.queryParams.startDate = time;
    },
    initQuery() {
      this.queryParams.caseOid = this.$route.query.caseOid;
      this.queryParams.caseNumber = this.$route.query.caseNumber;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        if (response.data) {
          this.caseRegList = response.data.data;
          this.queryParams.total = response.data.total;
        }
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.caseStatus = "1"; //重置radio无效
      this.queryParams.caseOid = "";
      this.queryParams.caseNumber = "";
      this.queryParams.startDate = null
      this.queryParams.endDate = null
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    /** 出库操作 */
    handleInit(row) {
      if (row.caseOid) {
        this.form.caseOid = row.caseOid;
        this.openInit = true;
      } else {
        this.openInit = true;
      }
      this.dialogTitle = "办件预审";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.form.caseStatus = _that.form.auditResult;
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateCase(_that.form).then(response => {
            if (response.code === 200) {
              saveOrUpdate(_that.form).then(response => {
                if (response.code === 200) {
                  _that.msgSuccess("预审成功");
                  _that.openInit = false;
                  setTimeout(() => {
                    _that.getList();
                  }, 10);
                }
              });
            }
          });
        }
      });
    },
    viewCaseClose() {
      this.openView = false;
    },
    //查询所有授权事项
    getRegSxServiceOids() {
      sxServiceOidsListByUserOid().then(respon => {
        if (respon.code === 200) {
          if (respon.data) {
            this.queryParams.serviceOids = respon.data.join(",");
          }
          this.getList();
        }
      });
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
    }
  },
  watch: {},
  created() {
    this.initStartTime();
    this.initQuery();
    this.getRegSxServiceOids();
  }
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
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
</style>

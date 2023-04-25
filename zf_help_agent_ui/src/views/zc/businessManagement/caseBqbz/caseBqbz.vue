/** * @Author: dxl * @Date: 2020-11-13 * @Description: 补齐补正 */
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
      <el-form-item label="登记日期" label-width="80px">
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
      <el-form-item label="告知状态" label-width="108px">
        <el-radio-group v-model="queryParams.noticeFlag">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
            >{{ status }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item label="补正状态" label-width="108px">
        <el-radio-group v-model="queryParams.correctionStatus">
          <el-radio
            v-for="(status, key) in correctionOptions"
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
      height="calc(100% - 165px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请项目名称"
        align="center"
        prop="applyProjectName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办件编号"
        align="center"
        prop="caseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="联系号码"
        align="center"
        prop="userPhone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="起始时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="补正时限"
        align="center"
        prop="dueDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:caseBqbz:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="scope.row.noticeFlag != 1"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:caseBqbz:initNotic']"
            >待告知</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="
              scope.row.correctionStatus == 0 &&
                Date.parse(scope.row.dueDate) >= new Date()
            "
            @click="handleBzsl(scope.row)"
            v-hasPermi="['sys:caseBqbz:initBzsl']"
            >补正受理</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-feiqi"
            v-if="
              scope.row.correctionStatus != 2 &&
                Date.parse(scope.row.dueDate) < new Date()
            "
            @click="handleBzzz(scope.row)"
            v-hasPermi="['sys:caseBqbz:initBzzz']"
            >终止</el-button
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
    <!-- 办件告知信息 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="openInit"
      append-to-body
      width="900px"
    >
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>申请人名称：</b></td>
            <td>
              {{ form.applyPerson.applyUserName }}
            </td>
            <td><b>联系方式：</b></td>
            <td>
              {{ form.applyPerson.applyUserPhone }}
            </td>
          </tr>
          <tr v-if="form.applyPerson.contactUserName">
            <td><b>代理人名称：</b></td>
            <td>
              {{ form.applyPerson.contactUserName }}
            </td>
            <td><b>代理人号码：</b></td>
            <td>
              {{ form.applyPerson.contactUserPhone }}
            </td>
          </tr>
          <tr>
            <td><b>是否短信通知：</b></td>
            <td>
              <el-radio-group v-model="form.messageFlag">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </td>
            <td><b>是否电话通知：</b></td>
            <td>
              <el-radio-group v-model="form.phoneFlag">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </td>
          </tr>
        </table>
      </div>
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
      height="700px"
      scrollbar
      title="查看办件信息"
      width="1100px"
      append-to-body
    >
      <view-case-info
        :caseNumber="indexCaseNumber"
        @view-case="viewCaseBqbzClose"
      ></view-case-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 补正受理-->
    <case-bzsl
      v-if="openViewBzsl"
      :visible.sync="openViewBzsl"
      :msg-val="childParams"
      @hideDialog="hideDialog"
    />
  </div>
</template>

<script>
import {
  page,
  saveOrUpdateNotice,
  getOneApplyPerson,
  saveStopCorrection,
  saveOrUpdateOut,
  saveOrUpdateRecord
} from "@/api/zc/businessManagement/caseBqbz.js";
import caseBzsl from "@/views/zc/businessManagement/caseBqbz/caseBzsl";
/*import viewCaseBqbz from "@/views/zc/businessManagement/caseBqbz/viewCaseBqbz";*/
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";
import {
  activeScanningGun,
  openScanningGun
} from "@/api/zc/businessManagement/charge";
export default {
  name: "CaseBqbz",
  components: { caseBzsl, viewCaseInfo },
  data() {
    return {
      loading: false,
      // 列表数据
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      // 弹窗标题
      dialogTitle: "",
      openView: false,
      openViewBzsl: false,

      // 新增/修改显示弹出层
      openInit: false,
      // 表单参数
      form: {
        id: "",
        caseOid: "",
        noticeFlag: 1,
        messageFlag: 1,
        phoneFlag: 1,
        applyPerson: {}
      },
      // 表单校验
      rules: {},

      indexCaseNumber: "",
      childParams: { id: "", caseNumber: "", correctionOid: "" },
      // 告知和补正状态
      statusOptions: { "0": "待告知", "1": "已告知" },
      correctionOptions: {
        "0": "全部",
        "1": "待补正",
        "2": "已补正",
        "3": "终止"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        noticeFlag: "0",
        correctionStatus: "0",
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
  methods: {
    initStartTime() {
      let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
      this.queryParams.startDate = time;
    },
    initQuery() {
      this.queryParams.caseOid = this.$route.query.caseOid;
      this.queryParams.caseNumber = this.$route.query.caseNumber;
    },
    viewCaseBqbzClose() {
      this.openView = false;
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
      this.queryParams.noticeFlag = "0"; //重置radio无效
      this.queryParams.correctionStatus = "0"; //重置radio无效
      this.queryParams.caseOid = "";
      this.queryParams.caseNumber = "";
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
      this.childParams.caseNumber = row.caseNumber;
      this.childParams.id = row.id;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    handleBzsl(row) {
      this.childParams.caseNumber = row.caseNumber;
      this.childParams.id = row.id;
      this.childParams.correctionOid = row.correctionOid;
      this.openViewBzsl = true;
    },
    /** 出库操作 */
    handleInit(row) {
      if (row.caseOid) {
        this.form.caseOid = row.caseOid;
        this.form.id = row.id;
        //查询办件申请人信息
        getOneApplyPerson(row.caseOid).then(response => {
          if (!response.data) return;
          this.form.applyPerson = response.data;
          this.openInit = true;
        });
      } else {
        this.openInit = true;
      }
      this.dialogTitle = "办件告知";
    },
    handleBzzz(row) {
      let _that = this;
      this.$confirm("是否确认终止?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          saveStopCorrection(row.id).then(response => {
            if (response.code == 200) {
              //失信人员插入
              let sxForm = {};
              sxForm.name = row.applyUserName;
              sxForm.phone = row.userPhone;
              sxForm.cardNumber = response.data.cardNumber;
              sxForm.cardType = response.data.cardType;
              //失信记录插入
              let sxRecord = {};
              sxRecord.dishonestRecordTitle = "补正受理过期!";
              sxRecord.dishonestRecord = "补正受理过期!";
              sxRecord.caseNumber = row.caseNumber;
              sxForm.dishonestRecord = sxRecord;
              saveOrUpdateOut(sxForm).then(response => {
                if (response.code == 200) {
                }
              });
              setTimeout(() => {
                _that.msgSuccess("终止成功!");
                _that.getList();
              }, 10);
            }
          });
        })
        .catch(function() {});
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      saveOrUpdateNotice(_that.form).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("告知成功");
          _that.openInit = false;
          setTimeout(() => {
            _that.getList();
          }, 10);
        }
      });
    },
    //关闭子页面方法
    hideDialog() {
      this.openViewBzsl = false;
      this.getList();
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

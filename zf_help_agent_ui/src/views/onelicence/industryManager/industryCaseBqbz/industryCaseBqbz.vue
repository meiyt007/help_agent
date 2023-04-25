/** * @Author: liangxm * @Date: 2020-11-29 * @Description: 补齐补正 */
<template>
  <div class="app-container">
    <!--列表数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="60px"
    >
      <el-row>
        <el-form-item label="申请人" prop="applyUserName">
          <el-input
            v-model.trim="queryParams.applyUserName"
            placeholder="请输入申请人"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="办件编号" prop="caseNumber" label-width="70px">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请输入办件编号"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="告知状态" label-width="100px">
          <el-radio-group v-model="queryParams.noticeFlag">
            <el-radio
              v-for="(status, key) in statusOptions"
              :key="key"
              :label="key"
              >{{ status }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="补正状态" label-width="100px">
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
      </el-row>
    </el-form>
    <!-- 列表信息-->
    <el-table :data="caseRegList" v-loading="loading" border :fit="true">
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
              scope.row.correctionStatus != 1 &&
                Date.parse(scope.row.dueDate) >= new Date()
            "
            @click="handleBzsl(scope.row)"
            v-hasPermi="['sys:caseBqbz:initBzsl']"
            >补正受理</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="
              scope.row.correctionStatus != 2 &&
                Date.parse(scope.row.dueDate) < new Date()
            "
            @click="handleBzzz(scope.row)"
            v-hasPermi="['sys:caseBqbz:initBzsl']"
            >终止</el-button
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
    <!-- 办件告知信息 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="openInit"
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>申请人名称：</b></td>
              <td>
                <el-form-item>
                  {{ form.applyPerson.applyUserName }}</el-form-item
                >
              </td>
              <td><b>联系方式：</b></td>
              <td>
                <el-form-item> {{ form.applyPerson.userPhone }}</el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>代理人名称：</b></td>
              <td>
                <el-form-item>
                  {{ form.applyPerson.contactUserName }}</el-form-item
                >
              </td>
              <td><b>代理人号码：</b></td>
              <td>
                <el-form-item>
                  {{ form.applyPerson.contactUserPhone }}</el-form-item
                >
              </td>
            </tr>
            <tr>
              <td><b>是否短信通知：</b></td>
              <td>
                <el-form-item>
                  <el-radio-group
                    v-model="form.messageFlag"
                    @change="handleRowChange"
                  >
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td><b>是否电话通知：</b></td>
              <td>
                <el-form-item>
                  <el-radio-group
                    v-model="form.phoneFlag"
                    @change="handleRowChange"
                  >
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看办件信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      title="补正信息"
      width="70%"
      append-to-body
    >
      <el-scrollbar style="height:500px;">
        <view-case-bqbz :msg-val="childParams"></view-case-bqbz>
      </el-scrollbar>
    </el-dialog>

    <!-- 补正受理-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openViewBzsl"
      title="补正受理"
      width="70%"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-scrollbar style="height:500px;">
        <industry-case-bzsl
          :msg-val="childParams"
          @hideDialog="hideDialog"
        ></industry-case-bzsl>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  saveOrUpdateNotice,
  getOneApplyPerson,
  getOneByCaseOid,
  saveStopCorrection,
  saveOrUpdateRecord,
  saveOrUpdateOut
} from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseBqbz";
import industryCaseBzsl from "@/views/onelicence/industryManager/industryCaseBqbz/industryCaseBzsl";
import viewCaseBqbz from "@/views/onelicence/industryManager/industryCaseBqbz/viewCaseBqbz";
export default {
  name: "IndustryCaseBqbz",
  components: { industryCaseBzsl, viewCaseBqbz },
  data() {
    return {
      total: 0,
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
      childParams: { id: "", caseNumber: "", caseOid: "", correctionOid: "" },
      // 告知和补正状态
      statusOptions: { "0": "待告知", "1": "已告知" },
      correctionOptions: { "0": "全部", "1": "待补正", "2": "已补正" },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        noticeFlag: "0",
        correctionStatus: "0",
        total:0
      }
    };
  },
  methods: {
    handleRowChange(data) {},
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
        this.caseRegList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.noticeFlag = "0"; //重置radio无效
      this.queryParams.correctionStatus = "0"; //重置radio无效
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.childParams.caseNumber = row.caseNumber;
      this.childParams.id = row.id;
      this.childParams.caseOid = row.caseOid;

      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    handleBzsl(row) {
      this.childParams.caseNumber = row.caseNumber;
      this.childParams.caseOid = row.caseOid;
      this.childParams.id = row.id;
      this.childParams.correctionOid = row.correctionOid;
      this.openViewBzsl = true;
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
              sxForm.cardNumber = response.data;
              //失信记录插入
              let sxRecord = {};
              //sxRecord.dishonestOid=response.data;
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
        .catch(() => {});
    },
    /** 出库操作 */
    handleInit(row) {
      if (row.caseOid) {
        this.form.caseOid = row.caseOid;
        this.form.id = row.id;
        //查询办件申请人信息
        getOneByCaseOid(row.caseOid).then(response => {
          this.form.applyPerson = response.data;
          this.openInit = true;
        });
      } else {
        this.openInit = true;
      }
      this.dialogTitle = "办件告知";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      console.log(_that.form);
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
    }
  },
  watch: {},
  created() {
    this.getList();
  }
};
</script>

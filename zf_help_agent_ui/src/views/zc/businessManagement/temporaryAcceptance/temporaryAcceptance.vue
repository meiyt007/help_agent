/** * 暂存受理管理 * @author: wangwg * @date: 2020-11-29 */
<template>
  <div class="app-container">
    <!--暂存受理查询数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-form-item label="申请项目名称" prop="projectName">
        <el-input
          v-model.trim="queryParams.projectName"
          placeholder="申请项目名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model.trim="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
            ></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="事项类型" prop="serviceType">
        <el-select
          v-model.trim="queryParams.serviceType"
          placeholder="请选择事项类型"
          size="small"
        >
          <el-option
            v-for="item in serviceTypeOptions"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          >
          </el-option>
        </el-select>
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
          >重置</el-button
        >
      </el-form-item>
      <el-form-item label="登记日期" label-width="80px">
        <el-date-picker
          size="small"
          v-model.trim="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker>
        -
        <el-date-picker
          size="small"
          v-model.trim="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="qlCaseList"
      border
      height="calc(100% - 165px)"
      :fit="true"
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
        label="操作"
        width="200"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-dengji"
            @click="acceptanceCaseEdit(scope.row)"
            v-hasPermi="['business:temporary:edit']"
            >修改
          </el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-dengji"
            @click="acceptanceCaseEdit2(scope.row)"
            v-hasPermi="['business:temporary:edit']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="acceptanceCaseView(scope.row)"
            v-hasPermi="['business:temporary:view']"
            >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-feiqi"
            @click="acceptanceCaseInvalid(scope.row)"
            v-hasPermi="['business:temporary:invalid']"
            >作废
          </el-button>
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

    <!-- 办件查看 -->
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
      <view-case-info :caseNumber="caseNumber" />
      <div slot="footer" style="text-align: center">
        <el-button @click="viewCaseClose"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 办件修改 -->
    <el-dialog
      v-dialog-drag
      v-if="acceptanceEditOptions.show"
      :visible.sync="acceptanceEditOptions.show"
      :title="acceptanceEditOptions.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
      :modal-append-to-body="false"
    >
      <ql-case-acceptance-edit
        :caseOid="data.caseOid"
        :serviceOid="data.serviceOid"
        :title="data.title"
        @case-close="closeCaseEditView"
      />
    </el-dialog>

    <!-- 办件作废 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="data.show"
      v-for="(data, index) in acceptanceInvalidOptions"
      :key="index"
      :title="data.title"
      width="800px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
      :modal-append-to-body="false"
    >
      <ql-case-acceptance-invalid
        :caseOid="data.caseOid"
        :title="data.title"
        @case-close="closeCaseInvalidView"
      />
    </el-dialog>

    <CommonDialog
      v-if="commonVisible"
      ref="commonDialog"
      :commmonVisible.sync="commonVisible"
      :serviceOid="serviceOid"
      :caseOid="caseOid"
      :serviceName="serviceName"
      :caseNumber="caseNumber"
      :loginUser="loginUser"
      :cegisterType.sync="cegisterType"
      :sxServiceMaterialList="sxServiceMaterialList"
      :handleType="handleType"
      :ruleForm="ruleForm"
      isTempComponent
      @case-close="closeCaseRegisterView"
    />
  </div>
</template>
<script>
import {
  downGzcnsWord,
  downloadPrintFile,
  getFileDownPath,
  getloginUser,
  getSituationMaterialListByOids
} from "@/api/zc/businessManagement/windowAcceptance";
import {
  getSxServiceByOid
} from "@/api/zc/qdgl/sxService";
import { page } from "@/api/zc/businessManagement/temporaryAcceptance";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import qlCaseAcceptanceEdit from "@/views/zc/businessManagement/temporaryAcceptance/qlCaseAcceptanceEdit";
import qlCaseAcceptanceInvalid from "@/views/zc/businessManagement/temporaryAcceptance/qlCaseAcceptanceInvalid";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";

import CommonDialog from "./dialogs/common-dialog.vue";
export default {
  inheritAttrs: false,
  components: {
    Treeselect,
    VueCropper,
    viewCaseInfo,
    qlCaseAcceptanceEdit,
    qlCaseAcceptanceInvalid,
    CommonDialog
  },
  name: "TemporaryAcceptance",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      acceptanceViewOptions: [],
      acceptanceEditOptions: {},
      acceptanceInvalidOptions: [],
      qlCaseOptions: [],
      // 办件数据
      qlCaseList: [],
      //查询事项业务主键
      serviceOid: "",
      //证件类型
      certificateTypeList: [],
      // 机构
      listOrganOptions: [],
      // 区划
      districtOptions: [],
      // 事项类型
      serviceTypeOptions: [],
      // 弹出层标题
      title: "",
      // 显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        caseNumber: "",
        pageNum: 1,
        pageSize: 10,
        organOid: "",
        projectName: "",
        applyUserName: "",
        startDate: null,
        endDate: new Date(),
        serviceType: "",
        sourceApp: 1,
        serviceOids: "",
      },
      queryServiceOids: "",
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
      },
      queryForm: {
        registrarOid: "", //逻辑主键
        organOid: "", // 所属组织机构
        serviceType: "" // 事项类型
      },
      labelPosition: "top",
      // 表单校验
      rules: {},
      /** common dialog */
      commonVisible: false,
      serviceName: "",
      cegisterType: "",
      caseNumber: "",
      sxServiceMaterialList:[],
       handleType:'',
       ruleForm:{}
    };
  },
  watch: {
    "queryParams.districtOid": function(val, oldVal) {
      //机构加载
      this.getListOrganTree(val);
    }
  },
  created() {
    this.queryLoginInfo();
    this.initStartTime();
    //this.getList();
    //查询所有授权事项
    this.getRegSxServiceOids();
    this.getServiceTypeTree();
    this.getDistrictTree();
    this.getListOrganTree();
  },
  methods: {
    /** 登录信息 */
    queryLoginInfo() {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },
    initStartTime() {
      let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
      this.queryParams.startDate = time;
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
            this.queryServiceOids = respon.data.join(",");
          }
          this.getList();
        }
      });
    },
    /** 获取事项类型树 */
    getServiceTypeTree(serviceType) {
      let _that = this;
      queryServiceTypeSimpleTree(serviceType).then(response => {
        _that.serviceTypeOptions = response.data;
      });
    },

    /** 获取区划树 */
    getDistrictTree(districtOid) {
      let _that = this;
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },

    /** 获取机构数据 */
    getListOrganTree(districtOid, callback) {
      let _that = this;
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          _that.listOrganOptions = response.data;
          callback && callback();
        });
      } else {
        _that.listOrganOptions = [];
        _that.queryParams.organOid = null;
      }
    },

    /** 查询窗口受理事项列表 */
    getList() {
      let _that = this;
      _that.loading = true;
      page(this.queryParams).then(response => {
        if (response.data) {
          _that.qlCaseList = response.data.data;
          _that.total = response.data.total;
        }
        _that.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      let _that = this;
      Object.assign(this.queryParams, this.$options.data().queryParams);
      _that.resetForm("queryForm");
      _that.queryParams.serviceOids = _that.queryServiceOids;
      this.initStartTime();
      _that.handleQuery();
    },
    /** 表单重置 */
    reset() {
      let _that = this;
      this.queryParams.startDate = "";
      this.queryParams.endDate = "";
      Object.assign(_that.queryForm, _that.$options.data().queryForm);
      _that.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      let _that = this;
      _that.openInit = false;
      _that.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let _that = this;
      _that.queryParams.pageNum = 1;
      _that.getList();
    },
    /** 办件查看按钮操作 */
    acceptanceCaseView(row) {
      this.caseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看办件详情";
    },
    /** 办件修改按钮操作 */
    acceptanceCaseEdit(row) {
      this.acceptanceEditOptions = {
        caseOid: row.caseOid,
        serviceOid: row.serviceOid,
        show: true,
        title: "办件修改"
      };
    },
    acceptanceCaseEdit2(row) {
      debugger
      this.ruleForm =  row.applay
      this.getMaterialList(row)
   
    },
    getMaterialList(row){
      const valOidList = ""
getSituationMaterialListByOids(row.serviceOid,valOidList).then(res=>{
  if(res.code===200 && res.data.length){
    this.sxServiceMaterialList = res.data
  }
})
getSxServiceByOid(row.serviceOid).then(res=>{
  if(res.code===200){
    this.handleType = res.data.handleType
     this.caseNumber = row.caseNumber;
      this.caseOid = row.caseOid;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.cegisterType = row?.applay?.applyUserType;
      this.commonVisible = true;
  }
})
    },
    // 办件修改关闭按钮
    closeCaseEditView(obj) {
      this.acceptanceEditOptions = {};
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        this.$confirm("是否打印容缺补正受理通知书?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.handleRqbzPrint(obj);
          })
          .catch(() => {});
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        this.$confirm("是否打印不予受理通知单?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.handlePrint(obj);
          })
          .catch(() => {});
      }
      this.getList();
    },
    //调用pageoffice的打印
    handlePrint(obj) {
      const applyUserName = encodeURIComponent(obj.applyUserName);
      const caseNumber = obj.caseNumber;
      const sqTime = obj.createDate;
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/notAcceptedNoticePrint?caseNumber=" +
          caseNumber +
          "&applyUserName=" +
          applyUserName +
          "&sqTime=" +
          sqTime,
        "width=1200px;height=800px;"
      );
    },
    //调用pageoffice的打印
    handleRqbzPrint(obj) {
      const caseName = encodeURIComponent(obj.caseName);
      const applyUserName = encodeURIComponent(obj.applyUserName);
      const sqTime = obj.createDate;
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/rqbzAcceptedNoticePrint?caseName=" +
          caseName +
          "&applyUserName=" +
          applyUserName +
          "&sqTime=" +
          sqTime,
        "width=1200px;height=800px;"
      );
    },
    /** 办件修改按钮操作 */
    acceptanceCaseInvalid(row) {
      let _that = this;
      let acceptanceCase = {
        caseOid: row.caseOid,
        show: true,
        title: "办件作废"
      };
      _that.acceptanceInvalidOptions.push(acceptanceCase);
    },
    // 办件修改关闭按钮
    closeCaseInvalidView() {
      this.acceptanceInvalidOptions.pop();
      let item = {
        show: false
      };
      this.getList();
    },

    // 办件修改关闭按钮
    closeCaseRegisterView(obj) {
      //this.closeCamera();
      let mesTitle = "";
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        mesTitle = "是否打印容缺补正受理通知书?";
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        mesTitle = "是否打印不予受理通知单?";
      } else if (obj && obj.isSl == "true") {
        if(obj.sjStatus && obj.sjStatus==2){
          mesTitle = "是否打印受理通知单?";
        }else{
          mesTitle = "是否打印收件通知单?";
        }
      } else if (obj && obj.isGzSl == "true") {
        mesTitle = "是否打印告知承诺单?";
      }
      /*if (!mesTitle) {
        // this.openWelcome();
        return this.$router.push("materialOut");
      }*/
      this.$confirm(mesTitle, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        type: "warning"
      })
        .then(() => {
          // if (obj.isGzSl == "true" || obj.isRqslFlag) {
          this.downGzWord(obj);
          /*} else {
            this.handlePrintAll(obj);
          }*/
          // this.openWelcome();
        })
        .catch(action => {
          if (action === "cancel") {
            console.log("打印取消...");
          }
        });
      this.commonVisible = false;
      this.getList();
    },
    //调用c++服务打印
    handlePrintAll(obj) {
      getFileDownPath(obj).then(response => {
        if (response.data) {
          let url = response.data;
          downloadPrintFile(url).then(res => {
            // 跳转到材料出库
            // obj.isAllowMaterialOut && this.$router.push("materialOut");
          });
          //window.location.href ='http://172.168.253.56:10001/Download?url='+response.data;
        } else {
          // 跳转到材料出库
          //obj.isAllowMaterialOut && this.$router.push("materialOut");
        }
      });
    },

    downGzWord(obj) {
      downGzcnsWord(obj).then(response => {
        let url = response.data;
        /*downloadPrintFile(url).then(res => {
          console.log(res.StateCode)
          if(res.StateCode!=0){
            if(res.StateCode==-3){
              this.$message.error("打开文档失败!");
            }else if(res.StateCode==-5){
              this.$message.error("下载失败!");
            }
          }
          // 跳转到材料出库
          // obj.isAllowMaterialOut && this.$router.push("materialOut");
        });*/
        //调用pageoffice的打印接口
        POBrowser.openWindowModeless(
          process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/pageOfficePrint?fileUrl="+url, "width=1200px;height=800px;"
        );
      });
    }
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
::v-deep .el-form--inline .el-form-item {
  margin-right: unset;
}
</style>

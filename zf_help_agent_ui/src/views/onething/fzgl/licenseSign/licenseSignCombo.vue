/**
* @Author: liangxm
* @Date: 2020-11-24
* @Description: 证照签发
*/
<template>
  <div class="app-container">
    <!--列表数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
      <el-row>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model.trim="queryParams.projectName" placeholder="请输入项目名称" clearable size="100" @keyup.enter.native="handleQuery" />
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
        <el-form-item label="申请人名称" prop="applyUserName">
          <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人名称" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <!-- 列表信息-->
    <el-table  :data="caseRegList" v-loading="loading" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目名称"  align="center" prop="projectName" :show-overflow-tooltip="true"/>
      <el-table-column label="办件编号"  align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
      <el-table-column label="申请人"  align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
      <el-table-column label="证件号"  align="center" prop="credentialNumber" :show-overflow-tooltip="true"/>
      <el-table-column label="手机号"  align="center" prop="applyUserPhone" :show-overflow-tooltip="true"/>
      <el-table-column label="登记日期" align="center"  prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="viewAccept(scope.row)" v-hasPermi="['sys:licenseSign:view']" >查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleViewCase(scope.row)" v-hasPermi="['sys:licenseSign:initlicenseSign']">签收</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="queryParams.total > 0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :close-on-click-modal="false" :title="dialogTitle" :visible.sync="openInit" append-to-body v-dialog-drag>
      <div >
        <el-form ref="form" :rules="rules" :model="form" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>送达方式：</b></td>
              <td colspan="3">
                <el-radio-group v-model="form.sendWay" @change="handleRowChange">
                  <el-radio :label="1">快递送达</el-radio>
                  <el-radio :label="2">人工送达窗口</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr>
              <td><b>证件类型：</b></td>
              <td colspan="3">
                <el-radio-group v-model="form.carType" @change="handleRowChange">
                  <el-radio :label="1">证照</el-radio>
                  <el-radio :label="2">批文</el-radio>
                  <el-radio :label="3">其他</el-radio>
                </el-radio-group>
              </td>
<!--              <td colspan="3">
                <el-form-item prop="cardType">
                  <el-col :span="24">
                    <el-input v-model.trim="form.cardType" placeholder="请输入证件类型" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>-->
            </tr>
            <tr v-if="form.sendWay == 1">
              <td><b>快递目标：</b></td>
              <td colspan="3">
                <el-radio-group v-model="form.deliverTarget" @change="handleRowChange">
                  <el-radio :label="1">窗口</el-radio>
                  <el-radio :label="2">申请人</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-if="form.sendWay == 1">
              <td><b>快递公司：</b></td>
              <td>
                <el-form-item prop="deliverCompany">
                  <el-col :span="24">
                    <el-input v-model.trim="form.deliverCompany" placeholder="请输入快递公司" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
              <td><b>快递单号：</b></td>
              <td>
                <el-form-item prop="deliverNumber">
                  <el-col :span="24">
                    <el-input v-model.trim="form.deliverNumber" placeholder="请输入快递单号" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>送件人姓名：</b></td>
              <td>
                <el-form-item prop="sendPerson">
                  <el-col :span="24">
                    <el-input v-model.trim="form.sendPerson" placeholder="请输入送件人姓名" maxlength="20" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
              <td><b>送件人手机号：</b></td>
              <td>
                <el-form-item prop="sendPhone">
                  <el-col :span="24">
                    <el-input v-model.trim="form.sendPhone" placeholder="请输入送件人手机号" maxlength="11" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>送件部门：</b></td>
              <td>
                <el-form-item prop="organName">
                  <el-col :span="24">
                    <el-input v-model.trim="form.organName" placeholder="请输入送件部门" maxlength="20" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
              <td><b>送件时间：</b></td>
              <td>
                <el-form-item prop="sendTime">
                  <el-date-picker v-model="form.sendTime" type="date"  :picker-options="pickerOptions" placeholder="送件时间"></el-date-picker>
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
    <!-- 记录信息-->
    <el-dialog :close-on-click-modal="false" :title="viewDialogTitle" :visible.sync="openRecordShow" append-to-body v-dialog-drag>
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>送达方式：</b></td>
            <td colspan="3">
              <span v-if="viewform.sendWay=='1'">快递送达</span>
              <span v-if="viewform.sendWay=='2'">人工送达窗口</span>
            </td>
          </tr>
          <tr>
            <td><b>证件类型：</b></td>
            <td colspan="3">
              <span v-if="viewform.carType=='1'">证照</span>
              <span v-if="viewform.carType=='2'">批文</span>
              <span v-if="viewform.carType=='3'">其他</span>
            </td>
          </tr>
          <tr v-if="viewform.sendWay=='1'">
            <td><b>快递目标：</b></td>
            <td colspan="3">
              <span v-if="viewform.deliverTarget=='1'">窗口</span>
              <span v-if="viewform.deliverTarget=='2'">申请人</span>
            </td>
          </tr>
          <tr v-if="viewform.sendWay== 1">
            <td><b>快递公司：</b></td>
            <td>
              {{viewform.deliverCompany}}
            </td>
            <td><b>快递单号：</b></td>
            <td>
              {{viewform.deliverNumber}}
            </td>
          </tr>
          <tr v-if="viewform.sendWay== 1 || viewform.sendWay==2">
            <td><b>送件人姓名：</b></td>
            <td>
              {{viewform.sendPerson}}
            </td>
            <td><b>送件人手机号：</b></td>
            <td>
              {{viewform.sendPhone}}
            </td>
          </tr>
          <tr v-if="viewform.sendWay== 1 || viewform.sendWay==2 ">
            <td><b>送件部门：</b></td>
            <td>
              {{viewform.organName}}
            </td>
            <td><b>送件时间：</b></td>
            <td>
              {{viewform.sendTime}}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openRecordShow =false">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 列表查看弹窗 -->
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false"  width="1100px" height="700px" v-dialog-drag append-to-body>
          <el-form :model="form">
<!--            <el-row>
              <el-form-item label="扫码签收" prop="caseNumber">
                <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="20" @keyup.enter.native="handleQuery" />
              </el-form-item>

              <el-form-item class="fr mr0">
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryCase">搜索</el-button>

              </el-form-item>
            </el-row>-->
            <el-table v-loading="loading" :data="resultList" border>
              <el-table-column label="序号" width="70" type="index" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column label="事项名称" width="260" align="center" prop="serviceName"/>
              <el-table-column label="证照名称" width="260" align="center" prop="licenseName"/>
              <el-table-column label="证照样本" width="260" align="center" prop="sampleName"/>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.caseCombo ==null" @click="handleInit(scope.row)" v-hasPermi="['sys:licenseSign:initlicenseSign']">签收</el-button>
                  <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" v-if="scope.row.caseCombo !=null"  @click="viewRecord(scope.row)" v-hasPermi="['sys:licenseIssued:view']">记录</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="noticeLicense">通知领证</el-button>
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 一件事办件查看开始 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="caseOpenView"
      v-if="caseOpenView"
      @close="closeOpenView"
      :title="title"
      width="1100px"
      scrollbar
      height="700px"
      append-to-body
    >
      <view-combo-case-new :caseOid="indexCaseOid"></view-combo-case-new>
      <div style="text-align: center" slot="footer">
        <el-button type="primary" @click="closeOpenView">关闭</el-button>
      </div>
    </el-dialog>
    <!--一件事办件查看结束-->

  </div>
</template>

<script>
import { page,saveOrUpdateSign,queryComboDirectoryResult,noticeSendMessage,queryComboSignByMaps} from "@/api/onething/fzgl/licenseSignCombo";
import viewComboCase from "@/views/onething/comboManager/comboAccept/viewComboCase";
import {getComboCaseByOid} from "@/api/onething/comboManager/comboAccept/caseBqbz";
import {validatePhone} from "@/utils/validate";
import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
import {activeScanningGun, openScanningGun} from "@/api/zc/businessManagement/charge";
export default {
  name: "LicenseSignCombo",
  components: {ViewComboCaseNew, viewComboCase},
  data() {
    return { 
      //时间控制
      pickerOptions:{
        disabledDate:(current)=>{
          return current.getTime()-new Date().getTime()<= -24*3600*1000;
        }
      },
      // 列表数据
      caseRegList: [],
      rowNum:1,
      // 弹窗标题
      dialogTitle: '',
      openView: false,
      // 新增/修改显示弹出层
      openInit: false,
      openRecordShow: false,
      viewDialogTitle: '',
      comboCaseOid:"",
      // 办件查看
      caseOpenView: false,
      indexCaseOid: '',
      title:"",
      viewform:{},
      // 表单参数
      form: {sendPhone:'',organName : '',sendTime :'',deliverCompany:'',sendPerson:'',deliverNumber :'',sendWay:1,deliverTarget:1,carType:1,serviceOid:''},
      rules: {
        cardType: [
          { required: true, message: "证件类型不能为空", trigger: "blur" }
        ],
        sendPerson: [
          { required: true, message: "送件人不能为空", trigger: "blur" }
        ],
        sendPhone: [
          { required: true, message: "送件人手机号不能为空", trigger: "blur" },
          {validator: validatePhone, message: '请输入正确的送件人手机号', trigger: 'blur'}
        ],
        organName: [
          { required: true, message: "送件部门不能为空", trigger: "blur" }
        ],
        sendTime: [
          { required: true, message: "送件时间不能为空", trigger: "blur" }
        ],
        deliverCompany: [
          { required: true, message: "快递公司不能为空", trigger: "blur" }
        ],
        deliverNumber: [
          { required: true, message: "快递单号不能为空", trigger: "blur" }
        ],
      },
      resultList:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        projectName: "",
        applyUserName: "",
        total: 0,
      }
    };
  },
  methods: {
    handleRowChange(data) {
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handleQueryCase() {
      this.loading = true;
      this.openView = false;
      getComboCaseByOid(this.comboCaseOid,this.queryParams.caseNumber).then(response => {
        this.form = response.data;
        this.loading = false;
        this.openView = true;
      })
    },
    noticeLicense(){
      let _that = this;
      this.$confirm('证件签收齐全，发送短信通知领取证件？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        noticeSendMessage(_that.comboCaseOid).then(response => {
          if (response.data != "") {
            _that.msgSuccess("发送短信成功！");
          }
        })
      })
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
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 办件查看按钮操作 */
    viewAccept(row) {
      this.indexCaseOid = row.caseOid;
      this.caseOpenView = true;
      this.title = "一件事办件信息";
    },
    /** 办件查看关闭按钮操作 */
    closeOpenView() {
      this.caseOpenView = false;
    },
    /** 列表查看按钮操作 */
    handleViewCase(row) {
      this.comboCaseOid = row.caseOid;
      this.queryResultList();
      this.openView = true
      this.title = '一件事证照信息'
    },
    queryResultList() {
      queryComboDirectoryResult(this.comboCaseOid).then(response => {
        this.resultList = response.data.data;
      })
    },
    /** 出库操作 */
    handleInit(row) {

      this.form.sendPhone = '';
      this.form.organName = '';
      this.form.sendTime = '';
      this.form.deliverCompany = '';
      this.form.sendPerson = '';
      this.form.deliverNumber = '';
      this.form.sendWay = 1;
      this.form.deliverTarget = 1;
      this.form.carType = 1;
      this.form.resultOid = row.resultOid;
      this.form.serviceOid = row.serivceOid;
      this.form.comboDirOid=row.comboDirOid;
      this.openInit = true;
      this.dialogTitle ="办件签收";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that=this;

      _that.$refs["form"].validate(valid => {
        if (valid) {
          _that.form.comboCaseOid = _that.comboCaseOid;
          saveOrUpdateSign(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("签收成功");
              _that.openInit = false;
              _that.queryResultList();
            }
          });
        }
      });
    },
    viewRecord(row){
      queryComboSignByMaps(row).then(response => {
        this.viewform = response.data;
      })
      this.openRecordShow = true;
      this.viewDialogTitle = "查看记录";
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
  watch: {},
  created() {
    this.getList();
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

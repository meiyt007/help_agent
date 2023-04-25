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
        <el-form-item label="办件名称" prop="projectName">
          <el-input v-model.trim="queryParams.projectName" placeholder="请输入办件名称" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="办件编号" prop="caseNumber">
          <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="100" @keyup.enter.native="handleQuery" />
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
      <el-table-column label="办件名称"  align="center" prop="projectName" :show-overflow-tooltip="true"/>
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
    <!-- 列表查看弹窗 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" :close-on-click-modal="false" width="1100px" height="700px" scrollbar append-to-body>
          <el-form :model="form">
<!--            <el-row>
              <el-form-item label="扫码签收" prop="caseNumber">
                <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="20" @keyup.enter.native="handleQuery" />
              </el-form-item>

              <el-form-item class="fr mr0">
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryCase">搜索</el-button>
              </el-form-item>
            </el-row>-->
            <el-table v-loading="loading" :data="industryResultList" border>
              <el-table-column label="序号" width="70" type="index" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column label="事项名称" align="center" prop="serviceName" show-overflow-tooltip/>
              <el-table-column label="证照名称" align="center" prop="licenseName" show-overflow-tooltip/>
              <el-table-column label="证照样本" align="center" prop="sampleName" show-overflow-tooltip/>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.resultOid!=null && scope.row.signRecord==null"  @click="openElementList(scope.row)" v-hasPermi="['sys:licenseSign:initlicenseSign']">打证</el-button>
                  <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.serviceOid!=null && scope.row.signRecord==null"  @click="handleInit(scope.row)" v-hasPermi="['sys:licenseSign:initlicenseSign']">签收</el-button>
                  <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.signRecord !=null"  @click="viewRecord(scope.row)" v-hasPermi="['sys:licenseIssued:view']">记录</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="noticeLicense">通知领证</el-button>
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 送达信息-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" :visible.sync="openInit" width="1100px" append-to-body>
      <div>
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
                <el-radio-group v-model="form.sendWay" >
                  <el-radio :label="1">快递送达</el-radio>
                  <el-radio :label="2">人工送达窗口</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr>
              <td><b>证件类型：</b></td>
              <td colspan="3">
                <el-radio-group v-model="form.cardType">
                  <el-radio :label="1">证照</el-radio>
                  <el-radio :label="2">批文</el-radio>
                  <el-radio :label="3">其他</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-if="form.sendWay == 1">
              <td><b>快递目标：</b></td>
              <td colspan="3">
                <el-radio-group v-model="form.deliverTarget" >
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
                  <el-date-picker v-model="form.sendTime" type="date" placeholder="送件时间"></el-date-picker>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 记录信息-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="viewDialogTitle" :visible.sync="openRecordShow" width="1100px" append-to-body>
      <div >
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
                <span v-if="viewform.recordType=='1'">窗口打印</span>
            </td>
          </tr>
          <tr>
            <td><b>证件类型：</b></td>
            <td colspan="3">
                <span v-if="viewform.recordType=='1'">打印证照</span>
                <span v-if="viewform.cardType=='1'">证照</span>
                <span v-if="viewform.cardType=='2'">批文</span>
                <span v-if="viewform.cardType=='3'">其他</span>
            </td>
          </tr>
          <tr v-if="viewform.recordType=='1'">
            <td><b>打印时间：</b></td>
            <td colspan="3">
              {{viewform.createDate}}
            </td>
          </tr>
          <tr v-if="viewform.recordType=='0' ">
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
      <div slot="footer" class="zf-text-center">
        <el-button @click="openRecordShow =false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="openCaseView" v-if="openCaseView"  :title="title" width="1100px" height="700px" scrollbar append-to-body>
        <view-case-info @view-case="closeCaseView" :caseOid="industryCaseOid"></view-case-info>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 查看证照要素内容填写-->
    <el-dialog v-dialog-drag :visible.sync="elementView" v-if="elementView"  title="证照要素填写" width="1100px" height="700px" scrollbar :close-on-click-modal="false" :modal-append-to-body="false" append-to-body>
        <license-element-list @case-close="closeView" :resultOid="resultOid" :industryCaseOid="industryCaseOid" :licenseName="licenseName"></license-element-list>
    </el-dialog>
  </div>
</template>

<script>
import { page,saveOrUpdateSign,queryIndustryDirectoryResult,noticeSendMessage,queryIndustrySignByMaps,getLicenseTemplateByOid} from "@/api/onelicence/fzgl/licenseSignIndustry";
import viewCaseInfo from "@/views/onelicence/industryManager/industryCaseAccept/viewCaseInfo";
import licenseElementList from "@/views/onelicence/fzgl/licenseSign/licenseElementList";
import {validatePhone} from "@/utils/validate";
export default {
  name: "LicenseSignIndustry",
  components: {viewCaseInfo,licenseElementList},
  data() {
    return {

      // 列表数据
      caseRegList: [],
      rowNum:1,
      total: 0,
      // 弹窗标题
      dialogTitle: '',
      elementTitle: '',
      viewDialogTitle: '',
      openView: false,
      openCaseView: false,
      elementView: false,
      resultOid: '',
      // 新增/修改显示弹出层
      openInit: false,
      openRecordShow: false,
      industryCaseOid:"",
      licenseName:"",
      serviceOid:"",
      title:"",
      comboDireOid:"",
      industryResultList:[],
      viewform:{},
      // 表单参数
      form: {id:'',sendWay:1,deliverTarget:1,cardType:1, elementsList: [],
        deliverCompany:'',deliverNumber:'',sendPerson:'',sendPhone:'',
        organName:'',sendTime:''},
      rules: {
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
      applyInfo:{},
      caseViewOptions:[],
      licenseElementOptions: [],
      indexCaseNumber:"",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        projectName: "",
        applyUserName: "",
        total:0
      }
    };
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      let _that = this;
      _that.queryParams.pageNum = 1;
      _that.getList();
    },
    // 取消按钮
    cancel() {
      let _that = this;
      _that.openInit = false;
    },

    /** 查询列表 */
    getList() {
      let _that = this;
      _that.loading = true;
      page(_that.queryParams).then(response => {
        _that.caseRegList = response.data.data;
        _that.queryParams.total = response.data.total;
        _that.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      let _that = this;
      _that.resetForm("queryForm");
      _that.handleQuery();
    },
    // 表单重置
    reset() {
      let _that = this;
      Object.assign(_that.form, _that.$options.data().form)
      _that.resetForm("form");
    },
    /** 查看按钮操作 */
   /* handleView(row) {
      this.indexCaseNumber= row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },*/
    /**业态办件修改**/
    viewAccept(row){
      this.industryCaseOid =row.caseOid;
      this.openCaseView = true
      this.title = '办件详细信息'
    },
    // 关闭按钮
    closeCaseView() {
      let _that = this;
      _that.openCaseView= false;
      _that.getList();
    },
    viewRecord(row){
      queryIndustrySignByMaps(row).then(response => {
        this.viewform = response.data;
      })
      this.openRecordShow = true;
      this.viewDialogTitle = "查看记录";
    },
    // 关闭按钮
    closeView() {
      let _that = this;
      _that.elementView= false;
      _that.$nextTick(() => {
        _that.getResultList();
      })
    },
    /** 列表查看按钮操作 */
    handleViewCase(row) {
      this.industryResultList=[];
      this.industryCaseOid = row.caseOid;
      this.comboDireOid=row.comboDireOid;
      this.getResultList()
      this.openView = true
      this.title = '业态办件证照信息'
    },
    getResultList(){
      let _that = this;
      queryIndustryDirectoryResult(this.industryCaseOid).then(response => {
        _that.industryResultList = response.data.data;
      })
    },
    noticeLicense(){
      let _that = this;
      _that.$confirm('证件签收齐全，发送短信通知领取证件？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        noticeSendMessage(_that.industryCaseOid).then(response => {
          if (response.data != "") {
            _that.msgSuccess("发送短信成功！");
          }
        })
      })
    },
    openElementList(row){
      let _that=this;
      getLicenseTemplateByOid(row.resultOid).then(response => {
          if (response.data ==null || response.data ==""){
            _that.msgError("请配置证照模板！");
            return false;
          }else{
            _that.resultOid = row.resultOid;
            _that.industryCaseOid =row.caseOid;
            _that.licenseName =row.licenseName;
            _that.elementView = true;
            _that.title = "证照要素填写";
          }
      })
    },
    handleInit(row) {
      let _that=this;
      _that.industryCaseOid =row.caseOid;
      _that.serviceOid =row.serviceOid;
      _that.licenseName =row.licenseName;
      _that.form.comboDirOid=this.comboDireOid;
      _that.openInit = true;
      _that.dialogTitle ="办件签收";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that=this;
      //_that.form.resultOid=_that.resultOid;
      _that.form.serviceOid=_that.serviceOid;
      _that.form.industryCaseOid=_that.industryCaseOid;
      _that.form.licenseName=_that.licenseName;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateSign(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("签收成功");
              _that.openInit = false;
            }
              _that.getResultList(_that.industryCaseOid);
          });
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

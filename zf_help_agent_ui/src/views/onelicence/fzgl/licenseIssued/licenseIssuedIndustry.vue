/**
* @Author: liangxm
* @Date: 2020-11-24
* @Description: 证照发证
*/
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>
      <el-row>
        <el-form-item label="办件编号" prop="caseNumber">
          <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
<!--        <el-form-item label="申请人名称" prop="applyUserName">
          <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人名称" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>-->
        <el-form-item class="fr mr0">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleBatch" v-hasPermi="['sys:licenseIssued:init']">扫描领证</el-button>
      </el-col>
    </el-row>
    <!-- 列表信息-->
    <el-table  :data="caseRegList" v-loading="loading" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="证照名称"  align="center" prop="licenseName" :show-overflow-tooltip="true"/>-->
      <el-table-column label="办件编号"  align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
      <el-table-column label="申请人"  align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
      <el-table-column label="申请人身份证"  align="center" prop="cardNumber" :show-overflow-tooltip="true"/>
      <el-table-column label="签收日期" align="center"  prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column  label="证照来源"  align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <p v-if="scope.row.recordType==0">签收证照</p>
          <p v-if="scope.row.recordType==1">打印证照</p>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" v-if="scope.row.issuedList.length == 0"  @click="handleInit(scope.row)" v-hasPermi="['sys:licenseIssued:view']" >发证</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" v-if="scope.row.issuedList.length > 0" @click="viewLicenseRecord(scope.row)" v-hasPermi="['sys:licenseIssued:view']" >记录查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="queryParams.total > 0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 扫码领证信息详细 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="batchView" :visible.sync="batchView" width="1100px" append-to-body>
      <el-row>
        <el-input v-model.trim="batch_caseNumber" placeholder="请扫描二维码" align="center" clearable size="100" @keyup.enter.native="handleBatchQuery" />
      </el-row>
      <div class="zf-zc-table--title">办件列表</div>
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column label="证照名称"  align="center" prop="licenseName" :show-overflow-tooltip="true"/>-->
        <el-table-column label="办件编号"  align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
        <el-table-column label="申请人"  align="center" prop="industryCase.applyUserName" :show-overflow-tooltip="true"/>
        <el-table-column label="申请人身份证"  align="center" prop="industryCase.credentialNumber" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:licenseIssued:out']">领证</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="zf-text-center">
        <el-button @click="() => {batchView = false;}">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 发证 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="addDialogShow" :visible.sync="addDialogShow" width="1100px" append-to-body>
      <div>
        <el-form ref="sendForm" :model="sendForm" :rules="rules.sendForm" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>申请人：</b></td>
              <td>
                {{form.industryCase.applyUserName}}
              </td>
              <td><b>证件号码：</b></td>
              <td>
                {{form.industryCase.credentialNumber}}
              </td>
            </tr>
            <tr>
              <td><b>申请人手机：</b></td>
              <td>
                {{form.industryCase.applyUserPhone}}
              </td>
              <td><b>申请人住址：</b></td>
              <td>
                {{form.industryCase.applyUserAddress}}
              </td>
            </tr>
            <tr>
              <td><b>证照结果：</b></td>
              <td colspan="3">
                {{sendForm.licenseResult}}
              </td>
            </tr>
            <tr>
              <td><b>送达方式：</b></td>
              <td colspan="3">
                <el-radio-group v-model="sendPostWay" >
                  <el-radio :label="1">快递送达</el-radio>
                  <el-radio :label="2">人工送达窗口</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>收件人证件类型：</b></td>
              <td colspan="3">
                <el-form-item prop="pickerCardType">
                  <el-col :span="24">
                    <el-select v-model="sendForm.pickerCardType" placeholder="请选择证件类型">
                      <el-option v-for="certificateType in certificateTypeList"
                                 :key="certificateType.dictOid"
                                 :label="certificateType.name"
                                 :value="certificateType.dictOid">
                      </el-option>
                    </el-select>
                    <span v-if="varify==1" style="color: green">人证比对通过</span>
                    <span v-if="varify==2" style="color: red">人证比对不通过</span>
                    <el-button type="primary" @click="getIdCardInfo('0');" style="float: right">✚ 扫描身份证</el-button>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>收件人证件号码：</b></td>
              <td colspan="3">
                <el-form-item prop="receiveCardCode">
                  <el-col :span="24">
                    <el-input v-model.trim="sendForm.receiveCardCode" placeholder="请输入取件人证件号码" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>收件人姓名：</b></td>
              <td>
                <el-form-item prop="receiveName">
                  <el-col :span="24">
                    <el-input v-model.trim="sendForm.receiveName" placeholder="请输入取件人姓名" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>收件人手机号：</b></td>
              <td>
                <el-form-item prop="receivePhone">
                  <el-col :span="24">
                    <el-input v-model.trim="sendForm.receivePhone" placeholder="请输入取件人手机号" maxlength="11" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="sendPostWay ==1">
              <td><i class="require">*</i><b>收件人所在地址：</b></td>
              <td colspan="3">
                <el-form-item prop="address">
                  <el-col :span="24">
                    <el-input v-model.trim="sendForm.address" placeholder="请输入收件人所在地址" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="sendPostWay ==1">
              <td><i class="require">*</i><b>快递编号：</b></td>
              <td colspan="3">
                <el-form-item prop="kdCode">
                  <el-col :span="24">
                    <el-input v-model.trim="sendForm.kdCode" placeholder="请输入快递编号" maxlength="50" show-word-limit />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="getImageCamera" v-if="sendPostWay =='2'">人证比对</el-button>
<!--        <el-button type="primary" @click="getReceivePhoto" v-if="sendPostWay =='2'" >人证比对</el-button>-->
<!--        <el-button type="primary" @click="signConfirm" v-if="sendPostWay =='2'">签名</el-button>-->
        <el-button type="primary" @click="viewReceiveInfo" v-if="sendPostWay =='2'" >预览领证回执</el-button>
        <el-button type="primary" @click="outOfStock">发证</el-button>
        <el-button @click="() => {addDialogShow = false; reset()}">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 发证信息页面 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" :visible.sync="qfShow" width="900px" append-to-body>
      <div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>扫码确认：</b></td>
              <td colspan="3">
                  <el-col :span="24">
                    <el-input v-model.trim="barCode" placeholder="请扫描条形码" maxlength="50" show-word-limit />
                  </el-col>
              </td>
            </tr>
            <tr>
              <td><b>使用说明：</b></td>
              <td colspan="3">
                请将档案袋上的条形码放在扫描枪下进行扫描条码，扫描后，系统会自动将该档案袋进行匹配发证
              </td>
            </tr>
          </table>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="outOfStockSave">确定</el-button>
        <el-button @click="() => {qfShow = false;}">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 预览领证回执单信息详细 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="issuedReceiveShow" :visible.sync="issuedReceiveShow" width="900px" append-to-body>
      <div>
        <el-form ref="form" :model="caseReceiveInfo" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>领证人姓名：</b></td>
              <td colspan="2">{{caseReceiveInfo.receiveName}}</td>
              <td rowspan="5">
                <img :src="verifyImg" class="user-avatar">
              </td>
            </tr>
            <tr>
              <td><b>领证人证件号码：</b></td>
              <td colspan="2">
                {{caseReceiveInfo.receiveCardCode}}
              </td>
            </tr>
            <tr>
              <td><b>联系电话：</b></td>
              <td colspan="2">
                {{caseReceiveInfo.receivePhone}}
              </td>
            </tr>
<!--            <tr>
              <td><b>采集时间：</b></td>
              <td colspan="2">
                {{caseReceiveInfo.createDate}}
              </td>
            </tr>
            <tr>
              <td><b>办件编号：</b></td>
              <td colspan="2">
                {{caseReceiveInfo.caseNumber}}
              </td>
            </tr>
            <tr>
              <td><b>影像采集确认信息：</b></td>
              <td colspan="3">
                {{caseReceiveInfo.address}}
              </td>
            </tr>-->
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="() => {issuedReceiveShow = false;}">关闭</el-button>
      </div>
    </el-dialog>
    <!--利用vue插件进行打印办件证照签收单-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="printShow" :visible.sync="printShow" width="900px" append-to-body>
      <div id="print">
        <div class="el-table__header-wrapper dialog-table" :model="printCaseInfo">
        <div class="zf-zc-table--title">签收单</div>
        <el-form   v-loading="loading" label-width="0" class="dialog-table">
          <table cellspacing="0" cellpadding="0"  border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td style="text-align: center"><b>办件编号</b></td>
              <td><el-form-item ><el-col :span="24">{{printCaseInfo.industryCase.caseNumber}}</el-col></el-form-item></td>
            </tr>

            <tr>
              <td style="text-align: center"><b>登记日期</b></td>
              <td><el-form-item><el-col :span="24">{{printCaseInfo.industryCase.createDate}}</el-col></el-form-item></td>
            </tr>
            <tr>
              <td colspan="2">办件编号为{{printCaseInfo.industryCase.caseNumber}}的证照，在{{printCaseInfo.modifyDate}}通过
                <span  v-if="printCaseInfo.receiveType==1">快递送达</span>
                <span v-if="printCaseInfo.receiveType==2">自行取件</span>
                <span v-if="printCaseInfo.receiveType==3">其他</span>已发证。
              </td>
            </tr>
          </table>
        </el-form>
        </div>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" v-print="'#print'">打印</el-button>
        <el-button @click="closePrint">关 闭</el-button>
      </div>
    </el-dialog>


    <!-- 发证信息详细 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="viewDialogTitle" v-if="viewDialogShow" :visible.sync="viewDialogShow" width="900px" append-to-body>
      <div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>申请人：</b></td>
              <td>
                {{viewform.industryCase.applyUserName}}
              </td>
              <td><b>证件号码：</b></td>
              <td>
                {{viewform.industryCase.credentialNumber}}
              </td>
            </tr>
            <tr>
              <td><b>申请人手机：</b></td>
              <td>
                {{viewform.industryCase.applyUserPhone}}
              </td>
              <td><b>申请人住址：</b></td>
              <td>
                {{viewform.industryCase.applyUserAddress}}
              </td>
            </tr>
            <tr>
              <td><b>收件人证件号码：</b></td>
              <td colspan="3">
                {{viewform.receiveCardCode}}
              </td>
            </tr>
            <tr>
              <td><b>收件人姓名：</b></td>
              <td>
                {{viewform.receiveName}}
              </td>
              <td><b>收件人手机号：</b></td>
              <td>
                {{viewform.receivePhone}}
              </td>
            </tr>
            <tr v-if="viewform.receiveType ==1">
              <td><b>收件人所在地址：</b></td>
              <td colspan="3">
                {{viewform.address}}
              </td>
            </tr>
            <tr v-if="viewform.receiveType ==1">
              <td><i class="require">*</i><b>快递编号：</b></td>
              <td colspan="3">
                {{viewform.kdCode}}
              </td>
            </tr>
          </table>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="() => {viewDialogShow = false; reset()}">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 人证比对 -->
    <div v-if="dialogVisible">
      <el-dialog v-dialog-drag :visible.sync="dialogVisible"  width="1100px" height='700px' scrollbar :before-close="handleClose" :close-on-click-modal="false" append-to-body>
          <hardware-scan ref="scanForm" :caseOid="industryCaseOid" :loginUser="loginUser" @close="handleClose"></hardware-scan>
      </el-dialog>
    </div>

  </div>
</template>

<script>
  import {saveOrUpdateIssued,getIndustryIssuedBySignOid,getLicenseIssuedIndustryByOid,
    getCertificateType,saveOrUpOutOfStock,getLicenseIssuedByCaseOid,getIdCard} from "@/api/onelicence/fzgl/licenseIssuedIndustry";
  import {getElecLicenUrl} from "@/api/zc/businessManagement/elemLice";
  import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
  import {validatePhone} from "@/utils/validate";
  import {downFileByoid} from "@/api/zc/businessManagement/fileUpload";
  import {uploadCaseMaterialFile} from "@/api/zc/businessManagement/caseMaterialAtta";
  import {queryIndustrySignByOid,pageListIssued,getLicenseListByCaseNumber} from "@/api/onelicence/fzgl/licenseSignIndustry";
  import hardwareScan from "@/views/common/hardwareScan";
import { getloginUser } from "@/api/zc/businessManagement/windowAcceptance";
  import GPYDrive from "@/api/handwareDrive.js";
  import DEVEICETYPE,{
      ID_CARD_V1,
      ID_CARD_V2,
      ID_CARD_V3
    } from '@/components/HiSpeedCamera/config.js'
  import {
    openIdcardv3,
    closeIdcardv3,
    getdataIdcardv3
  } from "@/api/handwarev3.js";
  export default {
    name: "LicenseIssuedIndustry",
    components: {viewCaseInfo,hardwareScan},
    data() {
      return {
        // 列表数据
        industryResultList:[],
        caseRegList: [],
        scanCaseList: [],
        tableData:[],
        rowNum:1,
        total: 0,
        sendPostWay:1,
        // 弹窗标题
        dialogTitle: '',
        viewDialogShow: false,
        addDialogShow: false,
        openView: false,
        batchView:false,
        printShow:false,
        issuedReceiveShow:false,
        qfShow:false,
        case_Number:"",//办件编号，用于预览回执单
        barCode:"",//条形码
        caseReceiveInfo:{},//证照领证信息
        batch_caseNumber:"",
        allSmCaseNum:[],
        currStep:"",
        clerkAble:"N",//是否启用签名
        varify:"",
        title:"",
        viewDialogTitle:"",
        certificateTypeList: [],//证件类型
        printCaseInfo: {},//用于打印证照领证信息
        indexCaseNumber:"",
        verifyImg:"",
        viewform:{},
        form: {industryCase:{},oid:''},
        sendForm: {currStep:'',receiveName:'',receiveCardCode:'',address:''},
        rules : {
          sendForm: {
            pickerCardType: [
              {required: true, message: "请选择证照类型", trigger: "blur"}
            ],
            receiveCardCode: [
              {required: true, message: "取件人证件号码不能为空", trigger: "blur"}
            ],
            receiveName: [
              {required: true, message: "取件人姓名不能为空", trigger: "blur"}
            ],
            receivePhone: [
              {required: true, message: "取件人手机号不能为空", trigger: "blur"},
              {validator: validatePhone, message: '请输入正确的取件人手机号', trigger: 'blur'}
            ],
            address: [
              {required: true, message: "收件人所在地址不能为空", trigger: "blur"}
            ],
            kdCode: [
              {required: true, message: "快递编号不能为空", trigger: "blur"}
            ],
          },
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caseNumber: "",
          licenseInStorage:'0',
          total:0
        },
        dialogVisible:false,
        industryCaseOid:'',
        loginUser: {}
      };
    },
    methods: {
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },

      /** 查询列表 */
      getList() {
        this.loading = true;
        pageListIssued(this.queryParams).then(response => {
          this.caseRegList = response.data.data;
          this.queryParams.total = response.data.total;
          this.loading = false;
        });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.licenseInStorage = '0';//重置radio无效
        this.handleQuery();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      /** 查看按钮操作 */
      handleView(row) {
        getElecLicenUrl(row.elecLicenOid).then(response => {
          let urlElem = [];
          if (response.data) {
            urlElem = response.data;
            if (urlElem[0].viewOfdUrl) {
              window.open(urlElem[0].viewOfdUrl, 'width=1200px;height=800px;');
            }
          }
        });
      },
      viewLicenseRecord(row){
        getIndustryIssuedBySignOid(row.oid).then(response => {
          if (response.data != null) {
            this.viewform = response.data;
          }
        })
        this.viewDialogShow = true;
        this.viewDialogTitle = "领证记录";
      },
      /** 发证操作 */
      handleInit(row) {
        console.log(row);
        let _that = this;
        _that.sendForm={
          receiveName: '',
          pickerCardType:'',
        };
        _that.varify ="";
        _that.industryCaseOid = row.industryCaseOid
        if (row.oid) {
          //查询办件送达信息
          queryIndustrySignByOid(row.oid).then(response => {
            if (response.data != null) {
              _that.form = response.data;
              if(_that.form.sendWay =='1' ){
                _that.sendPostWay = 1;
              }else{
                _that.sendPostWay = 2;
              }
            }
          })
          //查询发证记录信息
          getLicenseIssuedByCaseOid(row.industryCaseOid).then(response => {
            if (response.data != null) {
              _that.currStep = response.data.currStep;//防止修改重新进来必须人证比对
              _that.sendForm = response.data;
              _that.printCaseInfo =response.data;
            }
          });
          }
        _that.addDialogShow = true;
        _that.dialogTitle = "领证信息";
      },
      /** 获取证件类型 */
      getSelectCertificateType(cegisterType) {
        let _that = this;
        getCertificateType(cegisterType).then(response => {
          _that.certificateTypeList = response.data;
        });
      },
      /** 拍照保存记录提交按钮 */
      submitForm() {
        let _that = this;
        if(_that.sendForm.allFlag=='1'){
          _that.$message.error("存在未签收证照暂时无法进行发证!");
          return false;
        }
        this.$refs["sendForm"].validate(valid => {
          if (valid) {
            _that.sendForm.signOid =_that.form.oid;
            _that.sendForm.receiveType = _that.sendPostWay;
            _that.sendForm.industryCaseOid = _that.form.industryCaseOid;
            saveOrUpdateIssued(_that.sendForm).then(response => {
              if (response.code === 200) {
                _that.currStep = response.data.currStep;
                _that.sendForm = response.data;
                _that.sendPostWay = response.data.receiveType;
                _that.msgSuccess("发证信息保存成功");
                _that.addDialogShow = false;
              }
            });
            //进行发证保存操作
            //_that.qfShow = true;
            if(_that.sendPostWay===2){

            }else{
              _that.getList();


            }
          }
        });
      },
      /** 批量出库操作 */
      handleBatch() {
        this.batchView = true;
        this.dialogTitle = "扫码领证";
        this.batch_caseNumber = '';
        this.tableData = [];
        this.allSmCaseNum=[];
      },
      //批量扫描enter查询
      handleBatchQuery() {
        if (this.batch_caseNumber) {
          if(this.allSmCaseNum){
            if(this.allSmCaseNum.indexOf(this.batch_caseNumber)!=-1){
              this.$message.error("办件已存在!");
              return false;
            }
          }
          getLicenseListByCaseNumber(this.batch_caseNumber).then(response => {
            if (response.data !=null) {
              let arr=response.data[0];//目前只取第一个
              if(arr.issuedList && arr.issuedList.length>0){
                this.$message.error("办件已发证!");
                return false;
              }else{
                this.addRow(arr);
                /*arr.forEach((itm,i)=>{
                  this.addRow(itm);
                })*/
                this.allSmCaseNum.push(this.batch_caseNumber);
              }
            } else {
               this.$message.error("未查询到相关数据!");
            }
            this.batch_caseNumber = '';
          });
        } else {
          this.$message.error("办件编号不能为空!");
        }

      },
      // 增加行
      addRow(caseInfo) {
        this.tableData.unshift(caseInfo)//追加数据
        this.rowNum += 1;
      },
      handlePrint() {
        this.printShow = true;
      },
      closePrint() {
        this.printShow = false;
        setTimeout(() => {
          this.getList();//返回列表页面
          //判断是否有扫描领证的
          if (this.tableData && this.tableData.length > 0) {
            this.tableData.forEach((item, i) => {
              if (item.industryCaseOid == this.printCaseInfo.industryCaseOid) {
                this.tableData.splice(i, 1);//删除数组
                this.$forceUpdate();//强制刷新列表
              }
            })
          }
        }, 10);
      },
      //人证比对信息
      getReceivePhoto() {
        let _that = this;
        //此处获取人证比对信息
        const info = '{"device":"PersonVerify"}';
        //建立socket连接
        this.socketApi.initWebSocket(this.socketError);
        this.socketApi.sendSock(info, this.getConfigResult);
      },
      scanCard(scanType) {
        //Device：设备类型、
        let info  = '{"device":"IdCard"}';
        //建立socket连接
        this.socketApi.initWebSocket(this.socketError);
        this.socketApi.sendSock(info, this.getConfigResult);
      },
      //初始化socket发生错误回调
      socketError() {
        this.$message.error("请检查设备或连接是否正常")
      },
      //签名
   /*   signConfirm() {
        let _that = this;
        if (_that.currStep  =="" || _that.varify == 2) {
          _that.$message.error("请先进行人证比对！");
        } else {
          //签字对接，需要保存
          _that.sendForm.displaySign = 1;//签名
          //保存签字信息主键
          _that.sendForm.signInfo = "112323435";//签名返回的附件主键
          _that.sendForm.displaySign =1;
          saveOrUpdateIssued(_that.sendForm).then(response => {
            if (response.code === 200) {
              _that.sendForm = response.data;
              _that.msgSuccess("签名成功");
            }
          });
        }
      },*/
      // 接收socket回调函数返回数据的方法
      getConfigResult(data) {
        let _that = this;
        this.$nextTick(() => {
          if (data.status == 0) {
            //读卡
            if (data.device == "IdCard") {
              _that.sendForm.receiveCardCode = data.content.CardNum;
              _that.sendForm.receiveName = data.content.CardBelongName;
              _that.sendForm.pickerCardType = "身份证";
            }
            if (data.device == "PersonVerify") {
              if (data.content.verifyFlag && data.content.verifyFlag == 0) {
                _that.varify = 1;
                //将用户选择的图片进行保存处理
                //预览领证回执设置图片
                _that.verifyImg = "data:image/jpeg;base64," + data.content.verifyBase64;
                let file = _that.base64ToFile(data.content.verifyBase64, "verifyPicture.jpg");
                let formD = new FormData();
                formD.append("files", file);
                uploadCaseMaterialFile(formD).then(response => {
                  if (response.data != "") {
                    response.data.forEach(data => {
                      //取证人信息保存
                      _that.sendForm.photoOid = data.attaOid;//人证比对返回的附件主键
                      //_that.submitForm();
                      downFileByoid(data.attaOid).then(response => {
                        _that.verifyImg = response.data + "?attaOid=" + _that.form.photoOid;
                      });
                    });
                  }
                })
              } else {
                _that.varify = 2;
                if (data.content.verifyMessage) {
                  _that.$message.error(data.content.verifyMessage)
                }
              }
            }
          }
          if (data.status == 1) {
            _that.$message.error(data.message)
          }
        })
      },
      //预览领证回执
      viewReceiveInfo() {
        let _that = this;
        if (_that.varify!=1) {
          _that.$message.error("请先进行人证比对！");
          return false;
        }
        //预览回执单信息
        /*getLicenseIssuedIndustryByOid(_that.sendForm.oid).then(response => {
          //回执单
          _that.caseReceiveInfo = response.data;
          _that.caseReceiveInfo.caseNumber = response.data.industryCase.caseNumber;
          //图片预加载
          downFileByoid(_that.caseReceiveInfo.photoOid).then(response => {
            _that.verifyImg = response.data + "?attaOid=" + _that.caseReceiveInfo.photoOid;
          });
          _that.issuedReceiveShow = true;
        });*/

        this.$refs["sendForm"].validate(valid => {
          if (valid) {
            _that.caseReceiveInfo=_that.sendForm;
            //图片预加载
            if(_that.sendForm.photoOid){
              downFileByoid(_that.sendForm.photoOid).then(response => {
                _that.verifyImg = response.data+"?attaOid="+_that.sendForm.photoOid;
              });
            }
            _that.issuedReceiveShow=true;
          }
        })
      },
      //发证扫描条形码
      outOfStock() {
        let _that = this;
        if(_that.sendPostWay =='2'){
          if (_that.varify!=1) {
            _that.$message.error("请先进行人证比对！");
            return false;
          }
        }

          if(_that.sendForm.allFlag=='1'){
            _that.$message.error("存在未签收证照暂时无法进行发证!");
            return false;
          }else{
            if(_that.sendPostWay===2){
              this.$refs["sendForm"].validate(valid => {
                if (valid) {
                  //进行发证保存操作
                  _that.qfShow=true;
                  _that.barCode="";
                }})

            }else{
              _that.submitForm();
            }
          }
      },
      //发证扫描出件
      outOfStockSave() {
        let _that = this;
        if (!_that.barCode) {
          _that.$message.error("请扫描档案袋上的条形码！");
        } else {

          //调用保存信息
          //_that.addDialogShow = false;
          _that.sendForm.barCode =_that.barCode;
          _that.sendForm.industryCaseOid =_that.form.industryCaseOid;
          _that.sendForm.currStep =_that.currStep;
          saveOrUpOutOfStock(_that.sendForm).then(response => {
            _that.barCode = _that.barCode;
            if (response.data !=null && response.data.industryCaseOid != undefined) {
              _that.qfShow = false;
              _that.printCaseInfo =response.data;
              _that.submitForm();
              setTimeout(() => {
                //询问是否打印材料出库单
                this.$confirm('是否打印签收单?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.$message(this.handlePrint());
                }).catch(() => {
                  //判断是否有扫描领证的,如果有刷新去掉列表中出证的
                  if (this.tableData && this.tableData.length > 0) {
                    this.tableData.forEach((item, i) => {
                      if (item.industryCaseOid == this.printCaseInfo.industryCaseOid) {
                        this.tableData.splice(i, 1);//删除数组
                        this.$forceUpdate();//强制刷新列表
                      }
                    })
                  }
                  _that.getList()
                });

              }, 10);
            } else {
              _that.$message.error(response.data);
              return;
            }
          });
        }
      },
      base64ToFile(urlData, fileName) {
        let arr = urlData.split(',');
        // let mime = arr[0].match(/:(.*?);/)[1];
        let bytes = atob(arr[0]); // 解码base64
        let n = bytes.length
        let ia = new Uint8Array(n);
        while (n--) {
          ia[n] = bytes.charCodeAt(n);
        }
        return new File([ia], fileName, {type: 'image/jpeg'});
      },
      getIdCardInfo(){
         if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
          this.getIdcardDatav1();
          }
          if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
            this.getIdcardDatav2();
          }
          if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
            this.getIdcardDatav3();
          }
          // getIdCard().then(response => {
          //     this.sendForm.pickerCardType = "身份证";
          //     this.sendForm.receiveName = response.data.CNName;
          //     this.sendForm.receiveCardCode = response.data.carID;
          //     this.sendForm.address = response.data.address;
          // });
      },
      async getIdcardDatav1() {
        openIdCard().then(response => {
          //打开设备
          if (response.state == "sucess") {
            this.getIdcardDataByv1(); //重新获取身份证信息
          } else {
            //查找设备
            findIdCard().then(resFind => {
              if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
                this.getIdcardDataByv1();
              } else {
                this.$message.error("无法找到设备");
                return false;
              }
            });
          }
        });
      },
      async getIdcardDataByv1() {
        getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查设备或连接是否正常!");
          return false;
        }
        if (res.state == "sucess") {
            this.sendForm.pickerCardType = "身份证";
            this.sendForm.receiveName = res.CNName.trim();
            this.sendForm.receiveCardCode = res.carID;
            this.sendForm.address = res.address;
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(response => {
            if ((response.state = "sucess")) {
              this.getIdcardDataByv1(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(res.tips);
          return false;
        }
      });
      },
      async getIdcardDatav2() {
          const resData = await GPYDrive.readCardInfo();
          if (!resData || resData.code !== 200) {
            this.$message.error("读取身份证信息失败");
            return false;
          }
          this.sendForm.pickerCardType = "身份证";
          this.sendForm.receiveName = resData.data.name.trim();
          this.sendForm.receiveCardCode = resData.data.number;
          this.sendForm.address = resData.data.address;
      },
      async getIdcardDatav3() {
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              return this.getIdcardDataByv3(); //获取身份证信息
            } else {
              return this.$message.warning("请确认设备连接是否正常");
            }
          });
      },
      async getIdcardDataByv3() {
      getdataIdcardv3().then(resData => {
        if (resData.state == "sucess" && resData.StateCode == 0) {
          let resInfo = JSON.parse(resData.data);
            this.sendForm.pickerCardType = "身份证";
            this.sendForm.receiveName = resInfo.name.trim();
            this.sendForm.receiveCardCode = resInfo.number;
            this.sendForm.address = resInfo.department;
          } else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              this.getIdcardDataByv3(); //重新获取身份证信息
            }
          });
          } else {
          this.$message.error(resData.tips);
          return false;
          }
        });
      },
      handleClose(rzbdResult){
        this.varify = this.$refs?.scanForm.rzbdResult || rzbdResult;
        this.$refs.scanForm.getImageRes();
        let res= this.$refs.scanForm.returnInfo();
        this.saveAndUploadFile(res);
        this.dialogVisible=false;
      },

      //调用摄像头获取图像信息
      getImageCamera(){
        let _that=this;
        this.dialogVisible=true;
      },
      //保存并上传图片信息
      saveAndUploadFile(info){
        let _that=this;
        if(info && info.faceImg){
          _that.varify = 1;
          //将用户选择的图片进行保存处理
          //预览领证回执设置图片
          _that.verifyImg = "data:image/jpeg;base64," + info.faceImg;
          let file = _that.base64ToFile(info.faceImg, "verifyPicture.jpg");
          let formD = new FormData();
          formD.append("files", file);
          uploadCaseMaterialFile(formD).then(response => {
            if (response.data != "") {
              response.data.forEach(data => {
                //取证人信息保存
                _that.sendForm.photoOid = data.attaOid;//人证比对返回的附件主键
                //_that.submitForm();
                downFileByoid(data.attaOid).then(response => {
                  _that.verifyImg = response.data + "?attaOid=" + _that.form.photoOid;
                });
              });
            }
          })
        }else{
          _that.varify = 2;
        }
      },
      /** 登录信息 */
      queryLoginInfo() {
        getloginUser().then(response => {
          this.loginUser = response.data;
        });
      }
    },
    watch: {
    },
    created() {
      this.getList();
      this.getSelectCertificateType(0);
      this.queryLoginInfo();
    },
  };
</script>
<style lang="scss">
  .treeselect{
    width: 200px;
  }
  .treeselect240{
    width: 240px;
  }
</style>

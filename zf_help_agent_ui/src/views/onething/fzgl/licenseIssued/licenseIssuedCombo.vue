/** * @Author: liangxm * @Date: 2020-11-24 * @Description: 证照发证 */
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-row>
        <el-form-item label="申请人名称" prop="applyUserName">
          <el-input
            v-model.trim="queryParams.applyUserName"
            placeholder="请输入申请人名称"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="办件编号" prop="comboCaseNumber">
          <div class="card-item">
            <el-input
              v-model.trim="queryParams.comboCaseNumber"
              placeholder="请输入办件编号"
              clearable
              size="100"
              @keyup.enter.native="handleQuery"
            >
              <!-- <template slot="append"
                ><el-button
                  class="scan-btn"
                  type="primary"
                  @click="openScan(0)"
                ></el-button
              ></template> -->
            </el-input>
          </div>
        </el-form-item>
        <el-form-item label="发证状态" label-width="108px">
          <el-radio-group v-model="queryParams.hasissued">
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
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleBatch"
          v-hasPermi="['sys:licenseIssued:init']"
          >扫描领证</el-button
        >
      </el-col>
    </el-row>
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
        prop="comboCase.projectName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办件编号"
        align="center"
        prop="comboCaseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人身份证"
        align="center"
        prop="cardNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="签收日期"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="签收方式"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.sendWay == 1">快递送达</p>
          <p v-if="scope.row.sendWay == 2">自行取件</p>
          <p v-if="scope.row.sendWay == 3">其他</p>
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
            v-if="scope.row.hasissued == null"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:licenseIssued:view']"
            >发证</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            v-if="scope.row.hasissued == 1"
            @click="viewLicenseRecord(scope.row)"
            v-hasPermi="['sys:licenseIssued:view']"
            >记录查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="viewLicense(scope.row)"
            v-hasPermi="['sys:licenseIssued:view']"
            >证照预览</el-button
          >
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

    <!-- 发证 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="1100px"
      height="700px"
      append-to-body
      v-dialog-drag
    >
      <el-form
        ref="sendForm"
        :model="sendForm"
        :rules="rules.sendForm"
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
            <td><b>申请人：</b></td>
            <td>
              {{ form.comboCase.applyUserName }}
            </td>
            <td><b>证件号码：</b></td>
            <td>
              {{ form.comboCase.credentialNumber }}
            </td>
          </tr>
          <tr>
            <td><b>申请人手机：</b></td>
            <td>
              {{ form.comboCase.applyUserPhone }}
            </td>
            <td><b>申请人住址：</b></td>
            <td>
              {{ form.comboCase.applyUserAddress }}
            </td>
          </tr>
          <tr>
            <td><b>证照结果：</b></td>
            <td colspan="3">
              {{ sendForm.licenseResult }}
            </td>
          </tr>
          <tr>
            <td><b>送达方式：</b></td>
            <td colspan="3">
              <el-radio-group v-model="sendPostWay">
                <el-radio :label="1">快递送达</el-radio>
                <el-radio :label="2">窗口领取</el-radio>
                <el-radio :label="3">其他</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>收件人证件类型：</b></td>
            <td colspan="3">
              <el-form-item prop="pickerCardType">
                <el-col :span="24">
                  <el-select
                    v-model="sendForm.pickerCardType"
                    placeholder="请选择证件类型"
                  >
                    <el-option
                      v-for="certificateType in certificateTypeList"
                      :key="certificateType.dictOid"
                      :label="certificateType.name"
                      :value="certificateType.dictOid"
                    >
                    </el-option>
                  </el-select>
                  <span v-if="varify == 1" style="color: green"
                    >人证比对通过</span
                  >
                  <span v-if="varify == 2" style="color: red"
                    >人证比对不通过</span
                  >
                  <el-button
                    type="primary"
                    @click="scanCard(0)"
                    style="float: right"
                    >✚ 扫描身份证</el-button
                  >
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>收件人证件号码：</b></td>
            <td colspan="3">
              <el-form-item prop="receiveCardCode">
                <el-col :span="24">
                  <el-input
                    v-model.trim="sendForm.receiveCardCode"
                    placeholder="请输入取件人证件号码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>收件人姓名：</b></td>
            <td>
              <el-form-item prop="receiveName">
                <el-col :span="24">
                  <el-input
                    v-model.trim="sendForm.receiveName"
                    placeholder="请输入取件人姓名"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>收件人手机号：</b></td>
            <td>
              <el-form-item prop="receivePhone">
                <el-col :span="24">
                  <el-input
                    v-model.trim="sendForm.receivePhone"
                    placeholder="请输入取件人手机号"
                    maxlength="11"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="sendPostWay == 1">
            <td><i class="require">*</i><b>收件人所在地址：</b></td>
            <td colspan="3">
              <el-form-item prop="address">
                <el-col :span="24">
                  <el-input
                    v-model.trim="sendForm.address"
                    placeholder="请输入收件人所在地址"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="sendPostWay == 1">
            <td><i class="require">*</i><b>快递编号：</b></td>
            <td colspan="3">
              <el-form-item prop="kdCode">
                <el-col :span="24">
                  <el-input
                    v-model.trim="sendForm.kdCode"
                    placeholder="请输入快递编号"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button
          type="primary"
          @click="getImageCamera"
          v-if="sendPostWay == '2'"
          >人证比对</el-button
        >
        <!-- 调用高拍仪人证比对 -->
        <!--        <el-button type="primary" @click="getReceivePhoto" v-if="sendPostWay =='2'">人证比对</el-button>-->
        <!--  <el-button type="primary" @click="signConfirm" v-if="sendPostWay =='2'">签名</el-button>-->
        <el-button
          type="primary"
          @click="viewReceiveInfo"
          v-if="sendPostWay == '2'"
          >预览领证回执</el-button
        >
        <el-button type="primary" @click="outOfStock">发证</el-button>
        <el-button
          @click="
            () => {
              addDialogShow = false;
              reset();
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>
    <!-- 发证信息页面 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="qfShow"
      width="800px"
      append-to-body
    >
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
              <div class="card-item">
                <el-input
                  v-model.trim="barCode"
                  placeholder="请扫描条形码"
                  maxlength="50"
                  show-word-limit
                >
                  <!--                  <template slot="append"
                    ><el-button
                      class="scan-btn"
                      type="primary"
                      @click="openScan(1)"
                    ></el-button
                  ></template>-->
                </el-input>
              </div>
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

      <div slot="footer" align="center">
        <el-button type="primary" @click="outOfStockSave">确定</el-button>
        <el-button
          @click="
            () => {
              qfShow = false;
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 预览领证回执单信息详细 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="issuedReceiveShow"
      :visible.sync="issuedReceiveShow"
      width="1000px"
      height="400px"
      v-dialog-drag
      append-to-body
    >
      <el-form
        ref="form"
        :model="caseReceiveInfo"
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
            <td><b>领证人姓名：</b></td>
            <td colspan="2">{{ caseReceiveInfo.receiveName }}</td>
            <td rowspan="5">
              <img :src="verifyImg" class="user-avatar" />
            </td>
          </tr>
          <tr>
            <td><b>领证人证件号码：</b></td>
            <td colspan="2">
              {{ caseReceiveInfo.receiveCardCode }}
            </td>
          </tr>
          <tr>
            <td><b>联系电话：</b></td>
            <td colspan="2">
              {{ caseReceiveInfo.receivePhone }}
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
            </tr>-->
          <!--            <tr>
              <td><b>影像采集确认信息：</b></td>
              <td colspan="3">
                {{caseReceiveInfo.addresseeAddress}}
              </td>
            </tr>-->
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="
            () => {
              issuedReceiveShow = false;
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 扫码领证信息详细 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="batchView"
      :visible.sync="batchView"
      width="1100px"
      height="700px"
      append-to-body
      v-dialog-drag
    >
      <el-row>
        <!--        <el-input
          v-model.trim="batch_caseNumber"
          placeholder="请扫描二维码"
          align="center"
          clearable
          size="100"
          @keyup.enter.native="handleBatchQuery"
        />-->
        <div class="card-item">
          <el-input
            v-model.trim="batch_caseNumber"
            placeholder="请输入办件编号"
            align="center"
            clearable
            size="100"
            @keyup.enter.native="handleBatchQuery"
          >
            <!--             <template slot="append">
              <el-button class="scan-btn" type="primary" @click="openScan(2)">
              </el-button>
            </template> -->
          </el-input>
        </div>
      </el-row>
      <div class="zf-zc-table--title">办件列表</div>
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <!--        <el-table-column label="证照名称"  align="center" prop="licenseName" :show-overflow-tooltip="true"/>-->
        <el-table-column
          label="办件编号"
          align="center"
          prop="comboCaseNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="申请人"
          align="center"
          prop="applyUserName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="申请人身份证"
          align="center"
          prop="idCard"
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
              icon="iconfont zfsoft-xiugai"
              @click="handleInit(scope.row)"
              v-hasPermi="['sys:licenseIssued:out']"
              >领证</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="zf-text-center">
        <el-button
          @click="
            () => {
              batchView = false;
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 发证信息详细 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="viewDialogTitle"
      v-if="viewDialogShow"
      :visible.sync="viewDialogShow"
      width="800px"
      append-to-body
    >
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
            {{ viewform.comboCase.applyUserName }}
          </td>
          <td><b>证件号码：</b></td>
          <td>
            {{ viewform.comboCase.credentialNumber }}
          </td>
        </tr>
        <tr>
          <td><b>申请人手机：</b></td>
          <td>
            {{ viewform.comboCase.applyUserPhone }}
          </td>
          <td><b>申请人住址：</b></td>
          <td>
            {{ viewform.comboCase.applyUserAddress }}
          </td>
        </tr>
        <tr>
          <td><b>收件人证件号码：</b></td>
          <td colspan="3">
            {{ viewform.receiveCardCode }}
          </td>
        </tr>
        <tr>
          <td><b>收件人姓名：</b></td>
          <td>
            {{ viewform.receiveName }}
          </td>
          <td><b>收件人手机号：</b></td>
          <td>
            {{ viewform.receivePhone }}
          </td>
        </tr>
        <tr v-if="viewform.receiveType == 1">
          <td><b>收件人所在地址：</b></td>
          <td colspan="3">
            {{ viewform.address }}
          </td>
        </tr>
        <tr v-if="viewform.receiveType == 1">
          <td><i class="require">*</i><b>快递编号：</b></td>
          <td colspan="3">
            {{ viewform.kdCode }}
          </td>
        </tr>
      </table>

      <div slot="footer" align="center">
        <el-button
          @click="
            () => {
              viewDialogShow = false;
              reset();
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!--利用vue插件进行打印办件证照签收单-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="printShow"
      :visible.sync="printShow"
      width="800px"
      append-to-body
    >
      <div id="print">
        <div :model="printCaseInfo">
          <h3 align="center"><i class="el-icon-document"></i>签收单</h3>
          <table
            v-loading="loading"
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table"
          >
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td style="text-align: center"><b>办件编号</b></td>
              <td>
                {{ printCaseInfo.comboCase.caseNumber }}
              </td>
            </tr>

            <tr>
              <td style="text-align: center"><b>登记日期</b></td>
              <td>{{ printCaseInfo.comboCase.createDate }}</td>
            </tr>
            <tr>
              <td style="text-align: center"><b>注意</b></td>
              <td>
                办件编号为{{ printCaseInfo.comboCase.caseNumber }}的证照，在{{
                  printCaseInfo.modifyDate
                }}通过
                <span v-if="sendPostWay == 1">快递送达 </span>
                <span v-if="sendPostWay == 2">自行取件 </span>
                <span v-if="sendPostWay == 3">其他 </span>
                已发证。
              </td>
            </tr>
          </table>
        </div>
      </div>
      <div slot="footer" align="center">
        <el-button type="primary" v-print="printObj">打印</el-button>
        <el-button @click="closePrint">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 人证比对 -->
    <div v-if="dialogVisible">
      <el-dialog
        v-dialog-drag
        :visible.sync="dialogVisible"
        width="1158px"
        top="10vh"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :before-close="handleClose"
        append-to-body
        title="人证比对"
      >
        <hardware-scan
          ref="scanForm"
          :isShowSmall="true"
          :caseOid="comboCaseOid"
          :loginUser="loginUser"
          @close="handleClose"
        ></hardware-scan>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  page,
  saveOrUpdateKd,
  saveOrUpdateIssued,
  getCertificateType,
  saveOrUpOutOfStock,
  queryComboCaseSignByOid,
  getComboCaseIssuedBySignOid,
  getComboLicenseListByCaseNumber,
  getComboCaseIssuedByCaseOid
} from "@/api/onething/fzgl/licenseIssuedCombo";
import { getElecLicenUrl } from "@/api/zc/businessManagement/elemLice";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import { validatePhone } from "@/utils/validate";
import { getComboCaseByOid } from "@/api/onething/comboManager/comboAccept/caseBqbz";
import { downFileByoid } from "@/api/zc/businessManagement/fileUpload";
import { uploadCaseMaterialFile } from "@/api/zc/businessManagement/caseMaterialAtta";
import hardwareScan from "@/views/common/hardwareScan";
import {
  activeScanningGun,
  openScanningGun
} from "@/api/zc/businessManagement/charge";
import { findIdCard, openIdCard, getIdCardInfo } from "@/api/sys/hardwareScan";
import { getloginUser } from "@/api/zc/businessManagement/windowAcceptance";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
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
  name: "LicenseIssuedCombo",
  components: { hardwareScan, viewCaseInfo },
  data() {
    return {
      printObj: {
        id: "print",
        popTitle: "签收单",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      // 列表数据
      scanCaseList: [],
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      // 弹窗标题
      dialogTitle: "",
      addDialogShow: false,
      issuedKdShow: false,
      openView: false,
      batchView: false,
      printShow: false,
      issuedReceiveShow: false,
      viewDialogShow: false,
      qfShow: false,
      case_Number: "",
      barCode: "", //条形码
      recordInfo: {}, //证照管理信息
      caseReceiveInfo: {}, //证照领证信息
      batch_caseNumber: "",
      allSmCaseNum: [], //所有已经扫描的办件号
      viewDialogTitle: "",
      currStep: "",
      clerkAble: "N", //是否启用签名
      varify: "",
      title: "",
      sendPostWay: 1,
      //发证信息
      applyInfo: {},
      certificateTypeList: [], //证件类型
      printCaseInfo: {}, //用于打印证照领证信息
      verifyImg: "",
      viewform: {},
      form: { comboCase: {}, oid: "" },
      sendForm: { currStep: "" },
      comboCaseOid: "",
      signOid: "",
      rules: {
        sendForm: {
          pickerCardType: [
            { required: true, message: "请选择证照类型", trigger: "blur" }
          ],
          receiveCardCode: [
            {
              required: true,
              message: "取件人证件号码不能为空",
              trigger: "blur"
            }
          ],
          receiveName: [
            { required: true, message: "取件人姓名不能为空", trigger: "blur" }
          ],
          receivePhone: [
            {
              required: true,
              message: "取件人手机号不能为空",
              trigger: "blur"
            },
            {
              validator: validatePhone,
              message: "请输入正确的取件人手机号",
              trigger: "blur"
            }
          ],
          address: [
            {
              required: true,
              message: "收件人所在地址不能为空",
              trigger: "blur"
            }
          ],
          kdCode: [
            { required: true, message: "快递编号不能为空", trigger: "blur" }
          ]
        }
      },
      total: 0,
      // 签发状态
      statusOptions: { 2: "未发证", 1: "已发证" },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        applyUserName: "",
        hasissued: "2"
      },
      dialogVisible: false,
      loginUser: {}
    };
  },
  methods: {
    initQuery() {
      this.queryParams.caseNumber = this.$route.query.caseNumber;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.caseRegList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.licenseInStorage = "0"; //重置radio无效
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },

    /** 查看按钮操作 */
    handleView(row) {
      getElecLicenUrl(row.elecLicenOid).then(response => {
        let urlElem = [];
        if (response.data) {
          urlElem = response.data;
          if (urlElem[0].viewOfdUrl) {
            window.open(urlElem[0].viewOfdUrl, "width=1200px;height=800px;");
          }
        }
      });
    },
    /** 发证操作 */
    handleInit(row) {
      let _that = this;
      _that.sendForm = {};
      _that.varify = "";
      _that.comboCaseOid = row.comboCaseOid;
      _that.signOid = row.oid;
      _that.case_Number = row.comboCaseNumber;
      _that.sendPostWay = parseInt(row.comboCase.resultDeliveryWay);
      if (row.oid) {
        //查询办件送达信息
        queryComboCaseSignByOid(row.oid).then(response => {
          if (response.data != null) {
            _that.form = response.data;
            /*if (_that.form.sendWay == "1") {
              _that.sendPostWay = 1;
            } else {
              _that.sendPostWay = 2;
            }*/
          }
        });
        //查询发证记录信息
        getComboCaseIssuedByCaseOid(row.comboCaseOid).then(response => {
          if (response.data != null) {
            _that.currStep = response.data.currStep; //防止修改重新进来必须人证比对
            _that.sendForm = response.data;
            _that.printCaseInfo = response.data;
          }
        });
      }
      _that.addDialogShow = true;
      _that.dialogTitle = "领证信息";
    },
    /** 获取证件类型 */
    getSelectCertificateType(cegisterType) {
      let _that = this;
      cegisterType = 0; //只查询个人类型
      getCertificateType(cegisterType).then(response => {
        _that.certificateTypeList = response.data;
      });
    },
    /** 提交按钮 */
    /* submitKdForm: function () {
       let _that = this;
       if (!_that.kdform.kdCode) {
         _that.$message.error("快递单号不能为空！");
       } else {
         saveOrUpdateKd(_that.kdform).then(response => {
           if (response.code === 200) {
             _that.msgSuccess("保存成功");
             _that.issuedKdShow = false;
             setTimeout(() => {
               //询问是否打印材料出库单
               this.$confirm("是否打印签收单?", "提示", {
                 confirmButtonText: "确定",
                 cancelButtonText: "取消",
                 type: "warning"
               })
                 .then(() => {
                   _that.handlePrint();
                 })
                 .catch(() => {
                   //判断是否有扫描领证的
                   if (this.tableData && this.tableData.length > 0) {
                     this.tableData.forEach((item, i) => {
                       if (
                         item.comboCaseOid == this.printCaseInfo.comboCaseOid
                       ) {
                         this.tableData.splice(i, 1); //删除数组
                         this.$forceUpdate(); //强制刷新列表
                       }
                     });
                   }
                   _that.getList();
                 });
             }, 10);
           }
         });
       }
     },*/
    /** 拍照保存记录提交按钮 */
    async submitForm() {
      let _that = this;
      /* if(_that.sendForm.allFlag=='1'){
          _that.$message.error("存在未签收证照暂时无法进行发证!");
          return false;
        }*/
      const valid = await this.$refs["sendForm"].validate();
      if (valid) {
        _that.sendForm.signOid = _that.form.oid;
        _that.sendForm.receiveType = _that.sendPostWay;
        _that.sendForm.comboCaseOid = _that.form.comboCaseOid;
        return saveOrUpdateIssued(this.sendForm).then(response => {
          if (response.code === 200) {
            _that.currStep = response.data.currStep;
            _that.sendForm = response.data;
            _that.msgSuccess("保存成功");
            //_that.addDialogShow = false;
          }
        });
        /*if (_that.sendPostWay === 2) {
          //进行发证保存操作
          _that.addDialogShow = false;
        } else {
          //直接发证
          _that.qfShow = false;
          this.outOfStockSaveDo();
        }*/
      }
    },
    /** 批量出库操作 */
    handleBatch() {
      this.batchView = true;
      this.dialogTitle = "领证";
      this.batch_caseNumber = "";
      this.tableData = [];
      this.allSmCaseNum = [];
    },
    //批量扫描enter查询
    handleBatchQuery() {
      if (this.batch_caseNumber) {
        if (this.allSmCaseNum) {
          if (this.allSmCaseNum.indexOf(this.batch_caseNumber) != -1) {
            this.$message.error("办件已存在!");
            return false;
          }
        }
        getComboLicenseListByCaseNumber(this.batch_caseNumber).then(
          response => {
            if (response.data != null) {
              if (response.data == 1) {
                this.batch_caseNumber = "";
                this.$message.error("办件已发证!");
                return false;
              }
              let arr = response.data[0]; //目前只取第一个
              this.addRow(arr);
              /*arr.forEach((itm,i)=>{
                this.addRow(itm);
              })*/
              this.allSmCaseNum.push(this.batch_caseNumber);
            } else {
              this.$message.error("未查询到相关数据!");
            }
            this.batch_caseNumber = "";
          }
        );
      } else {
        this.$message.error("办件编号不能为空!");
      }
    },
    handleQueryCase() {
      let caseOid = this.comboCaseOid + ";";
      this.loading = true;
      this.openView = false;
      getComboCaseByOid(caseOid, this.queryParams.comboCaseNumber).then(
        response => {
          this.form.info = response.data;
          this.form.qlCases = response.data.licenseManageCombos;
          this.loading = false;
          this.openView = true;
        }
      );
    },
    // 增加行
    addRow(caseInfo) {
      this.tableData.unshift(caseInfo); //追加数据
      this.rowNum += 1;
    },
    handlePrint() {
      this.printShow = true;
    },
    closePrint() {
      this.printShow = false;
      setTimeout(() => {
        this.addDialogShow = false;
        this.getList(); //返回列表页面
        //判断是否有扫描领证的
        if (this.tableData && this.tableData.length > 0) {
          this.tableData.forEach((item, i) => {
            if (item.comboCaseOid == this.printCaseInfo.comboCaseOid) {
              this.tableData.splice(i, 1); //删除数组
              this.$forceUpdate(); //强制刷新列表
            }
          });
        }
      }, 10);
    },
    //签名
    signConfirm() {
      let _that = this;
      if (_that.currStep == "" || _that.varify == 2) {
        _that.$message.error("请先进行人证比对！");
      } else {
        //签字对接，需要保存
        _that.sendForm.displaySign = 1; //签名
        //保存签字信息主键
        _that.sendForm.signInfo = "112323435"; //签名返回的附件主键
        _that.sendForm.displaySign = 1;
        _that.sendForm.comboCaseOid = _that.comboCaseOid;
        _that.sendForm.signOid = _that.signOid;
        _that.sendForm.receiveType = _that.sendPostWay;
        saveOrUpdateIssued(_that.sendForm).then(response => {
          if (response.code === 200) {
            _that.sendForm = response.data;
            _that.msgSuccess("签名成功");
          }
        });
      }
    },
    //预览领证回执
    viewReceiveInfo() {
      let _that = this;
      if (_that.clerkAble == "Y") {
        //判断是否启用签名
        if (
          _that.sendForm.displaySign == null ||
          _that.sendForm.displaySign == ""
        ) {
          _that.$message.error("请先进行签名和人证比对！");
          return;
        }
      } else {
        if (_that.currStep == "" || _that.varify == 2) {
          _that.$message.error("请先进行人证比对！");
          return false;
        }
      }
      //预览回执单信息
      /*queryComboCaseSignByOid(_that.sendForm.oid).then(response => {
          //回执单
          _that.caseReceiveInfo=response.data;
          _that.caseReceiveInfo.caseNumber=response.data.comboCase.caseNumber;
          //图片预加载
          downFileByoid(_that.caseReceiveInfo.photoOid).then(response => {
            _that.verifyImg = response.data+"?attaOid="+_that.caseReceiveInfo.photoOid;
          });
          _that.issuedReceiveShow=true;
        });*/
      this.$refs["sendForm"].validate(valid => {
        if (valid) {
          _that.caseReceiveInfo = _that.sendForm;
          //图片预加载
          if (_that.sendForm.photoOid) {
            downFileByoid(_that.sendForm.photoOid).then(response => {
              _that.verifyImg =
                response.data + "?attaOid=" + _that.sendForm.photoOid;
            });
          }
          _that.issuedReceiveShow = true;
        }
      });
    },
    //发证扫描条形码
    async outOfStock() {
      let _that = this;
      if (_that.sendPostWay == "2") {
        if (_that.clerkAble == "Y") {
          if (
            _that.sendForm.displaySign == null ||
            _that.sendForm.displaySign == ""
          ) {
            _that.$message.error("请先进行签名!");
            return;
          }
        } else {
          if (!_that.varify) {
            _that.$message.error("请先进行人证比对！");
            return false;
          }
          if (_that.varify == 2) {
            _that.$message.error("人证比对不通过，请重新比对！");
            return false;
          }
        }
      }
      if (_that.sendForm.allFlag == "1") {
        _that.$message.error("存在未签收证照暂时无法进行发证!");
        return false;
      } else {
        if (_that.sendPostWay === 2) {
          //人工送达
          this.$refs["sendForm"].validate(valid => {
            if (valid) {
              //进行发证保存操作
              _that.qfShow = true;
              _that.barCode = "";
            }
          });
        } else {
          //快递送达
          //质控需求快递不需要扫码，直接进行发证
          _that.barCode = _that.case_Number;
          await _that.submitForm();
          _that.outOfStockSave();
          //_that.qfShow = true;
          //_that.barCode = "";
        }
      }
    },
    //发证扫描出件
    outOfStockSave() {
      let _that = this;
      if (_that.sendPostWay === 2 && !_that.barCode) {
        _that.$message.error("请扫描档案袋上的条形码！");
      } else {
        //调用保存信息
        //_that.addDialogShow = false;
        _that.sendForm.barCode = _that.barCode;
        _that.sendForm.comboCaseOid = _that.comboCaseOid;
        _that.sendForm.currStep = _that.currStep;
        saveOrUpOutOfStock(_that.sendForm).then(response => {
          _that.barCode = _that.barCode;
          if (
            response.data != null &&
            response.data.comboCaseOid != undefined
          ) {
            if (_that.sendPostWay == 2) {
              _that.qfShow = false;
            }
            _that.printCaseInfo = response.data;
            //_that.submitForm();
            setTimeout(() => {
              //询问是否打印材料出库单
              this.$confirm("是否打印签收单?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(() => {
                  //this.$message();
                  _that.handlePrint();
                })
                .catch(() => {
                  //判断是否有扫描领证的,如果有刷新去掉列表中出证的
                  if (this.tableData && this.tableData.length > 0) {
                    this.tableData.forEach((item, i) => {
                      if (
                        item.comboCaseOid == this.printCaseInfo.comboCaseOid
                      ) {
                        this.tableData.splice(i, 1); //删除数组
                        this.$forceUpdate(); //强制刷新列表
                      }
                    });
                  }
                  _that.getList();
                  _that.addDialogShow = false;
                });
            }, 10);
          } else {
            this.$message.error("发证失败，请输入正确的办件编号");
            return;
          }
        });
      }
    },
    //不进行扫码直接发证操作
    outOfStockSaveDo() {
      let _that = this;
      //调用保存信息
      _that.addDialogShow = false;
      _that.sendForm.comboCaseOid = _that.comboCaseOid;
      //扫码编号设置为办件编号
      _that.sendForm.barCode = _that.sendForm.comboCase.caseNumber;
      saveOrUpOutOfStock(_that.sendForm).then(response => {
        if (response.data != null && response.data.comboCaseOid != undefined) {
          _that.printCaseInfo = response.data;
          //询问是否打印材料出库单
          _that
            .$confirm("是否打印签收单?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
            .then(() => {
              this.loading = true;
              _that.handlePrint();
              // this.$message();
              _that.getList();
              this.loading = false;
            })
            .catch(() => {
              //判断是否有扫描领证的,如果有刷新去掉列表中出证的
              if (_that.tableData && _that.tableData.length > 0) {
                _that.tableData.forEach((item, i) => {
                  if (item.comboCaseOid == this.printCaseInfo.comboCaseOid) {
                    _that.tableData.splice(i, 1); //删除数组
                    _that.$forceUpdate(); //强制刷新列表
                  }
                });
              }
            });
        } else {
          //   _that.$message.error(response.data);
          return;
        }
      });
    },
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(",");
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: "image/jpeg" });
    },
    //人证比对信息
    /* getReceivePhoto () {
       let _that = this;
       //此处获取人证比对信息
       const info = '{"device":"PersonVerify"}';
       //建立socket连接
       this.socketApi.initWebSocket(this.socketError);
       this.socketApi.sendSock(info, this.getConfigResult);
     },*/
    scanCard(data) {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav1(data);
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardDatav2(data);
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
        this.getIdcardDatav3(data);
      }
      //Device：设备类型、
      //let info = '{"device":"IdCard"}';
      //建立socket连接
      //this.socketApi.initWebSocket(this.socketError);
      //this.socketApi.sendSock(info, this.getConfigResult);
    },
    async getIdcardDatav1() {
      openIdCard().then(response => {
        //打开设备
        if (response.state == "sucess") {
          this.getIdcardDataByv1(data); //重新获取身份证信息
        } else {
          //查找设备
          findIdCard().then(resFind => {
            if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
              this.getIdcardDataByv1(data);
            } else {
              this.$message.error("无法找到设备");
              return false;
            }
          });
        }
      });
    },
    async getIdcardDataByv1(data) {
      getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查设备或连接是否正常!");
          return false;
        }
        if (res.state == "sucess") {
          if (data == 0) {
            this.handleIdCardInfo(res);
          } else if (data == 1) {
            this.queryParams.applyUserName = res.CNName.trim();
          }
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(response => {
            if ((response.state = "sucess")) {
              this.getIdcardDataByv1(data); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(res.tips);
          return false;
        }
      });
    },
    async getIdcardDatav3(data) {
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(data); //获取身份证信息
        } else {
          return this.$message.warning("请确认设备连接是否正常");
        }
      });
    },
    async getIdcardDataByv3(data) {
      getdataIdcardv3().then(resData => {
        if (resData.state == "sucess" && resData.StateCode == 0) {
          let resInfo = JSON.parse(resData.data);
          let res = {
            CNName: resInfo.name,
            sex: resInfo.sex,
            carID: resInfo.number,
            nation: resInfo.nation,
            address: resInfo.department,
            bron: resInfo.birthday,
            imgData: "data:image/jpeg;base64," + resInfo.Image
          };
          if (data == 0) {
            this.handleIdCardInfo(res);
          } else if (data == 1) {
            this.queryParams.applyUserName = res.CNName.trim();
          }
        } else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              this.getIdcardDataByv3(data); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(resData.tips);
          return false;
        }
      });
    },
    async getIdcardDatav2(data) {
      this.form.receiveName = "";
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("读取身份证信息失败");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        sex: resData.data.sex,
        carID: resData.data.number,
        nation: resData.data.nation,
        address: resData.data.address,
        bron: resData.data.birthday,
        imgData: "data:image/jpeg;base64," + resData.data.image
      };
      if (data == 0) {
        this.handleIdCardInfo(res);
      } else if (data == 1) {
        this.queryParams.applyUserName = res.CNName.trim();
      }
    },
    handleIdCardInfo(res) {
      this.$set(this.sendForm, "receiveName", res.CNName.trim());
      this.$set(this.sendForm, "receiveCardCode", res.carID);
      const dictOid = this.certificateTypeList?.find?.(
        item => item.code === "SFZ"
      )?.dictOid;
      this.$set(this.sendForm, "pickerCardType", dictOid);

      localStorage.setItem("idCardInfo", JSON.stringify(res));
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常");
    },
    // 接收socket回调函数返回数据的方法
    /*getConfigResult (data) {
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
              _that.verifyImg =
                "data:image/jpeg;base64," + data.content.verifyBase64;
              let file = _that.base64ToFile(
                data.content.verifyBase64,
                "verifyPicture.jpg"
              );
              let formD = new FormData();
              formD.append("files", file);
              uploadCaseMaterialFile(formD).then(response => {
                if (response.data != "") {
                  response.data.forEach(data => {
                    //取证人信息保存
                    _that.sendForm.photoOid = data.attaOid; //人证比对返回的附件主键
                    downFileByoid(data.attaOid).then(response => {
                      _that.verifyImg =
                        response.data + "?attaOid=" + _that.sendForm.photoOid;
                    });
                  });
                }
              });
            } else {
              _that.varify = 2;
              if (data.content.verifyMessage) {
                _that.$message.error(data.content.verifyMessage);
              }
            }
          }
        }
        if (data.status == 1) {
          _that.$message.error(data.message);
        }
      });
    },*/
    viewLicenseRecord(row) {
      getComboCaseIssuedBySignOid(row.oid).then(response => {
        if (response.data != null) {
          this.viewform = response.data;
        }
      });
      this.viewDialogShow = true;
      this.viewDialogTitle = "领证记录";
    },
    viewLicense(row) {
      this.$message.error("暂不支持证照预览!");
    },
    handleClose(rzbdResult) {
      this.varify = this.$refs?.scanForm.rzbdResult || rzbdResult;
      this.$refs.scanForm.getImageRes();
      let res = this.$refs.scanForm.returnInfo();
      this.saveAndUploadFile(res);
      this.dialogVisible = false;
    },

    //调用摄像头获取图像信息
    getImageCamera() {
      this.$refs["sendForm"].validate(valid => {
        if (valid) {
          this.dialogVisible = true;
        }
      });
    },
    //保存并上传图片信息
    saveAndUploadFile(info) {
      let _that = this;
      if (info && info.faceImg) {
        _that.currStep = 1;
        //_that.varify = 1;
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
              _that.sendForm.photoOid = data.attaOid; //人证比对返回的附件主键
              this.submitForm();
              downFileByoid(data.attaOid).then(response => {
                _that.verifyImg =
                  response.data + "?attaOid=" + _that.sendForm.photoOid;
              });
            });
          }
        });
      }
    },
    /** 登录信息 */
    queryLoginInfo() {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    }
  },
  destroyed: function() {
    //在离开此页面的时候主动关闭socket
    // this.socketApi.webSocketClose();
  },
  watch: {},
  created() {
    this.initQuery();
    this.getList();
    this.getSelectCertificateType(0);
    this.queryLoginInfo();
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
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
.scan-picture {
  padding: 0 10px 10px;

  .title {
    font-size: 18px;
    color: #333;
    margin-bottom: 20px;
    display: block;
  }

  .scan-area {
    background-color: #000;
    height: 700px;
    position: relative;
    .scan-pic {
      width: 100%;
      height: 586px;
    }
    .scan-pic {
      width: 100%;
      height: 586px;
    }
    .scan-btn {
      background-color: #030719;
      text-align: center;
      position: absolute;
      width: 100%;
      height: 114px;
      bottom: 0;
      line-height: 13;

      img {
        width: 80px;
        height: 80px;
        cursor: pointer;
      }
    }

    .hint {
      width: 253px;
      height: 49px;
      color: #fff;
      font-size: 17px;
      background: url(../../../../assets/image/scan-bg.png) no-repeat;
      background-size: 100% 100%;
      line-height: 55px;
      text-align: center;
      position: absolute;
      bottom: -22px;
      left: 50%;
      margin-left: -130px;
      display: none;
    }

    .hint.active {
      display: block;
    }
  }

  .scan-result {
    position: relative;
    height: 700px;
    padding-top: 28px;
    background-color: #eff1f5;
    .contrast {
      width: 408px;
      margin: 0 auto;
      height: 220px;
      > div {
        width: 164px;
        height: 184px;
        text-align: center;
        font-size: 18px;
        color: #333;
        float: left;
        img {
          width: 100%;
          height: 100%;
        }
        img:hover {
          border: 1px solid #1890ff;
        }
        p {
          margin: 5px 0;
        }
      }
      > img {
        float: left;
        margin: 64px 25px;
      }
    }
    .result-show {
      border-collapse: collapse;
      width: 100%;
      margin-top: 48px;
      font-size: 18px;
      thead tr {
        th {
          background-color: #6685a3;
          color: #fff;
          padding: 15px;
        }
      }
      tbody {
        tr {
          td {
            color: #48627b;
            padding: 15px 5px 15px 45px;
            text-align: left;
          }
          td:nth-child(2n) {
            padding: 15px;
            color: #333;
          }
        }
        tr:nth-child(2n) {
          background-color: #e0e3ea;
        }
        tr:nth-child(2n + 1) {
          background-color: #eff1f5;
        }
        tr:last-child {
          height: 50px;
        }
      }
    }
    .verification {
      position: absolute;
      bottom: 136px;
      left: 50%;
      z-index: 1;
      width: 123px;
      height: 97px;
      margin-left: -60px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .scan-btn {
      background-color: #e4e6ec;
      text-align: center;
      position: absolute;
      width: 100%;
      height: 114px;
      bottom: 0;
      line-height: 8;
      button {
        width: 101px;
      }
      .vf-hint {
        width: 253px;
        height: 49px;
        color: #fff;
        font-size: 17px;
        background: url(../../../../assets/image/scan-bg.png) no-repeat;
        background-size: 100% 100%;
        line-height: 55px;
        text-align: center;
        position: absolute;
        bottom: -20px;
        left: 40%;
        display: none;
      }
      .vf-hint.active {
        display: block;
      }
    }
  }
}
</style>
